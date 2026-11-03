package com.example.engine

import android.content.Context
import android.content.Intent
import android.net.Uri
import com.example.data.dao.BubbleDao
import com.example.data.model.BubbleKeyword
import com.example.data.model.NoiseLog
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import java.net.URLEncoder
import java.nio.charset.StandardCharsets
import java.util.concurrent.TimeUnit
import kotlin.random.Random

class NoiseEngine(private val bubbleDao: BubbleDao) {

    private val httpClient = OkHttpClient.Builder()
        .connectTimeout(12, TimeUnit.SECONDS)
        .readTimeout(12, TimeUnit.SECONDS)
        .followRedirects(true)
        .build()

    private val userAgents = listOf(
        "Mozilla/5.0 (Linux; Android 14; Pixel 8 Pro) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/124.0.0.0 Mobile Safari/537.36",
        "Mozilla/5.0 (Linux; Android 13; SM-S918B) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/122.0.0.0 Mobile Safari/537.36",
        "Mozilla/5.0 (iPhone; CPU iPhone OS 17_4 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/17.4 Mobile/15E148 Safari/604.1",
        "Mozilla/5.0 (Linux; Android 14; SM-A546B) AppleWebKit/537.36 (KHTML, like Gecko) SamsungBrowser/24.0 Chrome/118.0.0.0 Mobile Safari/537.36"
    )

    data class ExecutionResult(
        val success: Boolean,
        val keyword: String,
        val categoryName: String,
        val httpCode: Int,
        val target: String,
        val latencyMs: Long,
        val details: String
    )

    /**
     * Executes a single noise cycle choosing a random enabled keyword from enabled categories.
     */
    suspend fun executeNoiseCycle(): ExecutionResult = withContext(Dispatchers.IO) {
        val categories = bubbleDao.getCategoriesWithKeywordsSync()
        val enabledCategories = categories.filter { it.category.isEnabled }

        val candidateKeywords = mutableListOf<Pair<String, BubbleKeyword>>()
        for (catWithKeys in enabledCategories) {
            val enabledKeys = catWithKeys.keywords.filter { it.isEnabled }
            for (key in enabledKeys) {
                candidateKeywords.add(Pair(catWithKeys.category.name, key))
            }
        }

        if (candidateKeywords.isEmpty()) {
            return@withContext ExecutionResult(
                success = false,
                keyword = "Nenhuma tag ativa",
                categoryName = "Sistema",
                httpCode = 0,
                target = "Local",
                latencyMs = 0,
                details = "Todas as categorias ou tags estão desabilitadas. Ative pelo menos uma categoria."
            )
        }

        // Pick a keyword, with slight bias towards less-used keywords
        val selectedPair = candidateKeywords.sortedBy { it.second.usageCount }.take(5).random()
        val categoryName = selectedPair.first
        val keyword = selectedPair.second

        val encodedTerm = URLEncoder.encode(keyword.term, StandardCharsets.UTF_8.toString())

        // Choose between realistic public search & knowledge endpoints to poison network cache & DNS
        val targets = listOf(
            TargetSpec(
                name = "Wikipedia OpenSearch API",
                url = "https://pt.wikipedia.org/w/api.php?action=opensearch&search=$encodedTerm&limit=5&namespace=0&format=json"
            ),
            TargetSpec(
                name = "DuckDuckGo Instant Answer API",
                url = "https://api.duckduckgo.com/?q=$encodedTerm&format=json&no_html=1&skip_disambig=1"
            ),
            TargetSpec(
                name = "Wikidata Entity Search",
                url = "https://www.wikidata.org/w/api.php?action=wbsearchentities&search=$encodedTerm&language=pt&format=json"
            ),
            TargetSpec(
                name = "Public Web Query Index",
                url = "https://html.duckduckgo.com/html/?q=$encodedTerm"
            )
        )

        val target = targets.random()
        val randomUserAgent = userAgents.random()

        val startTime = System.currentTimeMillis()
        var httpCode = 0
        var success = false
        var detailsMsg = ""

        try {
            val request = Request.Builder()
                .url(target.url)
                .header("User-Agent", randomUserAgent)
                .header("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,application/json;q=0.8,*/*;q=0.7")
                .header("Accept-Language", "pt-BR,pt;q=0.9,en-US;q=0.8,en;q=0.7")
                .header("Cache-Control", "no-cache")
                .build()

            val response = httpClient.newCall(request).execute()
            val latency = System.currentTimeMillis() - startTime
            httpCode = response.code
            val bodyPreview = response.body?.string()?.take(180)?.replace("\n", " ") ?: "Sem corpo"
            response.close()

            success = httpCode in 200..399
            detailsMsg = if (success) {
                "HTTP $httpCode em ${latency}ms • DNS & Tráfego injetados com sucesso"
            } else {
                "Código HTTP: $httpCode • $bodyPreview"
            }

            // Register in Room Database
            val log = NoiseLog(
                keyword = keyword.term,
                categoryName = categoryName,
                timestamp = System.currentTimeMillis(),
                status = if (success) "SUCCESS" else "FAILED",
                httpCode = httpCode,
                targetEngine = target.name,
                responseTimeMs = latency,
                queryUrl = target.url,
                details = detailsMsg
            )
            bubbleDao.insertNoiseLog(log)
            bubbleDao.incrementKeywordUsage(keyword.id, System.currentTimeMillis())

            ExecutionResult(
                success = success,
                keyword = keyword.term,
                categoryName = categoryName,
                httpCode = httpCode,
                target = target.name,
                latencyMs = latency,
                details = detailsMsg
            )
        } catch (e: Exception) {
            val latency = System.currentTimeMillis() - startTime
            val errorMsg = e.localizedMessage ?: "Erro de conexão"

            val log = NoiseLog(
                keyword = keyword.term,
                categoryName = categoryName,
                timestamp = System.currentTimeMillis(),
                status = "FAILED",
                httpCode = 0,
                targetEngine = target.name,
                responseTimeMs = latency,
                queryUrl = target.url,
                details = "Falha de rede: $errorMsg"
            )
            bubbleDao.insertNoiseLog(log)

            ExecutionResult(
                success = false,
                keyword = keyword.term,
                categoryName = categoryName,
                httpCode = 0,
                target = target.name,
                latencyMs = latency,
                details = errorMsg
            )
        }
    }

    private data class TargetSpec(val name: String, val url: String)

    companion object {
        fun createOpenInAppIntent(context: Context, term: String, platform: String): Intent {
            val encoded = URLEncoder.encode(term, StandardCharsets.UTF_8.toString())
            val uri = when (platform.uppercase()) {
                "YOUTUBE" -> Uri.parse("https://www.youtube.com/results?search_query=$encoded")
                "TIKTOK" -> Uri.parse("https://www.tiktok.com/search?q=$encoded")
                "GOOGLE" -> Uri.parse("https://www.google.com/search?q=$encoded")
                else -> Uri.parse("https://duckduckgo.com/?q=$encoded")
            }
            return Intent(Intent.ACTION_VIEW, uri).apply {
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
        }
    }
}

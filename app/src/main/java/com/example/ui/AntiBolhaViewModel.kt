package com.example.ui

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.AppDatabase
import com.example.data.model.BubbleCategory
import com.example.data.model.BubbleKeyword
import com.example.data.model.CategoryWithKeywords
import com.example.data.model.NoiseLog
import com.example.engine.NoiseEngine
import com.example.service.NoiseGeneratorService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AntiBolhaViewModel(application: Application) : AndroidViewModel(application) {

    private val db = AppDatabase.getInstance(application)
    private val dao = db.bubbleDao()
    private val noiseEngine = NoiseEngine(dao)

    val categoriesWithKeywords: StateFlow<List<CategoryWithKeywords>> =
        dao.getCategoriesWithKeywords()
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = emptyList()
            )

    val recentLogs: StateFlow<List<NoiseLog>> =
        dao.getRecentNoiseLogs(100)
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = emptyList()
            )

    val totalNoiseCount: StateFlow<Int> =
        dao.getTotalNoiseCountFlow()
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = 0
            )

    val isServiceRunning: StateFlow<Boolean> = NoiseGeneratorService.isServiceRunning
    val currentIntervalMinutes: StateFlow<Int> = NoiseGeneratorService.intervalMinutesFlow
    val lastNoiseSummary: StateFlow<String?> = NoiseGeneratorService.lastNoiseResult

    private val _isBurstRunning = MutableStateFlow(false)
    val isBurstRunning: StateFlow<Boolean> = _isBurstRunning.asStateFlow()

    private val _lastBurstMessage = MutableStateFlow<String?>(null)
    val lastBurstMessage: StateFlow<String?> = _lastBurstMessage.asStateFlow()

    init {
        // Ensure initial database seeding occurs
        viewModelScope.launch(Dispatchers.IO) {
            AppDatabase.seedDatabase(dao)
        }
    }

    fun toggleService(context: Context) {
        if (isServiceRunning.value) {
            NoiseGeneratorService.stopService(context)
        } else {
            NoiseGeneratorService.startService(context, currentIntervalMinutes.value)
        }
    }

    fun updateInterval(context: Context, minutes: Int) {
        if (isServiceRunning.value) {
            NoiseGeneratorService.updateInterval(context, minutes)
        } else {
            // Service not running, just update interval
            NoiseGeneratorService.updateInterval(context, minutes)
        }
    }

    fun triggerBurstNoise(count: Int = 1) {
        if (_isBurstRunning.value) return
        viewModelScope.launch {
            _isBurstRunning.value = true
            _lastBurstMessage.value = "Disparando $count consulta(s) de ruído..."
            withContext(Dispatchers.IO) {
                var successCount = 0
                for (i in 1..count) {
                    val result = noiseEngine.executeNoiseCycle()
                    if (result.success) successCount++
                }
                _lastBurstMessage.value = if (successCount > 0) {
                    "Sucesso! $successCount/$count ruídos injetados no tráfego."
                } else {
                    "Atenção: verifique se há categorias ativas."
                }
            }
            _isBurstRunning.value = false
        }
    }

    fun toggleCategory(category: BubbleCategory) {
        viewModelScope.launch(Dispatchers.IO) {
            dao.updateCategory(category.copy(isEnabled = !category.isEnabled))
        }
    }

    fun toggleKeyword(keyword: BubbleKeyword) {
        viewModelScope.launch(Dispatchers.IO) {
            dao.updateKeyword(keyword.copy(isEnabled = !keyword.isEnabled))
        }
    }

    fun addCategory(
        name: String,
        description: String,
        emoji: String,
        colorHex: String,
        keywords: List<String>
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            val newCat = BubbleCategory(
                name = name,
                description = description,
                iconEmoji = emoji.ifBlank { "⚡" },
                colorHex = colorHex.ifBlank { "#8B5CF6" },
                isEnabled = true
            )
            val catId = dao.insertCategory(newCat)
            val keywordEntities = keywords.filter { it.isNotBlank() }.map { term ->
                BubbleKeyword(
                    categoryId = catId,
                    term = term.trim(),
                    isEnabled = true
                )
            }
            if (keywordEntities.isNotEmpty()) {
                dao.insertKeywords(keywordEntities)
            }
        }
    }

    fun addKeyword(categoryId: Long, term: String, platform: String = "ALL") {
        if (term.isBlank()) return
        viewModelScope.launch(Dispatchers.IO) {
            dao.insertKeyword(
                BubbleKeyword(
                    categoryId = categoryId,
                    term = term.trim(),
                    targetPlatform = platform,
                    isEnabled = true
                )
            )
        }
    }

    fun deleteKeyword(keyword: BubbleKeyword) {
        viewModelScope.launch(Dispatchers.IO) {
            dao.deleteKeyword(keyword)
        }
    }

    fun deleteCategory(category: BubbleCategory) {
        viewModelScope.launch(Dispatchers.IO) {
            dao.deleteCategory(category)
        }
    }

    fun clearHistory() {
        viewModelScope.launch(Dispatchers.IO) {
            dao.clearNoiseLogs()
        }
    }
}

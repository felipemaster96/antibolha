package com.example.ui.screens

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.AmberWarning
import com.example.ui.theme.CyberCyan
import com.example.ui.theme.DarkBorder
import com.example.ui.theme.DarkSurface
import com.example.ui.theme.DarkSurfaceVariant
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary

/**
 * Um atalho de privacidade. Se [intentAction] ou [url] existirem, o botão "Abrir" tenta
 * abrir direto. Se não abrir (ou não existir link), mostramos o passo a passo.
 */
private data class PrivacyShortcut(
    val title: String,
    val description: String,
    val steps: List<String>,
    val intentAction: String? = null,
    val url: String? = null
)

private val shortcuts = listOf(
    PrivacyShortcut(
        title = "Apagar histórico do YouTube",
        description = "Apaga o que você assistiu e pesquisou. É o que mais alimenta as recomendações.",
        url = "https://myactivity.google.com/product/youtube",
        steps = listOf(
            "Abra o link no botão (ele abre a sua Atividade do Google).",
            "Toque em Excluir e escolha o período: \"Desde sempre\".",
            "Confirme. Se preferir pelo app: YouTube > Você > Configurações > Gerenciar todo o histórico."
        )
    ),
    PrivacyShortcut(
        title = "Pausar o histórico do YouTube",
        description = "Impede que novos vídeos entrem no histórico e moldem o seu feed.",
        url = "https://myactivity.google.com/activitycontrols",
        steps = listOf(
            "Abra o link no botão (Controles de atividade do Google).",
            "Procure \"Histórico do YouTube\" e toque em Desativar.",
            "Opcional: ative também a exclusão automática do histórico."
        )
    ),
    PrivacyShortcut(
        title = "Desligar personalização de anúncios",
        description = "Faz o Google parar de usar seu perfil para escolher os anúncios.",
        intentAction = "com.google.android.gms.settings.ADS_PRIVACY",
        url = "https://adssettings.google.com",
        steps = listOf(
            "Abra o link no botão (Configurações de anúncios do Google).",
            "Desative a \"Personalização de anúncios\".",
            "No Android: Configurações > Google > Anúncios também tem essa opção."
        )
    ),
    PrivacyShortcut(
        title = "TikTok: atualizar o feed Para Você",
        description = "Faz o TikTok recomendar como se você tivesse acabado de criar a conta. Não dá para desfazer.",
        steps = listOf(
            "No TikTok, abra seu Perfil e toque nas três linhas (canto superior direito).",
            "Toque em Configurações e privacidade > Preferências de conteúdo.",
            "Toque em \"Atualizar seu feed Para Você\" e confirme."
        )
    ),
    PrivacyShortcut(
        title = "Instagram: redefinir conteúdo sugerido",
        description = "Reinicia as sugestões do Explorar, Reels e feed. Quem você segue não muda. Não dá para desfazer.",
        steps = listOf(
            "No Instagram, abra seu Perfil e toque nas três linhas (canto superior direito).",
            "Toque em Configurações e atividade > Preferências de conteúdo.",
            "Toque em \"Redefinir conteúdo sugerido\", leia e confirme."
        )
    )
)

private fun openShortcut(context: Context, s: PrivacyShortcut): Boolean {
    if (s.intentAction != null) {
        try {
            context.startActivity(Intent(s.intentAction).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK))
            return true
        } catch (e: Exception) {
            // cai para a URL
        }
    }
    if (s.url != null) {
        try {
            context.startActivity(
                Intent(Intent.ACTION_VIEW, Uri.parse(s.url)).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            )
            return true
        } catch (e: Exception) {
            // cai para o passo a passo
        }
    }
    return false
}

@Composable
fun PrivacyScreen(
    noiseEngineEnabled: Boolean = false,
    onNoiseEngineChange: (Boolean) -> Unit = {}
) {
    val context = LocalContext.current
    var showWarning by rememberSaveable { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        Text(
            text = "Privacidade",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
            color = TextPrimary
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = "Atalhos para limpar o que as plataformas sabem sobre você. " +
                "O app não acessa suas contas: ele só abre as telas certas.",
            style = MaterialTheme.typography.bodyMedium,
            color = TextSecondary
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Os nomes dos menus podem variar conforme a versão do app e o país.",
            fontSize = 12.sp,
            color = AmberWarning
        )

        Spacer(modifier = Modifier.height(16.dp))

        shortcuts.forEach { shortcut ->
            PrivacyCard(shortcut = shortcut, context = context)
            Spacer(modifier = Modifier.height(12.dp))
        }

        Spacer(modifier = Modifier.height(12.dp))
        Text(
            text = "Avançado",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = TextPrimary
        )
        Spacer(modifier = Modifier.height(8.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = DarkSurface),
            shape = RoundedCornerShape(16.dp),
            border = BorderStroke(1.dp, DarkBorder)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f).padding(end = 12.dp)) {
                        Text(
                            "Robô de ruído (NoiseEngine)",
                            color = TextPrimary,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            "Faz buscas automáticas em segundo plano. Desligado por padrão.",
                            color = TextSecondary,
                            fontSize = 13.sp
                        )
                    }
                    Switch(
                        checked = noiseEngineEnabled,
                        onCheckedChange = { wantOn ->
                            if (wantOn) showWarning = true else onNoiseEngineChange(false)
                        }
                    )
                }
            }
        }
        Spacer(modifier = Modifier.height(24.dp))
    }

    if (showWarning) {
        AlertDialog(
            onDismissRequest = { showWarning = false },
            title = { Text("Ligar o robô de ruído?") },
            text = {
                Text(
                    "• Ele envia buscas automáticas para Wikipédia, DuckDuckGo e Wikidata, " +
                        "não para YouTube, TikTok ou Instagram. Por isso não muda o que " +
                        "essas plataformas recomendam para você.\n\n" +
                        "• Usa internet e bateria e mantém uma notificação fixa enquanto roda.\n\n" +
                        "• Esse tipo de tráfego automático pode ir contra os termos de uso de " +
                        "alguns serviços."
                )
            },
            confirmButton = {
                TextButton(onClick = {
                    showWarning = false
                    onNoiseEngineChange(true)
                }) { Text("Ligar mesmo assim") }
            },
            dismissButton = {
                TextButton(onClick = { showWarning = false }) { Text("Cancelar") }
            }
        )
    }
}

@Composable
private fun PrivacyCard(shortcut: PrivacyShortcut, context: Context) {
    var expanded by rememberSaveable(shortcut.title) { mutableStateOf(false) }
    var failed by rememberSaveable(shortcut.title) { mutableStateOf(false) }
    val hasTarget = shortcut.intentAction != null || shortcut.url != null

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = DarkSurface),
        shape = RoundedCornerShape(16.dp),
        border = BorderStroke(1.dp, DarkBorder)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(shortcut.title, color = TextPrimary, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(4.dp))
            Text(shortcut.description, color = TextSecondary, fontSize = 13.sp)
            Spacer(modifier = Modifier.height(12.dp))

            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (hasTarget) {
                    Button(
                        onClick = {
                            if (!openShortcut(context, shortcut)) {
                                failed = true
                                expanded = true
                            }
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = CyberCyan,
                            contentColor = Color(0xFF032830)
                        )
                    ) { Text("Abrir") }
                }
                TextButton(onClick = { expanded = !expanded }) {
                    Text(
                        if (expanded) "Esconder passo a passo" else "Passo a passo",
                        color = CyberCyan
                    )
                }
            }

            if (failed) {
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    "Não consegui abrir direto. Siga o passo a passo abaixo.",
                    color = AmberWarning,
                    fontSize = 12.sp
                )
            }

            if (expanded) {
                Spacer(modifier = Modifier.height(8.dp))
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 4.dp),
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    shortcut.steps.forEachIndexed { index, step ->
                        Text(
                            text = "${index + 1}. $step",
                            color = TextPrimary,
                            fontSize = 13.sp
                        )
                    }
                }
            }
        }
    }
}

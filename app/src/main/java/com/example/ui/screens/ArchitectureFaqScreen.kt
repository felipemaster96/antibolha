package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Code
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.AmberWarning
import com.example.ui.theme.CyberCyan
import com.example.ui.theme.DarkBorder
import com.example.ui.theme.DarkSurface
import com.example.ui.theme.DarkSurfaceVariant
import com.example.ui.theme.EmeraldGreen
import com.example.ui.theme.LuminousViolet
import com.example.ui.theme.NeonPink
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary

@Composable
fun ArchitectureFaqScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        Text(
            text = "Arquitetura & Estratégia AntiBolha",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            color = TextPrimary
        )
        Text(
            text = "Análise técnica de tecnologia, limitações de segundo plano do SO e eficácia algorítmica.",
            style = MaterialTheme.typography.bodySmall,
            color = TextSecondary
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Topic 1: Technology Comparison
        ArchitectureCard(
            title = "1. Qual Tecnologia Escolher?",
            subtitle = "Unity vs. Flutter/React Native vs. Nativo (Kotlin/Swift)",
            icon = Icons.Default.Code,
            accentColor = CyberCyan
        ) {
            Text(
                text = "• Unity (C#): ❌ Inadequada para este app. A Unity é um motor gráfico para jogos 3D. O runtime ocupa 30MB+ em memória, gasta bateria contínua e tanto o Android (Doze/OOM Killer) quanto o iOS matam o processo da Unity quase que imediatamente quando o app vai para segundo plano. A Unity não possui APIs nativas headless para serviços sem tela.",
                style = MaterialTheme.typography.bodySmall,
                color = TextSecondary,
                lineHeight = 20.sp
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "• Flutter / React Native: ⚠️ Viável para a UI, mas problemática no background. Executar requisições periódicas a cada 5-10 minutos em segundo plano exige pontes nativas complexas (MethodChannels / Headless JS). O Android 14+ impõe regras severas para Foreground Services com tipos obrigatórios (dataSync).",
                style = MaterialTheme.typography.bodySmall,
                color = TextSecondary,
                lineHeight = 20.sp
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "• Nativo (Kotlin / Jetpack Compose): ✅ Escolha Ideal e Superior. Permite controle cirúrgico de Foreground Services, WorkManager, OkHttp com pools de conexões leves (menos de 20MB de RAM), wake locks eficientes e persistência rápida com Room SQLite.",
                style = MaterialTheme.typography.bodySmall,
                color = EmeraldGreen,
                fontWeight = FontWeight.SemiBold,
                lineHeight = 20.sp
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Topic 2: Background limits
        ArchitectureCard(
            title = "2. Limitações de Segundo Plano no Android e iOS",
            subtitle = "Intervalos de 5-10 minutos, Doze Mode e Foreground Services",
            icon = Icons.Default.Speed,
            accentColor = AmberWarning
        ) {
            Text(
                text = "• WorkManager no Android possui um limite mínimo rígido de 15 minutos para tarefas periódicas (PeriodicWorkRequest). Se você pedir 5 minutos, o Android força 15m.",
                style = MaterialTheme.typography.bodySmall,
                color = TextSecondary,
                lineHeight = 20.sp
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = "• A solução utilizada neste app é um Foreground Service com tipo dataSync e notificação persistente discreta. Isso mantém a coroutine do Kotlin ativa para disparar nos intervalos exatos escolhidos (5m, 10m, 15m, 30m) sem que o sistema encerre o processo.",
                style = MaterialTheme.typography.bodySmall,
                color = TextSecondary,
                lineHeight = 20.sp
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = "• No iOS, a Apple não permite serviços persistentes equivalentes sem reprodução de áudio ou GPS. No iOS, o app dependeria de Background Fetch (onde o iOS decide o momento da execução) ou disparo via Push Notifications silenciosas.",
                style = MaterialTheme.typography.bodySmall,
                color = TextSecondary,
                lineHeight = 20.sp
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Topic 3: How recommendation algorithms work
        ArchitectureCard(
            title = "3. Como o 'Furar Bolha' Realmente Funciona",
            subtitle = "Filtragem colaborativa, embeddings vetoriais e poluição de histórico",
            icon = Icons.Default.Lightbulb,
            accentColor = LuminousViolet
        ) {
            Text(
                text = "Algoritmos modernos (YouTube, TikTok, Instagram, Meta Ads) utilizam redes neurais com embeddings vetoriais de interesse do usuário:",
                style = MaterialTheme.typography.bodySmall,
                color = TextSecondary,
                lineHeight = 20.sp
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = "1. Tráfego de Rede & DNS: Redes de anúncios rastreiam requisições de domínio e pesquisas do dispositivo em nível de IP e DNS. As consultas HTTP reais geradas pelo app alimentam esse histórico com assuntos opostos (física quântica, mecânica de tratores, numismática).",
                style = MaterialTheme.typography.bodySmall,
                color = TextSecondary,
                lineHeight = 20.sp
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = "2. Engajamento no App Oficial: Para maximizar o impacto nas recomendações do feed interno (YouTube/TikTok), use os botões rápidos 'YouTube / Google / TikTok' na aba de Histórico. Isso abre a busca com sua conta logada, confundindo o modelo colaborativo!",
                style = MaterialTheme.typography.bodySmall,
                color = TextSecondary,
                lineHeight = 20.sp
            )
        }

        Spacer(modifier = Modifier.height(30.dp))
    }
}

@Composable
fun ArchitectureCard(
    title: String,
    subtitle: String,
    icon: ImageVector,
    accentColor: Color,
    content: @Composable () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = DarkSurface),
        shape = RoundedCornerShape(16.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, DarkBorder)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(accentColor.copy(alpha = 0.2f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(imageVector = icon, contentDescription = null, tint = accentColor, modifier = Modifier.size(20.dp))
                }
                Spacer(modifier = Modifier.width(10.dp))
                Column {
                    Text(
                        text = title,
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )
                    Text(
                        text = subtitle,
                        style = MaterialTheme.typography.labelSmall,
                        color = accentColor,
                        fontSize = 11.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))
            content()
        }
    }
}

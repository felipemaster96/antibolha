package com.example.ui.screens

import android.content.Context
import androidx.compose.animation.AnimatedVisibility
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.CategoryWithKeywords
import com.example.data.model.NoiseLog
import com.example.ui.components.NoiseRadarPulse
import com.example.ui.theme.AmberWarning
import com.example.ui.theme.CyberCyan
import com.example.ui.theme.DarkBorder
import com.example.ui.theme.DarkSurface
import com.example.ui.theme.DarkSurfaceVariant
import com.example.ui.theme.EmeraldGreen
import com.example.ui.theme.LuminousViolet
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary

@Composable
fun DashboardScreen(
    isServiceRunning: Boolean,
    intervalMinutes: Int,
    totalNoiseCount: Int,
    categories: List<CategoryWithKeywords>,
    recentLogs: List<NoiseLog>,
    isBurstRunning: Boolean,
    lastBurstMessage: String?,
    onToggleService: (Context) -> Unit,
    onUpdateInterval: (Context, Int) -> Unit,
    onTriggerBurst: (Int) -> Unit,
    onNavigateToHistory: () -> Unit,
    noiseEngineEnabled: Boolean = true,
    onGoToBreakBubble: () -> Unit = {},
    onOpenAdvanced: () -> Unit = {}
) {
    val context = LocalContext.current
    val totalTags = categories.sumOf { it.keywords.size }
    val activeCategoriesCount = categories.count { it.category.isEnabled }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
      if (!noiseEngineEnabled) {
        AdvancedOffContent(
            onGoToBreakBubble = onGoToBreakBubble,
            onOpenAdvanced = onOpenAdvanced
        )
      } else {
        // Top status headline
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = "STATUS DO SISTEMA",
                    style = MaterialTheme.typography.labelSmall,
                    color = TextSecondary,
                    letterSpacing = 1.5.sp
                )
                Text(
                    text = if (isServiceRunning) "Proteção de Ruído Ativa" else "Injeção Pausada",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = if (isServiceRunning) EmeraldGreen else AmberWarning
                )
            }

            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(20.dp))
                    .background(if (isServiceRunning) EmeraldGreen.copy(alpha = 0.15f) else AmberWarning.copy(alpha = 0.15f))
                    .border(
                        1.dp,
                        if (isServiceRunning) EmeraldGreen.copy(alpha = 0.5f) else AmberWarning.copy(alpha = 0.5f),
                        RoundedCornerShape(20.dp)
                    )
                    .padding(horizontal = 12.dp, vertical = 6.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(8.dp)
                            .clip(CircleShape)
                            .background(if (isServiceRunning) EmeraldGreen else AmberWarning)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = if (isServiceRunning) "EM EXECUÇÃO" else "STANDBY",
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Bold,
                        color = if (isServiceRunning) EmeraldGreen else AmberWarning
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Center Radar Graphic
        NoiseRadarPulse(
            isActive = isServiceRunning || isBurstRunning,
            modifier = Modifier.padding(vertical = 12.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Big Main Toggle Button
        Button(
            onClick = { onToggleService(context) },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
                .testTag("toggle_service_button"),
            colors = ButtonDefaults.buttonColors(
                containerColor = if (isServiceRunning) AmberWarning else CyberCyan
            ),
            shape = RoundedCornerShape(14.dp)
        ) {
            Icon(
                imageVector = if (isServiceRunning) Icons.Default.Stop else Icons.Default.PlayArrow,
                contentDescription = null,
                tint = Color(0xFF032830),
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = if (isServiceRunning) "Pausar Injeção de Ruído" else "Ativar Robô AntiBolha",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF032830)
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Interval Setting Card
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = DarkSurface),
            shape = RoundedCornerShape(16.dp),
            border = androidx.compose.foundation.BorderStroke(1.dp, DarkBorder)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Tune,
                            contentDescription = null,
                            tint = CyberCyan,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Frequência em Segundo Plano",
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.SemiBold,
                            color = TextPrimary
                        )
                    }
                    Text(
                        text = "A cada $intervalMinutes min",
                        style = MaterialTheme.typography.labelMedium,
                        color = CyberCyan,
                        fontWeight = FontWeight.Bold
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    listOf(5, 10, 15, 30, 60).forEach { mins ->
                        val isSelected = intervalMinutes == mins
                        FilterChip(
                            selected = isSelected,
                            onClick = { onUpdateInterval(context, mins) },
                            label = { Text("${mins}m") },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = CyberCyan,
                                selectedLabelColor = Color(0xFF032830),
                                containerColor = DarkSurfaceVariant,
                                labelColor = TextSecondary
                            ),
                            border = FilterChipDefaults.filterChipBorder(
                                enabled = true,
                                selected = isSelected,
                                borderColor = if (isSelected) CyberCyan else DarkBorder
                            )
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Quick Manual Burst Action
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = DarkSurface),
            shape = RoundedCornerShape(16.dp),
            border = androidx.compose.foundation.BorderStroke(1.dp, DarkBorder)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Bolt,
                        contentDescription = null,
                        tint = LuminousViolet,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Disparo Manual Imediato",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.SemiBold,
                        color = TextPrimary
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Injeta termos opostos instantaneamente na rede sem esperar o próximo ciclo.",
                    style = MaterialTheme.typography.bodySmall,
                    color = TextSecondary
                )

                Spacer(modifier = Modifier.height(12.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Button(
                        onClick = { onTriggerBurst(1) },
                        enabled = !isBurstRunning,
                        modifier = Modifier
                            .weight(1f)
                            .height(44.dp)
                            .testTag("burst_1x_button"),
                        colors = ButtonDefaults.buttonColors(containerColor = LuminousViolet),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        if (isBurstRunning) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(18.dp),
                                color = Color.White,
                                strokeWidth = 2.dp
                            )
                        } else {
                            Text("Furar Agora (1x)", fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
                        }
                    }

                    OutlinedButton(
                        onClick = { onTriggerBurst(3) },
                        enabled = !isBurstRunning,
                        modifier = Modifier
                            .weight(1f)
                            .height(44.dp)
                            .testTag("burst_3x_button"),
                        border = androidx.compose.foundation.BorderStroke(1.dp, LuminousViolet),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Text("Rajada (3x)", fontSize = 13.sp, color = LuminousViolet, fontWeight = FontWeight.SemiBold)
                    }
                }

                AnimatedVisibility(visible = lastBurstMessage != null) {
                    Text(
                        text = lastBurstMessage ?: "",
                        style = MaterialTheme.typography.bodySmall,
                        color = EmeraldGreen,
                        modifier = Modifier.padding(top = 10.dp)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Three Metrics Counters
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            MetricCard(
                title = "Ruídos Injetados",
                value = "$totalNoiseCount",
                accent = CyberCyan,
                modifier = Modifier.weight(1f)
            )
            MetricCard(
                title = "Bolhas Ativas",
                value = "$activeCategoriesCount / ${categories.size}",
                accent = LuminousViolet,
                modifier = Modifier.weight(1f)
            )
            MetricCard(
                title = "Tags Opostas",
                value = "$totalTags",
                accent = EmeraldGreen,
                modifier = Modifier.weight(1f)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Last Generated Noise Peek
        val lastLog = recentLogs.firstOrNull()
        if (lastLog != null) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = DarkSurface),
                shape = RoundedCornerShape(16.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, DarkBorder)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "ÚLTIMO RUÍDO DISPARADO",
                            style = MaterialTheme.typography.labelSmall,
                            color = TextSecondary,
                            letterSpacing = 1.sp
                        )
                        OutlinedButton(
                            onClick = onNavigateToHistory,
                            shape = RoundedCornerShape(8.dp),
                            contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 8.dp, vertical = 2.dp)
                        ) {
                            Text("Ver Todos", fontSize = 11.sp, color = CyberCyan)
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "\"${lastLog.keyword}\"",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = lastLog.categoryName,
                            style = MaterialTheme.typography.bodySmall,
                            color = CyberCyan
                        )
                        Text(" • ", color = TextMuted)
                        Text(
                            text = "${lastLog.targetEngine} (${lastLog.responseTimeMs}ms)",
                            style = MaterialTheme.typography.bodySmall,
                            color = TextSecondary
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))
      }
    }
}

@Composable
private fun AdvancedOffContent(
    onGoToBreakBubble: () -> Unit,
    onOpenAdvanced: () -> Unit
) {
    Spacer(modifier = Modifier.height(24.dp))
    Text(
        text = "Seu painel",
        style = MaterialTheme.typography.titleLarge,
        fontWeight = FontWeight.Bold,
        color = TextPrimary
    )
    Spacer(modifier = Modifier.height(8.dp))
    Text(
        text = "A forma mais eficaz de sair da bolha é buscar você mesmo coisas novas " +
            "e limpar o que as plataformas guardaram sobre você.",
        color = TextSecondary,
        fontSize = 14.sp
    )
    Spacer(modifier = Modifier.height(20.dp))
    Button(
        onClick = onGoToBreakBubble,
        modifier = Modifier.fillMaxWidth(),
        colors = ButtonDefaults.buttonColors(
            containerColor = CyberCyan,
            contentColor = Color(0xFF032830)
        )
    ) {
        Text("Me tire da bolha", fontWeight = FontWeight.Bold)
    }
    Spacer(modifier = Modifier.height(24.dp))
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = DarkSurface),
        shape = RoundedCornerShape(16.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, DarkBorder)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "Robô de ruído (avançado)",
                color = TextPrimary,
                fontWeight = FontWeight.SemiBold
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Desligado. Faz buscas automáticas em segundo plano. " +
                    "Para ligar, vá em Privacidade > Avançado.",
                color = TextSecondary,
                fontSize = 13.sp
            )
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedButton(onClick = onOpenAdvanced) {
                Text("Abrir Privacidade", color = CyberCyan)
            }
        }
    }
}

@Composable
fun MetricCard(
    title: String,
    value: String,
    accent: Color,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(containerColor = DarkSurface),
        shape = RoundedCornerShape(14.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, DarkBorder)
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = value,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = accent
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = title,
                style = MaterialTheme.typography.labelSmall,
                color = TextSecondary,
                fontSize = 10.sp,
                maxLines = 1
            )
        }
    }
}

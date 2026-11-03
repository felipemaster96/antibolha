package com.example.ui.screens

import android.content.Context
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.DeleteSweep
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.OpenInNew
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import com.example.data.model.NoiseLog
import com.example.engine.NoiseEngine
import com.example.ui.theme.CyberCyan
import com.example.ui.theme.DarkBorder
import com.example.ui.theme.DarkSurface
import com.example.ui.theme.DarkSurfaceVariant
import com.example.ui.theme.EmeraldGreen
import com.example.ui.theme.NeonPink
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun HistoryScreen(
    logs: List<NoiseLog>,
    onClearLogs: () -> Unit
) {
    val context = LocalContext.current
    val timeFormat = SimpleDateFormat("HH:mm:ss", Locale.getDefault())
    val dateFormat = SimpleDateFormat("dd/MM", Locale.getDefault())

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = "Auditoria de Ruído & Logs",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )
                Text(
                    text = "${logs.size} requisições registradas",
                    style = MaterialTheme.typography.bodySmall,
                    color = TextSecondary
                )
            }

            if (logs.isNotEmpty()) {
                IconButton(
                    onClick = onClearLogs,
                    modifier = Modifier.testTag("clear_logs_button")
                ) {
                    Icon(
                        imageVector = Icons.Default.DeleteSweep,
                        contentDescription = "Limpar histórico",
                        tint = TextSecondary
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        if (logs.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(bottom = 60.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "Nenhum ruído registrado ainda.",
                        style = MaterialTheme.typography.titleMedium,
                        color = TextPrimary
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "Ative o robô no painel ou clique em 'Furar Agora' para disparar.",
                        style = MaterialTheme.typography.bodySmall,
                        color = TextSecondary
                    )
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(logs, key = { it.id }) { log ->
                    val isSuccess = log.status == "SUCCESS"
                    val date = Date(log.timestamp)

                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = DarkSurface),
                        shape = RoundedCornerShape(14.dp),
                        border = androidx.compose.foundation.BorderStroke(1.dp, DarkBorder)
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            // Header Row
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        imageVector = if (isSuccess) Icons.Default.CheckCircle else Icons.Default.Error,
                                        contentDescription = null,
                                        tint = if (isSuccess) EmeraldGreen else NeonPink,
                                        modifier = Modifier.size(16.dp)
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(
                                        text = log.categoryName,
                                        style = MaterialTheme.typography.labelMedium,
                                        color = CyberCyan,
                                        fontWeight = FontWeight.SemiBold
                                    )
                                }

                                Text(
                                    text = "${dateFormat.format(date)} às ${timeFormat.format(date)}",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = TextMuted,
                                    fontSize = 11.sp
                                )
                            }

                            Spacer(modifier = Modifier.height(8.dp))

                            Text(
                                text = "\"${log.keyword}\"",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = TextPrimary
                            )

                            Spacer(modifier = Modifier.height(6.dp))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "${log.targetEngine} • ${log.responseTimeMs}ms",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = TextSecondary
                                )

                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(6.dp))
                                        .background(if (isSuccess) EmeraldGreen.copy(alpha = 0.15f) else NeonPink.copy(alpha = 0.15f))
                                        .padding(horizontal = 6.dp, vertical = 2.dp)
                                ) {
                                    Text(
                                        text = if (log.httpCode > 0) "HTTP ${log.httpCode}" else "FALHA",
                                        color = if (isSuccess) EmeraldGreen else NeonPink,
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }

                            // Quick open buttons in official apps
                            Spacer(modifier = Modifier.height(10.dp))
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                OpenPlatformButton(
                                    label = "YouTube",
                                    onClick = {
                                        context.startActivity(
                                            NoiseEngine.createOpenInAppIntent(context, log.keyword, "YOUTUBE")
                                        )
                                    }
                                )
                                OpenPlatformButton(
                                    label = "Google",
                                    onClick = {
                                        context.startActivity(
                                            NoiseEngine.createOpenInAppIntent(context, log.keyword, "GOOGLE")
                                        )
                                    }
                                )
                                OpenPlatformButton(
                                    label = "TikTok",
                                    onClick = {
                                        context.startActivity(
                                            NoiseEngine.createOpenInAppIntent(context, log.keyword, "TIKTOK")
                                        )
                                    }
                                )
                            }
                        }
                    }
                }

                item {
                    Spacer(modifier = Modifier.height(40.dp))
                }
            }
        }
    }
}

@Composable
fun OpenPlatformButton(
    label: String,
    onClick: () -> Unit
) {
    OutlinedButton(
        onClick = onClick,
        shape = RoundedCornerShape(8.dp),
        contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 8.dp, vertical = 2.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, DarkBorder)
    ) {
        Icon(
            imageVector = Icons.Default.OpenInNew,
            contentDescription = null,
            tint = TextSecondary,
            modifier = Modifier.size(12.dp)
        )
        Spacer(modifier = Modifier.width(4.dp))
        Text(text = label, fontSize = 11.sp, color = TextSecondary)
    }
}

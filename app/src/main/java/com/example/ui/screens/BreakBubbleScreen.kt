package com.example.ui.screens

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
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.CyberCyan
import com.example.ui.theme.DarkBorder
import com.example.ui.theme.DarkSurface
import com.example.ui.theme.DarkSurfaceVariant
import com.example.ui.theme.EmeraldGreen
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary

private val platformOptions = listOf(
    "ALEATORIO" to "Aleatório",
    "YOUTUBE" to "YouTube",
    "TIKTOK" to "TikTok",
    "INSTAGRAM" to "Instagram"
)

@Composable
fun BreakBubbleScreen(
    countToday: Int,
    goal: Int,
    lastTopic: String?,
    lastCategory: String?,
    message: String?,
    onBreakBubble: (platform: String) -> Unit,
    onGoalChange: (Int) -> Unit
) {
    var selectedPlatform by rememberSaveable { mutableStateOf("ALEATORIO") }
    val progress = (countToday.toFloat() / goal.coerceAtLeast(1)).coerceIn(0f, 1f)
    val goalReached = countToday >= goal

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        Text(
            text = "Me tire da bolha",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
            color = TextPrimary
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = "Sorteia um tema fora do seu feed e abre a busca direto no app escolhido.",
            style = MaterialTheme.typography.bodyMedium,
            color = TextSecondary
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Meta diária
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
                    Text(
                        text = "Meta de hoje",
                        color = TextPrimary,
                        fontWeight = FontWeight.SemiBold
                    )
                    Text(
                        text = "$countToday/$goal",
                        color = if (goalReached) EmeraldGreen else CyberCyan,
                        fontWeight = FontWeight.Bold
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
                LinearProgressIndicator(
                    progress = { progress },
                    modifier = Modifier.fillMaxWidth(),
                    color = if (goalReached) EmeraldGreen else CyberCyan,
                    trackColor = DarkSurfaceVariant
                )
                Spacer(modifier = Modifier.height(8.dp))
                if (goalReached) {
                    Text(
                        text = "Meta batida! Cada tema novo ajuda a bagunçar o seu perfil.",
                        color = EmeraldGreen,
                        fontSize = 12.sp
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedButton(onClick = { onGoalChange(goal - 1) }) { Text("−") }
                    Text("Meta: $goal por dia", color = TextSecondary, fontSize = 13.sp)
                    OutlinedButton(onClick = { onGoalChange(goal + 1) }) { Text("+") }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text("Abrir em:", color = TextSecondary, fontSize = 13.sp)
        Spacer(modifier = Modifier.height(6.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
            platformOptions.forEach { (key, label) ->
                val isSelected = selectedPlatform == key
                FilterChip(
                    selected = isSelected,
                    onClick = { selectedPlatform = key },
                    label = { Text(label, fontSize = 12.sp) },
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

        Spacer(modifier = Modifier.height(20.dp))

        Button(
            onClick = { onBreakBubble(selectedPlatform) },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
                .testTag("btn_break_bubble"),
            shape = RoundedCornerShape(16.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = CyberCyan,
                contentColor = Color(0xFF032830)
            )
        ) {
            Text("Me tire da bolha", fontWeight = FontWeight.Bold, fontSize = 16.sp)
        }

        Spacer(modifier = Modifier.height(16.dp))

        if (lastTopic != null) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = DarkSurfaceVariant),
                shape = RoundedCornerShape(12.dp)
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Text("Último tema sorteado", color = TextMuted, fontSize = 11.sp)
                    Text(lastTopic, color = TextPrimary, fontWeight = FontWeight.Bold)
                    if (lastCategory != null) {
                        Text(lastCategory, color = TextSecondary, fontSize = 12.sp)
                    }
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
        }

        if (message != null) {
            Text(message, color = TextSecondary, fontSize = 12.sp)
        }
    }
}

package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.BubbleCategory
import com.example.ui.theme.CyberCyan
import com.example.ui.theme.DarkBorder
import com.example.ui.theme.DarkSurface
import com.example.ui.theme.LuminousViolet
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary

@Composable
fun NewBubbleDialog(
    categories: List<BubbleCategory>,
    onDismiss: () -> Unit,
    onAddCategory: (name: String, desc: String, emoji: String, colorHex: String, tags: List<String>) -> Unit,
    onAddTagToExisting: (categoryId: Long, tag: String) -> Unit
) {
    var selectedTab by remember { mutableIntStateOf(0) } // 0: Nova Categoria, 1: Nova Tag

    // Category form state
    var catName by remember { mutableStateOf("") }
    var catDesc by remember { mutableStateOf("") }
    var catEmoji by remember { mutableStateOf("⚡") }
    var catColorHex by remember { mutableStateOf("#8B5CF6") }
    var catInitialTags by remember { mutableStateOf("") }

    // Tag form state
    var selectedCategoryId by remember { mutableStateOf(categories.firstOrNull()?.id ?: 0L) }
    var singleTagTerm by remember { mutableStateOf("") }

    val presetEmojis = listOf("⚡", "🪐", "🔬", "⛏️", "🧶", "🏹", "🌾", "📜", "🎻", "🤿")
    val presetColors = listOf("#06B6D4", "#8B5CF6", "#EC4899", "#10B981", "#F59E0B", "#3B82F6", "#EF4444")

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = "Cadastrar Ruído / Bolha Oposta",
                style = MaterialTheme.typography.titleMedium,
                color = TextPrimary
            )
        },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
            ) {
                TabRow(
                    selectedTabIndex = selectedTab,
                    containerColor = DarkSurface,
                    contentColor = CyberCyan,
                    modifier = Modifier.padding(bottom = 12.dp)
                ) {
                    Tab(
                        selected = selectedTab == 0,
                        onClick = { selectedTab = 0 },
                        text = { Text("Nova Categoria", fontSize = 13.sp) }
                    )
                    Tab(
                        selected = selectedTab == 1,
                        onClick = { selectedTab = 1 },
                        text = { Text("Adicionar Tag", fontSize = 13.sp) }
                    )
                }

                if (selectedTab == 0) {
                    // Category creation
                    OutlinedTextField(
                        value = catName,
                        onValueChange = { catName = it },
                        label = { Text("Nome da Categoria Oposta") },
                        placeholder = { Text("Ex: Teoria Musical Medieval") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = CyberCyan,
                            unfocusedBorderColor = DarkBorder
                        )
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedTextField(
                        value = catDesc,
                        onValueChange = { catDesc = it },
                        label = { Text("Breve Descrição") },
                        placeholder = { Text("Ex: Canto gregoriano e notação antiga") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = CyberCyan,
                            unfocusedBorderColor = DarkBorder
                        )
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Text("Escolha o Ícone / Emoji:", style = MaterialTheme.typography.labelMedium, color = TextSecondary)
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        presetEmojis.take(6).forEach { emoji ->
                            Box(
                                modifier = Modifier
                                    .size(36.dp)
                                    .clip(CircleShape)
                                    .background(if (catEmoji == emoji) CyberCyan.copy(alpha = 0.3f) else Color.Transparent)
                                    .border(
                                        width = if (catEmoji == emoji) 2.dp else 1.dp,
                                        color = if (catEmoji == emoji) CyberCyan else DarkBorder,
                                        shape = CircleShape
                                    )
                                    .clickable { catEmoji = emoji },
                                contentAlignment = Alignment.Center
                            ) {
                                Text(emoji, fontSize = 18.sp)
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Text("Cor de Destaque:", style = MaterialTheme.typography.labelMedium, color = TextSecondary)
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        presetColors.forEach { hex ->
                            val color = runCatching { Color(android.graphics.Color.parseColor(hex)) }.getOrDefault(CyberCyan)
                            Box(
                                modifier = Modifier
                                    .size(28.dp)
                                    .clip(CircleShape)
                                    .background(color)
                                    .border(
                                        width = if (catColorHex == hex) 2.5.dp else 0.dp,
                                        color = if (catColorHex == hex) Color.White else Color.Transparent,
                                        shape = CircleShape
                                    )
                                    .clickable { catColorHex = hex }
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    OutlinedTextField(
                        value = catInitialTags,
                        onValueChange = { catInitialTags = it },
                        label = { Text("Palavras-chave Iniciais (separadas por vírgula)") },
                        placeholder = { Text("neuma gregoriano, polifonia renascentista, solfejo antigo") },
                        modifier = Modifier.fillMaxWidth(),
                        minLines = 2,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = CyberCyan,
                            unfocusedBorderColor = DarkBorder
                        )
                    )
                } else {
                    // Tag addition to existing category
                    Text("Selecione a Categoria de Destino:", style = MaterialTheme.typography.labelMedium, color = TextSecondary)
                    Spacer(modifier = Modifier.height(6.dp))

                    Column(
                        verticalArrangement = Arrangement.spacedBy(6.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        categories.forEach { cat ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(if (selectedCategoryId == cat.id) CyberCyan.copy(alpha = 0.2f) else DarkSurface)
                                    .border(
                                        width = 1.dp,
                                        color = if (selectedCategoryId == cat.id) CyberCyan else DarkBorder,
                                        shape = RoundedCornerShape(8.dp)
                                    )
                                    .clickable { selectedCategoryId = cat.id }
                                    .padding(horizontal = 10.dp, vertical = 8.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(cat.iconEmoji, fontSize = 16.sp)
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(cat.name, style = MaterialTheme.typography.bodyMedium, color = TextPrimary)
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    OutlinedTextField(
                        value = singleTagTerm,
                        onValueChange = { singleTagTerm = it },
                        label = { Text("Termo ou Busca Incomum") },
                        placeholder = { Text("Ex: cálculo de torque correia trator") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = CyberCyan,
                            unfocusedBorderColor = DarkBorder
                        )
                    )
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (selectedTab == 0 && catName.isNotBlank()) {
                        val tags = catInitialTags.split(",")
                            .map { it.trim() }
                            .filter { it.isNotBlank() }
                        onAddCategory(catName.trim(), catDesc.trim(), catEmoji, catColorHex, tags)
                        onDismiss()
                    } else if (selectedTab == 1 && singleTagTerm.isNotBlank() && selectedCategoryId > 0) {
                        onAddTagToExisting(selectedCategoryId, singleTagTerm.trim())
                        onDismiss()
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = CyberCyan)
            ) {
                Text("Salvar", color = Color(0xFF032830))
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancelar", color = TextSecondary)
            }
        },
        containerColor = DarkSurface
    )
}

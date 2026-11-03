package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
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
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Tag
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.BubbleCategory
import com.example.data.model.BubbleKeyword
import com.example.data.model.CategoryWithKeywords
import com.example.ui.theme.CyberCyan
import com.example.ui.theme.DarkBorder
import com.example.ui.theme.DarkSurface
import com.example.ui.theme.DarkSurfaceVariant
import com.example.ui.theme.EmeraldGreen
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun CategoriesScreen(
    categoriesWithKeywords: List<CategoryWithKeywords>,
    onToggleCategory: (BubbleCategory) -> Unit,
    onToggleKeyword: (BubbleKeyword) -> Unit,
    onDeleteKeyword: (BubbleKeyword) -> Unit,
    onOpenCreateDialog: () -> Unit,
    onQuickAddTag: (categoryId: Long, term: String) -> Unit
) {
    var searchQuery by remember { mutableStateOf("") }
    val expandedStates = remember { mutableStateMapOf<Long, Boolean>() }

    val filteredList = remember(searchQuery, categoriesWithKeywords) {
        if (searchQuery.isBlank()) {
            categoriesWithKeywords
        } else {
            categoriesWithKeywords.filter { catWithKeys ->
                catWithKeys.category.name.contains(searchQuery, ignoreCase = true) ||
                        catWithKeys.category.description.contains(searchQuery, ignoreCase = true) ||
                        catWithKeys.keywords.any { it.term.contains(searchQuery, ignoreCase = true) }
            }
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp, vertical = 8.dp)
        ) {
            Text(
                text = "Bolhas Opostas & Palavras-Chave",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = TextPrimary
            )
            Text(
                text = "Clusters temáticos antagônicos projetados para gerar entropia no perfil de rastreamento.",
                style = MaterialTheme.typography.bodySmall,
                color = TextSecondary
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Search Bar
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("search_tags_input"),
                placeholder = { Text("Buscar termo ou categoria...") },
                leadingIcon = {
                    Icon(imageVector = Icons.Default.Search, contentDescription = null, tint = TextSecondary)
                },
                trailingIcon = {
                    if (searchQuery.isNotBlank()) {
                        IconButton(onClick = { searchQuery = "" }) {
                            Icon(imageVector = Icons.Default.Close, contentDescription = "Limpar busca", tint = TextSecondary)
                        }
                    }
                },
                singleLine = true,
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = CyberCyan,
                    unfocusedBorderColor = DarkBorder,
                    focusedContainerColor = DarkSurface,
                    unfocusedContainerColor = DarkSurface
                )
            )

            Spacer(modifier = Modifier.height(12.dp))

            if (filteredList.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 40.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Nenhuma bolha ou tag encontrada com '$searchQuery'",
                        color = TextSecondary,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(filteredList, key = { it.category.id }) { catWithKeys ->
                        val isExpanded = expandedStates[catWithKeys.category.id] ?: (filteredList.size <= 3)
                        val catColor = runCatching {
                            Color(android.graphics.Color.parseColor(catWithKeys.category.colorHex))
                        }.getOrDefault(CyberCyan)

                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            colors = CardDefaults.cardColors(containerColor = DarkSurface),
                            shape = RoundedCornerShape(16.dp),
                            border = androidx.compose.foundation.BorderStroke(1.dp, if (catWithKeys.category.isEnabled) catColor.copy(alpha = 0.4f) else DarkBorder)
                        ) {
                            Column(modifier = Modifier.padding(14.dp)) {
                                // Category Header Row
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    // Emoji Badge
                                    Box(
                                        modifier = Modifier
                                            .size(42.dp)
                                            .clip(CircleShape)
                                            .background(catColor.copy(alpha = 0.2f)),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(catWithKeys.category.iconEmoji, fontSize = 20.sp)
                                    }

                                    Spacer(modifier = Modifier.width(12.dp))

                                    Column(
                                        modifier = Modifier
                                            .weight(1f)
                                            .clickable {
                                                expandedStates[catWithKeys.category.id] = !isExpanded
                                            }
                                    ) {
                                        Text(
                                            text = catWithKeys.category.name,
                                            style = MaterialTheme.typography.titleMedium,
                                            fontWeight = FontWeight.Bold,
                                            color = if (catWithKeys.category.isEnabled) TextPrimary else TextMuted
                                        )
                                        Text(
                                            text = "${catWithKeys.keywords.count { it.isEnabled }} ativas / ${catWithKeys.keywords.size} tags",
                                            style = MaterialTheme.typography.labelSmall,
                                            color = catColor
                                        )
                                    }

                                    // Switch to toggle entire category
                                    Switch(
                                        checked = catWithKeys.category.isEnabled,
                                        onCheckedChange = { onToggleCategory(catWithKeys.category) },
                                        colors = SwitchDefaults.colors(
                                            checkedThumbColor = Color(0xFF032830),
                                            checkedTrackColor = catColor,
                                            uncheckedTrackColor = DarkSurfaceVariant
                                        )
                                    )

                                    IconButton(
                                        onClick = {
                                            expandedStates[catWithKeys.category.id] = !isExpanded
                                        }
                                    ) {
                                        Icon(
                                            imageVector = if (isExpanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                                            contentDescription = if (isExpanded) "Recolher" else "Expandir",
                                            tint = TextSecondary
                                        )
                                    }
                                }

                                Text(
                                    text = catWithKeys.category.description,
                                    style = MaterialTheme.typography.bodySmall,
                                    color = TextSecondary,
                                    modifier = Modifier.padding(top = 6.dp)
                                )

                                // Expanded Keywords List
                                AnimatedVisibility(visible = isExpanded) {
                                    Column(modifier = Modifier.padding(top = 12.dp)) {
                                        Box(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .height(1.dp)
                                                .background(DarkBorder)
                                        )
                                        Spacer(modifier = Modifier.height(10.dp))

                                        FlowRow(
                                            modifier = Modifier.fillMaxWidth(),
                                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                                            verticalArrangement = Arrangement.spacedBy(8.dp)
                                        ) {
                                            catWithKeys.keywords.forEach { keyword ->
                                                KeywordChip(
                                                    keyword = keyword,
                                                    accentColor = catColor,
                                                    onToggle = { onToggleKeyword(keyword) },
                                                    onDelete = { onDeleteKeyword(keyword) }
                                                )
                                            }
                                        }

                                        Spacer(modifier = Modifier.height(8.dp))

                                        // Quick inline add tag
                                        var quickTagInput by remember { mutableStateOf("") }
                                        var isAddingTag by remember { mutableStateOf(false) }

                                        if (isAddingTag) {
                                            Row(
                                                modifier = Modifier
                                                    .fillMaxWidth()
                                                    .padding(top = 6.dp),
                                                verticalAlignment = Alignment.CenterVertically
                                            ) {
                                                OutlinedTextField(
                                                    value = quickTagInput,
                                                    onValueChange = { quickTagInput = it },
                                                    placeholder = { Text("Novo termo oposto...", fontSize = 12.sp) },
                                                    singleLine = true,
                                                    modifier = Modifier.weight(1f),
                                                    shape = RoundedCornerShape(8.dp),
                                                    colors = OutlinedTextFieldDefaults.colors(
                                                        focusedBorderColor = CyberCyan,
                                                        unfocusedBorderColor = DarkBorder
                                                    )
                                                )
                                                Spacer(modifier = Modifier.width(6.dp))
                                                TextButton(
                                                    onClick = {
                                                        if (quickTagInput.isNotBlank()) {
                                                            onQuickAddTag(catWithKeys.category.id, quickTagInput.trim())
                                                            quickTagInput = ""
                                                            isAddingTag = false
                                                        }
                                                    }
                                                ) {
                                                    Text("Adicionar", color = CyberCyan)
                                                }
                                                IconButton(onClick = { isAddingTag = false }) {
                                                    Icon(Icons.Default.Close, contentDescription = "Fechar", tint = TextSecondary)
                                                }
                                            }
                                        } else {
                                            TextButton(
                                                onClick = { isAddingTag = true },
                                                modifier = Modifier.align(Alignment.End)
                                            ) {
                                                Icon(Icons.Default.Add, contentDescription = null, tint = catColor, modifier = Modifier.size(16.dp))
                                                Spacer(modifier = Modifier.width(4.dp))
                                                Text("+ Adicionar Tag", fontSize = 12.sp, color = catColor)
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }

                    item {
                        Spacer(modifier = Modifier.height(80.dp))
                    }
                }
            }
        }

        // Floating Action Button to add new bubble
        FloatingActionButton(
            onClick = onOpenCreateDialog,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(20.dp)
                .testTag("create_bubble_fab"),
            containerColor = CyberCyan,
            contentColor = Color(0xFF032830)
        ) {
            Icon(imageVector = Icons.Default.Add, contentDescription = "Nova Bolha Oposta")
        }
    }
}

@Composable
fun KeywordChip(
    keyword: BubbleKeyword,
    accentColor: Color,
    onToggle: () -> Unit,
    onDelete: () -> Unit
) {
    val chipBg = if (keyword.isEnabled) accentColor.copy(alpha = 0.15f) else DarkSurfaceVariant
    val chipBorder = if (keyword.isEnabled) accentColor.copy(alpha = 0.6f) else DarkBorder
    val chipTextColor = if (keyword.isEnabled) TextPrimary else TextMuted

    Row(
        modifier = Modifier
            .clip(RoundedCornerShape(20.dp))
            .background(chipBg)
            .border(1.dp, chipBorder, RoundedCornerShape(20.dp))
            .clickable { onToggle() }
            .padding(start = 10.dp, end = 6.dp, top = 5.dp, bottom = 5.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = Icons.Default.Tag,
            contentDescription = null,
            tint = if (keyword.isEnabled) accentColor else TextMuted,
            modifier = Modifier.size(14.dp)
        )
        Spacer(modifier = Modifier.width(4.dp))
        Text(
            text = keyword.term,
            style = MaterialTheme.typography.bodySmall,
            color = chipTextColor,
            fontSize = 12.sp
        )
        if (keyword.usageCount > 0) {
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = "${keyword.usageCount}x",
                style = MaterialTheme.typography.labelSmall,
                color = accentColor,
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold
            )
        }
        Spacer(modifier = Modifier.width(2.dp))
        IconButton(
            onClick = onDelete,
            modifier = Modifier.size(20.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Close,
                contentDescription = "Remover tag",
                tint = TextMuted,
                modifier = Modifier.size(12.dp)
            )
        }
    }
}

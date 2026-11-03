package com.example.ui

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BubbleChart
import androidx.compose.material.icons.filled.Dashboard
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.Explore
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.ui.components.NewBubbleDialog
import com.example.ui.screens.ArchitectureFaqScreen
import com.example.ui.screens.BreakBubbleScreen
import com.example.ui.screens.CategoriesScreen
import com.example.ui.screens.DashboardScreen
import com.example.ui.screens.HistoryScreen
import com.example.ui.screens.PrivacyScreen
import com.example.ui.theme.CyberCyan
import com.example.ui.theme.DarkBorder
import com.example.ui.theme.DarkSurface
import com.example.ui.theme.EmeraldGreen
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary

sealed class Screen(val index: Int, val title: String, val icon: ImageVector, val tag: String) {
    object Dashboard : Screen(0, "Painel", Icons.Default.Dashboard, "nav_dashboard")
    object Categories : Screen(1, "Bolhas", Icons.Default.BubbleChart, "nav_categories")
    object History : Screen(2, "Histórico", Icons.Default.History, "nav_history")
    object Architecture : Screen(3, "Estratégia", Icons.Default.MenuBook, "nav_strategy")
    object BreakBubble : Screen(4, "Sair", Icons.Default.Explore, "nav_break_bubble")
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AntiBolhaApp(viewModel: AntiBolhaViewModel) {
    val context = LocalContext.current
    var currentTabIndex by rememberSaveable { mutableIntStateOf(0) }
    var showCreateDialog by remember { mutableStateOf(false) }

    // Request notification permission on Android 13+
    val notificationPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { /* Permission result handled */ }

    LaunchedEffect(Unit) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            val hasPermission = ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED
            if (!hasPermission) {
                notificationPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
        }
    }

    // Collect states
    val categoriesWithKeywords by viewModel.categoriesWithKeywords.collectAsStateWithLifecycle()
    val recentLogs by viewModel.recentLogs.collectAsStateWithLifecycle()
    val totalNoiseCount by viewModel.totalNoiseCount.collectAsStateWithLifecycle()
    val isServiceRunning by viewModel.isServiceRunning.collectAsStateWithLifecycle()
    val intervalMinutes by viewModel.currentIntervalMinutes.collectAsStateWithLifecycle()
    val isBurstRunning by viewModel.isBurstRunning.collectAsStateWithLifecycle()
    val lastBurstMessage by viewModel.lastBurstMessage.collectAsStateWithLifecycle()
    val burstToday by viewModel.burstToday.collectAsStateWithLifecycle()
    val burstGoal by viewModel.burstGoal.collectAsStateWithLifecycle()
    val lastTopic by viewModel.lastTopic.collectAsStateWithLifecycle()
    val lastTopicCategory by viewModel.lastTopicCategory.collectAsStateWithLifecycle()
    val breakMessage by viewModel.breakMessage.collectAsStateWithLifecycle()
    val noiseEngineEnabled by viewModel.noiseEngineEnabled.collectAsStateWithLifecycle()

    // BackHandler: if not in Dashboard, go back to Dashboard
    BackHandler(enabled = currentTabIndex != 0) {
        currentTabIndex = 0
    }

    val screens = listOf(
        Screen.Dashboard,
        Screen.Categories,
        Screen.History,
        Screen.Architecture,
        Screen.BreakBubble
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    androidx.compose.foundation.layout.Row(
                        verticalAlignment = androidx.compose.ui.Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Shield,
                            contentDescription = null,
                            tint = if (isServiceRunning) EmeraldGreen else CyberCyan,
                            modifier = Modifier.size(24.dp)
                        )
                        androidx.compose.foundation.layout.Spacer(modifier = Modifier.padding(horizontal = 4.dp))
                        Text(
                            text = "AntiBolha",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary
                        )
                        androidx.compose.foundation.layout.Spacer(modifier = Modifier.padding(horizontal = 4.dp))
                        Text(
                            text = "v1.0",
                            style = MaterialTheme.typography.labelSmall,
                            color = TextMuted,
                            fontSize = 10.sp
                        )
                    }
                },
                actions = {
                    IconButton(
                        onClick = { currentTabIndex = 5 },
                        modifier = Modifier.testTag("btn_privacy")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Lock,
                            contentDescription = "Privacidade",
                            tint = if (currentTabIndex == 5) CyberCyan else TextSecondary
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = DarkSurface,
                    titleContentColor = TextPrimary
                )
            )
        },
        bottomBar = {
            NavigationBar(
                containerColor = DarkSurface,
                tonalElevation = 8.dp
            ) {
                screens.forEach { screen ->
                    val isSelected = currentTabIndex == screen.index
                    NavigationBarItem(
                        selected = isSelected,
                        onClick = { currentTabIndex = screen.index },
                        icon = {
                            Icon(
                                imageVector = screen.icon,
                                contentDescription = screen.title
                            )
                        },
                        label = { Text(screen.title, fontSize = 11.sp) },
                        modifier = Modifier.testTag(screen.tag),
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = Color(0xFF032830),
                            selectedTextColor = CyberCyan,
                            indicatorColor = CyberCyan,
                            unselectedIconColor = TextSecondary,
                            unselectedTextColor = TextSecondary
                        )
                    )
                }
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(MaterialTheme.colorScheme.background)
        ) {
            when (currentTabIndex) {
                0 -> DashboardScreen(
                    isServiceRunning = isServiceRunning,
                    intervalMinutes = intervalMinutes,
                    totalNoiseCount = totalNoiseCount,
                    categories = categoriesWithKeywords,
                    recentLogs = recentLogs,
                    isBurstRunning = isBurstRunning,
                    lastBurstMessage = lastBurstMessage,
                    onToggleService = { ctx -> viewModel.toggleService(ctx) },
                    onUpdateInterval = { ctx, mins -> viewModel.updateInterval(ctx, mins) },
                    onTriggerBurst = { count -> viewModel.triggerBurstNoise(count) },
                    onNavigateToHistory = { currentTabIndex = 2 },
                    noiseEngineEnabled = noiseEngineEnabled,
                    onGoToBreakBubble = { currentTabIndex = 4 },
                    onOpenAdvanced = { currentTabIndex = 5 }
                )
                1 -> CategoriesScreen(
                    categoriesWithKeywords = categoriesWithKeywords,
                    onToggleCategory = { cat -> viewModel.toggleCategory(cat) },
                    onToggleKeyword = { kw -> viewModel.toggleKeyword(kw) },
                    onDeleteKeyword = { kw -> viewModel.deleteKeyword(kw) },
                    onOpenCreateDialog = { showCreateDialog = true },
                    onQuickAddTag = { catId, term -> viewModel.addKeyword(catId, term) }
                )
                2 -> HistoryScreen(
                    logs = recentLogs,
                    onClearLogs = { viewModel.clearHistory() }
                )
                3 -> ArchitectureFaqScreen()
                4 -> BreakBubbleScreen(
                    countToday = burstToday,
                    goal = burstGoal,
                    lastTopic = lastTopic,
                    lastCategory = lastTopicCategory,
                    message = breakMessage,
                    onBreakBubble = { platform -> viewModel.breakBubble(context, platform) },
                    onGoalChange = { viewModel.setBurstGoal(it) }
                )
                5 -> PrivacyScreen(
                    noiseEngineEnabled = noiseEngineEnabled,
                    onNoiseEngineChange = { enabled ->
                        viewModel.setNoiseEngineEnabled(context, enabled)
                    }
                )
            }
        }
    }

    if (showCreateDialog) {
        val categoriesOnly = categoriesWithKeywords.map { it.category }
        NewBubbleDialog(
            categories = categoriesOnly,
            onDismiss = { showCreateDialog = false },
            onAddCategory = { name, desc, emoji, colorHex, tags ->
                viewModel.addCategory(name, desc, emoji, colorHex, tags)
            },
            onAddTagToExisting = { catId, tag ->
                viewModel.addKeyword(catId, tag)
            }
        )
    }
}

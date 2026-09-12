package com.example

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Dashboard
import androidx.compose.material.icons.filled.Folder
import androidx.compose.material.icons.filled.SmartToy
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ui.LegalViewModel
import com.example.ui.screens.AiAssistantScreen
import com.example.ui.screens.CaseListScreen
import com.example.ui.screens.DashboardScreen
import com.example.ui.screens.ToolsScreen
import com.example.ui.theme.LuxuryGold
import com.example.ui.theme.LuxuryNavy
import com.example.ui.theme.MyApplicationTheme
import com.example.ui.theme.Slate100

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
                    LegalApp()
                }
            }
        }
    }
}

@Composable
fun LegalApp(viewModel: LegalViewModel = viewModel()) {
    val context = LocalContext.current
    var selectedScreen by remember { mutableIntStateOf(0) }

    val activeDeadlinesCount by viewModel.activeDeadlinesCount.collectAsStateWithLifecycle()
    val allEblaghs by viewModel.eblaghs.collectAsStateWithLifecycle()
    val pendingEblaghsCount = allEblaghs.count { !it.isProcessed }
    val chatMessages by viewModel.chatMessages.collectAsStateWithLifecycle()
    val deadlines by viewModel.deadlines.collectAsStateWithLifecycle()
    val criticalCount = deadlines.count { it.daysRemaining <= 3 && !it.isCompleted }

    val toastMsg by viewModel.toastMessage.collectAsStateWithLifecycle()
    LaunchedEffect(toastMsg) {
        toastMsg?.let { msg ->
            Toast.makeText(context, msg, Toast.LENGTH_LONG).show()
            viewModel.clearToast()
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            Card(
                modifier = Modifier
                    .navigationBarsPadding()
                    .shadow(20.dp, RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp)),
                shape = RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                border = BorderStroke(1.dp, Slate100),
                elevation = CardDefaults.cardElevation(20.dp)
            ) {
                NavigationBar(
                    modifier = Modifier.testTag("bottom_nav_bar"),
                    containerColor = Color.Transparent,
                    tonalElevation = 0.dp
                ) {
                    // 0: داشبورد - هوشمند و منظم
                    NavigationBarItem(
                        selected = selectedScreen == 0,
                        onClick = { selectedScreen = 0 },
                        icon = {
                            BadgedBox(badge = { if (criticalCount > 0) Badge(containerColor = Color(0xFFE11D48), contentColor = Color.White) { Text(criticalCount.toString(), fontSize = 10.sp, fontWeight = FontWeight.Bold) } }) {
                                Icon(Icons.Default.Dashboard, contentDescription = "داشبورد")
                            }
                        },
                        label = { Text("داشبورد", fontSize = 10.sp, fontWeight = if (selectedScreen == 0) FontWeight.Bold else FontWeight.Normal) },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = Color.White,
                            selectedTextColor = MaterialTheme.colorScheme.primary,
                            indicatorColor = MaterialTheme.colorScheme.primary,
                            unselectedIconColor = Color(0xFF94A3B8),
                            unselectedTextColor = Color(0xFF94A3B8)
                        ),
                        modifier = Modifier.testTag("nav_item_dashboard")
                    )

                    // 1: پرونده‌ها - با چک‌لیست
                    NavigationBarItem(
                        selected = selectedScreen == 1,
                        onClick = { selectedScreen = 1 },
                        icon = { Icon(Icons.Default.Folder, contentDescription = "پرونده‌ها") },
                        label = { Text("پرونده‌ها", fontSize = 10.sp, fontWeight = if (selectedScreen == 1) FontWeight.Bold else FontWeight.Normal) },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = Color.White,
                            selectedTextColor = MaterialTheme.colorScheme.primary,
                            indicatorColor = MaterialTheme.colorScheme.primary,
                            unselectedIconColor = Color(0xFF94A3B8),
                            unselectedTextColor = Color(0xFF94A3B8)
                        ),
                        modifier = Modifier.testTag("nav_item_cases")
                    )

                    // 2: میلانو AI - مرکز طلایی لاکچری
                    NavigationBarItem(
                        selected = selectedScreen == 2,
                        onClick = { selectedScreen = 2 },
                        icon = {
                            BadgedBox(badge = {
                                if (chatMessages.isNotEmpty()) Badge(containerColor = LuxuryGold, contentColor = LuxuryNavy) {
                                    Text("AI", fontSize = 8.sp, fontWeight = FontWeight.ExtraBold)
                                }
                            }) {
                                Icon(Icons.Default.SmartToy, contentDescription = "هوش مصنوعی")
                            }
                        },
                        label = {
                            Text("میلانو AI", fontSize = 10.sp, fontWeight = FontWeight.ExtraBold, color = if (selectedScreen == 2) LuxuryGold else Color(0xFF94A3B8))
                        },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = LuxuryNavy,
                            selectedTextColor = LuxuryGold,
                            indicatorColor = LuxuryGold,
                            unselectedIconColor = Color(0xFF94A3B8),
                            unselectedTextColor = Color(0xFF94A3B8)
                        ),
                        modifier = Modifier.testTag("nav_item_ai")
                    )

                    // 3: ابزارها - دسته‌بندی هوشمند (قضایی، هوشمند، سیستم)
                    NavigationBarItem(
                        selected = selectedScreen == 3,
                        onClick = { selectedScreen = 3 },
                        icon = {
                            BadgedBox(badge = {
                                if (pendingEblaghsCount > 0) Badge(containerColor = LuxuryGold, contentColor = LuxuryNavy) {
                                    Text(pendingEblaghsCount.toString(), fontSize = 10.sp, fontWeight = FontWeight.Bold)
                                }
                            }) {
                                Icon(Icons.Default.Build, contentDescription = "ابزارها")
                            }
                        },
                        label = { Text("ابزار لاکچری", fontSize = 10.sp, fontWeight = if (selectedScreen == 3) FontWeight.Bold else FontWeight.Normal) },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = Color.White,
                            selectedTextColor = MaterialTheme.colorScheme.primary,
                            indicatorColor = MaterialTheme.colorScheme.primary,
                            unselectedIconColor = Color(0xFF94A3B8),
                            unselectedTextColor = Color(0xFF94A3B8)
                        ),
                        modifier = Modifier.testTag("nav_item_tools")
                    )
                }
            }
        }
    ) { innerPadding ->
        Box(modifier = Modifier.fillMaxSize().padding(innerPadding)) {
            AnimatedContent(
                targetState = selectedScreen,
                transitionSpec = { fadeIn(animationSpec = tween(300)) togetherWith fadeOut(animationSpec = tween(300)) },
                label = "ScreenTransition"
            ) { target ->
                when (target) {
                    0 -> DashboardScreen(
                        viewModel = viewModel,
                        onNavigateToCases = { selectedScreen = 1 },
                        onNavigateToAi = { selectedScreen = 2 }
                    )
                    1 -> CaseListScreen(viewModel = viewModel)
                    2 -> AiAssistantScreen(viewModel = viewModel)
                    3 -> ToolsScreen(viewModel = viewModel)
                }
            }
        }
    }
}

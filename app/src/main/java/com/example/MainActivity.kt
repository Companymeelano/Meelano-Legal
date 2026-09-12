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
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Alarm
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.EditNote
import androidx.compose.material.icons.filled.Folder
import androidx.compose.material.icons.filled.Notifications
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
import androidx.compose.ui.graphics.Brush
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
import com.example.ui.screens.DeadlinesScreen
import com.example.ui.screens.DraftingStudioScreen
import com.example.ui.screens.EblaghScreen
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
                    .shadow(16.dp, RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp)),
                shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                border = BorderStroke(1.dp, Slate100),
                elevation = CardDefaults.cardElevation(16.dp)
            ) {
                NavigationBar(
                    modifier = Modifier.testTag("bottom_nav_bar"),
                    containerColor = Color.Transparent,
                    tonalElevation = 0.dp
                ) {
                    NavigationBarItem(
                        selected = selectedScreen == 0,
                        onClick = { selectedScreen = 0 },
                        icon = {
                            Icon(Icons.Default.Folder, contentDescription = "پرونده‌ها")
                        },
                        label = { Text("پرونده‌ها", fontSize = 10.sp, fontWeight = if (selectedScreen == 0) FontWeight.Bold else FontWeight.Normal) },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = Color.White,
                            selectedTextColor = MaterialTheme.colorScheme.primary,
                            indicatorColor = MaterialTheme.colorScheme.primary,
                            unselectedIconColor = Color(0xFF94A3B8),
                            unselectedTextColor = Color(0xFF94A3B8)
                        ),
                        modifier = Modifier.testTag("nav_item_cases")
                    )

                    NavigationBarItem(
                        selected = selectedScreen == 1,
                        onClick = { selectedScreen = 1 },
                        icon = {
                            if (activeDeadlinesCount > 0) {
                                BadgedBox(
                                    badge = {
                                        Badge(
                                            containerColor = Color(0xFFE11D48),
                                            contentColor = Color.White
                                        ) {
                                            Text(activeDeadlinesCount.toString(), fontSize = 10.sp, fontWeight = FontWeight.Bold)
                                        }
                                    }
                                ) {
                                    Icon(Icons.Default.Alarm, contentDescription = "مواعد قضایی")
                                }
                            } else {
                                Icon(Icons.Default.Alarm, contentDescription = "مواعد قضایی")
                            }
                        },
                        label = { Text("مواعد", fontSize = 10.sp, fontWeight = if (selectedScreen == 1) FontWeight.Bold else FontWeight.Normal) },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = Color.White,
                            selectedTextColor = MaterialTheme.colorScheme.primary,
                            indicatorColor = MaterialTheme.colorScheme.primary,
                            unselectedIconColor = Color(0xFF94A3B8),
                            unselectedTextColor = Color(0xFF94A3B8)
                        ),
                        modifier = Modifier.testTag("nav_item_deadlines")
                    )

                    // LUXURY AI CENTER BUTTON
                    NavigationBarItem(
                        selected = selectedScreen == 4,
                        onClick = { selectedScreen = 4 },
                        icon = {
                            BadgedBox(
                                badge = {
                                    if (chatMessages.isNotEmpty()) {
                                        Badge(
                                            containerColor = LuxuryGold,
                                            contentColor = LuxuryNavy
                                        ) {
                                            Text("AI", fontSize = 8.sp, fontWeight = FontWeight.ExtraBold)
                                        }
                                    }
                                }
                            ) {
                                Icon(
                                    Icons.Default.SmartToy,
                                    contentDescription = "هوش مصنوعی",
                                    modifier = Modifier
                                )
                            }
                        },
                        label = { 
                            Text(
                                "میلانو AI", 
                                fontSize = 10.sp, 
                                fontWeight = FontWeight.ExtraBold,
                                color = if (selectedScreen == 4) LuxuryGold else Color(0xFF94A3B8)
                            ) 
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

                    NavigationBarItem(
                        selected = selectedScreen == 2,
                        onClick = { selectedScreen = 2 },
                        icon = {
                            if (pendingEblaghsCount > 0) {
                                BadgedBox(
                                    badge = {
                                        Badge(
                                            containerColor = LuxuryGold,
                                            contentColor = LuxuryNavy
                                        ) {
                                            Text(pendingEblaghsCount.toString(), fontSize = 10.sp, fontWeight = FontWeight.Bold)
                                        }
                                    }
                                ) {
                                    Icon(Icons.Default.Notifications, contentDescription = "ابلاغیه ثنا")
                                }
                            } else {
                                Icon(Icons.Default.Notifications, contentDescription = "ابلاغیه ثنا")
                            }
                        },
                        label = { Text("ثنا", fontSize = 10.sp, fontWeight = if (selectedScreen == 2) FontWeight.Bold else FontWeight.Normal) },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = Color.White,
                            selectedTextColor = MaterialTheme.colorScheme.primary,
                            indicatorColor = MaterialTheme.colorScheme.primary,
                            unselectedIconColor = Color(0xFF94A3B8),
                            unselectedTextColor = Color(0xFF94A3B8)
                        ),
                        modifier = Modifier.testTag("nav_item_eblagh")
                    )

                    NavigationBarItem(
                        selected = selectedScreen == 3,
                        onClick = { selectedScreen = 3 },
                        icon = {
                            Icon(Icons.Default.EditNote, contentDescription = "تنظیم لایحه")
                        },
                        label = { Text("لوایح", fontSize = 10.sp, fontWeight = if (selectedScreen == 3) FontWeight.Bold else FontWeight.Normal) },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = Color.White,
                            selectedTextColor = MaterialTheme.colorScheme.primary,
                            indicatorColor = MaterialTheme.colorScheme.primary,
                            unselectedIconColor = Color(0xFF94A3B8),
                            unselectedTextColor = Color(0xFF94A3B8)
                        ),
                        modifier = Modifier.testTag("nav_item_drafts")
                    )
                }
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            AnimatedContent(
                targetState = selectedScreen,
                transitionSpec = { 
                    fadeIn(animationSpec = tween(300)) togetherWith fadeOut(animationSpec = tween(300)) 
                },
                label = "ScreenTransition"
            ) { target ->
                when (target) {
                    0 -> CaseListScreen(viewModel = viewModel)
                    1 -> DeadlinesScreen(viewModel = viewModel)
                    2 -> EblaghScreen(viewModel = viewModel)
                    3 -> DraftingStudioScreen(viewModel = viewModel)
                    4 -> AiAssistantScreen(viewModel = viewModel)
                }
            }
        }
    }
}

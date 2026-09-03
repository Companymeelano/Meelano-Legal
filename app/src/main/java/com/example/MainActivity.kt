package com.example

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Alarm
import androidx.compose.material.icons.filled.EditNote
import androidx.compose.material.icons.filled.Folder
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ui.LegalViewModel
import com.example.ui.screens.CaseListScreen
import com.example.ui.screens.DeadlinesScreen
import com.example.ui.screens.DraftingStudioScreen
import com.example.ui.screens.EblaghScreen
import com.example.ui.theme.GoldPrimary
import com.example.ui.theme.MyApplicationTheme
import com.example.ui.theme.NavyPrimary

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

    val toastMsg by viewModel.toastMessage.collectAsStateWithLifecycle()
    LaunchedEffect(toastMsg) {
        toastMsg?.let { msg ->
            Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
            viewModel.clearToast()
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            NavigationBar(
                modifier = Modifier
                    .navigationBarsPadding()
                    .testTag("bottom_nav_bar"),
                containerColor = MaterialTheme.colorScheme.surface,
                tonalElevation = 8.dp
            ) {
                NavigationBarItem(
                    selected = selectedScreen == 0,
                    onClick = { selectedScreen = 0 },
                    icon = {
                        Icon(Icons.Default.Folder, contentDescription = "پرونده‌ها")
                    },
                    label = { Text("پرونده‌ها", fontSize = 11.sp) },
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
                                        containerColor = MaterialTheme.colorScheme.error,
                                        contentColor = MaterialTheme.colorScheme.onError
                                    ) {
                                        Text(activeDeadlinesCount.toString())
                                    }
                                }
                            ) {
                                Icon(Icons.Default.Alarm, contentDescription = "مواعد قضایی")
                            }
                        } else {
                            Icon(Icons.Default.Alarm, contentDescription = "مواعد قضایی")
                        }
                    },
                    label = { Text("مواعد قضایی", fontSize = 11.sp) },
                    modifier = Modifier.testTag("nav_item_deadlines")
                )

                NavigationBarItem(
                    selected = selectedScreen == 2,
                    onClick = { selectedScreen = 2 },
                    icon = {
                        if (pendingEblaghsCount > 0) {
                            BadgedBox(
                                badge = {
                                    Badge(
                                        containerColor = GoldPrimary,
                                        contentColor = NavyPrimary
                                    ) {
                                        Text(pendingEblaghsCount.toString())
                                    }
                                }
                            ) {
                                Icon(Icons.Default.Notifications, contentDescription = "ابلاغیه ثنا")
                            }
                        } else {
                            Icon(Icons.Default.Notifications, contentDescription = "ابلاغیه ثنا")
                        }
                    },
                    label = { Text("ابلاغیه ثنا", fontSize = 11.sp) },
                    modifier = Modifier.testTag("nav_item_eblagh")
                )

                NavigationBarItem(
                    selected = selectedScreen == 3,
                    onClick = { selectedScreen = 3 },
                    icon = {
                        Icon(Icons.Default.EditNote, contentDescription = "تنظیم لایحه")
                    },
                    label = { Text("تنظیم لوایح", fontSize = 11.sp) },
                    modifier = Modifier.testTag("nav_item_drafts")
                )
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
                transitionSpec = { fadeIn() togetherWith fadeOut() },
                label = "ScreenTransition"
            ) { target ->
                when (target) {
                    0 -> CaseListScreen(viewModel = viewModel)
                    1 -> DeadlinesScreen(viewModel = viewModel)
                    2 -> EblaghScreen(viewModel = viewModel)
                    3 -> DraftingStudioScreen(viewModel = viewModel)
                }
            }
        }
    }
}


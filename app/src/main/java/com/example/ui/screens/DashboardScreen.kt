package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.data.model.LegalCase
import com.example.data.service.PredictionService
import com.example.ui.LegalViewModel
import com.example.ui.theme.*
import kotlinx.coroutines.launch

private val Bg = LuxuryGreenDeep
private val CardBg = LuxuryGreenDark
private val CardElev = LuxuryGreenMedium
private val Gold = LuxuryGreenGold
private val GoldBrush = LuxuryGreenGoldBrush
private val Border = LuxuryGreenBorder
private val BorderStrong = LuxuryGreenBorderStrong

@Composable
fun DashboardScreen(
    viewModel: LegalViewModel,
    onNavigateToCases: () -> Unit,
    onNavigateToAi: () -> Unit,
    modifier: Modifier = Modifier
) {
    val cases by viewModel.filteredCases.collectAsStateWithLifecycle()
    val deadlines by viewModel.deadlines.collectAsStateWithLifecycle()
    val eblaghs by viewModel.eblaghs.collectAsStateWithLifecycle()
    val chatMessages by viewModel.chatMessages.collectAsStateWithLifecycle()

    var selectedCaseForPrediction by remember { mutableStateOf<LegalCase?>(null) }
    var predictionResult by remember { mutableStateOf<com.example.data.service.PredictionResult?>(null) }
    val coroutineScope = rememberCoroutineScope()

    LazyColumn(
        modifier = modifier.fillMaxSize().background(Bg),
        contentPadding = PaddingValues(bottom = 100.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // هدر مینیمال یکدست - مثل وب
        item {
            Box(modifier = Modifier.fillMaxWidth().background(Bg).padding(20.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(modifier = Modifier.size(48.dp).clip(RoundedCornerShape(12.dp)).background(GoldBrush), contentAlignment = Alignment.Center) {
                        Text("M", fontSize = 24.sp, fontWeight = FontWeight.ExtraBold, color = Bg)
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text("داشبورد", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color.White)
                        Text("تم یکدست سبز طلایی • الهام از وب", fontSize = 10.sp, color = Color.White.copy(alpha = 0.6f))
                    }
                }
            }
        }

        // 4 آمار - یکدست
        item {
            Row(modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                StatUniform("پرونده", cases.size.toString(), Icons.Default.Folder, Modifier.weight(1f))
                StatUniform("بحرانی", deadlines.count { it.daysRemaining <= 3 }.toString(), Icons.Default.Warning, Modifier.weight(1f))
                StatUniform("ثنا", eblaghs.count { !it.isProcessed }.toString(), Icons.Default.NotificationsActive, Modifier.weight(1f))
                StatUniform("AI", chatMessages.size.toString(), Icons.Default.SmartToy, Modifier.weight(1f))
            }
        }

        // نمودار - تک‌رنگ طلایی
        item {
            Card(modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp), shape = RoundedCornerShape(16.dp), colors = CardDefaults.cardColors(containerColor = CardBg), border = BorderStroke(1.dp, Border)) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(modifier = Modifier.size(32.dp).clip(RoundedCornerShape(8.dp)).background(GoldBrush), contentAlignment = Alignment.Center) {
                            Icon(Icons.Default.BarChart, contentDescription = null, tint = Bg, modifier = Modifier.size(16.dp))
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("نمودار پرونده‌ها", fontWeight = FontWeight.Bold, fontSize = 13.sp, color = Color.White)
                    }
                    Spacer(modifier = Modifier.height(14.dp))
                    Row(modifier = Modifier.fillMaxWidth().height(70.dp), horizontalArrangement = Arrangement.SpaceEvenly, verticalAlignment = Alignment.Bottom) {
                        BarUniform("حقوقی", 0.8f)
                        BarUniform("کیفری", 0.5f)
                        BarUniform("خانواده", 0.6f)
                        BarUniform("ملکی", 0.9f)
                        BarUniform("ثبتی", 0.4f)
                    }
                }
            }
        }

        // پیش‌بینی - یکدست
        item {
            Card(modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp), shape = RoundedCornerShape(16.dp), colors = CardDefaults.cardColors(containerColor = CardBg), border = BorderStroke(1.dp, BorderStrong)) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(modifier = Modifier.size(32.dp).clip(RoundedCornerShape(8.dp)).background(GoldBrush), contentAlignment = Alignment.Center) {
                            Icon(Icons.Default.AutoAwesome, contentDescription = null, tint = Bg, modifier = Modifier.size(16.dp))
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("پیش‌بینی رای", fontWeight = FontWeight.Bold, fontSize = 13.sp, color = Color.White)
                    }
                    Spacer(modifier = Modifier.height(10.dp))
                    if (cases.isNotEmpty()) {
                        Button(
                            onClick = {
                                selectedCaseForPrediction = cases.firstOrNull()
                                coroutineScope.launch {
                                    cases.firstOrNull()?.let { predictionResult = PredictionService.predictCaseOutcome(it) }
                                }
                            },
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = CardElev, contentColor = Gold),
                            border = BorderStroke(1.dp, Border),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text("پیش‌بینی پرونده اول", fontSize = 11.sp)
                        }
                        predictionResult?.let { result ->
                            Spacer(modifier = Modifier.height(10.dp))
                            Card(shape = RoundedCornerShape(12.dp), colors = CardDefaults.cardColors(containerColor = CardElev), border = BorderStroke(1.dp, Border)) {
                                Column(modifier = Modifier.padding(12.dp)) {
                                    Text("موفقیت: ${result.successProbability}٪", fontWeight = FontWeight.Bold, color = Gold, fontSize = 13.sp)
                                    Text(result.predictedOutcome, fontSize = 10.sp, color = Color.White.copy(alpha = 0.7f))
                                }
                            }
                        }
                    }
                }
            }
        }

        // مواعد - یکدست
        item {
            Card(modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp), shape = RoundedCornerShape(16.dp), colors = CardDefaults.cardColors(containerColor = CardBg), border = BorderStroke(1.dp, Border)) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("مواعد بحرانی", fontWeight = FontWeight.Bold, fontSize = 13.sp, color = Color.White)
                    Spacer(modifier = Modifier.height(8.dp))
                    val critical = deadlines.filter { it.daysRemaining <= 7 && !it.isCompleted }.take(2)
                    if (critical.isEmpty()) {
                        Text("هیچ موعد بحرانی ندارید", fontSize = 10.sp, color = Color.White.copy(alpha = 0.5f))
                    } else {
                        critical.forEach { d ->
                            Text("• ${d.title} - ${d.daysRemaining} روز", fontSize = 10.sp, color = Color.White.copy(alpha = 0.7f), modifier = Modifier.padding(vertical = 2.dp))
                        }
                    }
                }
            }
        }

        // دسترسی سریع - 3 دکمه یکدست
        item {
            Row(modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                QuickUniform("اسکنر", Icons.Default.CameraAlt, Modifier.weight(1f)) { onNavigateToCases() }
                QuickUniform("AI", Icons.Default.SmartToy, Modifier.weight(1f)) { onNavigateToAi() }
                QuickUniform("PDF", Icons.Default.PictureAsPdf, Modifier.weight(1f)) { onNavigateToCases() }
            }
        }
    }
}

@Composable
fun StatUniform(label: String, value: String, icon: ImageVector, modifier: Modifier = Modifier) {
    Card(modifier = modifier, shape = RoundedCornerShape(12.dp), colors = CardDefaults.cardColors(containerColor = CardBg), border = BorderStroke(1.dp, Border)) {
        Column(modifier = Modifier.padding(12.dp), horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(icon, contentDescription = null, tint = Gold, modifier = Modifier.size(18.dp))
            Spacer(modifier = Modifier.height(4.dp))
            Text(value, fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color.White)
            Text(label, fontSize = 8.sp, color = Color.White.copy(alpha = 0.5f))
        }
    }
}

@Composable
fun BarUniform(label: String, ratio: Float) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Box(modifier = Modifier.width(24.dp).height((ratio * 50).dp).clip(RoundedCornerShape(6.dp)).background(GoldBrush))
        Spacer(modifier = Modifier.height(6.dp))
        Text(label, fontSize = 8.sp, color = Color.White.copy(alpha = 0.5f))
    }
}

@Composable
fun QuickUniform(title: String, icon: ImageVector, modifier: Modifier = Modifier, onClick: () -> Unit) {
    Card(modifier = modifier.clickable { onClick() }, shape = RoundedCornerShape(12.dp), colors = CardDefaults.cardColors(containerColor = CardBg), border = BorderStroke(1.dp, Border)) {
        Column(modifier = Modifier.padding(12.dp), horizontalAlignment = Alignment.CenterHorizontally) {
            Box(modifier = Modifier.size(36.dp).clip(RoundedCornerShape(10.dp)).background(GoldBrush), contentAlignment = Alignment.Center) {
                Icon(icon, contentDescription = null, tint = Bg, modifier = Modifier.size(18.dp))
            }
            Spacer(modifier = Modifier.height(6.dp))
            Text(title, fontSize = 10.sp, fontWeight = FontWeight.Bold, color = Color.White)
        }
    }
}

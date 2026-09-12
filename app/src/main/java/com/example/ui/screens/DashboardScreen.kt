package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
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

// تم یکدست: فقط سرمه‌ای تیره + طلایی
private val Bg = LuxuryNavyDeep
private val CardBg = LuxuryDarkCard
private val CardElevated = LuxuryDarkCardElevated
private val Gold = LuxuryGold
private val GoldBrush = LuxuryGoldDarkBrush
private val Border = Gold.copy(alpha = 0.18f)
private val BorderStrong = Gold.copy(alpha = 0.35f)

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
        // هدر مینیمال تک‌رنگ
        item {
            Box(modifier = Modifier.fillMaxWidth().background(Bg).padding(24.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(modifier = Modifier.size(56.dp).clip(RoundedCornerShape(14.dp)).background(GoldBrush), contentAlignment = Alignment.Center) {
                        Text("M", fontSize = 28.sp, fontWeight = FontWeight.ExtraBold, color = Bg)
                    }
                    Spacer(modifier = Modifier.width(14.dp))
                    Column {
                        Text("داشبورد", fontSize = 20.sp, fontWeight = FontWeight.ExtraBold, color = Color.White)
                        Text("تم یکدست تیره طلایی • بدون شلوغی", fontSize = 11.sp, color = Color.White.copy(alpha = 0.6f))
                    }
                }
            }
        }

        // 4 آمار - یکدست طلایی
        item {
            Row(modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                StatCardUniform("پرونده", cases.size.toString(), Icons.Default.Folder, Modifier.weight(1f))
                StatCardUniform("بحرانی", deadlines.count { it.daysRemaining <= 3 }.toString(), Icons.Default.Warning, Modifier.weight(1f))
                StatCardUniform("ثنا", eblaghs.count { !it.isProcessed }.toString(), Icons.Default.NotificationsActive, Modifier.weight(1f))
                StatCardUniform("AI", chatMessages.size.toString(), Icons.Default.SmartToy, Modifier.weight(1f))
            }
        }

        // نمودار - تک‌رنگ طلایی
        item {
            Card(modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp), shape = RoundedCornerShape(18.dp), colors = CardDefaults.cardColors(containerColor = CardBg), border = BorderStroke(1.dp, Border)) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(modifier = Modifier.size(36.dp).clip(RoundedCornerShape(10.dp)).background(GoldBrush), contentAlignment = Alignment.Center) {
                            Icon(Icons.Default.BarChart, contentDescription = null, tint = Bg, modifier = Modifier.size(18.dp))
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Text("نمودار پرونده‌ها", fontWeight = FontWeight.Bold, fontSize = 14.sp, color = Color.White)
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    Row(modifier = Modifier.fillMaxWidth().height(80.dp), horizontalArrangement = Arrangement.SpaceEvenly, verticalAlignment = Alignment.Bottom) {
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
            Card(modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp), shape = RoundedCornerShape(18.dp), colors = CardDefaults.cardColors(containerColor = CardBg), border = BorderStroke(1.dp, BorderStrong)) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(modifier = Modifier.size(36.dp).clip(RoundedCornerShape(10.dp)).background(GoldBrush), contentAlignment = Alignment.Center) {
                            Icon(Icons.Default.AutoAwesome, contentDescription = null, tint = Bg, modifier = Modifier.size(18.dp))
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Text("پیش‌بینی رای", fontWeight = FontWeight.Bold, fontSize = 14.sp, color = Color.White)
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                    if (cases.isNotEmpty()) {
                        Button(
                            onClick = { /* select first case */ 
                                selectedCaseForPrediction = cases.firstOrNull()
                                coroutineScope.launch {
                                    cases.firstOrNull()?.let { predictionResult = PredictionService.predictCaseOutcome(it) }
                                }
                            },
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = CardElevated, contentColor = Color.White),
                            border = BorderStroke(1.dp, Border),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text("پیش‌بینی پرونده اول", fontSize = 12.sp)
                        }
                        predictionResult?.let { result ->
                            Spacer(modifier = Modifier.height(12.dp))
                            Card(shape = RoundedCornerShape(14.dp), colors = CardDefaults.cardColors(containerColor = CardElevated), border = BorderStroke(1.dp, Border)) {
                                Column(modifier = Modifier.padding(14.dp)) {
                                    Text("موفقیت: ${result.successProbability}٪", fontWeight = FontWeight.Bold, color = Gold, fontSize = 14.sp)
                                    Spacer(modifier = Modifier.height(6.dp))
                                    Text(result.predictedOutcome, fontSize = 11.sp, color = Color.White.copy(alpha = 0.8f))
                                }
                            }
                        }
                    }
                }
            }
        }

        // مواعد - یکدست
        item {
            Card(modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp), shape = RoundedCornerShape(18.dp), colors = CardDefaults.cardColors(containerColor = CardBg), border = BorderStroke(1.dp, Border)) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(modifier = Modifier.size(36.dp).clip(RoundedCornerShape(10.dp)).background(GoldBrush), contentAlignment = Alignment.Center) {
                            Icon(Icons.Default.Alarm, contentDescription = null, tint = Bg, modifier = Modifier.size(18.dp))
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Text("مواعد بحرانی", fontWeight = FontWeight.Bold, fontSize = 14.sp, color = Color.White)
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                    val critical = deadlines.filter { it.daysRemaining <= 7 && !it.isCompleted }.take(2)
                    if (critical.isEmpty()) {
                        Text("هیچ موعد بحرانی ندارید", fontSize = 11.sp, color = Color.White.copy(alpha = 0.6f))
                    } else {
                        critical.forEach { d ->
                            Text("• ${d.title} - ${d.daysRemaining} روز", fontSize = 11.sp, color = Color.White.copy(alpha = 0.8f), modifier = Modifier.padding(vertical = 2.dp))
                        }
                    }
                }
            }
        }

        // دسترسی سریع - 3 دکمه یکدست طلایی
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
fun StatCardUniform(label: String, value: String, icon: ImageVector, modifier: Modifier = Modifier) {
    Card(modifier = modifier, shape = RoundedCornerShape(14.dp), colors = CardDefaults.cardColors(containerColor = CardBg), border = BorderStroke(1.dp, Border)) {
        Column(modifier = Modifier.padding(12.dp), horizontalAlignment = Alignment.CenterHorizontally) {
            Box(modifier = Modifier.size(28.dp).clip(CircleShape).background(Gold.copy(alpha = 0.15f)), contentAlignment = Alignment.Center) {
                Icon(icon, contentDescription = null, tint = Gold, modifier = Modifier.size(14.dp))
            }
            Spacer(modifier = Modifier.height(6.dp))
            Text(value, fontSize = 16.sp, fontWeight = FontWeight.ExtraBold, color = Color.White)
            Text(label, fontSize = 9.sp, color = Color.White.copy(alpha = 0.5f))
        }
    }
}

@Composable
fun BarUniform(label: String, ratio: Float) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Box(modifier = Modifier.width(28.dp).height((ratio * 60).dp).clip(RoundedCornerShape(6.dp)).background(GoldBrush))
        Spacer(modifier = Modifier.height(6.dp))
        Text(label, fontSize = 9.sp, color = Color.White.copy(alpha = 0.5f))
    }
}

@Composable
fun QuickUniform(title: String, icon: ImageVector, modifier: Modifier = Modifier, onClick: () -> Unit) {
    Card(modifier = modifier.shadow(6.dp, RoundedCornerShape(14.dp)).clip(RoundedCornerShape(14.dp)).clickable { onClick() }, shape = RoundedCornerShape(14.dp), colors = CardDefaults.cardColors(containerColor = CardBg), border = BorderStroke(1.dp, Border)) {
        Column(modifier = Modifier.padding(14.dp), horizontalAlignment = Alignment.CenterHorizontally) {
            Box(modifier = Modifier.size(40.dp).clip(RoundedCornerShape(10.dp)).background(GoldBrush), contentAlignment = Alignment.Center) {
                Icon(icon, contentDescription = null, tint = Bg, modifier = Modifier.size(20.dp))
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(title, fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color.White)
        }
    }
}

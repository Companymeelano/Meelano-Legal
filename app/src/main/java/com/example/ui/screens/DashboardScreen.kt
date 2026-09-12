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
import androidx.compose.ui.graphics.Brush
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
    val drafts by viewModel.drafts.collectAsStateWithLifecycle()
    val chatMessages by viewModel.chatMessages.collectAsStateWithLifecycle()

    var selectedCaseForPrediction by remember { mutableStateOf<LegalCase?>(null) }
    var predictionResult by remember { mutableStateOf<com.example.data.service.PredictionResult?>(null) }

    LazyColumn(
        modifier = modifier.fillMaxSize().background(Slate50),
        contentPadding = PaddingValues(bottom = 100.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Luxury Header
        item {
            Card(
                modifier = Modifier.fillMaxWidth().shadow(12.dp, RoundedCornerShape(bottomStart = 32.dp, bottomEnd = 32.dp)),
                shape = RoundedCornerShape(bottomStart = 32.dp, bottomEnd = 32.dp),
                colors = CardDefaults.cardColors(containerColor = LuxuryNavy)
            ) {
                Box(modifier = Modifier.fillMaxWidth().background(LuxuryNavyBrush).padding(24.dp)) {
                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(modifier = Modifier.size(48.dp).clip(CircleShape).background(LuxuryGoldBrush), contentAlignment = Alignment.Center) {
                                Icon(Icons.Default.Dashboard, contentDescription = null, tint = LuxuryNavy, modifier = Modifier.size(26.dp))
                            }
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Text("داشبورد مدیریتی لاکچری", fontSize = 20.sp, fontWeight = FontWeight.ExtraBold, color = Color.White)
                                Text("تحلیل هوشمند • پیش‌بینی رای • نمودارها • یادآورها", fontSize = 11.sp, color = Slate300)
                            }
                        }
                        Spacer(modifier = Modifier.height(16.dp))
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            DashboardQuickStat("پرونده", cases.size.toString(), Icons.Default.Folder, Modifier.weight(1f))
                            DashboardQuickStat("موعد بحرانی", deadlines.count { it.daysRemaining <= 3 }.toString(), Icons.Default.Warning, Modifier.weight(1f))
                            DashboardQuickStat("ثنا جدید", eblaghs.count { !it.isProcessed }.toString(), Icons.Default.NotificationsActive, Modifier.weight(1f))
                            DashboardQuickStat("AI چت", chatMessages.size.toString(), Icons.Default.SmartToy, Modifier.weight(1f))
                        }
                    }
                }
            }
        }

        // بخش ۱۱: نمودارها (شبیه‌سازی Vico Charts)
        item {
            Card(modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp), shape = RoundedCornerShape(20.dp), colors = CardDefaults.cardColors(containerColor = Color.White), border = BorderStroke(1.dp, Slate200), elevation = CardDefaults.cardElevation(4.dp)) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.BarChart, contentDescription = null, tint = Indigo600, modifier = Modifier.size(20.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("نمودار تحلیلی پرونده‌ها", fontWeight = FontWeight.Bold, fontSize = 15.sp)
                        Spacer(modifier = Modifier.weight(1f))
                        Surface(shape = RoundedCornerShape(8.dp), color = Emerald50) { Text("📈 +۱۲٪ رشد", fontSize = 10.sp, color = Emerald600, modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)) }
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    // شبیه‌سازی نمودار میله‌ای
                    Row(modifier = Modifier.fillMaxWidth().height(100.dp), horizontalArrangement = Arrangement.SpaceEvenly, verticalAlignment = Alignment.Bottom) {
                        ChartBar("حقوقی", 0.8f, Indigo600)
                        ChartBar("کیفری", 0.5f, Rose500)
                        ChartBar("خانواده", 0.6f, Emerald500)
                        ChartBar("ملکی", 0.9f, Amber500)
                        ChartBar("ثبتی", 0.4f, Sky500)
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text("میانگین موفقیت: ۷۸٪", fontSize = 11.sp, color = Slate500)
                        Text("تحلیل ۱۰۰۰ دادنامه مشابه", fontSize = 11.sp, color = Indigo600, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }

        // بخش ۷: پیش‌بینی رای
        item {
            Card(modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp), shape = RoundedCornerShape(20.dp), colors = CardDefaults.cardColors(containerColor = Color.White), border = BorderStroke(1.dp, LuxuryGold.copy(alpha = 0.3f)), elevation = CardDefaults.cardElevation(6.dp)) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(modifier = Modifier.size(36.dp).clip(RoundedCornerShape(10.dp)).background(LuxuryGoldBrush), contentAlignment = Alignment.Center) {
                            Icon(Icons.Default.AutoAwesome, contentDescription = null, tint = LuxuryNavy, modifier = Modifier.size(20.dp))
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text("پیش‌بینی هوشمند رای دادگاه", fontWeight = FontWeight.ExtraBold, fontSize = 14.sp)
                            Text("بر اساس تحلیل ۱۰۰۰ دادنامه مشابه با AI", fontSize = 11.sp, color = Slate500)
                        }
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                    if (cases.isNotEmpty()) {
                        var expanded by remember { mutableStateOf(false) }
                        OutlinedButton(onClick = { expanded = true }, shape = RoundedCornerShape(12.dp), modifier = Modifier.fillMaxWidth()) {
                            Text("انتخاب پرونده برای پیش‌بینی: ${selectedCaseForPrediction?.caseTitle?.take(30) ?: "انتخاب کنید"}")
                        }
                        if (expanded) {
                            Card(modifier = Modifier.fillMaxWidth().padding(top = 8.dp), shape = RoundedCornerShape(12.dp), colors = CardDefaults.cardColors(containerColor = Slate50)) {
                                Column {
                                    cases.take(3).forEach { c ->
                                        TextButton(onClick = {
                                            selectedCaseForPrediction = c
                                            expanded = false
                                            // شبیه‌سازی پیش‌بینی
                                            kotlinx.coroutines.CoroutineScope(kotlinx.coroutines.Dispatchers.Main).launch {
                                                predictionResult = PredictionService.predictCaseOutcome(c)
                                            }
                                        }, modifier = Modifier.fillMaxWidth()) {
                                            Text(c.caseTitle.take(40), fontSize = 12.sp)
                                        }
                                    }
                                }
                            }
                        }

                        predictionResult?.let { result ->
                            Spacer(modifier = Modifier.height(16.dp))
                            Surface(shape = RoundedCornerShape(16.dp), color = when {
                                result.successProbability > 80 -> Emerald50
                                result.successProbability > 60 -> Amber50
                                else -> Rose50
                            }, border = BorderStroke(1.dp, when {
                                result.successProbability > 80 -> Emerald100
                                result.successProbability > 60 -> Amber100
                                else -> Rose100
                            })) {
                                Column(modifier = Modifier.padding(16.dp)) {
                                    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                        Text("احتمال موفقیت: ${result.successProbability}٪", fontWeight = FontWeight.ExtraBold, fontSize = 18.sp, color = when {
                                            result.successProbability > 80 -> Emerald600
                                            result.successProbability > 60 -> Amber600
                                            else -> Rose600
                                        })
                                        Surface(shape = CircleShape, color = when (result.riskLevel) {
                                            "کم" -> Emerald500
                                            "متوسط" -> Amber500
                                            else -> Rose500
                                        }) {
                                            Text(result.riskLevel, color = Color.White, fontSize = 10.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp))
                                        }
                                    }
                                    Spacer(modifier = Modifier.height(8.dp))
                                    Text(result.predictedOutcome, fontSize = 12.sp, color = Slate700, lineHeight = 16.sp)
                                    Spacer(modifier = Modifier.height(8.dp))
                                    Text("عوامل کلیدی:", fontWeight = FontWeight.Bold, fontSize = 11.sp)
                                    result.keyFactors.forEach { factor ->
                                        Row(verticalAlignment = Alignment.CenterVertically) {
                                            Box(modifier = Modifier.size(6.dp).clip(CircleShape).background(Emerald500))
                                            Spacer(modifier = Modifier.width(6.dp))
                                            Text(factor, fontSize = 11.sp, color = Slate600)
                                        }
                                    }
                                }
                            }
                        }
                    } else {
                        Text("پرونده‌ای برای پیش‌بینی وجود ندارد", fontSize = 12.sp, color = Slate500)
                    }
                }
            }
        }

        // مواعد بحرانی - بخش ۱۲ ویجت
        item {
            Card(modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp), shape = RoundedCornerShape(20.dp), colors = CardDefaults.cardColors(containerColor = Color.White), border = BorderStroke(1.dp, Rose100), elevation = CardDefaults.cardElevation(4.dp)) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Alarm, contentDescription = null, tint = Rose600, modifier = Modifier.size(20.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("مواعد بحرانی - ویجت هوم‌اسکرین", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                        Spacer(modifier = Modifier.weight(1f))
                        Surface(shape = RoundedCornerShape(20.dp), color = Rose50, border = BorderStroke(1.dp, Rose100)) {
                            Text("۳ روز آینده", fontSize = 10.sp, color = Rose600, fontWeight = FontWeight.Bold, modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp))
                        }
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                    val critical = deadlines.filter { it.daysRemaining <= 7 && !it.isCompleted }.take(3)
                    if (critical.isEmpty()) {
                        Text("✅ هیچ موعد بحرانی در ۷ روز آینده ندارید", fontSize = 12.sp, color = Emerald600)
                    } else {
                        critical.forEach { d ->
                            Row(modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp), verticalAlignment = Alignment.CenterVertically) {
                                Box(modifier = Modifier.size(8.dp).clip(CircleShape).background(if (d.daysRemaining <= 3) Rose500 else Amber500))
                                Spacer(modifier = Modifier.width(8.dp))
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(d.title, fontSize = 12.sp, fontWeight = FontWeight.Medium)
                                    Text("${d.daysRemaining} روز مانده - ${d.caseNumber}", fontSize = 10.sp, color = Slate500)
                                }
                            }
                        }
                    }
                }
            }
        }

        // دسترسی سریع لاکچری
        item {
            Row(modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                QuickActionCard("اسکنر هوشمند", Icons.Default.DocumentScanner, Brush.linearGradient(listOf(Indigo600, Indigo800)), Modifier.weight(1f)) { onNavigateToCases() }
                QuickActionCard("AI فارسی", Icons.Default.SmartToy, Brush.linearGradient(listOf(LuxuryGold, LuxuryGoldDark)), Modifier.weight(1f)) { onNavigateToAi() }
                QuickActionCard("PDF لاکچری", Icons.Default.PictureAsPdf, Brush.linearGradient(listOf(Emerald500, Emerald600)), Modifier.weight(1f)) { onNavigateToCases() }
            }
        }
    }
}

@Composable
fun DashboardQuickStat(label: String, value: String, icon: ImageVector, modifier: Modifier = Modifier) {
    Column(modifier = modifier.background(Color.White.copy(alpha = 0.1f), RoundedCornerShape(12.dp)).padding(10.dp), horizontalAlignment = Alignment.CenterHorizontally) {
        Icon(icon, contentDescription = null, tint = LuxuryGold, modifier = Modifier.size(18.dp))
        Spacer(modifier = Modifier.height(4.dp))
        Text(value, fontSize = 16.sp, fontWeight = FontWeight.ExtraBold, color = Color.White)
        Text(label, fontSize = 9.sp, color = Slate300)
    }
}

@Composable
fun ChartBar(label: String, heightRatio: Float, color: Color) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Box(modifier = Modifier.width(32.dp).height((heightRatio * 80).dp).clip(RoundedCornerShape(topStart = 8.dp, topEnd = 8.dp)).background(Brush.verticalGradient(listOf(color, color.copy(alpha = 0.6f)))))
        Spacer(modifier = Modifier.height(6.dp))
        Text(label, fontSize = 10.sp, color = Slate600)
    }
}

@Composable
fun QuickActionCard(title: String, icon: ImageVector, gradient: Brush, modifier: Modifier = Modifier, onClick: () -> Unit) {
    Card(modifier = modifier.shadow(6.dp, RoundedCornerShape(16.dp)).clip(RoundedCornerShape(16.dp)), shape = RoundedCornerShape(16.dp), colors = CardDefaults.cardColors(containerColor = Color.White), border = BorderStroke(1.dp, Slate200), elevation = CardDefaults.cardElevation(4.dp)) {
        Box(modifier = Modifier.fillMaxWidth().background(Color.White).padding(16.dp).clip(RoundedCornerShape(16.dp)), contentAlignment = Alignment.Center) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Box(modifier = Modifier.size(44.dp).clip(CircleShape).background(gradient), contentAlignment = Alignment.Center) {
                    Icon(icon, contentDescription = null, tint = Color.White, modifier = Modifier.size(22.dp))
                }
                Spacer(modifier = Modifier.height(8.dp))
                Text(title, fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Slate800)
            }
        }
    }
}

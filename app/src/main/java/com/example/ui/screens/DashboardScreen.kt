package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import kotlinx.coroutines.launch

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
    val coroutineScope = rememberCoroutineScope()

    LazyColumn(
        modifier = modifier.fillMaxSize().background(LuxuryObsidianBrush),
        contentPadding = PaddingValues(bottom = 100.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Luxury Header - تیره با طلایی
        item {
            Card(
                modifier = Modifier.fillMaxWidth().shadow(24.dp, RoundedCornerShape(bottomStart = 36.dp, bottomEnd = 36.dp)),
                shape = RoundedCornerShape(bottomStart = 36.dp, bottomEnd = 36.dp),
                colors = CardDefaults.cardColors(containerColor = LuxuryNavyDeep),
                border = BorderStroke(1.dp, LuxuryDarkBorderStrong)
            ) {
                Box(modifier = Modifier.fillMaxWidth().background(LuxuryObsidianBrush).padding(24.dp)) {
                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier.size(56.dp).clip(RoundedCornerShape(16.dp)).background(LuxuryGoldDarkBrush).shadow(12.dp, RoundedCornerShape(16.dp)),
                                contentAlignment = Alignment.Center
                            ) {
                                Text("M", fontSize = 28.sp, fontWeight = FontWeight.ExtraBold, color = LuxuryNavyDeep)
                            }
                            Spacer(modifier = Modifier.width(14.dp))
                            Column {
                                Text("داشبورد مدیریتی لاکچری", fontSize = 20.sp, fontWeight = FontWeight.ExtraBold, color = Color.White)
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Box(modifier = Modifier.size(6.dp).clip(CircleShape).background(LuxuryGold))
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text("تحلیل هوشمند • پیش‌بینی رای • نمودارها", fontSize = 11.sp, color = Slate300)
                                }
                            }
                        }
                        Spacer(modifier = Modifier.height(20.dp))
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                            DashboardQuickStatDark("پرونده", cases.size.toString(), Icons.Default.Folder, Modifier.weight(1f))
                            DashboardQuickStatDark("موعد بحرانی", deadlines.count { it.daysRemaining <= 3 }.toString(), Icons.Default.Warning, Modifier.weight(1f))
                            DashboardQuickStatDark("ثنا جدید", eblaghs.count { !it.isProcessed }.toString(), Icons.Default.NotificationsActive, Modifier.weight(1f))
                            DashboardQuickStatDark("AI چت", chatMessages.size.toString(), Icons.Default.SmartToy, Modifier.weight(1f))
                        }
                    }
                }
            }
        }

        // بخش ۱۱: نمودارها - تیره لاکچری
        item {
            Card(modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp), shape = RoundedCornerShape(24.dp), colors = CardDefaults.cardColors(containerColor = LuxuryDarkCard), border = BorderStroke(1.dp, LuxuryDarkBorder), elevation = CardDefaults.cardElevation(8.dp)) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(modifier = Modifier.size(40.dp).clip(RoundedCornerShape(12.dp)).background(Brush.linearGradient(listOf(Indigo600, Indigo800))), contentAlignment = Alignment.Center) {
                            Icon(Icons.Default.BarChart, contentDescription = null, tint = Color.White, modifier = Modifier.size(20.dp))
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text("نمودار تحلیلی پرونده‌ها", fontWeight = FontWeight.ExtraBold, fontSize = 15.sp, color = Color.White)
                            Text("Vico Charts • تحلیل هوشمند", fontSize = 11.sp, color = Slate400)
                        }
                        Surface(shape = RoundedCornerShape(20.dp), color = Emerald500.copy(alpha = 0.15f), border = BorderStroke(1.dp, Emerald500.copy(alpha = 0.3f))) {
                            Text("📈 +۱۲٪ رشد", fontSize = 10.sp, color = Emerald300, fontWeight = FontWeight.Bold, modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp))
                        }
                    }
                    Spacer(modifier = Modifier.height(20.dp))
                    Row(modifier = Modifier.fillMaxWidth().height(110.dp), horizontalArrangement = Arrangement.SpaceEvenly, verticalAlignment = Alignment.Bottom) {
                        ChartBarDark("حقوقی", 0.8f, Indigo600)
                        ChartBarDark("کیفری", 0.5f, Rose500)
                        ChartBarDark("خانواده", 0.6f, Emerald500)
                        ChartBarDark("ملکی", 0.9f, Amber500)
                        ChartBarDark("ثبتی", 0.4f, Sky500)
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text("میانگین موفقیت: ۷۸٪", fontSize = 11.sp, color = Slate400)
                        Surface(shape = RoundedCornerShape(8.dp), color = Indigo600.copy(alpha = 0.2f)) {
                            Text("تحلیل ۱۰۰۰ دادنامه مشابه", fontSize = 11.sp, color = Indigo300, fontWeight = FontWeight.Bold, modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp))
                        }
                    }
                }
            }
        }

        // بخش ۷: پیش‌بینی رای - تیره طلایی
        item {
            Card(modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp), shape = RoundedCornerShape(24.dp), colors = CardDefaults.cardColors(containerColor = LuxuryDarkCard), border = BorderStroke(1.dp, LuxuryGold.copy(alpha = 0.35f)), elevation = CardDefaults.cardElevation(12.dp)) {
                Box(modifier = Modifier.fillMaxWidth().background(Brush.linearGradient(listOf(LuxuryDarkCard, LuxuryDarkCardElevated))).padding(20.dp)) {
                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(modifier = Modifier.size(48.dp).clip(RoundedCornerShape(14.dp)).background(LuxuryGoldDarkBrush).shadow(8.dp, RoundedCornerShape(14.dp)), contentAlignment = Alignment.Center) {
                                Icon(Icons.Default.AutoAwesome, contentDescription = null, tint = LuxuryNavyDeep, modifier = Modifier.size(24.dp))
                            }
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Text("پیش‌بینی هوشمند رای دادگاه", fontWeight = FontWeight.ExtraBold, fontSize = 15.sp, color = Color.White)
                                Text("بر اساس تحلیل ۱۰۰۰ دادنامه مشابه با AI", fontSize = 11.sp, color = Slate400)
                            }
                        }
                        Spacer(modifier = Modifier.height(16.dp))
                        if (cases.isNotEmpty()) {
                            var expanded by remember { mutableStateOf(false) }
                            Button(
                                onClick = { expanded = true },
                                shape = RoundedCornerShape(14.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = LuxuryDarkCardElevated, contentColor = Color.White),
                                border = BorderStroke(1.dp, LuxuryGold.copy(alpha = 0.3f)),
                                modifier = Modifier.fillMaxWidth().height(48.dp)
                            ) {
                                Icon(Icons.Default.Gavel, contentDescription = null, tint = LuxuryGold, modifier = Modifier.size(18.dp))
                                Spacer(modifier = Modifier.width(8.dp))
                                Text("انتخاب پرونده: ${selectedCaseForPrediction?.caseTitle?.take(30) ?: "انتخاب کنید"}", fontSize = 12.sp)
                            }
                            if (expanded) {
                                Card(modifier = Modifier.fillMaxWidth().padding(top = 12.dp), shape = RoundedCornerShape(16.dp), colors = CardDefaults.cardColors(containerColor = LuxuryCharcoal), border = BorderStroke(0.5.dp, Slate700)) {
                                    Column(modifier = Modifier.padding(8.dp)) {
                                        cases.take(3).forEach { c ->
                                            Card(modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp).clickable {
                                                selectedCaseForPrediction = c
                                                expanded = false
                                                coroutineScope.launch {
                                                    predictionResult = PredictionService.predictCaseOutcome(c)
                                                }
                                            }, shape = RoundedCornerShape(12.dp), colors = CardDefaults.cardColors(containerColor = LuxuryDarkCardElevated)) {
                                                Text(c.caseTitle.take(50), fontSize = 12.sp, color = Color.White, modifier = Modifier.padding(12.dp))
                                            }
                                        }
                                    }
                                }
                            }

                            predictionResult?.let { result ->
                                Spacer(modifier = Modifier.height(16.dp))
                                Card(shape = RoundedCornerShape(20.dp), colors = CardDefaults.cardColors(containerColor = when {
                                    result.successProbability > 80 -> Emerald600.copy(alpha = 0.15f)
                                    result.successProbability > 60 -> Amber600.copy(alpha = 0.15f)
                                    else -> Rose600.copy(alpha = 0.15f)
                                }), border = BorderStroke(1.dp, when {
                                    result.successProbability > 80 -> Emerald500.copy(alpha = 0.3f)
                                    result.successProbability > 60 -> Amber500.copy(alpha = 0.3f)
                                    else -> Rose500.copy(alpha = 0.3f)
                                })) {
                                    Column(modifier = Modifier.padding(16.dp)) {
                                        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                            Text("احتمال موفقیت: ${result.successProbability}٪", fontWeight = FontWeight.ExtraBold, fontSize = 18.sp, color = when {
                                                result.successProbability > 80 -> Emerald300
                                                result.successProbability > 60 -> Amber300
                                                else -> Rose300
                                            })
                                            Surface(shape = CircleShape, color = when (result.riskLevel) {
                                                "کم" -> Emerald500
                                                "متوسط" -> Amber500
                                                else -> Rose500
                                            }) {
                                                Text(result.riskLevel, color = Color.White, fontSize = 10.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(horizontal = 12.dp, vertical = 5.dp))
                                            }
                                        }
                                        Spacer(modifier = Modifier.height(10.dp))
                                        Text(result.predictedOutcome, fontSize = 13.sp, color = Slate200, lineHeight = 18.sp)
                                        Spacer(modifier = Modifier.height(10.dp))
                                        Text("عوامل کلیدی:", fontWeight = FontWeight.Bold, fontSize = 12.sp, color = Color.White)
                                        result.keyFactors.forEach { factor ->
                                            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(vertical = 2.dp)) {
                                                Box(modifier = Modifier.size(6.dp).clip(CircleShape).background(LuxuryGold))
                                                Spacer(modifier = Modifier.width(8.dp))
                                                Text(factor, fontSize = 11.sp, color = Slate300)
                                            }
                                        }
                                    }
                                }
                            }
                        } else {
                            Text("پرونده‌ای برای پیش‌بینی وجود ندارد", fontSize = 12.sp, color = Slate400)
                        }
                    }
                }
            }
        }

        // مواعد بحرانی - تیره
        item {
            Card(modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp), shape = RoundedCornerShape(24.dp), colors = CardDefaults.cardColors(containerColor = LuxuryDarkCard), border = BorderStroke(1.dp, Rose500.copy(alpha = 0.3f)), elevation = CardDefaults.cardElevation(8.dp)) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(modifier = Modifier.size(40.dp).clip(RoundedCornerShape(12.dp)).background(Brush.linearGradient(listOf(Rose500, Rose600))), contentAlignment = Alignment.Center) {
                            Icon(Icons.Default.Alarm, contentDescription = null, tint = Color.White, modifier = Modifier.size(20.dp))
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text("مواعد بحرانی - ویجت هوم‌اسکرین", fontWeight = FontWeight.ExtraBold, fontSize = 14.sp, color = Color.White)
                            Text("بخش 12 - نمایش در هوم", fontSize = 11.sp, color = Slate400)
                        }
                        Surface(shape = RoundedCornerShape(20.dp), color = Rose500.copy(alpha = 0.15f), border = BorderStroke(1.dp, Rose500.copy(alpha = 0.3f))) {
                            Text("۳ روز آینده", fontSize = 10.sp, color = Rose300, fontWeight = FontWeight.Bold, modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp))
                        }
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    val critical = deadlines.filter { it.daysRemaining <= 7 && !it.isCompleted }.take(3)
                    if (critical.isEmpty()) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.CheckCircle, contentDescription = null, tint = Emerald500, modifier = Modifier.size(20.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("هیچ موعد بحرانی در ۷ روز آینده ندارید", fontSize = 12.sp, color = Emerald300)
                        }
                    } else {
                        critical.forEach { d ->
                            Card(modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp), shape = RoundedCornerShape(12.dp), colors = CardDefaults.cardColors(containerColor = LuxuryCharcoal), border = BorderStroke(0.5.dp, Slate700)) {
                                Row(modifier = Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
                                    Box(modifier = Modifier.size(10.dp).clip(CircleShape).background(if (d.daysRemaining <= 3) Rose500 else Amber500).shadow(4.dp, CircleShape))
                                    Spacer(modifier = Modifier.width(10.dp))
                                    Column(modifier = Modifier.weight(1f)) {
                                        Text(d.title, fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color.White)
                                        Text("${d.daysRemaining} روز مانده - ${d.caseNumber}", fontSize = 10.sp, color = Slate400)
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        // دسترسی سریع لاکچری تیره
        item {
            Row(modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                QuickActionCardDark("اسکنر هوشمند", Icons.Default.CameraAlt, Brush.linearGradient(listOf(Indigo600, Indigo800)), Modifier.weight(1f)) { onNavigateToCases() }
                QuickActionCardDark("AI فارسی", Icons.Default.SmartToy, LuxuryGoldDarkBrush, Modifier.weight(1f)) { onNavigateToAi() }
                QuickActionCardDark("PDF لاکچری", Icons.Default.PictureAsPdf, Brush.linearGradient(listOf(Emerald500, Emerald600)), Modifier.weight(1f)) { onNavigateToCases() }
            }
        }
    }
}

@Composable
fun DashboardQuickStatDark(label: String, value: String, icon: ImageVector, modifier: Modifier = Modifier) {
    Card(modifier = modifier.shadow(8.dp, RoundedCornerShape(16.dp)), shape = RoundedCornerShape(16.dp), colors = CardDefaults.cardColors(containerColor = LuxuryDarkCardElevated), border = BorderStroke(0.5.dp, LuxuryGold.copy(alpha = 0.2f))) {
        Column(modifier = Modifier.padding(12.dp), horizontalAlignment = Alignment.CenterHorizontally) {
            Box(modifier = Modifier.size(28.dp).clip(CircleShape).background(Brush.linearGradient(listOf(LuxuryGold.copy(alpha = 0.3f), LuxuryGold.copy(alpha = 0.1f))),), contentAlignment = Alignment.Center) {
                Icon(icon, contentDescription = null, tint = LuxuryGold, modifier = Modifier.size(16.dp))
            }
            Spacer(modifier = Modifier.height(6.dp))
            Text(value, fontSize = 18.sp, fontWeight = FontWeight.ExtraBold, color = Color.White)
            Text(label, fontSize = 9.sp, color = Slate400, textAlign = androidx.compose.ui.text.style.TextAlign.Center)
        }
    }
}

@Composable
fun ChartBarDark(label: String, heightRatio: Float, color: Color) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Box(modifier = Modifier.width(36.dp).height((heightRatio * 90).dp).clip(RoundedCornerShape(topStart = 10.dp, topEnd = 10.dp)).background(Brush.verticalGradient(listOf(color, color.copy(alpha = 0.5f)))))
        Spacer(modifier = Modifier.height(8.dp))
        Text(label, fontSize = 10.sp, color = Slate400, fontWeight = FontWeight.Medium)
    }
}

@Composable
fun QuickActionCardDark(title: String, icon: ImageVector, gradient: Brush, modifier: Modifier = Modifier, onClick: () -> Unit) {
    Card(modifier = modifier.shadow(12.dp, RoundedCornerShape(20.dp)).clip(RoundedCornerShape(20.dp)).clickable { onClick() }, shape = RoundedCornerShape(20.dp), colors = CardDefaults.cardColors(containerColor = LuxuryDarkCardElevated), border = BorderStroke(1.dp, LuxuryGold.copy(alpha = 0.2f)), elevation = CardDefaults.cardElevation(8.dp)) {
        Box(modifier = Modifier.fillMaxWidth().background(Brush.verticalGradient(listOf(LuxuryDarkCardElevated, LuxuryDarkCard))).padding(16.dp), contentAlignment = Alignment.Center) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Box(modifier = Modifier.size(52.dp).clip(RoundedCornerShape(16.dp)).background(gradient).shadow(8.dp, RoundedCornerShape(16.dp)), contentAlignment = Alignment.Center) {
                    Icon(icon, contentDescription = null, tint = Color.White, modifier = Modifier.size(26.dp))
                }
                Spacer(modifier = Modifier.height(10.dp))
                Text(title, fontSize = 12.sp, fontWeight = FontWeight.ExtraBold, color = Color.White, textAlign = androidx.compose.ui.text.style.TextAlign.Center)
                Spacer(modifier = Modifier.height(4.dp))
                Box(modifier = Modifier.size(20.dp, 2.dp).clip(RoundedCornerShape(1.dp)).background(LuxuryGold.copy(alpha = 0.5f)))
            }
        }
    }
}

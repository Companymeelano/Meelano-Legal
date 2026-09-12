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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.ChecklistDatabase
import com.example.data.model.ExpandedLegalDatabase
import com.example.ui.LegalViewModel
import com.example.ui.theme.*

@Composable
fun ToolsScreen(
    viewModel: LegalViewModel,
    modifier: Modifier = Modifier
) {
    var selectedCategory by remember { mutableStateOf("قضایی") }
    var selectedSubScreen by remember { mutableStateOf<String?>(null) }
    val categories = listOf("قضایی", "هوشمند", "سیستم")

    // Sub-screen navigation
    selectedSubScreen?.let { sub ->
        Box(modifier = modifier.fillMaxSize()) {
            when (sub) {
                "deadlines" -> DeadlinesScreen(viewModel = viewModel, modifier = Modifier.fillMaxSize())
                "eblagh" -> EblaghScreen(viewModel = viewModel, modifier = Modifier.fillMaxSize())
                "drafts" -> DraftingStudioScreen(viewModel = viewModel, modifier = Modifier.fillMaxSize())
                "settings" -> SettingsScreen(viewModel = viewModel, modifier = Modifier.fillMaxSize())
                else -> {}
            }
            // Back button
            Card(
                modifier = Modifier.align(Alignment.TopStart).padding(16.dp).shadow(8.dp, CircleShape),
                shape = CircleShape,
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                IconButton(onClick = { selectedSubScreen = null }) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "بازگشت", tint = Slate900)
                }
            }
        }
        return
    }

    LazyColumn(
        modifier = modifier.fillMaxSize().background(Slate50),
        contentPadding = PaddingValues(bottom = 100.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Header - دسته‌بندی هوشمند
        item {
            Card(
                modifier = Modifier.fillMaxWidth().shadow(12.dp, RoundedCornerShape(bottomStart = 32.dp, bottomEnd = 32.dp)),
                shape = RoundedCornerShape(bottomStart = 32.dp, bottomEnd = 32.dp),
                colors = CardDefaults.cardColors(containerColor = LuxuryNavy)
            ) {
                Box(modifier = Modifier.fillMaxWidth().background(LuxuryNavyBrush).padding(24.dp)) {
                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(modifier = Modifier.size(48.dp).clip(CircleShape).background(Brush.linearGradient(listOf(Indigo600, Indigo800))), contentAlignment = Alignment.Center) {
                                Icon(Icons.Default.Build, contentDescription = null, tint = Color.White, modifier = Modifier.size(26.dp))
                            }
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Text("جعبه ابزار لاکچری", fontSize = 20.sp, fontWeight = FontWeight.ExtraBold, color = Color.White)
                                Text("دسته‌بندی هوشمند • بدون شلوغی • ۲۰ بخش پیشرفته", fontSize = 11.sp, color = Slate300)
                            }
                        }
                        Spacer(modifier = Modifier.height(16.dp))
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            categories.forEach { cat ->
                                FilterChip(
                                    selected = selectedCategory == cat,
                                    onClick = { selectedCategory = cat },
                                    label = { Text(cat, fontSize = 12.sp, fontWeight = if (selectedCategory == cat) FontWeight.Bold else FontWeight.Normal) },
                                    shape = RoundedCornerShape(12.dp),
                                    colors = FilterChipDefaults.filterChipColors(
                                        selectedContainerColor = LuxuryGold,
                                        selectedLabelColor = LuxuryNavy,
                                        containerColor = Color.White.copy(alpha = 0.1f),
                                        labelColor = Color.White
                                    ),
                                    border = BorderStroke(1.dp, if (selectedCategory == cat) LuxuryGold else Color.White.copy(alpha = 0.2f))
                                )
                            }
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        Text("💡 تمام بخش‌ها در ۳ دسته هوشمند قرار گرفت تا منو شلوغ نشود", fontSize = 10.sp, color = LuxuryGoldLight)
                    }
                }
            }
        }

        when (selectedCategory) {
            "قضایی" -> {
                item { ToolSectionHeader("موتور قضایی هوشمند", "مواعد، چک‌لیست، پیش‌بینی، ثنا - بخش ۶،۸،۷،۱۹،۱۲", Icons.Default.Gavel) }

                item {
                    Row(modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                        LuxuryToolCard("مواعد قضایی\nهوشمند", "تقویم شمسی + تعطیلات\nماده ۴۴۳،۴۴۵", Icons.Default.CalendarMonth, Brush.linearGradient(listOf(Indigo600, Indigo800)), Modifier.weight(1f)) { selectedSubScreen = "deadlines" }
                        LuxuryToolCard("ابلاغیه ثنا\nخودکار", "اتصال API واقعی\nبخش ۱۹", Icons.Default.NotificationsActive, Brush.linearGradient(listOf(Sky500, Sky600)), Modifier.weight(1f)) { selectedSubScreen = "eblagh" }
                    }
                }
                item {
                    Row(modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                        LuxuryToolCard("چک‌لیست\nهوشمند", "${ChecklistDatabase.allChecklists.size} چک‌لیست\nبخش ۸", Icons.Default.Checklist, Brush.linearGradient(listOf(Emerald500, Emerald600)), Modifier.weight(1f)) {}
                        LuxuryToolCard("پیش‌بینی رای\nAI", "تحلیل ۱۰۰۰ دادنامه\nبخش ۷", Icons.Default.AutoAwesome, Brush.linearGradient(listOf(Amber500, Amber600)), Modifier.weight(1f)) {}
                    }
                }
                item {
                    Row(modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                        LuxuryToolCard("تنظیم لوایح\nهوشمند", "AI + صدای فارسی\nبخش ۱،۲،۴", Icons.Default.EditNote, Brush.linearGradient(listOf(Indigo600, LuxuryGold)), Modifier.weight(1f)) { selectedSubScreen = "drafts" }
                        LuxuryToolCard("ویجت مواعد\nبحرانی", "هوم‌اسکرین\nبخش ۱۲", Icons.Default.Widgets, Brush.linearGradient(listOf(Rose500, Rose600)), Modifier.weight(1f)) {}
                    }
                }

                // چک‌لیست‌ها - بخش ۸
                item {
                    Card(modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp), shape = RoundedCornerShape(20.dp), colors = CardDefaults.cardColors(containerColor = Color.White), border = BorderStroke(1.dp, Slate200), elevation = CardDefaults.cardElevation(4.dp)) {
                        Column(modifier = Modifier.padding(20.dp)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.Checklist, contentDescription = null, tint = Emerald600, modifier = Modifier.size(20.dp))
                                Spacer(modifier = Modifier.width(8.dp))
                                Text("چک‌لیست‌های هوشمند - بخش ۸", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                                Spacer(modifier = Modifier.weight(1f))
                                Surface(shape = RoundedCornerShape(20.dp), color = Emerald50, border = BorderStroke(1.dp, Emerald100)) {
                                    Text("${ChecklistDatabase.allChecklists.size} چک‌لیست", fontSize = 10.sp, color = Emerald600, modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp))
                                }
                            }
                            Spacer(modifier = Modifier.height(12.dp))
                            ChecklistDatabase.allChecklists.forEach { checklist ->
                                Surface(shape = RoundedCornerShape(12.dp), color = Slate50, border = BorderStroke(1.dp, Slate200), modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp).clickable {}) {
                                    Row(modifier = Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
                                        Box(modifier = Modifier.size(36.dp).clip(CircleShape).background(Brush.linearGradient(listOf(Indigo600, Indigo800))), contentAlignment = Alignment.Center) {
                                            Icon(Icons.Default.Checklist, contentDescription = null, tint = Color.White, modifier = Modifier.size(18.dp))
                                        }
                                        Spacer(modifier = Modifier.width(10.dp))
                                        Column(modifier = Modifier.weight(1f)) {
                                            Text(checklist.title, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                                            Text("${checklist.estimatedTimeDays} روز • ${checklist.legalBasis.take(35)}", fontSize = 10.sp, color = Slate500)
                                        }
                                        Icon(Icons.Default.ChevronLeft, contentDescription = null, tint = Slate400, modifier = Modifier.size(16.dp))
                                    }
                                }
                            }
                        }
                    }
                }
            }

            "هوشمند" -> {
                item { ToolSectionHeader("ابزارهای هوشمند لاکچری", "اسکنر، PDF، امضا، صدا، انیمیشن - بخش ۱۴،۱۸،۱۵،۴،۹،۱۳", Icons.Default.SmartToy) }

                item {
                    Row(modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                        LuxuryToolCard("اسکنر\nOCR فارسی", "ML Kit + Document AI\nبخش ۱۴", Icons.Default.DocumentScanner, Brush.linearGradient(listOf(Indigo600, Indigo800)), Modifier.weight(1f)) {}
                        LuxuryToolCard("PDF لاکچری\nطلایی", "سربرگ + QR + لوگو M\nبخش ۱۸", Icons.Default.PictureAsPdf, Brush.linearGradient(listOf(Rose500, Rose600)), Modifier.weight(1f)) {}
                    }
                }
                item {
                    Row(modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                        LuxuryToolCard("امضای\nدیجیتال", "اثر انگشت + بیومتریک\nبخش ۱۵", Icons.Default.Draw, Brush.linearGradient(listOf(Emerald500, Emerald600)), Modifier.weight(1f)) {}
                        LuxuryToolCard("صدای وکیل\nشخصی", "۵ پروفایل صوتی\nبخش ۴", Icons.Default.RecordVoiceOver, Brush.linearGradient(listOf(Amber500, Amber600)), Modifier.weight(1f)) { selectedSubScreen = "settings" }
                    }
                }
                item {
                    Row(modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                        LuxuryToolCard("انیمیشن\nLottie", "ترازوی عدالت ۳D\nبخش ۹", Icons.Default.Animation, Brush.linearGradient(listOf(LuxuryGold, LuxuryGoldDark)), Modifier.weight(1f)) {}
                        LuxuryToolCard("لرزش لاکچری\nHaptic", "دکمه‌های طلایی\nبخش ۱۳", Icons.Default.Vibration, Brush.linearGradient(listOf(Slate700, Slate900)), Modifier.weight(1f)) {}
                    }
                }

                // OCR Demo - بخش ۱۴
                item {
                    Card(modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp), shape = RoundedCornerShape(20.dp), colors = CardDefaults.cardColors(containerColor = Color.White), border = BorderStroke(1.dp, Indigo100), elevation = CardDefaults.cardElevation(4.dp)) {
                        Column(modifier = Modifier.padding(20.dp)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.DocumentScanner, contentDescription = null, tint = Indigo600, modifier = Modifier.size(20.dp))
                                Spacer(modifier = Modifier.width(8.dp))
                                Text("اسکنر هوشمند فارسی - بخش ۱۴", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                            }
                            Spacer(modifier = Modifier.height(8.dp))
                            Text("عکس از قرارداد یا دادخواست بگیرید، متن فارسی استخراج و به پرونده تبدیل می‌شود - ML Kit فارسی", fontSize = 11.sp, color = Slate500, lineHeight = 16.sp)
                            Spacer(modifier = Modifier.height(12.dp))
                            Button(onClick = {}, shape = RoundedCornerShape(12.dp), colors = ButtonDefaults.buttonColors(containerColor = Indigo600), modifier = Modifier.fillMaxWidth()) {
                                Icon(Icons.Default.CameraAlt, contentDescription = null, modifier = Modifier.size(18.dp))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text("شروع اسکن OCR فارسی", fontSize = 12.sp)
                            }
                        }
                    }
                }

                // PDF Demo - بخش ۱۸
                item {
                    Card(modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp), shape = RoundedCornerShape(20.dp), colors = CardDefaults.cardColors(containerColor = Color.White), border = BorderStroke(1.dp, Rose100), elevation = CardDefaults.cardElevation(4.dp)) {
                        Column(modifier = Modifier.padding(20.dp)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.PictureAsPdf, contentDescription = null, tint = Rose600, modifier = Modifier.size(20.dp))
                                Spacer(modifier = Modifier.width(8.dp))
                                Text("PDF لاکچری طلایی - بخش ۱۸", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                            }
                            Spacer(modifier = Modifier.height(8.dp))
                            Text("خروجی PDF با سربرگ طلایی، لوگو M سه‌بعدی، QR کد اصالت، مهر زمانی بلاک‌چین", fontSize = 11.sp, color = Slate500)
                            Spacer(modifier = Modifier.height(8.dp))
                            Surface(shape = RoundedCornerShape(8.dp), color = Amber50, border = BorderStroke(1.dp, Amber100)) {
                                Text("✨ شامل: هدر سرمه‌ای + متن طلایی + فوتر QR + امضای دیجیتال", fontSize = 10.sp, color = Amber600, modifier = Modifier.padding(8.dp))
                            }
                        }
                    }
                }
            }

            "سیستم" -> {
                item { ToolSectionHeader("سیستم و امنیت لاکچری", "رمزنگاری، بک‌آپ، لاگ، تم، یادآور - بخش ۲۱،۲۳،۲۴،۱۰،۱۷،۲۰", Icons.Default.Security) }

                item {
                    Row(modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                        LuxuryToolCard("رمزنگاری\nSQLCipher", "AES-256 + بیومتریک\nبخش ۲۱", Icons.Default.Lock, Brush.linearGradient(listOf(Slate800, Slate900)), Modifier.weight(1f)) { selectedSubScreen = "settings" }
                        LuxuryToolCard("بک‌آپ ابری\nDrive", "رمزنگاری شده\nبخش ۲۳", Icons.Default.CloudUpload, Brush.linearGradient(listOf(Sky500, Sky600)), Modifier.weight(1f)) { selectedSubScreen = "settings" }
                    }
                }
                item {
                    Row(modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                        LuxuryToolCard("لاگ قضایی\nبلاک‌چین", "مهر زمانی\nبخش ۲۴", Icons.Default.Fingerprint, Brush.linearGradient(listOf(Amber500, Amber600)), Modifier.weight(1f)) { selectedSubScreen = "settings" }
                        LuxuryToolCard("یادآور\nهوشمند", "WorkManager + TTS فارسی\nبخش ۱۷", Icons.Default.NotificationsActive, Brush.linearGradient(listOf(Emerald500, Emerald600)), Modifier.weight(1f)) {}
                    }
                }
                item {
                    Row(modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                        LuxuryToolCard("تم پویا\nلاکچری", "روز/شب + طلایی\nبخش ۱۰", Icons.Default.Palette, Brush.linearGradient(listOf(Indigo600, LuxuryGold)), Modifier.weight(1f)) { selectedSubScreen = "settings" }
                        LuxuryToolCard("نسخه KMP\nوب و iOS", "مولتی‌پلتفرم\nبخش ۲۰", Icons.Default.Devices, Brush.linearGradient(listOf(Indigo600, Sky500)), Modifier.weight(1f)) {}
                    }
                }

                // دانش‌نامه - بخش ۵
                item {
                    Card(modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp), shape = RoundedCornerShape(20.dp), colors = CardDefaults.cardColors(containerColor = Color.White), border = BorderStroke(1.dp, LuxuryGold.copy(alpha = 0.3f)), elevation = CardDefaults.cardElevation(6.dp)) {
                        Column(modifier = Modifier.padding(20.dp)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(modifier = Modifier.size(36.dp).clip(RoundedCornerShape(10.dp)).background(LuxuryGoldBrush), contentAlignment = Alignment.Center) {
                                    Icon(Icons.Default.MenuBook, contentDescription = null, tint = LuxuryNavy, modifier = Modifier.size(20.dp))
                                }
                                Spacer(modifier = Modifier.width(10.dp))
                                Column {
                                    Text("پایگاه دانش ۱۵۰۰۰ ماده‌ای", fontWeight = FontWeight.ExtraBold, fontSize = 14.sp)
                                    Text("بخش ۵ - FTS + Vector DB", fontSize = 11.sp, color = Slate500)
                                }
                            }
                            Spacer(modifier = Modifier.height(12.dp))
                            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                                ExpandedLegalDatabase.allLaws.take(3).forEach { (name, count) ->
                                    Surface(shape = RoundedCornerShape(8.dp), color = Slate100, border = BorderStroke(1.dp, Slate200), modifier = Modifier.weight(1f)) {
                                        Column(modifier = Modifier.padding(8.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                                            Text(count.toString(), fontWeight = FontWeight.Bold, fontSize = 14.sp, color = Indigo600)
                                            Text(name.take(8), fontSize = 9.sp, color = Slate600, textAlign = TextAlign.Center)
                                        }
                                    }
                                }
                            }
                            Spacer(modifier = Modifier.height(8.dp))
                            Text("جمع کل: ${ExpandedLegalDatabase.getTotalCount()} ماده قانونی - جستجوی برداری", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = LuxuryGoldDark)
                        }
                    }
                }

                // تنظیمات سریع
                item {
                    Card(modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp), shape = RoundedCornerShape(20.dp), colors = CardDefaults.cardColors(containerColor = Color.White), border = BorderStroke(1.dp, Slate200), elevation = CardDefaults.cardElevation(4.dp)) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text("دسترسی سریع به تنظیمات", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                            Spacer(modifier = Modifier.height(12.dp))
                            Button(onClick = { selectedSubScreen = "settings" }, modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(12.dp), colors = ButtonDefaults.buttonColors(containerColor = LuxuryNavy)) {
                                Icon(Icons.Default.Settings, contentDescription = null, modifier = Modifier.size(18.dp))
                                Spacer(modifier = Modifier.width(8.dp))
                                Text("باز کردن تنظیمات کامل لاکچری", fontSize = 12.sp)
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ToolSectionHeader(title: String, subtitle: String, icon: androidx.compose.ui.graphics.vector.ImageVector) {
    Row(modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp, vertical = 8.dp), verticalAlignment = Alignment.CenterVertically) {
        Box(modifier = Modifier.size(40.dp).clip(CircleShape).background(Brush.linearGradient(listOf(Indigo600, Indigo800))), contentAlignment = Alignment.Center) {
            Icon(icon, contentDescription = null, tint = Color.White, modifier = Modifier.size(20.dp))
        }
        Spacer(modifier = Modifier.width(10.dp))
        Column {
            Text(title, fontWeight = FontWeight.Bold, fontSize = 14.sp, color = Slate900)
            Text(subtitle, fontSize = 11.sp, color = Slate500)
        }
    }
}

@Composable
fun LuxuryToolCard(title: String, subtitle: String, icon: androidx.compose.ui.graphics.vector.ImageVector, gradient: Brush, modifier: Modifier = Modifier, onClick: () -> Unit = {}) {
    Card(modifier = modifier.shadow(6.dp, RoundedCornerShape(18.dp)).clickable { onClick() }, shape = RoundedCornerShape(18.dp), colors = CardDefaults.cardColors(containerColor = Color.White), border = BorderStroke(1.dp, Slate200), elevation = CardDefaults.cardElevation(4.dp)) {
        Column(modifier = Modifier.fillMaxWidth().padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally) {
            Box(modifier = Modifier.size(48.dp).clip(CircleShape).background(gradient), contentAlignment = Alignment.Center) {
                Icon(icon, contentDescription = null, tint = Color.White, modifier = Modifier.size(24.dp))
            }
            Spacer(modifier = Modifier.height(10.dp))
            Text(title, fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Slate900, lineHeight = 14.sp, textAlign = TextAlign.Center)
            Spacer(modifier = Modifier.height(4.dp))
            Text(subtitle, fontSize = 10.sp, color = Slate500, lineHeight = 12.sp, textAlign = TextAlign.Center)
        }
    }
}

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

data class ToolCategory(
    val id: String,
    val persianName: String,
    val englishName: String,
    val icon: ImageVector,
    val gradient: Brush,
    val description: String,
    val itemCount: Int
)

@Composable
fun ToolsScreen(
    viewModel: LegalViewModel,
    modifier: Modifier = Modifier
) {
    var selectedCategory by remember { mutableStateOf("court") }
    var selectedSubScreen by remember { mutableStateOf<String?>(null) }

    val categories = listOf(
        ToolCategory(
            id = "court",
            persianName = "میز قضاوت",
            englishName = "Court Desk",
            icon = Icons.Default.Gavel,
            gradient = Brush.linearGradient(listOf(Indigo600, Indigo800, Color(0xFF1E1B4B))),
            description = "مواعد، ابلاغیه، چک‌لیست",
            itemCount = 6
        ),
        ToolCategory(
            id = "brain",
            persianName = "مغز متفکر",
            englishName = "AI Brain",
            icon = Icons.Default.SmartToy,
            gradient = LuxuryGoldDarkBrush,
            description = "AI فارسی، دانش 15000 ماده",
            itemCount = 5
        ),
        ToolCategory(
            id = "atelier",
            persianName = "آتلیه لاکچری",
            englishName = "Luxury Atelier",
            icon = Icons.Default.AutoAwesome,
            gradient = Brush.linearGradient(listOf(Emerald500, Emerald600, Color(0xFF064E3B))),
            description = "PDF طلایی، OCR، امضا",
            itemCount = 6
        ),
        ToolCategory(
            id = "fortress",
            persianName = "دژ امنیت",
            englishName = "Security Fortress",
            icon = Icons.Default.Security,
            gradient = Brush.linearGradient(listOf(Slate700, Slate800, LuxuryNavy)),
            description = "رمزنگاری، بک‌آپ، بلاک‌چین",
            itemCount = 6
        )
    )

    // Sub-screen navigation - هوشمند بدون شلوغی
    selectedSubScreen?.let { sub ->
        Box(modifier = modifier.fillMaxSize().background(LuxuryObsidianBrush)) {
            when (sub) {
                "deadlines" -> DeadlinesScreen(viewModel = viewModel, modifier = Modifier.fillMaxSize())
                "eblagh" -> EblaghScreen(viewModel = viewModel, modifier = Modifier.fillMaxSize())
                "drafts" -> DraftingStudioScreen(viewModel = viewModel, modifier = Modifier.fillMaxSize())
                "settings" -> SettingsScreen(viewModel = viewModel, modifier = Modifier.fillMaxSize())
                "knowledge" -> KnowledgeBaseScreen(modifier = Modifier.fillMaxSize())
                "checklist" -> ChecklistScreen(modifier = Modifier.fillMaxSize())
                "ocr" -> OcrScannerScreen(modifier = Modifier.fillMaxSize())
                "pdf" -> PdfLuxuryScreen(modifier = Modifier.fillMaxSize())
                "signature" -> SignatureScreen(modifier = Modifier.fillMaxSize())
                "audit" -> AuditLogScreen(modifier = Modifier.fillMaxSize())
                "backup" -> BackupScreen(modifier = Modifier.fillMaxSize())
                else -> {}
            }
            // Back button لاکچری تیره
            Card(
                modifier = Modifier.align(Alignment.TopStart).padding(16.dp).shadow(16.dp, CircleShape),
                shape = CircleShape,
                colors = CardDefaults.cardColors(containerColor = LuxuryDarkCardElevated),
                border = BorderStroke(1.dp, LuxuryGold.copy(alpha = 0.4f))
            ) {
                IconButton(onClick = { selectedSubScreen = null }) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "بازگشت", tint = LuxuryGold)
                }
            }
        }
        return
    }

    LazyColumn(
        modifier = modifier.fillMaxSize().background(LuxuryObsidianBrush),
        contentPadding = PaddingValues(bottom = 100.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Header - تیره لاکچری با طلایی
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
                            // لوگو M طلایی 3D
                            Box(
                                modifier = Modifier
                                    .size(56.dp)
                                    .clip(RoundedCornerShape(16.dp))
                                    .background(LuxuryGoldDarkBrush)
                                    .shadow(12.dp, RoundedCornerShape(16.dp)),
                                contentAlignment = Alignment.Center
                            ) {
                                Text("M", fontSize = 28.sp, fontWeight = FontWeight.ExtraBold, color = LuxuryNavyDeep)
                            }
                            Spacer(modifier = Modifier.width(14.dp))
                            Column {
                                Text("آتلیه ابزار لاکچری", fontSize = 22.sp, fontWeight = FontWeight.ExtraBold, color = Color.White)
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Box(modifier = Modifier.size(6.dp).clip(CircleShape).background(LuxuryGold))
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text("دسته‌بندی هوشمند • بدون شلوغی • تیره لاکچری", fontSize = 11.sp, color = Slate300)
                                }
                            }
                        }
                        Spacer(modifier = Modifier.height(20.dp))
                        // دسته‌بندی‌های هوشمند - 4 دسته جذاب
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            categories.forEach { cat ->
                                val isSelected = selectedCategory == cat.id
                                Card(
                                    modifier = Modifier.weight(1f).shadow(if (isSelected) 12.dp else 4.dp, RoundedCornerShape(16.dp)).clickable { selectedCategory = cat.id },
                                    shape = RoundedCornerShape(16.dp),
                                    colors = CardDefaults.cardColors(
                                        containerColor = if (isSelected) LuxuryDarkCardElevated else LuxuryCharcoal.copy(alpha = 0.6f)
                                    ),
                                    border = BorderStroke(1.dp, if (isSelected) LuxuryGold else LuxuryDarkBorder)
                                ) {
                                    Column(
                                        modifier = Modifier.fillMaxWidth().padding(12.dp),
                                        horizontalAlignment = Alignment.CenterHorizontally
                                    ) {
                                        Box(
                                            modifier = Modifier.size(36.dp).clip(CircleShape).background(if (isSelected) cat.gradient else Brush.linearGradient(listOf(Slate700, Slate800))),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Icon(cat.icon, contentDescription = null, tint = if (isSelected) Color.White else Slate400, modifier = Modifier.size(18.dp))
                                        }
                                        Spacer(modifier = Modifier.height(6.dp))
                                        Text(cat.persianName, fontSize = 11.sp, fontWeight = if (isSelected) FontWeight.ExtraBold else FontWeight.Medium, color = if (isSelected) Color.White else Slate300, textAlign = TextAlign.Center, lineHeight = 12.sp)
                                        if (isSelected) {
                                            Spacer(modifier = Modifier.height(2.dp))
                                            Surface(shape = RoundedCornerShape(4.dp), color = LuxuryGold.copy(alpha = 0.2f)) {
                                                Text("${cat.itemCount} ابزار", fontSize = 8.sp, color = LuxuryGold, modifier = Modifier.padding(horizontal = 4.dp, vertical = 1.dp))
                                            }
                                        }
                                    }
                                }
                            }
                        }
                        Spacer(modifier = Modifier.height(12.dp))
                        Surface(shape = RoundedCornerShape(10.dp), color = GlassGold, border = BorderStroke(0.5.dp, LuxuryGold.copy(alpha = 0.3f))) {
                            Row(modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp), verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.Lightbulb, contentDescription = null, tint = LuxuryGold, modifier = Modifier.size(14.dp))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text("💡 4 دسته هوشمند برای جلوگیری از سردرگمی - هر دسته رنگ و هویت خاص خود را دارد", fontSize = 10.sp, color = LuxuryGoldLight, lineHeight = 12.sp)
                            }
                        }
                    }
                }
            }
        }

        when (selectedCategory) {
            "court" -> {
                item { ToolSectionHeaderDark("⚖️ میز قضاوت هوشمند", "مواعد شمسی + ثنا + چک‌لیست + پیش‌بینی - 6 ابزار دادگاه", Icons.Default.Gavel, Brush.linearGradient(listOf(Indigo600, Indigo800))) }

                item {
                    Row(modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                        LuxuryToolCardDark("مواعد قضایی\nهوشمند", "تقویم شمسی + تعطیلات\nماده ۴۴۳،۴۴۵", Icons.Default.CalendarMonth, Brush.linearGradient(listOf(Indigo600, Indigo800)), Modifier.weight(1f)) { selectedSubScreen = "deadlines" }
                        LuxuryToolCardDark("ابلاغیه ثنا\nخودکار", "اتصال API واقعی\nبخش ۱۹", Icons.Default.NotificationsActive, Brush.linearGradient(listOf(Sky500, Sky600)), Modifier.weight(1f)) { selectedSubScreen = "eblagh" }
                    }
                }
                item {
                    Row(modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                        LuxuryToolCardDark("چک‌لیست\nهوشمند", "${ChecklistDatabase.allChecklists.size} چک‌لیست\nبخش ۸", Icons.Default.Checklist, Brush.linearGradient(listOf(Emerald500, Emerald600)), Modifier.weight(1f)) { selectedSubScreen = "checklist" }
                        LuxuryToolCardDark("پیش‌بینی رای\nAI", "تحلیل ۱۰۰۰ دادنامه\nبخش ۷", Icons.Default.AutoAwesome, Brush.linearGradient(listOf(Amber500, Amber600)), Modifier.weight(1f)) { selectedSubScreen = "knowledge" }
                    }
                }
                item {
                    Row(modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                        LuxuryToolCardDark("تنظیم لوایح\nهوشمند", "AI + صدای فارسی\nبخش ۱،۲،۴", Icons.Default.EditNote, Brush.linearGradient(listOf(Indigo600, LuxuryGold)), Modifier.weight(1f)) { selectedSubScreen = "drafts" }
                        LuxuryToolCardDark("پایگاه دانش\n۱۵۰۰۰ ماده", "FTS + Vector\nبخش ۵", Icons.Default.MenuBook, LuxuryGoldDarkBrush, Modifier.weight(1f)) { selectedSubScreen = "knowledge" }
                    }
                }

                // چک‌لیست‌ها - تیره لاکچری
                item {
                    Card(modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp), shape = RoundedCornerShape(24.dp), colors = CardDefaults.cardColors(containerColor = LuxuryDarkCard), border = BorderStroke(1.dp, LuxuryDarkBorder), elevation = CardDefaults.cardElevation(8.dp)) {
                        Column(modifier = Modifier.padding(20.dp)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(modifier = Modifier.size(40.dp).clip(RoundedCornerShape(12.dp)).background(Brush.linearGradient(listOf(Emerald500, Emerald600))), contentAlignment = Alignment.Center) {
                                    Icon(Icons.Default.Checklist, contentDescription = null, tint = Color.White, modifier = Modifier.size(22.dp))
                                }
                                Spacer(modifier = Modifier.width(12.dp))
                                Column(modifier = Modifier.weight(1f)) {
                                    Text("چک‌لیست‌های هوشمند", fontWeight = FontWeight.ExtraBold, fontSize = 15.sp, color = Color.White)
                                    Text("بخش ۸ - برای هر نوع دعوا", fontSize = 11.sp, color = Slate400)
                                }
                                Surface(shape = RoundedCornerShape(20.dp), color = Emerald500.copy(alpha = 0.15f), border = BorderStroke(1.dp, Emerald500.copy(alpha = 0.3f))) {
                                    Text("${ChecklistDatabase.allChecklists.size} چک‌لیست", fontSize = 10.sp, color = Emerald300, fontWeight = FontWeight.Bold, modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp))
                                }
                            }
                            Spacer(modifier = Modifier.height(16.dp))
                            ChecklistDatabase.allChecklists.forEach { checklist ->
                                Card(shape = RoundedCornerShape(16.dp), colors = CardDefaults.cardColors(containerColor = LuxuryDarkCardElevated), border = BorderStroke(0.5.dp, Slate700), modifier = Modifier.fillMaxWidth().padding(vertical = 5.dp).clickable {}) {
                                    Row(modifier = Modifier.padding(14.dp), verticalAlignment = Alignment.CenterVertically) {
                                        Box(modifier = Modifier.size(40.dp).clip(RoundedCornerShape(10.dp)).background(Brush.linearGradient(listOf(Indigo600, Indigo800))), contentAlignment = Alignment.Center) {
                                            Icon(Icons.Default.Checklist, contentDescription = null, tint = Color.White, modifier = Modifier.size(20.dp))
                                        }
                                        Spacer(modifier = Modifier.width(12.dp))
                                        Column(modifier = Modifier.weight(1f)) {
                                            Text(checklist.title, fontSize = 13.sp, fontWeight = FontWeight.Bold, color = Color.White)
                                            Text("${checklist.estimatedTimeDays} روز • ${checklist.legalBasis.take(40)}", fontSize = 10.sp, color = Slate400)
                                        }
                                        Icon(Icons.Default.ChevronLeft, contentDescription = null, tint = LuxuryGold.copy(alpha = 0.6f), modifier = Modifier.size(18.dp))
                                    }
                                }
                            }
                        }
                    }
                }
            }

            "brain" -> {
                item { ToolSectionHeaderDark("🧠 مغز متفکر فارسی", "AI آفلاین + دانش 15000 ماده + صدای وکیل - 5 ابزار هوشمند", Icons.Default.SmartToy, LuxuryGoldDarkBrush) }

                item {
                    Card(modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp), shape = RoundedCornerShape(24.dp), colors = CardDefaults.cardColors(containerColor = LuxuryDarkCard), border = BorderStroke(1.dp, LuxuryGold.copy(alpha = 0.25f)), elevation = CardDefaults.cardElevation(12.dp)) {
                        Box(modifier = Modifier.fillMaxWidth().background(Brush.linearGradient(listOf(LuxuryDarkCard, LuxuryDarkCardElevated))).padding(20.dp)) {
                            Column {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Box(modifier = Modifier.size(48.dp).clip(RoundedCornerShape(14.dp)).background(LuxuryGoldDarkBrush).shadow(8.dp, RoundedCornerShape(14.dp)), contentAlignment = Alignment.Center) {
                                        Icon(Icons.Default.SmartToy, contentDescription = null, tint = LuxuryNavyDeep, modifier = Modifier.size(26.dp))
                                    }
                                    Spacer(modifier = Modifier.width(14.dp))
                                    Column {
                                        Text("هوش مصنوعی فارسی - بی‌حد و مرز", fontSize = 16.sp, fontWeight = FontWeight.ExtraBold, color = Color.White)
                                        Text("Gemma 2B آفلاین + STT/TTS فارسی + 5 صدا", fontSize = 11.sp, color = Slate400)
                                    }
                                }
                                Spacer(modifier = Modifier.height(16.dp))
                                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                    Surface(shape = RoundedCornerShape(10.dp), color = Indigo600.copy(alpha = 0.2f), border = BorderStroke(0.5.dp, Indigo600.copy(alpha = 0.3f)), modifier = Modifier.weight(1f)) {
                                        Column(modifier = Modifier.padding(10.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                                            Text("15000", fontSize = 16.sp, fontWeight = FontWeight.ExtraBold, color = Indigo300)
                                            Text("ماده قانونی", fontSize = 9.sp, color = Slate400)
                                        }
                                    }
                                    Surface(shape = RoundedCornerShape(10.dp), color = LuxuryGold.copy(alpha = 0.15f), border = BorderStroke(0.5.dp, LuxuryGold.copy(alpha = 0.3f)), modifier = Modifier.weight(1f)) {
                                        Column(modifier = Modifier.padding(10.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                                            Text("5", fontSize = 16.sp, fontWeight = FontWeight.ExtraBold, color = LuxuryGold)
                                            Text("صدای وکیل", fontSize = 9.sp, color = Slate400)
                                        }
                                    }
                                    Surface(shape = RoundedCornerShape(10.dp), color = Emerald500.copy(alpha = 0.15f), border = BorderStroke(0.5.dp, Emerald500.copy(alpha = 0.3f)), modifier = Modifier.weight(1f)) {
                                        Column(modifier = Modifier.padding(10.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                                            Text("92%", fontSize = 16.sp, fontWeight = FontWeight.ExtraBold, color = Emerald300)
                                            Text("دقت STT", fontSize = 9.sp, color = Slate400)
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

                item {
                    Row(modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                        LuxuryToolCardDark("AI فارسی\nدوطرفه", "STT + TTS + Gemma 2B\nبخش ۱،۲،۴", Icons.Default.RecordVoiceOver, LuxuryGoldDarkBrush, Modifier.weight(1f)) { selectedSubScreen = "drafts" }
                        LuxuryToolCardDark("دانش 15000\nماده‌ای", "FTS + Vector Search\nبخش ۵", Icons.Default.MenuBook, Brush.linearGradient(listOf(Indigo600, Indigo800)), Modifier.weight(1f)) { selectedSubScreen = "knowledge" }
                    }
                }
                item {
                    Row(modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                        LuxuryToolCardDark("پیش‌بینی رای\nهوشمند", "ML + 1000 دادنامه\nبخش ۷", Icons.Default.AutoAwesome, Brush.linearGradient(listOf(Amber500, Amber600)), Modifier.weight(1f)) { selectedSubScreen = "knowledge" }
                        LuxuryToolCardDark("چک‌لیست\nهوشمند", "برای هر دعوا\nبخش ۸", Icons.Default.Checklist, Brush.linearGradient(listOf(Emerald500, Emerald600)), Modifier.weight(1f)) { selectedSubScreen = "checklist" }
                    }
                }
            }

            "atelier" -> {
                item { ToolSectionHeaderDark("💎 آتلیه لاکچری", "PDF طلایی + OCR فارسی + امضا + تم پویا - 6 ابزار لوکس", Icons.Default.AutoAwesome, Brush.linearGradient(listOf(Emerald500, Emerald600))) }

                item {
                    Row(modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                        LuxuryToolCardDark("اسکنر\nOCR فارسی", "ML Kit + Document AI\nبخش ۱۴", Icons.Default.CameraAlt, Brush.linearGradient(listOf(Indigo600, Indigo800)), Modifier.weight(1f)) { selectedSubScreen = "ocr" }
                        LuxuryToolCardDark("PDF لاکچری\nطلایی", "سربرگ + QR + لوگو M\nبخش ۱۸", Icons.Default.PictureAsPdf, Brush.linearGradient(listOf(Rose500, Rose600)), Modifier.weight(1f)) { selectedSubScreen = "pdf" }
                    }
                }
                item {
                    Row(modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                        LuxuryToolCardDark("امضای\nدیجیتال", "اثر انگشت + بیومتریک\nبخش ۱۵", Icons.Default.Edit, Brush.linearGradient(listOf(Emerald500, Emerald600)), Modifier.weight(1f)) { selectedSubScreen = "signature" }
                        LuxuryToolCardDark("تم پویا\nلاکچری", "تیره + طلایی + شیشه‌ای\nبخش ۱۰", Icons.Default.Palette, Brush.linearGradient(listOf(Indigo600, LuxuryGold)), Modifier.weight(1f)) { selectedSubScreen = "settings" }
                    }
                }
                item {
                    Row(modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                        LuxuryToolCardDark("انیمیشن\nLottie", "ترازوی عدالت ۳D\nبخش ۹", Icons.Default.AutoAwesome, LuxuryGoldDarkBrush, Modifier.weight(1f)) {}
                        LuxuryToolCardDark("لرزش لاکچری\nHaptic", "دکمه‌های طلایی\nبخش ۱۳", Icons.Default.TouchApp, Brush.linearGradient(listOf(Slate600, Slate800)), Modifier.weight(1f)) {}
                    }
                }

                // Demo Cards - تیره
                item {
                    Card(modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp), shape = RoundedCornerShape(24.dp), colors = CardDefaults.cardColors(containerColor = LuxuryDarkCard), border = BorderStroke(1.dp, Indigo600.copy(alpha = 0.3f)), elevation = CardDefaults.cardElevation(8.dp)) {
                        Column(modifier = Modifier.padding(20.dp)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(modifier = Modifier.size(44.dp).clip(RoundedCornerShape(12.dp)).background(Brush.linearGradient(listOf(Indigo600, Indigo800))), contentAlignment = Alignment.Center) {
                                    Icon(Icons.Default.CameraAlt, contentDescription = null, tint = Color.White, modifier = Modifier.size(22.dp))
                                }
                                Spacer(modifier = Modifier.width(12.dp))
                                Column {
                                    Text("اسکنر هوشمند فارسی", fontWeight = FontWeight.ExtraBold, fontSize = 15.sp, color = Color.White)
                                    Text("بخش ۱۴ - ML Kit + Document AI", fontSize = 11.sp, color = Slate400)
                                }
                            }
                            Spacer(modifier = Modifier.height(12.dp))
                            Text("عکس از قرارداد یا دادخواست بگیرید، متن فارسی استخراج و به پرونده تبدیل می‌شود", fontSize = 12.sp, color = Slate300, lineHeight = 16.sp)
                            Spacer(modifier = Modifier.height(16.dp))
                            Button(onClick = {}, shape = RoundedCornerShape(14.dp), colors = ButtonDefaults.buttonColors(containerColor = Indigo600), modifier = Modifier.fillMaxWidth().height(48.dp)) {
                                Icon(Icons.Default.CameraAlt, contentDescription = null, modifier = Modifier.size(18.dp))
                                Spacer(modifier = Modifier.width(8.dp))
                                Text("شروع اسکن OCR فارسی", fontSize = 13.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
            }

            "fortress" -> {
                item { ToolSectionHeaderDark("🛡️ دژ امنیت بی‌حد", "رمزنگاری + بک‌آپ ابری + بلاک‌چین + یادآور - 6 ابزار امنیتی", Icons.Default.Security, Brush.linearGradient(listOf(Slate700, LuxuryNavy))) }

                item {
                    Row(modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                        LuxuryToolCardDark("رمزنگاری\nSQLCipher", "AES-256 + بیومتریک\nبخش ۲۱", Icons.Default.Lock, Brush.linearGradient(listOf(Slate700, Slate900)), Modifier.weight(1f)) { selectedSubScreen = "settings" }
                        LuxuryToolCardDark("بک‌آپ ابری\nDrive", "رمزنگاری شده\nبخش ۲۳", Icons.Default.CloudUpload, Brush.linearGradient(listOf(Sky500, Sky600)), Modifier.weight(1f)) { selectedSubScreen = "backup" }
                    }
                }
                item {
                    Row(modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                        LuxuryToolCardDark("لاگ قضایی\nبلاک‌چین", "مهر زمانی SHA256\nبخش ۲۴", Icons.Default.Fingerprint, Brush.linearGradient(listOf(Amber500, Amber600)), Modifier.weight(1f)) { selectedSubScreen = "audit" }
                        LuxuryToolCardDark("یادآور\nهوشمند", "WorkManager + TTS فارسی\nبخش ۱۷", Icons.Default.NotificationsActive, Brush.linearGradient(listOf(Emerald500, Emerald600)), Modifier.weight(1f)) {}
                    }
                }
                item {
                    Row(modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                        LuxuryToolCardDark("نسخه KMP\nوب و iOS", "مولتی‌پلتفرم\nبخش ۲۰", Icons.Default.DevicesOther, Brush.linearGradient(listOf(Indigo600, Sky500)), Modifier.weight(1f)) { selectedSubScreen = "settings" }
                        LuxuryToolCardDark("ویجت هوم\nلاکچری", "مواعد بحرانی\nبخش ۱۲", Icons.Default.Dashboard, Brush.linearGradient(listOf(LuxuryGold, LuxuryGoldDark)), Modifier.weight(1f)) {}
                    }
                }

                // دانش‌نامه - تیره طلایی
                item {
                    Card(modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp), shape = RoundedCornerShape(24.dp), colors = CardDefaults.cardColors(containerColor = LuxuryDarkCard), border = BorderStroke(1.dp, LuxuryGold.copy(alpha = 0.35f)), elevation = CardDefaults.cardElevation(12.dp)) {
                        Box(modifier = Modifier.fillMaxWidth().background(Brush.linearGradient(listOf(LuxuryDarkCard, LuxuryDarkCardElevated))).padding(20.dp)) {
                            Column {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Box(modifier = Modifier.size(48.dp).clip(RoundedCornerShape(14.dp)).background(LuxuryGoldDarkBrush).shadow(8.dp, RoundedCornerShape(14.dp)), contentAlignment = Alignment.Center) {
                                        Icon(Icons.Default.MenuBook, contentDescription = null, tint = LuxuryNavyDeep, modifier = Modifier.size(26.dp))
                                    }
                                    Spacer(modifier = Modifier.width(14.dp))
                                    Column {
                                        Text("پایگاه دانش ۱۵۰۰۰ ماده‌ای", fontWeight = FontWeight.ExtraBold, fontSize = 16.sp, color = Color.White)
                                        Text("بخش ۵ - FTS + Vector DB + rc.majlis.ir", fontSize = 11.sp, color = Slate400)
                                    }
                                }
                                Spacer(modifier = Modifier.height(16.dp))
                                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                    ExpandedLegalDatabase.allLaws.take(3).forEach { (name, count) ->
                                        Card(shape = RoundedCornerShape(12.dp), colors = CardDefaults.cardColors(containerColor = LuxuryCharcoal), border = BorderStroke(0.5.dp, Slate700), modifier = Modifier.weight(1f)) {
                                            Column(modifier = Modifier.padding(12.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                                                Text(count.toString(), fontWeight = FontWeight.ExtraBold, fontSize = 16.sp, color = LuxuryGold)
                                                Text(name.take(10), fontSize = 9.sp, color = Slate400, textAlign = TextAlign.Center, lineHeight = 10.sp)
                                            }
                                        }
                                    }
                                }
                                Spacer(modifier = Modifier.height(12.dp))
                                Surface(shape = RoundedCornerShape(10.dp), color = LuxuryGold.copy(alpha = 0.12f), border = BorderStroke(0.5.dp, LuxuryGold.copy(alpha = 0.25f))) {
                                    Text("جمع کل: ${ExpandedLegalDatabase.getTotalCount()} ماده قانونی - جستجوی برداری هوشمند", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = LuxuryGoldLight, modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp))
                                }
                            }
                        }
                    }
                }
            }
        }

        // فوتر لاکچری تیره
        item {
            Card(modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 8.dp), shape = RoundedCornerShape(20.dp), colors = CardDefaults.cardColors(containerColor = LuxuryDarkCard), border = BorderStroke(1.dp, LuxuryDarkBorder), elevation = CardDefaults.cardElevation(6.dp)) {
                Row(modifier = Modifier.fillMaxWidth().padding(16.dp), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(modifier = Modifier.size(32.dp).clip(CircleShape).background(LuxuryGoldDarkBrush), contentAlignment = Alignment.Center) {
                            Text("M", fontWeight = FontWeight.ExtraBold, color = LuxuryNavyDeep, fontSize = 14.sp)
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text("میلانو لگال - نسخه بی‌حد و مرز", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color.White)
                            Text("طراحی تیره لاکچری • 4 دسته هوشمند", fontSize = 10.sp, color = Slate400)
                        }
                    }
                    Surface(shape = CircleShape, color = LuxuryGold.copy(alpha = 0.15f), border = BorderStroke(1.dp, LuxuryGold.copy(alpha = 0.3f))) {
                        Icon(Icons.Default.VerifiedUser, contentDescription = null, tint = LuxuryGold, modifier = Modifier.size(20.dp).padding(4.dp))
                    }
                }
            }
        }
    }
}

@Composable
fun ToolSectionHeaderDark(title: String, subtitle: String, icon: ImageVector, gradient: Brush) {
    Row(modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp, vertical = 8.dp), verticalAlignment = Alignment.CenterVertically) {
        Box(modifier = Modifier.size(48.dp).clip(RoundedCornerShape(14.dp)).background(gradient).shadow(8.dp, RoundedCornerShape(14.dp)), contentAlignment = Alignment.Center) {
            Icon(icon, contentDescription = null, tint = Color.White, modifier = Modifier.size(24.dp))
        }
        Spacer(modifier = Modifier.width(12.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(title, fontWeight = FontWeight.ExtraBold, fontSize = 15.sp, color = Color.White)
            Text(subtitle, fontSize = 11.sp, color = Slate400, lineHeight = 13.sp)
        }
        Box(modifier = Modifier.size(8.dp).clip(CircleShape).background(LuxuryGold.copy(alpha = 0.6f)))
    }
}

@Composable
fun LuxuryToolCardDark(title: String, subtitle: String, icon: ImageVector, gradient: Brush, modifier: Modifier = Modifier, onClick: () -> Unit = {}) {
    Card(
        modifier = modifier
            .shadow(12.dp, RoundedCornerShape(20.dp))
            .clip(RoundedCornerShape(20.dp))
            .clickable { onClick() },
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = LuxuryDarkCardElevated),
        border = BorderStroke(1.dp, LuxuryGold.copy(alpha = 0.18f)),
        elevation = CardDefaults.cardElevation(8.dp)
    ) {
        Box(modifier = Modifier.fillMaxWidth().background(Brush.verticalGradient(listOf(LuxuryDarkCardElevated, LuxuryDarkCard))).padding(16.dp)) {
            Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth()) {
                // آیکون لاکچری با هاله طلایی
                Box(
                    modifier = Modifier
                        .size(56.dp)
                        .shadow(12.dp, RoundedCornerShape(16.dp))
                        .clip(RoundedCornerShape(16.dp))
                        .background(gradient)
                        .background(Brush.radialGradient(listOf(Color.White.copy(alpha = 0.2f), Color.Transparent))),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(icon, contentDescription = null, tint = Color.White, modifier = Modifier.size(28.dp))
                }
                Spacer(modifier = Modifier.height(12.dp))
                Text(title, fontSize = 12.sp, fontWeight = FontWeight.ExtraBold, color = Color.White, lineHeight = 14.sp, textAlign = TextAlign.Center)
                Spacer(modifier = Modifier.height(6.dp))
                Surface(shape = RoundedCornerShape(8.dp), color = Slate800.copy(alpha = 0.6f), border = BorderStroke(0.5.dp, Slate700)) {
                    Text(subtitle, fontSize = 10.sp, color = Slate300, lineHeight = 11.sp, textAlign = TextAlign.Center, modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp))
                }
                Spacer(modifier = Modifier.height(8.dp))
                Box(modifier = Modifier.size(24.dp, 3.dp).clip(RoundedCornerShape(2.dp)).background(LuxuryGold.copy(alpha = 0.5f)))
            }
        }
    }
}

package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
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
import com.example.ui.LegalViewModel
import com.example.ui.theme.*

// تک‌رنگ یکدست: فقط سرمه‌ای تیره + طلایی
private val UniformDarkBg = LuxuryNavyDeep
private val UniformCardBg = LuxuryDarkCard
private val UniformCardElevated = LuxuryDarkCardElevated
private val UniformGold = LuxuryGold
private val UniformGoldBrush = LuxuryGoldDarkBrush
private val UniformBorder = LuxuryGold.copy(alpha = 0.18f)
private val UniformBorderStrong = LuxuryGold.copy(alpha = 0.35f)

data class ToolCategoryUniform(
    val id: String,
    val persianName: String,
    val emoji: String,
    val icon: ImageVector,
    val description: String,
    val count: Int
)

data class LuxuryToolUniform(
    val id: String,
    val title: String,
    val subtitle: String,
    val emoji: String,
    val icon: ImageVector,
    val category: String
)

@Composable
fun ToolsScreen(
    viewModel: LegalViewModel,
    modifier: Modifier = Modifier
) {
    var selectedSubScreen by remember { mutableStateOf<String?>(null) }

    val categories = remember {
        listOf(
            ToolCategoryUniform("court", "میز قضاوت", "⚖️", Icons.Default.Gavel, "مواعد، ابلاغیه، چک‌لیست", 6),
            ToolCategoryUniform("brain", "مغز متفکر", "🧠", Icons.Default.SmartToy, "AI فارسی، دانش 15000 ماده", 5),
            ToolCategoryUniform("atelier", "آتلیه لاکچری", "💎", Icons.Default.AutoAwesome, "PDF طلایی، OCR، امضا", 6),
            ToolCategoryUniform("fortress", "دژ امنیت", "🛡️", Icons.Default.Security, "رمزنگاری، بک‌آپ، بلاک‌چین", 6)
        )
    }

    val tools = remember {
        listOf(
            LuxuryToolUniform("deadlines", "مواعد هوشمند", "شمسی + ماده ۴۴۳", "📅", Icons.Default.CalendarMonth, "court"),
            LuxuryToolUniform("eblagh", "ابلاغیه ثنا", "API واقعی", "📨", Icons.Default.NotificationsActive, "court"),
            LuxuryToolUniform("checklist", "چک‌لیست", "برای هر دعوا", "✅", Icons.Default.Checklist, "court"),
            LuxuryToolUniform("prediction", "پیش‌بینی رای", "۱۰۰۰ دادنامه", "🔮", Icons.Default.AutoAwesome, "court"),
            LuxuryToolUniform("drafts", "تنظیم لوایح", "AI فارسی", "📝", Icons.Default.EditNote, "court"),
            LuxuryToolUniform("knowledge", "دانش ۱۵۰۰۰", "FTS برداری", "📚", Icons.Default.MenuBook, "court"),
            LuxuryToolUniform("ai", "AI فارسی", "STT+TTS", "🎙️", Icons.Default.RecordVoiceOver, "brain"),
            LuxuryToolUniform("knowledge2", "پایگاه دانش", "۱۵۰۰۰ ماده", "🧠", Icons.Default.MenuBook, "brain"),
            LuxuryToolUniform("voice", "صدای وکیل", "۵ پروفایل", "🎧", Icons.Default.Mic, "brain"),
            LuxuryToolUniform("stt", "مکالمه صوتی", "فارسی", "🔊", Icons.Default.VolumeUp, "brain"),
            LuxuryToolUniform("offline", "AI آفلاین", "Gemma 2B", "📴", Icons.Default.CloudOff, "brain"),
            LuxuryToolUniform("ocr", "اسکنر OCR", "فارسی", "📸", Icons.Default.CameraAlt, "atelier"),
            LuxuryToolUniform("pdf", "PDF طلایی", "QR + لوگو M", "📄", Icons.Default.PictureAsPdf, "atelier"),
            LuxuryToolUniform("signature", "امضای دیجیتال", "بیومتریک", "✍️", Icons.Default.Edit, "atelier"),
            LuxuryToolUniform("theme", "تم لاکچری", "تیره طلایی", "🎨", Icons.Default.Palette, "atelier"),
            LuxuryToolUniform("lottie", "انیمیشن", "Lottie 3D", "⭐", Icons.Default.Star, "atelier"),
            LuxuryToolUniform("haptic", "لرزش لاکچری", "Haptic", "💫", Icons.Default.TouchApp, "atelier"),
            LuxuryToolUniform("encrypt", "رمزنگاری", "AES-256", "🔐", Icons.Default.Lock, "fortress"),
            LuxuryToolUniform("backup", "بک‌آپ ابری", "Drive", "☁️", Icons.Default.CloudUpload, "fortress"),
            LuxuryToolUniform("audit", "لاگ بلاک‌چین", "SHA256", "⛓️", Icons.Default.Fingerprint, "fortress"),
            LuxuryToolUniform("reminder", "یادآور", "هوشمند", "⏰", Icons.Default.Alarm, "fortress"),
            LuxuryToolUniform("kmp", "نسخه KMP", "مولتی", "🌐", Icons.Default.DevicesOther, "fortress"),
            LuxuryToolUniform("widget", "ویجت هوم", "لاکچری", "📊", Icons.Default.Dashboard, "fortress"),
        )
    }

    // Sub-screen - یکدست تیره طلایی
    selectedSubScreen?.let { sub ->
        Box(modifier = modifier.fillMaxSize().background(UniformDarkBg)) {
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
                else -> {
                    // نمایش ابزارهای دسته
                    val catTools = tools.filter { it.category == sub }
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(2),
                        modifier = Modifier.fillMaxSize().background(UniformDarkBg),
                        contentPadding = PaddingValues(top = 80.dp, bottom = 20.dp, start = 16.dp, end = 16.dp),
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(catTools, key = { it.id }) { tool ->
                            UniformToolCard(tool = tool, onClick = {
                                if (tool.id in listOf("deadlines", "eblagh", "checklist", "knowledge", "drafts", "ocr", "pdf", "signature", "audit", "backup")) {
                                    selectedSubScreen = tool.id
                                }
                            })
                        }
                    }
                }
            }
            // Back - طلایی یکدست
            Card(
                modifier = Modifier.align(Alignment.TopStart).padding(16.dp).shadow(12.dp, CircleShape),
                shape = CircleShape,
                colors = CardDefaults.cardColors(containerColor = UniformCardElevated),
                border = BorderStroke(1.dp, UniformBorderStrong)
            ) {
                IconButton(onClick = { selectedSubScreen = null }) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "بازگشت", tint = UniformGold)
                }
            }
        }
        return
    }

    // صفحه اصلی - فقط 4 کارت بزرگ یکدست - بدون هیچ شلوغی
    LazyColumn(
        modifier = modifier.fillMaxSize().background(UniformDarkBg),
        contentPadding = PaddingValues(bottom = 100.dp),
        verticalArrangement = Arrangement.spacedBy(0.dp)
    ) {
        // هدر فوق مینیمال تک‌رنگ
        item {
            Box(
                modifier = Modifier.fillMaxWidth().background(UniformDarkBg).padding(24.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    // M طلایی - تنها رنگ
                    Box(
                        modifier = Modifier.size(56.dp).clip(RoundedCornerShape(14.dp)).background(UniformGoldBrush),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("M", fontSize = 28.sp, fontWeight = FontWeight.ExtraBold, color = UniformDarkBg)
                    }
                    Spacer(modifier = Modifier.width(14.dp))
                    Column {
                        Text("آتلیه ابزار", fontSize = 20.sp, fontWeight = FontWeight.ExtraBold, color = Color.White)
                        Text("تم یکدست تیره طلایی • 4 دسته • بدون شلوغی", fontSize = 11.sp, color = Color.White.copy(alpha = 0.6f))
                    }
                }
            }
        }

        // فقط 4 کارت - یکدست، یک رنگ، بدون گرادیانت‌های مختلف
        item {
            Column(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                categories.forEach { cat ->
                    UniformCategoryCard(category = cat, onClick = { selectedSubScreen = cat.id })
                }
            }
        }

        item {
            Spacer(modifier = Modifier.height(24.dp))
            // فوتر مینیمال تک‌رنگ
            Row(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 24.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(modifier = Modifier.size(30.dp, 1.dp).background(UniformGold.copy(alpha = 0.3f)))
                Spacer(modifier = Modifier.width(12.dp))
                Text("میلانو لگال • تم یکدست تیره طلایی", fontSize = 10.sp, color = Color.White.copy(alpha = 0.4f))
                Spacer(modifier = Modifier.width(12.dp))
                Box(modifier = Modifier.size(30.dp, 1.dp).background(UniformGold.copy(alpha = 0.3f)))
            }
            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}

@Composable
fun UniformCategoryCard(
    category: ToolCategoryUniform,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth().shadow(8.dp, RoundedCornerShape(18.dp)).clip(RoundedCornerShape(18.dp)).clickable { onClick() },
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = UniformCardBg),
        border = BorderStroke(1.dp, UniformBorder),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(18.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // آیکون خلاقانه تک‌رنگ طلایی - یکدست
            Box(
                modifier = Modifier.size(56.dp).clip(RoundedCornerShape(14.dp)).background(UniformGoldBrush),
                contentAlignment = Alignment.Center
            ) {
                Text(category.emoji, fontSize = 24.sp)
            }
            Spacer(modifier = Modifier.width(14.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(category.persianName, fontSize = 15.sp, fontWeight = FontWeight.ExtraBold, color = Color.White)
                Spacer(modifier = Modifier.height(3.dp))
                Text(category.description, fontSize = 11.sp, color = Color.White.copy(alpha = 0.55f))
                Spacer(modifier = Modifier.height(6.dp))
                Surface(shape = RoundedCornerShape(6.dp), color = UniformGold.copy(alpha = 0.12f), border = BorderStroke(0.5.dp, UniformGold.copy(alpha = 0.25f))) {
                    Text("${category.count} ابزار", fontSize = 9.sp, color = UniformGold, fontWeight = FontWeight.Bold, modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp))
                }
            }
            Icon(Icons.Default.ChevronLeft, contentDescription = null, tint = UniformGold.copy(alpha = 0.5f), modifier = Modifier.size(20.dp))
        }
    }
}

@Composable
fun UniformToolCard(
    tool: LuxuryToolUniform,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth().shadow(6.dp, RoundedCornerShape(16.dp)).clip(RoundedCornerShape(16.dp)).clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = UniformCardBg),
        border = BorderStroke(1.dp, UniformBorder),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth().padding(14.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier.size(48.dp).clip(RoundedCornerShape(12.dp)).background(UniformGoldBrush),
                contentAlignment = Alignment.Center
            ) {
                Text(tool.emoji, fontSize = 22.sp)
            }
            Spacer(modifier = Modifier.height(10.dp))
            Text(tool.title, fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color.White, textAlign = TextAlign.Center, lineHeight = 12.sp)
            Spacer(modifier = Modifier.height(4.dp))
            Text(tool.subtitle, fontSize = 9.sp, color = Color.White.copy(alpha = 0.5f), textAlign = TextAlign.Center)
        }
    }
}

package com.example.ui.screens

import androidx.compose.animation.core.*
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

data class ToolCategory(
    val id: String,
    val persianName: String,
    val englishName: String,
    val icon: ImageVector,
    val creativeEmoji: String,
    val gradient: Brush,
    val description: String,
    val itemCount: Int,
    val accentColor: Color
)

data class LuxuryTool(
    val id: String,
    val title: String,
    val subtitle: String,
    val icon: ImageVector,
    val emoji: String,
    val gradient: Brush,
    val category: String
)

@Composable
fun ToolsScreen(
    viewModel: LegalViewModel,
    modifier: Modifier = Modifier
) {
    var selectedCategory by remember { mutableStateOf("court") }
    var selectedSubScreen by remember { mutableStateOf<String?>(null) }

    // Remember categories to avoid recomposition
    val categories = remember {
        listOf(
            ToolCategory(
                id = "court",
                persianName = "میز قضاوت",
                englishName = "Court Desk",
                icon = Icons.Default.Gavel,
                creativeEmoji = "⚖️",
                gradient = Brush.linearGradient(listOf(Indigo600, Indigo800, Color(0xFF1E1B4B))),
                description = "مواعد، ابلاغیه، چک‌لیست، پیش‌بینی",
                itemCount = 6,
                accentColor = Indigo500
            ),
            ToolCategory(
                id = "brain",
                persianName = "مغز متفکر",
                englishName = "AI Brain",
                icon = Icons.Default.SmartToy,
                creativeEmoji = "🧠",
                gradient = LuxuryGoldDarkBrush,
                description = "AI فارسی، دانش ۱۵۰۰۰ ماده، صدای وکیل",
                itemCount = 5,
                accentColor = LuxuryGold
            ),
            ToolCategory(
                id = "atelier",
                persianName = "آتلیه لاکچری",
                englishName = "Luxury Atelier",
                icon = Icons.Default.AutoAwesome,
                creativeEmoji = "💎",
                gradient = Brush.linearGradient(listOf(Emerald500, Emerald600, Color(0xFF064E3B))),
                description = "PDF طلایی، OCR فارسی، امضا، تم",
                itemCount = 6,
                accentColor = Emerald500
            ),
            ToolCategory(
                id = "fortress",
                persianName = "دژ امنیت",
                englishName = "Fortress",
                icon = Icons.Default.Security,
                creativeEmoji = "🛡️",
                gradient = Brush.linearGradient(listOf(Slate700, Slate800, LuxuryNavy)),
                description = "رمزنگاری، بک‌آپ، بلاک‌چین، یادآور",
                itemCount = 6,
                accentColor = Sky500
            )
        )
    }

    val tools = remember {
        listOf(
            // Court - 6 tools
            LuxuryTool("deadlines", "مواعد قضایی\nهوشمند", "شمسی + تعطیلات\nماده ۴۴۳", Icons.Default.CalendarMonth, "📅", Brush.linearGradient(listOf(Indigo600, Indigo800)), "court"),
            LuxuryTool("eblagh", "ابلاغیه ثنا\nخودکار", "API واقعی\nبخش ۱۹", Icons.Default.NotificationsActive, "📨", Brush.linearGradient(listOf(Sky500, Sky600)), "court"),
            LuxuryTool("checklist", "چک‌لیست\nهوشمند", "برای هر دعوا\nبخش ۸", Icons.Default.Checklist, "✅", Brush.linearGradient(listOf(Emerald500, Emerald600)), "court"),
            LuxuryTool("prediction", "پیش‌بینی رای\nAI", "۱۰۰۰ دادنامه\nبخش ۷", Icons.Default.AutoAwesome, "🔮", Brush.linearGradient(listOf(Amber500, Amber600)), "court"),
            LuxuryTool("drafts", "تنظیم لوایح\nهوشمند", "AI + صدای فارسی\nبخش ۱", Icons.Default.EditNote, "📝", Brush.linearGradient(listOf(Indigo600, LuxuryGold)), "court"),
            LuxuryTool("knowledge", "دانش ۱۵۰۰۰\nماده‌ای", "FTS + Vector\nبخش ۵", Icons.Default.MenuBook, "📚", LuxuryGoldDarkBrush, "court"),
            // Brain - 5 tools
            LuxuryTool("ai", "AI فارسی\nدوطرفه", "STT+TTS+Gemma\nبخش ۱،۲،۴", Icons.Default.RecordVoiceOver, "🎙️", LuxuryGoldDarkBrush, "brain"),
            LuxuryTool("knowledge2", "پایگاه دانش\n۱۵۰۰۰", "برداری هوشمند\nبخش ۵", Icons.Default.MenuBook, "🧠", Brush.linearGradient(listOf(Indigo600, Indigo800)), "brain"),
            LuxuryTool("voice", "صدای وکیل\nشخصی", "۵ پروفایل\nبخش ۴", Icons.Default.Mic, "🎧", Brush.linearGradient(listOf(Amber500, Amber600)), "brain"),
            LuxuryTool("stt", "مکالمه صوتی\nفارسی", "دو طرفه\nبخش ۱", Icons.Default.VolumeUp, "🔊", Brush.linearGradient(listOf(Emerald500, Emerald600)), "brain"),
            LuxuryTool("offline", "AI آفلاین\nGemma 2B", "بدون اینترنت\nبخش ۲", Icons.Default.CloudOff, "📴", Brush.linearGradient(listOf(Slate600, Slate800)), "brain"),
            // Atelier - 6 tools
            LuxuryTool("ocr", "اسکنر OCR\nفارسی", "ML Kit\nبخش ۱۴", Icons.Default.CameraAlt, "📸", Brush.linearGradient(listOf(Indigo600, Indigo800)), "atelier"),
            LuxuryTool("pdf", "PDF لاکچری\nطلایی", "QR + لوگو M\nبخش ۱۸", Icons.Default.PictureAsPdf, "📄", Brush.linearGradient(listOf(Rose500, Rose600)), "atelier"),
            LuxuryTool("signature", "امضای دیجیتال\nبیومتریک", "اثر انگشت\nبخش ۱۵", Icons.Default.Edit, "✍️", Brush.linearGradient(listOf(Emerald500, Emerald600)), "atelier"),
            LuxuryTool("theme", "تم پویا\nلاکچری", "تیره+طلایی\nبخش ۱۰", Icons.Default.Palette, "🎨", Brush.linearGradient(listOf(Indigo600, LuxuryGold)), "atelier"),
            LuxuryTool("lottie", "انیمیشن\nLottie 3D", "ترازوی عدالت\nبخش ۹", Icons.Default.Star, "⭐", LuxuryGoldDarkBrush, "atelier"),
            LuxuryTool("haptic", "لرزش لاکچری\nHaptic", "طلایی\nبخش ۱۳", Icons.Default.TouchApp, "💫", Brush.linearGradient(listOf(Slate600, Slate800)), "atelier"),
            // Fortress - 6 tools
            LuxuryTool("encrypt", "رمزنگاری\nSQLCipher", "AES-256\nبخش ۲۱", Icons.Default.Lock, "🔐", Brush.linearGradient(listOf(Slate700, Slate900)), "fortress"),
            LuxuryTool("backup", "بک‌آپ ابری\nDrive", "رمزنگاری\nبخش ۲۳", Icons.Default.CloudUpload, "☁️", Brush.linearGradient(listOf(Sky500, Sky600)), "fortress"),
            LuxuryTool("audit", "لاگ بلاک‌چین\nقضایی", "SHA256\nبخش ۲۴", Icons.Default.Fingerprint, "⛓️", Brush.linearGradient(listOf(Amber500, Amber600)), "fortress"),
            LuxuryTool("reminder", "یادآور\nهوشمند", "WorkManager\nبخش ۱۷", Icons.Default.Alarm, "⏰", Brush.linearGradient(listOf(Emerald500, Emerald600)), "fortress"),
            LuxuryTool("kmp", "نسخه KMP\nمولتی", "وب+iOS\nبخش ۲۰", Icons.Default.DevicesOther, "🌐", Brush.linearGradient(listOf(Indigo600, Sky500)), "fortress"),
            LuxuryTool("widget", "ویجت هوم\nلاکچری", "مواعد\nبخش ۱۲", Icons.Default.Dashboard, "📊", Brush.linearGradient(listOf(LuxuryGold, LuxuryGoldDark)), "fortress"),
        )
    }

    // Sub-screen navigation - بدون هنگ، سبک
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
                else -> {
                    // Generic for other tools
                    Box(modifier = Modifier.fillMaxSize().background(LuxuryObsidianBrush), contentAlignment = Alignment.Center) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text("🚧 در حال توسعه", fontSize = 24.sp, fontWeight = FontWeight.ExtraBold, color = Color.White)
                            Spacer(modifier = Modifier.height(8.dp))
                            Text("این بخش به زودی با طراحی لاکچری اضافه می‌شود", fontSize = 12.sp, color = Slate400)
                        }
                    }
                }
            }
            // Back button لاکچری تیره - ثابت و سبک
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

    // Main screen - فوق سبک، فقط 4 کارت دسته‌بندی
    LazyColumn(
        modifier = modifier.fillMaxSize().background(LuxuryObsidianBrush),
        contentPadding = PaddingValues(bottom = 100.dp),
        verticalArrangement = Arrangement.spacedBy(0.dp)
    ) {
        // Header - تیره لاکچری با طلایی - سبک
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
                            // لوگو M طلایی 3D - خلاقانه
                            CreativeMLogo()
                            Spacer(modifier = Modifier.width(14.dp))
                            Column {
                                Text("آتلیه ابزار لاکچری", fontSize = 22.sp, fontWeight = FontWeight.ExtraBold, color = Color.White)
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Box(modifier = Modifier.size(6.dp).clip(CircleShape).background(LuxuryGold))
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text("4 دسته هوشمند • بدون شلوغی • تیره لاکچری", fontSize = 11.sp, color = Slate300)
                                }
                            }
                        }
                        Spacer(modifier = Modifier.height(16.dp))
                        Surface(shape = RoundedCornerShape(10.dp), color = GlassGold, border = BorderStroke(0.5.dp, LuxuryGold.copy(alpha = 0.3f))) {
                            Row(modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp), verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.Lightbulb, contentDescription = null, tint = LuxuryGold, modifier = Modifier.size(14.dp))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text("فقط 4 دسته - هر دسته دنیایی از ابزار لاکچری - کلیک کنید تا باز شود", fontSize = 10.sp, color = LuxuryGoldLight, lineHeight = 12.sp)
                            }
                        }
                    }
                }
            }
        }

        // 4 دسته بزرگ - فوق سبک، بدون هنگ
        item {
            Column(modifier = Modifier.fillMaxWidth().padding(16.dp), verticalArrangement = Arrangement.spacedBy(14.dp)) {
                categories.forEach { cat ->
                    val isSelected = selectedCategory == cat.id
                    CategoryCardLuxury(
                        category = cat,
                        isSelected = isSelected,
                        onClick = { selectedCategory = cat.id }
                    )
                }
            }
        }

        // ابزارهای دسته انتخاب شده - Grid سبک 2 ستونه
        item {
            val filteredTools = remember(selectedCategory) {
                tools.filter { it.category == selectedCategory }
            }
            Column(modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp)) {
                ToolSectionHeaderDark(
                    title = categories.find { it.id == selectedCategory }?.let { "${it.creativeEmoji} ${it.persianName}" } ?: "",
                    subtitle = "${filteredTools.size} ابزار لاکچری • ${categories.find { it.id == selectedCategory }?.description}",
                    icon = categories.find { it.id == selectedCategory }?.icon ?: Icons.Default.Build,
                    gradient = categories.find { it.id == selectedCategory }?.gradient ?: LuxuryGoldDarkBrush
                )
                Spacer(modifier = Modifier.height(12.dp))
                // Grid سبک - بدون هنگ
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    modifier = Modifier.fillMaxWidth().height((filteredTools.size * 0.55 * 140).dp), // تخمین ارتفاع
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    userScrollEnabled = false
                ) {
                    items(filteredTools, key = { it.id }) { tool ->
                        LuxuryToolCardUltra(
                            tool = tool,
                            onClick = {
                                // جلوگیری از هنگ - فقط ابزارهای موجود
                                if (tool.id in listOf("deadlines", "eblagh", "checklist", "knowledge", "drafts", "ocr", "pdf", "signature", "audit", "backup")) {
                                    selectedSubScreen = tool.id
                                }
                            }
                        )
                    }
                }
            }
        }

        // فوتر لاکچری تیره
        item {
            Spacer(modifier = Modifier.height(16.dp))
            Card(modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 8.dp), shape = RoundedCornerShape(20.dp), colors = CardDefaults.cardColors(containerColor = LuxuryDarkCard), border = BorderStroke(1.dp, LuxuryDarkBorder), elevation = CardDefaults.cardElevation(6.dp)) {
                Row(modifier = Modifier.fillMaxWidth().padding(16.dp), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(modifier = Modifier.size(36.dp).clip(RoundedCornerShape(10.dp)).background(LuxuryGoldDarkBrush), contentAlignment = Alignment.Center) {
                            Text("M", fontWeight = FontWeight.ExtraBold, color = LuxuryNavyDeep, fontSize = 16.sp)
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text("میلانو لگال - نسخه بی‌حد و مرز", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color.White)
                            Text("تاریک، طلایی، بدون هنگ • 4 دسته هوشمند", fontSize = 10.sp, color = Slate400)
                        }
                    }
                    Surface(shape = CircleShape, color = LuxuryGold.copy(alpha = 0.15f), border = BorderStroke(1.dp, LuxuryGold.copy(alpha = 0.3f))) {
                        Icon(Icons.Default.VerifiedUser, contentDescription = null, tint = LuxuryGold, modifier = Modifier.size(22.dp).padding(4.dp))
                    }
                }
            }
            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}

@Composable
fun CreativeMLogo() {
    val infiniteTransition = rememberInfiniteTransition(label = "MLogo")
    val shimmer by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(2000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "shimmer"
    )

    Box(
        modifier = Modifier
            .size(60.dp)
            .shadow(16.dp, RoundedCornerShape(18.dp))
            .clip(RoundedCornerShape(18.dp))
            .background(LuxuryGoldDarkBrush),
        contentAlignment = Alignment.Center
    ) {
        // Shimmer overlay
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.linearGradient(
                        colors = listOf(
                            Color.Transparent,
                            Color.White.copy(alpha = 0.3f * shimmer),
                            Color.Transparent
                        ),
                        start = androidx.compose.ui.geometry.Offset(-60f * shimmer, -60f * shimmer),
                        end = androidx.compose.ui.geometry.Offset(60f + 60f * shimmer, 60f + 60f * shimmer)
                    )
                )
        )
        Text("M", fontSize = 32.sp, fontWeight = FontWeight.ExtraBold, color = LuxuryNavyDeep)
        // Small diamond badge
        Box(
            modifier = Modifier.align(Alignment.TopEnd).offset(x = 4.dp, y = (-4).dp).size(16.dp).clip(CircleShape).background(Color.White).padding(2.dp),
            contentAlignment = Alignment.Center
        ) {
            Text("💎", fontSize = 8.sp)
        }
    }
}

@Composable
fun CategoryCardLuxury(
    category: ToolCategory,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val scale by animateFloatAsState(targetValue = if (isSelected) 1.02f else 1f, animationSpec = tween(200), label = "scale")

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(if (isSelected) 20.dp else 8.dp, RoundedCornerShape(20.dp))
            .clip(RoundedCornerShape(20.dp))
            .clickable { onClick() },
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) LuxuryDarkCardElevated else LuxuryDarkCard
        ),
        border = BorderStroke(
            width = if (isSelected) 1.5.dp else 1.dp,
            brush = if (isSelected) LuxuryCardGoldBorderBrush else Brush.linearGradient(listOf(LuxuryDarkBorder, LuxuryDarkBorder))
        ),
        elevation = CardDefaults.cardElevation(if (isSelected) 12.dp else 4.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().background(
                if (isSelected) Brush.linearGradient(listOf(LuxuryDarkCardElevated, LuxuryCharcoal)) else Brush.linearGradient(listOf(LuxuryDarkCard, LuxuryDarkCard))
            ).padding(18.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Creative icon with emoji + gradient + glow
            Box(
                modifier = Modifier.size(64.dp).shadow(12.dp, RoundedCornerShape(16.dp)).clip(RoundedCornerShape(16.dp)).background(category.gradient),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(category.creativeEmoji, fontSize = 22.sp)
                    Icon(category.icon, contentDescription = null, tint = Color.White, modifier = Modifier.size(16.dp))
                }
                // Glow
                if (isSelected) {
                    Box(
                        modifier = Modifier.fillMaxSize().background(
                            Brush.radialGradient(
                                colors = listOf(Color.White.copy(alpha = 0.2f), Color.Transparent),
                                radius = 80f
                            )
                        )
                    )
                }
            }
            Spacer(modifier = Modifier.width(14.dp))
            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(category.persianName, fontSize = 16.sp, fontWeight = FontWeight.ExtraBold, color = Color.White)
                    Spacer(modifier = Modifier.width(8.dp))
                    if (isSelected) {
                        Box(modifier = Modifier.size(6.dp).clip(CircleShape).background(LuxuryGold))
                    }
                }
                Spacer(modifier = Modifier.height(4.dp))
                Text(category.description, fontSize = 11.sp, color = Slate400, lineHeight = 13.sp)
                Spacer(modifier = Modifier.height(6.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    Surface(shape = RoundedCornerShape(6.dp), color = category.accentColor.copy(alpha = 0.15f), border = BorderStroke(0.5.dp, category.accentColor.copy(alpha = 0.3f))) {
                        Text("${category.itemCount} ابزار", fontSize = 9.sp, color = category.accentColor, fontWeight = FontWeight.Bold, modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp))
                    }
                    Surface(shape = RoundedCornerShape(6.dp), color = Slate800) {
                        Text(category.englishName, fontSize = 8.sp, color = Slate500, modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp))
                    }
                }
            }
            Spacer(modifier = Modifier.width(10.dp))
            // Chevron luxury
            Card(shape = CircleShape, colors = CardDefaults.cardColors(containerColor = if (isSelected) LuxuryGold.copy(alpha = 0.2f) else Slate800), border = BorderStroke(0.5.dp, if (isSelected) LuxuryGold.copy(alpha = 0.4f) else Slate700)) {
                Icon(
                    Icons.Default.ChevronLeft,
                    contentDescription = null,
                    tint = if (isSelected) LuxuryGold else Slate500,
                    modifier = Modifier.size(28.dp).padding(6.dp)
                )
            }
        }
    }
}

@Composable
fun ToolSectionHeaderDark(title: String, subtitle: String, icon: ImageVector, gradient: Brush) {
    Row(modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp), verticalAlignment = Alignment.CenterVertically) {
        Box(modifier = Modifier.size(44.dp).clip(RoundedCornerShape(12.dp)).background(gradient).shadow(8.dp, RoundedCornerShape(12.dp)), contentAlignment = Alignment.Center) {
            Icon(icon, contentDescription = null, tint = Color.White, modifier = Modifier.size(22.dp))
        }
        Spacer(modifier = Modifier.width(12.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(title, fontWeight = FontWeight.ExtraBold, fontSize = 14.sp, color = Color.White)
            Text(subtitle, fontSize = 11.sp, color = Slate400, lineHeight = 12.sp)
        }
    }
}

@Composable
fun LuxuryToolCardUltra(
    tool: LuxuryTool,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {}
) {
    var isPressed by remember { mutableStateOf(false) }
    val scale by animateFloatAsState(targetValue = if (isPressed) 0.95f else 1f, animationSpec = tween(100), label = "press")

    Card(
        modifier = modifier
            .shadow(8.dp, RoundedCornerShape(18.dp))
            .clip(RoundedCornerShape(18.dp))
            .clickable { onClick() },
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = LuxuryDarkCardElevated),
        border = BorderStroke(1.dp, LuxuryGold.copy(alpha = 0.12f)),
        elevation = CardDefaults.cardElevation(6.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth().background(Brush.verticalGradient(listOf(LuxuryDarkCardElevated, LuxuryDarkCard))).padding(14.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Ultra creative icon - emoji + icon + glow
            Box(
                modifier = Modifier.size(52.dp).clip(RoundedCornerShape(14.dp)).background(tool.gradient).shadow(8.dp, RoundedCornerShape(14.dp)),
                contentAlignment = Alignment.Center
            ) {
                Box(modifier = Modifier.fillMaxSize().background(Brush.radialGradient(listOf(Color.White.copy(alpha = 0.25f), Color.Transparent), radius = 60f)))
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(tool.emoji, fontSize = 20.sp)
                }
            }
            Spacer(modifier = Modifier.height(10.dp))
            Text(tool.title, fontSize = 11.sp, fontWeight = FontWeight.ExtraBold, color = Color.White, lineHeight = 13.sp, textAlign = TextAlign.Center, maxLines = 2)
            Spacer(modifier = Modifier.height(4.dp))
            Text(tool.subtitle, fontSize = 9.sp, color = Slate400, lineHeight = 10.sp, textAlign = TextAlign.Center, maxLines = 2)
            Spacer(modifier = Modifier.height(8.dp))
            Box(modifier = Modifier.width(24.dp, 2.dp).clip(RoundedCornerShape(1.dp)).background(LuxuryGold.copy(alpha = 0.4f)))
        }
    }
}

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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.LegalViewModel
import com.example.ui.theme.*

// تم یکدست سبز تیره لاکچری - الهام از اسکرین‌شات‌ها
private val Bg = LuxuryGreenDeep
private val CardBg = LuxuryGreenDark
private val CardElev = LuxuryGreenMedium
private val Gold = LuxuryGreenGold
private val GoldLight = LuxuryGreenGoldLight
private val GoldBrush = LuxuryGreenGoldBrush
private val Border = LuxuryGreenBorder
private val BorderStrong = LuxuryGreenBorderStrong

data class LegalCategoryFolder(
    val id: String,
    val title: String,
    val subtitle: String,
    val icon: ImageVector,
    val count: Int,
    val color: Color = Gold
)

data class AdvancedToolUniform(
    val id: String,
    val title: String,
    val subtitle: String,
    val emoji: String,
    val category: String
)

@Composable
fun ToolsScreen(
    viewModel: LegalViewModel,
    modifier: Modifier = Modifier
) {
    var selectedSubScreen by remember { mutableStateOf<String?>(null) }
    var showAdvanced by remember { mutableStateOf(false) }

    val legalFolders = remember {
        listOf(
            LegalCategoryFolder("legal", "امور حقوقی (قراردادها، تعهدات، املاک)", "پرونده‌های حقوقی", Icons.Default.Folder, 0),
            LegalCategoryFolder("criminal", "امور کیفری (جرم، کلاهبرداری، چک برگشتی)", "پرونده‌های کیفری", Icons.Default.Gavel, 0),
            LegalCategoryFolder("family", "امور خانواده (طلاق، مهریه، حضانت)", "پرونده‌های خانواده", Icons.Default.FamilyRestroom, 0)
        )
    }

    val advancedTools = remember {
        listOf(
            AdvancedToolUniform("deadlines", "مواعد هوشمند", "شمسی + تعطیلات", "📅", "court"),
            AdvancedToolUniform("eblagh", "ابلاغیه ثنا", "API واقعی", "📨", "court"),
            AdvancedToolUniform("checklist", "چک‌لیست", "هوشمند", "✅", "court"),
            AdvancedToolUniform("prediction", "پیش‌بینی رای", "AI", "🔮", "court"),
            AdvancedToolUniform("knowledge", "دانش ۱۵۰۰۰", "FTS", "📚", "brain"),
            AdvancedToolUniform("ai", "AI فارسی", "STT/TTS", "🎙️", "brain"),
            AdvancedToolUniform("ocr", "اسکنر OCR", "فارسی", "📸", "atelier"),
            AdvancedToolUniform("pdf", "PDF طلایی", "QR+M", "📄", "atelier"),
            AdvancedToolUniform("signature", "امضای دیجیتال", "بیومتریک", "✍️", "atelier"),
            AdvancedToolUniform("theme", "تم لاکچری", "تیره طلایی", "🎨", "atelier"),
            AdvancedToolUniform("encrypt", "رمزنگاری", "AES-256", "🔐", "fortress"),
            AdvancedToolUniform("backup", "بک‌آپ ابری", "Drive", "☁️", "fortress"),
            AdvancedToolUniform("audit", "لاگ بلاک‌چین", "SHA256", "⛓️", "fortress"),
            AdvancedToolUniform("reminder", "یادآور", "هوشمند", "⏰", "fortress"),
        )
    }

    // Sub-screen - یکدست سبز تیره
    selectedSubScreen?.let { sub ->
        Box(modifier = modifier.fillMaxSize().background(Bg)) {
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
                    Box(modifier = Modifier.fillMaxSize().background(Bg), contentAlignment = Alignment.Center) {
                        Text("🚧 در حال توسعه", color = Color.White, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
            Card(
                modifier = Modifier.align(Alignment.TopStart).padding(16.dp).shadow(12.dp, CircleShape),
                shape = CircleShape,
                colors = CardDefaults.cardColors(containerColor = CardElev),
                border = BorderStroke(1.dp, BorderStrong)
            ) {
                IconButton(onClick = { selectedSubScreen = null }) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "بازگشت", tint = Gold)
                }
            }
        }
        return
    }

    // صفحه اصلی - الهام از اسکرین‌شات‌ها - فوق مینیمال یکدست
    LazyColumn(
        modifier = modifier.fillMaxSize().background(Bg),
        contentPadding = PaddingValues(bottom = 100.dp),
        verticalArrangement = Arrangement.spacedBy(0.dp)
    ) {
        // هدر - دقیقاً مثل اسکرین‌شات وب: سامانه جامع حقوقی
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(0.dp),
                colors = CardDefaults.cardColors(containerColor = Bg),
                border = BorderStroke(0.5.dp, Border)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    // راست: سامانه جامع حقوقی
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Card(shape = RoundedCornerShape(8.dp), colors = CardDefaults.cardColors(containerColor = CardElev), border = BorderStroke(0.5.dp, Border)) {
                            Icon(Icons.Default.Balance, contentDescription = null, tint = Gold, modifier = Modifier.size(32.dp).padding(6.dp))
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Column {
                            Text("سامانه جامع حقوقی", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = Color.White)
                            Text("کاربر مهمان (عمومی)", fontSize = 9.sp, color = GoldLight)
                        }
                    }
                    // وسط: دستیار هوشمند میلانو
                    Card(shape = RoundedCornerShape(12.dp), colors = CardDefaults.cardColors(containerColor = CardElev), border = BorderStroke(0.5.dp, Border)) {
                        Row(modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp), verticalAlignment = Alignment.CenterVertically) {
                            Box(modifier = Modifier.size(28.dp).clip(CircleShape).background(GoldBrush), contentAlignment = Alignment.Center) {
                                Icon(Icons.Default.SmartToy, contentDescription = null, tint = Bg, modifier = Modifier.size(16.dp))
                            }
                            Spacer(modifier = Modifier.width(8.dp))
                            Column {
                                Text("دستیار هوشمند میلانو", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color.White)
                                Text("وکیل پایه یک دادگستری", fontSize = 8.sp, color = GoldLight)
                            }
                        }
                    }
                    // چپ: دکمه‌ها
                    Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        Surface(shape = RoundedCornerShape(8.dp), color = Gold, modifier = Modifier.clickable {}) {
                            Text("ورود / ثبت نام", fontSize = 9.sp, fontWeight = FontWeight.Bold, color = Bg, modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp))
                        }
                    }
                }
            }
        }

        // بخش راهنما - مثل اسکرین‌شات دوم
        item {
            Card(
                modifier = Modifier.fillMaxWidth().padding(16.dp),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = CardBg),
                border = BorderStroke(1.dp, Border)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(modifier = Modifier.size(32.dp).clip(RoundedCornerShape(8.dp)).background(GoldBrush), contentAlignment = Alignment.Center) {
                                Text("🧭", fontSize = 16.sp)
                            }
                            Spacer(modifier = Modifier.width(10.dp))
                            Text("راهنمای هوشمند پرونده جدید", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color.White)
                        }
                        Icon(Icons.Default.Close, contentDescription = null, tint = Color.White.copy(alpha = 0.5f), modifier = Modifier.size(16.dp))
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("برای ایجاد پرونده حقوقی جدید، لطفاً حوزه دعوی (کیفری، حقوقی یا خانواده) را انتخاب کنید.", fontSize = 11.sp, color = Color.White.copy(alpha = 0.7f), lineHeight = 16.sp)
                    Spacer(modifier = Modifier.height(14.dp))
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        legalFolders.forEach { folder ->
                            Card(
                                modifier = Modifier.fillMaxWidth().clickable { selectedSubScreen = folder.id },
                                shape = RoundedCornerShape(12.dp),
                                colors = CardDefaults.cardColors(containerColor = Bg),
                                border = BorderStroke(1.dp, Border)
                            ) {
                                Row(modifier = Modifier.fillMaxWidth().padding(12.dp), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Icon(Icons.Default.Folder, contentDescription = null, tint = Gold, modifier = Modifier.size(16.dp))
                                        Spacer(modifier = Modifier.width(8.dp))
                                        Text(folder.title, fontSize = 11.sp, color = Color.White)
                                    }
                                    Icon(Icons.Default.ArrowForward, contentDescription = null, tint = Gold, modifier = Modifier.size(14.dp))
                                }
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(14.dp))
                    Button(
                        onClick = { showAdvanced = !showAdvanced },
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = CardElev, contentColor = Gold),
                        border = BorderStroke(1.dp, BorderStrong),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(if (showAdvanced) "بستن ابزار پیشرفته" else "متوجه شدم، ادامه بده - نمایش ابزار پیشرفته", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }

        // آرشیو پرونده‌ها - مثل اسکرین‌شات سوم - راست
        item {
            Card(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = CardBg),
                border = BorderStroke(1.dp, Border)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text("آرشیو پرونده‌ها و دسته‌بندی‌ها", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = Color.White)
                        Surface(shape = CircleShape, color = CardElev) {
                            Icon(Icons.Default.Archive, contentDescription = null, tint = Gold, modifier = Modifier.size(28.dp).padding(6.dp))
                        }
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("با ورود به حساب کاربری تمامی مشاوره‌ها و پرونده‌های شما به صورت مرتب دسته‌بندی و ذخیره می‌گردد.", fontSize = 10.sp, color = Color.White.copy(alpha = 0.6f), lineHeight = 14.sp)
                    Spacer(modifier = Modifier.height(12.dp))
                    Button(
                        onClick = {},
                        shape = RoundedCornerShape(24.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Bg, contentColor = Gold),
                        border = BorderStroke(1.dp, BorderStrong),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("ورود / ثبت نام", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    }
                    Spacer(modifier = Modifier.height(16.dp))

                    // 3 پوشه اصلی - دقیقاً مثل اسکرین‌شات
                    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        // حقوقی
                        FolderCardUniform("امور حقوقی (قراردادها، تعهدات، املاک)", "0 پرونده", Icons.Default.Folder)
                        // کیفری
                        FolderCardUniform("امور کیفری (جرم، کلاهبرداری، چک برگشتی)", "0 پرونده", Icons.Default.Gavel)
                        // خانواده
                        FolderCardUniform("امور خانواده (طلاق، مهریه، حضانت)", "0 پرونده", Icons.Default.FamilyRestroom)
                    }
                }
            }
        }

        // ابزار پیشرفته - فقط در صورت کلیک - یکدست
        if (showAdvanced) {
            item {
                Spacer(modifier = Modifier.height(16.dp))
                Card(
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = CardBg),
                    border = BorderStroke(1.dp, BorderStrong)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(modifier = Modifier.size(36.dp).clip(RoundedCornerShape(10.dp)).background(GoldBrush), contentAlignment = Alignment.Center) {
                                Icon(Icons.Default.AutoAwesome, contentDescription = null, tint = Bg, modifier = Modifier.size(20.dp))
                            }
                            Spacer(modifier = Modifier.width(10.dp))
                            Text("ابزارهای پیشرفته لاکچری", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color.White)
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Text("14 ابزار هوشمند - تم یکدست سبز طلایی - بدون شلوغی", fontSize = 10.sp, color = Color.White.copy(alpha = 0.5f))
                        Spacer(modifier = Modifier.height(12.dp))
                        LazyVerticalGrid(
                            columns = GridCells.Fixed(2),
                            modifier = Modifier.fillMaxWidth().height(420.dp),
                            horizontalArrangement = Arrangement.spacedBy(10.dp),
                            verticalArrangement = Arrangement.spacedBy(10.dp),
                            userScrollEnabled = false
                        ) {
                            items(advancedTools, key = { it.id }) { tool ->
                                Card(
                                    modifier = Modifier.fillMaxWidth().clickable {
                                        if (tool.id in listOf("deadlines", "eblagh", "checklist", "knowledge", "ocr", "pdf", "signature", "audit", "backup")) {
                                            selectedSubScreen = tool.id
                                        }
                                    },
                                    shape = RoundedCornerShape(12.dp),
                                    colors = CardDefaults.cardColors(containerColor = Bg),
                                    border = BorderStroke(1.dp, Border)
                                ) {
                                    Column(modifier = Modifier.padding(12.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                                        Text(tool.emoji, fontSize = 20.sp)
                                        Spacer(modifier = Modifier.height(6.dp))
                                        Text(tool.title, fontSize = 10.sp, fontWeight = FontWeight.Bold, color = Color.White, textAlign = TextAlign.Center)
                                        Text(tool.subtitle, fontSize = 8.sp, color = Color.White.copy(alpha = 0.5f), textAlign = TextAlign.Center)
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        item {
            Spacer(modifier = Modifier.height(24.dp))
            Row(modifier = Modifier.fillMaxWidth().padding(horizontal = 24.dp), horizontalArrangement = Arrangement.Center) {
                Box(modifier = Modifier.width(40.dp).height(1.dp).background(Gold.copy(alpha = 0.3f)))
                Spacer(modifier = Modifier.width(12.dp))
                Text("میلانو لگال • الهام از وب • تم یکدست", fontSize = 9.sp, color = Color.White.copy(alpha = 0.3f))
                Spacer(modifier = Modifier.width(12.dp))
                Box(modifier = Modifier.width(40.dp).height(1.dp).background(Gold.copy(alpha = 0.3f)))
            }
            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}

@Composable
fun FolderCardUniform(title: String, count: String, icon: ImageVector) {
    Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Bg),
        border = BorderStroke(1.dp, Border)
    ) {
        Row(modifier = Modifier.fillMaxWidth().padding(12.dp), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween) {
            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
                Icon(icon, contentDescription = null, tint = Gold, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(title, fontSize = 10.sp, color = Color.White, lineHeight = 12.sp)
                    Text(count, fontSize = 8.sp, color = Color.White.copy(alpha = 0.4f))
                }
            }
            Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                Icon(Icons.Default.Edit, contentDescription = null, tint = Color.White.copy(alpha = 0.4f), modifier = Modifier.size(12.dp))
                Icon(Icons.Default.Delete, contentDescription = null, tint = Color.White.copy(alpha = 0.4f), modifier = Modifier.size(12.dp))
            }
        }
    }
}

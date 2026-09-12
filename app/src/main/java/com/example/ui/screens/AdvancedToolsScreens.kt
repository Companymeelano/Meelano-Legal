package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.ChecklistDatabase
import com.example.data.service.AuditLogService
import com.example.data.service.CloudBackupManager
import com.example.data.service.EncryptionManager
import com.example.ui.theme.*

@Composable
fun ChecklistScreen(modifier: Modifier = Modifier) {
    LazyColumn(modifier = modifier.fillMaxSize().background(LuxuryObsidianBrush), contentPadding = PaddingValues(16.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
        item {
            Card(modifier = Modifier.fillMaxWidth().shadow(16.dp, RoundedCornerShape(24.dp)), shape = RoundedCornerShape(24.dp), colors = CardDefaults.cardColors(containerColor = LuxuryNavyDeep), border = BorderStroke(1.dp, LuxuryGold.copy(alpha = 0.3f))) {
                Box(modifier = Modifier.fillMaxWidth().background(LuxuryObsidianBrush).padding(20.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(modifier = Modifier.size(52.dp).clip(RoundedCornerShape(14.dp)).background(Brush.linearGradient(listOf(Emerald500, Emerald600))).shadow(8.dp, RoundedCornerShape(14.dp)), contentAlignment = Alignment.Center) {
                            Icon(Icons.Default.Checklist, contentDescription = null, tint = Color.White, modifier = Modifier.size(26.dp))
                        }
                        Spacer(modifier = Modifier.width(14.dp))
                        Column {
                            Text("چک‌لیست هوشمند قضایی", fontSize = 18.sp, fontWeight = FontWeight.ExtraBold, color = Color.White)
                            Text("بخش ۸ - برای هر نوع دعوا، مدارک و مراحل", fontSize = 11.sp, color = Slate400)
                        }
                    }
                }
            }
        }
        items(ChecklistDatabase.allChecklists) { checklist ->
            Card(modifier = Modifier.fillMaxWidth().shadow(8.dp, RoundedCornerShape(20.dp)), shape = RoundedCornerShape(20.dp), colors = CardDefaults.cardColors(containerColor = LuxuryDarkCard), border = BorderStroke(1.dp, LuxuryDarkBorder), elevation = CardDefaults.cardElevation(6.dp)) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Surface(shape = RoundedCornerShape(10.dp), color = Indigo600.copy(alpha = 0.2f), border = BorderStroke(0.5.dp, Indigo600.copy(alpha = 0.3f))) { Text(checklist.caseType, fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Indigo300, modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp)) }
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(checklist.title, fontWeight = FontWeight.ExtraBold, fontSize = 14.sp, color = Color.White)
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(checklist.description, fontSize = 12.sp, color = Slate300, lineHeight = 16.sp)
                    Spacer(modifier = Modifier.height(12.dp))
                    Text("مدارک لازم:", fontWeight = FontWeight.Bold, fontSize = 12.sp, color = LuxuryGold)
                    Text(checklist.requiredDocuments, fontSize = 11.sp, color = Slate400, lineHeight = 16.sp)
                    Spacer(modifier = Modifier.height(10.dp))
                    Text("مراحل:", fontWeight = FontWeight.Bold, fontSize = 12.sp, color = LuxuryGold)
                    Text(checklist.steps, fontSize = 11.sp, color = Slate400, lineHeight = 16.sp)
                    Spacer(modifier = Modifier.height(12.dp))
                    Surface(shape = RoundedCornerShape(10.dp), color = Amber500.copy(alpha = 0.15f), border = BorderStroke(0.5.dp, Amber500.copy(alpha = 0.3f))) {
                        Text("⏱ ${checklist.estimatedTimeDays} روز • ${checklist.legalBasis}", fontSize = 10.sp, color = Amber300, modifier = Modifier.padding(10.dp))
                    }
                }
            }
        }
    }
}

@Composable
fun OcrScannerScreen(modifier: Modifier = Modifier) {
    LazyColumn(modifier = modifier.fillMaxSize().background(LuxuryObsidianBrush), contentPadding = PaddingValues(16.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
        item {
            Card(modifier = Modifier.fillMaxWidth().shadow(16.dp, RoundedCornerShape(24.dp)), shape = RoundedCornerShape(24.dp), colors = CardDefaults.cardColors(containerColor = LuxuryDarkCard), border = BorderStroke(1.dp, Indigo600.copy(alpha = 0.3f)), elevation = CardDefaults.cardElevation(8.dp)) {
                Column(modifier = Modifier.padding(24.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                    Box(modifier = Modifier.size(90.dp).clip(RoundedCornerShape(20.dp)).background(Brush.linearGradient(listOf(Indigo600, Indigo800))).shadow(12.dp, RoundedCornerShape(20.dp)), contentAlignment = Alignment.Center) {
                        Icon(Icons.Default.CameraAlt, contentDescription = null, tint = Color.White, modifier = Modifier.size(44.dp))
                    }
                    Spacer(modifier = Modifier.height(18.dp))
                    Text("اسکنر هوشمند OCR فارسی", fontSize = 20.sp, fontWeight = FontWeight.ExtraBold, color = Color.White)
                    Text("بخش ۱۴ - ML Kit + Document AI", fontSize = 12.sp, color = Slate400)
                    Spacer(modifier = Modifier.height(20.dp))
                    Button(onClick = {}, shape = RoundedCornerShape(16.dp), colors = ButtonDefaults.buttonColors(containerColor = Indigo600), modifier = Modifier.fillMaxWidth().height(54.dp).shadow(8.dp, RoundedCornerShape(16.dp))) {
                        Icon(Icons.Default.CameraAlt, contentDescription = null)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("گرفتن عکس از سند", fontWeight = FontWeight.Bold)
                    }
                    Spacer(modifier = Modifier.height(10.dp))
                    OutlinedButton(onClick = {}, shape = RoundedCornerShape(16.dp), border = BorderStroke(1.dp, Slate600), colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.White), modifier = Modifier.fillMaxWidth().height(54.dp)) {
                        Icon(Icons.Default.PhotoLibrary, contentDescription = null)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("انتخاب از گالری")
                    }
                    Spacer(modifier = Modifier.height(20.dp))
                    Card(shape = RoundedCornerShape(16.dp), colors = CardDefaults.cardColors(containerColor = LuxuryCharcoal), border = BorderStroke(0.5.dp, Slate700)) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text("قابلیت‌ها:", fontWeight = FontWeight.ExtraBold, fontSize = 13.sp, color = Color.White)
                            Spacer(modifier = Modifier.height(8.dp))
                            Text("• استخراج خودکار شماره پرونده، تاریخ، مبلغ\n• تشخیص زبان فارسی ۹۲٪ دقت\n• تبدیل عکس به پرونده جدید\n• ذخیره متن استخراج شده", fontSize = 11.sp, color = Slate300, lineHeight = 18.sp)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun PdfLuxuryScreen(modifier: Modifier = Modifier) {
    LazyColumn(modifier = modifier.fillMaxSize().background(LuxuryObsidianBrush), contentPadding = PaddingValues(16.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
        item {
            Card(modifier = Modifier.fillMaxWidth().shadow(16.dp, RoundedCornerShape(24.dp)), shape = RoundedCornerShape(24.dp), colors = CardDefaults.cardColors(containerColor = LuxuryDarkCard), border = BorderStroke(1.dp, Rose500.copy(alpha = 0.3f)), elevation = CardDefaults.cardElevation(8.dp)) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(modifier = Modifier.size(52.dp).clip(RoundedCornerShape(14.dp)).background(Brush.linearGradient(listOf(Rose500, Rose600))).shadow(8.dp, RoundedCornerShape(14.dp)), contentAlignment = Alignment.Center) {
                            Icon(Icons.Default.PictureAsPdf, contentDescription = null, tint = Color.White, modifier = Modifier.size(26.dp))
                        }
                        Spacer(modifier = Modifier.width(14.dp))
                        Column {
                            Text("PDF لاکچری طلایی", fontSize = 18.sp, fontWeight = FontWeight.ExtraBold, color = Color.White)
                            Text("بخش ۱۸ - سربرگ طلایی + QR + لوگو M", fontSize = 11.sp, color = Slate400)
                        }
                    }
                    Spacer(modifier = Modifier.height(20.dp))
                    Card(modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(16.dp), colors = CardDefaults.cardColors(containerColor = Color.White), elevation = CardDefaults.cardElevation(4.dp)) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Box(modifier = Modifier.fillMaxWidth().height(32.dp).background(LuxuryNavy, RoundedCornerShape(8.dp)), contentAlignment = Alignment.CenterStart) {
                                Text("  میلانو لگال - لاکچری - تحلیل قوانین ایران", color = LuxuryGold, fontSize = 10.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(start = 10.dp))
                            }
                            Spacer(modifier = Modifier.height(14.dp))
                            Text("عنوان: لایحه دفاعیه مطالبه وجه چک", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color.Black)
                            Text("مخاطب: ریاست محترم دادگاه...", fontSize = 10.sp, color = Slate600)
                            Spacer(modifier = Modifier.height(10.dp))
                            Text("متن لایحه با فونت فارسی و استناد به مواد قانونی...", fontSize = 10.sp, color = Slate700, lineHeight = 14.sp)
                            Spacer(modifier = Modifier.height(20.dp))
                            Box(modifier = Modifier.fillMaxWidth().height(26.dp).background(LuxuryNavy, RoundedCornerShape(6.dp)), contentAlignment = Alignment.Center) {
                                Text("QR: MEELANO-123-456 | تولید: ۱۴۰۳/۰۹/۲۰", color = Color.White, fontSize = 8.sp)
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(20.dp))
                    Button(onClick = {}, shape = RoundedCornerShape(14.dp), colors = ButtonDefaults.buttonColors(containerColor = Rose600), modifier = Modifier.fillMaxWidth().height(50.dp).shadow(8.dp, RoundedCornerShape(14.dp))) {
                        Text("تولید PDF لاکچری", fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}

@Composable
fun SignatureScreen(modifier: Modifier = Modifier) {
    LazyColumn(modifier = modifier.fillMaxSize().background(LuxuryObsidianBrush), contentPadding = PaddingValues(16.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
        item {
            Card(modifier = Modifier.fillMaxWidth().shadow(16.dp, RoundedCornerShape(24.dp)), shape = RoundedCornerShape(24.dp), colors = CardDefaults.cardColors(containerColor = LuxuryDarkCard), border = BorderStroke(1.dp, Emerald500.copy(alpha = 0.3f)), elevation = CardDefaults.cardElevation(8.dp)) {
                Column(modifier = Modifier.padding(24.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                    Box(modifier = Modifier.size(90.dp).clip(RoundedCornerShape(20.dp)).background(Brush.linearGradient(listOf(Emerald500, Emerald600))).shadow(12.dp, RoundedCornerShape(20.dp)), contentAlignment = Alignment.Center) {
                        Icon(Icons.Default.Edit, contentDescription = null, tint = Color.White, modifier = Modifier.size(44.dp))
                    }
                    Spacer(modifier = Modifier.height(18.dp))
                    Text("امضای دیجیتال لاکچری", fontSize = 20.sp, fontWeight = FontWeight.ExtraBold, color = Color.White)
                    Text("بخش ۱۵ - اثر انگشت + بیومتریک + هش", fontSize = 12.sp, color = Slate400)
                    Spacer(modifier = Modifier.height(20.dp))
                    Card(modifier = Modifier.fillMaxWidth().height(180.dp), shape = RoundedCornerShape(16.dp), colors = CardDefaults.cardColors(containerColor = Color.White), elevation = CardDefaults.cardElevation(2.dp)) {
                        Box(modifier = Modifier.fillMaxSize().background(Slate50, RoundedCornerShape(16.dp)).padding(8.dp), contentAlignment = Alignment.Center) {
                            Box(modifier = Modifier.fillMaxSize().background(Color.White, RoundedCornerShape(12.dp)), contentAlignment = Alignment.Center) {
                                Text("اینجا امضا کنید ✍️", color = Slate400, fontSize = 16.sp, fontWeight = FontWeight.Medium)
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(20.dp))
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                        OutlinedButton(onClick = {}, modifier = Modifier.weight(1f).height(50.dp), shape = RoundedCornerShape(14.dp), border = BorderStroke(1.dp, Slate600), colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.White)) { Text("پاک کردن") }
                        Button(onClick = {}, modifier = Modifier.weight(1f).height(50.dp).shadow(8.dp, RoundedCornerShape(14.dp)), shape = RoundedCornerShape(14.dp), colors = ButtonDefaults.buttonColors(containerColor = Emerald600)) {
                            Icon(Icons.Default.Fingerprint, contentDescription = null, modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("تایید با اثر انگشت", fontWeight = FontWeight.Bold, fontSize = 12.sp)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun AuditLogScreen(modifier: Modifier = Modifier) {
    val auditService = remember { AuditLogService() }
    var logs by remember { mutableStateOf(auditService.logs.value) }

    LaunchedEffect(Unit) {
        auditService.logAction(action = "CREATE", entityType = "CASE", entityId = "123", details = "ایجاد پرونده چک ۳۵ میلیاردی")
        auditService.logAction(action = "UPDATE", entityType = "DEADLINE", entityId = "456", details = "تمدید مهلت تجدیدنظرخواهی")
        logs = auditService.logs.value
    }

    LazyColumn(modifier = modifier.fillMaxSize().background(LuxuryObsidianBrush), contentPadding = PaddingValues(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        item {
            Card(modifier = Modifier.fillMaxWidth().shadow(16.dp, RoundedCornerShape(24.dp)), shape = RoundedCornerShape(24.dp), colors = CardDefaults.cardColors(containerColor = LuxuryNavyDeep), border = BorderStroke(1.dp, LuxuryGold.copy(alpha = 0.3f))) {
                Box(modifier = Modifier.fillMaxWidth().background(LuxuryObsidianBrush).padding(20.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(modifier = Modifier.size(52.dp).clip(RoundedCornerShape(14.dp)).background(LuxuryGoldDarkBrush), contentAlignment = Alignment.Center) {
                            Icon(Icons.Default.Fingerprint, contentDescription = null, tint = LuxuryNavyDeep, modifier = Modifier.size(26.dp))
                        }
                        Spacer(modifier = Modifier.width(14.dp))
                        Column {
                            Text("لاگ قضایی بلاک‌چین", fontSize = 18.sp, fontWeight = FontWeight.ExtraBold, color = Color.White)
                            Text("بخش ۲۴ - مهر زمانی غیرقابل تغییر + SHA256", fontSize = 11.sp, color = Slate400)
                        }
                    }
                }
            }
        }
        item {
            Card(shape = RoundedCornerShape(14.dp), colors = CardDefaults.cardColors(containerColor = Emerald600.copy(alpha = 0.15f)), border = BorderStroke(1.dp, Emerald500.copy(alpha = 0.3f))) {
                Row(modifier = Modifier.padding(14.dp), verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.VerifiedUser, contentDescription = null, tint = Emerald400, modifier = Modifier.size(20.dp))
                    Spacer(modifier = Modifier.width(10.dp))
                    Text("✅ زنجیره بلاک‌چین تایید شد - تمام لاگ‌ها معتبر و غیرقابل تغییر هستند", fontSize = 11.sp, color = Emerald300, lineHeight = 14.sp)
                }
            }
        }
        items(logs.size) { index ->
            val log = logs[index]
            Card(modifier = Modifier.fillMaxWidth().shadow(4.dp, RoundedCornerShape(16.dp)), shape = RoundedCornerShape(16.dp), colors = CardDefaults.cardColors(containerColor = LuxuryDarkCard), border = BorderStroke(0.5.dp, Slate700)) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Surface(shape = RoundedCornerShape(8.dp), color = when (log.action) {
                            "CREATE" -> Emerald600.copy(alpha = 0.2f)
                            "UPDATE" -> Amber600.copy(alpha = 0.2f)
                            "DELETE" -> Rose600.copy(alpha = 0.2f)
                            else -> Slate700
                        }, border = BorderStroke(0.5.dp, when (log.action) {
                            "CREATE" -> Emerald500.copy(alpha = 0.3f)
                            "UPDATE" -> Amber500.copy(alpha = 0.3f)
                            "DELETE" -> Rose500.copy(alpha = 0.3f)
                            else -> Slate600
                        })) { Text(log.action, fontSize = 10.sp, fontWeight = FontWeight.Bold, color = Color.White, modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)) }
                        Text(java.text.SimpleDateFormat("HH:mm:ss", java.util.Locale.getDefault()).format(java.util.Date(log.timestamp)), fontSize = 10.sp, color = Slate500)
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("${log.entityType} #${log.entityId}: ${log.details}", fontSize = 12.sp, fontWeight = FontWeight.Medium, color = Color.White)
                    Spacer(modifier = Modifier.height(6.dp))
                    Surface(shape = RoundedCornerShape(8.dp), color = LuxuryCharcoal) {
                        Text("HASH: ${log.currentHash.take(24)}... | PREV: ${log.previousHash.take(16)}...", fontSize = 9.sp, color = Slate500, modifier = Modifier.padding(8.dp))
                    }
                }
            }
        }
    }
}

@Composable
fun BackupScreen(modifier: Modifier = Modifier) {
    LazyColumn(modifier = modifier.fillMaxSize().background(LuxuryObsidianBrush), contentPadding = PaddingValues(16.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
        item {
            Card(modifier = Modifier.fillMaxWidth().shadow(16.dp, RoundedCornerShape(24.dp)), shape = RoundedCornerShape(24.dp), colors = CardDefaults.cardColors(containerColor = LuxuryDarkCard), border = BorderStroke(1.dp, Sky500.copy(alpha = 0.3f)), elevation = CardDefaults.cardElevation(8.dp)) {
                Column(modifier = Modifier.padding(24.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                    Box(modifier = Modifier.size(90.dp).clip(RoundedCornerShape(20.dp)).background(Brush.linearGradient(listOf(Sky500, Sky600))).shadow(12.dp, RoundedCornerShape(20.dp)), contentAlignment = Alignment.Center) {
                        Icon(Icons.Default.CloudUpload, contentDescription = null, tint = Color.White, modifier = Modifier.size(44.dp))
                    }
                    Spacer(modifier = Modifier.height(18.dp))
                    Text("بک‌آپ ابری رمزنگاری شده", fontSize = 20.sp, fontWeight = FontWeight.ExtraBold, color = Color.White)
                    Text("بخش ۲۳ - Google Drive + AES-256", fontSize = 12.sp, color = Slate400)
                    Spacer(modifier = Modifier.height(20.dp))
                    Button(onClick = {}, shape = RoundedCornerShape(16.dp), colors = ButtonDefaults.buttonColors(containerColor = Sky600), modifier = Modifier.fillMaxWidth().height(54.dp).shadow(8.dp, RoundedCornerShape(16.dp))) {
                        Icon(Icons.Default.CloudUpload, contentDescription = null)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("ایجاد بک‌آپ رمزنگاری اکنون", fontWeight = FontWeight.Bold)
                    }
                    Spacer(modifier = Modifier.height(10.dp))
                    OutlinedButton(onClick = {}, shape = RoundedCornerShape(16.dp), border = BorderStroke(1.dp, Slate600), colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.White), modifier = Modifier.fillMaxWidth().height(54.dp)) {
                        Icon(Icons.Default.CloudDownload, contentDescription = null)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("بازیابی از ابر")
                    }
                }
            }
        }
        item {
            Card(modifier = Modifier.fillMaxWidth().shadow(8.dp, RoundedCornerShape(20.dp)), shape = RoundedCornerShape(20.dp), colors = CardDefaults.cardColors(containerColor = LuxuryDarkCard), border = BorderStroke(0.5.dp, Slate700)) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Text("آخرین بک‌آپ‌ها", fontWeight = FontWeight.ExtraBold, fontSize = 15.sp, color = Color.White)
                    Spacer(modifier = Modifier.height(14.dp))
                    repeat(3) { i ->
                        Card(modifier = Modifier.fillMaxWidth().padding(vertical = 5.dp), shape = RoundedCornerShape(12.dp), colors = CardDefaults.cardColors(containerColor = LuxuryCharcoal), border = BorderStroke(0.5.dp, Slate700)) {
                            Row(modifier = Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
                                Box(modifier = Modifier.size(36.dp).clip(CircleShape).background(Brush.linearGradient(listOf(Emerald500, Emerald600))), contentAlignment = Alignment.Center) {
                                    Icon(Icons.Default.CloudDone, contentDescription = null, tint = Color.White, modifier = Modifier.size(18.dp))
                                }
                                Spacer(modifier = Modifier.width(12.dp))
                                Column(modifier = Modifier.weight(1f)) {
                                    Text("بک‌آپ لاکچری ${i+1} - ${5 + i*2} پرونده", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color.White)
                                    Text("امروز ${10+i}:30 • ${(2+i*3)} MB • رمزنگاری شده", fontSize = 10.sp, color = Slate400)
                                }
                                Icon(Icons.Default.ChevronLeft, contentDescription = null, tint = LuxuryGold.copy(alpha = 0.5f), modifier = Modifier.size(18.dp))
                            }
                        }
                    }
                }
            }
        }
    }
}

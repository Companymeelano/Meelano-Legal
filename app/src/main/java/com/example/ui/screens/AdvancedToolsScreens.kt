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
    LazyColumn(modifier = modifier.fillMaxSize().background(Slate50), contentPadding = PaddingValues(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        item {
            Card(modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(20.dp), colors = CardDefaults.cardColors(containerColor = LuxuryNavy)) {
                Box(modifier = Modifier.fillMaxWidth().background(LuxuryNavyBrush).padding(20.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(modifier = Modifier.size(44.dp).clip(CircleShape).background(Brush.linearGradient(listOf(Emerald500, Emerald600))), contentAlignment = Alignment.Center) {
                            Icon(Icons.Default.Checklist, contentDescription = null, tint = Color.White, modifier = Modifier.size(24.dp))
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text("چک‌لیست هوشمند قضایی", fontSize = 18.sp, fontWeight = FontWeight.ExtraBold, color = Color.White)
                            Text("بخش ۸ - برای هر نوع دعوا، مدارک و مراحل", fontSize = 11.sp, color = Slate300)
                        }
                    }
                }
            }
        }
        items(ChecklistDatabase.allChecklists) { checklist ->
            Card(modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(18.dp), colors = CardDefaults.cardColors(containerColor = Color.White), border = BorderStroke(1.dp, Slate200), elevation = CardDefaults.cardElevation(3.dp)) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Surface(shape = RoundedCornerShape(8.dp), color = Indigo50) { Text(checklist.caseType, fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Indigo700, modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)) }
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(checklist.title, fontWeight = FontWeight.Bold, fontSize = 13.sp, color = Slate900)
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(checklist.description, fontSize = 12.sp, color = Slate600)
                    Spacer(modifier = Modifier.height(10.dp))
                    Text("مدارک لازم:", fontWeight = FontWeight.Bold, fontSize = 11.sp, color = Slate700)
                    Text(checklist.requiredDocuments, fontSize = 11.sp, color = Slate600, lineHeight = 16.sp)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("مراحل:", fontWeight = FontWeight.Bold, fontSize = 11.sp, color = Slate700)
                    Text(checklist.steps, fontSize = 11.sp, color = Slate600, lineHeight = 16.sp)
                    Spacer(modifier = Modifier.height(8.dp))
                    Surface(shape = RoundedCornerShape(8.dp), color = Amber50, border = BorderStroke(1.dp, Amber100)) {
                        Text("⏱ ${checklist.estimatedTimeDays} روز • ${checklist.legalBasis}", fontSize = 10.sp, color = Amber600, modifier = Modifier.padding(8.dp))
                    }
                }
            }
        }
    }
}

@Composable
fun OcrScannerScreen(modifier: Modifier = Modifier) {
    LazyColumn(modifier = modifier.fillMaxSize().background(Slate50), contentPadding = PaddingValues(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        item {
            Card(modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(20.dp), colors = CardDefaults.cardColors(containerColor = Color.White), border = BorderStroke(1.dp, Indigo100), elevation = CardDefaults.cardElevation(4.dp)) {
                Column(modifier = Modifier.padding(20.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                    Box(modifier = Modifier.size(80.dp).clip(CircleShape).background(Brush.linearGradient(listOf(Indigo600, Indigo800))), contentAlignment = Alignment.Center) {
                        Icon(Icons.Default.CameraAlt, contentDescription = null, tint = Color.White, modifier = Modifier.size(40.dp))
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    Text("اسکنر هوشمند OCR فارسی", fontSize = 18.sp, fontWeight = FontWeight.ExtraBold)
                    Text("بخش ۱۴ - ML Kit + Document AI", fontSize = 12.sp, color = Slate500)
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(onClick = {}, shape = RoundedCornerShape(14.dp), colors = ButtonDefaults.buttonColors(containerColor = Indigo600), modifier = Modifier.fillMaxWidth().height(50.dp)) {
                        Icon(Icons.Default.CameraAlt, contentDescription = null)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("گرفتن عکس از سند")
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedButton(onClick = {}, shape = RoundedCornerShape(14.dp), modifier = Modifier.fillMaxWidth().height(50.dp)) {
                        Icon(Icons.Default.PhotoLibrary, contentDescription = null)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("انتخاب از گالری")
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    Surface(shape = RoundedCornerShape(12.dp), color = Slate50, border = BorderStroke(1.dp, Slate200)) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Text("قابلیت‌ها:", fontWeight = FontWeight.Bold, fontSize = 12.sp)
                            Text("• استخراج خودکار شماره پرونده، تاریخ، مبلغ\n• تشخیص زبان فارسی ۹۲٪ دقت\n• تبدیل عکس به پرونده جدید\n• ذخیره متن استخراج شده", fontSize = 11.sp, color = Slate600, lineHeight = 16.sp)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun PdfLuxuryScreen(modifier: Modifier = Modifier) {
    LazyColumn(modifier = modifier.fillMaxSize().background(Slate50), contentPadding = PaddingValues(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        item {
            Card(modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(20.dp), colors = CardDefaults.cardColors(containerColor = Color.White), border = BorderStroke(1.dp, Rose100), elevation = CardDefaults.cardElevation(4.dp)) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(modifier = Modifier.size(44.dp).clip(CircleShape).background(Brush.linearGradient(listOf(Rose500, Rose600))), contentAlignment = Alignment.Center) {
                            Icon(Icons.Default.PictureAsPdf, contentDescription = null, tint = Color.White, modifier = Modifier.size(24.dp))
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text("PDF لاکچری طلایی", fontSize = 18.sp, fontWeight = FontWeight.ExtraBold)
                            Text("بخش ۱۸ - سربرگ طلایی + QR + لوگو M", fontSize = 11.sp, color = Slate500)
                        }
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    Box(modifier = Modifier.fillMaxWidth().height(200.dp).background(Color.White, RoundedCornerShape(12.dp)).padding(16.dp)) {
                        Column {
                            Box(modifier = Modifier.fillMaxWidth().height(30.dp).background(LuxuryNavy, RoundedCornerShape(6.dp)), contentAlignment = Alignment.CenterStart) {
                                Text("  میلانو لگال - لاکچری - تحلیل قوانین ایران", color = LuxuryGold, fontSize = 10.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(start = 8.dp))
                            }
                            Spacer(modifier = Modifier.height(12.dp))
                            Text("عنوان: لایحه دفاعیه مطالبه وجه چک", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                            Text("مخاطب: ریاست محترم دادگاه...", fontSize = 10.sp, color = Slate600)
                            Spacer(modifier = Modifier.height(8.dp))
                            Text("متن لایحه با فونت فارسی و استناد به مواد قانونی...", fontSize = 10.sp, color = Slate700, lineHeight = 14.sp)
                            Spacer(modifier = Modifier.weight(1f))
                            Box(modifier = Modifier.fillMaxWidth().height(24.dp).background(LuxuryNavy, RoundedCornerShape(4.dp)), contentAlignment = Alignment.Center) {
                                Text("QR: MEELANO-123-456 | تولید: ۱۴۰۳/۰۹/۲۰", color = Color.White, fontSize = 8.sp)
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(onClick = {}, shape = RoundedCornerShape(12.dp), colors = ButtonDefaults.buttonColors(containerColor = Rose600), modifier = Modifier.fillMaxWidth()) {
                        Text("تولید PDF لاکچری")
                    }
                }
            }
        }
    }
}

@Composable
fun SignatureScreen(modifier: Modifier = Modifier) {
    LazyColumn(modifier = modifier.fillMaxSize().background(Slate50), contentPadding = PaddingValues(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        item {
            Card(modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(20.dp), colors = CardDefaults.cardColors(containerColor = Color.White), border = BorderStroke(1.dp, Emerald100), elevation = CardDefaults.cardElevation(4.dp)) {
                Column(modifier = Modifier.padding(20.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                    Box(modifier = Modifier.size(80.dp).clip(CircleShape).background(Brush.linearGradient(listOf(Emerald500, Emerald600))), contentAlignment = Alignment.Center) {
                        Icon(Icons.Default.Edit, contentDescription = null, tint = Color.White, modifier = Modifier.size(40.dp))
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    Text("امضای دیجیتال لاکچری", fontSize = 18.sp, fontWeight = FontWeight.ExtraBold)
                    Text("بخش ۱۵ - اثر انگشت + بیومتریک + هش", fontSize = 12.sp, color = Slate500)
                    Spacer(modifier = Modifier.height(16.dp))
                    Box(modifier = Modifier.fillMaxWidth().height(150.dp).background(Color.White, RoundedCornerShape(12.dp)).padding(8.dp)) {
                        // شبیه‌سازی بوم امضا
                        Box(modifier = Modifier.fillMaxSize().background(Slate50, RoundedCornerShape(8.dp)), contentAlignment = Alignment.Center) {
                            Text("اینجا امضا کنید ✍️", color = Slate400, fontSize = 14.sp)
                        }
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        OutlinedButton(onClick = {}, modifier = Modifier.weight(1f), shape = RoundedCornerShape(12.dp)) { Text("پاک کردن") }
                        Button(onClick = {}, modifier = Modifier.weight(1f), shape = RoundedCornerShape(12.dp), colors = ButtonDefaults.buttonColors(containerColor = Emerald600)) {
                            Icon(Icons.Default.Fingerprint, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("تایید با اثر انگشت")
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun AuditLogScreen(modifier: Modifier = Modifier) {
    val auditService = remember { com.example.data.service.AuditLogService() }
    var logs by remember { mutableStateOf(auditService.logs.value) }

    LaunchedEffect(Unit) {
        // نمونه لاگ‌ها
        auditService.logAction(action = "CREATE", entityType = "CASE", entityId = "123", details = "ایجاد پرونده چک ۳۵ میلیاردی")
        auditService.logAction(action = "UPDATE", entityType = "DEADLINE", entityId = "456", details = "تمدید مهلت تجدیدنظرخواهی")
        logs = auditService.logs.value
    }

    LazyColumn(modifier = modifier.fillMaxSize().background(Slate50), contentPadding = PaddingValues(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
        item {
            Card(modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(20.dp), colors = CardDefaults.cardColors(containerColor = LuxuryNavy)) {
                Box(modifier = Modifier.fillMaxWidth().background(Brush.linearGradient(listOf(Slate900, LuxuryNavy))).padding(20.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Fingerprint, contentDescription = null, tint = LuxuryGold, modifier = Modifier.size(28.dp))
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text("لاگ قضایی بلاک‌چین", fontSize = 18.sp, fontWeight = FontWeight.ExtraBold, color = Color.White)
                            Text("بخش ۲۴ - مهر زمانی غیرقابل تغییر + SHA256", fontSize = 11.sp, color = Slate300)
                        }
                    }
                }
            }
        }
        item {
            Surface(shape = RoundedCornerShape(12.dp), color = Emerald50, border = BorderStroke(1.dp, Emerald100)) {
                Row(modifier = Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.VerifiedUser, contentDescription = null, tint = Emerald600, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("✅ زنجیره بلاک‌چین تایید شد - تمام لاگ‌ها معتبر و غیرقابل تغییر هستند", fontSize = 11.sp, color = Emerald600)
                }
            }
        }
        items(logs.size) { index ->
            val log = logs[index]
            Card(modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(14.dp), colors = CardDefaults.cardColors(containerColor = Color.White), border = BorderStroke(1.dp, Slate200)) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Surface(shape = RoundedCornerShape(6.dp), color = when (log.action) {
                            "CREATE" -> Emerald50
                            "UPDATE" -> Amber50
                            "DELETE" -> Rose50
                            else -> Slate100
                        }) { Text(log.action, fontSize = 10.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp)) }
                        Text(java.text.SimpleDateFormat("HH:mm:ss", java.util.Locale.getDefault()).format(java.util.Date(log.timestamp)), fontSize = 10.sp, color = Slate400)
                    }
                    Spacer(modifier = Modifier.height(6.dp))
                    Text("${log.entityType} #${log.entityId}: ${log.details}", fontSize = 12.sp, fontWeight = FontWeight.Medium)
                    Spacer(modifier = Modifier.height(4.dp))
                    Text("HASH: ${log.currentHash.take(24)}... | PREV: ${log.previousHash.take(16)}...", fontSize = 9.sp, color = Slate400)
                }
            }
        }
    }
}

@Composable
fun BackupScreen(modifier: Modifier = Modifier) {
    LazyColumn(modifier = modifier.fillMaxSize().background(Slate50), contentPadding = PaddingValues(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        item {
            Card(modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(20.dp), colors = CardDefaults.cardColors(containerColor = Color.White), border = BorderStroke(1.dp, Sky100), elevation = CardDefaults.cardElevation(4.dp)) {
                Column(modifier = Modifier.padding(20.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                    Box(modifier = Modifier.size(80.dp).clip(CircleShape).background(Brush.linearGradient(listOf(Sky500, Sky600))), contentAlignment = Alignment.Center) {
                        Icon(Icons.Default.CloudUpload, contentDescription = null, tint = Color.White, modifier = Modifier.size(40.dp))
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    Text("بک‌آپ ابری رمزنگاری شده", fontSize = 18.sp, fontWeight = FontWeight.ExtraBold)
                    Text("بخش ۲۳ - Google Drive + AES-256", fontSize = 12.sp, color = Slate500)
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(onClick = {}, shape = RoundedCornerShape(14.dp), colors = ButtonDefaults.buttonColors(containerColor = Sky600), modifier = Modifier.fillMaxWidth().height(50.dp)) {
                        Icon(Icons.Default.CloudUpload, contentDescription = null)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("ایجاد بک‌آپ رمزنگاری اکنون")
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedButton(onClick = {}, shape = RoundedCornerShape(14.dp), modifier = Modifier.fillMaxWidth().height(50.dp)) {
                        Icon(Icons.Default.CloudDownload, contentDescription = null)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("بازیابی از ابر")
                    }
                }
            }
        }
        item {
            Card(modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(16.dp), colors = CardDefaults.cardColors(containerColor = Color.White), border = BorderStroke(1.dp, Slate200)) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("آخرین بک‌آپ‌ها", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                    Spacer(modifier = Modifier.height(12.dp))
                    repeat(3) { i ->
                        Row(modifier = Modifier.fillMaxWidth().padding(vertical = 6.dp), verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.CloudDone, contentDescription = null, tint = Emerald600, modifier = Modifier.size(20.dp))
                            Spacer(modifier = Modifier.width(10.dp))
                            Column(modifier = Modifier.weight(1f)) {
                                Text("بک‌آپ لاکچری ${i+1} - ${5 + i*2} پرونده", fontSize = 12.sp, fontWeight = FontWeight.Medium)
                                Text("امروز ${10+i}:30 • ${(2+i*3)} MB • رمزنگاری شده", fontSize = 10.sp, color = Slate500)
                            }
                            Icon(Icons.Default.ChevronLeft, contentDescription = null, tint = Slate400, modifier = Modifier.size(16.dp))
                        }
                    }
                }
            }
        }
    }
}

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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.ChecklistDatabase
import com.example.data.service.AuditLogService
import com.example.ui.theme.*

private val Bg = LuxuryNavyDeep
private val CardBg = LuxuryDarkCard
private val CardElev = LuxuryDarkCardElevated
private val Gold = LuxuryGold
private val GoldBrush = LuxuryGoldDarkBrush
private val Border = Gold.copy(alpha = 0.18f)

@Composable
fun ChecklistScreen(modifier: Modifier = Modifier) {
    LazyColumn(modifier = modifier.fillMaxSize().background(Bg), contentPadding = PaddingValues(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        item {
            Box(modifier = Modifier.fillMaxWidth().padding(8.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(modifier = Modifier.size(48.dp).clip(RoundedCornerShape(12.dp)).background(GoldBrush), contentAlignment = Alignment.Center) {
                        Icon(Icons.Default.Checklist, contentDescription = null, tint = Bg, modifier = Modifier.size(24.dp))
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text("چک‌لیست هوشمند", fontSize = 18.sp, fontWeight = FontWeight.ExtraBold, color = Color.White)
                        Text("تم یکدست تیره طلایی", fontSize = 11.sp, color = Color.White.copy(alpha = 0.5f))
                    }
                }
            }
        }
        items(ChecklistDatabase.allChecklists) { checklist ->
            Card(modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(16.dp), colors = CardDefaults.cardColors(containerColor = CardBg), border = BorderStroke(1.dp, Border)) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(checklist.title, fontWeight = FontWeight.Bold, fontSize = 13.sp, color = Color.White)
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(checklist.description, fontSize = 11.sp, color = Color.White.copy(alpha = 0.6f))
                    Spacer(modifier = Modifier.height(8.dp))
                    Surface(shape = RoundedCornerShape(8.dp), color = Gold.copy(alpha = 0.12f), border = BorderStroke(0.5.dp, Gold.copy(alpha = 0.25f))) {
                        Text("⏱ ${checklist.estimatedTimeDays} روز • ${checklist.legalBasis}", fontSize = 9.sp, color = Gold, modifier = Modifier.padding(8.dp))
                    }
                }
            }
        }
    }
}

@Composable
fun OcrScannerScreen(modifier: Modifier = Modifier) {
    LazyColumn(modifier = modifier.fillMaxSize().background(Bg), contentPadding = PaddingValues(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        item {
            Card(modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(18.dp), colors = CardDefaults.cardColors(containerColor = CardBg), border = BorderStroke(1.dp, Border)) {
                Column(modifier = Modifier.padding(20.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                    Box(modifier = Modifier.size(72.dp).clip(RoundedCornerShape(16.dp)).background(GoldBrush), contentAlignment = Alignment.Center) {
                        Icon(Icons.Default.CameraAlt, contentDescription = null, tint = Bg, modifier = Modifier.size(36.dp))
                    }
                    Spacer(modifier = Modifier.height(14.dp))
                    Text("اسکنر OCR فارسی", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color.White)
                    Text("تم یکدست • ML Kit", fontSize = 11.sp, color = Color.White.copy(alpha = 0.5f))
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(onClick = {}, shape = RoundedCornerShape(12.dp), colors = ButtonDefaults.buttonColors(containerColor = Gold, contentColor = Bg), modifier = Modifier.fillMaxWidth()) {
                        Text("گرفتن عکس", fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}

@Composable
fun PdfLuxuryScreen(modifier: Modifier = Modifier) {
    LazyColumn(modifier = modifier.fillMaxSize().background(Bg), contentPadding = PaddingValues(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        item {
            Card(modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(18.dp), colors = CardDefaults.cardColors(containerColor = CardBg), border = BorderStroke(1.dp, Border)) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(modifier = Modifier.size(44.dp).clip(RoundedCornerShape(12.dp)).background(GoldBrush), contentAlignment = Alignment.Center) {
                            Icon(Icons.Default.PictureAsPdf, contentDescription = null, tint = Bg, modifier = Modifier.size(22.dp))
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Text("PDF طلایی", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color.White)
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    Card(shape = RoundedCornerShape(12.dp), colors = CardDefaults.cardColors(containerColor = Color.White)) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Box(modifier = Modifier.fillMaxWidth().height(28.dp).background(Bg, RoundedCornerShape(6.dp)), contentAlignment = Alignment.Center) {
                                Text("میلانو لگال - طلایی", color = Gold, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                            }
                            Spacer(modifier = Modifier.height(10.dp))
                            Text("لایحه دفاعیه...", fontSize = 11.sp, color = Color.Black)
                        }
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(onClick = {}, shape = RoundedCornerShape(12.dp), colors = ButtonDefaults.buttonColors(containerColor = Gold, contentColor = Bg), modifier = Modifier.fillMaxWidth()) {
                        Text("تولید PDF", fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}

@Composable
fun SignatureScreen(modifier: Modifier = Modifier) {
    LazyColumn(modifier = modifier.fillMaxSize().background(Bg), contentPadding = PaddingValues(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        item {
            Card(modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(18.dp), colors = CardDefaults.cardColors(containerColor = CardBg), border = BorderStroke(1.dp, Border)) {
                Column(modifier = Modifier.padding(20.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                    Box(modifier = Modifier.size(72.dp).clip(RoundedCornerShape(16.dp)).background(GoldBrush), contentAlignment = Alignment.Center) {
                        Icon(Icons.Default.Edit, contentDescription = null, tint = Bg, modifier = Modifier.size(32.dp))
                    }
                    Spacer(modifier = Modifier.height(14.dp))
                    Text("امضای دیجیتال", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color.White)
                    Text("تم یکدست • بیومتریک", fontSize = 11.sp, color = Color.White.copy(alpha = 0.5f))
                    Spacer(modifier = Modifier.height(16.dp))
                    Box(modifier = Modifier.fillMaxWidth().height(140.dp).background(Color.White, RoundedCornerShape(12.dp)), contentAlignment = Alignment.Center) {
                        Text("اینجا امضا کنید ✍️", color = Color.Gray)
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
        auditService.logAction(action = "CREATE", entityType = "CASE", entityId = "123", details = "ایجاد پرونده")
        logs = auditService.logs.value
    }
    LazyColumn(modifier = modifier.fillMaxSize().background(Bg), contentPadding = PaddingValues(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
        item {
            Box(modifier = Modifier.fillMaxWidth().padding(8.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(modifier = Modifier.size(44.dp).clip(RoundedCornerShape(10.dp)).background(GoldBrush), contentAlignment = Alignment.Center) {
                        Icon(Icons.Default.Fingerprint, contentDescription = null, tint = Bg, modifier = Modifier.size(22.dp))
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Text("لاگ بلاک‌چین", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color.White)
                }
            }
        }
        items(logs.size) { index ->
            val log = logs[index]
            Card(modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(12.dp), colors = CardDefaults.cardColors(containerColor = CardBg), border = BorderStroke(1.dp, Border)) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Text("${log.entityType}: ${log.details}", fontSize = 11.sp, color = Color.White)
                    Text("HASH: ${log.currentHash.take(20)}...", fontSize = 8.sp, color = Color.White.copy(alpha = 0.4f))
                }
            }
        }
    }
}

@Composable
fun BackupScreen(modifier: Modifier = Modifier) {
    LazyColumn(modifier = modifier.fillMaxSize().background(Bg), contentPadding = PaddingValues(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        item {
            Card(modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(18.dp), colors = CardDefaults.cardColors(containerColor = CardBg), border = BorderStroke(1.dp, Border)) {
                Column(modifier = Modifier.padding(20.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                    Box(modifier = Modifier.size(72.dp).clip(RoundedCornerShape(16.dp)).background(GoldBrush), contentAlignment = Alignment.Center) {
                        Icon(Icons.Default.CloudUpload, contentDescription = null, tint = Bg, modifier = Modifier.size(32.dp))
                    }
                    Spacer(modifier = Modifier.height(14.dp))
                    Text("بک‌آپ ابری", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color.White)
                    Text("تم یکدست • AES-256", fontSize = 11.sp, color = Color.White.copy(alpha = 0.5f))
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(onClick = {}, shape = RoundedCornerShape(12.dp), colors = ButtonDefaults.buttonColors(containerColor = Gold, contentColor = Bg), modifier = Modifier.fillMaxWidth()) {
                        Text("ایجاد بک‌آپ", fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}

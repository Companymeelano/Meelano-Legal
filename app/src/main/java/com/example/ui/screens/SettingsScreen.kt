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
import com.example.data.model.VoiceProfiles
import com.example.ui.LegalViewModel
import com.example.ui.theme.*

@Composable
fun SettingsScreen(
    viewModel: LegalViewModel,
    modifier: Modifier = Modifier
) {
    val isSpeaking by viewModel.isTtsSpeaking.collectAsStateWithLifecycle()
    var selectedVoice by remember { mutableStateOf(VoiceProfiles.allProfiles[0]) }
    var ttsSpeed by remember { mutableStateOf(0.9f) }
    var isDarkTheme by remember { mutableStateOf(false) }
    var isBiometricEnabled by remember { mutableStateOf(true) }
    var isCloudBackupEnabled by remember { mutableStateOf(true) }

    LazyColumn(
        modifier = modifier.fillMaxSize().background(Slate50),
        contentPadding = PaddingValues(bottom = 100.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            Card(
                modifier = Modifier.fillMaxWidth().shadow(12.dp, RoundedCornerShape(bottomStart = 32.dp, bottomEnd = 32.dp)),
                shape = RoundedCornerShape(bottomStart = 32.dp, bottomEnd = 32.dp),
                colors = CardDefaults.cardColors(containerColor = LuxuryNavy)
            ) {
                Box(modifier = Modifier.fillMaxWidth().background(LuxuryNavyBrush).padding(24.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(modifier = Modifier.size(48.dp).clip(CircleShape).background(LuxuryGoldBrush), contentAlignment = Alignment.Center) {
                            Icon(Icons.Default.Settings, contentDescription = null, tint = LuxuryNavy, modifier = Modifier.size(26.dp))
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text("تنظیمات لاکچری", fontSize = 20.sp, fontWeight = FontWeight.ExtraBold, color = Color.White)
                            Text("صدا، تم، امنیت، بک‌آپ، بدون شلوغی", fontSize = 11.sp, color = Slate300)
                        }
                    }
                }
            }
        }

        // بخش ۴: صدای وکیل شخصی‌سازی
        item {
            Card(modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp), shape = RoundedCornerShape(20.dp), colors = CardDefaults.cardColors(containerColor = Color.White), border = BorderStroke(1.dp, LuxuryGold.copy(alpha = 0.3f)), elevation = CardDefaults.cardElevation(6.dp)) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.RecordVoiceOver, contentDescription = null, tint = LuxuryGoldDark, modifier = Modifier.size(22.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("صدای وکیل شخصی‌سازی - بخش ۴", fontWeight = FontWeight.ExtraBold, fontSize = 14.sp)
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                    VoiceProfiles.allProfiles.forEach { profile ->
                        val isSelected = selectedVoice.id == profile.id
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = if (isSelected) Indigo50 else Slate50,
                            border = BorderStroke(1.dp, if (isSelected) Indigo600 else Slate200),
                            modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)
                        ) {
                            Row(modifier = Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
                                RadioButton(selected = isSelected, onClick = { selectedVoice = profile })
                                Spacer(modifier = Modifier.width(8.dp))
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(profile.name, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                                    Text("${profile.gender} • سرعت ${profile.speed} • لحن ${profile.tone}", fontSize = 10.sp, color = Slate500)
                                }
                                IconButton(onClick = { viewModel.speakPersianText("سلام، من ${profile.name} هستم. این صدای تست لاکچری میلانو لگال است.") }) {
                                    Icon(Icons.Default.VolumeUp, contentDescription = null, tint = Indigo600, modifier = Modifier.size(18.dp))
                                }
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                    Text("سرعت پخش: ${String.format("%.1f", ttsSpeed)}x", fontSize = 12.sp, fontWeight = FontWeight.Medium)
                    Slider(value = ttsSpeed, onValueChange = { ttsSpeed = it; viewModel.setTtsSpeed(it) }, valueRange = 0.5f..1.5f, modifier = Modifier.fillMaxWidth())
                }
            }
        }

        // بخش ۱۰: تم پویا
        item {
            Card(modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp), shape = RoundedCornerShape(20.dp), colors = CardDefaults.cardColors(containerColor = Color.White), border = BorderStroke(1.dp, Slate200), elevation = CardDefaults.cardElevation(4.dp)) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Palette, contentDescription = null, tint = Indigo600, modifier = Modifier.size(20.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("تم پویا لاکچری - بخش ۱۰", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        ThemeOption("روز", Icons.Default.LightMode, !isDarkTheme, Modifier.weight(1f)) { isDarkTheme = false }
                        ThemeOption("شب", Icons.Default.DarkMode, isDarkTheme, Modifier.weight(1f)) { isDarkTheme = true }
                        ThemeOption("طلایی", Icons.Default.Star, false, Modifier.weight(1f)) { }
                    }
                }
            }
        }

        // بخش ۲۱: امنیت
        item {
            Card(modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp), shape = RoundedCornerShape(20.dp), colors = CardDefaults.cardColors(containerColor = Color.White), border = BorderStroke(1.dp, Slate200), elevation = CardDefaults.cardElevation(4.dp)) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Security, contentDescription = null, tint = Emerald600, modifier = Modifier.size(20.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("امنیت و رمزنگاری - بخش ۲۱", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                    SettingSwitch("ورود با اثر انگشت (Biometric)", "امنیت لاکچری با بیومتریک", isBiometricEnabled, Icons.Default.Fingerprint) { isBiometricEnabled = it }
                    Spacer(modifier = Modifier.height(8.dp))
                    SettingSwitch("رمزنگاری پایگاه داده (SQLCipher)", "AES-256 فعال", true, Icons.Default.Lock) { }
                    Spacer(modifier = Modifier.height(8.dp))
                    Surface(shape = RoundedCornerShape(10.dp), color = Emerald50, border = BorderStroke(1.dp, Emerald100)) {
                        Text("✅ SQLCipher فعال - کلید در Android Keystore - امنیت بی‌رقیب", fontSize = 11.sp, color = Emerald600, modifier = Modifier.padding(10.dp))
                    }
                }
            }
        }

        // بخش ۲۳ و ۲۴: بک‌آپ و لاگ
        item {
            Card(modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp), shape = RoundedCornerShape(20.dp), colors = CardDefaults.cardColors(containerColor = Color.White), border = BorderStroke(1.dp, Slate200), elevation = CardDefaults.cardElevation(4.dp)) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.CloudUpload, contentDescription = null, tint = Sky600, modifier = Modifier.size(20.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("بک‌آپ و لاگ قضایی - بخش ۲۳ و ۲۴", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                    SettingSwitch("بک‌آپ خودکار ابری", "Google Drive رمزنگاری شده", isCloudBackupEnabled, Icons.Default.CloudUpload) { isCloudBackupEnabled = it }
                    Spacer(modifier = Modifier.height(12.dp))
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        Button(onClick = {}, modifier = Modifier.weight(1f), shape = RoundedCornerShape(12.dp), colors = ButtonDefaults.buttonColors(containerColor = Indigo600)) {
                            Text("بک‌آپ اکنون", fontSize = 11.sp)
                        }
                        OutlinedButton(onClick = {}, modifier = Modifier.weight(1f), shape = RoundedCornerShape(12.dp)) {
                            Text("بازیابی", fontSize = 11.sp)
                        }
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                    Surface(shape = RoundedCornerShape(10.dp), color = Slate100, border = BorderStroke(1.dp, Slate200)) {
                        Column(modifier = Modifier.padding(10.dp)) {
                            Text("لاگ قضایی بلاک‌چین:", fontWeight = FontWeight.Bold, fontSize = 11.sp)
                            Text("تمام تغییرات پرونده با هش SHA256 و مهر زمانی غیرقابل تغییر ثبت می‌شود - امنیت قضایی بی‌رقیب", fontSize = 10.sp, color = Slate600)
                        }
                    }
                }
            }
        }

        // اطلاعات نسخه
        item {
            Card(modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp), shape = RoundedCornerShape(20.dp), colors = CardDefaults.cardColors(containerColor = LuxuryNavy), elevation = CardDefaults.cardElevation(6.dp)) {
                Box(modifier = Modifier.fillMaxWidth().background(LuxuryNavyBrush).padding(20.dp)) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth()) {
                        Text("میلانو لگال", fontSize = 18.sp, fontWeight = FontWeight.ExtraBold, color = Color.White)
                        Text("نسخه لاکچری بی‌رقیب ۲۰۲۶ - v2.0", fontSize = 12.sp, color = LuxuryGold)
                        Spacer(modifier = Modifier.height(8.dp))
                        Text("شامل ۲۰ بخش پیشرفته • بدون شلوغی • دسته‌بندی هوشمند", fontSize = 10.sp, color = Slate300)
                        Spacer(modifier = Modifier.height(8.dp))
                        Surface(shape = RoundedCornerShape(20.dp), color = LuxuryGold.copy(alpha = 0.2f), border = BorderStroke(1.dp, LuxuryGold.copy(alpha = 0.4f))) {
                            Text("✨ بی‌حد و مرز • بی‌رقیب • ایرانی", fontSize = 10.sp, color = LuxuryGoldLight, fontWeight = FontWeight.Bold, modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp))
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ThemeOption(title: String, icon: ImageVector, isSelected: Boolean, modifier: Modifier = Modifier, onClick: () -> Unit) {
    Surface(shape = RoundedCornerShape(12.dp), color = if (isSelected) Indigo50 else Slate50, border = BorderStroke(1.dp, if (isSelected) Indigo600 else Slate200), modifier = modifier.then(Modifier.clickable { onClick() })) {
        Column(modifier = Modifier.padding(12.dp), horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(icon, contentDescription = null, tint = if (isSelected) Indigo600 else Slate500, modifier = Modifier.size(20.dp))
            Spacer(modifier = Modifier.height(4.dp))
            Text(title, fontSize = 11.sp, fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal, color = if (isSelected) Indigo600 else Slate700)
        }
    }
}

@Composable
fun SettingSwitch(title: String, subtitle: String, checked: Boolean, icon: ImageVector, onCheckedChange: (Boolean) -> Unit) {
    Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
        Icon(icon, contentDescription = null, tint = Slate600, modifier = Modifier.size(18.dp))
        Spacer(modifier = Modifier.width(10.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(title, fontSize = 12.sp, fontWeight = FontWeight.Medium)
            Text(subtitle, fontSize = 10.sp, color = Slate500)
        }
        Switch(checked = checked, onCheckedChange = onCheckedChange)
    }
}

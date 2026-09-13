package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.data.model.AiChatMessage
import com.example.data.model.VoiceProfiles
import com.example.data.service.OfflineAiService
import com.example.data.service.VoiceRecognitionManager
import com.example.ui.LegalViewModel
import com.example.ui.theme.*
import kotlinx.coroutines.launch

// تم یکدست سبز تیره لاکچری - هماهنگ با پرونده‌ها و Tools
private val Bg = LuxuryGreenDeep
private val CardBg = LuxuryGreenDark
private val CardElev = LuxuryGreenMedium
private val CardElev2 = LuxuryGreenLight
private val Gold = LuxuryGreenGold
private val GoldLight = LuxuryGreenGoldLight
private val GoldBrush = LuxuryGreenGoldBrush
private val Border = LuxuryGreenBorder
private val BorderStrong = LuxuryGreenBorderStrong

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AiAssistantScreen(
    viewModel: LegalViewModel,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val messages by viewModel.chatMessages.collectAsStateWithLifecycle()
    val isThinking by viewModel.isAiThinking.collectAsStateWithLifecycle()
    val isSpeaking by viewModel.isTtsSpeaking.collectAsStateWithLifecycle()
    val listState = rememberLazyListState()
    var inputText by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf("همه") }
    var isOfflineMode by remember { mutableStateOf(false) }
    var isListening by remember { mutableStateOf(false) }
    var selectedVoiceProfile by remember { mutableStateOf(VoiceProfiles.allProfiles[0]) }

    val voiceManager = remember { VoiceRecognitionManager(context) }
    val offlineService = remember { OfflineAiService() }
    val partialText by voiceManager.partialResults.collectAsStateWithLifecycle()
    val scope = rememberCoroutineScope()

    val categories = listOf("همه", "حقوقی", "کیفری", "خانواده", "مواعد قضایی")
    val suggestions = listOf("مهلت تجدیدنظر؟", "چک برگشتی", "مهریه", "پیش‌بینی رای", "قرارداد", "طلاق")

    LaunchedEffect(messages.size) {
        if (messages.isNotEmpty()) listState.animateScrollToItem(messages.size - 1)
    }

    Box(modifier = modifier.fillMaxSize().background(Bg)) {
        Column(modifier = Modifier.fillMaxSize()) {
            // هدر لاکچری سبز طلایی - منظم و جذاب
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(bottomStart = 24.dp, bottomEnd = 24.dp),
                colors = CardDefaults.cardColors(containerColor = CardBg),
                border = BorderStroke(0.5.dp, Border)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
                        Box(
                            modifier = Modifier.size(48.dp).clip(CircleShape).background(GoldBrush).shadow(8.dp, CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(Icons.Default.SmartToy, contentDescription = null, tint = Bg, modifier = Modifier.size(26.dp))
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text("دستیار هوشمند میلانو", fontSize = 15.sp, fontWeight = FontWeight.ExtraBold, color = Color.White)
                                Spacer(modifier = Modifier.width(8.dp))
                                Surface(shape = RoundedCornerShape(6.dp), color = Gold.copy(alpha = 0.15f), border = BorderStroke(0.5.dp, Gold.copy(alpha = 0.3f))) {
                                    Text(if (isOfflineMode) "OFFLINE AI" else "LUXURY AI", fontSize = 8.sp, fontWeight = FontWeight.Bold, color = Gold, modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp))
                                }
                            }
                            Text("STT فارسی • TTS فارسی • آفلاین Gemma 2B • ${selectedVoiceProfile.name}", fontSize = 9.sp, color = Color.White.copy(alpha = 0.6f), lineHeight = 12.sp)
                            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(top = 4.dp)) {
                                Box(modifier = Modifier.size(6.dp).clip(CircleShape).background(if (isOfflineMode) Gold else Color(0xFF10B981)))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(if (isOfflineMode) "آفلاین Gemma 2B فعال" else "آنلاین • تحلیل قوانین ایران", fontSize = 9.sp, color = if (isOfflineMode) GoldLight else Color.White.copy(alpha = 0.5f))
                            }
                        }
                        Surface(shape = CircleShape, color = CardElev, border = BorderStroke(0.5.dp, Border)) {
                            IconButton(onClick = { viewModel.clearChatHistory() }, modifier = Modifier.size(36.dp)) {
                                Icon(Icons.Default.DeleteSweep, contentDescription = null, tint = Color.White.copy(alpha = 0.6f), modifier = Modifier.size(18.dp))
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // 3 دکمه لاکچری طلایی - بسیار جذاب و منظم
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        // STT
                        Card(
                            shape = RoundedCornerShape(12.dp),
                            colors = CardDefaults.cardColors(containerColor = if (isListening) Gold else CardElev),
                            border = BorderStroke(1.dp, if (isListening) Gold else Border),
                            modifier = Modifier.weight(1f).clickable {
                                if (isListening) { voiceManager.stopListening(); isListening = false }
                                else {
                                    isListening = true
                                    voiceManager.startListeningPersian { text -> inputText = text; isListening = false }
                                }
                            }
                        ) {
                            Row(modifier = Modifier.padding(10.dp).fillMaxWidth(), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.Center) {
                                Icon(if (isListening) Icons.Default.Stop else Icons.Default.Mic, contentDescription = null, tint = if (isListening) Bg else Gold, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(if (isListening) "توقف" else "صحبت", fontSize = 11.sp, color = if (isListening) Bg else Color.White, fontWeight = FontWeight.Bold)
                            }
                        }
                        // Offline
                        Card(
                            shape = RoundedCornerShape(12.dp),
                            colors = CardDefaults.cardColors(containerColor = if (isOfflineMode) Gold else CardElev),
                            border = BorderStroke(1.dp, if (isOfflineMode) Gold else Border),
                            modifier = Modifier.weight(1f).clickable { isOfflineMode = !isOfflineMode }
                        ) {
                            Row(modifier = Modifier.padding(10.dp).fillMaxWidth(), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.Center) {
                                Icon(if (isOfflineMode) Icons.Default.CloudOff else Icons.Default.Cloud, contentDescription = null, tint = if (isOfflineMode) Bg else Gold, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(if (isOfflineMode) "آفلاین" else "آنلاین", fontSize = 11.sp, color = if (isOfflineMode) Bg else Color.White, fontWeight = FontWeight.Bold)
                            }
                        }
                        // Voice
                        Card(
                            shape = RoundedCornerShape(12.dp),
                            colors = CardDefaults.cardColors(containerColor = CardElev),
                            border = BorderStroke(1.dp, Border),
                            modifier = Modifier.weight(1f)
                        ) {
                            Row(modifier = Modifier.padding(10.dp).fillMaxWidth(), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.Center) {
                                Icon(Icons.Default.RecordVoiceOver, contentDescription = null, tint = Gold, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(selectedVoiceProfile.tone, fontSize = 11.sp, color = Color.White, fontWeight = FontWeight.Bold, maxLines = 1)
                            }
                        }
                    }

                    if (partialText.isNotBlank()) {
                        Spacer(modifier = Modifier.height(8.dp))
                        Surface(shape = RoundedCornerShape(8.dp), color = CardElev, border = BorderStroke(0.5.dp, Border)) {
                            Text(partialText, fontSize = 11.sp, color = Color.White, modifier = Modifier.padding(10.dp))
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))
                    LazyRow(horizontalArrangement = Arrangement.spacedBy(6.dp), modifier = Modifier.fillMaxWidth()) {
                        items(categories.size) { idx ->
                            val cat = categories[idx]
                            val isSelected = selectedCategory == cat
                            if (isSelected) {
                                Box(modifier = Modifier.clip(RoundedCornerShape(10.dp)).background(GoldBrush).clickable { selectedCategory = cat }.padding(horizontal = 14.dp, vertical = 7.dp)) {
                                    Text(cat, fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Bg)
                                }
                            } else {
                                Surface(shape = RoundedCornerShape(10.dp), color = CardElev, border = BorderStroke(0.5.dp, Border), modifier = Modifier.clickable { selectedCategory = cat }) {
                                    Text(cat, fontSize = 11.sp, color = Color.White.copy(alpha = 0.7f), modifier = Modifier.padding(horizontal = 14.dp, vertical = 7.dp))
                                }
                            }
                        }
                    }
                }
            }

            // پیام‌ها - ارتباط آسان و منظم
            LazyColumn(state = listState, modifier = Modifier.weight(1f).fillMaxWidth(), contentPadding = PaddingValues(16.dp), verticalArrangement = Arrangement.spacedBy(14.dp)) {
                items(messages, key = { it.id }) { message ->
                    LuxuryChatBubbleGreen(
                        message = message,
                        isSpeaking = isSpeaking,
                        onSpeakClick = { viewModel.speakPersianText(message.content) },
                        onStopClick = { viewModel.stopSpeaking() },
                        onDeleteClick = { viewModel.deleteChatMessage(message.id) }
                    )
                }
                if (isThinking) {
                    item { AiThinkingBubbleGreen(isOffline = isOfflineMode) }
                }
                item { Spacer(modifier = Modifier.height(8.dp)) }
            }

            // ورودی - لاکچری و خلاقانه - ارتباط راحت
            Card(
                modifier = Modifier.fillMaxWidth().shadow(16.dp, RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp)),
                shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
                colors = CardDefaults.cardColors(containerColor = CardBg),
                border = BorderStroke(1.dp, BorderStrong)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    LazyRow(horizontalArrangement = Arrangement.spacedBy(6.dp), modifier = Modifier.fillMaxWidth()) {
                        items(suggestions.size) { idx ->
                            val sug = suggestions[idx]
                            Surface(shape = RoundedCornerShape(12.dp), color = CardElev, border = BorderStroke(0.5.dp, Border), modifier = Modifier.clickable { inputText = sug }) {
                                Text(sug, fontSize = 10.sp, color = Color.White.copy(alpha = 0.8f), modifier = Modifier.padding(horizontal = 12.dp, vertical = 7.dp))
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                    Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.Bottom) {
                        OutlinedTextField(
                            value = inputText,
                            onValueChange = { inputText = it },
                            placeholder = { Text(if (isOfflineMode) "سوال آفلاین با Gemma 2B..." else "سوال حقوقی فارسی + صحبت صوتی 🎙️", fontSize = 11.sp, color = Color.White.copy(alpha = 0.4f)) },
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(18.dp),
                            minLines = 1,
                            maxLines = 4,
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = Gold,
                                unfocusedBorderColor = Border,
                                focusedContainerColor = CardElev,
                                unfocusedContainerColor = CardElev,
                                focusedTextColor = Color.White,
                                unfocusedTextColor = Color.White,
                                cursorColor = Gold
                            )
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        // Mic لاکچری
                        Box(
                            modifier = Modifier.size(48.dp).clip(CircleShape).background(if (isListening) Gold else CardElev).shadow(0.dp).clickable {
                                if (isListening) { voiceManager.stopListening(); isListening = false }
                                else {
                                    isListening = true
                                    voiceManager.startListeningPersian { text -> inputText = text; isListening = false }
                                }
                            }.then(if (!isListening) Modifier.background(CardElev) else Modifier),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(if (isListening) Icons.Default.Stop else Icons.Default.Mic, contentDescription = null, tint = if (isListening) Bg else Gold, modifier = Modifier.size(20.dp))
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        // Send طلایی لاکچری
                        Box(
                            modifier = Modifier.size(52.dp).clip(CircleShape).background(if (inputText.isBlank()) CardElev2 else GoldBrush).shadow(8.dp, CircleShape).clip(CircleShape).clickable(enabled = inputText.isNotBlank() && !isThinking) {
                                if (inputText.isNotBlank()) {
                                    if (isOfflineMode) {
                                        scope.launch {
                                            val result = offlineService.askOffline(inputText)
                                            result.onSuccess { answer -> viewModel.sendAiMessage("آفلاین: $inputText\nپاسخ: $answer") }
                                        }
                                    } else {
                                        viewModel.sendAiMessage(inputText)
                                    }
                                    inputText = ""
                                }
                            },
                            contentAlignment = Alignment.Center
                        ) {
                            if (isThinking) CircularProgressIndicator(modifier = Modifier.size(20.dp), color = Bg, strokeWidth = 2.dp)
                            else Icon(Icons.Default.Send, contentDescription = null, tint = if (inputText.isBlank()) Color.White.copy(alpha = 0.3f) else Bg, modifier = Modifier.size(22.dp))
                        }
                    }
                    Spacer(modifier = Modifier.height(10.dp))
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.RecordVoiceOver, contentDescription = null, tint = if (isSpeaking) Gold else Color.White.copy(alpha = 0.3f), modifier = Modifier.size(14.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(if (isSpeaking) "🔊 در حال خواندن با صدای ${selectedVoiceProfile.name}..." else "STT فارسی • TTS فارسی • آفلاین • ۵ صدای وکیل لاکچری", fontSize = 9.sp, color = if (isSpeaking) GoldLight else Color.White.copy(alpha = 0.4f))
                        }
                        if (isSpeaking) {
                            Surface(shape = RoundedCornerShape(10.dp), color = Gold.copy(alpha = 0.15f), border = BorderStroke(0.5.dp, Gold.copy(alpha = 0.3f)), modifier = Modifier.clickable { viewModel.stopSpeaking() }) {
                                Row(modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp), verticalAlignment = Alignment.CenterVertically) {
                                    Icon(Icons.Default.Stop, contentDescription = null, tint = Gold, modifier = Modifier.size(12.dp))
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text("توقف", fontSize = 10.sp, color = Gold, fontWeight = FontWeight.Bold)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun LuxuryChatBubbleGreen(message: AiChatMessage, isSpeaking: Boolean, onSpeakClick: () -> Unit, onStopClick: () -> Unit, onDeleteClick: () -> Unit) {
    val isUser = message.role == "user"
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = if (isUser) Arrangement.End else Arrangement.Start) {
        if (!isUser) {
            Box(modifier = Modifier.size(36.dp).clip(CircleShape).background(GoldBrush), contentAlignment = Alignment.Center) {
                Icon(Icons.Default.SmartToy, contentDescription = null, tint = Bg, modifier = Modifier.size(20.dp))
            }
            Spacer(modifier = Modifier.width(8.dp))
        }
        Card(
            modifier = Modifier.widthIn(max = 300.dp).shadow(6.dp, RoundedCornerShape(18.dp)),
            shape = RoundedCornerShape(topStart = if (isUser) 18.dp else 4.dp, topEnd = if (isUser) 4.dp else 18.dp, bottomStart = 18.dp, bottomEnd = 18.dp),
            colors = CardDefaults.cardColors(containerColor = if (isUser) Gold else CardBg),
            border = BorderStroke(0.5.dp, if (isUser) GoldLight else Border),
            elevation = CardDefaults.cardElevation(2.dp)
        ) {
            Column(modifier = Modifier.padding(14.dp)) {
                if (!isUser && message.legalCategory.isNotBlank()) {
                    Surface(shape = RoundedCornerShape(6.dp), color = CardElev, border = BorderStroke(0.5.dp, Border)) {
                        Text(message.legalCategory, fontSize = 10.sp, fontWeight = FontWeight.Bold, color = Gold, modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp))
                    }
                    Spacer(modifier = Modifier.height(6.dp))
                }
                Text(message.content, fontSize = 13.sp, lineHeight = 19.sp, color = if (isUser) Bg else Color.White)
                if (!isUser && message.relatedLawArticles.isNotBlank()) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Surface(shape = RoundedCornerShape(8.dp), color = CardElev, border = BorderStroke(0.5.dp, Border)) {
                        Row(modifier = Modifier.padding(8.dp), verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Gavel, contentDescription = null, tint = Gold, modifier = Modifier.size(14.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(message.relatedLawArticles, fontSize = 10.sp, color = GoldLight, fontWeight = FontWeight.Medium)
                        }
                    }
                }
                Spacer(modifier = Modifier.height(8.dp))
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                    Text(formatTimestampGreen(message.timestamp), fontSize = 9.sp, color = if (isUser) Bg.copy(alpha = 0.7f) else Color.White.copy(alpha = 0.4f))
                    if (!isUser) {
                        Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                            Surface(shape = CircleShape, color = CardElev, border = BorderStroke(0.5.dp, Border), modifier = Modifier.size(28.dp).clickable { if (isSpeaking) onStopClick() else onSpeakClick() }) {
                                Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
                                    Icon(if (isSpeaking) Icons.Default.Stop else Icons.Default.VolumeUp, contentDescription = null, tint = if (isSpeaking) Color(0xFFF87171) else Gold, modifier = Modifier.size(16.dp))
                                }
                            }
                            Surface(shape = CircleShape, color = CardElev, border = BorderStroke(0.5.dp, Border), modifier = Modifier.size(28.dp).clickable { onDeleteClick() }) {
                                Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
                                    Icon(Icons.Default.DeleteOutline, contentDescription = null, tint = Color.White.copy(alpha = 0.4f), modifier = Modifier.size(16.dp))
                                }
                            }
                        }
                    }
                }
            }
        }
        if (isUser) {
            Spacer(modifier = Modifier.width(8.dp))
            Box(modifier = Modifier.size(36.dp).clip(CircleShape).background(GoldBrush), contentAlignment = Alignment.Center) {
                Icon(Icons.Default.Person, contentDescription = null, tint = Bg, modifier = Modifier.size(20.dp))
            }
        }
    }
}

@Composable
fun AiThinkingBubbleGreen(isOffline: Boolean = false) {
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Start) {
        Box(modifier = Modifier.size(36.dp).clip(CircleShape).background(GoldBrush), contentAlignment = Alignment.Center) {
            Icon(if (isOffline) Icons.Default.CloudOff else Icons.Default.SmartToy, contentDescription = null, tint = Bg, modifier = Modifier.size(20.dp))
        }
        Spacer(modifier = Modifier.width(8.dp))
        Card(shape = RoundedCornerShape(18.dp, 18.dp, 18.dp, 4.dp), colors = CardDefaults.cardColors(containerColor = CardBg), border = BorderStroke(1.dp, Border), elevation = CardDefaults.cardElevation(2.dp)) {
            Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                CircularProgressIndicator(modifier = Modifier.size(16.dp), strokeWidth = 2.dp, color = Gold)
                Spacer(modifier = Modifier.width(10.dp))
                Text(if (isOffline) "مدل آفلاین Gemma 2B در حال تحلیل..." else "در حال تحلیل بر اساس قوانین رسمی ایران...", fontSize = 11.sp, color = Color.White.copy(alpha = 0.7f), fontWeight = FontWeight.Medium)
            }
        }
    }
}

fun formatTimestampGreen(timestamp: Long): String {
    val now = System.currentTimeMillis()
    val diff = now - timestamp
    return when {
        diff < 60_000 -> "هم‌اکنون"
        diff < 3600_000 -> "${diff / 60_000} دقیقه پیش"
        diff < 86400_000 -> "${diff / 3600_000} ساعت پیش"
        else -> java.text.SimpleDateFormat("HH:mm", java.util.Locale.getDefault()).format(java.util.Date(timestamp))
    }
}

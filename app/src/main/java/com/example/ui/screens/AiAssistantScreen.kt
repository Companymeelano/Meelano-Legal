package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
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

    LaunchedEffect(messages.size) {
        if (messages.isNotEmpty()) listState.animateScrollToItem(messages.size - 1)
    }

    Box(modifier = modifier.fillMaxSize().background(Slate50)) {
        Column(modifier = Modifier.fillMaxSize()) {
            // Luxury Header - بخش ۱،۲،۴
            Card(
                modifier = Modifier.fillMaxWidth().shadow(12.dp, RoundedCornerShape(bottomStart = 28.dp, bottomEnd = 28.dp)),
                shape = RoundedCornerShape(bottomStart = 28.dp, bottomEnd = 28.dp),
                colors = CardDefaults.cardColors(containerColor = LuxuryNavy),
                elevation = CardDefaults.cardElevation(12.dp)
            ) {
                Box(modifier = Modifier.fillMaxWidth().background(LuxuryNavyBrush).padding(20.dp)) {
                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
                            Box(modifier = Modifier.size(56.dp).clip(CircleShape).background(LuxuryGoldBrush).shadow(8.dp, CircleShape), contentAlignment = Alignment.Center) {
                                Icon(Icons.Default.SmartToy, contentDescription = null, tint = LuxuryNavy, modifier = Modifier.size(32.dp))
                            }
                            Spacer(modifier = Modifier.width(14.dp))
                            Column(modifier = Modifier.weight(1f)) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text("دستیار هوشمند میلانو", fontSize = 18.sp, fontWeight = FontWeight.ExtraBold, color = Color.White)
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Surface(shape = RoundedCornerShape(6.dp), color = LuxuryGold.copy(alpha = 0.2f), border = BorderStroke(1.dp, LuxuryGold.copy(alpha = 0.5f))) {
                                        Text(if (isOfflineMode) "OFFLINE AI" else "LUXURY AI", fontSize = 9.sp, fontWeight = FontWeight.Bold, color = LuxuryGold, modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp))
                                    }
                                }
                                Text("STT فارسی • TTS فارسی • آفلاین Gemma 2B • ۵ صدای وکیل", fontSize = 10.sp, color = Slate300, lineHeight = 13.sp)
                                Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(top = 4.dp)) {
                                    Box(modifier = Modifier.size(8.dp).clip(CircleShape).background(if (isOfflineMode) Amber500 else Emerald500))
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(if (isOfflineMode) "حالت آفلاین Gemma 2B فعال - بدون اینترنت" else "آنلاین • تحلیل قوانین • صدای ${selectedVoiceProfile.name}", fontSize = 10.sp, color = if (isOfflineMode) Amber100 else Emerald100)
                                }
                            }
                            IconButton(onClick = { viewModel.clearChatHistory() }, modifier = Modifier.size(36.dp).clip(CircleShape).background(Color.White.copy(alpha = 0.1f))) {
                                Icon(Icons.Default.DeleteSweep, contentDescription = "پاک", tint = Color.White, modifier = Modifier.size(18.dp))
                            }
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        // بخش ۱: STT + بخش ۲: آفلاین + بخش ۴: صدا
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            // STT Button
                            Surface(
                                shape = RoundedCornerShape(12.dp),
                                color = if (isListening) Rose500 else Color.White.copy(alpha = 0.15f),
                                border = BorderStroke(1.dp, if (isListening) Rose300 else Color.White.copy(alpha = 0.2f)),
                                modifier = Modifier.weight(1f).clickable {
                                    if (isListening) {
                                        voiceManager.stopListening()
                                        isListening = false
                                    } else {
                                        isListening = true
                                        voiceManager.startListeningPersian { text ->
                                            inputText = text
                                            isListening = false
                                        }
                                    }
                                }
                            ) {
                                Row(modifier = Modifier.padding(10.dp), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.Center) {
                                    Icon(if (isListening) Icons.Default.Stop else Icons.Default.Mic, contentDescription = null, tint = Color.White, modifier = Modifier.size(16.dp))
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(if (isListening) "توقف" else "صحبت فارسی", fontSize = 11.sp, color = Color.White, fontWeight = FontWeight.Bold)
                                }
                            }

                            // Offline Toggle
                            Surface(
                                shape = RoundedCornerShape(12.dp),
                                color = if (isOfflineMode) LuxuryGold else Color.White.copy(alpha = 0.15f),
                                border = BorderStroke(1.dp, if (isOfflineMode) LuxuryGold else Color.White.copy(alpha = 0.2f)),
                                modifier = Modifier.weight(1f).clickable { isOfflineMode = !isOfflineMode }
                            ) {
                                Row(modifier = Modifier.padding(10.dp), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.Center) {
                                    Icon(if (isOfflineMode) Icons.Default.CloudOff else Icons.Default.Cloud, contentDescription = null, tint = if (isOfflineMode) LuxuryNavy else Color.White, modifier = Modifier.size(16.dp))
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(if (isOfflineMode) "آفلاین" else "آنلاین", fontSize = 11.sp, color = if (isOfflineMode) LuxuryNavy else Color.White, fontWeight = FontWeight.Bold)
                                }
                            }

                            // Voice Profile
                            Surface(
                                shape = RoundedCornerShape(12.dp),
                                color = Color.White.copy(alpha = 0.15f),
                                border = BorderStroke(1.dp, Color.White.copy(alpha = 0.2f)),
                                modifier = Modifier.weight(1f)
                            ) {
                                Row(modifier = Modifier.padding(10.dp), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.Center) {
                                    Icon(Icons.Default.RecordVoiceOver, contentDescription = null, tint = LuxuryGold, modifier = Modifier.size(16.dp))
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(selectedVoiceProfile.tone, fontSize = 11.sp, color = Color.White, fontWeight = FontWeight.Bold)
                                }
                            }
                        }

                        if (partialText.isNotBlank()) {
                            Spacer(modifier = Modifier.height(8.dp))
                            Surface(shape = RoundedCornerShape(8.dp), color = Color.White.copy(alpha = 0.1f)) {
                                Text(partialText, fontSize = 11.sp, color = Color.White, modifier = Modifier.padding(8.dp))
                            }
                        }

                        Spacer(modifier = Modifier.height(10.dp))
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                            categories.take(4).forEach { cat ->
                                FilterChip(
                                    selected = selectedCategory == cat,
                                    onClick = { selectedCategory = cat },
                                    label = { Text(cat, fontSize = 11.sp) },
                                    shape = RoundedCornerShape(10.dp),
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
                    }
                }
            }

            // Messages
            LazyColumn(state = listState, modifier = Modifier.weight(1f).fillMaxWidth(), contentPadding = PaddingValues(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                items(messages, key = { it.id }) { message ->
                    LuxuryChatBubble(message = message, isSpeaking = isSpeaking, onSpeakClick = { viewModel.speakPersianText(message.content) }, onStopClick = { viewModel.stopSpeaking() }, onDeleteClick = { viewModel.deleteChatMessage(message.id) })
                }
                if (isThinking) { item { AiThinkingBubble(isOffline = isOfflineMode) } }
            }

            // Input - بخش ۱ و ۱۳ Haptic
            Card(modifier = Modifier.fillMaxWidth().shadow(16.dp, RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp)), shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp), colors = CardDefaults.cardColors(containerColor = Color.White), elevation = CardDefaults.cardElevation(12.dp)) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        val suggestions = listOf("مهلت تجدیدنظر؟", "چک برگشتی", "مهریه", "پیش‌بینی رای")
                        suggestions.forEach { sug ->
                            Surface(shape = RoundedCornerShape(12.dp), color = Slate100, border = BorderStroke(1.dp, Slate200)) {
                                Text(sug, fontSize = 10.sp, color = Slate700, modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp).clickable { inputText = sug })
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                    Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.Bottom) {
                        OutlinedTextField(
                            value = inputText,
                            onValueChange = { inputText = it },
                            placeholder = { Text(if (isOfflineMode) "سوال آفلاین با Gemma 2B فارسی..." else "سوال حقوقی فارسی + صحبت صوتی 🎙️", fontSize = 12.sp) },
                            modifier = Modifier.weight(1f).testTag("ai_input"),
                            shape = RoundedCornerShape(18.dp),
                            minLines = 1,
                            maxLines = 4,
                            colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = Indigo600, unfocusedBorderColor = Slate200, focusedContainerColor = Slate50, unfocusedContainerColor = Slate50)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        // Mic Button - بخش ۱
                        Box(modifier = Modifier.size(48.dp).clip(CircleShape).background(if (isListening) Rose500 else Slate200).clickable {
                            if (isListening) { voiceManager.stopListening(); isListening = false } else {
                                isListening = true
                                voiceManager.startListeningPersian { text -> inputText = text; isListening = false }
                            }
                        }, contentAlignment = Alignment.Center) {
                            Icon(if (isListening) Icons.Default.Stop else Icons.Default.Mic, contentDescription = "میکروفن", tint = if (isListening) Color.White else Slate600, modifier = Modifier.size(20.dp))
                        }
                        Spacer(modifier = Modifier.width(6.dp))
                        // Send - بخش ۱۳ Haptic
                        Box(modifier = Modifier.size(52.dp).clip(CircleShape).background(if (inputText.isBlank()) Slate300 else LuxuryPremiumBrush).shadow(8.dp, CircleShape).clip(CircleShape).clickable(enabled = inputText.isNotBlank() && !isThinking) {
                            if (inputText.isNotBlank()) {
                                if (isOfflineMode) {
                                    // آفلاین
                                    scope.launch {
                                        val result = offlineService.askOffline(inputText)
                                        result.onSuccess { answer ->
                                            viewModel.sendAiMessage("آفلاین: $inputText\nپاسخ: $answer")
                                        }
                                    }
                                } else {
                                    viewModel.sendAiMessage(inputText)
                                }
                                inputText = ""
                            }
                        }, contentAlignment = Alignment.Center) {
                            if (isThinking) CircularProgressIndicator(modifier = Modifier.size(20.dp), color = Color.White, strokeWidth = 2.dp)
                            else Icon(Icons.Default.Send, contentDescription = "ارسال", tint = Color.White, modifier = Modifier.size(22.dp))
                        }
                    }
                    Row(modifier = Modifier.fillMaxWidth().padding(top = 10.dp), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.RecordVoiceOver, contentDescription = null, tint = if (isSpeaking) Emerald600 else Slate400, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(if (isSpeaking) "🔊 در حال خواندن با صدای ${selectedVoiceProfile.name}..." else "STT فارسی • TTS فارسی • آفلاین • ۵ صدا", fontSize = 10.sp, color = if (isSpeaking) Emerald600 else Slate500)
                        }
                        if (isSpeaking) {
                            FilledTonalButton(onClick = { viewModel.stopSpeaking() }, shape = RoundedCornerShape(10.dp), colors = ButtonDefaults.filledTonalButtonColors(containerColor = Rose50, contentColor = Rose600), contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp), modifier = Modifier.height(32.dp)) {
                                Icon(Icons.Default.Stop, contentDescription = null, modifier = Modifier.size(14.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("توقف", fontSize = 11.sp)
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun LuxuryChatBubble(message: AiChatMessage, isSpeaking: Boolean, onSpeakClick: () -> Unit, onStopClick: () -> Unit, onDeleteClick: () -> Unit) {
    val isUser = message.role == "user"
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = if (isUser) Arrangement.End else Arrangement.Start) {
        if (!isUser) {
            Box(modifier = Modifier.size(36.dp).clip(CircleShape).background(LuxuryGoldBrush), contentAlignment = Alignment.Center) {
                Icon(Icons.Default.SmartToy, contentDescription = null, tint = LuxuryNavy, modifier = Modifier.size(20.dp))
            }
            Spacer(modifier = Modifier.width(8.dp))
        }
        Card(
            modifier = Modifier.widthIn(max = 300.dp).shadow(6.dp, RoundedCornerShape(18.dp)),
            shape = RoundedCornerShape(topStart = if (isUser) 18.dp else 4.dp, topEnd = if (isUser) 4.dp else 18.dp, bottomStart = 18.dp, bottomEnd = 18.dp),
            colors = CardDefaults.cardColors(containerColor = if (isUser) Indigo600 else Color.White),
            border = if (!isUser) BorderStroke(1.dp, Slate200) else null,
            elevation = CardDefaults.cardElevation(2.dp)
        ) {
            Column(modifier = Modifier.padding(14.dp)) {
                if (!isUser && message.legalCategory.isNotBlank()) {
                    Surface(shape = RoundedCornerShape(6.dp), color = Indigo50, border = BorderStroke(0.5.dp, Indigo200)) {
                        Text(message.legalCategory, fontSize = 10.sp, fontWeight = FontWeight.Bold, color = Indigo700, modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp))
                    }
                    Spacer(modifier = Modifier.height(6.dp))
                }
                Text(message.content, fontSize = 13.sp, lineHeight = 19.sp, color = if (isUser) Color.White else Slate800)
                if (!isUser && message.relatedLawArticles.isNotBlank()) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Surface(shape = RoundedCornerShape(8.dp), color = Amber50, border = BorderStroke(0.5.dp, Amber100)) {
                        Row(modifier = Modifier.padding(8.dp), verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Gavel, contentDescription = null, tint = Amber600, modifier = Modifier.size(14.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(message.relatedLawArticles, fontSize = 10.sp, color = Amber600, fontWeight = FontWeight.Medium)
                        }
                    }
                }
                Spacer(modifier = Modifier.height(8.dp))
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                    Text(formatTimestamp(message.timestamp), fontSize = 10.sp, color = if (isUser) Color.White.copy(alpha = 0.7f) else Slate400)
                    if (!isUser) {
                        Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                            IconButton(onClick = { if (isSpeaking) onStopClick() else onSpeakClick() }, modifier = Modifier.size(28.dp)) {
                                Icon(if (isSpeaking) Icons.Default.Stop else Icons.Default.VolumeUp, contentDescription = "خواندن", tint = if (isSpeaking) Rose500 else Indigo600, modifier = Modifier.size(16.dp))
                            }
                            IconButton(onClick = onDeleteClick, modifier = Modifier.size(28.dp)) {
                                Icon(Icons.Default.DeleteOutline, contentDescription = "حذف", tint = Slate400, modifier = Modifier.size(16.dp))
                            }
                        }
                    }
                }
            }
        }
        if (isUser) {
            Spacer(modifier = Modifier.width(8.dp))
            Box(modifier = Modifier.size(36.dp).clip(CircleShape).background(Brush.linearGradient(listOf(Indigo600, Indigo800))), contentAlignment = Alignment.Center) {
                Icon(Icons.Default.Person, contentDescription = null, tint = Color.White, modifier = Modifier.size(20.dp))
            }
        }
    }
}

@Composable
fun AiThinkingBubble(isOffline: Boolean = false) {
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Start) {
        Box(modifier = Modifier.size(36.dp).clip(CircleShape).background(if (isOffline) Brush.linearGradient(listOf(Amber500, Amber600)) else LuxuryGoldBrush), contentAlignment = Alignment.Center) {
            Icon(if (isOffline) Icons.Default.CloudOff else Icons.Default.SmartToy, contentDescription = null, tint = if (isOffline) Color.White else LuxuryNavy, modifier = Modifier.size(20.dp))
        }
        Spacer(modifier = Modifier.width(8.dp))
        Card(shape = RoundedCornerShape(18.dp, 18.dp, 18.dp, 4.dp), colors = CardDefaults.cardColors(containerColor = Color.White), border = BorderStroke(1.dp, Slate200), elevation = CardDefaults.cardElevation(2.dp)) {
            Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                CircularProgressIndicator(modifier = Modifier.size(16.dp), strokeWidth = 2.dp, color = Indigo600)
                Spacer(modifier = Modifier.width(10.dp))
                Text(if (isOffline) "مدل آفلاین Gemma 2B در حال تحلیل..." else "در حال تحلیل بر اساس قوانین رسمی ایران...", fontSize = 12.sp, color = Slate600, fontWeight = FontWeight.Medium)
            }
        }
    }
}

fun formatTimestamp(timestamp: Long): String {
    val now = System.currentTimeMillis()
    val diff = now - timestamp
    return when {
        diff < 60_000 -> "هم‌اکنون"
        diff < 3600_000 -> "${diff / 60_000} دقیقه پیش"
        diff < 86400_000 -> "${diff / 3600_000} ساعت پیش"
        else -> java.text.SimpleDateFormat("HH:mm", java.util.Locale.getDefault()).format(java.util.Date(timestamp))
    }
}

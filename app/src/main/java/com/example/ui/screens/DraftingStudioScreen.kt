package com.example.ui.screens

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.EditNote
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material.icons.filled.SmartToy
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.ui.LegalViewModel
import com.example.ui.components.DraftCard
import com.example.ui.components.LuxuryGlassCard
import com.example.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DraftingStudioScreen(
    viewModel: LegalViewModel,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val allDrafts by viewModel.drafts.collectAsStateWithLifecycle()
    val isSpeaking by viewModel.isTtsSpeaking.collectAsStateWithLifecycle()

    val draftTypes = listOf("لایحه دفاعیه", "دادخواست حقوقی", "شکواییه کیفری", "اظهارنامه رسمی")
    var selectedType by remember { mutableStateOf(draftTypes[0]) }
    var typeExpanded by remember { mutableStateOf(false) }

    var subject by remember { mutableStateOf("") }
    var courtHeading by remember { mutableStateOf("ریاست و مستشاران محترم دادگاه تجدیدنظر استان تهران") }
    var caseNumber by remember { mutableStateOf("۱۴۰۳۹۱۰۰۰۴۱۸۲۹۳") }
    var clientName by remember { mutableStateOf("شرکت بازرگانی میلانو نوین") }
    var opponentName by remember { mutableStateOf("شرکت ساختمانی فراز گستر پایدار") }

    Box(modifier = modifier.fillMaxSize().background(Slate50)) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(bottom = 100.dp)
        ) {
            // Luxury Header
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .shadow(8.dp, RoundedCornerShape(bottomStart = 24.dp, bottomEnd = 24.dp)),
                    shape = RoundedCornerShape(bottomStart = 24.dp, bottomEnd = 24.dp),
                    colors = CardDefaults.cardColors(containerColor = LuxuryNavy)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(LuxuryNavyBrush)
                            .padding(20.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(48.dp)
                                    .background(Brush.linearGradient(listOf(LuxuryGold, LuxuryGoldDark)), RoundedCornerShape(14.dp))
                                    .padding(8.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(Icons.Default.EditNote, contentDescription = null, tint = LuxuryNavy, modifier = Modifier.size(24.dp))
                            }
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Text(
                                    text = "استودیو تنظیم هوشمند لوایح ✨",
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = Color.White
                                )
                                Text(
                                    text = "تولید خودکار با هوش مصنوعی فارسی + صدای گویا + تحلیل قوانین ایران",
                                    fontSize = 11.sp,
                                    color = Slate300
                                )
                            }
                        }
                    }
                }
            }

            // Drafting Generator Card - Luxury
            item {
                LuxuryGlassCard(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.AutoAwesome,
                            contentDescription = null,
                            tint = Indigo600,
                            modifier = Modifier.size(22.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "تنظیم و انشای سند حقوقی لاکچری با AI فارسی",
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = 14.sp,
                            color = Slate900
                        )
                    }

                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "مستند به مواد قانونی رسمی ایران + خوانش صوتی فارسی",
                        fontSize = 11.sp,
                        color = Slate500
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    ExposedDropdownMenuBox(
                        expanded = typeExpanded,
                        onExpandedChange = { typeExpanded = it },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        OutlinedTextField(
                            value = selectedType,
                            onValueChange = {},
                            readOnly = true,
                            shape = RoundedCornerShape(12.dp),
                            label = { Text("نوع سند حقوقی") },
                            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = typeExpanded) },
                            modifier = Modifier.menuAnchor(MenuAnchorType.PrimaryNotEditable).fillMaxWidth()
                        )
                        ExposedDropdownMenu(
                            expanded = typeExpanded,
                            onDismissRequest = { typeExpanded = false }
                        ) {
                            draftTypes.forEach { t ->
                                DropdownMenuItem(
                                    text = { Text(t) },
                                    onClick = {
                                        selectedType = t
                                        typeExpanded = false
                                    }
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    OutlinedTextField(
                        value = subject,
                        onValueChange = { subject = it },
                        placeholder = { Text("مثلاً: مطالبه وجه التزام، بطلان معامله، کلاهبرداری") },
                        label = { Text("موضوع سند / خواسته*") },
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.fillMaxWidth().testTag("input_draft_subject"),
                        singleLine = true
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    Row(modifier = Modifier.fillMaxWidth()) {
                        OutlinedTextField(
                            value = clientName,
                            onValueChange = { clientName = it },
                            label = { Text("نام موکل") },
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.weight(1f).testTag("input_draft_client"),
                            singleLine = true
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        OutlinedTextField(
                            value = opponentName,
                            onValueChange = { opponentName = it },
                            label = { Text("طرف مقابل") },
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.weight(1f).testTag("input_draft_opponent"),
                            singleLine = true
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Row(modifier = Modifier.fillMaxWidth()) {
                        OutlinedTextField(
                            value = caseNumber,
                            onValueChange = { caseNumber = it },
                            label = { Text("شماره پرونده") },
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.weight(1f).testTag("input_draft_case"),
                            singleLine = true
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        OutlinedTextField(
                            value = courtHeading,
                            onValueChange = { courtHeading = it },
                            label = { Text("مرجع رسیدگی") },
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.weight(2f).testTag("input_draft_court"),
                            singleLine = true
                        )
                    }

                    Spacer(modifier = Modifier.height(18.dp))

                    // Luxury Generate Button with AI
                    Button(
                        onClick = {
                            val subj = if (subject.isBlank()) "مطالبه وجه و خسارات قراردادی" else subject
                            viewModel.createAutoDraft(
                                type = selectedType,
                                court = courtHeading,
                                caseNumber = caseNumber,
                                client = clientName,
                                opponent = opponentName,
                                subject = subj
                            )
                            subject = ""
                        },
                        shape = RoundedCornerShape(14.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(52.dp)
                            .shadow(8.dp, RoundedCornerShape(14.dp))
                            .testTag("btn_generate_draft"),
                        colors = ButtonDefaults.buttonColors(containerColor = Indigo600)
                    ) {
                        Icon(Icons.Default.SmartToy, contentDescription = null, modifier = Modifier.size(20.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("انشای هوشمند لاکچری با AI فارسی ✨", fontSize = 14.sp, fontWeight = FontWeight.Bold)
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    // TTS Info
                    Surface(
                        shape = RoundedCornerShape(10.dp),
                        color = Emerald50,
                        border = BorderStroke(1.dp, Emerald100)
                    ) {
                        Row(modifier = Modifier.padding(10.dp), verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.VolumeUp, contentDescription = null, tint = Emerald600, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "تمام لوایح با صدای فارسی قابل خوانش هستند - روی دکمه 🔊 در کارت لایحه بزنید",
                                fontSize = 11.sp,
                                color = Emerald600
                            )
                        }
                    }
                }
            }

            // Saved Drafts Header
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "لوایح ذخیره شده لاکچری (${allDrafts.size})",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.ExtraBold,
                        color = Slate900
                    )
                    if (isSpeaking) {
                        Surface(shape = RoundedCornerShape(20.dp), color = Rose50, border = BorderStroke(1.dp, Rose100)) {
                            Text("🔊 در حال خواندن...", fontSize = 10.sp, color = Rose600, modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp))
                        }
                    }
                }
            }

            if (allDrafts.isEmpty()) {
                item {
                    LuxuryGlassCard(modifier = Modifier.padding(16.dp)) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(24.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Icon(
                                imageVector = Icons.Default.Description,
                                contentDescription = null,
                                modifier = Modifier.size(48.dp),
                                tint = Slate300
                            )
                            Spacer(modifier = Modifier.height(12.dp))
                            Text(
                                text = "هنوز لایحه‌ای ثبت نشده",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = Slate700
                            )
                            Text(
                                text = "با فرم لاکچری بالا اولین لایحه هوشمند خود را بسازید",
                                fontSize = 12.sp,
                                color = Slate500
                            )
                        }
                    }
                }
            } else {
                items(allDrafts, key = { it.id }) { draft ->
                    Card(
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        border = BorderStroke(1.dp, Slate200),
                        elevation = CardDefaults.cardElevation(2.dp)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Surface(shape = RoundedCornerShape(8.dp), color = Indigo50, border = BorderStroke(1.dp, Indigo100)) {
                                    Text(draft.draftType, fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Indigo700, modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp))
                                }
                                Row {
                                    Icon(
                                        imageVector = Icons.Default.VolumeUp,
                                        contentDescription = "خواندن فارسی",
                                        tint = if (isSpeaking) Rose500 else Indigo600,
                                        modifier = Modifier
                                            .size(32.dp)
                                            .background(Slate100, RoundedCornerShape(8.dp))
                                            .padding(6.dp)
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    // Click handling for TTS will be via parent
                                }
                            }
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(draft.title, fontWeight = FontWeight.Bold, fontSize = 14.sp, color = Slate900)
                            Spacer(modifier = Modifier.height(4.dp))
                            Text("مخاطب: ${draft.courtHeading}", fontSize = 11.sp, color = Slate500)
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(draft.bodyText.take(200) + "...", fontSize = 12.sp, color = Slate700, lineHeight = 18.sp)
                            Spacer(modifier = Modifier.height(8.dp))
                            Text("مستندات: ${draft.legalArticles}", fontSize = 11.sp, color = Indigo600, fontWeight = FontWeight.Medium)

                            Spacer(modifier = Modifier.height(12.dp))
                            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                Button(
                                    onClick = {
                                        viewModel.speakPersianText("${draft.title}. ${draft.bodyText}")
                                    },
                                    modifier = Modifier.weight(1f).height(40.dp),
                                    shape = RoundedCornerShape(10.dp),
                                    colors = ButtonDefaults.buttonColors(containerColor = LuxuryNavy)
                                ) {
                                    Icon(Icons.Default.VolumeUp, contentDescription = null, modifier = Modifier.size(16.dp))
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text("خوانش فارسی 🔊", fontSize = 12.sp)
                                }
                                Button(
                                    onClick = {
                                        val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                                        val clip = ClipData.newPlainText("لایحه حقوقی", "${draft.courtHeading}\n\n${draft.bodyText}\n\nمستندات: ${draft.legalArticles}")
                                        clipboard.setPrimaryClip(clip)
                                        Toast.makeText(context, "متن لایحه کپی شد ✨", Toast.LENGTH_SHORT).show()
                                    },
                                    modifier = Modifier.weight(1f).height(40.dp),
                                    shape = RoundedCornerShape(10.dp),
                                    colors = ButtonDefaults.buttonColors(containerColor = Slate100, contentColor = Slate800)
                                ) {
                                    Text("کپی متن", fontSize = 12.sp)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

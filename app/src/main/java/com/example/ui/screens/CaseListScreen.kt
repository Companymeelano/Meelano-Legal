package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.data.model.LegalCase
import com.example.ui.LegalViewModel
import com.example.ui.components.AddCaseDialog
import com.example.ui.components.CaseDetailDialog
import com.example.ui.components.DatabaseConfigDialog
import com.example.ui.components.DatabaseStatusBanner
import com.example.ui.theme.*

// تم یکدست سبز تیره لاکچری - هماهنگ با ToolsScreen
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
fun CaseListScreen(
    viewModel: LegalViewModel,
    modifier: Modifier = Modifier
) {
    val cases by viewModel.filteredCases.collectAsStateWithLifecycle()
    val dbInfo by viewModel.dbConnection.collectAsStateWithLifecycle()
    val isSyncing by viewModel.isSyncing.collectAsStateWithLifecycle()
    val searchQuery by viewModel.searchQuery.collectAsStateWithLifecycle()
    val statusFilter by viewModel.statusFilter.collectAsStateWithLifecycle()
    val activeDeadlinesCount by viewModel.activeDeadlinesCount.collectAsStateWithLifecycle()
    val drafts by viewModel.drafts.collectAsStateWithLifecycle()

    var showAddDialog by remember { mutableStateOf(false) }
    var showDbDialog by remember { mutableStateOf(false) }
    var selectedCaseForDetail by remember { mutableStateOf<LegalCase?>(null) }

    val filterOptions = listOf("همه", "در جریان رسیدگی", "در حال تجدیدنظر", "اجرای احکام", "مختومه")

    Box(modifier = modifier.fillMaxSize().background(Bg)) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(bottom = 110.dp),
            verticalArrangement = Arrangement.spacedBy(0.dp)
        ) {
            // هدر لاکچری - هماهنگ با ToolsScreen
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(bottomStart = 24.dp, bottomEnd = 24.dp),
                    colors = CardDefaults.cardColors(containerColor = CardBg),
                    border = BorderStroke(0.5.dp, Border)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(
                                    modifier = Modifier.size(44.dp).clip(RoundedCornerShape(12.dp)).background(GoldBrush),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(Icons.Default.Gavel, contentDescription = null, tint = Bg, modifier = Modifier.size(24.dp))
                                }
                                Spacer(modifier = Modifier.width(10.dp))
                                Column {
                                    Text("پرونده‌های حقوقی", fontSize = 16.sp, fontWeight = FontWeight.ExtraBold, color = Color.White)
                                    Text("سامانه جامع میلانو • ${cases.size} پرونده", fontSize = 10.sp, color = GoldLight)
                                }
                            }
                            Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                                Surface(shape = CircleShape, color = CardElev, border = BorderStroke(0.5.dp, Border)) {
                                    IconButton(onClick = { showDbDialog = true }, modifier = Modifier.size(36.dp)) {
                                        Icon(Icons.Default.Storage, contentDescription = null, tint = Gold, modifier = Modifier.size(18.dp))
                                    }
                                }
                                Surface(shape = RoundedCornerShape(10.dp), color = Gold, modifier = Modifier.clickable { showAddDialog = true }) {
                                    Row(modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp), verticalAlignment = Alignment.CenterVertically) {
                                        Icon(Icons.Default.Add, contentDescription = null, tint = Bg, modifier = Modifier.size(14.dp))
                                        Spacer(modifier = Modifier.width(4.dp))
                                        Text("جدید", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Bg)
                                    }
                                }
                            }
                        }
                        Spacer(modifier = Modifier.height(12.dp))
                        DatabaseStatusBanner(connectionInfo = dbInfo, isSyncing = isSyncing, onConfigureClick = { showDbDialog = true }, onSyncClick = { viewModel.syncDatabase() })
                    }
                }
            }

            // آمار لاکچری - 3 کارت یکدست طلایی
            item {
                Row(
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 12.dp),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    StatGreenCard("پرونده‌ها", cases.size.toString(), Icons.Default.Folder, Modifier.weight(1f))
                    StatGreenCard("مواعد فعال", activeDeadlinesCount.toString(), Icons.Default.Timer, Modifier.weight(1f))
                    StatGreenCard("لوایح", drafts.size.toString(), Icons.Default.Description, Modifier.weight(1f))
                }
            }

            // جستجو + فیلتر - کارت سبز تیره لاکچری
            item {
                Card(
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = CardBg),
                    border = BorderStroke(1.dp, Border)
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        OutlinedTextField(
                            value = searchQuery,
                            onValueChange = { viewModel.setSearchQuery(it) },
                            placeholder = { Text("جستجوی هوشمند پرونده‌ها 🔍", fontSize = 12.sp, color = Color.White.copy(alpha = 0.5f)) },
                            leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, tint = Gold, modifier = Modifier.size(18.dp)) },
                            trailingIcon = {
                                if (searchQuery.isNotBlank()) IconButton(onClick = { viewModel.setSearchQuery("") }) {
                                    Icon(Icons.Default.Clear, contentDescription = null, tint = Color.White.copy(alpha = 0.5f), modifier = Modifier.size(18.dp))
                                }
                            },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(14.dp),
                            singleLine = true,
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
                        Spacer(modifier = Modifier.height(12.dp))
                        LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.fillMaxWidth()) {
                            items(filterOptions) { filter ->
                                val isSelected = statusFilter == filter
                                if (isSelected) {
                                    Box(
                                        modifier = Modifier.clip(RoundedCornerShape(12.dp)).background(GoldBrush).clickable { viewModel.setStatusFilter(filter) }.padding(horizontal = 14.dp, vertical = 8.dp),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(filter, fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Bg)
                                    }
                                } else {
                                    Surface(
                                        shape = RoundedCornerShape(12.dp),
                                        color = CardElev,
                                        border = BorderStroke(0.5.dp, Border),
                                        modifier = Modifier.clickable { viewModel.setStatusFilter(filter) }
                                    ) {
                                        Text(filter, fontSize = 11.sp, color = Color.White.copy(alpha = 0.8f), modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp))
                                    }
                                }
                            }
                        }
                    }
                }
            }

            // عنوان بخش
            item {
                Row(
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp, vertical = 14.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("پرونده‌های حقوقی (${cases.size})", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color.White)
                    Surface(shape = RoundedCornerShape(20.dp), color = CardBg, border = BorderStroke(0.5.dp, BorderStrong)) {
                        Text("✨ لاکچری سبز طلایی", fontSize = 9.sp, fontWeight = FontWeight.Bold, color = Gold, modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp))
                    }
                }
            }

            if (cases.isEmpty()) {
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = CardBg),
                        border = BorderStroke(1.dp, Border)
                    ) {
                        Column(modifier = Modifier.fillMaxWidth().padding(24.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                            Box(modifier = Modifier.size(72.dp).clip(CircleShape).background(CardElev).shadow(0.dp), contentAlignment = Alignment.Center) {
                                Icon(Icons.Default.FolderOpen, contentDescription = null, modifier = Modifier.size(36.dp), tint = Gold)
                            }
                            Spacer(modifier = Modifier.height(16.dp))
                            Text("پرونده‌ای یافت نشد", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color.White)
                            Spacer(modifier = Modifier.height(4.dp))
                            Text("با دکمه طلایی لاکچری زیر اولین پرونده را ثبت کنید", fontSize = 11.sp, color = Color.White.copy(alpha = 0.5f), textAlign = androidx.compose.ui.text.style.TextAlign.Center)
                            Spacer(modifier = Modifier.height(16.dp))
                            Box(modifier = Modifier.clip(RoundedCornerShape(12.dp)).background(GoldBrush).clickable { showAddDialog = true }.padding(horizontal = 20.dp, vertical = 10.dp)) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(Icons.Default.Add, contentDescription = null, tint = Bg, modifier = Modifier.size(16.dp))
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text("ایجاد پرونده جدید", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Bg)
                                }
                            }
                        }
                    }
                }
            } else {
                items(cases, key = { it.id }) { legalCase ->
                    UniformCaseCard(
                        legalCase = legalCase,
                        onClick = { selectedCaseForDetail = legalCase },
                        onDelete = { viewModel.deleteCase(legalCase.id) },
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp)
                    )
                }
            }

            item {
                Spacer(modifier = Modifier.height(24.dp))
                Row(modifier = Modifier.fillMaxWidth().padding(horizontal = 24.dp), horizontalArrangement = Arrangement.Center, verticalAlignment = Alignment.CenterVertically) {
                    Box(modifier = Modifier.width(40.dp).height(1.dp).background(Gold.copy(alpha = 0.3f)))
                    Spacer(modifier = Modifier.width(12.dp))
                    Text("میلانو لگال • پرونده‌های سبز طلایی", fontSize = 9.sp, color = Color.White.copy(alpha = 0.3f))
                    Spacer(modifier = Modifier.width(12.dp))
                    Box(modifier = Modifier.width(40.dp).height(1.dp).background(Gold.copy(alpha = 0.3f)))
                }
            }
        }

        // FAB لاکچری طلایی
        Card(
            modifier = Modifier.align(Alignment.BottomEnd).padding(20.dp).shadow(16.dp, RoundedCornerShape(16.dp)).clickable { showAddDialog = true },
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Gold),
            border = BorderStroke(1.dp, GoldLight)
        ) {
            Row(modifier = Modifier.padding(horizontal = 18.dp, vertical = 12.dp), verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.Add, contentDescription = null, tint = Bg, modifier = Modifier.size(20.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text("پرونده جدید", fontWeight = FontWeight.Bold, fontSize = 13.sp, color = Bg)
            }
        }

        if (showAddDialog) {
            AddCaseDialog(onDismiss = { showAddDialog = false }, onSave = { caseNumber, archiveClass, courtBranch, title, client, role, opponent, status, priority, summary, strategy ->
                viewModel.addCase(caseNumber, archiveClass, courtBranch, title, client, role, opponent, status, priority, summary, strategy)
                showAddDialog = false
            })
        }
        if (showDbDialog) {
            DatabaseConfigDialog(connectionInfo = dbInfo, isSyncing = isSyncing, onDismiss = { showDbDialog = false }, onTestConnection = { host, db, user, pass -> viewModel.testDbConnection(host, db, user, pass) }, onSyncNow = { viewModel.syncDatabase() })
        }
        selectedCaseForDetail?.let { currentCase ->
            CaseDetailDialog(legalCase = currentCase, onDismiss = { selectedCaseForDetail = null }, onDelete = { viewModel.deleteCase(currentCase.id); selectedCaseForDetail = null })
        }
    }
}

@Composable
private fun StatGreenCard(label: String, value: String, icon: ImageVector, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = CardBg),
        border = BorderStroke(1.dp, Border)
    ) {
        Column(modifier = Modifier.padding(12.dp), horizontalAlignment = Alignment.CenterHorizontally) {
            Box(modifier = Modifier.size(36.dp).clip(RoundedCornerShape(10.dp)).background(GoldBrush), contentAlignment = Alignment.Center) {
                Icon(icon, contentDescription = null, tint = Bg, modifier = Modifier.size(18.dp))
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(value, fontSize = 18.sp, fontWeight = FontWeight.ExtraBold, color = Color.White)
            Text(label, fontSize = 10.sp, color = Color.White.copy(alpha = 0.6f))
        }
    }
}

@Composable
private fun UniformCaseCard(legalCase: LegalCase, onClick: () -> Unit, onDelete: () -> Unit, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier.fillMaxWidth().clickable { onClick() },
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = CardBg),
        border = BorderStroke(1.dp, Border)
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.Top) {
                Row(modifier = Modifier.weight(1f), verticalAlignment = Alignment.CenterVertically) {
                    Box(modifier = Modifier.size(32.dp).clip(RoundedCornerShape(8.dp)).background(CardElev), contentAlignment = Alignment.Center) {
                        Icon(Icons.Default.Gavel, contentDescription = null, tint = Gold, modifier = Modifier.size(16.dp))
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(legalCase.caseTitle, fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color.White, maxLines = 1, overflow = TextOverflow.Ellipsis)
                        Text("${legalCase.caseNumber} • ${legalCase.courtBranch}", fontSize = 9.sp, color = Color.White.copy(alpha = 0.5f), maxLines = 1, overflow = TextOverflow.Ellipsis)
                    }
                }
                Spacer(modifier = Modifier.width(8.dp))
                Surface(shape = RoundedCornerShape(8.dp), color = Gold.copy(alpha = 0.15f), border = BorderStroke(0.5.dp, Gold.copy(alpha = 0.3f))) {
                    Text(legalCase.caseStatus, fontSize = 9.sp, fontWeight = FontWeight.Bold, color = Gold, modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp))
                }
            }
            Spacer(modifier = Modifier.height(10.dp))
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                Surface(shape = RoundedCornerShape(8.dp), color = CardElev, border = BorderStroke(0.5.dp, Border)) {
                    Row(modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp), verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Person, contentDescription = null, tint = Gold, modifier = Modifier.size(10.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(legalCase.clientName, fontSize = 9.sp, color = Color.White.copy(alpha = 0.7f))
                    }
                }
                Surface(shape = RoundedCornerShape(8.dp), color = CardElev, border = BorderStroke(0.5.dp, Border)) {
                    Text(legalCase.clientRole, fontSize = 9.sp, color = Color.White.copy(alpha = 0.6f), modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp))
                }
                Surface(shape = RoundedCornerShape(8.dp), color = CardElev, border = BorderStroke(0.5.dp, Border)) {
                    Text(legalCase.priority, fontSize = 9.sp, color = if (legalCase.priority == "بحرانی") Color(0xFFF87171) else GoldLight, modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp))
                }
            }
            if (legalCase.summary.isNotBlank()) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(legalCase.summary, fontSize = 10.sp, color = Color.White.copy(alpha = 0.5f), maxLines = 2, overflow = TextOverflow.Ellipsis, lineHeight = 13.sp)
            }
            Spacer(modifier = Modifier.height(10.dp))
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    Surface(shape = RoundedCornerShape(8.dp), color = CardElev, border = BorderStroke(0.5.dp, Border), modifier = Modifier.clickable { onClick() }) {
                        Row(modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp), verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Visibility, contentDescription = null, tint = Gold, modifier = Modifier.size(12.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("مشاهده", fontSize = 10.sp, color = Gold)
                        }
                    }
                    Surface(shape = RoundedCornerShape(8.dp), color = CardElev, border = BorderStroke(0.5.dp, Border.copy(alpha = 0.5f))) {
                        Icon(Icons.Default.DeleteOutline, contentDescription = null, tint = Color.White.copy(alpha = 0.4f), modifier = Modifier.size(24.dp).padding(6.dp).clickable { onDelete() })
                    }
                }
                Text(text = java.text.SimpleDateFormat("yyyy/MM/dd", java.util.Locale.getDefault()).format(java.util.Date(legalCase.createdAt)), fontSize = 9.sp, color = Color.White.copy(alpha = 0.3f))
            }
        }
    }
}

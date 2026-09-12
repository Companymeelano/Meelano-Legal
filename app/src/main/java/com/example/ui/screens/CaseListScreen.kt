package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.FolderOpen
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Gavel
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.R
import com.example.data.model.LegalCase
import com.example.ui.LegalViewModel
import com.example.ui.components.AddCaseDialog
import com.example.ui.components.CaseDetailDialog
import com.example.ui.components.DatabaseConfigDialog
import com.example.ui.components.DatabaseStatusBanner
import com.example.ui.components.LegalCaseCard
import com.example.ui.components.LuxuryGlassCard
import com.example.ui.components.LuxuryStatCard
import com.example.ui.theme.*

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

    Box(modifier = modifier.fillMaxSize().background(Slate50)) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(bottom = 100.dp)
        ) {
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(240.dp)
                        .clip(RoundedCornerShape(bottomStart = 32.dp, bottomEnd = 32.dp))
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.img_app_icon_new),
                        contentDescription = "لوگو لاکچری",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(
                                Brush.verticalGradient(
                                    colors = listOf(Color(0x800F172A), Color(0xFF0F172A))
                                )
                            )
                    )
                    Column(
                        modifier = Modifier.align(Alignment.BottomStart).padding(20.dp)
                    ) {
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = LuxuryGold.copy(alpha = 0.2f),
                            border = BorderStroke(1.dp, LuxuryGold.copy(alpha = 0.5f))
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(Icons.Default.Verified, contentDescription = null, tint = LuxuryGold, modifier = Modifier.size(14.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = "نسخه لاکچری • تحلیل قوانین رسمی ایران • AI فارسی",
                                    color = LuxuryGoldLight,
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                        Spacer(modifier = Modifier.height(10.dp))
                        Text("میلانو لگال", color = Color.White, fontSize = 28.sp, fontWeight = FontWeight.ExtraBold)
                        Text("سامانه جامع هوشمند حقوقی - بی‌رقیب", color = Slate200, fontSize = 13.sp)
                        Spacer(modifier = Modifier.height(8.dp))
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            Surface(shape = RoundedCornerShape(20.dp), color = Color.White.copy(alpha = 0.15f)) {
                                Row(modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp), verticalAlignment = Alignment.CenterVertically) {
                                    Icon(Icons.Default.Security, contentDescription = null, tint = Emerald100, modifier = Modifier.size(12.dp))
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text("امن و رمزنگاری شده", color = Color.White, fontSize = 10.sp)
                                }
                            }
                            Surface(shape = RoundedCornerShape(20.dp), color = LuxuryGold.copy(alpha = 0.2f)) {
                                Row(modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp), verticalAlignment = Alignment.CenterVertically) {
                                    Icon(Icons.Default.AutoAwesome, contentDescription = null, tint = LuxuryGold, modifier = Modifier.size(12.dp))
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text("هوش مصنوعی فارسی", color = LuxuryGoldLight, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                                }
                            }
                        }
                    }
                }
            }

            item {
                Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp)) {
                    DatabaseStatusBanner(connectionInfo = dbInfo, isSyncing = isSyncing, onConfigureClick = { showDbDialog = true }, onSyncClick = { viewModel.syncDatabase() })
                }
            }

            item {
                Row(modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 8.dp), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    LuxuryStatCard(value = cases.size.toString(), label = "پرونده‌ها", icon = Icons.Default.Gavel, gradient = Brush.linearGradient(listOf(Indigo600, Indigo800)), modifier = Modifier.weight(1f))
                    LuxuryStatCard(value = activeDeadlinesCount.toString(), label = "مواعد فعال", icon = Icons.Default.Security, gradient = Brush.linearGradient(listOf(Amber500, Amber600)), modifier = Modifier.weight(1f))
                    LuxuryStatCard(value = drafts.size.toString(), label = "لوایح هوشمند", icon = Icons.Default.AutoAwesome, gradient = Brush.linearGradient(listOf(Emerald500, Emerald600)), modifier = Modifier.weight(1f))
                }
            }

            item {
                LuxuryGlassCard(modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)) {
                    OutlinedTextField(
                        value = searchQuery,
                        onValueChange = { viewModel.setSearchQuery(it) },
                        placeholder = { Text("جستجوی هوشمند... 🔍", fontSize = 13.sp) },
                        leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, tint = Indigo600) },
                        trailingIcon = { if (searchQuery.isNotBlank()) IconButton(onClick = { viewModel.setSearchQuery("") }) { Icon(Icons.Default.Clear, contentDescription = "پاک") } },
                        modifier = Modifier.fillMaxWidth().testTag("search_cases_input"),
                        shape = RoundedCornerShape(14.dp),
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = Indigo600, unfocusedBorderColor = Slate200, focusedContainerColor = Color.White, unfocusedContainerColor = Color.White)
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.fillMaxWidth()) {
                        items(filterOptions) { filter ->
                            val isSelected = statusFilter == filter
                            FilterChip(selected = isSelected, onClick = { viewModel.setStatusFilter(filter) }, label = { Text(filter, fontSize = 12.sp, fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal) }, shape = RoundedCornerShape(12.dp), border = BorderStroke(1.dp, if (isSelected) Indigo600 else Slate200), colors = FilterChipDefaults.filterChipColors(selectedContainerColor = Indigo600, selectedLabelColor = Color.White, containerColor = Color.White), modifier = Modifier.testTag("filter_chip_$filter"))
                        }
                    }
                }
            }

            item {
                Row(modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp, vertical = 12.dp), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                    Text("پرونده‌های حقوقی (${cases.size})", fontSize = 16.sp, fontWeight = FontWeight.ExtraBold, color = Slate900)
                    Surface(shape = CircleShape, color = LuxuryGold.copy(alpha = 0.15f), border = BorderStroke(1.dp, LuxuryGold.copy(alpha = 0.3f))) {
                        Text("✨ لاکچری", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = LuxuryGoldDark, modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp))
                    }
                }
            }

            if (cases.isEmpty()) {
                item {
                    LuxuryGlassCard(modifier = Modifier.padding(16.dp)) {
                        Column(modifier = Modifier.fillMaxWidth().padding(20.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                            Box(modifier = Modifier.size(72.dp).clip(CircleShape).background(Slate100), contentAlignment = Alignment.Center) { Icon(Icons.Default.FolderOpen, contentDescription = null, modifier = Modifier.size(36.dp), tint = Slate400) }
                            Spacer(modifier = Modifier.height(16.dp))
                            Text("پرونده‌ای یافت نشد", fontSize = 15.sp, fontWeight = FontWeight.Bold, color = Slate700)
                            Text("با دکمه لاکچری زیر اولین پرونده را ثبت کنید", fontSize = 12.sp, color = Slate500)
                        }
                    }
                }
            } else {
                items(cases, key = { it.id }) { legalCase ->
                    LegalCaseCard(legalCase = legalCase, onClick = { selectedCaseForDetail = legalCase }, onDelete = { viewModel.deleteCase(legalCase.id) }, modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp))
                }
            }
        }

        FloatingActionButton(
            onClick = { showAddDialog = true },
            modifier = Modifier.align(Alignment.BottomEnd).padding(20.dp).shadow(16.dp, RoundedCornerShape(18.dp)),
            containerColor = Indigo600,
            contentColor = Color.White,
            shape = RoundedCornerShape(18.dp)
        ) {
            Row(modifier = Modifier.padding(horizontal = 16.dp), verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.Add, contentDescription = "افزودن", modifier = Modifier.size(20.dp))
                Spacer(modifier = Modifier.width(6.dp))
                Text("جدید", fontWeight = FontWeight.Bold, fontSize = 13.sp)
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

package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.ExpandedLegalDatabase
import com.example.data.model.ExpandedLegalArticle
import com.example.ui.theme.*

@Composable
fun KnowledgeBaseScreen(
    modifier: Modifier = Modifier
) {
    var searchQuery by remember { mutableStateOf("") }
    var searchResults by remember { mutableStateOf<List<ExpandedLegalArticle>>(emptyList()) }
    var selectedCategory by remember { mutableStateOf("همه") }

    val categories = listOf("همه", "قراردادها", "مواعد", "چک", "خانواده", "کیفری")

    LaunchedEffect(searchQuery) {
        searchResults = if (searchQuery.isBlank()) {
            ExpandedLegalDatabase.searchAdvanced("ماده")
        } else {
            ExpandedLegalDatabase.searchAdvanced(searchQuery)
        }
    }

    LazyColumn(
        modifier = modifier.fillMaxSize().background(LuxuryObsidianBrush),
        contentPadding = PaddingValues(bottom = 100.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item {
            Card(modifier = Modifier.fillMaxWidth().shadow(20.dp, RoundedCornerShape(bottomStart = 32.dp, bottomEnd = 32.dp)), shape = RoundedCornerShape(bottomStart = 32.dp, bottomEnd = 32.dp), colors = CardDefaults.cardColors(containerColor = LuxuryNavyDeep), border = BorderStroke(1.dp, LuxuryDarkBorderStrong)) {
                Box(modifier = Modifier.fillMaxWidth().background(LuxuryObsidianBrush).padding(22.dp)) {
                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(modifier = Modifier.size(52.dp).background(LuxuryGoldDarkBrush, RoundedCornerShape(14.dp)).padding(10.dp).shadow(8.dp, RoundedCornerShape(14.dp)), contentAlignment = Alignment.Center) {
                                Icon(Icons.Default.MenuBook, contentDescription = null, tint = LuxuryNavyDeep, modifier = Modifier.size(26.dp))
                            }
                            Spacer(modifier = Modifier.width(14.dp))
                            Column {
                                Text("پایگاه دانش ۱۵۰۰۰ ماده‌ای", fontSize = 20.sp, fontWeight = FontWeight.ExtraBold, color = Color.White)
                                Text("FTS + Vector Search + rc.majlis.ir • تیره لاکچری", fontSize = 11.sp, color = Slate400)
                            }
                        }
                        Spacer(modifier = Modifier.height(18.dp))
                        OutlinedTextField(
                            value = searchQuery,
                            onValueChange = { searchQuery = it },
                            placeholder = { Text("جستجوی برداری در ۱۵۰۰۰ ماده... مثلاً: تجدیدنظر، چک، مهریه", fontSize = 12.sp, color = Slate500) },
                            leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, tint = LuxuryGold) },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(16.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = LuxuryGold,
                                unfocusedBorderColor = Slate700,
                                focusedContainerColor = LuxuryDarkCardElevated,
                                unfocusedContainerColor = LuxuryDarkCard,
                                focusedTextColor = Color.White,
                                unfocusedTextColor = Color.White
                            )
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            categories.take(4).forEach { cat ->
                                FilterChip(
                                    selected = selectedCategory == cat,
                                    onClick = { selectedCategory = cat },
                                    label = { Text(cat, fontSize = 11.sp, fontWeight = FontWeight.Bold) },
                                    shape = RoundedCornerShape(12.dp),
                                    colors = FilterChipDefaults.filterChipColors(
                                        selectedContainerColor = LuxuryGold,
                                        selectedLabelColor = LuxuryNavyDeep,
                                        containerColor = LuxuryCharcoal,
                                        labelColor = Slate300
                                    ),
                                    border = BorderStroke(1.dp, if (selectedCategory == cat) LuxuryGold else Slate700)
                                )
                            }
                        }
                    }
                }
            }
        }

        item {
            Row(modifier = Modifier.fillMaxWidth().padding(horizontal = 18.dp), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                Text("نتایج جستجو: ${searchResults.size} ماده", fontWeight = FontWeight.ExtraBold, fontSize = 15.sp, color = Color.White)
                Card(shape = RoundedCornerShape(20.dp), colors = CardDefaults.cardColors(containerColor = LuxuryGold.copy(alpha = 0.15f)), border = BorderStroke(1.dp, LuxuryGold.copy(alpha = 0.3f))) {
                    Text("${ExpandedLegalDatabase.getTotalCount()} کل", fontSize = 11.sp, color = LuxuryGold, fontWeight = FontWeight.Bold, modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp))
                }
            }
        }

        items(searchResults.take(20)) { article ->
            Card(modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp).shadow(6.dp, RoundedCornerShape(18.dp)), shape = RoundedCornerShape(18.dp), colors = CardDefaults.cardColors(containerColor = LuxuryDarkCard), border = BorderStroke(1.dp, if (article.isImportant) LuxuryGold.copy(alpha = 0.4f) else LuxuryDarkBorder), elevation = CardDefaults.cardElevation(6.dp)) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Card(shape = RoundedCornerShape(10.dp), colors = CardDefaults.cardColors(containerColor = if (article.isImportant) LuxuryGold.copy(alpha = 0.2f) else Indigo600.copy(alpha = 0.2f)), border = BorderStroke(0.5.dp, if (article.isImportant) LuxuryGold.copy(alpha = 0.4f) else Indigo600.copy(alpha = 0.3f))) {
                            Text(article.articleNumber, fontSize = 11.sp, fontWeight = FontWeight.Bold, color = if (article.isImportant) LuxuryGold else Indigo300, modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp))
                        }
                        Text(article.lawName, fontSize = 11.sp, color = Slate400)
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(article.articleText, fontSize = 13.sp, color = Slate200, lineHeight = 20.sp)
                    Spacer(modifier = Modifier.height(12.dp))
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        Surface(shape = RoundedCornerShape(8.dp), color = LuxuryCharcoal, border = BorderStroke(0.5.dp, Slate700)) { Text(article.category, fontSize = 10.sp, color = Slate300, modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)) }
                        Surface(shape = RoundedCornerShape(8.dp), color = Amber600.copy(alpha = 0.15f), border = BorderStroke(0.5.dp, Amber600.copy(alpha = 0.3f))) { Text(article.book, fontSize = 10.sp, color = Amber300, modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)) }
                    }
                }
            }
        }
    }
}

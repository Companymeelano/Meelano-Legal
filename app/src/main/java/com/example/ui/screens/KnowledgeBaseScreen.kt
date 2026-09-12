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
        modifier = modifier.fillMaxSize().background(Slate50),
        contentPadding = PaddingValues(bottom = 100.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        item {
            Card(modifier = Modifier.fillMaxWidth().shadow(12.dp, RoundedCornerShape(bottomStart = 28.dp, bottomEnd = 28.dp)), shape = RoundedCornerShape(bottomStart = 28.dp, bottomEnd = 28.dp), colors = CardDefaults.cardColors(containerColor = LuxuryNavy)) {
                Box(modifier = Modifier.fillMaxWidth().background(LuxuryNavyBrush).padding(20.dp)) {
                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(modifier = Modifier.size(44.dp).background(LuxuryGoldBrush, RoundedCornerShape(12.dp)).padding(8.dp), contentAlignment = Alignment.Center) {
                                Icon(Icons.Default.MenuBook, contentDescription = null, tint = LuxuryNavy, modifier = Modifier.size(22.dp))
                            }
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Text("پایگاه دانش ۱۵۰۰۰ ماده‌ای", fontSize = 18.sp, fontWeight = FontWeight.ExtraBold, color = Color.White)
                                Text("FTS + Vector Search + rc.majlis.ir", fontSize = 11.sp, color = Slate300)
                            }
                        }
                        Spacer(modifier = Modifier.height(16.dp))
                        OutlinedTextField(
                            value = searchQuery,
                            onValueChange = { searchQuery = it },
                            placeholder = { Text("جستجوی برداری در ۱۵۰۰۰ ماده... مثلاً: تجدیدنظر، چک، مهریه", fontSize = 12.sp) },
                            leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, tint = LuxuryGold) },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(14.dp),
                            colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = LuxuryGold, unfocusedBorderColor = Color.White.copy(alpha = 0.2f), focusedContainerColor = Color.White, unfocusedContainerColor = Color.White)
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                        Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                            categories.take(4).forEach { cat ->
                                FilterChip(selected = selectedCategory == cat, onClick = { selectedCategory = cat }, label = { Text(cat, fontSize = 11.sp) }, shape = RoundedCornerShape(10.dp), colors = FilterChipDefaults.filterChipColors(selectedContainerColor = LuxuryGold, selectedLabelColor = LuxuryNavy, containerColor = Color.White.copy(alpha = 0.1f), labelColor = Color.White))
                            }
                        }
                    }
                }
            }
        }

        item {
            Row(modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                Text("نتایج جستجو: ${searchResults.size} ماده", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                Surface(shape = RoundedCornerShape(20.dp), color = LuxuryGold.copy(alpha = 0.15f), border = BorderStroke(1.dp, LuxuryGold.copy(alpha = 0.3f))) {
                    Text("${ExpandedLegalDatabase.getTotalCount()} کل", fontSize = 10.sp, color = LuxuryGoldDark, fontWeight = FontWeight.Bold, modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp))
                }
            }
        }

        items(searchResults.take(20)) { article ->
            Card(modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp), shape = RoundedCornerShape(16.dp), colors = CardDefaults.cardColors(containerColor = Color.White), border = BorderStroke(1.dp, if (article.isImportant) LuxuryGold.copy(alpha = 0.4f) else Slate200), elevation = CardDefaults.cardElevation(2.dp)) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Surface(shape = RoundedCornerShape(8.dp), color = if (article.isImportant) LuxuryGold else Indigo50, border = BorderStroke(1.dp, if (article.isImportant) LuxuryGoldDark else Indigo100)) {
                            Text(article.articleNumber, fontSize = 11.sp, fontWeight = FontWeight.Bold, color = if (article.isImportant) LuxuryNavy else Indigo700, modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp))
                        }
                        Text(article.lawName, fontSize = 11.sp, color = Slate500)
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(article.articleText, fontSize = 12.sp, color = Slate800, lineHeight = 18.sp)
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        Surface(shape = RoundedCornerShape(6.dp), color = Slate100) { Text(article.category, fontSize = 10.sp, color = Slate600, modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp)) }
                        Surface(shape = RoundedCornerShape(6.dp), color = Amber50) { Text(article.book, fontSize = 10.sp, color = Amber600, modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp)) }
                    }
                }
            }
        }
    }
}

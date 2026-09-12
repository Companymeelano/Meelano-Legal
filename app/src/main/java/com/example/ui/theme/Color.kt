package com.example.ui.theme

import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

// ===== Meelano Legal Luxury Edition - 2026 =====
// Inspired by Iranian legal prestige: Gold + Royal Navy + Marble

// Primary - Royal Indigo & Deep Navy (Authority)
val Indigo600 = Color(0xFF4F46E5)
val Indigo700 = Color(0xFF4338CA)
val Indigo800 = Color(0xFF3730A3)
val Indigo900 = Color(0xFF1E1B4B) // Deep royal
val Indigo50 = Color(0xFFEEF2FF)
val Indigo100 = Color(0xFFE0E7FF)
val Indigo200 = Color(0xFFC7D2FE)

// Slate Neutrals - Marble & Graphite
val Slate900 = Color(0xFF0F172A)
val Slate800 = Color(0xFF1E293B)
val Slate700 = Color(0xFF334155)
val Slate600 = Color(0xFF475569)
val Slate500 = Color(0xFF64748B)
val Slate400 = Color(0xFF94A3B8)
val Slate300 = Color(0xFFCBD5E1)
val Slate200 = Color(0xFFE2E8F0)
val Slate100 = Color(0xFFF1F5F9)
val Slate50 = Color(0xFFF8FAFC)

// Semantic Accents
val Emerald600 = Color(0xFF059669)
val Emerald500 = Color(0xFF10B981)
val Emerald300 = Color(0xFF6EE7B7)
val Emerald50 = Color(0xFFECFDF5)
val Emerald100 = Color(0xFFD1FAE5)

val Amber600 = Color(0xFFD97706)
val Amber500 = Color(0xFFF59E0B)
val Amber300 = Color(0xFFFCD34D)
val Amber50 = Color(0xFFFFFBEB)
val Amber100 = Color(0xFFFEF3C7)

val Rose600 = Color(0xFFE11D48)
val Rose500 = Color(0xFFF43F5E)
val Rose300 = Color(0xFFFB7185)
val Rose50 = Color(0xFFFFF1F2)
val Rose100 = Color(0xFFFFE4E6)

val Sky600 = Color(0xFF0284C7)
val Sky500 = Color(0xFF0EA5E9)
val Sky300 = Color(0xFF7DD3FC)
val Sky50 = Color(0xFFF0F9FF)
val Sky100 = Color(0xFFE0F2FE)

// ===== LUXURY EXTENSION - DARK EDITION 2026 =====
// Gold Luxury - Iranian Prestige - Enhanced for Dark
val LuxuryGold = Color(0xFFFFD700)
val LuxuryGoldLight = Color(0xFFFFE55C)
val LuxuryGoldDark = Color(0xFFB8860B)
val LuxuryGoldDeep = Color(0xFF8B6914)
val LuxuryGoldGradientStart = Color(0xFFFFD700)
val LuxuryGoldGradientEnd = Color(0xFFFFA500)
val LuxuryGoldShimmer = Color(0xFFFFF8DC)
val LuxuryGoldGlow = Color(0x40FFD700)

// Royal Navy Luxury - Deep Dark
val LuxuryNavy = Color(0xFF0A0E27)
val LuxuryNavyLight = Color(0xFF1E2A5A)
val LuxuryNavyDeep = Color(0xFF050817)
val LuxuryNavyGradientStart = Color(0xFF0F172A)
val LuxuryNavyGradientEnd = Color(0xFF1E1B4B)
val LuxuryObsidian = Color(0xFF0B0F1A)
val LuxuryCharcoal = Color(0xFF151A2A)
val LuxuryGraphite = Color(0xFF1E2538)

// Emerald Luxury
val LuxuryEmerald = Color(0xFF00D4AA)
val LuxuryEmeraldDark = Color(0xFF059669)
val LuxuryEmeraldGlow = Color(0x4000D4AA)

// Glass & Luxury Surface - Dark Optimized
val GlassWhite = Color(0x80FFFFFF)
val GlassDark = Color(0x801E293B)
val GlassGold = Color(0x20FFD700)
val LuxuryCardShadow = Color(0x80000000)
val LuxuryGlow = Color(0x334F46E5)
val LuxuryDarkCard = Color(0xFF1A1F33)
val LuxuryDarkCardElevated = Color(0xFF242B45)
val LuxuryDarkBorder = Color(0x30FFD700)
val LuxuryDarkBorderStrong = Color(0x60FFD700)

// Gradient Brushes - Dark Luxury Edition
val LuxuryGoldBrush = Brush.linearGradient(
    colors = listOf(LuxuryGoldGradientStart, LuxuryGoldGradientEnd)
)
val LuxuryGoldDarkBrush = Brush.linearGradient(
    colors = listOf(LuxuryGoldDeep, LuxuryGoldDark, LuxuryGold)
)
val LuxuryNavyBrush = Brush.linearGradient(
    colors = listOf(LuxuryNavyGradientStart, LuxuryNavyGradientEnd)
)
val LuxuryObsidianBrush = Brush.linearGradient(
    colors = listOf(LuxuryObsidian, LuxuryCharcoal, LuxuryNavy)
)
val LuxuryPremiumBrush = Brush.linearGradient(
    colors = listOf(Color(0xFF4F46E5), Color(0xFF7C3AED), Color(0xFFFFD700))
)
val LuxuryDarkPremiumBrush = Brush.linearGradient(
    colors = listOf(LuxuryNavyDeep, Color(0xFF2D1B69), LuxuryGoldDeep)
)
val LuxuryGlassBrush = Brush.verticalGradient(
    colors = listOf(Color(0xCCFFFFFF), Color(0x99F8FAFC))
)
val LuxuryDarkGlassBrush = Brush.verticalGradient(
    colors = listOf(Color(0xCC1E293B), Color(0x990F172A))
)
val LuxuryGoldShimmerBrush = Brush.linearGradient(
    colors = listOf(LuxuryGold, Color(0xFFFFF8DC), LuxuryGold, LuxuryGoldDark)
)
val LuxuryCardGoldBorderBrush = Brush.linearGradient(
    colors = listOf(LuxuryGold.copy(alpha = 0.8f), LuxuryGoldLight.copy(alpha = 0.3f), LuxuryGoldDark.copy(alpha = 0.6f))
)

// Backward compatible aliases
val NavyDark = LuxuryNavy
val NavySurface = Slate800
val NavyPrimary = Indigo600
val NavyAccent = Indigo700

val GoldPrimary = LuxuryGold
val GoldLight = Amber100
val GoldDark = LuxuryGoldDark

val ParchmentBg = Slate50
val ParchmentSurface = Color.White
val ParchmentSurfaceVariant = Slate100

val LegalEmerald = Emerald600
val LegalRuby = Rose600
val LegalAmber = Amber600
val LegalCyan = Sky600

val TextPrimaryDark = Slate900
val TextSecondaryDark = Slate500
val TextPrimaryLight = Color(0xFFF8FAFC)
val TextSecondaryLight = Slate400
val BorderLight = Slate200
val BorderDark = Slate700

// Luxury Button Colors
val LuxuryButtonPrimary = Color(0xFF4F46E5)
val LuxuryButtonSecondary = Color(0xFF0F172A)
val LuxuryButtonGold = Color(0xFFFFD700)
val LuxuryButtonSuccess = Color(0xFF10B981)

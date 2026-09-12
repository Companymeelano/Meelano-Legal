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
val Emerald50 = Color(0xFFECFDF5)
val Emerald100 = Color(0xFFD1FAE5)

val Amber600 = Color(0xFFD97706)
val Amber500 = Color(0xFFF59E0B)
val Amber50 = Color(0xFFFFFBEB)
val Amber100 = Color(0xFFFEF3C7)

val Rose600 = Color(0xFFE11D48)
val Rose500 = Color(0xFFF43F5E)
val Rose50 = Color(0xFFFFF1F2)
val Rose100 = Color(0xFFFFE4E6)

val Sky600 = Color(0xFF0284C7)
val Sky500 = Color(0xFF0EA5E9)
val Sky50 = Color(0xFFF0F9FF)
val Sky100 = Color(0xFFE0F2FE)

// ===== LUXURY EXTENSION =====
// Gold Luxury - Iranian Prestige
val LuxuryGold = Color(0xFFFFD700)
val LuxuryGoldLight = Color(0xFFFFE55C)
val LuxuryGoldDark = Color(0xFFB8860B)
val LuxuryGoldGradientStart = Color(0xFFFFD700)
val LuxuryGoldGradientEnd = Color(0xFFFFA500)
val LuxuryGoldShimmer = Color(0xFFFFF8DC)

// Royal Navy Luxury
val LuxuryNavy = Color(0xFF0A0E27)
val LuxuryNavyLight = Color(0xFF1E2A5A)
val LuxuryNavyGradientStart = Color(0xFF0F172A)
val LuxuryNavyGradientEnd = Color(0xFF1E1B4B)

// Emerald Luxury
val LuxuryEmerald = Color(0xFF00D4AA)
val LuxuryEmeraldDark = Color(0xFF059669)

// Glass & Luxury Surface
val GlassWhite = Color(0x80FFFFFF)
val GlassDark = Color(0x801E293B)
val LuxuryCardShadow = Color(0x1A000000)
val LuxuryGlow = Color(0x334F46E5)

// Gradient Brushes - Ready to use
val LuxuryGoldBrush = Brush.linearGradient(
    colors = listOf(LuxuryGoldGradientStart, LuxuryGoldGradientEnd)
)
val LuxuryNavyBrush = Brush.linearGradient(
    colors = listOf(LuxuryNavyGradientStart, LuxuryNavyGradientEnd)
)
val LuxuryPremiumBrush = Brush.linearGradient(
    colors = listOf(Color(0xFF4F46E5), Color(0xFF7C3AED), Color(0xFFFFD700))
)
val LuxuryGlassBrush = Brush.verticalGradient(
    colors = listOf(Color(0xCCFFFFFF), Color(0x99F8FAFC))
)
val LuxuryDarkGlassBrush = Brush.verticalGradient(
    colors = listOf(Color(0xCC1E293B), Color(0x990F172A))
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

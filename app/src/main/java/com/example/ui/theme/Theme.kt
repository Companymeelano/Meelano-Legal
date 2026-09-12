package com.example.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorScheme =
  darkColorScheme(
    primary = LuxuryGreenGold,
    onPrimary = LuxuryGreenDeep,
    primaryContainer = LuxuryGreenMedium,
    onPrimaryContainer = Color(0xFFF0D878),
    secondary = LuxuryGreenGold,
    onSecondary = LuxuryGreenDeep,
    secondaryContainer = LuxuryGreenLight,
    onSecondaryContainer = Emerald100,
    tertiary = LuxuryGreenGold,
    onTertiary = LuxuryGreenDeep,
    tertiaryContainer = Color(0xFF2A4A32),
    onTertiaryContainer = Amber100,
    background = LuxuryGreenDeep,
    onBackground = Color(0xFFF8FAFC),
    surface = LuxuryGreenDark,
    onSurface = Color(0xFFF8FAFC),
    surfaceVariant = LuxuryGreenMedium,
    onSurfaceVariant = Color(0xFF8BA898),
    outline = LuxuryGreenBorderStrong,
    outlineVariant = LuxuryGreenBorder,
    error = Rose500,
    onError = Color.White,
    errorContainer = Color(0xFF4A1A1A),
    onErrorContainer = Rose100
  )

private val LightColorScheme =
  lightColorScheme(
    primary = Indigo600,
    onPrimary = Color.White,
    primaryContainer = Indigo50,
    onPrimaryContainer = Indigo800,
    secondary = Emerald600,
    onSecondary = Color.White,
    secondaryContainer = Emerald50,
    onSecondaryContainer = Color(0xFF065F46),
    tertiary = LuxuryGoldDark,
    onTertiary = Color.White,
    tertiaryContainer = Amber50,
    onTertiaryContainer = Color(0xFF92400E),
    background = Slate50,
    onBackground = Slate900,
    surface = Color.White,
    onSurface = Slate900,
    surfaceVariant = Slate100,
    onSurfaceVariant = Slate600,
    outline = Slate200,
    outlineVariant = Color(0xFFE2E8F0),
    error = Rose600,
    onError = Color.White,
    errorContainer = Rose50,
    onErrorContainer = Color(0xFF9F1239)
  )

@Composable
fun MyApplicationTheme(
  darkTheme: Boolean = true, // همیشه تیره لاکچری - درخواست کاربر
  dynamicColor: Boolean = false,
  content: @Composable () -> Unit,
) {
  val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme
  MaterialTheme(colorScheme = colorScheme, typography = Typography, content = content)
}

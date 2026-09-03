package com.example.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorScheme =
  darkColorScheme(
    primary = Color(0xFF818CF8), // Indigo 400
    onPrimary = Slate900,
    primaryContainer = Color(0xFF312E81), // Indigo 900
    onPrimaryContainer = Color(0xFFE0E7FF), // Indigo 100
    secondary = Emerald500,
    onSecondary = Slate900,
    secondaryContainer = Color(0xFF064E3B),
    onSecondaryContainer = Emerald100,
    tertiary = Amber500,
    onTertiary = Slate900,
    tertiaryContainer = Color(0xFF78350F),
    onTertiaryContainer = Amber100,
    background = Slate900,
    onBackground = Color(0xFFF8FAFC),
    surface = Slate800,
    onSurface = Color(0xFFF8FAFC),
    surfaceVariant = Color(0xFF1E293B),
    onSurfaceVariant = Slate400,
    outline = Slate700,
    outlineVariant = Color(0xFF334155),
    error = Rose500,
    onError = Color.White,
    errorContainer = Color(0xFF881337),
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
    tertiary = Amber600,
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
  darkTheme: Boolean = isSystemInDarkTheme(),
  dynamicColor: Boolean = false, // Keep consistent Iranian Legal identity
  content: @Composable () -> Unit,
) {
  val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme
  MaterialTheme(colorScheme = colorScheme, typography = Typography, content = content)
}


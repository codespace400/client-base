package com.example.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val LightColorScheme = lightColorScheme(
  primary = IndigoPrimary,
  onPrimary = Color.White,
  primaryContainer = IndigoContainer,
  onPrimaryContainer = OnIndigoContainer,
  secondary = TealAccent,
  onSecondary = Color.White,
  secondaryContainer = TealContainer,
  onSecondaryContainer = OnTealContainer,
  tertiary = IndigoLight,
  onTertiary = Color.White,
  background = Slate50,
  onBackground = Slate900,
  surface = Color.White,
  onSurface = Slate900,
  surfaceVariant = Slate100,
  onSurfaceVariant = Slate600,
  outline = Slate200,
  outlineVariant = Slate300,
  error = ErrorRed,
  onError = OnError,
  errorContainer = ErrorContainer
)

private val DarkColorScheme = darkColorScheme(
  primary = IndigoLight,
  onPrimary = Color.White,
  primaryContainer = Color(0xFF312E81),
  onPrimaryContainer = IndigoContainer,
  secondary = TealAccentLight,
  onSecondary = Color.Black,
  secondaryContainer = Color(0xFF134E4A),
  onSecondaryContainer = TealContainer,
  tertiary = IndigoPrimary,
  onTertiary = Color.White,
  background = Color(0xFF0F172A),
  onBackground = Color(0xFFF8FAFC),
  surface = Color(0xFF1E293B),
  onSurface = Color(0xFFF8FAFC),
  surfaceVariant = Color(0xFF334155),
  onSurfaceVariant = Color(0xFFCBD5E1),
  outline = Color(0xFF475569),
  outlineVariant = Color(0xFF334155),
  error = Color(0xFFF87171),
  onError = Color.Black
)

@Composable
fun ClientBaseTheme(
  darkTheme: Boolean = isSystemInDarkTheme(),
  content: @Composable () -> Unit,
) {
  val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

  MaterialTheme(
    colorScheme = colorScheme,
    typography = Typography,
    content = content
  )
}

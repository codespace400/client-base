package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlin.math.abs

@Composable
fun CustomerAvatar(
  name: String,
  initials: String,
  size: Dp = 40.dp,
  modifier: Modifier = Modifier
) {
  val palettes = listOf(
    Pair(Color(0xFF4F46E5), Color(0xFF6366F1)), // Indigo
    Pair(Color(0xFF0D9488), Color(0xFF14B8A6)), // Teal
    Pair(Color(0xFF0284C7), Color(0xFF38BDF8)), // Sky
    Pair(Color(0xFF7C3AED), Color(0xFFA78BFA)), // Violet
    Pair(Color(0xFFD97706), Color(0xFFFBBF24)), // Amber
    Pair(Color(0xFFE11D48), Color(0xFFFB7185))  // Rose
  )

  val hash = abs(name.hashCode())
  val gradientColors = palettes[hash % palettes.size]

  Box(
    contentAlignment = Alignment.Center,
    modifier = modifier
      .size(size)
      .clip(CircleShape)
      .background(Brush.linearGradient(listOf(gradientColors.first, gradientColors.second)))
  ) {
    Text(
      text = initials,
      color = Color.White,
      fontSize = (size.value * 0.4f).sp,
      fontWeight = FontWeight.Bold
    )
  }
}

package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.Slate200
import com.example.ui.theme.Slate700
import com.example.ui.theme.StatusActiveBg
import com.example.ui.theme.StatusActiveText
import com.example.ui.theme.StatusInactiveBg
import com.example.ui.theme.StatusInactiveText
import com.example.ui.theme.StatusLeadBg
import com.example.ui.theme.StatusLeadText
import com.example.ui.theme.StatusProspectBg
import com.example.ui.theme.StatusProspectText
import com.example.ui.theme.StatusVipBg
import com.example.ui.theme.StatusVipText
import com.example.ui.theme.TealAccent
import com.example.ui.theme.TealContainer

@Composable
fun StatusBadge(
  status: String,
  modifier: Modifier = Modifier
) {
  val (bgColor, textColor, dotColor) = when (status.lowercase()) {
    "active" -> Triple(StatusActiveBg, StatusActiveText, TealAccent)
    "lead" -> Triple(StatusLeadBg, StatusLeadText, Color(0xFF4F46E5))
    "prospect" -> Triple(StatusProspectBg, StatusProspectText, Color(0xFFF59E0B))
    "vip" -> Triple(StatusVipBg, StatusVipText, Color(0xFF8B5CF6))
    "inactive" -> Triple(StatusInactiveBg, StatusInactiveText, Color(0xFF94A3B8))
    else -> Triple(StatusActiveBg, StatusActiveText, TealAccent)
  }

  Row(
    verticalAlignment = Alignment.CenterVertically,
    modifier = modifier
      .clip(RoundedCornerShape(12.dp))
      .background(bgColor)
      .padding(horizontal = 8.dp, vertical = 3.dp)
  ) {
    Box(
      modifier = Modifier
        .size(6.dp)
        .clip(CircleShape)
        .background(dotColor)
    )
    Text(
      text = status,
      color = textColor,
      fontSize = 11.sp,
      fontWeight = FontWeight.SemiBold,
      modifier = Modifier.padding(start = 5.dp)
    )
  }
}

@Composable
fun TagChip(
  tag: String,
  isSelected: Boolean = false,
  onClick: (() -> Unit)? = null,
  modifier: Modifier = Modifier
) {
  val isPositiveTag = tag.equals("Enterprise", ignoreCase = true) ||
    tag.equals("VIP", ignoreCase = true) ||
    tag.equals("High-Value", ignoreCase = true) ||
    tag.equals("Partner", ignoreCase = true)

  val bg = when {
    isSelected -> MaterialTheme.colorScheme.primary
    isPositiveTag -> TealContainer
    else -> MaterialTheme.colorScheme.surfaceVariant
  }

  val textColor = when {
    isSelected -> Color.White
    isPositiveTag -> StatusActiveText
    else -> Slate700
  }

  val clickableModifier = if (onClick != null) {
    modifier.clickable(onClick = onClick)
  } else {
    modifier
  }

  Box(
    modifier = clickableModifier
      .clip(RoundedCornerShape(6.dp))
      .background(bg)
      .border(
        width = if (isSelected) 0.dp else 1.dp,
        color = if (isSelected) Color.Transparent else Slate200,
        shape = RoundedCornerShape(6.dp)
      )
      .padding(horizontal = 8.dp, vertical = 3.dp)
  ) {
    Text(
      text = tag,
      color = textColor,
      fontSize = 11.sp,
      fontWeight = FontWeight.Medium
    )
  }
}

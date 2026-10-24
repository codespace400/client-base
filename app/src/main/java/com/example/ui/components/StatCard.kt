package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.Slate200
import com.example.ui.theme.Slate500
import com.example.ui.theme.Slate900

@Composable
fun StatCard(
  title: String,
  value: String,
  subtitle: String,
  icon: ImageVector,
  iconTint: Color,
  iconBg: Color,
  modifier: Modifier = Modifier,
  badgeText: String? = null,
  badgeBg: Color = Color.Transparent,
  badgeColor: Color = Color.Unspecified,
  onClick: (() -> Unit)? = null
) {
  val baseModifier = modifier
    .clip(RoundedCornerShape(12.dp))
    .background(MaterialTheme.colorScheme.surface)
    .border(1.dp, Slate200, RoundedCornerShape(12.dp))

  val clickableModifier = if (onClick != null) {
    baseModifier.clickable(onClick = onClick)
  } else {
    baseModifier
  }

  Column(
    modifier = clickableModifier
      .padding(18.dp)
      .testTag("stat_card_${title.lowercase().replace(" ", "_")}"),
    verticalArrangement = Arrangement.spacedBy(10.dp)
  ) {
    Row(
      modifier = Modifier.fillMaxWidth(),
      horizontalArrangement = Arrangement.SpaceBetween,
      verticalAlignment = Alignment.CenterVertically
    ) {
      Text(
        text = title,
        color = Slate500,
        fontSize = 13.sp,
        fontWeight = FontWeight.Medium
      )
      Box(
        modifier = Modifier
          .size(38.dp)
          .clip(RoundedCornerShape(10.dp))
          .background(iconBg),
        contentAlignment = Alignment.Center
      ) {
        Icon(
          imageVector = icon,
          contentDescription = title,
          tint = iconTint,
          modifier = Modifier.size(20.dp)
        )
      }
    }

    Text(
      text = value,
      color = Slate900,
      fontSize = 28.sp,
      fontWeight = FontWeight.Bold,
      letterSpacing = (-0.5).sp
    )

    Row(
      verticalAlignment = Alignment.CenterVertically,
      horizontalArrangement = Arrangement.spacedBy(6.dp)
    ) {
      if (badgeText != null) {
        Box(
          modifier = Modifier
            .clip(RoundedCornerShape(6.dp))
            .background(badgeBg)
            .padding(horizontal = 6.dp, vertical = 2.dp)
        ) {
          Text(
            text = badgeText,
            color = badgeColor,
            fontSize = 11.sp,
            fontWeight = FontWeight.SemiBold
          )
        }
      }
      Text(
        text = subtitle,
        color = Slate500,
        fontSize = 12.sp
      )
    }
  }
}

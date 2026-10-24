package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.IndigoPrimary
import com.example.ui.theme.Slate200
import com.example.ui.theme.Slate500
import com.example.ui.theme.Slate900
import com.example.ui.theme.TealAccent

@Composable
fun CrmTopBar(
  title: String,
  subtitle: String? = null,
  isCompact: Boolean = false,
  onMenuClick: (() -> Unit)? = null,
  actionButtonText: String? = null,
  onActionClick: (() -> Unit)? = null,
  modifier: Modifier = Modifier
) {
  Row(
    modifier = modifier
      .fillMaxWidth()
      .background(MaterialTheme.colorScheme.surface)
      .border(1.dp, Slate200)
      .padding(horizontal = 20.dp, vertical = 14.dp),
    verticalAlignment = Alignment.CenterVertically,
    horizontalArrangement = Arrangement.SpaceBetween
  ) {
    Row(verticalAlignment = Alignment.CenterVertically) {
      if (isCompact && onMenuClick != null) {
        IconButton(
          onClick = onMenuClick,
          modifier = Modifier
            .padding(end = 8.dp)
            .testTag("mobile_menu_btn")
        ) {
          Icon(
            imageVector = Icons.Default.Menu,
            contentDescription = "Open Navigation",
            tint = Slate900
          )
        }
      }

      Column {
        Text(
          text = title,
          color = Slate900,
          fontSize = if (isCompact) 18.sp else 20.sp,
          fontWeight = FontWeight.Bold,
          letterSpacing = (-0.3).sp
        )
        if (subtitle != null) {
          Text(
            text = subtitle,
            color = Slate500,
            fontSize = 12.sp
          )
        }
      }
    }

    Row(verticalAlignment = Alignment.CenterVertically) {
      if (actionButtonText != null && onActionClick != null) {
        Button(
          onClick = onActionClick,
          colors = ButtonDefaults.buttonColors(containerColor = IndigoPrimary),
          shape = RoundedCornerShape(8.dp),
          modifier = Modifier
            .height(38.dp)
            .testTag("topbar_action_btn")
        ) {
          Icon(
            imageVector = Icons.Default.Add,
            contentDescription = null,
            tint = Color.White,
            modifier = Modifier.size(16.dp)
          )
          Spacer(modifier = Modifier.width(6.dp))
          Text(
            text = actionButtonText,
            fontSize = 13.sp,
            fontWeight = FontWeight.SemiBold,
            color = Color.White
          )
        }
      }
    }
  }
}

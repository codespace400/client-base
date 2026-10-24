package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Business
import androidx.compose.material.icons.filled.Category
import androidx.compose.material.icons.filled.Dashboard
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.People
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import com.example.ui.theme.IndigoContainer
import com.example.ui.theme.IndigoPrimary
import com.example.ui.theme.Slate200
import com.example.ui.theme.Slate400
import com.example.ui.theme.Slate500
import com.example.ui.theme.Slate600
import com.example.ui.theme.Slate700
import com.example.ui.theme.Slate900
import com.example.ui.theme.TealAccent
import com.example.viewmodel.AppScreen

data class NavItem(
  val screen: AppScreen,
  val label: String,
  val icon: ImageVector,
  val badgeCount: Int? = null
)

@Composable
fun CrmSidebar(
  currentScreen: AppScreen,
  onScreenSelected: (AppScreen) -> Unit,
  onAddCustomerClick: () -> Unit,
  workspaceName: String,
  userName: String,
  userEmail: String,
  pendingRemindersCount: Int,
  onLogoutClick: () -> Unit,
  modifier: Modifier = Modifier
) {
  val navItems = listOf(
    NavItem(AppScreen.DASHBOARD, "Dashboard", Icons.Default.Dashboard),
    NavItem(AppScreen.CUSTOMERS, "Customers", Icons.Default.People),
    NavItem(AppScreen.SEGMENTS, "Segments", Icons.Default.Category),
    NavItem(
      AppScreen.REMINDERS,
      "Reminders",
      Icons.Default.Notifications,
      if (pendingRemindersCount > 0) pendingRemindersCount else null
    )
  )

  Column(
    modifier = modifier
      .width(260.dp)
      .fillMaxHeight()
      .background(MaterialTheme.colorScheme.surface)
      .border(
        width = 1.dp,
        color = Slate200
      )
      .padding(16.dp),
    verticalArrangement = Arrangement.SpaceBetween
  ) {
    Column {
      // Brand Header
      Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(bottom = 16.dp)
      ) {
        Box(
          modifier = Modifier
            .size(36.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(IndigoPrimary),
          contentAlignment = Alignment.Center
        ) {
          Icon(
            imageVector = Icons.Default.Business,
            contentDescription = "ClientBase Logo",
            tint = Color.White,
            modifier = Modifier.size(20.dp)
          )
        }
        Column(modifier = Modifier.padding(start = 12.dp)) {
          Text(
            text = "ClientBase",
            color = Slate900,
            fontSize = 17.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = (-0.3).sp
          )
          Text(
            text = "Lightweight CRM",
            color = Slate500,
            fontSize = 11.sp
          )
        }
      }

      // Workspace Pill
      Box(
        modifier = Modifier
          .fillMaxWidth()
          .clip(RoundedCornerShape(8.dp))
          .background(IndigoContainer)
          .padding(horizontal = 10.dp, vertical = 6.dp)
      ) {
        Row(
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.SpaceBetween,
          modifier = Modifier.fillMaxWidth()
        ) {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
              modifier = Modifier
                .size(7.dp)
                .clip(CircleShape)
                .background(TealAccent)
            )
            Text(
              text = workspaceName,
              color = IndigoPrimary,
              fontSize = 12.sp,
              fontWeight = FontWeight.SemiBold,
              modifier = Modifier.padding(start = 6.dp)
            )
          }
        }
      }

      Spacer(modifier = Modifier.height(16.dp))

      // Primary Quick Action "+ Add Customer"
      Button(
        onClick = onAddCustomerClick,
        colors = ButtonDefaults.buttonColors(containerColor = IndigoPrimary),
        shape = RoundedCornerShape(8.dp),
        modifier = Modifier
          .fillMaxWidth()
          .height(44.dp)
          .testTag("sidebar_add_customer_btn")
      ) {
        Icon(
          imageVector = Icons.Default.Add,
          contentDescription = "Add Customer",
          tint = Color.White,
          modifier = Modifier.size(18.dp)
        )
        Spacer(modifier = Modifier.width(6.dp))
        Text(
          text = "Add Customer",
          fontSize = 13.sp,
          fontWeight = FontWeight.SemiBold,
          color = Color.White
        )
      }

      Spacer(modifier = Modifier.height(20.dp))

      // Navigation Items
      Text(
        text = "NAVIGATION",
        color = Slate400,
        fontSize = 11.sp,
        fontWeight = FontWeight.Bold,
        letterSpacing = 0.8.sp,
        modifier = Modifier.padding(start = 8.dp, bottom = 8.dp)
      )

      navItems.forEach { item ->
        val isSelected = currentScreen == item.screen
        val itemBg = if (isSelected) IndigoContainer else Color.Transparent
        val itemTextColor = if (isSelected) IndigoPrimary else Slate700
        val iconColor = if (isSelected) IndigoPrimary else Slate500

        Row(
          verticalAlignment = Alignment.CenterVertically,
          modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .background(itemBg)
            .clickable { onScreenSelected(item.screen) }
            .padding(horizontal = 12.dp, vertical = 10.dp)
            .testTag("nav_item_${item.label.lowercase()}")
        ) {
          Icon(
            imageVector = item.icon,
            contentDescription = item.label,
            tint = iconColor,
            modifier = Modifier.size(20.dp)
          )
          Text(
            text = item.label,
            color = itemTextColor,
            fontSize = 14.sp,
            fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Medium,
            modifier = Modifier
              .padding(start = 12.dp)
              .weight(1f)
          )
          if (item.badgeCount != null) {
            Box(
              modifier = Modifier
                .clip(CircleShape)
                .background(if (isSelected) IndigoPrimary else Slate200)
                .padding(horizontal = 7.dp, vertical = 2.dp)
            ) {
              Text(
                text = "${item.badgeCount}",
                color = if (isSelected) Color.White else Slate700,
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold
              )
            }
          }
        }
        Spacer(modifier = Modifier.height(4.dp))
      }
    }

    // User Profile / Logout Footer
    Column {
      HorizontalDivider(color = Slate200, modifier = Modifier.padding(bottom = 12.dp))
      Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier.fillMaxWidth()
      ) {
        Row(
          verticalAlignment = Alignment.CenterVertically,
          modifier = Modifier.weight(1f)
        ) {
          Box(
            modifier = Modifier
              .size(36.dp)
              .clip(CircleShape)
              .background(IndigoPrimary),
            contentAlignment = Alignment.Center
          ) {
            Text(
              text = userName.take(2).uppercase(),
              color = Color.White,
              fontSize = 13.sp,
              fontWeight = FontWeight.Bold
            )
          }
          Column(modifier = Modifier.padding(start = 10.dp)) {
            Text(
              text = userName,
              color = Slate900,
              fontSize = 13.sp,
              fontWeight = FontWeight.SemiBold
            )
            Text(
              text = userEmail,
              color = Slate500,
              fontSize = 11.sp
            )
          }
        }
        IconButton(
          onClick = onLogoutClick,
          modifier = Modifier.testTag("sidebar_logout_btn")
        ) {
          Icon(
            imageVector = Icons.AutoMirrored.Filled.Logout,
            contentDescription = "Logout",
            tint = Slate500,
            modifier = Modifier.size(18.dp)
          )
        }
      }
    }
  }
}

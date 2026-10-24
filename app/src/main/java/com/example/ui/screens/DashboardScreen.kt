package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.EventNote
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material.icons.filled.NotificationsActive
import androidx.compose.material.icons.filled.PersonAdd
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.Videocam
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
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
import com.example.data.model.Customer
import com.example.data.model.Interaction
import com.example.data.model.Reminder
import com.example.ui.components.CustomerAvatar
import com.example.ui.components.StatCard
import com.example.ui.theme.IndigoContainer
import com.example.ui.theme.IndigoPrimary
import com.example.ui.theme.Slate200
import com.example.ui.theme.Slate400
import com.example.ui.theme.Slate500
import com.example.ui.theme.Slate600
import com.example.ui.theme.Slate700
import com.example.ui.theme.Slate900
import com.example.ui.theme.TealAccent
import com.example.ui.theme.TealContainer
import com.example.viewmodel.AppScreen
import com.example.viewmodel.CrmViewModel

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun DashboardScreen(
  customers: List<Customer>,
  interactions: List<Interaction>,
  reminders: List<Reminder>,
  onAddCustomerClick: () -> Unit,
  onAddInteractionClick: () -> Unit,
  onNavigateToScreen: (AppScreen) -> Unit,
  onCustomerClick: (Long) -> Unit,
  onReminderToggle: (Reminder) -> Unit,
  modifier: Modifier = Modifier
) {
  val (totalCustomers, newThisMonth, followupsDue) = rememberDashboardStats(customers, reminders)

  LazyColumn(
    modifier = modifier
      .fillMaxSize()
      .padding(20.dp),
    verticalArrangement = Arrangement.spacedBy(20.dp)
  ) {
    // Header Banner with Welcome & Quick Actions
    item {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Column {
          Text(
            text = "Welcome to your CRM Dashboard",
            color = Slate900,
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = (-0.4).sp
          )
          Text(
            text = "Here is what's happening with your pipeline and client communications today.",
            color = Slate500,
            fontSize = 13.sp,
            modifier = Modifier.padding(top = 2.dp)
          )
        }
      }
    }

    // Stat Cards Row
    item {
      FlowRow(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        maxItemsInEachRow = 3
      ) {
        StatCard(
          title = "Total Customers",
          value = "$totalCustomers",
          subtitle = "Active in database",
          icon = Icons.Default.Groups,
          iconTint = IndigoPrimary,
          iconBg = IndigoContainer,
          badgeText = "+12% vs last mo",
          badgeBg = TealContainer,
          badgeColor = TealAccent,
          modifier = Modifier
            .weight(1f)
            .clickable { onNavigateToScreen(AppScreen.CUSTOMERS) }
        )

        StatCard(
          title = "New This Month",
          value = "$newThisMonth",
          subtitle = "New accounts onboarded",
          icon = Icons.Default.PersonAdd,
          iconTint = TealAccent,
          iconBg = TealContainer,
          badgeText = "Pipeline growth",
          badgeBg = IndigoContainer,
          badgeColor = IndigoPrimary,
          modifier = Modifier.weight(1f)
        )

        StatCard(
          title = "Follow-ups Due",
          value = "$followupsDue",
          subtitle = "Pending reminder tasks",
          icon = Icons.Default.NotificationsActive,
          iconTint = Color(0xFFF59E0B),
          iconBg = Color(0xFFFEF3C7),
          badgeText = if (followupsDue > 0) "Needs attention" else "All caught up",
          badgeBg = if (followupsDue > 0) Color(0xFFFEE2E2) else TealContainer,
          badgeColor = if (followupsDue > 0) Color(0xFFDC2626) else TealAccent,
          modifier = Modifier
            .weight(1f)
            .clickable { onNavigateToScreen(AppScreen.REMINDERS) }
        )
      }
    }

    // Quick Action Bar
    item {
      Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = androidx.compose.foundation.BorderStroke(1.dp, Slate200)
      ) {
        Row(
          modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Column {
            Text(
              text = "Quick Management Shortcuts",
              color = Slate900,
              fontSize = 14.sp,
              fontWeight = FontWeight.SemiBold
            )
            Text(
              text = "Fast-track client logging and communication entries",
              color = Slate500,
              fontSize = 12.sp
            )
          }

          Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            OutlinedButton(
              onClick = onAddInteractionClick,
              shape = RoundedCornerShape(8.dp),
              modifier = Modifier.testTag("dashboard_log_interaction_btn")
            ) {
              Icon(Icons.Default.EventNote, contentDescription = null, tint = IndigoPrimary, modifier = Modifier.size(16.dp))
              Spacer(modifier = Modifier.width(6.dp))
              Text("Log Interaction", color = IndigoPrimary, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
            }

            Button(
              onClick = onAddCustomerClick,
              colors = ButtonDefaults.buttonColors(containerColor = IndigoPrimary),
              shape = RoundedCornerShape(8.dp),
              modifier = Modifier.testTag("dashboard_add_customer_btn")
            ) {
              Icon(Icons.Default.Add, contentDescription = null, tint = Color.White, modifier = Modifier.size(16.dp))
              Spacer(modifier = Modifier.width(6.dp))
              Text("Add Customer", color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
            }
          }
        }
      }
    }

    // Section: Recent Activity Feed & Upcoming Follow-ups side-by-side or stacked
    item {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Text(
          text = "Recent Activity Feed",
          color = Slate900,
          fontSize = 16.sp,
          fontWeight = FontWeight.Bold
        )
        Text(
          text = "${interactions.size} total entries",
          color = Slate500,
          fontSize = 12.sp
        )
      }
    }

    if (interactions.isEmpty()) {
      item {
        Card(
          modifier = Modifier.fillMaxWidth(),
          shape = RoundedCornerShape(12.dp),
          colors = CardDefaults.cardColors(containerColor = Color.White),
          border = androidx.compose.foundation.BorderStroke(1.dp, Slate200)
        ) {
          Column(
            modifier = Modifier
              .fillMaxWidth()
              .padding(32.dp),
            horizontalAlignment = Alignment.CenterHorizontally
          ) {
            Icon(Icons.Default.Schedule, contentDescription = null, tint = Slate400, modifier = Modifier.size(40.dp))
            Spacer(modifier = Modifier.height(10.dp))
            Text("No interaction activity yet", color = Slate700, fontWeight = FontWeight.SemiBold, fontSize = 14.sp)
            Text("Log your calls, emails, and meetings to populate the timeline.", color = Slate500, fontSize = 12.sp)
          }
        }
      }
    } else {
      items(interactions.take(10)) { item ->
        ActivityFeedCard(
          interaction = item,
          onCustomerClick = { onCustomerClick(item.customerId) }
        )
      }
    }
  }
}

@Composable
fun ActivityFeedCard(
  interaction: Interaction,
  onCustomerClick: () -> Unit,
  modifier: Modifier = Modifier
) {
  val (typeIcon, iconColor, iconBg) = when (interaction.type.lowercase()) {
    "call" -> Triple(Icons.Default.Call, IndigoPrimary, IndigoContainer)
    "meeting" -> Triple(Icons.Default.Videocam, TealAccent, TealContainer)
    "email" -> Triple(Icons.Default.Email, Color(0xFF0284C7), Color(0xFFE0F2FE))
    else -> Triple(Icons.Default.EventNote, Color(0xFF8B5CF6), Color(0xFFF3E8FF))
  }

  Card(
    modifier = modifier
      .fillMaxWidth()
      .clickable(onClick = onCustomerClick)
      .testTag("activity_card_${interaction.id}"),
    shape = RoundedCornerShape(12.dp),
    colors = CardDefaults.cardColors(containerColor = Color.White),
    border = androidx.compose.foundation.BorderStroke(1.dp, Slate200)
  ) {
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .padding(16.dp),
      verticalAlignment = Alignment.Top
    ) {
      Box(
        modifier = Modifier
          .size(40.dp)
          .clip(RoundedCornerShape(10.dp))
          .background(iconBg),
        contentAlignment = Alignment.Center
      ) {
        Icon(
          imageVector = typeIcon,
          contentDescription = interaction.type,
          tint = iconColor,
          modifier = Modifier.size(20.dp)
        )
      }

      Spacer(modifier = Modifier.width(14.dp))

      Column(modifier = Modifier.weight(1f)) {
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
              text = interaction.customerName,
              color = Slate900,
              fontSize = 14.sp,
              fontWeight = FontWeight.Bold
            )
            Box(
              modifier = Modifier
                .padding(start = 8.dp)
                .clip(RoundedCornerShape(4.dp))
                .background(iconBg)
                .padding(horizontal = 6.dp, vertical = 2.dp)
            ) {
              Text(
                text = interaction.type.uppercase(),
                color = iconColor,
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold
              )
            }
          }

          Text(
            text = CrmViewModel.formatRelativeTime(interaction.dateMillis),
            color = Slate400,
            fontSize = 11.sp
          )
        }

        Text(
          text = interaction.summary,
          color = Slate700,
          fontSize = 13.sp,
          fontWeight = FontWeight.SemiBold,
          modifier = Modifier.padding(top = 4.dp)
        )

        if (interaction.description.isNotBlank()) {
          Text(
            text = interaction.description,
            color = Slate500,
            fontSize = 12.sp,
            lineHeight = 17.sp,
            modifier = Modifier.padding(top = 3.dp)
          )
        }
      }
    }
  }
}

@Composable
private fun rememberDashboardStats(
  customers: List<Customer>,
  reminders: List<Reminder>
): Triple<Int, Int, Int> {
  val total = customers.size
  val cal = java.util.Calendar.getInstance().apply {
    set(java.util.Calendar.DAY_OF_MONTH, 1)
    set(java.util.Calendar.HOUR_OF_DAY, 0)
    set(java.util.Calendar.MINUTE, 0)
  }
  val newThisMonth = customers.count { it.createdAtMillis >= cal.timeInMillis }
  val followupsDue = reminders.count { !it.isCompleted }
  return Triple(total, newThisMonth, followupsDue)
}

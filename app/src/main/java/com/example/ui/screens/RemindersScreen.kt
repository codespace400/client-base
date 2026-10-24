package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Alarm
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.EventAvailable
import androidx.compose.material.icons.filled.NotificationsActive
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
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
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.Reminder
import com.example.ui.theme.IndigoContainer
import com.example.ui.theme.IndigoPrimary
import com.example.ui.theme.Slate200
import com.example.ui.theme.Slate400
import com.example.ui.theme.Slate50
import com.example.ui.theme.Slate500
import com.example.ui.theme.Slate600
import com.example.ui.theme.Slate700
import com.example.ui.theme.Slate900
import com.example.ui.theme.TealAccent
import com.example.ui.theme.TealContainer
import com.example.viewmodel.CrmViewModel
import com.example.viewmodel.ReminderFilter

@Composable
fun RemindersScreen(
  reminders: List<Reminder>,
  allReminders: List<Reminder>,
  currentFilter: ReminderFilter,
  onFilterChange: (ReminderFilter) -> Unit,
  onToggleCompleted: (Reminder) -> Unit,
  onDeleteReminder: (Reminder) -> Unit,
  onAddReminderClick: () -> Unit,
  onCustomerClick: (Long) -> Unit,
  modifier: Modifier = Modifier
) {
  val now = System.currentTimeMillis()
  val pendingCount = allReminders.count { !it.isCompleted }
  val overdueCount = allReminders.count { !it.isCompleted && it.dueDateMillis < now }
  val completedCount = allReminders.count { it.isCompleted }

  LazyColumn(
    modifier = modifier
      .fillMaxSize()
      .background(Slate50)
      .padding(20.dp),
    verticalArrangement = Arrangement.spacedBy(16.dp)
  ) {
    // Header
    item {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Column {
          Text(
            text = "Follow-up Reminders",
            color = Slate900,
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = (-0.4).sp
          )
          Text(
            text = "Keep up with critical customer action items and promised timelines.",
            color = Slate500,
            fontSize = 13.sp,
            modifier = Modifier.padding(top = 2.dp)
          )
        }

        Button(
          onClick = onAddReminderClick,
          colors = ButtonDefaults.buttonColors(containerColor = IndigoPrimary),
          shape = RoundedCornerShape(8.dp),
          modifier = Modifier.testTag("reminders_add_btn")
        ) {
          Icon(Icons.Default.Add, contentDescription = null, tint = Color.White, modifier = Modifier.size(16.dp))
          Spacer(modifier = Modifier.width(6.dp))
          Text("New Follow-up", color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
        }
      }
    }

    // Filter Tabs
    item {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
      ) {
        FilterTab(
          title = "All Reminders",
          count = allReminders.size,
          isSelected = currentFilter == ReminderFilter.ALL,
          onClick = { onFilterChange(ReminderFilter.ALL) }
        )

        FilterTab(
          title = "Pending",
          count = pendingCount,
          badgeColor = if (overdueCount > 0) Color(0xFFEF4444) else IndigoPrimary,
          isSelected = currentFilter == ReminderFilter.PENDING,
          onClick = { onFilterChange(ReminderFilter.PENDING) }
        )

        FilterTab(
          title = "Completed",
          count = completedCount,
          badgeColor = TealAccent,
          isSelected = currentFilter == ReminderFilter.COMPLETED,
          onClick = { onFilterChange(ReminderFilter.COMPLETED) }
        )
      }
    }

    // Reminders List
    if (reminders.isEmpty()) {
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
              .padding(40.dp),
            horizontalAlignment = Alignment.CenterHorizontally
          ) {
            Icon(Icons.Default.EventAvailable, contentDescription = null, tint = Slate400, modifier = Modifier.size(44.dp))
            Spacer(modifier = Modifier.height(10.dp))
            Text(
              text = if (currentFilter == ReminderFilter.COMPLETED) "No completed follow-ups yet" else "All caught up on reminders!",
              color = Slate700,
              fontSize = 15.sp,
              fontWeight = FontWeight.SemiBold
            )
            Text(
              text = "Set reminders for contracts, calls, or demos to stay proactive.",
              color = Slate500,
              fontSize = 12.sp,
              modifier = Modifier.padding(top = 4.dp)
            )
          }
        }
      }
    } else {
      items(reminders) { reminder ->
        ReminderCard(
          reminder = reminder,
          now = now,
          onToggle = { onToggleCompleted(reminder) },
          onDelete = { onDeleteReminder(reminder) },
          onCustomerClick = { onCustomerClick(reminder.customerId) }
        )
      }
    }
  }
}

@Composable
fun ReminderCard(
  reminder: Reminder,
  now: Long,
  onToggle: () -> Unit,
  onDelete: () -> Unit,
  onCustomerClick: () -> Unit,
  modifier: Modifier = Modifier
) {
  val isOverdue = !reminder.isCompleted && reminder.dueDateMillis < now
  val isDueToday = !reminder.isCompleted && !isOverdue && (reminder.dueDateMillis - now < 86_400_000L)

  val cardBg = when {
    reminder.isCompleted -> Color(0xFFF8FAFC)
    isOverdue -> Color(0xFFFFF5F5)
    isDueToday -> Color(0xFFFFFBEB)
    else -> Color.White
  }

  val borderStroke = when {
    isOverdue -> Color(0xFFFECACA)
    isDueToday -> Color(0xFFFDE68A)
    else -> Slate200
  }

  Card(
    modifier = modifier
      .fillMaxWidth()
      .testTag("reminder_card_${reminder.id}"),
    shape = RoundedCornerShape(12.dp),
    colors = CardDefaults.cardColors(containerColor = cardBg),
    border = androidx.compose.foundation.BorderStroke(1.dp, borderStroke)
  ) {
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .padding(16.dp),
      verticalAlignment = Alignment.Top
    ) {
      // Checkbox to mark done
      Checkbox(
        checked = reminder.isCompleted,
        onCheckedChange = { onToggle() },
        colors = CheckboxDefaults.colors(
          checkedColor = TealAccent,
          uncheckedColor = if (isOverdue) Color(0xFFEF4444) else Slate400
        ),
        modifier = Modifier.testTag("reminder_check_${reminder.id}")
      )

      Spacer(modifier = Modifier.width(8.dp))

      Column(modifier = Modifier.weight(1f)) {
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          // Title
          Text(
            text = reminder.title,
            color = if (reminder.isCompleted) Slate400 else Slate900,
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            textDecoration = if (reminder.isCompleted) TextDecoration.LineThrough else TextDecoration.None
          )

          // Delete
          IconButton(
            onClick = onDelete,
            modifier = Modifier.size(24.dp)
          ) {
            Icon(Icons.Default.Delete, contentDescription = "Delete Reminder", tint = Slate400, modifier = Modifier.size(16.dp))
          }
        }

        // Customer link badge
        Row(
          verticalAlignment = Alignment.CenterVertically,
          modifier = Modifier
            .padding(top = 4.dp)
            .clip(RoundedCornerShape(6.dp))
            .background(IndigoContainer)
            .clickable(onClick = onCustomerClick)
            .padding(horizontal = 8.dp, vertical = 3.dp)
        ) {
          Icon(Icons.Default.Person, contentDescription = null, tint = IndigoPrimary, modifier = Modifier.size(12.dp))
          Spacer(modifier = Modifier.width(4.dp))
          Text(
            text = reminder.customerName,
            color = IndigoPrimary,
            fontSize = 11.sp,
            fontWeight = FontWeight.SemiBold
          )
        }

        if (reminder.note.isNotBlank()) {
          Text(
            text = reminder.note,
            color = Slate600,
            fontSize = 12.sp,
            modifier = Modifier.padding(top = 6.dp)
          )
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Due date indicator
        Row(
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
          val (badgeText, badgeBg, badgeColor) = when {
            reminder.isCompleted -> Triple("Completed", TealContainer, TealAccent)
            isOverdue -> Triple("Overdue", Color(0xFFFEE2E2), Color(0xFFDC2626))
            isDueToday -> Triple("Due Today", Color(0xFFFEF3C7), Color(0xFFB45309))
            else -> Triple("Upcoming", IndigoContainer, IndigoPrimary)
          }

          Box(
            modifier = Modifier
              .clip(RoundedCornerShape(4.dp))
              .background(badgeBg)
              .padding(horizontal = 6.dp, vertical = 2.dp)
          ) {
            Text(
              text = badgeText,
              color = badgeColor,
              fontSize = 10.sp,
              fontWeight = FontWeight.Bold
            )
          }

          Icon(Icons.Default.Schedule, contentDescription = null, tint = Slate400, modifier = Modifier.size(14.dp))
          Text(
            text = CrmViewModel.formatDateTime(reminder.dueDateMillis),
            color = Slate500,
            fontSize = 11.sp
          )
        }
      }
    }
  }
}

@Composable
fun FilterTab(
  title: String,
  count: Int,
  badgeColor: Color = IndigoPrimary,
  isSelected: Boolean,
  onClick: () -> Unit
) {
  Box(
    modifier = Modifier
      .clip(RoundedCornerShape(8.dp))
      .background(if (isSelected) IndigoPrimary else Color.White)
      .border(1.dp, if (isSelected) IndigoPrimary else Slate200, RoundedCornerShape(8.dp))
      .clickable(onClick = onClick)
      .padding(horizontal = 14.dp, vertical = 8.dp)
  ) {
    Row(verticalAlignment = Alignment.CenterVertically) {
      Text(
        text = title,
        color = if (isSelected) Color.White else Slate700,
        fontSize = 13.sp,
        fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Medium
      )
      Spacer(modifier = Modifier.width(6.dp))
      Box(
        modifier = Modifier
          .clip(RoundedCornerShape(10.dp))
          .background(if (isSelected) Color.White.copy(alpha = 0.25f) else Slate50)
          .padding(horizontal = 6.dp, vertical = 1.dp)
      ) {
        Text(
          text = "$count",
          color = if (isSelected) Color.White else badgeColor,
          fontSize = 11.sp,
          fontWeight = FontWeight.Bold
        )
      }
    }
  }
}

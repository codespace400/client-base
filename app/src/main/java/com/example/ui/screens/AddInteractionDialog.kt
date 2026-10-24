package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.EventNote
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.Subject
import androidx.compose.material.icons.filled.Videocam
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.data.model.Customer
import com.example.ui.theme.IndigoContainer
import com.example.ui.theme.IndigoPrimary
import com.example.ui.theme.Slate200
import com.example.ui.theme.Slate50
import com.example.ui.theme.Slate500
import com.example.ui.theme.Slate700
import com.example.ui.theme.Slate900
import com.example.ui.theme.TealAccent
import com.example.ui.theme.TealContainer
import com.example.viewmodel.CrmViewModel

data class InteractionTypeOption(
  val type: String,
  val icon: ImageVector,
  val color: Color,
  val bg: Color
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddInteractionDialog(
  initialCustomerId: Long?,
  customers: List<Customer>,
  onDismiss: () -> Unit,
  onSave: (
    customerId: Long,
    type: String,
    dateMillis: Long,
    summary: String,
    description: String,
    scheduleFollowUpDays: Int?
  ) -> Unit
) {
  var selectedCustomerId by remember {
    mutableStateOf(initialCustomerId ?: customers.firstOrNull()?.id ?: 1L)
  }
  var selectedType by remember { mutableStateOf("Call") }
  var summary by remember { mutableStateOf("") }
  var description by remember { mutableStateOf("") }
  var dateMillis by remember { mutableLongStateOf(System.currentTimeMillis()) }

  var scheduleFollowUp by remember { mutableStateOf(false) }
  var followUpDays by remember { mutableStateOf(3) }

  var customerDropdownExpanded by remember { mutableStateOf(false) }
  var summaryError by remember { mutableStateOf(false) }

  val typeOptions = listOf(
    InteractionTypeOption("Call", Icons.Default.Call, IndigoPrimary, IndigoContainer),
    InteractionTypeOption("Meeting", Icons.Default.Videocam, TealAccent, TealContainer),
    InteractionTypeOption("Email", Icons.Default.Email, Color(0xFF0284C7), Color(0xFFE0F2FE)),
    InteractionTypeOption("Note", Icons.Default.EventNote, Color(0xFF8B5CF6), Color(0xFFF3E8FF))
  )

  val currentCustomer = customers.find { it.id == selectedCustomerId }

  Dialog(onDismissRequest = onDismiss) {
    Card(
      modifier = Modifier
        .fillMaxWidth()
        .widthIn(max = 500.dp)
        .testTag("add_interaction_dialog"),
      shape = RoundedCornerShape(16.dp),
      colors = CardDefaults.cardColors(containerColor = Color.White),
      border = androidx.compose.foundation.BorderStroke(1.dp, Slate200)
    ) {
      Column(
        modifier = Modifier
          .padding(24.dp)
          .verticalScroll(rememberScrollState())
      ) {
        // Title & Close
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Text(
            text = "Log Note / Interaction",
            color = Slate900,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold
          )
          IconButton(
            onClick = onDismiss,
            modifier = Modifier.testTag("interaction_dialog_close")
          ) {
            Icon(Icons.Default.Close, contentDescription = "Close", tint = Slate500)
          }
        }

        HorizontalDivider(color = Slate200, modifier = Modifier.padding(vertical = 12.dp))

        // Customer Selector
        Text("Customer Account *", color = Slate700, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
        Spacer(modifier = Modifier.height(4.dp))
        ExposedDropdownMenuBox(
          expanded = customerDropdownExpanded,
          onExpandedChange = { customerDropdownExpanded = !customerDropdownExpanded }
        ) {
          OutlinedTextField(
            value = currentCustomer?.let { "${it.name} (${it.company})" } ?: "Select Customer",
            onValueChange = {},
            readOnly = true,
            leadingIcon = { Icon(Icons.Default.Person, contentDescription = null, tint = IndigoPrimary, modifier = Modifier.size(18.dp)) },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = customerDropdownExpanded) },
            modifier = Modifier
              .fillMaxWidth()
              .menuAnchor(MenuAnchorType.PrimaryNotEditable)
              .testTag("interaction_customer_selector"),
            shape = RoundedCornerShape(8.dp),
            colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = IndigoPrimary, unfocusedBorderColor = Slate200)
          )

          ExposedDropdownMenu(
            expanded = customerDropdownExpanded,
            onDismissRequest = { customerDropdownExpanded = false }
          ) {
            customers.forEach { cust ->
              DropdownMenuItem(
                text = { Text("${cust.name} - ${cust.company}") },
                onClick = {
                  selectedCustomerId = cust.id
                  customerDropdownExpanded = false
                }
              )
            }
          }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Interaction Type Selection
        Text("Interaction Type *", color = Slate700, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
        Spacer(modifier = Modifier.height(6.dp))
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
          typeOptions.forEach { opt ->
            val isSelected = selectedType.equals(opt.type, ignoreCase = true)
            Row(
              modifier = Modifier
                .weight(1f)
                .clip(RoundedCornerShape(8.dp))
                .background(if (isSelected) opt.bg else Slate50)
                .border(
                  width = 1.dp,
                  color = if (isSelected) opt.color else Slate200,
                  shape = RoundedCornerShape(8.dp)
                )
                .clickable { selectedType = opt.type }
                .padding(vertical = 10.dp),
              horizontalArrangement = Arrangement.Center,
              verticalAlignment = Alignment.CenterVertically
            ) {
              Icon(
                imageVector = opt.icon,
                contentDescription = opt.type,
                tint = if (isSelected) opt.color else Slate500,
                modifier = Modifier.size(16.dp)
              )
              Spacer(modifier = Modifier.width(5.dp))
              Text(
                text = opt.type,
                color = if (isSelected) opt.color else Slate700,
                fontSize = 12.sp,
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
              )
            }
          }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Date selection presets
        Text("Interaction Date & Time", color = Slate700, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
        Spacer(modifier = Modifier.height(6.dp))
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
          val now = System.currentTimeMillis()
          val dayMillis = 86_400_000L

          val datePresets = listOf(
            Pair("Now", now),
            Pair("Today Morning", now - (3 * 3600_000L)),
            Pair("Yesterday", now - dayMillis),
            Pair("2 Days Ago", now - (2 * dayMillis))
          )

          datePresets.forEach { (label, presetTime) ->
            val isSelected = Math.abs(dateMillis - presetTime) < 1800_000L
            Box(
              modifier = Modifier
                .weight(1f)
                .clip(RoundedCornerShape(6.dp))
                .background(if (isSelected) IndigoContainer else Slate50)
                .border(1.dp, if (isSelected) IndigoPrimary else Slate200, RoundedCornerShape(6.dp))
                .clickable { dateMillis = presetTime }
                .padding(vertical = 6.dp),
              contentAlignment = Alignment.Center
            ) {
              Text(
                text = label,
                color = if (isSelected) IndigoPrimary else Slate700,
                fontSize = 11.sp,
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
              )
            }
          }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Headline / Summary
        Text("Subject / Summary *", color = Slate700, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
        Spacer(modifier = Modifier.height(4.dp))
        OutlinedTextField(
          value = summary,
          onValueChange = {
            summary = it
            if (it.isNotBlank()) summaryError = false
          },
          placeholder = { Text("e.g. Q4 Contract Renewal & Cloud Review", fontSize = 13.sp) },
          leadingIcon = { Icon(Icons.Default.Subject, contentDescription = null, tint = IndigoPrimary, modifier = Modifier.size(18.dp)) },
          isError = summaryError,
          singleLine = true,
          modifier = Modifier
            .fillMaxWidth()
            .testTag("input_interaction_summary"),
          shape = RoundedCornerShape(8.dp),
          colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = IndigoPrimary, unfocusedBorderColor = Slate200)
        )
        if (summaryError) {
          Text("Summary is required", color = Color(0xFFEF4444), fontSize = 11.sp, modifier = Modifier.padding(top = 2.dp))
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Detailed Description / Notes
        Text("Detailed Notes / Minutes", color = Slate700, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
        Spacer(modifier = Modifier.height(4.dp))
        OutlinedTextField(
          value = description,
          onValueChange = { description = it },
          placeholder = { Text("Record key conversation points, agreed action items, pricing discussed...", fontSize = 13.sp) },
          leadingIcon = { Icon(Icons.Default.Description, contentDescription = null, tint = IndigoPrimary, modifier = Modifier.size(18.dp)) },
          minLines = 3,
          maxLines = 6,
          modifier = Modifier
            .fillMaxWidth()
            .testTag("input_interaction_description"),
          shape = RoundedCornerShape(8.dp),
          colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = IndigoPrimary, unfocusedBorderColor = Slate200)
        )

        Spacer(modifier = Modifier.height(14.dp))

        // Schedule Reminder Checkbox
        Row(
          modifier = Modifier.fillMaxWidth(),
          verticalAlignment = Alignment.CenterVertically
        ) {
          Checkbox(
            checked = scheduleFollowUp,
            onCheckedChange = { scheduleFollowUp = it },
            colors = CheckboxDefaults.colors(checkedColor = IndigoPrimary)
          )
          Spacer(modifier = Modifier.width(6.dp))
          Text(
            text = "Schedule a follow-up reminder in Reminders",
            color = Slate700,
            fontSize = 12.sp,
            fontWeight = FontWeight.Medium
          )
        }

        if (scheduleFollowUp) {
          Row(
            modifier = Modifier
              .fillMaxWidth()
              .padding(start = 32.dp, top = 4.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
          ) {
            listOf(1, 3, 7, 14).forEach { days ->
              Box(
                modifier = Modifier
                  .clip(RoundedCornerShape(6.dp))
                  .background(if (followUpDays == days) IndigoPrimary else Slate50)
                  .border(1.dp, if (followUpDays == days) IndigoPrimary else Slate200, RoundedCornerShape(6.dp))
                  .clickable { followUpDays = days }
                  .padding(horizontal = 10.dp, vertical = 6.dp)
              ) {
                Text(
                  text = "in $days days",
                  color = if (followUpDays == days) Color.White else Slate700,
                  fontSize = 11.sp,
                  fontWeight = FontWeight.SemiBold
                )
              }
            }
          }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Save & Cancel buttons
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.End
        ) {
          OutlinedButton(
            onClick = onDismiss,
            shape = RoundedCornerShape(8.dp),
            modifier = Modifier.testTag("interaction_cancel_btn")
          ) {
            Text("Cancel", color = Slate700, fontSize = 13.sp)
          }

          Spacer(modifier = Modifier.width(10.dp))

          Button(
            onClick = {
              if (summary.isBlank()) {
                summaryError = true
                return@Button
              }
              onSave(
                selectedCustomerId,
                selectedType,
                dateMillis,
                summary.trim(),
                description.trim(),
                if (scheduleFollowUp) followUpDays else null
              )
            },
            colors = ButtonDefaults.buttonColors(containerColor = IndigoPrimary),
            shape = RoundedCornerShape(8.dp),
            modifier = Modifier.testTag("interaction_save_btn")
          ) {
            Text("Save Interaction", color = Color.White, fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
          }
        }
      }
    }
  }
}

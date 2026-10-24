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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Subject
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddReminderDialog(
  initialCustomerId: Long?,
  customers: List<Customer>,
  onDismiss: () -> Unit,
  onSave: (
    customerId: Long,
    title: String,
    dueDateMillis: Long,
    note: String
  ) -> Unit
) {
  var selectedCustomerId by remember {
    mutableStateOf(initialCustomerId ?: customers.firstOrNull()?.id ?: 1L)
  }
  var title by remember { mutableStateOf("") }
  var note by remember { mutableStateOf("") }
  val now = System.currentTimeMillis()
  val dayMillis = 86_400_000L
  var dueDateMillis by remember { mutableLongStateOf(now + dayMillis) } // Default tomorrow

  var dropdownExpanded by remember { mutableStateOf(false) }
  var titleError by remember { mutableStateOf(false) }

  val currentCustomer = customers.find { it.id == selectedCustomerId }

  Dialog(onDismissRequest = onDismiss) {
    Card(
      modifier = Modifier
        .fillMaxWidth()
        .widthIn(max = 480.dp)
        .testTag("add_reminder_dialog"),
      shape = RoundedCornerShape(16.dp),
      colors = CardDefaults.cardColors(containerColor = Color.White),
      border = androidx.compose.foundation.BorderStroke(1.dp, Slate200)
    ) {
      Column(
        modifier = Modifier.padding(24.dp)
      ) {
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Text(
            text = "Create Follow-up Reminder",
            color = Slate900,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold
          )
          IconButton(onClick = onDismiss) {
            Icon(Icons.Default.Close, contentDescription = "Close", tint = Slate500)
          }
        }

        HorizontalDivider(color = Slate200, modifier = Modifier.padding(vertical = 12.dp))

        // Customer
        Text("Customer Account *", color = Slate700, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
        Spacer(modifier = Modifier.height(4.dp))
        ExposedDropdownMenuBox(
          expanded = dropdownExpanded,
          onExpandedChange = { dropdownExpanded = !dropdownExpanded }
        ) {
          OutlinedTextField(
            value = currentCustomer?.let { "${it.name} (${it.company})" } ?: "Select Customer",
            onValueChange = {},
            readOnly = true,
            leadingIcon = { Icon(Icons.Default.Person, contentDescription = null, tint = IndigoPrimary, modifier = Modifier.size(18.dp)) },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = dropdownExpanded) },
            modifier = Modifier
              .fillMaxWidth()
              .menuAnchor(MenuAnchorType.PrimaryNotEditable),
            shape = RoundedCornerShape(8.dp),
            colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = IndigoPrimary, unfocusedBorderColor = Slate200)
          )

          ExposedDropdownMenu(
            expanded = dropdownExpanded,
            onDismissRequest = { dropdownExpanded = false }
          ) {
            customers.forEach { cust ->
              DropdownMenuItem(
                text = { Text("${cust.name} - ${cust.company}") },
                onClick = {
                  selectedCustomerId = cust.id
                  dropdownExpanded = false
                }
              )
            }
          }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Title
        Text("Follow-up Subject / Action *", color = Slate700, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
        Spacer(modifier = Modifier.height(4.dp))
        OutlinedTextField(
          value = title,
          onValueChange = {
            title = it
            if (it.isNotBlank()) titleError = false
          },
          placeholder = { Text("e.g. Call to finalize Enterprise SLA agreement", fontSize = 13.sp) },
          leadingIcon = { Icon(Icons.Default.Subject, contentDescription = null, tint = IndigoPrimary, modifier = Modifier.size(18.dp)) },
          isError = titleError,
          singleLine = true,
          modifier = Modifier
            .fillMaxWidth()
            .testTag("input_reminder_title"),
          shape = RoundedCornerShape(8.dp),
          colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = IndigoPrimary, unfocusedBorderColor = Slate200)
        )
        if (titleError) {
          Text("Title is required", color = Color(0xFFEF4444), fontSize = 11.sp, modifier = Modifier.padding(top = 2.dp))
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Due date presets
        Text("Due Date", color = Slate700, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
        Spacer(modifier = Modifier.height(6.dp))
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
          val presets = listOf(
            Pair("Today", now + (4 * 3600_000L)),
            Pair("Tomorrow", now + dayMillis),
            Pair("In 3 Days", now + (3 * dayMillis)),
            Pair("In 1 Week", now + (7 * dayMillis))
          )

          presets.forEach { (label, time) ->
            val isSelected = Math.abs(dueDateMillis - time) < 3600_000L
            Box(
              modifier = Modifier
                .weight(1f)
                .clip(RoundedCornerShape(6.dp))
                .background(if (isSelected) IndigoContainer else Slate50)
                .border(1.dp, if (isSelected) IndigoPrimary else Slate200, RoundedCornerShape(6.dp))
                .clickable { dueDateMillis = time }
                .padding(vertical = 8.dp),
              contentAlignment = Alignment.Center
            ) {
              Text(
                text = label,
                color = if (isSelected) IndigoPrimary else Slate700,
                fontSize = 11.sp,
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
              )
            }
          }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Additional Note
        Text("Additional Context", color = Slate700, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
        Spacer(modifier = Modifier.height(4.dp))
        OutlinedTextField(
          value = note,
          onValueChange = { note = it },
          placeholder = { Text("e.g. Check if legal council sent revisions...", fontSize = 13.sp) },
          minLines = 2,
          maxLines = 4,
          modifier = Modifier.fillMaxWidth(),
          shape = RoundedCornerShape(8.dp),
          colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = IndigoPrimary, unfocusedBorderColor = Slate200)
        )

        Spacer(modifier = Modifier.height(20.dp))

        // Buttons
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.End
        ) {
          OutlinedButton(onClick = onDismiss, shape = RoundedCornerShape(8.dp)) {
            Text("Cancel", color = Slate700, fontSize = 13.sp)
          }

          Spacer(modifier = Modifier.width(10.dp))

          Button(
            onClick = {
              if (title.isBlank()) {
                titleError = true
                return@Button
              }
              onSave(selectedCustomerId, title.trim(), dueDateMillis, note.trim())
            },
            colors = ButtonDefaults.buttonColors(containerColor = IndigoPrimary),
            shape = RoundedCornerShape(8.dp),
            modifier = Modifier.testTag("save_reminder_btn")
          ) {
            Text("Set Reminder", color = Color.White, fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
          }
        }
      }
    }
  }
}

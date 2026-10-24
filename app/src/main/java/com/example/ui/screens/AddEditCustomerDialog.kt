package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Business
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Notes
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Tag
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.data.model.Customer
import com.example.ui.components.StatusBadge
import com.example.ui.theme.IndigoContainer
import com.example.ui.theme.IndigoPrimary
import com.example.ui.theme.Slate200
import com.example.ui.theme.Slate50
import com.example.ui.theme.Slate500
import com.example.ui.theme.Slate700
import com.example.ui.theme.Slate900

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun AddEditCustomerDialog(
  customer: Customer?,
  onDismiss: () -> Unit,
  onSave: (
    id: Long,
    name: String,
    company: String,
    phone: String,
    email: String,
    address: String,
    tags: String,
    notes: String,
    status: String
  ) -> Unit
) {
  var name by remember { mutableStateOf(customer?.name ?: "") }
  var company by remember { mutableStateOf(customer?.company ?: "") }
  var phone by remember { mutableStateOf(customer?.phone ?: "") }
  var email by remember { mutableStateOf(customer?.email ?: "") }
  var address by remember { mutableStateOf(customer?.address ?: "") }
  var tags by remember { mutableStateOf(customer?.tags ?: "") }
  var notes by remember { mutableStateOf(customer?.notes ?: "") }
  var status by remember { mutableStateOf(customer?.status ?: "Active") }

  var nameError by remember { mutableStateOf(false) }
  var companyError by remember { mutableStateOf(false) }

  val presetTags = listOf("Enterprise", "SMB", "Tech", "SaaS", "VIP", "Retail", "Healthcare", "Finance")

  Dialog(onDismissRequest = onDismiss) {
    Card(
      modifier = Modifier
        .fillMaxWidth()
        .widthIn(max = 520.dp)
        .testTag("add_edit_customer_dialog"),
      shape = RoundedCornerShape(16.dp),
      colors = CardDefaults.cardColors(containerColor = Color.White),
      border = androidx.compose.foundation.BorderStroke(1.dp, Slate200)
    ) {
      Column(
        modifier = Modifier
          .padding(24.dp)
          .verticalScroll(rememberScrollState())
      ) {
        // Header
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Text(
            text = if (customer == null) "Add New Customer" else "Edit Customer Profile",
            color = Slate900,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold
          )
          IconButton(
            onClick = onDismiss,
            modifier = Modifier.testTag("dialog_close_btn")
          ) {
            Icon(Icons.Default.Close, contentDescription = "Close", tint = Slate500)
          }
        }

        HorizontalDivider(color = Slate200, modifier = Modifier.padding(vertical = 12.dp))

        // Full Name
        Text("Customer Name *", color = Slate700, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
        Spacer(modifier = Modifier.height(4.dp))
        OutlinedTextField(
          value = name,
          onValueChange = {
            name = it
            if (it.isNotBlank()) nameError = false
          },
          placeholder = { Text("e.g. Elena Rostova", fontSize = 13.sp) },
          leadingIcon = { Icon(Icons.Default.Person, contentDescription = null, tint = IndigoPrimary, modifier = Modifier.size(18.dp)) },
          isError = nameError,
          singleLine = true,
          modifier = Modifier
            .fillMaxWidth()
            .testTag("input_customer_name"),
          shape = RoundedCornerShape(8.dp),
          colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = IndigoPrimary, unfocusedBorderColor = Slate200)
        )
        if (nameError) {
          Text("Name is required", color = Color(0xFFEF4444), fontSize = 11.sp, modifier = Modifier.padding(top = 2.dp))
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Company
        Text("Company / Organization *", color = Slate700, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
        Spacer(modifier = Modifier.height(4.dp))
        OutlinedTextField(
          value = company,
          onValueChange = {
            company = it
            if (it.isNotBlank()) companyError = false
          },
          placeholder = { Text("e.g. Solstice Creative Agency", fontSize = 13.sp) },
          leadingIcon = { Icon(Icons.Default.Business, contentDescription = null, tint = IndigoPrimary, modifier = Modifier.size(18.dp)) },
          isError = companyError,
          singleLine = true,
          modifier = Modifier
            .fillMaxWidth()
            .testTag("input_customer_company"),
          shape = RoundedCornerShape(8.dp),
          colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = IndigoPrimary, unfocusedBorderColor = Slate200)
        )
        if (companyError) {
          Text("Company is required", color = Color(0xFFEF4444), fontSize = 11.sp, modifier = Modifier.padding(top = 2.dp))
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Phone & Email Row
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
          Column(modifier = Modifier.weight(1f)) {
            Text("Phone", color = Slate700, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
            Spacer(modifier = Modifier.height(4.dp))
            OutlinedTextField(
              value = phone,
              onValueChange = { phone = it },
              placeholder = { Text("+1 (555) 000-0000", fontSize = 13.sp) },
              leadingIcon = { Icon(Icons.Default.Phone, contentDescription = null, tint = IndigoPrimary, modifier = Modifier.size(18.dp)) },
              singleLine = true,
              keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
              modifier = Modifier
                .fillMaxWidth()
                .testTag("input_customer_phone"),
              shape = RoundedCornerShape(8.dp),
              colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = IndigoPrimary, unfocusedBorderColor = Slate200)
            )
          }

          Column(modifier = Modifier.weight(1f)) {
            Text("Email", color = Slate700, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
            Spacer(modifier = Modifier.height(4.dp))
            OutlinedTextField(
              value = email,
              onValueChange = { email = it },
              placeholder = { Text("contact@company.com", fontSize = 13.sp) },
              leadingIcon = { Icon(Icons.Default.Email, contentDescription = null, tint = IndigoPrimary, modifier = Modifier.size(18.dp)) },
              singleLine = true,
              keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
              modifier = Modifier
                .fillMaxWidth()
                .testTag("input_customer_email"),
              shape = RoundedCornerShape(8.dp),
              colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = IndigoPrimary, unfocusedBorderColor = Slate200)
            )
          }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Address
        Text("Office / Billing Address", color = Slate700, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
        Spacer(modifier = Modifier.height(4.dp))
        OutlinedTextField(
          value = address,
          onValueChange = { address = it },
          placeholder = { Text("e.g. 100 Montgomery St, Suite 1400, San Francisco, CA", fontSize = 13.sp) },
          leadingIcon = { Icon(Icons.Default.LocationOn, contentDescription = null, tint = IndigoPrimary, modifier = Modifier.size(18.dp)) },
          singleLine = true,
          modifier = Modifier
            .fillMaxWidth()
            .testTag("input_customer_address"),
          shape = RoundedCornerShape(8.dp),
          colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = IndigoPrimary, unfocusedBorderColor = Slate200)
        )

        Spacer(modifier = Modifier.height(12.dp))

        // Status Selection
        Text("Account Status", color = Slate700, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
        Spacer(modifier = Modifier.height(6.dp))
        Row(
          modifier = Modifier
            .fillMaxWidth()
            .horizontalScroll(rememberScrollState()),
          horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
          listOf("Active", "Lead", "Prospect", "VIP", "Inactive").forEach { opt ->
            val isSelected = status.equals(opt, ignoreCase = true)
            Box(
              modifier = Modifier
                .clip(RoundedCornerShape(8.dp))
                .background(if (isSelected) IndigoContainer else Slate50)
                .border(
                  width = 1.dp,
                  color = if (isSelected) IndigoPrimary else Slate200,
                  shape = RoundedCornerShape(8.dp)
                )
                .clickable { status = opt }
                .padding(horizontal = 12.dp, vertical = 8.dp)
            ) {
              StatusBadge(status = opt)
            }
          }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Tags & Segments
        Text("Tags & Segments (comma-separated)", color = Slate700, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
        Spacer(modifier = Modifier.height(4.dp))
        OutlinedTextField(
          value = tags,
          onValueChange = { tags = it },
          placeholder = { Text("e.g. Enterprise, SaaS, High-Value", fontSize = 13.sp) },
          leadingIcon = { Icon(Icons.Default.Tag, contentDescription = null, tint = IndigoPrimary, modifier = Modifier.size(18.dp)) },
          singleLine = true,
          modifier = Modifier
            .fillMaxWidth()
            .testTag("input_customer_tags"),
          shape = RoundedCornerShape(8.dp),
          colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = IndigoPrimary, unfocusedBorderColor = Slate200)
        )

        // Preset tag helpers
        Spacer(modifier = Modifier.height(6.dp))
        FlowRow(
          horizontalArrangement = Arrangement.spacedBy(6.dp),
          verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
          presetTags.forEach { preset ->
            val currentList = tags.split(",").map { it.trim() }
            val contains = currentList.any { it.equals(preset, ignoreCase = true) }
            Box(
              modifier = Modifier
                .clip(RoundedCornerShape(6.dp))
                .background(if (contains) IndigoContainer else Slate50)
                .border(1.dp, if (contains) IndigoPrimary else Slate200, RoundedCornerShape(6.dp))
                .clickable {
                  if (contains) {
                    tags = currentList.filterNot { it.equals(preset, ignoreCase = true) }.joinToString(", ")
                  } else {
                    tags = if (tags.isBlank()) preset else "$tags, $preset"
                  }
                }
                .padding(horizontal = 8.dp, vertical = 4.dp)
            ) {
              Text(
                text = "+ $preset",
                color = if (contains) IndigoPrimary else Slate700,
                fontSize = 11.sp,
                fontWeight = FontWeight.Medium
              )
            }
          }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Notes Field
        Text("Internal Notes / Overview", color = Slate700, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
        Spacer(modifier = Modifier.height(4.dp))
        OutlinedTextField(
          value = notes,
          onValueChange = { notes = it },
          placeholder = { Text("Add key customer context, contracts, preferences...", fontSize = 13.sp) },
          leadingIcon = { Icon(Icons.Default.Notes, contentDescription = null, tint = IndigoPrimary, modifier = Modifier.size(18.dp)) },
          minLines = 3,
          maxLines = 5,
          modifier = Modifier
            .fillMaxWidth()
            .testTag("input_customer_notes"),
          shape = RoundedCornerShape(8.dp),
          colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = IndigoPrimary, unfocusedBorderColor = Slate200)
        )

        Spacer(modifier = Modifier.height(20.dp))

        // Actions
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.End
        ) {
          OutlinedButton(
            onClick = onDismiss,
            shape = RoundedCornerShape(8.dp),
            modifier = Modifier.testTag("dialog_cancel_btn")
          ) {
            Text("Cancel", color = Slate700, fontSize = 13.sp)
          }

          Spacer(modifier = Modifier.width(10.dp))

          Button(
            onClick = {
              if (name.isBlank()) {
                nameError = true
                return@Button
              }
              if (company.isBlank()) {
                companyError = true
                return@Button
              }
              onSave(
                customer?.id ?: 0L,
                name.trim(),
                company.trim(),
                phone.trim(),
                email.trim(),
                address.trim(),
                tags.trim(),
                notes.trim(),
                status
              )
            },
            colors = ButtonDefaults.buttonColors(containerColor = IndigoPrimary),
            shape = RoundedCornerShape(8.dp),
            modifier = Modifier.testTag("dialog_save_btn")
          ) {
            Text("Save Customer", color = Color.White, fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
          }
        }
      }
    }
  }
}

package com.example.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.Customer
import com.example.data.model.Interaction
import com.example.ui.components.CustomerAvatar
import com.example.ui.components.StatusBadge
import com.example.ui.components.TagChip
import com.example.ui.theme.IndigoContainer
import com.example.ui.theme.IndigoPrimary
import com.example.ui.theme.Slate100
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

@Composable
fun CustomerListScreen(
  customers: List<Customer>,
  allCustomers: List<Customer>,
  interactions: List<Interaction>,
  selectedCustomerId: Long?,
  onCustomerSelect: (Long?) -> Unit,
  searchQuery: String,
  onSearchChange: (String) -> Unit,
  selectedSegment: String?,
  onSegmentSelect: (String?) -> Unit,
  selectedStatus: String?,
  onStatusSelect: (String?) -> Unit,
  onAddCustomerClick: () -> Unit,
  onEditCustomerClick: (Customer) -> Unit,
  onDeleteCustomerClick: (Customer) -> Unit,
  onAddInteractionClick: (Long) -> Unit,
  onDeleteInteractionClick: (Interaction) -> Unit,
  modifier: Modifier = Modifier
) {
  BoxWithConstraints(modifier = modifier.fillMaxSize()) {
    val isWideScreen = maxWidth >= 840.dp
    val selectedCustomer = allCustomers.find { it.id == selectedCustomerId } ?: customers.firstOrNull()

    // Compact screen sub-screen state
    var showMobileProfile by remember { mutableStateOf(false) }

    if (!isWideScreen && showMobileProfile && selectedCustomer != null) {
      BackHandler {
        showMobileProfile = false
      }
      CustomerProfilePane(
        customer = selectedCustomer,
        interactions = interactions,
        onAddInteractionClick = onAddInteractionClick,
        onEditCustomerClick = onEditCustomerClick,
        onDeleteCustomerClick = onDeleteCustomerClick,
        onDeleteInteractionClick = onDeleteInteractionClick,
        onBackClick = { showMobileProfile = false },
        modifier = Modifier.fillMaxSize()
      )
    } else {
      Row(modifier = Modifier.fillMaxSize()) {
        // Table / List Column
        Column(
          modifier = Modifier
            .weight(if (isWideScreen) 1f else 1f)
            .fillMaxHeight()
            .background(Slate50)
            .padding(16.dp)
        ) {
          // Top Controls: Search & Add Customer
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            OutlinedTextField(
              value = searchQuery,
              onValueChange = onSearchChange,
              placeholder = { Text("Search by name, company, email, tag...", fontSize = 13.sp) },
              leadingIcon = {
                Icon(Icons.Default.Search, contentDescription = "Search", tint = Slate400, modifier = Modifier.size(18.dp))
              },
              trailingIcon = {
                if (searchQuery.isNotEmpty()) {
                  IconButton(onClick = { onSearchChange("") }) {
                    Icon(Icons.Default.Clear, contentDescription = "Clear search", tint = Slate400, modifier = Modifier.size(16.dp))
                  }
                }
              },
              singleLine = true,
              modifier = Modifier
                .weight(1f)
                .height(48.dp)
                .testTag("customer_search_input"),
              shape = RoundedCornerShape(8.dp),
              colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = IndigoPrimary,
                unfocusedBorderColor = Slate200,
                focusedContainerColor = Color.White,
                unfocusedContainerColor = Color.White
              )
            )

            Spacer(modifier = Modifier.width(12.dp))

            Button(
              onClick = onAddCustomerClick,
              colors = ButtonDefaults.buttonColors(containerColor = IndigoPrimary),
              shape = RoundedCornerShape(8.dp),
              modifier = Modifier
                .height(48.dp)
                .testTag("table_add_customer_btn")
            ) {
              Icon(Icons.Default.Add, contentDescription = null, tint = Color.White, modifier = Modifier.size(18.dp))
              Spacer(modifier = Modifier.width(6.dp))
              Text("Add Customer", color = Color.White, fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
            }
          }

          Spacer(modifier = Modifier.height(10.dp))

          // Filter Row: Statuses & Tags
          Row(
            modifier = Modifier
              .fillMaxWidth()
              .horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(6.dp),
            verticalAlignment = Alignment.CenterVertically
          ) {
            FilterChip(
              label = "All Customers (${allCustomers.size})",
              isSelected = selectedSegment == null && selectedStatus == null,
              onClick = {
                onSegmentSelect(null)
                onStatusSelect(null)
              }
            )

            listOf("Active", "Lead", "Prospect", "VIP", "Inactive").forEach { status ->
              FilterChip(
                label = status,
                isSelected = selectedStatus == status,
                onClick = {
                  onStatusSelect(if (selectedStatus == status) null else status)
                }
              )
            }

            listOf("Enterprise", "SMB", "Tech", "SaaS", "Partner").forEach { segment ->
              FilterChip(
                label = "#$segment",
                isSelected = selectedSegment == segment,
                onClick = {
                  onSegmentSelect(if (selectedSegment == segment) null else segment)
                }
              )
            }
          }

          Spacer(modifier = Modifier.height(12.dp))

          // Table Header
          Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(topStart = 8.dp, topEnd = 8.dp, bottomStart = 0.dp, bottomEnd = 0.dp),
            colors = CardDefaults.cardColors(containerColor = Slate100),
            border = androidx.compose.foundation.BorderStroke(1.dp, Slate200)
          ) {
            Row(
              modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 10.dp),
              verticalAlignment = Alignment.CenterVertically
            ) {
              Text("CUSTOMER", color = Slate500, fontSize = 11.sp, fontWeight = FontWeight.Bold, modifier = Modifier.weight(1.4f))
              Text("COMPANY", color = Slate500, fontSize = 11.sp, fontWeight = FontWeight.Bold, modifier = Modifier.weight(1.1f))
              Text("TAGS / SEGMENT", color = Slate500, fontSize = 11.sp, fontWeight = FontWeight.Bold, modifier = Modifier.weight(1.2f))
              Text("LAST CONTACT", color = Slate500, fontSize = 11.sp, fontWeight = FontWeight.Bold, modifier = Modifier.weight(0.9f))
              Text("STATUS", color = Slate500, fontSize = 11.sp, fontWeight = FontWeight.Bold, modifier = Modifier.weight(0.8f))
            }
          }

          // Table Rows / Empty State
          if (customers.isEmpty()) {
            Card(
              modifier = Modifier.fillMaxWidth(),
              shape = RoundedCornerShape(topStart = 0.dp, topEnd = 0.dp, bottomStart = 8.dp, bottomEnd = 8.dp),
              colors = CardDefaults.cardColors(containerColor = Color.White),
              border = androidx.compose.foundation.BorderStroke(1.dp, Slate200)
            ) {
              Column(
                modifier = Modifier
                  .fillMaxWidth()
                  .padding(40.dp),
                horizontalAlignment = Alignment.CenterHorizontally
              ) {
                Icon(Icons.Default.People, contentDescription = null, tint = Slate400, modifier = Modifier.size(44.dp))
                Spacer(modifier = Modifier.height(10.dp))
                Text("No customers found", color = Slate700, fontSize = 15.sp, fontWeight = FontWeight.SemiBold)
                Text(
                  if (searchQuery.isNotEmpty() || selectedSegment != null || selectedStatus != null)
                    "Try clearing your search filters to view all client accounts."
                  else
                    "Click '+ Add Customer' to onboard your first business client.",
                  color = Slate500,
                  fontSize = 12.sp,
                  modifier = Modifier.padding(top = 4.dp)
                )
              }
            }
          } else {
            LazyColumn(
              modifier = Modifier
                .fillMaxWidth()
                .border(
                  width = 1.dp,
                  color = Slate200,
                  shape = RoundedCornerShape(bottomStart = 8.dp, bottomEnd = 8.dp)
                )
                .background(Color.White)
            ) {
              items(customers) { customer ->
                val isSelected = isWideScreen && customer.id == selectedCustomer?.id

                CustomerTableRow(
                  customer = customer,
                  isSelected = isSelected,
                  onClick = {
                    onCustomerSelect(customer.id)
                    if (!isWideScreen) {
                      showMobileProfile = true
                    }
                  },
                  onEditClick = { onEditCustomerClick(customer) },
                  onDeleteClick = { onDeleteCustomerClick(customer) }
                )
                HorizontalDivider(color = Slate200)
              }
            }
          }
        }

        // Desktop Right Pane: Customer Profile Card Hybrid
        if (isWideScreen) {
          Box(
            modifier = Modifier
              .width(380.dp)
              .fillMaxHeight()
              .border(1.dp, Slate200)
          ) {
            CustomerProfilePane(
              customer = selectedCustomer,
              interactions = interactions,
              onAddInteractionClick = onAddInteractionClick,
              onEditCustomerClick = onEditCustomerClick,
              onDeleteCustomerClick = onDeleteCustomerClick,
              onDeleteInteractionClick = onDeleteInteractionClick,
              onBackClick = null,
              modifier = Modifier.fillMaxSize()
            )
          }
        }
      }
    }
  }
}

@Composable
fun CustomerTableRow(
  customer: Customer,
  isSelected: Boolean,
  onClick: () -> Unit,
  onEditClick: () -> Unit,
  onDeleteClick: () -> Unit,
  modifier: Modifier = Modifier
) {
  val rowBg = if (isSelected) IndigoContainer.copy(alpha = 0.5f) else Color.White

  Row(
    modifier = modifier
      .fillMaxWidth()
      .background(rowBg)
      .clickable(onClick = onClick)
      .padding(horizontal = 16.dp, vertical = 12.dp)
      .testTag("customer_row_${customer.id}"),
    verticalAlignment = Alignment.CenterVertically
  ) {
    // Name + Avatar + Email
    Row(
      modifier = Modifier.weight(1.4f),
      verticalAlignment = Alignment.CenterVertically
    ) {
      CustomerAvatar(name = customer.name, initials = customer.getInitials(), size = 34.dp)
      Spacer(modifier = Modifier.width(10.dp))
      Column {
        Text(
          text = customer.name,
          color = Slate900,
          fontSize = 13.sp,
          fontWeight = FontWeight.Bold
        )
        Text(
          text = customer.email.ifBlank { "No email" },
          color = Slate500,
          fontSize = 11.sp
        )
      }
    }

    // Company
    Text(
      text = customer.company,
      color = Slate700,
      fontSize = 13.sp,
      fontWeight = FontWeight.Medium,
      modifier = Modifier.weight(1.1f)
    )

    // Tag / Segment
    Box(modifier = Modifier.weight(1.2f)) {
      val tags = customer.getTagList()
      if (tags.isNotEmpty()) {
        Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
          TagChip(tag = tags.first())
          if (tags.size > 1) {
            Text(
              text = "+${tags.size - 1}",
              color = Slate500,
              fontSize = 10.sp,
              fontWeight = FontWeight.SemiBold,
              modifier = Modifier.align(Alignment.CenterVertically)
            )
          }
        }
      } else {
        Text("—", color = Slate400, fontSize = 12.sp)
      }
    }

    // Last Contacted
    Text(
      text = CrmViewModel.formatShortDate(customer.lastContactedMillis),
      color = Slate500,
      fontSize = 12.sp,
      modifier = Modifier.weight(0.9f)
    )

    // Status
    Box(modifier = Modifier.weight(0.8f)) {
      StatusBadge(status = customer.status)
    }
  }
}

@Composable
fun FilterChip(
  label: String,
  isSelected: Boolean,
  onClick: () -> Unit,
  modifier: Modifier = Modifier
) {
  Box(
    modifier = modifier
      .clip(RoundedCornerShape(6.dp))
      .background(if (isSelected) IndigoPrimary else Color.White)
      .border(
        width = 1.dp,
        color = if (isSelected) IndigoPrimary else Slate200,
        shape = RoundedCornerShape(6.dp)
      )
      .clickable(onClick = onClick)
      .padding(horizontal = 10.dp, vertical = 6.dp)
  ) {
    Text(
      text = label,
      color = if (isSelected) Color.White else Slate700,
      fontSize = 12.sp,
      fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Medium
    )
  }
}

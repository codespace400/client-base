package com.example.ui.screens

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.EventNote
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.Videocam
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
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
import com.example.ui.theme.Slate200
import com.example.ui.theme.Slate400
import com.example.ui.theme.Slate500
import com.example.ui.theme.Slate600
import com.example.ui.theme.Slate700
import com.example.ui.theme.Slate900
import com.example.ui.theme.TealAccent
import com.example.ui.theme.TealContainer
import com.example.viewmodel.CrmViewModel

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun CustomerProfilePane(
  customer: Customer?,
  interactions: List<Interaction>,
  onAddInteractionClick: (Long) -> Unit,
  onEditCustomerClick: (Customer) -> Unit,
  onDeleteCustomerClick: (Customer) -> Unit,
  onDeleteInteractionClick: (Interaction) -> Unit,
  onBackClick: (() -> Unit)? = null,
  modifier: Modifier = Modifier
) {
  val context = LocalContext.current

  if (customer == null) {
    Box(
      modifier = modifier
        .fillMaxSize()
        .background(Color.White)
        .padding(24.dp),
      contentAlignment = Alignment.Center
    ) {
      Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Icon(
          imageVector = Icons.Default.EventNote,
          contentDescription = null,
          tint = Slate400,
          modifier = Modifier.size(48.dp)
        )
        Spacer(modifier = Modifier.height(12.dp))
        Text(
          text = "No Customer Selected",
          color = Slate700,
          fontSize = 16.sp,
          fontWeight = FontWeight.SemiBold
        )
        Text(
          text = "Select a customer from the table or list to inspect their profile & timeline.",
          color = Slate500,
          fontSize = 12.sp,
          modifier = Modifier.padding(top = 4.dp)
        )
      }
    }
    return
  }

  val customerInteractions = interactions
    .filter { it.customerId == customer.id }
    .sortedByDescending { it.dateMillis }

  LazyColumn(
    modifier = modifier
      .fillMaxSize()
      .background(Color.White)
      .padding(20.dp),
    verticalArrangement = Arrangement.spacedBy(16.dp)
  ) {
    // Header & Quick Navigation if on compact
    item {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
          if (onBackClick != null) {
            IconButton(
              onClick = onBackClick,
              modifier = Modifier
                .padding(end = 8.dp)
                .testTag("profile_back_btn")
            ) {
              Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = Slate700)
            }
          }
          Text(
            text = "Customer Profile",
            color = Slate900,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold
          )
        }

        Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
          IconButton(
            onClick = { onEditCustomerClick(customer) },
            modifier = Modifier.testTag("profile_edit_btn")
          ) {
            Icon(Icons.Default.Edit, contentDescription = "Edit Customer", tint = IndigoPrimary, modifier = Modifier.size(20.dp))
          }
          IconButton(
            onClick = { onDeleteCustomerClick(customer) },
            modifier = Modifier.testTag("profile_delete_btn")
          ) {
            Icon(Icons.Default.Delete, contentDescription = "Delete Customer", tint = Color(0xFFEF4444), modifier = Modifier.size(20.dp))
          }
        }
      }
    }

    // Customer Identity Header Card
    item {
      Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFF8FAFC)),
        border = androidx.compose.foundation.BorderStroke(1.dp, Slate200)
      ) {
        Column(
          modifier = Modifier
            .fillMaxWidth()
            .padding(18.dp)
        ) {
          Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
          ) {
            CustomerAvatar(
              name = customer.name,
              initials = customer.getInitials(),
              size = 54.dp
            )
            Spacer(modifier = Modifier.width(14.dp))
            Column(modifier = Modifier.weight(1f)) {
              Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
              ) {
                Text(
                  text = customer.name,
                  color = Slate900,
                  fontSize = 18.sp,
                  fontWeight = FontWeight.Bold
                )
                StatusBadge(status = customer.status)
              }
              Text(
                text = customer.company,
                color = Slate600,
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                modifier = Modifier.padding(top = 2.dp)
              )
            }
          }

          Spacer(modifier = Modifier.height(14.dp))

          // Contact details
          if (customer.email.isNotBlank()) {
            Row(
              verticalAlignment = Alignment.CenterVertically,
              modifier = Modifier.padding(vertical = 3.dp)
            ) {
              Icon(Icons.Default.Email, contentDescription = null, tint = Slate400, modifier = Modifier.size(16.dp))
              Spacer(modifier = Modifier.width(8.dp))
              Text(text = customer.email, color = Slate700, fontSize = 13.sp)
            }
          }

          if (customer.phone.isNotBlank()) {
            Row(
              verticalAlignment = Alignment.CenterVertically,
              modifier = Modifier.padding(vertical = 3.dp)
            ) {
              Icon(Icons.Default.Phone, contentDescription = null, tint = Slate400, modifier = Modifier.size(16.dp))
              Spacer(modifier = Modifier.width(8.dp))
              Text(text = customer.phone, color = Slate700, fontSize = 13.sp)
            }
          }

          if (customer.address.isNotBlank()) {
            Row(
              verticalAlignment = Alignment.CenterVertically,
              modifier = Modifier.padding(vertical = 3.dp)
            ) {
              Icon(Icons.Default.LocationOn, contentDescription = null, tint = Slate400, modifier = Modifier.size(16.dp))
              Spacer(modifier = Modifier.width(8.dp))
              Text(text = customer.address, color = Slate500, fontSize = 12.sp)
            }
          }

          // Tags row
          val tagList = customer.getTagList()
          if (tagList.isNotEmpty()) {
            Spacer(modifier = Modifier.height(10.dp))
            FlowRow(
              horizontalArrangement = Arrangement.spacedBy(6.dp),
              verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
              tagList.forEach { tag ->
                TagChip(tag = tag)
              }
            }
          }

          // Notes
          if (customer.notes.isNotBlank()) {
            Spacer(modifier = Modifier.height(12.dp))
            Box(
              modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(8.dp))
                .background(Color.White)
                .border(1.dp, Slate200, RoundedCornerShape(8.dp))
                .padding(10.dp)
            ) {
              Text(
                text = customer.notes,
                color = Slate600,
                fontSize = 12.sp,
                lineHeight = 16.sp
              )
            }
          }

          Spacer(modifier = Modifier.height(16.dp))

          // Quick Actions (Call, Email, Add Interaction)
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
          ) {
            OutlinedButton(
              onClick = { launchPhoneCall(context, customer.phone) },
              modifier = Modifier
                .weight(1f)
                .testTag("action_call_btn"),
              shape = RoundedCornerShape(8.dp)
            ) {
              Icon(Icons.Default.Call, contentDescription = null, tint = IndigoPrimary, modifier = Modifier.size(16.dp))
              Spacer(modifier = Modifier.width(6.dp))
              Text("Call", color = IndigoPrimary, fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
            }

            OutlinedButton(
              onClick = { launchEmail(context, customer.email, customer.name) },
              modifier = Modifier
                .weight(1f)
                .testTag("action_email_btn"),
              shape = RoundedCornerShape(8.dp)
            ) {
              Icon(Icons.Default.Email, contentDescription = null, tint = IndigoPrimary, modifier = Modifier.size(16.dp))
              Spacer(modifier = Modifier.width(6.dp))
              Text("Email", color = IndigoPrimary, fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
            }
          }
        }
      }
    }

    // Timeline Header + Add Interaction Button
    item {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Column {
          Text(
            text = "Interaction Timeline",
            color = Slate900,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold
          )
          Text(
            text = "Chronological record of calls, meetings & notes",
            color = Slate500,
            fontSize = 12.sp
          )
        }

        Button(
          onClick = { onAddInteractionClick(customer.id) },
          colors = ButtonDefaults.buttonColors(containerColor = IndigoPrimary),
          shape = RoundedCornerShape(8.dp),
          modifier = Modifier.testTag("timeline_add_interaction_btn")
        ) {
          Icon(Icons.Default.Add, contentDescription = null, tint = Color.White, modifier = Modifier.size(16.dp))
          Spacer(modifier = Modifier.width(4.dp))
          Text("Add Interaction", color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
        }
      }
    }

    // Timeline Items
    if (customerInteractions.isEmpty()) {
      item {
        Card(
          modifier = Modifier.fillMaxWidth(),
          shape = RoundedCornerShape(12.dp),
          colors = CardDefaults.cardColors(containerColor = Color(0xFFF8FAFC)),
          border = androidx.compose.foundation.BorderStroke(1.dp, Slate200)
        ) {
          Column(
            modifier = Modifier
              .fillMaxWidth()
              .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
          ) {
            Icon(Icons.Default.Schedule, contentDescription = null, tint = Slate400, modifier = Modifier.size(36.dp))
            Spacer(modifier = Modifier.height(8.dp))
            Text(
              text = "No recorded interactions",
              color = Slate700,
              fontSize = 14.sp,
              fontWeight = FontWeight.SemiBold
            )
            Text(
              text = "Click '+ Add Interaction' to log calls, notes, or meetings with ${customer.name}.",
              color = Slate500,
              fontSize = 12.sp,
              modifier = Modifier.padding(top = 2.dp)
            )
          }
        }
      }
    } else {
      items(customerInteractions) { item ->
        TimelineItemCard(
          interaction = item,
          onDeleteClick = { onDeleteInteractionClick(item) }
        )
      }
    }
  }
}

@Composable
fun TimelineItemCard(
  interaction: Interaction,
  onDeleteClick: () -> Unit,
  modifier: Modifier = Modifier
) {
  val (icon, color, bg) = when (interaction.type.lowercase()) {
    "call" -> Triple(Icons.Default.Call, IndigoPrimary, IndigoContainer)
    "meeting" -> Triple(Icons.Default.Videocam, TealAccent, TealContainer)
    "email" -> Triple(Icons.Default.Email, Color(0xFF0284C7), Color(0xFFE0F2FE))
    else -> Triple(Icons.Default.EventNote, Color(0xFF8B5CF6), Color(0xFFF3E8FF))
  }

  Card(
    modifier = modifier.fillMaxWidth(),
    shape = RoundedCornerShape(10.dp),
    colors = CardDefaults.cardColors(containerColor = Color.White),
    border = androidx.compose.foundation.BorderStroke(1.dp, Slate200)
  ) {
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .padding(14.dp),
      verticalAlignment = Alignment.Top
    ) {
      Box(
        modifier = Modifier
          .size(36.dp)
          .clip(RoundedCornerShape(8.dp))
          .background(bg),
        contentAlignment = Alignment.Center
      ) {
        Icon(imageVector = icon, contentDescription = interaction.type, tint = color, modifier = Modifier.size(18.dp))
      }

      Spacer(modifier = Modifier.width(12.dp))

      Column(modifier = Modifier.weight(1f)) {
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
              modifier = Modifier
                .clip(RoundedCornerShape(4.dp))
                .background(bg)
                .padding(horizontal = 6.dp, vertical = 2.dp)
            ) {
              Text(text = interaction.type.uppercase(), color = color, fontSize = 10.sp, fontWeight = FontWeight.Bold)
            }
            Text(
              text = CrmViewModel.formatDateTime(interaction.dateMillis),
              color = Slate400,
              fontSize = 11.sp,
              modifier = Modifier.padding(start = 8.dp)
            )
          }

          IconButton(
            onClick = onDeleteClick,
            modifier = Modifier.size(24.dp)
          ) {
            Icon(Icons.Default.Delete, contentDescription = "Delete entry", tint = Slate400, modifier = Modifier.size(14.dp))
          }
        }

        Text(
          text = interaction.summary,
          color = Slate900,
          fontSize = 13.sp,
          fontWeight = FontWeight.SemiBold,
          modifier = Modifier.padding(top = 4.dp)
        )

        if (interaction.description.isNotBlank()) {
          Text(
            text = interaction.description,
            color = Slate600,
            fontSize = 12.sp,
            lineHeight = 16.sp,
            modifier = Modifier.padding(top = 3.dp)
          )
        }
      }
    }
  }
}

private fun launchPhoneCall(context: Context, phone: String) {
  if (phone.isBlank()) {
    Toast.makeText(context, "No phone number available", Toast.LENGTH_SHORT).show()
    return
  }
  try {
    val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:${phone.trim()}"))
    context.startActivity(intent)
  } catch (e: Exception) {
    Toast.makeText(context, "Could not open dialer: ${e.message}", Toast.LENGTH_SHORT).show()
  }
}

private fun launchEmail(context: Context, email: String, name: String) {
  if (email.isBlank()) {
    Toast.makeText(context, "No email address available", Toast.LENGTH_SHORT).show()
    return
  }
  try {
    val intent = Intent(Intent.ACTION_SENDTO).apply {
      data = Uri.parse("mailto:$email")
      putExtra(Intent.EXTRA_SUBJECT, "ClientBase follow-up: $name")
    }
    context.startActivity(intent)
  } catch (e: Exception) {
    Toast.makeText(context, "Could not open mail app: ${e.message}", Toast.LENGTH_SHORT).show()
  }
}

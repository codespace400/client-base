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
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Business
import androidx.compose.material.icons.filled.Category
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.LocalHospital
import androidx.compose.material.icons.filled.MonetizationOn
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.VpnKey
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
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
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
import com.example.viewmodel.SegmentCount

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun SegmentsScreen(
  segments: List<SegmentCount>,
  totalCustomersCount: Int,
  onSegmentClick: (String) -> Unit,
  onAddSegmentClick: () -> Unit,
  modifier: Modifier = Modifier
) {
  var searchQuery by remember { mutableStateOf("") }

  val filteredSegments = segments.filter {
    searchQuery.isBlank() || it.tag.contains(searchQuery, ignoreCase = true)
  }

  LazyColumn(
    modifier = modifier
      .fillMaxSize()
      .background(Slate50)
      .padding(20.dp),
    verticalArrangement = Arrangement.spacedBy(18.dp)
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
            text = "Customer Segments & Tags",
            color = Slate900,
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = (-0.4).sp
          )
          Text(
            text = "Organize customer cohorts, industries, and value tiers. Tap any segment to filter.",
            color = Slate500,
            fontSize = 13.sp,
            modifier = Modifier.padding(top = 2.dp)
          )
        }

        Button(
          onClick = onAddSegmentClick,
          colors = ButtonDefaults.buttonColors(containerColor = IndigoPrimary),
          shape = RoundedCornerShape(8.dp),
          modifier = Modifier.testTag("segments_add_customer_btn")
        ) {
          Icon(Icons.Default.Add, contentDescription = null, tint = Color.White, modifier = Modifier.size(16.dp))
          Spacer(modifier = Modifier.width(6.dp))
          Text("New Client / Tag", color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
        }
      }
    }

    // Search bar
    item {
      OutlinedTextField(
        value = searchQuery,
        onValueChange = { searchQuery = it },
        placeholder = { Text("Search segments & tags (e.g. Enterprise, Tech, VIP)...", fontSize = 13.sp) },
        leadingIcon = {
          Icon(Icons.Default.Search, contentDescription = null, tint = Slate400, modifier = Modifier.size(18.dp))
        },
        singleLine = true,
        modifier = Modifier
          .fillMaxWidth()
          .height(48.dp)
          .testTag("segment_search_input"),
        shape = RoundedCornerShape(8.dp),
        colors = OutlinedTextFieldDefaults.colors(
          focusedBorderColor = IndigoPrimary,
          unfocusedBorderColor = Slate200,
          focusedContainerColor = Color.White,
          unfocusedContainerColor = Color.White
        )
      )
    }

    // Grid of Segments
    if (filteredSegments.isEmpty()) {
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
            Icon(Icons.Default.Category, contentDescription = null, tint = Slate400, modifier = Modifier.size(44.dp))
            Spacer(modifier = Modifier.height(10.dp))
            Text("No segments found", color = Slate700, fontSize = 15.sp, fontWeight = FontWeight.SemiBold)
            Text("Add tags to your customers in their profile or customer editor.", color = Slate500, fontSize = 12.sp)
          }
        }
      }
    } else {
      item {
        FlowRow(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.spacedBy(16.dp),
          verticalArrangement = Arrangement.spacedBy(16.dp),
          maxItemsInEachRow = 3
        ) {
          filteredSegments.forEach { seg ->
            SegmentCard(
              segment = seg,
              totalCustomersCount = totalCustomersCount,
              onClick = { onSegmentClick(seg.tag) },
              modifier = Modifier.weight(1f)
            )
          }
        }
      }
    }
  }
}

@Composable
fun SegmentCard(
  segment: SegmentCount,
  totalCustomersCount: Int,
  onClick: () -> Unit,
  modifier: Modifier = Modifier
) {
  val isPositive = segment.tag.equals("Enterprise", ignoreCase = true) ||
    segment.tag.equals("VIP", ignoreCase = true) ||
    segment.tag.equals("High-Value", ignoreCase = true) ||
    segment.tag.equals("Partner", ignoreCase = true)

  val (icon, iconTint, iconBg) = when (segment.tag.lowercase()) {
    "enterprise" -> Triple(Icons.Default.Business, IndigoPrimary, IndigoContainer)
    "vip" -> Triple(Icons.Default.Star, Color(0xFF8B5CF6), Color(0xFFF3E8FF))
    "tech", "saas" -> Triple(Icons.Default.VpnKey, Color(0xFF0284C7), Color(0xFFE0F2FE))
    "healthcare" -> Triple(Icons.Default.LocalHospital, Color(0xFFE11D48), Color(0xFFFFE4E6))
    "finance" -> Triple(Icons.Default.MonetizationOn, Color(0xFF059669), Color(0xFFD1FAE5))
    "retail" -> Triple(Icons.Default.ShoppingCart, Color(0xFFD97706), Color(0xFFFEF3C7))
    else -> Triple(Icons.Default.Category, TealAccent, TealContainer)
  }

  val progress = if (totalCustomersCount > 0) segment.count.toFloat() / totalCustomersCount else 0f

  Card(
    modifier = modifier
      .clip(RoundedCornerShape(12.dp))
      .border(1.dp, Slate200, RoundedCornerShape(12.dp))
      .clickable(onClick = onClick)
      .testTag("segment_card_${segment.tag.lowercase()}"),
    shape = RoundedCornerShape(12.dp),
    colors = CardDefaults.cardColors(containerColor = Color.White),
    elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
  ) {
    Column(
      modifier = Modifier
        .fillMaxWidth()
        .padding(18.dp)
    ) {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Box(
          modifier = Modifier
            .size(42.dp)
            .clip(RoundedCornerShape(10.dp))
            .background(iconBg),
          contentAlignment = Alignment.Center
        ) {
          Icon(imageVector = icon, contentDescription = null, tint = iconTint, modifier = Modifier.size(22.dp))
        }

        Box(
          modifier = Modifier
            .clip(RoundedCornerShape(12.dp))
            .background(if (isPositive) TealContainer else Slate100())
            .padding(horizontal = 8.dp, vertical = 3.dp)
        ) {
          Text(
            text = if (segment.count == 1) "1 client" else "${segment.count} clients",
            color = if (isPositive) TealAccent else Slate600,
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold
          )
        }
      }

      Spacer(modifier = Modifier.height(14.dp))

      Text(
        text = segment.tag,
        color = Slate900,
        fontSize = 16.sp,
        fontWeight = FontWeight.Bold
      )

      Text(
        text = "${segment.activeCount} active / high engagement accounts",
        color = Slate500,
        fontSize = 12.sp,
        modifier = Modifier.padding(top = 2.dp, bottom = 12.dp)
      )

      LinearProgressIndicator(
        progress = { progress },
        modifier = Modifier
          .fillMaxWidth()
          .height(6.dp)
          .clip(RoundedCornerShape(3.dp)),
        color = if (isPositive) TealAccent else IndigoPrimary,
        trackColor = Slate200,
        strokeCap = StrokeCap.Round
      )

      Spacer(modifier = Modifier.height(14.dp))

      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Text(
          text = "${(progress * 100).toInt()}% of client base",
          color = Slate500,
          fontSize = 11.sp
        )

        Row(
          verticalAlignment = Alignment.CenterVertically,
          modifier = Modifier.clickable(onClick = onClick)
        ) {
          Text(
            text = "Filter table",
            color = IndigoPrimary,
            fontSize = 12.sp,
            fontWeight = FontWeight.SemiBold
          )
          Spacer(modifier = Modifier.width(4.dp))
          Icon(
            imageVector = Icons.AutoMirrored.Filled.ArrowForward,
            contentDescription = null,
            tint = IndigoPrimary,
            modifier = Modifier.size(14.dp)
          )
        }
      }
    }
  }
}

@Composable
private fun Slate100() = Color(0xFFF1F5F9)

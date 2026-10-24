package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "customers")
data class Customer(
  @PrimaryKey(autoGenerate = true)
  val id: Long = 0,
  val name: String,
  val company: String,
  val phone: String = "",
  val email: String = "",
  val address: String = "",
  val tags: String = "", // Comma-separated list of tags/segments
  val notes: String = "",
  val status: String = "Active", // "Active", "Lead", "Prospect", "Inactive", "VIP"
  val lastContactedMillis: Long = System.currentTimeMillis(),
  val createdAtMillis: Long = System.currentTimeMillis()
) {
  fun getTagList(): List<String> {
    return if (tags.isBlank()) emptyList()
    else tags.split(",").map { it.trim() }.filter { it.isNotEmpty() }
  }

  fun getInitials(): String {
    val parts = name.trim().split("\\s+".toRegex())
    return when {
      parts.isEmpty() || parts[0].isEmpty() -> "CB"
      parts.size == 1 -> parts[0].take(2).uppercase()
      else -> (parts[0].take(1) + parts[1].take(1)).uppercase()
    }
  }
}

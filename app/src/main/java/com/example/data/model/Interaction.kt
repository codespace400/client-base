package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "interactions")
data class Interaction(
  @PrimaryKey(autoGenerate = true)
  val id: Long = 0,
  val customerId: Long,
  val customerName: String,
  val type: String, // "Call", "Meeting", "Email", "Note"
  val dateMillis: Long = System.currentTimeMillis(),
  val summary: String,
  val description: String,
  val createdAtMillis: Long = System.currentTimeMillis()
)

package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "reminders")
data class Reminder(
  @PrimaryKey(autoGenerate = true)
  val id: Long = 0,
  val customerId: Long,
  val customerName: String,
  val title: String,
  val dueDateMillis: Long,
  val isCompleted: Boolean = false,
  val note: String = ""
)

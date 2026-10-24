package com.example.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.data.model.Reminder
import kotlinx.coroutines.flow.Flow

@Dao
interface ReminderDao {
  @Query("SELECT * FROM reminders ORDER BY dueDateMillis ASC")
  fun getAllReminders(): Flow<List<Reminder>>

  @Query("SELECT * FROM reminders WHERE customerId = :customerId ORDER BY dueDateMillis ASC")
  fun getRemindersForCustomer(customerId: Long): Flow<List<Reminder>>

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertReminder(reminder: Reminder): Long

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertReminders(reminders: List<Reminder>)

  @Update
  suspend fun updateReminder(reminder: Reminder)

  @Query("UPDATE reminders SET isCompleted = :completed WHERE id = :id")
  suspend fun setCompleted(id: Long, completed: Boolean)

  @Delete
  suspend fun deleteReminder(reminder: Reminder)

  @Query("DELETE FROM reminders WHERE customerId = :customerId")
  suspend fun deleteRemindersForCustomer(customerId: Long)
}

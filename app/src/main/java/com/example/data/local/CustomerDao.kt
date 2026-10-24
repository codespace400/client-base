package com.example.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.data.model.Customer
import kotlinx.coroutines.flow.Flow

@Dao
interface CustomerDao {
  @Query("SELECT * FROM customers ORDER BY lastContactedMillis DESC")
  fun getAllCustomers(): Flow<List<Customer>>

  @Query("SELECT * FROM customers WHERE id = :id LIMIT 1")
  fun getCustomerById(id: Long): Flow<Customer?>

  @Query("SELECT * FROM customers WHERE id = :id LIMIT 1")
  suspend fun getCustomerByIdDirect(id: Long): Customer?

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertCustomer(customer: Customer): Long

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertCustomers(customers: List<Customer>)

  @Update
  suspend fun updateCustomer(customer: Customer)

  @Delete
  suspend fun deleteCustomer(customer: Customer)

  @Query("DELETE FROM customers WHERE id = :id")
  suspend fun deleteCustomerById(id: Long)

  @Query("SELECT COUNT(*) FROM customers")
  suspend fun getCustomerCount(): Int
}

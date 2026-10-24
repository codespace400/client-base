package com.example.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.local.AppDatabase
import com.example.data.model.Customer
import com.example.data.model.Interaction
import com.example.data.model.Reminder
import com.example.data.repository.CrmRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

enum class AppScreen {
  LOGIN,
  DASHBOARD,
  CUSTOMERS,
  SEGMENTS,
  REMINDERS
}

enum class ReminderFilter {
  ALL,
  PENDING,
  COMPLETED
}

data class SegmentCount(
  val tag: String,
  val count: Int,
  val activeCount: Int
)

class CrmViewModel(application: Application) : AndroidViewModel(application) {
  private val repository: CrmRepository

  init {
    val db = AppDatabase.getDatabase(application)
    repository = CrmRepository(db.customerDao(), db.interactionDao(), db.reminderDao())
    viewModelScope.launch {
      repository.seedSampleDataIfEmpty()
    }
  }

  // Auth / Business Workspace State
  private val _isLoggedIn = MutableStateFlow(true) // Auto-login to demo workspace for quick start
  val isLoggedIn: StateFlow<Boolean> = _isLoggedIn.asStateFlow()

  private val _workspaceName = MutableStateFlow("Acme Global CRM")
  val workspaceName: StateFlow<String> = _workspaceName.asStateFlow()

  private val _userEmail = MutableStateFlow("alex.morgan@acmeglobal.com")
  val userEmail: StateFlow<String> = _userEmail.asStateFlow()

  private val _userName = MutableStateFlow("Alex Morgan")
  val userName: StateFlow<String> = _userName.asStateFlow()

  // Navigation State
  private val _currentScreen = MutableStateFlow(AppScreen.DASHBOARD)
  val currentScreen: StateFlow<AppScreen> = _currentScreen.asStateFlow()

  // Selected Customer for Profile view (Hybrid pane or subscreen)
  private val _selectedCustomerId = MutableStateFlow<Long?>(1L)
  val selectedCustomerId: StateFlow<Long?> = _selectedCustomerId.asStateFlow()

  // Search & Filters for Customers
  private val _customerSearchQuery = MutableStateFlow("")
  val customerSearchQuery: StateFlow<String> = _customerSearchQuery.asStateFlow()

  private val _selectedSegmentFilter = MutableStateFlow<String?>(null)
  val selectedSegmentFilter: StateFlow<String?> = _selectedSegmentFilter.asStateFlow()

  private val _selectedStatusFilter = MutableStateFlow<String?>(null)
  val selectedStatusFilter: StateFlow<String?> = _selectedStatusFilter.asStateFlow()

  // Reminder filter
  private val _reminderFilter = MutableStateFlow(ReminderFilter.ALL)
  val reminderFilter: StateFlow<ReminderFilter> = _reminderFilter.asStateFlow()

  // Dialog States
  private val _showAddEditCustomerDialog = MutableStateFlow(false)
  val showAddEditCustomerDialog: StateFlow<Boolean> = _showAddEditCustomerDialog.asStateFlow()

  private val _customerToEdit = MutableStateFlow<Customer?>(null)
  val customerToEdit: StateFlow<Customer?> = _customerToEdit.asStateFlow()

  private val _showAddInteractionDialog = MutableStateFlow(false)
  val showAddInteractionDialog: StateFlow<Boolean> = _showAddInteractionDialog.asStateFlow()

  private val _interactionTargetCustomerId = MutableStateFlow<Long?>(null)
  val interactionTargetCustomerId: StateFlow<Long?> = _interactionTargetCustomerId.asStateFlow()

  private val _showAddReminderDialog = MutableStateFlow(false)
  val showAddReminderDialog: StateFlow<Boolean> = _showAddReminderDialog.asStateFlow()

  private val _reminderTargetCustomerId = MutableStateFlow<Long?>(null)
  val reminderTargetCustomerId: StateFlow<Long?> = _reminderTargetCustomerId.asStateFlow()

  // Data streams
  val allCustomers: StateFlow<List<Customer>> = repository.allCustomers
    .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

  val allInteractions: StateFlow<List<Interaction>> = repository.allInteractions
    .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

  val allReminders: StateFlow<List<Reminder>> = repository.allReminders
    .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

  // Filtered customers flow
  val filteredCustomers: StateFlow<List<Customer>> = combine(
    allCustomers,
    _customerSearchQuery,
    _selectedSegmentFilter,
    _selectedStatusFilter
  ) { customers, query, segment, status ->
    customers.filter { customer ->
      val matchesQuery = query.isBlank() ||
        customer.name.contains(query, ignoreCase = true) ||
        customer.company.contains(query, ignoreCase = true) ||
        customer.email.contains(query, ignoreCase = true) ||
        customer.tags.contains(query, ignoreCase = true)

      val matchesSegment = segment.isNullOrBlank() ||
        customer.getTagList().any { it.equals(segment, ignoreCase = true) }

      val matchesStatus = status.isNullOrBlank() ||
        customer.status.equals(status, ignoreCase = true)

      matchesQuery && matchesSegment && matchesStatus
    }
  }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

  // Segment counts flow
  val segmentsList: StateFlow<List<SegmentCount>> = allCustomers.combine(_customerSearchQuery) { customers, _ ->
    val tagMap = mutableMapOf<String, MutableList<Customer>>()
    customers.forEach { customer ->
      customer.getTagList().forEach { tag ->
        val list = tagMap.getOrPut(tag) { mutableListOf() }
        list.add(customer)
      }
    }
    tagMap.map { (tag, list) ->
      SegmentCount(
        tag = tag,
        count = list.size,
        activeCount = list.count { it.status.equals("Active", ignoreCase = true) || it.status.equals("VIP", ignoreCase = true) }
      )
    }.sortedByDescending { it.count }
  }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

  // Filtered Reminders
  val filteredReminders: StateFlow<List<Reminder>> = combine(
    allReminders,
    _reminderFilter
  ) { reminders, filter ->
    when (filter) {
      ReminderFilter.ALL -> reminders
      ReminderFilter.PENDING -> reminders.filter { !it.isCompleted }
      ReminderFilter.COMPLETED -> reminders.filter { it.isCompleted }
    }
  }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

  // Navigation handlers
  fun navigateTo(screen: AppScreen) {
    _currentScreen.value = screen
  }

  fun selectCustomer(customerId: Long?) {
    _selectedCustomerId.value = customerId
  }

  fun setSearchQuery(query: String) {
    _customerSearchQuery.value = query
  }

  fun setSegmentFilter(segment: String?) {
    _selectedSegmentFilter.value = segment
  }

  fun setStatusFilter(status: String?) {
    _selectedStatusFilter.value = status
  }

  fun setReminderFilter(filter: ReminderFilter) {
    _reminderFilter.value = filter
  }

  // Filter by segment from Segments screen and jump to Customers screen
  fun filterBySegmentAndNavigate(segment: String) {
    _selectedSegmentFilter.value = segment
    _customerSearchQuery.value = ""
    _currentScreen.value = AppScreen.CUSTOMERS
  }

  // Dialog actions
  fun openAddCustomerDialog() {
    _customerToEdit.value = null
    _showAddEditCustomerDialog.value = true
  }

  fun openEditCustomerDialog(customer: Customer) {
    _customerToEdit.value = customer
    _showAddEditCustomerDialog.value = true
  }

  fun closeAddEditCustomerDialog() {
    _showAddEditCustomerDialog.value = false
    _customerToEdit.value = null
  }

  fun openAddInteractionDialog(customerId: Long? = null) {
    _interactionTargetCustomerId.value = customerId ?: _selectedCustomerId.value
    _showAddInteractionDialog.value = true
  }

  fun closeAddInteractionDialog() {
    _showAddInteractionDialog.value = false
    _interactionTargetCustomerId.value = null
  }

  fun openAddReminderDialog(customerId: Long? = null) {
    _reminderTargetCustomerId.value = customerId ?: _selectedCustomerId.value
    _showAddReminderDialog.value = true
  }

  fun closeAddReminderDialog() {
    _showAddReminderDialog.value = false
    _reminderTargetCustomerId.value = null
  }

  // Login / Logout
  fun login(workspace: String, email: String) {
    _workspaceName.value = workspace.ifBlank { "ClientBase Workspace" }
    _userEmail.value = email.ifBlank { "team@company.com" }
    val extractedName = email.substringBefore("@").replace(".", " ").split(" ")
      .joinToString(" ") { it.replaceFirstChar { char -> char.uppercase() } }
    _userName.value = if (extractedName.isNotBlank()) extractedName else "Workspace Admin"
    _isLoggedIn.value = true
    _currentScreen.value = AppScreen.DASHBOARD
  }

  fun logout() {
    _isLoggedIn.value = false
    _currentScreen.value = AppScreen.LOGIN
  }

  // CRUD Customer
  fun saveCustomer(
    id: Long = 0,
    name: String,
    company: String,
    phone: String,
    email: String,
    address: String,
    tags: String,
    notes: String,
    status: String
  ) {
    viewModelScope.launch {
      val now = System.currentTimeMillis()
      if (id == 0L) {
        val newCustomer = Customer(
          name = name,
          company = company,
          phone = phone,
          email = email,
          address = address,
          tags = tags,
          notes = notes,
          status = status,
          lastContactedMillis = now,
          createdAtMillis = now
        )
        val newId = repository.insertCustomer(newCustomer)
        _selectedCustomerId.value = newId
      } else {
        val existing = repository.getCustomerDirect(id)
        val updated = (existing ?: Customer(name = name, company = company)).copy(
          id = id,
          name = name,
          company = company,
          phone = phone,
          email = email,
          address = address,
          tags = tags,
          notes = notes,
          status = status
        )
        repository.updateCustomer(updated)
        _selectedCustomerId.value = id
      }
      closeAddEditCustomerDialog()
    }
  }

  fun deleteCustomer(customer: Customer) {
    viewModelScope.launch {
      repository.deleteCustomer(customer)
      if (_selectedCustomerId.value == customer.id) {
        _selectedCustomerId.value = null
      }
    }
  }

  // CRUD Interaction
  fun saveInteraction(
    customerId: Long,
    type: String,
    dateMillis: Long,
    summary: String,
    description: String,
    scheduleFollowUpDays: Int? = null
  ) {
    viewModelScope.launch {
      val customer = repository.getCustomerDirect(customerId)
      val customerName = customer?.name ?: "Customer #$customerId"
      val interaction = Interaction(
        customerId = customerId,
        customerName = customerName,
        type = type,
        dateMillis = dateMillis,
        summary = summary,
        description = description
      )
      repository.addInteraction(interaction)

      if (scheduleFollowUpDays != null && scheduleFollowUpDays > 0) {
        val due = System.currentTimeMillis() + (scheduleFollowUpDays * 86_400_000L)
        repository.addReminder(
          Reminder(
            customerId = customerId,
            customerName = customerName,
            title = "Follow-up: $summary",
            dueDateMillis = due,
            note = "Scheduled via interaction ($type)"
          )
        )
      }
      closeAddInteractionDialog()
    }
  }

  fun deleteInteraction(interaction: Interaction) {
    viewModelScope.launch {
      repository.deleteInteraction(interaction)
    }
  }

  // CRUD Reminder
  fun saveReminder(
    customerId: Long,
    title: String,
    dueDateMillis: Long,
    note: String
  ) {
    viewModelScope.launch {
      val customer = repository.getCustomerDirect(customerId)
      val customerName = customer?.name ?: "Client"
      repository.addReminder(
        Reminder(
          customerId = customerId,
          customerName = customerName,
          title = title,
          dueDateMillis = dueDateMillis,
          note = note
        )
      )
      closeAddReminderDialog()
    }
  }

  fun toggleReminderCompleted(reminder: Reminder) {
    viewModelScope.launch {
      repository.setReminderCompleted(reminder.id, !reminder.isCompleted)
    }
  }

  fun deleteReminder(reminder: Reminder) {
    viewModelScope.launch {
      repository.deleteReminder(reminder)
    }
  }

  // Stats for Dashboard
  fun getStats(customers: List<Customer>, reminders: List<Reminder>): Triple<Int, Int, Int> {
    val totalCustomers = customers.size

    val cal = Calendar.getInstance()
    cal.set(Calendar.DAY_OF_MONTH, 1)
    cal.set(Calendar.HOUR_OF_DAY, 0)
    cal.set(Calendar.MINUTE, 0)
    cal.set(Calendar.SECOND, 0)
    val startOfMonth = cal.timeInMillis

    val newThisMonth = customers.count { it.createdAtMillis >= startOfMonth }
    val followupsDue = reminders.count { !it.isCompleted }

    return Triple(totalCustomers, newThisMonth, followupsDue)
  }

  companion object {
    private val dateFormat = SimpleDateFormat("MMM d, yyyy", Locale.getDefault())
    private val dateTimeFormat = SimpleDateFormat("MMM d, yyyy · h:mm a", Locale.getDefault())
    private val shortDateFormat = SimpleDateFormat("MMM d", Locale.getDefault())

    fun formatDate(millis: Long): String = dateFormat.format(Date(millis))
    fun formatDateTime(millis: Long): String = dateTimeFormat.format(Date(millis))
    fun formatShortDate(millis: Long): String = shortDateFormat.format(Date(millis))

    fun formatRelativeTime(millis: Long): String {
      val diff = System.currentTimeMillis() - millis
      val seconds = diff / 1000
      val minutes = seconds / 60
      val hours = minutes / 60
      val days = hours / 24

      return when {
        days > 30 -> formatDate(millis)
        days > 1 -> "$days days ago"
        days == 1L -> "Yesterday"
        hours > 0 -> "$hours hr ago"
        minutes > 0 -> "$minutes min ago"
        else -> "Just now"
      }
    }
  }
}

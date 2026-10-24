package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Category
import androidx.compose.material.icons.filled.Dashboard
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.People
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.ui.components.CrmSidebar
import com.example.ui.components.CrmTopBar
import com.example.ui.screens.AddEditCustomerDialog
import com.example.ui.screens.AddInteractionDialog
import com.example.ui.screens.AddReminderDialog
import com.example.ui.screens.CustomerListScreen
import com.example.ui.screens.DashboardScreen
import com.example.ui.screens.LoginScreen
import com.example.ui.screens.RemindersScreen
import com.example.ui.screens.SegmentsScreen
import com.example.ui.theme.ClientBaseTheme
import com.example.ui.theme.IndigoContainer
import com.example.ui.theme.IndigoPrimary
import com.example.ui.theme.Slate200
import com.example.ui.theme.Slate50
import com.example.ui.theme.Slate500
import com.example.ui.theme.Slate900
import com.example.viewmodel.AppScreen
import com.example.viewmodel.CrmViewModel
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
  private val viewModel: CrmViewModel by viewModels()

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    enableEdgeToEdge()
    setContent {
      ClientBaseTheme {
        ClientBaseApp(viewModel = viewModel)
      }
    }
  }
}

@Composable
fun ClientBaseApp(viewModel: CrmViewModel) {
  val isLoggedIn by viewModel.isLoggedIn.collectAsStateWithLifecycle()
  val currentScreen by viewModel.currentScreen.collectAsStateWithLifecycle()
  val workspaceName by viewModel.workspaceName.collectAsStateWithLifecycle()
  val userName by viewModel.userName.collectAsStateWithLifecycle()
  val userEmail by viewModel.userEmail.collectAsStateWithLifecycle()

  val customers by viewModel.filteredCustomers.collectAsStateWithLifecycle()
  val allCustomers by viewModel.allCustomers.collectAsStateWithLifecycle()
  val interactions by viewModel.allInteractions.collectAsStateWithLifecycle()
  val reminders by viewModel.filteredReminders.collectAsStateWithLifecycle()
  val allReminders by viewModel.allReminders.collectAsStateWithLifecycle()
  val segments by viewModel.segmentsList.collectAsStateWithLifecycle()

  val selectedCustomerId by viewModel.selectedCustomerId.collectAsStateWithLifecycle()
  val searchQuery by viewModel.customerSearchQuery.collectAsStateWithLifecycle()
  val selectedSegment by viewModel.selectedSegmentFilter.collectAsStateWithLifecycle()
  val selectedStatus by viewModel.selectedStatusFilter.collectAsStateWithLifecycle()
  val reminderFilter by viewModel.reminderFilter.collectAsStateWithLifecycle()

  val showAddEditCustomerDialog by viewModel.showAddEditCustomerDialog.collectAsStateWithLifecycle()
  val customerToEdit by viewModel.customerToEdit.collectAsStateWithLifecycle()
  val showAddInteractionDialog by viewModel.showAddInteractionDialog.collectAsStateWithLifecycle()
  val interactionTargetCustomerId by viewModel.interactionTargetCustomerId.collectAsStateWithLifecycle()
  val showAddReminderDialog by viewModel.showAddReminderDialog.collectAsStateWithLifecycle()
  val reminderTargetCustomerId by viewModel.reminderTargetCustomerId.collectAsStateWithLifecycle()

  val pendingRemindersCount = allReminders.count { !it.isCompleted }

  if (!isLoggedIn) {
    LoginScreen(
      onLoginSuccess = { ws, email ->
        viewModel.login(ws, email)
      },
      modifier = Modifier
        .fillMaxSize()
        .windowInsetsPadding(WindowInsets.safeDrawing)
    )
    return
  }

  // Handle Back key: navigate back to Dashboard if on another top-level screen
  BackHandler(enabled = currentScreen != AppScreen.DASHBOARD) {
    viewModel.navigateTo(AppScreen.DASHBOARD)
  }

  val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
  val scope = rememberCoroutineScope()

  BoxWithConstraints(
    modifier = Modifier
      .fillMaxSize()
      .background(Slate50)
      .windowInsetsPadding(WindowInsets.safeDrawing)
  ) {
    val isWideScreen = maxWidth >= 840.dp

    if (isWideScreen) {
      // Desktop / Tablet layout with Permanent Sidebar
      Row(modifier = Modifier.fillMaxSize()) {
        CrmSidebar(
          currentScreen = currentScreen,
          onScreenSelected = { screen -> viewModel.navigateTo(screen) },
          onAddCustomerClick = { viewModel.openAddCustomerDialog() },
          workspaceName = workspaceName,
          userName = userName,
          userEmail = userEmail,
          pendingRemindersCount = pendingRemindersCount,
          onLogoutClick = { viewModel.logout() }
        )

        Column(
          modifier = Modifier
            .weight(1f)
            .fillMaxSize()
        ) {
          CrmTopBar(
            title = when (currentScreen) {
              AppScreen.DASHBOARD -> "Dashboard"
              AppScreen.CUSTOMERS -> "Customer Directory"
              AppScreen.SEGMENTS -> "Segments & Tags"
              AppScreen.REMINDERS -> "Follow-up Reminders"
              AppScreen.LOGIN -> "ClientBase"
            },
            subtitle = "Workspace: $workspaceName",
            isCompact = false,
            actionButtonText = if (currentScreen == AppScreen.CUSTOMERS) "+ Add Customer" else null,
            onActionClick = { viewModel.openAddCustomerDialog() }
          )

          Box(modifier = Modifier.weight(1f)) {
            when (currentScreen) {
              AppScreen.DASHBOARD -> DashboardScreen(
                customers = allCustomers,
                interactions = interactions,
                reminders = allReminders,
                onAddCustomerClick = { viewModel.openAddCustomerDialog() },
                onAddInteractionClick = { viewModel.openAddInteractionDialog() },
                onNavigateToScreen = { screen -> viewModel.navigateTo(screen) },
                onCustomerClick = { id ->
                  viewModel.selectCustomer(id)
                  viewModel.navigateTo(AppScreen.CUSTOMERS)
                },
                onReminderToggle = { reminder -> viewModel.toggleReminderCompleted(reminder) }
              )
              AppScreen.CUSTOMERS -> CustomerListScreen(
                customers = customers,
                allCustomers = allCustomers,
                interactions = interactions,
                selectedCustomerId = selectedCustomerId,
                onCustomerSelect = { id -> viewModel.selectCustomer(id) },
                searchQuery = searchQuery,
                onSearchChange = { q -> viewModel.setSearchQuery(q) },
                selectedSegment = selectedSegment,
                onSegmentSelect = { s -> viewModel.setSegmentFilter(s) },
                selectedStatus = selectedStatus,
                onStatusSelect = { st -> viewModel.setStatusFilter(st) },
                onAddCustomerClick = { viewModel.openAddCustomerDialog() },
                onEditCustomerClick = { cust -> viewModel.openEditCustomerDialog(cust) },
                onDeleteCustomerClick = { cust -> viewModel.deleteCustomer(cust) },
                onAddInteractionClick = { custId -> viewModel.openAddInteractionDialog(custId) },
                onDeleteInteractionClick = { item -> viewModel.deleteInteraction(item) }
              )
              AppScreen.SEGMENTS -> SegmentsScreen(
                segments = segments,
                totalCustomersCount = allCustomers.size,
                onSegmentClick = { tag -> viewModel.filterBySegmentAndNavigate(tag) },
                onAddSegmentClick = { viewModel.openAddCustomerDialog() }
              )
              AppScreen.REMINDERS -> RemindersScreen(
                reminders = reminders,
                allReminders = allReminders,
                currentFilter = reminderFilter,
                onFilterChange = { f -> viewModel.setReminderFilter(f) },
                onToggleCompleted = { r -> viewModel.toggleReminderCompleted(r) },
                onDeleteReminder = { r -> viewModel.deleteReminder(r) },
                onAddReminderClick = { viewModel.openAddReminderDialog() },
                onCustomerClick = { id ->
                  viewModel.selectCustomer(id)
                  viewModel.navigateTo(AppScreen.CUSTOMERS)
                }
              )
              AppScreen.LOGIN -> Unit
            }
          }
        }
      }
    } else {
      // Compact Mobile Layout with Navigation Drawer and Bottom Bar
      ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
          ModalDrawerSheet(
            drawerContainerColor = Color.White
          ) {
            CrmSidebar(
              currentScreen = currentScreen,
              onScreenSelected = { screen ->
                viewModel.navigateTo(screen)
                scope.launch { drawerState.close() }
              },
              onAddCustomerClick = {
                viewModel.openAddCustomerDialog()
                scope.launch { drawerState.close() }
              },
              workspaceName = workspaceName,
              userName = userName,
              userEmail = userEmail,
              pendingRemindersCount = pendingRemindersCount,
              onLogoutClick = {
                viewModel.logout()
                scope.launch { drawerState.close() }
              }
            )
          }
        }
      ) {
        Scaffold(
          topBar = {
            CrmTopBar(
              title = when (currentScreen) {
                AppScreen.DASHBOARD -> "Dashboard"
                AppScreen.CUSTOMERS -> "Customers"
                AppScreen.SEGMENTS -> "Segments"
                AppScreen.REMINDERS -> "Reminders"
                AppScreen.LOGIN -> "ClientBase"
              },
              subtitle = workspaceName,
              isCompact = true,
              onMenuClick = { scope.launch { drawerState.open() } },
              actionButtonText = "+ Add",
              onActionClick = { viewModel.openAddCustomerDialog() }
            )
          },
          bottomBar = {
            NavigationBar(
              containerColor = Color.White,
              contentColor = Slate900,
              modifier = Modifier
                .windowInsetsPadding(WindowInsets.navigationBars)
                .testTag("bottom_nav_bar")
            ) {
              val navTabs = listOf(
                Pair(AppScreen.DASHBOARD, Pair("Dashboard", Icons.Default.Dashboard)),
                Pair(AppScreen.CUSTOMERS, Pair("Customers", Icons.Default.People)),
                Pair(AppScreen.SEGMENTS, Pair("Segments", Icons.Default.Category)),
                Pair(AppScreen.REMINDERS, Pair("Reminders", Icons.Default.Notifications))
              )

              navTabs.forEach { (screen, info) ->
                val (label, icon) = info
                val isSelected = currentScreen == screen
                NavigationBarItem(
                  selected = isSelected,
                  onClick = { viewModel.navigateTo(screen) },
                  icon = {
                    Icon(imageVector = icon, contentDescription = label)
                  },
                  label = {
                    Text(
                      text = label,
                      fontSize = 11.sp,
                      fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
                    )
                  },
                  colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = IndigoPrimary,
                    selectedTextColor = IndigoPrimary,
                    indicatorColor = IndigoContainer,
                    unselectedIconColor = Slate500,
                    unselectedTextColor = Slate500
                  )
                )
              }
            }
          }
        ) { paddingValues ->
          Box(
            modifier = Modifier
              .fillMaxSize()
              .padding(paddingValues)
          ) {
            when (currentScreen) {
              AppScreen.DASHBOARD -> DashboardScreen(
                customers = allCustomers,
                interactions = interactions,
                reminders = allReminders,
                onAddCustomerClick = { viewModel.openAddCustomerDialog() },
                onAddInteractionClick = { viewModel.openAddInteractionDialog() },
                onNavigateToScreen = { screen -> viewModel.navigateTo(screen) },
                onCustomerClick = { id ->
                  viewModel.selectCustomer(id)
                  viewModel.navigateTo(AppScreen.CUSTOMERS)
                },
                onReminderToggle = { reminder -> viewModel.toggleReminderCompleted(reminder) }
              )
              AppScreen.CUSTOMERS -> CustomerListScreen(
                customers = customers,
                allCustomers = allCustomers,
                interactions = interactions,
                selectedCustomerId = selectedCustomerId,
                onCustomerSelect = { id -> viewModel.selectCustomer(id) },
                searchQuery = searchQuery,
                onSearchChange = { q -> viewModel.setSearchQuery(q) },
                selectedSegment = selectedSegment,
                onSegmentSelect = { s -> viewModel.setSegmentFilter(s) },
                selectedStatus = selectedStatus,
                onStatusSelect = { st -> viewModel.setStatusFilter(st) },
                onAddCustomerClick = { viewModel.openAddCustomerDialog() },
                onEditCustomerClick = { cust -> viewModel.openEditCustomerDialog(cust) },
                onDeleteCustomerClick = { cust -> viewModel.deleteCustomer(cust) },
                onAddInteractionClick = { custId -> viewModel.openAddInteractionDialog(custId) },
                onDeleteInteractionClick = { item -> viewModel.deleteInteraction(item) }
              )
              AppScreen.SEGMENTS -> SegmentsScreen(
                segments = segments,
                totalCustomersCount = allCustomers.size,
                onSegmentClick = { tag -> viewModel.filterBySegmentAndNavigate(tag) },
                onAddSegmentClick = { viewModel.openAddCustomerDialog() }
              )
              AppScreen.REMINDERS -> RemindersScreen(
                reminders = reminders,
                allReminders = allReminders,
                currentFilter = reminderFilter,
                onFilterChange = { f -> viewModel.setReminderFilter(f) },
                onToggleCompleted = { r -> viewModel.toggleReminderCompleted(r) },
                onDeleteReminder = { r -> viewModel.deleteReminder(r) },
                onAddReminderClick = { viewModel.openAddReminderDialog() },
                onCustomerClick = { id ->
                  viewModel.selectCustomer(id)
                  viewModel.navigateTo(AppScreen.CUSTOMERS)
                }
              )
              AppScreen.LOGIN -> Unit
            }
          }
        }
      }
    }
  }

  // Modals / Dialogs
  if (showAddEditCustomerDialog) {
    AddEditCustomerDialog(
      customer = customerToEdit,
      onDismiss = { viewModel.closeAddEditCustomerDialog() },
      onSave = { id, name, company, phone, email, address, tags, notes, status ->
        viewModel.saveCustomer(id, name, company, phone, email, address, tags, notes, status)
      }
    )
  }

  if (showAddInteractionDialog) {
    AddInteractionDialog(
      initialCustomerId = interactionTargetCustomerId,
      customers = allCustomers,
      onDismiss = { viewModel.closeAddInteractionDialog() },
      onSave = { custId, type, dateMillis, summary, desc, followUpDays ->
        viewModel.saveInteraction(custId, type, dateMillis, summary, desc, followUpDays)
      }
    )
  }

  if (showAddReminderDialog) {
    AddReminderDialog(
      initialCustomerId = reminderTargetCustomerId,
      customers = allCustomers,
      onDismiss = { viewModel.closeAddReminderDialog() },
      onSave = { custId, title, dueDateMillis, note ->
        viewModel.saveReminder(custId, title, dueDateMillis, note)
      }
    )
  }
}

package com.example.data.repository

import com.example.data.local.CustomerDao
import com.example.data.local.InteractionDao
import com.example.data.local.ReminderDao
import com.example.data.model.Customer
import com.example.data.model.Interaction
import com.example.data.model.Reminder
import kotlinx.coroutines.flow.Flow
import java.util.Calendar

class CrmRepository(
  private val customerDao: CustomerDao,
  private val interactionDao: InteractionDao,
  private val reminderDao: ReminderDao
) {
  val allCustomers: Flow<List<Customer>> = customerDao.getAllCustomers()
  val allInteractions: Flow<List<Interaction>> = interactionDao.getAllInteractions()
  val allReminders: Flow<List<Reminder>> = reminderDao.getAllReminders()

  fun getCustomer(id: Long): Flow<Customer?> = customerDao.getCustomerById(id)

  suspend fun getCustomerDirect(id: Long): Customer? = customerDao.getCustomerByIdDirect(id)

  fun getInteractionsForCustomer(customerId: Long): Flow<List<Interaction>> =
    interactionDao.getInteractionsForCustomer(customerId)

  fun getRemindersForCustomer(customerId: Long): Flow<List<Reminder>> =
    reminderDao.getRemindersForCustomer(customerId)

  suspend fun insertCustomer(customer: Customer): Long =
    customerDao.insertCustomer(customer)

  suspend fun updateCustomer(customer: Customer) =
    customerDao.updateCustomer(customer)

  suspend fun deleteCustomer(customer: Customer) {
    customerDao.deleteCustomer(customer)
    interactionDao.deleteInteractionsForCustomer(customer.id)
    reminderDao.deleteRemindersForCustomer(customer.id)
  }

  suspend fun addInteraction(interaction: Interaction, updateCustomerLastContact: Boolean = true): Long {
    val id = interactionDao.insertInteraction(interaction)
    if (updateCustomerLastContact) {
      val customer = customerDao.getCustomerByIdDirect(interaction.customerId)
      if (customer != null && interaction.dateMillis > customer.lastContactedMillis) {
        customerDao.updateCustomer(customer.copy(lastContactedMillis = interaction.dateMillis))
      }
    }
    return id
  }

  suspend fun deleteInteraction(interaction: Interaction) =
    interactionDao.deleteInteraction(interaction)

  suspend fun addReminder(reminder: Reminder): Long =
    reminderDao.insertReminder(reminder)

  suspend fun updateReminder(reminder: Reminder) =
    reminderDao.updateReminder(reminder)

  suspend fun setReminderCompleted(id: Long, completed: Boolean) =
    reminderDao.setCompleted(id, completed)

  suspend fun deleteReminder(reminder: Reminder) =
    reminderDao.deleteReminder(reminder)

  suspend fun seedSampleDataIfEmpty() {
    if (customerDao.getCustomerCount() > 0) return

    val now = System.currentTimeMillis()
    val dayMillis = 86_400_000L

    val sampleCustomers = listOf(
      Customer(
        id = 1,
        name = "Sarah Jenkins",
        company = "NovaTech Cloud",
        phone = "+1 (415) 890-2341",
        email = "s.jenkins@novatech.io",
        address = "100 Montgomery St, Suite 1400, San Francisco, CA",
        tags = "Enterprise, SaaS, Tech",
        notes = "Key decision maker for enterprise cloud migration. Evaluated tier 3 pricing. Requested SOC2 documentation.",
        status = "Active",
        lastContactedMillis = now - (dayMillis * 1),
        createdAtMillis = now - (dayMillis * 45)
      ),
      Customer(
        id = 2,
        name = "Marcus Vance",
        company = "Apex Industrial Supplies",
        phone = "+1 (312) 555-0199",
        email = "mvance@apexsupplies.com",
        address = "420 Michigan Ave, Chicago, IL",
        tags = "SMB, Manufacturing, High-Value",
        notes = "Looking to automate wholesale inventory ordering. High contract value potential.",
        status = "Prospect",
        lastContactedMillis = now - (dayMillis * 3),
        createdAtMillis = now - (dayMillis * 18)
      ),
      Customer(
        id = 3,
        name = "Elena Rostova",
        company = "Solstice Creative Agency",
        phone = "+1 (212) 777-4821",
        email = "elena@solsticeagency.design",
        address = "75 Broadway, New York, NY",
        tags = "Agency, VIP, Partner",
        notes = "VIP client partner. Working together on joint agency client onboarding. Renewal coming up in Q4.",
        status = "VIP",
        lastContactedMillis = now - (dayMillis * 2),
        createdAtMillis = now - (dayMillis * 90)
      ),
      Customer(
        id = 4,
        name = "David K. Chen",
        company = "Veritas Financial Group",
        phone = "+1 (617) 424-9102",
        email = "dchen@veritasfin.com",
        address = "200 State St, Boston, MA",
        tags = "Enterprise, Finance, Compliance",
        notes = "Initial demo completed. Legal team currently reviewing Master Services Agreement.",
        status = "Lead",
        lastContactedMillis = now - (dayMillis * 5),
        createdAtMillis = now - (dayMillis * 12)
      ),
      Customer(
        id = 5,
        name = "Amara Diallo",
        company = "GreenRoot Logistics",
        phone = "+1 (206) 819-3320",
        email = "amara@greenrootlogistics.net",
        address = "800 5th Ave, Seattle, WA",
        tags = "Logistics, GreenTech, SMB",
        notes = "Fleet tracking pilot successfully deployed across 50 regional vehicles.",
        status = "Active",
        lastContactedMillis = now - (dayMillis * 7),
        createdAtMillis = now - (dayMillis * 60)
      ),
      Customer(
        id = 6,
        name = "Liam O'Connor",
        company = "Emerald Retailers",
        phone = "+1 (512) 349-8800",
        email = "liam@emeraldretail.com",
        address = "500 Congress Ave, Austin, TX",
        tags = "Retail, SMB, Inactive",
        notes = "Paused subscription during store remodeling. Follow up in autumn for reopening.",
        status = "Inactive",
        lastContactedMillis = now - (dayMillis * 35),
        createdAtMillis = now - (dayMillis * 120)
      ),
      Customer(
        id = 7,
        name = "Chloe Dubois",
        company = "Pulse Biomedical",
        phone = "+1 (858) 990-1122",
        email = "c.dubois@pulsebiomed.org",
        address = "10900 N Torrey Pines Rd, La Jolla, CA",
        tags = "Healthcare, VIP, Enterprise",
        notes = "Requires HIPAA compliant workspace configuration. Custom SLA signed.",
        status = "VIP",
        lastContactedMillis = now - (dayMillis * 2),
        createdAtMillis = now - (dayMillis * 80)
      )
    )

    customerDao.insertCustomers(sampleCustomers)

    val sampleInteractions = listOf(
      Interaction(
        id = 1,
        customerId = 1,
        customerName = "Sarah Jenkins",
        type = "Call",
        dateMillis = now - (dayMillis * 1),
        summary = "Quarterly Architecture Review Call",
        description = "Reviewed cluster capacity and scaling requirements. Sarah confirmed approval for 25 additional seat licenses starting next month.",
        createdAtMillis = now - (dayMillis * 1)
      ),
      Interaction(
        id = 2,
        customerId = 1,
        customerName = "Sarah Jenkins",
        type = "Email",
        dateMillis = now - (dayMillis * 4),
        summary = "Sent Security & SOC2 Compliance Whitepaper",
        description = "Forwarded latest SOC2 Type II audit report and cloud security specifications per IT team request.",
        createdAtMillis = now - (dayMillis * 4)
      ),
      Interaction(
        id = 3,
        customerId = 3,
        customerName = "Elena Rostova",
        type = "Meeting",
        dateMillis = now - (dayMillis * 2),
        summary = "Partner Strategy Luncheon",
        description = "Met at Solstice HQ to review co-marketing campaign metrics and Q4 enterprise client referrals. Elena was very positive about performance.",
        createdAtMillis = now - (dayMillis * 2)
      ),
      Interaction(
        id = 4,
        customerId = 2,
        customerName = "Marcus Vance",
        type = "Note",
        dateMillis = now - (dayMillis * 3),
        summary = "Budget Approval Note",
        description = "Marcus indicated board meets on Thursday to approve CapEx allocation for operations software modernization.",
        createdAtMillis = now - (dayMillis * 3)
      ),
      Interaction(
        id = 5,
        customerId = 4,
        customerName = "David K. Chen",
        type = "Call",
        dateMillis = now - (dayMillis * 5),
        summary = "Introductory Pipeline Call",
        description = "Walked David through compliance audit logs and role-based access control. Scheduled deep dive with their InfoSec team.",
        createdAtMillis = now - (dayMillis * 5)
      ),
      Interaction(
        id = 6,
        customerId = 7,
        customerName = "Chloe Dubois",
        type = "Meeting",
        dateMillis = now - (dayMillis * 2),
        summary = "Executive Check-in",
        description = "Discussed biomedical sensor data throughput. Signed off on expanding pipeline deployment to San Diego campus labs.",
        createdAtMillis = now - (dayMillis * 2)
      )
    )

    interactionDao.insertInteractions(sampleInteractions)

    val sampleReminders = listOf(
      Reminder(
        id = 1,
        customerId = 1,
        customerName = "Sarah Jenkins",
        title = "Send expanded license agreement addendum",
        dueDateMillis = now + (dayMillis * 1), // Tomorrow
        isCompleted = false,
        note = "Prepare contract addendum for 25 additional enterprise seats."
      ),
      Reminder(
        id = 2,
        customerId = 4,
        customerName = "David K. Chen",
        title = "Check in on legal MSA review status",
        dueDateMillis = now + (dayMillis * 2), // In 2 days
        isCompleted = false,
        note = "Send friendly nudge to legal council regarding master agreement."
      ),
      Reminder(
        id = 3,
        customerId = 2,
        customerName = "Marcus Vance",
        title = "Call Marcus regarding board budget decision",
        dueDateMillis = now - (dayMillis * 1), // Overdue
        isCompleted = false,
        note = "Board meeting was scheduled for yesterday; follow up on decision."
      ),
      Reminder(
        id = 4,
        customerId = 3,
        customerName = "Elena Rostova",
        title = "Co-marketing draft review",
        dueDateMillis = now + (dayMillis * 5),
        isCompleted = false,
        note = "Review press release draft for joint enterprise partnership."
      ),
      Reminder(
        id = 5,
        customerId = 5,
        customerName = "Amara Diallo",
        title = "Fleet telemetry milestone check",
        dueDateMillis = now - (dayMillis * 4),
        isCompleted = true,
        note = "Completed 30-day trial review with lead transport engineer."
      )
    )

    reminderDao.insertReminders(sampleReminders)
  }
}

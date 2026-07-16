package tech.sumato.avn.mp.data.dashboard.repository

import tech.sumato.avn.mp.domain.dashboard.model.DashboardData
import tech.sumato.avn.mp.domain.dashboard.model.DashboardStat
import tech.sumato.avn.mp.domain.dashboard.model.RevenueItem
import tech.sumato.avn.mp.domain.dashboard.model.Transaction
import tech.sumato.avn.mp.domain.dashboard.repository.DashboardRepository

class DashboardRepositoryImpl : DashboardRepository {

    override suspend fun getDashboardData(): DashboardData {
        return DashboardData(
            stats = listOf(
                DashboardStat(label = "Total Revenue", value = "$54,280", change = "+12.5%", isPositive = true),
                DashboardStat(label = "Active Users", value = "2,847", change = "+8.2%", isPositive = true),
                DashboardStat(label = "Conversion", value = "3.24%", change = "-0.8%", isPositive = false),
                DashboardStat(label = "Bounce Rate", value = "24.1%", change = "-2.4%", isPositive = true),
            ),
            monthlyRevenue = listOf(
                RevenueItem(month = "Jan", fraction = 0.45f, displayValue = "$4.5k"),
                RevenueItem(month = "Feb", fraction = 0.60f, displayValue = "$6.0k"),
                RevenueItem(month = "Mar", fraction = 0.55f, displayValue = "$5.5k"),
                RevenueItem(month = "Apr", fraction = 0.75f, displayValue = "$7.5k"),
                RevenueItem(month = "May", fraction = 0.70f, displayValue = "$7.0k"),
                RevenueItem(month = "Jun", fraction = 0.85f, displayValue = "$8.5k"),
            ),
            recentTransactions = listOf(
                Transaction(name = "Design Pro Subscription", category = "Software", amount = "$29.00", isInflow = false),
                Transaction(name = "Freelance Project Payment", category = "Income", amount = "$1,200.00", isInflow = true),
                Transaction(name = "Cloud Hosting", category = "Infrastructure", amount = "$89.99", isInflow = false),
                Transaction(name = "Ad Revenue - June", category = "Marketing", amount = "$340.00", isInflow = true),
            ),
        )
    }
}

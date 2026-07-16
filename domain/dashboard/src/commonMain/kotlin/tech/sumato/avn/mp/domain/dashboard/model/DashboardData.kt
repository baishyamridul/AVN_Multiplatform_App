package tech.sumato.avn.mp.domain.dashboard.model

data class DashboardData(
    val stats: List<DashboardStat>,
    val monthlyRevenue: List<RevenueItem>,
    val recentTransactions: List<Transaction>,
)

data class DashboardStat(
    val label: String,
    val value: String,
    val change: String?,
    val isPositive: Boolean,
)

data class RevenueItem(
    val month: String,
    val fraction: Float,
    val displayValue: String,
)

data class Transaction(
    val name: String,
    val category: String,
    val amount: String,
    val isInflow: Boolean,
)

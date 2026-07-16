package tech.sumato.kmptemplate.feature.dashboard.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import tech.sumato.kmptemplate.designsystem.components.AppCard
import tech.sumato.kmptemplate.designsystem.components.AppTextField
import tech.sumato.kmptemplate.designsystem.components.ScreenHeader
import tech.sumato.kmptemplate.designsystem.components.SectionHeader
import tech.sumato.kmptemplate.designsystem.components.StatCard
import tech.sumato.kmptemplate.designsystem.theme.MainColor
import tech.sumato.kmptemplate.domain.dashboard.model.DashboardData
import tech.sumato.kmptemplate.domain.dashboard.model.RevenueItem
import tech.sumato.kmptemplate.domain.dashboard.model.Transaction

@Composable
fun DashboardScreen(
    state: DashboardState,
    onEvent: (DashboardEvent) -> Unit,
) {
    when (state) {
        is DashboardState.Loading -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center,
            ) {
                CircularProgressIndicator()
            }
        }

        is DashboardState.Success -> {
            DashboardContent(state.data)
        }

        is DashboardState.Error -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    text = "Error: ${state.message}",
                    color = MaterialTheme.colorScheme.error,
                )
            }
        }
    }
}

@Composable
private fun DashboardContent(data: DashboardData) {
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .verticalScroll(scrollState)
            .padding(16.dp),
    ) {
        ScreenHeader(
            title = "Dashboard",
            subtitle = "Welcome back, here's your overview.",
        )
        Spacer(Modifier.height(20.dp))
        StatsGrid(data)
        Spacer(Modifier.height(20.dp))
        MonthlyRevenue(data)
        Spacer(Modifier.height(20.dp))
        RecentTransactions(data)
        Spacer(Modifier.height(20.dp))
        SampleForm()
        Spacer(Modifier.height(16.dp))
    }
}

@Composable
private fun StatsGrid(data: DashboardData) {
    val stats = data.stats
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            if (stats.size > 0) {
                StatCard(
                    label = stats[0].label,
                    value = stats[0].value,
                    change = stats[0].change,
                    isPositive = stats[0].isPositive,
                    modifier = Modifier.weight(1f),
                )
            }
            if (stats.size > 1) {
                StatCard(
                    label = stats[1].label,
                    value = stats[1].value,
                    change = stats[1].change,
                    isPositive = stats[1].isPositive,
                    modifier = Modifier.weight(1f),
                )
            }
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            if (stats.size > 2) {
                StatCard(
                    label = stats[2].label,
                    value = stats[2].value,
                    change = stats[2].change,
                    isPositive = stats[2].isPositive,
                    modifier = Modifier.weight(1f),
                )
            }
            if (stats.size > 3) {
                StatCard(
                    label = stats[3].label,
                    value = stats[3].value,
                    change = stats[3].change,
                    isPositive = stats[3].isPositive,
                    modifier = Modifier.weight(1f),
                )
            }
        }
    }
}

@Composable
private fun MonthlyRevenue(data: DashboardData) {
    AppCard {
        SectionHeader(title = "Monthly Revenue")
        Spacer(Modifier.height(16.dp))
        data.monthlyRevenue.forEach { item ->
            RevenueBar(item = item)
            Spacer(Modifier.height(10.dp))
        }
    }
}

@Composable
private fun RevenueBar(item: RevenueItem) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            item.month,
            modifier = Modifier.width(32.dp),
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        Spacer(Modifier.width(8.dp))
        Box(
            modifier = Modifier
                .weight(1f)
                .height(20.dp)
                .clip(RoundedCornerShape(4.dp))
                .background(MaterialTheme.colorScheme.surfaceVariant),
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth(item.fraction)
                    .fillMaxSize()
                    .clip(RoundedCornerShape(4.dp))
                    .background(MainColor.copy(alpha = 0.3f)),
            )
        }
        Spacer(Modifier.width(8.dp))
        Text(
            item.displayValue,
            style = MaterialTheme.typography.bodySmall,
            fontWeight = FontWeight.Medium,
            color = MaterialTheme.colorScheme.onSurface,
        )
    }
}

@Composable
private fun RecentTransactions(data: DashboardData) {
    AppCard {
        SectionHeader(title = "Recent Transactions")
        Spacer(Modifier.height(12.dp))
        data.recentTransactions.forEach { transaction ->
            TransactionItem(transaction = transaction)
        }
    }
}

@Composable
private fun TransactionItem(transaction: Transaction) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(
                    if (transaction.isInflow) MainColor.copy(alpha = 0.1f)
                    else MainColor.copy(alpha = 0.08f)
                ),
            contentAlignment = Alignment.Center,
        ) {
            Text(
                if (transaction.isInflow) "\u2191" else "\u2193",
                color = if (transaction.isInflow) MainColor else MainColor,
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
            )
        }
        Spacer(Modifier.width(12.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(
                transaction.name,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.onSurface,
            )
            Text(
                transaction.category,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
        Text(
            transaction.amount,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.SemiBold,
            color = if (transaction.isInflow) MainColor else MaterialTheme.colorScheme.onSurface,
        )
    }
}

@Composable
private fun SampleForm() {
    AppCard {
        SectionHeader(title = "Sample Form")
        Spacer(Modifier.height(16.dp))
        AppTextField(
            value = "",
            onValueChange = { },
            label = "Full Name",
            placeholder = "Enter your name",
        )
        Spacer(Modifier.height(12.dp))
        AppTextField(
            value = "",
            onValueChange = { },
            label = "Email Address",
            placeholder = "Enter your email",
        )
    }
}

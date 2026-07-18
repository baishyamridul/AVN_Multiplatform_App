package tech.sumato.avn.mp.feature.dashboard.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
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
import tech.sumato.avn.mp.designsystem.FormFactor
import tech.sumato.avn.mp.designsystem.LocalFormFactor
import tech.sumato.avn.mp.designsystem.components.AppCard
import tech.sumato.avn.mp.designsystem.components.AppTextField
import tech.sumato.avn.mp.designsystem.components.ScreenHeader
import tech.sumato.avn.mp.designsystem.components.SectionHeader
import tech.sumato.avn.mp.designsystem.components.StatCard
import tech.sumato.avn.mp.designsystem.theme.MainColor
import tech.sumato.avn.mp.domain.dashboard.model.DashboardData
import tech.sumato.avn.mp.domain.dashboard.model.RevenueItem
import tech.sumato.avn.mp.domain.dashboard.model.Transaction

@Composable
fun DashboardScreen(
    state: DashboardState,
    onEvent: (DashboardEvent) -> Unit,
) {
    val formFactor = LocalFormFactor.current

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
            when (formFactor) {
                FormFactor.Compact -> DashboardCompact(state.data)
                FormFactor.Medium -> DashboardMedium(state.data)
                FormFactor.Expanded -> DashboardExpanded(state.data)
            }
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
private fun DashboardCompact(data: DashboardData) {
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
        StatsGrid(data, columns = 2)
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
private fun DashboardMedium(data: DashboardData) {
    val scrollState = rememberScrollState()

    Row(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(24.dp),
    ) {
        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(scrollState)
                .padding(end = 12.dp),
        ) {
            ScreenHeader(
                title = "Dashboard",
                subtitle = "Welcome back, here's your overview.",
            )
            Spacer(Modifier.height(20.dp))
            StatsGrid(data, columns = 2)
            Spacer(Modifier.height(20.dp))
            MonthlyRevenue(data)
        }

        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(scrollState)
                .padding(start = 12.dp),
        ) {
            RecentTransactions(data)
            Spacer(Modifier.height(20.dp))
            SampleForm()
        }
    }
}

@Composable
private fun DashboardExpanded(data: DashboardData) {
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .verticalScroll(scrollState)
            .padding(32.dp),
    ) {
        ScreenHeader(
            title = "Dashboard",
            subtitle = "Welcome back, here's your overview.",
        )
        Spacer(Modifier.height(24.dp))
        StatsGrid(data, columns = 4)
        Spacer(Modifier.height(24.dp))

        Row(modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.weight(1f)) {
                MonthlyRevenue(data)
            }
            Spacer(Modifier.width(24.dp))
            Column(modifier = Modifier.weight(1f)) {
                RecentTransactions(data)
            }
        }

        Spacer(Modifier.height(24.dp))
        SampleForm()
        Spacer(Modifier.height(16.dp))
    }
}

@Composable
private fun StatsGrid(data: DashboardData, columns: Int) {
    val stats = data.stats
    val rows = stats.chunked(columns)

    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        rows.forEach { row ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                row.forEach { stat ->
                    StatCard(
                        label = stat.label,
                        value = stat.value,
                        change = stat.change,
                        isPositive = stat.isPositive,
                        modifier = Modifier.weight(1f),
                    )
                }
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
                    .fillMaxHeight()
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
                color = MainColor,
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

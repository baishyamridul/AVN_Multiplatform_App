package tech.sumato.avn.mp.feature.project_dashboard.presentation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import tech.sumato.avn.mp.designsystem.FormFactor
import tech.sumato.avn.mp.designsystem.LocalFormFactor
import tech.sumato.avn.mp.feature.project_dashboard.presentation.event.ProjectDashboardEvent
import tech.sumato.avn.mp.feature.project_dashboard.presentation.screen_variants.ProjectDashboardScreenCompact
import tech.sumato.avn.mp.feature.project_dashboard.presentation.screen_variants.ProjectDashboardScreenExpanded
import tech.sumato.avn.mp.feature.project_dashboard.presentation.screen_variants.ProjectDashboardScreenMedium
import tech.sumato.avn.mp.feature.project_dashboard.presentation.state.ProjectDashboardState

@Composable
fun ProjectDashboardScreen(
    state: ProjectDashboardState,
    onEvent: (ProjectDashboardEvent) -> Unit,
) {
    val formFactor = LocalFormFactor.current

    when (formFactor) {
        FormFactor.Compact -> ProjectDashboardScreenCompact()
        FormFactor.Medium -> ProjectDashboardScreenMedium()
        FormFactor.Expanded -> ProjectDashboardScreenExpanded()
    }
}

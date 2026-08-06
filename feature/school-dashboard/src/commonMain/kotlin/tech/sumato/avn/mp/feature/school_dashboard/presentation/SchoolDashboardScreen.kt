package tech.sumato.avn.mp.feature.school_dashboard.presentation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import tech.sumato.avn.mp.designsystem.FormFactor
import tech.sumato.avn.mp.designsystem.LocalFormFactor
import tech.sumato.avn.mp.feature.school_dashboard.presentation.screen_variants.SchoolDashboardScreenCompact
import tech.sumato.avn.mp.feature.school_dashboard.presentation.screen_variants.SchoolDashboardScreenExpanded
import tech.sumato.avn.mp.feature.school_dashboard.presentation.screen_variants.SchoolDashboardScreenMedium
import tech.sumato.avn.mp.feature.school_dashboard.presentation.event.SchoolDashboardEvent
import tech.sumato.avn.mp.feature.school_dashboard.presentation.state.SchoolDashboardState

@Composable
fun SchoolDashboardScreen(
    state: SchoolDashboardState,
    onEvent: (SchoolDashboardEvent) -> Unit,
) {


    val formFactor = LocalFormFactor.current

    when (formFactor) {
        FormFactor.Compact -> SchoolDashboardScreenCompact()
        FormFactor.Medium -> SchoolDashboardScreenMedium()
        FormFactor.Expanded -> SchoolDashboardScreenExpanded(state = state, onEvent = onEvent)
    }

}

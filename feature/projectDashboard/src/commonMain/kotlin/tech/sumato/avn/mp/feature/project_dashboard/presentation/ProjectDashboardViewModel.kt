package tech.sumato.avn.mp.feature.project_dashboard.presentation

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import tech.sumato.avn.mp.core.navigation.MviViewModel
import tech.sumato.avn.mp.feature.project_dashboard.presentation.effect.ProjectDashboardEffect
import tech.sumato.avn.mp.feature.project_dashboard.presentation.event.ProjectDashboardEvent
import tech.sumato.avn.mp.feature.project_dashboard.presentation.state.ProjectDashboardState

class ProjectDashboardViewModel : ViewModel(), MviViewModel<ProjectDashboardState, ProjectDashboardEffect> {

    private val _state = MutableStateFlow<ProjectDashboardState>(ProjectDashboardState.Loading)
    override val state: StateFlow<ProjectDashboardState> = _state.asStateFlow()

    private val _effects = Channel<ProjectDashboardEffect>(Channel.BUFFERED)
    override val effects: Flow<ProjectDashboardEffect> = _effects.receiveAsFlow()

    fun onEvent(event: ProjectDashboardEvent) {
    }
}

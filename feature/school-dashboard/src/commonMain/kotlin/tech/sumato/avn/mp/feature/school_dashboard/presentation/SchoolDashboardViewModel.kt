package tech.sumato.avn.mp.feature.school_dashboard.presentation

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import tech.sumato.avn.mp.core.navigation.MviViewModel

class SchoolDashboardViewModel : ViewModel(), MviViewModel<SchoolDashboardState, SchoolDashboardEffect> {

    private val _state = MutableStateFlow<SchoolDashboardState>(SchoolDashboardState.Loading)
    override val state: StateFlow<SchoolDashboardState> = _state.asStateFlow()

    private val _effects = Channel<SchoolDashboardEffect>(Channel.BUFFERED)
    override val effects: Flow<SchoolDashboardEffect> = _effects.receiveAsFlow()

    fun onEvent(event: SchoolDashboardEvent) {
        when (event) {
            is SchoolDashboardEvent.Back -> {
                _effects.trySend(SchoolDashboardEffect.NavigateBack)
            }
            else -> {}
        }
    }
}

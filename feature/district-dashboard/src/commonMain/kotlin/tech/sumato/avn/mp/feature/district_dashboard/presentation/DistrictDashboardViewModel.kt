package tech.sumato.avn.mp.feature.district_dashboard.presentation

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import tech.sumato.avn.mp.core.navigation.MviViewModel
import tech.sumato.avn.mp.core.navigation.Route

class DistrictDashboardViewModel : ViewModel(), MviViewModel<DistrictDashboardState, DistrictDashboardEffect> {

    private val _state = MutableStateFlow<DistrictDashboardState>(DistrictDashboardState.Loading)
    override val state: StateFlow<DistrictDashboardState> = _state.asStateFlow()

    private val _effects = Channel<DistrictDashboardEffect>(Channel.BUFFERED)
    override val effects: Flow<DistrictDashboardEffect> = _effects.receiveAsFlow()

    fun onEvent(event: DistrictDashboardEvent) {
        when (event) {
            //
            else -> {}
        }
    }

    fun navigateToSchoolDashboard() {
        _effects.trySend(DistrictDashboardEffect.Navigate(Route.SCHOOL_DASHBOARD))
    }

}

package tech.sumato.avn.mp.feature.district_dashboard.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import tech.sumato.avn.mp.core.navigation.MviViewModel
import tech.sumato.avn.mp.core.navigation.Route
import tech.sumato.avn.mp.domain.districtDashboard.usecase.GetDistrictDashboardDataUseCase
import tech.sumato.avn.mp.domain.user.usecase.LoginUseCase

class DistrictDashboardViewModel(
    private val getDistrictDashboardDataUseCase: GetDistrictDashboardDataUseCase,
    private val loginUseCase: LoginUseCase,
) : ViewModel(), MviViewModel<DistrictDashboardState, DistrictDashboardEffect> {

    private val _state = MutableStateFlow<DistrictDashboardState>(DistrictDashboardState.Loading)
    override val state: StateFlow<DistrictDashboardState> = _state.asStateFlow()

    private val _effects = Channel<DistrictDashboardEffect>(Channel.BUFFERED)
    override val effects: Flow<DistrictDashboardEffect> = _effects.receiveAsFlow()

    init {
        loadData()
    }

    fun onEvent(event: DistrictDashboardEvent) {
        when (event) {
            is DistrictDashboardEvent.LoadData -> loadData(event.districtId)
            is DistrictDashboardEvent.Retry -> loadData(event.districtId)
            is DistrictDashboardEvent.Logout -> logout()
        }
    }


    private fun loadData(districtId: Int = -1) {
        viewModelScope.launch {
            _state.value = DistrictDashboardState.Loading
            try {
                val data = getDistrictDashboardDataUseCase(districtId)
                _state.value = DistrictDashboardState.Success(data)
            } catch (e: Exception) {
                _state.value = DistrictDashboardState.Error(e.message ?: "Unknown error")
                _effects.send(DistrictDashboardEffect.ShowSnackbar("Failed to load dashboard data"))
            }
        }
    }

    fun navigateToSchoolDashboard() {
        _effects.trySend(DistrictDashboardEffect.Navigate(Route.SCHOOL_DASHBOARD))
    }

    private fun logout() {
        viewModelScope.launch {
            loginUseCase.logoutCurrentUser()
            _effects.trySend(DistrictDashboardEffect.Navigate(Route.LOGIN))
        }

    }

}

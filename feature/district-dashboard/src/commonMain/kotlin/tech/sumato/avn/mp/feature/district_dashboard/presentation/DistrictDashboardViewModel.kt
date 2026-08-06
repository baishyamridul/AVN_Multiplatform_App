package tech.sumato.avn.mp.feature.district_dashboard.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Job
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
import tech.sumato.avn.mp.domain.user.usecase.GetStoredUserDetailsUseCase
import tech.sumato.avn.mp.domain.user.usecase.LoginUseCase
import tech.sumato.avn.mp.feature.district_dashboard.presentation.effect.DistrictDashboardEffect
import tech.sumato.avn.mp.feature.district_dashboard.presentation.event.DistrictDashboardEvent
import tech.sumato.avn.mp.feature.district_dashboard.presentation.state.DistrictDashboardState

class DistrictDashboardViewModel(
    private val getDistrictDashboardDataUseCase: GetDistrictDashboardDataUseCase,
    private val getStoredUserDetailsUseCase: GetStoredUserDetailsUseCase,
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
            is DistrictDashboardEvent.Navigate -> handleNavigation(event)
        }
    }

    private fun handleNavigation(event: DistrictDashboardEvent.Navigate) {
        _effects.trySend(DistrictDashboardEffect.Navigate(event.route))
    }


    private var loadJob: Job? = null

    private fun loadData(districtId: Int = -1) {
        if (state.value is DistrictDashboardState.Success && (state.value as DistrictDashboardState.Success).selectedDistrictId == districtId) {
            return
        }
        loadJob?.cancel()
        loadJob = viewModelScope.launch {
            val current = _state.value
            val previousSelectedId =
                (current as? DistrictDashboardState.Success)?.selectedDistrictId ?: -1
            if (current is DistrictDashboardState.Success) {
                _state.value = current.copy(
                    isRefreshing = true,
                    selectedDistrictId = districtId,
                )
            } else {
                _state.value = DistrictDashboardState.Loading
            }
            try {
                val data = getDistrictDashboardDataUseCase(districtId)
                val storedDetails = getStoredUserDetailsUseCase()
                _state.value = DistrictDashboardState.Success(
                    data = data,
                    userDistricts = storedDetails?.districts.orEmpty(),
                    selectedDistrictId = districtId,
                )
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                val latest = _state.value
                if (latest is DistrictDashboardState.Success) {
                    _state.value = latest.copy(
                        isRefreshing = false,
                        selectedDistrictId = previousSelectedId,
                    )
                } else {
                    _state.value = DistrictDashboardState.Error(e.message ?: "Unknown error")
                }
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

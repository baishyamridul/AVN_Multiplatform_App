package tech.sumato.avn.mp.feature.dashboard.presentation

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
import tech.sumato.avn.mp.domain.dashboard.usecase.GetDashboardDataUseCase

class DashboardViewModel(
    private val getDashboardDataUseCase: GetDashboardDataUseCase,
) : ViewModel(), MviViewModel<DashboardState, DashboardEffect> {

    private val _state = MutableStateFlow<DashboardState>(DashboardState.Loading)
    override val state: StateFlow<DashboardState> = _state.asStateFlow()

    private val _effects = Channel<DashboardEffect>(Channel.BUFFERED)
    override val effects = _effects.receiveAsFlow()

    init {
        loadDashboard()
    }

    fun onEvent(event: DashboardEvent) {
        when (event) {
            DashboardEvent.LoadDashboard -> loadDashboard()
            DashboardEvent.Retry -> loadDashboard()
        }
    }

    private fun loadDashboard() {
        viewModelScope.launch {
            _state.value = DashboardState.Loading
            try {
                val data = getDashboardDataUseCase()
                _state.value = DashboardState.Success(data)
            } catch (e: Exception) {
                _state.value = DashboardState.Error(e.message ?: "Unknown error")
                _effects.send(DashboardEffect.ShowSnackbar("Failed to load dashboard data"))
            }
        }
    }
}

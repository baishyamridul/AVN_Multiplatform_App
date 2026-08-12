package tech.sumato.avn.mp.feature.school_dashboard.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import tech.sumato.avn.mp.core.navigation.MviViewModel
import tech.sumato.avn.mp.domain.common.model.exception.ResponseExceptionModel
import tech.sumato.avn.mp.domain.school.usecase.GetSchoolDetailsUseCase
import tech.sumato.avn.mp.domain.school.usecase.GetSchoolsUseCase
import tech.sumato.avn.mp.domain.user.usecase.GetStoredUserDetailsUseCase
import tech.sumato.avn.mp.feature.school_dashboard.presentation.effect.SchoolDashboardEffect
import tech.sumato.avn.mp.feature.school_dashboard.presentation.event.SchoolDashboardEvent
import tech.sumato.avn.mp.feature.school_dashboard.presentation.model.toUiModel
import tech.sumato.avn.mp.feature.school_dashboard.presentation.state.SchoolDashboardState

class SchoolDashboardViewModel(
    private val getSchoolsUseCase: GetSchoolsUseCase,
    private val getSchoolDetailsUseCase: GetSchoolDetailsUseCase,
    private val getStoredUserDetailsUseCase: GetStoredUserDetailsUseCase,
) : ViewModel(), MviViewModel<SchoolDashboardState, SchoolDashboardEffect> {

    private val _state = MutableStateFlow(SchoolDashboardState())
    override val state: StateFlow<SchoolDashboardState> = _state.asStateFlow()

    private val _effects = Channel<SchoolDashboardEffect>(Channel.BUFFERED)
    override val effects: Flow<SchoolDashboardEffect> = _effects.receiveAsFlow()

    init {
        fetchSchools()
        loadDistricts()
    }


    fun onEvent(event: SchoolDashboardEvent) {
        when (event) {
            is SchoolDashboardEvent.Back -> {
                _effects.trySend(SchoolDashboardEffect.NavigateBack)
            }

            is SchoolDashboardEvent.SelectSchool -> {
                _state.update { state ->
                    state.copy(
                        schoolsState = state.schoolsState.copy(
                            selectedSchoolId = event.schoolId
                        )
                    )
                }
            }

            is SchoolDashboardEvent.ClearSchoolSelection -> {
                _state.update { state ->
                    state.copy(
                        schoolsState = state.schoolsState.copy(
                            selectedSchoolId = null
                        )
                    )
                }
            }

            is SchoolDashboardEvent.LoadSchoolDetails -> {
                loadSchoolDetails(schoolId = event.schoolId)
            }

            is SchoolDashboardEvent.ClearSchoolDetails -> {
                clearSchoolDetails()
            }

            is SchoolDashboardEvent.UpdateSearchQuery -> {
                _state.update { state ->
                    state.copy(
                        schoolsState = state.schoolsState.copy(
                            searchQuery = event.query
                        )
                    )
                }
            }

            is SchoolDashboardEvent.SelectDistrict -> {
                _state.update { state ->
                    state.copy(
                        schoolsState = state.schoolsState.copy(
                            selectedDistrictId = event.districtId
                        )
                    )
                }
            }

            is SchoolDashboardEvent.SelectCategory -> {
                _state.update { state ->
                    state.copy(
                        schoolsState = state.schoolsState.copy(
                            selectedCategory = event.category
                        )
                    )
                }
            }

            is SchoolDashboardEvent.SelectSortOption -> {
                _state.update { state ->
                    state.copy(
                        schoolsState = state.schoolsState.copy(
                            sortOption = event.option
                        )
                    )
                }
            }
        }
    }


    fun preselectDistrict(districtId: Int) {
        _state.update { state ->
            state.copy(
                schoolsState = state.schoolsState.copy(
                    selectedDistrictId = districtId
                )
            )
        }
    }


    private fun loadDistricts() {
        viewModelScope.launch {
            try {
                val storedDetails = getStoredUserDetailsUseCase()
                _state.update { state ->
                    state.copy(
                        schoolsState = state.schoolsState.copy(
                            districts = storedDetails?.districts.orEmpty()
                        )
                    )
                }
            } catch (e: ResponseExceptionModel) {
                //
            }
        }
    }


    private fun fetchSchools(districtId: Int = -1) {
        viewModelScope.launch {
            _state.update { state ->
                state.copy(
                    schoolsState = state.schoolsState.copy(
                        isLoading = true
                    )
                )
            }

            try {
                val response = getSchoolsUseCase.invoke(districtId = districtId)
                _state.update { state ->
                    state.copy(
                        schoolsState = state.schoolsState.copy(
                            isLoading = false,
                            schools = response
                        )
                    )
                }
            } catch (e: ResponseExceptionModel) {
                _state.update { state ->
                    state.copy(
                        schoolsState = state.schoolsState.copy(
                            isLoading = false
                        )
                    )
                }
            }
        }
    }


    private fun loadSchoolDetails(schoolId: String) {
        viewModelScope.launch {
            _state.update { state ->
                state.copy(
                    schoolsState = state.schoolsState.copy(
                        isSchoolDetailsLoading = true
                    )
                )
            }

            try {
                val response = getSchoolDetailsUseCase.invoke(schoolId = schoolId)
                _state.update { state ->
                    state.copy(
                        schoolsState = state.schoolsState.copy(
                            isSchoolDetailsLoading = false,
                            schoolDetails = response.toUiModel()
                        )
                    )
                }
            } catch (e: ResponseExceptionModel) {
                _state.update { state ->
                    state.copy(
                        schoolsState = state.schoolsState.copy(
                            isSchoolDetailsLoading = false,
                            schoolDetails = null
                        )
                    )
                }
            }
        }
    }


    private fun clearSchoolDetails() {
        _state.update { state ->
            state.copy(
                schoolsState = state.schoolsState.copy(
                    isSchoolDetailsLoading = false,
                    schoolDetails = null
                )
            )
        }
    }

}

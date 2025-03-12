package com.rperez.thermostat.mvvm.ui.screens.home

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getStatusUseCase: GetThermostatStatusUseCase,
    private val setTemperatureUseCase: SetTemperatureUseCase,
    private val changeModeUseCase: ChangeModeUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<Thermostat?>(null)
    val uiState: StateFlow<Thermostat?> = _uiState.asStateFlow()

    fun loadThermostat(id: String) {
        viewModelScope.launch {
            _uiState.value = getStatusUseCase(id)
        }
    }

    fun changeTemperature(id: String, temp: Float) {
        viewModelScope.launch {
            setTemperatureUseCase(id, temp)
            _uiState.value = _uiState.value?.copy(targetTemperature = temp)
        }
    }

    fun changeMode(id: String, mode: ThermostatMode) {
        viewModelScope.launch {
            changeModeUseCase(id, mode)
            _uiState.value = _uiState.value?.copy(mode = mode)
        }
    }
}

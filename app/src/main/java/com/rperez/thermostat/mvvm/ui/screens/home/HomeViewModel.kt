package com.rperez.thermostat.mvvm.ui.screens.home

import dagger.hilt.android.lifecycle.HiltViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rperez.thermostat.mvvm.domain.usecase.GetThermostatStatusUseCase
import com.rperez.thermostat.mvvm.domain.usecase.SetTemperatureUseCase
import com.rperez.thermostat.mvvm.domain.usecase.ChangeModeUseCase
import com.rperez.thermostat.mvvm.data.model.Thermostat
import com.rperez.thermostat.mvvm.data.model.ThermostatMode
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

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

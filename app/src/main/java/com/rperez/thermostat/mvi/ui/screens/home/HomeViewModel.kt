package com.rperez.thermostat.mvi.ui.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rperez.thermostat.mvi.data.model.ThermostatMode
import com.rperez.thermostat.mvi.data.repository.ThermostatRepository
import com.rperez.thermostat.mvi.domain.intent.ThermostatIntent
import com.rperez.thermostat.mvi.domain.state.ThermostatState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: ThermostatRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<ThermostatState>(ThermostatState.Loading)
    val uiState: StateFlow<ThermostatState> = _uiState.asStateFlow()

    fun processIntent(intent: ThermostatIntent) {
        viewModelScope.launch {
            when (intent) {
                is ThermostatIntent.LoadThermostat -> loadThermostat(intent.id)
                is ThermostatIntent.ChangeTemperature -> changeTemperature(intent.id, intent.temp)
                is ThermostatIntent.ChangeMode -> changeMode(intent.id, intent.mode)
            }
        }
    }

    private suspend fun loadThermostat(id: String) {
        _uiState.value = ThermostatState.Loading
        try {
            val thermostat = repository.getStatus(id)
            _uiState.value = ThermostatState.Success(thermostat)
        } catch (e: Exception) {
            _uiState.value = ThermostatState.Error("Failed to load thermostat")
        }
    }

    private suspend fun changeTemperature(id: String, temp: Float) {
        try {
            repository.setTemperature(id, temp)
            (_uiState.value as? ThermostatState.Success)?.let {
                _uiState.value =
                    ThermostatState.Success(it.thermostat.copy(targetTemperature = temp))
            }
        } catch (e: Exception) {
            _uiState.value = ThermostatState.Error("Failed to update temperature")
        }
    }

    private suspend fun changeMode(id: String, mode: ThermostatMode) {
        try {
            repository.changeMode(id, mode)
            (_uiState.value as? ThermostatState.Success)?.let {
                _uiState.value = ThermostatState.Success(it.thermostat.copy(mode = mode))
            }
        } catch (e: Exception) {
            _uiState.value = ThermostatState.Error("Failed to change mode")
        }
    }
}

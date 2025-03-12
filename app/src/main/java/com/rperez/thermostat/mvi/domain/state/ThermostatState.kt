package com.rperez.thermostat.mvi.domain.state

import com.rperez.thermostat.mvi.data.model.Thermostat

sealed class ThermostatState {
    object Loading : ThermostatState()
    data class Success(val thermostat: Thermostat) : ThermostatState()
    data class Error(val message: String) : ThermostatState()
}

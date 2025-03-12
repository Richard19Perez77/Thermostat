package com.rperez.thermostat.mvi.domain.intent

import com.rperez.thermostat.mvi.data.model.ThermostatMode

sealed class ThermostatIntent {
    data class LoadThermostat(val id: String) : ThermostatIntent()
    data class ChangeTemperature(val id: String, val temp: Float) : ThermostatIntent()
    data class ChangeMode(val id: String, val mode: ThermostatMode) : ThermostatIntent()
}

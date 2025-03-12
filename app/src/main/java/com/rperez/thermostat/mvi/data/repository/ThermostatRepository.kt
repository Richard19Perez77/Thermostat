package com.rperez.thermostat.mvi.data.repository

import com.rperez.thermostat.mvi.data.model.Thermostat
import com.rperez.thermostat.mvi.data.model.ThermostatMode
import com.rperez.thermostat.mvi.data.remote.ThermostatApi

class ThermostatRepository(private val api: ThermostatApi) {
    suspend fun getStatus(id: String): Thermostat = api.getThermostatStatus(id)
    suspend fun setTemperature(id: String, temp: Float) = api.setTemperature(id, temp)
    suspend fun changeMode(id: String, mode: ThermostatMode) = api.changeMode(id, mode)
}

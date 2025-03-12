package com.rperez.thermostat.mvvm.data.repository

import com.rperez.thermostat.mvvm.data.model.Thermostat
import com.rperez.thermostat.mvvm.data.model.ThermostatMode
import com.rperez.thermostat.mvvm.data.remote.ThermostatApi

class ThermostatRepository(private val api: ThermostatApi) {
    suspend fun getStatus(id: String): Thermostat = api.getThermostatStatus(id)
    suspend fun setTemperature(id: String, temp: Float) = api.setTemperature(id, temp)
    suspend fun changeMode(id: String, mode: ThermostatMode) = api.changeMode(id, mode)
}

package com.rperez.thermostat.mvi.data.model

data class Thermostat(
    val id: String,
    val currentTemperature: Float,
    val targetTemperature: Float,
    val mode: ThermostatMode,
    val isOnline: Boolean
)
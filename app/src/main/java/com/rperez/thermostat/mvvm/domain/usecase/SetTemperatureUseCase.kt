package com.rperez.thermostat.mvvm.domain.usecase

import com.rperez.thermostat.mvvm.data.repository.ThermostatRepository

class SetTemperatureUseCase(private val repository: ThermostatRepository) {
    suspend operator fun invoke(id: String, temp: Float) = repository.setTemperature(id, temp)
}

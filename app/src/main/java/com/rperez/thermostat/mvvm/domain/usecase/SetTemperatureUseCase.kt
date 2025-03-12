package com.rperez.thermostat.mvvm.domain.usecase

class SetTemperatureUseCase(private val repository: ThermostatRepository) {
    suspend operator fun invoke(id: String, temp: Float) = repository.setTemperature(id, temp)
}

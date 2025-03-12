package com.rperez.thermostat.mvvm.domain.usecase

import com.rperez.thermostat.mvvm.data.repository.ThermostatRepository

class GetThermostatStatusUseCase(private val repository: ThermostatRepository) {
    suspend operator fun invoke(id: String) = repository.getStatus(id)
}

package com.rperez.thermostat.mvvm.domain.usecase

class GetThermostatStatusUseCase(private val repository: ThermostatRepository) {
    suspend operator fun invoke(id: String) = repository.getStatus(id)
}

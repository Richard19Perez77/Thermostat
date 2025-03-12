package com.rperez.thermostat.mvvm.domain.usecase


class ChangeModeUseCase(private val repository: ThermostatRepository) {
    suspend operator fun invoke(id: String, mode: ThermostatMode) = repository.changeMode(id, mode)
}
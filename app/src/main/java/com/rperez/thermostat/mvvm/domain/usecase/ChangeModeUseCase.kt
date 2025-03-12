package com.rperez.thermostat.mvvm.domain.usecase

import com.rperez.thermostat.mvvm.data.model.ThermostatMode
import com.rperez.thermostat.mvvm.data.repository.ThermostatRepository


class ChangeModeUseCase(private val repository: ThermostatRepository) {
    suspend operator fun invoke(id: String, mode: ThermostatMode) = repository.changeMode(id, mode)
}
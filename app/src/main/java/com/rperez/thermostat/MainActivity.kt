package com.rperez.thermostat


import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.rperez.thermostat.ui.theme.ThermostatTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ThermostatTheme {
            }
        }
    }
}

/**
 * MVVM
 *
 * +------------------------------------------------+
 * |                 ThermostatApp                  |
 * +------------------------------------------------+
 *           │
 *           │
 *           ▼
 * +------------------------------------------------+
 * |                  UI Layer                      |
 * +------------------------------------------------+
 * | - HomeScreen.kt                                |
 * | - Other Composables (TemperatureControl, etc.) |
 * +------------------------------------------------+
 *           │
 *           ▼
 * +-----------------------------------+
 * |        HomeViewModel.kt           |
 * +-----------------------------------+
 * | - _uiState: MutableStateFlow<Thermostat?> |
 * | - uiState: StateFlow<Thermostat?>        |
 * | - loadThermostat(id: String)             |
 * | - changeTemperature(id: String, temp: Float) |
 * | - changeMode(id: String, mode: ThermostatMode) |
 * +-----------------------------------+
 *           │
 *           ▼
 * +-----------------------------------+
 * |       Use Case Layer (Optional)  |
 * +-----------------------------------+
 * | - GetThermostatStatusUseCase.kt  |
 * | - SetTemperatureUseCase.kt       |
 * | - ChangeModeUseCase.kt           |
 * +-----------------------------------+
 *           │
 *           ▼
 * +------------------------------------------------+
 * |                 Repository Layer               |
 * +------------------------------------------------+
 * | - ThermostatRepository.kt                      |
 * |    - getStatus(id: String): Thermostat        |
 * |    - setTemperature(id: String, temp: Float)  |
 * |    - changeMode(id: String, mode: ThermostatMode) |
 * +------------------------------------------------+
 *           │
 *           ▼
 * +------------------------------------------------+
 * |                 Data Layer                     |
 * +------------------------------------------------+
 * | - Thermostat.kt                                |
 * | - ThermostatMode.kt                            |
 * | - ThermostatApi.kt                             |
 * | - WebSocketManager.kt                          |
 * +------------------------------------------------+
 *
 */

/**
 * MVI
 *
 * +------------------------------------------------+
 * |                 ThermostatApp                  |
 * +------------------------------------------------+
 *           │
 *           ▼
 * +------------------------------------------------+
 * |                  UI Layer                      |
 * +------------------------------------------------+
 * | - HomeScreen.kt                                |
 * | - Other Composables (TemperatureControl, etc.) |
 * +------------------------------------------------+
 *           │
 *           ▼
 * +-----------------------------------------------+
 * |        ViewModel Layer (HomeViewModel.kt)     |
 * +-----------------------------------------------+
 * | - _uiState: MutableStateFlow<ThermostatState> |
 * | - uiState: StateFlow<ThermostatState>         |
 * | - processIntent(intent: ThermostatIntent)     |
 * | - loadThermostat(id: String)                  |
 * | - changeTemperature(id: String, temp: Float)  |
 * | - changeMode(id: String, mode: ThermostatMode)|
 * +-----------------------------------------------+
 *           │
 *           ▼
 * +-------------------------------------------------+
 * |          Intent Layer (ThermostatIntent.kt)     |
 * +-------------------------------------------------+
 * | - Sealed class representing user actions        |
 * |   - LoadThermostat(val id: String)              |
 * |   - ChangeTemperature(val id: String, val temp: Float) |
 * |   - ChangeMode(val id: String, val mode: ThermostatMode) |
 * +-------------------------------------------------+
 *           │
 *           ▼
 * +-------------------------------------------------+
 * |          State Layer (ThermostatState.kt)       |
 * +-------------------------------------------------+
 * | - Sealed class representing UI states           |
 * |   - Loading                                    |
 * |   - Success(val thermostat: Thermostat)        |
 * |   - Error(val message: String)                 |
 * +-------------------------------------------------+
 *           │
 *           ▼
 * +------------------------------------------------+
 * |                 Repository Layer               |
 * +------------------------------------------------+
 * | - ThermostatRepository.kt                      |
 * |    - getStatus(id: String): Thermostat        |
 * |    - setTemperature(id: String, temp: Float)  |
 * |    - changeMode(id: String, mode: ThermostatMode) |
 * +------------------------------------------------+
 *           │
 *           ▼
 * +------------------------------------------------+
 * |                 Data Layer                     |
 * +------------------------------------------------+
 * | - Thermostat.kt                                |
 * | - ThermostatMode.kt                            |
 * | - ThermostatApi.kt                             |
 * | - WebSocketManager.kt                          |
 * +------------------------------------------------+
 *
 */
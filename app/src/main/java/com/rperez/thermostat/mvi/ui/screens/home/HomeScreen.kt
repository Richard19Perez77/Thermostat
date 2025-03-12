package com.rperez.thermostat.mvi.ui.screens.home

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.rperez.thermostat.mvi.data.model.ThermostatMode
import com.rperez.thermostat.mvi.domain.intent.ThermostatIntent
import com.rperez.thermostat.mvi.domain.state.ThermostatState

@Composable
fun HomeScreen(viewModel: HomeViewModel = hiltViewModel()) {
    val uiState by viewModel.uiState.collectAsState()

    when (uiState) {
        is ThermostatState.Loading -> Text("Loading...")
        is ThermostatState.Error -> Text("Error: ${(uiState as ThermostatState.Error).message}")
        is ThermostatState.Success -> {
            val thermostat = (uiState as ThermostatState.Success).thermostat

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text("Current Temp: ${thermostat.currentTemperature}°F", fontSize = 24.sp)

                Slider(
                    value = thermostat.targetTemperature,
                    onValueChange = {
                        viewModel.processIntent(
                            ThermostatIntent.ChangeTemperature(
                                thermostat.id,
                                it
                            )
                        )
                    },
                    valueRange = 50f..90f
                )

                Spacer(modifier = Modifier.height(16.dp))

                Row {
                    Button(onClick = {
                        viewModel.processIntent(
                            ThermostatIntent.ChangeMode(
                                thermostat.id,
                                ThermostatMode.HEAT
                            )
                        )
                    }) {
                        Text("Heat")
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(onClick = {
                        viewModel.processIntent(
                            ThermostatIntent.ChangeMode(
                                thermostat.id,
                                ThermostatMode.COOL
                            )
                        )
                    }) {
                        Text("Cool")
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(onClick = {
                        viewModel.processIntent(
                            ThermostatIntent.ChangeMode(
                                thermostat.id,
                                ThermostatMode.OFF
                            )
                        )
                    }) {
                        Text("Off")
                    }
                }
            }
        }
    }
}

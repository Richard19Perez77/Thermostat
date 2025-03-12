package com.rperez.thermostat.mvvm.ui.screens.home

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.rperez.thermostat.mvvm.data.model.ThermostatMode

@Composable
fun HomeScreen(viewModel: HomeViewModel = hiltViewModel()) {
    val uiState by viewModel.uiState.collectAsState()

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        uiState?.let { thermostat ->
            Text("Current Temp: ${thermostat.currentTemperature}°F", fontSize = 24.sp)

            Slider(
                value = thermostat.targetTemperature,
                onValueChange = { viewModel.changeTemperature(thermostat.id, it) },
                valueRange = 50f..90f
            )

            Spacer(modifier = Modifier.height(16.dp))

            Row {
                Button(onClick = { viewModel.changeMode(thermostat.id, ThermostatMode.HEAT) }) {
                    Text("Heat")
                }
                Spacer(modifier = Modifier.width(8.dp))
                Button(onClick = { viewModel.changeMode(thermostat.id, ThermostatMode.COOL) }) {
                    Text("Cool")
                }
                Spacer(modifier = Modifier.width(8.dp))
                Button(onClick = { viewModel.changeMode(thermostat.id, ThermostatMode.OFF) }) {
                    Text("Off")
                }
            }
        } ?: Text("Loading...", fontSize = 18.sp)
    }
}

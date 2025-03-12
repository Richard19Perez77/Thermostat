package com.rperez.thermostat.mvvm.ui.screens.home

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

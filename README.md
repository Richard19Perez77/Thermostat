# Thermostat

Connected-thermostat UI (mode + temperature) implemented twice:

- **MVI** — intents, state, WebSocket manager
- **MVVM** — use cases, Hilt module, same screens

Also includes small bug reproductions under `bug/` (race conditions, inconsistent UI).

## Stack

Kotlin, Jetpack Compose, Retrofit/WebSockets, Hilt.

## Run

Open the project in Android Studio and run the `app` configuration.

```bash
./gradlew :app:assembleDebug
```

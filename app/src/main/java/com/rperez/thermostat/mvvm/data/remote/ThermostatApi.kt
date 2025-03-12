package com.rperez.thermostat.mvvm.data.remote

import com.rperez.thermostat.mvvm.data.model.Thermostat
import com.rperez.thermostat.mvvm.data.model.ThermostatMode
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface ThermostatApi {
    @GET("thermostat/{id}/status")
    suspend fun getThermostatStatus(@Path("id") id: String): Thermostat

    @POST("thermostat/{id}/temperature")
    suspend fun setTemperature(@Path("id") id: String, @Body temp: Float)

    @POST("thermostat/{id}/mode")
    suspend fun changeMode(@Path("id") id: String, @Body mode: ThermostatMode)
}

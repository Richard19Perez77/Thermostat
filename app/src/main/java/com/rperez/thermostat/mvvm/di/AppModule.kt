package com.rperez.thermostat.mvvm.di

import com.rperez.thermostat.mvvm.data.remote.ThermostatApi
import com.rperez.thermostat.mvvm.data.repository.ThermostatRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    fun provideThermostatApi(): ThermostatApi {
        return Retrofit.Builder()
            .baseUrl("https://api.yourbackend.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ThermostatApi::class.java)
    }

    @Provides
    fun provideThermostatRepository(api: ThermostatApi) = ThermostatRepository(api)
}

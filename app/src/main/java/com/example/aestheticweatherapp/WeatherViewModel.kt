package com.example.aestheticweatherapp

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

// Retrofit API interface
interface WeatherApi {
    @GET("data/2.5/weather")
    suspend fun getCurrentWeather(
        @Query("q") cityName: String,
        @Query("appid") apiKey: String,
        @Query("units") units: String = "metric"
    ): WeatherResponse
}

// Data classes for API response
data class WeatherResponse(
    val name: String,
    val main: Main
)

data class Main(
    val temp: Double
)

class WeatherViewModel : ViewModel() {

    private val _weather = MutableStateFlow("Loading weather...")
    val weather: StateFlow<String> = _weather

    private val retrofit = Retrofit.Builder()
        .baseUrl("https://api.openweathermap.org/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val weatherApi = retrofit.create(WeatherApi::class.java)

    fun fetchWeather(city: String, apiKey: String) {
        viewModelScope.launch {
            try {
                val response = weatherApi.getCurrentWeather(city, apiKey)
                val temp = response.main.temp
                val cityName = response.name
                _weather.value = "Weather in $cityName: $temp°C"
            } catch (e: Exception) {
                _weather.value = "Failed to load weather: ${e.message}"
            }
        }
    }
}

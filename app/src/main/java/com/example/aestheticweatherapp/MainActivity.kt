package com.example.aestheticweatherapp

import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.aestheticweatherapp.network.RetrofitInstance
import com.example.aestheticweatherapp.network.WeatherResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {

    private lateinit var cityInput: EditText
    private lateinit var searchButton: Button
    private lateinit var cityNameText: TextView
    private lateinit var temperatureText: TextView
    private lateinit var descriptionText: TextView
    private lateinit var humidityText: TextView
    private lateinit var windSpeedText: TextView

    private val apiKey = "1479dc4ee882095703fb03bd683f9446"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        cityInput = findViewById(R.id.cityInput)
        searchButton = findViewById(R.id.searchButton)
        cityNameText = findViewById(R.id.cityNameText)
        temperatureText = findViewById(R.id.temperatureText)
        descriptionText = findViewById(R.id.descriptionText)
        humidityText = findViewById(R.id.humidityText)
        windSpeedText = findViewById(R.id.windSpeedText)

        searchButton.setOnClickListener {
            val city = cityInput.text.toString()
            if (city.isNotBlank()) {
                getWeatherData(city)
            } else {
                Toast.makeText(this, "Enter a city name!", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun getWeatherData(cityName: String) {
        val call = RetrofitInstance.api.getCurrentWeather(cityName, "metric", apiKey)

        call.enqueue(object : Callback<WeatherResponse> {
            override fun onResponse(call: Call<WeatherResponse>, response: Response<WeatherResponse>) {
                if (response.isSuccessful && response.body() != null) {
                    val data = response.body()!!

                    cityNameText.text = data.name
                    temperatureText.text = "Temperature: ${data.main.temp} °C"
                    val desc = data.weather[0].description
                    descriptionText.text = "Description: ${desc.replaceFirstChar { it.uppercaseChar() }}"
                    humidityText.text = "Humidity: ${data.main.humidity}%"
                    windSpeedText.text = "Wind Speed: ${data.wind.speed} m/s"
                } else {
                    Toast.makeText(this@MainActivity, "City not found 😕", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<WeatherResponse>, t: Throwable) {
                Toast.makeText(this@MainActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
}

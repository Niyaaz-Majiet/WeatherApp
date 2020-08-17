package com.example.weatherapp.activities

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Looper
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import com.example.weatherapp.R
import com.example.weatherapp.models.WeatherResponse
import com.example.weatherapp.services.OkHttpClient
import com.google.android.material.textfield.TextInputEditText
import com.google.gson.Gson
import com.squareup.picasso.Picasso
import okhttp3.Call
import okhttp3.Callback
import okhttp3.Response
import java.io.IOException

class MainActivity : AppCompatActivity() {
    private val weatherService: OkHttpClient = OkHttpClient()
    private var etCity: TextInputEditText? = null
    private var loadingSpinner: ProgressBar? = null
    private var ivWeatherIcon: ImageView? = null
    private var tvMain: TextView? = null
    private var tvDescription: TextView? = null
    private var tvTemp: TextView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        etCity = findViewById<TextInputEditText>(R.id.etCityText)
        loadingSpinner = findViewById<ProgressBar>(R.id.loadingSpinner)
        ivWeatherIcon = findViewById<ImageView>(R.id.ivWeatherIcon)
        tvMain = findViewById<TextView>(R.id.tvMain)
        tvDescription = findViewById<TextView>(R.id.tvDescription)
        tvTemp = findViewById<TextView>(R.id.tvTemp)
    }

    fun searchCity(view: View) {
        val city = etCity?.text
        val apiKey = "5c1ca109c8a2be44a6d00021ec27454e"

        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
        imm?.hideSoftInputFromWindow(view.windowToken, 0)

        view.clearFocus();

        loadingSpinner?.visibility = View.VISIBLE

        this.weatherService.getWeatherDataByCityName("https://api.openweathermap.org/data/2.5/weather?q=${city}&appid=${apiKey}")
            .enqueue(object :
                Callback {
                override fun onFailure(call: Call, e: IOException) {
                    e.printStackTrace()
                    runOnUiThread {
                        Toast.makeText(
                            this@MainActivity,
                            "An Error Occurred. Please Try Again Later ...",
                            Toast.LENGTH_SHORT
                        ).show()
                        loadingSpinner?.visibility = View.GONE
                        resetFormData()
                    }

                }

                override fun onResponse(call: Call, response: Response) {
                    response.use {
                        if (!response.isSuccessful) {
                            runOnUiThread {
                                Toast.makeText(
                                    this@MainActivity,
                                    "An Error Occurred. City Not Found ...",
                                    Toast.LENGTH_SHORT
                                ).show()
                                loadingSpinner?.visibility = View.GONE
                                resetFormData()
                            }
                        } else {
                            val gson = Gson()
                            val picasso = Picasso.get()
                            val weatherData: WeatherResponse =
                                gson.fromJson(
                                    response.body()?.string(),
                                    WeatherResponse::class.java
                                )


                            val degrees:Double = String.format("%.2f", (weatherData.main.temp - 273.15)).toDouble()

                            runOnUiThread {
                                picasso.load("http://openweathermap.org/img/wn/${weatherData.weather[0].icon}@2x.png")
                                    .placeholder(R.drawable.placeholder)
                                    .into(ivWeatherIcon)
                                tvMain?.text = weatherData.weather[0].main
                                tvDescription?.text = weatherData.weather[0].description
                                tvTemp?.text = "$degrees°C"
                                loadingSpinner?.visibility = View.GONE
                            }
                        }
                    }
                }

            })
    }

    fun resetFormData() {
        tvMain?.text = "..."
        tvDescription?.text = "..."
        tvTemp?.text = "..."
        etCity?.setText("")
        ivWeatherIcon?.setImageResource(R.drawable.placeholder)
    }

    fun resetForm(view: View) {
        this.resetFormData()
    }
}
package com.example.weatherapp.services

import okhttp3.Call
import okhttp3.OkHttpClient
import okhttp3.Request


class OkHttpClient{
    private var client : OkHttpClient = OkHttpClient()

    fun getWeatherDataByCityName(urlInput: String): Call {
        val request = Request.Builder()
            .url(urlInput)
            .build()

        return this.client.newCall(request)
    }

}
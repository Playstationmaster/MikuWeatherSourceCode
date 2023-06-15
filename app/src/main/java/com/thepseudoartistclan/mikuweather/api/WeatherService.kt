package com.thepseudoartistclan.mikuweather.api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object WeatherService {
    private const val BASE_URL = "https://api.weatherapi.com/v1/"
    fun getInstance() : WeatherInstance {
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        return retrofit.create(WeatherInstance::class.java)
    }
}
package com.thepseudoartistclan.mikuweather.api

import com.thepseudoartistclan.mikuweather.modal.Weather
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherInstance {
    @GET("forecast.json")
    suspend fun getCurrentTemp(@Query("key")key : String, @Query("q")query : String, @Query("days")days : Int, @Query("aqi")aqi: String, @Query("alerts")alert: String) : Response<Weather>
}
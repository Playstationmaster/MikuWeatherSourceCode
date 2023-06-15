package com.thepseudoartistclan.mikuweather.repository

import com.thepseudoartistclan.mikuweather.api.WeatherInstance

class WeatherRepository(private val instance: WeatherInstance) {
    suspend fun getCurrentWeather(key: String, query : String, days : Int) = instance.getCurrentTemp(key,query,days, "no", "no")
}
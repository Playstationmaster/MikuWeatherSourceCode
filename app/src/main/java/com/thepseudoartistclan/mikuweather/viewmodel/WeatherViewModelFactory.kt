package com.thepseudoartistclan.mikuweather.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.thepseudoartistclan.mikuweather.repository.WeatherRepository

class WeatherViewModelFactory(val weatherRepository: WeatherRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(WeatherViewModel::class.java)) {
            WeatherViewModel(this.weatherRepository) as T
        } else {
            throw IllegalArgumentException("ViewModel Not Found")
        }
    }
}
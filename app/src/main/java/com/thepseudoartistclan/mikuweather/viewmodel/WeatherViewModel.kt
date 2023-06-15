package com.thepseudoartistclan.mikuweather.viewmodel

import android.content.ContentValues.TAG
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.thepseudoartistclan.mikuweather.modal.Weather
import com.thepseudoartistclan.mikuweather.repository.WeatherRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class WeatherViewModel(private val repository: WeatherRepository) : ViewModel() {
    val weather : MutableLiveData<Weather> = MutableLiveData()

    //WeatherAPI API key
    val apikey = "12eb644962ed4576b4d181719230306"

    fun getCurrentTemp(query: String) {
        CoroutineScope(Dispatchers.IO).launch {
            val response = repository.getCurrentWeather(apikey, query,2)
            withContext(Dispatchers.Main) {
                if (response.isSuccessful) {
                    weather.value = response.body()
                }
                else {
                    Log.d(TAG, "Error fetching data")
                }
            }
        }
    }
}
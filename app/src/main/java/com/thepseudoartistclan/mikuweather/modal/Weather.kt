package com.thepseudoartistclan.mikuweather.modal

data class Weather(
    val current: Current,
    val forecast: Forecast,
    val location: Location
)

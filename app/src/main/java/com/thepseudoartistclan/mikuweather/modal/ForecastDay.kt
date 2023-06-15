package com.thepseudoartistclan.mikuweather.modal

data class ForecastDay(
    val astro: Astro,
    val date: String,
    val date_epoch: Int,
    val day: Day,
    val hour: List<Hour>
)

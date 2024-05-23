package com.thepseudoartistclan.mikuweather

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.thepseudoartistclan.mikuweather.modal.Hour

class ForecastAdapter(private var forecastList: ArrayList<Hour>): RecyclerView.Adapter<ForecastAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) : ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_item, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount() = forecastList.size
    override fun onBindViewHolder(forecastHolder: ViewHolder, position: Int) {
        val forecast = forecastList[position]
        forecastHolder.bind(forecast)
    }

    inner class ViewHolder(private val view: View) : RecyclerView.ViewHolder(view), View.OnClickListener  {
        private lateinit var forecast: Hour
        init {
            itemView.setOnClickListener(this)
        }
        override fun onClick(v: View) {
        }

        fun bind(forecast: Hour) {
            this.forecast = forecast

            val weatherIcon: ImageView = view.findViewById(R.id.forecastWeatherIcon)
            val timeView: TextView = view.findViewById(R.id.forecastTime)
            val tempView: TextView = view.findViewById(R.id.forecastTemp)
            val conditionView: TextView = view.findViewById(R.id.forecastCondition)

            timeView.text = forecast.time
            val temperature = forecast.temp_c.toString() + " °C"
            tempView.text = temperature
            conditionView.text = forecast.condition.text

            var dayCheck = false
            if(forecast.is_day == 1) dayCheck = true
            when(forecast.condition.code) {
                //Sunny
                1000 -> (
                        if(dayCheck) {
                            weatherIcon.setImageResource(R.mipmap.tenki_hare)
                            conditionView.setText(R.string.code1000_day)
                        } else {
                            weatherIcon.setImageResource(R.mipmap.tenki_hare_yoru)
                            conditionView.setText(R.string.code1000_night)
                        })
                //Partly Cloudy
                1003 -> (
                        if(dayCheck) {
                            weatherIcon.setImageResource(R.mipmap.tenki_hare_kumori)
                            conditionView.setText(R.string.code1003_day)
                        } else{
                            weatherIcon.setImageResource(R.mipmap.tenki_hare_kumori_yoru)
                            conditionView.setText(R.string.code1003_night)
                        })
                //Cloudy
                1006 -> (
                        if(dayCheck) {
                            weatherIcon.setImageResource(R.mipmap.tenki_kumori_hare)
                            conditionView.setText(R.string.code1006_day)
                        } else{
                            weatherIcon.setImageResource(R.mipmap.tenki_kumori_hare_yoru)
                            conditionView.setText(R.string.code1006_night)
                        })
                //Overcast
                1009 -> (
                        if(dayCheck) {
                            weatherIcon.setImageResource(R.mipmap.tenki_kumori)
                            conditionView.setText(R.string.code1009_night)
                        } else{
                            weatherIcon.setImageResource(R.mipmap.tenki_kumori_hare_yoru)
                            conditionView.setText(R.string.code1009_night)
                        })
                //Mist
                1030 -> (
                        if(dayCheck) {
                            weatherIcon.setImageResource(R.mipmap.tenki_kumori)
                            conditionView.setText(R.string.code1030_day)
                        } else{
                            weatherIcon.setImageResource(R.mipmap.tenki_kumori_hare_yoru)
                            conditionView.setText(R.string.code1030_night)
                        })
                //Patchy Rain Possible
                1063 -> (
                        if(dayCheck) {
                            weatherIcon.setImageResource(R.mipmap.tenki_ame_hare)
                            conditionView.setText(R.string.code1063_day)
                        } else{
                            weatherIcon.setImageResource(R.mipmap.tenki_ame_hare_yoru)
                            conditionView.setText(R.string.code1063_night)
                        })
                //Patchy Snow Possible
                1066 -> (
                        if(dayCheck) {
                            weatherIcon.setImageResource(R.mipmap.tenki_hare_yuki)
                            conditionView.setText(R.string.code1066_day)
                        } else{
                            weatherIcon.setImageResource(R.mipmap.tenki_hare_yuki_yoru)
                            conditionView.setText(R.string.code1066_day)
                        })
                //Patchy Sleet Possible
                1069 -> (
                        if(dayCheck) {
                            weatherIcon.setImageResource(R.mipmap.tenki_yuki)
                            conditionView.setText(R.string.code1069_day)
                        } else{
                            weatherIcon.setImageResource(R.mipmap.tenki_yuki_hare_yoru)
                            conditionView.setText(R.string.code1069_day)
                        })
                //Patchy Freezing Drizzle Possible
                1072 -> (
                        if(dayCheck) {
                            weatherIcon.setImageResource(R.mipmap.tenki_hare_yuki)
                            conditionView.setText(R.string.code1072_day)
                        } else{
                            weatherIcon.setImageResource(R.mipmap.tenki_hare_yuki_yoru)
                            conditionView.setText(R.string.code1072_night)
                        })
                //Thundery outbreaks possible
                1087 -> (
                        if(dayCheck) {
                            weatherIcon.setImageResource(R.mipmap.tenki_hare_kaminari)
                            conditionView.setText(R.string.code1087_day)
                        } else{
                            weatherIcon.setImageResource(R.mipmap.tenki_hare_kaminari_yoru)
                            conditionView.setText(R.string.code1087_night)
                        })
                //Blowing snow
                1114 -> (
                        if(dayCheck) {
                            weatherIcon.setImageResource(R.mipmap.tenki_yuki)
                            conditionView.setText(R.string.code1114_day)
                        } else{
                            weatherIcon.setImageResource(R.mipmap.tenki_yuki_hare_yoru)
                            conditionView.setText(R.string.code1114_night)
                        })
                //Blizzard
                1117 -> (
                        if(dayCheck) {
                            weatherIcon.setImageResource(R.mipmap.tenki_bouhusetsu)
                            conditionView.setText(R.string.code1117_day)
                        } else{
                            weatherIcon.setImageResource(R.mipmap.tenki_bouhusetsu)
                            conditionView.setText(R.string.code1117_night)
                        })
                //Fog
                1135 -> (
                        if(dayCheck) {
                            weatherIcon.setImageResource(R.mipmap.tenki_kumori)
                            conditionView.setText(R.string.code1135_day)
                        } else{
                            weatherIcon.setImageResource(R.mipmap.tenki_kumori_hare_yoru)
                            conditionView.setText(R.string.code1135_night)
                        })
                //Freezing Fog
                1147 -> (
                        if(dayCheck) {
                            weatherIcon.setImageResource(R.mipmap.tenki_kumori_yuki)
                            conditionView.setText(R.string.code1147_day)
                        } else{
                            weatherIcon.setImageResource(R.mipmap.tenki_kumori_yuki)
                            conditionView.setText(R.string.code1147_night)
                        })
                //Patchy light drizzle
                1150 -> (
                        if(dayCheck) {
                            weatherIcon.setImageResource(R.mipmap.tenki_hare_ame)
                            conditionView.setText(R.string.code1150_day)
                        } else{
                            weatherIcon.setImageResource(R.mipmap.tenki_hare_ame_yoru)
                            conditionView.setText(R.string.code1150_night)
                        })
                //Light drizzle
                1153 -> (
                        if(dayCheck) {
                            weatherIcon.setImageResource(R.mipmap.tenki_hare_ame)
                            conditionView.setText(R.string.code1153_day)
                        } else{
                            weatherIcon.setImageResource(R.mipmap.tenki_hare_ame_yoru)
                            conditionView.setText(R.string.code1153_night)
                        })
                //Freezing drizzle
                1168 -> (
                        if(dayCheck) {
                            weatherIcon.setImageResource(R.mipmap.tenki_ame_yuki)
                            conditionView.setText(R.string.code1168_day)
                        } else{
                            weatherIcon.setImageResource(R.mipmap.tenki_ame_yuki)
                            conditionView.setText(R.string.code1168_night)
                        })
                //Heavy freezing drizzle
                1171 -> (
                        if(dayCheck) {
                            weatherIcon.setImageResource(R.mipmap.tenki_yuki_ame)
                            conditionView.setText(R.string.code1171_day)
                        } else{
                            weatherIcon.setImageResource(R.mipmap.tenki_yuki_ame)
                            conditionView.setText(R.string.code1171_night)
                        })
                //Patchy light rain
                1180 -> (
                        if(dayCheck) {
                            weatherIcon.setImageResource(R.mipmap.tenki_hare_ame)
                            conditionView.setText(R.string.code1180_day)
                        } else{
                            weatherIcon.setImageResource(R.mipmap.tenki_hare_ame_yoru)
                            conditionView.setText(R.string.code1180_night)
                        })
                //Light rain
                1183 -> (
                        if(dayCheck) {
                            weatherIcon.setImageResource(R.mipmap.tenki_ame_hare)
                            conditionView.setText(R.string.code1183_day)
                        } else{
                            weatherIcon.setImageResource(R.mipmap.tenki_ame_hare_yoru)
                            conditionView.setText(R.string.code1183_night)
                        })
                //Moderate rain at times
                1186 -> (
                        if(dayCheck) {
                            weatherIcon.setImageResource(R.mipmap.tenki_ame)
                            conditionView.setText(R.string.code1186_day)
                        } else{
                            weatherIcon.setImageResource(R.mipmap.tenki_ame_hare_yoru)
                            conditionView.setText(R.string.code1186_night)
                        })
                //Moderate rain
                1189 -> (
                        if(dayCheck) {
                            weatherIcon.setImageResource(R.mipmap.tenki_ame)
                            conditionView.setText(R.string.code1189_day)
                        } else{
                            weatherIcon.setImageResource(R.mipmap.tenki_ame_hare_yoru)
                            conditionView.setText(R.string.code1189_night)
                        })
                //Heavy rain at times
                1192 -> (
                        if(dayCheck) {
                            weatherIcon.setImageResource(R.mipmap.tenki_bouhuu)
                            conditionView.setText(R.string.code1192_day)
                        } else{
                            weatherIcon.setImageResource(R.mipmap.tenki_bouhuu)
                            conditionView.setText(R.string.code1192_night)
                        })
                //Heavy rain
                1195 -> (
                        if(dayCheck) {
                            weatherIcon.setImageResource(R.mipmap.tenki_bouhuu)
                            conditionView.setText(R.string.code1195_day)
                        } else{
                            weatherIcon.setImageResource(R.mipmap.tenki_bouhuu)
                            conditionView.setText(R.string.code1195_night)
                        })
                //Light freezing rain
                1198 -> (
                        if(dayCheck) {
                            weatherIcon.setImageResource(R.mipmap.tenki_ame_yuki)
                            conditionView.setText(R.string.code1198_day)
                        } else{
                            weatherIcon.setImageResource(R.mipmap.tenki_ame_yuki)
                            conditionView.setText(R.string.code1198_night)
                        })
                //Moderate or heavy freezing rain
                1201 -> (
                        if(dayCheck) {
                            weatherIcon.setImageResource(R.mipmap.tenki_yuki_ame)
                            conditionView.setText(R.string.code1201_day)
                        } else{
                            weatherIcon.setImageResource(R.mipmap.tenki_yuki_ame)
                            conditionView.setText(R.string.code1201_night)
                        })
                //Light sleet
                1204 -> (
                        if(dayCheck) {
                            weatherIcon.setImageResource(R.mipmap.tenki_yuki)
                            conditionView.setText(R.string.code1204_day)
                        } else{
                            weatherIcon.setImageResource(R.mipmap.tenki_yuki_hare_yoru)
                            conditionView.setText(R.string.code1204_night)
                        })
                //Moderate or heavy sleet
                1207 -> (
                        if(dayCheck) {
                            weatherIcon.setImageResource(R.mipmap.tenki_yuki)
                            conditionView.setText(R.string.code1207_day)
                        } else{
                            weatherIcon.setImageResource(R.mipmap.tenki_yuki)
                            conditionView.setText(R.string.code1207_night)
                        })
                //Patchy light snow
                1210 -> (
                        if(dayCheck) {
                            weatherIcon.setImageResource(R.mipmap.tenki_hare_yuki)
                            conditionView.setText(R.string.code1210_day)
                        } else{
                            weatherIcon.setImageResource(R.mipmap.tenki_hare_yuki_yoru)
                            conditionView.setText(R.string.code1210_night)
                        })
                //Light snow
                1213 -> (
                        if(dayCheck) {
                            weatherIcon.setImageResource(R.mipmap.tenki_yuki_hare)
                            conditionView.setText(R.string.code1213_day)
                        } else{
                            weatherIcon.setImageResource(R.mipmap.tenki_yuki_hare_yoru)
                            conditionView.setText(R.string.code1213_night)
                        })
                //Patchy moderate snow
                1216 -> (
                        if(dayCheck) {
                            weatherIcon.setImageResource(R.mipmap.tenki_yuki)
                            conditionView.setText(R.string.code1216_day)
                        } else{
                            weatherIcon.setImageResource(R.mipmap.tenki_yuki_hare_yoru)
                            conditionView.setText(R.string.code1216_night)
                        })
                //Moderate snow
                1219 -> (
                        if(dayCheck) {
                            weatherIcon.setImageResource(R.mipmap.tenki_yuki)
                            conditionView.setText(R.string.code1219_day)
                        } else{
                            weatherIcon.setImageResource(R.mipmap.tenki_yuki_hare_yoru)
                            conditionView.setText(R.string.code1219_night)
                        })
                //Patchy heavy snow
                1222 -> (
                        if(dayCheck) {
                            weatherIcon.setImageResource(R.mipmap.tenki_kumori_yuki)
                            conditionView.setText(R.string.code1222_day)
                        } else{
                            weatherIcon.setImageResource(R.mipmap.tenki_kumori_yuki)
                            conditionView.setText(R.string.code1222_night)
                        })
                //Heavy snow
                1225 -> (
                        if(dayCheck) {
                            weatherIcon.setImageResource(R.mipmap.tenki_bouhusetsu)
                            conditionView.setText(R.string.code1225_day)
                        } else{
                            weatherIcon.setImageResource(R.mipmap.tenki_bouhusetsu)
                            conditionView.setText(R.string.code1225_night)
                        })
                //Ice pellets
                1237 -> (
                        if(dayCheck) {
                            weatherIcon.setImageResource(R.mipmap.tenki_yuki)
                            conditionView.setText(R.string.code1237_day)
                        } else{
                            weatherIcon.setImageResource(R.mipmap.tenki_yuki_hare_yoru)
                            conditionView.setText(R.string.code1237_night)
                        })
                //Light rain shower
                1240 -> (
                        if(dayCheck) {
                            weatherIcon.setImageResource(R.mipmap.tenki_ame_hare)
                            conditionView.setText(R.string.code1240_day)
                        } else{
                            weatherIcon.setImageResource(R.mipmap.tenki_ame_hare_yoru)
                            conditionView.setText(R.string.code1240_night)
                        })
                //Moderate or heavy rain shower
                1243 -> (
                        if(dayCheck) {
                            weatherIcon.setImageResource(R.mipmap.tenki_ame)
                            conditionView.setText(R.string.code1243_day)
                        } else{
                            weatherIcon.setImageResource(R.mipmap.tenki_ame_hare_yoru)
                            conditionView.setText(R.string.code1243_night)
                        })
                //Torrential rain shower
                1246 -> (
                        if(dayCheck) {
                            weatherIcon.setImageResource(R.mipmap.tenki_bouhuu)
                            conditionView.setText(R.string.code1246_day)
                        } else{
                            weatherIcon.setImageResource(R.mipmap.tenki_bouhuu)
                            conditionView.setText(R.string.code1246_night)
                        })
                //Light sleet showers
                1249 -> (
                        if(dayCheck) {
                            weatherIcon.setImageResource(R.mipmap.tenki_yuki)
                            conditionView.setText(R.string.code1249_day)
                        } else{
                            weatherIcon.setImageResource(R.mipmap.tenki_yuki_hare_yoru)
                            conditionView.setText(R.string.code1249_night)
                        })
                //Moderate or heavy sleet showers
                1252 -> (
                        if(dayCheck) {
                            weatherIcon.setImageResource(R.mipmap.tenki_yuki)
                            conditionView.setText(R.string.code1252_day)
                        } else{
                            weatherIcon.setImageResource(R.mipmap.tenki_yuki_hare_yoru)
                            conditionView.setText(R.string.code1252_night)
                        })
                //Light snow showers
                1255 -> (
                        if(dayCheck) {
                            weatherIcon.setImageResource(R.mipmap.tenki_hare_yuki)
                            conditionView.setText(R.string.code1255_day)
                        } else{
                            weatherIcon.setImageResource(R.mipmap.tenki_hare_yuki_yoru)
                            conditionView.setText(R.string.code1255_night)
                        })
                //Moderate or heavy snow showers
                1258 -> (
                        if(dayCheck) {
                            weatherIcon.setImageResource(R.mipmap.tenki_yuki_hare)
                            conditionView.setText(R.string.code1258_day)
                        } else{
                            weatherIcon.setImageResource(R.mipmap.tenki_yuki_hare)
                            conditionView.setText(R.string.code1258_night)
                        })
                //Light showers of ice pellets
                1261 -> (
                        if(dayCheck) {
                            weatherIcon.setImageResource(R.mipmap.tenki_yuki)
                            conditionView.setText(R.string.code1261_day)
                        } else{
                            weatherIcon.setImageResource(R.mipmap.tenki_yuki_hare_yoru)
                            conditionView.setText(R.string.code1261_night)
                        })
                //Moderate or heavy showers of ice pellets
                1264 -> (
                        if(dayCheck) {
                            weatherIcon.setImageResource(R.mipmap.tenki_yuki)
                            conditionView.setText(R.string.code1264_day)
                        } else{
                            weatherIcon.setImageResource(R.mipmap.tenki_yuki_hare_yoru)
                            conditionView.setText(R.string.code1264_night)
                        })
                //Patchy light rain with thunder
                1273 -> (
                        if(dayCheck) {
                            weatherIcon.setImageResource(R.mipmap.tenki_hare_kaminari)
                            conditionView.setText(R.string.code1273_day)
                        } else{
                            weatherIcon.setImageResource(R.mipmap.tenki_hare_kaminari_yoru)
                            conditionView.setText(R.string.code1273_night)
                        })
                //Moderate or heavy rain with thunder
                1276 -> (
                        if(dayCheck) {
                            weatherIcon.setImageResource(R.mipmap.tenki_kumori_kaminari)
                            conditionView.setText(R.string.code1276_day)
                        } else{
                            weatherIcon.setImageResource(R.mipmap.tenki_kumori_kaminari)
                            conditionView.setText(R.string.code1276_night)
                        })
                //Patchy light snow with thunder
                1279 -> (
                        if(dayCheck) {
                            weatherIcon.setImageResource(R.mipmap.tenki_yuki_kaminari)
                            conditionView.setText(R.string.code1279_day)
                        } else{
                            weatherIcon.setImageResource(R.mipmap.tenki_yuki_kaminari)
                            conditionView.setText(R.string.code1279_night)
                        })
                //Moderate or heavy snow with thunder
                1282 -> (
                        if(dayCheck) {
                            weatherIcon.setImageResource(R.mipmap.tenki_kumori_yuki_kaminari)
                            conditionView.setText(R.string.code1282_day)
                        } else{
                            weatherIcon.setImageResource(R.mipmap.tenki_kumori_yuki_kaminari)
                            conditionView.setText(R.string.code1282_night)
                        })
            }
        }
    }
}
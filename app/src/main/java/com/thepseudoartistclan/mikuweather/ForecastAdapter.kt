package com.thepseudoartistclan.mikuweather

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.thepseudoartistclan.mikuweather.modal.Hour

class ForecastAdapter(var forecastList: ArrayList<Hour>): RecyclerView.Adapter<ForecastAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) : ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_item, parent, false);
        return ViewHolder(view)
    }

    override fun getItemCount() = forecastList.size
    override fun onBindViewHolder(forecastHolder: ViewHolder, position: Int) {
        val forecast = forecastList[position]
        forecastHolder.bind(forecast)
    }

    inner class ViewHolder(val view: View) : RecyclerView.ViewHolder(view), View.OnClickListener  {
        lateinit var forecast: Hour
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

            timeView.setText(forecast.time)
            tempView.setText(forecast.temp_c.toString() + "°C")
            conditionView.setText(forecast.condition.text)

            var dayCheck = false
            if(forecast.is_day == 1) dayCheck = true
            when(forecast.condition.code) {
                //Sunny
                1000 -> (if(dayCheck) {weatherIcon.setImageResource(R.mipmap.tenki_hare)} else {weatherIcon.setImageResource(R.mipmap.tenki_hare_yoru)})
                //Partly Cloudy
                1003 -> (if(dayCheck) {weatherIcon.setImageResource(R.mipmap.tenki_hare_kumori)} else{weatherIcon.setImageResource(R.mipmap.tenki_hare_kumori_yoru)})
                //Cloudy
                1006 -> (if(dayCheck) {weatherIcon.setImageResource(R.mipmap.tenki_kumori_hare)} else{weatherIcon.setImageResource(R.mipmap.tenki_kumori_hare_yoru)})
                //Overcast
                1009 -> (if(dayCheck) {weatherIcon.setImageResource(R.mipmap.tenki_kumori)} else{weatherIcon.setImageResource(R.mipmap.tenki_kumori_hare_yoru)})
                //Mist
                1030 -> (if(dayCheck) {weatherIcon.setImageResource(R.mipmap.tenki_kumori)} else{weatherIcon.setImageResource(R.mipmap.tenki_kumori_hare_yoru)})
                //Patchy Rain Possible
                1063 -> (if(dayCheck) {weatherIcon.setImageResource(R.mipmap.tenki_ame_hare)} else{weatherIcon.setImageResource(R.mipmap.tenki_ame_hare_yoru)})
                //Patchy Snow Possible
                1066 -> (if(dayCheck) {weatherIcon.setImageResource(R.mipmap.tenki_hare_yuki)} else{weatherIcon.setImageResource(R.mipmap.tenki_hare_yuki_yoru)})
                //Patchy Sleet Possible
                1069 -> ((if(dayCheck) {weatherIcon.setImageResource(R.mipmap.tenki_yuki)} else{weatherIcon.setImageResource(R.mipmap.tenki_yuki_hare_yoru)}))
                //Patchy Freezing Drizzle Possible
                1072 -> (if(dayCheck) {weatherIcon.setImageResource(R.mipmap.tenki_hare_yuki)} else{weatherIcon.setImageResource(R.mipmap.tenki_hare_yuki_yoru)})
                //Thundery outbreaks possible
                1087 -> (if(dayCheck) {weatherIcon.setImageResource(R.mipmap.tenki_hare_kaminari)} else{weatherIcon.setImageResource(R.mipmap.tenki_hare_kaminari_yoru)})
                //Blowing snow
                1114 -> (if(dayCheck) {weatherIcon.setImageResource(R.mipmap.tenki_yuki)} else{weatherIcon.setImageResource(R.mipmap.tenki_yuki_hare_yoru)})
                //Blizzard
                1117 -> (if(dayCheck) {weatherIcon.setImageResource(R.mipmap.tenki_bouhusetsu)} else{weatherIcon.setImageResource(R.mipmap.tenki_bouhusetsu)})
                //Fog
                1135 -> (if(dayCheck) {weatherIcon.setImageResource(R.mipmap.tenki_kumori)} else{weatherIcon.setImageResource(R.mipmap.tenki_kumori_hare_yoru)})
                //Freezing Fog
                1147 -> (if(dayCheck) {weatherIcon.setImageResource(R.mipmap.tenki_kumori_yuki)} else{weatherIcon.setImageResource(R.mipmap.tenki_kumori_yuki)})
                //Patchy light drizzle
                1150 -> (if(dayCheck) {weatherIcon.setImageResource(R.mipmap.tenki_hare_ame)} else{weatherIcon.setImageResource(R.mipmap.tenki_hare_ame_yoru)})
                //Light drizzle
                1153 -> (if(dayCheck) {weatherIcon.setImageResource(R.mipmap.tenki_hare_ame)} else{weatherIcon.setImageResource(R.mipmap.tenki_hare_ame_yoru)})
                //Freezing drizzle
                1168 -> (if(dayCheck) {weatherIcon.setImageResource(R.mipmap.tenki_ame_yuki)} else{weatherIcon.setImageResource(R.mipmap.tenki_ame_yuki)})
                //Heavy freezing drizzle
                1171 -> (if(dayCheck) {weatherIcon.setImageResource(R.mipmap.tenki_yuki_ame)} else{weatherIcon.setImageResource(R.mipmap.tenki_yuki_ame)})
                //Patchy light rain
                1180 -> (if(dayCheck) {weatherIcon.setImageResource(R.mipmap.tenki_hare_ame)} else{weatherIcon.setImageResource(R.mipmap.tenki_hare_ame_yoru)})
                //Light rain
                1183 -> (if(dayCheck) {weatherIcon.setImageResource(R.mipmap.tenki_ame_hare)} else{weatherIcon.setImageResource(R.mipmap.tenki_ame_hare_yoru)})
                //Moderate rain at times
                1186 -> (if(dayCheck) {weatherIcon.setImageResource(R.mipmap.tenki_ame)} else{weatherIcon.setImageResource(R.mipmap.tenki_ame_hare_yoru)})
                //Moderate rain
                1189 -> (if(dayCheck) {weatherIcon.setImageResource(R.mipmap.tenki_ame)} else{weatherIcon.setImageResource(R.mipmap.tenki_ame_hare_yoru)})
                //Heavy rain at times
                1192 -> (if(dayCheck) {weatherIcon.setImageResource(R.mipmap.tenki_bouhuu)} else{weatherIcon.setImageResource(R.mipmap.tenki_bouhuu)})
                //Heavy rain
                1195 -> (if(dayCheck) {weatherIcon.setImageResource(R.mipmap.tenki_bouhuu)} else{weatherIcon.setImageResource(R.mipmap.tenki_bouhuu)})
                //Light freezing rain
                1198 -> (if(dayCheck) {weatherIcon.setImageResource(R.mipmap.tenki_ame_yuki)} else{weatherIcon.setImageResource(R.mipmap.tenki_ame_yuki)})
                //Moderate or heavy freezing rain
                1201 -> (if(dayCheck) {weatherIcon.setImageResource(R.mipmap.tenki_yuki_ame)} else{weatherIcon.setImageResource(R.mipmap.tenki_yuki_ame)})
                //Light sleet
                1204 -> (if(dayCheck) {weatherIcon.setImageResource(R.mipmap.tenki_yuki)} else{weatherIcon.setImageResource(R.mipmap.tenki_yuki_hare_yoru)})
                //Moderate or heavy sleet
                1207 -> (if(dayCheck) {weatherIcon.setImageResource(R.mipmap.tenki_yuki)} else{weatherIcon.setImageResource(R.mipmap.tenki_yuki)})
                //Patchy light snow
                1210 -> (if(dayCheck) {weatherIcon.setImageResource(R.mipmap.tenki_hare_yuki)} else{weatherIcon.setImageResource(R.mipmap.tenki_hare_yuki_yoru)})
                //Light snow
                1213 -> (if(dayCheck) {weatherIcon.setImageResource(R.mipmap.tenki_yuki_hare)} else{weatherIcon.setImageResource(R.mipmap.tenki_yuki_hare_yoru)})
                //Patchy moderate snow
                1216 -> (if(dayCheck) {weatherIcon.setImageResource(R.mipmap.tenki_yuki)} else{weatherIcon.setImageResource(R.mipmap.tenki_yuki_hare_yoru)})
                //Moderate snow
                1219 -> (if(dayCheck) {weatherIcon.setImageResource(R.mipmap.tenki_yuki)} else{weatherIcon.setImageResource(R.mipmap.tenki_yuki_hare_yoru)})
                //Patchy heavy snow
                1222 -> (if(dayCheck) {weatherIcon.setImageResource(R.mipmap.tenki_kumori_yuki)} else{weatherIcon.setImageResource(R.mipmap.tenki_kumori_yuki)})
                //Heavy snow
                1225 -> (if(dayCheck) {weatherIcon.setImageResource(R.mipmap.tenki_bouhusetsu)} else{weatherIcon.setImageResource(R.mipmap.tenki_bouhusetsu)})
                //Ice pellets
                1237 -> (if(dayCheck) {weatherIcon.setImageResource(R.mipmap.tenki_yuki)} else{weatherIcon.setImageResource(R.mipmap.tenki_yuki_hare_yoru)})
                //Light rain shower
                1240 -> (if(dayCheck) {weatherIcon.setImageResource(R.mipmap.tenki_ame_hare)} else{weatherIcon.setImageResource(R.mipmap.tenki_ame_hare_yoru)})
                //Moderate or heavy rain shower
                1243 -> (if(dayCheck) {weatherIcon.setImageResource(R.mipmap.tenki_ame)} else{weatherIcon.setImageResource(R.mipmap.tenki_ame_hare_yoru)})
                //Torrential rain shower
                1246 -> (if(dayCheck) {weatherIcon.setImageResource(R.mipmap.tenki_bouhuu)} else{weatherIcon.setImageResource(R.mipmap.tenki_bouhuu)})
                //Light sleet showers
                1249 -> (if(dayCheck) {weatherIcon.setImageResource(R.mipmap.tenki_yuki)} else{weatherIcon.setImageResource(R.mipmap.tenki_yuki_hare_yoru)})
                //Moderate or heavy sleet showers
                1252 -> (if(dayCheck) {weatherIcon.setImageResource(R.mipmap.tenki_yuki)} else{weatherIcon.setImageResource(R.mipmap.tenki_yuki_hare_yoru)})
                //Light snow showers
                1255 -> (if(dayCheck) {weatherIcon.setImageResource(R.mipmap.tenki_hare_yuki)} else{weatherIcon.setImageResource(R.mipmap.tenki_hare_yuki_yoru)})
                //Moderate or heavy snow showers
                1258 -> (if(dayCheck) {weatherIcon.setImageResource(R.mipmap.tenki_yuki_hare)} else{weatherIcon.setImageResource(R.mipmap.tenki_yuki_hare)})
                //Light showers of ice pellets
                1261 -> (if(dayCheck) {weatherIcon.setImageResource(R.mipmap.tenki_yuki)} else{weatherIcon.setImageResource(R.mipmap.tenki_yuki_hare_yoru)})
                //Moderate or heavy showers of ice pellets
                1264 -> (if(dayCheck) {weatherIcon.setImageResource(R.mipmap.tenki_yuki)} else{weatherIcon.setImageResource(R.mipmap.tenki_yuki_hare_yoru)})
                //Patchy light rain with thunder
                1273 -> (if(dayCheck) {weatherIcon.setImageResource(R.mipmap.tenki_hare_kaminari)} else{weatherIcon.setImageResource(R.mipmap.tenki_hare_kaminari_yoru)})
                //Moderate or heavy rain with thunder
                1276 -> (if(dayCheck) {weatherIcon.setImageResource(R.mipmap.tenki_kumori_kaminari)} else{weatherIcon.setImageResource(R.mipmap.tenki_kumori_kaminari)})
                //Patchy light snow with thunder
                1279 -> (if(dayCheck) {weatherIcon.setImageResource(R.mipmap.tenki_yuki_kaminari)} else{weatherIcon.setImageResource(R.mipmap.tenki_yuki_kaminari)})
                //Moderate or heavy snow with thunder
                1282 -> (if(dayCheck) {weatherIcon.setImageResource(R.mipmap.tenki_kumori_yuki_kaminari)} else{weatherIcon.setImageResource(R.mipmap.tenki_kumori_yuki_kaminari)})
            }
        }
    }
}
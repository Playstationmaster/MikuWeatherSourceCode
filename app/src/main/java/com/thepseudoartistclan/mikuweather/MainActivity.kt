package com.thepseudoartistclan.mikuweather

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.LocationManager
import android.os.Bundle
import android.os.Handler
import android.provider.Settings
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.thepseudoartistclan.mikuweather.api.WeatherService
import com.thepseudoartistclan.mikuweather.modal.Hour
import com.thepseudoartistclan.mikuweather.repository.WeatherRepository
import com.thepseudoartistclan.mikuweather.viewmodel.WeatherViewModel
import com.thepseudoartistclan.mikuweather.viewmodel.WeatherViewModelFactory
import java.util.*

private val TAG = "LocationServices"
private val LOCATION_PERMISSION_CODE = 1

class MainActivity : AppCompatActivity() {
    //Google Maps Location Request Variables
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var locationRequest: LocationRequest
    private lateinit var locationCallback: LocationCallback

    //Query Location
    private var mLat:Double = 0.0
    private var mLon:Double = 0.0
    private var hour:ArrayList<Hour> = ArrayList()
    private var city:String = "" //Required for location calls

    //Main screen start
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        serviceInitialisation()

        val logoicon = findViewById<View>(R.id.titleimage) as ImageView
        logoicon.setOnClickListener(View.OnClickListener {
            activityRestart()
        })
    }

    //Main Function
    private fun serviceInitialisation() {
        val manager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            showAlertLocation()
        }
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        initLocationCallbacks()
        checkLocationUpdate()
    }

    private fun checkLocationUpdate() {
        if((mLat != 0.0 && mLon != 0.0) || !city.equals("")) {
            weatherUpdate()
        }
        else  {
            Handler().postDelayed({
                 checkLocationUpdate()
            }, 50)
        }
    }

    @Throws (java.lang.IndexOutOfBoundsException::class)
    private fun weatherUpdate() {
        val weatherInstance = WeatherService.getInstance()
        val repository = WeatherRepository(weatherInstance)
        val viewModel = ViewModelProvider(this, WeatherViewModelFactory(repository)).get(WeatherViewModel::class.java)
        if(mLat != 0.0 && mLon != 0.0) {
            viewModel.getCurrentTemp("$mLat,$mLon")
        }
        else {
            viewModel.getCurrentTemp(city)
            //For unknown reason, latitute and longitude are not imported when teokbyeol-si or gwangyeok-si cities are queried.
        }
        viewModel.weather.observe(this) {
            currentWeatherDisplay(it.current.condition.code, it.current.is_day)
            findViewById<TextView>(R.id.currentTemp).text = it.current.temp_c.toString()
            hour = it.forecast.forecastday[0].hour as ArrayList<Hour>
            hour.addAll(it.forecast.forecastday[1].hour)
            val forecastRecyclerView = findViewById<RecyclerView>(R.id.forecast_fragment)
            forecastRecyclerView.layoutManager = LinearLayoutManager(this)
            forecastRecyclerView.adapter = ForecastAdapter(hour)
        }
    }

    //Activity Restart
    private fun activityRestart() {
        val intent = intent
        finish()
        startActivity(intent)
    }

    //Main Screen Weather Data Display
    private fun currentWeatherDisplay(code: Int, isDay: Int) {
        var dayCheck = false
        if(isDay == 1) dayCheck = true
        val weatherIcon:ImageView = findViewById(R.id.weatherIcon)
        val weatherCondition:TextView = findViewById(R.id.conditionString)
        when(code) {
            //Sunny
            1000 -> (
                    if(dayCheck) {
                        weatherIcon.setImageResource(R.mipmap.tenki_hare)
                        weatherCondition.setText(R.string.code1000_day)
                    } else {
                        weatherIcon.setImageResource(R.mipmap.tenki_hare_yoru)
                        weatherCondition.setText(R.string.code1000_night)
                    })
            //Partly Cloudy
            1003 -> (
                    if(dayCheck) {
                        weatherIcon.setImageResource(R.mipmap.tenki_hare_kumori)
                        weatherCondition.setText(R.string.code1003_day)
                    } else{
                        weatherIcon.setImageResource(R.mipmap.tenki_hare_kumori_yoru)
                        weatherCondition.setText(R.string.code1003_night)
                    })
            //Cloudy
            1006 -> (
                    if(dayCheck) {
                        weatherIcon.setImageResource(R.mipmap.tenki_kumori_hare)
                        weatherCondition.setText(R.string.code1006_day)
                    } else{
                        weatherIcon.setImageResource(R.mipmap.tenki_kumori_hare_yoru)
                        weatherCondition.setText(R.string.code1006_night)
                    })
            //Overcast
            1009 -> (
                    if(dayCheck) {
                        weatherIcon.setImageResource(R.mipmap.tenki_kumori)
                        weatherCondition.setText(R.string.code1009_night)
                    } else{
                        weatherIcon.setImageResource(R.mipmap.tenki_kumori_hare_yoru)
                        weatherCondition.setText(R.string.code1009_night)
                    })
            //Mist
            1030 -> (
                    if(dayCheck) {
                        weatherIcon.setImageResource(R.mipmap.tenki_kumori)
                        weatherCondition.setText(R.string.code1030_day)
                    } else{
                        weatherIcon.setImageResource(R.mipmap.tenki_kumori_hare_yoru)
                        weatherCondition.setText(R.string.code1030_night)
                    })
            //Patchy Rain Possible
            1063 -> (
                    if(dayCheck) {
                        weatherIcon.setImageResource(R.mipmap.tenki_ame_hare)
                        weatherCondition.setText(R.string.code1063_day)
                    } else{
                        weatherIcon.setImageResource(R.mipmap.tenki_ame_hare_yoru)
                        weatherCondition.setText(R.string.code1063_night)
                    })
            //Patchy Snow Possible
            1066 -> (
                    if(dayCheck) {
                        weatherIcon.setImageResource(R.mipmap.tenki_hare_yuki)
                        weatherCondition.setText(R.string.code1066_day)
                    } else{
                        weatherIcon.setImageResource(R.mipmap.tenki_hare_yuki_yoru)
                        weatherCondition.setText(R.string.code1066_day)
                    })
            //Patchy Sleet Possible
            1069 -> (
                    if(dayCheck) {
                        weatherIcon.setImageResource(R.mipmap.tenki_yuki)
                        weatherCondition.setText(R.string.code1069_day)
                    } else{
                        weatherIcon.setImageResource(R.mipmap.tenki_yuki_hare_yoru)
                        weatherCondition.setText(R.string.code1069_day)
                    })
            //Patchy Freezing Drizzle Possible
            1072 -> (
                    if(dayCheck) {
                        weatherIcon.setImageResource(R.mipmap.tenki_hare_yuki)
                        weatherCondition.setText(R.string.code1072_day)
                    } else{
                        weatherIcon.setImageResource(R.mipmap.tenki_hare_yuki_yoru)
                        weatherCondition.setText(R.string.code1072_night)
                    })
            //Thundery outbreaks possible
            1087 -> (
                    if(dayCheck) {
                        weatherIcon.setImageResource(R.mipmap.tenki_hare_kaminari)
                        weatherCondition.setText(R.string.code1087_day)
                    } else{
                        weatherIcon.setImageResource(R.mipmap.tenki_hare_kaminari_yoru)
                        weatherCondition.setText(R.string.code1087_night)
                    })
            //Blowing snow
            1114 -> (
                    if(dayCheck) {
                        weatherIcon.setImageResource(R.mipmap.tenki_yuki)
                        weatherCondition.setText(R.string.code1114_day)
                    } else{
                        weatherIcon.setImageResource(R.mipmap.tenki_yuki_hare_yoru)
                        weatherCondition.setText(R.string.code1114_night)
                    })
            //Blizzard
            1117 -> (
                    if(dayCheck) {
                        weatherIcon.setImageResource(R.mipmap.tenki_bouhusetsu)
                        weatherCondition.setText(R.string.code1117_day)
                    } else{
                        weatherIcon.setImageResource(R.mipmap.tenki_bouhusetsu)
                        weatherCondition.setText(R.string.code1117_night)
                    })
            //Fog
            1135 -> (
                    if(dayCheck) {
                        weatherIcon.setImageResource(R.mipmap.tenki_kumori)
                        weatherCondition.setText(R.string.code1135_day)
                    } else{
                        weatherIcon.setImageResource(R.mipmap.tenki_kumori_hare_yoru)
                        weatherCondition.setText(R.string.code1135_night)
                    })
            //Freezing Fog
            1147 -> (
                    if(dayCheck) {
                        weatherIcon.setImageResource(R.mipmap.tenki_kumori_yuki)
                        weatherCondition.setText(R.string.code1147_day)
                    } else{
                        weatherIcon.setImageResource(R.mipmap.tenki_kumori_yuki)
                        weatherCondition.setText(R.string.code1147_night)
                    })
            //Patchy light drizzle
            1150 -> (
                    if(dayCheck) {
                        weatherIcon.setImageResource(R.mipmap.tenki_hare_ame)
                        weatherCondition.setText(R.string.code1150_day)
                    } else{
                        weatherIcon.setImageResource(R.mipmap.tenki_hare_ame_yoru)
                        weatherCondition.setText(R.string.code1150_night)
                    })
            //Light drizzle
            1153 -> (
                    if(dayCheck) {
                        weatherIcon.setImageResource(R.mipmap.tenki_hare_ame)
                        weatherCondition.setText(R.string.code1153_day)
                    } else{
                        weatherIcon.setImageResource(R.mipmap.tenki_hare_ame_yoru)
                        weatherCondition.setText(R.string.code1153_night)
                    })
            //Freezing drizzle
            1168 -> (
                    if(dayCheck) {
                        weatherIcon.setImageResource(R.mipmap.tenki_ame_yuki)
                        weatherCondition.setText(R.string.code1168_day)
                    } else{
                        weatherIcon.setImageResource(R.mipmap.tenki_ame_yuki)
                        weatherCondition.setText(R.string.code1168_night)
                    })
            //Heavy freezing drizzle
            1171 -> (
                    if(dayCheck) {
                        weatherIcon.setImageResource(R.mipmap.tenki_yuki_ame)
                        weatherCondition.setText(R.string.code1171_day)
                    } else{
                        weatherIcon.setImageResource(R.mipmap.tenki_yuki_ame)
                        weatherCondition.setText(R.string.code1171_night)
                    })
            //Patchy light rain
            1180 -> (
                    if(dayCheck) {
                        weatherIcon.setImageResource(R.mipmap.tenki_hare_ame)
                        weatherCondition.setText(R.string.code1180_day)
                    } else{
                        weatherIcon.setImageResource(R.mipmap.tenki_hare_ame_yoru)
                        weatherCondition.setText(R.string.code1180_night)
                    })
            //Light rain
            1183 -> (
                    if(dayCheck) {
                        weatherIcon.setImageResource(R.mipmap.tenki_ame_hare)
                        weatherCondition.setText(R.string.code1183_day)
                    } else{
                        weatherIcon.setImageResource(R.mipmap.tenki_ame_hare_yoru)
                        weatherCondition.setText(R.string.code1183_night)
                    })
            //Moderate rain at times
            1186 -> (
                    if(dayCheck) {
                        weatherIcon.setImageResource(R.mipmap.tenki_ame)
                        weatherCondition.setText(R.string.code1186_day)
                    } else{
                        weatherIcon.setImageResource(R.mipmap.tenki_ame_hare_yoru)
                        weatherCondition.setText(R.string.code1186_night)
                    })
            //Moderate rain
            1189 -> (
                    if(dayCheck) {
                        weatherIcon.setImageResource(R.mipmap.tenki_ame)
                        weatherCondition.setText(R.string.code1189_day)
                    } else{
                        weatherIcon.setImageResource(R.mipmap.tenki_ame_hare_yoru)
                        weatherCondition.setText(R.string.code1189_night)
                    })
            //Heavy rain at times
            1192 -> (
                    if(dayCheck) {
                        weatherIcon.setImageResource(R.mipmap.tenki_bouhuu)
                        weatherCondition.setText(R.string.code1192_day)
                    } else{
                        weatherIcon.setImageResource(R.mipmap.tenki_bouhuu)
                        weatherCondition.setText(R.string.code1192_night)
                    })
            //Heavy rain
            1195 -> (
                    if(dayCheck) {
                        weatherIcon.setImageResource(R.mipmap.tenki_bouhuu)
                        weatherCondition.setText(R.string.code1195_day)
                    } else{
                        weatherIcon.setImageResource(R.mipmap.tenki_bouhuu)
                        weatherCondition.setText(R.string.code1195_night)
                    })
            //Light freezing rain
            1198 -> (
                    if(dayCheck) {
                        weatherIcon.setImageResource(R.mipmap.tenki_ame_yuki)
                        weatherCondition.setText(R.string.code1198_day)
                    } else{
                        weatherIcon.setImageResource(R.mipmap.tenki_ame_yuki)
                        weatherCondition.setText(R.string.code1198_night)
                    })
            //Moderate or heavy freezing rain
            1201 -> (
                    if(dayCheck) {
                        weatherIcon.setImageResource(R.mipmap.tenki_yuki_ame)
                        weatherCondition.setText(R.string.code1201_day)
                    } else{
                        weatherIcon.setImageResource(R.mipmap.tenki_yuki_ame)
                        weatherCondition.setText(R.string.code1201_night)
                    })
            //Light sleet
            1204 -> (
                    if(dayCheck) {
                        weatherIcon.setImageResource(R.mipmap.tenki_yuki)
                        weatherCondition.setText(R.string.code1204_day)
                    } else{
                        weatherIcon.setImageResource(R.mipmap.tenki_yuki_hare_yoru)
                        weatherCondition.setText(R.string.code1204_night)
                    })
            //Moderate or heavy sleet
            1207 -> (
                    if(dayCheck) {
                        weatherIcon.setImageResource(R.mipmap.tenki_yuki)
                        weatherCondition.setText(R.string.code1207_day)
                    } else{
                        weatherIcon.setImageResource(R.mipmap.tenki_yuki)
                        weatherCondition.setText(R.string.code1207_night)
                    })
            //Patchy light snow
            1210 -> (
                    if(dayCheck) {
                        weatherIcon.setImageResource(R.mipmap.tenki_hare_yuki)
                        weatherCondition.setText(R.string.code1210_day)
                    } else{
                        weatherIcon.setImageResource(R.mipmap.tenki_hare_yuki_yoru)
                        weatherCondition.setText(R.string.code1210_night)
                    })
            //Light snow
            1213 -> (
                    if(dayCheck) {
                        weatherIcon.setImageResource(R.mipmap.tenki_yuki_hare)
                        weatherCondition.setText(R.string.code1213_day)
                    } else{
                        weatherIcon.setImageResource(R.mipmap.tenki_yuki_hare_yoru)
                        weatherCondition.setText(R.string.code1213_night)
                    })
            //Patchy moderate snow
            1216 -> (
                    if(dayCheck) {
                        weatherIcon.setImageResource(R.mipmap.tenki_yuki)
                        weatherCondition.setText(R.string.code1216_day)
                    } else{
                        weatherIcon.setImageResource(R.mipmap.tenki_yuki_hare_yoru)
                        weatherCondition.setText(R.string.code1216_night)
                    })
            //Moderate snow
            1219 -> (
                    if(dayCheck) {
                        weatherIcon.setImageResource(R.mipmap.tenki_yuki)
                        weatherCondition.setText(R.string.code1219_day)
                    } else{
                        weatherIcon.setImageResource(R.mipmap.tenki_yuki_hare_yoru)
                        weatherCondition.setText(R.string.code1219_night)
                    })
            //Patchy heavy snow
            1222 -> (
                    if(dayCheck) {
                        weatherIcon.setImageResource(R.mipmap.tenki_kumori_yuki)
                        weatherCondition.setText(R.string.code1222_day)
                    } else{
                        weatherIcon.setImageResource(R.mipmap.tenki_kumori_yuki)
                        weatherCondition.setText(R.string.code1222_night)
                    })
            //Heavy snow
            1225 -> (
                    if(dayCheck) {
                        weatherIcon.setImageResource(R.mipmap.tenki_bouhusetsu)
                        weatherCondition.setText(R.string.code1225_day)
                    } else{
                        weatherIcon.setImageResource(R.mipmap.tenki_bouhusetsu)
                        weatherCondition.setText(R.string.code1225_night)
                    })
            //Ice pellets
            1237 -> (
                    if(dayCheck) {
                        weatherIcon.setImageResource(R.mipmap.tenki_yuki)
                        weatherCondition.setText(R.string.code1237_day)
                    } else{
                        weatherIcon.setImageResource(R.mipmap.tenki_yuki_hare_yoru)
                        weatherCondition.setText(R.string.code1237_night)
                    })
            //Light rain shower
            1240 -> (
                    if(dayCheck) {
                        weatherIcon.setImageResource(R.mipmap.tenki_ame_hare)
                        weatherCondition.setText(R.string.code1240_day)
                    } else{
                        weatherIcon.setImageResource(R.mipmap.tenki_ame_hare_yoru)
                        weatherCondition.setText(R.string.code1240_night)
                    })
            //Moderate or heavy rain shower
            1243 -> (
                    if(dayCheck) {
                        weatherIcon.setImageResource(R.mipmap.tenki_ame)
                        weatherCondition.setText(R.string.code1243_day)
                    } else{
                        weatherIcon.setImageResource(R.mipmap.tenki_ame_hare_yoru)
                        weatherCondition.setText(R.string.code1243_night)
                    })
            //Torrential rain shower
            1246 -> (
                    if(dayCheck) {
                        weatherIcon.setImageResource(R.mipmap.tenki_bouhuu)
                        weatherCondition.setText(R.string.code1246_day)
                    } else{
                        weatherIcon.setImageResource(R.mipmap.tenki_bouhuu)
                        weatherCondition.setText(R.string.code1246_night)
                    })
            //Light sleet showers
            1249 -> (
                    if(dayCheck) {
                        weatherIcon.setImageResource(R.mipmap.tenki_yuki)
                        weatherCondition.setText(R.string.code1249_day)
                    } else{
                        weatherIcon.setImageResource(R.mipmap.tenki_yuki_hare_yoru)
                        weatherCondition.setText(R.string.code1249_night)
                    })
            //Moderate or heavy sleet showers
            1252 -> (
                    if(dayCheck) {
                        weatherIcon.setImageResource(R.mipmap.tenki_yuki)
                        weatherCondition.setText(R.string.code1252_day)
                    } else{
                        weatherIcon.setImageResource(R.mipmap.tenki_yuki_hare_yoru)
                        weatherCondition.setText(R.string.code1252_night)
                    })
            //Light snow showers
            1255 -> (
                    if(dayCheck) {
                        weatherIcon.setImageResource(R.mipmap.tenki_hare_yuki)
                        weatherCondition.setText(R.string.code1255_day)
                    } else{
                        weatherIcon.setImageResource(R.mipmap.tenki_hare_yuki_yoru)
                        weatherCondition.setText(R.string.code1255_night)
                    })
            //Moderate or heavy snow showers
            1258 -> (
                    if(dayCheck) {
                        weatherIcon.setImageResource(R.mipmap.tenki_yuki_hare)
                        weatherCondition.setText(R.string.code1258_day)
                    } else{
                        weatherIcon.setImageResource(R.mipmap.tenki_yuki_hare)
                        weatherCondition.setText(R.string.code1258_night)
                    })
            //Light showers of ice pellets
            1261 -> (
                    if(dayCheck) {
                        weatherIcon.setImageResource(R.mipmap.tenki_yuki)
                        weatherCondition.setText(R.string.code1261_day)
                    } else{
                        weatherIcon.setImageResource(R.mipmap.tenki_yuki_hare_yoru)
                        weatherCondition.setText(R.string.code1261_night)
                    })
            //Moderate or heavy showers of ice pellets
            1264 -> (
                    if(dayCheck) {
                        weatherIcon.setImageResource(R.mipmap.tenki_yuki)
                        weatherCondition.setText(R.string.code1264_day)
                    } else{
                        weatherIcon.setImageResource(R.mipmap.tenki_yuki_hare_yoru)
                        weatherCondition.setText(R.string.code1264_night)
                    })
            //Patchy light rain with thunder
            1273 -> (
                    if(dayCheck) {
                        weatherIcon.setImageResource(R.mipmap.tenki_hare_kaminari)
                        weatherCondition.setText(R.string.code1273_day)
                    } else{
                        weatherIcon.setImageResource(R.mipmap.tenki_hare_kaminari_yoru)
                        weatherCondition.setText(R.string.code1273_night)
                    })
            //Moderate or heavy rain with thunder
            1276 -> (
                    if(dayCheck) {
                        weatherIcon.setImageResource(R.mipmap.tenki_kumori_kaminari)
                        weatherCondition.setText(R.string.code1276_day)
                    } else{
                        weatherIcon.setImageResource(R.mipmap.tenki_kumori_kaminari)
                        weatherCondition.setText(R.string.code1276_night)
                    })
            //Patchy light snow with thunder
            1279 -> (
                    if(dayCheck) {
                        weatherIcon.setImageResource(R.mipmap.tenki_yuki_kaminari)
                        weatherCondition.setText(R.string.code1279_day)
                    } else{
                        weatherIcon.setImageResource(R.mipmap.tenki_yuki_kaminari)
                        weatherCondition.setText(R.string.code1279_night)
                    })
            //Moderate or heavy snow with thunder
            1282 -> (
                    if(dayCheck) {
                        weatherIcon.setImageResource(R.mipmap.tenki_kumori_yuki_kaminari)
                        weatherCondition.setText(R.string.code1282_day)
                    } else{
                        weatherIcon.setImageResource(R.mipmap.tenki_kumori_yuki_kaminari)
                        weatherCondition.setText(R.string.code1282_night)
                    })
        }
    }

    //Location Service Codes
    private fun initLocationCallbacks() {
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        locationRequest = LocationRequest()
        locationRequest.interval = 10000
        locationRequest.fastestInterval = 10000
        locationRequest.smallestDisplacement = 170f     //170 m = 0.1 mile
        locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult?) {
                locationResult ?: return
                if (locationResult.locations.isNotEmpty()) {
                    val location = locationResult.lastLocation
                    Log.d("location", location.toString())
                    val latitude = location.latitude
                    val longitude = location.longitude
                    displayAddress(latitude, longitude)
                    mLat = latitude
                    mLon = longitude
                }
            }
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        requestCode != (LOCATION_PERMISSION_CODE ?: return)
        if (grantResults[0] == PackageManager.PERMISSION_DENIED) {
            Log.d(TAG, "Permission denied")
            finish()
        }
        else {
            Log.d(TAG, "Permission granted")
        }
    }

    private fun stopLocationUpdates() {
        fusedLocationClient.removeLocationUpdates(locationCallback)
    }

    private fun showAlertLocation() {
        val dialog = AlertDialog.Builder(this)
        dialog.setMessage("Location setting is Off, Please enable for this app")
        dialog.setCancelable(false)
        dialog.setPositiveButton("Settings") { _, _ ->
            val myIntent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
            startActivity(myIntent)
        }
        dialog.setNegativeButton("Cancel") { alertDialog, _ -> alertDialog.cancel() }
        dialog.show()
    }

    private fun displayAddress(latitude: Double, longitude: Double) {
        val addresses: List<Address>?
        val geoCoder = Geocoder(applicationContext, Locale.getDefault())
        addresses = geoCoder.getFromLocation(latitude, longitude, 1)
        if (!addresses.isNullOrEmpty()) {
            if (addresses[0].locality != null) {
                city = addresses[0].locality //Example: Sydney (AUS), San Francisco (USA), Changwon-si (KOR), Sapporo (JPN)
            }
            else if (addresses[0].adminArea != null) {
                city = addresses[0].adminArea //Example: New South Wales (AUS), California (USA), Gyeongsangnam-do (KOR), Seoul (KOR), Hokkaido (JPN)
            }
            val country: String = addresses[0].countryName
            val addressDetailsLabel: TextView = findViewById(R.id.locationName)

            addressDetailsLabel.text = "$city, $country"
        }
    }

    private fun startLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
            && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION), LOCATION_PERMISSION_CODE)
            return
        }
        fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, null)
    }

    override fun onPause() {
        super.onPause()
        stopLocationUpdates()
    }

    override fun onResume() {
        super.onResume()
        startLocationUpdates()
    }
}
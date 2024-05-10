package com.thepseudoartistclan.mikuweather

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.content.Intent
import android.location.LocationManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.provider.Settings
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
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
import kotlinx.coroutines.CoroutineExceptionHandler
import java.util.*
import kotlin.collections.ArrayList
import kotlin.properties.Delegates

private val TAG = "LocationServices"
private val LOCATION_PERMISSION_CODE = 1

class MainActivity : AppCompatActivity() {
    //Google Maps Location Request Variables
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var locationRequest: LocationRequest
    private lateinit var locationCallback: LocationCallback

    //Query Location
    var mLat:Double = 0.0
    var mLon:Double = 0.0
    var hour: ArrayList<Hour> = ArrayList()

    //Main screen start
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        serviceInitialisation()
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
        if(mLat != 0.0 && mLon != 0.0) {
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
        viewModel.getCurrentTemp("$mLat,$mLon")
        viewModel.weather.observe(this) {
            currentWeatherDisplay(it.current.condition.code, it.current.is_day)
            findViewById<TextView>(R.id.currentTemp).text = it.current.temp_c.toString()
            findViewById<TextView>(R.id.conditionString).text = it.current.condition.text
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
        when(code) {
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
            val city: String = addresses[0].locality
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
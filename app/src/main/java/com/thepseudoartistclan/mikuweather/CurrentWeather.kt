package com.thepseudoartistclan.mikuweather

import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import android.location.LocationManager
import android.os.Handler
import android.util.Log
import android.widget.RemoteViews
import android.widget.TextView
import androidx.core.content.ContextCompat.getSystemService
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

/**
 * Implementation of App Widget functionality.
 */
class CurrentWeather : AppWidgetProvider() {
    override fun onUpdate(context: Context, appWidgetManager: AppWidgetManager, appWidgetIds: IntArray) {
        var coordinates = "Unavailable"
        var currentcondition = "Unavailable"
        // There may be multiple widgets active, so update all of them
        for (appWidgetId in appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId, coordinates, currentcondition)
        }
    }

    override fun onEnabled(context: Context) {
        // Enter relevant functionality for when the first widget is created
    }

    override fun onDisabled(context: Context) {
        // Enter relevant functionality for when the last widget is disabled
    }

    override fun onReceive(context: Context?, intent: Intent?) {
        super.onReceive(context, intent)

    }
}

internal fun updateAppWidget(context: Context, appWidgetManager: AppWidgetManager, appWidgetId: Int, locationString: String, currentCondition:String) {
    // Construct the RemoteViews object
    val views = RemoteViews(context.packageName, R.layout.current_weather)
    views.setTextViewText(R.id.appwidget_title, "Cannot fetch data")
    views.setImageViewResource(R.id.widgetWeatherIcon, R.mipmap.tenki_hare)
    views.setTextViewText(R.id.widgetQueryLocation, locationString)
    views.setTextViewText(R.id.widgetCurrentWeather, currentCondition)
    // Instruct the widget manager to update the widget
    appWidgetManager.updateAppWidget(appWidgetId, views)
}
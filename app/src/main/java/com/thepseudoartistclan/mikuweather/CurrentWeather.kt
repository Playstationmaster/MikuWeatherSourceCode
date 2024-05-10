package com.thepseudoartistclan.mikuweather

import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import android.widget.RemoteViews

/**
 * Implementation of App Widget functionality.
 */
class CurrentWeather : AppWidgetProvider() {
    private var coordinates = "Unavailable"
    private var currentcondition = "Unavailable"

    override fun onUpdate(context: Context, appWidgetManager: AppWidgetManager, appWidgetIds: IntArray) {
        // There may be multiple widgets active, so update all of them
        for (appWidgetId in appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId, coordinates, currentcondition)
        }
    }

    override fun onEnabled(context: Context) {
        // Enter relevant functionality for when the first widget is created
        super.onEnabled(context)
    }

    override fun onDisabled(context: Context) {
        // Enter relevant functionality for when the last widget is disabled
        super.onDisabled(context)
    }

    override fun onReceive(context: Context?, intent: Intent?) {
        super.onReceive(context, intent)
    }

    override fun onDeleted(context: Context?, appWidgetIds: IntArray?) {
        // Enter relevant functionality for when the last widget is deleted
        super.onDeleted(context, appWidgetIds)
    }

    companion object {
        fun updateAppWidget(context: Context, appWidgetManager: AppWidgetManager, appWidgetId: Int, locationString: String, currentCondition:String) {
            // Construct the RemoteViews object
            val views = RemoteViews(context.packageName, R.layout.current_weather)
            views.setTextViewText(R.id.appwidget_title, "Cannot fetch data")
            views.setImageViewResource(R.id.widgetWeatherIcon, R.mipmap.tenki_hare)
            views.setTextViewText(R.id.widgetQueryLocation, locationString)
            views.setTextViewText(R.id.widgetCurrentWeather, currentCondition)
            // Instruct the widget manager to update the widget
            appWidgetManager.updateAppWidget(appWidgetId, views)
        }
    }
}
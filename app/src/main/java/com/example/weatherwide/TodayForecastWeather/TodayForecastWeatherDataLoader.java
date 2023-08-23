package com.example.weatherwide.TodayForecastWeather;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.preference.PreferenceManager;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.loader.content.AsyncTaskLoader;

import com.example.weatherwide.QueryUtilsWeatherApi;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class TodayForecastWeatherDataLoader extends AsyncTaskLoader<ArrayList<TodayForecastWeatherData>> {

    private String m_url;
    private Context m_context;

    public TodayForecastWeatherDataLoader(@NonNull Context context, String url) {
        super(context);
        m_url = url;
        m_context = context;
    }

    private String getTempUnit(SharedPreferences preferences){
        String unitsList = preferences.getString("temperature_unit", "-1");
        int index = (Integer) Integer.parseInt(unitsList);
        if(index >= 1 && index < 2){
            return "temp_f";
        }
        else{
            return "temp_c";
        }
    }

    private String getSpeedUnit(SharedPreferences preferences){
        String unitsList = preferences.getString("speed_unit", "-1");
        int index = (Integer) Integer.parseInt(unitsList);
        if(index >= 1 && index < 2){
            return "wind_mph";
        }
        else{
            return "wind_kph";
        }
    }

    @Nullable
    @Override
    public ArrayList<TodayForecastWeatherData> loadInBackground() {
        ArrayList<TodayForecastWeatherData> data = new ArrayList<>();
        String rootObject = QueryUtilsWeatherApi.fetchAndRetrieveData(m_url);
        if(rootObject == null){
            return null;
        }

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(m_context);
        String tempUnit = getTempUnit(preferences);
        String speedUnit = getSpeedUnit(preferences);

        Log.v("SharedTest", speedUnit);

        JSONObject root = null;
        try {
            root = new JSONObject(rootObject);
            JSONObject current = root.getJSONObject("current");
            JSONObject forecast = root.getJSONObject("forecast");
            JSONArray forecastDay = forecast.getJSONArray("forecastday");
            JSONObject today = forecastDay.getJSONObject(0);
            JSONArray hour = today.getJSONArray("hour");
            JSONObject astro = forecastDay.getJSONObject(0).getJSONObject("astro");
            double wind = current.getDouble(speedUnit);
            int pressure = current.getInt("pressure_mb");
            int humidity = current.getInt("humidity");
            int uv = current.getInt("uv");
            String sunrise = astro.getString("sunrise");
            String sunset = astro.getString("sunset");
            data.add(new TodayForecastWeatherData(wind, pressure, humidity, uv, sunrise, sunset));
            for(int i = 0; i < hour.length(); i++){
                JSONObject hourly = hour.getJSONObject(i);
                String time = hourly.getString("time");
                int temperature = hourly.getInt(tempUnit);
                JSONObject condition = hourly.getJSONObject("condition");
                Bitmap icon = QueryUtilsWeatherApi.extractIconFromUrl(condition.getString("icon"));
                data.add(new TodayForecastWeatherData(time, temperature, icon));
            }
        }
        catch (JSONException e){
            e.printStackTrace();
        }

        return data;
    }
}

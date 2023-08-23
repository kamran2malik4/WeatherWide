package com.example.weatherwide.CurrentAndForcastWeather;

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

public class CurrentAndForecastWeatherDataLoader extends AsyncTaskLoader<ArrayList<CurrentAndForecastWeatherData>> {

    private String m_url;

    private Context m_context;

    public CurrentAndForecastWeatherDataLoader(@NonNull Context context, String url) {
        super(context);
        m_context = context;
        m_url = url;
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

    @Nullable
    @Override
    public ArrayList<CurrentAndForecastWeatherData> loadInBackground() {
        ArrayList<CurrentAndForecastWeatherData> data = new ArrayList<>();
        String rootObject = QueryUtilsWeatherApi.fetchAndRetrieveData(m_url);

        if(rootObject == null){
            return null;
        }

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(m_context);
        String tempUnit = getTempUnit(preferences);


        JSONObject root = null;

        try {
            root = new JSONObject(rootObject);
            JSONObject current = root.getJSONObject("current");
            JSONObject forecast = root.getJSONObject("forecast");
            JSONArray forecastDay = forecast.getJSONArray("forecastday");
            JSONObject condition = current.getJSONObject("condition");
            int temperature = 0;
            String text = null;
            String currentIconUrl = null;
            temperature = current.getInt(tempUnit);
            text = condition.getString("text");
            currentIconUrl = condition.getString("icon");
            Bitmap icon = QueryUtilsWeatherApi.extractIconFromUrl(currentIconUrl);
            data.add(new CurrentAndForecastWeatherData(text, temperature, icon));
            for(int i = 0; i < forecastDay.length(); i++){
                long date = 0;
                int maxTemp = 0;
                int minTemp = 0;
                JSONObject currentDay = forecastDay.getJSONObject(i);
                date = currentDay.getLong("date_epoch");
                JSONObject day = currentDay.getJSONObject("day");
                maxTemp = day.getInt("max" + tempUnit);
                minTemp = day.getInt("min" + tempUnit);
                JSONObject dayCondition = day.getJSONObject("condition");
                String iconUrl = dayCondition.getString("icon");
                Bitmap dayIcon = QueryUtilsWeatherApi.extractIconFromUrl(iconUrl);
                data.add(new CurrentAndForecastWeatherData(dayIcon, date, maxTemp, minTemp));
            }
        }
        catch (JSONException e){
            e.printStackTrace();
        }

        return data;
    }
}

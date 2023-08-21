package com.example.weatherwide.CurrentAndForcastWeather;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.loader.content.AsyncTaskLoader;

import java.util.ArrayList;

public class CurrentAndForecastWeatherDataLoader extends AsyncTaskLoader<ArrayList<CurrentAndForecastWeatherData>> {

    private String m_url;

    private Context m_context;

    public CurrentAndForecastWeatherDataLoader(@NonNull Context context, String url) {
        super(context);
        m_context = context;
        m_url = url;
    }

    @Nullable
    @Override
    public ArrayList<CurrentAndForecastWeatherData> loadInBackground() {
        return QueryUtilsCurrentAndForecastWeather.extractCurrentAndForecastWeatherData(m_url, m_context);
    }
}

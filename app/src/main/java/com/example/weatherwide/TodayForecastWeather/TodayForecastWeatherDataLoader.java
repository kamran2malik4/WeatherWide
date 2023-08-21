package com.example.weatherwide.TodayForecastWeather;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.loader.content.AsyncTaskLoader;

import java.util.ArrayList;

public class TodayForecastWeatherDataLoader extends AsyncTaskLoader<ArrayList<TodayForecastWeatherData>> {

    private String m_url;
    private Context m_context;

    public TodayForecastWeatherDataLoader(@NonNull Context context, String url) {
        super(context);
        m_url = url;
        m_context = context;
    }

    @Nullable
    @Override
    public ArrayList<TodayForecastWeatherData> loadInBackground() {
        return QueryUtilsTodayForecastWeather.extractTodayForecastWeatherData(m_url, m_context);
    }
}

package com.example.weatherwide.CurrentAndForcastWeather;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.Loader;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.weatherwide.R;

import java.util.ArrayList;

public class CurrentAndForecastFragment extends Fragment implements LoaderManager.LoaderCallbacks<ArrayList<CurrentAndForecastWeatherData>> {

    private static String WEATHER_API_URL = "https://api.weatherapi.com/v1/forecast.json?";

    private static int LOADER_ID = 0;

    private TextView m_currentWeatherCondition;
    private TextView m_currentWeatherTemperature;
    private ImageView m_currentWeatherImage;
    private RecyclerView m_weatherForecast;

    private ProgressBar m_progressBar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_current_and_forecast, container, false);

        m_progressBar = view.findViewById(R.id.progress_bar_horizontal_current_and_forecast);
        m_currentWeatherCondition = view.findViewById(R.id.current_weather_condition);
        m_currentWeatherTemperature = view.findViewById(R.id.current_weather_temperature);
        m_currentWeatherImage = view.findViewById(R.id.current_weather_image);
        m_weatherForecast = view.findViewById(R.id.six_day_forecast_recycler);

        getLoaderManager().initLoader(LOADER_ID, null, this).forceLoad();

        return view;
    }

    private String getTemperatureUnit(SharedPreferences preferences){
        String unit = preferences.getString("temperature_unit", "-1");
        int index = (Integer) Integer.parseInt(unit);
        if(index >= 1 && index < 2){
            return "°F";
        }
        else{
            return "°C";
        }
    }

    private void updateCurrentWeather(CurrentAndForecastWeatherData currentWeather){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        String tempUnit = getTemperatureUnit(preferences);
        m_currentWeatherCondition.setText(currentWeather.getCondition());
        m_currentWeatherTemperature.setText(currentWeather.getTemperature() + tempUnit);
        m_currentWeatherImage.setImageBitmap(currentWeather.getIcon());
    }


    private void updateForecastView(ArrayList<CurrentAndForecastWeatherData> forecast){
        WeatherForecastRecyclerAdapter adapter = new WeatherForecastRecyclerAdapter(getActivity(), forecast);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        m_weatherForecast.setLayoutManager(linearLayoutManager);
        m_weatherForecast.setAdapter(adapter);
    }


    @NonNull
    @Override
    public Loader<ArrayList<CurrentAndForecastWeatherData>> onCreateLoader(int id, @Nullable Bundle args) {
        SharedPreferences preferences = this.getActivity().getSharedPreferences("CityName", Context.MODE_PRIVATE);

        String key = "417f11a692f7406d85b150359231008";
        String city = preferences.getString("name", "");

        Log.v("CityTest", "onLoad" + city);

        Uri baseUri = Uri.parse(WEATHER_API_URL);

        Uri.Builder uriBuilder = baseUri.buildUpon();
        uriBuilder.appendQueryParameter("key", key);
        uriBuilder.appendQueryParameter("days", "14");
        uriBuilder.appendQueryParameter("q", city);
        return new CurrentAndForecastWeatherDataLoader(getActivity(), uriBuilder.toString());
    }

    @Override
    public void onLoadFinished(@NonNull Loader<ArrayList<CurrentAndForecastWeatherData>> loader, ArrayList<CurrentAndForecastWeatherData> data) {
        if(data != null){
            updateCurrentWeather(data.get(0));
            updateForecastView(data);
        }
        m_progressBar.setVisibility(View.GONE);
    }

    @Override
    public void onLoaderReset(@NonNull Loader<ArrayList<CurrentAndForecastWeatherData>> loader) {

    }
}

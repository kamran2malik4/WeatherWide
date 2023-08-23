package com.example.weatherwide.TodayForecastWeather;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.Loader;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.weatherwide.R;

import java.util.ArrayList;

public class TodayForecastFragment extends Fragment implements LoaderManager.LoaderCallbacks<ArrayList<TodayForecastWeatherData>>{

    private RecyclerView m_hourlyRecycler;
    private TextView sunrise, humidity, wind, uvIndex, atmp, sunset;

    private static String WEATHER_API_URL = "https://api.weatherapi.com/v1/forecast.json?";

    private static int LOADER_ID = 0;

    private ProgressBar m_progressBar;
    private LinearLayout m_layout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_today_forecast, container, false);
        m_progressBar = view.findViewById(R.id.progress_bar_horizontal_today_forecast);
        m_layout = view.findViewById(R.id.today_forecast_data_layout);
        m_layout.setVisibility(View.INVISIBLE);
        m_hourlyRecycler = view.findViewById(R.id.hour_recycler_view);
        sunrise = view.findViewById(R.id.sunrise_text_view);
        humidity = view.findViewById(R.id.humidity_text_view);
        wind = view.findViewById(R.id.wind_speed_text_view);
        uvIndex = view.findViewById(R.id.uvindex_text_view);
        atmp = view.findViewById(R.id.atmp_text_view);
        sunset = view.findViewById(R.id.sunset_text_view);

        getLoaderManager().initLoader(LOADER_ID, null, this).forceLoad();

        return view;
    }

    private String getSpeedUnit(){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        String unitsList = preferences.getString("speed_unit", "-1");
        int index = (Integer) Integer.parseInt(unitsList);
        if(index >= 1 && index < 2){
            return "m/h";
        }
        else{
            return "km/h";
        }
    }

    private void updateLayout(TodayForecastWeatherData currentDetails){
        if(currentDetails != null){
            sunrise.setText(currentDetails.getSunriseTime());
            humidity.setText(currentDetails.getHumidity() + "%");
            wind.setText(currentDetails.getWind() + getSpeedUnit());
            uvIndex.setText("" + currentDetails.getUvIndex());
            atmp.setText(currentDetails.getAtmPressure() + " mmHg");
            sunset.setText(currentDetails.getSunsetTime());
        }
    }

    private void updateRecyclerLayout(ArrayList<TodayForecastWeatherData> forecast){
        HourlyForecastRecyclerAdapter adapter = new HourlyForecastRecyclerAdapter(getActivity(), forecast);
        LinearLayoutManager manager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        m_hourlyRecycler.setLayoutManager(manager);
        m_hourlyRecycler.setAdapter(adapter);
    }

    @NonNull
    @Override
    public Loader<ArrayList<TodayForecastWeatherData>> onCreateLoader(int id, @Nullable Bundle args) {
        SharedPreferences preferences = this.getActivity().getSharedPreferences("CityName", Context.MODE_PRIVATE);

        String key = "417f11a692f7406d85b150359231008";
        String city = preferences.getString("name", "");

        Log.v("CityTest", "onLoad" + city);

        Uri baseUri = Uri.parse(WEATHER_API_URL);

        Uri.Builder uriBuilder = baseUri.buildUpon();
        uriBuilder.appendQueryParameter("key", key);
        uriBuilder.appendQueryParameter("days", "14");
        uriBuilder.appendQueryParameter("q", city);
        return new TodayForecastWeatherDataLoader(getActivity(), uriBuilder.toString());
    }

    @Override
    public void onLoadFinished(@NonNull Loader<ArrayList<TodayForecastWeatherData>> loader, ArrayList<TodayForecastWeatherData> data) {
        if(data != null){
            updateLayout(data.get(0));
            updateRecyclerLayout(data);
            m_layout.setVisibility(View.VISIBLE);
        }
        m_progressBar.setVisibility(View.GONE);
    }

    @Override
    public void onLoaderReset(@NonNull Loader<ArrayList<TodayForecastWeatherData>> loader) {

    }
}

package com.example.weatherwide.CurrentAndForcastWeather;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.weatherwide.R;

import java.util.ArrayList;

public class WeatherForecastRecyclerAdapter extends RecyclerView.Adapter<WeatherForecastRecyclerAdapter.MyViewHolder>{

    private Activity m_context;
    private ArrayList<CurrentAndForecastWeatherData> m_forecast;

    public WeatherForecastRecyclerAdapter(Activity context, ArrayList<CurrentAndForecastWeatherData> forecast){
        m_context = context;
        m_forecast = forecast;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(m_context).inflate(R.layout.days_forecast, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        CurrentAndForecastWeatherData currentItem = m_forecast.get(position + 1);
        holder.m_icon.setImageBitmap(currentItem.getIcon());
        holder.m_Day.setText(currentItem.getDay());
        holder.m_maxTemperature.setText(currentItem.getMaxTemperature() + "");
        holder.m_minTemperature.setText(currentItem.getMinTemperature() + "");
    }

    @Override
    public int getItemCount() {
        return m_forecast.size() - 1;
    }

    class MyViewHolder extends RecyclerView.ViewHolder{
        private TextView m_Day, m_maxTemperature, m_minTemperature;
        private ImageView m_icon;
        public MyViewHolder(View itemView) {
            super(itemView);
            m_icon = itemView.findViewById(R.id.forecast_icon);
            m_Day = itemView.findViewById(R.id.day_text_view);
            m_maxTemperature = itemView.findViewById(R.id.max_temperature);
            m_minTemperature = itemView.findViewById(R.id.min_temperature);
        }
    }
}

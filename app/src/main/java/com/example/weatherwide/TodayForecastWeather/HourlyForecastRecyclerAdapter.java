package com.example.weatherwide.TodayForecastWeather;

import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.weatherwide.R;

import java.util.ArrayList;

public class HourlyForecastRecyclerAdapter extends RecyclerView.Adapter<HourlyForecastRecyclerAdapter.MyViewHolder>{

    private Activity m_context;
    private ArrayList<TodayForecastWeatherData> m_forecast;

    public HourlyForecastRecyclerAdapter(Activity context, ArrayList<TodayForecastWeatherData> forecast){
        m_context = context;
        m_forecast = forecast;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(m_context).inflate(R.layout.hourly_forecast, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        TodayForecastWeatherData current = m_forecast.get(position + 1);
        holder.m_hour.setText(current.getHour());
        holder.m_temp.setText(current.getTemperature() + "");
        holder.m_icon.setImageBitmap(current.getIcon());
    }

    @Override
    public int getItemCount() {
        return m_forecast.size() - 1;
    }

    class MyViewHolder extends RecyclerView.ViewHolder{
        private ImageView m_icon;
        private TextView m_hour;
        private TextView m_temp;
        public MyViewHolder(View itemView) {
            super(itemView);
            m_icon = itemView.findViewById(R.id.hourly_forecast_icon);
            m_hour =itemView.findViewById(R.id.hour_text_view);
            m_temp = itemView.findViewById(R.id.hour_temperature_view);
            if(m_icon == null && m_hour == null && m_temp == null){
                Log.i("MyViewHolder", "Null");
            }
        }
    }
}

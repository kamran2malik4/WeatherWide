package com.example.weatherwide.CurrentAndForcastWeather;

import android.graphics.Bitmap;

import java.text.SimpleDateFormat;
import java.util.Date;

//This class will be used as an ArrayList
public class CurrentAndForecastWeatherData {
    //This data should be used with array index 0
    //Attributes
    private String m_condition;
    private int m_temperature;
    private Bitmap m_icon;

    //Constructor
    public CurrentAndForecastWeatherData(String condition, int temperature, Bitmap icon){
        m_condition = condition;
        m_temperature = temperature;
        m_icon = icon;
    }

    //Getters
    public String getCondition(){ return m_condition; }
    public int getTemperature(){ return m_temperature; }

    //This data is used for updating RecyclerView
    //Attributes
    private long m_time;
    private int m_maxTemperature;
    private int m_minTemperature;

    private String convertLongToDay(){
        String day = null;
        Date date = new Date(m_time * 1000);
        SimpleDateFormat format = new SimpleDateFormat("EEE");
        day = format.format(date);
        return day;
    }

    //Constructor
    public CurrentAndForecastWeatherData(Bitmap icon, long time, int maxTemperature, int minTemperature){
        m_icon = icon;
        m_time = time;
        m_maxTemperature = maxTemperature;
        m_minTemperature = minTemperature;
    }
    //Getters
    public String getDay(){ return convertLongToDay(); }
    public int getMaxTemperature(){ return m_maxTemperature; }
    public int getMinTemperature(){ return m_minTemperature; }
    public Bitmap getIcon(){ return m_icon; }

}

package com.example.weatherwide.TodayForecastWeather;

import android.graphics.Bitmap;

public class TodayForecastWeatherData {
    //Attributes
    private double m_wind;
    private int m_atmPressure;
    private int m_humidity;
    private  int m_uvIndex;
    private String m_sunriseTime, m_sunsetTime;
    //Constructor
    public TodayForecastWeatherData(double wind, int atmPressure, int humidity, int uvIndex,
                                 String sunriseTime, String sunsetTime){
        m_wind = wind;
        m_atmPressure = atmPressure;
        m_humidity = humidity;
        m_uvIndex = uvIndex;
        m_sunriseTime = sunriseTime;
        m_sunsetTime = sunsetTime;
    }
    //Getters
    public double getWind(){ return m_wind; }
    public int getAtmPressure(){ return m_atmPressure; }
    public int getHumidity(){ return m_humidity; }
    public int getUvIndex(){ return m_uvIndex; }
    public String getSunriseTime(){ return m_sunriseTime; }
    public String getSunsetTime(){ return m_sunsetTime; }


    //Attributes
    private String m_hour;
    private int m_temperature;
    private Bitmap m_icon;
    //Constructor
    public TodayForecastWeatherData(String hour, int temperature, Bitmap icon){
        m_hour = hour;
        m_temperature = temperature;
        m_icon = icon;
    }
    //Getters
    public String getHour(){
        String hour = "0";
        int index = m_hour.indexOf(' ');
        hour = m_hour.substring(index);
        return hour;
    }
    public int getTemperature(){ return m_temperature; }
    public Bitmap getIcon(){ return m_icon; }
}

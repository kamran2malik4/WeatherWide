package com.example.weatherwide.TodayForecastWeather;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.preference.PreferenceManager;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class QueryUtilsTodayForecastWeather {

    private static Bitmap extractIconFromUrl(String iconUrl){
        Bitmap icon = null;

        URL url = null;

        try{
            url = new URL("https:" + iconUrl);
            icon = BitmapFactory.decodeStream(url.openConnection().getInputStream());
        }
        catch (Exception e){
            e.printStackTrace();
        }

        return icon;
    }

    private static String getTempUnit(SharedPreferences preferences){
        String unitsList = preferences.getString("temperature_unit", "-1");
        int index = (Integer) Integer.parseInt(unitsList);
        if(index >= 1 && index < 2){
            return "temp_f";
        }
        else{
            return "temp_c";
        }
    }

    private static String getSpeedUnit(SharedPreferences preferences){
        String unitsList = preferences.getString("speed_unit", "-1");
        int index = (Integer) Integer.parseInt(unitsList);
        if(index >= 1 && index < 2){
            return "wind_mph";
        }
        else{
            return "wind_kph";
        }
    }

    public static ArrayList<TodayForecastWeatherData> extractTodayForecastWeatherData(String url, Context context){
        ArrayList<TodayForecastWeatherData> data = new ArrayList<>();
        String rootObject = fetchAndRetrieveData(url);
        if(rootObject == null){
            return null;
        }

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
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
                Bitmap icon = extractIconFromUrl(condition.getString("icon"));
                data.add(new TodayForecastWeatherData(time, temperature, icon));
            }
        }
        catch (JSONException e){
            e.printStackTrace();
        }

        return data;
    }

    public static String fetchAndRetrieveData(String web_url){
        Log.i("Request", "fetchAndRetrieveData");
        String rootObject = null;
        URL url = createURLObject(web_url);
        try{
            rootObject = makehttprequest(url);
        }
        catch (IOException e){
            e.printStackTrace();
        }
        return rootObject;
    }

    //This function create URL object
    private static URL createURLObject(String web_url){
        Log.i("Request", "createURLObject");
        URL url = null;
        try{
            url = new URL(web_url);
        }
        catch (MalformedURLException e){
            e.printStackTrace();
        }
        return url;
    }

    //This function create and http request and gets response if response is success data is extracted
    private static String makehttprequest(URL url) throws IOException{
        Log.i("Request", "makehttprequest");
        String rootObject = null;
        HttpURLConnection connection = null;
        InputStream inputStream = null;

        try{
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setConnectTimeout(15000);
            connection.setReadTimeout(20000);
            connection.connect();
            Log.i("Request", "" + connection.getResponseCode());
            if(connection.getResponseCode() == 200){
                inputStream = connection.getInputStream();
                rootObject = retrieveJSONString(inputStream);
            }
        }
        catch (IOException e){
            e.printStackTrace();
        }
        finally {
            connection.disconnect();
            if(inputStream != null){
                inputStream.close();
            }
        }
        return rootObject;
    }

    //This function will read data from input stream and return JSON string
    private static String retrieveJSONString(InputStream inputStream){
        Log.i("Request", "retrieveJSONString");
        StringBuilder builder = new StringBuilder();
        BufferedReader reader = null;
        InputStreamReader inputReader = null;
        try{
            inputReader = new InputStreamReader(inputStream);
            reader = new BufferedReader(inputReader);
            String line = reader.readLine();
            while (line != null){
                builder.append(line);
                line = reader.readLine();
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return builder.toString();
    }
}

package com.example.weatherwide.CurrentAndForcastWeather;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.preference.PreferenceManager;
import android.util.Log;

import com.example.weatherwide.R;

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

public class QueryUtilsCurrentAndForecastWeather {
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

    public static ArrayList<CurrentAndForecastWeatherData> extractCurrentAndForecastWeatherData(String url, Context context){
        ArrayList<CurrentAndForecastWeatherData> data = new ArrayList<>();
        String rootObject = fetchAndRetrieveData(url);

        if(rootObject == null){
            return null;
        }

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
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
            Bitmap icon = extractIconFromUrl(currentIconUrl);
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
                Bitmap dayIcon = extractIconFromUrl(iconUrl);
                data.add(new CurrentAndForecastWeatherData(dayIcon, date, maxTemp, minTemp));
            }
        }
        catch (JSONException e){
            e.printStackTrace();
        }

        return data;
    }
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
    private static String fetchAndRetrieveData(String web_url){
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

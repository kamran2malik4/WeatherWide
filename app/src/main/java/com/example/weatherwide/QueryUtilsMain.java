package com.example.weatherwide;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

//All methods of this class are static and will retrieve data from internet
public final class QueryUtilsMain {

    public static LocationAndDate extractLocationAndDate(String url){
        Log.i("Request", "extractLocationAndDateFromJSON");

        String rootObject = fetchAndRetrieveData(url);

        if(rootObject == null){
            return null;
        }
        LocationAndDate current = null;
        JSONObject root = null;
        try {
            root = new JSONObject(rootObject);
            JSONObject location = root.getJSONObject("location");
            String currentLocation = location.getString("name") + ", " + location.getString("region");
            long currentDate = location.getLong("localtime_epoch");
            Log.i("Request", currentLocation + " " + currentDate);
            current = new LocationAndDate(currentLocation, currentDate);
        }
        catch (JSONException e){
            e.printStackTrace();
        }
        return current;
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

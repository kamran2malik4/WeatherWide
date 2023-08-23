package com.example.weatherwide;

import android.content.Context;
import android.util.Log;

import androidx.loader.content.AsyncTaskLoader;

import org.json.JSONException;
import org.json.JSONObject;

public class LocationAndDateAsyncLoader extends AsyncTaskLoader<LocationAndDate> {

    private String m_url;

    public LocationAndDateAsyncLoader(Context context, String url) {
        super(context);
        m_url = url;
    }

    @Override
    public LocationAndDate loadInBackground() {

        String rootObject = QueryUtilsWeatherApi.fetchAndRetrieveData(m_url);

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
            current = new LocationAndDate(currentLocation, currentDate);
        }
        catch (JSONException e){
            e.printStackTrace();
        }
        return current;
    }
}

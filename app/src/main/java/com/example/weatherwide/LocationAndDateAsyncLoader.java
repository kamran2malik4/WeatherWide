package com.example.weatherwide;

import android.content.Context;
import android.util.Log;

import androidx.loader.content.AsyncTaskLoader;

public class LocationAndDateAsyncLoader extends AsyncTaskLoader<LocationAndDate> {

    private String m_url;

    public LocationAndDateAsyncLoader(Context context, String url) {
        super(context);
        m_url = url;
    }

    @Override
    public LocationAndDate loadInBackground() {

        LocationAndDate current = QueryUtilsMain.extractLocationAndDate(m_url);

        return current;
    }
}

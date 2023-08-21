package com.example.weatherwide;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.Loader;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.tbuonomo.viewpagerdotsindicator.SpringDotsIndicator;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<LocationAndDate> {

    //This is url for fetching data from weatherapi server
    private static String WEATHER_API_URL = "https://api.weatherapi.com/v1/forecast.json?";

    //This variable is Unique id for AsyncTaskLoader Object
    private static final int LOADER_ID = 0;

    private SharedPreferences m_preferences;

    private String m_currentCity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportLoaderManager().initLoader(LOADER_ID, null, this).forceLoad();
        initializeFragments();

        m_preferences = getSharedPreferences("CityName", MODE_PRIVATE);
        m_currentCity = m_preferences.getString("name", "");

        if(m_currentCity.isEmpty()){
            Toast.makeText(this, "Add City", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(MainActivity.this, SearchActivity.class);
            startActivity(intent);
        }

        ImageView searchCities = findViewById(R.id.search_city_activity);
        searchCities.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, SearchActivity.class);
                startActivity(intent);
            }
        });

        ImageView changeSettings = findViewById(R.id.change_settings_activity);
        changeSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
                startActivity(intent);
            }
        });
        Log.v("CityTest", "Create " + m_currentCity);
    }

    @Override
    protected void onResume(){
        super.onResume();
        m_preferences = getSharedPreferences("CityName", MODE_PRIVATE);
        String changeCity = m_preferences.getString("name", "");
        if(!m_currentCity.equals(changeCity)){
            finish();
            startActivity(getIntent());
        }
    }

    private void updateLayout(LocationAndDate current){
        if(current != null){
            TextView location = findViewById(R.id.location_text_view);
            location.setText(current.getLocation());
            TextView date = findViewById(R.id.date_text_view);
            date.setText(current.getDate());
        }
    }

    //This function makes necessary initialization for fragments
    private void initializeFragments(){
        ViewPager weatherPages = findViewById(R.id.weather_fragments);
        SpringDotsIndicator indicator = findViewById(R.id.fragment_dot_indicator);
        FragmentManager fm = getSupportFragmentManager();
        WeatherFragmentPagerAdapter adapter = new WeatherFragmentPagerAdapter(fm);
        weatherPages.setAdapter(adapter);
        indicator.setViewPager(weatherPages);
    }

    @NonNull
    @Override
    public Loader<LocationAndDate> onCreateLoader(int id, @Nullable Bundle args) {
        SharedPreferences preferences = getSharedPreferences("CityName", MODE_PRIVATE);

        String key = "417f11a692f7406d85b150359231008";
        String city = preferences.getString("name", "");

        Log.v("CityTest", "onLoad" + city);

        Uri baseUri = Uri.parse(WEATHER_API_URL);

        Uri.Builder uriBuilder = baseUri.buildUpon();
        uriBuilder.appendQueryParameter("key", key);
        uriBuilder.appendQueryParameter("days", "14");
        uriBuilder.appendQueryParameter("q", city);
        return new LocationAndDateAsyncLoader(this, uriBuilder.toString());
    }

    @Override
    public void onLoadFinished(@NonNull Loader<LocationAndDate> loader, LocationAndDate data) {
        if(data != null){
            updateLayout(data);
        }
        else{
            if(!m_currentCity.isEmpty()){
                Toast.makeText(this, "Location or Internet Error", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onLoaderReset(@NonNull Loader<LocationAndDate> loader) {

    }
}

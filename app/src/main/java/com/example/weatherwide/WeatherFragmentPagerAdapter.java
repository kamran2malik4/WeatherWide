package com.example.weatherwide;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.weatherwide.CurrentAndForcastWeather.CurrentAndForecastFragment;
import com.example.weatherwide.TodayForecastWeather.TodayForecastFragment;

public class WeatherFragmentPagerAdapter extends FragmentPagerAdapter {

    public WeatherFragmentPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        if (position == 0){
            return new CurrentAndForecastFragment();
        }
        else {
            return new TodayForecastFragment();
        }
    }

    @Override
    public int getCount() {
        return 2;
    }
}

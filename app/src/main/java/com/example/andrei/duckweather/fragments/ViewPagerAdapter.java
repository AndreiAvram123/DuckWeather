package com.example.andrei.duckweather.fragments;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.example.andrei.duckweather.model.Forecast;

import java.util.ArrayList;

public class ViewPagerAdapter extends FragmentPagerAdapter {

    private ArrayList<Fragment> fragmentList;

    public ViewPagerAdapter(FragmentManager fm, Forecast forecast) {
        super(fm);
        fragmentList = new ArrayList<>();
        getFragments(forecast);

    }

    private void getFragments(Forecast forecast) {
        fragmentList.add(CurrentWeatherFragment.getInstance(forecast.getCurrentWeather()));
        fragmentList.add(DailyFragment.getInstance(forecast.getCurrentWeekWeather()));
        fragmentList.add(HourlyFragment.getInstance(forecast.getHourlyWeather()));

    }


    @Override
    public Fragment getItem(int index) {
        return fragmentList.get(index);
    }

    @Override
    public int getCount() {
        return fragmentList.size();

    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return "Current";

            case 1:
                return "Daily";

            case 2:
                return "Hourly";
            default:
                return null;
        }
    }
}

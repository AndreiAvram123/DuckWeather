package com.example.andrei.duckweather.model;

/**
 * This class models the Forecast
 * of an entire week
 */
public class Forecast {
    private CurrentWeather currentWeather;
    private DailyWeather[] currentWeekWeather;
    private HourlyWeather[] hourlyWeather;


    public void setCurrentWeather(CurrentWeather currentWeather) {
        this.currentWeather = currentWeather;
    }

    public void setCurrentWeekWeather(DailyWeather[] currentWeekWeather) {
        this.currentWeekWeather = currentWeekWeather;
    }

    public void setHourlyWeather(HourlyWeather[] hourlyWeather) {
        this.hourlyWeather = hourlyWeather;
    }

    public DailyWeather[] getCurrentWeekWeather() {
        return currentWeekWeather;
    }

    public HourlyWeather[] getHourlyWeather() {
        return hourlyWeather;
    }

    public CurrentWeather getCurrentWeather() {
        return currentWeather;
    }

}

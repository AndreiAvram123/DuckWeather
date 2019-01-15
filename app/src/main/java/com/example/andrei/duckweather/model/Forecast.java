package com.example.andrei.duckweather.model;

/**
 * This class contains 3 objects:
 * CurrentWeather
 * DailyWeather
 * HourlyWeather
 */
public class Forecast {
    private CurrentWeather currentWeather;
    private DailyWeather[] currentWeekWeather;
    private HourlyWeather[] twoDaysWeather;
    public CurrentWeather getCurrentWeather() {
        return currentWeather;
    }



    public void setCurrentWeather(CurrentWeather currentWeather) {
        this.currentWeather = currentWeather;
    }

    public void setCurrentWeekWeather(DailyWeather[] currentWeekWeather) {
        this.currentWeekWeather = currentWeekWeather;
    }

    public void setTwoDaysWeather(HourlyWeather[]twoDaysWeather){
        this.twoDaysWeather = twoDaysWeather;
    }

    public DailyWeather[] getCurrentWeekWeather() {
        return currentWeekWeather;
    }

    public HourlyWeather[] getTwoDaysWeather(){
        return twoDaysWeather;
    }


}

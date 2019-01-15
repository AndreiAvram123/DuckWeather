package com.example.andrei.duckweather.model;


import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;


@android.arch.persistence.room.Dao
public interface Dao {
    @Query("DELETE FROM current_weather")
    public void deleteAllDataFromCurrentWeather();

    @Query("DELETE FROM hourly_weather")
    public void deleteAllDataFromHourlyWeather();

    @Query("DELETE FROM daily_weather")
    public void deleteAllDataFromDailyWeather();

    @Query("SELECT * FROM current_weather")
    public CurrentWeather getCurrentWeather();

    @Query("SELECT * FROM daily_weather")
    public DailyWeather[] getCurrentWeekWeather();

    @Query("SELECT * FROM hourly_weather")
    public HourlyWeather[] getTwoDaysWeather();

    @Insert
    public void insertCurrentWeather(CurrentWeather currentWeather);

    @Insert
    public void insertCurrentWeekWeather(DailyWeather[] weekWeather);

    @Insert
    public void insertTwoDaysWeather(HourlyWeather[] twoDaysWeather);
}

package com.example.andrei.duckweather.model;


import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;


@android.arch.persistence.room.Dao
public interface Dao {

    @Query("DELETE FROM current_weather")
    void deleteAllDataFromCurrentWeather();

    @Query("DELETE FROM hourly_weather")
    void deleteAllDataFromHourlyWeather();

    @Query("DELETE FROM daily_weather")
    void deleteAllDataFromDailyWeather();

    @Query("SELECT * FROM current_weather")
     CurrentWeather getCurrentWeather();

    @Query("SELECT * FROM daily_weather")
     DailyWeather[] getCurrentWeekWeather();

    @Query("SELECT * FROM hourly_weather")
     HourlyWeather[] getTwoDaysWeather();

    @Insert
     void insertCurrentWeather(CurrentWeather currentWeather);

    @Insert
     void insertCurrentWeekWeather(DailyWeather[] weekWeather);

    @Insert
     void insertTwoDaysWeather(HourlyWeather[] twoDaysWeather);

}

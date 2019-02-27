package com.example.andrei.duckweather.model;


import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

/**
 * This class is used in order to interact with the backend database
 */
@Database(entities = {DailyWeather.class,HourlyWeather.class,CurrentWeather.class},version = 1,exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {
   public abstract DatabaseInterface dao();

}

package com.example.andrei.duckweather.model;


import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

@Database(entities = {DailyWeather.class,HourlyWeather.class,CurrentWeather.class},version = 1)
public abstract class AppDatabase extends RoomDatabase {
   public abstract Dao dao();

}

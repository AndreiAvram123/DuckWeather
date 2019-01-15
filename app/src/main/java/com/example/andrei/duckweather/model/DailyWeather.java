package com.example.andrei.duckweather.model;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.os.Parcel;
import android.os.Parcelable;


@Entity(tableName = "daily_weather")
public class DailyWeather implements Parcelable {
    @PrimaryKey(autoGenerate = true)
    public int id;
    private String day;
    @ColumnInfo(name = "sunrise_time")
    private String sunriseTime;
    @ColumnInfo(name = "sunsetTime")
    private String sunsetTime;
    @ColumnInfo(name = "temperature_min")
    private int temperatureMin;
    @ColumnInfo(name = "temperature_max")
    private int temperatureMax;
    @ColumnInfo(name = "week_summary")
    private String weekSummary;
    private double humidity;
    @ColumnInfo(name = "temperature_max_time")
    private String  temperatureMaxTime;
    @ColumnInfo(name = "temperature_min_time")
    private String temperatureMinTime;

    public DailyWeather() {

    }




    /**
     * !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
     * ---------------THE ROOM DATABASE REQUIRES GETTERS AND SETTERS FOR EVERY PRIVATE FIELD IN ORDER TO WORK-------------------------
     */



    public void setHumidity(double humidity) {
        this.humidity = humidity;
    }

    public void setWeekSummary(String weekSummary) {
        this.weekSummary = weekSummary;
    }

    public void setTemperatureMin(int temperatureMin) {
        this.temperatureMin = temperatureMin;
    }

    public void setTemperatureMax(int temperatureMax) {
        this.temperatureMax = temperatureMax;
    }

    public double getHumidity() {
        return humidity;
    }

    public int getTemperatureMin() {
        return temperatureMin;
    }

    public int getTemperatureMax() {
        return temperatureMax;
    }

    public String getSunriseTime() {
        return sunriseTime;
    }

    public void setSunriseTime(String sunriseTime) {
        this.sunriseTime = sunriseTime;
    }

    public String getSunsetTime() {
        return sunsetTime;
    }

    public void setSunsetTime(String sunsetTime) {
        this.sunsetTime = sunsetTime;
    }

    public String getWeekSummary() {
        return weekSummary;
    }

    public String getTemperatureMaxTime() {
        return temperatureMaxTime;
    }

    public void setTemperatureMaxTime(String temperatureMaxTime) {
        this.temperatureMaxTime = temperatureMaxTime;
    }

    public String getTemperatureMinTime() {
        return temperatureMinTime;
    }

    public void setTemperatureMinTime(String temperatureMinTime) {
        this.temperatureMinTime = temperatureMinTime;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(day);
        dest.writeString(sunriseTime);
        dest.writeString(sunsetTime);
        dest.writeInt(temperatureMin);
        dest.writeInt(temperatureMax);
        dest.writeString(weekSummary);
    }

    public DailyWeather(Parcel in) {

        day = in.readString();
        sunriseTime = in.readString();
        sunsetTime = in.readString();
        temperatureMin = in.readInt();
        temperatureMax = in.readInt();
        weekSummary = in.readString();
    }

    @Ignore
    public static final Creator<DailyWeather> CREATOR = new Creator<DailyWeather>() {
        @Override
        public DailyWeather createFromParcel(Parcel in) {
            return new DailyWeather(in);
        }

        @Override
        public DailyWeather[] newArray(int size) {
            return new DailyWeather[size];
        }
    };

}

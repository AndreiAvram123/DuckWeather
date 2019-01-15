package com.example.andrei.duckweather.model;


import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

/**
 * This class is a POJO
 * It is intended to store data about the current weather
 */
@Entity(tableName = "current_weather")
public class CurrentWeather {
    //the id must be public
    @PrimaryKey(autoGenerate = true)
    public int id;

    private String summary;
    private long time;
    private int temperature;
    private String timezone;
    @ColumnInfo(name = "precipitationProbability")
    private double precipitationProbability;
    private double humidity;

    public CurrentWeather(){

    }

    /**
     * !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
     * ---------------THE ROOM DATABASE REQUIRES GETTERS AND SETTERS FOR EVERY PRIVATE FIELD IN ORDER TO WORK-------------------------
     */
    public void setSummary(String summary) {
        this.summary = summary;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public void setHumidity(double humidity) {
        this.humidity = humidity;
    }

    public void setPrecipitationProbability(double precipitationProbability) {
        this.precipitationProbability = precipitationProbability;
    }

    public void setTimezone(String timezone) {
        this.timezone = timezone;
    }

    public void setTemperature(int temperature) {
        this.temperature = temperature;
    }

    public String getSummary() {
        return summary;
    }

    public double getPrecipitationProbability() {
        return precipitationProbability;
    }

    public double getHumidity() {
        return humidity;
    }

    public int getTemperature() {
        return temperature;
    }

    public long getTime() {
        return time;
    }

    public String getTimezone() {
        return timezone;
    }

    /**
     * @return time as a String as follow  month : hour : minutes
     * This method uses a simpleDateFormat object
     * When we create a new Date object we have to multiply the number
     * of seconds by 1000 in order to convert them in milliseconds
     */
    public String getTimeAsString() {
        return Useful.formatTime(time, timezone, "EEEE h:mm");
    }


}

package com.example.andrei.duckweather.model;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.os.Parcel;
import android.os.Parcelable;



/**
 * This class is a model for an hour in the forecast
 * It uses the Parcelable interface in order
 * to pass an Array of 48 hour data from
 * the MainActivity to the HourlyActivity
 *
 * !!!!!! BE CAREFUL WITH THE PARCELABLE INTERFACE
 * BECAUSE IT MATTERS HOW YOU SERIALIZE FIELDS
 * THE ORDER MATTERS , OTHERWISE THERE IS GONNA BE
 * AN ERROR
 */
@Entity(tableName = "hourly_weather")
public class HourlyWeather implements Parcelable {
    @PrimaryKey(autoGenerate = true)
    public int id;
    private String icon;
    private String hour;
    private String summary;
    private double humidity;
    private int temperature;

    public HourlyWeather() {

    }

    public HourlyWeather(String timezone, String summary, double humidity, double temperature, long unixTime,String icon) {
        this.summary = summary;
        this.temperature = (int) temperature;
        this.hour = Utilities.formatTime(unixTime, timezone, "h:mm");
        this.humidity = humidity;
        this.icon = icon;
    }

    /**
     * -----------------------------------special Creator ---------------------------
     * !!!!!!!!! REMEMBER THAT THE THE NAME OF THE CREATOR MUST BE IN UPPERCASE LETTERS
     * OTHERWISE IT DOES NOT WORK ,THROWS AN ERROR
     */
    @Ignore
    public static Creator CREATOR = new Creator() {
        @Override
        public Object createFromParcel(Parcel source) {
            return new HourlyWeather(source);
        }

        @Override
        public Object[] newArray(int size) {
            return new HourlyWeather[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(hour);
        dest.writeString(summary);
        dest.writeDouble(humidity);
        dest.writeInt(temperature);
        dest.writeString(icon);
    }

    /**
     * BE VARY CAREFUL BECAUSE THE ORDER BY WHICH THE
     * VALUES HAS BEEN WRITTEN TO THE PARCEL MATTERS
     *
     * @param source
     */
    public HourlyWeather(Parcel source) {
        this.hour = source.readString();
        this.summary = source.readString();
        this.humidity = source.readDouble();
        this.temperature = source.readInt();
        this.icon = source.readString();
    }

    /**
     * !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
     * ---------------THE ROOM DATABASE REQUIRES GETTERS AND SETTERS FOR EVERY PRIVATE FIELD IN ORDER TO WORK-------------------------
     */


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getHour() {
        return hour;
    }

    public void setHour(String hour) {
        this.hour = hour;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public double getHumidity() {
        return humidity;
    }

    public void setHumidity(double humidity) {
        this.humidity = humidity;
    }

    public int getTemperature() {
        return temperature;
    }

    public void setTemperature(int temperature) {
        this.temperature = temperature;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }
}

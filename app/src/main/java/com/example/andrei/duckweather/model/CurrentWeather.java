package com.example.andrei.duckweather.model;


import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * This class is a POJO
 * It is intended to store data about the current weather
 */
@Entity(tableName = "current_weather")
public class CurrentWeather implements Parcelable {
    //the id must be public
    @PrimaryKey(autoGenerate = true)
    public int id;
    private int iconID;
    private String summary;
    private long time;
    private int temperature;
    private double precipitationProbability;
    private double humidity;
    private String timezone;
    private String locationName;

    //!!!!!Provide empty constructor for room !!!!!!
    public CurrentWeather() {

    }

    private CurrentWeather(Parcel in) {
        iconID = in.readInt();
        summary = in.readString();
        time = in.readLong();
        temperature = in.readInt();
        precipitationProbability = in.readDouble();
        humidity = in.readDouble();
        timezone = in.readString();

    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(iconID);
        parcel.writeString(summary);
        parcel.writeLong(time);
        parcel.writeInt(temperature);
        parcel.writeDouble(precipitationProbability);
        parcel.writeDouble(humidity);
        parcel.writeString(timezone);
    }

    @Ignore
    public static final Creator CREATOR = new Creator() {
        @Override
        public Object createFromParcel(Parcel parcel) {
            return new CurrentWeather(parcel);
        }

        @Override
        public Object[] newArray(int size) {
            return new CurrentWeather[size];
        }
    };


    public String getLocationName() {
        return locationName;
    }

    public void setLocationName(String locationName) {
        this.locationName = locationName;
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


    public int getIconID() {
        return iconID;
    }

    public void setIconID(int iconID) {
        this.iconID = iconID;
    }

    public String getTimezone() {
        return timezone;
    }

    public void setTimezone(String timezone) {
        this.timezone = timezone;
    }
}

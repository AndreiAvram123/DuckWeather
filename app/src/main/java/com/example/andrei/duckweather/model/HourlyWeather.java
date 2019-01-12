package com.example.andrei.duckweather.model;

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
public class HourlyWeather implements Parcelable {
    private String hour;
    private String summary;
    private double humidity;
    private int temperature;
     public HourlyWeather(String timezone,String summary,double humidity,double temperature,long unixTime){
         this.summary = summary;
         this.temperature = (int)temperature;
         this.hour = Useful.formatTime(unixTime,timezone,"h:mm");
         this.humidity = humidity;
     }

    /**
     *    -----------------------------------special Creator ---------------------------
     *    !!!!!!!!! REMEMBER THAT THE THE NAME OF THE CREATOR MUST BE IN UPPERCASE LETTERS
     *    OTHERWISE IT DOES NOT WORK ,THROWS AN ERROR
     */
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
    }

    /**
     * BE VARY CAREFUL BECAUSE THE ORDER BY WHICH THE
     * VALUES HAS BEEN WRITTEN TO THE PARCEL MATTERS
     * @param source
     */
    public HourlyWeather(Parcel source) {
        this.hour = source.readString();
        this.summary = source.readString();
        this.humidity = source.readDouble();
        this.temperature = source.readInt();
    }

    public String getHour() {
        return hour;
    }

    public String getSummary() {
        return summary;
    }

    public double getHumidity() {
        return humidity;
    }

    public int getTemperature() {
        return temperature;
    }



}

package com.example.andrei.duckweather.model;

/**
 * This class is a model for an hour in the forecast
 * It uses the Parcelable interface in order
 * to pass an Array of 48 hour data from
 * the MainActivity to the HourlyActivity
 */
public class HourlyWeather {
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

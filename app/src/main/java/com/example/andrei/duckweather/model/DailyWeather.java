package com.example.andrei.duckweather.model;

import android.os.Parcel;
import android.os.Parcelable;
public class DailyWeather implements Parcelable {
    private long time;
    private String summary;
    private long sunriseTime;
    private long sunsetTime;
    private double temperatureMin;
    private double temperatureMax;
    private String timezone;
    private String weekSummary;
    private double humidity;
    private long temperatureMaxTime;
    private long temperatureMinTime;
    private String icon;

    public DailyWeather() {

    }

    public DailyWeather(Parcel in) {

        time = in.readLong();
        summary = in.readString();
        sunriseTime = in.readLong();
        sunsetTime = in.readLong();
        temperatureMin = in.readDouble();
        temperatureMax = in.readDouble();
        timezone = in.readString();
        weekSummary = in.readString();
    }

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

     public void setTemperatureMaxTime(long temperatureMaxTime) {
        this.temperatureMaxTime = temperatureMaxTime;
    }
     public void setTemperatureMinTime(long temperatureMinTime) {
        this.temperatureMinTime = temperatureMinTime;
    }

    public void setIcon(String icon){
         this.icon = icon;
     }
     public void setHumidity(double humidity){
        this.humidity=humidity;
    }
     public void setWeekSummary(String weekSummary){
         this.weekSummary = weekSummary;
     }
     public void setTimezone(String timezone){
         this.timezone = timezone;
     }
     public void setTime(long time){
         this.time = time;
     }
     public void setSummary(String summary){
         this.summary = summary;
     }
     public void setSunriseTime(long sunriseTime){
         this.sunriseTime = sunriseTime;
     }
     public void setSunsetTime (long sunsetTime)
     {
         this.sunsetTime = sunsetTime;
     }
     public void setTemperatureMin(double temperatureMin){
         this.temperatureMin = Useful.convertToCelsius(temperatureMin);
     }
     public void setTemperatureMax(double temperatureMax){
         this.temperatureMax = Useful.convertToCelsius(temperatureMax);
     }

     public String getTemperatureMaxTimeAsString() {
        return Useful.formatTime(temperatureMaxTime,timezone,"H:mm");
    }
     public String getTemperatureMinTimeAsString() {
        return  Useful.formatTime(temperatureMinTime,timezone,"H:mm");
    }

    public String getIcon(){
         return icon;
     }
     public long getTime() {
         return time;
     }
     public String getWeekSummary(){
         return weekSummary;
     }
     public String getSummary() {
         return summary;
     }
     public String getTimezone(){
         return timezone;
     }
     public String getSunriseTimeAsString() {
         return Useful.formatTime(sunriseTime,timezone,"H:mm");
     }
     public double getHumidity(){
        return  humidity;
     }
     public String getSunsetTimeAsString(){
         return Useful.formatTime(sunsetTime,timezone,"H:mm");
     }

    public int getTemperatureMin() {
         return (int)temperatureMin;
     }
     public int getTemperatureMax() {
         return (int) temperatureMax;
     }
     public String getDayInWeek(){
         return Useful.formatTime(time,timezone,"EEEE");
     }



    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(time);
        dest.writeString(summary);
        dest.writeLong(sunriseTime);
        dest.writeLong(sunsetTime);
        dest.writeDouble(temperatureMin);
        dest.writeDouble(temperatureMax);
        dest.writeString(timezone);
        dest.writeString(weekSummary);
    }

}

package com.example.andrei.duckweather.model;

import com.example.andrei.duckweather.R;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class Useful {
    public static String formatTime(long time,String timezone,String pattern){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        simpleDateFormat.setTimeZone(TimeZone.getTimeZone(timezone));
        return simpleDateFormat.format(new Date(time*1000));
    }

    public static int convertToCelsius(double fahrenheit) {
        return (int) ((fahrenheit-32)/1.8);
    }


    public static int getImageId(String icon) {
        switch (icon){
            case "clear-day":
                return R.drawable.clear_day;

            case "clear-night":
               return R.drawable.clear_night;

            case "rain":
                return R.drawable.rain;

            case "snow":
                return R.drawable.snow;

            case "sleet":
                return R.drawable.sleet;

            case "wind":
                return R.drawable.wind;

            case "fog":
                return R.drawable.fog;
            case "cloudy":
                return R.drawable.cloudy;
            case "partly-cloudy-day":
                return R.drawable.partly_cloudy;
            case "partly-cloudy-night":
                return R.drawable.partly_cloudy;
        }
        return R.drawable.clear_day;
    }
}

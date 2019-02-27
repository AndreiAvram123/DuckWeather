package com.example.andrei.duckweather.model;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.example.andrei.duckweather.R;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class Utilities {

    public static String formatTime(long time,String timezone,String pattern){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        simpleDateFormat.setTimeZone(TimeZone.getTimeZone(timezone));
        return simpleDateFormat.format(new Date(time*1000));
    }

    public static int convertToCelsius(double fahrenheit) {
        return (int) ((fahrenheit-32)/1.8);
    }

    /**
     * This method uses a ConnectivityManager object along with
     * a NetworkInfo object to check if there is active internet
     * connection
     * @return- true if the NetworkInfo object is not null and there the
     * method isConnected() from the NetworkInfo class returns true
     */
    public static  boolean isNetworkAvailable(Context context){
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return  networkInfo !=null && networkInfo.isConnected();
    }

    /**
     * This method converts the time in UNIX format into
     * a String depending on the pattern
     * This method uses a SimpleDateFormat object
     * When we create a new Date object we have to multiply the number
     * of seconds by 1000 in order to convert them in milliseconds
     */
    public String getTimeAsString(long time ,String timezone,String pattern) {
        return Utilities.formatTime(time, timezone, pattern);
    }


    /**
     * According to the DarkSky api documentation there
     * is only a fixed number of possible icons
     * When we push our request for data, we get a String that
     * represents the icon for the given weather
     * In order to obtain an id to resource icons stored in the
     * drawable folder,all we need to do is to use a switch statement
     * @param icon
     * @return
     */
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

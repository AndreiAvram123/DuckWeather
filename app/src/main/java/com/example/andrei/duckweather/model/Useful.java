package com.example.andrei.duckweather.model;

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


}

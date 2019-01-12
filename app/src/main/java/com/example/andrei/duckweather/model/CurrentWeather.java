package com.example.andrei.duckweather.model;

/**
 * This class is a POJO
 * It is intended to store data about the current weather
 * There is no need to override any method
 */
public class CurrentWeather {
    private String summary;
    private long time;
    private int temperature;
    private String timezone;
    private double precipitationProbability;
    private  double humidity;

    public void setSummary(String summary){
        this.summary=summary;
    }
    public void setTime(long time){
        this.time=time;
    }
    public void setHumidity(double humidity){
        this.humidity=humidity;
    }
    public void setPrecipitationProbability(double precipitationProbability){
        this.precipitationProbability=precipitationProbability;
    }
    public void setTimezone(String timezone) {
        this.timezone = timezone;
    }

    /**
     *
     * @param temperature  the temperature in fahrenheit
     * We convert the temperature in celsius
     */
    public void setTemperature(int temperature){
        this.temperature= Useful.convertToCelsius(temperature);
    }
    public String getSummary(){
        return summary;
    }
    public double getPrecipitationProbability(){
        return precipitationProbability;
    }
    public double getHumidity(){
        return humidity;
    }

    /**
     * @return time as a String as follow  month : hour : minutes
     * This method uses a simpleDateFormat object
     * When we create a new Date object we have to multiply the number
     * of seconds by 1000 in order to convert them in milliseconds
     */
    public String getTimeAsString(){
       return Useful.formatTime(time,timezone,"EEEE h:mm");
    }

    public int getTemperature(){
        return temperature;
    }

}

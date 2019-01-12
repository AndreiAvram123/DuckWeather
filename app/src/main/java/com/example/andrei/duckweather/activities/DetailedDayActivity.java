package com.example.andrei.duckweather.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.andrei.duckweather.R;
import com.example.andrei.duckweather.model.DailyWeather;

public class DetailedDayActivity extends AppCompatActivity {
private TextView summaryValue,timeValue,sunriseTimeValue
        ,sunsetTimeValue,temperatureMinValue,temperatureMinTimeValue,
        temperatureMaxValue,temperatureMaxTimeValue,humidityValue;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_day_detailed);

        initializeViews();

        populateViews();

    }

    private void populateViews() {
        DailyWeather dailyWeather = getIntent().getParcelableExtra("daily_weather_object");
        summaryValue.setText(dailyWeather.getSummary());
        sunriseTimeValue.setText(dailyWeather.getSunriseTimeAsString());
        sunsetTimeValue.setText(dailyWeather.getSunsetTimeAsString());
        timeValue.setText(dailyWeather.getDayInWeek());
        temperatureMaxValue.setText(dailyWeather.getTemperatureMax()+"");
        temperatureMaxTimeValue.setText(dailyWeather.getTemperatureMaxTimeAsString());
        temperatureMinValue.setText(dailyWeather.getTemperatureMin()+"");
        temperatureMinTimeValue.setText(dailyWeather.getTemperatureMinTimeAsString());
        humidityValue.setText(dailyWeather.getHumidity()+"");
    }

    private void initializeViews() {
        ImageView backButtonImageView = findViewById(R.id.back_image_view_daily_detailed);
        backButtonImageView.setOnClickListener(view -> finish());

        summaryValue = findViewById(R.id.summaryValue);
        timeValue = findViewById(R.id.timeValue);
        sunriseTimeValue = findViewById(R.id.sunriseTimeValue);
        sunsetTimeValue = findViewById(R.id.sunsetTimeValue);
        temperatureMinValue = findViewById(R.id.temperatureMinValue);
        temperatureMinTimeValue = findViewById(R.id.temperatureMinTimeValue);
        temperatureMaxValue = findViewById(R.id.temperatureMaxValue);
        temperatureMaxTimeValue = findViewById(R.id.temperatureMaxTimeValue);
        humidityValue = findViewById(R.id.himidityValue);
    }
}

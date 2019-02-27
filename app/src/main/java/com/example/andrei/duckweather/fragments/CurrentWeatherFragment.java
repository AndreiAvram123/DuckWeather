package com.example.andrei.duckweather.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.andrei.duckweather.R;
import com.example.andrei.duckweather.model.CurrentWeather;
import com.example.andrei.duckweather.model.Utilities;

public class CurrentWeatherFragment extends Fragment {
    private static final String KEY_CURRENT_WEATHER = "KEY_CURRENT_WEATHER";
    private MainFragmentCallback mainFragmentCallback;
    private ImageView summaryIcon;
    private TextView temperature;
    private TextView time;
    private TextView summary;
    private TextView humidityTextView;
    private TextView precipitationTextView;
    private TextView locationNameTextView;
    private CurrentWeather currentWeather;

    public static CurrentWeatherFragment getInstance(CurrentWeather currentWeather) {
        Bundle bundle = new Bundle();
        bundle.putParcelable(KEY_CURRENT_WEATHER, currentWeather);
        CurrentWeatherFragment currentWeatherFragment = new CurrentWeatherFragment();
        currentWeatherFragment.setArguments(bundle);
        return currentWeatherFragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_current_weather, container, false);

        mainFragmentCallback = (MainFragmentCallback) getActivity();

        initializeViews(layout);

         currentWeather = getArguments().getParcelable(KEY_CURRENT_WEATHER);

        if(currentWeather!=null) {
            updateUI();
        }else{
            //TODO
            //display progress bar
        }
        return layout;
    }

    private void initializeViews(View layout) {
        SwipeRefreshLayout swipeRefreshLayout = layout.findViewById(R.id.swipe_refresh_main);
        locationNameTextView = layout.findViewById(R.id.location_text_view_main);
        temperature = layout.findViewById(R.id.temperatureTextView);
        time = layout.findViewById(R.id.timeTextView);
        summary = layout.findViewById(R.id.summaryTextView);
        summaryIcon = layout.findViewById(R.id.icon_current_weather);
        humidityTextView = layout.findViewById(R.id.humidityValueTextView);
        precipitationTextView = layout.findViewById(R.id.precipitationValueTextView);


        swipeRefreshLayout.setOnRefreshListener(() -> {
            mainFragmentCallback.refresh();
            swipeRefreshLayout.setRefreshing(false);
        });

    }

    /**
     * This method needs to run on the main UI thread
     * otherwise it will cause the app to crash
     */
    public void updateUI() {
            locationNameTextView.setText(currentWeather.getLocationName());
            summaryIcon.setImageResource(currentWeather.getIconID());
            //locationNameTextView.setText(locationName);
            time.setText(Utilities.formatTime(
                    currentWeather.getTime(),currentWeather.getTimezone(),"H:mm"));

            temperature.setText(currentWeather.getTemperature() + "");
            summary.setText(currentWeather.getSummary());
            humidityTextView.setText(currentWeather.getHumidity() + "");

            if (currentWeather.getPrecipitationProbability() == 0.0) {
                precipitationTextView.setText("NONE");
            } else {
                precipitationTextView.setText(currentWeather.getPrecipitationProbability() + "");
            }
        }



    public interface MainFragmentCallback {
        void refresh();
    }
}

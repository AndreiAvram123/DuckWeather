package com.example.andrei.duckweather.Adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.andrei.duckweather.R;
import com.example.andrei.duckweather.model.HourlyWeather;
import com.example.andrei.duckweather.model.Utilities;

public class HourlyAdapter extends RecyclerView.Adapter<HourlyAdapter.ViewHolder> {


    private HourlyWeather[] hourlyWeather;

    public HourlyAdapter(HourlyWeather[] hourlyWeather) {
        this.hourlyWeather = hourlyWeather;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View layout = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_hourly, viewGroup, false);
        return new ViewHolder(layout);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
        viewHolder.bindItem(position);

    }

    @Override
    public int getItemCount() {
        return hourlyWeather.length;
    }


    class ViewHolder extends RecyclerView.ViewHolder {
        private TextView currentTime;
        private TextView temperature;
        private ImageView imageIcon;

        ViewHolder(@NonNull View layout) {
            super(layout);
            currentTime = layout.findViewById(R.id.time_item_hourly);
            temperature = layout.findViewById(R.id.temperature_item_hourly);
            imageIcon = layout.findViewById(R.id.image_item_hourly);
        }

        void bindItem(int position) {
            currentTime.setText(hourlyWeather[position].getHour());
            temperature.setText(hourlyWeather[position].getTemperature() + "");
            imageIcon.setImageResource(Utilities.getImageId(hourlyWeather[position].getIcon()));
        }


    }
}

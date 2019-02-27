package com.example.andrei.duckweather.Adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.andrei.duckweather.R;
import com.example.andrei.duckweather.model.DailyWeather;
import com.example.andrei.duckweather.model.Utilities;

public class DailyAdapter extends RecyclerView.Adapter<DailyAdapter.ViewHolder> {
    private DailyWeather[] currentWeekWeather;

    public DailyAdapter(DailyWeather[] currentWeekWeather) {
        this.currentWeekWeather = currentWeekWeather;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_daily, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        DailyWeather currentDay = currentWeekWeather[i];
        if (i == 0)
            viewHolder.dayTextView.setText("TODAY");
        else {
            viewHolder.dayTextView.setText(currentDay.getDay());
        }
        viewHolder.sunsetTimeTextView.setText(currentDay.getSunsetTime());
        viewHolder.sunriseTimeTextView.setText(currentDay.getSunriseTime());
        viewHolder.temperatureMinTextView.setText(currentDay.getTemperatureMin() + "");
        viewHolder.temperatureMaxTextView.setText(currentDay.getTemperatureMax() + "");
        viewHolder.iconImageView.setImageResource(Utilities.getImageId(currentDay.getIcon()));
    }


    @Override
    public int getItemCount() {
        return currentWeekWeather.length;

    }


    class ViewHolder extends RecyclerView.ViewHolder {
        private TextView temperatureMinTextView;
        private TextView temperatureMaxTextView;
        private TextView sunriseTimeTextView;
        private TextView sunsetTimeTextView;
        private TextView dayTextView;
        private ImageView iconImageView;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            temperatureMinTextView = itemView.findViewById(R.id.temperatureMinTextView);
            temperatureMaxTextView = itemView.findViewById(R.id.temperatureMaxTextView);
            sunriseTimeTextView = itemView.findViewById(R.id.sunriseTimeTextView);
            sunsetTimeTextView = itemView.findViewById(R.id.sunsetTimeTextView);
            dayTextView = itemView.findViewById(R.id.dayTextView);
            iconImageView = itemView.findViewById(R.id.summary_icon_simple_list);


        }
    }

}

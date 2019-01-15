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
import com.example.andrei.duckweather.model.Useful;

public class HourlyWeatherAdapter extends RecyclerView.Adapter<HourlyWeatherAdapter.ViewHolder> {


    private  HourlyWeather[] twoDaysWeather;

    public HourlyWeatherAdapter(HourlyWeather[] twoDaysWeather){
        this.twoDaysWeather = twoDaysWeather;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View layout = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.simple_list_item_hourly_weather,viewGroup,false);
        return new ViewHolder(layout);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
         if(i==0){
             viewHolder.hour.setText("NOW");
         }else{
             viewHolder.hour.setText(twoDaysWeather[i].getHour());
         }
         viewHolder.summary.setText(twoDaysWeather[i].getSummary());
         viewHolder.temperature.setText(twoDaysWeather[i].getTemperature()+"");
         viewHolder.humidity.setText(twoDaysWeather[i].getHumidity()+"");
         viewHolder.icon.setImageResource(Useful.getImageId(twoDaysWeather[i].getIcon()));

    }

    @Override
    public int getItemCount() {
        return twoDaysWeather.length;
    }

    class ViewHolder extends RecyclerView.ViewHolder{
       private TextView humidity;
       private TextView temperature;
       private TextView summary;
       private TextView hour;
       private ImageView icon;
    public ViewHolder(@NonNull View layout) {
        super(layout);
        humidity = itemView.findViewById(R.id.humidity_value_hourly_list);
        temperature = itemView.findViewById(R.id.temperature_text_view_hourly_list);
        summary = itemView.findViewById(R.id.summary_text_view_hourly_list);
        hour = itemView.findViewById(R.id.hour_text_view_hourly_list);
        icon = itemView.findViewById(R.id.summary_icon_hourly_list);
    }
}
}

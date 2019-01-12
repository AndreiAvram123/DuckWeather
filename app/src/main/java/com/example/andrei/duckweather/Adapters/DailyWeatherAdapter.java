package com.example.andrei.duckweather.Adapters;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.andrei.duckweather.R;
import com.example.andrei.duckweather.activities.DetailedDayActivity;
import com.example.andrei.duckweather.model.DailyWeather;
import com.example.andrei.duckweather.model.Useful;

import java.util.ArrayList;
import java.util.Calendar;

public class  DailyWeatherAdapter extends RecyclerView.Adapter<DailyWeatherAdapter.ViewHolder>
{
   private ArrayList<DailyWeather>weekWeather;
   private Context context;
   public DailyWeatherAdapter(ArrayList<DailyWeather> weekWeather,Context context){
       this.weekWeather = weekWeather;
       this.context = context;
   }



    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.simple_list_item,viewGroup,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
       DailyWeather currentDay = weekWeather.get(i);
       if(i ==0)
             viewHolder.dayTextView.setText("TODAY");
        else{
           viewHolder.dayTextView.setText(currentDay.getDayInWeek());
        }
        viewHolder.sunsetTimeTextView.setText(currentDay.getSunsetTimeAsString());
        viewHolder.sunriseTimeTextView.setText(currentDay.getSunriseTimeAsString());
        viewHolder.temperatureMinTextView.setText(currentDay.getTemperatureMin() + "");
        viewHolder.temperatureMaxTextView.setText(currentDay.getTemperatureMax() + "");
        viewHolder.layout.setOnClickListener(listener -> startDetailedDayActivity(i));
    }

    @Override
    public int getItemCount() {
        return weekWeather.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
      private TextView temperatureMinTextView;
      private TextView temperatureMaxTextView;
      private TextView sunriseTimeTextView;
      private TextView sunsetTimeTextView;
      private TextView dayTextView;
      private ConstraintLayout layout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            layout = itemView.findViewById(R.id.constraintLayoutItemList);
            temperatureMinTextView = itemView.findViewById(R.id.temperatureMinTextView);
            temperatureMaxTextView = itemView.findViewById(R.id.temperatureMaxTextView);
            sunriseTimeTextView = itemView.findViewById(R.id.sunriseTimeTextView);
            sunsetTimeTextView = itemView.findViewById(R.id.sunsetTimeTextView);
            dayTextView = itemView.findViewById(R.id.dayTextView);


        }
    }

    private void startDetailedDayActivity(int day) {
        Intent intent  = new Intent(context,DetailedDayActivity.class);
        intent.putExtra("daily_weather_object",weekWeather.get(day));
        context.startActivity(intent);

   }
}

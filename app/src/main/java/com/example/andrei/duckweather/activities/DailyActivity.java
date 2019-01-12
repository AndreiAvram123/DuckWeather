package com.example.andrei.duckweather.activities;

import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.example.andrei.duckweather.Adapters.DailyWeatherAdapter;
import com.example.andrei.duckweather.R;
import com.example.andrei.duckweather.model.DailyWeather;

import java.util.ArrayList;

public class DailyActivity extends AppCompatActivity {
private ArrayList <DailyWeather> weekWeather;
    private RecyclerView recyclerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daily);

        TextView weekSummaryTextView = findViewById(R.id.weekSummaryTextView);
        recyclerView = findViewById(R.id.recylerViewDailyActivity);


        weekWeather=  getIntent().getParcelableArrayListExtra(MainActivity.KEY_PARCELABLE_DAILY_ARRAY);
        weekSummaryTextView.setText(weekWeather.get(0).getWeekSummary());

        initializeAdapter();
    }

    private void initializeAdapter() {
        DailyWeatherAdapter dailyWeatherAdapter = new DailyWeatherAdapter(weekWeather, this);
        recyclerView.setAdapter(dailyWeatherAdapter);
        recyclerView.addItemDecoration(new SpecialDividerItem(20));
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

    }


    //------------------Special nested class in order to add space to dividerItemDecoration------------------
   public  class SpecialDividerItem extends RecyclerView.ItemDecoration{
       private final int verticalHeight;
        public SpecialDividerItem(int verticalHeight){
            this.verticalHeight = verticalHeight;
        }

        @Override
        public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
            outRect.bottom = verticalHeight;
        }
    }
}

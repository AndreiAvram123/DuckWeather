package com.example.andrei.duckweather.activities;

import android.graphics.Rect;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.example.andrei.duckweather.Adapters.HourlyWeatherAdapter;
import com.example.andrei.duckweather.R;
import com.example.andrei.duckweather.model.HourlyWeather;

import java.util.Arrays;

public class HourlyActivity extends AppCompatActivity {
   private RecyclerView recyclerView;
   private HourlyWeather[] twoDaysWeather;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hourly);

        initialiseViews();

           Parcelable[] parcelableArray = getIntent().getParcelableArrayExtra(MainActivity.KEY_PARCELABLE_HOURLY_ARRAY);
          twoDaysWeather = Arrays.copyOf( parcelableArray,
                           parcelableArray.length,
                           HourlyWeather[].class

        );

        setUpRecyclerView();
    }

    private void setUpRecyclerView() {
        recyclerView.setAdapter(new HourlyWeatherAdapter(twoDaysWeather));
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addItemDecoration(new SpecialDividerItem(5));
    }

    private void initialiseViews() {
       recyclerView = findViewById(R.id.recycler_view_hourly_activity);
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

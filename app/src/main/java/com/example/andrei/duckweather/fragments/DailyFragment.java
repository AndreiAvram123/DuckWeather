package com.example.andrei.duckweather.fragments;

import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.example.andrei.duckweather.Adapters.DailyAdapter;
import com.example.andrei.duckweather.R;
import com.example.andrei.duckweather.model.DailyWeather;

import java.util.Arrays;

public class DailyFragment extends BaseFragment {

    private DailyAdapter dailyAdapter;
    private DailyWeather[] currentWeekWeather;

    public static DailyFragment getInstance(DailyWeather[] currentWeekWeather){
        Bundle bundle = new Bundle();
        bundle.putParcelableArray(KEY_FRAGMENT_DATA,currentWeekWeather);
        DailyFragment dailyFragment = new DailyFragment();
        dailyFragment.setArguments(bundle);
        return  dailyFragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_daily,container,false);

        Parcelable[] parcelables = getArguments().getParcelableArray(KEY_FRAGMENT_DATA);

        if(parcelables!=null) {
            currentWeekWeather = Arrays.copyOf(parcelables, parcelables.length, DailyWeather[].class);
            initializeAdapter();
            RecyclerView recyclerView = layout.findViewById(R.id.recycler_view_daily);
            setUpRecyclerView(dailyAdapter, recyclerView);
        }

        return layout;

    }


    @Override
    void initializeAdapter() {
        dailyAdapter = new DailyAdapter(currentWeekWeather);
    }
}

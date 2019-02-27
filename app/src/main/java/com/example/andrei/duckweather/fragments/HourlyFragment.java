package com.example.andrei.duckweather.fragments;

import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.andrei.duckweather.Adapters.HourlyAdapter;
import com.example.andrei.duckweather.R;
import com.example.andrei.duckweather.model.HourlyWeather;

import java.util.Arrays;

public class HourlyFragment extends BaseFragment {

    private HourlyWeather[] hourlyWeather;
    private HourlyAdapter hourlyAdapter;

    public static HourlyFragment getInstance(HourlyWeather[]twoDaysWeather){
        Bundle bundle  = new Bundle();
        bundle.putParcelableArray(KEY_FRAGMENT_DATA,twoDaysWeather);
        HourlyFragment hourlyFragment = new HourlyFragment();
        hourlyFragment.setArguments(bundle);
        return hourlyFragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View layout = inflater.inflate(R.layout.fragment_hourly,container,false);

        Parcelable [] parcelables = getArguments().getParcelableArray(KEY_FRAGMENT_DATA);

        if(parcelables!=null) {
            hourlyWeather = Arrays.copyOf(parcelables, parcelables.length, HourlyWeather[].class);
                initializeAdapter();
                RecyclerView recyclerView = layout.findViewById(R.id.recycler_view_hourly);
                setUpRecyclerView(hourlyAdapter,recyclerView);
            }



        return layout;
    }

    @Override
    void initializeAdapter() {
       hourlyAdapter = new HourlyAdapter(hourlyWeather);

    }


}

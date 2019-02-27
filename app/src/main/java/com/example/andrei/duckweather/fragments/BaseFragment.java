package com.example.andrei.duckweather.fragments;

import android.graphics.Rect;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.example.andrei.duckweather.model.Forecast;

public abstract class BaseFragment extends Fragment {

    protected static final String KEY_FRAGMENT_DATA ="KEY_FRAGMENT_DATA";

    abstract void initializeAdapter();


    protected void setUpRecyclerView(RecyclerView.Adapter adapter,RecyclerView recyclerView){
        recyclerView.setAdapter(adapter);
        recyclerView.addItemDecoration(new SpecialDividerItem(20));
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

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

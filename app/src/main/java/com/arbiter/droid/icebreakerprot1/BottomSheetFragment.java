package com.arbiter.droid.icebreakerprot1;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;


import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class BottomSheetFragment extends BottomSheetDialogFragment {

    private List<String> filtersList = new ArrayList<>();
    private Button mApplyFilterButton;

    private RecyclerView mRecyclerFiltersView;
    private BottomSheetFiltersViewAdapter mFiltersViewAdapter;
    private RecyclerView.LayoutManager mFiltersViewLayoutManager;

    public BottomSheetFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view;
        view = inflater.inflate(R.layout.bottom_sheet_filters, null);

        mApplyFilterButton = view.findViewById(R.id.button_apply);
        mApplyFilterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Map<String,Boolean> filterState = new HashMap<>();
                EventBus.getDefault().post(new FilterUpdateEvent(mFiltersViewAdapter.getCheckboxStates()));
                dismiss();
            }
        });

        mRecyclerFiltersView = view.findViewById(R.id.recycler_filters);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerFiltersView.setHasFixedSize(true);

        // use a linear layout manager
        mFiltersViewLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerFiltersView.setLayoutManager(mFiltersViewLayoutManager);

        // specify an adapter (see also next example)
        mFiltersViewAdapter = new BottomSheetFiltersViewAdapter(filtersList);
        mRecyclerFiltersView.setAdapter(mFiltersViewAdapter);

        prepareFilters();

        return view;
    }

    private void prepareFilters() {
        String filter = "Alcohol";
        filtersList.add(filter);

        filter = "Food";
        filtersList.add(filter);

        filter = "Veg Only";
        filtersList.add(filter);

        filter = "Drinks Only";
        filtersList.add(filter);

        filter = "Dance";
        filtersList.add(filter);

        filter = "Hookah";
        filtersList.add(filter);

        filter = "Night";
        filtersList.add(filter);
    }
}

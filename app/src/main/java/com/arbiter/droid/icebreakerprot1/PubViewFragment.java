package com.arbiter.droid.icebreakerprot1;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;


import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import static com.arbiter.droid.icebreakerprot1.Common.getDatabaseReference;

public class PubViewFragment extends Fragment {

    private ImageButton mFiltersButton;
    private List<Pub> pubList = new ArrayList<>();

    private RecyclerView mRecyclerPubView;
    private RecyclerView.Adapter mPubViewAdapter;
    private RecyclerView.LayoutManager mPubViewLayoutManager;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view;
        view = inflater.inflate(R.layout.fragment_pub_view, null);

        mFiltersButton = view.findViewById(R.id.button_filters);
        mFiltersButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showBottomSheetDialogFragment();
            }
        });

        mRecyclerPubView = view.findViewById(R.id.recycler_pub_view);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerPubView.setHasFixedSize(true);

        // use a linear layout manager
        mPubViewLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerPubView.setLayoutManager(mPubViewLayoutManager);

        // specify an adapter (see also next example)
        mPubViewAdapter = new PubViewAdapter(pubList, getContext());
        mRecyclerPubView.setAdapter(mPubViewAdapter);

        preparePubData();

        return view;
    }

    public void showBottomSheetDialogFragment() {
        BottomSheetFragment bottomSheetFragment = new BottomSheetFragment();
        bottomSheetFragment.show(getFragmentManager(), bottomSheetFragment.getTag());
    }

    private void preparePubData() {
        //Pub pub = new Pub("DJ the BJ", "DJ the BJ", Arrays.asList("Alcohol", "Food"), 4.5);
        getDatabaseReference().child("pubs").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot pubNode : dataSnapshot.getChildren()){
                    List<String> tags = new ArrayList<>();
                    for(DataSnapshot tag : pubNode.child("tags").getChildren()){
                        if(Boolean.parseBoolean(tag.getValue().toString()))
                            tags.add(tag.getKey());
                    }
                    pubList.add(new Pub(pubNode.child("name").getValue().toString(),pubNode.child("name").getValue().toString(),tags,4.5));
                }
                mPubViewAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        //pubList.add(pub);

    }
}

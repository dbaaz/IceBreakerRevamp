package com.arbiter.droid.icebreakerprot1;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;


import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnTextChanged;

import static com.arbiter.droid.icebreakerprot1.Common.getDatabaseReference;
import static com.arbiter.droid.icebreakerprot1.Common.removeValueEventListener;
import static com.arbiter.droid.icebreakerprot1.Common.setHashMap;

public class PubViewFragment extends Fragment {

    private ImageButton mFiltersButton;
    private List<Pub> pubList = new ArrayList<>();
    @BindView(R.id.text_search) EditText searchText;
    private RecyclerView mRecyclerPubView;
    Map<String,Boolean> filterList = new HashMap<>();
    private RecyclerView.Adapter mPubViewAdapter;
    private RecyclerView.LayoutManager mPubViewLayoutManager;
    @Subscribe
    public void onFilterChange(FilterUpdateEvent event){
        filterList=event.filter;
        setHashMap(filterList);
        String searchString = searchText.getText().toString();
        preparePubData(searchString);
    }
    @OnTextChanged(R.id.text_search)
    public void searchQuery(){
        String searchString = searchText.getText().toString();
        preparePubData(searchString);
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view;
        view = inflater.inflate(R.layout.fragment_pub_view, null);
        ButterKnife.bind(this,view);
        EventBus.getDefault().register(this);
        mFiltersButton = view.findViewById(R.id.button_filters);
        mFiltersButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showBottomSheetDialogFragment();
            }
        });
        mRecyclerPubView = view.findViewById(R.id.recycler_pub_view);
        filterList.put("Alcohol",false);
        filterList.put("Food",false);
        filterList.put("Night",false);
        filterList.put("Veg Only",false);
        filterList.put("Hookah",false);
        filterList.put("Drinks Only",false);
        filterList.put("Dance",false);
        setHashMap(filterList);
        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerPubView.setHasFixedSize(true);

        // use a linear layout manager
        mPubViewLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerPubView.setLayoutManager(mPubViewLayoutManager);

        // specify an adapter (see also next example)
        mPubViewAdapter = new PubViewAdapter(pubList, getContext());
        mRecyclerPubView.setAdapter(mPubViewAdapter);

        preparePubData("");

        return view;
    }

    public void showBottomSheetDialogFragment() {
        BottomSheetFragment bottomSheetFragment = new BottomSheetFragment();
        bottomSheetFragment.show(getFragmentManager(), bottomSheetFragment.getTag());
    }

    private void preparePubData(String search) {
        //Pub pub = new Pub("DJ the BJ", "DJ the BJ", Arrays.asList("Alcohol", "Food"), 4.5);
        getDatabaseReference().child("pubs").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                pubList.clear();
                for(DataSnapshot pubNode : dataSnapshot.getChildren()){
                        List<String> tags = new ArrayList<>();
                        for (DataSnapshot tag : pubNode.child("tags").getChildren()) {
                            if (Boolean.parseBoolean(tag.getValue().toString()))
                                tags.add(getTagText(tag.getKey()));
                        }
                        if(allKeysFalse()){
                            if(pubNode.child("name").getValue().toString().toLowerCase().contains(search.toLowerCase()))
                                pubList.add(new Pub(pubNode.child("name").getValue().toString(), pubNode.child("name").getValue().toString(), tags, 4.5));
                        }
                        if(pubNode.child("name").getValue().toString().toLowerCase().contains(search.toLowerCase())&&parseFilter(tags))
                            pubList.add(new Pub(pubNode.child("name").getValue().toString(), pubNode.child("name").getValue().toString(), tags, 4.5));
                }
                mPubViewAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        //pubList.add(pub);

    }
    String getTagText(String tag){
        switch (tag){
            case "alcohol":
                return "Alcohol";
            case "dance":
                return "Dance";
            case "drinksonly":
                return "Drinks only";
            case "food":
                return "Food";
            case "hookah":
                return "Hookah";
            case "night":
                return "Night";
            case "vegonly":
                return "Veg Only";
        }
        return "";
    }
    boolean parseFilter(List<String> tags){
        for(String tag: filterList.keySet())
            if(tags.contains(tag) && filterList.get(tag)) return true;
        return false;
    }
    boolean allKeysFalse()
    {
        for(String key:filterList.keySet())
            if(filterList.get(key)) return false;
        return true;
    }
}

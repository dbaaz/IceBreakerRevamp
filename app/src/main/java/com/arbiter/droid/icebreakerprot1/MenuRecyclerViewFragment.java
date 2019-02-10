package com.arbiter.droid.icebreakerprot1;


import android.os.Bundle;


import android.provider.ContactsContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import android.widget.TextView;
import android.widget.Toast;
import android.os.Handler;


import android.view.ViewGroup;
import android.view.MenuInflater;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.arbiter.droid.icebreakerprot1.Common.getCurrentUser;
import static com.arbiter.droid.icebreakerprot1.Common.getDatabaseReference;
import static com.arbiter.droid.icebreakerprot1.Common.getItemMap;
import static com.arbiter.droid.icebreakerprot1.Common.getPreference;
import static com.arbiter.droid.icebreakerprot1.Common.randomString;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MenuRecyclerViewFragment#newInstance} factory method to
 * create an instance of this fragment.
 */


public class MenuRecyclerViewFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    DatabaseReference pubNode;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    private RecyclerView recyclerView;

    // @BindView(R.id.recycler_view)
    // RecyclerView recyclerView;

    String tableNo;
    private RecyclerViewMenuAdapter mAdapter;

    private ArrayList<AbstractMenuModel> modelList = new ArrayList<>();


    public MenuRecyclerViewFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MenuRecyclerViewFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MenuRecyclerViewFragment newInstance(String param1, String param2) {
        MenuRecyclerViewFragment fragment = new MenuRecyclerViewFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public static MenuRecyclerViewFragment newInstance() {
        MenuRecyclerViewFragment fragment = new MenuRecyclerViewFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_menu_recycler_view, container, false);
        ((VenueMenuActivity)getActivity()).setOnFragmentInteract(bundle -> {
            String pubName = bundle.getString("pubName");
            tableNo = bundle.getString("tableno");
            getDatabaseReference().child("pubs").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for(DataSnapshot pub : dataSnapshot.getChildren()){
                        if(pub.child("name").getValue().toString().equals(pubName)){
                            setAdapter(pub.getRef());
                            break;
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        });
        EventBus.getDefault().register(this);
        //ButterKnife.bind(this,view);
        findViews(view);
        Log.v("myapp","Called from oncreateview");
        return view;

    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Log.v("myapp","Called from onviewcreated");
        //setAdapter();


    }


    private void findViews(View view) {

        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
    }
    @Subscribe
    public void updateItemOrder(OrderSubmitEvent event){
        Map<String,Integer> itemNameMap = getItemMap();
        List<String> itemNames = new ArrayList<>();
        List<Item> itemList = new ArrayList<>();
        for(String itemName : itemNameMap.keySet()){
            for(int i=0;i<itemNameMap.get(itemName);i++){
                itemNames.add(itemName);
                Log.v("myapp",itemNames+"");
            }
        }
        pubNode.child("category").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<Category> categoryList = new ArrayList<>();
                List<Item> tmpItemList = new ArrayList<>();
                for(DataSnapshot category : dataSnapshot.getChildren())
                    categoryList.add(category.getValue(Category.class));
                for(Category category : categoryList)
                    for(Item item : category.items)
                        tmpItemList.add(item);
                /*for(Item item : tmpItemList)
                    if(itemNames.contains(item.itemname))
                        itemList.add(item);*/
                for(String itemName : itemNames){
                    for(Item item : tmpItemList){
                        if(itemName.equals(item.itemname)){
                            itemList.add(item);
                            break;
                        }
                    }
                }
                Log.v("myapp",tableNo);
                pubNode.child("tables").child(tableNo).child("orderItems").setValue(itemList);
                if(itemList.size()!=0) {
                    pubNode.child("tables").child(tableNo).child("status").setValue("accepted");
                    pubNode.child("tables").child(tableNo).child("uniqueOrderNode").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if(!dataSnapshot.exists()){
                                String uniqueOrderNode = randomString(25);
                                pubNode.child("tables").child(tableNo).child("uniqueOrderNode").setValue(uniqueOrderNode);
                                List<String> orderNodeList = new ArrayList<>();
                                getDatabaseReference().child("users").child(getPreference("saved_uid")).child("orders").addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        Log.v("myapp","cunt");
                                        for(DataSnapshot order : dataSnapshot.getChildren())
                                            orderNodeList.add(order.getValue(String.class));
                                        if(!orderNodeList.contains(uniqueOrderNode)){
                                            Log.v("myapp",uniqueOrderNode);
                                            orderNodeList.add(uniqueOrderNode);
                                            getDatabaseReference().child("users").child(getPreference("saved_uid")).child("orders").setValue(orderNodeList);
                                            pubNode.child("orders").child(uniqueOrderNode).child("orderItems").setValue(itemList);
                                            pubNode.child("orders").child(uniqueOrderNode).child("status").setValue("accepted");
                                        }
                                    }
                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                    }
                                });
                            }
                            else{
                                String uniqueOrderNode = dataSnapshot.getValue().toString();
                                pubNode.child("orders").child(uniqueOrderNode).child("orderItems").setValue(itemList);
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    private void setAdapter(DatabaseReference pubNode) {
        this.pubNode=pubNode;
        List<Category> categoryList = new ArrayList<>();
        pubNode.child("category").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                categoryList.clear();
                modelList.clear();
                for(DataSnapshot category : dataSnapshot.getChildren())
                    categoryList.add(category.getValue(Category.class));
                for(Category category : categoryList){
                    ArrayList<AbstractMenuModel> itemList = new ArrayList<>();
                    for(Item item : category.items)
                        itemList.add(new AbstractMenuModel(item.itemname,"Rs."+item.price));
                    modelList.add(new AbstractMenuModel(category.categoryname,"",itemList));
                }

                mAdapter = new RecyclerViewMenuAdapter(getActivity(), modelList);

                recyclerView.setHasFixedSize(true);

                LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
                recyclerView.setLayoutManager(layoutManager);


                recyclerView.setAdapter(mAdapter);
                mAdapter.SetOnItemClickListener((view, position, model) -> {

                    //handle item click events here
                    Toast.makeText(getActivity(), "Hey " + model.getTitle(), Toast.LENGTH_SHORT).show();


                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

}

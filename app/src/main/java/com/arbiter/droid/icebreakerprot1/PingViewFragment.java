package com.arbiter.droid.icebreakerprot1;


import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import static com.arbiter.droid.icebreakerprot1.Common.getCurrentUser;
import static com.arbiter.droid.icebreakerprot1.Common.getDatabaseReference;
import static com.arbiter.droid.icebreakerprot1.Common.removeValueEventListener;
import static com.arbiter.droid.icebreakerprot1.Common.user_viewer_mode;

//import android.support.v7.widget.LinearLayoutManager;
//import android.support.v4.app.Fragment;
//import android.support.v4.content.ContextCompat;
//import android.support.v7.widget.DividerItemDecoration;
//import android.support.v7.widget.RecyclerView;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PingViewFragment#newInstance} factory method to
 * create an instance of this fragment.
 */


public class PingViewFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    int mode;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    int image_unload_count;
    private RecyclerView recyclerView;
    private HashMap<DatabaseReference, ValueEventListener> listenerHashMap = new HashMap<>();
    // @BindView(R.id.recycler_view)
    // RecyclerView recyclerView;


    private UsersViewAdapter mAdapter;

    private ArrayList<UserModel> modelList = new ArrayList<>();

    public PingViewFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment UsersViewFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static PingViewFragment newInstance(String param1, String param2) {
        PingViewFragment fragment = new PingViewFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }
    public Fragment newInstance() {
        Fragment fragment = new PingViewFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }
    @Override
    public void onPause() {
        removeValueEventListener(listenerHashMap);
        super.onPause();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_users_view, container, false);

        // ButterKnife.bind(this);
        findViews(view);
        mode=user_viewer_mode;
        setAdapter();
        return view;

    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //EventBus.getDefault().register(this);
    }


    private void findViews(View view) {

        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
    }

    @Subscribe
    void setMode(UserFragmentModesetEvent event){
        mode=event.mode;
    }
    public void setAdapter() {
            //setTitle("Your Pings");
            final DatabaseReference childr = getDatabaseReference().child("pings");
            List<String> pingnodes = new ArrayList<>();
            childr.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    modelList.clear();
                    Iterable<DataSnapshot> children = dataSnapshot.getChildren();
                    Iterator<DataSnapshot> iterator = children.iterator();
                    while(iterator.hasNext())
                    {
                        DataSnapshot next = iterator.next();
                        if (next.child("to").getValue().toString().equals(getCurrentUser()) && next.child("accepted").getValue().toString().equals("no")) {
                            pingnodes.add(next.getKey());
                            getDatabaseReference().child("users").child(next.child("from_uid").getValue().toString()).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    String prof_img_url = dataSnapshot.child("prof_img_url").getValue().toString();
                                    String uid = dataSnapshot.getKey();
                                    modelList.add(new UserModel(next.child("from").getValue().toString(), "", prof_img_url,uid));
                                    mAdapter.updateList(modelList);
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });
                        }
                    }

                }
                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
            mAdapter = new UsersViewAdapter(getActivity(), modelList);
            recyclerView.setHasFixedSize(true);
            LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
            recyclerView.setLayoutManager(layoutManager);
            recyclerView.setAdapter(mAdapter);
            mAdapter.SetOnItemClickListener(new UsersViewAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(View view, int position, UserModel model) {
                    Intent i = new Intent(getContext(),ViewProfileActivity.class);
                    i.putExtra("name",model.getTitle());
                    i.putExtra("uid",model.getUid());
                    i.putExtra("pingnode",pingnodes.get(position));
                    startActivity(i);
                }
            });
        }
}

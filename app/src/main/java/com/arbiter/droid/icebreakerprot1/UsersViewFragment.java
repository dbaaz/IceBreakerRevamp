package com.arbiter.droid.icebreakerprot1;


import androidx.fragment.app.Fragment;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

//import android.support.v7.widget.LinearLayoutManager;

//import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
//import android.support.v4.content.ContextCompat;
//import android.support.v7.widget.DividerItemDecoration;
//import android.support.v7.widget.RecyclerView;
import android.view.View;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.function.LongFunction;


import android.widget.TextView;
import android.widget.Toast;
import android.os.Handler;


import android.view.ViewGroup;
import android.view.MenuInflater;

import com.facebook.share.Share;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import static com.arbiter.droid.icebreakerprot1.Common.getCurrentUser;
import static com.arbiter.droid.icebreakerprot1.Common.getDatabaseReference;
import static com.arbiter.droid.icebreakerprot1.Common.removeValueEventListener;
import static com.arbiter.droid.icebreakerprot1.Common.user_viewer_mode;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link UsersViewFragment#newInstance} factory method to
 * create an instance of this fragment.
 */


public class UsersViewFragment extends Fragment {

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

    public UsersViewFragment() {
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
    public static UsersViewFragment newInstance(String param1, String param2) {
        UsersViewFragment fragment = new UsersViewFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }
    public Fragment newInstance() {
        Fragment fragment = new UsersViewFragment();
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
        Toast.makeText(getContext(), mode+"", Toast.LENGTH_SHORT).show();
        if (mode == 0) {
            ValueEventListener valueEventListener = getDatabaseReference().child("users").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    modelList.clear();
                    Iterable<DataSnapshot> children = dataSnapshot.getChildren();
                    Iterator<DataSnapshot> iterator = children.iterator();
                    while (iterator.hasNext()) {
                        DataSnapshot next = iterator.next();
                        String name_read = next.child("name").getValue().toString();
                        String prof_url = next.child("prof_img_url").getValue().toString();
                        if (!getCurrentUser().equals(name_read))
                            modelList.add(new UserModel(name_read, "", prof_url,next.getKey()));
                    }
                    mAdapter.updateList(modelList);
                    image_unload_count=modelList.size();
                    EventBus.getDefault().post(new ShimmerDisableEvent());
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                }
            });
            mAdapter = new UsersViewAdapter(getActivity(), modelList);
            recyclerView.setHasFixedSize(true);
            // use a linear layout manager
            LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
            recyclerView.setLayoutManager(layoutManager);
            recyclerView.setAdapter(mAdapter);
            mAdapter.SetOnItemClickListener(new UsersViewAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(View view, int position, UserModel model) {
                    Intent i = new Intent(view.getContext(), ViewProfileActivity.class);
                    i.putExtra("uid",model.getUid());
                    i.putExtra("name",model.getTitle());
                    startActivity(i);
                }
            });
            listenerHashMap.put(getDatabaseReference().child("users"),valueEventListener);
        }
        if(mode == 1)
        {
            final ArrayList<String> name = new ArrayList<>();
            getDatabaseReference().child("user_chats").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    modelList.clear();
                    Iterable<DataSnapshot> children = dataSnapshot.getChildren();
                    Iterator<DataSnapshot> iterator = children.iterator();
                    ParticipantWrite[] participantWrite = new ParticipantWrite[(int)dataSnapshot.getChildrenCount()];
                    /*while (iterator.hasNext()) {
                        DataSnapshot next = iterator.next();
                        if ((next.child("participants").child("1").getValue().toString()).equals(getCurrentUser()))
                            name.add(next.child("participants").child("2").getValue().toString());
                        else if ((next.child("participants").child("2").getValue().toString()).equals(getCurrentUser()))
                            name.add(next.child("participants").child("1").getValue().toString());
                        break;
                    }*/
                    int index=0;
                    while (iterator.hasNext()){
                        DataSnapshot next = iterator.next();
                        participantWrite[index++] = next.getValue(ParticipantWrite.class);
                    }
                    for(ParticipantWrite participant : participantWrite){
                        if(participant.participants.get(0).equals(getCurrentUser()))
                            name.add(participant.participants.get(1));
                        else if(participant.participants.get(1).equals(getCurrentUser()))
                            name.add(participant.participants.get(0));
                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
            getDatabaseReference().child("user_chats").addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                    modelList.clear();
                    Iterable<DataSnapshot> children = dataSnapshot.getChildren();
                    Iterator<DataSnapshot> iterator = children.iterator();
                    ParticipantWrite[] participantWrite = new ParticipantWrite[(int)dataSnapshot.getChildrenCount()];
                    int index=0;
                    while (iterator.hasNext()){
                        DataSnapshot next = iterator.next().child("participants");
                        participantWrite[index++] = next.getValue(ParticipantWrite.class);
                    }
                    for(ParticipantWrite participant : participantWrite){
                        try {
                            if (participant.participants.get(0).equals(getCurrentUser()))
                                name.add(participant.participants.get(1));
                            else if (participant.participants.get(1).equals(getCurrentUser()))
                                name.add(participant.participants.get(0));
                        }catch (NullPointerException e){}
                    }
                }

                @Override
                public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                }

                @Override
                public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

                }

                @Override
                public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
            getDatabaseReference().child("users").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    Iterable<DataSnapshot> children = dataSnapshot.getChildren();
                    Iterator<DataSnapshot> iterator = children.iterator();
                    while(iterator.hasNext())
                    {
                        DataSnapshot next = iterator.next();
                        if(name.contains(next.child("name").getValue().toString()))
                            modelList.add(new UserModel(next.child("name").getValue().toString(),"",next.child("prof_img_url").getValue().toString(),next.getKey()));
                    }
                    mAdapter.updateList(modelList);
                    EventBus.getDefault().post(new ShimmerDisableEvent());
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
            //listenerHashMap.put(getDatabaseReference().child("user_chats"),valueEventListener);
            mAdapter = new UsersViewAdapter(getActivity(), modelList);
            recyclerView.setHasFixedSize(true);
            LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
            recyclerView.setLayoutManager(layoutManager);
            recyclerView.setAdapter(mAdapter);
            mAdapter.SetOnItemClickListener(new UsersViewAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(View view, int position, UserModel model) {
                    Intent i = new Intent(view.getContext(), ChatActivity.class);
                    i.putExtra("sender", getCurrentUser());
                    i.putExtra("venname", model.getTitle());
                    i.putExtra("groupChat", "no");
                    startActivity(i);
                }
            });
        }
        if(mode==2){
            //setTitle("Your Pings");
            final DatabaseReference childr = getDatabaseReference().child("pings");
            childr.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    modelList.clear();
                    Iterable<DataSnapshot> children = dataSnapshot.getChildren();
                    Iterator<DataSnapshot> iterator = children.iterator();
                    while(iterator.hasNext())
                    {
                        DataSnapshot next = iterator.next();
                        if (next.child("to").getValue().toString().equals(getCurrentUser()) && next.child("accepted").getValue().toString().equals("no")) {
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
                    startActivity(i);
                }
            });
        }
    }
}

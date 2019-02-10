package com.arbiter.droid.icebreakerprot1;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.jakewharton.threetenabp.AndroidThreeTen;

import org.threeten.bp.LocalDate;
import org.threeten.bp.Period;

import java.util.Iterator;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.arbiter.droid.icebreakerprot1.Common.getCurrentUser;
import static com.arbiter.droid.icebreakerprot1.Common.getDatabaseReference;
import static com.arbiter.droid.icebreakerprot1.Common.getPreference;


public class ViewProfileActivity extends AppCompatActivity {
    FragmentInterface fragmentInterface;
    ImageButton mEditButton;
    ViewPager mProfileImagePager;
    ProfileImageAdapter mProfileImageAdapter;
    TextView mUserNameText;
    @BindView(R.id.button_facebook) ImageButton facebookBtn;
    @BindView(R.id.button_addimg) ImageButton addImg;
    @BindView(R.id.fabPingAccept) FloatingActionButton pingAcceptBtn;
    @BindView(R.id.fabPingReject) FloatingActionButton pingRejectBtn;
    @BindView(R.id.fab_ping) FloatingActionButton pingBtn;
    @BindView(R.id.fabChat) FloatingActionButton chatBtn;
    @OnClick(R.id.fabPingAccept)
    public void acceptPing(){
        String pingNode = getIntent().getStringExtra("pingnode");
        getDatabaseReference().child("pings").child(pingNode).child("accepted").setValue("yes");
        pingAcceptBtn.setVisibility(View.GONE);
        pingRejectBtn.setVisibility(View.GONE);
        chatBtn.setVisibility(View.VISIBLE);
    }
    @OnClick(R.id.fabPingReject)
    public void rejectPing(){
        String pingNode = getIntent().getStringExtra("pingnode");
        getDatabaseReference().child("pings").child(pingNode).removeValue();
        pingAcceptBtn.setVisibility(View.GONE);
        pingRejectBtn.setVisibility(View.GONE);
        pingBtn.setVisibility(View.VISIBLE);
    }
    @OnClick(R.id.fabChat)
    public void startChatActivity(){
        Intent i = new Intent(getApplicationContext(),ChatActivity.class);
        i.putExtra("venname",getIntent().getStringExtra("name"));
        i.putExtra("groupChat","no");
        i.putExtra("sender",getCurrentUser());
        startActivity(i);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_profile);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ButterKnife.bind(this);
        if(getIntent().hasExtra("friend")){
            chatBtn.setVisibility(View.VISIBLE);
            pingBtn.setVisibility(View.GONE);
        }
        if(getIntent().hasExtra("pingnode"))
        {
            pingAcceptBtn.setVisibility(View.VISIBLE);
            pingRejectBtn.setVisibility(View.VISIBLE);
            pingBtn.setVisibility(View.GONE);

        }
        facebookBtn.setVisibility(View.GONE);
        addImg.setVisibility(View.GONE);
        AndroidThreeTen.init(this);
        mEditButton = findViewById(R.id.button_edit);
        mEditButton.setVisibility(View.GONE);

        mUserNameText = findViewById(R.id.text_name);

        //mProfileImagePager = findViewById(R.id.pager_profile_images);
        mProfileImageAdapter = new ProfileImageAdapter(this);
        String name = getIntent().getStringExtra("name");
        String uid = getIntent().getStringExtra("uid");
        getDatabaseReference().child("users").child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                int img_size = (int)dataSnapshot.child("image_url").getChildrenCount();
                String[] dobstring = dataSnapshot.child("dob").getValue().toString().split("/");
                String bio = dataSnapshot.child("bio").getValue().toString();
                String[] url_array = new String[img_size+1];
                int index=1;
                url_array[0]=dataSnapshot.child("prof_img_url").getValue().toString();
                for(DataSnapshot user : dataSnapshot.child("image_url").getChildren()){
                    url_array[index++] = user.child("url").getValue().toString();
                }
                mProfileImagePager = findViewById(R.id.pager_profile_images);
                mProfileImageAdapter.setImageList(url_array);
                mProfileImageAdapter.setUid(uid);
                mProfileImagePager.setAdapter(mProfileImageAdapter);
                TextView age = findViewById(R.id.text_age);
                TextView bioTextView = findViewById(R.id.text_bio);
                int[] dob = new int[3];
                for(int i=0;i<3;i++)
                    dob[i]=Integer.parseInt(dobstring[i]);
                int age_val = Period.between(LocalDate.of(dob[2],dob[1],dob[0]),LocalDate.now()).getYears();
                age.setText(age_val+"");
                bioTextView.setText(bio);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if (getIntent().hasExtra("name")){
            getSupportActionBar().setTitle(name);
            mUserNameText.setText(name);
        }

        FloatingActionButton fab = findViewById(R.id.fab_ping);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final int[] result={0};
                getDatabaseReference().child("pings").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        Iterable<DataSnapshot> children = dataSnapshot.getChildren();
                        Iterator<DataSnapshot> iterator = children.iterator();
                        while (iterator.hasNext()) {
                            String name = getIntent().getStringExtra("name");
                            DataSnapshot next = iterator.next();
                            String from = next.child("from").getValue().toString();
                            String to = next.child("to").getValue().toString();
                            String accepted = next.child("accepted").getValue().toString();
                            if (from.equals(getCurrentUser()) && to.equals(name) && accepted.equals("no"))
                                result[0]=1;
                            else if(from.equals(getCurrentUser()) && to.equals(name) && accepted.equals("yes"))
                                result[0]=2;
                            else if(from.equals(name) && to.equals(getCurrentUser()) && accepted.equals("yes"))
                                result[0]=2;
                            else if(from.equals(name) && to.equals(getCurrentUser()) && accepted.equals("no"))
                                result[0]=3;
                        }
                        if(result[0]==0) {
                            DatabaseReference tmp = getDatabaseReference().child("pings").push();
                            tmp.child("from").setValue(getCurrentUser());
                            tmp.child("to").setValue(name);
                            tmp.child("accepted").setValue("no");
                            String from_uid,to_uid;
                            getDatabaseReference().child("users").addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    for(DataSnapshot user: dataSnapshot.getChildren()){
                                        if(user.child("name").getValue().toString().equals(getCurrentUser()))
                                            tmp.child("from_uid").setValue(user.getKey());
                                        else if(user.child("name").getValue().toString().equals(name))
                                            tmp.child("to_uid").setValue(user.getKey());
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });
                        }
                        else if(result[0]==1)
                            Toast.makeText(ViewProfileActivity.this, "You've already pinged this user", Toast.LENGTH_SHORT).show();
                        else if(result[0]==2)
                        {
                            Intent i = new Intent(getApplicationContext(),ChatActivity.class);
                            i.putExtra("venname",name);
                            i.putExtra("groupChat","no");
                            i.putExtra("sender",getCurrentUser());
                            startActivity(i);
                        }
                        else
                            Toast.makeText(ViewProfileActivity.this, "This user has already pinged you", Toast.LENGTH_SHORT).show();
                    }


                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
                //Snackbar.make(view, "Initiate 'Ping' from this.", Snackbar.LENGTH_LONG)
                 //       .setAction("Action", null).show();
            }
        });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            onBackPressed();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
    public interface FragmentInterface
    {
        void onFragmentInteract(Bundle bundle);
    }
    public void setOnFragmentInteract(ViewProfileActivity.FragmentInterface fragmentInterface)
    {
        this.fragmentInterface = fragmentInterface;
    }
}

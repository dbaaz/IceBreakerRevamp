package com.arbiter.droid.icebreakerprot1;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.jakewharton.threetenabp.AndroidThreeTen;

import org.threeten.bp.LocalDate;
import org.threeten.bp.Period;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.arbiter.droid.icebreakerprot1.Common.getCurrentUser;
import static com.arbiter.droid.icebreakerprot1.Common.getDatabaseReference;
import static com.arbiter.droid.icebreakerprot1.Common.getPreference;


public class ProfileFragment extends Fragment {

    ImageButton mEditButton;
    ViewPager mProfileImagePager;
    ProfileImageAdapter mProfileImageAdapter;
    @OnClick(R.id.button_addimg)
    public void startImageActivity(){
        startActivity(new Intent(getContext(),ImageListActivity.class));
    }
    @OnClick(R.id.button_facebook)
    public void startFacebookImageActivity(){
        try {
            AccessToken.getCurrentAccessToken().toString();
            startActivity(new Intent(getContext(), FacebookAlbumListActivity.class));
        }catch (Exception e){
            Snackbar.make(getView(),"You need to be logged into Facebook to access this feature",Snackbar.LENGTH_SHORT).show();
        }
    }
    @BindView(R.id.fabPingAccept) FloatingActionButton pingAcceptBtn;
    @BindView(R.id.fabPingReject) FloatingActionButton pingRejectBtn;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile_view, null);
        mEditButton = view.findViewById(R.id.button_edit);
        mEditButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO Start edit profile activity here
                Intent editProfile = new Intent(getContext(), CreateProfileActivity.class);
                editProfile.putExtra("editmode",1);
                startActivity(editProfile);
            }
        });

        mProfileImageAdapter = new ProfileImageAdapter(getContext());
        getDatabaseReference().child("users").child(getPreference("saved_uid")).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                int img_size = (int)dataSnapshot.child("image_url").getChildrenCount();
                String[] url_array = new String[img_size+1];
                int index=1;
                url_array[0]=dataSnapshot.child("prof_img_url").getValue().toString();
                for(DataSnapshot user : dataSnapshot.child("image_url").getChildren()){
                    url_array[index++] = user.child("url").getValue().toString();
                }
                mProfileImagePager = view.findViewById(R.id.pager_profile_images);
                mProfileImageAdapter.setImageList(url_array);
                mProfileImagePager.setAdapter(mProfileImageAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        TextView name = view.findViewById(R.id.text_name);
        TextView age = view.findViewById(R.id.text_age);
        TextView bio = view.findViewById(R.id.text_bio);
        name.setText(getCurrentUser());
        AndroidThreeTen.init(getActivity());
        getDatabaseReference().child("users").child(getPreference("saved_uid")).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String[] tmp = dataSnapshot.child("dob").getValue().toString().split("/");
                int[] dob = new int[3];
                for(int i=0;i<3;i++)
                    dob[i]=Integer.parseInt(tmp[i]);
                int age_val = Period.between(LocalDate.of(dob[2],dob[1],dob[0]),LocalDate.now()).getYears();
                age.setText(age_val+"");
                bio.setText(dataSnapshot.child("bio").getValue().toString());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        ButterKnife.bind(this,view);
        return view;
    }
}

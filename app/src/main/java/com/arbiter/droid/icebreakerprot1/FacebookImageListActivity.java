package com.arbiter.droid.icebreakerprot1;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import static com.arbiter.droid.icebreakerprot1.Common.getPreference;
import static com.arbiter.droid.icebreakerprot1.Common.setCurrentUser;

public class FacebookImageListActivity extends AppCompatActivity {
    FragmentInterface fragmentInterface;
    @Override
    protected void onStart(){
        super.onStart();
        setCurrentUser(getSharedPreferences("Icebreak",0).getString("saved_name",""));
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_facebook_image_list);
        Bundle b = new Bundle();
        b.putString("album_id",getIntent().getStringExtra("album_id"));
        fragmentInterface.onFragmentInteract(b);
    }
    public interface FragmentInterface
    {
        void onFragmentInteract(Bundle bundle);
    }
    public void setOnFragmentInteract(FacebookImageListActivity.FragmentInterface fragmentInterface)
    {
        this.fragmentInterface = fragmentInterface;
    }
}

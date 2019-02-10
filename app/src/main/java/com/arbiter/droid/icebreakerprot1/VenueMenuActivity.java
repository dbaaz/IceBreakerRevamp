package com.arbiter.droid.icebreakerprot1;

import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.util.Log;

import org.greenrobot.eventbus.EventBus;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.arbiter.droid.icebreakerprot1.Common.getItemMap;

public class VenueMenuActivity extends AppCompatActivity {
    FragmentInterface fragmentInterface;
    @OnClick(R.id.button9)
    public void submitOrder(){
        EventBus.getDefault().post(new OrderSubmitEvent());
        startActivity(new Intent(this,HomeActivity.class));
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_venue_menu);
        Toolbar toolbar = findViewById(R.id.toolbar6);
        toolbar.setTitleTextColor(getResources().getColor(R.color.white));
        ButterKnife.bind(this);
        Bundle b = new Bundle();
        b.putString("pubName",getIntent().getStringExtra("name"));
        if(getIntent().hasExtra("tableno"))
            b.putString("tableno",getIntent().getStringExtra("tableno"));
        fragmentInterface.onFragmentInteract(b);
    }
    public interface FragmentInterface
    {
        void onFragmentInteract(Bundle bundle);
    }
    public void setOnFragmentInteract(VenueMenuActivity.FragmentInterface fragmentInterface)
    {
        this.fragmentInterface = fragmentInterface;
    }
}

package com.arbiter.droid.icebreakerprot1;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.view.Window;

import com.facebook.shimmer.ShimmerFrameLayout;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import static com.arbiter.droid.icebreakerprot1.Common.getPreference;
import static com.arbiter.droid.icebreakerprot1.Common.setCurrentUser;

public class UsersViewRecyclerActivity extends AppCompatActivity {
    @Override
    protected void onStart(){
        super.onStart();
        setCurrentUser(getPreference("saved_name"));
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
        setContentView(R.layout.activity_users_view_recycler);
        int mode = getIntent().getIntExtra("mode",0);
        if(mode==0)
            setTitle("Users in Location");
        else if(mode==1)
            setTitle("Your Messages");
        EventBus.getDefault().post(new UserFragmentModesetEvent(mode));
    }
    @Subscribe
    public void onLoadComplete(ShimmerDisableEvent event)
    {
        //findViewById(R.id.fragment5).setVisibility(View.VISIBLE);
        findViewById(R.id.shimmer_view_container).setVisibility(View.GONE);
        ((ShimmerFrameLayout)findViewById(R.id.shimmer_view_container)).stopShimmer();
    }

}

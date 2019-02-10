package com.arbiter.droid.icebreakerprot1;
import android.app.Activity;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import org.greenrobot.eventbus.EventBus;

import java.util.concurrent.TimeUnit;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import static com.arbiter.droid.icebreakerprot1.Common.curr_user_lat;
import static com.arbiter.droid.icebreakerprot1.Common.curr_user_long;
import static com.arbiter.droid.icebreakerprot1.Common.getCurrentUser;
import static com.arbiter.droid.icebreakerprot1.Common.getDatabaseReference;
import static com.arbiter.droid.icebreakerprot1.Common.getPreference;
import static com.arbiter.droid.icebreakerprot1.Common.setCurrentUser;
import static com.arbiter.droid.icebreakerprot1.Common.user_viewer_mode;

public class HomeActivity extends AppCompatActivity {

    private FusedLocationProviderClient fusedLocationProviderClient;
    private Toolbar mToolbarMain;
    private BottomNavigationView mBottomNavigationView;
    boolean doubleBackToExitPressedOnce = false;
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.bottom_nav_home:
                    loadFragment(new PubViewFragment());
                    changeTitle(R.string.app_name);
                    return true;
                case R.id.bottom_nav_icebreaker:
                    user_viewer_mode=0;
                    loadFragment(new UsersViewFragment().newInstance());
                    changeTitle(R.string.view_users);
                    return true;
                case R.id.bottom_nav_profile:
                    loadFragment(new ProfileFragment());
                    changeTitle(R.string.profile);
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        //startActivity(new Intent(this,VenueMenuActivity.class));
        //finish();
        Activity currentActivity = this;
        fusedLocationProviderClient=LocationServices.getFusedLocationProviderClient(this);
        setCurrentUser(getPreference("saved_name"));
        mToolbarMain = findViewById(R.id.home_activity_toolbar);
        changeTitle(R.string.app_name);
        Handler locationHandler = new Handler();
        locationHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                try {
                    fusedLocationProviderClient.getLastLocation().addOnSuccessListener(currentActivity, new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            if(location==null)
                                Toast.makeText(currentActivity, "Please enable location and restart", Toast.LENGTH_SHORT).show();
                            else {
                                double latitude = location.getLatitude();
                                curr_user_lat = latitude;
                                double longitude = location.getLongitude();
                                curr_user_long = longitude;
                                long timestamp = TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis());
                                DatabaseReference locationNode = getDatabaseReference().child("users").child(getPreference("saved_uid")).child("location");
                                locationNode.child("latitude").setValue(latitude);
                                locationNode.child("longitude").setValue(longitude);
                                locationNode.child("timestamp").setValue(timestamp);
                            }
                        }
                    });
                }catch (SecurityException ex){
                    ex.printStackTrace();
                }
                locationHandler.postDelayed(this,60000);
            }
        },1000);
        mToolbarMain.inflateMenu(R.menu.menu_home_activity);

        mToolbarMain.setOnMenuItemClickListener(
                new Toolbar.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {

                        switch(item.getItemId()){
                            case R.id.home_menu_scan_qr:
                                startActivity(new Intent(getApplicationContext(),QRScanActivity.class));
                                return true;
                            case R.id.home_menu_social:
                                loadFragment(new SocialFragment());
                                changeTitle(R.string.social);
                                return true;
                            case R.id.order_menu_item:
                                loadFragment(new OrderRecyclerViewFragment());
                                changeTitle(R.string.orders);
                                return true;
                        }
                        return false;
                    }
                });

        loadFragment(new PubViewFragment());
        if (getSharedPreferences("Icebreak", 0).getString("firebaseinstanceid", "").equals("")) {
            FirebaseInstanceId.getInstance().getInstanceId().addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                @Override
                public void onComplete(@NonNull Task<InstanceIdResult> task) {
                    if (!task.isSuccessful())
                        return;
                    getDatabaseReference().child("users").child(getPreference("saved_uid")).child("firebaseinstanceid").setValue(task.getResult().getToken());
                }
            });
        }
        mBottomNavigationView = findViewById(R.id.navigation);
        mBottomNavigationView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        mBottomNavigationView.setSelectedItemId(R.id.bottom_nav_home);
    }

    private boolean loadFragment(Fragment fragment) {
        //switching fragment
        if (fragment != null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.home_activity_fragment, fragment)
                    .commit();

            return true;
        }
        return false;
    }
    @Override
    public void onBackPressed() {
        /*DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        }*/
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce=false;
            }
        }, 2000);
    }
    private void changeTitle(int stringResource) {
        mToolbarMain.setTitle(stringResource);
    }
}

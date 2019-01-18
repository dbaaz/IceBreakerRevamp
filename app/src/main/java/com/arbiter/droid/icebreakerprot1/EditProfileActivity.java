package com.arbiter.droid.icebreakerprot1;

import android.os.Bundle;
import android.view.Menu;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;


public class EditProfileActivity extends AppCompatActivity {

    Spinner mSpinnerGender;
    Spinner mSpinnerInterest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mSpinnerGender = findViewById(R.id.spinner_gender);
        mSpinnerInterest = findViewById(R.id.spinner_interest);

//        ArrayAdapter<CharSequence> spinnerGenderAdapter = ArrayAdapter
//                .createFromResource(this, R.array.genders,
//                        android.R.layout.simple_spinner_item);
//
//        mSpinnerGender.setAdapter(spinnerGenderAdapter);
//
//        ArrayAdapter<CharSequence> spinnerInterestAdapter = ArrayAdapter
//                .createFromResource(this, R.array.genders,
//                        android.R.layout.simple_spinner_item);
//
//        mSpinnerInterest.setAdapter(spinnerInterestAdapter);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_edit_profile_activity, menu);
        return true;
    }
}

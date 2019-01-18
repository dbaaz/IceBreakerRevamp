package com.arbiter.droid.icebreakerprot1;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.MultiAutoCompleteTextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import androidx.appcompat.app.AppCompatActivity;

import static com.arbiter.droid.icebreakerprot1.Common.getPreference;

public class ExtendedCreateProfileActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_extended_create_profile);
        Button btn = findViewById(R.id.button5);
        final SharedPreferences sharedPref;
        sharedPref = this.getSharedPreferences("Icebreak", 0);
        final DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
        final String[] biotext=new String[1];
        final MultiAutoCompleteTextView mactv = findViewById(R.id.multiAutoCompleteTextView);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                biotext[0] = mactv.getText().toString();
                if(!biotext[0].trim().equals("")) {
                    mDatabase.child("users").child(getPreference("saved_uid")).child("bio").setValue(biotext[0]);
                    startActivity(new Intent(v.getContext(), HomeActivity.class));
                    finish();
                }
                else
                    Toast.makeText(ExtendedCreateProfileActivity.this, "Bio is mandatory", Toast.LENGTH_SHORT).show();
            }
        });
    }
}

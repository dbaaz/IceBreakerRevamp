package com.arbiter.droid.icebreakerprot1;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;


public class PubActivity extends AppCompatActivity {

    TextView mTagsText;
    TextView mRatingText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pub);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mTagsText = findViewById(R.id.text_tags);
        mRatingText = findViewById(R.id.text_rating);
        String title="";
        if (getIntent().hasExtra("name")){
            title = getIntent().getStringExtra("name");
            getSupportActionBar().setTitle(title);
        }

        if (getIntent().hasExtra("tags")) {
            mTagsText.setText(getIntent().getStringExtra("tags"));
        }

        if (getIntent().hasExtra("rating")) {
            mRatingText.setText(getIntent().getStringExtra("rating"));
        }

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab_chat_room);
        String finalTitle = title;
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(view.getContext(),ChatActivity.class);
                i.putExtra("venname", finalTitle);
                i.putExtra("groupChat","yes");
                i.putExtra("sender",finalTitle);
                startActivity(i);
            }
        });
    }
}

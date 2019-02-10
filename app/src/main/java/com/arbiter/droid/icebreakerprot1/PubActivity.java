package com.arbiter.droid.icebreakerprot1;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import org.greenrobot.eventbus.EventBus;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnItemClick;

import static com.arbiter.droid.icebreakerprot1.Common.getDatabaseReference;


public class PubActivity extends AppCompatActivity {
    TextView mTagsText;
    TextView mRatingText;
    @OnClick(R.id.button12)
    public void startMenu(){
        Intent i = new Intent(this, VenueMenuActivity.class);
        i.putExtra("name",getIntent().getStringExtra("name"));
        startActivity(i);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pub);
        ButterKnife.bind(this);
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

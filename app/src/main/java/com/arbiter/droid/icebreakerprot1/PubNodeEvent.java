package com.arbiter.droid.icebreakerprot1;

import com.google.firebase.database.DatabaseReference;

public class PubNodeEvent {
    public DatabaseReference pubNode;
    public PubNodeEvent(DatabaseReference pubNode){
        this.pubNode = pubNode;
    }
}

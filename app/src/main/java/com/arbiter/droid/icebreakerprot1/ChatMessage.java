package com.arbiter.droid.icebreakerprot1;

import java.util.List;

public class ChatMessage {
    String challenge;
    String challengenode;
    String challengetype;
    String sender;
    String text;
    long timestamp;
    ChatMessage(){}
    ChatMessage(String challenge,String challengenode,String challengetype,String sender,String text,long timestamp){
        this.challenge=challenge;
        this.challengenode=challengenode;
        this.challengetype=challengetype;
        this.sender=sender;
        this.text=text;
        this.timestamp=timestamp;
    }
}

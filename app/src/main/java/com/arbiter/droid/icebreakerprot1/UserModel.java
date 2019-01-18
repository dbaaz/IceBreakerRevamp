package com.arbiter.droid.icebreakerprot1;

import java.util.ArrayList;

public class UserModel {

    private String title;
    private String message;
    private String imageUrl;
    private String uid;
    public UserModel(String title, String message, String url,String uid) {
        this.title = title;
        this.message = message;
        this.imageUrl = url;
        this.uid=uid;
    }

    public UserModel() {

    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getImageUrl() {
        return imageUrl;
    }
    public void setUid(String uid){this.uid=uid;}
    public String getUid(){return uid;}
    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}

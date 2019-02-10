package com.arbiter.droid.icebreakerprot1;

import java.util.ArrayList;

public class OrderAbstractModel {

    private String title;

    private String message;


    public OrderAbstractModel(String title, String message) {
        this.title = title;
        this.message = message;
    }

    public OrderAbstractModel() {

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
}

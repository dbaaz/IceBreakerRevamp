package com.arbiter.droid.icebreakerprot1;

import java.util.ArrayList;

public class AbstractMenuModel {

    private String title;

    private String message;

    private ArrayList<AbstractMenuModel> singleItemModelArrayList;

    public AbstractMenuModel(String title, String message) {
        this.title = title;
        this.message = message;
    }

    public AbstractMenuModel(String title, String message, ArrayList<AbstractMenuModel> singleItemModelArrayList) {
        this.title = title;
        this.message = message;
        this.singleItemModelArrayList = singleItemModelArrayList;
    }


    public AbstractMenuModel() {

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

    public ArrayList<AbstractMenuModel> getSingleItemArrayList() {
        return singleItemModelArrayList;
    }

    public void setSingleItemArrayList(ArrayList<AbstractMenuModel> singleItemModelArrayList) {
        this.singleItemModelArrayList = singleItemModelArrayList;
    }
}

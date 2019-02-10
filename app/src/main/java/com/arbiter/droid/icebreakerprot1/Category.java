package com.arbiter.droid.icebreakerprot1;

import java.util.List;

public class Category {
    public String categoryname;
    public List<Item> items;
    public Category(String categoryname, List<Item> items){
        this.categoryname=categoryname;
        this.items=items;
    }
    public Category(){}
}

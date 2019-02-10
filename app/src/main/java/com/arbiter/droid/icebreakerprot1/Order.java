package com.arbiter.droid.icebreakerprot1;

import java.util.List;

public class Order {
    String orderStatus;
    List<Item> orderItems;
    public Order(String orderStatus, List<Item> orderItems){
        this.orderStatus=orderStatus;
        this.orderItems=orderItems;
    }
}

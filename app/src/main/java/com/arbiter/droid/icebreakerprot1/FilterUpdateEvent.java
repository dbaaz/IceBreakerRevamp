package com.arbiter.droid.icebreakerprot1;

import java.util.List;
import java.util.Map;

public class FilterUpdateEvent {
    Map<String,Boolean> filter;
    public FilterUpdateEvent(Map<String,Boolean> filter){
        this.filter=filter;
    }
}

package com.xl.example;

import java.sql.Timestamp;

public class TimeID implements Comparable<TimeID>{
    private Long id ;
    private Timestamp time;

    private long gap = -1;

    private int index = 0;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Timestamp getTime() {
        return time;
    }

    public void setTime(Timestamp time) {
        this.time = time;
    }

    public int compareTo(TimeID o) {
        return o.getTime().compareTo(this.getTime());
    }

    public long getGap() {
        return gap;
    }

    public void setGap(long gap) {
        this.gap = gap;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }
}

package com.xl.example;

import java.util.Comparator;

public class TimeIDGapComparator implements Comparator<TimeID> {

    @Override
    public int compare(TimeID o1, TimeID o2) {
        return (int) (o2.getGap()-o1.getGap());
    }
}

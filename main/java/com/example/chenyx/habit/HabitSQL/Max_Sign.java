package com.example.chenyx.habit.HabitSQL;

import org.parceler.Parcel;

import java.io.Serializable;

/**
 * Created by chenyx on 18-12-23.
 */

public class Max_Sign  {
    SignData start;
    SignData end;

    public Max_Sign(SignData start,SignData end)
    {
        this.start=start;
        this.end=end;
    }

    public SignData getStart() {
        return start;
    }

    public SignData getEnd() {
        return end;
    }

    public void setEnd(SignData end) {
        this.end = end;
    }
}
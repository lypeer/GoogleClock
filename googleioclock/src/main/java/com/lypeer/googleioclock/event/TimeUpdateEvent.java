package com.lypeer.googleioclock.event;

/**
 * To notify time's updating .
 * Created by lypeer on 16/9/13.
 */
public class TimeUpdateEvent extends EmptyEvent {

    //Ten and single digits of hour
    private final int mHourTenDigits;
    private final int mHourSingleDigits;

    //Ten and single digits of minute
    private final int mMinTenDigits;
    private final int mMinSingleDigits;

    //Ten and single digits of second
    private final int mSecSingleDigits;
    private final int mSecTenDigits;

    public TimeUpdateEvent(int hourTenDigits, int hourSingleDigits, int minTenDigits, int minSingleDigits, int secTenDigits, int secSingleDigits) {
        mHourTenDigits = hourTenDigits;
        mHourSingleDigits = hourSingleDigits;
        mMinTenDigits = minTenDigits;
        mMinSingleDigits = minSingleDigits;
        mSecTenDigits = secTenDigits;
        mSecSingleDigits = secSingleDigits;
    }

    public int getHourTenDigits() {
        return mHourTenDigits;
    }

    public int getHourSingleDigits() {
        return mHourSingleDigits;
    }

    public int getMinTenDigits() {
        return mMinTenDigits;
    }

    public int getMinSingleDigits() {
        return mMinSingleDigits;
    }

    public int getSecSingleDigits() {
        return mSecSingleDigits;
    }

    public int getSecTenDigits() {
        return mSecTenDigits;
    }
}

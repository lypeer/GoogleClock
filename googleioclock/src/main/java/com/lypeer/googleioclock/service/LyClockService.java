package com.lypeer.googleioclock.service;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.lypeer.googleioclock.event.TimeUpdateEvent;

import org.greenrobot.eventbus.EventBus;

import java.sql.Time;
import java.util.Timer;
import java.util.TimerTask;

/**
 * A Service to notify the clock update time .
 * <p/>
 * I am trying to reduce the time to visit system's time ,
 * because it needs so much time . So I choose the way in this class
 * to make a clock .
 * <p/>
 * Created by lypeer on 16/9/14.
 */
public class LyClockService extends Service {

    private int mHourTenDigits;
    private int mHourSingleDigits;

    private int mMinTenDigits;
    private int mMinSingleDigits;

    private int mSecSingleDigits;
    private int mSecTenDigits;

    private Timer mTimer;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        IntentFilter filter = new IntentFilter();
        filter.addAction(Intent.ACTION_SCREEN_ON);
        registerReceiver(mReceiver , filter);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        beginClock();
        return super.onStartCommand(intent, flags, startId);
    }

    private void beginClock() {
        initTime();

        mTimer = new Timer();
        mTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                updateTime();
            }
        }, 0, 1000);
    }

    private void updateTime() {
        //get current hour , minute and second through the values
        int second = mSecTenDigits * 10 + mSecSingleDigits;
        int minute = mMinTenDigits * 10 + mMinSingleDigits;
        int hour = mHourTenDigits * 10 + mHourSingleDigits;

        if (second != 59) {
            if (mSecSingleDigits != 9) {
                mSecSingleDigits += 1;
            } else {
                mSecSingleDigits = 0;
                mSecTenDigits += 1;
            }
            sendEvent();
            return;
        }
        mSecTenDigits = mSecSingleDigits = 0;
        if (minute != 59) {
            if (mMinSingleDigits != 9) {
                mMinSingleDigits += 1;
            } else {
                mMinSingleDigits = 0;
                mMinTenDigits += 1;
            }
            sendEvent();
            return;
        }
        mMinTenDigits = mMinSingleDigits = 0;
        if (hour != 23) {
            if (mHourSingleDigits != 9) {
                mHourSingleDigits += 1;
            } else {
                mHourSingleDigits = 0;
                mHourTenDigits += 1;
            }
            sendEvent();
            return;
        }
        mHourTenDigits = mHourSingleDigits = 0;
        sendEvent();

        //revise time
        initTime();
    }

    private void sendEvent() {
        EventBus.getDefault().post(new TimeUpdateEvent(
                mHourTenDigits,
                mHourSingleDigits,
                mMinTenDigits,
                mMinSingleDigits,
                mSecTenDigits,
                mSecSingleDigits)
        );
    }

    /**
     * To get the exact time now and to assign the values .
     */
    @SuppressWarnings("deprecation")
    private void initTime() {
        Time time = new Time(System.currentTimeMillis());

        int hours = time.getHours();
        mHourSingleDigits = hours % 10;
        mHourTenDigits = hours / 10;

        int minutes = time.getMinutes();
        mMinSingleDigits = minutes % 10;
        mMinTenDigits = minutes / 10;

        int seconds = time.getSeconds();
        mSecSingleDigits = seconds % 10;
        mSecTenDigits = seconds / 10;
    }

    // Monitoring the screen on to reset time
    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            initTime();
        }
    };

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mTimer != null) {
            mTimer.cancel();
        }
        unregisterReceiver(mReceiver);
    }
}

package com.lypeer.googleioclock;

import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.drawable.AnimatedVectorDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.DimenRes;
import android.support.annotation.StyleRes;
import android.util.AttributeSet;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.lypeer.googleioclock.event.EmptyEvent;
import com.lypeer.googleioclock.event.TimeUpdateEvent;
import com.lypeer.googleioclock.service.LyClockService;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

/**
 * Main class for the clock .
 * <p/>
 * Created by lypeer on 16/9/14.
 */
public class GoogleClock extends LinearLayout {

    private ImageView mIvHourTenDigits;
    private ImageView mIvHourSingleDigits;
    private ImageView mIvMinTenDigits;
    private ImageView mIvMinSingleDigits;
    private ImageView mIvSecTenDigits;
    private ImageView mIvSecSingleDigits;
    private ImageView mIvColon;

    //it contains the drawable res id of all the numbers
    private List<Integer> mResList;
    private Context mContext;

    @DimenRes
    private float mClockWidth = 0.0f;
    @DimenRes
    private float mHourWidth = 0.0f;
    @DimenRes
    private float mMinWidth = 0.0f;
    @DimenRes
    private float mSecWidth = 0.0f;
    @DimenRes
    private float mDividerHorizontal = 0.0f;
    @DimenRes
    private float mDividerVertical = 0.0f;
    @StyleRes
    private int mTheme = -1;

    private int mHourTenDigits = -1;
    private int mHourSingleDigits = -1;

    private int mMinTenDigits = -1;
    private int mMinSingleDigits = -1;

    private int mSecSingleDigits = -1;
    private int mSecTenDigits = -1;

    public GoogleClock(Context context) {
        super(context, null);
        init(context, null, 0);
    }

    public GoogleClock(Context context, AttributeSet attrs) {
        super(context, attrs, 0);
        init(context, attrs, 0);
    }

    public GoogleClock(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr);
    }

    private void init(Context context, AttributeSet attrs, int defStyleAttr) {
        mContext = context;
        EventBus.getDefault().register(this);
        View.inflate(context, R.layout.view_google_clock, this);
        initList();

        if (attrs != null) {
            TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.LyGoogleClock, defStyleAttr, 0);
            try {
                mClockWidth = typedArray.getDimension(R.styleable.LyGoogleClock_lyClockWidth, 0.0f);
                mHourWidth = typedArray.getDimension(R.styleable.LyGoogleClock_lyHourWidth, 0.0f);
                mMinWidth = typedArray.getDimension(R.styleable.LyGoogleClock_lyMinWidth, 0.0f);
                mSecWidth = typedArray.getDimension(R.styleable.LyGoogleClock_lySecWidth, 0.0f);
                mDividerHorizontal = typedArray.getDimension(R.styleable.LyGoogleClock_lyDividerHorizontal, 0.0f);
                mDividerVertical = typedArray.getDimension(R.styleable.LyGoogleClock_lyDividerVertical, 0.0f);
                mTheme = typedArray.getResourceId(R.styleable.LyGoogleClock_lyTheme, -1);
            } finally {
                typedArray.recycle();
            }
        }
        initView();
    }

    private void initView() {
        mIvHourTenDigits = (ImageView) findViewById(R.id.iv_hour_ten_digits);
        mIvHourSingleDigits = (ImageView) findViewById(R.id.iv_hour_single_digits);
        mIvMinTenDigits = (ImageView) findViewById(R.id.iv_min_ten_digits);
        mIvMinSingleDigits = (ImageView) findViewById(R.id.iv_min_single_digits);
        mIvSecTenDigits = (ImageView) findViewById(R.id.iv_sec_ten_digits);
        mIvSecSingleDigits = (ImageView) findViewById(R.id.iv_sec_single_digits);
        mIvColon = (ImageView) findViewById(R.id.iv_colon);

        changeSize(mHourWidth, mClockWidth, mIvHourTenDigits);
        changeSize(mHourWidth, mClockWidth, mIvHourSingleDigits);
        changeSize(mMinWidth, mClockWidth, mIvMinTenDigits);
        changeSize(mMinWidth, mClockWidth, mIvMinSingleDigits);
        changeSize(mSecWidth, mClockWidth, mIvSecTenDigits);
        changeSize(mSecWidth, mClockWidth, mIvSecSingleDigits);

        View mViewDividerHorizontal0 = findViewById(R.id.view_divider_horizontal_0);
        View mViewDividerHorizontal1 = findViewById(R.id.view_divider_horizontal_1);
        View mViewDividerHorizontal2 = findViewById(R.id.view_divider_horizontal_2);
        View mViewDividerVertical = findViewById(R.id.view_divider_vertical);

        if (mDividerHorizontal != 0.0f) {
            ViewGroup.LayoutParams params = mViewDividerHorizontal0.getLayoutParams();
            params.width = (int) mDividerHorizontal;
            mViewDividerHorizontal0.setLayoutParams(params);
            mViewDividerHorizontal1.setLayoutParams(params);
            mViewDividerHorizontal2.setLayoutParams(params);
        }

        if (mDividerVertical != 0.0f) {
            ViewGroup.LayoutParams params = mViewDividerVertical.getLayoutParams();
            params.height = (int) mDividerVertical;
            mViewDividerVertical.setLayoutParams(params);
        }
    }

    /**
     * ChangeSize of target view
     */
    private void changeSize(float targetSize, float parentSize, View targetView) {
        ViewGroup.LayoutParams params = targetView.getLayoutParams();

        if (targetSize != 0.0f) {
            params.width = (int) targetSize;
            params.height = (int) targetSize;
        } else {
            if (parentSize != 0.0f) {
                params.width = (int) parentSize;
                params.height = (int) parentSize;
            }
        }
        targetView.setLayoutParams(params);
    }

    private void initList() {
        mResList = new ArrayList<>();
        mResList.add(R.drawable.animated_9);
        mResList.add(R.drawable.animated_0);
        mResList.add(R.drawable.animated_1);
        mResList.add(R.drawable.animated_2);
        mResList.add(R.drawable.animated_3);
        mResList.add(R.drawable.animated_4);
        mResList.add(R.drawable.animated_5);
        mResList.add(R.drawable.animated_6);
        mResList.add(R.drawable.animated_7);
        mResList.add(R.drawable.animated_8);
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(EmptyEvent event) {
        if (event == null) {
            return;
        }
        if (event instanceof TimeUpdateEvent) {
            updateClock((TimeUpdateEvent) event);
        }
    }

    private void updateClock(TimeUpdateEvent event) {
        //if new value is equal to the old value , it don't need to change .
        if (mHourTenDigits != event.getHourTenDigits()) {
            updateImageView(mIvHourTenDigits, mResList.get(event.getHourTenDigits()));
            mHourTenDigits = event.getHourTenDigits();
        }
        if (mHourSingleDigits != event.getHourSingleDigits()) {
            updateImageView(mIvHourSingleDigits, mResList.get(event.getHourSingleDigits()));
            mHourSingleDigits = event.getHourSingleDigits();
        }
        if (mMinTenDigits != event.getMinTenDigits()) {
            updateImageView(mIvMinTenDigits, mResList.get(event.getMinTenDigits()));
            mMinTenDigits = event.getMinTenDigits();
        }
        if (mMinSingleDigits != event.getMinSingleDigits()) {
            updateImageView(mIvMinSingleDigits, mResList.get(event.getMinSingleDigits()));
            mMinSingleDigits = event.getMinSingleDigits();
        }
        if (mSecTenDigits != event.getSecTenDigits()) {
            updateImageView(mIvSecTenDigits, mResList.get(event.getSecTenDigits()));
            mSecTenDigits = event.getSecTenDigits();
        }
        if (mSecSingleDigits != event.getSecSingleDigits()) {
            updateImageView(mIvSecSingleDigits, mResList.get(event.getSecSingleDigits()));
            mSecSingleDigits = event.getSecSingleDigits();
        }
        updateImageView(mIvColon, R.drawable.animated_colon);
    }

    /**
     * The core method of animation
     *
     * @param target which image view to anim
     * @param animTo anim to which vector
     */
    private void updateImageView(ImageView target, int animTo) {
        final ContextThemeWrapper wrapper;
        if (mTheme != -1) {
            wrapper = new ContextThemeWrapper(mContext, mTheme);
        } else {
            wrapper = new ContextThemeWrapper(mContext, R.style.LyTheme);
        }
        Drawable drawable = mContext.getDrawable(animTo);
        if (drawable == null) {
            Log.e(mContext.getPackageCodePath(), "ResId is wrong .");
            return;
        }

        target.setImageDrawable(drawable);
        drawable.applyTheme(wrapper.getTheme());

        if (drawable instanceof AnimatedVectorDrawable) {
            final AnimatedVectorDrawable animatedVectorDrawable = (AnimatedVectorDrawable) drawable;

            if (mHourTenDigits != -1 &&
                    mHourSingleDigits != -1 &&
                    mMinTenDigits != -1 &&
                    mMinSingleDigits != -1 &&
                    mSecTenDigits != -1) {
                animatedVectorDrawable.start();
                return;
            }
            //todo there exists a bug in XiaoMi phone : when the time is 00:00 ,
            //todo 11:11, 22:22(there are some same numbers) , only the first can work correctly
            //todo So I do this to avoid that . But this way is so fool .
            target.post(new Runnable() {
                @Override
                public void run() {
                    animatedVectorDrawable.start();
                }
            });
        }
    }

    @Override
    protected void onWindowVisibilityChanged(int visibility) {
        super.onWindowVisibilityChanged(visibility);
        if (visibility == VISIBLE) {
            // When the app is in the background , service may be hang up ,
            // so we need to reset service to revise time .
            Intent intent = new Intent(mContext, LyClockService.class);
            mContext.stopService(intent);
            mContext.startService(intent);
        }
    }
}

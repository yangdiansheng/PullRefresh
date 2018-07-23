package com.example.yangdiansheng.myapplication.pullrefresh;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.yangdiansheng.myapplication.R;
import com.example.yangdiansheng.myapplication.utils.Utils;

/**
 * Created by yangdiansheng on 2018/7/20.
 */

public class HeadTimeView extends LinearLayout implements PullRefreshHead {

    public HeadTimeView(Context context) {
        this(context, null);
    }

    public HeadTimeView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public HeadTimeView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private TextView mTvRefreshStatus;
    private TextView mTvRefreshTime;
    private ImageView mIvRefresh;
    private LinearLayout mLlHeaderRefreshTimeContainer;

    private long mTimeMillis = 0;
    private float mHeight;

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        mHeight = getHeight();
        Log.i("yyy", " HeadTimeView mHeight=" + mHeight);
    }

    private void init() {
        LayoutInflater.from(getContext()).inflate(R.layout.list_head_time, this);
        mTvRefreshStatus = (TextView) findViewById(R.id.tv_refresh_status);
        mTvRefreshTime = (TextView) findViewById(R.id.tv_refresh_time);
        mIvRefresh = (ImageView) findViewById(R.id.iv_refresh);
        mLlHeaderRefreshTimeContainer = (LinearLayout) findViewById(R.id.ll_header_refresh_time_container);
        initRefresh();
    }

    private void initRefresh() {
        mTvRefreshStatus.setText(getResources().getString(R.string.listview_header_hint_normal));
        mTvRefreshTime.setText(Utils.getTime(mTimeMillis));
        Utils.reserveView(mIvRefresh,0,180);
        mLlHeaderRefreshTimeContainer.setVisibility(mTimeMillis == 0 ? GONE : VISIBLE);
    }

    private void releaseRefresh() {
        mTvRefreshStatus.setText(getResources().getString(R.string.listview_header_hint_release));
        mTvRefreshTime.setText(Utils.getTime(mTimeMillis));
        Utils.reserveView(mIvRefresh,180,360);
    }

    @Override
    public void onStart() {
        Log.i("yyy", "onStart");
    }

    @Override
    public void onRefresh() {
        mTimeMillis = System.currentTimeMillis();
        Log.i("yyy", "onRefresh");
    }

    @Override
    public void onComplete() {
        Log.i("yyy", "onComplete");
    }

    @Override
    public void onPullDown(float setOffRate) {
        Log.i("yyy", " onPull setOffRate=" + setOffRate);
        if (setOffRate < 1f) {
            initRefresh();
        } else {
            releaseRefresh();
        }
    }

    @Override
    public View getView() {
        return this;
    }
}

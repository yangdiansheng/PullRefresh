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

/**
 * Created by yangdiansheng on 2018/7/20.
 */

public class HeadTimeView extends LinearLayout implements PullRefreshHead {

    public HeadTimeView(Context context) {
        this(context,null);
    }

    public HeadTimeView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public HeadTimeView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private TextView mTvRefreshStatus;
    private TextView mTvRefreshTime;
    private ImageView mIvRefresh;

    private long mTime;


    private void init() {
        LayoutInflater.from(getContext()).inflate(R.layout.list_head_time, this);
        mTvRefreshStatus = (TextView) findViewById(R.id.tv_refresh_status);
        mTvRefreshTime = (TextView) findViewById(R.id.tv_refresh_time);
        mIvRefresh = (ImageView) findViewById(R.id.iv_refresh);
        initRefresh();
    }

    private void initRefresh(){
        mTvRefreshStatus.setText(getResources().getString(R.string.listview_header_hint_normal));
        mIvRefresh.setImageResource(R.mipmap.ic_pulltorefresh_arrow);
        mTvRefreshTime.setText(mTime + "");
    }

    private void releaseRefresh(){
        mTvRefreshStatus.setText(getResources().getString(R.string.listview_header_hint_release));
        mIvRefresh.setImageResource(R.mipmap.ic_pulltorefresh_arrow);
        mTvRefreshTime.setText(mTime + "");
    }

    @Override
    public void onStart() {
        Log.i("yyy","onStart");
    }

    @Override
    public void onRefresh() {
        mTime = System.currentTimeMillis();
        Log.i("yyy","onRefresh" );
    }

    @Override
    public void onComplete() {
        Log.i("yyy","onComplete");
    }

    @Override
    public void onPull(float dy) {
        Log.i("yyy","" + dy);
        if (dy < 1f){
            initRefresh();
        } else {
            releaseRefresh();
        }
    }

    @Override
    public boolean canRefresh() {
        return false;
    }

    @Override
    public View getView() {
        return this;
    }
}

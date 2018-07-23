package com.example.yangdiansheng.myapplication.pullrefresh;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.yangdiansheng.myapplication.R;

/**
 * Created by yangdiansheng on 2018/7/23.
 */

public class FooterView extends LinearLayout implements PullRefreshFooter{

    public FooterView(Context context) {
        this(context,null);
    }

    public FooterView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public FooterView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private TextView mTvLoadMore;
    private boolean mIsLoading = false;

    private void init() {
        LayoutInflater.from(getContext()).inflate(R.layout.list_footer, this);
        mTvLoadMore = (TextView) findViewById(R.id.tv_load_more);
    }


    private void initLoadMoreComplete(){
        mTvLoadMore.setText(getResources().getString(R.string.loading_done));
    }

    private void initLoadMoreLoading(){
        mIsLoading = true;
        mTvLoadMore.setText(getResources().getString(R.string.listview_loading));
    }

    public void initLoadMoreNoMore(){
        mTvLoadMore.setText(getResources().getString(R.string.nomore_loading));
    }



    @Override
    public void onStart() {
        Log.i("yyy", "onStart");

    }

    @Override
    public void onLoadMore() {
        Log.i("yyy", "onLoadMore");
        initLoadMoreLoading();
    }

    @Override
    public void onComplete() {
        Log.i("yyy", "onComplete");
        postDelayed(new Runnable() {
            @Override
            public void run() {
                mIsLoading = false;
            }
        },500);
        initLoadMoreComplete();
    }

    @Override
    public void onPullUp(float setOffRate) {
        Log.i("yyy", "onPullUp");
        if (setOffRate < 1f) {
        }
    }

    @Override
    public View getView() {
        return this;
    }

    @Override
    public boolean isLoading() {
        return mIsLoading;
    }
}

package com.example.yangdiansheng.myapplication.pullrefresh;

import android.view.View;

/**
 * Created by yangdiansheng on 2018/7/20.
 * 底部上拉
 */

public interface PullRefreshFooter {
    //开始上拉
    void onStart();
    //开始加重
    void onLoadMore();
    //刷新完成
    void onComplete();
    //持续上拉 setOffRate上拉的比率 0为未动，1为应该刷新了
    void onPullUp(float setOffRate);

    View getView();

    boolean isLoading();
}

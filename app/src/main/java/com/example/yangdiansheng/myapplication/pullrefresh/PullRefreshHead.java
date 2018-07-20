package com.example.yangdiansheng.myapplication.pullrefresh;

import android.view.View;

/**
 * Created by yangdiansheng on 2018/7/20.
 * 顶部下拉类
 */

public interface PullRefreshHead {
    //开始下拉
    void onStart();
    //开始刷新
    void onRefresh();
    //刷新完成
    void onComplete();
    //持续下拉
    void onPull(float dy);
    //是否可以刷新
    boolean canRefresh();

    View getView();
}

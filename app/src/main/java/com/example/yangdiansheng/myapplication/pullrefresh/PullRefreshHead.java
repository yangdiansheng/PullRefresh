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
    //持续下拉 setOffRate下拉的比率 0为未动，1为应该刷新了
    void onPullDown(float setOffRate);

    View getView();
}

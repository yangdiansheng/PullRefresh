package com.yangdiansheng.lib_recyclerview;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;

/**
 * Created by yangdiansheng on 2018/7/16.
 * 头部的刷新类如扩展需要实现BaseRefreshHeader接口
 */

public class HeaderRefreshArrow extends LinearLayout implements BaseRefreshHeader{

    private LinearLayout mHeaderRefreshTimeContainer;
    private LinearLayout mContainer;

    public HeaderRefreshArrow(Context context) {
        super(context);
        initView();
    }

    public HeaderRefreshArrow(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    private void initView() {
        // 初始情况，设置下拉刷新view高度为0
        mContainer = (LinearLayout) LayoutInflater.from(getContext()).inflate(
                R.layout.lib_recyclerview_header_arrow, null);

    }


    @Override
    public void onMove(float delta) {

    }

    @Override
    public boolean releaseAction() {
        return false;
    }

    @Override
    public void refreshComplete() {

    }
}

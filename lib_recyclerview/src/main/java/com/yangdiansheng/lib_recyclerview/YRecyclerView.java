package com.yangdiansheng.lib_recyclerview;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Created by yangdiansheng on 2018/7/16.
 * recyclerview
 */

public class YRecyclerView extends RecyclerView {


    private HeaderRefreshArrow mRefreshHeaderArrow;//下拉刷新的view
    private boolean mPullRefreshEnabled = true;//是否可以下拉刷新
    private float mLastY = -1;//最后一次点击的Y坐标
    private static final float DRAG_RATE = 3;//下拉率

    public YRecyclerView(Context context) {
        this(context,null);
    }

    public YRecyclerView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public YRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        if (mPullRefreshEnabled) {
            mRefreshHeaderArrow = new HeaderRefreshArrow(getContext());
        }
    }

    public void setPullRefreshEnabled(boolean enabled) {
        mPullRefreshEnabled = enabled;
    }


    public HeaderRefreshArrow getDefaultRefreshHeaderView(){
        if(mRefreshHeaderArrow == null){
            return null;
        }
        return mRefreshHeaderArrow;
    }

    @Override
    public boolean onTouchEvent(MotionEvent e) {
        if (mLastY == -1){
            mLastY = e.getRawY();
        }
        switch (e.getAction()){
            case MotionEvent.ACTION_DOWN:
                mLastY = e.getRawY();
                break;
            case MotionEvent.ACTION_MOVE:
                final float detlaY = e.getRawY() - mLastY;
                mLastY = e.getRawY();
                mRefreshHeaderArrow.onMove(detlaY / DRAG_RATE);
                return false;
            default:
                mLastY = -1;
                break;
        }
        return super.onTouchEvent(e);
    }
}

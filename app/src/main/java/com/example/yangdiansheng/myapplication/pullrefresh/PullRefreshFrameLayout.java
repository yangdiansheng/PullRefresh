package com.example.yangdiansheng.myapplication.pullrefresh;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.NestedScrollView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.FrameLayout;

/**
 * Created by yangdiansheng on 2018/7/17.
 */

public class PullRefreshFrameLayout extends FrameLayout {

    public PullRefreshFrameLayout(@NonNull Context context) {
        this(context, null);
    }

    public PullRefreshFrameLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PullRefreshFrameLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    private static final float MAX_HEAD_REFRESH_HIGH = 10000;
    private static final float REFRESH_HIGH = 400;

    private float mTouchY;
    private float mTouchX;
    private View mChildView;
    private DecelerateInterpolator mDecelerateInterpolator;
    private PullRefreshHead mPullRefreshHead;

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        mChildView = getChildAt(0);
        mDecelerateInterpolator = new DecelerateInterpolator(.5f);
        mPullRefreshHead = new HeadTimeView(getContext());
        addView(mPullRefreshHead.getView());
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mTouchY = ev.getY();
                mTouchX = ev.getX();
                break;
            case MotionEvent.ACTION_MOVE:
                float currentY = ev.getY();
                float dy = currentY - mTouchY;
                if (dy > 0 && !canChildScroll(-1)) {
                    return true;
                } else if (dy < 0 && !canChildScroll(1)) {
                    return true;
                } else {
                    super.onInterceptTouchEvent(ev);
                }
                break;
        }
        return super.onInterceptTouchEvent(ev);
    }

    /**
     * @param flag 负数检测下拉能否滚动，正数检测上拉能否滚动
     */
    private boolean canChildScroll(int flag) {
        if (mChildView == null){
            return false;
        }
        return ViewCompat.canScrollVertically(mChildView, flag);

    }

    private float dy;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mTouchY = event.getY();
                mTouchX = event.getX();
                break;
            case MotionEvent.ACTION_MOVE:
                dy = event.getY() - mTouchY;
                float dx = event.getX() - mTouchX;
                if (Math.abs(dy) > Math.abs(dx) && Math.abs(dy) < MAX_HEAD_REFRESH_HIGH){
                    if (mChildView != null) {
                        float offset = mDecelerateInterpolator.getInterpolation(Math.abs(dy) / MAX_HEAD_REFRESH_HIGH) * MAX_HEAD_REFRESH_HIGH / 2;
                        if (dy > 0){
                            mChildView.setTranslationY(offset);
                            mPullRefreshHead.onPull(dy / REFRESH_HIGH);
                        } else {
                            mChildView.setTranslationY(- offset);
                        }
                    }
                }
                return true;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                if (mChildView != null) {
                    mChildView.setTranslationY(0);
                    if (dy > REFRESH_HIGH){
                        mPullRefreshHead.onRefresh();
                    }
                }
                break;
        }
        return super.onTouchEvent(event);
    }
}

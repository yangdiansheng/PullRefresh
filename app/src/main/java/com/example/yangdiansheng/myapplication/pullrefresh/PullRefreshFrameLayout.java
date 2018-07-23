package com.example.yangdiansheng.myapplication.pullrefresh;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.NestedScrollView;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
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
        init();
    }

    private void init() {
        mPullRefreshHead = new HeadTimeView(getContext());
        mDecelerateInterpolator = new DecelerateInterpolator(.5f);
    }

    private static final float MAX_HEAD_REFRESH_HIGH = 10000;
    private static final float REFRESH_HIGH = 500;

    private float mTouchY;
    private float mTouchX;
    private View mChildView;
    private DecelerateInterpolator mDecelerateInterpolator;
    private PullRefreshHead mPullRefreshHead;
    private int mHeadOffSetY;
    private float mDy;

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        mChildView = getChildAt(0);
        addView(mPullRefreshHead.getView());
        ViewGroup.LayoutParams params = mPullRefreshHead.getView().getLayoutParams();
        params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        params.width = ViewGroup.LayoutParams.MATCH_PARENT;
        mPullRefreshHead.getView().setLayoutParams(params);
        mPullRefreshHead.getView().post(new Runnable() {
            @Override
            public void run() {
                mHeadOffSetY = mPullRefreshHead.getView().getHeight();
                mPullRefreshHead.getView().setTranslationY(- mHeadOffSetY);
            }
        });
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
        if (mChildView == null) {
            return false;
        }
        return ViewCompat.canScrollVertically(mChildView, flag);

    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mTouchY = event.getY();
                mTouchX = event.getX();
                mPullRefreshHead.onStart();
                break;
            case MotionEvent.ACTION_MOVE:
                mDy = event.getY() - mTouchY;
                float dx = event.getX() - mTouchX;
                if (Math.abs(mDy) > Math.abs(dx) && Math.abs(mDy) < MAX_HEAD_REFRESH_HIGH) {
                    if (mChildView != null) {
                        float offset = mDecelerateInterpolator.getInterpolation(Math.abs(mDy) / MAX_HEAD_REFRESH_HIGH) * MAX_HEAD_REFRESH_HIGH / 2;
                        if (mDy > 0) {
                            pullHeadViewDown(offset);
                            mPullRefreshHead.onPullDown( mDy / REFRESH_HIGH);
                        } else {
                            pullHeadViewUp(- offset);
                        }
                    }
                }
                return true;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                if (mChildView != null) {
                    pullHeadViewInit();
                    if (mDy > REFRESH_HIGH) {
                        mPullRefreshHead.onRefresh();
                    }
                }
                break;
        }
        return super.onTouchEvent(event);
    }

    //恢复头部向下或者向上拉的状态
    private void pullHeadViewInit(){
        mChildView.setTranslationY(0);
        mPullRefreshHead.getView().setTranslationY(-mHeadOffSetY);
    }

    //头部向下拉
    private void pullHeadViewDown(float offset){
        mChildView.setTranslationY(offset);
        mPullRefreshHead.getView().setTranslationY(-mHeadOffSetY + offset);
    }

    //头部向上拉
    private void pullHeadViewUp(float offset){
        mChildView.setTranslationY(offset);
    }
}

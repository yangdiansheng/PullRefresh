package com.example.yangdiansheng.myapplication.pullrefresh;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
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
        mPullRefreshFooter = new FooterView(getContext());
    }

    private static final float MAX_HEAD_REFRESH_HIGH = 10000;
    private static final float REFRESH_HIGH = 500;
    private static final float LOADMORE_HIGH = 200;

    private float mTouchY;
    private float mTouchX;
    private View mChildView;
    private DecelerateInterpolator mDecelerateInterpolator;
    private PullRefreshHead mPullRefreshHead;
    private PullRefreshFooter mPullRefreshFooter;
    private int mHeadOffSetY;
    private int mFooterOffSetY;
    private float mDy;

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        mChildView = getChildAt(0);
        addHeadView();
        addFooterView();
    }

    private void addHeadView() {
        addView(mPullRefreshHead.getView());
        ViewGroup.LayoutParams params = mPullRefreshHead.getView().getLayoutParams();
        params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        params.width = ViewGroup.LayoutParams.MATCH_PARENT;
        mPullRefreshHead.getView().setLayoutParams(params);
        mPullRefreshHead.getView().post(new Runnable() {
            @Override
            public void run() {
                mHeadOffSetY = mPullRefreshHead.getView().getHeight();
                mPullRefreshHead.getView().setTranslationY(-mHeadOffSetY);
                refresh();
            }
        });
    }

    private void addFooterView() {
        addView(mPullRefreshFooter.getView());
        FrameLayout.LayoutParams params = (LayoutParams) mPullRefreshFooter.getView().getLayoutParams();
        params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        params.width = ViewGroup.LayoutParams.MATCH_PARENT;
        params.gravity = Gravity.BOTTOM;
        mPullRefreshFooter.getView().setLayoutParams(params);
        mPullRefreshFooter.getView().post(new Runnable() {
            @Override
            public void run() {
                mFooterOffSetY = mPullRefreshFooter.getView().getHeight();
                Log.i("yyy", "getBottom()=" + getHeight());
                Log.i("yyy", "mPullRefreshFooter.getView().getHeight()=" + mPullRefreshFooter.getView().getHeight());
                mPullRefreshFooter.getView().setTranslationY(mFooterOffSetY);
            }
        });
    }

    //防止更多加载数据阻挡列表数据
    private void setMarginToBottom(int value) {
        FrameLayout.LayoutParams params = (LayoutParams) mChildView.getLayoutParams();
        params.height = ViewGroup.LayoutParams.MATCH_PARENT;
        params.width = ViewGroup.LayoutParams.MATCH_PARENT;
        params.setMargins(0, 0, 0, value);
        mChildView.setLayoutParams(params);
    }


    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mTouchY = ev.getY();
                mTouchX = ev.getX();
                break;
            case MotionEvent.ACTION_MOVE:
                if (mPullRefreshHead.isRefreshing() || mPullRefreshFooter.isLoading()) {
                    return super.onInterceptTouchEvent(ev);
                }
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
                            if (!mPullRefreshHead.isRefreshing()) {
                                pullViewDown(offset);
                                mPullRefreshHead.onPullDown(mDy / REFRESH_HIGH);
                            }
                        } else {
                            if (!mPullRefreshFooter.isLoading()) {
                                pullFooterViewUp(-offset);
                                mPullRefreshFooter.onPullUp(Math.abs(mDy) / LOADMORE_HIGH);
                            }
                        }
                    }
                }
                return true;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                if (mChildView != null) {
                    if (mDy > 0) {
                        if (!mPullRefreshHead.isRefreshing()) {
                            if (mDy > REFRESH_HIGH) {
                                refresh();
                            } else {
                                pullViewInit();
                            }
                        }
                    } else {
                        if (!mPullRefreshFooter.isLoading()) {
                            if (Math.abs(mDy) > LOADMORE_HIGH) {
                                loadMore();
                            } else {
                                pullFooterViewInit();
                            }
                        }
                    }
                }
                break;
        }
        return super.onTouchEvent(event);
    }

    private void loadMore() {
        pullFooterViewLoading();
        mPullRefreshFooter.onLoadMore();
        if (mCallBack != null) {
            mCallBack.onLoadMore();
        }
    }

    public void refresh() {
        pullViewLoading();
        mPullRefreshHead.onRefresh();
        if (mCallBack != null) {
            mCallBack.onRefresh();
        }
    }

    //恢复头部向下或者向上拉的状态
    private void pullViewInit() {
        mChildView.setTranslationY(0);
        mPullRefreshHead.getView().setTranslationY(-mHeadOffSetY);
    }

    //恢复头部加载的状态
    private void pullViewLoading() {
        mChildView.setTranslationY(mHeadOffSetY);
        mPullRefreshHead.getView().setTranslationY(0);
    }

    //头部向下拉
    private void pullViewDown(float offset) {
        mChildView.setTranslationY(offset);
        mPullRefreshHead.getView().setTranslationY(-mHeadOffSetY + offset);
    }

    //向上拉
    private void pullFooterViewUp(float offset) {
        mChildView.setTranslationY(offset);
        mPullRefreshFooter.getView().setTranslationY(mFooterOffSetY - Math.abs(offset));
    }

    //底部复位
    private void pullFooterViewInit() {
        mChildView.setTranslationY(0);
        mPullRefreshFooter.getView().setTranslationY(mFooterOffSetY);
        setMarginToBottom(0);
    }

    //底部正在加载
    private void pullFooterViewLoading() {
        mChildView.setTranslationY(-mFooterOffSetY);
        mPullRefreshFooter.getView().setTranslationY(0);
    }

    public static interface CallBack {
        void onRefresh();

        void onLoadMore();
    }

    private CallBack mCallBack;

    public void setCallBack(CallBack callBack) {
        mCallBack = callBack;
    }

    //刷新完成
    public void refreshComplete() {
        mPullRefreshHead.onComplete();
        postDelayed(new Runnable() {
            @Override
            public void run() {
                pullViewInit();
            }
        }, 500);
    }

    //加载更多完成
    public void loadMoreComplete() {
        mPullRefreshFooter.onComplete();
        postDelayed(new Runnable() {
            @Override
            public void run() {
                pullFooterViewInit();
            }
        }, 500);
    }
}

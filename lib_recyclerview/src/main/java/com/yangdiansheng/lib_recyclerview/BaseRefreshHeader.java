package com.yangdiansheng.lib_recyclerview;

/**
 * Created by yangdiansheng on 2018/7/16.
 */
interface BaseRefreshHeader {

	int STATE_NORMAL = 0;//正常状态
	int STATE_RELEASE_TO_REFRESH = 1;//准备刷新状态
	int STATE_REFRESHING = 2;//正在刷新状态
	int STATE_DONE = 3;//刷新完毕状态

	/**
	 * 移动距离
	 * @param delta 移动的偏移量
	 */
	void onMove(float delta);

	/**
	 * 释放状态
	 * @return 是否需要刷新
	 */
	boolean releaseAction();

	/**
	 * 刷新完毕
	 */
	void refreshComplete();

}
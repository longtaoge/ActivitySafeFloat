package org.xiangbalao.floatview.base;


import org.xiangbalao.floatview.MyWindowManager;

import android.content.Context;
import android.view.View;

public abstract class BaseFloatView {

	public static MyWindowManager mMyWindowManager;



	public Context mContext;

	public BaseFloatView(Context context) {

		this.mContext = context;
		mMyWindowManager = MyWindowManager.getInstance(mContext);

	}

	/**
	 * 返回浮窗
	 * 
	 * @return
	 */
	public abstract View getFloatView();

	/**
	 * 初始化浮窗
	 */
	public abstract void initView();



}

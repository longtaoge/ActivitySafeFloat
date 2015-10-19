package org.xiangbalao.floatview;

import org.xiangbalao.activityfloat.R;
import org.xiangbalao.floatview.base.BaseWindowManager;
import org.xiangbalao.floatview.view.SafeFloatView;

import android.content.Context;
import android.content.SharedPreferences.Editor;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.WindowManager;

public class MyWindowManager extends BaseWindowManager {

	private static KillThead mKillThead;
	private static float lastX;
	private static float lastY;
	// 移动或点击
	protected static int tag;
	private static MyWindowManager mMyWindowManagerIntance = null;

	public static MyWindowManager getInstance(Context context) {
		if (mMyWindowManagerIntance == null) {
			synchronized (MyWindowManager.class) {
				if (mMyWindowManagerIntance == null) {
					mMyWindowManagerIntance = new MyWindowManager(context);
				}
			}
		}
		return mMyWindowManagerIntance;

	}

	private MyWindowManager(Context context) {
		super();
		mContext = context;
		if (mContext != null) {
			mWindowManager = (WindowManager) mContext
					.getSystemService(Context.WINDOW_SERVICE);
			mWindowManager = MyFloatServes.mWindowManager;
			ScreenWidth = MyFloatServes.display.getWidth();
			ScreenHeight = MyFloatServes.display.getHeight();
		}
		mLayoutParams = new WindowManager.LayoutParams();
		mLayoutParams.type = WindowManager.LayoutParams.TYPE_PHONE;
		mLayoutParams.flags = 40;
		mLayoutParams.format = -3;
	}

	/**
	 * 退出窗口
	 */
	public static void andSafeFloatView() {

		if (mLayoutParams_Safe == null) {
			mLayoutParams_Safe = new WindowManager.LayoutParams();
		}
		mLayoutParams_Safe.type = WindowManager.LayoutParams.TYPE_PHONE;
		mLayoutParams_Safe.flags = 40;
		mLayoutParams_Safe.format = -3;

		mLayoutParams_Safe.width = WindowManager.LayoutParams.WRAP_CONTENT;
		mLayoutParams_Safe.height = WindowManager.LayoutParams.WRAP_CONTENT;

		MyFloatServes.sp = MyFloatServes.mContext.getSharedPreferences(
				MyFloatServes.SP_NAME, 0);
		mExitFloatView = SafeFloatView.getInstance().getFloatView();
		if (mWindowManager != null) {
			if (mExitFloatView != null) {
				try {
					if (mExitFloatView.getParent() == null) {
						mWindowManager.addView(mExitFloatView,
								mLayoutParams_Safe);
					}

				} catch (Exception e) {
					mWindowManager.addView(mExitFloatView, mLayoutParams_Safe);
				}

			}
		} else {
			mWindowManager = (WindowManager) mContext
					.getSystemService(Context.WINDOW_SERVICE);

			mWindowManager.addView(mExitFloatView, mLayoutParams_Safe);

		}
		setOnTouch(mExitFloatView, mLayoutParams_Safe);

	}

	/**
	 * 开始
	 */
	public static void start() {

		andSafeFloatView();

	}

	public void removeAll() {

		removeView(mExitFloatView);

	}

	/**
	 * 从窗口中移除
	 * 
	 * @param view
	 */
	public static void removeView(View view) {

		if (view != null) {
			if (view.getParent() != null) {

				if (mWindowManager != null) {
					mWindowManager.removeView(view);
				}

			}

		}

	};

	public static void setOnTouch(final View mView,
			final WindowManager.LayoutParams mwParams) {

		mView.setOnTouchListener(new OnTouchListener() {

			public boolean onTouch(View v, MotionEvent event) {
				final int action = event.getAction();
				float x = event.getX();
				float y = event.getY();

				if (action == MotionEvent.ACTION_DOWN) {
					tag = 0;
					lastX = x;
					lastY = y;

				} else if (action == MotionEvent.ACTION_MOVE) {

					x = event.getX();
					y = event.getY();

					if (Math.abs(x - lastX) > 10 | Math.abs(y - lastY) > 10) {
						tag = 1;
						mwParams.x += (int) (x - lastX) / 2;
						mwParams.y += (int) (y - lastY) / 2;

						ScreenHeight = MyFloatServes.display.getHeight();
						ScreenWidth = MyFloatServes.display.getWidth();

						mWindowManager.updateViewLayout(mView, mwParams);

						return true;

					}

					return false;
				}

				else if (action == MotionEvent.ACTION_UP) {

					x = event.getX();
					y = event.getY();

					MyFloatServes.sp = MyFloatServes.mContext
							.getSharedPreferences(MyFloatServes.SP_NAME, 0);

					Editor editor = MyFloatServes.sp.edit();

					ScreenHeight = MyFloatServes.display.getHeight();
					ScreenWidth = MyFloatServes.display.getWidth();

					if (mwParams.x < 0) {

						mwParams.x = -MyFloatServes.display.getWidth();

					}

					if (mwParams.x > 0) {
						mwParams.x = MyFloatServes.display.getWidth();

					}

					editor.putInt("paramsx_exit", mwParams.x);
					editor.putInt("paramsy_exit", mwParams.y);

					editor.commit();

					mwParams.x = MyFloatServes.sp.getInt("paramsx_exit",
							-ScreenHeight);
					mwParams.y = MyFloatServes.sp.getInt("paramsy_exit",
							-ScreenWidth / 8);

					mWindowManager.updateViewLayout(
							MyWindowManager.mExitFloatView, mwParams);

					processClick(mView, v, x, y);

				}
				return false;
			}

		});
	}

	private static void processClick(final View mView, View v, float x, float y) {

		if (v.getId() == R.id.qsyd_float_window_small_quit_linearlayout
				& tag != 1) {

			if ((Math.abs(x - lastX) < 10 & Math.abs(y - lastY) < 10)) {

				if (mView.getParent() != null) {

					mKillThead = new KillThead();
					mKillThead.start();

					MyWindowManager.getInstance(MyFloatServes.mContext)
							.removeAll();

				}

			}

		}

		tag = 0;
	}

	public static class KillThead extends Thread {

		public KillThead() {
			super();

		}

		@Override
		public void run() {

			SafeFloatView.killProcess(MyFloatServes.mPackageName);

			super.run();
		}

	}

}

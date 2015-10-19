package org.xiangbalao.floatview;

import java.util.List;

import org.xiangbalao.floatview.view.SafeFloatView;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.SystemClock;
import android.util.Log;
import android.view.Display;
import android.view.WindowManager;

public class MyFloatServes extends Service {

	// 窗口管理
	public static WindowManager mWindowManager;
	public static Context mContext;
	public static Display display;
	public static String mPackageName ;//= "org.xiangbalao.activityfloat";
	public static String mActivityName;
	public static String SP_NAME = "float_config";
	public static SharedPreferences sp;
	public static String uid;
	public static Thread eloan_monitor = null;
	protected static final int SAFE_Y = 0X001;
	private static final int SAFE_N = 0X002;
	private String activityName;
	public static ActivityManager mActivityManager;
	private Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case SAFE_Y:
				String safe_y = "正常运行中";
				MyWindowManager.getInstance(MyFloatServes.mContext)
						.andSafeFloatView();
				if (SafeFloatView.mSafeMsg != null) {
					if (!SafeFloatView.mSafeMsg.getMsg().equals(safe_y)) {
						SafeFloatView.mSafeMsg.setMsg(safe_y);
					}
				}
				// 获取传递的数据
				// Bundle data = msg.getData();
				// int count = data.getInt("COUNT");
				// 处理UI更新等打操作
				break;
			case SAFE_N:
				String safe_n = "正在后台运行，注意保护用户名和密码！";
				if (SafeFloatView.mSafeMsg != null) {
					if (!SafeFloatView.mSafeMsg.getMsg().equals(safe_n)) {
						SafeFloatView.mSafeMsg.setMsg(safe_n);
					}
				}

				// Intent mIntent= new Intent(mContext, MainActivity.class);
				// mIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				// startActivity(mIntent);
				// MyWindowManager.getInstance(MyFloatServes.mContext).removeAll();

			}
		};
	};

	@Override
	public IBinder onBind(Intent intent) {

		return null;

	}

	@Override
	public void onCreate() {

		MyFloatServes.mContext = this.getApplicationContext();
		mPackageName=mContext.getPackageName();
		MyFloatServes.mWindowManager = (WindowManager) this
				.getApplicationContext().getSystemService(
						Context.WINDOW_SERVICE);

		MyFloatServes.display = MyFloatServes.mWindowManager
				.getDefaultDisplay();

		mActivityManager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);

		super.onCreate();

	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {

		mActivityManager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);

		synchronized (MyFloatServes.class) {

			eloan_monitor = new Thread(new Runnable() {

				@Override
				public void run() {
					programWatch();

				}
			});

			eloan_monitor.start();

		}

		return super.onStartCommand(intent, flags, startId);
	}

	/**
	 * 
	 */
	private void programWatch() {

		ActivityManager manager = (ActivityManager) MyFloatServes.mContext
				.getSystemService(ACTIVITY_SERVICE);
		String packName = MyFloatServes.mPackageName;
		while (true) {
			String packageName = "";

			if (manager != null) {
				String topActivityName = "";
				List<RunningTaskInfo> runningTasks;
				if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT) {

					List<ActivityManager.RunningAppProcessInfo> tasks = manager
							.getRunningAppProcesses();
					if (tasks.get(0).importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {

						topActivityName = tasks.get(0).processName;
						packageName = tasks.get(0).processName;

						Log.i("TOPActivity", topActivityName);

					}
				} else {

					/** 获取当前正在运行的任务栈列表， 越是靠近当前运行的任务栈会被排在第一位，之后的以此类推 */
					runningTasks = manager.getRunningTasks(1);
					/** 获得当前最顶端的任务栈，即前台任务栈 */
					RunningTaskInfo runningTaskInfo = runningTasks.get(0);
					/** 获取前台任务栈的最顶端 Activity */
					ComponentName topActivity = runningTaskInfo.topActivity;
					/** 获取应用的包名 */
					packageName = topActivity.getPackageName();
					activityName = topActivity.getClassName();
					/** 输出检测到的启动应用信息 */

				}
			}

			synchronized (MyFloatServes.class) {

				if (packageName.equals(packName)) {
					Message msg = new Message();
					msg.what = SAFE_Y;
					// 这三句可以传递数据
					// Bundle data = new Bundle();
					// data.putInt("COUNT", 100);//COUNT是标签,handleMessage中使用
					// msg.setData(data);

					mHandler.sendMessage(msg);

				} else {
					Message msg = new Message();
					msg.what = SAFE_N;
					mHandler.sendMessage(msg);

				}

			}

			SystemClock.sleep(1000);
		}
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
	}

}

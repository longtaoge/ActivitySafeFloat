package org.xiangbalao.floatview.view;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import java.util.StringTokenizer;

import org.xiangbalao.activityfloat.R;
import org.xiangbalao.floatview.MyFloatServes;
import org.xiangbalao.floatview.base.BaseFloatView;
import org.xiangbalao.floatview.bean.SafeMsg;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;

/**
 * 退出按钮
 * 
 * @author longtaoger
 * 
 */
public class SafeFloatView extends BaseFloatView implements OnClickListener,
		Observer {
	private static KillThead mKillThead = null;
	private static SafeFloatView mSafeFloatViewIntance;
	private View mView;

	private ImageView mIcon;
	private MarqueeTextView mTextView;
	public static SafeMsg mSafeMsg;

	private SafeFloatView(Context context) {
		super(context);
		mContext = context;
		mSafeMsg = new SafeMsg();

		mSafeMsg.setDrawableId(R.drawable.ic_launcher);
		mSafeMsg.setMsg("程序正常运行中");
		mSafeMsg.addObserver(this);

		// 初始化view
		initView();
	}

	public static SafeFloatView getInstance() {

		if (mSafeFloatViewIntance == null) {

			synchronized (SafeFloatView.class) {

				if (mSafeFloatViewIntance == null) {

					mSafeFloatViewIntance = new SafeFloatView(
							MyFloatServes.mContext);

				}

			}

		}

		return mSafeFloatViewIntance;

	}

	@Override
	public View getFloatView() {

		if (mView != null) {
			return mView;
		} else {
			mView = LayoutInflater.from(mContext).inflate(
					R.layout.qsyd_float_window_safe, null);
			return mView;
		}
	}

	@Override
	public void initView() {
		mView = LayoutInflater.from(mContext).inflate(
				R.layout.qsyd_float_window_safe, null);
		mIcon = (ImageView) mView
				.findViewById(R.id.qsyd_float_window_small_icon_image);
		mTextView = (MarqueeTextView) mView
				.findViewById(R.id.qsyd_float_window_text);

	}

	@Override
	public void onClick(View v) {
		// TODO 处理点击事件
		mKillThead = new KillThead();
		mKillThead.start();
		// System.exit(0);
	}

	/**
	 * 杀进程
	 */
	private void mykillProcess(String pagename) {

		ActivityManager am = (ActivityManager) MyFloatServes.mContext
				.getSystemService(Context.ACTIVITY_SERVICE);
		List<RunningAppProcessInfo> infos = am.getRunningAppProcesses();
		for (RunningAppProcessInfo info : infos) {

			if (pagename.equals(info.processName)) {

				try {
					killProcess(info.pid);
				} catch (InterruptedException e) {

					e.printStackTrace();
				}
				break;
			}
		}

	}

	private void killProcess(int processId) throws InterruptedException {
		java.lang.Process process = null;
		try {
			process = Runtime.getRuntime().exec("su");
			OutputStream os = process.getOutputStream();
			os.write(("kill_processid " + processId).getBytes());
			os.flush();
			os.close();
			Thread.sleep(3000); // 这里的sleep的目的让上面的kill命令执行完成
		} catch (IOException ex) {

		} finally {
			if (process != null) {
				process.destroy();
				process = null;
			}
		}
	}

	public static void killProcess(String packageName) {

		java.lang.Process process = null;
		try {
			String processId = "";
			process = Runtime.getRuntime().exec("su");
			OutputStream os = process.getOutputStream();
			os.write("ps \n".getBytes());
			os.flush();
			BufferedReader br = new BufferedReader(new InputStreamReader(
					process.getInputStream()));
			String inline;
			while ((inline = br.readLine()) != null) {
				if (inline.contains(packageName)) {

					StringTokenizer processInfoTokenizer = new StringTokenizer(
							inline);
					int count = 0;
					while (processInfoTokenizer.hasMoreTokens()) {
						count++;
						processId = processInfoTokenizer.nextToken();
						if (count == 2) {
							break;
						}
					}

					os.write(("kill " + processId).getBytes());
					os.flush();
					if (os != null) {
						os.close();
						os = null;
					}
					br.close();
					return;
				}
			}
		} catch (IOException ex) {

		} finally {
			if (process != null) {
				process.destroy();
				process = null;
			}
		}
	}

	public class KillThead extends Thread {

		@Override
		public void run() {

			Log.i("kill", MyFloatServes.mPackageName);

			killProcess(MyFloatServes.mPackageName);
			// runOnUiThread(new Runnable() {
			// @Override
			// public void run() {
			//
			//
			//
			// Toast.makeText(MyFloatServes.mContext,
			// "kill	Tead", 0).show();
			//
			// }
			// });
			super.run();
		}

	}

	@Override
	public void update(Observable observable, Object data) {

		if (mTextView != null) {

			if (mSafeMsg != null) {

				if (mSafeMsg.getMsg().equals("程序正常运行中")) {
					mTextView.setBackgroundColor(Color.parseColor("#008866"));
				} else {
					mTextView.setBackgroundColor(Color.parseColor("#ff0000"));
				}
				mTextView.setText(mSafeMsg.getMsg());
			}

		}

	}

}

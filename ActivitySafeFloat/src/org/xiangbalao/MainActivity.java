package org.xiangbalao;

import org.xiangbalao.activityfloat.R;
import org.xiangbalao.floatview.MyFloatServes;
import org.xiangbalao.utils.SignUtils;
import org.xiangbalao.utils.ThemeUtils;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

public class MainActivity extends Activity {

	
	private SignUtils mSignUtils;
	
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		showMiuiFloatWindow();
		startServiceS();

       
		
		//
		mSignUtils = new SignUtils(this);
		Boolean debug = true;
		if (debug) {

			if (mSignUtils.getSingInfo("6bbd1c43d804e095144ec482603ba982")) {
				MainActivity.this.finish();
			}

		} else {
			// mSignUtils.getSingInfo("918162448699c16293ae99358b488892")
			
			//获取签名
			changeTheme();

		}

	}

	private void changeTheme() {
		ThemeUtils themes = new ThemeUtils(MainActivity.this);
		if (mSignUtils.getSingInfo(themes.getTheme())) {
			MainActivity.this.finish();
		}

	}

	private void showMiuiFloatWindow() {
		// 如果是小米，打开悬浮窗口选项
		if (android.os.Build.MODEL.contains("MI 3")) {
			Intent localIntent = new Intent(
					"android.settings.APPLICATION_DETAILS_SETTINGS");
			localIntent.setData(Uri.fromParts("package",
					"org.xiangbalao.activityfloat", null));
			startActivity(localIntent);

		}
	}

	private void startServiceS() {

		Intent floatServes = new Intent(MainActivity.this, MyFloatServes.class);

		startService(floatServes);

	}

	
	
	
	
	

	
	
	
	
	
	
}

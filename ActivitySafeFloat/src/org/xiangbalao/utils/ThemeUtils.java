package org.xiangbalao.utils;

import org.xiangbalao.activityfloat.R;

import android.content.Context;

public class ThemeUtils {
	Context mContext;

	public ThemeUtils(Context mContext) {
		super();
		this.mContext = mContext;
	}

	public String getTheme() {

		return getRGB();

	}

	public String getRed() {
		return mContext.getResources().getString(R.string.sign_red);

	}

	public String getBlue() {
		return mContext.getResources().getString(R.string.sign_blue);

	}

	public String getYellow() {
		return mContext.getResources().getString(R.string.sign_yello);

	}

	public String getRGB() {
		return getRed() + getBlue() + getYellow();

	}

}

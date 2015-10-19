package org.xiangbalao.test;

import org.xiangbalao.DESUtils;
import org.xiangbalao.MainActivity;
import org.xiangbalao.activityfloat.R;
import org.xiangbalao.utils.LogUtil;
import org.xiangbalao.utils.SignUtils;

import android.test.AndroidTestCase;

public class signTest extends AndroidTestCase {

	public void sngtest() {

		SignUtils mSignUtils = new SignUtils(getContext());

		// LogUtil.i("MD5LOG1",mSignUtils.getSingInfo("6bbd1c43d804e095144ec482603ba982")+"");

		// LogUtil.i("MD5LOG2",getContext().getResources().getString(R.string.sign_blue));

		try {
			String string = DESUtils.encryptMode("18500047001", getContext());

			LogUtil.i("MD5LOG42", string);

			LogUtil.i("MD5LOG4", DESUtils.decryptMode(string, getContext()));

			String string2 = "681B213181CE54145D94C72EACBA58D245CC6B8C275D95A5";
			String string3 = "65B4585F8CC659DD9FD09582E8DCCCAB";

			LogUtil.i("MD5LOG4ddd", DESUtils.decryptMode(string3, getContext()));

		} catch (Exception e) {

			e.printStackTrace();
		}

	}

}

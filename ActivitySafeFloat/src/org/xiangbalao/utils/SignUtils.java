package org.xiangbalao.utils;

import java.io.ByteArrayInputStream;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.Arrays;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.content.pm.PackageManager.NameNotFoundException;

public class SignUtils {
	Context mContext;

	public SignUtils(Context mContext) {
		super();
		this.mContext = mContext;
	}

	public boolean getSingInfo(String s) {
		try {
			PackageInfo packageInfo = mContext.getPackageManager()
					.getPackageInfo(mContext.getPackageName(),
							PackageManager.GET_SIGNATURES);
			Signature[] signatures = packageInfo.signatures;
			Signature sign = signatures[0];
			return !s.equals(parseSigature(sign.toByteArray()));

		} catch (NameNotFoundException e) {

			e.printStackTrace();
			return true;
		}

	}

	private String parseSigature(byte[] signArray) {

		try {
			CertificateFactory certFactory = CertificateFactory
					.getInstance("x.509");
			X509Certificate certificate = (X509Certificate) certFactory
					.generateCertificate(new ByteArrayInputStream(signArray));
			byte[] buffer = certificate.getEncoded();

			LogUtil.i("MD5LOG", Md5Utils.encode(Arrays.toString(buffer)));

			return Md5Utils.encode(Arrays.toString(buffer));

		} catch (CertificateException e) {

			e.printStackTrace();
			return null;
		}

	}

}

package org.xiangbalao.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Md5Utils {

	public static String encode(String charsString) {
		try {
			MessageDigest disgest = MessageDigest.getInstance("MD5");
			byte[] result = disgest.digest(charsString.getBytes());
			StringBuffer sb = new StringBuffer();

			for (byte b : result) {
				int number = (int) (b & 0xff);// 加盐 -3//10进制
				String str = Integer.toHexString(number);
				if (str.length() == 1) {
					sb.append("0");
				}
				sb.append(str);

			}

			return sb.toString();

		} catch (NoSuchAlgorithmException e) {

			e.printStackTrace();
		}

		return "";
	}

}

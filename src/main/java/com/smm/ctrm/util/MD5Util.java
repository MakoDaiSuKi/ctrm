package com.smm.ctrm.util;

import java.security.MessageDigest;

/**
 * Created by hao.zheng on 2016/4/28. md5 加密工具类
 */
public class MD5Util {

	public static String MD5(String str) {

		char md5String[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };

		try {

			byte[] btInput = str.getBytes();

			MessageDigest mdInst = MessageDigest.getInstance("MD5");

			mdInst.update(btInput);

			byte[] md = mdInst.digest();

			char[] md5Str = new char[md.length * 2];

			int k = 0;

			for (byte b : md) {

				md5Str[k++] = md5String[b >>> 4 & 0xf];
				md5Str[k++] = md5String[b & 0xf];
			}

			return new String(md5Str);

		} catch (Exception e) {

			return null;
		}

	}

	public static void main(String[] args) {
		System.out.println(Md5Utils.md5("123456"));
	}

}

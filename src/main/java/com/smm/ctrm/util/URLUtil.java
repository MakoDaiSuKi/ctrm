package com.smm.ctrm.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import org.apache.log4j.Logger;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.PascalNameFilter;

/**
 * Created by zhenghao on 2016/6/30.
 *
 * 接口调用测试方法
 */
public class URLUtil {

	private static Logger logger = Logger.getLogger(URLUtil.class);

	public static String post(String remoteUrl, Object parameter, String authToken) {

		try {

			URL url = new URL(remoteUrl);

			logger.info("remoteUrl:" + remoteUrl);

			// 打开连接
			HttpURLConnection con = (HttpURLConnection) url.openConnection();

			// 设置通用的请求属性
			con.setConnectTimeout(10 * 1000); // 设置超时时间 10 秒
			con.setRequestMethod("POST");
			con.setRequestProperty("accept", "*/*");
			con.setRequestProperty("connection", "Keep-Alive");
			con.setRequestProperty("user-agent", "Mozilla/5.0");
			con.setDoOutput(true);
			con.setDoInput(true);

			// 参数对象转换为 json 字符串. 属性名称大写
			String dataStr = JSON.toJSONString(parameter, new PascalNameFilter());

			logger.info("parameter:" + dataStr);

			// 发送数据
			byte[] data = dataStr.getBytes();

			// 指定数据格式为 json
			con.setRequestProperty("Content-Type", "application/json; charset=UTF-8");

			if (authToken != null && !authToken.isEmpty()) {

				con.setRequestProperty("Authorization",
						"Basic Account:zhenghao,AuthToken:" + authToken + ",orgName:baiyin");

			}

			con.setRequestProperty("Content-Lenth", String.valueOf(data.length));

			OutputStream out = con.getOutputStream();
			out.write(data);

			out.close();

			// 获得服务器响应码
			int responseCode = con.getResponseCode();

			// 响应成功， 读取返回内容
			if (responseCode == 200) {

				InputStream oin = con.getInputStream();

				String msg = changeInputStream(oin, "UTF-8");

				// logger.info("return msg:"+msg);

				oin.close();

				return msg;

			} else {

				logger.error("服务端响应失败。 code:" + responseCode);
			}

		} catch (Exception e) {

			e.printStackTrace();

			logger.error(e);
		}

		return null;

	}

	public static String changeInputStream(InputStream inputStream, String encode) {

		// ByteArrayOutputStream 一般叫做内存流
		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

		byte[] data = new byte[1024];
		int len = 0;
		String result = "";

		if (inputStream != null) {

			try {
				while ((len = inputStream.read(data)) != -1) {
					byteArrayOutputStream.write(data, 0, len);

				}

				result = new String(byteArrayOutputStream.toByteArray(), encode);

			} catch (IOException e) {
				e.printStackTrace();
			}

		}

		return result;
	}
}

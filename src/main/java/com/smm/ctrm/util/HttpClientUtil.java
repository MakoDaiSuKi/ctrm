package com.smm.ctrm.util;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

public class HttpClientUtil {
	/**
	 * 通过GET方式发起http请求
	 */
	public static String requestByGetMethod(String url) {
		/**
		 * 创建默认的httpClient实例
		 */
		String resJson = null;
		CloseableHttpClient httpClient = getHttpClient();
		CloseableHttpResponse httpResponse = null;
		try {
			/**
			 * 用get方法发送http请求
			 */
			HttpGet get = new HttpGet(url);
			System.out.println("执行get请求:...." + get.getURI());

			/**
			 * 发送get请求
			 */
			httpResponse = httpClient.execute(get);

			/**
			 * response实体
			 */
			HttpEntity entity = httpResponse.getEntity();
			if (null != entity) {
				resJson = new String(EntityUtils.toString(entity).getBytes("ISO-8859-1"), "UTF-8");
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				httpResponse.close();
				closeHttpClient(httpClient);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return resJson;

	}

	private static CloseableHttpClient getHttpClient() {
		return HttpClients.createDefault();
	}

	private static void closeHttpClient(CloseableHttpClient client) throws IOException {
		if (client != null) {
			client.close();
		}
	}

	public static void main(String[] args) throws UnsupportedEncodingException {
		String content = requestByGetMethod("http://newtest.smm.cn/quotecenter/instrument/zn1608/current");
		System.out.println(content);
	}
}

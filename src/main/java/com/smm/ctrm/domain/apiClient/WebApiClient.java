
package com.smm.ctrm.domain.apiClient;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map.Entry;

import org.apache.http.HttpEntity;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.InputStreamEntity;
import org.apache.http.impl.client.HttpClientBuilder;

public class WebApiClient
{
	private String _host;
	private String _authToken;

	private HttpClient client = HttpClientBuilder.create().build();
	
	public WebApiClient(String host)
	{
		_host = host;
	}

	public WebApiClient(String host, String token)
	{
		_host = host;
		_authToken = token;
	}

	public String Get(String api)
	{
		try
		{
			HttpGet httpGet = new HttpGet(FullAddress(api));
			return client.execute(httpGet).getEntity().toString();
		} catch (Exception ex) {
			ex.printStackTrace();
			return "error";
		}
	}

	public String Get(String api, String id)
	{
		try
		{
			HttpGet httpGet = new HttpGet(FullAddress(api) + id);
			return client.execute(httpGet).getEntity().toString();
		} catch (Exception ex) {
			ex.printStackTrace();
			return "error";
		}
	}

	/** 
	 下载文件 add by chenyj 20141113
	 
	 <typeparam name="TResult"></typeparam>
	 @param fileName 保存到的文件路径：路径+文件名
	 @param api
	 @param callback
	*/
	/*public <TResult> void GetFile(String fileName, String api, Guid id, Action<ActionResult<TResult>> callback)
	{
		try
		{
//			using (var httpClient = NewClient())
			HttpClient httpClient = NewClient();
			try
			{

				String uri = FullAddress(api);
				uri += "?guid=" + id;


				var result = httpClient.GetAsync(uri).Result;
				if (result == null)
				{
					throw new RuntimeException("result is null!!!");
				}

				if (result.IsSuccessStatusCode)
				{

					var stream = result.Content.ReadAsStreamAsync().Result;

					if (File.Exists(fileName))
					{
						File.Delete(fileName);
					}


					var fileStream = File.Create(fileName);
					stream.CopyTo(fileStream);

					fileStream.Close();
					stream.Close();

					fileStream.dispose();
					stream.dispose();

					ActionResult<TResult> tempVar = new ActionResult<TResult>();
					tempVar.success = true;
					tempVar.message = "文件下载成功";
					callback(tempVar);
				}
			}
			finally
			{
				httpClient.dispose();
			}
		}
		catch (RuntimeException ex)
		{

			var t = ex.getMessage();
			//    throw ex;
		}
		finally
		{

		}
	}*/




	/** 
	 上传数据流（文件）
	 
	 <typeparam name="TResult"></typeparam>
	 @param api
	 @param parameters 其他参数
	 @param content
	 @param callback
	*/
	public String Post(String api, HashMap<String, String> parameters, InputStream content)
	{
			if (content == null){
				throw new RuntimeException("数据流有误！");
			}
			try {
				StringBuilder uri = new StringBuilder();
				uri.append(FullAddress(api));

				if (parameters != null && parameters.size() > 0) {
					uri.append("?");
					for (Entry<String, String> parameter : parameters.entrySet()) {
						uri.append(parameter.getKey());
						uri.append("=");
						uri.append(parameter.getValue());
						uri.append("&");
					}
					uri.deleteCharAt(uri.length() - 1);
				}
				HttpPost httpPost = new HttpPost(uri.toString());
				HttpEntity httpEntity = new InputStreamEntity(content);
				httpPost.setEntity(httpEntity);
				
				String result = client.execute(httpPost).toString();
				if (result == null) {
					throw new RuntimeException("result is null!!!");
				}
				return result;
			} catch(Exception ex) {
				return "error";
			}
			
	}

	/** 
	 POST
	 
	 <typeparam name="TResult"></typeparam>
	 @param api
	 @param parameters 其他参数
	 @param callback
	*/
	public String Post(String api, java.util.HashMap<String, String> parameters)
	{
		try {
			StringBuilder uri = new StringBuilder();
			uri.append(FullAddress(api));
			if (parameters != null && parameters.size() > 0) {
				uri.append("?");
				for (Entry<String, String> parameter : parameters.entrySet()){
					uri.append(parameter.getKey());
					uri.append("=");
					uri.append(parameter.getValue());
					uri.append("&");
				}
			}
			uri.deleteCharAt(uri.length() - 1);
			HttpPost httpPost = new HttpPost(uri.toString());
			String result = client.execute(httpPost).getEntity().toString();
			if (result == null){
				throw new RuntimeException("result is null!!!");
			}
			return result;
		}catch (Exception ex) {
			ex.printStackTrace();
			return "error";
		}
	}
/*
	public <TResult, TParameters> void Post(String api, java.util.HashMap<String, String> parameters, TParameters data, Action<TResult> callback)
	{
//		using (var httpClient = NewClient())
		HttpClient httpClient = NewClient();
		try
		{
				///#region 生成url

			StringBuilder uri = new StringBuilder();
			uri.append(FullAddress(api));

			if (parameters != null && parameters.size() > 0)
			{
				uri.append("?");

				for (var parameter : parameters)
				{
					uri.append(parameter.getKey());
					uri.append("=");
					uri.append(parameter.getValue());
					uri.append("&");
				}
				uri.deleteCharAt(uri.length() - 1);
			}

				///#endregion


			var result = httpClient.PostAsJsonAsync(uri.toString(), data).Result;
			if (result == null)
			{
				throw new RuntimeException("result is null!!!");
			}
			if (result.IsSuccessStatusCode)
			{

				var s = result.Content.ReadAsStringAsync().Result;
				if (s != null)
					// callback(JsonConvert.DeserializeObject<TResult>(s));
				{
					callback(JsonConvert.<TResult>DeserializeObject(s));
				}
			}
			else
			{
				throw new RuntimeException(result.ReasonPhrase);
			}
		}
		finally
		{
			httpClient.dispose();
		}
	}

	public <TResult, TParameters> void Post(String api, TParameters data, Action<TResult> callback)
	{
		//try
		//{
//			using (var httpClient = NewClient())
			HttpClient httpClient = NewClient();
			try
			{

				var result = httpClient.PostAsJsonAsync(FullAddress(api), data).Result;
				if (result == null)
				{
					throw new RuntimeException("result is null!!!");
				}
				if (result.IsSuccessStatusCode)
				{

					var s = result.Content.ReadAsStringAsync().Result;
					if (s != null)
						// callback(JsonConvert.DeserializeObject<TResult>(s));
					{
						callback(JsonConvert.<TResult>DeserializeObject(s));
					}
				}
				else
				{
					throw new RuntimeException(result.ReasonPhrase);
				}
			}
			finally
			{
				httpClient.dispose();
			}
		//}
		//catch (Exception ex)
		//{
		//    var t = ex.Message;
		//    throw ex;
		//}
		//finally
		//{

		//}
	}

	public <TParameters> void Put(String api, Guid id, TParameters data, Action<String> callback)
	{
//		using (var httpClient = NewClient())
		HttpClient httpClient = NewClient();
		try
		{

			var result = httpClient.PutAsJsonAsync(FullAddress(api) + id, data).Result;
			if (result.IsSuccessStatusCode)
			{
				callback(result.Content.ReadAsStringAsync().Result);
			}
		}
		finally
		{
			httpClient.dispose();
		}
	}

	public void Delete(String api, Guid id, Action<String> callback)
	{
		try
		{
//			using (var httpClient = NewClient())
			HttpClient httpClient = NewClient();
			try
			{

				var result = httpClient.DeleteAsync(FullAddress(api) + id).Result;
				callback(result.Content.toString());
			}
			finally
			{
				httpClient.dispose();
			}
		}
		catch (RuntimeException ex)
		{
			throw ex;
		}
	}

	private HttpClient NewClient()
	{
		HttpClientHandler tempVar = new HttpClientHandler();
		tempVar.AutomaticDecompression = DecompressionMethods.GZip;
		HttpClientHandler handler = tempVar;
		HttpClient client = new HttpClient(handler);
		client.Timeout = new TimeSpan(0, 10, 0);
		if (!DotNetToJavaStringHelper.isNullOrEmpty(_authToken))
		{
			client.DefaultRequestHeaders.Authorization = new AuthenticationHeaderValue("Basic", _authToken);
		}
		return client;
	}
*/
	private String FullAddress(String api) {
		return String.format("%s/%s", DotNetToJavaStringHelper.trimEnd(_host, '/'), DotNetToJavaStringHelper.trimStart(api, '/'));
	}	
}
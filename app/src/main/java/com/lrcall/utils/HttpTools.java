package com.lrcall.utils;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class HttpTools
{
	private static final String TAG = HttpTools.class.getName();

	public static String doGet(String url)
	{
		return doGet(url, "utf-8");
	}

	/**
	 * HTTP GET方式提交数据
	 *
	 * @param url    带参数的URL
	 * @param encode 编码类型
	 * @return 服务器返回的数据
	 */
	public static String doGet(String url, String encode)
	{
		LogcatTools.debug(TAG + " doGet", "RELEASE_URL GET:" + url);
		String strResult = null;
		HttpClient client = null;
		HttpGet request = null;
		BufferedReader in = null;
		try
		{
			client = new DefaultHttpClient();
			request = new HttpGet(url);
			request.setHeader("Cache-Control", "no-cache");
			HttpResponse response = client.execute(request);
			if (response.getStatusLine().getStatusCode() == 200)
			{
				// "gb2312", "utf-8"
				in = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), encode));
				String line = "";
				strResult = "";
				while ((line = in.readLine()) != null)
				{
					strResult += line;
				}
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			if (in != null)
			{
				try
				{
					in.close();
				}
				catch (IOException e)
				{
					e.printStackTrace();
				}
			}
			if (request != null)
			{
				request.abort();
			}
			client.getConnectionManager().shutdown();
		}
		LogcatTools.debug(TAG + " doGet", "strResult:" + strResult);
		return strResult;
	}

	/**
	 * HTTP POST方式提交数据
	 *
	 * @param url 提交的URL
	 * @param map 参数Map
	 * @return 服务器返回的数据
	 */
	public static String doPost(String url, HashMap<String, String> map)
	{
		LogcatTools.debug(TAG + " doPost", "POST RELEASE_URL:" + url);
		String strResult = null;
		HttpPost post = new HttpPost(url);
		// post
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		for (String key : map.keySet())
		{
			params.add(new BasicNameValuePair(key, map.get(key)));
			LogcatTools.debug(TAG + " doPost", "key:" + key + ",value:" + map.get(key) + ".");
		}
		try
		{
			post.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
			HttpResponse response = new DefaultHttpClient().execute(post);
			if (response.getStatusLine().getStatusCode() == 200)
			{
				strResult = EntityUtils.toString(response.getEntity(), HTTP.UTF_8);
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		LogcatTools.debug(TAG + " doPost", "strResult:" + strResult);
		return strResult;
	}

	/**
	 * 取服务器返回数据的<body></body>之间的数据
	 *
	 * @param htmlContent 原始的数据
	 * @return 解析后的数据
	 */
	public static String getBodyContent(String htmlContent)
	{
		if (StringTools.isNull(htmlContent))
		{
			return htmlContent;
		}
		String result = htmlContent.trim();
		String comp = "<body>";
		if (result.indexOf(comp) >= 0)
		{
			result = result.substring(result.indexOf(comp) + comp.length());
		}
		comp = "</body>";
		if (result.indexOf(comp) >= 0)
		{
			result = result.substring(0, result.indexOf(comp));
		}
		return result;
	}
}

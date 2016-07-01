package com.lrcall.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.lrcall.appcall.models.ReturnInfo;
import com.lrcall.appcall.models.ReturnListInfo;

import java.lang.reflect.Type;

public class GsonTools
{
	// 获取正常的Gson，通常用于解析
	public static Gson getGson()
	{
		return new Gson();
	}

	// 获取build的Gson，用于不转义特殊字符串
	public static Gson getBuildGson()
	{
		// return new GsonBuilder().disableHtmlEscaping().serializeNulls().create();
		return new GsonBuilder().disableHtmlEscaping().create();
	}

	// 转化成json字符串
	public static String toJson(Object object)
	{
		return getBuildGson().toJson(object);
	}

	// 解析json返回对象
	public static ReturnInfo getReturnInfo(String json)
	{
		if (StringTools.isNull(json))
		{
			return null;
		}
		try
		{
			ReturnInfo returnInfo = getBuildGson().fromJson(json, ReturnInfo.class);
			return returnInfo;
		}
		catch (Exception e)
		{
			return null;
		}
	}

	public static ReturnListInfo getReturnListInfo(String json)
	{
		if (StringTools.isNull(json))
		{
			return null;
		}
		try
		{
			ReturnListInfo returnInfo = getBuildGson().fromJson(json, ReturnListInfo.class);
			return returnInfo;
		}
		catch (Exception e)
		{
			return null;
		}
	}

	// 解析json获取打包的对象，用于单个对象，这里直接解析Service返回的字符串
	public static <T> T getReturnObject(ReturnInfo returnInfo, Class<T> classOfT)
	{
		if (returnInfo == null)
		{
			return null;
		}
		try
		{
			if (ReturnInfo.isSuccess(returnInfo))
			{
				T t = getBuildGson().fromJson(returnInfo.getErrmsg(), classOfT);
				return t;
			}
		}
		catch (Exception e)
		{
			return null;
		}
		return null;
	}

	// 解析json获取打包的对象，用于单个对象，这里直接解析Service返回的字符串
	public static <T> T getReturnObject(String json, Class<T> classOfT)
	{
		if (StringTools.isNull(json))
		{
			return null;
		}
		try
		{
			ReturnInfo returnInfo = getBuildGson().fromJson(json, ReturnInfo.class);
			if (ReturnInfo.isSuccess(returnInfo))
			{
				T t = getBuildGson().fromJson(returnInfo.getErrmsg(), classOfT);
				return t;
			}
		}
		catch (Exception e)
		{
			return null;
		}
		return null;
	}

	// 解析json获取打包的对象，用于单个对象
	public static <T> T getObject(String json, Class<T> classOfT)
	{
		if (StringTools.isNull(json))
		{
			return null;
		}
		try
		{
			T t = getBuildGson().fromJson(json, classOfT);
			return t;
		}
		catch (Exception e)
		{
			return null;
		}
	}

	/**
	 * 解析返回的ReturnInfo中的数组对象
	 *
	 * @param json     带ReturnInfo的json字符串
	 * @param classOfT 转换成的数组元素类型
	 * @return 数组 classOfT可以直接用类名.class方式或者用 Type type = new TypeToken<List<UserInfo>>() { }.getType();方式构造
	 */
	public static <T> T getReturnObjects(String json, Type classOfT)
	{
		if (StringTools.isNull(json))
		{
			return null;
		}
		try
		{
			ReturnInfo returnInfo = getBuildGson().fromJson(json, ReturnInfo.class);
			if (ReturnInfo.isSuccess(returnInfo))
			{
				T t = getBuildGson().fromJson(returnInfo.getErrmsg(), classOfT);
				return t;
			}
		}
		catch (Exception e)
		{
			return null;
		}
		return null;
	}

	public static <T> T getReturnObjects(ReturnInfo returnInfo, Type classOfT)
	{
		if (returnInfo == null)
		{
			return null;
		}
		try
		{
			if (ReturnInfo.isSuccess(returnInfo))
			{
				T t = getBuildGson().fromJson(returnInfo.getErrmsg(), classOfT);
				return t;
			}
		}
		catch (Exception e)
		{
			return null;
		}
		return null;
	}

	public static <T> T getReturnListObjects(String json, Type classOfT)
	{
		if (StringTools.isNull(json))
		{
			return null;
		}
		try
		{
			ReturnListInfo returnInfo = getBuildGson().fromJson(json, ReturnListInfo.class);
			if (ReturnListInfo.isSuccess(returnInfo))
			{
				T t = getBuildGson().fromJson(returnInfo.getErrmsg(), classOfT);
				return t;
			}
		}
		catch (Exception e)
		{
			return null;
		}
		return null;
	}

	public static <T> T getReturnListObjects(ReturnListInfo returnListInfo, Type classOfT)
	{
		if (returnListInfo == null)
		{
			return null;
		}
		try
		{
			if (ReturnListInfo.isSuccess(returnListInfo))
			{
				T t = getBuildGson().fromJson(returnListInfo.getErrmsg(), classOfT);
				return t;
			}
		}
		catch (Exception e)
		{
			return null;
		}
		return null;
	}

	// 解析json获取打包的对象，用于数组对象
	public static <T> T getObjects(String json, Type classOfT)
	{
		if (StringTools.isNull(json))
		{
			return null;
		}
		try
		{
			T t = getBuildGson().fromJson(json, classOfT);
			return t;
		}
		catch (Exception e)
		{
			return null;
		}
	}
}

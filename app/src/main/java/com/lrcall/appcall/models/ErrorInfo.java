/*
 * Libit保留所有版权，如有疑问联系QQ：308062035
 * Copyright (c) 2016.
 */

package com.lrcall.appcall.models;

import com.lrcall.utils.StringTools;

import java.util.HashMap;

public class ErrorInfo
{
	private static final int SUCCESS = 0;
	private static final int PARAM_ERROR = -10;
	private static final int EXIST_ERROR = -11;
	private static final int NOT_EXIST_ERROR = -12;
	private static final int HIBERNATE_ERROR = -100;
	private static final int NETWORK_ERROR = -200;
	private static final int FORBIDDEN_ERROR = -300;
	private static final int UNKNOWN_ERROR = -10000;
	public final static HashMap<Integer, String> ERROR_INFO = new HashMap<Integer, String>()
	{
		private static final long serialVersionUID = 111L;

		{
			put(SUCCESS, "操作成功");
			put(PARAM_ERROR, "参数错误");
			put(EXIST_ERROR, "对象已存在");
			put(NOT_EXIST_ERROR, "对象不存在");
			put(HIBERNATE_ERROR, "数据库错误");
			put(NETWORK_ERROR, "网络错误");
			put(FORBIDDEN_ERROR, "禁止访问");
			put(UNKNOWN_ERROR, "未知错误");
		}
	};
	private static final int PASSWORD_ERROR = -20000;

	public static int getSuccessId()
	{
		return SUCCESS;
	}

	public static int getParamErrorId()
	{
		return PARAM_ERROR;
	}

	public static int getPasswordErrorId()
	{
		return PASSWORD_ERROR;
	}

	public static int getExistErrorId()
	{
		return EXIST_ERROR;
	}

	public static int getNotExistErrorId()
	{
		return NOT_EXIST_ERROR;
	}

	public static int getHibernateErrorId()
	{
		return HIBERNATE_ERROR;
	}

	public static int getNetWorkErrorId()
	{
		return NETWORK_ERROR;
	}

	public static int getForbiddenErrorId()
	{
		return FORBIDDEN_ERROR;
	}

	public static int getUnknownErrorId()
	{
		return UNKNOWN_ERROR;
	}

	public static String getErrorInfo(int errcode)
	{
		String errInfo = ERROR_INFO.get(errcode);
		if (StringTools.isNull(errInfo))
		{
			errInfo = String.format("未知错误代码%d", errcode);
		}
		return errInfo;
	}
}

/*
 * Libit保留所有版权，如有疑问联系QQ：308062035
 * Copyright (c) 2016.
 */
package com.lrcall.appcall.services;

import com.lrcall.utils.MyConfig;

/**
 * Created by libit on 16/4/6.
 */
public class ApiConfig
{
	public static final String API_VERSION = "1";
	public static final String RELEASE_URL = "http://www.lrcall.com/lr_dialer/user";
	public static final String DEBUG_URL = "http://192.168.0.105:8080/lr_dialer/user";
	public static final String SUBMIT_BUG = getServerUrl() + "/ajaxClientToSubmitBug";//BUG日志提交
	public static final String CHECK_UPDATE = getServerUrl() + "/ajaxClientToCheckUpdate";//检查更新
	public static final String UPLOAD_FILE = getServerUrl() + "/ajaxFileToUploadBug";//上传文件
	public static final String SUBMIT_ADVICE = getServerUrl() + "/ajaxAdviceToSubmit";//提交意见反馈
	public static final String USER_LOGIN = getServerUrl() + "/ajaxLogin1";//用户登录
	public static final String USER_CHANGE_PWD = getServerUrl() + "/ajaxChangePwd";//用户修改密码
	public static final String USER_REGISTER = getServerUrl() + "/ajaxRegister";//用户注册
	public static final String GET_USER_INFO = getServerUrl() + "/ajaxGetUserInfo";//获取用户信息
	public static final String USER_BACKUP_CONTACTS = getServerUrl() + "/ajaxBackupContactList";//用户备份联系人
	public static final String GET_BACKUP_CONFIG = getServerUrl() + "/ajaxUserToGetBackupConfig";//获取用户配置备份信息
	public static final String UPDATE_BACKUP_CONFIG = getServerUrl() + "/ajaxUserToUpdateBackupConfig";//用户配置备份
	public static final String USER_SHARE = getServerUrl() + "/ajaxShareApp";//用户分享App

	public static String getServerUrl()
	{
		if (MyConfig.isDebug())
		{
			return DEBUG_URL;
		}
		else
		{
			return RELEASE_URL;
		}
	}

	// 关于我们页面
	public static String getAboutUrl()
	{
		return getServerUrl() + "/../about";
	}

	// 更多应用页面
	public static String getMoreAppUrl()
	{
		return getServerUrl() + "/../moreApp";
		//		return getServerUrl() + "/pageRegister";
	}

	//教程页面
	public static String getTutorialUrl()
	{
		return getServerUrl() + "/../tutorial";
	}

	//意见反馈页面，已废弃，用原生界面提交意见反馈代替
	public static String getAdviceUrl()
	{
		return getServerUrl() + "/pageAdvice";
	}
}

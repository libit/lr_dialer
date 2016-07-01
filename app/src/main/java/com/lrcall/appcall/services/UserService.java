/*
 * Libit保留所有版权，如有疑问联系QQ：308062035
 * Copyright (c) 2016.
 */
package com.lrcall.appcall.services;

import android.content.Context;
import android.content.Intent;

import com.androidquery.callback.AjaxStatus;
import com.lrcall.appcall.R;
import com.lrcall.appcall.models.ReturnInfo;
import com.lrcall.appcall.models.UserInfo;
import com.lrcall.ui.customer.ToastView;
import com.lrcall.utils.GsonTools;
import com.lrcall.utils.PreferenceUtils;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * 用户服务类，用于操作与用户相关的服务
 * Created by libit on 16/4/6.
 */
public class UserService extends BaseService
{
	public UserService(Context context)
	{
		super(context);
	}

	/**
	 * 用户登录
	 *
	 * @param username 账号
	 * @param password 密码
	 */
	public void login(String username, String password)
	{
		Map<String, Object> params = new HashMap<>();
		params.put("userId", username);
		params.put("password", password);
		ajaxStringCallback(ApiConfig.USER_LOGIN, params, "正在登录...");
	}

	/**
	 * 用户注册
	 *
	 * @param username  账号
	 * @param password  密码
	 * @param name      姓名
	 * @param nickname  昵称
	 * @param number    手机号码
	 * @param email     邮箱
	 * @param country   国家
	 * @param province  省份
	 * @param city      城市
	 * @param address   地址
	 * @param subscribe 签名
	 * @param language  语言
	 * @param sex       性别
	 * @param picId     图片ID
	 * @param birthday  出生日期
	 * @param remark    备注
	 */
	public void register(String username, String password, String name, String nickname, String number, String email, String country, String province, String city, String address, String subscribe, String language, Byte sex, Integer picId, Long birthday, String remark)
	{
		Map<String, Object> params = new HashMap<>();
		params.put("userId", username);
		params.put("password", password);
		params.put("name", name);
		params.put("nickname", nickname);
		params.put("number", number);
		params.put("email", email);
		params.put("country", country);
		params.put("province", province);
		params.put("city", city);
		params.put("address", address);
		params.put("subscribe", subscribe);
		params.put("language", language);
		params.put("remark", remark);
		params.put("sex", sex);
		params.put("picId", picId);
		params.put("birthday", birthday);
		ajaxStringCallback(ApiConfig.USER_REGISTER, params, "正在注册...");
	}

	/**
	 * 获取用户信息
	 */
	public void getUserInfo()
	{
		Map<String, Object> params = new HashMap<>();
		ajaxStringCallback(ApiConfig.GET_USER_INFO, params);
	}

	/**
	 * 注销登录
	 */
	public void logout()
	{
		PreferenceUtils.getInstance().setSessionId("");
	}

	/**
	 * 分享给好友
	 */
	public void share()
	{
		Map<String, Object> params = new HashMap<>();
		ajaxStringCallback(ApiConfig.USER_SHARE, params, "请稍后...");
	}

	/**
	 * 用户修改密码
	 *
	 * @param password    旧密码
	 * @param newPassword 新密码
	 */
	public void changePwd(String password, String newPassword)
	{
		Map<String, Object> params = new HashMap<>();
		params.put("password", password);
		params.put("newPassword", newPassword);
		ajaxStringCallback(ApiConfig.USER_CHANGE_PWD, params, "正在修改密码...");
	}

	@Override
	public void parseData(String url, String result, AjaxStatus status)
	{
		if (url.endsWith(ApiConfig.USER_LOGIN))
		{
			ReturnInfo returnInfo = GsonTools.getReturnInfo(result);
			if (ReturnInfo.isSuccess(returnInfo))
			{
				//登录成功，保存账号和SessionId
				UserInfo userInfo = GsonTools.getReturnObject(returnInfo, UserInfo.class);
				PreferenceUtils.getInstance().setUsername(userInfo.getUserId());
				PreferenceUtils.getInstance().setSessionId(userInfo.getSessionId());
			}
			else
			{
				// 登录失败，清空SessionId
				PreferenceUtils.getInstance().setSessionId("");
				if (returnInfo != null)
				{
					ToastView.showCenterToast(context, "登录失败：" + returnInfo.getErrmsg());
				}
				else
				{
					ToastView.showCenterToast(context, "登录失败：" + result);
				}
			}
		}
		else if (url.endsWith(ApiConfig.USER_REGISTER))
		{
			ReturnInfo returnInfo = GsonTools.getReturnInfo(result);
			if (ReturnInfo.isSuccess(returnInfo))
			{
				//注册成功，保存账号和SessionId
				UserInfo userInfo = GsonTools.getReturnObject(returnInfo, UserInfo.class);
				PreferenceUtils.getInstance().setUsername(userInfo.getUserId());
				PreferenceUtils.getInstance().setSessionId(userInfo.getSessionId());
			}
			else
			{
				// 注册失败，清空SessionId
				PreferenceUtils.getInstance().setSessionId("");
				if (returnInfo != null)
				{
					ToastView.showCenterToast(context, "注册失败：" + returnInfo.getErrmsg());
				}
				else
				{
					ToastView.showCenterToast(context, "注册失败：" + result);
				}
			}
		}
		else if (url.endsWith(ApiConfig.GET_USER_INFO))
		{
			ReturnInfo returnInfo = GsonTools.getReturnInfo(result);
			if (!ReturnInfo.isSuccess(returnInfo))
			{
				// 获取失败，清空SessionId
				PreferenceUtils.getInstance().setSessionId("");
			}
		}
		else if (url.endsWith(ApiConfig.USER_SHARE))
		{
			ReturnInfo returnInfo = GsonTools.getReturnInfo(result);
			if (ReturnInfo.isSuccess(returnInfo))
			{
				String msg = returnInfo.getErrmsg();
				Intent intent = new Intent(Intent.ACTION_SEND); // 启动分享发送到属性
				intent.setType("text/plain"); // 分享发送到数据类型
				intent.putExtra(Intent.EXTRA_SUBJECT, "分享"); // 分享的主题
				intent.putExtra(Intent.EXTRA_TEXT, msg); // 分享的内容
				intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK); // 允许intent启动新的activity
				context.startActivity(Intent.createChooser(intent, "来自" + context.getString(R.string.app_name) + "的分享")); // //目标应用选择对话框的
			}
			else
			{
				ToastView.showCenterToast(context, "暂时无法分享！");
			}
		}
		else if (url.endsWith(ApiConfig.USER_CHANGE_PWD))
		{
			ReturnInfo returnInfo = GsonTools.getReturnInfo(result);
			if (ReturnInfo.isSuccess(returnInfo))
			{
				//修改成功，保存SessionId
				UserInfo userInfo = GsonTools.getReturnObject(returnInfo, UserInfo.class);
				PreferenceUtils.getInstance().setSessionId(userInfo.getSessionId());
				ToastView.showCenterToast(context, "修改密码成功！");
			}
			else
			{
				if (returnInfo != null)
				{
					ToastView.showCenterToast(context, "修改密码失败：" + returnInfo.getErrmsg());
				}
				else
				{
					ToastView.showCenterToast(context, "修改密码失败：" + result);
				}
			}
		}
	}

	@Override
	protected void parseData(String url, File file, AjaxStatus status)
	{
		super.parseData(url, file, status);
		if (url.endsWith("jpg"))
		{
			if (file == null)
			{
			}
		}
	}
}

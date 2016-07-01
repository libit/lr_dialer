/*
 * Libit保留所有版权，如有疑问联系QQ：308062035
 * Copyright (c) 2016.
 */
package com.lrcall.appcall.services;

import android.content.Context;

import com.androidquery.callback.AjaxStatus;
import com.google.gson.reflect.TypeToken;
import com.lrcall.appcall.models.ReturnInfo;
import com.lrcall.appcall.models.UserBackupInfo;
import com.lrcall.ui.customer.ToastView;
import com.lrcall.utils.CryptoTools;
import com.lrcall.utils.GsonTools;
import com.lrcall.utils.PreferenceUtils;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by libit on 16/4/6.
 */
public class BackupService extends BaseService
{
	public BackupService(Context context)
	{
		super(context);
	}

	/**
	 * 用户获取配置备份
	 *
	 * @param name 备份名称
	 */
	public void getBackupConfig(String name)
	{
		Map<String, Object> params = new HashMap<>();
		params.put("name", name);
		ajaxStringCallback(ApiConfig.GET_BACKUP_CONFIG, params, "正在同步...");
	}

	/**
	 * 用户备份配置
	 *
	 * @param name        备份名称
	 * @param data        备份数据
	 * @param description 描述
	 */
	public void updateBackupConfig(String name, String data, String description)
	{
		Map<String, Object> params = new HashMap<>();
		params.put("name", name);
		params.put("data", data);
		params.put("description", description);
		params.put("signData", CryptoTools.getMD5Str(PreferenceUtils.getInstance().getUsername() + data));
		ajaxStringCallback(ApiConfig.UPDATE_BACKUP_CONFIG, params, "正在备份...");
	}

	/**
	 * 用户备份联系人
	 *
	 * @param content 备份数据
	 */
	public void updateContactBackupInfos(String content)
	{
		Map<String, Object> params = new HashMap<>();
		//		params.put("userId", "libit");
		//		params.put("sessionId", "FE824C53D018ABA0F038762A9673CA382D2CC6F9");
		params.put("content", content);
		ajaxStringCallback(ApiConfig.USER_BACKUP_CONTACTS, params, "正在备份联系人...");
	}

	@Override
	public void parseData(String url, String result, AjaxStatus status)
	{
		if (url.endsWith(ApiConfig.UPDATE_BACKUP_CONFIG) || url.endsWith(ApiConfig.USER_BACKUP_CONTACTS))
		{
			ReturnInfo returnInfo = GsonTools.getReturnInfo(result);
			if (ReturnInfo.isSuccess(returnInfo))
			{
				ToastView.showCenterToast(context, "备份成功！");
			}
			else
			{
				if (returnInfo != null)
				{
					ToastView.showCenterToast(context, "备份失败：" + returnInfo.getErrmsg());
				}
				else
				{
					ToastView.showCenterToast(context, "备份失败：" + result);
				}
			}
		}
		else if (url.endsWith(ApiConfig.GET_BACKUP_CONFIG))
		{
			ReturnInfo returnInfo = GsonTools.getReturnInfo(result);
			UserBackupInfo userBackupInfo = GsonTools.getReturnObjects(returnInfo, UserBackupInfo.class);
			if (ReturnInfo.isSuccess(returnInfo) && userBackupInfo != null)
			{
				//获取成功
				Map map = GsonTools.getObjects(userBackupInfo.getData(), new TypeToken<Map<String, String>>()
				{
				}.getType());
				if (map != null)
				{
					Iterator<String> iterator = map.keySet().iterator();
					while (iterator.hasNext())
					{
						String key = iterator.next();
						String value = (String) map.get(key);
						if (value.equals("true") || value.equals("false"))
						{
							PreferenceUtils.getInstance().setBooleanValue(key, Boolean.valueOf(value));
						}
						else
						{
							PreferenceUtils.getInstance().setStringValue(key, value);
						}
					}
					PreferenceUtils.getInstance().setBooleanValue(PreferenceUtils.IS_FIRST_RUN, false);
					ToastView.showCenterToast(context, "同步成功！");
				}
				else
				{
					ToastView.showCenterToast(context, "用户未备份！");
				}
			}
			else
			{
				// 获取失败
				if (returnInfo != null)
				{
					ToastView.showCenterToast(context, "同步失败：" + returnInfo.getErrmsg());
				}
				else
				{
					ToastView.showCenterToast(context, "同步失败：" + result);
				}
			}
		}
	}
}

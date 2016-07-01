/*
 * Libit保留所有版权，如有疑问联系QQ：308062035
 * Copyright (c) 2016.
 */
package com.lrcall.utils;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import com.lrcall.appcall.models.ErrorInfo;
import com.lrcall.appcall.models.ReturnInfo;

/**
 * Created by libit on 16/5/1.
 */
public class CallTools
{
	//呼叫
	public static ReturnInfo makeCall(Context context, String number)
	{
		if (StringTools.isNull(number))
		{
			return new ReturnInfo(ErrorInfo.getForbiddenErrorId(), "呼叫号码不能为空！");
		}
		try
		{
			Intent i = new Intent(Intent.ACTION_CALL);
			i.setData(Uri.fromParts("tel", number, null));
			PendingIntent.getActivity(context, 0, i, 0).send();
			return new ReturnInfo(ErrorInfo.getSuccessId(), "呼叫号码成功！");
		}
		catch (PendingIntent.CanceledException e)
		{
			e.printStackTrace();
			return new ReturnInfo(ErrorInfo.getForbiddenErrorId(), e.getMessage());
		}
	}
}

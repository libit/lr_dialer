/*
 * Libit保留所有版权，如有疑问联系QQ：308062035
 * Copyright (c) 2016.
 */
package com.lrcall.calllogs;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.BaseColumns;
import android.provider.CallLog;

import com.lrcall.models.CallLogInfo;
import com.lrcall.utils.apptools.AppFactory;

import java.util.List;

/**
 * Created by libit on 16/4/30.
 */
public abstract class CallLogsFactory
{
	public final static Uri CALLLOG_URL = CallLog.Calls.CONTENT_URI;
	public final static String ID = BaseColumns._ID;
	public final static String CACHED_NAME = CallLog.Calls.CACHED_NAME;
	public final static String NUMBER = CallLog.Calls.NUMBER;
	public final static String CACHED_NUMBER_TYPE = CallLog.Calls.CACHED_NUMBER_TYPE;
	public final static String TYPE = CallLog.Calls.TYPE;
	public final static String DATE = CallLog.Calls.DATE;
	public final static String DURATION = CallLog.Calls.DURATION;
	public final static String DEFAULT_SORT_ORDER = CallLog.Calls.DEFAULT_SORT_ORDER;
	public final static String[] logs_projection = {ID, CACHED_NAME, CACHED_NUMBER_TYPE, DATE, NUMBER, TYPE, DURATION};
	private static CallLogsFactory instance;

	protected CallLogsFactory()
	{
	}

	synchronized public static CallLogsFactory getInstance()
	{
		if (instance == null)
		{
			if (AppFactory.isCompatible(19))
			{
				instance = new CallLogsUtils8();
			}
			else if (AppFactory.isCompatible(14))
			{
				instance = new CallLogsUtils8();
			}
			else
			{
				instance = new CallLogsUtils8();
			}
		}
		return instance;
	}

	/**
	 * 获取所有通话记录的Cursor
	 *
	 * @param context
	 * @return
	 */
	public abstract Cursor getCallLogs(Context context);

	/**
	 * 根据条件查询通话记录
	 *
	 * @param context
	 * @param selection 查询条件
	 * @return
	 */
	public abstract Cursor getCallLogs(Context context, String selection);

	/**
	 * 获取指定类型的通话记录
	 *
	 * @param context
	 * @param type    通话类型
	 * @return
	 */
	public abstract Cursor getCallLogsByType(Context context, int type);

	/**
	 * 根据联系人查询通话记录
	 *
	 * @param context
	 * @param contactId 联系人ID
	 * @return
	 */
	public abstract List<CallLogInfo> getCallLogsByContactId(Context context, Long contactId);

	/**
	 * 删除所有通话记录
	 *
	 * @param context
	 */
	public abstract void deleteAllCallLogs(Context context);

	/**
	 * 删除通话记录
	 *
	 * @param context
	 * @param where
	 * @param selectionArgs
	 */
	public abstract void deleteCallLog(Context context, String where, String[] selectionArgs);

	/**
	 * 删除联系人通话记录
	 *
	 * @param context
	 * @param contactId 联系人ID
	 */
	public abstract void deleteContactCallLogs(Context context, Long contactId);

	/**
	 * 删除指定号码的通话记录
	 *
	 * @param context
	 * @param number  号码
	 */
	public abstract void deleteNumberCallLogs(Context context, String number);

	/**
	 * 创建排序的通话记录列表
	 *
	 * @param cursor
	 * @return
	 */
	public abstract List<CallLogInfo> createListSort(Cursor cursor);

	/**
	 * 创建排序的通话记录列表
	 *
	 * @param cursor
	 * @param start
	 * @param callLogCount
	 * @return
	 */
	public abstract List<CallLogInfo> createListSort(Cursor cursor, int start, int callLogCount);
}

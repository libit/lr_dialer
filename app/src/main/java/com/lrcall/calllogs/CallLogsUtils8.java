/*
 * Libit保留所有版权，如有疑问联系QQ：308062035
 * Copyright (c) 2016.
 */
package com.lrcall.calllogs;

import android.content.Context;
import android.database.Cursor;
import android.provider.CallLog;

import com.lrcall.contacts.ContactsFactory;
import com.lrcall.models.CallLogInfo;
import com.lrcall.models.ContactInfo;
import com.lrcall.utils.StringTools;

import java.util.ArrayList;
import java.util.List;

public class CallLogsUtils8 extends CallLogsFactory
{


	/**
	 * 获取所有通话记录的Cursor
	 *
	 * @param context
	 * @return
	 */
	@Override
	public Cursor getCallLogs(Context context)
	{
		//		String number = PreferenceUtils.getInstance().getStringValue(PreferenceUtils.PREF_INCOMING_NUMBER);
		String selection = "";
		//		if (!StringTools.isNull(number))
		//		{
		//			selection = CallLog.Calls.NUMBER + " NOT LIKE '-%' AND " + CallLog.Calls.NUMBER + " != '" + number + "'";
		//		}
		//		else
		//		{
		//			selection = CallLog.Calls.NUMBER + " NOT LIKE '-%' ";
		//		}
		return context.getContentResolver().query(CALLLOG_URL, logs_projection, selection, null, DEFAULT_SORT_ORDER);
	}

	/**
	 * 根据条件查询通话记录
	 *
	 * @param context
	 * @param selection 查询条件
	 * @return
	 */
	@Override
	public Cursor getCallLogs(Context context, String selection)
	{
		return context.getContentResolver().query(CALLLOG_URL, logs_projection, selection, null, DEFAULT_SORT_ORDER);
	}

	/**
	 * 获取指定类型的通话记录
	 *
	 * @param context
	 * @param type    通话类型
	 * @return
	 */
	@Override
	public Cursor getCallLogsByType(Context context, int type)
	{
		String selection = NUMBER + " NOT LIKE '-%' AND " + TYPE + "=" + type;
		return getCallLogs(context, selection);
	}

	@Override
	public List<CallLogInfo> getCallLogsByContactId(Context context, Long contactId)
	{
		ContactInfo contactInfo = ContactsFactory.getInstance().getContactInfoById(context, contactId);
		String selection = "";
		if (contactInfo != null && contactInfo.getPhoneInfoList() != null)
		{
			for (ContactInfo.PhoneInfo phoneInfo : contactInfo.getPhoneInfoList())
			{
				if (!StringTools.isNull(phoneInfo.getNumber()))
				{
					selection += NUMBER + " = '" + phoneInfo.getNumber() + "' OR ";
				}
			}
		}
		selection += " 1 != 1";
		Cursor cursor = context.getContentResolver().query(CALLLOG_URL, logs_projection, selection, null, DEFAULT_SORT_ORDER);
		return createList(cursor);
	}

	/**
	 * 删除所有通话记录
	 *
	 * @param context
	 */
	@Override
	public void deleteAllCallLogs(Context context)
	{
		context.getContentResolver().delete(CALLLOG_URL, null, null);
	}

	/**
	 * 删除通话记录
	 *
	 * @param context
	 * @param where
	 * @param selectionArgs
	 */
	public void deleteCallLog(Context context, String where, String[] selectionArgs)
	{
		context.getContentResolver().delete(CALLLOG_URL, where, selectionArgs);
	}

	/**
	 * 删除联系人通话记录
	 *
	 * @param context
	 * @param contactId 联系人ID
	 */
	@Override
	public void deleteContactCallLogs(Context context, Long contactId)
	{
		ContactInfo contactInfo = ContactsFactory.getInstance().getContactInfoById(context, contactId);
		String selection = "";
		if (contactInfo != null && contactInfo.getPhoneInfoList() != null)
		{
			for (ContactInfo.PhoneInfo phoneInfo : contactInfo.getPhoneInfoList())
			{
				if (!StringTools.isNull(phoneInfo.getNumber()))
				{
					selection += CallLog.Calls.NUMBER + " = '" + phoneInfo.getNumber() + "' OR ";
				}
			}
		}
		selection += " 1 != 1";
		context.getContentResolver().delete(CALLLOG_URL, selection, null);
	}

	/**
	 * 删除指定号码的通话记录
	 *
	 * @param context
	 * @param number  号码
	 */
	@Override
	public void deleteNumberCallLogs(Context context, String number)
	{
		context.getContentResolver().delete(CALLLOG_URL, NUMBER + " = '" + number + "'", null);
	}

	/**
	 * 创建排序的通话记录列表
	 *
	 * @param cursor
	 * @return
	 */
	@Override
	public List<CallLogInfo> createListSort(Cursor cursor)
	{
		List<CallLogInfo> callLogInfoList = new ArrayList<>();
		List<String> data = new ArrayList<>();
		if (cursor != null && cursor.moveToFirst())
		{
			do
			{
				String number = cursor.getString(cursor.getColumnIndex(NUMBER)).trim();
				if (data.contains(number))
				{
					for (CallLogInfo callLogInfo : callLogInfoList)
					{
						if (callLogInfo.getNumber().trim().indexOf(number) > -1)
						{
							String cache_name = callLogInfo.getName();
							if ((cache_name.indexOf("(") > -1) && (cache_name.indexOf(")") > -1) && (cache_name.lastIndexOf("(")) < (cache_name.lastIndexOf(")")))
							{
								String strCount = cache_name.substring(cache_name.lastIndexOf("(") + 1, cache_name.lastIndexOf(")"));
								try
								{
									int count = Integer.parseInt(strCount);
									count++;
									cache_name = cache_name.substring(0, cache_name.lastIndexOf("(")) + "(" + count + ")";
									callLogInfo.setName(cache_name);
								}
								catch (Exception e)
								{
									callLogInfo.setName(cache_name + "(2)");
								}
							}
							else
							{
								callLogInfo.setName(cache_name + "(2)");
							}
							break;
						}
					}
					continue;
				}
				else
				{
					data.add(number);
				}
				CallLogInfo callLogInfo = new CallLogInfo();
				String cache_name = cursor.getString(cursor.getColumnIndex(CACHED_NAME));
				if (StringTools.isNull(cache_name))
				{
					cache_name = number;
				}
				callLogInfo.setName(cache_name);
				callLogInfo.setNumber(number);
				callLogInfo.setType(cursor.getInt(cursor.getColumnIndex(TYPE)));
				callLogInfo.setDate(cursor.getLong(cursor.getColumnIndex(DATE)));
				callLogInfo.setDuration(cursor.getLong(cursor.getColumnIndex(DURATION)));
				callLogInfoList.add(callLogInfo);
			}
			while (cursor.moveToNext());
		}
		return callLogInfoList;
	}

	/**
	 * 创建排序的通话记录列表
	 *
	 * @param cursor
	 * @param start
	 * @param callLogCount
	 * @return
	 */
	@Override
	public List<CallLogInfo> createListSort(Cursor cursor, int start, int callLogCount)
	{
		List<CallLogInfo> callLogInfoList = new ArrayList<>();
		List<String> data = new ArrayList<>();
		if (cursor != null && cursor.moveToFirst())
		{
			int index = 0;
			int getCount = 0;
			do
			{
				index++;
				if (index < start)
				{
					continue;
				}
				String number = cursor.getString(cursor.getColumnIndex(NUMBER)).trim();
				if (data.contains(number))
				{
					for (CallLogInfo callLogInfo : callLogInfoList)
					{
						if (callLogInfo.getNumber().trim().indexOf(number) > -1)
						{
							String cache_name = callLogInfo.getName();
							if ((cache_name.indexOf("(") > -1) && (cache_name.indexOf(")") > -1) && (cache_name.lastIndexOf("(")) < (cache_name.lastIndexOf(")")))
							{
								String strCount = cache_name.substring(cache_name.lastIndexOf("(") + 1, cache_name.lastIndexOf(")"));
								try
								{
									int count = Integer.parseInt(strCount);
									count++;
									cache_name = cache_name.substring(0, cache_name.lastIndexOf("(")) + "(" + count + ")";
									callLogInfo.setName(cache_name);
								}
								catch (Exception e)
								{
									callLogInfo.setName(cache_name + "(2)");
								}
							}
							else
							{
								callLogInfo.setName(cache_name + "(2)");
							}
							break;
						}
					}
					continue;
				}
				else
				{
					data.add(number);
				}
				CallLogInfo callLogInfo = new CallLogInfo();
				String cache_name = cursor.getString(cursor.getColumnIndex(CACHED_NAME));
				if (StringTools.isNull(cache_name))
				{
					cache_name = number;
				}
				callLogInfo.setName(cache_name);
				callLogInfo.setNumber(number);
				callLogInfo.setType(cursor.getInt(cursor.getColumnIndex(TYPE)));
				callLogInfo.setDate(cursor.getLong(cursor.getColumnIndex(DATE)));
				callLogInfo.setDuration(cursor.getLong(cursor.getColumnIndex(DURATION)));
				callLogInfoList.add(callLogInfo);
				getCount++;
			}
			while (cursor.moveToNext() && getCount < (callLogCount));
		}
		return callLogInfoList;
	}

	private static List<CallLogInfo> createList(Cursor cursor)
	{
		List<CallLogInfo> callLogInfoList = new ArrayList<>();
		if (cursor != null && cursor.moveToFirst())
		{
			do
			{
				String number = cursor.getString(cursor.getColumnIndex(NUMBER));
				String cache_name = cursor.getString(cursor.getColumnIndex(CACHED_NAME));
				if (StringTools.isNull(cache_name))
				{
					cache_name = number;
				}
				else
				{
					cache_name += "(" + number + ")";
				}
				CallLogInfo callLogInfo = new CallLogInfo();
				callLogInfo.setName(cache_name);
				callLogInfo.setNumber(number);
				callLogInfo.setType(cursor.getInt(cursor.getColumnIndex(TYPE)));
				callLogInfo.setDate(cursor.getLong(cursor.getColumnIndex(DATE)));
				callLogInfo.setDuration(cursor.getLong(cursor.getColumnIndex(DURATION)));
				callLogInfoList.add(callLogInfo);
			}
			while (cursor.moveToNext());
		}
		return callLogInfoList;
	}
}

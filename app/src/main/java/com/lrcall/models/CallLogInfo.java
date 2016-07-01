/*
 * Libit保留所有版权，如有疑问联系QQ：308062035
 * Copyright (c) 2016.
 */
package com.lrcall.models;

import android.provider.CallLog;
import android.text.format.DateUtils;

import com.lrcall.appcall.MyApplication;
import com.lrcall.appcall.R;

public class CallLogInfo
{
	private String id;//ID
	private String name;//姓名
	private String number;//号码
	private int type;//类型：来电、去电、未接...
	private long date;//通话时间
	private long duration;//通话时长
	private String location;//号码归属地
	private String remark;//备注

	public CallLogInfo()
	{
	}

	public CallLogInfo(String id, String name, String number, int type, long date, long duration, String location, String remark)
	{
		this.id = id;
		this.name = name;
		this.number = number;
		this.type = type;
		this.date = date;
		this.duration = duration;
		this.location = location;
		this.remark = remark;
	}

	//获取来去电图标
	public static int getTypeRes(int type)
	{
		Integer typeRes = null;
		switch (type)
		{
			case CallLog.Calls.INCOMING_TYPE:
			{
				typeRes = R.drawable.incall;
				break;
			}
			case CallLog.Calls.OUTGOING_TYPE:
			{
				typeRes = R.drawable.tocall;
				break;
			}
			case CallLog.Calls.MISSED_TYPE:
			{
				typeRes = R.drawable.incall;
				break;
			}
			default:
			{
				typeRes = R.drawable.incall;
				break;
			}
		}
		return typeRes;
	}

	//获取来去电文字
	public static String getTypeString(int type)
	{
		String strType = null;
		switch (type)
		{
			case CallLog.Calls.INCOMING_TYPE:
			{
				strType = MyApplication.getContext().getString(R.string.text_incoming_call);
				break;
			}
			case CallLog.Calls.OUTGOING_TYPE:
			{
				strType = MyApplication.getContext().getString(R.string.text_outgoing_call);
				break;
			}
			case CallLog.Calls.MISSED_TYPE:
			{
				strType = MyApplication.getContext().getString(R.string.text_missed_call);
				break;
			}
			default:
			{
				strType = MyApplication.getContext().getString(R.string.text_missed_call);
				break;
			}
		}
		return strType;
	}

	//获取相对时间
	public static String getCustomerDate(long date)
	{
		return DateUtils.getRelativeTimeSpanString(date).toString();
	}

	//通话时长转换为分秒制
	public static String getDurationString(long duration)
	{
		String strDuration = "";
		if (duration > 60 * 60)
		{
			strDuration = MyApplication.getContext().getString(R.string.text_time_hour, duration / (60 * 60), (duration % (60 * 60)) / 60, (duration % (60 * 60)) % 60);
		}
		else if (duration > 60)
		{
			strDuration = MyApplication.getContext().getString(R.string.text_time_minute, duration / 60, duration % 60);
		}
		else if (duration > 0)
		{
			strDuration = MyApplication.getContext().getString(R.string.text_time_second, duration);
		}
		else
		{
			//			if (strType.equals(MyApplication.getContext().getString(R.string.text_missed_call)))
			//			{
			//				strDuration = strType;
			//			}
			//			else
			{
				strDuration = MyApplication.getContext().getString(R.string.text_time_no_second);
			}
		}
		return strDuration;
	}

	public String getId()
	{
		return id;
	}

	public void setId(String id)
	{
		this.id = id;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public String getNumber()
	{
		return number;
	}

	public void setNumber(String number)
	{
		this.number = number;
	}

	public int getType()
	{
		return type;
	}

	public void setType(int type)
	{
		this.type = type;
	}

	public long getDate()
	{
		return date;
	}

	public void setDate(long date)
	{
		this.date = date;
	}

	public long getDuration()
	{
		return duration;
	}

	public void setDuration(long duration)
	{
		this.duration = duration;
	}

	public String getLocation()
	{
		return location;
	}

	public void setLocation(String location)
	{
		this.location = location;
	}

	public String getRemark()
	{
		return remark;
	}

	public void setRemark(String remark)
	{
		this.remark = remark;
	}
}

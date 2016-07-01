/*
 * Libit保留所有版权，如有疑问联系QQ：308062035
 * Copyright (c) 2016.
 */

package com.lrcall.appcall.models;

import com.lrcall.utils.GsonTools;

public class ReturnInfo
{
	private Integer errcode;
	private String errmsg;

	public ReturnInfo()
	{
		super();
	}

	public ReturnInfo(Integer errcode, String errmsg)
	{
		super();
		this.errcode = errcode;
		this.errmsg = errmsg;
	}

	public static boolean isSuccess(ReturnInfo info)
	{
		if (info != null && info.getErrcode() != null && info.getErrcode().intValue() == 0)
		{
			return true;
		}
		else
		{
			return false;
		}
	}

	public Integer getErrcode()
	{
		return errcode;
	}

	public void setErrcode(Integer errcode)
	{
		this.errcode = errcode;
	}

	public String getErrmsg()
	{
		return errmsg;
	}

	public void setErrmsg(String errmsg)
	{
		this.errmsg = errmsg;
	}

	@Override
	public String toString()
	{
		return GsonTools.toJson(this);
	}
}

/*
 * Libit保留所有版权，如有疑问联系QQ：308062035
 * Copyright (c) 2016.
 */
package com.lrcall.enums;

/**
 * Created by libit on 16/6/30.
 */
public enum EventTypeLayoutSideMain
{
	CLOSE_DRAWER("close_drawer");
	private String type;

	EventTypeLayoutSideMain(String type)
	{
		this.type = type;
	}

	public String getType()
	{
		return type;
	}
}

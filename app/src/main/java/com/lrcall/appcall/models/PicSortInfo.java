/*
 * Libit保留所有版权，如有疑问联系QQ：308062035
 * Copyright (c) 2016.
 */
package com.lrcall.appcall.models;

/**
 * Created by libit on 16/4/13.
 */
public class PicSortInfo
{
	private Integer id;
	private String sortId;
	private String name;

	public PicSortInfo()
	{
	}

	public PicSortInfo(String sortId, String name)
	{
		this.sortId = sortId;
		this.name = name;
	}

	public Integer getId()
	{
		return id;
	}

	public void setId(Integer id)
	{
		this.id = id;
	}

	public String getSortId()
	{
		return sortId;
	}

	public void setSortId(String sortId)
	{
		this.sortId = sortId;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}
}

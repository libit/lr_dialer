/*
 * Libit保留所有版权，如有疑问联系QQ：308062035
 * Copyright (c) 2016.
 */
package com.lrcall.appcall.models;

public class UserBackupInfo
{
	private Integer id;
	private String userId;
	private String dataType;
	private String name;
	private String data;
	private String description;
	private String signData;
	private long addDateLong;

	public UserBackupInfo()
	{
	}

	public UserBackupInfo(Integer id, String userId, String dataType, String name, String data, String description, String signData, long addDateLong)
	{
		this.id = id;
		this.userId = userId;
		this.dataType = dataType;
		this.name = name;
		this.data = data;
		this.description = description;
		this.signData = signData;
		this.addDateLong = addDateLong;
	}

	public Integer getId()
	{
		return id;
	}

	public void setId(Integer id)
	{
		this.id = id;
	}

	public String getUserId()
	{
		return userId;
	}

	public void setUserId(String userId)
	{
		this.userId = userId;
	}

	public String getDataType()
	{
		return dataType;
	}

	public void setDataType(String dataType)
	{
		this.dataType = dataType;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public String getData()
	{
		return data;
	}

	public void setData(String data)
	{
		this.data = data;
	}

	public String getDescription()
	{
		return description;
	}

	public void setDescription(String description)
	{
		this.description = description;
	}

	public String getSignData()
	{
		return signData;
	}

	public void setSignData(String signData)
	{
		this.signData = signData;
	}

	public long getAddDateLong()
	{
		return addDateLong;
	}

	public void setAddDateLong(long addDateLong)
	{
		this.addDateLong = addDateLong;
	}
}

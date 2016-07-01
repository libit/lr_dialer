/*
 * Libit保留所有版权，如有疑问联系QQ：308062035
 * Copyright (c) 2016.
 */
package com.lrcall.appcall.models;

public class UserInfo
{
	private String userId;
	private String sessionId;
	private String number;
	private String name;
	private String nickname;
	private byte sex;
	private String picId;
	private Long birthday;
	private String country;
	private String province;
	private String city;
	private String address;
	private String subscribe;
	private Long subscribeTime;
	private String language;
	private String remark;
	private PicInfo picInfo;

	public UserInfo()
	{
	}

	public UserInfo(String userId, String sessionId, String number, String name, String nickname, byte sex, String picId, Long birthday, String country, String province, String city, String address, String subscribe, Long subscribeTime, String language, String remark, PicInfo picInfo)
	{
		this.userId = userId;
		this.sessionId = sessionId;
		this.number = number;
		this.name = name;
		this.nickname = nickname;
		this.sex = sex;
		this.picId = picId;
		this.birthday = birthday;
		this.country = country;
		this.province = province;
		this.city = city;
		this.address = address;
		this.subscribe = subscribe;
		this.subscribeTime = subscribeTime;
		this.language = language;
		this.remark = remark;
		this.picInfo = picInfo;
	}

	public String getUserId()
	{
		return userId;
	}

	public void setUserId(String userId)
	{
		this.userId = userId;
	}

	public String getSessionId()
	{
		return sessionId;
	}

	public void setSessionId(String sessionId)
	{
		this.sessionId = sessionId;
	}

	public String getNumber()
	{
		return number;
	}

	public void setNumber(String number)
	{
		this.number = number;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public String getNickname()
	{
		return nickname;
	}

	public void setNickname(String nickname)
	{
		this.nickname = nickname;
	}

	public byte getSex()
	{
		return sex;
	}

	public void setSex(byte sex)
	{
		this.sex = sex;
	}

	public String getPicId()
	{
		return picId;
	}

	public void setPicId(String picId)
	{
		this.picId = picId;
	}

	public Long getBirthday()
	{
		return birthday;
	}

	public void setBirthday(Long birthday)
	{
		this.birthday = birthday;
	}

	public String getCountry()
	{
		return country;
	}

	public void setCountry(String country)
	{
		this.country = country;
	}

	public String getProvince()
	{
		return province;
	}

	public void setProvince(String province)
	{
		this.province = province;
	}

	public String getCity()
	{
		return city;
	}

	public void setCity(String city)
	{
		this.city = city;
	}

	public String getAddress()
	{
		return address;
	}

	public void setAddress(String address)
	{
		this.address = address;
	}

	public String getSubscribe()
	{
		return subscribe;
	}

	public void setSubscribe(String subscribe)
	{
		this.subscribe = subscribe;
	}

	public Long getSubscribeTime()
	{
		return subscribeTime;
	}

	public void setSubscribeTime(Long subscribeTime)
	{
		this.subscribeTime = subscribeTime;
	}

	public String getLanguage()
	{
		return language;
	}

	public void setLanguage(String language)
	{
		this.language = language;
	}

	public String getRemark()
	{
		return remark;
	}

	public void setRemark(String remark)
	{
		this.remark = remark;
	}

	public PicInfo getPicInfo()
	{
		return picInfo;
	}

	public void setPicInfo(PicInfo picInfo)
	{
		this.picInfo = picInfo;
	}
}

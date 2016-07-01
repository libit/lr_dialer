/*
 * Libit保留所有版权，如有疑问联系QQ：308062035
 * Copyright (c) 2016.
 */

package com.lrcall.models;

import android.graphics.Bitmap;
import android.provider.ContactsContract;

import com.lrcall.appcall.MyApplication;

import java.util.List;

/**
 * Created by libit on 15/8/9.
 */
public class ContactInfo
{
	// 联系人属性
	private Long contactId;// 联系人ID
	private String name;// 联系人姓名
	private String py;// 联系人拼音首字母，用于索引
	private boolean starred;// 联系人是否已收藏
	private Bitmap contactPhoto;// 联系人图片
	private Long photoId;// 联系人图片ID
	private List<PhoneInfo> phoneInfoList;// 联系人号码列表

	public ContactInfo()
	{
		contactId = null;
		name = "";
		py = "";
		starred = false;
		contactPhoto = null;
		photoId = 0L;
		phoneInfoList = null;
	}

	public Long getContactId()
	{
		return contactId;
	}

	public void setContactId(Long contactId)
	{
		this.contactId = contactId;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public String getPy()
	{
		return py;
	}

	public void setPy(String py)
	{
		this.py = py;
	}

	public boolean isStarred()
	{
		return starred;
	}

	public void setStarred(boolean starred)
	{
		this.starred = starred;
	}

	public Bitmap getContactPhoto()
	{
		return contactPhoto;
	}

	public void setContactPhoto(Bitmap contactPhoto)
	{
		this.contactPhoto = contactPhoto;
	}

	public Long getPhotoId()
	{
		return photoId;
	}

	public void setPhotoId(Long photoId)
	{
		this.photoId = photoId;
	}

	public List<PhoneInfo> getPhoneInfoList()
	{
		return phoneInfoList;
	}

	public void setPhoneInfoList(List<PhoneInfo> phoneInfoList)
	{
		this.phoneInfoList = phoneInfoList;
	}

	/**
	 * 号码信息
	 */
	public class PhoneInfo
	{
		private String number;// 号码
		private String type;// 类型

		public PhoneInfo(String n, String t)
		{
			this.number = n;
			this.type = t;
		}

		public String getNumber()
		{
			return number;
		}

		public void setNumber(String number)
		{
			this.number = number;
		}

		public String getType()
		{
			return switchType(Integer.parseInt(type));
		}

		public void setType(String type)
		{
			this.type = type;
		}

		private String switchType(int type)
		{
			return MyApplication.getContext().getString(ContactsContract.CommonDataKinds.Phone.getTypeLabelResource(type));
		}
	}
}
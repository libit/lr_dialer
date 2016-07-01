/*
 * Libit保留所有版权，如有疑问联系QQ：308062035
 * Copyright (c) 2016.
 */
package com.lrcall.models;

import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by libit on 16/5/8.
 */
public class TabInfo
{
	private String label;
	private int imgResId;
	private Class loadClass;
	private ImageView imgIcon;
	private TextView tvLabel;

	public TabInfo()
	{
	}

	public TabInfo(String label, int imgResId, Class loadClass)
	{
		this.label = label;
		this.imgResId = imgResId;
		this.loadClass = loadClass;
	}

	public String getLabel()
	{
		return label;
	}

	public void setLabel(String label)
	{
		this.label = label;
	}

	public int getImgResId()
	{
		return imgResId;
	}

	public void setImgResId(int imgResId)
	{
		this.imgResId = imgResId;
	}

	public Class getLoadClass()
	{
		return loadClass;
	}

	public void setLoadClass(Class loadClass)
	{
		this.loadClass = loadClass;
	}

	public ImageView getImgIcon()
	{
		return imgIcon;
	}

	public void setImgIcon(ImageView imgIcon)
	{
		this.imgIcon = imgIcon;
	}

	public TextView getTvLabel()
	{
		return tvLabel;
	}

	public void setTvLabel(TextView tvLabel)
	{
		this.tvLabel = tvLabel;
	}
}

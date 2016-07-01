/*
 * Libit保留所有版权，如有疑问联系QQ：308062035
 * Copyright (c) 2016.
 */
package com.lrcall.ui.customer;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.lrcall.appcall.R;

import java.util.ArrayList;
import java.util.Collection;

public class Numpad extends LinearLayout implements AddressAware
{
	private boolean mPlayDtmf;

	public Numpad(Context context, boolean playDtmf)
	{
		super(context);
		mPlayDtmf = playDtmf;
		LayoutInflater.from(context).inflate(R.layout.layout_numpad, this);
		setLongClickable(true);
		onFinishInflate();
	}

	public Numpad(Context context, AttributeSet attrs)
	{
		super(context, attrs);
		TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.Numpad);
		mPlayDtmf = 1 == a.getInt(R.styleable.Numpad_play_dtmf, 1);
		a.recycle();
		LayoutInflater.from(context).inflate(R.layout.layout_numpad, this);
		setLongClickable(true);
	}

	public void setPlayDtmf(boolean sendDtmf)
	{
		this.mPlayDtmf = sendDtmf;
	}

	@Override
	protected final void onFinishInflate()
	{
		super.onFinishInflate();
	}

	public void setAddressWidget(AddressText address)
	{
		for (AddressAware v : retrieveChildren(this, AddressAware.class))
		{
			v.setAddressWidget(address);
		}
	}

	private final <T> Collection<T> retrieveChildren(ViewGroup viewGroup, Class<T> clazz)
	{
		final Collection<T> views = new ArrayList<T>();
		for (int i = 0; i < viewGroup.getChildCount(); i++)
		{
			View v = viewGroup.getChildAt(i);
			if (v instanceof ViewGroup)
			{
				views.addAll(retrieveChildren((ViewGroup) v, clazz));
			}
			else
			{
				if (clazz.isInstance(v))
					views.add(clazz.cast(v));
			}
		}
		return views;
	}
}

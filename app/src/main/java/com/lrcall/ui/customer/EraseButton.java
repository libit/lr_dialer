/*
 * Libit保留所有版权，如有疑问联系QQ：308062035
 * Copyright (c) 2016.
 */
package com.lrcall.ui.customer;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.widget.ImageView;

public class EraseButton extends ImageView implements AddressAware, OnClickListener, OnLongClickListener
{
	private AddressText address;

	public EraseButton(Context context, AttributeSet attrs)
	{
		super(context, attrs);
		setOnClickListener(this);
		setOnLongClickListener(this);
	}

	public void onClick(View v)
	{
		if (address.getText().length() > 0)
		{
			int lBegin = address.getSelectionStart();
			if (lBegin == -1)
			{
				lBegin = address.getEditableText().length() - 1;
			}
			if (lBegin > 0)
			{
				address.getEditableText().delete(lBegin - 1, lBegin);
			}
		}
	}

	public boolean onLongClick(View v)
	{
		address.getEditableText().clear();
		// address.setText("");
		return true;
	}

	public void setAddressWidget(AddressText view)
	{
		address = view;
	}
}

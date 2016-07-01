/*
 * Libit保留所有版权，如有疑问联系QQ：308062035
 * Copyright (c) 2016.
 */
package com.lrcall.ui.customer;

import android.content.Context;
import android.text.InputType;
import android.util.AttributeSet;
import android.widget.EditText;

import com.lrcall.utils.StringTools;
import com.lrcall.utils.apptools.AppFactory;

import java.lang.reflect.Method;

public class AddressText extends EditText
{
	private InputNumberChangedListener inputNumberChangedListener;

	public AddressText(Context context, AttributeSet attrs)
	{
		super(context, attrs);
		if (!AppFactory.isCompatible(11))
		{
			setInputType(InputType.TYPE_NULL);
		}
		else
		{
			try
			{
				Class<EditText> cls = EditText.class;
				Method setShowSoftInputOnFocus = cls.getMethod("setShowSoftInputOnFocus", boolean.class);
				setShowSoftInputOnFocus.setAccessible(true);
				setShowSoftInputOnFocus.invoke(this, false);
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}
		setCursorVisible(false);
	}

	public void setInputNumberChangedListener(InputNumberChangedListener l)
	{
		inputNumberChangedListener = l;
	}

	@Override
	protected void onTextChanged(CharSequence text, int start, int before, int after)
	{
		if (inputNumberChangedListener != null)
		{
			inputNumberChangedListener.onInputNumberChanged(this.getText().toString());
		}
		if (StringTools.isNull(text.toString()))
		{
			setCursorVisible(false);
		}
		else
		{
			setCursorVisible(true);
		}
		super.onTextChanged(text, start, before, after);
	}

	public interface InputNumberChangedListener
	{
		void onInputNumberChanged(String txt);
	}
}

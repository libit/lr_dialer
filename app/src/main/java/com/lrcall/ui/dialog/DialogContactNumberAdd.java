/*
 * Libit保留所有版权，如有疑问联系QQ：308062035
 * Copyright (c) 2016.
 */
package com.lrcall.ui.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.EditText;

import com.lrcall.appcall.R;

public class DialogContactNumberAdd extends Dialog implements OnClickListener
{
	private EditText etNumber, etType;
	private OnContactNumberAddListenser listenser;

	public DialogContactNumberAdd(Context context, OnContactNumberAddListenser listenser)
	{
		super(context, R.style.MyDialog);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setCancelable(false);
		this.listenser = listenser;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dialog_contact_number_add);
		initView();
	}

	private void initView()
	{
		etNumber = (EditText) findViewById(R.id.et_number);
		etType = (EditText) findViewById(R.id.et_type);
		findViewById(R.id.dialog_btn_ok).setOnClickListener(this);
		findViewById(R.id.dialog_btn_cancel).setOnClickListener(this);
	}

	@Override
	public void onClick(View v)
	{
		switch (v.getId())
		{
			case R.id.dialog_btn_ok:
			{
				if (listenser != null)
				{
					listenser.onOkClick(etType.getText().toString(), etNumber.getText().toString());
				}
				dismiss();
				break;
			}
			case R.id.dialog_btn_cancel:
			{
				if (listenser != null)
				{
					listenser.onCancelClick();
				}
				dismiss();
				break;
			}
		}
	}

	public interface OnContactNumberAddListenser
	{
		void onOkClick(String type, String number);

		void onCancelClick();
	}
}

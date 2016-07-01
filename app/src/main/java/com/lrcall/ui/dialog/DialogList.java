/*
 * Libit保留所有版权，如有疑问联系QQ：308062035
 * Copyright (c) 2016.
 */
package com.lrcall.ui.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Window;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.lrcall.appcall.R;

public class DialogList extends Dialog
{
	private ListView listView;
	private ListAdapter adapter;

	public DialogList(Context context, ListAdapter adapter)
	{
		super(context, R.style.MyDialog);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
//		setCancelable(false);
		this.adapter = adapter;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dialog_list);
		initView();
	}

	private void initView()
	{
		listView = (ListView) findViewById(R.id.list_view);
		listView.setAdapter(adapter);
	}
}

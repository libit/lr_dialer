/*
 * Libit保留所有版权，如有疑问联系QQ：308062035
 * Copyright (c) 2016.
 */
package com.lrcall.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lrcall.appcall.R;

public class FragmentSms extends Fragment
{
	private static final String TAG = FragmentSms.class.getSimpleName();

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		// Inflate the layout for this fragment
		View rootView = inflater.inflate(R.layout.fragment_sms, container, false);
		initView(rootView);
		return rootView;
	}

	protected void initView(View rootView)
	{
	}
}

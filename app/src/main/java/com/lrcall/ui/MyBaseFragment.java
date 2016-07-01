/*
 * Libit保留所有版权，如有疑问联系QQ：308062035
 * Copyright (c) 2016.
 */
package com.lrcall.ui;

import android.support.v4.app.Fragment;
import android.view.View;

import com.lrcall.utils.LogcatTools;

/**
 * Created by libit on 16/4/27.
 */
public class MyBaseFragment extends Fragment
{
	private static final String TAG = MyBaseFragment.class.getSimpleName();
	protected boolean isInit = false;//视图是否已经初始化

	protected void viewInit(View rootView)
	{
		isInit = true;
	}

	@Override
	public void setUserVisibleHint(boolean isVisibleToUser)
	{
		super.setUserVisibleHint(isVisibleToUser);
		LogcatTools.debug(TAG, "fragment setUserVisibleHint:" + isVisibleToUser);
		if (isInit)
		{
			if (isVisibleToUser)
			{
				fragmentShow();
			}
			else
			{
				fragmentHide();
			}
		}
	}

	/**
	 * 当fragment隐藏时调用
	 * 注意：第一次调用时必须等待视图已经初始化完成。
	 */
	public void fragmentHide()
	{
	}

	/**
	 * 当fragment显示时调用
	 * 注意：第一次调用时必须等待视图已经初始化完成。
	 */
	public void fragmentShow()
	{
	}
}

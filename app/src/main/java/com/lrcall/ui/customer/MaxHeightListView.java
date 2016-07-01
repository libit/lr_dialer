/*
 * Libit保留所有版权，如有疑问联系QQ：308062035
 * Copyright (c) 2016.
 */
package com.lrcall.ui.customer;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;

/**
 * Created by libit on 16/5/7.
 */
public class MaxHeightListView extends ListView
{
	public MaxHeightListView(Context context)
	{
		super(context);
	}

	public MaxHeightListView(Context context, AttributeSet attrs)
	{
		super(context, attrs);
	}

	public MaxHeightListView(Context context, AttributeSet attrs, int defStyle)
	{
		super(context, attrs, defStyle);
	}

	//解决和ScrollView显示的问题
	@Override
	public void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
	{
		int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);
		super.onMeasure(widthMeasureSpec, expandSpec);
	}
}

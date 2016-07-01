/*
 * Libit保留所有版权，如有疑问联系QQ：308062035
 * Copyright (c) 2016.
 */
package com.lrcall.ui.adapter;

import android.content.Context;
import android.widget.BaseAdapter;

import com.lrcall.models.CallLogInfo;

import java.util.List;

/**
 * Created by libit on 16/4/30.
 */
public abstract class BaseCallLogsAdapter extends BaseAdapter
{
	public interface IBaseCallLogsAdapterItemClicked
	{
		void onItemClicked(CallLogInfo callLogInfo);
	}

	protected Context context;
	protected List<CallLogInfo> list;

	public BaseCallLogsAdapter(Context context, List<CallLogInfo> list)
	{
		this.context = context;
		this.list = list;
	}

	@Override
	public int getCount()
	{
		return list != null ? list.size() : 0;
	}

	@Override
	public Object getItem(int position)
	{
		return list != null ? list.get(position) : null;
	}

	@Override
	public long getItemId(int position)
	{
		return position;
	}
}

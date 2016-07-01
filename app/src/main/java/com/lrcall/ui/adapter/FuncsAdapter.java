/*
 * Libit保留所有版权，如有疑问联系QQ：308062035
 * Copyright (c) 2016.
 */
package com.lrcall.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.lrcall.appcall.R;
import com.lrcall.models.FuncInfo;

import java.util.List;

/**
 * Created by libit on 16/4/30.
 */
public class FuncsAdapter extends BaseAdapter
{
	public interface IFuncsAdapterItemClicked
	{
		void onFuncClicked(FuncInfo funcInfo);
	}

	private Context context;
	private List<FuncInfo> list;
	private IFuncsAdapterItemClicked funcsAdapterItemClicked;

	public FuncsAdapter(Context context, List<FuncInfo> list, IFuncsAdapterItemClicked funcsAdapterItemClicked)
	{
		this.context = context;
		this.list = list;
		this.funcsAdapterItemClicked = funcsAdapterItemClicked;
	}

	@Override
	public int getCount()
	{
		return list.size();
	}

	@Override
	public Object getItem(int position)
	{
		return list.get(position);
	}

	@Override
	public long getItemId(int position)
	{
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{
		convertView = LayoutInflater.from(context).inflate(R.layout.item_func, null);
		ImageView ivHead = (ImageView) convertView.findViewById(R.id.iv_head);
		TextView tvName = (TextView) convertView.findViewById(R.id.tv_label);
		final FuncInfo funcInfo = list.get(position);
		ivHead.setImageResource(funcInfo.getImgRes());
		tvName.setText(funcInfo.getLabel());
		convertView.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				if (funcsAdapterItemClicked != null)
				{
					funcsAdapterItemClicked.onFuncClicked(funcInfo);
				}
			}
		});
		return convertView;
	}
}

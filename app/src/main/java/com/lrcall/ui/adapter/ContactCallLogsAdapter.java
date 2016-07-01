/*
 * Libit保留所有版权，如有疑问联系QQ：308062035
 * Copyright (c) 2016.
 */
package com.lrcall.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.lrcall.appcall.R;
import com.lrcall.models.CallLogInfo;

import java.util.List;

/**
 * Created by libit on 16/4/30.
 */
public class ContactCallLogsAdapter extends CallLogsAdapter
{
	public ContactCallLogsAdapter(Context context, List<CallLogInfo> list, ICallLogsAdapterItemClicked callLogsAdapterItemClicked)
	{
		super(context, list, callLogsAdapterItemClicked);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{
		convertView = LayoutInflater.from(context).inflate(R.layout.item_contact_call_log, null);
		ImageView ivCallLogType = (ImageView) convertView.findViewById(R.id.call_log_type_icon);
		TextView tvNumber = (TextView) convertView.findViewById(R.id.call_log_number);
		TextView tvDuration = (TextView) convertView.findViewById(R.id.call_log_duration);
		TextView tvTime = (TextView) convertView.findViewById(R.id.call_log_date);
		final CallLogInfo callLogInfo = list.get(position);
		ivCallLogType.setImageResource(CallLogInfo.getTypeRes(callLogInfo.getType()));
		tvNumber.setText(callLogInfo.getNumber());
		tvDuration.setText(CallLogInfo.getDurationString(callLogInfo.getDuration()));
		tvTime.setText(CallLogInfo.getCustomerDate(callLogInfo.getDate()));
		convertView.findViewById(R.id.v_call).setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				if (callLogsAdapterItemClicked != null)
				{
					callLogsAdapterItemClicked.onCallClicked(callLogInfo);
				}
			}
		});
		convertView.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				if (callLogsAdapterItemClicked != null)
				{
					callLogsAdapterItemClicked.onItemClicked(callLogInfo);
				}
			}
		});
		return convertView;
	}
}

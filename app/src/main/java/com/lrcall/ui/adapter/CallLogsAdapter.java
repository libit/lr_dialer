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
import com.lrcall.utils.StringTools;

import java.util.List;

/**
 * Created by libit on 16/4/30.
 */
public class CallLogsAdapter extends BaseCallLogsAdapter
{
	public interface ICallLogsAdapterItemClicked extends IBaseCallLogsAdapterItemClicked
	{
		void onCallClicked(CallLogInfo callLogInfo);
	}

	protected ICallLogsAdapterItemClicked callLogsAdapterItemClicked;

	public CallLogsAdapter(Context context, List<CallLogInfo> list, ICallLogsAdapterItemClicked callLogsAdapterItemClicked)
	{
		super(context, list);
		this.callLogsAdapterItemClicked = callLogsAdapterItemClicked;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{
		convertView = LayoutInflater.from(context).inflate(R.layout.item_call_log, null);
		ImageView ivCallLogType = (ImageView) convertView.findViewById(R.id.call_log_type_icon);
		TextView tvName = (TextView) convertView.findViewById(R.id.call_log_name);
		TextView tvNumber = (TextView) convertView.findViewById(R.id.call_log_number);
		TextView tvLocal = (TextView) convertView.findViewById(R.id.tv_local);
		TextView tvDuration = (TextView) convertView.findViewById(R.id.call_log_duration);
		TextView tvTime = (TextView) convertView.findViewById(R.id.call_log_date);
		final CallLogInfo callLogInfo = list.get(position);
		ivCallLogType.setImageResource(CallLogInfo.getTypeRes(callLogInfo.getType()));
		tvName.setText(callLogInfo.getName());
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
		String areacode = StringTools.convertToCallPhoneNumber(callLogInfo.getNumber());
		//			DBAdapter db = new DBAdapter(ActivityDialer.this);
		//			db.open();
		//			String local = "";
		//			if (StringTools.isChinaMobilePhoneNumber(areacode))
		//			{
		//				areacode = areacode.substring(0, 7);
		//				local = db.getLocal(areacode);
		//			}
		//			else
		//			{
		//				if (areacode.startsWith("0") && areacode.length() > 8)
		//				{
		//					areacode = areacode.substring(0, 4);
		//					local = db.getLocal(areacode);
		//					if (StringTools.isNull(local))
		//					{
		//						areacode = areacode.substring(0, 3);
		//						local = db.getLocal(areacode);
		//					}
		//				}
		//			}
		//
		//			db.close();
		//			if (!StringTools.isNull(local))
		//			{
		//				holder.tvLocal.setText(local.replace(" ", ""));
		//			}
		//			else
		//			{
		//				new LoadLocal(holder.tvLocal, number).execute();
		//			}
		return convertView;
	}
}

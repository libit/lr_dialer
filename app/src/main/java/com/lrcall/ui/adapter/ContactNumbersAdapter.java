/*
 * Libit保留所有版权，如有疑问联系QQ：308062035
 * Copyright (c) 2016.
 */
package com.lrcall.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.lrcall.appcall.R;
import com.lrcall.models.ContactInfo;

import java.util.List;

/**
 * Created by libit on 16/4/30.
 */
public class ContactNumbersAdapter extends BaseContactsAdapter<ContactInfo.PhoneInfo>
{
	public interface IContactNumberAdapterItemClicked extends IBaseContactsAdapterItemClicked<ContactInfo.PhoneInfo>
	{
		void onCallClicked(ContactInfo.PhoneInfo phoneInfo);
	}

	private IContactNumberAdapterItemClicked contactNumberAdapterItemClicked;

	public ContactNumbersAdapter(Context context, List<ContactInfo.PhoneInfo> list, IContactNumberAdapterItemClicked contactNumberAdapterItemClicked)
	{
		super(context, list);
		this.contactNumberAdapterItemClicked = contactNumberAdapterItemClicked;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{
		convertView = LayoutInflater.from(context).inflate(R.layout.item_contact_number, null);
		TextView tvNumber = (TextView) convertView.findViewById(R.id.tv_number);
		TextView tvType = (TextView) convertView.findViewById(R.id.tv_type);
		TextView tvLocal = (TextView) convertView.findViewById(R.id.tv_local);
		final ContactInfo.PhoneInfo phoneInfo = list.get(position);
		final String number = phoneInfo.getNumber();
		String type = phoneInfo.getType();
		tvNumber.setText(number);
		tvType.setText(type);
		convertView.findViewById(R.id.v_call).setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				if (contactNumberAdapterItemClicked != null)
				{
					contactNumberAdapterItemClicked.onCallClicked(phoneInfo);
				}
			}
		});
		convertView.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				if (contactNumberAdapterItemClicked != null)
				{
					contactNumberAdapterItemClicked.onItemClicked(phoneInfo);
				}
			}
		});
		return convertView;
	}
}

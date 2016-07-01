/*
 * Libit保留所有版权，如有疑问联系QQ：308062035
 * Copyright (c) 2016.
 */
package com.lrcall.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.lrcall.appcall.R;
import com.lrcall.models.ContactInfo;

import java.util.List;

/**
 * Created by libit on 16/4/30.
 */
public class ContactEditNumbersAdapter extends BaseContactsAdapter<ContactInfo.PhoneInfo>
{
	public interface IContactEditNumberAdapterItemClicked extends IBaseContactsAdapterItemClicked<ContactInfo.PhoneInfo>
	{
		void onDeleteClicked(ContactInfo.PhoneInfo phoneInfo);
	}

	private IContactEditNumberAdapterItemClicked contactEditNumberAdapterItemClicked;

	public ContactEditNumbersAdapter(Context context, List<ContactInfo.PhoneInfo> list, IContactEditNumberAdapterItemClicked contactEditNumberAdapterItemClicked)
	{
		super(context, list);
		this.contactEditNumberAdapterItemClicked = contactEditNumberAdapterItemClicked;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{
		convertView = LayoutInflater.from(context).inflate(R.layout.item_contact_number_edit, null);
		EditText etNumber = (EditText) convertView.findViewById(R.id.et_number);
		TextView tvType = (TextView) convertView.findViewById(R.id.tv_type);
		final ContactInfo.PhoneInfo phoneInfo = list.get(position);
		final String number = phoneInfo.getNumber();
		String type = phoneInfo.getType();
		etNumber.setText(number);
		tvType.setText(type);
		convertView.findViewById(R.id.btn_delete).setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				if (contactEditNumberAdapterItemClicked != null)
				{
					contactEditNumberAdapterItemClicked.onDeleteClicked(phoneInfo);
				}
			}
		});
		convertView.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				if (contactEditNumberAdapterItemClicked != null)
				{
					contactEditNumberAdapterItemClicked.onItemClicked(phoneInfo);
				}
			}
		});
		return convertView;
	}
}

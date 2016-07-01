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
import com.lrcall.models.ContactInfo;
import com.lrcall.utils.StringTools;

import java.util.List;

/**
 * Created by libit on 16/4/30.
 */
public class ContactsSearchAdapter extends BaseContactsAdapter<ContactInfo>
{
	protected IBaseContactsAdapterItemClicked contactsAdapterItemClicked;

	public ContactsSearchAdapter(Context context, List<ContactInfo> list, IBaseContactsAdapterItemClicked contactsAdapterItemClicked)
	{
		super(context, list);
		this.contactsAdapterItemClicked = contactsAdapterItemClicked;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{
		convertView = LayoutInflater.from(context).inflate(R.layout.item_phonebook, null);
		ImageView ivHead = (ImageView) convertView.findViewById(R.id.contact_picture);
		TextView tvName = (TextView) convertView.findViewById(R.id.contact_name);
		TextView tvNumber = (TextView) convertView.findViewById(R.id.contact_number);
		//		holder.tvLocal = (TextView) convertView.findViewById(R.id.tv_local);
		final ContactInfo contactInfo = list.get(position);
		//		holder.ivHead.setImageBitmap(contactInfo.getContactPhoto());
		tvName.setText(contactInfo.getName());
		if (contactInfo != null && contactInfo.getPhoneInfoList() != null && contactInfo.getPhoneInfoList().size() > 0)
		{
			tvNumber.setText(contactInfo.getPhoneInfoList().get(0).getNumber());
		}
		loadBitmap(contactInfo.getContactId(), ivHead);
		convertView.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				if (contactsAdapterItemClicked != null)
				{
					contactsAdapterItemClicked.onItemClicked(contactInfo);
				}
			}
		});
		String areacode = StringTools.convertToCallPhoneNumber(contactInfo.getPhoneInfoList().get(0).getNumber());
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

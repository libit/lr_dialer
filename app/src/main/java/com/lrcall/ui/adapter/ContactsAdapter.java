/*
 * Libit保留所有版权，如有疑问联系QQ：308062035
 * Copyright (c) 2016.
 */
package com.lrcall.ui.adapter;

import android.content.Context;
import android.text.SpannableStringBuilder;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.lrcall.appcall.R;
import com.lrcall.contacts.ContactsFactory;
import com.lrcall.models.ContactInfo;
import com.lrcall.utils.StringTools;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

/**
 * Created by libit on 16/5/7.
 */
public class ContactsAdapter extends BaseContactsAdapter<ContactInfo>
{
	public static class ViewHolder
	{
		public Long contactId;
		public ImageView contactHeader;
		public TextView name;
		public TextView number;
	}

	private HashMap<String, Integer> alphaIndexer;
	private String[] sections;
	private String condition;

	public ContactsAdapter(Context context, List<ContactInfo> list, String condition)
	{
		super(context, list);
		this.condition = condition;
		this.alphaIndexer = new HashMap<>();
		for (int i = 0; i < list.size(); i++)
		{
			String name = getAlpha(list.get(i).getPy());
			if (!alphaIndexer.containsKey(name))
			{
				alphaIndexer.put(name, i);
			}
		}
		Set<String> sectionLetters = alphaIndexer.keySet();
		ArrayList<String> sectionList = new ArrayList<>(sectionLetters);
		Collections.sort(sectionList);
		sections = new String[sectionList.size()];
		sectionList.toArray(sections);
	}

	public HashMap<String, Integer> getAlphaIndexer()
	{
		return alphaIndexer;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{
		String currentStr = getAlpha(list.get(position).getPy());
		String previewStr = (position - 1) >= 0 ? getAlpha(list.get(position - 1).getPy()) : " ";
		TextView alpha = null;
		if (!previewStr.equals(currentStr))
		{
			convertView = LayoutInflater.from(context).inflate(R.layout.item_phonebook_with_head, null);
			alpha = ((TextView) convertView.findViewById(R.id.alpha));
		}
		else
		{
			convertView = LayoutInflater.from(context).inflate(R.layout.item_phonebook, null);
		}
		if (alpha != null)
		{
			alpha.setText(currentStr);
		}
		ViewHolder holder = new ViewHolder();
		holder.contactHeader = (ImageView) convertView.findViewById(R.id.contact_picture);
		holder.name = (TextView) convertView.findViewById(R.id.contact_name);
		holder.number = (TextView) convertView.findViewById(R.id.contact_number);
		convertView.setTag(holder);
		ContactInfo contactInfo = list.get(position);
		Long id = contactInfo.getContactId();
		String name = contactInfo.getName();
		String number = "";
		if (contactInfo.getPhoneInfoList() != null)
		{
			number = contactInfo.getPhoneInfoList().get(0).getNumber();
		}
		holder.contactId = id;
		loadBitmap(id, holder.contactHeader);
		if (!StringTools.isNull(number))
		{
			SpannableStringBuilder name_style = ContactsFactory.getInstance().getSpanNameString(name, condition);
			holder.name.setText(name_style);
			SpannableStringBuilder number_style = ContactsFactory.getInstance().getSpanNumberString(number, condition);
			holder.number.setText(number_style);
			holder.number.setVisibility(View.VISIBLE);
			holder.name.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
		}
		else
		{
			holder.name.setText(name);
			holder.number.setText(number);
			holder.number.setVisibility(View.GONE);
			holder.name.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
		}
		return convertView;
	}

	private String getAlpha(String str)
	{
		if (StringTools.isNull(str))
		{
			return "#";
		}
		CharSequence c = str.trim().charAt(0) + "";
		Pattern pattern = Pattern.compile("^[A-Za-z]+$");
		if (pattern.matcher(c).matches())
		{
			return c.toString().toUpperCase();
		}
		else
		{
			return "#";
		}
	}
}




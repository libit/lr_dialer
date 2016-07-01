/*
 * Libit保留所有版权，如有疑问联系QQ：308062035
 * Copyright (c) 2016.
 */
package com.lrcall.ui.adapter;

import android.content.ContentUris;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.ContactsContract;
import android.support.v4.util.LruCache;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.lrcall.appcall.R;
import com.lrcall.contacts.ContactsFactory;
import com.lrcall.models.CallLogInfo;
import com.lrcall.models.ContactInfo;
import com.lrcall.utils.apptools.AppFactory;

import java.util.List;

/**
 * Created by libit on 16/4/30.
 */
public class SearchAdapter extends BaseAdapter
{
	final static int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);
	final static int cacheSize = maxMemory / 8;
	private Context context;
	private LruCache<String, Bitmap> mMemoryCache;
	private List<CallLogInfo> callLogInfoList;
	private List<ContactInfo> contactInfoList;
	private CallLogsAdapter.ICallLogsAdapterItemClicked callLogsAdapterItemClicked;
	private ContactsSearchAdapter.IBaseContactsAdapterItemClicked contactsAdapterItemClicked;

	public SearchAdapter(Context context, List<CallLogInfo> callLogInfoList, List<ContactInfo> contactInfoList)
	{
		this.context = context;
		this.callLogInfoList = callLogInfoList;
		this.contactInfoList = contactInfoList;
		mMemoryCache = new LruCache<String, Bitmap>(cacheSize)
		{
			@Override
			protected int sizeOf(String key, Bitmap bitmap)
			{
				if (AppFactory.isCompatible(12))
				{
					return bitmap.getByteCount() / 1024;
				}
				else
				{
					return bitmap.getRowBytes() * bitmap.getHeight() / 1024;
				}
			}
		};
	}

	public void setCallLogsAdapterItemClicked(CallLogsAdapter.ICallLogsAdapterItemClicked callLogsAdapterItemClicked)
	{
		this.callLogsAdapterItemClicked = callLogsAdapterItemClicked;
	}

	public void setContactsAdapterItemClicked(ContactsSearchAdapter.IBaseContactsAdapterItemClicked contactsAdapterItemClicked)
	{
		this.contactsAdapterItemClicked = contactsAdapterItemClicked;
	}

	@Override
	public int getCount()
	{
		int count1 = callLogInfoList != null ? callLogInfoList.size() : 0;
		int count2 = contactInfoList != null ? contactInfoList.size() : 0;
		return count1 + count2;
	}

	@Override
	public Object getItem(int position)
	{
		int count1 = callLogInfoList != null ? callLogInfoList.size() : 0;
		int count2 = contactInfoList != null ? contactInfoList.size() : 0;
		if (position < count1)
		{
			return callLogInfoList.get(position);
		}
		else
		{
			return contactInfoList.get(position - count1);
		}
	}

	@Override
	public long getItemId(int position)
	{
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{
		int count1 = callLogInfoList != null ? callLogInfoList.size() : 0;
		int count2 = contactInfoList != null ? contactInfoList.size() : 0;
		if (position < count1)
		{
			convertView = LayoutInflater.from(context).inflate(R.layout.item_call_log, null);
			ImageView ivCallLogType = (ImageView) convertView.findViewById(R.id.call_log_type_icon);
			TextView tvName = (TextView) convertView.findViewById(R.id.call_log_name);
			TextView tvNumber = (TextView) convertView.findViewById(R.id.call_log_number);
			TextView tvDuration = (TextView) convertView.findViewById(R.id.call_log_duration);
			TextView tvTime = (TextView) convertView.findViewById(R.id.call_log_date);
			final CallLogInfo callLogInfo = callLogInfoList.get(position);
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
		}
		else
		{
			convertView = LayoutInflater.from(context).inflate(R.layout.item_phonebook, null);
			ImageView ivHead = (ImageView) convertView.findViewById(R.id.contact_picture);
			TextView tvName = (TextView) convertView.findViewById(R.id.contact_name);
			TextView tvNumber = (TextView) convertView.findViewById(R.id.contact_number);
			final ContactInfo contactInfo = contactInfoList.get(position - count1);
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
		}
		return convertView;
	}

	//图片加入缓存
	public void addBitmapToMemoryCache(String key, Bitmap value)
	{
		if (getBitmapFromMemCache(key) == null)
		{
			mMemoryCache.put(key, value);
		}
		else
		{
			mMemoryCache.remove(key);
			mMemoryCache.put(key, value);
		}
	}

	//从图片取缓存
	public Bitmap getBitmapFromMemCache(String key)
	{
		return mMemoryCache.get(key);
	}

	public void loadBitmap(Long cId, ImageView imageView)
	{
		final String imageKey = String.valueOf(cId);
		final Bitmap bitmap = getBitmapFromMemCache(imageKey);
		if (bitmap != null)
		{
			imageView.setImageBitmap(bitmap);
		}
		else
		{
			imageView.setImageResource(R.drawable.placeholder_large);
			BitmapWorkerTask task = new BitmapWorkerTask(imageView);
			task.execute(cId);
		}
	}

	class BitmapWorkerTask extends AsyncTask<Long, Void, Bitmap>
	{
		private ImageView imageView;

		public BitmapWorkerTask(ImageView imageView)
		{
			this.imageView = imageView;
		}

		// Decode image in background.
		@Override
		protected Bitmap doInBackground(Long... params)
		{
			Uri uri = ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI, params[0]);
			Bitmap bitmap = ContactsFactory.getInstance().getContactPhoto(context, uri);
			if (bitmap == null)
			{
				bitmap = ((BitmapDrawable) context.getResources().getDrawable(R.drawable.placeholder_large)).getBitmap();
			}
			addBitmapToMemoryCache(String.valueOf(params[0]), bitmap);
			return bitmap;
		}

		@Override
		protected void onPostExecute(Bitmap result)
		{
			super.onPostExecute(result);
			imageView.setImageBitmap(result);
		}
	}
}

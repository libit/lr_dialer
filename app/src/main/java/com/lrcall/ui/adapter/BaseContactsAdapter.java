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
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.lrcall.appcall.R;
import com.lrcall.contacts.ContactsFactory;
import com.lrcall.utils.apptools.AppFactory;

import java.util.List;

/**
 * Created by libit on 16/5/7.
 */
public abstract class BaseContactsAdapter<T> extends BaseAdapter
{
	public interface IBaseContactsAdapterItemClicked<T>
	{
		void onItemClicked(final T t);
	}

	public final static int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);
	public final static int cacheSize = maxMemory / 8;
	protected Context context;
	protected List<T> list;
	protected LruCache<String, Bitmap> mMemoryCache;

	public BaseContactsAdapter(Context context, List<T> list)
	{
		super();
		this.context = context;
		this.list = list;
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

	/**
	 * How many items are in the data set represented by this Adapter.
	 *
	 * @return Count of items.
	 */
	@Override
	public int getCount()
	{
		return list != null ? list.size() : 0;
	}

	/**
	 * Get the data item associated with the specified position in the data set.
	 *
	 * @param position Position of the item whose data we want within the adapter's
	 *                 data set.
	 * @return The data at the specified position.
	 */
	@Override
	public Object getItem(int position)
	{
		return list != null ? list.get(position) : null;
	}

	/**
	 * Get the row id associated with the specified position in the list.
	 *
	 * @param position The position of the item within the adapter's data set whose row id we want.
	 * @return The id of the item at the specified position.
	 */
	@Override
	public long getItemId(int position)
	{
		return position;
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

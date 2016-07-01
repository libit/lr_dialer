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
import com.lrcall.utils.StringTools;

import java.io.File;
import java.util.List;

/**
 * 文件选择器
 * Created by libit on 15/8/31.
 */
public class FileListAdapter extends BaseAdapter
{
	private Context context;
	private File rootFile;
	private List<File> fileList;
	private IFileListAdapter fileListAdapter;

	public interface IFileListAdapter
	{
		//选择上一层
		void onParentSelected(File file);

		//选择文件
		void onFileSelected(File file);
	}

	public FileListAdapter(Context context, File rootFile, List<File> fileList, IFileListAdapter fileListAdapter)
	{
		this.context = context;
		this.rootFile = rootFile;
		this.fileList = fileList;
		this.fileListAdapter = fileListAdapter;
	}

	@Override
	public int getCount()
	{
		if (fileList != null)
			return fileList.size();
		return 0;
	}

	@Override
	public Object getItem(int position)
	{
		if (fileList != null)
			return fileList.get(position);
		return null;
	}

	@Override
	public long getItemId(int position)
	{
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{
		convertView = LayoutInflater.from(context).inflate(R.layout.item_file, null);
		View rootView = convertView.findViewById(R.id.layout_file);
		ImageView ivHead = (ImageView) convertView.findViewById(R.id.iv_icon);
		TextView tvName = (TextView) convertView.findViewById(R.id.tv_file_name);
		TextView tvTime = (TextView) convertView.findViewById(R.id.tv_file_time);
		final File file = fileList.get(position);
		if (file.isDirectory())
		{
			ivHead.setImageResource(R.drawable.ic_folder);
		}
		else
		{
			ivHead.setImageResource(R.drawable.ic_file);
		}
		//第一个应该是返回上一层
		if (position == 0)
		{
			if (rootFile.getAbsolutePath().equals(file.getAbsolutePath()))
			{
				rootView.setVisibility(View.GONE);
			}
			else
			{
				tvName.setText("...");
				tvTime.setText("上层文件夹");
				rootView.setOnClickListener(new View.OnClickListener()
				{
					@Override
					public void onClick(View v)
					{
						if (fileListAdapter != null)
						{
							fileListAdapter.onParentSelected(file);
						}
					}
				});
			}
		}
		else
		{
			tvName.setText(file.getName());
			if (file.isDirectory())
			{
				tvTime.setText(StringTools.getTime(file.lastModified()));
			}
			else
			{
				tvTime.setText(String.format("%s %.2fK", StringTools.getTime(file.lastModified()), (double) ((double) file.length() / (double) 1024)));
			}
			rootView.setOnClickListener(new View.OnClickListener()
			{
				@Override
				public void onClick(View v)
				{
					if (fileListAdapter != null)
					{
						fileListAdapter.onFileSelected(file);
					}
				}
			});
		}
		return convertView;
	}
}

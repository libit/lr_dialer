/*
 * Libit保留所有版权，如有疑问联系QQ：308062035
 * Copyright (c) 2016.
 */
package com.lrcall.ui.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Window;
import android.widget.ListView;

import com.lrcall.appcall.R;
import com.lrcall.ui.adapter.FileListAdapter;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.List;

public class DialogFileList extends Dialog implements FileListAdapter.IFileListAdapter
{
	private ListView listView;
	private FileListAdapter adapter;
	private File rootFile;
	private FilenameFilter filenameFilter;
	private IDialogChooseFile dialogChooseFile;
	private List<File> fileList = new ArrayList<>();

	public interface IDialogChooseFile
	{
		//已选择文件
		void onFileSelected(File file);
		//取消选择
		//		void onCancelled();
	}

	public DialogFileList(Context context, File rootFile, FilenameFilter filenameFilter, IDialogChooseFile dialogChooseFile)
	{
		super(context, R.style.MyDialog);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		//		setCancelable(false);
		this.rootFile = rootFile;
		this.filenameFilter = filenameFilter;
		this.dialogChooseFile = dialogChooseFile;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dialog_file_list);
		initView();
		initData();
	}

	private void initView()
	{
		listView = (ListView) findViewById(R.id.list_view);
		listView.setAdapter(adapter);
	}

	//初始化数据
	private void initData()
	{
		File[] files = null;
		if (filenameFilter == null)
		{
			files = rootFile.listFiles();
		}
		else
		{
			files = rootFile.listFiles(filenameFilter);
		}
		fileList.clear();
		//文件夹所在的位置
		int index = 0;
		if (rootFile.getParentFile() != null)
		{
			fileList.add(index++, rootFile.getParentFile());
		}
		else
		{
			fileList.add(index++, rootFile);
		}
		for (File file : files)
		{
			if (file.isDirectory())
			{
				fileList.add(index++, file);
			}
			else
			{
				fileList.add(file);
			}
		}
		if (adapter == null)
		{
			adapter = new FileListAdapter(getContext(), rootFile, fileList, this);
			listView.setAdapter(adapter);
		}
		else
		{
			adapter.notifyDataSetChanged();
		}
	}

	@Override
	public void onFileSelected(File file)
	{
		if (file.isDirectory())
		{
			//			ToastView.showCenterToast(getContext(), "选择文件夹：" + file.getName());
			rootFile = file;
			initData();
		}
		else
		{
//			ToastView.showCenterToast(getContext(), "选择文件：" + file.getName());
			if (dialogChooseFile != null)
			{
				dialogChooseFile.onFileSelected(file);
			}
			dismiss();
		}
	}

	@Override
	public void onParentSelected(File file)
	{
//		ToastView.showCenterToast(getContext(), "选择上一层：" + file.getName());
		rootFile = file;
		initData();
	}
}

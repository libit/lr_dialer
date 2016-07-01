/*
 * Libit保留所有版权，如有疑问联系QQ：308062035
 * Copyright (c) 2016.
 */
package com.lrcall.ui;

import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.lrcall.appcall.R;
import com.lrcall.calllogs.CallLogsFactory;
import com.lrcall.contacts.ContactsFactory;
import com.lrcall.models.CallLogInfo;
import com.lrcall.models.ContactInfo;
import com.lrcall.appcall.models.ReturnInfo;
import com.lrcall.ui.adapter.BaseContactsAdapter;
import com.lrcall.ui.adapter.CallLogsAdapter;
import com.lrcall.ui.adapter.ContactsSearchAdapter;
import com.lrcall.utils.CallTools;
import com.lrcall.utils.ConstValues;
import com.lrcall.utils.StringTools;

import java.util.List;

public class ActivitySearch extends MyBaseActivity implements View.OnClickListener, BaseContactsAdapter.IBaseContactsAdapterItemClicked<ContactInfo>, CallLogsAdapter.ICallLogsAdapterItemClicked
{
	private static final String TAG = ActivitySearch.class.getSimpleName();
	private EditText etSearch;
	private ListView lvContactNumbers, lvContactCalllogs;
	private ContactsSearchAdapter contactsSearchAdapter;
	private CallLogsAdapter contactCallLogsAdapter;
	private SearchContactsTask searchContactsTask;
	private SearchCallLogsTask searchCallLogsTask;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_search);
		viewInit();
	}

	@Override
	protected void viewInit()
	{
		super.viewInit();
		setBackButton();
		lvContactNumbers = (ListView) findViewById(R.id.list_contact_numbers);
		lvContactCalllogs = (ListView) findViewById(R.id.list_contact_calllogs);
		etSearch = (EditText) findViewById(R.id.edit_input_number);
		etSearch.addTextChangedListener(new TextWatcher()
		{
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count)
			{
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after)
			{
			}

			@Override
			public void afterTextChanged(Editable s)
			{
				String condition = etSearch.getText().toString();
				if (!StringTools.isNull(condition))
				{
					//					searchContactsTask = new SearchContactsTask(condition);
					new SearchContactsTask(condition).execute();
					//					searchCallLogsTask = new SearchCallLogsTask(condition);
					new SearchCallLogsTask(condition).execute();
				}
				else
				{
					lvContactNumbers.setAdapter(null);
					lvContactCalllogs.setAdapter(null);
					//					if (searchContactsTask != null)
					//					{
					//						synchronized (searchContactsTask)
					//						{
					//							if (searchContactsTask != null && !searchContactsTask.isCancelled())
					//							{
					//								searchContactsTask.cancel(true);
					//							}
					//						}
					//					}
					//					if (searchCallLogsTask != null)
					//					{
					//						synchronized (searchCallLogsTask)
					//						{
					//							if (searchCallLogsTask != null && !searchCallLogsTask.isCancelled())
					//							{
					//								searchCallLogsTask.cancel(true);
					//							}
					//						}
					//					}
					//					lvContactNumbers.setAdapter(null);
					//					lvContactCalllogs.setAdapter(null);
				}
			}
		});
		findViewById(R.id.search_del).setOnClickListener(this);
		//		lvContactNumbers.setOnTouchListener(new View.OnTouchListener()
		//		{
		//			@Override
		//			public boolean onTouch(View v, MotionEvent event)
		//			{
		//				hideSoftPad();
		//				return false;
		//			}
		//		});
		//		lvContactCalllogs.setOnTouchListener(new View.OnTouchListener()
		//		{
		//			@Override
		//			public boolean onTouch(View v, MotionEvent event)
		//			{
		//				hideSoftPad();
		//				return false;
		//			}
		//		});
	}

	@Override
	public void onClick(View v)
	{
		switch (v.getId())
		{
			case R.id.search_del:
			{
				String number = etSearch.getText().toString();
				if (!StringTools.isNull(number))
				{
					etSearch.setTextKeepState(number.substring(0, number.length() - 1));
				}
				break;
			}
		}
	}

	@Override
	public void onItemClicked(CallLogInfo callLogInfo)
	{
		if (callLogInfo != null)
		{
			Toast.makeText(this, "点击通话记录" + callLogInfo.getName(), Toast.LENGTH_LONG).show();
		}
	}

	@Override
	public void onCallClicked(CallLogInfo callLogInfo)
	{
		if (callLogInfo != null)
		{
			ReturnInfo returnInfo = CallTools.makeCall(this, callLogInfo.getNumber());
			if (!ReturnInfo.isSuccess(returnInfo))
			{
				Toast.makeText(this, returnInfo.getErrmsg(), Toast.LENGTH_LONG).show();
			}
		}
	}

	@Override
	public void onItemClicked(ContactInfo contactInfo)
	{
		if (contactInfo != null)
		{
			//			Toast.makeText(this, "点击联系人" + contactInfo.getName(), Toast.LENGTH_LONG).show();
			Intent intent = new Intent(this, ActivityContactDetail.class);
			intent.putExtra(ConstValues.DATA_CONTACT_ID, contactInfo.getContactId());
			startActivity(intent);
		}
	}
	// 隐藏软键盘

	private void hideSoftPad()
	{
		etSearch.clearFocus();
		((InputMethodManager) getSystemService(INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(etSearch.getWindowToken(), 0);
	}

	/**
	 * 搜索通话记录任务
	 */
	class SearchCallLogsTask extends AsyncTask<Long, Void, List<CallLogInfo>>
	{
		private String condition;

		public SearchCallLogsTask(String condition)
		{
			this.condition = condition;
		}

		// Decode image in background.
		@Override
		protected List<CallLogInfo> doInBackground(Long... params)
		{
			Cursor cursor = CallLogsFactory.getInstance().getCallLogs(ActivitySearch.this, CallLogsFactory.CACHED_NAME + " LIKE '%" + condition + "%' OR " + CallLogsFactory.NUMBER + " LIKE '%" + condition + "%'");
			List<CallLogInfo> callLogInfoList = CallLogsFactory.getInstance().createListSort(cursor);
			if (cursor != null && !cursor.isClosed())
			{
				cursor.close();
			}
			return callLogInfoList;
		}

		@Override
		protected void onPostExecute(List<CallLogInfo> callLogInfoList)
		{
			super.onPostExecute(callLogInfoList);
			contactCallLogsAdapter = new CallLogsAdapter(ActivitySearch.this, callLogInfoList, ActivitySearch.this);
			lvContactCalllogs.setAdapter(contactCallLogsAdapter);
		}
	}

	/**
	 * 搜索联系人任务
	 */
	class SearchContactsTask extends AsyncTask<Long, Void, List<ContactInfo>>
	{
		private String condition;

		public SearchContactsTask(String condition)
		{
			this.condition = condition;
		}

		// Decode image in background.
		@Override
		protected List<ContactInfo> doInBackground(Long... params)
		{
			List<ContactInfo> contactInfoList = ContactsFactory.getInstance().getContactInfos(ActivitySearch.this, condition);
			return contactInfoList;
		}

		@Override
		protected void onPostExecute(List<ContactInfo> contactInfoList)
		{
			super.onPostExecute(contactInfoList);
			contactsSearchAdapter = new ContactsSearchAdapter(ActivitySearch.this, contactInfoList, ActivitySearch.this);
			lvContactNumbers.setAdapter(contactsSearchAdapter);
		}
	}
}

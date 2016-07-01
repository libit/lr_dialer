/*
 * Libit保留所有版权，如有疑问联系QQ：308062035
 * Copyright (c) 2015.
 */
package com.lrcall.ui;

import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.external.xlistview.XListView;
import com.lrcall.appcall.R;
import com.lrcall.calllogs.CallLogsFactory;
import com.lrcall.contacts.ContactsFactory;
import com.lrcall.models.CallLogInfo;
import com.lrcall.models.ContactInfo;
import com.lrcall.appcall.models.ReturnInfo;
import com.lrcall.ui.adapter.CallLogsAdapter;
import com.lrcall.ui.adapter.ContactsSearchAdapter;
import com.lrcall.ui.customer.AddressAware;
import com.lrcall.ui.customer.AddressText;
import com.lrcall.ui.customer.DraftImageView;
import com.lrcall.ui.customer.EraseButton;
import com.lrcall.ui.customer.ToastView;
import com.lrcall.utils.CallTools;
import com.lrcall.utils.DisplayTools;
import com.lrcall.utils.LogcatTools;
import com.lrcall.utils.StringTools;

import java.util.ArrayList;
import java.util.List;

public class FragmentDialer extends MyBaseFragment implements XListView.IXListViewListener, View.OnClickListener, AddressText.InputNumberChangedListener, CallLogsAdapter.ICallLogsAdapterItemClicked, ContactsSearchAdapter.IBaseContactsAdapterItemClicked<ContactInfo>
{
	private static final String TAG = FragmentDialer.class.getSimpleName();
	private XListView xListView;
	private EraseButton erase;
	private AddressText mAddress;
	private View vPad, layoutSwitchPad;
	private DraftImageView vSwitchPad;
	private ContactsSearchAdapter contactsSearchAdapter = null;
	private static FragmentDialer instance = null;
	private int start = 0;
	private static final int count = 200;
	private List<CallLogInfo> callLogInfoList = new ArrayList<>();
	private CallLogsAdapter adapterCalllogs = null;

	//设置号码
	public static void setAddressNumber(String number)
	{
		if (instance != null)
		{
			instance.mAddress.setText(number);
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		LogcatTools.debug(TAG, "fragment onCreateView");
		View rootView = inflater.inflate(R.layout.fragment_dialer, container, false);
		viewInit(rootView);
		setPadVisible(true);
		instance = this;
		return rootView;
	}

	@Override
	protected void viewInit(View rootView)
	{
		xListView = (XListView) rootView.findViewById(R.id.xlist);
		xListView.setPullRefreshEnable(true);
		xListView.setPullLoadEnable(true);
		xListView.setXListViewListener(this);
		mAddress = (AddressText) rootView.findViewById(R.id.tv_number);
		erase = (EraseButton) rootView.findViewById(R.id.btn_delete);
		erase.setAddressWidget(mAddress);
		AddressAware numpad = (AddressAware) rootView.findViewById(R.id.number_pad);
		if (numpad != null)
		{
			numpad.setAddressWidget(mAddress);
		}
		vPad = rootView.findViewById(R.id.number_pad);
		vSwitchPad = (DraftImageView) rootView.findViewById(R.id.btn_switch_pad);
		vSwitchPad.setOnTouchListener(DisplayTools.getWindowHeight(this.getContext()) - rootView.findViewById(R.id.layout_root).getHeight());
		vSwitchPad.setOnClickListener(this);
		layoutSwitchPad = rootView.findViewById(R.id.layout_switch);
		layoutSwitchPad.setOnClickListener(this);
		xListView.setOnTouchListener(new View.OnTouchListener()
		{
			@Override
			public boolean onTouch(View v, MotionEvent event)
			{
				setPadVisible(false);
				return false;
			}
		});
		registerForContextMenu(xListView);
		rootView.findViewById(R.id.layout_make_call).setOnClickListener(this);
		mAddress.setInputNumberChangedListener(this);
		super.viewInit(rootView);
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo)
	{
		super.onCreateContextMenu(menu, v, menuInfo);
		getActivity().getMenuInflater().inflate(R.menu.menu_calllog_list_context, menu);
	}

	@Override
	public boolean onContextItemSelected(MenuItem item)
	{
		int id = item.getItemId();
		if (id == R.id.action_add_contact)
		{
			ToastView.showCenterToast(getContext(), "添加联系人");
			return true;
		}
		return super.onContextItemSelected(item);
	}

	@Override
	public void onViewCreated(View view, @Nullable Bundle savedInstanceState)
	{
		super.onViewCreated(view, savedInstanceState);
		LogcatTools.debug(TAG, "fragment onViewCreated,start=" + start);
		instance = this;
		onRefresh();
	}

	@Override
	public void fragmentShow()
	{
		hideSoftPad();
		setPadVisible(true);
	}

	@Override
	public void onDestroyView()
	{
		LogcatTools.debug(TAG, "fragment onDestroyView");
		callLogInfoList.clear();
		adapterCalllogs = null;
		instance = this;
		super.onDestroyView();
	}

	private void getAllCallLog()
	{
		xListView.setPullLoadEnable(true);
		xListView.setPullRefreshEnable(true);
		new SearchCallLogsTask().execute();
	}

	// 解决从外部传过来的号码 光标的位置置为最后
	public void setAddressCursorIndex()
	{
		String number = mAddress.getText().toString();
		if (!StringTools.isNull(number))
		{
			mAddress.setSelection(number.length());
		}
	}

	@Override
	public void onClick(View v)
	{
		switch (v.getId())
		{
			case R.id.layout_make_call:
			{
				String number = mAddress.getText().toString();
				ReturnInfo returnInfo = CallTools.makeCall(this.getContext(), number);
				if (!ReturnInfo.isSuccess(returnInfo))
				{
					Toast.makeText(this.getContext(), returnInfo.getErrmsg(), Toast.LENGTH_LONG).show();
				}
				mAddress.setText("");
				break;
			}
			case R.id.btn_switch_pad:
			{
				switchPad();
				break;
			}
			case R.id.layout_switch:
			{
				setPadVisible(false);
				break;
			}
		}
	}

	//切换键盘显示
	private void switchPad()
	{
		if (vPad.getVisibility() == View.VISIBLE)
		{
			vPad.setVisibility(View.GONE);
		}
		else
		{
			vPad.setVisibility(View.VISIBLE);
		}
		onPadViewChanged();
	}

	//设置键盘显示状态
	private void setPadVisible(boolean show)
	{
		if (show)
		{
			vPad.setVisibility(View.VISIBLE);
		}
		else
		{
			vPad.setVisibility(View.GONE);
		}
		onPadViewChanged();
	}

	//键盘显示状态变化后的通知
	private void onPadViewChanged()
	{
		if (vPad.getVisibility() == View.VISIBLE)
		{
			vSwitchPad.setVisibility(View.GONE);
		}
		else
		{
			vSwitchPad.setVisibility(View.VISIBLE);
		}
	}

	@Override
	public void onInputNumberChanged(String txt)
	{
		hideSoftPad();
		//		List<ContactInfo> contactInfoList;
		String condition = txt;
		if (!StringTools.isNull(condition))
		{
			new SearchContactsTask(condition).execute();
			//			contactInfoList = ContactsFactory.getInstance().getContactInfos(getContext(), condition);
			//			if (contactInfoList.size() > 0)
			//			{
			//				contactsSearchAdapter = new ContactsSearchAdapter(getContext(), contactInfoList, this);
			//				xListView.setPullLoadEnable(false);
			//				xListView.setPullRefreshEnable(false);
			//				xListView.setAdapter(contactsSearchAdapter);
			//			}
		}
		else
		{
			onRefresh();
		}
	}

	@Override
	public void onRefresh()
	{
		start = 0;
		callLogInfoList.clear();
		getAllCallLog();
	}

	@Override
	public void onLoadMore()
	{
		start += count;
		getAllCallLog();
	}

	@Override
	public void onItemClicked(CallLogInfo callLogInfo)
	{
		setPadVisible(true);
		if (callLogInfo != null)
		{
			mAddress.setText(callLogInfo.getNumber());
			setAddressCursorIndex();
		}
	}

	@Override
	public void onCallClicked(CallLogInfo callLogInfo)
	{
		setPadVisible(true);
		if (callLogInfo != null)
		{
			ReturnInfo returnInfo = CallTools.makeCall(this.getContext(), callLogInfo.getNumber());
			if (!ReturnInfo.isSuccess(returnInfo))
			{
				Toast.makeText(this.getContext(), returnInfo.getErrmsg(), Toast.LENGTH_LONG).show();
			}
			mAddress.setText("");
		}
	}

	@Override
	public void onItemClicked(ContactInfo contactInfo)
	{
		setPadVisible(true);
		if (contactInfo != null && contactInfo.getPhoneInfoList() != null && contactInfo.getPhoneInfoList().size() > 0)
		{
			mAddress.setText(contactInfo.getPhoneInfoList().get(0).getNumber());
			setAddressCursorIndex();
		}
	}

	/**
	 * 搜索通话记录任务
	 */
	class SearchCallLogsTask extends AsyncTask<Long, Void, Boolean>
	{
		public SearchCallLogsTask()
		{
		}

		// Decode image in background.
		@Override
		protected Boolean doInBackground(Long... params)
		{
			boolean bAdd = false;
			synchronized (callLogInfoList)
			{
				Cursor cursor = CallLogsFactory.getInstance().getCallLogs(FragmentDialer.this.getContext());
				List<CallLogInfo> list = CallLogsFactory.getInstance().createListSort(cursor, start, count);
				if (cursor != null && !cursor.isClosed())
				{
					cursor.close();
				}
				if (list != null && list.size() > 0)
				{
					bAdd = true;
					callLogInfoList.addAll(list);
				}
			}
			return bAdd;
		}

		@Override
		protected void onPostExecute(Boolean bAdd)
		{
			super.onPostExecute(bAdd);
			xListView.stopRefresh();
			xListView.stopLoadMore();
			if (adapterCalllogs == null || start == 0)
			{
				adapterCalllogs = new CallLogsAdapter(FragmentDialer.this.getContext(), callLogInfoList, FragmentDialer.this);
				xListView.setAdapter(adapterCalllogs);
			}
			else
			{
				if (bAdd)
				{
					adapterCalllogs.notifyDataSetChanged();
				}
			}
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
			List<ContactInfo> contactInfoList = ContactsFactory.getInstance().getContactInfos(getContext(), condition);
			return contactInfoList;
		}

		@Override
		protected void onPostExecute(List<ContactInfo> contactInfoList)
		{
			super.onPostExecute(contactInfoList);
			//			if (contactInfoList.size() > 0)
			{
				contactsSearchAdapter = new ContactsSearchAdapter(FragmentDialer.this.getContext(), contactInfoList, FragmentDialer.this);
				xListView.setPullLoadEnable(false);
				xListView.setPullRefreshEnable(false);
				xListView.setAdapter(contactsSearchAdapter);
			}
		}
	}

	// 隐藏软键盘
	private void hideSoftPad()
	{
		mAddress.clearFocus();
		((InputMethodManager) getActivity().getSystemService(getContext().INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(mAddress.getWindowToken(), 0);
	}
}

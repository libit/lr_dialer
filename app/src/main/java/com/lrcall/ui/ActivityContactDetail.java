/*
 * Libit保留所有版权，如有疑问联系QQ：308062035
 * Copyright (c) 2016.
 */
package com.lrcall.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.lrcall.appcall.R;
import com.lrcall.calllogs.CallLogsFactory;
import com.lrcall.contacts.ContactsFactory;
import com.lrcall.models.CallLogInfo;
import com.lrcall.models.ContactInfo;
import com.lrcall.appcall.models.ReturnInfo;
import com.lrcall.ui.adapter.CallLogsAdapter;
import com.lrcall.ui.adapter.ContactCallLogsAdapter;
import com.lrcall.ui.adapter.ContactNumbersAdapter;
import com.lrcall.ui.customer.ToastView;
import com.lrcall.ui.dialog.DialogCommon;
import com.lrcall.utils.CallTools;
import com.lrcall.utils.ConstValues;

import java.util.List;

public class ActivityContactDetail extends MyBaseActivity implements ContactNumbersAdapter.IContactNumberAdapterItemClicked, CallLogsAdapter.ICallLogsAdapterItemClicked
{
	private static final String TAG = FragmentDialer.class.getSimpleName();
	public static final int REQ_EDIT = 11;
	private TextView tvName;
	private ImageView ivHead;
	private ListView lvContactNumbers, lvContactCalllogs;
	private Long contactId;
	private ContactNumbersAdapter contactNumbersAdapter;
	private ContactCallLogsAdapter contactCallLogsAdapter;
	private List<CallLogInfo> callLogInfoList;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_contact_detail);
		contactId = getIntent().getLongExtra(ConstValues.DATA_CONTACT_ID, -1);
		viewInit();
		initData();
	}

	@Override
	protected void viewInit()
	{
		super.viewInit();
		setBackButton();
		tvName = (TextView) findViewById(R.id.tv_name);
		ivHead = (ImageView) findViewById(R.id.iv_head);
		lvContactNumbers = (ListView) findViewById(R.id.list_contact_numbers);
		lvContactCalllogs = (ListView) findViewById(R.id.list_contact_calllogs);
	}

	protected void initData()
	{
		ContactInfo contactInfo = ContactsFactory.getInstance().getContactInfoById(this, contactId);
		if (contactInfo != null)
		{
			tvName.setText(contactInfo.getName());
			ivHead.setImageBitmap(contactInfo.getContactPhoto());
			if (contactNumbersAdapter == null)
			{
				contactNumbersAdapter = new ContactNumbersAdapter(ActivityContactDetail.this, contactInfo.getPhoneInfoList(), this);
				lvContactNumbers.setAdapter(contactNumbersAdapter);
			}
			else
			{
				contactNumbersAdapter.notifyDataSetChanged();
			}
			//			if (contactCallLogsAdapter == null)
			{
				callLogInfoList = CallLogsFactory.getInstance().getCallLogsByContactId(ActivityContactDetail.this, contactInfo.getContactId());
				if (callLogInfoList == null || callLogInfoList.size() < 1)
				{
					findViewById(R.id.tv_no_contact_calllogs).setVisibility(View.VISIBLE);
					lvContactCalllogs.setVisibility(View.GONE);
				}
				else
				{
					findViewById(R.id.tv_no_contact_calllogs).setVisibility(View.GONE);
					lvContactCalllogs.setVisibility(View.VISIBLE);
					contactCallLogsAdapter = new ContactCallLogsAdapter(ActivityContactDetail.this, callLogInfoList, this);
					lvContactCalllogs.setAdapter(contactCallLogsAdapter);
				}
			}
			//			else
			//			{
			//				contactCallLogsAdapter.notifyDataSetChanged();
			//			}
		}
		else
		{
			tvName.setText("");
			lvContactNumbers.setAdapter(null);
			lvContactCalllogs.setAdapter(null);
		}
	}

	@Override
	public void onItemClicked(CallLogInfo callLogInfo)
	{
		//		if (callLogInfo != null)
		//		{
		//			ReturnInfo returnInfo = CallTools.makeCall(this, callLogInfo.getNumber());
		//			if (!ReturnInfo.isSuccess(returnInfo))
		//			{
		//				Toast.makeText(this, returnInfo.getErrmsg(), Toast.LENGTH_LONG).show();
		//			}
		//		}
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
	public void onCallClicked(ContactInfo.PhoneInfo phoneInfo)
	{
		if (phoneInfo != null)
		{
			ReturnInfo returnInfo = CallTools.makeCall(this, phoneInfo.getNumber());
			if (!ReturnInfo.isSuccess(returnInfo))
			{
				Toast.makeText(this, returnInfo.getErrmsg(), Toast.LENGTH_LONG).show();
			}
		}
	}

	@Override
	public void onItemClicked(ContactInfo.PhoneInfo phoneInfo)
	{
		//		if (phoneInfo != null)
		//		{
		//			ReturnInfo returnInfo = CallTools.makeCall(this, phoneInfo.getNumber());
		//			if (!ReturnInfo.isSuccess(returnInfo))
		//			{
		//				Toast.makeText(this, returnInfo.getErrmsg(), Toast.LENGTH_LONG).show();
		//			}
		//		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		getMenuInflater().inflate(R.menu.menu_activity_contact_detail, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent context in AndroidManifest.xml.
		int id = item.getItemId();
		//noinspection SimplifiableIfStatement
		if (id == R.id.action_edit)
		{
			//			Intent intent = new Intent(this, ActivityContactEdit.class);
			//			intent.putExtra(ConstValues.DATA_CONTACT_ID, contactId);
			//			startActivityForResult(intent, REQ_EDIT);
			ContactsFactory.getInstance().toSystemEditContactActivity(this, contactId);
			return true;
		}
		else if (id == R.id.action_delete)
		{
			new DialogCommon(this, new DialogCommon.LibitDialogListener()
			{
				@Override
				public void onOkClick()
				{
					boolean b = ContactsFactory.getInstance().deleteContact(ActivityContactDetail.this, contactId);
					if (b)
					{
						finish();
						ToastView.showCenterToast(ActivityContactDetail.this, "删除联系人成功！");
					}
					else
					{
						ToastView.showCenterToast(ActivityContactDetail.this, "删除联系人失败！");
					}
				}

				@Override
				public void onCancelClick()
				{
				}
			}, getString(R.string.title_warning), "确定要删除联系人吗？", true, false, true).show();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		super.onActivityResult(requestCode, resultCode, data);
		if (REQ_EDIT == requestCode)
		{
			if (requestCode == RESULT_OK)
			{
				initData();
			}
		}
	}
}

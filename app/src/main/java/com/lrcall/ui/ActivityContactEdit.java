/*
 * Libit保留所有版权，如有疑问联系QQ：308062035
 * Copyright (c) 2016.
 */
package com.lrcall.ui;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.lrcall.appcall.R;
import com.lrcall.contacts.ContactsFactory;
import com.lrcall.models.ContactInfo;
import com.lrcall.ui.adapter.ContactEditNumbersAdapter;
import com.lrcall.ui.dialog.DialogContactNumberAdd;
import com.lrcall.utils.ConstValues;

public class ActivityContactEdit extends MyBaseActivity implements ContactEditNumbersAdapter.IContactEditNumberAdapterItemClicked, View.OnClickListener
{
	private static final String TAG = FragmentDialer.class.getSimpleName();
	private EditText tvName, tvRemark;
	private ImageView ivHead;
	private ListView lvContactNumbers;
	private Long contactId;
	private ContactInfo contactInfo;
	private ContactEditNumbersAdapter contactEditNumbersAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_contact_edit);
		contactId = getIntent().getLongExtra(ConstValues.DATA_CONTACT_ID, -1);
		contactInfo = ContactsFactory.getInstance().getContactInfoById(this, contactId);
		viewInit();
		initData();
	}

	@Override
	protected void viewInit()
	{
		super.viewInit();
		setBackButton();
		tvName = (EditText) findViewById(R.id.tv_name);
		tvRemark = (EditText) findViewById(R.id.tv_remark);
		ivHead = (ImageView) findViewById(R.id.iv_head);
		lvContactNumbers = (ListView) findViewById(R.id.list_contact_numbers);
		findViewById(R.id.btn_add).setOnClickListener(this);
	}

	protected void initData()
	{
		if (contactInfo != null)
		{
			tvName.setText(contactInfo.getName());
			ivHead.setImageBitmap(contactInfo.getContactPhoto());
			if (contactEditNumbersAdapter == null)
			{
				contactEditNumbersAdapter = new ContactEditNumbersAdapter(ActivityContactEdit.this, contactInfo.getPhoneInfoList(), this);
				lvContactNumbers.setAdapter(contactEditNumbersAdapter);
			}
			else
			{
				contactEditNumbersAdapter.notifyDataSetChanged();
			}
		}
		else
		{
			tvName.setText("");
			lvContactNumbers.setAdapter(null);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		getMenuInflater().inflate(R.menu.menu_activity_contact_edit, menu);
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
		if (id == R.id.action_ok)
		{
			finish();
			return true;
		}
		if (id == R.id.action_cancel)
		{
			Toast.makeText(this, "取消编辑联系人！", Toast.LENGTH_LONG).show();
			finish();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onItemClicked(ContactInfo.PhoneInfo phoneInfo)
	{
		if (phoneInfo != null)
		{
			//			DialogContactNumberAdd dialogContactNumberAdd = new DialogContactNumberAdd(this, new DialogContactNumberAdd.OnContactNumberAddListenser()
			//			{
			//				@Override
			//				public void onOkClick(String type, String number)
			//				{
			//				}
			//
			//				@Override
			//				public void onCancelClick()
			//				{
			//				}
			//			});
			//			dialogContactNumberAdd.show();
		}
	}

	@Override
	public void onDeleteClicked(ContactInfo.PhoneInfo phoneInfo)
	{
		if (phoneInfo != null)
		{
			contactInfo.getPhoneInfoList().remove(phoneInfo);
			contactEditNumbersAdapter.notifyDataSetChanged();
		}
	}

	@Override
	public void onClick(View v)
	{
		switch (v.getId())
		{
			case R.id.btn_add:
			{
				DialogContactNumberAdd dialogContactNumberAdd = new DialogContactNumberAdd(this, new DialogContactNumberAdd.OnContactNumberAddListenser()
				{
					@Override
					public void onOkClick(String type, String number)
					{
						contactInfo.getPhoneInfoList().add(contactInfo.new PhoneInfo(number, type));
						contactEditNumbersAdapter.notifyDataSetChanged();
					}

					@Override
					public void onCancelClick()
					{
					}
				});
				dialogContactNumberAdd.show();
				break;
			}
		}
	}
}

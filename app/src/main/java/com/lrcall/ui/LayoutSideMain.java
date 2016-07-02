/*
 * Libit保留所有版权，如有疑问联系QQ：308062035
 * Copyright (c) 2016.
 */
package com.lrcall.ui;

import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.androidquery.callback.AjaxStatus;
import com.lrcall.appcall.R;
import com.lrcall.appcall.models.ReturnInfo;
import com.lrcall.appcall.services.ApiConfig;
import com.lrcall.appcall.services.BackupService;
import com.lrcall.appcall.services.IAjaxDataResponse;
import com.lrcall.appcall.services.UserService;
import com.lrcall.contacts.ContactsFactory;
import com.lrcall.models.ContactInfo;
import com.lrcall.nums.EventTypeLayoutSideMain;
import com.lrcall.ui.customer.ToastView;
import com.lrcall.utils.GsonTools;
import com.lrcall.utils.PreferenceUtils;
import com.lrcall.utils.StringTools;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

/**
 * Created by libit on 16/6/29.
 */

public class LayoutSideMain extends LinearLayout implements View.OnClickListener, IAjaxDataResponse
{
	private Context mContext;
	private View rootView, layoutInfo, btnLogout;
	private TextView tvName, tvBackupCount;
	private UserService mUserService;
	private boolean bLogin = false;

	public LayoutSideMain(Context context)
	{
		super(context);
		this.mContext = context;
		viewInit();
	}

	public LayoutSideMain(Context context, AttributeSet attrs)
	{
		super(context, attrs);
		this.mContext = context;
		viewInit();
	}

	private void viewInit()
	{
		if (mUserService == null)
		{
			mUserService = new UserService(mContext);
		}
		mUserService.addDataResponse(this);
		rootView = LayoutInflater.from(mContext).inflate(R.layout.activity_main_slide_head, null);
		tvName = (TextView) rootView.findViewById(R.id.tv_name);
		tvBackupCount = (TextView) rootView.findViewById(R.id.tv_backup_count);
		layoutInfo = rootView.findViewById(R.id.layout_info);
		btnLogout = rootView.findViewById(R.id.btn_logout);
		rootView.findViewById(R.id.iv_photo).setOnClickListener(this);
		rootView.findViewById(R.id.layout_upload).setOnClickListener(this);
		rootView.findViewById(R.id.layout_change_password).setOnClickListener(this);
		rootView.findViewById(R.id.layout_share).setOnClickListener(this);
		btnLogout.setOnClickListener(this);
		this.addView(rootView);
		setbLogin(false);
		refresh();
	}

	public void refresh()
	{
		String userId = PreferenceUtils.getInstance().getUsername();
		String sessionId = PreferenceUtils.getInstance().getSessionId();
		if (!StringTools.isNull(userId) && !StringTools.isNull(sessionId))
		{
			tvName.setText(userId);
			mUserService.getUserInfo();
		}
		else
		{
			setbLogin(false);
			tvName.setText("请登录！");
		}
//		List<ContactInfo> contactInfoList = ContactsFactory.getInstance().getContactInfos(mContext);
//		tvBackupCount.setText(contactInfoList.size() + "");
	}

	@Override
	public void onClick(View v)
	{
		switch (v.getId())
		{
			case R.id.iv_photo:
			{
				if (isbLogin())
				{
				}
				else
				{
					mContext.startActivity(new Intent(mContext, ActivityLogin.class));
				}
				break;
			}
			case R.id.layout_upload:
			{
				EventBus.getDefault().post(EventTypeLayoutSideMain.CLOSE_DRAWER);
				if (isbLogin())
				{
					List<ContactInfo> contactInfoList = ContactsFactory.getInstance().getContactInfos(mContext);
					String content = GsonTools.toJson(contactInfoList);
					new BackupService(mContext).updateContactBackupInfos(content);
				}
				else
				{
					mContext.startActivity(new Intent(mContext, ActivityLogin.class));
				}
				break;
			}
			case R.id.layout_change_password:
			{
				EventBus.getDefault().post(EventTypeLayoutSideMain.CLOSE_DRAWER);
				if (isbLogin())
				{
					mContext.startActivity(new Intent(mContext, ActivityChangePwd.class));
				}
				else
				{
					mContext.startActivity(new Intent(mContext, ActivityLogin.class));
				}
				break;
			}
			case R.id.layout_share:
			{
				mUserService.share();
				break;
			}
			case R.id.btn_logout:
			{
				EventBus.getDefault().post(EventTypeLayoutSideMain.CLOSE_DRAWER);
				PreferenceUtils.getInstance().setSessionId("");
				mContext.startActivity(new Intent(mContext, ActivityLogin.class));
				break;
			}
		}
	}

	@Override
	public boolean onAjaxDataResponse(String url, String result, AjaxStatus status)
	{
		if (url.endsWith(ApiConfig.GET_USER_INFO))
		{
			ReturnInfo returnInfo = GsonTools.getReturnInfo(result);
			if (!ReturnInfo.isSuccess(returnInfo))
			{
				ToastView.showCenterToast(mContext, "登录信息已过期，请重新登录！");
				tvName.setText("请登录！");
				setbLogin(false);
			}
			else
			{
				setbLogin(true);
			}
		}
		return true;
	}

	public boolean isbLogin()
	{
		return bLogin;
	}

	public void setbLogin(boolean bLogin)
	{
		this.bLogin = bLogin;
		if (bLogin)
		{
			layoutInfo.setVisibility(View.VISIBLE);
			btnLogout.setVisibility(View.VISIBLE);
		}
		else
		{
			layoutInfo.setVisibility(View.GONE);
			btnLogout.setVisibility(View.GONE);
		}
	}
}

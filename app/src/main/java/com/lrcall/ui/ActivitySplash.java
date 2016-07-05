/*
 * Libit保留所有版权，如有疑问联系QQ：308062035
 * Copyright (c) 2016.
 */
package com.lrcall.ui;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.TextView;

import com.lrcall.appcall.R;
import com.lrcall.utils.LogcatTools;
import com.lrcall.utils.apptools.AppFactory;

import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.RuntimePermissions;

@RuntimePermissions
public class ActivitySplash extends Activity
{
	private TextView tvStart;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		final View rootView = View.inflate(this, R.layout.activity_splash, null);
		setContentView(rootView);
		if (AppFactory.isCompatible(23))
		{
			ActivitySplashPermissionsDispatcher.initViewWithCheck(this, rootView);
		}
		else
		{
			initView(rootView);
		}
	}

	@NeedsPermission({Manifest.permission.READ_CONTACTS, Manifest.permission.READ_CALL_LOG, Manifest.permission.WRITE_EXTERNAL_STORAGE})
	protected void initView(View rootView)
	{
		tvStart = (TextView) findViewById(R.id.tv_start);
		AlphaAnimation aa = new AlphaAnimation(0.5f, 1.0f);
		aa.setDuration(200);
		rootView.setAnimation(aa);
		aa.setAnimationListener(new Animation.AnimationListener()
		{
			@Override
			public void onAnimationStart(Animation animation)
			{
			}

			@Override
			public void onAnimationRepeat(Animation animation)
			{
			}

			@Override
			public void onAnimationEnd(Animation animation)
			{
				mHandler.sendEmptyMessage(INIT_RESULT);
			}
		});
		new Thread("splash")
		{
			@Override
			public void run()
			{
				super.run();
				LogcatTools.getInstance().start();
			}
		}.start();
	}

	private final int INIT_RESULT = 1001;
	private final Handler mHandler = new Handler()
	{
		@Override
		public void handleMessage(Message msg)
		{
			super.handleMessage(msg);
			switch (msg.what)
			{
				case INIT_RESULT:
				{
					startActivity(new Intent(ActivitySplash.this, ActivityMain.class));
					finish();
					break;
				}
			}
		}
	};
}

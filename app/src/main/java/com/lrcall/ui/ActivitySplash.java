/*
 * Libit保留所有版权，如有疑问联系QQ：308062035
 * Copyright (c) 2016.
 */
package com.lrcall.ui;

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

public class ActivitySplash extends Activity
{
	private TextView tvStart;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		final View rootView = View.inflate(this, R.layout.activity_splash, null);
		setContentView(rootView);
		initView();//渐变
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
		//		new Thread("splash")
		//		{
		//			@Override
		//			public void run()
		//			{
		//				super.run();
		//				String text = getString(R.string.app_name);
		//				final int tm = 250;
		//				if (!StringTools.isNull(text))
		//				{
		//					int size = text.length();
		//					for (int i = 0; i < size; i++)
		//					{
		//						try
		//						{
		//							Thread.sleep(tm);
		//						}
		//						catch (InterruptedException e)
		//						{
		//							e.printStackTrace();
		//						}
		//						finally
		//						{
		//							//							Message msg = Message.obtain();
		//							//							msg.obj = text.substring(0, i + 1);
		//							//							msg.what = CHANGE_TEXT;
		//							//							mHandler.sendMessage(msg);
		//						}
		//					}
		//					try
		//					{
		//						Thread.sleep(tm);
		//					}
		//					catch (InterruptedException e)
		//					{
		//						e.printStackTrace();
		//					}
		//					finally
		//					{
		//						mHandler.sendEmptyMessage(INIT_RESULT);
		//					}
		//				}
		//				else
		//				{
		//					try
		//					{
		//						Thread.sleep(tm * 4);
		//					}
		//					catch (InterruptedException e)
		//					{
		//						e.printStackTrace();
		//					}
		//					finally
		//					{
		//						mHandler.sendEmptyMessage(INIT_RESULT);
		//					}
		//				}
		//			}
		//		}.start();
	}

	protected void initView()
	{
		tvStart = (TextView) findViewById(R.id.tv_start);
	}

	private final int CHANGE_TEXT = 1000;
	private final int INIT_RESULT = 1001;
	private final String DATA_INIT_RESULT = "data.init.result";
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
				case CHANGE_TEXT:
				{
					String text = (String) msg.obj;
					tvStart.setText(text);
					break;
				}
			}
		}
	};
}

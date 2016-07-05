package com.lrcall.appcall;

import android.app.Application;
import android.content.Context;

import com.lrcall.utils.LogcatTools;

/**
 * Created by libit on 15/8/19.
 */
public class MyApplication extends Application
{
	private static MyApplication instance;

	public static MyApplication getInstance()
	{
		return instance;
	}

	public static Context getContext()
	{
		return getInstance();
	}

	@Override
	public void onCreate()
	{
		super.onCreate();
		instance = this;
	}

	@Override
	public void onTerminate()
	{
		LogcatTools.getInstance().stop();
		instance = null;
		super.onTerminate();
	}
}

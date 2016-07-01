package com.lrcall.utils;

import android.content.Context;

public class DisplayTools
{
	/**
	 * 获取屏幕宽度
	 *
	 * @param context
	 * @return
	 */
	public static int getWindowWidth(Context context)
	{
		final float width = context.getResources().getDisplayMetrics().widthPixels;
		LogcatTools.debug("getWindowWidth", "width:" + width);
		return (int) (width);
	}

	/**
	 * 获取屏幕高度
	 *
	 * @param context
	 * @return
	 */
	public static int getWindowHeight(Context context)
	{
		final float height = context.getResources().getDisplayMetrics().heightPixels;
		LogcatTools.debug("getWindowHeight", "height:" + height);
		return (int) (height);
	}

	/**
	 * dip转px
	 *
	 * @param context
	 * @param dipValue
	 * @return
	 */
	public static int dip2px(Context context, float dipValue)
	{
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dipValue * scale + 0.5f);
	}

	/**
	 * px转dip
	 *
	 * @param context
	 * @param pxValue
	 * @return
	 */
	public static int px2dip(Context context, float pxValue)
	{
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (pxValue / scale + 0.5f);
	}
}

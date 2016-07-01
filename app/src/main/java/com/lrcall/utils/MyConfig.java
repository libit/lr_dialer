package com.lrcall.utils;

import android.os.Build;

import com.lrcall.utils.apptools.AppFactory;

public class MyConfig
{
	/**
	 * 代理ID
	 */
	public static final String AGENT_ID = "libit";
	/**
	 * 平台类型
	 */
	public static final String PLATFORM = "android";
	/**
	 * 程序存放数据的目录
	 */
	public static final String APP_FOLDER = "dialer";
	/**
	 * 数据分享的名字
	 */
	public static final String AUTHORITY_NAME = "lr_dialer";

	public static final String getBackupFileName()
	{
		return Build.MODEL + "_" + Build.DISPLAY + "_" + Build.VERSION.RELEASE + "_" + AppFactory.getInstance().getVersionName() + "_" + StringTools.getCurrentTimeNum() + ".bak";
	}

	/**
	 * 调试模式
	 * 用于显示LOG，签名验证等
	 *
	 * @return
	 */
	public static boolean isDebug()
	{
		return true;
	}

	/**
	 * 获取代理ID
	 *
	 * @return
	 */
	public static String getAgent()
	{
		return AGENT_ID;
	}

	/**
	 * 获取程序存放数据的目录
	 *
	 * @return
	 */
	public static String getSDCardFolder()
	{
		return AGENT_ID + "/" + APP_FOLDER;
	}

	/**
	 * 数据备份目录
	 *
	 * @return
	 */
	public static String getBackupFolder()
	{
		//        return "backup/data/" + Build.MODEL + "_" + Build.DISPLAY + "_" + Build.VERSION.RELEASE;
		return "backup";
	}

	/**
	 * 日志记录的文件夹
	 *
	 * @return
	 */
	public static String getLogcatFolder()
	{
		return "logcat";
	}
}

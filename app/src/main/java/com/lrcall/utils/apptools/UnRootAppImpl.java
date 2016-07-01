package com.lrcall.utils.apptools;

import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;

import com.lrcall.appcall.MyApplication;
import com.lrcall.utils.LogcatTools;
import com.lrcall.utils.ShellUtils;
import com.lrcall.utils.StringTools;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by libit on 15/9/29.
 */
public class UnRootAppImpl implements AppInterface
{
	/**
	 * 启用App
	 *
	 * @param packageName App包名
	 * @return
	 */
	public ShellUtils.CommandResult enableApp(String packageName)
	{
		if (StringTools.isNull(packageName))
		{
			return new ShellUtils.CommandResult(-1, null, "包名不能为空");
		}
		String cmd = String.format("pm unblock %s", packageName);
		return ShellUtils.execCommand(cmd, false, true);
	}

	/**
	 * 启用Apps
	 *
	 * @param packageNames App包名列表
	 * @return
	 */
	@Override
	public ShellUtils.CommandResult enableApps(List<String> packageNames)
	{
		if (packageNames == null || packageNames.size() < 1)
		{
			return new ShellUtils.CommandResult(-1, null, "包名不能为空");
		}
		List<String> cmds = new ArrayList<String>();
		for (String packageName : packageNames)
		{
			if (StringTools.isNull(packageName))
			{
				continue;
			}
			String cmd = String.format("pm unblock %s", packageName);
			cmds.add(cmd);
		}
		return ShellUtils.execCommand(cmds, false, true);
	}

	/**
	 * 禁用App
	 *
	 * @param packageName App包名
	 * @return
	 */
	public ShellUtils.CommandResult disableApp(String packageName)
	{
		if (StringTools.isNull(packageName))
		{
			return new ShellUtils.CommandResult(-1, null, "包名不能为空");
		}
		String cmd = String.format("pm block %s", packageName);
		return ShellUtils.execCommand(cmd, false, true);
	}

	/**
	 * 禁用Apps
	 *
	 * @param packageNames App包名列表
	 * @return
	 */
	@Override
	public ShellUtils.CommandResult disableApps(List<String> packageNames)
	{
		if (packageNames == null || packageNames.size() < 1)
		{
			return new ShellUtils.CommandResult(-1, null, "包名不能为空");
		}
		List<String> cmds = new ArrayList<String>();
		for (String packageName : packageNames)
		{
			if (StringTools.isNull(packageName))
			{
				continue;
			}
			String cmd = String.format("pm block %s", packageName);
			cmds.add(cmd);
		}
		return ShellUtils.execCommand(cmds, false, true);
	}

	/**
	 * 启动应用程序
	 *
	 * @param packageName 应用包名
	 * @return 启动成功返回true，失败返回false
	 */
	public boolean startApp(String packageName)
	{
		if (StringTools.isNull(packageName))
		{
			return false;
		}
		boolean isFind = false;
		PackageManager packageManager = MyApplication.getContext().getPackageManager();
		PackageInfo packageInfo = null;
		try
		{
			packageInfo = packageManager.getPackageInfo(packageName, 0);
		}
		catch (PackageManager.NameNotFoundException e)
		{
			e.printStackTrace();
		}
		if (packageInfo == null)
		{
			return isFind;
		}
		Intent intent = new Intent(Intent.ACTION_MAIN, null);
		intent.addCategory(Intent.CATEGORY_LAUNCHER);
		intent.setPackage(packageName);
		List<ResolveInfo> resolveInfoList = packageManager.queryIntentActivities(intent, 0);
		while (resolveInfoList.iterator().hasNext())
		{
			ResolveInfo resolveInfo = resolveInfoList.iterator().next();
			if (resolveInfo != null)
			{
				String className = resolveInfo.activityInfo.name;
				intent.setComponent(new ComponentName(packageName, className));
				intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				MyApplication.getContext().startActivity(intent);
				isFind = true;
				LogcatTools.debug("startApp", packageName + "启动class：" + className);
				break;
			}
		}
		return isFind;
	}

	/**
	 * 关闭正在运行的程序信息
	 *
	 * @param packageName
	 * @return
	 */
	public ShellUtils.CommandResult killApp(String packageName)
	{
		if (StringTools.isNull(packageName))
		{
			return new ShellUtils.CommandResult(-1, null, "包名不能为空");
		}
		ActivityManager activityManager = (ActivityManager) MyApplication.getContext().getSystemService(Context.ACTIVITY_SERVICE);
		activityManager.killBackgroundProcesses(packageName);
		return new ShellUtils.CommandResult(0, "成功", null);
	}

	/**
	 * 安装App
	 *
	 * @param file 安装包
	 */
	public void installApp(File file)
	{
		if (file == null)
		{
			return;
		}
		Intent intent = new Intent(Intent.ACTION_VIEW);
		intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		MyApplication.getContext().startActivity(intent);
	}

	/**
	 * 卸载App
	 *
	 * @param packageName App包名
	 */
	public void uninstallApp(String packageName)
	{
		if (StringTools.isNull(packageName))
		{
			return;
		}
		Uri uri = Uri.parse("package:" + packageName);
		Intent intent = new Intent();
		intent.setAction(Intent.ACTION_DELETE);
		intent.setData(uri);
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		MyApplication.getContext().startActivity(intent);
	}
}

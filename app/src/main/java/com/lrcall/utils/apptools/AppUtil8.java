package com.lrcall.utils.apptools;

import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;

import com.lrcall.appcall.MyApplication;
import com.lrcall.models.AppInfo;
import com.lrcall.appcall.models.ErrorInfo;
import com.lrcall.models.MemoryInfo;
import com.lrcall.appcall.models.ReturnInfo;
import com.lrcall.utils.ConstValues;
import com.lrcall.utils.GsonTools;
import com.lrcall.utils.LogcatTools;
import com.lrcall.utils.ShellUtils;
import com.lrcall.utils.StringTools;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Created by libit on 15/8/19.
 */
public class AppUtil8 extends AppFactory
{
	@Override
	public List<AppInfo> getApps(int type, int status)
	{
		long t0 = System.currentTimeMillis();
		ArrayList<AppInfo> appInfos = new ArrayList<>();
		PackageManager packageManager = MyApplication.getContext().getPackageManager();
		List<PackageInfo> packageInfos = packageManager.getInstalledPackages(0);
		//        PackageInfo myPackageInfo = null;// 程序自身的包信息
		//        try
		//        {
		//            myPackageInfo = getSelfPackageInfo();// 程序自身的包信息
		//        }
		//        catch (PackageManager.NameNotFoundException e)
		//        {
		//        }
		int count = packageInfos.size();
		for (int i = 0; i < count; i++)
		{
			PackageInfo packageInfo = packageInfos.get(i);
			ApplicationInfo applicationInfo = packageInfo.applicationInfo;
			//            if (myPackageInfo.packageName == packageInfo.packageName)// 如果是自身应用，则跳过
			//            {
			//                continue;
			//            }
			int appType = (applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) > 0 ? ConstValues.TYPE_SYSTEM : ConstValues.TYPE_USER;
			int appStatus = applicationInfo.enabled ? ConstValues.STATUS_ENABLED : ConstValues.STATUS_DISABLED;
			// 如果要查询的是用户程序
			if (type != ConstValues.TYPE_ALL && appType != type)
			{
				continue;
			}
			// 要查询的状态
			if (status != ConstValues.STATUS_ALL && appStatus != status)
			{
				continue;
			}
			AppInfo appInfo = new AppInfo();
			appInfo.setUid(applicationInfo.uid + "");
			appInfo.setName(applicationInfo.loadLabel(packageManager).toString());
			appInfo.setPackageName(applicationInfo.packageName);
			//			try
			//			{
			//				appInfo.setPhoto(((BitmapDrawable) applicationInfo.loadIcon(packageManager)).getBitmap());
			//			}
			//			catch (ClassCastException e)
			//			{
			//				MyConfig.showLog("ClassCastException", "\"" + appInfo.getName() + "\"强制转换图片失败了！");
			//			}
			appInfo.setType(appType);
			appInfo.setIsEnabled(applicationInfo.enabled);
			LogcatTools.debug("appInfo", appInfo.toString());
			appInfos.add(appInfo);
		}
		long t1 = System.currentTimeMillis();
		Collections.sort(appInfos, new AppInfo());
		long t2 = System.currentTimeMillis();
		LogcatTools.debug("getApps", "总花费时间：" + (t2 - t0) + "毫秒,排序花费时间：" + (t2 - t1) + "毫秒");
		return appInfos;
	}

	/**
	 * 获取App信息
	 *
	 * @param packageName App的packageName
	 * @param showPhoto   是否显示图片
	 * @return App对象
	 */
	@Override
	public AppInfo getAppInfoByPackageName(String packageName, boolean showPhoto)
	{
		AppInfo appInfo = new AppInfo();
		PackageManager packageManager = MyApplication.getContext().getPackageManager();
		try
		{
			PackageInfo packageInfo = packageManager.getPackageInfo(packageName, 0);
			ApplicationInfo applicationInfo = packageInfo.applicationInfo;
			int appType = (applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) > 0 ? ConstValues.TYPE_SYSTEM : ConstValues.TYPE_USER;
			appInfo.setUid(applicationInfo.uid + "");
			appInfo.setName(applicationInfo.loadLabel(packageManager).toString());
			appInfo.setPackageName(applicationInfo.packageName);
			if (showPhoto)
			{
				try
				{
					appInfo.setPhoto(((BitmapDrawable) applicationInfo.loadIcon(packageManager)).getBitmap());
				}
				catch (ClassCastException e)
				{
					LogcatTools.debug("ClassCastException", "\"" + appInfo.getName() + "\"强制转换图片失败了！");
				}
			}
			appInfo.setType(appType);
			appInfo.setIsEnabled(applicationInfo.enabled);
			appInfo.setIsExist(true);
			LogcatTools.debug("appInfo", appInfo.toString());
		}
		catch (PackageManager.NameNotFoundException e)
		{
			LogcatTools.debug("NameNotFoundException", "包名" + packageName + "\"未找到！");
		}
		return appInfo;
	}

	/**
	 * 获取自身App包信息
	 *
	 * @return App对象
	 */
	@Override
	public PackageInfo getSelfPackageInfo() throws PackageManager.NameNotFoundException
	{
		PackageManager packageManager = MyApplication.getContext().getPackageManager();
		PackageInfo info = packageManager.getPackageInfo(MyApplication.getContext().getPackageName(), 0);
		return info;
	}

	/**
	 * 启动应用程序
	 *
	 * @param packageName 应用包名
	 * @return 启动成功返回true，失败返回false
	 */
	@Override
	public boolean startApp(String packageName)
	{
		if (StringTools.isNull(packageName))
		{
			return false;
		}
		return appInterface.startApp(packageName);
	}

	/**
	 * 获取正在运行的程序列表
	 *
	 * @return App列表
	 */
	@Override
	public List<AppInfo> getRunningApps()
	{
		PackageInfo myPackageInfo = null;// 程序自身的包信息
		try
		{
			myPackageInfo = getSelfPackageInfo();// 程序自身的包信息
		}
		catch (PackageManager.NameNotFoundException e)
		{
		}
		ArrayList<AppInfo> appInfos = new ArrayList<>();
		ActivityManager activityManager = (ActivityManager) MyApplication.getContext().getSystemService(Context.ACTIVITY_SERVICE);
		List<ActivityManager.RunningAppProcessInfo> runningAppProcessInfoList = new CopyOnWriteArrayList<>(activityManager.getRunningAppProcesses());
		for (ActivityManager.RunningAppProcessInfo runningAppProcessInfo : runningAppProcessInfoList)
		{
			LogcatTools.debug("runningAppProcessInfo", "uid:" + runningAppProcessInfo.uid + ",processName:" + runningAppProcessInfo.processName);
			for (String packageName : runningAppProcessInfo.pkgList)
			{
				if (myPackageInfo != null && myPackageInfo.packageName.equals(packageName))// 如果是自身，则跳过
				{
					continue;
				}
				// 如果已存在，则跳过继续
				boolean isExist = false;
				for (AppInfo appInfo : appInfos)
				{
					if (appInfo.getPackageName().equals(packageName))
					{
						isExist = true;
						break;
					}
				}
				if (isExist)
				{
					continue;
				}
				AppInfo newAppInfo = AppFactory.getInstance().getAppInfoByPackageName(packageName, true);
				if (newAppInfo != null)
				{
					LogcatTools.debug("runningAppProcessInfo", "newAppInfo:" + newAppInfo.toString());
					appInfos.add(newAppInfo);
				}
			}
		}
		return appInfos;
	}

	/**
	 * 安装App
	 *
	 * @param file 安装包
	 */
	@Override
	public void installApp(File file, boolean root)
	{
		if (root)
		{
			new RootAppImpl().installApp(file);
		}
		else
		{
			new UnRootAppImpl().installApp(file);
		}
	}

	/**
	 * 卸载App
	 *
	 * @param packageName App包名
	 */
	@Override
	public void uninstallApp(String packageName, boolean root)
	{
		if (root)
		{
			new RootAppImpl().uninstallApp(packageName);
		}
		else
		{
			new UnRootAppImpl().uninstallApp(packageName);
		}
	}

	/**
	 * 启用App
	 *
	 * @param packageName    App包名
	 * @param deleteBlackApp 是否在黑名单中删除
	 * @return
	 */
	@Override
	public ShellUtils.CommandResult enableApp(String packageName, boolean deleteBlackApp)
	{
		if (StringTools.isNull(packageName))
		{
			return new ShellUtils.CommandResult(-1, null, "包名不能为空");
		}
		AppInfo appInfo1 = getAppInfoByPackageName(packageName, false);
		boolean enabled = false;
		ShellUtils.CommandResult result = null;
		if (appInfo1 != null && !appInfo1.isEnabled())
		{
			result = appInterface.enableApp(packageName);
			if (result.result == 0)// 启用成功
			{
				enabled = true;
			}
		}
		else
		{
			result = new ShellUtils.CommandResult(0, "已经启用", null);
			enabled = true;
		}
		return result;
	}

	/**
	 * 启用App
	 *
	 * @param packageNames   App包名列表
	 * @param deleteBlackApp 是否在黑名单中删除
	 * @return
	 */
	@Override
	public ShellUtils.CommandResult enableApps(List<String> packageNames, boolean deleteBlackApp)
	{
		if (packageNames == null || packageNames.size() < 1)
		{
			return new ShellUtils.CommandResult(-1, null, "包名不能为空");
		}
		boolean enabled = false;
		List<String> disPackages = new ArrayList<>();
		for (String packageName : packageNames)
		{
			if (StringTools.isNull(packageName))
			{
				continue;
			}
			AppInfo appInfo1 = getAppInfoByPackageName(packageName, false);
			if (appInfo1 == null || !appInfo1.isEnabled())
			{
				LogcatTools.debug("enableApps", "disPackages:" + packageName);
				disPackages.add(packageName);
			}
		}
		ShellUtils.CommandResult result = appInterface.enableApps(disPackages);
		if (result.result == 0)// 启用成功
		{
			enabled = true;
		}
		return result;
	}

	/**
	 * 禁用App
	 *
	 * @param packageName App包名
	 * @return
	 */
	@Override
	public ShellUtils.CommandResult disableApp(String packageName)
	{
		if (StringTools.isNull(packageName))
		{
			return new ShellUtils.CommandResult(-1, null, "包名不能为空");
		}
		AppInfo appInfo = getAppInfoByPackageName(packageName, false);
		boolean disabled = false;
		ShellUtils.CommandResult result = null;
		if (appInfo != null && appInfo.isEnabled())
		{
			result = appInterface.disableApp(packageName);
			if (result.result == 0)// 禁用成功
			{
				disabled = true;
			}
		}
		else
		{
			result = new ShellUtils.CommandResult(0, "已经禁用", null);
			disabled = true;
		}
		return result;
	}

	/**
	 * 禁用App
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
		boolean disabled = false;
		List<String> enablePackages = new ArrayList<>();
		for (String packageName : packageNames)
		{
			if (StringTools.isNull(packageName))
			{
				continue;
			}
			AppInfo appInfo1 = getAppInfoByPackageName(packageName, false);
			if (appInfo1 == null || appInfo1.isEnabled())
			{
				enablePackages.add(packageName);
			}
		}
		ShellUtils.CommandResult result = appInterface.disableApps(enablePackages);
		if (result.result == 0)// 启用成功
		{
			disabled = true;
		}
		return result;
	}

	/**
	 * 关闭正在运行的程序信息
	 *
	 * @param packageName App包名
	 * @return
	 */
	@Override
	public ShellUtils.CommandResult killApp(String packageName)
	{
		if (StringTools.isNull(packageName))
		{
			return new ShellUtils.CommandResult(-1, null, "包名不能为空");
		}
		return appInterface.killApp(packageName);
	}

	/**
	 * 获取程序的版本名称
	 *
	 * @return 版本名称
	 */
	@Override
	public String getVersionName()
	{
		try
		{
			PackageInfo packageInfo = getSelfPackageInfo();
			String versionName = packageInfo.versionName;
			return versionName;
		}
		catch (PackageManager.NameNotFoundException e)
		{
			e.printStackTrace();
			return "未知";
		}
	}

	/**
	 * 获取程序的版本号
	 *
	 * @return 版本号
	 */
	@Override
	public int getVersionCode()
	{
		try
		{
			PackageInfo packageInfo = getSelfPackageInfo();
			int versionCode = packageInfo.versionCode;
			return versionCode;
		}
		catch (PackageManager.NameNotFoundException e)
		{
			e.printStackTrace();
			return 0;
		}
	}

	/**
	 * 获取程序的证书信息
	 *
	 * @return 证书信息
	 */
	@Override
	public String getCertInfo()
	{
		PackageManager pm = MyApplication.getContext().getPackageManager();
		String packageName = MyApplication.getContext().getPackageName();
		PackageInfo packageInfo = null;
		try
		{
			packageInfo = pm.getPackageInfo(packageName, PackageManager.GET_SIGNATURES);
		}
		catch (PackageManager.NameNotFoundException e)
		{
			e.printStackTrace();
			return "libit";
		}
		Signature[] signatures = packageInfo.signatures;
		String str = "";
		for (int i = 0; i < signatures.length; i++)
		{
			str += signatures[i].toCharsString();
		}
		return str;
	}

	/**
	 * 获取手机设备名称
	 *
	 * @return
	 */
	@Override
	public String getDeviceName()
	{
		return Build.MODEL;
	}

	/**
	 * 获取系统版本号
	 *
	 * @return
	 */
	@Override
	public String getSysVersion()
	{
		return Build.VERSION.RELEASE;
	}

	/**
	 * 获取内存信息
	 *
	 * @return
	 */
	@Override
	public String getMemoryInfo()
	{
		MemoryInfo memoryInfo = new MemoryInfo();
		ActivityManager activityManager = (ActivityManager) MyApplication.getContext().getSystemService(Context.ACTIVITY_SERVICE);
		ActivityManager.MemoryInfo outMemoryInfo = new ActivityManager.MemoryInfo();
		activityManager.getMemoryInfo(outMemoryInfo);
		memoryInfo.setUnusedMemory(outMemoryInfo.availMem);
		if (isCompatible(16))
		{
			memoryInfo.setTotalMemory(outMemoryInfo.totalMem);
		}
		else
		{
		}
		String json = new ReturnInfo(ErrorInfo.getSuccessId(), GsonTools.toJson(memoryInfo)).toString();
		LogcatTools.debug("getMemoryInfo", "json:" + json);
		return json;
	}
}

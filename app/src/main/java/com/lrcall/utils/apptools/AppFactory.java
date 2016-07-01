package com.lrcall.utils.apptools;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import com.lrcall.models.AppInfo;
import com.lrcall.utils.ShellUtils;

import java.io.File;
import java.util.List;

/**
 * Created by libit on 15/8/19.
 */
public abstract class AppFactory
{
	private static AppFactory appInstance;
	protected AppInterface appInterface;

	protected AppFactory()
	{
//		if (ShellUtils.checkRootPermission())
//			appInterface = new RootAppImpl();
//		else
			appInterface = new UnRootAppImpl();
	}

	synchronized public static AppFactory getInstance()
	{
		if (appInstance == null)
		{
			if (AppFactory.isCompatible(22))
			{
				appInstance = new AppUtil22();
			}
			else if (AppFactory.isCompatible(8))
			{
				appInstance = new AppUtil8();
			}
		}
		return appInstance;
	}

	/**
	 * 判断设备API是否兼容指定的版本号
	 *
	 * @param apiLevel 指定的版本号
	 * @return 兼容返回true，否则返回false
	 */
	public static boolean isCompatible(int apiLevel)
	{
		return android.os.Build.VERSION.SDK_INT >= apiLevel;
	}

	/**
	 * 获取API版本号
	 *
	 * @return API版本号
	 */
	public int getApiLevel()
	{
		return android.os.Build.VERSION.SDK_INT;
	}

	/**
	 * 启用App
	 *
	 * @param packageName    App包名
	 * @param deleteBlackApp 是否在黑名单中删除
	 * @return
	 */
	public abstract ShellUtils.CommandResult enableApp(String packageName, boolean deleteBlackApp);

	/**
	 * 启用App
	 *
	 * @param packageNames   App包名列表
	 * @param deleteBlackApp 是否在黑名单中删除
	 * @return
	 */
	public abstract ShellUtils.CommandResult enableApps(List<String> packageNames, boolean deleteBlackApp);

	/**
	 * 禁用App
	 *
	 * @param packageName App包名
	 * @return
	 */
	public abstract ShellUtils.CommandResult disableApp(String packageName);

	/**
	 * 禁用App
	 *
	 * @param packageNames App包名列表
	 * @return
	 */
	public abstract ShellUtils.CommandResult disableApps(List<String> packageNames);

	/**
	 * 安装App
	 *
	 * @param file 安装包
	 */
	public abstract void installApp(File file, boolean root);

	/**
	 * 卸载App
	 *
	 * @param packageName App包名
	 */
	public abstract void uninstallApp(String packageName, boolean root);

	/**
	 * 关闭正在运行的程序信息
	 *
	 * @param packageName
	 * @return
	 */
	public abstract ShellUtils.CommandResult killApp(String packageName);

	/**
	 * 获取App列表
	 *
	 * @param type   App类型
	 * @param status App状态
	 * @return App列表
	 */
	public abstract List<AppInfo> getApps(int type, int status);

	/**
	 * 获取App信息
	 *
	 * @param packageName App的packageName
	 * @param showPhoto   是否显示图片
	 * @return App对象
	 */
	public abstract AppInfo getAppInfoByPackageName(String packageName, boolean showPhoto);

	/**
	 * 获取自身App包信息
	 *
	 * @return App对象
	 */
	public abstract PackageInfo getSelfPackageInfo() throws PackageManager.NameNotFoundException;

	/**
	 * 启动应用程序
	 *
	 * @param packageName 应用包名
	 * @return 启动成功返回true，失败返回false
	 */
	public abstract boolean startApp(String packageName);

	/**
	 * 获取正在运行的程序列表
	 *
	 * @return App列表
	 */
	public abstract List<AppInfo> getRunningApps();

	/**
	 * 获取程序的版本名称
	 *
	 * @return 版本名称
	 */
	public abstract String getVersionName();

	/**
	 * 获取程序的版本号
	 *
	 * @return 版本号
	 */
	public abstract int getVersionCode();

	/**
	 * 获取程序的证书信息
	 *
	 * @return 证书信息
	 */
	public abstract String getCertInfo();

	/**
	 * 获取手机设备名称
	 *
	 * @return
	 */
	public abstract String getDeviceName();

	/**
	 * 获取系统版本号
	 *
	 * @return
	 */
	public abstract String getSysVersion();

	/**
	 * 获取内存信息
	 *
	 * @return
	 */
	public abstract String getMemoryInfo();
}

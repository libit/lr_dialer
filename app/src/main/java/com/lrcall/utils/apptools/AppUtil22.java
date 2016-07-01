/*
 * Libit保留所有版权，如有疑问联系QQ：308062035
 * Copyright (c) 2016.
 */
package com.lrcall.utils.apptools;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import com.lrcall.models.AppInfo;
import com.lrcall.utils.LogcatTools;
import com.lrcall.utils.StringTools;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by libit on 16/4/12.
 */
public class AppUtil22 extends AppUtil8
{
	/** first app user */
	public static final int AID_APP = 10000;
	/** offset for uid ranges for each user */
	public static final int AID_USER = 100000;

	private static String read(String path) throws IOException
	{
		StringBuilder output = new StringBuilder();
		BufferedReader reader = new BufferedReader(new FileReader(path));
		output.append(reader.readLine());
		for (String line = reader.readLine(); line != null; line = reader.readLine())
		{
			output.append('\n').append(line);
		}
		reader.close();
		return output.toString().trim();//不调用trim()，包名后面会带有乱码
	}

	@Override
	public List<AppInfo> getRunningApps()
	{
		ArrayList<AppInfo> appInfos = new ArrayList<>();
		File[] files = new File("/proc").listFiles();
		int lowestOomScore = Integer.MAX_VALUE;
		String foregroundProcess = null;
		PackageInfo myPackageInfo = null;// 程序自身的包信息
		try
		{
			myPackageInfo = getSelfPackageInfo();// 程序自身的包信息
		}
		catch (PackageManager.NameNotFoundException e)
		{
		}
		for (File file : files)
		{
			if (!file.isDirectory())
			{
				continue;
			}
			int pid;
			try
			{
				pid = Integer.parseInt(file.getName());
			}
			catch (NumberFormatException e)
			{
				continue;
			}
			try
			{
				String cgroup = read(String.format("/proc/%d/cgroup", pid));
				String[] lines = cgroup.split("\n");
				String cpuSubsystem;
				String cpuaccctSubsystem;
				if (lines.length == 2)
				{
					//有的手机里cgroup包含2行或者3行，我们取cpu和cpuacct两行数据
					cpuSubsystem = lines[0];
					cpuaccctSubsystem = lines[1];
				}
				else if (lines.length == 3)
				{
					cpuSubsystem = lines[0];
					cpuaccctSubsystem = lines[2];
				}
				else
				{
					continue;
				}
				if (!cpuaccctSubsystem.endsWith(Integer.toString(pid)))
				{
					// not an application process
					continue;
				}
				if (cpuSubsystem.endsWith("bg_non_interactive"))
				{
					// background policy
					continue;
				}
				String cmdline = read(String.format("/proc/%d/cmdline", pid));
				LogcatTools.debug("packageName11", "cmdLine:" + cmdline);
				if (cmdline.contains("com.android.systemui") || cmdline.contains(myPackageInfo.packageName))
				{
					continue;
				}
				AppInfo appInfo = getAppInfoByPackageName(cmdline, true);
				if (appInfo != null && !StringTools.isNull(appInfo.getPackageName()))
				{
					appInfos.add(appInfo);
				}
				//				int uid = Integer.parseInt(cpuaccctSubsystem.split(":")[2].split("/")[1].replace("uid_", ""));
				//				if (uid >= 1000 && uid <= 1038)
				//				{
				//					// system process
				//					continue;
				//				}
				//				int appId = uid - AID_APP;
				//				int userId = 0;
				//				// loop until we get the correct user id.
				//				// 100000 is the offset for each user.
				//				while (appId > AID_USER)
				//				{
				//					appId -= AID_USER;
				//					userId++;
				//				}
				//				if (appId < 0)
				//				{
				//					continue;
				//				}
				//				// u{user_id}_a{app_id} is used on API 17+ for multiple user account support.
				//				// String uidName = String.format("u%d_a%d", userId, appId);
				//				File oomScoreAdj = new File(String.format("/proc/%d/oom_score_adj", pid));
				//				if (oomScoreAdj.canRead())
				//				{
				//					int oomAdj = Integer.parseInt(read(oomScoreAdj.getAbsolutePath()));
				//					if (oomAdj != 0)
				//					{
				//						continue;
				//					}
				//				}
				//				int oomscore = Integer.parseInt(read(String.format("/proc/%d/oom_score", pid)));
				//				if (oomscore < lowestOomScore)
				//				{
				//					lowestOomScore = oomscore;
				//					foregroundProcess = cmdline;
				//				}
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
		}
		return appInfos;
	}
}

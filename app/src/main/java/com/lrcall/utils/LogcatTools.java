package com.lrcall.utils;

import android.util.Log;

import com.lrcall.utils.apptools.AppFactory;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * 日志记录类
 */
public class LogcatTools implements Thread.UncaughtExceptionHandler
{
	private static final String TAG = LogcatTools.class.getSimpleName();
	private static LogcatTools instance = null;
	private Thread.UncaughtExceptionHandler mDefaultHandler;// 系统默认的UncaughtException处理类
	private LogDumper mLogDumper = null;
	private int mPId;
	private String fileName;

	private LogcatTools()
	{
		init();
		mPId = android.os.Process.myPid();
	}

	public static LogcatTools getInstance()
	{
		if (instance == null)
		{
			instance = new LogcatTools();
		}
		return instance;
	}

	public static void debug(String tag, String msg)
	{
		if (MyConfig.isDebug())
		{
			Log.d(tag, msg);
		}
	}

	public static void info(String tag, String msg)
	{
		//		if (MyConfig.isDebug())
		{
			Log.d(tag, msg);
		}
	}

	public static void error(String tag, String msg)
	{
		//		if (MyConfig.isDebug())
		{
			Log.e(tag, msg);
		}
	}

	private static String getFileName()
	{
		return "V" + AppFactory.getInstance().getVersionCode() + "_" + StringTools.getCurrentTimeNum() + ".log";
	}

	/**
	 * 初始化目录
	 */
	private void init()
	{
		mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();// 获取系统默认的UncaughtException处理器
		Thread.setDefaultUncaughtExceptionHandler(this);// 设置该CrashHandler为程序的默认处理器
	}

	/**
	 * 当UncaughtException发生时会转入该重写的方法来处理
	 */
	public void uncaughtException(Thread thread, Throwable ex)
	{
		if (!handleException(ex) && mDefaultHandler != null)
		{
			// 如果自定义的没有处理则让系统默认的异常处理器来处理
			mDefaultHandler.uncaughtException(thread, ex);
		}
	}

	/**
	 * 自定义错误处理,收集错误信息 发送错误报告等操作均在此完成.
	 *
	 * @param ex 异常信息
	 * @return true 如果处理了该异常信息;否则返回false.
	 */
	public boolean handleException(Throwable ex)
	{
		if (ex == null)
		{
			return false;
		}
		// 保存出错日志
		saveCrashFile();
		//        android.os.Process.killProcess(android.os.Process.myPid());
		//        new DialogCommon(context, null, "提示", "哦哦，出错了！", true, false, false).show();
		//        Toast.makeText(context, "哦哦，出错了！", Toast.LENGTH_LONG).show();
		return false;
	}

	public void start()
	{
		if (mLogDumper == null)
		{
			mLogDumper = new LogDumper(String.valueOf(mPId));
		}
		mLogDumper.start();
		//只保留最近3个log日志
		String dir = FileTools.getDir(MyConfig.getLogcatFolder());
		File directory = new File(dir);
		if (directory.isDirectory())
		{
			File[] files = directory.listFiles();
			int count = files.length;
			final int MAX_SAVE = 3;
			if (count > MAX_SAVE)
			{
				List<File> saveFiles = new ArrayList<File>();
				for (int i = 0; i < count; i++)
				{
					saveFiles.add(i, files[i]);
					for (int j = i; j > 0; j--)
					{
						try
						{
							String fileName1 = saveFiles.get(j).getName();
							long date1 = Long.parseLong(fileName1.substring(fileName1.lastIndexOf("_") + 1, fileName1.lastIndexOf(".")));
							String fileName2 = saveFiles.get(j - 1).getName();
							long date2 = Long.parseLong(fileName2.substring(fileName2.lastIndexOf("_") + 1, fileName2.lastIndexOf(".")));
							if (date1 > date2)
							{
								File tmp = saveFiles.get(j - 1);
								saveFiles.set(j - 1, saveFiles.get(j));
								saveFiles.set(j, tmp);
							}
							else
							{
								break;
							}
						}
						catch (Exception e)
						{
							saveFiles.remove(i);
						}
					}
				}
				LogcatTools.debug("saveFiles", "saveFiles:" + GsonTools.toJson(saveFiles));
				for (int i = saveFiles.size() - 1; i >= MAX_SAVE; i--)
				{
					if (saveFiles.get(i).isFile())
					{
						LogcatTools.debug("saveFiles", "delete:" + GsonTools.toJson(saveFiles.get(i)));
						saveFiles.get(i).delete();
					}
				}
			}
		}
	}

	public void stop()
	{
		if (mLogDumper != null)
		{
			mLogDumper.stopLogs();
			mLogDumper = null;
		}
		LogcatTools.debug(TAG + "stop", "停止纪录日志！");
	}

	/**
	 * 保存当前记录的日志为出错日志
	 */
	public void saveCrashFile()
	{
		if (!StringTools.isNull(fileName))
		{
			PreferenceUtils.getInstance().setStringValue(PreferenceUtils.PREF_CRASH_FILE, fileName);
		}
	}

	// 抓取日志，并写入文件
	private class LogDumper extends Thread
	{
		String cmds = null;
		private Process logcatProc;
		private BufferedReader mReader = null;
		private boolean mRunning = true;
		private String mPID;
		private FileOutputStream out = null;

		public LogDumper(String pid)
		{
			mPID = pid;
			/**
			 * 日志等级：*:v , *:d , *:w , *:e , *:f , *:s 显示当前mPID程序的 E和W等级的日志.
			 */
			int level = PreferenceUtils.getInstance().getIntegerValue(PreferenceUtils.LOGCAT_LEVEL);
			if (level == PreferenceUtils.LEVEL_0)
			{
				cmds = "";
			}
			else
			{
				if (level == PreferenceUtils.LEVEL_1)
				{
					cmds = "logcat *:e | grep \"(" + mPID + ")\"";
				}
				else if (level == PreferenceUtils.LEVEL_2)
				{
					cmds = "logcat *:e *:w | grep \"(" + mPID + ")\"";
				}
				else if (level == PreferenceUtils.LEVEL_3)
				{
					cmds = "logcat *:e *:w *:i | grep \"(" + mPID + ")\"";
				}
				else if (level == PreferenceUtils.LEVEL_4)
				{
					cmds = "logcat *:e *:w *:i *:d | grep \"(" + mPID + ")\"";
				}
				else if (level == PreferenceUtils.LEVEL_5)
				{
					cmds = "logcat | grep \"(" + mPID + ")\"";
				}
				//            cmds = "logcat | grep \"(" + mPID + ")\"";// 打印所有日志信息
				// cmds = "logcat -s way";//打印标签过滤信息
				// cmds = "logcat *:e *:i | grep \"(" + mPID + ")\"";
				try
				{
					fileName = getFileName();
					out = new FileOutputStream(FileTools.getFile(MyConfig.getLogcatFolder(), fileName));
				}
				catch (FileNotFoundException e)
				{
					e.printStackTrace();
				}
			}
		}

		public void stopLogs()
		{
			mRunning = false;
		}

		@Override
		public void run()
		{
			try
			{
				if (!StringTools.isNull(cmds))
				{
					logcatProc = Runtime.getRuntime().exec(cmds);
					mReader = new BufferedReader(new InputStreamReader(logcatProc.getInputStream()), 10240);
					String line = null;
					while (mRunning && (line = mReader.readLine()) != null)
					{
						if (!mRunning)
						{
							break;
						}
						if (line.length() == 0)
						{
							continue;
						}
						if (out != null && line.contains(mPID))
						{
							out.write((StringTools.getCurrentTime() + "  " + line + "\n").getBytes());
						}
					}
				}
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
			finally
			{
				if (logcatProc != null)
				{
					logcatProc.destroy();
					logcatProc = null;
				}
				if (mReader != null)
				{
					try
					{
						mReader.close();
						mReader = null;
					}
					catch (IOException e)
					{
						e.printStackTrace();
					}
				}
				if (out != null)
				{
					try
					{
						out.close();
					}
					catch (IOException e)
					{
						e.printStackTrace();
					}
					out = null;
				}
			}
		}
	}
}

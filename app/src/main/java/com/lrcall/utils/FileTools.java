package com.lrcall.utils;

import android.os.Environment;

import com.lrcall.appcall.MyApplication;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Created by libit on 15/9/1.
 */
public class FileTools
{
	/**
	 * 创建文件
	 *
	 * @param dir
	 * @param fileName
	 * @return
	 */
	public static File getFile(String dir, String fileName)
	{
		if (StringTools.isNull(fileName))
		{
			return null;
		}
		if (StringTools.isNull(dir))
		{
			dir = "";
		}
		if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED))
		{
			// 优先保存到SD卡中
			dir = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + MyConfig.getSDCardFolder() + File.separator + dir;
			if (!dir.endsWith(File.separator))
			{
				dir += File.separator;
			}
		}
		else
		{
			// 如果SD卡不存在，就保存到本应用的目录下
			dir = MyApplication.getContext().getFilesDir().getAbsolutePath() + File.separator + MyConfig.getSDCardFolder() + File.separator + dir;
			if (!dir.endsWith(File.separator))
			{
				dir += File.separator;
			}
		}
		if (!createDir(dir))// 创建文件夹失败
		{
			return null;
		}
		File file = new File(dir, fileName);
		return file;
	}

	/**
	 * 取得文件夹路径
	 *
	 * @param dir
	 * @return
	 */
	public static String getDir(String dir)
	{
		if (StringTools.isNull(dir))
		{
			dir = "";
		}
		if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED))
		{
			// 优先保存到SD卡中
			dir = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + MyConfig.getSDCardFolder() + File.separator + dir;
			if (!dir.endsWith(File.separator))
			{
				dir += File.separator;
			}
		}
		else
		{
			// 如果SD卡不存在，就保存到本应用的目录下
			dir = MyApplication.getContext().getFilesDir().getAbsolutePath() + File.separator + MyConfig.getSDCardFolder() + File.separator + dir;
			if (!dir.endsWith(File.separator))
			{
				dir += File.separator;
			}
		}
		if (!createDir(dir))// 创建文件夹失败
		{
			return null;
		}
		return dir;
	}

	/**
	 * 创建路径
	 *
	 * @param path 要创建的路径
	 * @return 成功true，失败false
	 */
	private static boolean createDir(String path)
	{
		if (StringTools.isNull(path))
		{
			return false;
		}
		if (new File(path).exists())
		{
			return true;
		}
		if (path.startsWith(Environment.getExternalStorageDirectory().getAbsolutePath()))// 如果是存储卡
		{
			if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED))
			{
				int index = path.indexOf(File.separator, Environment.getExternalStorageDirectory().getAbsolutePath().length() + 1);
				while (index > 0)
				{
					String subPath = path.substring(0, index);
					File dir = new File(subPath);
					if (!dir.exists())
					{
						if (!dir.mkdir())
						{
							return false;
						}
					}
					index = path.indexOf(File.separator, index + 1);
				}
			}
			else
			{
				return false;
			}
		}
		else
		{
			int index = path.indexOf(File.separator, 1);
			while (index > 0)
			{
				String subPath = path.substring(0, index);
				File dir = new File(subPath);
				if (!dir.exists())
				{
					if (!dir.mkdir())
					{
						return false;
					}
				}
				index = path.indexOf(File.separator, index + 1);
			}
		}
		return true;
	}

	/**
	 * 读文件内容
	 *
	 * @param fileName 文件名
	 * @return 文件内容
	 */
	public String readFile(String dir, String fileName)
	{
		if (StringTools.isNull(fileName))
		{
			return null;
		}
		File file = getFile(dir, fileName);
		return readFile(file);
	}

	/**
	 * 读文件内容
	 *
	 * @param file
	 * @return
	 */
	public String readFile(File file)
	{
		if (file != null && file.canRead())
		{
			BufferedReader input = null;
			try
			{
				input = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
				String line = "";
				StringBuffer strBuffer = new StringBuffer("");
				while ((line = input.readLine()) != null)
				{
					strBuffer.append(line + "\n");
				}
				final String content = strBuffer.toString();
				return content;
			}
			catch (IOException e)
			{
			}
			finally
			{
				if (input != null)
				{
					try
					{
						input.close();
					}
					catch (IOException e)
					{
					}
				}
			}
		}
		return null;
	}

	/**
	 * 写文件
	 *
	 * @param fileName 文件名
	 * @param content  文件内容
	 * @return 写入成功true，失败false
	 */
	public boolean writeFile(String dir, String fileName, String content)
	{
		try
		{
			File file = getFile(dir, fileName);
			FileOutputStream fos = new FileOutputStream(file);
			fos.write(content.getBytes());
			fos.close();
			return true;
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return false;
	}
}

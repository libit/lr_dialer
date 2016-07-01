package com.lrcall.db;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;

import com.lrcall.appcall.MyApplication;
import com.lrcall.models.AppInfo;
import com.lrcall.utils.ConstValues;
import com.lrcall.utils.LogcatTools;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by libit on 15/8/31.
 */
public class DbAppFactory
{
	private static final String TAG = "DbAppFactory";
	private static final String TABLE_NAME = DbConstant.TABLE_NAME_APPINFO;
	private static DbAppFactory instance;

	protected DbAppFactory()
	{
	}

	synchronized public static DbAppFactory getInstance()
	{
		if (instance == null)
		{
			instance = new DbAppFactory();
		}
		return instance;
	}

	/**
	 * 增加或更新App列表
	 *
	 * @param appInfoList App列表
	 * @return 增加成功的个数
	 */
	public int addOrUpdateList(List<AppInfo> appInfoList)
	{
		int count = 0;
		if (appInfoList == null || appInfoList.size() < 1)
		{
			return count;
		}
		final ContentResolver contentResolver = MyApplication.getContext().getContentResolver();
		final Uri tableUri = DbConstant.getTableUri(TABLE_NAME);
		final String where = AppInfo.FIELD_PACKAGE_NAME + " = ?";
		for (AppInfo appInfo : appInfoList)
		{
			//            MyConfig.showLog(TAG + " addList", "当前App：" + appInfo.toString());
			ContentValues values = appInfo.getObjectContentValues();
			int rows = contentResolver.update(tableUri, values, where, new String[]{appInfo.getPackageName()});
			if (rows < 1)
			{
				Uri uri = contentResolver.insert(tableUri, values);
				if (uri != null)
					count++;
			}
		}
		return count;
	}

	/**
	 * 取出App列表
	 *
	 * @return List 所有App
	 */
	public ArrayList<AppInfo> getAll()
	{
		ArrayList<AppInfo> appInfoList = new ArrayList<AppInfo>();
		Cursor cursor = MyApplication.getContext().getContentResolver().query(DbConstant.getTableUri(TABLE_NAME), null, null, null, null);
		if (cursor != null)
		{
			if (cursor.moveToFirst())
			{
				do
				{
					AppInfo appInfo = AppInfo.getObjectFromDb(cursor);
					appInfoList.add(appInfo);
				}
				while (cursor.moveToNext());
			}
			cursor.close();
		}
		return appInfoList;
	}

	/**
	 * 取出App列表
	 *
	 * @return List 所有存在的App
	 */
	public ArrayList<AppInfo> getAllExistApps()
	{
		ArrayList<AppInfo> appInfoList = new ArrayList<AppInfo>();
		Cursor cursor = MyApplication.getContext().getContentResolver().query(DbConstant.getTableUri(TABLE_NAME), null, AppInfo.FIELD_EXIST + " = ?", new String[]{ConstValues.STATUS_EXIST + ""}, AppInfo.FIELD_NAME_LABEL);
		if (cursor != null)
		{
			if (cursor.moveToFirst())
			{
				do
				{
					AppInfo appInfo = AppInfo.getObjectFromDb(cursor);
					appInfoList.add(appInfo);
				}
				while (cursor.moveToNext());
			}
			cursor.close();
		}
		return appInfoList;
	}

	/**
	 * 清空App列表
	 */
	public void deleteAll()
	{
		int rows = MyApplication.getContext().getContentResolver().delete(DbConstant.getTableUri(TABLE_NAME), null, null);
		LogcatTools.debug(TAG + " deleteAll", "清空数据库条数：" + rows);
	}

	/**
	 * 添加App
	 *
	 * @param appInfo App对象
	 * @return 成功：true，失败：false
	 */
	public boolean add(AppInfo appInfo)
	{
		Uri uri = MyApplication.getContext().getContentResolver().insert(DbConstant.getTableUri(TABLE_NAME), appInfo.getObjectContentValues());
		return uri != null ? true : false;
	}

	/**
	 * 更新App
	 *
	 * @param appInfo App对象
	 * @return 成功：true，失败：false
	 */
	public boolean update(AppInfo appInfo)
	{
		int rows = MyApplication.getContext().getContentResolver().update(DbConstant.getTableUri(TABLE_NAME), appInfo.getObjectContentValues(), AppInfo.FIELD_PACKAGE_NAME + " = ?", new String[]{appInfo.getPackageName()});
		return rows > 0 ? true : false;
	}

	/**
	 * 添加或更新App
	 *
	 * @param appInfo App对象
	 * @return 成功：true，失败：false
	 */
	public boolean addOrUpdate(AppInfo appInfo)
	{
		ContentValues values = appInfo.getObjectContentValues();
		int rows = MyApplication.getContext().getContentResolver().update(DbConstant.getTableUri(TABLE_NAME), values, AppInfo.FIELD_PACKAGE_NAME + " = ?", new String[]{appInfo.getPackageName()});
		if (rows < 1)
		{
			Uri uri = MyApplication.getContext().getContentResolver().insert(DbConstant.getTableUri(TABLE_NAME), values);
			return uri != null ? true : false;
		}
		return true;
	}

	/**
	 * 删除App
	 *
	 * @param packageName App包名
	 * @return 成功：true，失败：false
	 */
	public boolean delete(String packageName)
	{
		int rows = MyApplication.getContext().getContentResolver().delete(DbConstant.getTableUri(TABLE_NAME), AppInfo.FIELD_PACKAGE_NAME + " = ?", new String[]{packageName});
		return rows > 0 ? true : false;
	}

	/**
	 * 获取指定的App
	 *
	 * @param packageName App包名
	 * @return AppInfo
	 */
	public AppInfo query(String packageName)
	{
		AppInfo appInfo = null;
		Cursor cursor = MyApplication.getContext().getContentResolver().query(DbConstant.getTableUri(TABLE_NAME), null, AppInfo.FIELD_PACKAGE_NAME + " = ?", new String[]{packageName}, null);
		if (cursor != null)
		{
			if (cursor.moveToFirst())
			{
				appInfo = AppInfo.getObjectFromDb(cursor);
				LogcatTools.debug(TAG + " query", "packageName:" + packageName + ",APP信息：" + appInfo.toString());
			}
			cursor.close();
		}
		return appInfo;
	}

	/**
	 * 从数据库中App列表获取指定的App列表
	 *
	 * @param status   应用状态
	 * @param hideType 应用是否隐藏
	 * @return App列表
	 */
	public ArrayList<AppInfo> query(int status, int hideType)
	{
		ArrayList<AppInfo> appInfoList = new ArrayList<>();
		String condition = AppInfo.FIELD_ENABLED + " = ? AND " + AppInfo.FIELD_EXIST + " = " + ConstValues.STATUS_EXIST;
		String[] args = new String[]{status + ""};
		if (hideType == ConstValues.STATUS_HIDE || hideType == ConstValues.STATUS_SHOW)
		{
			condition += " AND " + AppInfo.FIELD_HIDE + " = ?";
			args = new String[]{status + "", hideType + ""};
		}
		Cursor cursor = MyApplication.getContext().getContentResolver().query(DbConstant.getTableUri(TABLE_NAME), null, condition, args, AppInfo.FIELD_NAME_LABEL + " ASC");
		if (cursor != null)
		{
			if (cursor.moveToFirst())
			{
				do
				{
					AppInfo appInfo = AppInfo.getObjectFromDb(cursor);
					LogcatTools.debug(TAG + " query", "status:" + status + ",APP信息：" + appInfo.toString());
				}
				while (cursor.moveToNext());
			}
			cursor.close();
		}
		return appInfoList;
	}

	/**
	 * 从数据库中App列表获取指定的App列表
	 *
	 * @param type 应用类型
	 * @param hide 应用是否隐藏
	 * @return App列表
	 */
	public ArrayList<AppInfo> queryByType(int type, int hide)
	{
		ArrayList<AppInfo> appInfoList = new ArrayList<AppInfo>();
		String condition = AppInfo.FIELD_TYPE + " = ? AND " + AppInfo.FIELD_EXIST + " = " + ConstValues.STATUS_EXIST;
		String[] args = new String[]{type + ""};
		if (hide == ConstValues.STATUS_HIDE || hide == ConstValues.STATUS_SHOW)
		{
			condition += " AND " + AppInfo.FIELD_HIDE + " = ?";
			args = new String[]{type + "", hide + ""};
		}
		Cursor cursor = MyApplication.getContext().getContentResolver().query(DbConstant.getTableUri(TABLE_NAME), null, condition, args, AppInfo.FIELD_NAME_LABEL + " ASC");
		if (cursor != null)
		{
			if (cursor.moveToFirst())
			{
				do
				{
					AppInfo appInfo = AppInfo.getObjectFromDb(cursor);
					LogcatTools.debug(TAG + " query", "type:" + type + ",APP信息：" + appInfo.toString());
				}
				while (cursor.moveToNext());
			}
			cursor.close();
		}
		return appInfoList;
	}

	/**
	 * 重置所有的App状态为不存在
	 *
	 * @return 成功：true，失败：false
	 */
	public boolean resetAppExistStatus(boolean isExist)
	{
		ContentValues contentValues = new ContentValues();
		contentValues.put(AppInfo.FIELD_EXIST, isExist ? ConstValues.STATUS_EXIST : ConstValues.STATUS_NOT_EXIST);
		int rows = MyApplication.getContext().getContentResolver().update(DbConstant.getTableUri(TABLE_NAME), contentValues, null, null);
		return rows > 0 ? true : false;
	}

	/**
	 * 重置所有的App状态为不隐藏
	 *
	 * @return 成功：true，失败：false
	 */
	public boolean resetAppHideStatus(boolean isHide)
	{
		ContentValues contentValues = new ContentValues();
		contentValues.put(AppInfo.FIELD_HIDE, isHide ? ConstValues.STATUS_HIDE : ConstValues.STATUS_SHOW);
		int rows = MyApplication.getContext().getContentResolver().update(DbConstant.getTableUri(TABLE_NAME), contentValues, null, null);
		return rows > 0 ? true : false;
	}

	/**
	 * 重置所有的App状态为启用
	 *
	 * @return 成功：true，失败：false
	 */
	public boolean resetAppEnableStatus(boolean isEnabled)
	{
		ContentValues contentValues = new ContentValues();
		contentValues.put(AppInfo.FIELD_ENABLED, isEnabled ? ConstValues.STATUS_ENABLED : ConstValues.STATUS_DISABLED);
		int rows = MyApplication.getContext().getContentResolver().update(DbConstant.getTableUri(TABLE_NAME), contentValues, null, null);
		return rows > 0 ? true : false;
	}

	/**
	 * 删除
	 *
	 * @return 成功：true，失败：false
	 */
	public boolean deleteNotExist()
	{
		int rows = MyApplication.getContext().getContentResolver().delete(DbConstant.getTableUri(TABLE_NAME), AppInfo.FIELD_EXIST + " = ?", new String[]{ConstValues.STATUS_NOT_EXIST + ""});
		return rows > 0 ? true : false;
	}

	/**
	 * 查询包名是否存在
	 *
	 * @param packageName 包名
	 * @return
	 */
	public boolean isExist(String packageName)
	{
		boolean isExist = false;
		Cursor cursor = MyApplication.getContext().getContentResolver().query(DbConstant.getTableUri(TABLE_NAME), null, AppInfo.FIELD_PACKAGE_NAME + " = ?", new String[]{packageName}, null);
		if (cursor != null)
		{
			if (cursor.moveToFirst())
			{
				AppInfo appInfo = AppInfo.getObjectFromDb(cursor);
				if (appInfo != null && appInfo.isExist())
				{
					isExist = true;
				}
			}
			cursor.close();
		}
		return isExist;
	}
}

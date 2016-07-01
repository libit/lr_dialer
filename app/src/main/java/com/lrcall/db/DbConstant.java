package com.lrcall.db;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.net.Uri;

import com.lrcall.utils.MyConfig;

/**
 * Created by libit on 15/8/26.
 */
public class DbConstant
{
	/**
	 * AppInfo表
	 */
	public static final String TABLE_NAME_APPINFO = "app_info";
	/**
	 * AppInfo隐藏表
	 */
	public static final String TABLE_NAME_HIDE_APPINFO = "hide_app";
	/**
	 * AppInfo黑名单表
	 */
	public static final String TABLE_NAME_BLACK_APPINFO = "black_app";
	/**
	 * 备份信息表
	 */
	public static final String TABLE_NAME_BACKUP_INFO = "backup_info";
	/**
	 * Authority for regular database of the application.
	 * 值与com.lrcall.db.DBProvider的authorities值相同
	 */
	public static final String AUTHORITY = MyConfig.AUTHORITY_NAME + ".db";
	/**
	 * Base content type for appinfo objects.
	 */
	public static final String BASE_DIR_TYPE = "vnd.android.cursor.dir/vnd.starter";
	/**
	 * Base item content type for appinfo objects.
	 */
	public static final String BASE_ITEM_TYPE = "vnd.android.cursor.item/vnd.starter";

	/**
	 * Base uri for the table. <br/>
	 * To append with FIELD_ID
	 *
	 * @param tableName 表名
	 * @return 表的Uri
	 * @see ContentUris#appendId(android.net.Uri.Builder, long)
	 */
	public static Uri getTableUriBase(String tableName)
	{
		return Uri.parse(ContentResolver.SCHEME_CONTENT + "://" + AUTHORITY + "/" + tableName + "/");
	}

	/**
	 * Uri of table.
	 *
	 * @param tableName 表名
	 * @return 表的Uri
	 */
	public static Uri getTableUri(String tableName)
	{
		return Uri.parse(ContentResolver.SCHEME_CONTENT + "://" + AUTHORITY + "/" + tableName);
	}

	/**
	 * Content type for table
	 *
	 * @param tableName 表名
	 * @return
	 */
	public static String getTableContentType(String tableName)
	{
		return BASE_DIR_TYPE + "." + tableName;
	}

	/**
	 * Item type for table
	 *
	 * @param tableName 表名
	 * @return
	 */
	public static String getTableContentItemType(String tableName)
	{
		return BASE_ITEM_TYPE + "." + tableName;
	}
}

package com.lrcall.models;

import android.content.ContentValues;
import android.database.Cursor;

/**
 * Created by libit on 15/8/31.
 */
public abstract class DbObject
{
	/**
	 * Primary key id.
	 *
	 * @see Long
	 */
	public static final String FIELD_ID = "id";

	/**
	 * 获取创建表的SQL语句
	 *
	 * @return SQL语句
	 */
	public static String getCreateTableSQL()
	{
		return null;
	}

	/**
	 * 从数据库中取出对象
	 *
	 * @param cursor
	 * @return
	 */
	public static Object getObjectFromDb(Cursor cursor)
	{
		return null;
	}

	/**
	 * 转换成数据库存储的数据
	 *
	 * @return ContentValues
	 */
	public abstract ContentValues getObjectContentValues();
}

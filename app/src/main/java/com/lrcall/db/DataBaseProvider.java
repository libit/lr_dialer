package com.lrcall.db;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.support.v4.database.DatabaseUtilsCompat;

import com.lrcall.models.AppInfo;
import com.lrcall.utils.LogcatTools;

public class DataBaseProvider extends ContentProvider
{
	private static final String UNKNOWN_URI_LOG = "Unknown URI ";
	private static final TableInfo[] tableInfos;
	/**
	 * A UriMatcher instance
	 */
	private static final UriMatcher URI_MATCHER;

	static
	{
		tableInfos = new TableInfo[]{new TableInfo(DbConstant.TABLE_NAME_APPINFO, 1, 2, AppInfo.FIELD_ID)};
		// Create and initialize URI matcher.
		URI_MATCHER = new UriMatcher(UriMatcher.NO_MATCH);
		int count = tableInfos.length;
		for (int i = 0; i < count; i++)
		{
			TableInfo tableInfo = tableInfos[i];
			URI_MATCHER.addURI(tableInfo.AUTHORITY, tableInfo.tableName, tableInfo.table);
			URI_MATCHER.addURI(tableInfo.AUTHORITY, tableInfo.tableName + "/#", tableInfo.table_id);
		}
	}

	private DataBaseFactory.DBHelper mOpenHelper;

	public DataBaseProvider()
	{
	}

	@Override
	public String getType(Uri uri)
	{
		int matched = URI_MATCHER.match(uri);
		int count = tableInfos.length;
		for (int i = 0; i < count; i++)
		{
			TableInfo tableInfo = tableInfos[i];
			if (matched == tableInfo.table)
			{
				return DbConstant.getTableContentType(tableInfo.tableName);
			}
			else if (matched == tableInfo.table_id)
			{
				return DbConstant.getTableContentItemType(tableInfo.tableName);
			}
		}
		throw new IllegalArgumentException(UNKNOWN_URI_LOG + uri);
	}

	@Override
	public boolean onCreate()
	{
		mOpenHelper = new DataBaseFactory.DBHelper(getContext());
		return true;
	}

	@Override
	public Uri insert(Uri uri, ContentValues values)
	{
		int matched = URI_MATCHER.match(uri);
		String matchedTable = null;
		Uri baseInsertedUri = null;
		int count = tableInfos.length;
		for (int i = 0; i < count; i++)
		{
			TableInfo tableInfo = tableInfos[i];
			if (matched == tableInfo.table || matched == tableInfo.table_id)
			{
				matchedTable = tableInfo.tableName;
				baseInsertedUri = DbConstant.getTableUriBase(tableInfo.tableName);
				break;
			}
		}
		if (matchedTable == null)
		{
			throw new IllegalArgumentException(UNKNOWN_URI_LOG + uri);
		}
		if (values == null)
		{
			values = new ContentValues();
		}
		SQLiteDatabase db = mOpenHelper.getWritableDatabase();
		long rowId = db.insert(matchedTable, null, values);
		// If the insert succeeded, the row ID exists.
		if (rowId >= 0)
		{
			return ContentUris.withAppendedId(baseInsertedUri, rowId);
		}
		LogcatTools.debug("插入数据库操作", "插入数据到" + uri + "失败！");
		throw new SQLException("Failed to insert row into " + uri);
	}

	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs)
	{
		SQLiteDatabase db = mOpenHelper.getWritableDatabase();
		int count = 0;
		int matched = URI_MATCHER.match(uri);
		boolean isMatch = false;
		int length = tableInfos.length;
		for (int i = 0; i < length; i++)
		{
			TableInfo tableInfo = tableInfos[i];
			if (matched == tableInfo.table)
			{
				count = db.delete(tableInfo.tableName, selection, selectionArgs);
				isMatch = true;
				break;
			}
			else if (matched == tableInfo.table_id)
			{
				String finalWhere = DatabaseUtilsCompat.concatenateWhere(tableInfo.field_id + " = " + ContentUris.parseId(uri), selection);
				count = db.delete(tableInfo.tableName, finalWhere, selectionArgs);
				isMatch = true;
				break;
			}
		}
		if (!isMatch)
		{
			throw new IllegalArgumentException(UNKNOWN_URI_LOG + uri);
		}
		return count;
	}

	@Override
	public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder)
	{
		// Constructs a new query builder and sets its table name
		SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
		String finalSortOrder = sortOrder;
		String[] finalSelectionArgs = selectionArgs;
		String finalGrouping = null;
		String finalHaving = null;
		int matched = URI_MATCHER.match(uri);
		boolean isMatch = false;
		int length = tableInfos.length;
		for (int i = 0; i < length; i++)
		{
			TableInfo tableInfo = tableInfos[i];
			if (matched == tableInfo.table)
			{
				qb.setTables(tableInfo.tableName);
				if (sortOrder == null)
				{
					finalSortOrder = tableInfo.field_id + " ASC";
				}
				isMatch = true;
				break;
			}
			else if (matched == tableInfo.table_id)
			{
				qb.setTables(tableInfo.tableName);
				qb.appendWhere(tableInfo.field_id + " = ? ");
				finalSelectionArgs = DatabaseUtilsCompat.appendSelectionArgs(selectionArgs, new String[]{uri.getLastPathSegment()});
				isMatch = true;
				break;
			}
		}
		if (!isMatch)
		{
			throw new IllegalArgumentException(UNKNOWN_URI_LOG + uri);
		}
		SQLiteDatabase db = mOpenHelper.getReadableDatabase();
		Cursor c = qb.query(db, projection, selection, finalSelectionArgs, finalGrouping, finalHaving, finalSortOrder);
		return c;
	}

	@Override
	public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs)
	{
		SQLiteDatabase db = mOpenHelper.getWritableDatabase();
		int count = 0;
		int matched = URI_MATCHER.match(uri);
		boolean isMatch = false;
		int length = tableInfos.length;
		for (int i = 0; i < length; i++)
		{
			TableInfo tableInfo = tableInfos[i];
			if (matched == tableInfo.table)
			{
				count = db.update(tableInfo.tableName, values, selection, selectionArgs);
				isMatch = true;
				break;
			}
			else if (matched == tableInfo.table_id)
			{
				String finalWhere = DatabaseUtilsCompat.concatenateWhere(tableInfo.field_id + " = " + ContentUris.parseId(uri), selection);
				count = db.update(tableInfo.tableName, values, finalWhere, selectionArgs);
				isMatch = true;
				break;
			}
		}
		if (!isMatch)
		{
			throw new IllegalArgumentException(UNKNOWN_URI_LOG + uri);
		}
		return count;
	}

	public static class TableInfo
	{
		//
		public static final String AUTHORITY = DbConstant.AUTHORITY;
		// 表名
		public String tableName;
		// Ids for matcher
		public int table;
		public int table_id;
		// 主键名
		public String field_id;

		public TableInfo(String tableName, int table, int table_id, String field_id)
		{
			this.tableName = tableName;
			this.table = table;
			this.table_id = table_id;
			this.field_id = field_id;
		}
	}
}

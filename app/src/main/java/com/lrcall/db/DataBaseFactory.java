package com.lrcall.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.lrcall.models.AppInfo;

/**
 * Created by libit on 15/8/26.
 */
public class DataBaseFactory
{
	private static DataBaseFactory instance = null;

	private DataBaseFactory()
	{
	}

	public static DataBaseFactory getInstance()
	{
		if (instance == null)
		{
			instance = new DataBaseFactory();
		}
		return instance;
	}

	public static class DBHelper extends SQLiteOpenHelper
	{
		private static final int DATABASE_VERSION = 12;
		private static final String[] TABLES = new String[]{AppInfo.getCreateTableSQL()// App信息表
		};

		DBHelper(Context context)
		{
			super(context, DbConstant.AUTHORITY, null, DATABASE_VERSION);
		}

		@Override
		public void onCreate(SQLiteDatabase db)
		{
			int count = TABLES.length;
			for (int i = 0; i < count; i++)
			{
				db.execSQL(TABLES[i]);
			}
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
		{
			if (oldVersion < 1)
			{
				db.execSQL("DROP TABLE IF EXISTS " + DbConstant.TABLE_NAME_APPINFO);
			}
			onCreate(db);
		}
	}
}

package com.lrcall.models;

import android.content.ContentValues;
import android.database.Cursor;
import android.graphics.Bitmap;

import com.lrcall.db.DbConstant;
import com.lrcall.utils.ConstValues;
import com.lrcall.utils.PinyinTools;
import com.lrcall.utils.StringTools;

import java.util.Comparator;

/**
 * Created by libit on 15/8/19.
 */
public class AppInfo extends DbObject implements Comparator<AppInfo>
{
	public static final String FIELD_UID = "uid";
	public static final String FIELD_NAME = "name";
	public static final String FIELD_NAME_LABEL = "name_label";
	public static final String FIELD_PACKAGE_NAME = "package_name";
	public static final String FIELD_LAUNCH_CLASS = "launch_class";
	//    public static final String FIELD_PHOTO = "photo";
	public static final String FIELD_TYPE = "type";
	public static final String FIELD_ENABLED = "is_enabled";
	public static final String FIELD_HIDE = "is_hide";
	public static final String FIELD_EXIST = "is_exist";
	private String id;// 主键
	private String uid;// 程序的用户ID，不同的程序ID可能一样
	private String name;//程序名称
	private String nameLabel;//程序名称的拼音，用户检索
	private String packageName;// 包名，唯一标识
	private String launchClassName;// 启动的class
	private Bitmap photo;// 程序图片
	private int type;// 程序类型（系统程序或用户程序）
	private boolean isEnabled;// 程序状态（启用或禁用）
	private boolean isHide;// 程序是否是隐藏程序
	private boolean isExist;// 程序是否还存在（当用户卸载后）

	public AppInfo()
	{
		//        type = ConstValues.TYPE_USER;
		//        isEnabled = true;
		//        isHide = false;
	}

	/**
	 * 获取创建表的SQL语句
	 *
	 * @return SQL语句
	 */
	public static final String getCreateTableSQL()
	{
		// App黑名单信息表
		String sql = "CREATE TABLE IF NOT EXISTS " + DbConstant.TABLE_NAME_APPINFO + " (" + AppInfo.FIELD_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + AppInfo.FIELD_UID + " TEXT NOT NULL," + AppInfo.FIELD_NAME + " TEXT NOT NULL," + AppInfo.FIELD_NAME_LABEL + " TEXT NOT NULL," + AppInfo.FIELD_PACKAGE_NAME + " TEXT NOT NULL UNIQUE," + AppInfo.FIELD_LAUNCH_CLASS + " TEXT,"
				//                + AppInfo.FIELD_PHOTO + " BLOB,"
				+ AppInfo.FIELD_TYPE + " INTEGER NOT NULL," + AppInfo.FIELD_ENABLED + " INTEGER NOT NULL," + AppInfo.FIELD_HIDE + " INTEGER NOT NULL," + AppInfo.FIELD_EXIST + " INTEGER NOT NULL" + ");";
		return sql;
	}

	/**
	 * 从数据库中取出对象
	 *
	 * @param cursor
	 * @return
	 */
	public static AppInfo getObjectFromDb(Cursor cursor)
	{
		AppInfo appInfo = new AppInfo();
		appInfo.setId(cursor.getString(cursor.getColumnIndex(FIELD_ID)));
		appInfo.setUid(cursor.getString(cursor.getColumnIndex(FIELD_UID)));
		appInfo.setName(cursor.getString(cursor.getColumnIndex(FIELD_NAME)));
		appInfo.setNameLabel(cursor.getString(cursor.getColumnIndex(FIELD_NAME_LABEL)));
		appInfo.setPackageName(cursor.getString(cursor.getColumnIndex(FIELD_PACKAGE_NAME)));
		appInfo.setLaunchClassName(cursor.getString(cursor.getColumnIndex(FIELD_LAUNCH_CLASS)));
		//        byte[] bs = cursor.getBlob(cursor.getColumnIndex(FIELD_PHOTO));
		//        if (bs != null && bs.length > 0)
		//        {
		//            Bitmap bmp = BitmapFactory.decodeByteArray(bs, 0, bs.length);
		//            appInfo.setPhoto(bmp);
		//        }
		appInfo.setType(cursor.getInt(cursor.getColumnIndex(FIELD_TYPE)));
		appInfo.setIsEnabled(cursor.getInt(cursor.getColumnIndex(FIELD_ENABLED)) == ConstValues.STATUS_ENABLED ? true : false);
		appInfo.setIsHide(cursor.getInt(cursor.getColumnIndex(FIELD_HIDE)) == ConstValues.STATUS_HIDE ? true : false);
		appInfo.setIsExist(cursor.getInt(cursor.getColumnIndex(FIELD_EXIST)) == ConstValues.STATUS_EXIST ? true : false);
		return appInfo;
	}

	public String getId()
	{
		return id;
	}

	public void setId(String id)
	{
		this.id = id;
	}

	public String getUid()
	{
		return uid;
	}

	public void setUid(String uid)
	{
		this.uid = uid;
	}

	public String getPackageName()
	{
		return packageName;
	}

	public void setPackageName(String packageName)
	{
		this.packageName = packageName;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public String getLaunchClassName()
	{
		return launchClassName;
	}

	public void setLaunchClassName(String launchClassName)
	{
		this.launchClassName = launchClassName;
	}

	public Bitmap getPhoto()
	{
		return photo;
	}

	public void setPhoto(Bitmap photo)
	{
		this.photo = photo;
	}

	public int getType()
	{
		return type;
	}

	public void setType(int type)
	{
		this.type = type;
	}

	public boolean isEnabled()
	{
		return isEnabled;
	}

	public void setIsEnabled(boolean isEnabled)
	{
		this.isEnabled = isEnabled;
	}

	public boolean isHide()
	{
		return isHide;
	}

	public void setIsHide(boolean isHide)
	{
		this.isHide = isHide;
	}

	public boolean isExist()
	{
		return isExist;
	}

	public void setIsExist(boolean isExist)
	{
		this.isExist = isExist;
	}

	public String getNameLabel()
	{
		return nameLabel;
	}

	public void setNameLabel(String nameLabel)
	{
		this.nameLabel = nameLabel;
	}

	/**
	 * 转换成数据库存储的数据
	 *
	 * @return ContentValues
	 */
	public ContentValues getObjectContentValues()
	{
		ContentValues contentValues = new ContentValues();
		if (id != null)
		{
			contentValues.put(AppInfo.FIELD_ID, id);
		}
		contentValues.put(AppInfo.FIELD_UID, uid);
		contentValues.put(AppInfo.FIELD_NAME, name);
		if (StringTools.isNull(nameLabel))
		{
			nameLabel = PinyinTools.Chinese2Pinyin(name);
		}
		contentValues.put(AppInfo.FIELD_NAME_LABEL, nameLabel);
		contentValues.put(AppInfo.FIELD_PACKAGE_NAME, packageName);
		if (!StringTools.isNull(getLaunchClassName()))
		{
			contentValues.put(AppInfo.FIELD_LAUNCH_CLASS, launchClassName);
		}
		//        if (photo != null)
		//        {
		//            ByteArrayOutputStream os = new ByteArrayOutputStream();
		//            photo.compress(Bitmap.CompressFormat.PNG, 100, os);
		//            contentValues.put(AppInfo.FIELD_PHOTO, os.toByteArray());
		//        }
		contentValues.put(AppInfo.FIELD_TYPE, type);
		contentValues.put(AppInfo.FIELD_ENABLED, isEnabled ? ConstValues.STATUS_ENABLED : ConstValues.STATUS_DISABLED);
		contentValues.put(AppInfo.FIELD_HIDE, isHide ? ConstValues.STATUS_HIDE : ConstValues.STATUS_SHOW);
		contentValues.put(AppInfo.FIELD_EXIST, isExist ? ConstValues.STATUS_HIDE : ConstValues.STATUS_SHOW);
		return contentValues;
	}

	@Override
	public String toString()
	{
		return String.format("uid:%s,name:%s,nameLabel:%s,packageName:%s,type:%d,enabled:%d,hide:%d,exist:%d.", uid, name, nameLabel, packageName, type, isEnabled ? ConstValues.STATUS_ENABLED : ConstValues.STATUS_DISABLED, isHide() ? ConstValues.STATUS_HIDE : ConstValues.STATUS_SHOW, isExist ? ConstValues.STATUS_EXIST : ConstValues.STATUS_NOT_EXIST);
	}

	@Override
	public int compare(AppInfo lhs, AppInfo rhs)
	{
		if (lhs == null && rhs == null)
		{
			return 0;
		}
		if (lhs == null && rhs != null)
		{
			return -1;
		}
		if (rhs == null)
		{
			return -1;
		}
		if (lhs == rhs)
		{
			return 0;
		}
		String lName = PinyinTools.Chinese2Pinyin(lhs.getName());
		String rName = PinyinTools.Chinese2Pinyin(rhs.getName());
		return lName.compareTo(rName);
	}
}

package com.lrcall.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.util.Log;

import com.lrcall.appcall.MyApplication;

import java.util.HashMap;
import java.util.Map;

public class PreferenceUtils
{
	public static final int GENERIC_TYPE_AUTO = 0;
	public static final int GENERIC_TYPE_FORCE = 1;
	public static final int GENERIC_TYPE_PREVENT = 2;
	// 日志记录级别，级别越高记录的日志越多
	public static final int LEVEL_0 = 0;
	public static final int LEVEL_1 = 1;
	public static final int LEVEL_2 = 2;
	public static final int LEVEL_3 = 3;
	public static final int LEVEL_4 = 4;
	public static final int LEVEL_5 = 5;
	public static final String PREF_USERNAME_KEY = "username";
	public static final String PREF_SESSION_ID_KEY = "session_id";
	public static final String PREF_CRASH_FILE = "crash_file_name";
	public static final String LOGCAT_LEVEL = "logcat_level";
	public static final String LOGCAT_AUTO_UPDATE = "logcat_auto_update";
	public static final String IS_ROOT = "is_root";
	public static final String IS_FIRST_RUN = "is_first_run";
	public static final String IS_SHOW_NOTIFICATION = "is_show_notification";
	public static final String IS_ACCEPT_ROLE = "is_accepted_role";
	public static final String APP_WIDGET_INFOS = "app_widget_infos";
	public static final String DIAL_PRESS_TONE_MODE = "dial_press_tone_mode";
	public static final String DIAL_PRESS_VIBRATE_MODE = "dial_press_vibrate_mode";
	private static final String TAG = PreferenceUtils.class.getName();
	private final static HashMap<String, String> STRING_PREFS = new HashMap<String, String>()
	{
		private static final long serialVersionUID = 1L;

		{
			put(PREF_USERNAME_KEY, "");
			put(PREF_SESSION_ID_KEY, "");
			put(PREF_CRASH_FILE, "");
			put(LOGCAT_LEVEL, LEVEL_3 + "");
			put(APP_WIDGET_INFOS, "");
			put(DIAL_PRESS_TONE_MODE, GENERIC_TYPE_PREVENT + "");
			put(DIAL_PRESS_VIBRATE_MODE, GENERIC_TYPE_PREVENT + "");
		}
	};
	private final static HashMap<String, Boolean> BOOLEAN_PREFS = new HashMap<String, Boolean>()
	{
		private static final long serialVersionUID = 1L;

		{
			put(LOGCAT_AUTO_UPDATE, true);
			put(IS_ACCEPT_ROLE, false);
			put(IS_ROOT, false);
			put(IS_FIRST_RUN, true);
			put(IS_SHOW_NOTIFICATION, true);
		}
	};
	protected static PreferenceUtils instance = null;
	private SharedPreferences prefs;
	private Context context;

	protected PreferenceUtils()
	{
		context = MyApplication.getContext();
		prefs = PreferenceManager.getDefaultSharedPreferences(context);
	}

	synchronized public static PreferenceUtils getInstance()
	{
		if (instance == null)
		{
			instance = new PreferenceUtils();
		}
		return instance;
	}

	private static String gPrefStringValue(SharedPreferences aPrefs, String key)
	{
		if (STRING_PREFS.containsKey(key))
		{
			return aPrefs.getString(key, STRING_PREFS.get(key));
		}
		// return null;
		return "";
	}

	private static Boolean gPrefBooleanValue(SharedPreferences aPrefs, String key)
	{
		if (BOOLEAN_PREFS.containsKey(key))
		{
			return aPrefs.getBoolean(key, BOOLEAN_PREFS.get(key));
		}
		return false;
	}

	public String getUsername()
	{
		return getStringValue(PREF_USERNAME_KEY);
	}

	public boolean setUsername(String value)
	{
		return setStringValue(PREF_USERNAME_KEY, value);
	}

	public String getSessionId()
	{
		return getStringValue(PREF_SESSION_ID_KEY);
	}

	public boolean setSessionId(String value)
	{
		return setStringValue(PREF_SESSION_ID_KEY, value);
	}

	public boolean setStringValue(String key, String value)
	{
		Editor editor = prefs.edit();
		editor.putString(key, value);
		return editor.commit();
	}

	public String getStringValue(String key)
	{
		return gPrefStringValue(prefs, key);
	}

	public void setBooleanValue(String key, Boolean value)
	{
		Editor editor = prefs.edit();
		editor.putBoolean(key, value);
		editor.commit();
	}

	public Boolean getBooleanValue(String key)
	{
		return gPrefBooleanValue(prefs, key);
	}

	public int getIntegerValue(String key)
	{
		try
		{
			return Integer.parseInt(getStringValue(key));
		}
		catch (NumberFormatException e)
		{
			Log.e(TAG, "Invalid " + key + " format : expect a int");
		}
		return Integer.parseInt(STRING_PREFS.get(key));
	}

	/**
	 * Set all values to default
	 */
	public void resetAllDefaultValues()
	{
		for (String key : STRING_PREFS.keySet())
		{
			setStringValue(key, STRING_PREFS.get(key));
		}
		for (String key : BOOLEAN_PREFS.keySet())
		{
			setBooleanValue(key, BOOLEAN_PREFS.get(key));
		}
	}

	/**
	 * 备份数据
	 *
	 * @return
	 */
	public String backup()
	{
		Map<String, String> map = new HashMap<>();
		for (String key : STRING_PREFS.keySet())
		{
			map.put(key, getStringValue(key));
		}
		for (String key : BOOLEAN_PREFS.keySet())
		{
			setBooleanValue(key, BOOLEAN_PREFS.get(key));
			map.put(key, getBooleanValue(key).toString());
		}
		return GsonTools.toJson(map);
	}

	// ----
	// UI related
	// ----
	public boolean dialPressTone()
	{
		int mode = getIntegerValue(DIAL_PRESS_TONE_MODE);
		switch (mode)
		{
			case GENERIC_TYPE_AUTO:
				return Settings.System.getInt(context.getContentResolver(), Settings.System.DTMF_TONE_WHEN_DIALING, 1) == 1;
			case GENERIC_TYPE_FORCE:
				return true;
			case GENERIC_TYPE_PREVENT:
				return false;
			default:
				break;
		}
		return false;
	}

	public boolean dialPressVibrate()
	{
		int mode = getIntegerValue(DIAL_PRESS_VIBRATE_MODE);
		switch (mode)
		{
			case GENERIC_TYPE_AUTO:
				return Settings.System.getInt(context.getContentResolver(), Settings.System.HAPTIC_FEEDBACK_ENABLED, 1) == 1;
			case GENERIC_TYPE_FORCE:
				return true;
			case GENERIC_TYPE_PREVENT:
				return false;
			default:
				break;
		}
		return false;
	}
}

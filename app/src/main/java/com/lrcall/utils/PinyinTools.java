/*
 * Copyright (c) 2015. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */
package com.lrcall.utils;

import android.graphics.Color;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.HanyuPinyinVCharType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;

/**
 * Created by libit on 15/8/11.
 */
public class PinyinTools
{
	private static final String TAG = PinyinTools.class.getName();
	public static boolean isHanzi = false;
	public static String chach_name_first_py = "";
	public static int[] chach_name_single_length;

	/**
	 * 汉字转拼音
	 *
	 * @param cnStr 待转换的汉字
	 * @return 拼音的字符串（全部大写）
	 */
	public static String Chinese2Pinyin(String cnStr)
	{
		HanyuPinyinOutputFormat PINYIN_FORMAT = new HanyuPinyinOutputFormat();
		PINYIN_FORMAT.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
		PINYIN_FORMAT.setVCharType(HanyuPinyinVCharType.WITH_V);
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < cnStr.length(); i++)
		{
			char c = cnStr.charAt(i);
			if (c <= 255)
			{
				sb.append(c);
			}
			else
			{
				String pinyin = null;
				try
				{
					String[] pinyinArray = PinyinHelper.toHanyuPinyinStringArray(c, PINYIN_FORMAT);
					pinyin = pinyinArray[0];
				}
				catch (BadHanyuPinyinOutputFormatCombination e)
				{
					LogcatTools.error(TAG + " Chinese2Pinyin", "BadHanyuPinyinOutputFormatCombination:" + e.getMessage());
				}
				catch (NullPointerException e)
				{
				}
				if (pinyin != null)
				{
					sb.append(pinyin);
				}
			}
		}
		//        Log.i("汉字转拼音", cnStr + "->" + sb.toString());
		return sb.toString();
	}

	public static String getCharacterPinYin(char c)
	{
		HanyuPinyinOutputFormat format = new HanyuPinyinOutputFormat();
		format.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
		String[] pinyin = null;
		try
		{
			pinyin = PinyinHelper.toHanyuPinyinStringArray(c, format);
		}
		catch (BadHanyuPinyinOutputFormatCombination e)
		{
			e.printStackTrace();
		}
		if (pinyin == null)
		{
			return null;
		}
		return pinyin[0];
	}

	public static void checkHanzi(String str)
	{
		if (getCharacterPinYin(str.charAt(0)) == null)
		{
			isHanzi = false;
		}
		else
		{
			isHanzi = true;
		}
	}

	public static void resetCache()
	{
		isHanzi = false;
		chach_name_first_py = "";
		chach_name_single_length = null;
	}

	public static SpannableStringBuilder getSpanNameString(String name, String condition)
	{
		name = name.trim();
		SpannableStringBuilder style = new SpannableStringBuilder(name);
		style.setSpan(new ForegroundColorSpan(Color.rgb(0xff, 0x77, 0x00)), 0, 0, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		if (StringTools.isNull(condition))
		{
			return style;
		}
		checkHanzi(condition);
		if (isHanzi)
		{
			int start = name.indexOf(condition);
			if (start > -1)
			{
				int end = start + condition.length();
				style.setSpan(new ForegroundColorSpan(Color.rgb(0xff, 0x77, 0x00)), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
			}
		}
		else
		{
			String name_first_py = "";
			int[] name_single_length = new int[name.length()];
			int index = 0;
			String str1;
			if (StringTools.isNull(chach_name_first_py) && (chach_name_single_length == null))
			{
				StringBuilder sb = new StringBuilder();
				for (int i = 0; i < name.length(); i++)
				{
					str1 = getCharacterPinYin(name.charAt(i));
					if (str1 == null)
					{
						sb.append(name.substring(i, i + 1));
						name_single_length[index++] = 1;
					}
					else
					{
						sb.append(str1.substring(0, 1));
						name_single_length[index++] = str1.length();
					}
				}
				name_first_py = sb.toString().trim();
			}
			else
			{
				name_first_py = chach_name_first_py;
				name_single_length = chach_name_single_length;
			}
			int start = name_first_py.indexOf(condition);
			if (start > -1)
			{
				if (start > -1)
				{
					int end = start + condition.length();
					style.setSpan(new ForegroundColorSpan(Color.rgb(0xff, 0x77, 0x00)), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
				}
			}
			else
			{
				if (!name_first_py.contains(condition.substring(0, 1)))
				{
					return style;
				}
				index = 0;
				int count = 0;
				while (index > -1 && count < name_first_py.length())
				{
					index = name_first_py.indexOf(condition.substring(0, 1), index);
					str1 = getCharacterPinYin(name.charAt(index));
					if (!StringTools.isNull(str1) && (condition.contains(str1) || str1.contains(condition)))
					{
						break;
					}
					count++;
				}
				start = index;
				int end = start;
				int condition_length = condition.length();
				for (int i = index; i < name_single_length.length; i++)
				{
					end++;
					condition_length = condition_length - name_single_length[i];
					if (condition_length < 1)
					{
						break;
					}
				}
				style.setSpan(new ForegroundColorSpan(Color.rgb(0xff, 0x77, 0x00)), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
			}
		}
		return style;
	}

	public static SpannableStringBuilder getSpanNumberString(String number, String condition)
	{
		SpannableStringBuilder style = new SpannableStringBuilder(number);
		if (!StringTools.isNull(condition))
		{
			int start = number.indexOf(condition);
			if (start > -1)
			{
				int end = start + condition.length();
				style.setSpan(new ForegroundColorSpan(Color.rgb(0xff, 0x77, 0x00)), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
			}
			else
			{
				style.setSpan(new ForegroundColorSpan(Color.rgb(0xff, 0x77, 0x00)), 0, 0, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
			}
		}
		return style;
	}

	public static SpannableStringBuilder getSpanNameString5(String name, String condition)
	{
		name = name.trim();
		SpannableStringBuilder style = new SpannableStringBuilder(name);
		// style.setSpan(new ForegroundColorSpan(Color.rgb(0xff, 0x77, 0x00)), 0, 0, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		if (StringTools.isNull(condition))
		{
			return style;
		}
		checkHanzi(condition);
		if (isHanzi)
		{
			int start = name.indexOf(condition);
			if (start > -1)
			{
				int end = start + condition.length();
				style.setSpan(new ForegroundColorSpan(Color.rgb(0xff, 0x77, 0x00)), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
			}
		}
		else
		{
			String name_first_py = "";
			int[] name_single_length = new int[name.length()];
			int index = 0;
			String str1;
			if (StringTools.isNull(chach_name_first_py) && (chach_name_single_length == null))
			{
				StringBuilder sb = new StringBuilder();
				for (int i = 0; i < name.length(); i++)
				{
					str1 = getCharacterPinYin(name.charAt(i));
					if (str1 == null)
					{
						sb.append(name.substring(i, i + 1));
						name_single_length[index++] = 1;
					}
					else
					{
						sb.append(str1.substring(0, 1));
						name_single_length[index++] = str1.length();
					}
				}
				name_first_py = sb.toString().trim();
			}
			else
			{
				name_first_py = chach_name_first_py;
				name_single_length = chach_name_single_length;
			}
			int start = name_first_py.indexOf(condition);
			if (start > -1)
			{
				start = name_first_py.indexOf(condition);
				if (start > -1)
				{
					int end = start + condition.length();
					style.setSpan(new ForegroundColorSpan(Color.rgb(0xff, 0x77, 0x00)), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
				}
			}
			else
			{
				if (!name_first_py.contains(condition.substring(0, 1)))
				{
					return style;
				}
				index = 0;
				int count = 0;
				while (index > -1 && count < name_first_py.length())
				{
					index = name_first_py.indexOf(condition.substring(0, 1), index);
					str1 = getCharacterPinYin(name.charAt(index));
					if (!StringTools.isNull(str1) && (condition.contains(str1) || str1.contains(condition)))
					{
						break;
					}
					count++;
				}
				start = index;
				int end = start;
				int condition_length = condition.length();
				for (int i = index; i < name_single_length.length; i++)
				{
					end++;
					condition_length = condition_length - name_single_length[i];
					if (condition_length < 1)
					{
						break;
					}
				}
				style.setSpan(new ForegroundColorSpan(Color.rgb(0xff, 0x77, 0x00)), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
			}
		}
		return style;
	}

	public static SpannableStringBuilder getSpanNumberString5(String number, String condition)
	{
		SpannableStringBuilder style = new SpannableStringBuilder(number);
		if (!StringTools.isNull(condition))
		{
			int start = number.indexOf(condition);
			if (start > -1)
			{
				int end = start + condition.length();
				style.setSpan(new ForegroundColorSpan(Color.rgb(0xff, 0x77, 0x00)), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
			}
			else
			{
				// style.setSpan(new ForegroundColorSpan(Color.rgb(0xff, 0x77, 0x00)), 0, 0, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
			}
		}
		return style;
	}
}

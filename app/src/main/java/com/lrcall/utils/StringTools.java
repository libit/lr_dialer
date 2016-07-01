/*
 * Copyright (c) 2015. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */
package com.lrcall.utils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * String辅助类
 *
 * @author libit
 * @Date 2015-08-09
 */
public class StringTools
{
	private static final String CHINA_CODE = "+86";
	private static final String[] ipPrefix = new String[]{"12593", "17911", "17909"};

	/**
	 * 判断字符串是否符合正则表达式
	 *
	 * @param regExp 正则表达式
	 * @param string 要判断的字符串
	 * @return 匹配：true，不匹配：false;
	 */
	public static boolean isMatch(String regExp, String string)
	{
		// 如果正则表达式为空，则返回true
		if (isNull(regExp))
		{
			return true;
		}
		//如果判断的字符串为空，则返回fasle
		if (isNull(string))
		{
			return false;
		}
		Pattern p = Pattern.compile(regExp);
		Matcher m = p.matcher(string);
		return m.matches();
	}

	/**
	 * 判断是否是Email
	 *
	 * @param str
	 * @return
	 */
	public static boolean isEmail(String str)
	{
		String regExp = "^[a-z0-9]+([._\\-]*[a-z0-9])*@([a-z0-9]+[-a-z0-9]*[a-z0-9]+.){1,63}[a-z0-9]+$";
		return isMatch(regExp, str);
	}

	/**
	 * 判断是不是中国手机号码，包括手机和座机
	 *
	 * @param number 要判断的号码
	 * @return
	 */
	public static boolean isChinaPhoneNumber(String number)
	{
		// 正则表达式
		String regExp = "(\\+86)?((1\\d{10})|((0\\d{2,3})?\\d{8}))";
		return isMatch(regExp, number);
	}

	/**
	 * 判断是不是中国手机号码
	 *
	 * @param number 要判断的手机号码
	 * @return
	 */
	public static boolean isChinaMobilePhoneNumber(String number)
	{
		String regExp = "(\\+86)?1\\d{10}";
		return isMatch(regExp, number);
	}

	/**
	 * 判断是不是中国手机号码并带前缀
	 *
	 * @param number 要判断的号码
	 * @param prefix 前缀
	 * @return
	 */
	public static boolean isChinaMobilePhoneNumberWithPrefix(String number, String prefix)
	{
		if (isNull(prefix))
		{
			return isChinaMobilePhoneNumber(number);
		}
		String regExp = "(\\+86)?" + prefix + "1\\d{10}";
		return isMatch(regExp, number);
	}

	/**
	 * 判断是不是带区号的座机号码
	 *
	 * @param number 要判断的号码
	 * @return
	 */
	public static boolean isChinaTelePhoneNumberWithAreaCode(String number)
	{
		String regExp = "(\\+86)?0\\d{10,11}";
		return isMatch(regExp, number);
	}

	/**
	 * 判断是不是不带区号的座机号码
	 *
	 * @param number 要判断的号码
	 * @return
	 */
	public static boolean isChinaTelePhoneNumberWithoutAreaCode(String number)
	{
		String regExp = "\\d{8}";
		return isMatch(regExp, number);
	}

	/**
	 * 去除国家代号
	 *
	 * @param number 要去掉的号码
	 * @return
	 */
	public static String removeChinaCodeInChinaPhoneNumber(String number)
	{
		if (StringTools.isNull(number))
		{
			return "";
		}
		if (number.startsWith(CHINA_CODE))
		{
			return number.substring(CHINA_CODE.length());
		}
		return number;
	}

	/**
	 * 转换成中国的号码，去掉国家代码，添加前缀，如果是座机则添加区号
	 *
	 * @param number   号码
	 * @param prefix   要添加的前缀
	 * @param areaCode 区号
	 * @return
	 */
	public static String convertToChinaPhoneNumber(String number, String prefix, String areaCode)
	{
		if (StringTools.isNull(number))
		{
			return "";
		}
		if (isChinaPhoneNumber(number))
		{
			number = removeChinaCodeInChinaPhoneNumber(number);
			if (isChinaMobilePhoneNumber(number))
			{
				number = prefix + number;
			}
			else if (isChinaTelePhoneNumberWithoutAreaCode(number))
			{
				if (areaCode != null && !number.startsWith("0"))
				{
					number = areaCode + number;
				}
			}
		}
		return number;
	}

	/**
	 * 去掉号码的IP前缀
	 *
	 * @param number    被去掉的号码
	 * @param ipPrefixs IP前缀的集合
	 * @return 处理后的号码
	 */
	public static String trimIpPrefix(String number, ArrayList<String> ipPrefixs)
	{
		if (isNull(number))
		{
			return "";
		}
		number = removeChinaCodeInChinaPhoneNumber(number);
		int count = ipPrefix.length;
		for (int i = 0; i < count; i++)
		{
			if (number.startsWith(ipPrefix[i]))
			{
				number = number.substring(ipPrefix[i].length());
				return number;
			}
		}
		if (ipPrefixs != null && ipPrefixs.size() > 0)
		{
			for (String prefix : ipPrefixs)
			{
				if (number.startsWith(prefix))
				{
					number = number.substring(prefix.length());
					return number;
				}
			}
		}
		return number;
	}

	/**
	 * 判断字符串的长度范围
	 *
	 * @param str 要判断的字符串
	 * @param min 字符串最小长度
	 * @param max 字符串最大长度
	 * @return
	 */
	public static boolean isStringLengthBetween(String str, int min, int max)
	{
		// 如果最小长度小于0，则将最小值设为0
		if (min < 0)
		{
			min = 0;
		}
		// 如果最大长度小于最小长度，则将最大长度设为最小长度
		if (max < min)
		{
			max = min;
		}
		String regExp = "\\d{" + min + "," + max + "}";
		return isMatch(regExp, str);
	}

	/**
	 * 判断是不是未知来电号码
	 *
	 * @param number 要判断的号码
	 * @return
	 */
	public static boolean isUnKnownPhoneNumber(String number)
	{
		String regExp = "-\\d*";
		return isMatch(regExp, number);
	}

	/**
	 * 判断是不是数字（包含证书和带小数的数）
	 *
	 * @param str 要判断的字符串
	 * @return
	 */
	public static boolean isFloat(String str)
	{
		String regExp = "\\d+(.\\d*)?";
		Pattern p = Pattern.compile(regExp);
		Matcher m = p.matcher(str);
		return m.matches();
	}

	/**
	 * 转换成可以呼叫的号码
	 *
	 * @param originalCallNumber 原始号码
	 * @return
	 */
	public static String convertToCallPhoneNumber(String originalCallNumber)
	{
		if (StringTools.isNull(originalCallNumber))
		{
			return "";
		}
		originalCallNumber = removeChinaCodeInChinaPhoneNumber(originalCallNumber);
		String num = "";
		for (int i = 0; i < originalCallNumber.length(); i++)
		{
			char ch = originalCallNumber.charAt(i);
			if (ch >= '0' && ch <= '9')
			{
				num += ch;
			}
		}
		return num;
	}

	/**
	 * 判断字符串是不是为空
	 *
	 * @param str 要判断的字符串
	 * @return
	 */
	public static boolean isNull(String str)
	{
		if (str == null || str.length() < 1)
		{
			return true;
		}
		return false;
	}

	/**
	 * 取字符串之间的值
	 *
	 * @param str   原始字符串
	 * @param start 开始匹配的字符串
	 * @param end   结束匹配的字符串
	 * @return
	 */
	public static String getValue(String str, String start, String end)
	{
		String result = "";
		if (isNull(str))
		{
			return result;
		}
		int index = -1;
		if (isNull(start))
		{
			result = str;
		}
		else
		{
			index = str.indexOf(start);
			if (index > -1)
			{
				result = str.substring(index + start.length());
			}
		}
		if (!isNull(end))
		{
			index = result.indexOf(end);
			if (index > -1)
			{
				result = result.substring(0, index);
			}
		}
		return result;
	}

	/**
	 * 获取当前时间，格式为yyyyMMddHHmmss
	 *
	 * @return
	 */
	public static String getCurrentTimeNum()
	{
		return getTimeNum(System.currentTimeMillis());
	}

	/**
	 * 获取当前时间，格式为yyyyMMddHHmmss
	 *
	 * @param tm 时间long类型
	 * @return
	 */
	public static String getTimeNum(long tm)
	{
		SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
		String date = format.format(new Date(tm));
		return date;
	}

	/**
	 * 获取当前时间，格式为yyyy-MM-dd HH:mm:ss
	 *
	 * @return
	 */
	public static String getCurrentTime()
	{
		return getTime(System.currentTimeMillis());
	}

	/**
	 * 获取当前时间，格式为yyyy-MM-dd HH:mm:ss
	 *
	 * @param tm 时间long类型
	 * @return
	 */
	public static String getTime(long tm)
	{
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String date = format.format(new Date(tm));
		return date;
	}
}

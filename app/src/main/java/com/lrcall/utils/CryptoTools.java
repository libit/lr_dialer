package com.lrcall.utils;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

public class CryptoTools
{
	/**
	 * MD5的算法在RFC1321 中定义 在RFC 1321中，给出了Test suite用来检验你的实现是否正确：
	 * MD5 ("") = d41d8cd98f00b204e9800998ecf8427e
	 * MD5 ("a") = 0cc175b9c0f1b6a831c399e269772661
	 * MD5 ("abc") = 900150983cd24fb0d6963f7d28e17f72
	 * MD5 ("message digest") = f96b697d7cb7938d525a2f31aaf161d0
	 * MD5 ("abcdefghijklmnopqrstuvwxyz") = c3fcd3d76192e4007dfb496cca67e13b
	 * 传入参数：一个字节数组
	 * 传出参数：字节数组的 MD5 结果字符串
	 */
	public static String getMD5(byte[] source)
	{
		String s = null;
		// 用来将字节转换成 16 进制表示的字符
		char hexDigits[] = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
		try
		{
			java.security.MessageDigest md = java.security.MessageDigest.getInstance("MD5");
			md.update(source);
			byte tmp[] = md.digest();
			// MD5 的计算结果是一个 128 位的长整数， 用字节表示就是 16 个字节
			// 每个字节用 16 进制表示的话，使用两个字符， 所以表示成 16 进制需要 32 个字符
			char str[] = new char[16 * 2];
			int k = 0; // 表示转换结果中对应的字符位置
			for (int i = 0; i < 16; i++)
			{
				// 从第一个字节开始，对 MD5 的每一个字节 转换成 16 进制字符的转换
				// 取第 i 个字节
				byte byte0 = tmp[i];
				// 取字节中高 4 位的数字转换, >>> 为逻辑右移，将符号位一起右移
				str[k++] = hexDigits[byte0 >>> 4 & 0xf];
				// 取字节中低 4 位的数字转换
				str[k++] = hexDigits[byte0 & 0xf];
			}
			// 换后的结果转换为字符串
			s = new String(str);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return s;
	}

	/**
	 * MD5加密字符串
	 *
	 * @param str 带加密的字符串
	 * @return 加密后的字符串
	 */
	public static String getMD5Str(String str)
	{
		MessageDigest messageDigest = null;
		try
		{
			messageDigest = MessageDigest.getInstance("MD5");
			messageDigest.reset();
			messageDigest.update(str.getBytes("UTF-8"));
		}
		catch (NoSuchAlgorithmException e)
		{
			return "";
		}
		catch (UnsupportedEncodingException e)
		{
			e.printStackTrace();
			return "";
		}
		byte[] byteArray = messageDigest.digest();
		StringBuffer md5StrBuff = new StringBuffer();
		for (int i = 0; i < byteArray.length; i++)
		{
			if (Integer.toHexString(0xFF & byteArray[i]).length() == 1)
			{
				md5StrBuff.append("0").append(Integer.toHexString(0xFF & byteArray[i]));
			}
			else
			{
				md5StrBuff.append(Integer.toHexString(0xFF & byteArray[i]));
			}
		}
		return md5StrBuff.toString();
	}

	/**
	 * 按字典顺序排序并取MD5值
	 *
	 * @param params 字符数组
	 * @return 加密后的字符串
	 */
	public static String getSignValue(String[] params)
	{
		if (params == null || params.length == 0)
		{
			return "";
		}
		ArrayList<String> array = new ArrayList<>();
		for (Object str : params)
		{
			array.add((String) str);
		}
		Collections.sort(array);
		StringBuilder builder = new StringBuilder();
		for (String s : array)
		{
			builder.append(s);
		}
		LogcatTools.debug("客户端签名", "客户端签名数据：" + builder.toString() + ",签名结果：" + getMD5Str(builder.toString()));
		return getMD5Str(builder.toString());
	}

	public static String getSignValue(Collection<Object> params)
	{
		if (params == null || params.size() == 0)
		{
			return "";
		}
		ArrayList<String> array = new ArrayList<>();
		for (Object str : params)
		{
			if (str != null && str.getClass().getSimpleName().equals("String"))
			{
				String s = (String) str;
				if (!StringTools.isNull(s))
				{
					array.add(s);
				}
			}
		}
		Collections.sort(array);
		StringBuilder builder = new StringBuilder();
		for (String s : array)
		{
			builder.append(s);
		}
		LogcatTools.debug("客户端签名", "客户端签名数据：" + builder.toString() + ",签名结果：" + getMD5Str(builder.toString()));
		return getMD5Str(builder.toString());
	}
}

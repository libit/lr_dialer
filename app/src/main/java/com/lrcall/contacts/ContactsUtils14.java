package com.lrcall.contacts;

import android.text.SpannableStringBuilder;

import com.lrcall.utils.PinyinTools;

public class ContactsUtils14 extends ContactsUtils8
{
	/**
	 * 返回带颜色的姓名
	 *
	 * @param name
	 * @param condition
	 * @return
	 */
	@Override
	public SpannableStringBuilder getSpanNameString(String name, String condition)
	{
		return PinyinTools.getSpanNumberString(name, condition);
	}

	/**
	 * 返回带颜色的号码
	 *
	 * @param number
	 * @param condition
	 * @return
	 */
	@Override
	public SpannableStringBuilder getSpanNumberString(String number, String condition)
	{
		return PinyinTools.getSpanNumberString(number, condition);
	}
}

package com.lrcall.contacts;

import android.content.Context;
import android.net.Uri;
import android.provider.ContactsContract;

import com.lrcall.utils.LogcatTools;

public class ContactsUtils19 extends ContactsUtils14
{
	public static final String SORT_KEY = "phonebook_label";
	public static final String PHOTO_URI = ContactsContract.Contacts.PHOTO_URI;

	/**
	 * 测试失败
	 *
	 * @param context
	 * @param contactId 联系人ID
	 * @return
	 */
	@Override
	public boolean deleteContact(Context context, Long contactId)
	{
		if (contactId == null)
		{
			return false;
		}
		LogcatTools.debug("deleteContact", "contactId:" + contactId);
		//根据姓名求id
		int count = context.getContentResolver().delete(Uri.parse("content://com.android.contacts/contacts"), "contact_id" + " = ?", new String[]{contactId.toString()});
		return count > 0;
	}
}

package com.lrcall.contacts;

import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.provider.ContactsContract;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;

import com.lrcall.appcall.R;
import com.lrcall.models.ContactInfo;
import com.lrcall.utils.LogcatTools;
import com.lrcall.utils.PinyinTools;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class ContactsUtils8 extends ContactsFactory
{
	/**
	 * 获取所有联系人
	 *
	 * @param context 应用Context
	 * @return 联系人数组
	 */
	public List<ContactInfo> getContactInfos(Context context)
	{
		Uri uri = ContactsContract.Contacts.CONTENT_URI;
		String[] projection = new String[]{CONTACT_ID, DISPLAY_NAME, PHOTO_ID, STARRED, SORT_KEY};
		String selection = ContactsContract.Contacts.HAS_PHONE_NUMBER + " = '1'";
		String[] selectionArgs = null;
		String sortOrder = SORT_KEY + " COLLATE LOCALIZED ASC";
		Cursor cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs, sortOrder);
		List<ContactInfo> list = new ArrayList<>();
		if (cursor != null)
		{
			if (cursor.moveToFirst())
			{
				do
				{
					ContactInfo contactInfo = new ContactInfo();
					Long id = cursor.getLong(cursor.getColumnIndex(CONTACT_ID));
					String name = cursor.getString(cursor.getColumnIndex(DISPLAY_NAME));
					String py = cursor.getString(cursor.getColumnIndex(SORT_KEY));
					Long photoId = cursor.getLong(cursor.getColumnIndex(PHOTO_ID));
					boolean starred = cursor.getInt(cursor.getColumnIndex(STARRED)) == 1 ? true : false;
					if (photoId == null)
					{
						photoId = 0L;
					}
					contactInfo.setContactId(id);
					contactInfo.setName(name);
					contactInfo.setPy(py);
					contactInfo.setPhotoId(photoId);
					contactInfo.setStarred(starred);
					list.add(contactInfo);
				}
				while (cursor.moveToNext());
			}
			cursor.close();
		}
		return list;
	}

	/**
	 * 查询联系人列表
	 *
	 * @param context   应用Context
	 * @param condition 查询条件
	 * @return 联系人数组
	 */
	public List<ContactInfo> getContactInfos(Context context, String condition)
	{
		Uri uri = Uri.withAppendedPath(ContactsContract.CommonDataKinds.Phone.CONTENT_FILTER_URI, condition);
		//		String selection = CommonDataKinds.Phone.HAS_PHONE_NUMBER + " = '1'";
		//		String selection = CommonDataKinds.Phone.NUMBER + " LIKE '%" + condition + "%' OR " + CommonDataKinds.Phone.DISPLAY_NAME + " LIKE '%" + condition + "%'";
		String sortOrder = SORT_KEY + " COLLATE LOCALIZED ASC";
		Cursor cursor = context.getContentResolver().query(uri, new String[]{ContactsContract.CommonDataKinds.Phone.CONTACT_ID, ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME, ContactsContract.CommonDataKinds.Phone.NUMBER, ContactsContract.CommonDataKinds.Phone.TYPE, ContactsContract.CommonDataKinds.Phone.PHOTO_ID, ContactsContract.CommonDataKinds.Phone.STARRED, SORT_KEY}, null, null, sortOrder);
		List<ContactInfo> list = new ArrayList<>();
		if (cursor != null)
		{
			if (cursor.moveToFirst())
			{
				do
				{
					Long contactId = cursor.getLong(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.CONTACT_ID));
					String number = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
					String type = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.TYPE));
					// 如果和上个联系人ID一样则合并
					if (list.size() > 0 && list.get(list.size() - 1).getContactId() == contactId)
					{
						list.get(list.size() - 1).getPhoneInfoList().add(list.get(list.size() - 1).new PhoneInfo(number, type));
					}
					else
					{
						String name = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
						String py = cursor.getString(cursor.getColumnIndex(SORT_KEY));
						boolean starred = cursor.getInt(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.STARRED)) == 1 ? true : false;
						Long photoId = cursor.getLong(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.PHOTO_ID));
						ContactInfo contactInfo = new ContactInfo();
						contactInfo.setContactId(contactId);
						contactInfo.setName(name);
						contactInfo.setPy(py);
						contactInfo.setStarred(starred);
						//						Uri uri = ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI, contactId);
						//						contactInfo.setContactPhoto(getContactPhoto(context, uri));
						contactInfo.setPhotoId(photoId);
						LogcatTools.debug("联系人图片ID", contactInfo.getName() + "->" + contactInfo.getPhotoId());
						List<ContactInfo.PhoneInfo> phoneInfos = new ArrayList<>();
						phoneInfos.add(new ContactInfo().new PhoneInfo(number, type));
						contactInfo.setPhoneInfoList(phoneInfos);
						list.add(contactInfo);
					}
				}
				while (cursor.moveToNext());
			}
			cursor.close();
		}
		return list;
	}

	/**
	 * 根据ID获取联系人
	 *
	 * @param context   应用Context
	 * @param contactId 联系人ID
	 * @return 联系人信息
	 */
	public ContactInfo getContactInfoById(Context context, Long contactId)
	{
		String sortOrder = SORT_KEY + " COLLATE LOCALIZED ASC";
		Cursor cursor = context.getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?", new String[]{contactId.toString()}, sortOrder);
		ContactInfo contactInfo = new ContactInfo();
		if (cursor != null)
		{
			if (cursor.moveToFirst())
			{
				String name = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
				boolean starred = cursor.getInt(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.STARRED)) == 1 ? true : false;
				Long photoId = cursor.getLong(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.PHOTO_ID));
				contactInfo.setContactId(contactId);
				contactInfo.setName(name);
				contactInfo.setStarred(starred);
				contactInfo.setPhotoId(photoId);
				LogcatTools.debug("联系人图片ID", contactInfo.getName() + "->" + contactInfo.getPhotoId());
				Uri uri = ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI, contactId);
				contactInfo.setContactPhoto(getContactPhoto(context, uri));
				List<ContactInfo.PhoneInfo> phoneInfos = new ArrayList<>();
				do
				{
					String number = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
					String type = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.TYPE));
					phoneInfos.add(new ContactInfo().new PhoneInfo(number, type));
				}
				while (cursor.moveToNext());
				contactInfo.setPhoneInfoList(phoneInfos);
			}
			cursor.close();
		}
		return contactInfo;
	}

	/**
	 * 根据姓名获取联系人
	 *
	 * @param context 应用Context
	 * @param name    姓名
	 * @return 联系人列表（考虑到用户可以存储多个同名的名片）
	 */
	public List<ContactInfo> getContactInfosByName(Context context, String name)
	{
		String sortOrder = SORT_KEY + " COLLATE LOCALIZED ASC";
		Cursor cursor = context.getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " = ?", new String[]{name}, sortOrder);
		List<ContactInfo> contactInfos = new ArrayList<>();
		if (cursor != null)
		{
			if (cursor.moveToFirst())
			{
				do
				{
					Long contactId = cursor.getLong(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.CONTACT_ID));
					boolean starred = cursor.getInt(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.STARRED)) == 1 ? true : false;
					Long photoId = cursor.getLong(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.PHOTO_ID));
					String number = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
					String type = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.TYPE));
					// 如果和上个联系人ID一样则合并
					if (contactInfos.size() > 0 && contactInfos.get(contactInfos.size() - 1).getContactId() == contactId)
					{
						contactInfos.get(contactInfos.size() - 1).getPhoneInfoList().add(contactInfos.get(contactInfos.size() - 1).new PhoneInfo(number, type));
					}
					else
					{
						ContactInfo contactInfo = new ContactInfo();
						contactInfo.setContactId(contactId);
						contactInfo.setName(name);
						contactInfo.setStarred(starred);
						contactInfo.setPhotoId(photoId);
						LogcatTools.debug("联系人图片ID", contactInfo.getName() + "->" + contactInfo.getPhotoId());
						Uri uri = ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI, contactId);
						contactInfo.setContactPhoto(getContactPhoto(context, uri));
						List<ContactInfo.PhoneInfo> phoneInfos = new ArrayList<>();
						phoneInfos.add(new ContactInfo().new PhoneInfo(number, type));
						contactInfo.setPhoneInfoList(phoneInfos);
					}
				}
				while (cursor.moveToNext());
			}
			cursor.close();
		}
		return contactInfos;
	}

	/**
	 * 根据号码获取联系人
	 *
	 * @param context 应用Context
	 * @param number  号码
	 * @return 联系人列表（考虑到用户可以存储多个同号码的名片）
	 */
	public List<ContactInfo> getContactInfosByNumber(Context context, String number)
	{
		String sortOrder = SORT_KEY + " COLLATE LOCALIZED ASC";
		Cursor cursor = context.getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, ContactsContract.CommonDataKinds.Phone.NUMBER + " = ?", new String[]{number}, sortOrder);
		List<ContactInfo> contactInfos = new ArrayList<>();
		if (cursor != null)
		{
			if (cursor.moveToFirst())
			{
				do
				{
					Long contactId = cursor.getLong(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.CONTACT_ID));
					boolean starred = cursor.getInt(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.STARRED)) == 1 ? true : false;
					Long photoId = cursor.getLong(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.PHOTO_ID));
					String name = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
					String type = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.TYPE));
					// 如果和上个联系人ID一样则合并
					if (contactInfos.size() > 0 && contactInfos.get(contactInfos.size() - 1).getContactId() == contactId)
					{
						contactInfos.get(contactInfos.size() - 1).getPhoneInfoList().add(contactInfos.get(contactInfos.size() - 1).new PhoneInfo(number, type));
					}
					else
					{
						ContactInfo contactInfo = new ContactInfo();
						contactInfo.setContactId(contactId);
						contactInfo.setName(name);
						contactInfo.setStarred(starred);
						contactInfo.setPhotoId(photoId);
						LogcatTools.debug("联系人图片ID", contactInfo.getName() + "->" + contactInfo.getPhotoId());
						Uri uri = ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI, contactId);
						contactInfo.setContactPhoto(getContactPhoto(context, uri));
						List<ContactInfo.PhoneInfo> phoneInfos = new ArrayList<>();
						phoneInfos.add(new ContactInfo().new PhoneInfo(number, type));
						contactInfo.setPhoneInfoList(phoneInfos);
					}
				}
				while (cursor.moveToNext());
			}
			cursor.close();
		}
		return contactInfos;
	}

	/**
	 * 根据号码获取姓名
	 *
	 * @param context 应用Context
	 * @param number  号码
	 * @return 姓名，如果没找到则返回空，如果找到多个，则返回第一个找到的
	 */
	public String getContactNameByNumber(Context context, String number)
	{
		String sortOrder = SORT_KEY + " COLLATE LOCALIZED ASC";
		Cursor cursor = context.getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, ContactsContract.CommonDataKinds.Phone.NUMBER + " = ?", new String[]{number}, sortOrder);
		List<ContactInfo> contactInfos = new ArrayList<>();
		if (cursor != null)
		{
			if (cursor.moveToFirst())
			{
				do
				{
					Long contactId = cursor.getLong(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.CONTACT_ID));
					boolean starred = cursor.getInt(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.STARRED)) == 1 ? true : false;
					Long photoId = cursor.getLong(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.PHOTO_ID));
					String name = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
					String type = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.TYPE));
					// 如果和上个联系人ID一样则合并
					if (contactInfos.size() > 0 && contactInfos.get(contactInfos.size() - 1).getContactId() == contactId)
					{
						contactInfos.get(contactInfos.size() - 1).getPhoneInfoList().add(contactInfos.get(contactInfos.size() - 1).new PhoneInfo(number, type));
					}
					else
					{
						ContactInfo contactInfo = new ContactInfo();
						contactInfo.setContactId(contactId);
						contactInfo.setName(name);
						contactInfo.setStarred(starred);
						contactInfo.setPhotoId(photoId);
						LogcatTools.debug("联系人图片ID", contactInfo.getName() + "->" + contactInfo.getPhotoId());
						Uri uri = ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI, contactId);
						contactInfo.setContactPhoto(getContactPhoto(context, uri));
						List<ContactInfo.PhoneInfo> phoneInfos = new ArrayList<>();
						phoneInfos.add(new ContactInfo().new PhoneInfo(number, type));
						contactInfo.setPhoneInfoList(phoneInfos);
					}
				}
				while (cursor.moveToNext());
			}
			cursor.close();
		}
		if (contactInfos.size() > 0)
		{
			return contactInfos.get(0).getName();
		}
		else
		{
			return "";
		}
	}

	/**
	 * 获取联系人图片
	 *
	 * @param context 应用Context
	 * @param uri
	 * @return
	 */
	public Bitmap getContactPhoto(Context context, Uri uri)
	{
		InputStream s = ContactsContract.Contacts.openContactPhotoInputStream(context.getContentResolver(), uri);
		Bitmap img = BitmapFactory.decodeStream(s);
		if (img == null)
		{
			BitmapDrawable drawableBitmap = ((BitmapDrawable) context.getResources().getDrawable(R.drawable.placeholder_large));
			if (drawableBitmap != null)
			{
				img = drawableBitmap.getBitmap();
			}
		}
		return img;
	}

	/**
	 * 获取联系人图片
	 *
	 * @param context 应用Context
	 * @param photoId
	 * @return
	 */
	public Bitmap getContactPhoto(Context context, Long photoId)
	{
		Cursor cursor = context.getContentResolver().query(ContactsContract.Data.CONTENT_URI, new String[]{ContactsContract.CommonDataKinds.Photo.PHOTO}, ContactsContract.CommonDataKinds.Photo.PHOTO_ID + " = ? " + " AND " + ContactsContract.Contacts.Data.MIMETYPE + " = '" + ContactsContract.CommonDataKinds.Photo.CONTENT_ITEM_TYPE + "'", new String[]{String.valueOf(photoId)}, null);
		Bitmap img = null;
		BitmapDrawable drawableBitmap = ((BitmapDrawable) context.getResources().getDrawable(R.drawable.placeholder_large));
		if (drawableBitmap != null)
		{
			img = drawableBitmap.getBitmap();
		}
		if (cursor != null)
		{
			if (cursor.moveToFirst())
			{
				byte[] photo = cursor.getBlob(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Photo.PHOTO));
				img = BitmapFactory.decodeByteArray(photo, 0, photo.length);
				if (img == null && drawableBitmap != null)
				{
					img = drawableBitmap.getBitmap();
				}
			}
			cursor.close();
		}
		return img;
	}

	/**
	 * 设置字符串高亮效果
	 *
	 * @param str   要高亮的字符串
	 * @param start 起始位置
	 * @param end   终点位置
	 * @return 高亮的字符串
	 */
	public SpannableStringBuilder getSpanString(String str, int start, int end)
	{
		if (start < 0)
		{
			start = 0;
		}
		if (end < start)
		{
			end = start;
		}
		SpannableStringBuilder style = new SpannableStringBuilder(str);
		style.setSpan(new ForegroundColorSpan(Color.rgb(0xff, 0x77, 0x00)), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		return style;
	}

	/**
	 * 添加联系人，跳转到系统添加联系人界面
	 *
	 * @param context 应用Context
	 * @param number
	 */
	public void toSystemAddContactActivity(Context context, String number)
	{
		Intent intent = new Intent(Intent.ACTION_INSERT, ContactsContract.Contacts.CONTENT_URI);
		intent.putExtra("phone", number);
		context.startActivity(intent);
	}

	/**
	 * 编辑联系人，跳转到系统添加联系人界面
	 *
	 * @param context   应用Context
	 * @param contactId 联系人ID
	 */
	public void toSystemEditContactActivity(Context context, Long contactId)
	{
		Intent intent = new Intent(Intent.ACTION_EDIT);
		intent.setData(ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI, contactId));
		context.startActivity(intent);
	}

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
		return PinyinTools.getSpanNumberString5(name, condition);
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
		return PinyinTools.getSpanNameString5(number, condition);
	}

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
		int count = context.getContentResolver().delete(ContactsContract.RawContacts.CONTENT_URI, ContactsContract.Contacts.NAME_RAW_CONTACT_ID + " = ?", new String[]{contactId.toString()});
		boolean result = count > 0;
		return result;
	}
}

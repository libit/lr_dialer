/*
 * Libit保留所有版权，如有疑问联系QQ：308062035
 * Copyright (c) 2016.
 */
package com.lrcall.contacts;

public class ContactsUtils23 extends ContactsUtils19
{
//	private static final int REQUEST_READ_CONTACTS = 0;
//
//	private void accessContacts()
//	{
//		if (!mayRequestContacts())
//		{
//			return;
//		}
//
//	}
//
//	private boolean mayRequestContacts()
//	{
//		if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M)
//		{
//			return true;
//		}
//		if (checkSelfPermission(READ_CONTACTS) == PackageManager.PERMISSION_GRANTED)
//		{
//			return true;
//		}
//		if (shouldShowRequestPermissionRationale(READ_CONTACTS))
//		{
//			Snackbar.make(mEmailView, R.string.permission_rationale, Snackbar.LENGTH_INDEFINITE).setAction(android.R.string.ok, new View.OnClickListener()
//			{
//				@Override
//				@TargetApi(Build.VERSION_CODES.M)
//				public void onClick(View v)
//				{
//					requestPermissions(new String[]{READ_CONTACTS}, REQUEST_READ_CONTACTS);
//				}
//			});
//		}
//		else
//		{
//			requestPermissions(new String[]{READ_CONTACTS}, REQUEST_READ_CONTACTS);
//		}
//		return false;
//	}
//
//	@Override
//	public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults)
//	{
//		if (requestCode == REQUEST_READ_CONTACTS)
//		{
//			if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
//			{
//				// Permission granted , Access contacts here or do whatever you need.
//			}
//		}
//	}
}

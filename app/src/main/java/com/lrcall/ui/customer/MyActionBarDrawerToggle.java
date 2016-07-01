/*
 * Libit保留所有版权，如有疑问联系QQ：308062035
 * Copyright (c) 2016.
 */
package com.lrcall.ui.customer;

import android.app.Activity;
import android.support.annotation.StringRes;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.lrcall.utils.LogcatTools;

/**
 * Created by libit on 16/5/7.
 */
public class MyActionBarDrawerToggle extends ActionBarDrawerToggle
{
	private static final String TAG = MyActionBarDrawerToggle.class.getSimpleName();
	private ActionBarDrawerToggleStatusChanged statusChanged;

	/**
	 * Construct a new ActionBarDrawerToggle.
	 * <p>
	 * <p>The given {@link Activity} will be linked to the specified {@link DrawerLayout} and
	 * its Actionbar's Up button will be set to a custom drawable.
	 * <p>This drawable shows a Hamburger icon when drawer is closed and an arrow when drawer
	 * is open. It animates between these two states as the drawer opens.</p>
	 * <p>
	 * <p>String resources must be provided to describe the open/close drawer actions for
	 * accessibility services.</p>
	 *
	 * @param activity                  The Activity hosting the drawer. Should have an ActionBar.
	 * @param drawerLayout              The DrawerLayout to link to the given Activity's ActionBar
	 * @param openDrawerContentDescRes  A String resource to describe the "open drawer" action
	 *                                  for accessibility
	 * @param closeDrawerContentDescRes A String resource to describe the "close drawer" action
	 */
	public MyActionBarDrawerToggle(Activity activity, DrawerLayout drawerLayout, @StringRes int openDrawerContentDescRes, @StringRes int closeDrawerContentDescRes, ActionBarDrawerToggleStatusChanged actionBarDrawerToggleStatusChanged)
	{
		super(activity, drawerLayout, openDrawerContentDescRes, closeDrawerContentDescRes);
		this.statusChanged = actionBarDrawerToggleStatusChanged;
	}

	/**
	 * Construct a new ActionBarDrawerToggle with a Toolbar.
	 * <p/>
	 * The given {@link Activity} will be linked to the specified {@link DrawerLayout} and
	 * the Toolbar's navigation icon will be set to a custom drawable. Using this constructor
	 * will set Toolbar's navigation click listener to toggle the drawer when it is clicked.
	 * <p/>
	 * This drawable shows a Hamburger icon when drawer is closed and an arrow when drawer
	 * is open. It animates between these two states as the drawer opens.
	 * <p/>
	 * String resources must be provided to describe the open/close drawer actions for
	 * accessibility services.
	 * <p/>
	 * Please use {@link #ActionBarDrawerToggle(Activity, DrawerLayout, int, int)} if you are
	 * setting the Toolbar as the ActionBar of your activity.
	 *
	 * @param activity                  The Activity hosting the drawer.
	 * @param drawerLayout              The DrawerLayout to link to the given Activity's ActionBar
	 * @param toolbar                   The toolbar to use if you have an independent Toolbar.
	 * @param openDrawerContentDescRes  A String resource to describe the "open drawer" action
	 *                                  for accessibility
	 * @param closeDrawerContentDescRes A String resource to describe the "close drawer" action
	 */
	public MyActionBarDrawerToggle(Activity activity, DrawerLayout drawerLayout, Toolbar toolbar, @StringRes int openDrawerContentDescRes, @StringRes int closeDrawerContentDescRes, ActionBarDrawerToggleStatusChanged actionBarDrawerToggleStatusChanged)
	{
		super(activity, drawerLayout, toolbar, openDrawerContentDescRes, closeDrawerContentDescRes);
		this.statusChanged = actionBarDrawerToggleStatusChanged;
	}

	@Override
	public void onDrawerOpened(View drawerView)
	{
		super.onDrawerOpened(drawerView);
		LogcatTools.debug(TAG, "onDrawerOpened");
		if (statusChanged != null)
		{
			statusChanged.onDrawerOpened(drawerView);
		}
	}

	@Override
	public void onDrawerClosed(View drawerView)
	{
		super.onDrawerClosed(drawerView);
		LogcatTools.debug(TAG, "onDrawerClosed");
		if (statusChanged != null)
		{
			statusChanged.onDrawerClosed(drawerView);
		}
	}

	public interface ActionBarDrawerToggleStatusChanged
	{
		void onDrawerOpened(View drawerView);

		void onDrawerClosed(View drawerView);
	}
}

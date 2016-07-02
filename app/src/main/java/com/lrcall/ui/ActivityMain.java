/*
 * Libit保留所有版权，如有疑问联系QQ：308062035
 * Copyright (c) 2015.
 */
package com.lrcall.ui;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.external.xlistview.XListView;
import com.lrcall.appcall.R;
import com.lrcall.contacts.ContactsFactory;
import com.lrcall.models.TabInfo;
import com.lrcall.nums.EventTypeLayoutSideMain;
import com.lrcall.ui.customer.MyActionBarDrawerToggle;
import com.lrcall.utils.LogcatTools;
import com.lrcall.utils.apptools.AppFactory;
import com.ogaclejapan.smarttablayout.SmartTabLayout;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItem;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItemAdapter;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItems;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.RuntimePermissions;

@RuntimePermissions
public class ActivityMain extends MyBaseActivity implements MyActionBarDrawerToggle.ActionBarDrawerToggleStatusChanged, XListView.IXListViewListener
{
	private List<TabInfo> tabInfos = new ArrayList<>();
	private DrawerLayout mDrawerLayout;
	private ActionBarDrawerToggle mDrawerToggle;
	private ViewPager viewPager;
	private XListView xListView;
	private LayoutSideMain layoutSideMain;
	private Menu menu;
	private static ActivityMain instance = null;

	public static ActivityMain getInstance()
	{
		return instance;
	}

	@NeedsPermission({Manifest.permission.READ_CONTACTS, Manifest.permission.READ_CALL_LOG})
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		viewInit();
		instance = this;
		EventBus.getDefault().register(this);
	}

	@Override
	protected void onPause()
	{
		super.onPause();
		mDrawerLayout.closeDrawers();
	}

	@Override
	protected void onDestroy()
	{
		instance = null;
		EventBus.getDefault().unregister(this);
		super.onDestroy();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		getMenuInflater().inflate(R.menu.menu_activity_main, menu);
		this.menu = menu;
		setMenu();
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		LogcatTools.debug("onOptionsItemSelected", "ActivityMain");
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent context in AndroidManifest.xml.
		int id = item.getItemId();
		//noinspection SimplifiableIfStatement
		if (id == R.id.action_search)
		{
			//			Toast.makeText(this, "点击搜索", Toast.LENGTH_LONG).show();
			startActivity(new Intent(this, ActivitySearch.class));
			return true;
		}
		else if (id == R.id.action_settings)
		{
			startActivity(new Intent(this, ActivitySearch.class));
			return true;
		}
		else if (id == R.id.action_check_update)
		{
			Toast.makeText(this, "检查更新", Toast.LENGTH_LONG).show();
			return true;
		}
		else if (id == R.id.action_share)
		{
			Toast.makeText(this, "好友分享", Toast.LENGTH_LONG).show();
			return true;
		}
		else if (id == R.id.action_exit)
		{
			exit();
			return true;
		}
		else if (id == R.id.action_add_contact)
		{
			ContactsFactory.getInstance().toSystemAddContactActivity(this, "");
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	synchronized protected void viewInit()
	{
		super.viewInit();
		setSwipeBackEnable(false); //禁止滑动返回
		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer);
		mDrawerToggle = new MyActionBarDrawerToggle(this, mDrawerLayout, mToolbar, R.string.drawer_open, R.string.drawer_close, this);
		mDrawerToggle.syncState();
		mDrawerLayout.setDrawerListener(mDrawerToggle);
		//初始化Tab
		tabInfos.add(new TabInfo("拨号", R.drawable.ic_tab_dialer_normal, FragmentDialer.class));
		tabInfos.add(new TabInfo("联系人", R.drawable.ic_tab_contacts_normal, FragmentContacts.class));
		//		tabInfos.add(new TabInfo("短信", R.drawable.ic_tab_money_normal, FragmentSms.class));
		tabInfos.add(new TabInfo("常用", R.drawable.ic_tab_more_normal, FragmentMore.class));
		ViewGroup tab = (ViewGroup) findViewById(R.id.tab);
		//加载tab布局
		tab.addView(LayoutInflater.from(this).inflate(R.layout.layout_custom_tab, tab, false));
		viewPager = (ViewPager) findViewById(R.id.viewpager);
		SmartTabLayout viewPagerTab = (SmartTabLayout) findViewById(R.id.viewpagertab);
		final LayoutInflater inflater = LayoutInflater.from(viewPagerTab.getContext());
		viewPagerTab.setCustomTabView(new SmartTabLayout.TabProvider()
		{
			@Override
			public View createTabView(ViewGroup container, int position, PagerAdapter adapter)
			{
				TabInfo tabInfo = tabInfos.get(position);
				View view = inflater.inflate(R.layout.item_tab, container, false);
				ImageView imageView = (ImageView) view.findViewById(R.id.tab_icon);
				TextView textView = (TextView) view.findViewById(R.id.tab_label);
				imageView.setImageResource(tabInfo.getImgResId());
				textView.setText(tabInfo.getLabel());
				tabInfo.setImgIcon(imageView);
				tabInfo.setTvLabel(textView);
				return view;
			}
		});
		viewPagerTab.setOnPageChangeListener(new ViewPager.OnPageChangeListener()
		{
			@Override
			public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels)
			{
			}

			@Override
			public void onPageSelected(int position)
			{
				int size = tabInfos.size();
				for (int i = 0; i < size; i++)
				{
					TabInfo tabInfo = tabInfos.get(i);
					if (i == position)
					{
						tabInfo.getTvLabel().setTextColor(getResources().getColor(R.color.icon_enabled));
						mToolbar.setTitle(tabInfo.getLabel());
					}
					else
					{
						tabInfo.getTvLabel().setTextColor(getResources().getColor(R.color.icon_disabled));
					}
				}
				setMenu();
			}

			@Override
			public void onPageScrollStateChanged(int state)
			{
			}
		});
		FragmentPagerItems pages = new FragmentPagerItems(this);
		for (TabInfo tabInfo : tabInfos)
		{
			pages.add(FragmentPagerItem.of(tabInfo.getLabel(), tabInfo.getLoadClass()));
		}
		FragmentPagerItemAdapter adapter = new FragmentPagerItemAdapter(getSupportFragmentManager(), pages);
		viewPager.setAdapter(adapter);
		viewPagerTab.setViewPager(viewPager);
		tabInfos.get(0).getTvLabel().setTextColor(getResources().getColor(R.color.icon_enabled));
		//侧滑布局
		xListView = (XListView) findViewById(R.id.xlist);
		layoutSideMain = new LayoutSideMain(this);
		xListView.setPullRefreshEnable(true);
		xListView.setPullLoadEnable(false);
		xListView.addHeaderView(layoutSideMain);
		xListView.setAdapter(null);
		xListView.setXListViewListener(this);
	}

	/**
	 * 设置当前到第几页
	 *
	 * @param index
	 */
	public void setCurrentPage(int index)
	{
		if (index > -1 && index < tabInfos.size())
		{
			viewPager.setCurrentItem(index);
		}
	}

	/**
	 * 设置菜单
	 */
	public void setMenu()
	{
		int index = viewPager.getCurrentItem();
		if (index == 0)
		{
			menu.findItem(R.id.action_search).setVisible(true);
			menu.findItem(R.id.action_add_contact).setVisible(false);
			if (AppFactory.isCompatible(11))
			{
				menu.findItem(R.id.action_share).setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
				menu.findItem(R.id.action_check_update).setShowAsAction(MenuItem.SHOW_AS_ACTION_NEVER);
			}
		}
		else if (index == 1)
		{
			menu.findItem(R.id.action_search).setVisible(true);
			menu.findItem(R.id.action_add_contact).setVisible(true);
			if (AppFactory.isCompatible(11))
			{
				menu.findItem(R.id.action_share).setShowAsAction(MenuItem.SHOW_AS_ACTION_NEVER);
				menu.findItem(R.id.action_check_update).setShowAsAction(MenuItem.SHOW_AS_ACTION_NEVER);
			}
		}
		else if (index == 2)
		{
			menu.findItem(R.id.action_search).setVisible(false);
			menu.findItem(R.id.action_add_contact).setVisible(false);
			if (AppFactory.isCompatible(11))
			{
				menu.findItem(R.id.action_share).setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
				menu.findItem(R.id.action_check_update).setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
			}
		}
	}

	/**
	 * 设置标题
	 *
	 * @param title
	 */
	@Override
	public void setTitle(CharSequence title)
	{
		mToolbar.setTitle(title);
	}

	@Override
	public void onDrawerOpened(View drawerView)
	{
		layoutSideMain.refresh();
	}

	@Override
	public void onDrawerClosed(View drawerView)
	{
	}

	@Override
	public void onRefresh()
	{
		layoutSideMain.refresh();
		xListView.stopRefresh();
	}

	@Override
	public void onLoadMore()
	{
	}

	@Override
	public void onBackPressed()
	{
		startActivity(new Intent(Intent.ACTION_MAIN).addCategory(Intent.CATEGORY_HOME).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
	}

	public void exit()
	{
		finish();
	}

	@Subscribe
	public void onEventMainThread(EventTypeLayoutSideMain msg)
	{
		if (msg.getType().equalsIgnoreCase(EventTypeLayoutSideMain.CLOSE_DRAWER.getType()))
		{
			mDrawerLayout.closeDrawers();
		}
	}
}

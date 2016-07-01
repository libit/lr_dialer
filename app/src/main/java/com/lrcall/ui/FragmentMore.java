/*
 * Libit保留所有版权，如有疑问联系QQ：308062035
 * Copyright (c) 2016.
 */
package com.lrcall.ui;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.Toast;

import com.external.xlistview.XListView;
import com.lrcall.appcall.R;
import com.lrcall.models.FuncInfo;
import com.lrcall.ui.adapter.FuncsAdapter;
import com.lrcall.utils.DisplayTools;
import com.lrcall.utils.LogcatTools;
import com.lrcall.utils.StringTools;
import com.ogaclejapan.smarttablayout.SmartTabLayout;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class FragmentMore extends MyBaseFragment implements XListView.IXListViewListener, FuncsAdapter.IFuncsAdapterItemClicked
{
	private static final String TAG = FragmentMore.class.getSimpleName();
	private static final int SCROLL_VIEW_PAGE = 111;
	private XListView xListView;
	private View headView;
	private GridView gvFuncs;
	private List<Fragment> mFragmentRecommendList = new ArrayList<>();
	private ViewPager viewPager;
	private ScheduledExecutorService scheduledExecutorService = null;
	private ScheduledFuture scheduledFuture = null;
	private long scrollTime = 5;

	synchronized private void updateView()
	{
		if (scheduledExecutorService == null)
		{
			scheduledExecutorService = Executors.newScheduledThreadPool(1);
		}
		else
		{
			cancelScheduledFuture();
		}
		try
		{
			scheduledFuture = scheduledExecutorService.scheduleAtFixedRate(updateViewpagerThread, scrollTime, scrollTime, TimeUnit.SECONDS);
		}
		catch (RejectedExecutionException e)
		{
			LogcatTools.debug(TAG, "ScheduledFuture,RejectedExecutionException:" + e.getMessage());
		}
	}

	Thread updateViewpagerThread = new Thread("updateView")
	{
		@Override
		public void run()
		{
			super.run();
			LogcatTools.debug(TAG, "ScheduledFuture,时间:" + StringTools.getCurrentTime());
			mHandler.sendEmptyMessage(SCROLL_VIEW_PAGE);
		}
	};

	synchronized private void cancelScheduledFuture()
	{
		if (scheduledFuture != null && !scheduledFuture.isCancelled())
		{
			scheduledFuture.cancel(true);
			scheduledFuture = null;
		}
	}

	synchronized private void stopScheduledFuture()
	{
		cancelScheduledFuture();
		if (scheduledExecutorService != null)
		{
			scheduledExecutorService.shutdown();
			scheduledExecutorService = null;
		}
	}

	@Override
	public void onCreate(@Nullable Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		mFragmentRecommendList.add(FragmentImage.newInstance(R.drawable.ic_test_0, "http://lrcall.com"));
		mFragmentRecommendList.add(FragmentImage.newInstance(R.drawable.ic_test_1, "http://lrcall.com"));
		mFragmentRecommendList.add(FragmentImage.newInstance(R.drawable.ic_test_2, "http://lrcall.com"));
		mFragmentRecommendList.add(FragmentImage.newInstance(R.drawable.ic_test_3, "http://lrcall.com"));
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		// Inflate the layout for this fragment
		View rootView = inflater.inflate(R.layout.fragment_more, container, false);
		viewInit(rootView);
		initData();
		return rootView;
	}

	protected void viewInit(View rootView)
	{
		xListView = (XListView) rootView.findViewById(R.id.xlist);
		headView = LayoutInflater.from(this.getActivity()).inflate(R.layout.fragment_more_head, null);
		xListView.setPullRefreshEnable(false);
		xListView.setPullLoadEnable(false);
		xListView.addHeaderView(headView);
		xListView.setAdapter(null);
		xListView.setXListViewListener(this);
		gvFuncs = (GridView) headView.findViewById(R.id.gv_func);
		gvFuncs.setNumColumns(4);
		viewPager = (ViewPager) rootView.findViewById(R.id.viewpager);
		//设置图片的长宽，这里便于制作图片
		ViewGroup.LayoutParams layoutParams = viewPager.getLayoutParams();
		layoutParams.width = DisplayTools.getWindowWidth(this.getContext());
		layoutParams.height = DisplayTools.getWindowWidth(this.getContext()) * 1 / 2;
		viewPager.setLayoutParams(layoutParams);
		SmartTabLayout viewPagerTab = (SmartTabLayout) rootView.findViewById(R.id.viewpagertab);
		SectionsPagerAdapter adapter = new SectionsPagerAdapter(getChildFragmentManager(), mFragmentRecommendList);
		viewPager.setAdapter(adapter);
		viewPagerTab.setViewPager(viewPager);
		viewPager.setOnTouchListener(new View.OnTouchListener()
		{
			@Override
			public boolean onTouch(View v, MotionEvent event)
			{
				if (event.getAction() == MotionEvent.ACTION_DOWN)
				{
					cancelScheduledFuture();
				}
				else if (event.getAction() == MotionEvent.ACTION_MOVE)
				{
					cancelScheduledFuture();
				}
				else if (event.getAction() == MotionEvent.ACTION_UP)
				{
					updateView();
				}
				else
				{
					updateView();
				}
				return false;
			}
		});
		//		viewPagerTab.setOnScrollChangeListener(new SmartTabLayout.OnScrollChangeListener()
		//		{
		//			@Override
		//			public void onScrollChanged(int scrollX, int oldScrollX)
		//			{
		//				if (scrollX - oldScrollX > 10)
		//				{
		//					int index = viewPager.getCurrentItem();
		//					if (index == mFragmentRecommendList.size() - 1)
		//					{
		//						index = 0;
		//						viewPager.setCurrentItem(index);
		//					}
		//				}
		//			}
		//		});
		super.viewInit(rootView);
	}

	protected void initData()
	{
		List<FuncInfo> funcInfoList = new ArrayList<>();
		funcInfoList.add(new FuncInfo(R.drawable.ic_tab_dialer_normal, "快递电话"));
		funcInfoList.add(new FuncInfo(R.drawable.ic_tab_contacts_normal, "银行电话"));
		funcInfoList.add(new FuncInfo(R.drawable.ic_tab_money_normal, "快递电话"));
		funcInfoList.add(new FuncInfo(R.drawable.ic_tab_more_normal, "快递电话"));
		funcInfoList.add(new FuncInfo(R.drawable.ic_tab_dialer_normal, "快递电话"));
		funcInfoList.add(new FuncInfo(R.drawable.ic_tab_contacts_normal, "银行电话"));
		funcInfoList.add(new FuncInfo(R.drawable.ic_tab_money_normal, "快递电话"));
		funcInfoList.add(new FuncInfo(R.drawable.ic_tab_more_normal, "快递电话"));
		funcInfoList.add(new FuncInfo(R.drawable.ic_tab_dialer_normal, "快递电话"));
		funcInfoList.add(new FuncInfo(R.drawable.ic_tab_contacts_normal, "银行电话"));
		funcInfoList.add(new FuncInfo(R.drawable.ic_tab_money_normal, "快递电话"));
		funcInfoList.add(new FuncInfo(R.drawable.ic_tab_more_normal, "快递电话"));
		funcInfoList.add(new FuncInfo(R.drawable.ic_tab_dialer_normal, "快递电话"));
		funcInfoList.add(new FuncInfo(R.drawable.ic_tab_contacts_normal, "银行电话"));
		funcInfoList.add(new FuncInfo(R.drawable.ic_tab_money_normal, "快递电话"));
		funcInfoList.add(new FuncInfo(R.drawable.ic_tab_more_normal, "快递电话"));
		funcInfoList.add(new FuncInfo(R.drawable.ic_tab_dialer_normal, "快递电话"));
		funcInfoList.add(new FuncInfo(R.drawable.ic_tab_contacts_normal, "银行电话"));
		gvFuncs.setAdapter(new FuncsAdapter(this.getContext(), funcInfoList, this));
	}

	@Override
	public void fragmentShow()
	{
		super.fragmentShow();
		LogcatTools.debug(TAG, "ScheduledFuture,fragmentShow:" + StringTools.getCurrentTime());
		updateView();
	}

	@Override
	public void fragmentHide()
	{
		super.fragmentHide();
		LogcatTools.debug(TAG, "ScheduledFuture,fragmentHide:" + StringTools.getCurrentTime());
		cancelScheduledFuture();
	}

	@Override
	public void onDestroyView()
	{
		LogcatTools.debug(TAG, "ScheduledFuture,onDestroyView:" + StringTools.getCurrentTime());
		stopScheduledFuture();
		super.onDestroyView();
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater)
	{
		super.onCreateOptionsMenu(menu, inflater);
		getActivity().getMenuInflater().inflate(R.menu.menu_fragment_more, menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onRefresh()
	{
		xListView.stopRefresh();
	}

	@Override
	public void onLoadMore()
	{
		xListView.stopLoadMore();
	}

	@Override
	public void onFuncClicked(FuncInfo funcInfo)
	{
		Toast.makeText(this.getContext(), "点击" + funcInfo.getLabel(), Toast.LENGTH_LONG).show();
	}

	private Handler mHandler = new Handler()
	{
		@Override
		public void handleMessage(Message msg)
		{
			super.handleMessage(msg);
			switch (msg.what)
			{
				case SCROLL_VIEW_PAGE:
				{
					int index = viewPager.getCurrentItem();
					if (index < mFragmentRecommendList.size() - 1)
					{
						index++;
					}
					else
					{
						index = 0;
					}
					viewPager.setCurrentItem(index);
					break;
				}
			}
		}
	};

	public class SectionsPagerAdapter extends FragmentPagerAdapter
	{
		private List<Fragment> fragmentList;

		public SectionsPagerAdapter(FragmentManager fm, List<Fragment> fragmentList)
		{
			super(fm);
			this.fragmentList = fragmentList;
		}

		@Override
		public Fragment getItem(int position)
		{
			if (position < fragmentList.size())
			{
				return fragmentList.get(position);
			}
			else
			{
				return null;
			}
		}

		@Override
		public int getCount()
		{
			return fragmentList.size();
		}

		@Override
		public CharSequence getPageTitle(int position)
		{
			return "";
		}
	}
}

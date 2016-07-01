package com.lrcall.ui;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.lrcall.appcall.R;

import me.imid.swipebacklayout.lib.app.SwipeBackActivity;

/**
 * 自定义ActionBar基类
 * Created by libit on 15/8/30.
 */
public class MyBaseActivity extends SwipeBackActivity
{
	protected Toolbar mToolbar;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		//		setOverflowShowingAlways();
	}

	protected void viewInit()
	{
		mToolbar = (Toolbar) findViewById(R.id.toolbar_title);
		mToolbar.setTitle(getTitle().toString());// 标题的文字需在setSupportActionBar之前，不然会无效
		// mToolbar.setSubtitle("副标题");
		setSupportActionBar(mToolbar);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		//		setBackButton();
	}

	//后退按钮
	protected void setBackButton()
	{
//		mToolbar.setNavigationIcon(R.drawable.back);
		mToolbar.setNavigationOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				finish();
			}
		});
	}
}

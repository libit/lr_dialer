/*
 * Libit保留所有版权，如有疑问联系QQ：308062035
 * Copyright (c) 2016.
 */
package com.lrcall.ui;

import android.content.Context;
import android.support.v7.widget.SearchView;
import android.util.AttributeSet;
import android.widget.Toast;

/**
 * Created by libit on 16/5/8.
 */
public class SearchContentView extends SearchView implements SearchView.OnQueryTextListener
{
	public SearchContentView(Context context)
	{
		super(context);
		setOnQueryTextListener(this);
	}

	public SearchContentView(Context context, AttributeSet attrs)
	{
		super(context, attrs);
		setOnQueryTextListener(this);
	}

	public SearchContentView(Context context, AttributeSet attrs, int defStyleAttr)
	{
		super(context, attrs, defStyleAttr);
		setOnQueryTextListener(this);
	}

	/**
	 * Called when the user submits the query. This could be due to a key press on the
	 * keyboard or due to pressing a submit button.
	 * The listener can override the standard behavior by returning true
	 * to indicate that it has handled the submit request. Otherwise return false to
	 * let the SearchView handle the submission by launching any associated intent.
	 *
	 * @param query the query text that is to be submitted
	 * @return true if the query has been handled by the listener, false to let the
	 * SearchView perform the default action.
	 */
	@Override
	public boolean onQueryTextSubmit(String query)
	{
		Toast.makeText(this.getContext(), "onQueryTextSubmit", Toast.LENGTH_LONG).show();
		return true;
	}

	/**
	 * Called when the query text is changed by the user.
	 *
	 * @param newText the new content of the query text field.
	 * @return false if the SearchView should perform the default action of showing any
	 * suggestions if available, true if the action was handled by the listener.
	 */
	@Override
	public boolean onQueryTextChange(String newText)
	{
		Toast.makeText(this.getContext(), "onQueryTextChange", Toast.LENGTH_LONG).show();
		return true;
	}
}

/*
 * Libit保留所有版权，如有疑问联系QQ：308062035
 * Copyright (c) 2016.
 */
package com.lrcall.ui.customer;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

import com.lrcall.utils.DisplayTools;
import com.lrcall.utils.LogcatTools;

/**
 * Created by libit on 16/5/9.
 */
public class DraftImageView extends ImageView
{
	private Context mContext;

	public DraftImageView(Context context)
	{
		super(context);
		mContext = context;
		//		setOnTouchListener();
	}

	public DraftImageView(Context context, AttributeSet attrs)
	{
		super(context, attrs);
		mContext = context;
		//		setOnTouchListener();
	}

	public DraftImageView(Context context, AttributeSet attrs, int defStyleAttr)
	{
		super(context, attrs, defStyleAttr);
		mContext = context;
		//		setOnTouchListener();
	}

	public void setOnTouchListener(final int parentEndY)
	{
		final int viewWidth = DisplayTools.getWindowWidth(mContext);
		final int viewHeight = DisplayTools.getWindowHeight(mContext) - parentEndY;
		this.setOnTouchListener(new OnTouchListener()
		{
			private float lastX;
			private float lastY;
			private boolean bDraft = false;

			@Override
			public boolean onTouch(View v, MotionEvent event)
			{
				int action = event.getAction();
				if (action == MotionEvent.ACTION_DOWN)
				{
					lastX = event.getRawX();
					lastY = event.getRawY();
					int left = getLeft();
					int top = getTop();
					int right = getRight();
					int bottom = getBottom();
					if (lastX <= right && lastX >= left)// && lastY >= top && lastY <= bottom)
					{
						bDraft = true;
					}
					LogcatTools.debug("DraftImageView", "lastX:" + lastX + ",lastY:" + lastY + ",left:" + left + ",right:" + right + ",top:" + top + ",bottom:" + bottom);
				}
				else if (action == MotionEvent.ACTION_MOVE)
				{
					if (bDraft)
					{
						float dx = event.getRawX() - lastX;
						float dy = event.getRawY() - lastY;
						lastX = event.getRawX();
						lastY = event.getRawY();
						if (dx != 0 && dy != 0)
						{
							int left = (int) (getLeft() + dx);
							int top = (int) (getTop() + dy);
							int right = (int) (getRight() + dx);
							int bottom = (int) (getBottom() + dy);
							if (left < 0)
							{
								left = 0;
								right = left + getWidth();
							}
							else
							{
								if (right > viewWidth)
								{
									right = viewWidth;
									left = right - v.getWidth();
								}
							}
							//							if (top < 0)
							//							{
							//								top = 0;
							//								bottom = top + v.getHeight();
							//							}
							//							else
							//							{
							//								if (bottom > viewHeight)
							//								{
							//									bottom = viewHeight;
							//									top = bottom - v.getHeight();
							//								}
							//							}
							layout(left, top, right, bottom);
							LogcatTools.debug("DraftImageView", "lastX:" + lastX + ",lastY:" + lastY + ",left:" + left + ",right:" + right + ",top:" + top + ",bottom:" + bottom);
						}
					}
				}
				else if (action == MotionEvent.ACTION_UP)
				{
					bDraft = false;
				}
				return onTouchEvent(event);
			}
		});
	}
}

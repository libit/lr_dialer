package com.lrcall.ui.customer;

import android.content.Context;
import android.graphics.Color;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.lrcall.appcall.R;
import com.lrcall.utils.LogcatTools;
import com.lrcall.utils.apptools.AppFactory;

import java.util.HashMap;

public class QuickAlphabeticBar extends ImageButton
{
	private View rootView;
	private TextView mDialogText;
	private Handler mHandler = new Handler();

	private ListView mList;
	private float mHight;
	private final String[] letters = new String[]{"#", "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z"};
	private final int[] ids = {R.id.v_pound, R.id.v_a, R.id.v_b, R.id.v_c, R.id.v_d, R.id.v_e, R.id.v_f, R.id.v_g, R.id.v_h, R.id.v_i, R.id.v_j, R.id.v_k, R.id.v_l, R.id.v_m, R.id.v_n, R.id.v_o, R.id.v_p, R.id.v_q, R.id.v_r, R.id.v_s, R.id.v_t, R.id.v_u, R.id.v_v, R.id.v_w, R.id.v_x, R.id.v_y, R.id.v_z};
	private int count;
	private HashMap<String, Integer> alphaIndexer;

	public QuickAlphabeticBar(Context context)
	{
		super(context);
	}

	public QuickAlphabeticBar(Context context, AttributeSet attrs)
	{
		super(context, attrs);
	}

	public QuickAlphabeticBar(Context context, AttributeSet attrs, int defStyle)
	{
		super(context, attrs, defStyle);
	}

	@Override
	public void setPressed(boolean pressed)
	{
		// If the parent is pressed, do not set to pressed.
		if (pressed && ((View) getParent()).isPressed())
		{
			return;
		}
		super.setPressed(pressed);
	}

	// 初始化
	public void init(View v)
	{
		rootView = v;
		mDialogText = (TextView) rootView.findViewById(R.id.fast_position);
		mDialogText.setVisibility(View.INVISIBLE);
		count = letters.length;
	}

	public void setListView(ListView mList)
	{
		this.mList = mList;
	}

	public void setAlphaIndexer(HashMap<String, Integer> alphaIndexer)
	{
		this.alphaIndexer = alphaIndexer;
	}

	public void setDialogTextVisibile(boolean visibile)
	{
		if (mDialogText == null)
		{
			return;
		}
		if (visibile)
		{
			mDialogText.setVisibility(View.VISIBLE);
		}
		else
		{
			mDialogText.setVisibility(View.INVISIBLE);
		}
	}

	@Override
	public boolean onTouchEvent(MotionEvent event)
	{
		int action = event.getAction();
		float y = event.getY();
		mHight = this.getHeight();
		LogcatTools.debug("onTouch", "QuickAlphabeticBar->act:" + action + ",y:" + y);
		int selectIndex = (int) (y / (mHight / count));
		if (selectIndex < 0)
		{
			selectIndex = 0;
		}
		for (int i = 0; i < ids.length; i++)
		{
			TextView tv = (TextView) rootView.findViewById(ids[i]);
			if (tv != null)
			{
				tv.setTextColor(0xff999999);
			}
		}
		onTouch(action, selectIndex);
		return super.onTouchEvent(event);
	}

	public void onTansferTouch(MotionEvent event)
	{
		int act = event.getAction();
		float y = mHight - event.getY();
		onTouch(act, act);
	}

	public void onTouch(int action, int selectIndex)
	{
		if (selectIndex < count)
		{
			String key = letters[selectIndex];
			if (alphaIndexer.containsKey(key))
			{
				int pos = alphaIndexer.get(key);
				if (mList.getHeaderViewsCount() > 0)
				{
					mList.setSelectionFromTop(pos + mList.getHeaderViewsCount(), 0);
				}
				else
				{
					mList.setSelectionFromTop(pos, 0);
				}
				mDialogText.setText(letters[selectIndex]);
				TextView tv = (TextView) rootView.findViewById(ids[selectIndex]);
				if (tv != null)
				{
					tv.setTextColor(TV_SELECTED);
				}
			}
		}
		if (action == MotionEvent.ACTION_DOWN)
		{
			mHandler.post(new Runnable()
			{
				@Override
				public void run()
				{
					if (mDialogText != null)
					{
						mDialogText.setVisibility(VISIBLE);
					}
					View view = rootView.findViewById(R.id.layout_letters);
					if (AppFactory.isCompatible(23))
					{
						view.setBackgroundColor(getContext().getResources().getColor(BG_SELECTED, null));
					}
					else
					{
						view.setBackgroundColor(getContext().getResources().getColor(BG_SELECTED));
					}
				}
			});
		}
		else if (action == MotionEvent.ACTION_UP)
		{
			LogcatTools.debug("onTouch", "ACTION_UP");
			mHandler.post(new Runnable()
			{
				@Override
				public void run()
				{
					if (mDialogText != null)
					{
						mDialogText.setVisibility(INVISIBLE);
					}
					View view = rootView.findViewById(R.id.layout_letters);
					if (AppFactory.isCompatible(23))
					{
						view.setBackgroundColor(getContext().getResources().getColor(BG_UNSELECTED, null));
					}
					else
					{
						view.setBackgroundColor(getContext().getResources().getColor(BG_UNSELECTED));
					}
					for (int i = 0; i < ids.length; i++)
					{
						TextView tv = (TextView) rootView.findViewById(ids[i]);
						if (tv != null)
						{
							tv.setTextColor(TV_UNSELECTED);
						}
					}
				}
			});
		}
	}

	private final int TV_SELECTED = Color.RED;
	private final int TV_UNSELECTED = Color.GRAY;
	private final int BG_SELECTED = R.color.line_height;
	private final int BG_UNSELECTED = android.R.color.transparent;
}

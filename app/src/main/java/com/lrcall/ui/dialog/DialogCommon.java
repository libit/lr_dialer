/*
 * Libit保留所有版权，如有疑问联系QQ：308062035
 * Copyright (c) 2016.
 */
package com.lrcall.ui.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.lrcall.appcall.R;
import com.lrcall.utils.StringTools;

public class DialogCommon extends Dialog implements OnClickListener
{
	String title, message;
	private TextView tvTitle, tvMessage;
	private Button btnOk, btnCancel;
	private LinearLayout linearlayoutButtons;
	private ProgressBar pbWaiting;
	private LibitDialogListener listener;
	private boolean showButtons, showWaiting, showCancelButton;

	public DialogCommon(Context context, LibitDialogListener l)
	{
		// super(context);
		super(context, R.style.MyDialog);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setCancelable(false);
		listener = l;
		showButtons = true;
		showWaiting = false;
		showCancelButton = true;
	}

	public DialogCommon(Context context, LibitDialogListener l, String title, String message)
	{
		// super(context);
		super(context, R.style.MyDialog);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setCancelable(false);
		listener = l;
		this.title = title;
		this.message = message;
		showButtons = true;
		showWaiting = false;
		showCancelButton = true;
	}

	public DialogCommon(Context context, LibitDialogListener l, String title, String message, boolean bButtons, boolean bWaiting)
	{
		// super(context);
		super(context, R.style.MyDialog);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setCancelable(false);
		listener = l;
		this.title = title;
		this.message = message;
		showButtons = bButtons;
		showWaiting = bWaiting;
		showCancelButton = true;
	}

	public DialogCommon(Context context, LibitDialogListener l, String title, String message, boolean bButtons, boolean bWaiting, boolean bCancelButton)
	{
		// super(context);
		super(context, R.style.MyDialog);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setCancelable(false);
		listener = l;
		this.title = title;
		this.message = message;
		showButtons = bButtons;
		showWaiting = bWaiting;
		showCancelButton = bCancelButton;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dialog_common);
		initView();
	}

	private void initView()
	{
		View root = findViewById(R.id.dialog_root_view);
		//        root.setLayoutParams(new ViewGroup.LayoutParams(DisplayTools.dip2px(this.getContext(), DisplayTools.getWindowWidth(this.getContext())), ViewGroup.LayoutParams.WRAP_CONTENT));
		//        int distance = DisplayTools.dip2px(this.getContext(), DisplayTools.getWindowWidth(this.getContext()) / 3);
		//        if (AppFactory.isCompatible(11))
		//        {
		//            root.setLeft(distance);
		//            root.setRight(distance);
		//        }
		tvTitle = (TextView) findViewById(R.id.dialog_tv_title);
		tvMessage = (TextView) findViewById(R.id.dialog_tv_message);
		if (!StringTools.isNull(title))
		{
			tvTitle.setText(title);
		}
		if (!StringTools.isNull(message))
		{
			tvMessage.setText(message);
		}
		btnOk = (Button) findViewById(R.id.dialog_btn_ok);
		btnOk.setOnClickListener(this);
		btnCancel = (Button) findViewById(R.id.dialog_btn_cancel);
		btnCancel.setOnClickListener(this);
		linearlayoutButtons = (LinearLayout) findViewById(R.id.dialog_layout_buttons);
		if (showButtons)
		{
			linearlayoutButtons.setVisibility(View.VISIBLE);
		}
		else
		{
			linearlayoutButtons.setVisibility(View.GONE);
		}
		pbWaiting = (ProgressBar) findViewById(R.id.dialog_progressbar);
		if (showWaiting)
		{
			pbWaiting.setVisibility(View.VISIBLE);
		}
		else
		{
			pbWaiting.setVisibility(View.GONE);
		}
		if (showCancelButton)
		{
			btnCancel.setVisibility(View.VISIBLE);
		}
		else
		{
			btnCancel.setVisibility(View.GONE);
		}
	}

	public void setDialogTitle(String title)
	{
		if (tvTitle != null)
			tvTitle.setText(title);
	}

	public void setDialogTitle(int resId)
	{
		if (tvTitle != null)
			tvTitle.setText(this.getContext().getString(resId));
	}

	public void setDialogMessage(String message)
	{
		if (tvMessage != null)
			tvMessage.setText(message);
	}

	public void setDialogMessage(int resId)
	{
		if (tvMessage != null)
			tvMessage.setText(this.getContext().getString(resId));
	}

	public void setButtonsVisible(boolean visible)
	{
		if (visible)
		{
			linearlayoutButtons.setVisibility(View.VISIBLE);
		}
		else
		{
			linearlayoutButtons.setVisibility(View.GONE);
		}
	}

	public void setPBWaiting(boolean visible)
	{
		if (visible)
		{
			pbWaiting.setVisibility(View.VISIBLE);
		}
		else
		{
			pbWaiting.setVisibility(View.GONE);
		}
	}

	public void setOKString(String ok)
	{
		if (btnOk != null)
			btnOk.setText(ok);
	}

	public void setOKString(int okId)
	{
		if (btnOk != null)
			btnOk.setText(okId);
	}

	public void setCancelString(String cancel)
	{
		if (btnCancel != null)
			btnCancel.setText(cancel);
	}

	public void setCancelString(int cancelId)
	{
		if (btnCancel != null)
			btnCancel.setText(cancelId);
	}
	//    public void showTips()
	//    {
	//        findViewById(R.id.dialog_textview_tips).setVisibility(View.VISIBLE);
	//    }

	@Override
	public void onClick(View v)
	{
		switch (v.getId())
		{
			case R.id.dialog_btn_ok:
			{
				if (listener != null)
				{
					listener.onOkClick();
				}
				dismiss();
				break;
			}
			case R.id.dialog_btn_cancel:
			{
				if (listener != null)
				{
					listener.onCancelClick();
				}
				dismiss();
				break;
			}
		}
	}

	public interface LibitDialogListener
	{
		void onOkClick();

		void onCancelClick();
	}
}

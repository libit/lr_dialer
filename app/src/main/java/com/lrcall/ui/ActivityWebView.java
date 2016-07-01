/*
 * Libit保留所有版权，如有疑问联系QQ：308062035
 * Copyright (c) 2016.
 */
package com.lrcall.ui;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.webkit.DownloadListener;
import android.webkit.JsResult;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.external.xlistview.XListView;
import com.lrcall.appcall.R;
import com.lrcall.ui.dialog.DialogFileList;
import com.lrcall.ui.customer.ToastView;
import com.lrcall.utils.ConstValues;
import com.lrcall.utils.StringTools;
import com.lrcall.utils.apptools.AppFactory;

import java.io.File;

public class ActivityWebView extends MyBaseActivity implements XListView.IXListViewListener
{
	private static final String TAG = ActivityWebView.class.getSimpleName();
	public final static int FILECHOOSER_RESULTCODE = 1;
	public final static int FILECHOOSER_RESULTCODE_FOR_ANDROID_5 = 2;
	private XListView xListView;
	private View headView;
	private FrameLayout layoutWeb;
	private WebView webView;
	private View videoView;
	private ProgressBar progressBar;
	private String webTitle, url;
	private ValueCallback<Uri> mUploadMessage;
	public ValueCallback<Uri[]> mUploadMessageForAndroid5;
	private WebChromeClient.CustomViewCallback customViewCallback;

	public static void startWebActivity(Context c, String title, String url)
	{
		Intent intent = new Intent(c, ActivityWebView.class);
		Bundle bundle = new Bundle();
		bundle.putString(ConstValues.DATA_WEB_TITLE, title);
		bundle.putString(ConstValues.DATA_WEB_URL, url);
		intent.putExtras(bundle);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		c.startActivity(intent);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_web_view);
		Bundle bundle = getIntent().getExtras();
		if (bundle != null)
		{
			webTitle = bundle.getString(ConstValues.DATA_WEB_TITLE);
			setTitle(webTitle);
			url = bundle.getString(ConstValues.DATA_WEB_URL);
		}
		viewInit();
		loadUrl();
	}

	private void loadUrl()
	{
		if (!StringTools.isNull(url))
		{
			webView.loadUrl(url);
		}
	}

	public void viewInit()
	{
		super.viewInit();
		setBackButton();
		layoutWeb = (FrameLayout) findViewById(R.id.layout_web);
		xListView = (XListView) findViewById(R.id.xlist);
		headView = LayoutInflater.from(this).inflate(R.layout.activity_web_view_head, null);
		xListView.setPullRefreshEnable(true);
		xListView.setPullLoadEnable(false);
		xListView.addHeaderView(headView);
		xListView.setAdapter(null);
		xListView.setXListViewListener(this);
		progressBar = (ProgressBar) findViewById(R.id.progress_bar);
		webView = (WebView) findViewById(R.id.webview);
		webView.getSettings().setJavaScriptEnabled(true);
		webView.clearCache(true);
		webView.clearHistory();
		webView.setWebChromeClient(new WebChromeClient()
		{
			@Override
			public void onReceivedTitle(WebView view, String title)
			{
				mToolbar.setTitle(title);
			}

			@Override
			public boolean onJsConfirm(WebView view, String url, String message, JsResult result)
			{
				super.onJsConfirm(view, url, message, result);
				// new LibitDialog(ActivityWebView.this, new LibitDialogListener()
				// {
				// @Override
				// public void onOkClick()
				// {
				//
				// }
				//
				// @Override
				// public void onCancelClick()
				// {
				//
				// }
				// }, "提示", message, true, false, true).show();
				return false;
			}

			@Override
			public void onShowCustomView(View view, CustomViewCallback callback)
			{
				videoView = view;
				customViewCallback = callback;
				// 设置webView隐藏
				mToolbar.setVisibility(View.GONE);
				webView.setVisibility(View.GONE);
				// 声明video，把之后的视频放到这里面去
				// 将video放到当前视图中
				layoutWeb.addView(view);
				// 竖屏显示
				setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_USER);
				// 设置全屏
				setFullScreen();
			}

			@Override
			public void onHideCustomView()
			{
				// 设置WebView可见
				webView.setVisibility(View.VISIBLE);
				mToolbar.setVisibility(View.VISIBLE);
				if (videoView != null)
				{
					layoutWeb.removeView(videoView);
					videoView = null;
				}
				if (customViewCallback != null)
				{
					// 隐藏掉
					customViewCallback.onCustomViewHidden();
				}
				// 用户当前的首选方向
				setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_USER);
				// 退出全屏
				quitFullScreen();
			}

			@Override
			public void onProgressChanged(WebView view, int newProgress)
			{
				super.onProgressChanged(view, newProgress);
				progressBar.setProgress(newProgress);
			}

			// API=21(5.0)
			@Override
			public boolean onShowFileChooser(WebView webView, final ValueCallback<Uri[]> filePathCallback, FileChooserParams fileChooserParams)
			{
				// 自定义的选择文件对话框
				if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED))
				{
					new DialogFileList(ActivityWebView.this, new File(Environment.getExternalStorageDirectory().getAbsolutePath()), null, new DialogFileList.IDialogChooseFile()
					{
						@Override
						public void onFileSelected(File file)
						{
							if (file != null && !file.isDirectory())
							{
								Toast.makeText(ActivityWebView.this, "选择文件：" + file.getAbsolutePath(), Toast.LENGTH_LONG).show();
								filePathCallback.onReceiveValue(new Uri[]{Uri.parse("file:" + file.getAbsolutePath())});
							}
						}
					}).show();
				}
				else
				{
					ToastView.showCenterToast(ActivityWebView.this, "找不到存储！");
				}
				//系统选择文件对话框
				//				openFileChooserImplForAndroid5(filePathCallback);
				return true;
			}

			// For Android 3.0+
			public void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType)
			{
				openFileChooserImpl(uploadMsg);
			}

			// For Android < 3.0
			public void openFileChooser(ValueCallback<Uri> uploadMsg)
			{
				openFileChooser(uploadMsg, "");
			}

			// For Android  > 4.1.1
			public void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType, String capture)
			{
				openFileChooser(uploadMsg, acceptType);
			}
		});
		webView.setWebViewClient(new WebViewClient()
		{
			@Override
			public void onPageStarted(WebView view, String url, Bitmap favicon)
			{
				super.onPageStarted(view, url, favicon);
				//				findViewById(R.id.layout_web_waiting).setVisibility(View.VISIBLE);
				progressBar.setVisibility(View.VISIBLE);
			}

			@Override
			public void onPageFinished(WebView view, String url)
			{
				super.onPageFinished(view, url);
				//				findViewById(R.id.layout_web_waiting).setVisibility(View.GONE);
				progressBar.setVisibility(View.GONE);
				//				xListView.setAdapter(null);
			}
		});
		webView.setDownloadListener(new DownloadListener()
		{
			@Override
			public void onDownloadStart(String url, String userAgent, String contentDisposition, String mimetype, long contentLength)
			{
				openUrl(url);
			}
		});
		// 禁止浏览器外部攻击
		if (AppFactory.isCompatible(11))
		{
			webView.removeJavascriptInterface("searchBoxJavaBredge_");
		}
	}

	/**
	 * 设置全屏
	 */
	private void setFullScreen()
	{
		// 设置全屏的相关属性，获取当前的屏幕状态，然后设置全屏
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		// 全屏下的状态码：1098974464
		// 窗口下的状态吗：1098973440
	}

	/**
	 * 退出全屏
	 */
	private void quitFullScreen()
	{
		// 声明当前屏幕状态的参数并获取
		final WindowManager.LayoutParams attrs = getWindow().getAttributes();
		attrs.flags &= (~WindowManager.LayoutParams.FLAG_FULLSCREEN);
		getWindow().setAttributes(attrs);
		getWindow().clearFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
	}

	// 5.0以下选择图片
	private void openFileChooserImpl(ValueCallback<Uri> uploadMsg)
	{
		mUploadMessage = uploadMsg;
		Intent i = new Intent(Intent.ACTION_GET_CONTENT);
		i.addCategory(Intent.CATEGORY_OPENABLE);
		i.setType("image/*");
		startActivityForResult(Intent.createChooser(i, "选择图片"), FILECHOOSER_RESULTCODE);
	}

	//5.0及以上选择图片
	private void openFileChooserImplForAndroid5(ValueCallback<Uri[]> uploadMsg)
	{
		mUploadMessageForAndroid5 = uploadMsg;
		Intent contentSelectionIntent = new Intent(Intent.ACTION_GET_CONTENT);
		contentSelectionIntent.addCategory(Intent.CATEGORY_OPENABLE);
		contentSelectionIntent.setType("image/*");
		Intent chooserIntent = new Intent(Intent.ACTION_CHOOSER);
		chooserIntent.putExtra(Intent.EXTRA_INTENT, contentSelectionIntent);
		chooserIntent.putExtra(Intent.EXTRA_TITLE, "选择图片");
		startActivityForResult(chooserIntent, FILECHOOSER_RESULTCODE_FOR_ANDROID_5);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent intent)
	{
		if (requestCode == FILECHOOSER_RESULTCODE)
		{
			if (null == mUploadMessage)
				return;
			Uri result = intent == null || resultCode != RESULT_OK ? null : intent.getData();
			mUploadMessage.onReceiveValue(result);
			mUploadMessage = null;
		}
		else if (requestCode == FILECHOOSER_RESULTCODE_FOR_ANDROID_5)
		{
			if (null == mUploadMessageForAndroid5)
				return;
			Uri result = (intent == null || resultCode != RESULT_OK) ? null : intent.getData();
			if (result != null)
			{
				mUploadMessageForAndroid5.onReceiveValue(new Uri[]{result});
			}
			else
			{
				mUploadMessageForAndroid5.onReceiveValue(new Uri[]{});
			}
			mUploadMessageForAndroid5 = null;
		}
	}

	@Override
	public void onBackPressed()
	{
		if (videoView != null)
		{
			quitFullScreen();
		}
		else
		{
			super.onBackPressed();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.menu_activity_webview, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent context in AndroidManifest.xml.
		int id = item.getItemId();
		//noinspection SimplifiableIfStatement
		if (id == R.id.action_refresh)
		{
			webView.reload();
			return true;
		}
		else if (id == R.id.action_back)
		{
			if (webView.canGoBack())
			{
				webView.goBack();
			}
			return true;
		}
		else if (id == R.id.action_forward)
		{
			if (webView.canGoForward())
			{
				webView.goForward();
			}
			return true;
		}
		else if (id == R.id.action_home)
		{
			loadUrl();
			return true;
		}
		else if (id == R.id.action_close)
		{
			finish();
			return true;
		}
		else if (id == R.id.action_open_in_browser)
		{
			openUrl(webView.getUrl());
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onRefresh()
	{
		webView.reload();
		xListView.stopRefresh();
	}

	@Override
	public void onLoadMore()
	{
	}

	private void openUrl(String url)
	{
		try
		{
			Intent intent = new Intent().setAction(Intent.ACTION_VIEW);
			intent.setData(Uri.parse(url));
			startActivity(intent);
		}
		catch (Exception e)
		{
			String msg = e.getMessage();
			if (msg.indexOf("No Activity found") > -1)
			{
				msg = "没有找到浏览器！";
			}
			ToastView.showCenterToast(this, msg);
		}
	}
}

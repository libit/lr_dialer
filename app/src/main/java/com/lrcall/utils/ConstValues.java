package com.lrcall.utils;

/**
 * Created by libit on 15/8/31.
 */
public class ConstValues
{
	// App类型
	public static final int TYPE_ALL = 0;
	public static final int TYPE_SYSTEM = 1;
	public static final int TYPE_USER = 2;
	// App状态
	public static final int STATUS_ALL = 0;
	public static final int STATUS_ENABLED = 1;
	public static final int STATUS_DISABLED = 2;
	// App隐藏属性
	public static final int STATUS_HIDE = 1;
	public static final int STATUS_SHOW = 2;
	// App属性--是否存在
	public static final int STATUS_EXIST = 1;
	public static final int STATUS_NOT_EXIST = 2;
	public static final String DATA_TITLE = "dialog.layout_title";
	public static final String DATA_CONTENT = "dialog.content";
	public final static String DATA_WEB_TITLE = "data.web.layout_title";
	public final static String DATA_WEB_URL = "data.web.url";
	public final static String DATA_PACKAGE_NAME = "data.package.name";
	public final static String DATA_ACTION = "data.action";
	// 登录结果
	public final static int REQUEST_LOGIN = 1000;//登录代码
	public final static int REQUEST_LOGIN_USER = 1001;//登录代码，跳转到用户中心
	public final static int REQUEST_LOGIN_ADVICE = 1002;//登录代码，跳转到意见反馈
	public final static int RESULT_LOGIN_SUCCESS = 2000;//登录成功代码
	public final static int RESULT_LOGIN_ERROR = 2001;//登录失败代码
	// 注册结果
	public final static int REQUEST_REGISTER = 1100;//注册代码
	public final static int RESULT_REGISTER_SUCCESS = 2002;//注册成功代码
	public final static int RESULT_REGISTER_ERROR = 2003;//注册失败代码
	public static final String DATA_SHOW_SERVER = "data.show_server";//是否显示云端
	//联系人
	public static final String DATA_CONTACT_ID = "contact_id";
}

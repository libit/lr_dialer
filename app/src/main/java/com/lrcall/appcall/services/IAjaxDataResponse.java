/*
 * Libit保留所有版权，如有疑问联系QQ：308062035
 * Copyright (c) 2016.
 */
package com.lrcall.appcall.services;

import com.androidquery.callback.AjaxStatus;

/**
 * Created by libit on 16/4/6.
 * ajax数据回调接口
 */
public interface IAjaxDataResponse
{
	/**
	 * ajax回调函数
	 *
	 * @param url
	 * @param result 返回结果
	 * @param status 状态
	 * @return
	 */
	boolean onAjaxDataResponse(String url, String result, AjaxStatus status);
}

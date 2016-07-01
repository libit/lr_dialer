/*
 * Libit保留所有版权，如有疑问联系QQ：308062035
 * Copyright (c) 2015.
 */
package com.lrcall.models;

/**
 * Created by libit on 15/9/30.
 */
public class MemoryInfo
{
	private long totalMemory;
	private long unusedMemory;

	public long getTotalMemory()
	{
		return totalMemory;
	}

	public void setTotalMemory(long totalMemory)
	{
		this.totalMemory = totalMemory;
	}

	public long getUnusedMemory()
	{
		return unusedMemory;
	}

	public void setUnusedMemory(long unusedMemory)
	{
		this.unusedMemory = unusedMemory;
	}
}

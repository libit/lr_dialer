/*
 * Libit保留所有版权，如有疑问联系QQ：308062035
 * Copyright (c) 2016.
 */
package com.lrcall.utils;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * Created by libit on 16/4/6.
 */
public class MyExecutorService
{
	//	private BlockingQueue<Runnable> queue = new ArrayBlockingQueue<>(8);
	private static MyExecutorService instance = null;
	private ExecutorService executorService = Executors.newFixedThreadPool(1);

	private MyExecutorService()
	{
	}

	synchronized public static MyExecutorService getInstance()
	{
		if (instance == null)
		{
			instance = new MyExecutorService();
		}
		return instance;
	}

	/**
	 * 关闭线程池
	 */
	public static void shutdown()
	{
		if (instance != null && !instance.executorService.isShutdown() && !instance.executorService.isTerminated())
		{
			instance.executorService.shutdown();
		}
	}

	/**
	 * 提交任务
	 *
	 * @param task 任务
	 * @return
	 */
	public Future submitTask(Runnable task)
	{
		Future result = null;
		if (!executorService.isTerminated() && !executorService.isShutdown() && task != null)
		{
			result = executorService.submit(task);
			//			synchronized (queue)
			//			{
			//				queue.add(task);
			//				try
			//				{
			//					result = executorService.submit(queue.take());
			//				}
			//				catch (InterruptedException e)
			//				{
			//				}
			//		}
		}
		return result;
	}
}

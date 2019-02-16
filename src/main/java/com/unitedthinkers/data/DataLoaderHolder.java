package com.unitedthinkers.data;

import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;

/**
 * 
 * @author A. Karpenko
 * @date 16 февр. 2019 г. 16:55:20
 */
public class DataLoaderHolder
{
	public static final int CORE_POOL_SIZE = 10;
	public static final long UPDATE_DELAY = 10000; // миллисекунды
	
	private static ScheduledThreadPoolExecutor _poolExecutor;
	private static ScheduledFuture<?> _listUpdateTask;
	
	private DataLoaderHolder()
	{
		DataLoader.getInstance();
		
		_poolExecutor =  new ScheduledThreadPoolExecutor(CORE_POOL_SIZE, new ThreadFactory()
		{
			@Override
			public Thread newThread(Runnable r)
			{
				return new Thread(r);
			}
			
		});
		
		_listUpdateTask = _poolExecutor.schedule(new UpdateTask(), UPDATE_DELAY, TimeUnit.MILLISECONDS);
	}
	
	private static class UpdateTask implements Runnable
	{
		@Override
		public void run()
		{
			DataLoader.getInstance().reload();
			
			_listUpdateTask.cancel(false);
			_listUpdateTask = _poolExecutor.schedule(new UpdateTask(), UPDATE_DELAY, TimeUnit.MILLISECONDS);
		}
	}
	
	public static final DataLoaderHolder getInstance()
	{
		return SingletonHolder.INSTANCE;
	}
	
	private static final class SingletonHolder
	{
		private static final DataLoaderHolder INSTANCE = new DataLoaderHolder();
	}
}
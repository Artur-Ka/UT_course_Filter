package com.unitedthinkers.data;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * 
 * @author A. Karpenko
 * @date 16 февр. 2019 г. 15:17:55
 */
public final class DataLoader
{
	private static final Logger LOG = Logger.getLogger(DataLoader.class.getName());
	
	private static final String BANNED_IPs_FILE = "banned_ips.ini";
	
	private final List<String> _ips = new ArrayList<>();
	
	// Единственный экземпляр - getInstance()
	private DataLoader()
	{
		try
		{
			final File file = new File(BANNED_IPs_FILE);
			
			if (!file.exists())
			{
				file.createNewFile();
			}
			
			reload();
		}
		catch (IOException ioe)
		{
			ioe.printStackTrace();
		}
		finally
		{
			LOG.log(Level.INFO, "Banned ips file not found, created new file.");
		}
	}
	
	public List<String> getBannedIps()
	{
		return _ips;
	}
	
	public void reload()
	{
		try
		{
			_ips.clear();
			
			final List<String> newIps = Files.readAllLines(Paths.get(BANNED_IPs_FILE), StandardCharsets.UTF_8);
			
			for (String ip : newIps)
			{
				final String nIp = ip.trim();
				
				checkAddIp(nIp);
			}
		}
		catch (IOException ioe)
		{
			ioe.printStackTrace();
		}
		finally
		{
			LOG.log(Level.INFO, "Loaded banned ips: " + _ips.size());
		}
	}
	
	private boolean checkAddIp(String ip)
	{
		if (isValidIPAddress(ip))
		{
			return _ips.add(ip);
		}
		else
		{
			LOG.log(Level.WARNING, "Invalid ip address: " + ip);
			return false;
		}
	}
	
	private static boolean isValidIPAddress(String ipAddress)
	{
		final String[] parts = ipAddress.trim().split("\\.");
		if (parts.length != 4)
		{
			return false;
		}
		
		for (String s : parts)
		{
			final int i = Integer.parseInt(s);
			if ((i < 0) || (i > 255))
			{
				return false;
			}
		}
		
		return true;
	}
	
	public static final DataLoader getInstance()
	{
		return SingletonHolder.INSTANCE;
	}
	
	private static final class SingletonHolder
	{
		private static final DataLoader INSTANCE = new DataLoader();
	}
}
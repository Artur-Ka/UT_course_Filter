package com.unitedthinkers.servlets.filter;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import com.unitedthinkers.data.DataLoader;

/**
 * 
 * @author A. Karpenko
 * @date 16 февр. 2019 г. 18:56:44
 */
public class IPFilter implements Filter
{
	private static final Logger LOG = Logger.getLogger(IPFilter.class.getName());
	
	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException
	{
		final String remoteIp = request.getRemoteAddr();
		
		if (DataLoader.getInstance().getBannedIps().contains(remoteIp))
		{
			response.getWriter().append("Access disallowed");
			
			LOG.log(Level.INFO, "Access disallowed for address: " + remoteIp);
		}
		else
		{
			request.getRequestDispatcher("index.html").forward(request, response);
			
			LOG.log(Level.INFO, "Access allowed for address: " + remoteIp);
		}
	}
	
	@Override
	public void init(FilterConfig config) throws ServletException
	{
		
	}
	
	@Override
	public void destroy()
	{
		
	}
}

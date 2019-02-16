package com.unitedthinkers;

import java.util.EnumSet;
import java.util.logging.LogManager;
import java.util.logging.Logger;

import javax.servlet.DispatcherType;

import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.NCSARequestLog;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.server.handler.RequestLogHandler;
import org.eclipse.jetty.servlet.DefaultServlet;
import org.eclipse.jetty.servlet.FilterHolder;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;

import com.unitedthinkers.data.DataLoaderHolder;
import com.unitedthinkers.servlets.filter.IPFilter;

/**
 * 
 * @author A. Karpenko
 * @date 16 февр. 2019 г. 16:13:09
 */
public class MainServer
{
	private static final Logger LOG = Logger.getLogger(MainServer.class.getName());
	
	private static final int WEB_PORT = 8080;
	
	public static void main(String[] args) throws Exception
	{
		final long startTime = System.currentTimeMillis();
		
		LogManager.getLogManager().readConfiguration();
		DataLoaderHolder.getInstance();
		
		final Server webServer = new Server(WEB_PORT);
		
		final ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
		context.setContextPath("/");
		context.setResourceBase("WebContent");
		context.addFilter(new FilterHolder(new IPFilter()), "/*", EnumSet.of(DispatcherType.REQUEST));
		
		final ServletHolder staticHolder = new ServletHolder(new DefaultServlet()); 
	    context.addServlet(staticHolder, "/index.html");
		
		final NCSARequestLog requestLog = new NCSARequestLog(System.getProperty("user.dir") + "/request_log.log");
		requestLog.setRetainDays(3);
		requestLog.setAppend(true);
		requestLog.setExtended(true);
		requestLog.setLogTimeZone("EST");
		
		final RequestLogHandler logHandler = new RequestLogHandler();
		logHandler.setRequestLog(requestLog);
		
		final HandlerList handlers = new HandlerList();
		handlers.setHandlers(new Handler[] {context, logHandler});
		webServer.setHandler(handlers);   		
		
		printSection("WEB Server");
		webServer.start();
		LOG.info("WEB Server started on port: " + WEB_PORT);
		LOG.info("Server started in " + (System.currentTimeMillis() - startTime) + " milliseconds.");
	}
	
	public static void printSection(String s)
	{
		s = "=[ " + s + " ]";
		while (s.length() < 51)
		{
			s = "-" + s;
		}
		
		LOG.info(s);
	}
}
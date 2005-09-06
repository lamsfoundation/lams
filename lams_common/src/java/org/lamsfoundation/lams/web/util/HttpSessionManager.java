package org.lamsfoundation.lams.web.util;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Enumeration;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;

import org.apache.log4j.Logger;

import org.lamsfoundation.lams.util.Configuration;
import org.lamsfoundation.lams.util.ConfigurationKeys;


/**
 * It is used to manage HttpSession according to lams configuration (in lams.xml file).
 * There are 2 configuration items related to it. 
 * One is AllowMultipleLogin, the other is UserInactiveTimeout
 * <p>
 * When running with the test harness, need to set a configuration parameter allowMultipleLogins=true
 * This disables the invalidation of older
 * duplicated sessions - so that the poll and application test threads will run together.
 * It shouldn't be run with this set to true in production as not all the sessions are
 * tracked properly - only one session is tracked for each user. 
 * <p>
 * Why isn't this class a servlet rather than a singleton? Because it doesn't directly 
 * serve data back to a webpage - its primary function is to return data to the controller. 
 * Yet it is in the "web" part of the system, so there
 * is some argument for it being a servlet. An advantage of it being a servlet would be
 * that we could probably make it directly a listener (rather than having a listener
 * which calls this class), and we wouldn't need the test servlet ReportHttpSessionDataServlet
 * to dump the current status to a webpage.
 * 
 * @author Fei Yang
 * 		   
 */
public class HttpSessionManager
{
	private static Logger log = Logger.getLogger(HttpSessionManager.class);

	private static HttpSessionManager realMe = null;

	private static ServletContext context = null; //for UniversalLoginModule to use

	private Map HttpSessionsByLogin = null; // allows checking for two sessions
	private int timeout;//in seconds
	private boolean allowMultipleLogin; // must only be true for the test harness.

	private HttpSessionManager()
	{
		HttpSessionsByLogin = Collections.synchronizedMap(new HashMap());
		
		timeout = Configuration.getAsInt(ConfigurationKeys.INACTIVE_TIME);
		
		allowMultipleLogin = Configuration.getAsBoolean(ConfigurationKeys.ALLOW_MULTIPLE_LOGIN);
	}

	public static HttpSessionManager getInstance()
	{
		if (realMe == null)
			realMe= new HttpSessionManager();

		return realMe;
	}
	
	/** 
	 * Get the user http session.
	 */
	public HttpSession getHttpSessionByLogin(String login)
	{
		return (HttpSession)HttpSessionsByLogin.get(login);
	}

	/** 
	 * Update the user record
	 */
	public void updateHttpSessionByLogin(HttpSession session, String login)
	{
		if(!allowMultipleLogin)
		{
			if (login == null || session == null)
				return;
			HttpSession oldSession = (HttpSession)HttpSessionsByLogin.get(login);
			if (oldSession != null)
			{
				if(oldSession.getId().equals(session.getId()))
					return;
				try
				{
					copySessionAttributes(oldSession,session);
					oldSession.invalidate();	
				}catch(IllegalStateException e)
				{
					//it's already invalidated
					//do nothing
				}
			}
			HttpSessionsByLogin.put(login,session);
		}
	}

	private void copySessionAttributes(HttpSession oldSession,HttpSession session)
	{
		Enumeration enu = oldSession.getAttributeNames();
		while(enu.hasMoreElements())
		{
			String name = (String)enu.nextElement();
			session.setAttribute(name,oldSession.getAttribute(name));
		}
	}
	
	public ServletContext getServletContext()
	{
		return context;
	}
	
	/** 
	 * Matches sessionCreated in SessionListener, which implements the
	 * HttpSessionListener interface. SessionListener passes the session event
	 * to here. Can't be accessed directly (ie defined in web.xml) in this class as
	 * this class is a singleton and the creation method is private. 
	 */
	public void sessionCreated(HttpSessionEvent se)
	{
		if (se == null)
			return;
		HttpSession session= se.getSession();
		session.setMaxInactiveInterval(timeout);
		context = session.getServletContext();
	}
}

	
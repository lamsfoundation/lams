/*
 *Copyright (C) 2005 LAMS Foundation (http://lamsfoundation.org)
 *
 *This program is free software; you can redistribute it and/or modify
 *it under the terms of the GNU General Public License as published by
 *the Free Software Foundation; either version 2 of the License, or
 *(at your option) any later version.
 *
 *This program is distributed in the hope that it will be useful,
 *but WITHOUT ANY WARRANTY; without even the implied warranty of
 *MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *GNU General Public License for more details.
 *
 *You should have received a copy of the GNU General Public License
 *along with this program; if not, write to the Free Software
 *Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307
 *USA
 *
 *http://www.gnu.org/licenses/gpl.txt
 */
package org.lamsfoundation.lams.web.session;


import java.util.Enumeration;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionBindingEvent;
import javax.servlet.http.HttpSessionBindingListener;
import javax.servlet.http.HttpSessionContext;

import org.hibernate.id.UUIDHexGenerator;

import org.apache.log4j.Logger;
import org.lamsfoundation.lams.util.Configuration;
import org.lamsfoundation.lams.util.ConfigurationKeys;

import EDU.oswego.cs.dl.util.concurrent.ConcurrentReaderHashMap;
/**
 * 
 * @author Steve.Ni
 * 
 * @version $Revision$
 */
public class SessionManager{

	private static final Logger log = Logger.getLogger(SessionManager.class);
	
	//signlton 
	private static SessionManager sessionMgr;
	//KEY: sessionId, each session will have an identified id. VALUE: SessionImpl instance, which contains 
	//true session key/value pairs. 
	private Map sessionContainer = new ConcurrentReaderHashMap();
	//Save current session id
	private ThreadLocal currentSessionIdContainer = new ThreadLocal();
	
	//The system monitoring thread instance
	private Monitor monitor;
	//The sleep time in seconds period to monitoring the thread.
	//This attribute could be set in spring context.xml.
	private short monitorPeriod = 20;
	
	/**
	 * Get the singleton instance of this class.
	 * @return
	 */
	private static SessionManager getInstance(){
		if(sessionMgr == null)
			log.error("init SessionManager failed");
		
		return sessionMgr;
	}
	/**
	 * Get system level HttpSession by current session id.  
	 * @return HttpSession instanceof org.lamsfoundation.lams.systemsession.SessionManager#SessionImpl
	 */
	public static HttpSession getSession(){
		String sessionId = (String) getInstance().currentSessionIdContainer.get();
		return getSession(sessionId);
	}
	/**
	 * Get system session by given session id.
	 * @param sessionId
	 * @return system session. Return an null if the given sessionid can not map to an existed session. 
	 */
	public static HttpSession getSession(String sessionId){
		if(sessionId == null){
			log.error("Failed on finding current system session with null sessionId");
			return null;
		}
		return (HttpSession) getInstance().sessionContainer.get(sessionId);
		
	}
	
	static void createSession(String sessionId){
		//initialize a new one
		HttpSession session = getInstance().new SessionImpl(sessionId);
		getInstance().sessionContainer.put(sessionId,session);
		// TODO remove this debugging - only put in to diagnose Ozgur's session problem
		log.debug("SessionManager: creating new session "+sessionId);
	}
	
	/**
	 * Return  <code>SessionVisitor</code> of <code>currentSessionId</code>.
	 * <strong>An internal method, only avaliable in package.</strong>
	 * @return
	 */
	static SessionVisitor getSessionVisitor() {
		return (SessionVisitor)getSession();
	}
	/**
	 * <strong>An internal method, only avaliable in package.</strong>
	 * @param currentSessionId
	 */
	static void setCurrentSessionId(String currentSessionId) {
		getInstance().currentSessionIdContainer.set(currentSessionId);
	}
	/**
	 * This class initialize method called by Spring framework.
	 */
	public void init(){
		if(sessionMgr == null){
			//only start once
			sessionMgr = this;
			if (monitorPeriod > 0){
				monitor = new Monitor();
				monitor.start();
			}
		}
	}
	/**
	 * This class destroy method called by Spring framework.
	 */
	public void destroy(){
		if(monitor != null){
			sessionMgr = null;
			monitor.stop();
			monitor = null;
		}
		
	}
	
	public short getMonitorPeriod() {
		return monitorPeriod;
	}
	public void setMonitorPeriod(short monitorPeriod) {
		this.monitorPeriod = monitorPeriod;
	}

	/**
	 * Start a session for current ServletRequest and SerlvetResponse. 
	 * If session does not exist, then create a new session. If it exists, just using current session.
	 * 
	 * @param req
	 * @param res
	 */
	public static void startSession(ServletRequest req, ServletResponse res) {
		Cookie cookie = findCookie((HttpServletRequest) req,SystemSessionFilter.SYS_SESSION_COOKIE);
		String currentSessionId = null;
		if(cookie != null){
			currentSessionId = cookie.getValue();
			Object obj = getSession(currentSessionId);
			// TODO remove this debugging - only put in to diagnose Ozgur's session problem
			if ( log.isDebugEnabled() ) {
				log.debug("SessionManager: cookie is not null, currentSessionId="+currentSessionId
						+" session="+obj);
			}
			//if cookie exist, but session does not. This usually menas seesion expired. 
			//then delete the cookie first and set it null in order to create a new one
			if(obj == null){
				createSession(currentSessionId);
				//After changing cookie name to JSESSIONID, left cookie lifecycle management to Server
//				removeCookie((HttpServletResponse) res,SystemSessionFilter.SYS_SESSION_COOKIE);
//				cookie = null;
			}
		}
		//can not be in else!
		if(cookie == null){
			// TODO remove this debugging - only put in to diagnose Ozgur's session problem
			if ( log.isDebugEnabled() ) {
				log.debug("SessionManager: cookie is null, generating a new session");
			}
			//create new session and set it into cookie
			currentSessionId = (String) new UUIDHexGenerator().generate(null,null);
			createSession(currentSessionId);
			cookie = createCookie((HttpServletResponse) res,SystemSessionFilter.SYS_SESSION_COOKIE,currentSessionId);
		}
		
		setCurrentSessionId(currentSessionId);
		//reset session last access time
		SessionVisitor sessionVisitor = getSessionVisitor();
		sessionVisitor.accessed();
	}
	/**
	 * This method will reset current session id, so programs can not use <code>getSession()</code> to get current
	 * session after this method is called.  
	 */
	public static void endSession() {
		setCurrentSessionId(null);
	}

	/**
	 * Find a cookie by given cookie name from request.
	 * 
	 * @param req
	 * @param name The cookie name
	 * @return The cookie of this name in the request, or null if not found.
	 */
	private static Cookie findCookie(HttpServletRequest req, String name)
	{
		Cookie[] cookies = req.getCookies();
		if (cookies != null) {
			for (int i = 0; i < cookies.length; i++) {
				if (cookies[i].getName().equals(name)) {
					return cookies[i];
				}
			}
		}
	
		return null;
	}
	/**
	 * Remove cookie by given name from request
	 * @param res
	 * @param name
	 * @return the removed cookies
	 */
	private  static Cookie removeCookie(HttpServletResponse res, String name){
		Cookie cookie = new Cookie(name, "");
		cookie.setPath("/");
		cookie.setMaxAge(0);
		res.addCookie(cookie);
		
		return cookie;
	}
	/**
	 * Create a new cookie for request.
	 * @param res
	 * @param name cookie name
	 * @param value cookie value
	 * @return the created cookie.
	 */
	private static Cookie createCookie(HttpServletResponse res, String name, String value){
		Cookie cookie = new Cookie(name, value);
		cookie.setPath("/");
		cookie.setMaxAge(-1);
		res.addCookie(cookie);
		
		return cookie;
	}

	//************************************************************************
	// SYSTEM SESSION MONITOR CLASS
	//************************************************************************
	class Monitor implements Runnable{
		private static final String THREAD_NAME = "LAMS SYSTEM SESSION MONITOR";
		private Thread monitoringThread;
		private boolean stopSign = false;
		public void start(){
			monitoringThread = new Thread(this,THREAD_NAME);
			stopSign = false;
			monitoringThread.start();
		}
		public void run() {
			while (!stopSign) {
				try {
					//check whether session is expired
					Iterator iter = sessionContainer.values().iterator();
					while(iter.hasNext()) {
						SessionImpl session = (SessionImpl) iter.next();
						if(session.getMaxInactiveInterval() > 0){
							if ((System.currentTimeMillis() - session.getLastAccessedTime() - 
									session.getMaxInactiveInterval() * 1000L) > 0)
								session.invalidate();
						}
					}
				} catch (Throwable e) {
					log.warn("Monitor thread exception: " + e);
				}
				if (!stopSign) {
					try {
						Thread.sleep(monitorPeriod * 1000L);
					} catch (Exception e) {
						// do nothing
					}
				}
			}
		}
		
		public void stop(){
			
			if (monitoringThread != null){
				stopSign = true;
				monitoringThread.interrupt();
				try{
					monitoringThread.join();
				}catch (InterruptedException ignore){
					log.error("Exception when interruptting Session Monitoring Thread");
				}
				monitoringThread = null;
			}
		}
	}
	//************************************************************************
	// SYSTEM SESSION IMPLEMENTAION CLASS
	//************************************************************************
	class SessionImpl implements HttpSession,SessionVisitor {

		private String sessionId;
		private long createTime;
		private long accessTime;
		private int timeout;
		
		private Map valueMap;
		
		public SessionImpl(String sessionId){
			this.sessionId = sessionId;
			createTime = System.currentTimeMillis();
			accessTime = createTime;
			timeout = Configuration.getAsInt(ConfigurationKeys.INACTIVE_TIME);
			valueMap = new ConcurrentReaderHashMap();
		}
		/**
		 * {@inheritDoc}
		 */		
		public long getCreationTime() {
			return createTime;
		}
		/**
		 * {@inheritDoc}
		 */
		public String getId() {
			return sessionId;
		}
		/**
		 * {@inheritDoc}
		 */
		public long getLastAccessedTime() {
			return accessTime;
		}
		/**
		 * {@inheritDoc}
		 */
		public void setMaxInactiveInterval(int timeout) {
			this.timeout = timeout; 
		}
		/**
		 * {@inheritDoc}
		 */
		public int getMaxInactiveInterval() {
			return timeout;
		}

		/**
		 * {@inheritDoc}
		 */
		public Object getAttribute(String name) {
			return valueMap.get(name);
		}
		/**
		 * {@inheritDoc}
		 */
		public Enumeration getAttributeNames() {
			
			return new Enumeration(){
				Iterator iter = valueMap.keySet().iterator();
				public boolean hasMoreElements() {
					return iter.hasNext();
				}

				public Object nextElement() {
					return iter.next();
				}
				
			};
		}
		/**
		 * {@inheritDoc}
		 */
		public void setAttribute(String name, Object value) {
			if(value == null)
				removeAttribute(name);
			
			Object old = valueMap.put(name, value);

			fireBound(name, value);
			
			if (old != null){
				fireUnbound(name, old);
			}
		}
		/**
		 * {@inheritDoc}
		 */
		public void removeAttribute(String name) {
			Object value = valueMap.remove(name);
			if(value != null)
				fireUnbound(name, value);
		}
		/**
		 * {@inheritDoc}
		 */
		public void invalidate() {
			
			Iterator iter = valueMap.entrySet().iterator();
			while(iter.hasNext()){
				Map.Entry entry = (Map.Entry) iter.next();
				fireUnbound((String) entry.getKey(),entry.getValue());
			}
			valueMap.clear();
		}
		/**
		 * Notice: This method always return <strong>false</strong>
		 * {@inheritDoc}
		 */
		public boolean isNew() {
			return false;
		}
		
		/**
		 * {@inheritDoc}
		 */
		public void putValue(String name, Object value) {
			setAttribute(name,value);
		}
		/**
		 * {@inheritDoc}
		 */
		public void removeValue(String name) {
			removeAttribute(name);
		}
		/**
		 * {@inheritDoc}
		 */
		public Object getValue(String name) {
			return getAttribute(name);
		}
		/**
		 * {@inheritDoc}
		 */
		public String[] getValueNames() {
			return (String[])valueMap.keySet().toArray(new String[valueMap.size()]);
		}
		/**
		 * {@inheritDoc}
		 */
		public HttpSessionContext getSessionContext() {
			return new HttpSessionContext(){

				public HttpSession getSession(String sessionId) {
					return SessionImpl.this;
				}

				public Enumeration getIds() {
					return new Enumeration(){
						public boolean hasMoreElements() {
							return false;
						}
						public Object nextElement() {
							return null;
						}
					};
				}
			
			};
		}
		/**
		 * Notice: This method always return null.
		 * {@inheritDoc}
		 */		
		public ServletContext getServletContext() {
			return null;
		}
		//**********************************************************
		// SessionVisitor method
		public void accessed() {
			accessTime = System.currentTimeMillis();
		}
		//**********************************************************
		// private method 
		private void fireUnbound(String name, Object value) {
			if(value instanceof HttpSessionBindingListener){
				HttpSessionBindingEvent event = new HttpSessionBindingEvent(this,name,value);
				((HttpSessionBindingListener)value).valueUnbound(event);
			}
		}		
		private void fireBound(String name, Object value) {
			if(value instanceof HttpSessionBindingListener){
				HttpSessionBindingEvent event = new HttpSessionBindingEvent(this,name,value);
				((HttpSessionBindingListener)value).valueBound(event);
			}
		}

	}

}

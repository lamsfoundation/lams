/****************************************************************
 * Copyright (C) 2005 LAMS Foundation (http://lamsfoundation.org)
 * =============================================================
 * License Information: http://lamsfoundation.org/licensing/lams/2.0/
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License version 2.0
 * as published by the Free Software Foundation.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301
 * USA
 *
 * http://www.gnu.org/licenses/gpl.txt
 * ****************************************************************
 */

package org.lamsfoundation.lams.web.session;

import java.util.Enumeration;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

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

import org.apache.log4j.Logger;
import org.hibernate.id.UUIDHexGenerator;
import org.lamsfoundation.lams.util.Configuration;
import org.lamsfoundation.lams.util.ConfigurationKeys;

/**
 *
 * @author Steve.Ni
 *
 * @version $Revision$
 */
public class SessionManager {

    private static final Logger log = Logger.getLogger(SessionManager.class);

    // singleton
    private static SessionManager sessionMgr;
    // KEY: sessionId, each session will have an identified id. VALUE: SessionImpl instance, which contains
    // true session key/value pairs.
    private Map<String, HttpSession> sessionContainer = new ConcurrentHashMap<String, HttpSession>();
    // Save current session id
    private ThreadLocal<String> currentSessionIdContainer = new ThreadLocal<String>();

    // The system monitoring thread instance
    private Monitor monitor;
    // The sleep time in seconds period to monitoring the thread.
    // This attribute could be set in spring context.xml.
    private short monitorPeriod = 20;

    /**
     * Get the singleton instance of this class.
     *
     * @return
     */
    private static SessionManager getInstance() {
	if (SessionManager.sessionMgr == null) {
	    SessionManager.log.error("init SessionManager failed");
	}

	return SessionManager.sessionMgr;
    }

    /**
     * Get system level HttpSession by current session id.
     *
     * @return HttpSession instanceof org.lamsfoundation.lams.systemsession.SessionManager#SessionImpl
     */
    public static HttpSession getSession() {
	String sessionId = SessionManager.getInstance().currentSessionIdContainer.get();
	return SessionManager.getSession(sessionId);
    }

    /**
     * Get system session by given session id.
     *
     * @param sessionId
     * @return system session. Return an null if the given sessionid can not map to an existed session.
     */
    public static HttpSession getSession(String sessionId) {
	if (sessionId == null) {
	    SessionManager.log.debug("Failed on finding current system session with null sessionId");
	    return null;
	}
	return SessionManager.getInstance().sessionContainer.get(sessionId);

    }

    /**
     * Returns number of sessions stored in the container.
     */
    public static int getSessionCount() {
	return SessionManager.getInstance().sessionContainer.size();
    }

    static void createSession(String sessionId) {
	// initialize a new one
	HttpSession session = SessionManager.getInstance().new SessionImpl(sessionId);
	SessionManager.getInstance().sessionContainer.put(sessionId, session);
    }

    /**
     * Return <code>SessionVisitor</code> of <code>currentSessionId</code>. <strong>An internal method, only available
     * in package.</strong>
     *
     * @return
     */
    static SessionVisitor getSessionVisitor() {
	return (SessionVisitor) SessionManager.getSession();
    }

    /**
     * <strong>An internal method, only available in package.</strong>
     *
     * @param currentSessionId
     */
    static void setCurrentSessionId(String currentSessionId) {
	SessionManager.getInstance().currentSessionIdContainer.set(currentSessionId);
    }

    /**
     * This class initialize method called by Spring framework.
     */
    public void init() {
	if (SessionManager.sessionMgr == null) {
	    // only start once
	    SessionManager.sessionMgr = this;
	    if (monitorPeriod > 0) {
		monitor = new Monitor();
		monitor.start();
	    }
	}
    }

    /**
     * This class destroy method called by Spring framework.
     */
    public void destroy() {
	if (monitor != null) {
	    SessionManager.sessionMgr = null;
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
     * Start a session for current ServletRequest and SerlvetResponse. If session does not exist, then create a new
     * session. If it exists, just using current session.
     *
     * @param req
     * @param res
     */
    public static void startSession(ServletRequest req, ServletResponse res) {
	Cookie ssoCookie = SessionManager.findCookie((HttpServletRequest) req, SystemSessionFilter.SSO_SESSION_COOKIE);
	String currentSessionId = null;
	if (ssoCookie != null) {
	    currentSessionId = ssoCookie.getValue();
	    Object obj = SessionManager.getSession(currentSessionId);
	    // log.debug(ssoCookie.getName() + " cookie exists, value " + currentSessionId);
	    // if cookie exists, but session does not - usually means session expired.
	    // delete the cookie first and set it to null in order to create a new one
	    if (obj == null) {
		SessionManager.log.debug(SystemSessionFilter.SSO_SESSION_COOKIE + " " + currentSessionId
			+ " cookie exists, but corresponding session doesn't exist, removing cookie");
		SessionManager.removeCookie((HttpServletResponse) res, SystemSessionFilter.SSO_SESSION_COOKIE);
		ssoCookie = null;
	    }
	}
	if (ssoCookie == null) {
	    currentSessionId = (String) new UUIDHexGenerator().generate(null, null);
	    // create new session and set it into cookie
	    SessionManager.createSession(currentSessionId);
	    ssoCookie = SessionManager.createCookie((HttpServletResponse) res, SystemSessionFilter.SSO_SESSION_COOKIE,
		    currentSessionId);
	    SessionManager.log
		    .debug("==>Creating new " + SystemSessionFilter.SSO_SESSION_COOKIE + " - " + ssoCookie.getValue());
	}

	Cookie cookie = SessionManager.findCookie((HttpServletRequest) req, SystemSessionFilter.SYS_SESSION_COOKIE);
	if (cookie == null) {
	    // If a session exists in the request without a corresponding JSESSIONID cookie, assume
	    // user lost their cookie or closed their browser, so invalidate the session
	    HttpSession session = ((HttpServletRequest) req).getSession(false);
	    if (session != null) {
		session.invalidate();
	    }
	}

	SessionManager.setCurrentSessionId(currentSessionId);
	// reset session last access time
	SessionVisitor sessionVisitor = SessionManager.getSessionVisitor();
	sessionVisitor.accessed();
    }

    /**
     * This method will reset current session id, so programs can not use <code>getSession()</code> to get current
     * session after this method is called.
     */
    public static void endSession() {
	SessionManager.setCurrentSessionId(null);
    }

    /**
     * Find a cookie by given cookie name from request.
     *
     * @param req
     * @param name
     *            The cookie name
     * @return The cookie of this name in the request, or null if not found.
     */
    private static Cookie findCookie(HttpServletRequest req, String name) {
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
     *
     * @param res
     * @param name
     * @return the removed cookies
     */
    private static Cookie removeCookie(HttpServletResponse res, String name) {
	Cookie cookie = new Cookie(name, "");
	cookie.setPath("/");
	cookie.setMaxAge(0);
	res.addCookie(cookie);

	return cookie;
    }

    /**
     * Create a new cookie for request.
     *
     * @param res
     * @param name
     *            cookie name
     * @param value
     *            cookie value
     * @return the created cookie.
     */
    private static Cookie createCookie(HttpServletResponse res, String name, String value) {
	Cookie cookie = new Cookie(name, value);
	cookie.setPath("/");
	// cookie.setMaxAge(Configuration.getAsInt(ConfigurationKeys.INACTIVE_TIME));
	res.addCookie(cookie);

	return cookie;
    }

    // ************************************************************************
    // SYSTEM SESSION MONITOR CLASS
    // ************************************************************************
    class Monitor implements Runnable {
	private static final String THREAD_NAME = "LAMS SYSTEM SESSION MONITOR";
	private Thread monitoringThread;
	private boolean stopSign = false;

	public void start() {
	    monitoringThread = new Thread(this, Monitor.THREAD_NAME);
	    stopSign = false;
	    monitoringThread.start();
	}

	@Override
	public void run() {
	    while (!stopSign) {
		try {
		    // check whether session is expired
		    Iterator<HttpSession> iter = sessionContainer.values().iterator();
		    while (iter.hasNext()) {
			SessionImpl session = (SessionImpl) iter.next();
			if (session.getMaxInactiveInterval() > 0) {
			    if (System.currentTimeMillis() - session.getLastAccessedTime()
				    - session.getMaxInactiveInterval() * 1000L > 0) {
				session.invalidate();
			    }
			}
		    }
		} catch (Throwable e) {
		    SessionManager.log.warn("Monitor thread exception: " + e);
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

	public void stop() {

	    if (monitoringThread != null) {
		stopSign = true;
		monitoringThread.interrupt();
		try {
		    monitoringThread.join();
		} catch (InterruptedException ignore) {
		    SessionManager.log.error("Exception when interruptting Session Monitoring Thread");
		}
		monitoringThread = null;
	    }
	}
    }

    // ************************************************************************
    // SYSTEM SESSION IMPLEMENTAION CLASS
    // ************************************************************************
    class SessionImpl implements HttpSession, SessionVisitor {

	private String sessionId;
	private long createTime;
	private long accessTime;
	private int timeout;

	private Map<String, Object> valueMap;

	public SessionImpl(String sessionId) {
	    this.sessionId = sessionId;
	    createTime = System.currentTimeMillis();
	    accessTime = createTime;
	    timeout = Configuration.getAsInt(ConfigurationKeys.INACTIVE_TIME);
	    valueMap = new ConcurrentHashMap<String, Object>();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public long getCreationTime() {
	    return createTime;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getId() {
	    return sessionId;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public long getLastAccessedTime() {
	    return accessTime;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setMaxInactiveInterval(int timeout) {
	    this.timeout = timeout;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int getMaxInactiveInterval() {
	    return timeout;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Object getAttribute(String name) {
	    return valueMap.get(name);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Enumeration<String> getAttributeNames() {
	    return new Enumeration<String>() {
		Iterator<String> iter = valueMap.keySet().iterator();

		@Override
		public boolean hasMoreElements() {
		    return iter.hasNext();
		}

		@Override
		public String nextElement() {
		    return iter.next();
		}

	    };
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setAttribute(String name, Object value) {
	    if (value == null) {
		removeAttribute(name);
	    }

	    Object old = valueMap.put(name, value);

	    fireBound(name, value);

	    if (old != null) {
		fireUnbound(name, old);
	    }
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void removeAttribute(String name) {
	    Object value = valueMap.remove(name);
	    if (value != null) {
		fireUnbound(name, value);
	    }
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void invalidate() {
	    Iterator<Entry<String, Object>> iter = valueMap.entrySet().iterator();
	    while (iter.hasNext()) {
		Entry<String, Object> entry = iter.next();
		fireUnbound(entry.getKey(), entry.getValue());
	    }
	    valueMap.clear();
	    // remove from map
	    SessionManager.getInstance().sessionContainer.remove(this.sessionId);
	}

	/**
	 * Notice: This method always return <strong>false</strong> {@inheritDoc}
	 */
	@Override
	public boolean isNew() {
	    return false;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void putValue(String name, Object value) {
	    setAttribute(name, value);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void removeValue(String name) {
	    removeAttribute(name);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Object getValue(String name) {
	    return getAttribute(name);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String[] getValueNames() {
	    return valueMap.keySet().toArray(new String[valueMap.size()]);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public HttpSessionContext getSessionContext() {
	    return new HttpSessionContext() {

		@Override
		public HttpSession getSession(String sessionId) {
		    return SessionImpl.this;
		}

		@Override
		public Enumeration getIds() {
		    return new Enumeration() {
			@Override
			public boolean hasMoreElements() {
			    return false;
			}

			@Override
			public Object nextElement() {
			    return null;
			}
		    };
		}

	    };
	}

	/**
	 * Notice: This method always return null. {@inheritDoc}
	 */
	@Override
	public ServletContext getServletContext() {
	    return null;
	}

	// **********************************************************
	// SessionVisitor method
	@Override
	public void accessed() {
	    accessTime = System.currentTimeMillis();
	}

	// **********************************************************
	// private method
	private void fireUnbound(String name, Object value) {
	    if (value instanceof HttpSessionBindingListener) {
		HttpSessionBindingEvent event = new HttpSessionBindingEvent(this, name, value);
		((HttpSessionBindingListener) value).valueUnbound(event);
	    }
	}

	private void fireBound(String name, Object value) {
	    if (value instanceof HttpSessionBindingListener) {
		HttpSessionBindingEvent event = new HttpSessionBindingEvent(this, name, value);
		((HttpSessionBindingListener) value).valueBound(event);
	    }
	}

    }
}
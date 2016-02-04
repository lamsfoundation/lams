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
/* $$Id$$ */
package org.lamsfoundation.lams.web.session;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

public class SessionManager {
    public static final String SYS_SESSION_COOKIE = "JSESSIONID";

    // singleton
    private static SessionManager sessionManager;
    private static final Map<String, HttpSession> sessionIdMapping = new ConcurrentHashMap<String, HttpSession>();
    private static final Map<String, HttpSession> loginMapping = new ConcurrentHashMap<String, HttpSession>();
    private ThreadLocal<String> currentSessionIdContainer = new ThreadLocal<String>();

    // various classes need to have to access these
    private static ServletContext servletContext;
    private static String jvmRoute;

    /**
     * This class initialize method called by Spring framework.
     */
    public void init() {
	if (SessionManager.sessionManager == null) {
	    // only start once
	    SessionManager.sessionManager = this;
	}
    }

    /**
     * This class destroy method called by Spring framework.
     */
    public void destroy() {
	SessionManager.sessionManager = null;
    }

    /**
     * Stores session in current thread and mapping so other modules can refer to it.
     */
    public static void startSession(HttpServletRequest request) {
	HttpSession session = request.getSession();
	String sessionId = session.getId();
	SessionManager.sessionIdMapping.put(sessionId, session);
	SessionManager.sessionManager.currentSessionIdContainer.set(sessionId);
    }

    /**
     * This method will reset current session id, so programs can not use <code>getSession()</code> to get current
     * session after this method is called.
     */
    public static void endSession() {
	SessionManager.sessionManager.currentSessionIdContainer.set(null);
    }

    /**
     * Registeres the session for the given user.
     */
    public static void addSession(String login, HttpSession session) {
	SessionManager.loginMapping.put(login, session);
    }

    /**
     * Unregisteres the session for the given user.
     */
    public static void removeSession(String login, boolean invalidate) {
	HttpSession session = SessionManager.loginMapping.get(login);
	if (session != null) {
	    SessionManager.loginMapping.remove(login);
	    SessionManager.sessionIdMapping.remove(session.getId());

	    if (invalidate) {
		try {
		    session.invalidate();
		} catch (IllegalStateException e) {
		    System.out.println("SessionMananger invalidation exception");
		    // if it was already invalidated, do nothing
		}
	    }
	}
    }

    /**
     * Get system level HttpSession by current session id.
     */
    public static HttpSession getSession() {
	String sessionId = SessionManager.sessionManager.currentSessionIdContainer.get();
	return SessionManager.getSession(sessionId);
    }

    /**
     * Get system session by given session id.
     */
    public static HttpSession getSession(String sessionId) {
	return sessionId == null ? null : SessionManager.sessionIdMapping.get(sessionId);
    }

    /**
     * Returns number of sessions stored in the container.
     */
    public static int getSessionCount() {
	return SessionManager.sessionIdMapping.size();
    }

    public static void setServletContext(ServletContext servletContext) {
	SessionManager.servletContext = servletContext;
    }

    public static ServletContext getServletContext() {
	return SessionManager.servletContext;
    }

    public static String getJvmRoute() {
	return SessionManager.jvmRoute;
    }

    public static void setJvmRoute(String jvmRoute) {
	SessionManager.jvmRoute = jvmRoute;
    }
}
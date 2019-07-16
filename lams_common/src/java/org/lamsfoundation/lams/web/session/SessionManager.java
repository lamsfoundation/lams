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

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentHashMap;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.lamsfoundation.lams.usermanagement.dto.UserDTO;
import org.lamsfoundation.lams.web.util.AttributeNames;

public class SessionManager {
    public static final String SYS_SESSION_COOKIE = "JSESSIONID";
    // if this attribute is set in session, next call will force the user to log out
    public static final String LOG_OUT_FLAG = "lamsLogOutFlag";

    // singleton
    private static SessionManager sessionManager;
    private static final Map<String, HttpSession> sessionIdMapping = new ConcurrentHashMap<>();
    private static final Map<String, HttpSession> loginMapping = new ConcurrentHashMap<>();
    private ThreadLocal<String> currentSessionIdContainer = new ThreadLocal<>();

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
	if (session.getAttribute(LOG_OUT_FLAG) != null) {
	    // session was flagged for invalidation
	    session.invalidate();
	    throw new SecurityException("You were logged out");
	}

	String sessionId = session.getId();
	sessionIdMapping.put(sessionId, session);
	sessionManager.currentSessionIdContainer.set(sessionId);

	// check if another session for this user already exists
	String login = request.getRemoteUser();
	if (login != null) {
	    HttpSession existingSession = loginMapping.get(login);
	    // check if it's a different session and if so, which one is newer
	    if (existingSession != null && !existingSession.getId().equals(sessionId)) {
		if (session.getCreationTime() > existingSession.getCreationTime()) {
		    // mark the other session for invalidation
		    existingSession.setAttribute(LOG_OUT_FLAG, true);
		} else {
		    // invalidate this session
		    session.invalidate();
		    throw new SecurityException("You were logged out");
		}
	    }
	    loginMapping.put(login, session);
	}
    }

    /**
     * This method will reset current session id, so programs can not use <code>getSession()</code> to get current
     * session after this method is called.
     */
    public static void endSession() {
	SessionManager.sessionManager.currentSessionIdContainer.set(null);
    }

    /**
     * Unregisteres the session for the given user.
     */
    public static void removeSessionByLogin(String login, boolean invalidate) {
	HttpSession session = SessionManager.loginMapping.get(login);
	if (session == null) {
	    return;
	}
	SessionManager.loginMapping.remove(login);

	if (invalidate) {
	    // only mark for invalidation, not invalidate
	    // otherwise on clustered environment we get problems with server-side invalidation of Infinispan distributed sessions
	    // see WFLY-7281 and WFLY-7229; maybe it will work on WildFly 11
	    session.setAttribute(LOG_OUT_FLAG, true);
	}
    }

    /**
     * Unregisteres the session by the given ID.
     */
    public static boolean removeSessionByID(String sessionID, boolean invalidate, boolean clearLoginMapping) {
	HttpSession session = SessionManager.getSession(sessionID);
	if (session != null) {
	    SessionManager.sessionIdMapping.remove(sessionID);

	    if (invalidate) {
		// only mark for invalidation, not invalidate
		// otherwise on clustered environment we get problems with server-side invalidation of Infinispan distributed sessions
		// see WFLY-7281 and WFLY-7229; maybe it will work on WildFly 11
		session.setAttribute(LOG_OUT_FLAG, true);
	    }
	}
	if (clearLoginMapping) {
	    // it seems that sometimes session does not contain userDTO, but login mapping exists
	    // we need to try to clear it when destroying the session
	    String login = null;
	    for (Entry<String, HttpSession> sessionEntry : SessionManager.loginMapping.entrySet()) {
		if (sessionID.equals(sessionEntry.getValue().getId())) {
		    login = sessionEntry.getKey();
		    break;
		}
	    }
	    if (login != null) {
		SessionManager.loginMapping.remove(login);
	    }
	}

	return session != null;
    }

    /**
     * Makes sure that given session ID points to correct session.
     * It may not be the case after session ID change after login.
     */
    public static void updateSessionID(String sessionID) {
	HttpSession session = SessionManager.getSession(sessionID);
	if (session == null) {
	    return;
	}
	String newSessionID = session.getId();
	if (!sessionID.equals(newSessionID)) {
	    SessionManager.sessionIdMapping.remove(sessionID);
	    SessionManager.sessionIdMapping.put(newSessionID, session);
	    SessionManager.sessionManager.currentSessionIdContainer.set(newSessionID);
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
     * Get system session by given login.
     */
    public static HttpSession getSessionForLogin(String login) {
	return SessionManager.loginMapping.get(login);
    }

    /**
     * Returns number of all sessions stored in the container.
     */
    public static int getSessionTotalCount() {
	return SessionManager.sessionIdMapping.size();
    }

    /**
     * Returns number of authenticated sessions stored in the container.
     */
    public static int getSessionUserCount() {
	return SessionManager.loginMapping.size();
    }

    /**
     * Lists all logins with their assigned sessions
     */
    public static Map<String, List<String>> getLoginToSessionIDMappings() {
	Map<String, List<String>> result = new TreeMap<>();
	for (Entry<String, HttpSession> entry : loginMapping.entrySet()) {
	    HttpSession session = entry.getValue();
	    UserDTO user = (UserDTO) session.getAttribute(AttributeNames.USER);
	    List<String> sessionInfo = new LinkedList<>();
	    sessionInfo.add(user.getFirstName());
	    sessionInfo.add(user.getLastName());
	    sessionInfo.add(session.getId());
	    result.put(entry.getKey(), sessionInfo);
	}
	return result;
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
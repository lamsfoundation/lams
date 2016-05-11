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

package org.lamsfoundation.lams.web.util;

import java.util.Date;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForward;

/**
 * @author daveg
 *
 *         This class is a replacement for the struts TokenProcessor class.
 *
 */
public class TokenProcessor {

    /**
     * The session token key
     */
    public static String TRANSACTION_TOKEN_KEY = "org.lamsfoundation.lams.action.TOKEN";
    /**
     * The request token key
     */
    public static String TOKEN_KEY = "lams_token";
    /**
     * The session forwards key
     */
    public static String TRANSACTION_FORWARDS = "org.lamsfoundation.lams.action.FORWARD";

    /**
     * The timestamp used most recently to generate a token value.
     */
    private long previous;

    /**
     * The singleton instance of this class.
     */
    private static TokenProcessor instance = new TokenProcessor();

    private static org.apache.struts.util.TokenProcessor strutsTokenProcessor = org.apache.struts.util.TokenProcessor
	    .getInstance();

    /**
     * Retrieves the singleton instance of this class.
     */
    public static TokenProcessor getInstance() {
	return instance;
    }

    /**
     * Protected constructor for TokenProcessor. Use TokenProcessor.getInstance()
     * to obtain a reference to the processor.
     */
    protected TokenProcessor() {
	super();
    }

    /**
     * Gets the tokens map from session.
     */
    private HashMap getTokens(HttpSession session) {
	HashMap tokens = (HashMap) session.getAttribute(TRANSACTION_TOKEN_KEY);
	return tokens;
    }

    /**
     * Sets the tokens map in session.
     */
    private void setTokens(HttpSession session, HashMap tokens) {
	session.setAttribute(TRANSACTION_TOKEN_KEY, tokens);
    }

    /**
     * Gets the given token from the request parameter.
     */
    private String getTokenFromRequest(HttpServletRequest request) {
	String token = request.getParameter(TOKEN_KEY);
	return token;
    }

    /**
     * Sets the given token as a request attribute.
     */
    private void setTokenInRequest(HttpServletRequest request, String token) {
	request.setAttribute(TOKEN_KEY, token);
    }

    /**
     * Gets the forwards map from session.
     */
    private HashMap getForwards(HttpSession session) {
	HashMap forwards = (HashMap) session.getAttribute(TRANSACTION_FORWARDS);
	return forwards;
    }

    /**
     * Sets the tokens map in session.
     */
    private void setForwards(HttpSession session, HashMap forwards) {
	session.setAttribute(TRANSACTION_FORWARDS, forwards);
    }

    /**
     * Return <code>true</code> if there is a transaction token stored in
     * the user's current session that matches the value submitted as a request
     * parameter. Returns <code>false</code>
     * <ul>
     * <li>No session associated with this request</li>
     * <li>No transaction token saved in the session</li>
     * <li>No transaction token included as a request parameter</li>
     * <li>The included transaction token value does not match any
     * transaction token in the user's session</li>
     * </ul>
     * 
     * @param request
     *            The servlet request we are processing
     */
    public synchronized boolean isTokenValid(HttpServletRequest request) {
	return this.isTokenValid(request, false);
    }

    /**
     * Return <code>true</code> if there is a transaction token stored in
     * the user's current session that matches the value submitted as a request
     * parameter. Returns <code>false</code>
     * <ul>
     * <li>No session associated with this request</li>
     * <li>No transaction token saved in the session</li>
     * <li>No transaction token included as a request parameter</li>
     * <li>The included transaction token value does not match any
     * transaction token in the user's session</li>
     * </ul>
     * 
     * @param request
     *            The servlet request
     * @param reset
     *            Reset the token after checking it?
     */
    public synchronized boolean isTokenValid(HttpServletRequest request, boolean reset) {

	// Retrieve the current session for this request
	HttpSession session = request.getSession(false);
	if (session == null) {
	    return false;
	}

	// Retrieve the transaction tokens from this session
	HashMap tokens = getTokens(session);
	if (tokens == null) {
	    return false;
	}

	// Retrieve the transaction token included in this request
	String token = getTokenFromRequest(request);
	if (token == null) {
	    return false;
	}

	// Find token in session token map
	Long timestamp = (Long) tokens.get(token);

	// Reset token
	if (reset) {
	    this.resetToken(request);
	}

	return (timestamp != null);
    }

    /**
     * Reset the saved transaction token from request in the user's session.
     * 
     * @param request
     *            The servlet request
     */
    public synchronized void resetToken(HttpServletRequest request) {
	HttpSession session = request.getSession(false);
	if (session == null) {
	    return;
	}

	HashMap tokens = getTokens(session);
	if (tokens == null) {
	    return;
	}

	String token = getTokenFromRequest(request);
	if (token == null) {
	    return;
	}

	tokens.remove(token);
    }

    /**
     * Save a new transaction token in the user's current session, creating
     * a new session if necessary. This method also saves the new token as
     * a request attribute so that the JSP form add the required hidden input
     * field.
     * 
     * @param request
     *            The servlet request
     */
    public synchronized void saveToken(HttpServletRequest request) {
	HttpSession session = request.getSession(true);

	HashMap tokens = getTokens(session);
	if (tokens == null) {
	    tokens = new HashMap();
	}

	String token = generateToken(request);
	if (token != null) {
	    Long timestamp = new Long((new Date()).getTime());
	    tokens.put(token, timestamp);
	    setTokens(session, tokens);
	    setTokenInRequest(request, token);
	}

    }

    /**
     * Generate a new transaction token, calls the struts TokenProcessor.
     * 
     * @param request
     *            The servlet request
     */
    public synchronized String generateToken(HttpServletRequest request) {
	// hopefully this will have no side effects.
	return strutsTokenProcessor.generateToken(request);
    }

    /**
     * Saves the ActionForward that was used for this token. For use with
     * auto-recovery.
     */
    public synchronized void saveForward(javax.servlet.http.HttpServletRequest request, ActionForward actionForward) {
	HttpSession session = request.getSession(false);
	if (session == null) {
	    return;
	}

	String token = getTokenFromRequest(request);
	if (token == null) {
	    return;
	}

	HashMap forwards = getForwards(session);
	if (forwards == null) {
	    forwards = new HashMap();
	}

	Long timestamp = new Long((new Date()).getTime());
	Forward forward = new Forward();
	forward.setActionForward(actionForward);
	forward.setTimestamp(timestamp);

	forwards.put(token, forward);
	setForwards(session, forwards);
    }

    /**
     * Returns the ActionForward that was saved for this token. For use with
     * auto-recovery.
     */
    public synchronized ActionForward getForward(javax.servlet.http.HttpServletRequest request) {
	return getForward(request, false);
    }

    /**
     * Returns the ActionForward that was saved for this token. For use with
     * auto-recovery.
     */
    public synchronized ActionForward getForward(javax.servlet.http.HttpServletRequest request, boolean reset) {
	HttpSession session = request.getSession(false);
	if (session == null) {
	    return null;
	}

	String token = getTokenFromRequest(request);
	if (token == null) {
	    return null;
	}

	HashMap forwards = getForwards(session);
	if (forwards == null) {
	    return null;
	}

	Forward forward = (Forward) forwards.get(token);
	if (forward == null) {
	    return null;
	}

	if (reset) {
	    forwards.remove(forward);
	}

	return forward.getActionForward();
    }

    /**
     * Holds an ActionForward and the timestamp it was saved. For use with
     * auto-recovery.
     */
    private class Forward {
	private ActionForward actionForward;
	private Long timestamp;

	public ActionForward getActionForward() {
	    return actionForward;
	}

	public void setActionForward(ActionForward actionForward) {
	    this.actionForward = actionForward;
	}

	public Long getTimestamp() {
	    return timestamp;
	}

	public void setTimestamp(Long timestamp) {
	    this.timestamp = timestamp;
	}
    }

}

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

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.web.util.NestedServletException;

/**
 * This filter must set before <code>org.lamsfoundation.lams.web.filter.LocaleFilter</code> in web.xml
 * because LocaleFilter need get value from SystemSession.
 *
 * @author Steve.Ni
 */
public class SystemSessionFilter implements Filter {
    private static Logger log = Logger.getLogger(SystemSessionFilter.class.getName());

    private static final String CONTEXT_ERROR_PAGE = "/error.jsp";

    @Override
    public void init(FilterConfig config) throws ServletException {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
	    throws IOException, ServletException {
	// Skip non-http request/response
	if (!(request instanceof HttpServletRequest && response instanceof HttpServletResponse)) {
	    chain.doFilter(request, response);
	    return;
	}

	HttpServletRequest httpRequest = (HttpServletRequest) request;
	String requestURI = httpRequest.getRequestURI();
	if (requestURI.endsWith(CONTEXT_ERROR_PAGE)) {
	    // do not create a session for displaying error page
	    chain.doFilter(request, response);
	    return;
	}
	
	HttpSession session = null;
	try {
	    session = SessionManager.startSession(httpRequest);
	    // do following part of chain
	    chain.doFilter(request, response);
	} catch (NestedServletException e) {
	    if (e.getCause() instanceof IllegalStateException && session != null) {
		// There seems to be a problem with Infinispan session invalidation.
		// Until we upgrade WildFly we need to keep these safety measures.
		String sessionId = session.getId();
		log.warn("Session " + sessionId + " was already invalidated");
		SessionManager.removeSessionByID(sessionId, false, true);
	    } else {
		throw e;
	    }
	} catch (IllegalArgumentException e) {
	    log.warn("Error while creating session: " + e.getMessage());
	} finally {
	    SessionManager.endSession();
	}
    }

    @Override
    public void destroy() {
	// do nothing
    }
}
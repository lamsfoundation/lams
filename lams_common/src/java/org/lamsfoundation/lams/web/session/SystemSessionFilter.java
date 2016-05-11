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

/**
 *
 * @author Steve.Ni Create and manage system wide (across multiple webapps in JBOSS) session.
 *
 *         <p>
 *         <b>NOTICE: This filter must set before <code>org.lamsfoundation.lams.web.filter.LocaleFilter</code> in
 *         web.xml
 *         because LocaleFilter need get value from SystemSession .</b>
 *
 * @version $Revision$
 */
public class SystemSessionFilter implements Filter {

    // The session name to trace shared session
    public static final String SYS_SESSION_COOKIE = "JSESSIONID";

    public static final String SSO_SESSION_COOKIE = "JSESSIONIDSSO";

    @Override
    public void init(FilterConfig config) throws ServletException {
    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
	    throws IOException, ServletException {

	// Skip non-http request/response
	if (!(req instanceof HttpServletRequest && res instanceof HttpServletResponse)) {
	    chain.doFilter(req, res);
	    return;
	}

	SessionManager.startSession(req, res);

	// do following part of chain
	chain.doFilter(req, res);

	SessionManager.endSession();

    }

    @Override
    public void destroy() {
	// do nothing
    }
}

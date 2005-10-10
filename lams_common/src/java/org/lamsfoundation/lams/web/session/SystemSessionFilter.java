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
 * @author Steve.Ni
 * 
 * @version $Revision$
 */
public class SystemSessionFilter implements Filter {
	
	//The session name to trace shared session
	public static final String SYS_SESSION_COOKIE = "JSESSIONID";

	public void init(FilterConfig config) throws ServletException {
	}

	public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) 
				throws IOException, ServletException {

		// Skip non-http request/response
		if (!((req instanceof HttpServletRequest) && (res instanceof HttpServletResponse))){
			chain.doFilter(req, res);
			return;
		}
		
		System.out.println(req.getServerName());
		System.out.println(((HttpServletRequest)req).getRequestURI());
		
		SessionManager.startSession(req, res);
		
		//do following part of chain
		chain.doFilter(req,res);
		
		SessionManager.endSession();
		
	}

	public void destroy() {
		//do nothing
	}
}

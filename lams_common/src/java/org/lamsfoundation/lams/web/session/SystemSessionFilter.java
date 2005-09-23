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
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.hibernate.id.UUIDHexGenerator;

/**
 * 
 * @author Steve.Ni
 * 
 * $version$
 */
public class SystemSessionFilter implements Filter {
	
	/** The name of the cookie we use to keep sakai session. */
	public static final String SYS_SESSION_COOKIE = "SYSSESSIONID";

	public void init(FilterConfig config) throws ServletException {
	}

	public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) 
				throws IOException, ServletException {

		// Skip non-http request/response
		if (!((req instanceof HttpServletRequest) && (res instanceof HttpServletResponse))){
			chain.doFilter(req, res);
			return;
		}
		
		Cookie cookie = findCookie((HttpServletRequest) req,SYS_SESSION_COOKIE);
		String currentSessionId = null;
		if(cookie != null){
			currentSessionId = cookie.getValue();
			Object obj = SessionManager.getSession(currentSessionId);
			//if cookie exist, but session does not. This usually menas seesion expired. 
			//then delete the cookie first and set it null in order to create a new one
			if(obj == null){
				removeCookie((HttpServletResponse) res,SYS_SESSION_COOKIE);
				cookie = null;
			}
		}
		//can not be in else!
		if(cookie == null){
			//create new session and set it into cookie
			currentSessionId = (String) new UUIDHexGenerator().generate(null,null);
			SessionManager.createSession(currentSessionId);
			cookie = createCookie((HttpServletResponse) res,SYS_SESSION_COOKIE,currentSessionId);
		}
		
		SessionManager.setCurrentSessionId(currentSessionId);
		//reset session last access time
		SessionVisitor sessionVisitor = SessionManager.getSessionVisitor();
		sessionVisitor.accessed();
		
		//do following part of chain
		chain.doFilter(req,res);
		
		SessionManager.setCurrentSessionId(null);
		
	}

	public void destroy() {
		//do nothing
	}

	/**
	 * Find a cookie by given cookie name from request.
	 * 
	 * @param req
	 * @param name The cookie name
	 * @return The cookie of this name in the request, or null if not found.
	 */
	private Cookie findCookie(HttpServletRequest req, String name)
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
	private Cookie removeCookie(HttpServletResponse res, String name){
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
	private Cookie createCookie(HttpServletResponse res, String name, String value){
		Cookie cookie = new Cookie(name, value);
		cookie.setPath("/");
		cookie.setMaxAge(-1);
		res.addCookie(cookie);
		
		return cookie;
	}
}

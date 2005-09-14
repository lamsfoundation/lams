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
package org.lamsfoundation.lams.web;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

import org.apache.log4j.Logger;
/**
 * 
 * @author Steve.Ni
 * 
 * $version$
 */
/* Should come out in web.xml in LAMS root web applicaton as:
 * <!-- Listeners -->
 *	<listener>
 *		<listener-class>
 *		  org.lamsfoundation.lams.web.SharedSession
 *		</listener-class>
 *	</listener>
 */
public class SharedSession  implements HttpSessionListener{

	private static Logger log = Logger.getLogger(SharedSession.class);
//	TODO: hardcode for lams root context
	private static String ROOT_CONTEXT = "/lams"; 
	
	private ServletContext context;
	private static final String SHARE_SESSION_NAME = "LAMS_SHARED_SESSION";
	/**
	 * This construct method is just for HttpSessionListener. To get an instance of
	 * this class,  use <code>getInstance(ServletContext)</code> method.
	 *
	 */
	public SharedSession(){
		
	}
	private SharedSession(ServletContext context){
		this.context = context;
	}
	/**
	 * Set value with the given sessionId into whole system scope <code>HttpSession</code>.
	 * 
	 * @param sessionId
	 * @param value
	 */
	public void setAttribute(String sessionId, Object value){
		Object map = context.getAttribute(SHARE_SESSION_NAME);
		if(map == null || !(map instanceof HashMap))
			map = new HashMap();
		
		Map sessionMap = (Map) map;
		sessionMap.put(sessionId,value);
		context.setAttribute(SHARE_SESSION_NAME,sessionMap);
	}
	/**
	 * Get value by the given sessionId from whole system scope <code>HttpSession</code>.
	 * @param sessionId
	 * @return
	 */
	public Object getAttribute(String sessionId){
		Object map = context.getAttribute(SHARE_SESSION_NAME);
		if(map == null || !(map instanceof HashMap))
			return null;
		
		Map sessionMap = (Map) map;
		return sessionMap.get(sessionId);
		
	}
	/**
	 * Get <code>SharedSession</code> instance by given <code>SevletContext</code>
	 * @param context
	 * @return
	 */
	public static SharedSession getInstance(ServletContext context){
		
		SharedSession ss = new SharedSession(context.getContext(ROOT_CONTEXT));
		if(ss.context == null){
			log.error("Failed in retrieving lams core context.");
			return null;
		}
		return ss;
	}
	public void sessionCreated(HttpSessionEvent event) {
	}
	
	/**
	 * To ensure destroy shared session simultaneously with true HttpSession variables. 
	 */
	public void sessionDestroyed(HttpSessionEvent event) {
		if(event == null)
			return;
		
		ServletContext context = event.getSession().getServletContext();
		//to ensure this context is root context
		context = context.getContext(ROOT_CONTEXT);
		context.setAttribute(SHARE_SESSION_NAME,null);
	}
}

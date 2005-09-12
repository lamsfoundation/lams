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

import org.apache.log4j.Logger;
/**
 * 
 * @author Steve.Ni
 * 
 * $version$
 */
public class SharedSession   {

	private static Logger log = Logger.getLogger(SharedSession.class);
	private ServletContext context;
	private static final String SHARE_SESSION_NAME = "name";
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
		SharedSession ss = new SharedSession(context.getContext("/lams"));
		if(ss.context == null){
			log.error("Failed in retrieving lams core context.");
			return null;
		}
		return ss;
	}
}

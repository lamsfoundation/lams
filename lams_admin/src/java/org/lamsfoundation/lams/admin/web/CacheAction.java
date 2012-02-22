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
/* $CacheAction.java,v 1.3 2006/04/03 23:20:58 fmalikoff Exp$ */
package org.lamsfoundation.lams.admin.web;

import java.io.IOException;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.lamsfoundation.lams.cache.CacheManager;
import org.lamsfoundation.lams.cache.ICacheManager;
import org.lamsfoundation.lams.util.WebUtil;
import org.lamsfoundation.lams.web.action.LamsDispatchAction;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;


/**
 * This is an action where all lams client environments launch.
 * Initial configuration of the individual environment setting is done here.
 * 
 * @struts:action name="CacheActionForm"
 * 				  path="/cache"
 * 				  parameter="method"
 * 				  validate="false"
 * @struts:action-forward name="cache" path=".cache"
 *
 */
public class CacheAction extends LamsDispatchAction {
	
	public static final String CACHE_ENTRIES = "cache";
	public static final String NODE_KEY = "node";
	
	private static Logger log = Logger.getLogger(CacheAction.class);
	private static ICacheManager cacheManager;
    /**
	 * request for sysadmin environment
	 */
	public ActionForward unspecified(ActionMapping mapping, ActionForm form, 
			HttpServletRequest req, HttpServletResponse res)
			throws IOException, ServletException {

		try {
			if (log.isDebugEnabled()) {
			    log.debug("Cache lookup");
			}
			
			// todo restrict access to admin only. Can't do at present as don't know orgID
			log.warn("CacheAction should be restricted to admin only. No check being done. Please implement.");
			/*String login = req.getRemoteUser();
			int orgId = new Integer(req.getParameter("orgId")).intValue();
			
			if ( isUserInRole(login,orgId,Role.ADMIN))
			{
				log.debug("user is admin");
				Organisation org = service.getOrganisationById(new Integer(orgId));
				AdminPreparer.prepare(org,req,service);
				return mapping.findForward("admin");
			}
			else
			{
				log.error("User "+login+" tried to get cache admin screen but isn't admin in organisation: "+orgId);
				return mapping.findForward("error");
			} */
			
			Set<String> items = getCacheManager().getCachedClasses();
			req.setAttribute(CACHE_ENTRIES, items);
			return mapping.findForward("cache");
			
		} catch (Exception e) {
			e.printStackTrace();
			return mapping.findForward("error");
		}
	}
		
	/**
	 * request for sysadmin environment
	 */
	public ActionForward remove(ActionMapping mapping, ActionForm form, 
			HttpServletRequest req, HttpServletResponse res)
			throws IOException, ServletException {

		try {
		    	// todo restrict access to admin only. Can't do at present as don't know orgID
		    
			if (log.isDebugEnabled()) {
			    log.debug("Remove entity from cache");
			}
		    	String node = WebUtil.readStrParam(req, NODE_KEY);

		    	// if node = ALL, remove all cache
			getCacheManager().clearCachedClass(node.equalsIgnoreCase("ALL") ? null : node);
			
			// so we know what entity has been removed
			req.setAttribute(NODE_KEY, node);
			
			// display the list again
			return unspecified(mapping,form,req,res);
			
		} catch (Exception e) {
			e.printStackTrace();
			return mapping.findForward("error");
		}
	}
	
	private ICacheManager getCacheManager(){
		if(cacheManager==null){
			WebApplicationContext ctx = WebApplicationContextUtils.getRequiredWebApplicationContext(getServlet().getServletContext());
			cacheManager = (CacheManager) ctx.getBean("cacheManager");
		}
		return cacheManager;
	}

}
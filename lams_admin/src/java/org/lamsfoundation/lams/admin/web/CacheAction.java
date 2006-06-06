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
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307
 * USA
 * 
 * http://www.gnu.org/licenses/gpl.txt
 * ****************************************************************
 */
/* $CacheAction.java,v 1.3 2006/04/03 23:20:58 fmalikoff Exp$ */
package org.lamsfoundation.lams.admin.web;

import java.io.IOException;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.lamsfoundation.lams.cache.CacheManager;
import org.lamsfoundation.lams.util.WebUtil;
import org.lamsfoundation.lams.web.action.LamsDispatchAction;
import org.lamsfoundation.lams.web.util.HttpSessionManager;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;


/**
 * this is an action where all lams client environments launch.
 * initial configuration of the individual environment setting is done here.
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
	private static WebApplicationContext ctx = WebApplicationContextUtils.getWebApplicationContext(HttpSessionManager.getInstance().getServletContext());
	private static CacheManager manager = (CacheManager) ctx.getBean("cacheManager");
	//private static UserManagementService service = (UserManagementService) ctx.getBean("userManagementServiceTarget");
	
	/*
	private boolean isUserInRole(String login,int orgId, String roleName)
	{
		if (service.getUserOrganisationRole(login, new Integer(orgId),roleName)==null)
			return false;
		return true;
	} */
	
    /**
	 * request for sysadmin environment
	 */
	public ActionForward unspecified(ActionMapping mapping, ActionForm form, 
			HttpServletRequest req, HttpServletResponse res)
			throws IOException, ServletException {

		try {
			
			log.debug("cache lookup");

			// todo restrict access to admin only. Can't do at present as don't know orgID
			log.error("CacheAction should be restricted to admin only. No check being done. Please implement.");
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
			
			Map items = manager.getCachedItems();
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
			
			log.debug("remove");

			// todo restrict access to admin only. Can't do at present as don't know orgID
			log.error("CacheAction should be restricted to admin only. No check being done. Please implement.");
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

			String node = WebUtil.readStrParam(req, NODE_KEY, false);
			manager.clearCache(node);
			
			Map items = manager.getCachedItems();
			req.setAttribute(CACHE_ENTRIES, items);
			return mapping.findForward("cache");
			
		} catch (Exception e) {
			e.printStackTrace();
			return mapping.findForward("error");
		}
	}
}
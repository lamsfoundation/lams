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
package org.lamsfoundation.lams.web;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.lamsfoundation.lams.usermanagement.Role;
import org.lamsfoundation.lams.usermanagement.User;
import org.lamsfoundation.lams.usermanagement.service.IUserManagementService;
import org.lamsfoundation.lams.util.Configuration;
import org.lamsfoundation.lams.util.ConfigurationKeys;
import org.lamsfoundation.lams.util.WebUtil;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

/**
 * @version
 * 
 * <p>
 * <a href="IndexAction.java.html"><i>View Source</i></a>
 * </p>
 * 
 * @author <a href="mailto:fyang@melcoe.mq.edu.au">Fei Yang</a>
 * 
 * Created at 16:59:28 on 13/06/2006
 */
/**
 * struts doclet
 * 
 * @struts.action path="/index" validate="false"
 * 
 * @struts.action-forward name="main" path="/main.jsp"
 * @struts.action-forward name="community" path=".community"
 * @struts.action-forward name="profile" path="/profile.do?method=view"
 * @struts.action-forward name="editprofile" path="/profile.do?method=edit"
 * @struts.action-forward name="password" path="/password.do"
 * @struts.action-forward name="passwordChanged" path=".passwordChangeOk"
 * @struts.action-forward name="portrait" path="/portrait.do"
 * @struts.action-forward name="lessons" path="/profile.do?method=lessons"
 */
public class IndexAction extends Action {

	private static Logger log = Logger.getLogger(IndexAction.class);
	private static IUserManagementService service;
	
	@SuppressWarnings("unchecked")
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		setHeaderLinks(request);
		setAdminLinks(request);
		
		// check if user is flagged as needing to change their password
		User loggedInUser = getService().getUserByLogin(request.getRemoteUser());
		if (loggedInUser.getChangePassword()!=null) {
			if (loggedInUser.getChangePassword()) {
				return mapping.findForward("password");
			}
		}
		
		String tab = WebUtil.readStrParam(request, "tab", true);
		if (StringUtils.equals(tab, "profile")) {
			List courseIds = getService().getArchivedCourseIdsByUser(loggedInUser.getUserId(), request.isUserInRole(Role.SYSADMIN));
			request.setAttribute("courseIds", courseIds);
			return mapping.findForward("profile");
		} else if (StringUtils.equals(tab, "editprofile")) {
			return mapping.findForward("editprofile");
		} else if (StringUtils.equals(tab, "password")) {
			return mapping.findForward("password");
		} else if (StringUtils.equals(tab, "passwordChanged")) {
			request.setAttribute("tab", "profile");
			return mapping.findForward("passwordChanged");
		} else if (StringUtils.equals(tab, "portrait")) {
			return mapping.findForward("portrait");
		} else if (StringUtils.equals(tab, "lessons")) {
			return mapping.findForward("lessons");
		} else if (StringUtils.equals(tab, "community")) {
			request.setAttribute("tab", tab);
			return mapping.findForward("community");
		}
		
		List courseIds = getService().getActiveCourseIdsByUser(loggedInUser.getUserId(), request.isUserInRole(Role.SYSADMIN));
		request.setAttribute("courseIds", courseIds);
		
		return mapping.findForward("main");
	}
	
	private void setHeaderLinks(HttpServletRequest request) {
		List<IndexLinkBean> headerLinks = new ArrayList<IndexLinkBean>();
		if (request.isUserInRole(Role.AUTHOR) || request.isUserInRole(Role.AUTHOR_ADMIN)) {
			log.debug("user is author");
			headerLinks.add(new IndexLinkBean("index.author", "javascript:openAuthor()"));
		}
		headerLinks.add(new IndexLinkBean("index.myprofile", "index.do?tab=profile"));
		
		if(Configuration.getAsBoolean(ConfigurationKeys.LAMS_COMMUNITY_ENABLE))
			if (request.isUserInRole(Role.SYSADMIN) || request.isUserInRole(Role.GROUP_ADMIN) || request.isUserInRole(Role.GROUP_MANAGER) || 
				request.isUserInRole(Role.AUTHOR) || request.isUserInRole(Role.AUTHOR_ADMIN) || request.isUserInRole(Role.MONITOR))
				headerLinks.add(new IndexLinkBean("index.community", "index.do?tab=community"));
		
		log.debug("set headerLinks in request");
		request.setAttribute("headerLinks", headerLinks);
	}
	
	private void setAdminLinks(HttpServletRequest request) {
		List<IndexLinkBean> adminLinks = new ArrayList<IndexLinkBean>();
		if (request.isUserInRole(Role.SYSADMIN) || request.isUserInRole(Role.GROUP_ADMIN) || request.isUserInRole(Role.GROUP_MANAGER))
			adminLinks.add(new IndexLinkBean("index.courseman", "javascript:openOrgManagement(" + getService().getRootOrganisation().getOrganisationId()+')'));
		if (request.isUserInRole(Role.SYSADMIN) || request.isUserInRole(Role.AUTHOR_ADMIN) || getService().isUserGlobalGroupAdmin())
			adminLinks.add(new IndexLinkBean("index.sysadmin", "javascript:openSysadmin()"));
		request.setAttribute("adminLinks", adminLinks);
	}
	
	private IUserManagementService getService(){
		if(service==null){
			WebApplicationContext ctx = WebApplicationContextUtils.getRequiredWebApplicationContext(getServlet().getServletContext());
			service = (IUserManagementService) ctx.getBean("userManagementService");
		}
		return service;
	}
}

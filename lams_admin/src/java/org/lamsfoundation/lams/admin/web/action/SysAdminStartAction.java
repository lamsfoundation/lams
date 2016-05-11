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
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 * USA
 *
 * http://www.gnu.org/licenses/gpl.txt
 * ****************************************************************
 */


package org.lamsfoundation.lams.admin.web.action;

import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.lamsfoundation.lams.admin.service.AdminServiceProxy;
import org.lamsfoundation.lams.admin.web.dto.LinkBean;
import org.lamsfoundation.lams.openid.OpenIDConfig;
import org.lamsfoundation.lams.usermanagement.Role;
import org.lamsfoundation.lams.usermanagement.service.IUserManagementService;

/**
 * @author jliew
 *
 * @struts.action path="/sysadminstart" validate="false"
 * @struts.action-forward name="sysadmin" path=".sysadmin"
 */
public class SysAdminStartAction extends Action {

    private static IUserManagementService service;

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {

	service = AdminServiceProxy.getService(getServlet().getServletContext());

	ArrayList<LinkBean> links = new ArrayList<LinkBean>();
	if (request.isUserInRole(Role.SYSADMIN)) {
	    LinkBean linkBean = new LinkBean("cache.do", "cache.title");
	    links.add(linkBean);
	    links.add(new LinkBean("cleanup.do", "sysadmin.batch.temp.file.delete"));
	    links.add(new LinkBean("config.do", "sysadmin.config.settings.edit"));
	    links.add(new LinkBean("toolcontentlist.do", "sysadmin.tool.management"));
	    links.add(new LinkBean("usersearch.do", "admin.user.find"));
	    links.add(new LinkBean("importgroups.do", "sysadmin.import.groups.title"));
	    links.add(new LinkBean("importexcel.do", "admin.user.import"));
	    links.add(new LinkBean("ldap.do", "sysadmin.ldap.configuration"));
	    links.add(new LinkBean("disabledmanage.do", "admin.list.disabled.users"));
	    links.add(new LinkBean("loginmaintain.do", "sysadmin.maintain.loginpage"));
	    links.add(new LinkBean("serverlist.do", "sysadmin.maintain.external.servers"));
	    links.add(new LinkBean("register.do", "sysadmin.register.server"));
	    links.add(new LinkBean("statistics.do", "admin.statistics.title"));
	    links.add(new LinkBean("signupManagement.do", "admin.signup.title"));
	    links.add(new LinkBean("themeManagement.do", "admin.themes.title"));
	    links.add(new LinkBean("timezonemanagement.do", "admin.timezone.title"));

	    OpenIDConfig openIDEnabled = (OpenIDConfig) service.findById(OpenIDConfig.class, OpenIDConfig.KEY_ENABLED);
	    if (openIDEnabled != null && Boolean.parseBoolean(openIDEnabled.getConfigValue()) == Boolean.TRUE) {
		links.add(new LinkBean("openIDConfig.do", "admin.openid.title"));
	    }
	} else if (service.isUserGlobalGroupAdmin()) {
	    LinkBean linkBean = new LinkBean("usersearch.do", "admin.user.find");
	    links.add(linkBean);
	    links.add(new LinkBean("importgroups.do", "sysadmin.import.groups.title"));
	    links.add(new LinkBean("importexcel.do", "admin.user.import"));
	    links.add(new LinkBean("disabledmanage.do", "admin.list.disabled.users"));
	} else {
	    request.setAttribute("errorName", "SysAdminStartAction");
	    request.setAttribute("errorMessage", AdminServiceProxy.getMessageService(getServlet().getServletContext())
		    .getMessage("error.authorisation"));
	    return mapping.findForward("error");
	}

	request.setAttribute("links", links);
	return mapping.findForward("sysadmin");
    }

}

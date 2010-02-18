/****************************************************************
 * Copyright (C) 2006 LAMS Foundation (http://lamsfoundation.org)
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
package org.lamsfoundation.lams.admin.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.lamsfoundation.lams.admin.service.AdminServiceProxy;
import org.lamsfoundation.lams.openid.OpenIDConfig;
import org.lamsfoundation.lams.usermanagement.service.IUserManagementService;
import org.lamsfoundation.lams.web.action.LamsDispatchAction;

/**
 * openIDConfig
 * 
 * @author lfoxton
 * 
 * @struts.action path="/openIDConfig" parameter="method" name="openIDForm" scope="request" validate="false"
 * @struts.action-forward name="config" path="/openidConfig.jsp"
 * @struts.action-forward name="sysadmin" path="/sysadminstart.do"
 */
public class OpenIDConfigAction extends LamsDispatchAction {
	
	IUserManagementService userService;

	public ActionForward unspecified(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		setService();
		OpenIDConfigForm configForm = (OpenIDConfigForm) form;

		OpenIDConfig openIDEnabled = (OpenIDConfig)userService.findById(OpenIDConfig.class, OpenIDConfig.KEY_ENABLED);
		OpenIDConfig portalURL = (OpenIDConfig)userService.findById(OpenIDConfig.class, OpenIDConfig.KEY_PORTAL_URL);
		OpenIDConfig trustedIDPs = (OpenIDConfig)userService.findById(OpenIDConfig.class,OpenIDConfig.KEY_TRUSTED_IDPS);

		if (openIDEnabled != null) {
			configForm.setOpenIDEnabled(Boolean.parseBoolean(openIDEnabled.getConfigValue()));
		} else {
			configForm.setOpenIDEnabled(Boolean.FALSE);
		}

		if (portalURL != null) {
			configForm.setPortalURL(portalURL.getConfigValue());
		}

		if (trustedIDPs != null) {
			configForm.setTrustedIDPs(trustedIDPs.getConfigValue());
		}

		return mapping.findForward("config");
	}

	public ActionForward save(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		setService();
		OpenIDConfigForm configForm = (OpenIDConfigForm) form;
		
		OpenIDConfig openIDEnabled = (OpenIDConfig)userService.findById(OpenIDConfig.class, OpenIDConfig.KEY_ENABLED);
		OpenIDConfig portalURL = (OpenIDConfig)userService.findById(OpenIDConfig.class, OpenIDConfig.KEY_PORTAL_URL);
		OpenIDConfig trustedIDPs = (OpenIDConfig)userService.findById(OpenIDConfig.class,OpenIDConfig.KEY_TRUSTED_IDPS);

		if (openIDEnabled != null) {
			openIDEnabled.setConfigValue(configForm.getOpenIDEnabled().toString());
		}
		
		if (portalURL != null) {
			portalURL.setConfigValue(configForm.getPortalURL());
		}
		
		if (trustedIDPs != null) {
			trustedIDPs.setConfigValue(configForm.getTrustedIDPs());
		}
		
		userService.save(openIDEnabled);
		userService.save(portalURL);
		userService.save(trustedIDPs);
		
		request.setAttribute("success", true);
		
		return mapping.findForward("config");
	}

	private void setService() {
		if (userService == null) {
			userService = AdminServiceProxy.getService(getServlet().getServletContext());
		}
	}

}

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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.action.DynaActionForm;
import org.lamsfoundation.lams.admin.service.AdminServiceProxy;
import org.lamsfoundation.lams.integration.ExtServerOrgMap;
import org.lamsfoundation.lams.integration.service.IIntegrationService;
import org.lamsfoundation.lams.usermanagement.Organisation;
import org.lamsfoundation.lams.usermanagement.OrganisationState;
import org.lamsfoundation.lams.usermanagement.OrganisationType;
import org.lamsfoundation.lams.usermanagement.SupportedLocale;
import org.lamsfoundation.lams.usermanagement.dto.UserDTO;
import org.lamsfoundation.lams.usermanagement.service.IUserManagementService;
import org.lamsfoundation.lams.util.LanguageUtil;
import org.lamsfoundation.lams.util.MessageService;
import org.lamsfoundation.lams.util.ValidationUtil;
import org.lamsfoundation.lams.web.session.SessionManager;
import org.lamsfoundation.lams.web.util.AttributeNames;

/**
 * <p>
 * <a href="ServerSaveAction.java.html"><i>View Source</i><a>
 * </p>
 *
 * @author <a href="mailto:fyang@melcoe.mq.edu.au">Fei Yang</a>
 *
 * @struts.action path="/serversave" input=".servermaintain" name="ServerOrgMapForm" scope="request" validate="false"
 * @struts.action-forward name="success" path="/serverlist.do"
 */
public class ServerSaveAction extends Action {

    private static IIntegrationService service;
    private static IUserManagementService userService;
    private static MessageService messageService;

    @Override
    @SuppressWarnings("unchecked")
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {

	if (isCancelled(request)) {
	    return mapping.findForward("success");
	}

	service = AdminServiceProxy.getIntegrationService(getServlet().getServletContext());
	userService = AdminServiceProxy.getService(getServlet().getServletContext());
	messageService = AdminServiceProxy.getMessageService(getServlet().getServletContext());

	DynaActionForm serverOrgMapForm = (DynaActionForm) form;
	ActionMessages errors = new ActionMessages();
	String[] requiredFields = { "serverid", "serverkey", "servername", "prefix", "userinfoUrl", "timeoutUrl" };
	for (String requiredField : requiredFields) {
	    if (StringUtils.trimToNull(serverOrgMapForm.getString(requiredField)) == null) {
		errors.add(requiredField,
			new ActionMessage("error.required", messageService.getMessage("sysadmin." + requiredField)));
	    }
	}
	Organisation org = null;
	UserDTO user = (UserDTO) SessionManager.getSession().getAttribute(AttributeNames.USER);
	if ((Boolean) serverOrgMapForm.get("newOrg")) {

	    String orgName = serverOrgMapForm.getString("orgName");
	    if (StringUtils.trimToNull(orgName) == null) {
		errors.add("orgId",
			new ActionMessage("error.required", messageService.getMessage("sysadmin.organisation")));

	    } else if (!ValidationUtil.isOrgNameValid(orgName)) {
		errors.add("orgId", new ActionMessage("error.name.invalid.characters"));

	    } else {

		org = new Organisation();
		org.setName(orgName);
		org.setParentOrganisation(userService.getRootOrganisation());
		org.setOrganisationType(
			(OrganisationType) userService.findById(OrganisationType.class, OrganisationType.COURSE_TYPE));
		org.setOrganisationState(
			(OrganisationState) userService.findById(OrganisationState.class, OrganisationState.ACTIVE));
		SupportedLocale locale = LanguageUtil.getDefaultLocale();
		org.setLocale(locale);
		userService.saveOrganisation(org, user.getUserID());
		serverOrgMapForm.set("orgId", org.getOrganisationId());
		serverOrgMapForm.set("newOrg", false);
		serverOrgMapForm.set("orgName", null);
	    }

	} else {
	    Integer orgId = (Integer) serverOrgMapForm.get("orgId");
	    if (orgId.equals(-1)) {
		// LDEV-1284 no need for integration organisation anymore
		// errors.add("orgId",new ActionMessage("error.required", messageService.getMessage("sysadmin.organisation")));
	    } else {
		org = (Organisation) userService.findById(Organisation.class, orgId);
	    }
	}
	Integer sid = (Integer) serverOrgMapForm.get("sid");
	if (errors.isEmpty()) {//check duplication
	    String[] uniqueFields = { "serverid", "prefix" };
	    for (String uniqueField : uniqueFields) {
		List list = userService.findByProperty(ExtServerOrgMap.class, uniqueField,
			serverOrgMapForm.get(uniqueField));
		if (list != null && list.size() > 0) {
		    if (sid.equals(-1)) {//new map
			errors.add(uniqueField, new ActionMessage("error.not.unique",
				messageService.getMessage("sysadmin." + uniqueField)));
		    } else {
			ExtServerOrgMap map = (ExtServerOrgMap) list.get(0);
			if (!map.getSid().equals(sid)) {
			    errors.add(uniqueField, new ActionMessage("error.not.unique",
				    messageService.getMessage("sysadmin." + uniqueField)));
			}
		    }

		}
	    }
	}
	if (errors.isEmpty()) {
	    ExtServerOrgMap map = null;
	    if (sid.equals(-1)) {
		map = new ExtServerOrgMap();
		BeanUtils.copyProperties(map, serverOrgMapForm);
		map.setSid(null);
		map.setServerTypeId(ExtServerOrgMap.LTI_CONSUMER_SERVER_TYPE);
	    } else {
		map = service.getExtServerOrgMap(sid);
		BeanUtils.copyProperties(map, serverOrgMapForm);
	    }
	    map.setOrganisation(org);
	    service.saveExtServerOrgMap(map);
	    return mapping.findForward("success");
	} else {
	    saveErrors(request, errors);
	    Map<String, Object> properties = new HashMap<String, Object>();
	    properties.put("organisationType.organisationTypeId", OrganisationType.COURSE_TYPE);
	    properties.put("organisationState.organisationStateId", OrganisationState.ACTIVE);
	    List list = userService.findByProperties(Organisation.class, properties);
	    Organisation dummy = new Organisation();
	    dummy.setOrganisationId(-1);
	    dummy.setName(messageService.getMessage("sysadmin.organisation.select"));
	    if (list == null) {
		list = new ArrayList();
	    }
	    list.add(dummy);
	    request.setAttribute("orgs", list);
	    return mapping.getInputForward();
	}
    }
}

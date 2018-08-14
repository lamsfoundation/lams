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
package org.lamsfoundation.lams.admin.web.controller;

import java.util.List;

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
import org.lamsfoundation.lams.integration.ExtServer;
import org.lamsfoundation.lams.integration.service.IIntegrationService;
import org.lamsfoundation.lams.usermanagement.service.IUserManagementService;
import org.lamsfoundation.lams.util.MessageService;

/**
 * <p>
 * <a href="ServerSaveAction.java.html"><i>View Source</i><a>
 * </p>
 *
 * @author <a href="mailto:fyang@melcoe.mq.edu.au">Fei Yang</a>
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

	DynaActionForm extServerForm = (DynaActionForm) form;
	ActionMessages errors = new ActionMessages();
	String[] requiredFields = { "serverid", "serverkey", "servername", "prefix", "userinfoUrl" };
	for (String requiredField : requiredFields) {
	    if (StringUtils.trimToNull(extServerForm.getString(requiredField)) == null) {
		errors.add(requiredField,
			new ActionMessage("error.required", messageService.getMessage("sysadmin." + requiredField)));
	    }
	}

	Integer sid = (Integer) extServerForm.get("sid");
	if (errors.isEmpty()) {//check duplication
	    String[] uniqueFields = { "serverid", "prefix" };
	    for (String uniqueField : uniqueFields) {
		List list = userService.findByProperty(ExtServer.class, uniqueField,
			extServerForm.get(uniqueField));
		if (list != null && list.size() > 0) {
		    if (sid.equals(-1)) {//new map
			errors.add(uniqueField, new ActionMessage("error.not.unique",
				messageService.getMessage("sysadmin." + uniqueField)));
		    } else {
			ExtServer map = (ExtServer) list.get(0);
			if (!map.getSid().equals(sid)) {
			    errors.add(uniqueField, new ActionMessage("error.not.unique",
				    messageService.getMessage("sysadmin." + uniqueField)));
			}
		    }

		}
	    }
	}
	if (errors.isEmpty()) {
	    ExtServer map = null;
	    if (sid.equals(-1)) {
		map = new ExtServer();
		BeanUtils.copyProperties(map, extServerForm);
		map.setSid(null);
		map.setServerTypeId(ExtServer.INTEGRATION_SERVER_TYPE);
	    } else {
		map = service.getExtServer(sid);
		BeanUtils.copyProperties(map, extServerForm);
	    }
	    service.saveExtServer(map);
	    return mapping.findForward("success");
	} else {
	    saveErrors(request, errors);
	    return mapping.getInputForward();
	}
    }
}

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
import org.lamsfoundation.lams.admin.service.AdminServiceProxy;
import org.lamsfoundation.lams.admin.web.form.ExtServerForm;
import org.lamsfoundation.lams.integration.ExtServer;
import org.lamsfoundation.lams.integration.service.IIntegrationService;
import org.lamsfoundation.lams.usermanagement.service.IUserManagementService;
import org.lamsfoundation.lams.util.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.WebApplicationContext;

/**
 * <p>
 * <a href="ServerSaveAction.java.html"><i>View Source</i><a>
 * </p>
 *
 * @author <a href="mailto:fyang@melcoe.mq.edu.au">Fei Yang</a>
 */
@Controller
public class ServerSaveController {

    private static IIntegrationService service;
    private static IUserManagementService userService;
    private static MessageService messageService;

    @Autowired
    private WebApplicationContext applicationContext;

    @RequestMapping(path = "/serversave")
    public String execute(@ModelAttribute ExtServerForm extServerForm, Errors errors, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {

	service = AdminServiceProxy.getIntegrationService(applicationContext.getServletContext());
	userService = AdminServiceProxy.getService(applicationContext.getServletContext());
	messageService = AdminServiceProxy.getMessageService(applicationContext.getServletContext());

	String[] requiredFields = { "serverid", "serverkey", "servername", "prefix", "userinfoUrl" };
	for (String requiredField : requiredFields) {
	    if (StringUtils.trimToNull(requiredField) == null) {
		errors.reject("error.required", messageService.getMessage("sysadmin." + requiredField));
	    }
	}

	Integer sid = extServerForm.getSid();
	if (!errors.hasErrors()) {//check duplication
	    String[] uniqueFields = { "serverid", "prefix" };
	    for (String uniqueField : uniqueFields) {
		List list = userService.findByProperty(ExtServer.class, "uniqueField", uniqueField);
		if (list != null && list.size() > 0) {
		    if (sid.equals(-1)) {//new map
			errors.reject("error.not.unique", messageService.getMessage("sysadmin." + uniqueField));
		    } else {
			ExtServer map = (ExtServer) list.get(0);
			if (!map.getSid().equals(sid)) {
			    errors.reject("error.not.unique", messageService.getMessage("sysadmin." + uniqueField));
			}
		    }

		}
	    }
	}
	if (!errors.hasErrors()) {
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
	    return "forward:/serverlist.do";
	} else {
	    return "servermaintain";
	}
    }
}

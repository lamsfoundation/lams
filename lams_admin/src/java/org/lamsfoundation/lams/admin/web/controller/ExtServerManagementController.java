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

import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang.StringUtils;
import org.lamsfoundation.lams.admin.web.form.ExtServerForm;
import org.lamsfoundation.lams.integration.ExtServer;
import org.lamsfoundation.lams.integration.service.IIntegrationService;
import org.lamsfoundation.lams.usermanagement.service.IUserManagementService;
import org.lamsfoundation.lams.util.MessageService;
import org.lamsfoundation.lams.util.WebUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author <a href="mailto:fyang@melcoe.mq.edu.au">Fei Yang</a>
 */
@Controller
@RequestMapping("/extserver")
public class ExtServerManagementController {

    @Autowired
    private IIntegrationService integrationService;
    @Autowired
    private IUserManagementService userManagementService;
    @Autowired
    @Qualifier("adminMessageService")
    private MessageService messageService;
    
    @RequestMapping(path = "/serverlist")
    public String serverlist(HttpServletRequest request) throws Exception {
	List<ExtServer> extServers = integrationService.getAllExtServers();
	Collections.sort(extServers);
	request.setAttribute("servers", extServers);
	return "integration/serverlist";
    }

    @RequestMapping(path = "/edit")
    public String edit(@ModelAttribute ExtServerForm extServerForm, HttpServletRequest request) throws Exception {
	
	Integer sid = WebUtil.readIntParam(request, "sid", true);
	if (sid != null) {
	    ExtServer map = integrationService.getExtServer(sid);
	    BeanUtils.copyProperties(extServerForm, map);
	}
	return "integration/servermaintain";
    }

    @RequestMapping(path = "/serversave")
    public String serversave(@ModelAttribute ExtServerForm extServerForm, BindingResult bindingResult,
	    HttpServletRequest request, HttpServletResponse response) throws Exception {

	MultiValueMap<String, String> errorMap = new LinkedMultiValueMap<>();

	if (StringUtils.trimToNull(extServerForm.getServerid()) == null) {
	    errorMap.add("serverid", messageService.getMessage("error.required",
		    new Object[] { messageService.getMessage("sysadmin.serverid") }));
	}
	if (StringUtils.trimToNull(extServerForm.getServerkey()) == null) {
	    errorMap.add("serverkey", messageService.getMessage("error.required",
		    new Object[] { messageService.getMessage("sysadmin.serverkey") }));
	}
	if (StringUtils.trimToNull(extServerForm.getServername()) == null) {
	    errorMap.add("servername", messageService.getMessage("error.required",
		    new Object[] { messageService.getMessage("sysadmin.servername") }));
	}
	if (StringUtils.trimToNull(extServerForm.getPrefix()) == null) {
	    errorMap.add("prefix", messageService.getMessage("error.required",
		    new Object[] { messageService.getMessage("sysadmin.prefix") }));
	}
	if (StringUtils.trimToNull(extServerForm.getPrefix()) == null) {
	    errorMap.add("userinfoUrl", messageService.getMessage("error.required",
		    new Object[] { messageService.getMessage("sysadmin.userinfoUrl") }));
	}
	
	Integer sid = extServerForm.getSid();
	if (errorMap.isEmpty()) {//check duplication
	    List listServer = userManagementService.findByProperty(ExtServer.class, "serverid",
		    extServerForm.getServerid());
	    if (listServer != null && listServer.size() > 0) {
		if (sid.equals(-1)) {//new map
		    errorMap.add("serverid", messageService.getMessage("error.not.unique",
			    new Object[] { messageService.getMessage("sysadmin.serverid") }));
		} else {
		    ExtServer map = (ExtServer) listServer.get(0);
		    if (!map.getSid().equals(sid)) {
			errorMap.add("serverid", messageService.getMessage("error.not.unique",
				new Object[] { messageService.getMessage("sysadmin.serverid") }));
		    }
		}
	    }

	    List<ExtServer> listPrefix = userManagementService.findByProperty(ExtServer.class, "prefix",
		    extServerForm.getPrefix());
	    if (listPrefix != null && listPrefix.size() > 0) {
		if (sid.equals(0)) {//new map
		    errorMap.add("prefix", messageService.getMessage("error.not.unique",
			    new Object[] { messageService.getMessage("sysadmin.prefix") }));
		} else {
		    ExtServer map = (ExtServer) listPrefix.get(0);
		    if (!map.getSid().equals(sid)) {
			errorMap.add("prefix", messageService.getMessage("error.not.unique",
				new Object[] { messageService.getMessage("sysadmin.prefix") }));
		    }
		}
	    }
	}

	if (errorMap.isEmpty()) {
	    ExtServer map = null;
	    if (sid.equals(-1)) {
		map = new ExtServer();
		BeanUtils.copyProperties(map, extServerForm);
		map.setSid(null);
		map.setServerTypeId(ExtServer.INTEGRATION_SERVER_TYPE);
	    } else {
		map = integrationService.getExtServer(sid);
		BeanUtils.copyProperties(map, extServerForm);
	    }
	    integrationService.saveExtServer(map);
	    return "forward:/extserver/serverlist.do";
	} else {
	    request.setAttribute("errorMap", errorMap);
	    return "integration/servermaintain";
	}
    }

    @RequestMapping(path = "/disable")
    public String disable(HttpServletRequest request) throws Exception {
	Integer sid = WebUtil.readIntParam(request, "sid", false);
	ExtServer map = integrationService.getExtServer(sid);
	map.setDisabled(true);
	integrationService.saveExtServer(map);
	return "redirect:/extserver/serverlist.do";
    }

    @RequestMapping(path = "/enable")
    public String enable(HttpServletRequest request) throws Exception {
	Integer sid = WebUtil.readIntParam(request, "sid", false);
	ExtServer map = integrationService.getExtServer(sid);
	map.setDisabled(false);
	integrationService.saveExtServer(map);
	return "redirect:/extserver/serverlist.do";
    }

    @RequestMapping(path = "/delete")
    public String delete(HttpServletRequest request) throws Exception {
	Integer sid = WebUtil.readIntParam(request, "sid", false);
	userManagementService.deleteById(ExtServer.class, sid);
	return "redirect:/extserver/serverlist.do";
    }

}

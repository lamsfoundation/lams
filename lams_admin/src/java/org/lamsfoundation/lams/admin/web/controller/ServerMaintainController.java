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

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.beanutils.BeanUtils;
import org.lamsfoundation.lams.admin.web.form.ExtServerForm;
import org.lamsfoundation.lams.integration.ExtServer;
import org.lamsfoundation.lams.integration.service.IIntegrationService;
import org.lamsfoundation.lams.usermanagement.service.IUserManagementService;
import org.lamsfoundation.lams.util.MessageService;
import org.lamsfoundation.lams.util.WebUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author <a href="mailto:fyang@melcoe.mq.edu.au">Fei Yang</a>
 */
@Controller
@RequestMapping("/servermaintain")
public class ServerMaintainController {

    @Autowired
    private IIntegrationService integrationService;
    @Autowired
    private IUserManagementService userManagementService;
    @Autowired
    @Qualifier("adminMessageService")
    private MessageService messageService;

    @RequestMapping(path = "/edit")
    public String edit(@ModelAttribute ExtServerForm extServerForm, HttpServletRequest request) throws Exception {
	
	Integer sid = WebUtil.readIntParam(request, "sid", true);
	if (sid != null) {
	    ExtServer map = integrationService.getExtServer(sid);
	    BeanUtils.copyProperties(extServerForm, map);
	}
	return "servermaintain";
    }

    @RequestMapping(path = "/disable")
    public String disable(HttpServletRequest request) throws Exception {
	Integer sid = WebUtil.readIntParam(request, "sid", false);
	ExtServer map = integrationService.getExtServer(sid);
	map.setDisabled(true);
	integrationService.saveExtServer(map);
	return "forward:/serverlist.do";
    }

    @RequestMapping(path = "/enable")
    public String enable(HttpServletRequest request) throws Exception {
	Integer sid = WebUtil.readIntParam(request, "sid", false);
	ExtServer map = integrationService.getExtServer(sid);
	map.setDisabled(false);
	integrationService.saveExtServer(map);
	return "forward:/serverlist.do";
    }

    @RequestMapping(path = "/delete")
    public String delete(HttpServletRequest request) throws Exception {
	Integer sid = WebUtil.readIntParam(request, "sid", false);
	userManagementService.deleteById(ExtServer.class, sid);
	return "forward:/serverlist.do";
    }

}

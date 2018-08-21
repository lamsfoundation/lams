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
import org.lamsfoundation.lams.admin.service.AdminServiceProxy;
import org.lamsfoundation.lams.admin.web.form.ExtServerForm;
import org.lamsfoundation.lams.integration.ExtServer;
import org.lamsfoundation.lams.integration.service.IIntegrationService;
import org.lamsfoundation.lams.util.WebUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.context.WebApplicationContext;

/**
 * <p>
 * <a href="ServerMaintainAction.java.html"><i>View Source</i><a>
 * </p>
 *
 * @author <a href="mailto:fyang@melcoe.mq.edu.au">Fei Yang</a>
 */
@Controller
@RequestMapping("/servermaintain")
public class ServerMaintainController {

    @Autowired
    private WebApplicationContext applicationContext;

    @RequestMapping(path = "/edit")
    public String edit(@ModelAttribute ExtServerForm extServerForm, HttpServletRequest request) throws Exception {
	Integer sid = WebUtil.readIntParam(request, "sid", true);
	if (sid != null) {
	    ExtServer map = AdminServiceProxy.getIntegrationService(applicationContext.getServletContext())
		    .getExtServer(sid);
	    BeanUtils.copyProperties(extServerForm, map);
	}
	return "servermaintain";
    }

    @RequestMapping(path = "/disable")
    public String disable(HttpServletRequest request) throws Exception {
	IIntegrationService service = AdminServiceProxy.getIntegrationService(applicationContext.getServletContext());
	Integer sid = WebUtil.readIntParam(request, "sid", false);
	ExtServer map = service.getExtServer(sid);
	map.setDisabled(true);
	service.saveExtServer(map);
	return "redirect:/serverlist.do";
    }

    @RequestMapping(path = "/enable")
    public String enable(HttpServletRequest request) throws Exception {
	IIntegrationService service = AdminServiceProxy.getIntegrationService(applicationContext.getServletContext());
	Integer sid = WebUtil.readIntParam(request, "sid", false);
	ExtServer map = service.getExtServer(sid);
	map.setDisabled(false);
	service.saveExtServer(map);
	return "redirect:/serverlist.do";
    }

    @RequestMapping(path = "/delete")
    public String delete(HttpServletRequest request) throws Exception {
	Integer sid = WebUtil.readIntParam(request, "sid", false);
	AdminServiceProxy.getService(applicationContext.getServletContext()).deleteById(ExtServer.class, sid);
	return "redirect:/serverlist.do";
    }

}

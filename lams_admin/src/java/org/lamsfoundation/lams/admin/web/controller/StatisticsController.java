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

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.lamsfoundation.lams.admin.service.AdminServiceProxy;
import org.lamsfoundation.lams.statistics.dto.GroupStatisticsDTO;
import org.lamsfoundation.lams.statistics.dto.StatisticsDTO;
import org.lamsfoundation.lams.statistics.service.IStatisticsService;
import org.lamsfoundation.lams.usermanagement.Role;
import org.lamsfoundation.lams.util.WebUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.context.WebApplicationContext;

/**
 * Gives the overall statistics for a LAMS server
 *
 * @author Luke Foxton
 */
@Controller
@RequestMapping("/statistics")
public class StatisticsController {

    private static IStatisticsService statisticsService;

    @Autowired
    private WebApplicationContext applicationContext;

    @RequestMapping(path = "/start")
    public String unspecified(HttpServletRequest request) throws Exception {

	// check permission
	if (!request.isUserInRole(Role.SYSADMIN)) {
	    request.setAttribute("errorName", "StatisticsAction");
	    request.setAttribute("errorMessage", AdminServiceProxy
		    .getMessageService(applicationContext.getServletContext()).getMessage("error.authorisation"));
	    return "error";
	}

	if (statisticsService == null) {
	    statisticsService = AdminServiceProxy.getStatisticsService(applicationContext.getServletContext());
	}

	StatisticsDTO stats = statisticsService.getOverallStatistics();

	Map<String, Integer> groupMap = statisticsService.getGroupMap();

	request.setAttribute("statisticsDTO", stats);
	request.setAttribute("groupMap", groupMap);
	return "statistics";
    }

    @RequestMapping(path = "/groupStats", method = RequestMethod.POST)
    public String groupStats(HttpServletRequest request) throws Exception {

	Integer orgId = WebUtil.readIntParam(request, "orgId");

	// check permission
	if (!request.isUserInRole(Role.SYSADMIN)) {
	    request.setAttribute("errorName", "StatisticsAction");
	    request.setAttribute("errorMessage", AdminServiceProxy
		    .getMessageService(applicationContext.getServletContext()).getMessage("error.authorisation"));
	    return "error";
	}

	if (statisticsService == null) {
	    statisticsService = AdminServiceProxy.getStatisticsService(applicationContext.getServletContext());
	}

	GroupStatisticsDTO groupStats = statisticsService.getGroupStatisticsDTO(orgId);

	request.setAttribute("groupStatisticsDTO", groupStats);
	return "groupStatistics";
    }

}

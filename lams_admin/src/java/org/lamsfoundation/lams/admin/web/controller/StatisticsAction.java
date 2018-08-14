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
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.lamsfoundation.lams.admin.service.AdminServiceProxy;
import org.lamsfoundation.lams.statistics.dto.GroupStatisticsDTO;
import org.lamsfoundation.lams.statistics.dto.StatisticsDTO;
import org.lamsfoundation.lams.statistics.service.IStatisticsService;
import org.lamsfoundation.lams.usermanagement.Role;
import org.lamsfoundation.lams.util.WebUtil;
import org.lamsfoundation.lams.web.action.LamsDispatchAction;

/**
 * Gives the overall statistics for a LAMS server
 * 
 * @author Luke Foxton
 */
public class StatisticsAction extends LamsDispatchAction {

    private static IStatisticsService statisticsService;

    @Override
    public ActionForward unspecified(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {

	// check permission
	if (!request.isUserInRole(Role.SYSADMIN)) {
	    request.setAttribute("errorName", "StatisticsAction");
	    request.setAttribute("errorMessage", AdminServiceProxy.getMessageService(getServlet().getServletContext())
		    .getMessage("error.authorisation"));
	    return mapping.findForward("error");
	}

	if (statisticsService == null) {
	    statisticsService = AdminServiceProxy.getStatisticsService(getServlet().getServletContext());
	}

	StatisticsDTO stats = statisticsService.getOverallStatistics();

	Map<String, Integer> groupMap = statisticsService.getGroupMap();

	request.setAttribute("statisticsDTO", stats);
	request.setAttribute("groupMap", groupMap);
	return mapping.findForward("success");
    }

    public ActionForward groupStats(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {

	Integer orgId = WebUtil.readIntParam(request, "orgId");

	// check permission
	if (!request.isUserInRole(Role.SYSADMIN)) {
	    request.setAttribute("errorName", "StatisticsAction");
	    request.setAttribute("errorMessage", AdminServiceProxy.getMessageService(getServlet().getServletContext())
		    .getMessage("error.authorisation"));
	    return mapping.findForward("error");
	}

	if (statisticsService == null) {
	    statisticsService = AdminServiceProxy.getStatisticsService(getServlet().getServletContext());
	}

	GroupStatisticsDTO groupStats = statisticsService.getGroupStatisticsDTO(orgId);

	request.setAttribute("groupStatisticsDTO", groupStats);
	return mapping.findForward("groupStats");
    }

}

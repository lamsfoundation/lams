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
import java.util.List;
import java.util.TimeZone;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;
import org.lamsfoundation.lams.admin.service.AdminServiceProxy;
import org.lamsfoundation.lams.timezone.Timezone;
import org.lamsfoundation.lams.timezone.dto.TimezoneDTO;
import org.lamsfoundation.lams.timezone.service.ITimezoneService;
import org.lamsfoundation.lams.util.WebUtil;
import org.lamsfoundation.lams.web.action.LamsDispatchAction;

/**
 * Implements time zone manager.
 *
 * @author Andrey Balan
 *
 *
 *
 *
 *
 *
 */
public class TimezoneManagementAction extends LamsDispatchAction {

    private final static String FORWARD_BACK = "sysadmin";
    private final static String FORWARD_TIMEZONE_MANAGEMENT = "timezoneManagement";
    private final static String FORWARD_SERVER_TIMEZONE_MANAGEMENT = "timezoneServerManagement";

    private static ITimezoneService timezoneService;

    /**
     * Displays list of all JRE available timezones.
     */
    @Override
    public ActionForward unspecified(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {

	timezoneService = AdminServiceProxy.getTimezoneService(getServlet().getServletContext());
	List<Timezone> defaultTimezones = timezoneService.getDefaultTimezones();

	ArrayList<TimezoneDTO> timezoneDtos = new ArrayList<TimezoneDTO>();
	for (String availableTimezoneId : TimeZone.getAvailableIDs()) {
	    boolean isSelected = defaultTimezones.contains(new Timezone(availableTimezoneId));
	    TimeZone timeZone = TimeZone.getTimeZone(availableTimezoneId);
	    TimezoneDTO timezoneDto = TimezoneDTO.createTimezoneDTO(timeZone, isSelected);
	    timezoneDtos.add(timezoneDto);
	}

	request.setAttribute("timezoneDtos", timezoneDtos);
	request.setAttribute("serverTimezone", timezoneService.getServerTimezone().getTimezoneId());

	return mapping.findForward(FORWARD_TIMEZONE_MANAGEMENT);
    }

    /**
     * Makes selected timezones default ones.
     *
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    public ActionForward save(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {

	if (isCancelled(request)) {
	    return mapping.findForward(FORWARD_BACK);
	}

	DynaActionForm timezoneForm = (DynaActionForm) form;
	String[] selectedTimezoneIds = (String[]) timezoneForm.get("selected");

	List<Timezone> selectedTimezones = new ArrayList<Timezone>();
	for (String selectedTimezoneId : selectedTimezoneIds) {
	    selectedTimezones.add(new Timezone(selectedTimezoneId));
	}
	timezoneService.updateTimezones(selectedTimezones);

	return mapping.findForward(FORWARD_BACK);
    }

    /**
     * Shows page where admin can choose server timezone.
     *
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    public ActionForward serverTimezoneManagement(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {

	timezoneService = AdminServiceProxy.getTimezoneService(getServlet().getServletContext());

	ArrayList<TimezoneDTO> timezoneDtos = new ArrayList<TimezoneDTO>();
	for (String availableTimezoneId : TimeZone.getAvailableIDs()) {
	    TimeZone timeZone = TimeZone.getTimeZone(availableTimezoneId);
	    TimezoneDTO timezoneDto = TimezoneDTO.createTimezoneDTO(timeZone, false);
	    timezoneDtos.add(timezoneDto);
	}

	request.setAttribute("timezoneDtos", timezoneDtos);
	request.setAttribute("serverTimezone", timezoneService.getServerTimezone().getTimezoneId());

	return mapping.findForward(FORWARD_SERVER_TIMEZONE_MANAGEMENT);
    }

    /**
     * Changes server timezone with the one selected by user.
     *
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    public ActionForward changeServerTimezone(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {
	timezoneService = AdminServiceProxy.getTimezoneService(getServlet().getServletContext());

	String timeZoneId = WebUtil.readStrParam(request, "timeZoneId");
	timezoneService.setServerTimezone(timeZoneId);

	return unspecified(mapping, form, request, response);
    }

}

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

package org.lamsfoundation.lams.admin.web.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.TimeZone;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.lamsfoundation.lams.admin.service.AdminServiceProxy;
import org.lamsfoundation.lams.admin.web.form.TimezoneForm;
import org.lamsfoundation.lams.timezone.Timezone;
import org.lamsfoundation.lams.timezone.dto.TimezoneDTO;
import org.lamsfoundation.lams.timezone.service.ITimezoneService;
import org.lamsfoundation.lams.util.WebUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.context.WebApplicationContext;

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
@Controller
@RequestMapping("/timezonemanagement")
public class TimezoneManagementController {

    private static ITimezoneService timezoneService;

    @Autowired
    private WebApplicationContext applicationContext;

    /**
     * Displays list of all JRE available timezones.
     */
    @RequestMapping("/start")
    public String unspecified(HttpServletRequest request) throws Exception {

	timezoneService = AdminServiceProxy.getTimezoneService(applicationContext.getServletContext());
	List<Timezone> defaultTimezones = timezoneService.getDefaultTimezones();

	ArrayList<TimezoneDTO> timezoneDtos = new ArrayList<>();
	for (String availableTimezoneId : TimeZone.getAvailableIDs()) {
	    boolean isSelected = defaultTimezones.contains(new Timezone(availableTimezoneId));
	    TimeZone timeZone = TimeZone.getTimeZone(availableTimezoneId);
	    TimezoneDTO timezoneDto = TimezoneDTO.createTimezoneDTO(timeZone, isSelected);
	    timezoneDtos.add(timezoneDto);
	}

	request.setAttribute("timezoneDtos", timezoneDtos);
	request.setAttribute("serverTimezone", timezoneService.getServerTimezone().getTimezoneId());

	return "timezoneManagement";
    }

    /**
     * Makes selected timezones default ones.
     */
    @RequestMapping(path = "/save", method = RequestMethod.POST)
    public String save(@ModelAttribute TimezoneForm timezoneForm, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {

	if (request.getAttribute("CANCEL") != null) {
	    return "redirect:/sysadminstart.do";
	}

	String[] selectedTimezoneIds = timezoneForm.getSelected();

	List<Timezone> selectedTimezones = new ArrayList<>();
	for (String selectedTimezoneId : selectedTimezoneIds) {
	    selectedTimezones.add(new Timezone(selectedTimezoneId));
	}
	timezoneService.updateTimezones(selectedTimezones);

	return "redirect:/sysadminstart.do";
    }

    /**
     * Shows page where admin can choose server timezone.
     */
    @RequestMapping(path = "/serverTimezoneManagement", method = RequestMethod.POST)
    public String serverTimezoneManagement(HttpServletRequest request) throws Exception {

	timezoneService = AdminServiceProxy.getTimezoneService(applicationContext.getServletContext());

	ArrayList<TimezoneDTO> timezoneDtos = new ArrayList<>();
	for (String availableTimezoneId : TimeZone.getAvailableIDs()) {
	    TimeZone timeZone = TimeZone.getTimeZone(availableTimezoneId);
	    TimezoneDTO timezoneDto = TimezoneDTO.createTimezoneDTO(timeZone, false);
	    timezoneDtos.add(timezoneDto);
	}

	request.setAttribute("timezoneDtos", timezoneDtos);
	request.setAttribute("serverTimezone", timezoneService.getServerTimezone().getTimezoneId());

	return "timezoneServerManagement";
    }

    /**
     * Changes server timezone with the one selected by user.
     */
    @RequestMapping(path = "/changeServerTimezone", method = RequestMethod.POST)
    public String changeServerTimezone(HttpServletRequest request) throws Exception {
	timezoneService = AdminServiceProxy.getTimezoneService(applicationContext.getServletContext());

	String timeZoneId = WebUtil.readStrParam(request, "timeZoneId");
	timezoneService.setServerTimezone(timeZoneId);

	return unspecified(request);
    }

}

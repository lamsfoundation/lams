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

import org.lamsfoundation.lams.timezone.Timezone;
import org.lamsfoundation.lams.timezone.dto.TimezoneDTO;
import org.lamsfoundation.lams.timezone.service.ITimezoneService;
import org.lamsfoundation.lams.web.filter.AuditLogFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Implements time zone manager.
 *
 * @author Andrey Balan
 */
@Controller
@RequestMapping("/timezonemanagement")
public class TimezoneManagementController {

    @Autowired
    private ITimezoneService timezoneService;

    /**
     * Shows page where admin can choose server timezone.
     */
    @RequestMapping(path = "/start")
    public String serverTimezoneManagement(Model model) throws Exception {
	ArrayList<TimezoneDTO> timezoneDtos = new ArrayList<>();

	for (Timezone timeZone : timezoneService.getDefaultTimezones()) {
	    TimezoneDTO timezoneDto = TimezoneDTO.createTimezoneDTO(timeZone);
	    timezoneDtos.add(timezoneDto);
	}

	model.addAttribute("timezoneDtos", timezoneDtos);
	model.addAttribute("serverTimezone", timezoneService.getServerTimezone().getTimezoneId());

	return "timezoneManagement";
    }

    /**
     * Changes server timezone with the one selected by user.
     */
    @RequestMapping(path = "/changeServerTimezone")
    public String changeServerTimezone(@RequestParam String serverTimezone) throws Exception {
	timezoneService.setServerTimezone(serverTimezone);

	AuditLogFilter.log(AuditLogFilter.TIMEZONE_CHANGE_ACTION, "server time zone: " + serverTimezone);

	return "forward:start.do?saved=true";
    }
}
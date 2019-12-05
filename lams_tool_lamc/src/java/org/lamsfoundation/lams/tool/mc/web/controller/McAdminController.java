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

package org.lamsfoundation.lams.tool.mc.web.controller;

import javax.servlet.http.HttpServletRequest;

import org.lamsfoundation.lams.tool.mc.McAppConstants;
import org.lamsfoundation.lams.tool.mc.service.IMcService;
import org.lamsfoundation.lams.tool.mc.web.form.McAdminForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * @author Andrey Balan, Marcin Cieslak
 */
@Controller
@RequestMapping("/admin")
public class McAdminController {

    @Autowired
    private IMcService mcService;

    @RequestMapping("/start")
    public String start(@ModelAttribute("mcAdminForm") McAdminForm mcAdminForm, HttpServletRequest request) {
	mcAdminForm.setHideTitles(Boolean.valueOf(mcService.getConfigValue(McAppConstants.CONFIG_KEY_HIDE_TITLES)));

	return "/admin/config";
    }

    @RequestMapping(value = "/saveContent", method = RequestMethod.POST)
    public String saveContent(@ModelAttribute("mcAdminForm") McAdminForm mcAdminForm, HttpServletRequest request) {
	mcService.setConfigValue(McAppConstants.CONFIG_KEY_HIDE_TITLES, String.valueOf(mcAdminForm.isHideTitles()));

	request.setAttribute("savedSuccess", true);
	return "/admin/config";

    }
}
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

package org.lamsfoundation.lams.tool.whiteboard.web.controller;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.lamsfoundation.lams.tool.whiteboard.model.WhiteboardConfigItem;
import org.lamsfoundation.lams.tool.whiteboard.service.IWhiteboardService;
import org.lamsfoundation.lams.tool.whiteboard.web.form.AdminForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private IWhiteboardService whiteboardService;

    @RequestMapping("/start")
    public String start(@ModelAttribute("whiteboardAdminForm") AdminForm whiteboardAdminForm,
	    HttpServletRequest request) {

	WhiteboardConfigItem whiteboardServerUrlConfigItem = whiteboardService
		.getConfigItem(WhiteboardConfigItem.KEY_SERVER_URL);
	if (whiteboardServerUrlConfigItem != null
		&& StringUtils.isNotBlank(whiteboardServerUrlConfigItem.getConfigValue())) {
	    whiteboardAdminForm.setWhiteboardServerUrl(whiteboardServerUrlConfigItem.getConfigValue());
	}

	WhiteboardConfigItem whiteboardAccessTokenConfigItem = whiteboardService
		.getConfigItem(WhiteboardConfigItem.KEY_ACCESS_TOKEN);
	if (whiteboardAccessTokenConfigItem != null
		&& StringUtils.isNotBlank(whiteboardAccessTokenConfigItem.getConfigValue())) {
	    whiteboardAdminForm.setWhiteboardServerUrl(whiteboardAccessTokenConfigItem.getConfigValue());
	}

	return "pages/admin/config";
    }

    @RequestMapping(value = "/saveContent", method = RequestMethod.POST)
    public String saveContent(@ModelAttribute("whiteboardAdminForm") AdminForm whiteboardAdminForm,
	    HttpServletRequest request) {

	WhiteboardConfigItem whiteboardServerUrlConfigItem = whiteboardService
		.getConfigItem(WhiteboardConfigItem.KEY_SERVER_URL);
	if (whiteboardServerUrlConfigItem == null) {
	    whiteboardServerUrlConfigItem = new WhiteboardConfigItem();
	    whiteboardServerUrlConfigItem.setConfigKey(WhiteboardConfigItem.KEY_SERVER_URL);
	}
	whiteboardServerUrlConfigItem
		.setConfigValue(StringUtils.isBlank(whiteboardAdminForm.getWhiteboardServerUrl()) ? null
			: whiteboardAdminForm.getWhiteboardServerUrl().strip());
	whiteboardService.saveOrUpdateWhiteboardConfigItem(whiteboardServerUrlConfigItem);

	WhiteboardConfigItem whiteboardAccessTokenConfigItem = whiteboardService
		.getConfigItem(WhiteboardConfigItem.KEY_ACCESS_TOKEN);
	if (whiteboardAccessTokenConfigItem == null) {
	    whiteboardAccessTokenConfigItem = new WhiteboardConfigItem();
	    whiteboardAccessTokenConfigItem.setConfigKey(WhiteboardConfigItem.KEY_ACCESS_TOKEN);
	}
	whiteboardAccessTokenConfigItem
		.setConfigValue(StringUtils.isBlank(whiteboardAdminForm.getWhiteboardAccessToken()) ? null
			: whiteboardAdminForm.getWhiteboardAccessToken().strip());
	whiteboardService.saveOrUpdateWhiteboardConfigItem(whiteboardAccessTokenConfigItem);

	request.setAttribute("savedSuccess", true);
	return "pages/admin/config";

    }
}
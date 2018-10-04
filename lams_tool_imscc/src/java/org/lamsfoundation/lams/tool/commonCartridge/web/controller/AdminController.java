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

package org.lamsfoundation.lams.tool.commonCartridge.web.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.lamsfoundation.lams.tool.commonCartridge.model.CommonCartridgeConfigItem;
import org.lamsfoundation.lams.tool.commonCartridge.service.ICommonCartridgeService;
import org.lamsfoundation.lams.tool.commonCartridge.web.form.AdminForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author Andrey Balan
 */
@Controller
@RequestMapping("/laimsc11admin")
public class AdminController {

    @Autowired
    private ICommonCartridgeService commonCartridgeService;

    @RequestMapping("/start")
    public String start(@ModelAttribute("commonCartridgeAdminForm") AdminForm commonCartridgeAdminForm,
	    HttpServletRequest request, HttpServletResponse response) {

	CommonCartridgeConfigItem allowExposeUserName = commonCartridgeService
		.getConfigItem(CommonCartridgeConfigItem.KEY_EXPOSE_USER_NAME);
	if (allowExposeUserName != null) {
	    commonCartridgeAdminForm.setAllowExposeUserName(Boolean.parseBoolean(allowExposeUserName.getConfigValue()));
	}

	CommonCartridgeConfigItem allowExposeUserEmail = commonCartridgeService
		.getConfigItem(CommonCartridgeConfigItem.KEY_EXPOSE_USER_EMAIL);
	if (allowExposeUserEmail != null) {
	    commonCartridgeAdminForm
		    .setAllowExposeUserEmail(Boolean.parseBoolean(allowExposeUserEmail.getConfigValue()));
	}

	request.setAttribute("error", false);
	return "pages/admin/config";
    }

    @RequestMapping("/saveContent")
    public String saveContent(@ModelAttribute("commonCartridgeAdminForm") AdminForm commonCartridgeAdminForm,
	    HttpServletRequest request) {

	MultiValueMap<String, String> errorMap = validateAdminForm(commonCartridgeAdminForm);
	if (!errorMap.isEmpty()) {
	    request.setAttribute("errorMap", errorMap);
	    return "pages/admin/config";
	}

	CommonCartridgeConfigItem allowExposeUserName = commonCartridgeService
		.getConfigItem(CommonCartridgeConfigItem.KEY_EXPOSE_USER_NAME);
	allowExposeUserName.setConfigValue(String.valueOf(commonCartridgeAdminForm.isAllowExposeUserName()));
	commonCartridgeService.saveOrUpdateConfigItem(allowExposeUserName);

	CommonCartridgeConfigItem allowExposeUserEmail = commonCartridgeService
		.getConfigItem(CommonCartridgeConfigItem.KEY_EXPOSE_USER_EMAIL);
	allowExposeUserEmail.setConfigValue(String.valueOf(commonCartridgeAdminForm.isAllowExposeUserEmail()));
	commonCartridgeService.saveOrUpdateConfigItem(allowExposeUserEmail);

	request.setAttribute("savedSuccess", true);
	return "pages/admin/config";

    }

    /**
     * Validate BasicLTIConfigItems.
     *
     * @param adminForm
     * @return
     */
    private MultiValueMap<String, String> validateAdminForm(AdminForm commonCartridgeAdminForm) {
	MultiValueMap<String, String> errorMap = new LinkedMultiValueMap<>();

	// if ((adminForm.isAllowExposeUserName() == null) || adminForm.isAllowExposeUserName().equals("")) {
	// if (!isParsableToInt(adminForm.isAllowExposeUserName())) {
	// errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
	// BasicLTIConstants.ERROR_MSG_ENTERED_VALUES_NOT_INTEGERS));
	// }
	// } else {
	// errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
	// BasicLTIConstants.ERROR_MSG_REQUIRED_FIELDS_MISSING));
	// }
	//
	// if ((adminForm.isAllowExposeUserEmail() != null) && !adminForm.isAllowExposeUserEmail().equals("")) {
	// if (!isParsableToInt(adminForm.isAllowExposeUserEmail())) {
	// errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
	// BasicLTIConstants.ERROR_MSG_ENTERED_VALUES_NOT_INTEGERS));
	// }
	// } else {
	// errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
	// BasicLTIConstants.ERROR_MSG_REQUIRED_FIELDS_MISSING));
	// }

	return errorMap;
    }
}

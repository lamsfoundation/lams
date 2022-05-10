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

package org.lamsfoundation.lams.tool.scratchie.web.controller;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.lamsfoundation.lams.tool.scratchie.ScratchieConstants;
import org.lamsfoundation.lams.tool.scratchie.model.ScratchieConfigItem;
import org.lamsfoundation.lams.tool.scratchie.service.IScratchieService;
import org.lamsfoundation.lams.tool.scratchie.web.form.AdminForm;
import org.lamsfoundation.lams.util.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * @author Andrey Balan
 */
@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private IScratchieService scratchieService;

    @Autowired
    @Qualifier("scratchieMessageService")
    private MessageService messageService;

    @RequestMapping("/start")
    public String start(@ModelAttribute("scratchieAdminForm") AdminForm scratchieAdminForm,
	    HttpServletRequest request) {

	ScratchieConfigItem presetMarks = scratchieService.getConfigItem(ScratchieConfigItem.KEY_PRESET_MARKS);
	if (presetMarks != null) {
	    scratchieAdminForm.setPresetMarks(presetMarks.getConfigValue());
	}

	ScratchieConfigItem hideTitles = scratchieService.getConfigItem(ScratchieConfigItem.KEY_HIDE_TITLES);
	if (hideTitles != null) {
	    scratchieAdminForm.setHideTitles(Boolean.valueOf(hideTitles.getConfigValue()));
	}

	request.setAttribute("error", false);
	return "pages/admin/config";
    }

    @RequestMapping(value = "/saveContent", method = RequestMethod.POST)
    public String saveContent(@ModelAttribute("scratchieAdminForm") AdminForm scratchieAdminForm,
	    HttpServletRequest request) {

	MultiValueMap<String, String> errorMap = validateAdminForm(scratchieAdminForm);
	if (!errorMap.isEmpty()) {
	    request.setAttribute("errorMap", errorMap);
	    return "pages/admin/config";
	}

	ScratchieConfigItem presetMarks = scratchieService.getConfigItem(ScratchieConfigItem.KEY_PRESET_MARKS);
	presetMarks.setConfigValue(scratchieAdminForm.getPresetMarks());
	scratchieService.saveOrUpdateScratchieConfigItem(presetMarks);

	ScratchieConfigItem hideTitles = scratchieService.getConfigItem(ScratchieConfigItem.KEY_HIDE_TITLES);
	hideTitles.setConfigValue("" + scratchieAdminForm.isHideTitles());
	scratchieService.saveOrUpdateScratchieConfigItem(hideTitles);

	request.setAttribute("savedSuccess", true);
	return "pages/admin/config";

    }

    /**
     * Validate ScratchieConfigItems.
     *
     * @param adminForm
     * @return
     */
    private MultiValueMap<String, String> validateAdminForm(
	    @ModelAttribute("scratchieAdminForm") AdminForm scratchieAdminForm) {
	MultiValueMap<String, String> errorMap = new LinkedMultiValueMap<>();

	String presetMarks = scratchieAdminForm.getPresetMarks();
	if (StringUtils.isNotBlank(presetMarks)) {

	    //it's not a comma separated numbers
	    if (!presetMarks.matches("[0-9]+(,[0-9]+)*")) {
		errorMap.add("GLOBAL", messageService
			.getMessage(ScratchieConstants.ERROR_MSG_ENTERED_MARKS_NOT_COMMA_SEPARATED_INTEGERS));
	    }

	} else {
	    errorMap.add("GlOBAL", messageService.getMessage(ScratchieConstants.ERROR_MSG_REQUIRED_FIELDS_MISSING));
	}

	return errorMap;
    }
}

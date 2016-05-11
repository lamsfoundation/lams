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


package org.lamsfoundation.lams.tool.scratchie.web.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.lamsfoundation.lams.tool.scratchie.ScratchieConstants;
import org.lamsfoundation.lams.tool.scratchie.model.ScratchieConfigItem;
import org.lamsfoundation.lams.tool.scratchie.service.IScratchieService;
import org.lamsfoundation.lams.tool.scratchie.service.ScratchieServiceProxy;
import org.lamsfoundation.lams.tool.scratchie.web.form.AdminForm;

/**
 * @author Andrey Balan
 */
public class AdminAction extends Action {
    private IScratchieService scratchieService;

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {

	String param = mapping.getParameter();
	if (param.equals("start")) {
	    return start(mapping, form, request, response);
	}
	if (param.equals("saveContent")) {
	    return saveContent(mapping, form, request, response);
	}

	return start(mapping, form, request, response);
    }

    public ActionForward start(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {
	// set up scratchieService
	if (scratchieService == null) {
	    scratchieService = ScratchieServiceProxy.getScratchieService(this.getServlet().getServletContext());
	}

	AdminForm adminForm = (AdminForm) form;

	ScratchieConfigItem isEnabledExtraPointOption = scratchieService
		.getConfigItem(ScratchieConfigItem.KEY_IS_ENABLED_EXTRA_POINT_OPTION);
	if (isEnabledExtraPointOption != null) {
	    adminForm.setEnabledExtraPointOption(new Boolean(isEnabledExtraPointOption.getConfigValue()));
	}

	ScratchieConfigItem presetMarks = scratchieService.getConfigItem(ScratchieConfigItem.KEY_PRESET_MARKS);
	if (presetMarks != null) {
	    adminForm.setPresetMarks(presetMarks.getConfigValue());
	}

	request.setAttribute("error", false);
	return mapping.findForward("config");
    }

    public ActionForward saveContent(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {
	AdminForm adminForm = (AdminForm) form;

	ActionErrors errors = validateAdminForm(adminForm);
	if (!errors.isEmpty()) {
	    this.addErrors(request, errors);
	    return mapping.findForward("config");
	}

	// set up scratchieService
	if (scratchieService == null) {
	    scratchieService = ScratchieServiceProxy.getScratchieService(this.getServlet().getServletContext());
	}

	ScratchieConfigItem isEnabledExtraPointOption = scratchieService
		.getConfigItem(ScratchieConfigItem.KEY_IS_ENABLED_EXTRA_POINT_OPTION);
	isEnabledExtraPointOption.setConfigValue("" + adminForm.isEnabledExtraPointOption());
	scratchieService.saveOrUpdateScratchieConfigItem(isEnabledExtraPointOption);

	ScratchieConfigItem presetMarks = scratchieService.getConfigItem(ScratchieConfigItem.KEY_PRESET_MARKS);
	presetMarks.setConfigValue(adminForm.getPresetMarks());
	scratchieService.saveOrUpdateScratchieConfigItem(presetMarks);

	request.setAttribute("savedSuccess", true);
	return mapping.findForward("config");

    }

    /**
     * Validate ScratchieConfigItems.
     *
     * @param adminForm
     * @return
     */
    private ActionErrors validateAdminForm(AdminForm adminForm) {
	ActionErrors errors = new ActionErrors();

	String presetMarks = adminForm.getPresetMarks();
	if (StringUtils.isNotBlank(presetMarks)) {

	    //it's not a comma separated numbers
	    if (!presetMarks.matches("[0-9]+(,[0-9]+)*")) {
		errors.add(ActionMessages.GLOBAL_MESSAGE,
			new ActionMessage(ScratchieConstants.ERROR_MSG_ENTERED_MARKS_NOT_COMMA_SEPARATED_INTEGERS));
	    }

	} else {
	    errors.add(ActionMessages.GLOBAL_MESSAGE,
		    new ActionMessage(ScratchieConstants.ERROR_MSG_REQUIRED_FIELDS_MISSING));
	}

	return errors;
    }
}

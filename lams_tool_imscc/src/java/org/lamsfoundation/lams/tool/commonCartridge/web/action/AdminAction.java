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


package org.lamsfoundation.lams.tool.commonCartridge.web.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.lamsfoundation.lams.tool.commonCartridge.model.CommonCartridgeConfigItem;
import org.lamsfoundation.lams.tool.commonCartridge.service.CommonCartridgeServiceProxy;
import org.lamsfoundation.lams.tool.commonCartridge.service.ICommonCartridgeService;
import org.lamsfoundation.lams.tool.commonCartridge.web.form.AdminForm;

/**
 * @author Andrey Balan
 */
public class AdminAction extends Action {
    private ICommonCartridgeService commonCartridgeService;

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {

	String param = mapping.getParameter();
	// -----------------------BasicLTI Author function ---------------------------
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
	// set up commonCartridgeService
	if (commonCartridgeService == null) {
	    commonCartridgeService = CommonCartridgeServiceProxy
		    .getCommonCartridgeService(this.getServlet().getServletContext());
	}

	AdminForm adminForm = (AdminForm) form;

	CommonCartridgeConfigItem allowExposeUserName = commonCartridgeService
		.getConfigItem(CommonCartridgeConfigItem.KEY_EXPOSE_USER_NAME);
	if (allowExposeUserName != null) {
	    adminForm.setAllowExposeUserName(Boolean.parseBoolean(allowExposeUserName.getConfigValue()));
	}

	CommonCartridgeConfigItem allowExposeUserEmail = commonCartridgeService
		.getConfigItem(CommonCartridgeConfigItem.KEY_EXPOSE_USER_EMAIL);
	if (allowExposeUserEmail != null) {
	    adminForm.setAllowExposeUserEmail(Boolean.parseBoolean(allowExposeUserEmail.getConfigValue()));
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

	// set up commonCartridgeService
	if (commonCartridgeService == null) {
	    commonCartridgeService = CommonCartridgeServiceProxy
		    .getCommonCartridgeService(this.getServlet().getServletContext());
	}

	CommonCartridgeConfigItem allowExposeUserName = commonCartridgeService
		.getConfigItem(CommonCartridgeConfigItem.KEY_EXPOSE_USER_NAME);
	allowExposeUserName.setConfigValue(String.valueOf(adminForm.isAllowExposeUserName()));
	commonCartridgeService.saveOrUpdateConfigItem(allowExposeUserName);

	CommonCartridgeConfigItem allowExposeUserEmail = commonCartridgeService
		.getConfigItem(CommonCartridgeConfigItem.KEY_EXPOSE_USER_EMAIL);
	allowExposeUserEmail.setConfigValue(String.valueOf(adminForm.isAllowExposeUserEmail()));
	commonCartridgeService.saveOrUpdateConfigItem(allowExposeUserEmail);

	request.setAttribute("savedSuccess", true);
	return mapping.findForward("config");

    }

    /**
     * Validate BasicLTIConfigItems.
     *
     * @param adminForm
     * @return
     */
    private ActionErrors validateAdminForm(AdminForm adminForm) {
	ActionErrors errors = new ActionErrors();

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

	return errors;
    }
}

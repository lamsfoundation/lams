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
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301
 * USA
 *
 * http://www.gnu.org/licenses/gpl.txt
 * ****************************************************************
 */


package org.lamsfoundation.lams.tool.spreadsheet.web.action;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.lamsfoundation.lams.tool.spreadsheet.SpreadsheetConstants;
import org.lamsfoundation.lams.tool.spreadsheet.model.SpreadsheetUser;
import org.lamsfoundation.lams.tool.spreadsheet.service.ISpreadsheetService;
import org.lamsfoundation.lams.util.WebUtil;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

public class ViewItemAction extends Action {

    private static final Logger log = Logger.getLogger(ViewItemAction.class);

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws IOException, ServletException {
	String param = mapping.getParameter();

	if (param.equals("reviewItem")) {
	    return reviewItem(mapping, form, request, response);
	}

	return mapping.findForward(SpreadsheetConstants.ERROR);
    }

    /**
     * Display main frame to display instrcution and item content.
     * 
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     */
    private ActionForward reviewItem(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {

	Long userId = WebUtil.readLongParam(request, SpreadsheetConstants.ATTR_USER_UID);
	SpreadsheetUser user = getSpreadsheetService().getUser(userId);
	request.setAttribute(SpreadsheetConstants.ATTR_USER_NAME, user.getFullUsername());
	String code = null;
	if (user.getUserModifiedSpreadsheet() != null) {
	    code = user.getUserModifiedSpreadsheet().getUserModifiedSpreadsheet();
	}
	request.setAttribute(SpreadsheetConstants.ATTR_CODE, code);

	return mapping.findForward(SpreadsheetConstants.SUCCESS);
    }

    //*************************************************************************************
    // Private method 
    //*************************************************************************************

    private ISpreadsheetService getSpreadsheetService() {
	WebApplicationContext wac = WebApplicationContextUtils
		.getRequiredWebApplicationContext(getServlet().getServletContext());
	return (ISpreadsheetService) wac.getBean(SpreadsheetConstants.RESOURCE_SERVICE);
    }

}

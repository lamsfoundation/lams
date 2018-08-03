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

package org.lamsfoundation.lams.tool.spreadsheet.web.controller;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.lamsfoundation.lams.tool.spreadsheet.SpreadsheetConstants;
import org.lamsfoundation.lams.tool.spreadsheet.model.SpreadsheetUser;
import org.lamsfoundation.lams.tool.spreadsheet.service.ISpreadsheetService;
import org.lamsfoundation.lams.util.WebUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class ViewItemController {

    private static final Logger log = Logger.getLogger(ViewItemController.class);

    @Autowired
    @Qualifier("spreadsheetService")
    private ISpreadsheetService service;

    /**
     * Display main frame to display instrcution and item content.
     */
    @RequestMapping("/reviewItem")
    public String reviewItem(HttpServletRequest request) {

	Long userId = WebUtil.readLongParam(request, SpreadsheetConstants.ATTR_USER_UID);
	SpreadsheetUser user = service.getUser(userId);
	request.setAttribute(SpreadsheetConstants.ATTR_USER_NAME, user.getFullUsername());
	String code = null;
	if (user.getUserModifiedSpreadsheet() != null) {
	    code = user.getUserModifiedSpreadsheet().getUserModifiedSpreadsheet();
	}
	request.setAttribute("code", code);

	return "pages/reviewitem/reviewitem";
    }

}

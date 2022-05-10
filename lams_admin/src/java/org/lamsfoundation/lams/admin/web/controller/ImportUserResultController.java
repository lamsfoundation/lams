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

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.lamsfoundation.lams.admin.service.IImportService;
import org.lamsfoundation.lams.util.MessageService;
import org.lamsfoundation.lams.web.session.SessionManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author jliew
 */
@Controller
public class ImportUserResultController {
    private static Logger log = Logger.getLogger(ImportUserResultController.class);

    @Autowired
    private IImportService importService;
    @Autowired
    @Qualifier("adminMessageService")
    private MessageService messageService;

    @RequestMapping(path = "/importuserresult")
    public String execute(HttpServletRequest request) throws Exception {
	HttpSession ss = SessionManager.getSession();

	List results = (List) ss.getAttribute(IImportService.IMPORT_RESULTS);
	String successMessageKey = "";
	try {
	    File file = (File) ss.getAttribute(IImportService.IMPORT_FILE);
	    successMessageKey = (importService.isUserSpreadsheet(file) ? "msg.users.created" : "msg.users.added");
	} catch (Exception e) {
	    log.error("Couldn't check spreadsheet type!", e);
	}

	Integer successful = (Integer) ss.getAttribute(IImportService.STATUS_SUCCESSFUL);
	if (successful == null) {
	    successful = 0;
	    for (int i = 0; i < results.size(); i++) {
		ArrayList rowResult = (ArrayList) results.get(i);
		if (rowResult.isEmpty()) {
		    successful++;
		}
	    }
	}

	request.setAttribute("results", results);
	request.setAttribute("successful", messageService.getMessage(successMessageKey, new Integer[] { successful }));

	// remove temporary session vars that allowed status to be displayed
	// to user during import
	ss.removeAttribute(IImportService.STATUS_IMPORT_TOTAL);
	ss.removeAttribute(IImportService.STATUS_IMPORTED);
	ss.removeAttribute(IImportService.STATUS_SUCCESSFUL);
	ss.removeAttribute(IImportService.IMPORT_FILE);
	ss.removeAttribute(IImportService.IMPORT_RESULTS);

	return "import/importresult";
    }
}

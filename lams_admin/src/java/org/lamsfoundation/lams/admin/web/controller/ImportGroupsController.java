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
import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.lamsfoundation.lams.admin.service.IImportService;
import org.lamsfoundation.lams.admin.web.form.ImportExcelForm;
import org.lamsfoundation.lams.util.FileUtil;
import org.lamsfoundation.lams.web.session.SessionManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author jliew
 */
@Controller
public class ImportGroupsController {

    @Autowired
    private IImportService importService;

    @RequestMapping(path = "/importgroups")
    public String execute(@ModelAttribute("importForm") ImportExcelForm importForm, HttpServletRequest request)
	    throws Exception {
	importForm.setOrgId(0);
	File file = null;

	File uploadDir = FileUtil.getTmpFileUploadDir(importForm.getTmpFileUploadId());
	if (uploadDir.canRead()) {
	    File[] files = uploadDir.listFiles();
	    if (files.length > 1) {
		throw new IOException("Uploaded more than 1 file");
	    } else if (files.length == 1) {
		file = files[0];
	    }
	}

	importForm.setTmpFileUploadId(FileUtil.generateTmpFileUploadId());

	// validation
	if (file == null) {
	    return "import/importGroups";
	}

	String sessionId = SessionManager.getSession().getId();
	List results = importService.parseGroupSpreadsheet(file, sessionId);
	request.setAttribute("results", results);

	FileUtil.deleteTmpFileUploadDir(importForm.getTmpFileUploadId());

	return "import/importGroups";
    }

}

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

import org.apache.log4j.Logger;
import org.lamsfoundation.lams.admin.service.IImportService;
import org.lamsfoundation.lams.admin.web.form.ImportExcelForm;
import org.lamsfoundation.lams.util.FileUtil;
import org.lamsfoundation.lams.web.session.SessionManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

/**
 * @author jliew
 */
@Controller
public class ImportExcelSaveController {
    private static Logger log = Logger.getLogger(ImportExcelSaveController.class);

    @Autowired
    private IImportService importService;

    @Autowired
    private WebApplicationContext applicationContext;

    @RequestMapping(path = "/importexcelsave", method = RequestMethod.POST)
    public String execute(@ModelAttribute ImportExcelForm importExcelForm, HttpServletRequest request)
	    throws Exception {
	File file = null;
	File uploadDir = FileUtil.getTmpFileUploadDir(importExcelForm.getTmpFileUploadId());
	if (uploadDir.canRead()) {
	    File[] files = uploadDir.listFiles();
	    if (files.length > 1) {
		throw new IOException("Uploaded more than 1 file");
	    } else if (files.length == 1) {
		file = files[0];
	    }
	}

	// validation
	if (file == null) {
	    return "forward:/importexcel.do";
	}

	String sessionId = SessionManager.getSession().getId();
	SessionManager.getSession().setAttribute(IImportService.IMPORT_FILE, file);
	// use a new thread only if number of users is > threshold
	if (importService.getNumRows(file) < IImportService.THRESHOLD) {
	    List results = importService.parseSpreadsheet(file, sessionId);
	    SessionManager.getSession(sessionId).setAttribute(IImportService.IMPORT_RESULTS, results);
	    return "forward:/importuserresult.do";
	} else {
	    Thread t = new Thread(new ImportExcelThread(sessionId));
	    t.start();
	    return "import/status";
	}
    }

    private class ImportExcelThread implements Runnable {
	private String sessionId;

	public ImportExcelThread(String sessionId) {
	    this.sessionId = sessionId;
	}

	@Override
	public void run() {
	    WebApplicationContext wac = WebApplicationContextUtils
		    .getRequiredWebApplicationContext(applicationContext.getServletContext());
	    IImportService importService = (IImportService) wac.getBean("importService");
	    try {
		File file = (File) SessionManager.getSession(sessionId).getAttribute(IImportService.IMPORT_FILE);
		List results = importService.parseSpreadsheet(file, sessionId);
		SessionManager.getSession(sessionId).setAttribute(IImportService.IMPORT_RESULTS, results);
	    } catch (Exception e) {
		log.warn("Exception in ImportExcelThread: ", e);
	    }
	}
    }

}

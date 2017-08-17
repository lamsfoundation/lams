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


package org.lamsfoundation.lams.admin.web.action;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.upload.FormFile;
import org.lamsfoundation.lams.admin.service.AdminServiceProxy;
import org.lamsfoundation.lams.admin.service.IImportService;
import org.lamsfoundation.lams.admin.web.form.ImportExcelForm;
import org.lamsfoundation.lams.web.session.SessionManager;

/**
 * @author jliew
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 */
public class ImportExcelSaveAction extends Action {

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {

	if (isCancelled(request)) {
	    return mapping.findForward("sysadmin");
	}

	IImportService importService = AdminServiceProxy.getImportService(getServlet().getServletContext());
	ImportExcelForm importExcelForm = (ImportExcelForm) form;
	FormFile file = importExcelForm.getFile();

	// validation
	if (file == null || file.getFileSize() <= 0) {
	    return mapping.findForward("import");
	}

	String sessionId = SessionManager.getSession().getId();
	SessionManager.getSession().setAttribute(IImportService.IMPORT_FILE, file);
	// use a new thread only if number of users is > threshold
	if (importService.getNumRows(file) < IImportService.THRESHOLD) {
	    List results = importService.parseSpreadsheet(file, sessionId);
	    SessionManager.getSession(sessionId).setAttribute(IImportService.IMPORT_RESULTS, results);
	    return mapping.findForward("results");
	} else {
	    Thread t = new Thread(new ImportExcelThread(sessionId));
	    t.start();
	    return mapping.findForward("status");
	}
    }

    private class ImportExcelThread implements Runnable {
	private String sessionId;

	public ImportExcelThread(String sessionId) {
	    this.sessionId = sessionId;
	}

	@Override
	public void run() {
	    IImportService importService = AdminServiceProxy.getImportService(getServlet().getServletContext());
	    try {
		FormFile file = (FormFile) SessionManager.getSession(sessionId)
			.getAttribute(IImportService.IMPORT_FILE);
		List results = importService.parseSpreadsheet(file, sessionId);
		SessionManager.getSession(sessionId).setAttribute(IImportService.IMPORT_RESULTS, results);
	    } catch (Exception e) {
	    }
	}
    }

}

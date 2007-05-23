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

/* $Id$ */
package org.lamsfoundation.lams.admin.web;

import java.util.ArrayList;
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
import org.lamsfoundation.lams.util.MessageService;

/**
 * @author jliew
 *
 * @struts:action path="/importexcelsave"
 *              name="ImportExcelForm"
 *              input=".importexcel"
 *              scope="request"
 * 				validate="false"
 * 
 * @struts:action-forward name="importresult" path=".importresult"
 * @struts:action-forward name="sysadmin" path="/sysadminstart.do"
 * @struts:action-forward name="import" path="/importexcel.do"
 */
public class ImportExcelSaveAction extends Action {
	
	public ActionForward execute(ActionMapping mapping,
            ActionForm form,
            HttpServletRequest request,
            HttpServletResponse response) throws Exception {
		
		if (isCancelled(request)) {
			//return mapping.getInputForward();
			return mapping.findForward("sysadmin");
		}
		
		MessageService messageService = AdminServiceProxy.getMessageService(getServlet().getServletContext());
		IImportService importService = AdminServiceProxy.getImportService(getServlet().getServletContext());
		ImportExcelForm importExcelForm = (ImportExcelForm)form;
		FormFile file = importExcelForm.getFile();
		
		// validation
		if (file==null || file.getFileSize()<=0) {
			return mapping.findForward("import");
		}
		
		List results = importService.parseSpreadsheet(file);
		String successMessageKey = (importService.isUserSpreadsheet(file) ? "msg.users.created" : "msg.users.added");
		
		int successful = 0;
		for(int i=0; i<results.size(); i++) {
			ArrayList rowResult = (ArrayList)results.get(i);
			if (rowResult.isEmpty()) successful++;
		}
		String[] args = new String[1];
		args[0] = String.valueOf(successful);
		
		request.setAttribute("results", results);
		request.setAttribute("successful", messageService.getMessage(successMessageKey, args));
		return mapping.findForward("importresult");
	}

}

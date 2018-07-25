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

import java.io.File;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.action.DynaActionForm;
import org.lamsfoundation.lams.admin.service.AdminServiceProxy;
import org.lamsfoundation.lams.usermanagement.Role;
import org.lamsfoundation.lams.util.FileUtil;
import org.lamsfoundation.lams.util.MessageService;
import org.lamsfoundation.lams.util.TempDirectoryFilter;
import org.lamsfoundation.lams.util.WebUtil;

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
 */
public class CleanupTempFilesAction extends Action {

    private static Logger log = Logger.getLogger(CleanupTempFilesAction.class);

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {

	// check user is sysadmin
	if (!(request.isUserInRole(Role.SYSADMIN))) {
	    request.setAttribute("errorName", "CleanupTempFilesAction");
	    request.setAttribute("errorMessage", AdminServiceProxy.getMessageService(getServlet().getServletContext())
		    .getMessage("error.need.sysadmin"));
	    return mapping.findForward("error");
	}

	if (isCancelled(request)) {
	    return mapping.findForward("sysadmin");
	}

	// check if url contains request for refresh folder sizes only
	String action = WebUtil.readStrParam(request, "action", true);
	if (action != null && StringUtils.equals(action, "refresh")) {
	    return refresh(mapping, form, request, response);
	}

	ActionMessages errors = new ActionMessages();
	DynaActionForm dynaForm = (DynaActionForm) form;
	Integer numDays = (Integer) dynaForm.get("numDays");

	// delete directories if form has been submitted
	if (numDays != null) {
	    if (numDays >= 0) {
		int filesDeleted = FileUtil.cleanupOldFiles(FileUtil.getOldTempFiles(numDays));
		MessageService messageService = AdminServiceProxy.getMessageService(getServlet().getServletContext());
		String args[] = new String[1];
		args[0] = new Integer(filesDeleted).toString();
		request.setAttribute("filesDeleted", messageService.getMessage("msg.cleanup.files.deleted", args));
	    } else {
		errors.add("numDays", new ActionMessage("error.non.negative.number.required"));
	    }
	} else {
	    // recommended number of days to leave temp files
	    dynaForm.set("numDays", new Integer(1));
	}

	return mapping.findForward("cleanup");
    }

    public ActionForward refresh(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {

	// display temp files info
	File oldFiles[] = FileUtil.getOldTempFiles(0);
	long zipTotal = 0;
	long tmpTotal = 0;
	if (oldFiles != null) {
	    for (int i = 0; i < oldFiles.length; i++) {
		if (oldFiles[i].getName().startsWith(TempDirectoryFilter.zip_prefix)) {
		    zipTotal += FileUtil.calculateFileSize(oldFiles[i]);
		} else if (oldFiles[i].getName().startsWith(TempDirectoryFilter.tmp_prefix)) {
		    tmpTotal += FileUtil.calculateFileSize(oldFiles[i]);
		}
	    }
	}
	request.setAttribute("zipTotal", zipTotal / 1024);
	request.setAttribute("tmpTotal", tmpTotal / 1024);

	// set default numDays
	DynaActionForm dynaForm = (DynaActionForm) form;
	Integer numDays = (Integer) dynaForm.get("numDays");
	if (numDays == null) {
	    dynaForm.set("numDays", new Integer(1));
	}

	return mapping.findForward("cleanup");
    }

}

/****************************************************************
 * Copyright (C) 2008 LAMS Foundation (http://lamsfoundation.org)
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


package org.lamsfoundation.lams.tool.sbmt.web.action;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.lamsfoundation.lams.contentrepository.exception.InvalidParameterException;
import org.lamsfoundation.lams.contentrepository.exception.RepositoryCheckedException;
import org.lamsfoundation.lams.tool.sbmt.dto.FileDetailsDTO;
import org.lamsfoundation.lams.tool.sbmt.service.ISubmitFilesService;
import org.lamsfoundation.lams.tool.sbmt.service.SubmitFilesServiceProxy;
import org.lamsfoundation.lams.tool.sbmt.web.form.MarkForm;
import org.lamsfoundation.lams.util.NumberUtil;
import org.lamsfoundation.lams.util.WebUtil;
import org.lamsfoundation.lams.web.action.LamsDispatchAction;
import org.lamsfoundation.lams.web.util.AttributeNames;

/**
 * @author lfoxton
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
public class MarkAction extends LamsDispatchAction {

    private ISubmitFilesService submitFilesService;

    /**
     * Update mark.
     *
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws RepositoryCheckedException
     * @throws InvalidParameterException
     */
    public ActionForward updateMark(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws InvalidParameterException, RepositoryCheckedException {

	MarkForm markForm = (MarkForm) form;

	ActionMessages errors = new ActionMessages();
	// Check whether the mark is valid.
	Float marks = null;
	String markStr = markForm.getMarks();
	try {
	    marks = NumberUtil.getLocalisedFloat(markStr, request.getLocale());
	} catch (Exception e) {
	    errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("errors.mark.invalid.number"));
	}

	String comments = WebUtil.readStrParam(request, "comments", true);
	if (!errors.isEmpty()) {
	    submitFilesService = getSubmitFilesService();
	    List report = new ArrayList<FileDetailsDTO>();
	    FileDetailsDTO fileDetail = submitFilesService.getFileDetails(markForm.getDetailID(), request.getLocale());
	    // echo back the input, even they are wrong.
	    fileDetail.setComments(comments);
	    fileDetail.setMarks(markStr);
	    report.add(fileDetail);

	    request.setAttribute("report", report);
	    request.setAttribute(AttributeNames.PARAM_TOOL_SESSION_ID, markForm.getToolSessionID());

	    saveErrors(request, errors);
	    return mapping.findForward("updateMark");
	}

	if (submitFilesService == null) {
	    submitFilesService = getSubmitFilesService();
	}

	// Update the mark based on the form
	submitFilesService.updateMarks(markForm.getReportID(), marks, comments, markForm.getMarkFile(),markForm.getToolSessionID());
	
	// Return to the appropriate screen based upon the updateMode
	request.setAttribute(AttributeNames.PARAM_TOOL_SESSION_ID, markForm.getToolSessionID());
	if (StringUtils.equals(markForm.getUpdateMode(), "listMark")) {
	    List report = submitFilesService.getFilesUploadedByUser(markForm.getUserID(), markForm.getToolSessionID(),
		    request.getLocale(), true);
	    request.setAttribute("report", report);
	    return mapping.findForward("listMark");
	} else {
	    Map report = submitFilesService.getFilesUploadedBySession(markForm.getToolSessionID(), request.getLocale());
	    request.setAttribute("reports", report);
	    return mapping.findForward("listAllMarks");
	}
    }

    /**
     * Display update mark initial page.
     *
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     */
    public ActionForward newMark(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {

	MarkForm markForm = (MarkForm) form;

	if (submitFilesService == null) {
	    submitFilesService = getSubmitFilesService();
	}

	FileDetailsDTO fileDetailsDTO = submitFilesService.getFileDetails(markForm.getDetailID(), request.getLocale());
	updateMarkForm(markForm, fileDetailsDTO);

	List report = new ArrayList<FileDetailsDTO>();
	report.add(submitFilesService.getFileDetails(markForm.getDetailID(), request.getLocale()));

	request.setAttribute("updateMode", markForm.getUpdateMode());
	request.setAttribute("toolSessionID", markForm.getToolSessionID());
	request.setAttribute("report", report);

	return mapping.findForward("updateMark");
    }

    /**
     * Update the form
     * 
     * @param markForm
     * @param fileDetailsDTO
     */
    private void updateMarkForm(MarkForm markForm, FileDetailsDTO fileDetailsDTO) {

	if (fileDetailsDTO.getMarks() != null) {
	    markForm.setMarks(fileDetailsDTO.getMarks().toString());
	}
	markForm.setReportID(fileDetailsDTO.getReportID());
	markForm.setComments(fileDetailsDTO.getComments());
	markForm.setMarkFileUUID(fileDetailsDTO.getMarkFileUUID());
	markForm.setMarkFileVersionID(fileDetailsDTO.getMarkFileVersionID());
    }

    /**
     * Remove a mark file
     *
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     */
    public ActionForward removeMarkFile(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {
	MarkForm markForm = (MarkForm) form;

	if (submitFilesService == null) {
	    submitFilesService = getSubmitFilesService();
	}

	submitFilesService.removeMarkFile(markForm.getReportID(), markForm.getMarkFileUUID(),
		markForm.getMarkFileVersionID());

	FileDetailsDTO fileDetailsDTO = submitFilesService.getFileDetails(markForm.getDetailID(), request.getLocale());
	updateMarkForm(markForm, fileDetailsDTO);

	List report = new ArrayList<FileDetailsDTO>();
	report.add(submitFilesService.getFileDetails(markForm.getDetailID(), request.getLocale()));

	request.setAttribute("updateMode", markForm.getUpdateMode());
	request.setAttribute("toolSessionID", markForm.getToolSessionID());
	request.setAttribute("report", report);

	return mapping.findForward("updateMark");
    }

    private ISubmitFilesService getSubmitFilesService() {
	return SubmitFilesServiceProxy.getSubmitFilesService(this.getServlet().getServletContext());
    }
}

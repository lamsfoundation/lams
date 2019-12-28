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

package org.lamsfoundation.lams.tool.sbmt.web.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.lamsfoundation.lams.contentrepository.exception.InvalidParameterException;
import org.lamsfoundation.lams.contentrepository.exception.RepositoryCheckedException;
import org.lamsfoundation.lams.tool.sbmt.dto.FileDetailsDTO;
import org.lamsfoundation.lams.tool.sbmt.dto.SubmitUserDTO;
import org.lamsfoundation.lams.tool.sbmt.service.ISubmitFilesService;
import org.lamsfoundation.lams.tool.sbmt.web.form.MarkForm;
import org.lamsfoundation.lams.util.MessageService;
import org.lamsfoundation.lams.util.NumberUtil;
import org.lamsfoundation.lams.util.WebUtil;
import org.lamsfoundation.lams.web.util.AttributeNames;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * @author lfoxton
 */
@Controller
@RequestMapping("/mark")
public class MarkController {

    @Autowired
    private ISubmitFilesService submitFilesService;

    @Autowired
    @Qualifier("sbmtMessageService")
    private MessageService messageService;

    /**
     * Update mark.
     */
    @RequestMapping(path = "/updateMark", method = RequestMethod.POST)
    public String updateMark(@ModelAttribute MarkForm markForm, HttpServletRequest request)
	    throws InvalidParameterException, RepositoryCheckedException {

	// Check whether the mark is valid.
	Float marks = null;
	String markStr = markForm.getMarks();
	MultiValueMap<String, String> errorMap = new LinkedMultiValueMap<>();
	try {
	    marks = NumberUtil.getLocalisedFloat(markStr, request.getLocale());
	} catch (Exception e) {
	    errorMap.add("GLOBAL", messageService.getMessage("errors.mark.invalid.number"));
	}

	String comments = WebUtil.readStrParam(request, "comments", true);
	if (!errorMap.isEmpty()) {
	    List<FileDetailsDTO> report = new ArrayList<>();
	    FileDetailsDTO fileDetail = submitFilesService.getFileDetails(markForm.getDetailID(), request.getLocale());
	    // echo back the input, even they are wrong.
	    fileDetail.setComments(comments);
	    fileDetail.setMarks(markStr);
	    report.add(fileDetail);

	    request.setAttribute("report", report);
	    request.setAttribute(AttributeNames.PARAM_TOOL_SESSION_ID, markForm.getToolSessionID());

	    request.setAttribute("errorMap", errorMap);
	    return "monitoring/mark/updatemark";
	}

	// Update the mark based on the form
	submitFilesService.updateMarks(markForm.getReportID(), marks, comments, markForm.getMarkFile(),
		markForm.getToolSessionID());

	// Return to the appropriate screen based upon the updateMode
	request.setAttribute(AttributeNames.PARAM_TOOL_SESSION_ID, markForm.getToolSessionID());
	if (StringUtils.equals(markForm.getUpdateMode(), "listMark")) {
	    List<FileDetailsDTO> report = submitFilesService.getFilesUploadedByUser(markForm.getUserID(),
		    markForm.getToolSessionID(), request.getLocale(), true);
	    request.setAttribute("report", report);
	    return "monitoring/mark/mark";
	} else {
	    Map<SubmitUserDTO, List<FileDetailsDTO>> report = submitFilesService
		    .getFilesUploadedBySession(markForm.getToolSessionID(), request.getLocale());
	    request.setAttribute("reports", report);
	    return "monitoring/mark/allmarks";
	}
    }

    /**
     * Display update mark initial page.
     */
    @RequestMapping("/newMark")
    public String newMark(@ModelAttribute MarkForm markForm, HttpServletRequest request) {

	FileDetailsDTO fileDetailsDTO = submitFilesService.getFileDetails(markForm.getDetailID(), request.getLocale());
	updateMarkForm(markForm, fileDetailsDTO);

	List<FileDetailsDTO> report = new ArrayList<FileDetailsDTO>();
	report.add(submitFilesService.getFileDetails(markForm.getDetailID(), request.getLocale()));

	request.setAttribute("updateMode", markForm.getUpdateMode());
	request.setAttribute("toolSessionID", markForm.getToolSessionID());
	request.setAttribute("report", report);

	return "monitoring/mark/updatemark";
    }

    /**
     * Update the form
     */
    @RequestMapping("/updateMarkForm")
    private void updateMarkForm(@ModelAttribute MarkForm markForm, FileDetailsDTO fileDetailsDTO) {

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
     */
    @RequestMapping("/removeMarkFile")
    public String removeMarkFile(@ModelAttribute MarkForm markForm, HttpServletRequest request,
	    HttpServletResponse response) throws InvalidParameterException, RepositoryCheckedException {

	submitFilesService.removeMarkFile(markForm.getReportID(), markForm.getMarkFileUUID(),
		markForm.getMarkFileVersionID(), markForm.getToolSessionID());

	FileDetailsDTO fileDetailsDTO = submitFilesService.getFileDetails(markForm.getDetailID(), request.getLocale());
	updateMarkForm(markForm, fileDetailsDTO);

	List<FileDetailsDTO> report = new ArrayList<>();
	report.add(submitFilesService.getFileDetails(markForm.getDetailID(), request.getLocale()));

	request.setAttribute("updateMode", markForm.getUpdateMode());
	request.setAttribute("toolSessionID", markForm.getToolSessionID());
	request.setAttribute("report", report);

	return "monitoring/mark/updatemark";
    }

}

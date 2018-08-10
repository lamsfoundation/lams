/****************************************************************
 * Copyright (C) 2005 LAMS Foundation (http://lamsfoundation.org)
 * =============================================================
 * License Information: http://lamsfoundation.org/licensing/lams/2.0/
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation.
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

package org.lamsfoundation.lams.tool.kaltura.web.controller;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.TimeZone;
import java.util.TreeSet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.lamsfoundation.lams.tool.kaltura.dto.KalturaSessionDTO;
import org.lamsfoundation.lams.tool.kaltura.dto.NotebookEntryDTO;
import org.lamsfoundation.lams.tool.kaltura.model.Kaltura;
import org.lamsfoundation.lams.tool.kaltura.model.KalturaSession;
import org.lamsfoundation.lams.tool.kaltura.service.IKalturaService;
import org.lamsfoundation.lams.tool.kaltura.util.KalturaConstants;
import org.lamsfoundation.lams.tool.kaltura.util.KalturaSessionDTOComparator;
import org.lamsfoundation.lams.usermanagement.dto.UserDTO;
import org.lamsfoundation.lams.util.DateUtil;
import org.lamsfoundation.lams.util.WebUtil;
import org.lamsfoundation.lams.web.session.SessionManager;
import org.lamsfoundation.lams.web.util.AttributeNames;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author Andrey Balan
 */
@Controller
@RequestMapping("/monitoring")
public class MonitoringController {

    private static Logger log = Logger.getLogger(MonitoringController.class);

    @Autowired
    @Qualifier("kalturaService")
    private IKalturaService kalturaService;

    @RequestMapping("")
    public String unspecified(HttpServletRequest request) {

	request.setAttribute("initialTabId", WebUtil.readLongParam(request, AttributeNames.PARAM_CURRENT_TAB, true));
	Long toolContentID = WebUtil.readLongParam(request, AttributeNames.PARAM_TOOL_CONTENT_ID);
	String contentFolderID = WebUtil.readStrParam(request, AttributeNames.PARAM_CONTENT_FOLDER_ID);

	Kaltura kaltura = kalturaService.getKalturaByContentId(toolContentID);
	boolean isGroupedActivity = kalturaService.isGroupedActivity(toolContentID);
	request.setAttribute(KalturaConstants.ATTR_KALTURA, kaltura);
	request.setAttribute("contentFolderID", contentFolderID);
	request.setAttribute("isGroupedActivity", isGroupedActivity);

	Set<KalturaSessionDTO> sessionDTOs = new TreeSet<>(new KalturaSessionDTOComparator());
	for (KalturaSession session : (Set<KalturaSession>) kaltura.getKalturaSessions()) {
	    KalturaSessionDTO sessionDTO = new KalturaSessionDTO(session);
	    sessionDTOs.add(sessionDTO);
	}
	request.setAttribute("sessionDTOs", sessionDTOs);

	// Create reflectList if reflection is enabled.
	if (kaltura.isReflectOnActivity()) {
	    List<NotebookEntryDTO> reflectList = kalturaService.getReflectList(kaltura);
	    request.setAttribute(KalturaConstants.ATTR_REFLECT_LIST, reflectList);
	}

	Date submissionDeadline = kaltura.getSubmissionDeadline();
	if (submissionDeadline != null) {
	    HttpSession ss = SessionManager.getSession();
	    UserDTO teacher = (UserDTO) ss.getAttribute(AttributeNames.USER);
	    TimeZone teacherTimeZone = teacher.getTimeZone();
	    Date tzSubmissionDeadline = DateUtil.convertToTimeZoneFromDefault(teacherTimeZone, submissionDeadline);
	    request.setAttribute(KalturaConstants.ATTR_SUBMISSION_DEADLINE, tzSubmissionDeadline.getTime());
	    // use the unconverted time, as convertToStringForJSON() does the timezone conversion if needed
	    request.setAttribute(KalturaConstants.ATTR_SUBMISSION_DEADLINE_DATESTRING,
		    DateUtil.convertToStringForJSON(submissionDeadline, request.getLocale()));
	}
	String dispatch = request.getParameter("dispatch");
	if("markItem".equals(dispatch)) {
	    return markItem(request);
	}



	return "pages/monitoring/monitoring";
    }

    @RequestMapping("/showGroupLearning")
    public String showGroupLearning(HttpServletRequest request) {

	String toolSessionID = WebUtil.readStrParam(request, AttributeNames.PARAM_TOOL_SESSION_ID);
	UserDTO user = (UserDTO) SessionManager.getSession().getAttribute(AttributeNames.USER);

	String redirectURL = "redirect:/learning.do";
	//to distinguish from opening learning page from monitor's Learners tab pass this parameter
	redirectURL = WebUtil.appendParameterToURL(redirectURL, KalturaConstants.ATTR_IS_GROUP_MONITORING, "true");
	redirectURL = WebUtil.appendParameterToURL(redirectURL, AttributeNames.PARAM_MODE, "teacher");
	redirectURL = WebUtil.appendParameterToURL(redirectURL, AttributeNames.PARAM_TOOL_SESSION_ID, toolSessionID);
	redirectURL = WebUtil.appendParameterToURL(redirectURL, AttributeNames.PARAM_USER_ID,
		user.getUserID().toString());
	return redirectURL;
    }

    /**
     * Update item's mark. If it's not long then store 0.
     */
    @RequestMapping("/markItem")
    @ResponseBody
    public String markItem(HttpServletRequest request) {

	String markStr = WebUtil.readStrParam(request, "content");
	try {
	    Long mark = Long.parseLong(markStr);
	    Long itemUid = WebUtil.readLongParam(request, KalturaConstants.PARAM_ITEM_UID);
	    kalturaService.markItem(itemUid, mark);
	} catch (NumberFormatException e) {
	    log.debug(e.getMessage());
	}
	return null;
    }

    @RequestMapping("/setItemVisibility")
    @ResponseBody
    public void setItemVisibility(HttpServletRequest request) {

	Long itemUid = WebUtil.readLongParam(request, KalturaConstants.PARAM_ITEM_UID);
	boolean isHide = WebUtil.readBooleanParam(request, KalturaConstants.PARAM_IS_HIDING);
	kalturaService.hideItem(itemUid, isHide);

    }

    @RequestMapping("/setCommentVisibility")
    @ResponseBody
    public void setCommentVisibility(HttpServletRequest request) {

	Long commentUid = WebUtil.readLongParam(request, KalturaConstants.PARAM_COMMENT_UID);
	boolean isHide = WebUtil.readBooleanParam(request, KalturaConstants.PARAM_IS_HIDING);
	kalturaService.hideComment(commentUid, isHide);

    }

    /**
     * Set Submission Deadline
     *
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws IOException
     */
    @RequestMapping("/setSubmissionDeadline")
    @ResponseBody
    public void setSubmissionDeadline(HttpServletRequest request, HttpServletResponse response) throws IOException {

	Long contentID = WebUtil.readLongParam(request, AttributeNames.PARAM_TOOL_CONTENT_ID);

	Kaltura kaltura = kalturaService.getKalturaByContentId(contentID);

	Long dateParameter = WebUtil.readLongParam(request, KalturaConstants.ATTR_SUBMISSION_DEADLINE, true);
	Date tzSubmissionDeadline = null;
	String formattedDate = "";
	if (dateParameter != null) {
	    Date submissionDeadline = new Date(dateParameter);
	    HttpSession ss = SessionManager.getSession();
	    org.lamsfoundation.lams.usermanagement.dto.UserDTO teacher = (org.lamsfoundation.lams.usermanagement.dto.UserDTO) ss
		    .getAttribute(AttributeNames.USER);
	    TimeZone teacherTimeZone = teacher.getTimeZone();
	    tzSubmissionDeadline = DateUtil.convertFromTimeZoneToDefault(teacherTimeZone, submissionDeadline);
	    formattedDate = DateUtil.convertToStringForJSON(tzSubmissionDeadline, request.getLocale());
	}
	kaltura.setSubmissionDeadline(tzSubmissionDeadline);
	kalturaService.saveOrUpdateKaltura(kaltura);

	response.setContentType("text/plain;charset=utf-8");
	response.getWriter().print(formattedDate);
    }
}

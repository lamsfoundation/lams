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


package org.lamsfoundation.lams.tool.kaltura.web.actions;

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
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionRedirect;
import org.lamsfoundation.lams.tool.kaltura.dto.KalturaSessionDTO;
import org.lamsfoundation.lams.tool.kaltura.dto.KalturaUserDTO;
import org.lamsfoundation.lams.tool.kaltura.dto.NotebookEntryDTO;
import org.lamsfoundation.lams.tool.kaltura.model.Kaltura;
import org.lamsfoundation.lams.tool.kaltura.model.KalturaSession;
import org.lamsfoundation.lams.tool.kaltura.model.KalturaUser;
import org.lamsfoundation.lams.tool.kaltura.service.IKalturaService;
import org.lamsfoundation.lams.tool.kaltura.service.KalturaServiceProxy;
import org.lamsfoundation.lams.tool.kaltura.util.KalturaConstants;
import org.lamsfoundation.lams.tool.kaltura.util.KalturaSessionDTOComparator;
import org.lamsfoundation.lams.usermanagement.dto.UserDTO;
import org.lamsfoundation.lams.util.DateUtil;
import org.lamsfoundation.lams.util.WebUtil;
import org.lamsfoundation.lams.web.action.LamsDispatchAction;
import org.lamsfoundation.lams.web.session.SessionManager;
import org.lamsfoundation.lams.web.util.AttributeNames;

/**
 * @author Andrey Balan
 */
public class MonitoringAction extends LamsDispatchAction {

    private static Logger log = Logger.getLogger(MonitoringAction.class);

    public IKalturaService kalturaService;

    @Override
    public ActionForward unspecified(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {

	setupService();
	request.setAttribute("initialTabId", WebUtil.readLongParam(request, AttributeNames.PARAM_CURRENT_TAB, true));
	Long toolContentID = WebUtil.readLongParam(request, AttributeNames.PARAM_TOOL_CONTENT_ID);
	String contentFolderID = WebUtil.readStrParam(request, AttributeNames.PARAM_CONTENT_FOLDER_ID);

	Kaltura kaltura = kalturaService.getKalturaByContentId(toolContentID);
	boolean isGroupedActivity = kalturaService.isGroupedActivity(toolContentID);
	request.setAttribute(KalturaConstants.ATTR_KALTURA, kaltura);
	request.setAttribute("contentFolderID", contentFolderID);
	request.setAttribute("isGroupedActivity", isGroupedActivity);

	Set<KalturaSessionDTO> sessionDTOs = new TreeSet<KalturaSessionDTO>(new KalturaSessionDTOComparator());
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
	    request.setAttribute(KalturaConstants.ATTR_SUBMISSION_DEADLINE_DATESTRING, DateUtil.convertToStringForJSON(submissionDeadline, request.getLocale()));
	}

	return mapping.findForward("success");
    }

    public ActionForward showGroupLearning(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {

	setupService();

	String toolSessionID = WebUtil.readStrParam(request, AttributeNames.PARAM_TOOL_SESSION_ID);
	UserDTO user = (UserDTO) SessionManager.getSession().getAttribute(AttributeNames.USER);

	ActionRedirect redirect = new ActionRedirect(mapping.findForwardConfig(KalturaConstants.GROUP_LEARNING));
	//to distinguish from opening learning page from monitor's Learners tab pass this parameter
	redirect.addParameter(KalturaConstants.ATTR_IS_GROUP_MONITORING, true);
	redirect.addParameter(AttributeNames.PARAM_MODE, "teacher");
	redirect.addParameter(AttributeNames.PARAM_TOOL_SESSION_ID, toolSessionID);
	redirect.addParameter(AttributeNames.PARAM_USER_ID, user.getUserID());
	return redirect;
    }

    /**
     * Update item's mark. If it's not long then store 0.
     */
    public ActionForward markItem(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {

	setupService();

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

    public ActionForward setItemVisibility(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {

	setupService();

	Long itemUid = WebUtil.readLongParam(request, KalturaConstants.PARAM_ITEM_UID);
	boolean isHide = WebUtil.readBooleanParam(request, KalturaConstants.PARAM_IS_HIDING);
	kalturaService.hideItem(itemUid, isHide);

	return null;
    }

    public ActionForward setCommentVisibility(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {

	setupService();

	Long commentUid = WebUtil.readLongParam(request, KalturaConstants.PARAM_COMMENT_UID);
	boolean isHide = WebUtil.readBooleanParam(request, KalturaConstants.PARAM_IS_HIDING);
	kalturaService.hideComment(commentUid, isHide);

	return null;
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
    public ActionForward setSubmissionDeadline(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws IOException {

	setupService();

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
	    formattedDate = DateUtil.convertToStringForJSON(submissionDeadline, request.getLocale());
	}
	kaltura.setSubmissionDeadline(tzSubmissionDeadline);
	kalturaService.saveOrUpdateKaltura(kaltura);

	response.setContentType("text/plain;charset=utf-8");
	response.getWriter().print(formattedDate);
	return null;
    }

    /**
     * set up kalturaService
     */
    private void setupService() {
	if (kalturaService == null) {
	    kalturaService = KalturaServiceProxy.getKalturaService(this.getServlet().getServletContext());
	}
    }
}

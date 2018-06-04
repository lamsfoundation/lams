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


package org.lamsfoundation.lams.tool.mindmap.web.actions;

import java.io.IOException;
import java.util.Date;
import java.util.TimeZone;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.lamsfoundation.lams.notebook.model.NotebookEntry;
import org.lamsfoundation.lams.tool.ToolAccessMode;
import org.lamsfoundation.lams.tool.mindmap.dto.MindmapDTO;
import org.lamsfoundation.lams.tool.mindmap.dto.MindmapUserDTO;
import org.lamsfoundation.lams.tool.mindmap.model.Mindmap;
import org.lamsfoundation.lams.tool.mindmap.model.MindmapUser;
import org.lamsfoundation.lams.tool.mindmap.service.IMindmapService;
import org.lamsfoundation.lams.tool.mindmap.service.MindmapServiceProxy;
import org.lamsfoundation.lams.tool.mindmap.util.MindmapConstants;
import org.lamsfoundation.lams.usermanagement.dto.UserDTO;
import org.lamsfoundation.lams.util.DateUtil;
import org.lamsfoundation.lams.util.WebUtil;
import org.lamsfoundation.lams.web.action.LamsDispatchAction;
import org.lamsfoundation.lams.web.session.SessionManager;
import org.lamsfoundation.lams.web.util.AttributeNames;

/**
 * @author Ruslan Kazakov
 * @version 1.0.1
 *
 *
 *
 *
 *
 */
public class MonitoringAction extends LamsDispatchAction {

    private static Logger log = Logger.getLogger(MonitoringAction.class);
    public IMindmapService mindmapService;

    /**
     * Default action on page load
     *
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return null
     */
    @Override
    public ActionForward unspecified(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {

	setupService();
	Long toolContentID = new Long(WebUtil.readLongParam(request, AttributeNames.PARAM_TOOL_CONTENT_ID));
	String contentFolderID = WebUtil.readStrParam(request, AttributeNames.PARAM_CONTENT_FOLDER_ID);
	Mindmap mindmap = mindmapService.getMindmapByContentId(toolContentID);

	if (mindmap == null) {
	    log.error("unspecified(): Mindmap is not found!");
	    return null;
	}

	boolean isGroupedActivity = mindmapService.isGroupedActivity(toolContentID);
	MindmapDTO mindmapDTO = new MindmapDTO(mindmap);

	Long currentTab = WebUtil.readLongParam(request, AttributeNames.PARAM_CURRENT_TAB, true);
	mindmapDTO.setCurrentTab(currentTab);

	request.setAttribute("mindmapDTO", mindmapDTO);
	request.setAttribute("contentFolderID", contentFolderID);
	request.setAttribute("isGroupedActivity", isGroupedActivity);

	//set SubmissionDeadline, if any
	if (mindmap.getSubmissionDeadline() != null) {
	    Date submissionDeadline = mindmap.getSubmissionDeadline();
	    HttpSession ss = SessionManager.getSession();
	    UserDTO teacher = (UserDTO) ss.getAttribute(AttributeNames.USER);
	    TimeZone teacherTimeZone = teacher.getTimeZone();
	    Date tzSubmissionDeadline = DateUtil.convertToTimeZoneFromDefault(teacherTimeZone, submissionDeadline);
	    request.setAttribute(MindmapConstants.ATTR_SUBMISSION_DEADLINE, tzSubmissionDeadline.getTime());
	    request.setAttribute(MindmapConstants.ATTR_SUBMISSION_DEADLINE_DATESTRING, DateUtil.convertToStringForJSON(submissionDeadline, request.getLocale()));
	}

	return mapping.findForward("success");
    }

    /**
     * Shows Mindmap Nodes for each learner
     *
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return null
     */
    public ActionForward showMindmap(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {

	setupService();

	Long toolContentID = new Long(WebUtil.readLongParam(request, AttributeNames.PARAM_TOOL_CONTENT_ID));
	Mindmap mindmap = mindmapService.getMindmapByContentId(toolContentID);
	request.setAttribute("isMultiUserMode", mindmap.isMultiUserMode());
	request.setAttribute("mindmapId", mindmap.getUid());
	request.setAttribute("sessionId", WebUtil.readLongParam(request, AttributeNames.PARAM_TOOL_SESSION_ID));
	request.setAttribute("mode", ToolAccessMode.TEACHER);

	if ( ! mindmap.isMultiUserMode() ) {
	    Long userId = new Long(WebUtil.readLongParam(request, "userUID"));
	    MindmapUser mindmapUser = mindmapService.getUserByUID(userId);
	    MindmapUserDTO userDTO = new MindmapUserDTO(mindmapUser);
	    request.setAttribute("userDTO", userDTO);
	    request.setAttribute("userId", mindmapUser.getUid());
	}

	return mapping.findForward("mindmap_display");
    }
    /**
     * Shows Notebook reflection that Learner has done.
     *
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     */
    public ActionForward reflect(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {

	Long userId = WebUtil.readLongParam(request, "userUID", false);
	Long toolContentId = WebUtil.readLongParam(request, "toolContentID", false);
	MindmapUser mindmapUser = mindmapService.getUserByUID(userId);
	Mindmap mindmap = mindmapService.getMindmapByContentId(toolContentId);

	request.setAttribute("reflectTitle", mindmap.getTitle());
	request.setAttribute("mindmapUser", mindmapUser.getFirstName() + " " + mindmapUser.getLastName());
	request.setAttribute("mode", ToolAccessMode.TEACHER);

	// Reflection
	NotebookEntry entry = mindmapService.getEntry(mindmapUser.getEntryUID());
	if (entry != null) {
	    request.setAttribute("reflectEntry", entry.getEntry());
	}

	return mapping.findForward("reflect");
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
	Mindmap mindmap = mindmapService.getMindmapByContentId(contentID);

	Long dateParameter = WebUtil.readLongParam(request, MindmapConstants.ATTR_SUBMISSION_DEADLINE, true);
	Date tzSubmissionDeadline = null;
	String formattedDate = "";
	if (dateParameter != null) {
	    Date submissionDeadline = new Date(dateParameter);
	    HttpSession ss = SessionManager.getSession();
	    UserDTO teacher = (UserDTO) ss.getAttribute(AttributeNames.USER);
	    TimeZone teacherTimeZone = teacher.getTimeZone();
	    tzSubmissionDeadline = DateUtil.convertFromTimeZoneToDefault(teacherTimeZone, submissionDeadline);
	    formattedDate = DateUtil.convertToStringForJSON(tzSubmissionDeadline, request.getLocale());
	}
	mindmap.setSubmissionDeadline(tzSubmissionDeadline);
	mindmapService.saveOrUpdateMindmap(mindmap);
	response.setContentType("text/plain;charset=utf-8");
	response.getWriter().print(formattedDate);
	return null;
    }

    /**
     * Sets mindmapService
     */
    private void setupService() {
	if (mindmapService == null) {
	    mindmapService = MindmapServiceProxy.getMindmapService(this.getServlet().getServletContext());
	}
    }
}

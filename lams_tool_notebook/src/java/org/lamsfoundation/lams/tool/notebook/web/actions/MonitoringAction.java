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
/* $$Id$$ */

package org.lamsfoundation.lams.tool.notebook.web.actions;

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
import org.lamsfoundation.lams.tool.notebook.dto.NotebookDTO;
import org.lamsfoundation.lams.tool.notebook.dto.NotebookEntryDTO;
import org.lamsfoundation.lams.tool.notebook.dto.NotebookSessionDTO;
import org.lamsfoundation.lams.tool.notebook.dto.NotebookUserDTO;
import org.lamsfoundation.lams.tool.notebook.model.Notebook;
import org.lamsfoundation.lams.tool.notebook.model.NotebookUser;
import org.lamsfoundation.lams.tool.notebook.service.INotebookService;
import org.lamsfoundation.lams.tool.notebook.service.NotebookServiceProxy;
import org.lamsfoundation.lams.tool.notebook.util.NotebookConstants;
import org.lamsfoundation.lams.usermanagement.dto.UserDTO;
import org.lamsfoundation.lams.util.DateUtil;
import org.lamsfoundation.lams.util.WebUtil;
import org.lamsfoundation.lams.web.action.LamsDispatchAction;
import org.lamsfoundation.lams.web.session.SessionManager;
import org.lamsfoundation.lams.web.util.AttributeNames;

/**
 * @struts.action path="/monitoring" parameter="dispatch" scope="request" name="monitoringForm" validate="false"
 * 
 * @struts.action-forward name="success" path="tiles:/monitoring/main"
 */
public class MonitoringAction extends LamsDispatchAction {

    private static Logger log = Logger.getLogger(MonitoringAction.class);

    public INotebookService notebookService;

    public ActionForward unspecified(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {

	setupService();
	Long toolContentID = new Long(WebUtil.readLongParam(request, AttributeNames.PARAM_TOOL_CONTENT_ID));
	String contentFolderID = WebUtil.readStrParam(request, AttributeNames.PARAM_CONTENT_FOLDER_ID);
	request.setAttribute("contentFolderID", contentFolderID);

	Notebook notebook = notebookService.getNotebookByContentId(toolContentID);
	if (notebook == null) {
	    // TODO error page.
	}

	boolean isGroupedActivity = notebookService.isGroupedActivity(toolContentID);
	request.setAttribute("isGroupedActivity", isGroupedActivity);

	NotebookDTO notebookDTO = new NotebookDTO(notebook);
	Long currentTab = WebUtil.readLongParam(request, AttributeNames.PARAM_CURRENT_TAB, true);
	notebookDTO.setCurrentTab(currentTab);

	// add the notebookEntry for each user in each session
	for (NotebookSessionDTO session : notebookDTO.getSessionDTOs()) {
	    for (NotebookUserDTO user : session.getUserDTOs()) {
		NotebookEntry entry = notebookService.getEntry(user.getEntryUID());
		if (entry != null) {
		    NotebookEntryDTO entryDTO = new NotebookEntryDTO(entry);
		    user.setEntryDTO(entryDTO);
		}
	    }
	}

	request.setAttribute("notebookDTO", notebookDTO);

	Date submissionDeadline = notebook.getSubmissionDeadline();
	if (submissionDeadline != null) {
	    HttpSession ss = SessionManager.getSession();
	    UserDTO teacher = (UserDTO) ss.getAttribute(AttributeNames.USER);
	    TimeZone teacherTimeZone = teacher.getTimeZone();
	    Date tzSubmissionDeadline = DateUtil.convertToTimeZoneFromDefault(teacherTimeZone, submissionDeadline);
	    request.setAttribute(NotebookConstants.ATTR_SUBMISSION_DEADLINE, tzSubmissionDeadline.getTime());
	}

	return mapping.findForward("success");
    }

    /**
     * Updates a user's mark or feedback for an entire lesson
     * 
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    public ActionForward saveTeacherComment(ActionMapping mapping, ActionForm form,
	    HttpServletRequest request, HttpServletResponse response) throws Exception {
	setupService();
	
	String teachersComment = WebUtil.readStrParam(request, "teachersComment");
	Long userUid = WebUtil.readLongParam(request, NotebookConstants.PARAM_USER_UID);
	NotebookUser user = notebookService.getUserByUID(userUid);
	
	//check user had available notebook entry
	if (user.getEntryUID() != null) {
	    Long toolSessionID = user.getNotebookSession().getSessionId();
	    user.setTeachersComment(teachersComment);
	    notebookService.saveOrUpdateNotebookUser(user);
	}

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
     */
    public ActionForward setSubmissionDeadline(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {

	setupService();

	Long contentID = WebUtil.readLongParam(request, AttributeNames.PARAM_TOOL_CONTENT_ID);

	Notebook notebook = notebookService.getNotebookByContentId(contentID);

	Long dateParameter = WebUtil.readLongParam(request, NotebookConstants.ATTR_SUBMISSION_DEADLINE, true);
	Date tzSubmissionDeadline = null;
	if (dateParameter != null) {
	    Date submissionDeadline = new Date(dateParameter);
	    HttpSession ss = SessionManager.getSession();
	    org.lamsfoundation.lams.usermanagement.dto.UserDTO teacher = (org.lamsfoundation.lams.usermanagement.dto.UserDTO) ss
		    .getAttribute(AttributeNames.USER);
	    TimeZone teacherTimeZone = teacher.getTimeZone();
	    tzSubmissionDeadline = DateUtil.convertFromTimeZoneToDefault(teacherTimeZone, submissionDeadline);
	}
	notebook.setSubmissionDeadline(tzSubmissionDeadline);
	notebookService.saveOrUpdateNotebook(notebook);
	return null;
    }

    /**
     * set up notebookService
     */
    private void setupService() {
	if (notebookService == null) {
	    notebookService = NotebookServiceProxy.getNotebookService(this.getServlet().getServletContext());
	}
    }
}

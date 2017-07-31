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


package org.lamsfoundation.lams.tool.scribe.web.actions;

import java.io.IOException;
import java.util.Iterator;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.tomcat.util.json.JSONException;
import org.lamsfoundation.lams.notebook.model.NotebookEntry;
import org.lamsfoundation.lams.notebook.service.CoreNotebookConstants;
import org.lamsfoundation.lams.tool.scribe.dto.ScribeDTO;
import org.lamsfoundation.lams.tool.scribe.dto.ScribeSessionDTO;
import org.lamsfoundation.lams.tool.scribe.dto.ScribeUserDTO;
import org.lamsfoundation.lams.tool.scribe.model.Scribe;
import org.lamsfoundation.lams.tool.scribe.model.ScribeSession;
import org.lamsfoundation.lams.tool.scribe.model.ScribeUser;
import org.lamsfoundation.lams.tool.scribe.service.IScribeService;
import org.lamsfoundation.lams.tool.scribe.service.ScribeServiceProxy;
import org.lamsfoundation.lams.tool.scribe.util.ScribeConstants;
import org.lamsfoundation.lams.tool.scribe.util.ScribeUtils;
import org.lamsfoundation.lams.tool.scribe.web.forms.MonitoringForm;
import org.lamsfoundation.lams.usermanagement.dto.UserDTO;
import org.lamsfoundation.lams.util.WebUtil;
import org.lamsfoundation.lams.web.action.LamsDispatchAction;
import org.lamsfoundation.lams.web.session.SessionManager;
import org.lamsfoundation.lams.web.util.AttributeNames;

/**
 * @author
 * @version
 *
 *
 *
 *
 *
 *
 *
 */
public class MonitoringAction extends LamsDispatchAction {

    private static Logger log = Logger.getLogger(MonitoringAction.class);

    public IScribeService scribeService;

    @Override
    public ActionForward unspecified(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {
	log.info("excuting monitoring action");

	Long toolContentID = new Long(WebUtil.readLongParam(request, AttributeNames.PARAM_TOOL_CONTENT_ID));

	String contentFolderID = WebUtil.readStrParam(request, AttributeNames.PARAM_CONTENT_FOLDER_ID);
	MonitoringForm monForm = (MonitoringForm) form;
	monForm.setContentFolderID(contentFolderID);

	monForm.setCurrentTab(WebUtil.readLongParam(request, AttributeNames.PARAM_CURRENT_TAB, true));

	// set up scribeService
	if (scribeService == null) {
	    scribeService = ScribeServiceProxy.getScribeService(this.getServlet().getServletContext());
	}
	Scribe scribe = scribeService.getScribeByContentId(toolContentID);

	ScribeDTO scribeDTO = setupScribeDTO(scribe);
	boolean isGroupedActivity = scribeService.isGroupedActivity(toolContentID);
	request.setAttribute("isGroupedActivity", isGroupedActivity);
	request.setAttribute("monitoringDTO", scribeDTO);
	request.setAttribute("contentFolderID", contentFolderID);

	return mapping.findForward("success");
    }

    public ActionForward openNotebook(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {

	Long uid = WebUtil.readLongParam(request, "uid", false);

	ScribeUser scribeUser = scribeService.getUserByUID(uid);
	NotebookEntry notebookEntry = scribeService.getEntry(scribeUser.getScribeSession().getSessionId(),
		CoreNotebookConstants.NOTEBOOK_TOOL, ScribeConstants.TOOL_SIGNATURE, scribeUser.getUserId().intValue());

	ScribeUserDTO scribeUserDTO = new ScribeUserDTO(scribeUser);
	scribeUserDTO.setNotebookEntry(notebookEntry.getEntry());

	request.setAttribute("scribeUserDTO", scribeUserDTO);

	return mapping.findForward("notebook");
    }

    public ActionForward appointScribe(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {

	MonitoringForm monForm = (MonitoringForm) form;

	ScribeSession session = scribeService.getSessionBySessionId(monForm.getToolSessionID());
	ScribeUser user = scribeService.getUserByUID(monForm.getAppointedScribeUID());

	session.setAppointedScribe(user);
	scribeService.saveOrUpdateScribeSession(session);

	ScribeDTO scribeDTO = setupScribeDTO(session.getScribe());
	boolean isGroupedActivity = scribeService.isGroupedActivity(session.getScribe().getToolContentId());
	request.setAttribute("isGroupedActivity", isGroupedActivity);
	request.setAttribute("monitoringDTO", scribeDTO);
	request.setAttribute("contentFolderID", monForm.getContentFolderID());

	monForm.setCurrentTab(WebUtil.readLongParam(request, AttributeNames.PARAM_CURRENT_TAB, true));

	return mapping.findForward("success");
    }

    public ActionForward forceCompleteActivity(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws JSONException, IOException {

	MonitoringForm monForm = (MonitoringForm) form;

	ScribeSession session = scribeService.getSessionBySessionId(monForm.getToolSessionID());
	session.setForceComplete(true);
	scribeService.saveOrUpdateScribeSession(session);

	ScribeDTO scribeDTO = setupScribeDTO(session.getScribe());
	request.setAttribute("monitoringDTO", scribeDTO);
	request.setAttribute("contentFolderID", monForm.getContentFolderID());

	return mapping.findForward("success");
    }

    /* Private Methods */

    private ScribeDTO setupScribeDTO(Scribe scribe) {
	ScribeDTO scribeDTO = new ScribeDTO(scribe);

	for (Iterator sessIter = scribe.getScribeSessions().iterator(); sessIter.hasNext();) {
	    ScribeSession session = (ScribeSession) sessIter.next();

	    ScribeSessionDTO sessionDTO = new ScribeSessionDTO(session);
	    int numberOfVotes = 0;

	    for (Iterator userIter = session.getScribeUsers().iterator(); userIter.hasNext();) {
		ScribeUser user = (ScribeUser) userIter.next();
		ScribeUserDTO userDTO = new ScribeUserDTO(user);

		// get the notebook entry.
		NotebookEntry notebookEntry = scribeService.getEntry(session.getSessionId(),
			CoreNotebookConstants.NOTEBOOK_TOOL, ScribeConstants.TOOL_SIGNATURE,
			user.getUserId().intValue());
		if (notebookEntry != null) {
		    userDTO.finishedReflection = true;
		} else {
		    userDTO.finishedReflection = false;
		}

		if (user.isReportApproved()) {
		    numberOfVotes++;
		}

		sessionDTO.getUserDTOs().add(userDTO);
	    }
	    int numberOfLearners = session.getScribeUsers().size();

	    sessionDTO.setNumberOfVotes(numberOfVotes);
	    sessionDTO.setNumberOfLearners(numberOfLearners);
	    sessionDTO.setVotePercentage(ScribeUtils.calculateVotePercentage(numberOfVotes, numberOfLearners));
	    scribeDTO.getSessionDTOs().add(sessionDTO);
	}

	return scribeDTO;

    }

    private ScribeUser getCurrentUser(Long toolSessionID) {
	UserDTO user = (UserDTO) SessionManager.getSession().getAttribute(AttributeNames.USER);

	// attempt to retrieve user using userId and toolSessionID
	ScribeUser scribeUser = scribeService.getUserByUserIdAndSessionId(new Long(user.getUserID().intValue()),
		toolSessionID);

	if (scribeUser == null) {
	    ScribeSession scribeSession = scribeService.getSessionBySessionId(toolSessionID);
	    scribeUser = scribeService.createScribeUser(user, scribeSession);
	}

	return scribeUser;
    }
}

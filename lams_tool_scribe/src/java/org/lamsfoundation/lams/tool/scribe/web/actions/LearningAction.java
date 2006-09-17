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
/* $$Id$ */

package org.lamsfoundation.lams.tool.scribe.web.actions;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.lamsfoundation.lams.notebook.model.NotebookEntry;
import org.lamsfoundation.lams.notebook.service.CoreNotebookConstants;
import org.lamsfoundation.lams.tool.ToolAccessMode;
import org.lamsfoundation.lams.tool.ToolSessionManager;
import org.lamsfoundation.lams.tool.exception.DataMissingException;
import org.lamsfoundation.lams.tool.exception.ToolException;
import org.lamsfoundation.lams.tool.scribe.dto.ScribeDTO;
import org.lamsfoundation.lams.tool.scribe.dto.ScribeUserDTO;
import org.lamsfoundation.lams.tool.scribe.model.Scribe;
import org.lamsfoundation.lams.tool.scribe.model.ScribeSession;
import org.lamsfoundation.lams.tool.scribe.model.ScribeUser;
import org.lamsfoundation.lams.tool.scribe.service.IScribeService;
import org.lamsfoundation.lams.tool.scribe.service.ScribeServiceProxy;
import org.lamsfoundation.lams.tool.scribe.util.ScribeConstants;
import org.lamsfoundation.lams.tool.scribe.util.ScribeException;
import org.lamsfoundation.lams.tool.scribe.web.forms.LearningForm;
import org.lamsfoundation.lams.usermanagement.dto.UserDTO;
import org.lamsfoundation.lams.util.WebUtil;
import org.lamsfoundation.lams.web.action.LamsDispatchAction;
import org.lamsfoundation.lams.web.session.SessionManager;
import org.lamsfoundation.lams.web.util.AttributeNames;

/**
 * @author
 * @version
 * 
 * @struts.action path="/learning" parameter="dispatch" scope="request"
 *                name="learningForm"
 * @struts.action-forward name="learning" path="tiles:/learning/main"
 * @struts.action-forward name="runOffline" path="tiles:/learning/runOffline"
 * @struts.action-forward name="defineLater" path="tiles:/learning/defineLater"
 * @struts.action-forward name="notebook" path="tiles:/learning/notebook"
 */
public class LearningAction extends LamsDispatchAction {

	private static Logger log = Logger.getLogger(LearningAction.class);

	private static final boolean MODE_OPTIONAL = false;

	private IScribeService scribeService;

	public ActionForward unspecified(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		// 'toolSessionID' and 'mode' paramters are expected to be present.
		// TODO need to catch exceptions and handle errors.
		ToolAccessMode mode = WebUtil.readToolAccessModeParam(request,
				AttributeNames.PARAM_MODE, MODE_OPTIONAL);

		Long toolSessionID = WebUtil.readLongParam(request,
				AttributeNames.PARAM_TOOL_SESSION_ID);

		// set up scribeService
		if (scribeService == null) {
			scribeService = ScribeServiceProxy.getScribeService(this.getServlet()
					.getServletContext());
		}

		// Retrieve the session and content.
		ScribeSession scribeSession = scribeService
				.getSessionBySessionId(toolSessionID);
		if (scribeSession == null) {
			throw new ScribeException(
					"Cannot retreive session with toolSessionID"
							+ toolSessionID);
		}

		Scribe scribe = scribeSession.getScribe();

		// Retrieve the current user
		ScribeUser scribeUser = getCurrentUser(toolSessionID);

		// check defineLater
		if (scribe.getDefineLater()) {
			return mapping.findForward("defineLater");
		}

		request.setAttribute("MODE", mode.toString());

		ScribeDTO scribeDTO = new ScribeDTO(scribe);
		request.setAttribute("scribeDTO", scribeDTO);
		
		ScribeUserDTO scribeUserDTO = new ScribeUserDTO(scribeUser);
		if (scribeUser.getFinishedActivity()) {
			// get the notebook entry.
			NotebookEntry notebookEntry = scribeService.getEntry(toolSessionID,
					CoreNotebookConstants.NOTEBOOK_TOOL,
					ScribeConstants.TOOL_SIGNATURE, scribeUser.getUserId()
							.intValue());
			if (notebookEntry != null) {
				scribeUserDTO.notebookEntry = notebookEntry.getEntry();
			}
		}
		request.setAttribute("scribeUserDTO", scribeUserDTO);		
		
		// Ensure that the content is use flag is set.
		if (!scribe.getContentInUse()) {
			scribe.setContentInUse(new Boolean(true));
			scribeService.saveOrUpdateScribe(scribe);
		}
		
		// check runOffline
		if (scribe.getRunOffline()) {
			return mapping.findForward("runOffline");
		}
		
		return mapping.findForward("learning");
		
	}

	private ScribeUser getCurrentUser(Long toolSessionId) {
		UserDTO user = (UserDTO) SessionManager.getSession().getAttribute(
				AttributeNames.USER);

		// attempt to retrieve user using userId and toolSessionId
		ScribeUser scribeUser = scribeService.getUserByUserIdAndSessionId(new Long(
				user.getUserID().intValue()), toolSessionId);

		if (scribeUser == null) {
			ScribeSession scribeSession = scribeService
					.getSessionBySessionId(toolSessionId);
			scribeUser = scribeService.createScribeUser(user, scribeSession);
		}

		return scribeUser;
	}

	public ActionForward finishActivity(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {

		LearningForm lrnForm = (LearningForm) form;

		// set the finished flag
		ScribeUser scribeUser = scribeService.getUserByUID(lrnForm.getScribeUserUID());
		if (scribeUser != null) {
			scribeUser.setFinishedActivity(true);
			scribeService.saveOrUpdateScribeUser(scribeUser);
		} else {
			log.error("finishActivity(): couldn't find ScribeUser with uid: "
					+ lrnForm.getScribeUserUID());
		}

		ToolSessionManager sessionMgrService = ScribeServiceProxy
				.getScribeSessionManager(getServlet().getServletContext());

		HttpSession ss = SessionManager.getSession();
		UserDTO user = (UserDTO) ss.getAttribute(AttributeNames.USER);
		Long userID = new Long(user.getUserID().longValue());
		Long toolSessionID = scribeUser.getScribeSession().getSessionId();

		String nextActivityUrl;
		try {
			nextActivityUrl = sessionMgrService.leaveToolSession(toolSessionID,
					userID);
			response.sendRedirect(nextActivityUrl);
		} catch (DataMissingException e) {
			throw new ScribeException(e);
		} catch (ToolException e) {
			throw new ScribeException(e);
		} catch (IOException e) {
			throw new ScribeException(e);
		}

		return null; // TODO need to return proper page.
	}

	public ActionForward openNotebook(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {

		LearningForm lrnForm = (LearningForm) form;

		// set the finished flag
		ScribeUser scribeUser = scribeService.getUserByUID(lrnForm.getScribeUserUID());
		ScribeDTO scribeDTO = new ScribeDTO(scribeUser.getScribeSession().getScribe());

		request.setAttribute("scribeDTO", scribeDTO);
		return mapping.findForward("notebook");
	}

	public ActionForward submitReflection(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) {

		// save the reflection entry and call the notebook.

		LearningForm lrnForm = (LearningForm) form;

		ScribeUser scribeUser = scribeService.getUserByUID(lrnForm.getScribeUserUID());

		scribeService.createNotebookEntry(scribeUser.getScribeSession()
				.getSessionId(), CoreNotebookConstants.NOTEBOOK_TOOL,
				ScribeConstants.TOOL_SIGNATURE, scribeUser.getUserId().intValue(),
				lrnForm.getEntryText());

		return finishActivity(mapping, form, request, response);
	}

}

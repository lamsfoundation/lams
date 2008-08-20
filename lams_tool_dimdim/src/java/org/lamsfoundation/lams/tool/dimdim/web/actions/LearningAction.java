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

package org.lamsfoundation.lams.tool.dimdim.web.actions;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;
import org.lamsfoundation.lams.notebook.model.NotebookEntry;
import org.lamsfoundation.lams.notebook.service.CoreNotebookConstants;
import org.lamsfoundation.lams.tool.ToolAccessMode;
import org.lamsfoundation.lams.tool.ToolSessionManager;
import org.lamsfoundation.lams.tool.dimdim.dto.ContentDTO;
import org.lamsfoundation.lams.tool.dimdim.model.Dimdim;
import org.lamsfoundation.lams.tool.dimdim.model.DimdimSession;
import org.lamsfoundation.lams.tool.dimdim.model.DimdimUser;
import org.lamsfoundation.lams.tool.dimdim.service.DimdimServiceProxy;
import org.lamsfoundation.lams.tool.dimdim.service.IDimdimService;
import org.lamsfoundation.lams.tool.dimdim.util.Constants;
import org.lamsfoundation.lams.tool.dimdim.util.DimdimException;
import org.lamsfoundation.lams.tool.dimdim.web.forms.LearningForm;
import org.lamsfoundation.lams.tool.exception.DataMissingException;
import org.lamsfoundation.lams.tool.exception.ToolException;
import org.lamsfoundation.lams.usermanagement.dto.UserDTO;
import org.lamsfoundation.lams.util.WebUtil;
import org.lamsfoundation.lams.web.session.SessionManager;
import org.lamsfoundation.lams.web.util.AttributeNames;

/**
 * @author Anthony Sukkar
 * 
 * @struts.action path="/learning" parameter="dispatch" scope="request"
 *                name="learningForm"
 * @struts.action-forward name="dimdim" path="tiles:/learning/main"
 * @struts.action-forward name="runOffline" path="tiles:/learning/runOffline"
 * @struts.action-forward name="defineLater" path="tiles:/learning/defineLater"
 */
public class LearningAction extends DispatchAction {

	private static final Logger logger = Logger.getLogger(LearningAction.class);

	private IDimdimService dimdimService;

	@Override
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		// set up dimdimService
		dimdimService = DimdimServiceProxy.getDimdimService(this.getServlet()
				.getServletContext());

		return super.execute(mapping, form, request, response);
	}

	public ActionForward unspecified(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		LearningForm learningForm = (LearningForm) form;

		// 'toolSessionID' and 'mode' parameters are expected to be present.
		// TODO need to catch exceptions and handle errors.
		ToolAccessMode mode = WebUtil.readToolAccessModeParam(request,
				AttributeNames.PARAM_MODE, false);

		Long toolSessionID = WebUtil.readLongParam(request,
				AttributeNames.PARAM_TOOL_SESSION_ID);

		// Retrieve the session and content.
		DimdimSession dimdimSession = dimdimService
				.getSessionBySessionId(toolSessionID);
		if (dimdimSession == null) {
			throw new DimdimException(
					"Cannot retrieve session with toolSessionID"
							+ toolSessionID);
		}

		Dimdim dimdim = dimdimSession.getDimdim();

		// check defineLater
		if (dimdim.isDefineLater()) {
			return mapping.findForward("defineLater");
		}

		// set mode, toolSessionID and DimdimDTO
		request.setAttribute(AttributeNames.ATTR_MODE, mode.toString());
		learningForm.setToolSessionID(toolSessionID);

		ContentDTO contentDTO = new ContentDTO();
		contentDTO.title = dimdim.getTitle();
		contentDTO.instructions = dimdim.getInstructions();
		contentDTO.allowRichEditor = dimdim.isAllowRichEditor();
		contentDTO.lockOnFinish = dimdim.isLockOnFinished();

		request.setAttribute(Constants.ATTR_CONTENT_DTO, contentDTO);

		// Set the content in use flag.
		if (!dimdim.isContentInUse()) {
			dimdim.setContentInUse(true);
			dimdimService.saveOrUpdateDimdim(dimdim);
		}

		// check runOffline
		if (dimdim.isRunOffline()) {
			return mapping.findForward("runOffline");
		}

		DimdimUser dimdimUser;
		if (mode.equals(ToolAccessMode.TEACHER)) {
			Long userID = WebUtil.readLongParam(request,
					AttributeNames.PARAM_USER_ID, false);
			dimdimUser = dimdimService.getUserByUserIdAndSessionId(userID,
					toolSessionID);
		} else {
			dimdimUser = getCurrentUser(toolSessionID);
		}

		// get any existing dimdim entry
		NotebookEntry nbEntry = null;
		if (dimdimUser != null) {
			nbEntry = dimdimService.getEntry(dimdimUser.getEntryUID());
		}
		if (nbEntry != null) {
			learningForm.setEntryText(nbEntry.getEntry());
		}

		// set readOnly flag.
		if (mode.equals(ToolAccessMode.TEACHER)
				|| (dimdim.isLockOnFinished() && dimdimUser
						.isFinishedActivity())) {
			request.setAttribute("contentEditable", false);
		} else {
			request.setAttribute("contentEditable", true);
		}
		request.setAttribute(Constants.ATTR_FINISHED_ACTIVITY, dimdimUser
				.isFinishedActivity());

		return mapping.findForward("dimdim");
	}

	private DimdimUser getCurrentUser(Long toolSessionId) {
		UserDTO user = (UserDTO) SessionManager.getSession().getAttribute(
				AttributeNames.USER);

		// attempt to retrieve user using userId and toolSessionId
		DimdimUser dimdimUser = dimdimService.getUserByUserIdAndSessionId(
				new Long(user.getUserID().intValue()), toolSessionId);

		if (dimdimUser == null) {
			DimdimSession dimdimSession = dimdimService
					.getSessionBySessionId(toolSessionId);
			dimdimUser = dimdimService.createDimdimUser(user, dimdimSession);
		}

		return dimdimUser;
	}

	public ActionForward finishActivity(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {

		Long toolSessionID = WebUtil.readLongParam(request,
				AttributeNames.PARAM_TOOL_SESSION_ID);

		DimdimUser dimdimUser = getCurrentUser(toolSessionID);

		if (dimdimUser != null) {

			LearningForm learningForm = (LearningForm) form;

			// TODO fix idType to use real value not 999

			if (dimdimUser.getEntryUID() == null) {
				dimdimUser.setEntryUID(dimdimService.createNotebookEntry(
						toolSessionID, CoreNotebookConstants.NOTEBOOK_TOOL,
						Constants.TOOL_SIGNATURE, dimdimUser.getUserId()
								.intValue(), learningForm.getEntryText()));
			} else {
				// update existing entry.
				dimdimService.updateEntry(dimdimUser.getEntryUID(),
						learningForm.getEntryText());
			}

			dimdimUser.setFinishedActivity(true);
			dimdimService.saveOrUpdateDimdimUser(dimdimUser);
		} else {
			logger.error("finishActivity(): couldn't find DimdimUser with id: "
					+ dimdimUser.getUserId() + "and toolSessionID: "
					+ toolSessionID);
		}

		ToolSessionManager sessionMgrService = DimdimServiceProxy
				.getDimdimSessionManager(getServlet().getServletContext());

		String nextActivityUrl;
		try {
			nextActivityUrl = sessionMgrService.leaveToolSession(toolSessionID,
					dimdimUser.getUserId());
			response.sendRedirect(nextActivityUrl);
		} catch (DataMissingException e) {
			throw new DimdimException(e);
		} catch (ToolException e) {
			throw new DimdimException(e);
		} catch (IOException e) {
			throw new DimdimException(e);
		}

		return null; // TODO need to return proper page.
	}
}

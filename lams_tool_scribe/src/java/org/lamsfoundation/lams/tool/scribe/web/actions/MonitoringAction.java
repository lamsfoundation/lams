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

import java.util.Iterator;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
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
import org.lamsfoundation.lams.usermanagement.dto.UserDTO;
import org.lamsfoundation.lams.util.WebUtil;
import org.lamsfoundation.lams.web.action.LamsDispatchAction;
import org.lamsfoundation.lams.web.session.SessionManager;
import org.lamsfoundation.lams.web.util.AttributeNames;

/**
 * @author
 * @version
 * 
 * @struts.action path="/monitoring" parameter="dispatch" scope="request"
 *                name="monitoringForm" validate="false"
 * 
 * @struts.action-forward name="success" path="tiles:/monitoring/main"
 * @struts.action-forward name="scribe_client"
 *                        path="tiles:/monitoring/scribe_client"
 * @struts.action-forward name="scribe_history"
 *                        path="tiles:/monitoring/scribe_history"
 * 
 * @struts.action-forward name="notebook" path="tiles:/monitoring/notebook"
 * 
 */
public class MonitoringAction extends LamsDispatchAction {

	private static Logger log = Logger.getLogger(MonitoringAction.class);

	public IScribeService scribeService;

	public ActionForward unspecified(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		log.info("excuting monitoring action");

		Long toolContentID = new Long(WebUtil.readLongParam(request,
				AttributeNames.PARAM_TOOL_CONTENT_ID));

		// set up scribeService
		if (scribeService == null) {
			scribeService = ScribeServiceProxy.getScribeService(this.getServlet()
					.getServletContext());
		}
		Scribe scribe = scribeService.getScribeByContentId(toolContentID);
		ScribeDTO scribeDTO = new ScribeDTO(scribe);

		for (Iterator sessIter = scribe.getScribeSessions().iterator(); sessIter
				.hasNext();) {
			ScribeSession session = (ScribeSession) sessIter.next();

			ScribeSessionDTO sessionDTO = new ScribeSessionDTO(session);
			
			for (Iterator userIter = session.getScribeUsers().iterator(); userIter
					.hasNext();) {
				ScribeUser user = (ScribeUser) userIter.next();
				ScribeUserDTO userDTO = new ScribeUserDTO(user);
								
				// get the notebook entry.
				NotebookEntry notebookEntry = scribeService.getEntry(session.getSessionId(),
						CoreNotebookConstants.NOTEBOOK_TOOL,
						ScribeConstants.TOOL_SIGNATURE, user.getUserId()
								.intValue());
				if (notebookEntry != null) {
					userDTO.finishedReflection = true;
				} else {
					userDTO.finishedReflection = false;
				}			
				
				sessionDTO.getUserDTOs().add(userDTO);
			}

			scribeDTO.getSessionDTOs().add(sessionDTO);
		}
		request.setAttribute("monitoringDTO", scribeDTO);
		return mapping.findForward("success");
	}
	
	public ActionForward openNotebook(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) {
		
		Long uid = WebUtil.readLongParam(request, "uid", false);
		
		ScribeUser scribeUser = scribeService.getUserByUID(uid);
		NotebookEntry notebookEntry = scribeService.getEntry(scribeUser.getScribeSession().getSessionId(), CoreNotebookConstants.NOTEBOOK_TOOL, ScribeConstants.TOOL_SIGNATURE, scribeUser.getUserId().intValue());
		
		ScribeUserDTO scribeUserDTO = new ScribeUserDTO(scribeUser);
		scribeUserDTO.setNotebookEntry(notebookEntry.getEntry());
		
		request.setAttribute("scribeUserDTO", scribeUserDTO);
		
		return mapping.findForward("notebook");
	}

	/* Private Methods */

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
}

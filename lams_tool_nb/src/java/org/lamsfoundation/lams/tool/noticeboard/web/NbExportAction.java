/****************************************************************
 * Copyright (C) 2005 LAMS Foundation (http://lamsfoundation.org)
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
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301
 * USA
 *
 * http://www.gnu.org/licenses/gpl.txt
 * ****************************************************************
 */

/* $$Id$$ */
package org.lamsfoundation.lams.tool.noticeboard.web;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.lamsfoundation.lams.notebook.model.NotebookEntry;
import org.lamsfoundation.lams.notebook.service.CoreNotebookConstants;
import org.lamsfoundation.lams.tool.noticeboard.NbApplicationException;
import org.lamsfoundation.lams.tool.noticeboard.NoticeboardConstants;
import org.lamsfoundation.lams.tool.noticeboard.NoticeboardContent;
import org.lamsfoundation.lams.tool.noticeboard.NoticeboardSession;
import org.lamsfoundation.lams.tool.noticeboard.NoticeboardUser;
import org.lamsfoundation.lams.tool.noticeboard.dto.ReflectionDTO;
import org.lamsfoundation.lams.tool.noticeboard.service.INoticeboardService;
import org.lamsfoundation.lams.tool.noticeboard.service.NoticeboardServiceProxy;
import org.lamsfoundation.lams.tool.noticeboard.util.NbWebUtil;
import org.lamsfoundation.lams.web.action.LamsDispatchAction;

/**
 * @author mtruong
 *
 * Export Portfolio functionality.
 *
 * With this noticeboard tool,
 * both the learner and teacher will export the contents of the noticeboard.
 *
 */

/**
 * ----------------XDoclet Tags--------------------
 *
 * @struts:action path="/exportPortfolio" name="NbExportForm" scope="request"
 *                type="org.lamsfoundation.lams.tool.noticeboard.web.NbExportAction"
 *                validate="false" parameter="mode"
 * @struts:action-forward name="exportPortfolio" path="/exportPortfolio.jsp"
 *                        ----------------XDoclet Tags--------------------
 */
public class NbExportAction extends LamsDispatchAction {

    static Logger logger = Logger.getLogger(NbExportForm.class.getName());

    @Override
    public ActionForward unspecified(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {
	return mapping.findForward(NoticeboardConstants.EXPORT_PORTFOLIO);
    }

    public ActionForward learner(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {
	//parameters given are the toolSessionId and userId
	NbExportForm exportForm = (NbExportForm) form;
	Long toolSessionId = NbWebUtil.convertToLong(request.getParameter(NoticeboardConstants.TOOL_SESSION_ID));
	Long userId = NbWebUtil.convertToLong(request.getParameter(NoticeboardConstants.USER_ID));

	INoticeboardService nbService = NoticeboardServiceProxy.getNbService(getServlet().getServletContext());

	if (userId == null || toolSessionId == null) {
	    String error = "Tool session Id or user Id is null. Unable to continue";
	    logger.error(error);
	    throw new NbApplicationException(error);
	}

	NoticeboardUser userInThisSession = nbService.retrieveNbUserBySession(userId, toolSessionId);

	if (userInThisSession == null) {
	    String error = "The user with user id " + userId
		    + " does not exist in this session or session may not exist.";
	    logger.error(error);
	    throw new NbApplicationException(error);
	}

	NoticeboardContent content = nbService.retrieveNoticeboardBySessionID(toolSessionId);

	if (content == null) {
	    String error = "The content for this activity has not been defined yet.";
	    logger.error(error);
	    throw new NbApplicationException(error);
	}

	// Get user's reflection if exists
	if (content.getReflectOnActivity()) {
	    log.debug(content.getReflectOnActivity());
	    request.setAttribute("learner", true);
	    NotebookEntry nbEntry = nbService.getEntry(userInThisSession.getNbSession().getNbSessionId(),
		    CoreNotebookConstants.NOTEBOOK_TOOL, NoticeboardConstants.TOOL_SIGNATURE, userId.intValue());
	    log.debug(nbEntry);
	    if (nbEntry != null) {
		request.setAttribute("nbEntry", nbEntry.getEntry());
	    }
	}

	exportForm.populateForm(content);

	return mapping.findForward(NoticeboardConstants.EXPORT_PORTFOLIO);
    }

    public ActionForward teacher(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {
	//given the toolcontentId as a parameter
	NbExportForm exportForm = (NbExportForm) form;
	INoticeboardService nbService = NoticeboardServiceProxy.getNbService(getServlet().getServletContext());

	Long toolContentId = NbWebUtil.convertToLong(request.getParameter(NoticeboardConstants.TOOL_CONTENT_ID));

	//check if toolContentId exists in db or not
	if (toolContentId == null) {
	    String error = "Tool Content Id is missing. Unable to continue";
	    logger.error(error);
	    throw new NbApplicationException(error);
	}

	NoticeboardContent content = nbService.retrieveNoticeboard(toolContentId);

	if (content == null) {
	    String error = "Data is missing from the database. Unable to Continue";
	    logger.error(error);
	    throw new NbApplicationException(error);
	}

	// Get class's reflections if exists
	if (content.getReflectOnActivity()) {
	    Set sessions = content.getNbSessions();
	    Iterator i = sessions.iterator();
	    List<ReflectionDTO> reflections = new ArrayList<ReflectionDTO>();
	    while (i.hasNext()) {
		NoticeboardSession session = (NoticeboardSession) i.next();
		List sessionUsers = nbService.getUsersBySession(session.getNbSessionId());
		for (int j = 0; j < sessionUsers.size(); j++) {
		    NoticeboardUser nbUser = (NoticeboardUser) sessionUsers.get(j);
		    NotebookEntry nbEntry = nbService.getEntry(nbUser.getNbSession().getNbSessionId(),
			    CoreNotebookConstants.NOTEBOOK_TOOL, NoticeboardConstants.TOOL_SIGNATURE,
			    nbUser.getUserId().intValue());
		    log.debug(nbEntry);
		    if (nbEntry != null) {
			ReflectionDTO dto = new ReflectionDTO(nbEntry);
			dto.setFullName(nbUser.getFullname());
			reflections.add(dto);
		    }
		}
	    }
	    request.setAttribute("reflections", reflections);
	}

	exportForm.populateForm(content);

	return mapping.findForward(NoticeboardConstants.EXPORT_PORTFOLIO);
    }

}

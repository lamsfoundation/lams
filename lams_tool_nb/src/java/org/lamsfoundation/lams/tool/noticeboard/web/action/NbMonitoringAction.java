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


package org.lamsfoundation.lams.tool.noticeboard.web.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.lamsfoundation.lams.notebook.model.NotebookEntry;
import org.lamsfoundation.lams.notebook.service.CoreNotebookConstants;
import org.lamsfoundation.lams.tool.noticeboard.NoticeboardConstants;
import org.lamsfoundation.lams.tool.noticeboard.NoticeboardContent;
import org.lamsfoundation.lams.tool.noticeboard.NoticeboardSession;
import org.lamsfoundation.lams.tool.noticeboard.NoticeboardUser;
import org.lamsfoundation.lams.tool.noticeboard.dto.ReflectionDTO;
import org.lamsfoundation.lams.tool.noticeboard.service.INoticeboardService;
import org.lamsfoundation.lams.tool.noticeboard.service.NoticeboardServiceProxy;
import org.lamsfoundation.lams.tool.noticeboard.util.NbApplicationException;
import org.lamsfoundation.lams.tool.noticeboard.util.NbWebUtil;
import org.lamsfoundation.lams.tool.noticeboard.web.form.NbMonitoringForm;
import org.lamsfoundation.lams.util.WebUtil;
import org.lamsfoundation.lams.web.action.LamsDispatchAction;
import org.lamsfoundation.lams.web.util.AttributeNames;

/**
 * The buttons are a switch between tabs and will forward to a jsp and display
 * the appropriate page.
 * 
 * @author mtruong
 */
public class NbMonitoringAction extends LamsDispatchAction {

    static Logger logger = Logger.getLogger(NbMonitoringAction.class.getName());

    public final static String FORM = "NbMonitoringForm";

    @Override
    public ActionForward unspecified(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws NbApplicationException {
	Long toolContentId = NbWebUtil.convertToLong(request.getParameter(NoticeboardConstants.TOOL_CONTENT_ID));
	String contentFolderID = WebUtil.readStrParam(request, NoticeboardConstants.CONTENT_FOLDER_ID);
	if (toolContentId == null) {
	    String error = "Unable to continue. Tool content id missing";
	    logger.error(error);
	    throw new NbApplicationException(error);
	}

	NbMonitoringForm monitorForm = new NbMonitoringForm();

	INoticeboardService nbService = NoticeboardServiceProxy.getNbService(getServlet().getServletContext());
	NoticeboardContent content = nbService.retrieveNoticeboard(toolContentId);

	monitorForm.setTitle(content.getTitle());
	monitorForm.setBasicContent(content.getContent());

	request.setAttribute(NoticeboardConstants.TOOL_CONTENT_ID, toolContentId);
	request.setAttribute(NoticeboardConstants.CONTENT_FOLDER_ID, contentFolderID);

	//Get the total number of learners that have participated in this tool activity
	monitorForm.setTotalLearners(nbService.calculateTotalNumberOfUsers(toolContentId));

	Set sessions = content.getNbSessions();
	Iterator i = sessions.iterator();
	Map numUsersMap = new HashMap();
	Map sessionIdMap = new HashMap();
	List<ReflectionDTO> reflections = new ArrayList<ReflectionDTO>();
	while (i.hasNext()) {
	    NoticeboardSession session = (NoticeboardSession) i.next();
	    int numUsersInSession = nbService.getNumberOfUsersInSession(session);
	    numUsersMap.put(session.getNbSessionName(), new Integer(numUsersInSession));
	    sessionIdMap.put(session.getNbSessionName(), session.getNbSessionId());
	    // Get list of users that have made a reflection entry 
	    if (content.getReflectOnActivity()) {
		List sessionUsers = nbService.getUsersBySession(session.getNbSessionId());
		for (int j = 0; j < sessionUsers.size(); j++) {
		    NoticeboardUser nbUser = (NoticeboardUser) sessionUsers.get(j);
		    NotebookEntry nbEntry = nbService.getEntry(session.getNbSessionId(),
			    CoreNotebookConstants.NOTEBOOK_TOOL, NoticeboardConstants.TOOL_SIGNATURE,
			    nbUser.getUserId().intValue());
		    if (nbEntry != null) {
			ReflectionDTO dto = new ReflectionDTO(nbEntry);
			dto.setExternalId(session.getNbSessionId());
			dto.setUserId(nbUser.getUserId());
			dto.setUsername(nbUser.getUsername());
			reflections.add(dto);
		    }
		}
	    }
	}
	monitorForm.setGroupStatsMap(numUsersMap);
	monitorForm.setSessionIdMap(sessionIdMap);

	boolean isGroupedActivity = nbService.isGroupedActivity(toolContentId);
	request.setAttribute("isGroupedActivity", isGroupedActivity);

	// Set reflection statistics, if reflection is set
	request.setAttribute("reflectOnActivity", content.getReflectOnActivity());
	request.setAttribute("reflectInstructions", content.getReflectInstructions());
	request.setAttribute("reflections", reflections);

	request.setAttribute("allowComments", content.isAllowComments());

	String currentTab = WebUtil.readStrParam(request, AttributeNames.PARAM_CURRENT_TAB, true);
	monitorForm.setCurrentTab(currentTab != null ? currentTab : "1");
	request.setAttribute(FORM, monitorForm);
	return mapping.findForward(NoticeboardConstants.MONITOR_PAGE);
    }

    public ActionForward viewReflection(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws NbApplicationException {
	Long userId = NbWebUtil.convertToLong(request.getParameter(NoticeboardConstants.USER_ID));
	Long toolSessionId = NbWebUtil.convertToLong(request.getParameter(NoticeboardConstants.TOOL_SESSION_ID));
	INoticeboardService nbService = NoticeboardServiceProxy.getNbService(getServlet().getServletContext());
	NoticeboardUser nbUser = nbService.retrieveNoticeboardUser(userId, toolSessionId);
	NotebookEntry nbEntry = nbService.getEntry(nbUser.getNbSession().getNbSessionId(),
		CoreNotebookConstants.NOTEBOOK_TOOL, NoticeboardConstants.TOOL_SIGNATURE, userId.intValue());
	if (nbEntry != null) {
	    request.setAttribute("nbEntry", nbEntry.getEntry());
	    request.setAttribute("name", nbUser.getFullname());
	}

	return mapping.findForward(NoticeboardConstants.MONITOR_REFLECTION_PAGE);
    }

    public ActionForward viewComments(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws NbApplicationException {
	request.setAttribute(NoticeboardConstants.TOOL_SESSION_ID,
		request.getParameter(NoticeboardConstants.TOOL_SESSION_ID));
	return mapping.findForward(NoticeboardConstants.MONITOR_COMMENTS_PAGE);
    }

}

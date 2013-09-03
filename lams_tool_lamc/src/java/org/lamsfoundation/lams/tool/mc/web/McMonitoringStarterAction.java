/***************************************************************************
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
 * ***********************************************************************/

package org.lamsfoundation.lams.tool.mc.web;

import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.lamsfoundation.lams.tool.mc.McAppConstants;
import org.lamsfoundation.lams.tool.mc.McApplicationException;
import org.lamsfoundation.lams.tool.mc.McComparator;
import org.lamsfoundation.lams.tool.mc.McGeneralAuthoringDTO;
import org.lamsfoundation.lams.tool.mc.McGeneralMonitoringDTO;
import org.lamsfoundation.lams.tool.mc.McQuestionContentDTO;
import org.lamsfoundation.lams.tool.mc.McUtils;
import org.lamsfoundation.lams.tool.mc.pojos.McContent;
import org.lamsfoundation.lams.tool.mc.pojos.McQueContent;
import org.lamsfoundation.lams.tool.mc.service.IMcService;
import org.lamsfoundation.lams.tool.mc.service.McServiceProxy;
import org.lamsfoundation.lams.util.WebUtil;
import org.lamsfoundation.lams.web.util.AttributeNames;
import org.lamsfoundation.lams.web.util.SessionMap;

/**
 * <p>
 * Starts up the monitoring module
 * </p>
 * 
 * @author Ozgur Demirtas
 */
public class McMonitoringStarterAction extends Action implements McAppConstants {
    private static Logger logger = Logger.getLogger(McMonitoringStarterAction.class.getName());
    private static IMcService service;

    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws IOException, ServletException, McApplicationException {
	initializeMcService();
	McUtils.cleanUpSessionAbsolute(request);

	McMonitoringForm mcMonitoringForm = (McMonitoringForm) form;
	McGeneralAuthoringDTO mcGeneralAuthoringDTO = new McGeneralAuthoringDTO();
	McGeneralMonitoringDTO mcGeneralMonitoringDTO = new McGeneralMonitoringDTO();

	String toolContentID = null;
	try {
	    Long toolContentIDLong = WebUtil.readLongParam(request, AttributeNames.PARAM_TOOL_CONTENT_ID, false);
	    toolContentID = toolContentIDLong.toString();
	    mcMonitoringForm.setToolContentId(toolContentID);
	    mcMonitoringForm.setToolContentID(toolContentID);
	} catch (IllegalArgumentException e) {
	    logger.error("Unable to start monitoring as tool content id is missing");
	    McUtils.cleanUpSessionAbsolute(request);
	    throw (e);
	}

	McContent mcContent = service.retrieveMc(new Long(toolContentID));
	mcGeneralMonitoringDTO.setToolContentID(toolContentID.toString());
	mcGeneralMonitoringDTO.setActivityTitle(mcContent.getTitle());
	mcGeneralMonitoringDTO.setActivityInstructions(mcContent.getInstructions());
	mcGeneralMonitoringDTO.setCurrentMonitoringTab("summary");
	mcGeneralMonitoringDTO.setSbmtSuccess(new Boolean(false).toString());
	mcGeneralMonitoringDTO.setDefineLaterInEditMode(new Boolean(false).toString());
	mcGeneralMonitoringDTO.setRequestLearningReport(new Boolean(false).toString());
	mcGeneralMonitoringDTO.setUserExceptionNoToolSessions(new Boolean(true).toString());

	/*
	 * get the questions section is needed for the Edit tab's View Only mode, starts here
	 */
	List listQuestionContentDTO = new LinkedList();

	Map mapOptionsContent = new TreeMap(new McComparator());
	mapOptionsContent.clear();
	Iterator queIterator = mcContent.getMcQueContents().iterator();
	Long mapIndex = new Long(1);
	while (queIterator.hasNext()) {
	    McQuestionContentDTO mcContentDTO = new McQuestionContentDTO();

	    McQueContent mcQueContent = (McQueContent) queIterator.next();
	    if (mcQueContent != null) {
		mapOptionsContent.put(mapIndex.toString(), mcQueContent.getQuestion());

		mcContentDTO.setQuestion(mcQueContent.getQuestion());
		mcContentDTO.setDisplayOrder(mcQueContent.getDisplayOrder().toString());
		listQuestionContentDTO.add(mcContentDTO);

		mapIndex = new Long(mapIndex.longValue() + 1);
	    }
	}
	mcGeneralMonitoringDTO.setMapOptionsContent(mapOptionsContent);
	/* ends here */

	request.setAttribute(LIST_QUESTION_CONTENT_DTO, listQuestionContentDTO);
	request.setAttribute(TOTAL_QUESTION_COUNT, new Integer(listQuestionContentDTO.size()));

	mcGeneralMonitoringDTO.setExistsOpenMcs(new Boolean(false).toString());

	MonitoringUtil.setupAllSessionsData(request, mcContent, service);
	mcGeneralMonitoringDTO.setGroupName("All Groups");
	mcGeneralMonitoringDTO.setListMonitoredAnswersContainerDto(new LinkedList());
	mcGeneralMonitoringDTO.setExistsOpenMcs(new Boolean(false).toString());

	// The edit activity code needs a session map
	SessionMap sessionMap = new SessionMap();
	mcMonitoringForm.setHttpSessionID(sessionMap.getSessionID());
	request.getSession().setAttribute(sessionMap.getSessionID(), sessionMap);

	mcMonitoringForm.setCurrentTab("1");
	mcGeneralMonitoringDTO.setCurrentTab("1");

	mcMonitoringForm.setActiveModule(MONITORING);
	mcGeneralMonitoringDTO.setActiveModule(MONITORING);

	mcGeneralMonitoringDTO.setRequestLearningReport(new Boolean(false).toString());
	mcGeneralMonitoringDTO.setIsPortfolioExport(new Boolean(false).toString());

	/* this section is needed for Edit Activity screen, from here... */
	mcGeneralAuthoringDTO.setActivityTitle(mcGeneralMonitoringDTO.getActivityTitle());
	mcGeneralAuthoringDTO.setActivityInstructions(mcGeneralMonitoringDTO.getActivityInstructions());
	mcGeneralAuthoringDTO.setActiveModule(MONITORING);

	request.setAttribute(MC_GENERAL_MONITORING_DTO, mcGeneralMonitoringDTO);
	request.setAttribute(MC_GENERAL_AUTHORING_DTO, mcGeneralAuthoringDTO);

	return new McMonitoringAction().commonSubmitSessionCode(mcMonitoringForm, request, mapping, service,
		mcGeneralMonitoringDTO);
    }
    
    // *************************************************************************************
    // Private method
    // *************************************************************************************
    private void initializeMcService() {
	if (service == null) {
	    service = McServiceProxy.getMcService(getServlet().getServletContext());
	}
    }
}

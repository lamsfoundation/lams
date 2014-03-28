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
package org.lamsfoundation.lams.tool.qa.web;

import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.Globals;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.lamsfoundation.lams.learningdesign.TextSearchConditionComparator;
import org.lamsfoundation.lams.tool.qa.QaAppConstants;
import org.lamsfoundation.lams.tool.qa.QaCondition;
import org.lamsfoundation.lams.tool.qa.QaContent;
import org.lamsfoundation.lams.tool.qa.QaQueContent;
import org.lamsfoundation.lams.tool.qa.dto.EditActivityDTO;
import org.lamsfoundation.lams.tool.qa.dto.GeneralLearnerFlowDTO;
import org.lamsfoundation.lams.tool.qa.dto.GeneralMonitoringDTO;
import org.lamsfoundation.lams.tool.qa.dto.QaMonitoredAnswersDTO;
import org.lamsfoundation.lams.tool.qa.dto.QaQuestionDTO;
import org.lamsfoundation.lams.tool.qa.service.IQaService;
import org.lamsfoundation.lams.tool.qa.service.QaServiceProxy;
import org.lamsfoundation.lams.tool.qa.util.QaApplicationException;
import org.lamsfoundation.lams.tool.qa.util.QaUtils;
import org.lamsfoundation.lams.tool.qa.web.form.QaMonitoringForm;
import org.lamsfoundation.lams.util.WebUtil;
import org.lamsfoundation.lams.web.util.AttributeNames;
import org.lamsfoundation.lams.web.util.SessionMap;

/**
 * Starts up the monitoring module
 * 
 * @author Ozgur Demirtas 
 */
public class QaMonitoringStarterAction extends Action implements QaAppConstants {
    private static Logger logger = Logger.getLogger(QaMonitoringStarterAction.class.getName());

    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws IOException, ServletException, QaApplicationException {
	QaUtils.cleanUpSessionAbsolute(request);

	QaMonitoringForm qaMonitoringForm = (QaMonitoringForm) form;
	

	IQaService qaService = QaServiceProxy.getQaService(getServlet().getServletContext());
	
	qaMonitoringForm.setQaService(qaService);

	String contentFolderID = WebUtil.readStrParam(request, AttributeNames.PARAM_CONTENT_FOLDER_ID);
	qaMonitoringForm.setContentFolderID(contentFolderID);

	ActionForward validateParameters = validateParameters(request, mapping, qaMonitoringForm);
	
	if (validateParameters != null) {
	    return validateParameters;
	}

	String toolContentID = qaMonitoringForm.getToolContentID();
	QaContent qaContent = qaService.getQa(new Long(toolContentID).longValue());
	if (qaContent == null) {
	    QaUtils.cleanUpSessionAbsolute(request);
	    throw new ServletException("Data not initialised in Monitoring");
	}

	QaMonitoringAction qaMonitoringAction = new QaMonitoringAction();
	GeneralMonitoringDTO generalMonitoringDTO = new GeneralMonitoringDTO();

	if (qaContent.getTitle() == null) {
	    generalMonitoringDTO.setActivityTitle("Questions and Answers");
	    generalMonitoringDTO.setActivityInstructions("Please answer the questions.");
	} else {
	    generalMonitoringDTO.setActivityTitle(qaContent.getTitle());
	    generalMonitoringDTO.setActivityInstructions(qaContent.getInstructions());
	}

	/* getting stats screen content from here... */
	int countAllUsers = qaService.getTotalNumberOfUsers(qaContent);
	if (countAllUsers == 0) {
	    //error: countAllUsers is 0
	    generalMonitoringDTO.setUserExceptionNoStudentActivity(new Boolean(true).toString());
	}

	generalMonitoringDTO.setCountAllUsers(new Integer(countAllUsers).toString());
	
	request.setAttribute(QaAppConstants.QA_GENERAL_MONITORING_DTO, generalMonitoringDTO);

	MonitoringUtil.setUpMonitoring(request, qaService, qaContent);

	qaMonitoringForm.setCurrentTab("1");

	String strToolContentID = request.getParameter(AttributeNames.PARAM_TOOL_CONTENT_ID);
	qaMonitoringForm.setToolContentID(strToolContentID);
	

	/* this section is related to summary tab. Starts here. */
	Map summaryToolSessions = MonitoringUtil.populateToolSessions(request, qaContent, qaService);
	request.setAttribute(QaAppConstants.SUMMARY_TOOL_SESSIONS, summaryToolSessions);

	EditActivityDTO editActivityDTO = new EditActivityDTO();
	boolean isContentInUse = qaContent.isContentLocked();
	if (isContentInUse == true) {
	    editActivityDTO.setMonitoredContentInUse(new Boolean(true).toString());
	}
	request.setAttribute(QaAppConstants.EDIT_ACTIVITY_DTO, editActivityDTO);

	generalMonitoringDTO.setContentFolderID(contentFolderID);
	int countSessionComplete = qaService.countSessionComplete(qaContent);
	generalMonitoringDTO.setCountSessionComplete(new Integer(countSessionComplete).toString());
	request.setAttribute(QaAppConstants.QA_GENERAL_MONITORING_DTO, generalMonitoringDTO);

	qaMonitoringForm.setActiveModule(MONITORING);

	/*for Edit Activity screen, BasicTab-ViewOnly*/
	qaMonitoringAction.prepareEditActivityScreenData(request, qaContent);

	SessionMap sessionMap = new SessionMap();
	sessionMap.put(ACTIVITY_TITLE_KEY, qaContent.getTitle());
	sessionMap.put(ACTIVITY_INSTRUCTIONS_KEY, qaContent.getInstructions());

	qaMonitoringForm.setHttpSessionID(sessionMap.getSessionID());
	request.getSession().setAttribute(sessionMap.getSessionID(), sessionMap);

	List listQuestionContentDTO = new LinkedList();
	Iterator queIterator = qaContent.getQaQueContents().iterator();
	while (queIterator.hasNext()) {
	    QaQueContent qaQuestion = (QaQueContent) queIterator.next();
	    if (qaQuestion != null) {
		QaQuestionDTO qaQuestionDTO = new QaQuestionDTO(qaQuestion);
		listQuestionContentDTO.add(qaQuestionDTO);
	    }
	}
	request.setAttribute(LIST_QUESTION_CONTENT_DTO, listQuestionContentDTO);
	sessionMap.put(LIST_QUESTION_CONTENT_DTO_KEY, listQuestionContentDTO);
	request.setAttribute(TOTAL_QUESTION_COUNT, new Integer(listQuestionContentDTO.size()));
	
	// preserve conditions into sessionMap
	SortedSet<QaCondition> conditionSet = new TreeSet<QaCondition>(new TextSearchConditionComparator());
	conditionSet.addAll(qaContent.getConditions());
	sessionMap.put(QaAppConstants.ATTR_CONDITION_SET, conditionSet);

	request.setAttribute("currentMonitoredToolSession", "All");
	MonitoringUtil.setUpMonitoring(request, qaService, qaContent);

	return (mapping.findForward(LOAD_MONITORING));
    }

    /**
     * validates request paramaters based on tool contract
     * validateParameters(HttpServletRequest request, ActionMapping mapping)
     * 
     * @param request
     * @param mapping
     * @return ActionForward
     * @throws ServletException 
     */
    protected ActionForward validateParameters(HttpServletRequest request, ActionMapping mapping,
	    QaMonitoringForm qaMonitoringForm) throws ServletException {

	String strToolContentId = request.getParameter(AttributeNames.PARAM_TOOL_CONTENT_ID);

	if ((strToolContentId == null) || (strToolContentId.length() == 0)) {
	    QaUtils.cleanUpSessionAbsolute(request);
	    throw new ServletException("No Tool Content ID found");
	} else {
	    try {
		long toolContentId = new Long(strToolContentId).longValue();

		qaMonitoringForm.setToolContentID(new Long(toolContentId).toString());
	    } catch (NumberFormatException e) {
		QaUtils.cleanUpSessionAbsolute(request);
		throw e;
	    }
	}
	return null;
    }

    /**
     * persists error messages to request scope
     * 
     * @param request
     * @param message
     */
    public void persistError(HttpServletRequest request, String message) {
	ActionMessages errors = new ActionMessages();
	errors.add(Globals.ERROR_KEY, new ActionMessage(message));
	saveErrors(request, errors);
    }
}

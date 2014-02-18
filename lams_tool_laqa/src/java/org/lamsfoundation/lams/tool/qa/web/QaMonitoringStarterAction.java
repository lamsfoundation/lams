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
import org.lamsfoundation.lams.tool.qa.dto.QaQuestionDTO;
import org.lamsfoundation.lams.tool.qa.dto.ReflectionDTO;
import org.lamsfoundation.lams.tool.qa.service.IQaService;
import org.lamsfoundation.lams.tool.qa.service.QaServiceProxy;
import org.lamsfoundation.lams.tool.qa.util.QaApplicationException;
import org.lamsfoundation.lams.tool.qa.util.QaUtils;
import org.lamsfoundation.lams.tool.qa.web.form.QaMonitoringForm;
import org.lamsfoundation.lams.util.WebUtil;
import org.lamsfoundation.lams.web.util.AttributeNames;
import org.lamsfoundation.lams.web.util.SessionMap;

/**
 * 
 * @author Ozgur Demirtas 
 * 
 * starts up the monitoring module
 */
public class QaMonitoringStarterAction extends Action implements QaAppConstants {
    static Logger logger = Logger.getLogger(QaMonitoringStarterAction.class.getName());

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

	GeneralMonitoringDTO generalMonitoringDTO = new GeneralMonitoringDTO();
	boolean initData = initialiseMonitoringData(mapping, qaMonitoringForm, request, response, qaService,
		generalMonitoringDTO);
	if (initData == false) {
	    throw new ServletException("Data not initialised in Monitoring");
	}

	qaMonitoringForm.setCurrentTab("1");

	generalMonitoringDTO.setUserExceptionNoToolSessions(new Boolean(true).toString());
	generalMonitoringDTO.setContentFolderID(contentFolderID);

	QaMonitoringAction qaMonitoringAction = new QaMonitoringAction();
	qaMonitoringAction.initSummaryContent(mapping, form, request, response);
	qaMonitoringAction.initInstructionsContent(mapping, form, request, response);
	qaMonitoringAction.initStatsContent(mapping, form, request, response, generalMonitoringDTO);

	String toolContentID = qaMonitoringForm.getToolContentID();
	QaContent qaContent = qaService.getQa(new Long(toolContentID).longValue());

	/*true means there is at least 1 response*/
	if (qaService.isStudentActivityOccurredGlobal(qaContent)) {
	    generalMonitoringDTO.setUserExceptionNoToolSessions(new Boolean(false).toString());
	} else {
	    generalMonitoringDTO.setUserExceptionNoToolSessions(new Boolean(true).toString());
	}

	request.setAttribute(SELECTION_CASE, new Long(2));
	qaMonitoringForm.setActiveModule(MONITORING);
	qaMonitoringForm.setEditResponse(new Boolean(false).toString());

	EditActivityDTO editActivityDTO = new EditActivityDTO();
	boolean isContentInUse = qaContent.isContentLocked();
	if (isContentInUse == true) {
	    editActivityDTO.setMonitoredContentInUse(new Boolean(true).toString());
	}
	request.setAttribute(EDIT_ACTIVITY_DTO, editActivityDTO);

	List<ReflectionDTO> reflectionDTOs = qaService.getReflectList(qaContent, null);
	request.setAttribute(QaAppConstants.REFLECTIONS_CONTAINER_DTO, reflectionDTOs);
	
	request.setAttribute(QA_GENERAL_MONITORING_DTO, generalMonitoringDTO);

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
	
	// preserve conditions into sessionMap
	SortedSet<QaCondition> conditionSet = new TreeSet<QaCondition>(new TextSearchConditionComparator());
	conditionSet.addAll(qaContent.getConditions());
	sessionMap.put(QaAppConstants.ATTR_CONDITION_SET, conditionSet);

	request.setAttribute(TOTAL_QUESTION_COUNT, new Integer(listQuestionContentDTO.size()));

	boolean notebookEntriesExist = MonitoringUtil.notebookEntriesExist(qaService, qaContent);

	if (notebookEntriesExist) {
	    request.setAttribute(NOTEBOOK_ENTRIES_EXIST, new Boolean(true).toString());

	    String userExceptionNoToolSessions = (String) generalMonitoringDTO.getUserExceptionNoToolSessions();

	    if (userExceptionNoToolSessions.equals("true")) {
		//there are no online student activity but there are reflections 
		request.setAttribute(NO_SESSIONS_NOTEBOOK_ENTRIES_EXIST, new Boolean(true).toString());
	    }

	} else {
	    request.setAttribute(NOTEBOOK_ENTRIES_EXIST, new Boolean(false).toString());
	}

	request.setAttribute("currentMonitoredToolSession", "All");
	MonitoringUtil.setUpMonitoring(request, qaService, qaContent);

	return (mapping.findForward(LOAD_MONITORING));
    }

    /**
     * initialises monitoring data mainly for jsp purposes
     * initialiseMonitoringData(ActionMapping mapping, ActionForm form,
     * HttpServletRequest request, HttpServletResponse response)
     * 
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return boolean
     */
    public boolean initialiseMonitoringData(ActionMapping mapping, QaMonitoringForm qaMonitoringForm,
	    HttpServletRequest request, HttpServletResponse response, IQaService qaService,
	    GeneralMonitoringDTO generalMonitoringDTO) {
	generalMonitoringDTO.setEditResponse(new Boolean(false).toString());
	generalMonitoringDTO.setUserExceptionNoToolSessions(new Boolean(true).toString());

	String toolContentID = qaMonitoringForm.getToolContentID();
	QaContent qaContent = qaService.getQa(new Long(toolContentID).longValue());
	if (qaContent == null) {
	    QaUtils.cleanUpSessionAbsolute(request);
	    return false;
	}

	QaMonitoringAction qaMonitoringAction = new QaMonitoringAction();
	GeneralLearnerFlowDTO generalLearnerFlowDTO = LearningUtil.buildGeneralLearnerFlowDTO(qaContent);
	qaMonitoringAction.refreshSummaryData(request, qaContent, qaService, true, false, null, null,
		generalLearnerFlowDTO, false, "All");
	qaMonitoringAction.refreshStatsData(request, qaMonitoringForm, qaService, generalMonitoringDTO);

	return true;
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

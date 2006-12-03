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
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

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
import org.lamsfoundation.lams.tool.qa.EditActivityDTO;
import org.lamsfoundation.lams.tool.qa.GeneralLearnerFlowDTO;
import org.lamsfoundation.lams.tool.qa.GeneralMonitoringDTO;
import org.lamsfoundation.lams.tool.qa.QaAppConstants;
import org.lamsfoundation.lams.tool.qa.QaApplicationException;
import org.lamsfoundation.lams.tool.qa.QaContent;
import org.lamsfoundation.lams.tool.qa.QaQueContent;
import org.lamsfoundation.lams.tool.qa.QaQuestionContentDTO;
import org.lamsfoundation.lams.tool.qa.QaUtils;
import org.lamsfoundation.lams.tool.qa.service.IQaService;
import org.lamsfoundation.lams.tool.qa.service.QaServiceProxy;
import org.lamsfoundation.lams.util.WebUtil;
import org.lamsfoundation.lams.web.util.AttributeNames;
import org.lamsfoundation.lams.web.util.SessionMap;

/**
 * 
 * @author Ozgur Demirtas
 * starts up the monitoring module
 * 
 *  <action
		path="/monitoringStarter"
		type="org.lamsfoundation.lams.tool.qa.web.QaMonitoringStarterAction"
		name="QaMonitoringForm"
		scope="session"
		parameter="method"
		unknown="false"
		validate="false">

	  	<forward
		    name="loadMonitoring"
		    path="/monitoring/MonitoringMaincontent.jsp"
		    redirect="true"
	  	/>
	      
   	  	<forward
		    name="errorList"
		    path="/QaErrorBox.jsp"
		    redirect="true"
	  	/>
	</action>

 *
 */

public class QaMonitoringStarterAction extends Action implements QaAppConstants {
	static Logger logger = Logger.getLogger(QaMonitoringStarterAction.class.getName());

	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) 
  								throws IOException, ServletException, QaApplicationException 
	{
		logger.debug("init QaMonitoringStarterAction...");
		QaUtils.cleanUpSessionAbsolute(request);
		
		QaMonitoringForm qaMonitoringForm = (QaMonitoringForm) form;
		logger.debug("qaMonitoringForm: " + qaMonitoringForm);

		IQaService qaService = QaServiceProxy.getQaService(getServlet().getServletContext());
		logger.debug("qaService: " + qaService);
		qaMonitoringForm.setQaService(qaService);
		
		String contentFolderID = WebUtil.readStrParam(request, AttributeNames.PARAM_CONTENT_FOLDER_ID);
		logger.debug("contentFolderID: " + contentFolderID);
		qaMonitoringForm.setContentFolderID(contentFolderID);

		
		
	    ActionForward validateParameters=validateParameters(request, mapping, qaMonitoringForm);
	    logger.debug("validateParamaters: " + validateParameters);
	    if (validateParameters != null)
	    {
	    	return validateParameters;
	    }

		
	    GeneralMonitoringDTO  generalMonitoringDTO= new GeneralMonitoringDTO();
		boolean initData=initialiseMonitoringData(mapping, qaMonitoringForm, request, response, qaService, generalMonitoringDTO);
		logger.debug("initData: " + initData);
		if (initData == false)
			return (mapping.findForward(ERROR_LIST));
		
		qaMonitoringForm.setCurrentTab("1");
		logger.debug("setting current tab to 1: ");
		
		generalMonitoringDTO.setUserExceptionNoToolSessions(new Boolean(true).toString());
		generalMonitoringDTO.setContentFolderID(contentFolderID);
		
		
		QaMonitoringAction qaMonitoringAction= new QaMonitoringAction();
		logger.debug("calling initSummaryContent.");
		qaMonitoringAction.initSummaryContent(mapping, form, request, response);
		logger.debug("calling initInstructionsContent.");
		qaMonitoringAction.initInstructionsContent(mapping, form, request, response);
		logger.debug("calling initStatsContent.");
		qaMonitoringAction.initStatsContent(mapping, form, request, response, generalMonitoringDTO);
		logger.debug("post initStatsContent." + generalMonitoringDTO);
		
	    String toolContentID=qaMonitoringForm.getToolContentID();
	    logger.debug("toolContentID: " + toolContentID);
	    
	    QaContent qaContent=qaService.loadQa(new Long(toolContentID).longValue());
		logger.debug("existing qaContent:" + qaContent);
		
		/*true means there is at least 1 response*/
		if (qaService.studentActivityOccurredGlobal(qaContent))
		{
			logger.debug("USER_EXCEPTION_NO_TOOL_SESSIONS is set to false");
			generalMonitoringDTO.setUserExceptionNoToolSessions(new Boolean(false).toString());
		}
		else
		{
			logger.debug("USER_EXCEPTION_NO_TOOL_SESSIONS is set to true");
			generalMonitoringDTO.setUserExceptionNoToolSessions(new Boolean(true).toString());
		}

		request.setAttribute(SELECTION_CASE, new Long(2));
		qaMonitoringForm.setActiveModule(MONITORING);
		qaMonitoringForm.setEditResponse(new Boolean(false).toString());
		
		
		/**getting instructions screen content from here... */
		generalMonitoringDTO.setOnlineInstructions(qaContent.getOnlineInstructions());
		generalMonitoringDTO.setOfflineInstructions(qaContent.getOfflineInstructions());
		
        List attachmentList = qaService.retrieveQaUploadedFiles(qaContent);
        logger.debug("attachmentList: " + attachmentList);
        generalMonitoringDTO.setAttachmentList(attachmentList);
        generalMonitoringDTO.setDeletedAttachmentList(new ArrayList());
        /** ...till here **/

        
    	EditActivityDTO editActivityDTO = new EditActivityDTO();
		boolean isContentInUse=QaUtils.isContentInUse(qaContent);
		logger.debug("isContentInUse:" + isContentInUse);
		if (isContentInUse == true)
		{
		    editActivityDTO.setMonitoredContentInUse(new Boolean(true).toString());
		}
		request.setAttribute(EDIT_ACTIVITY_DTO, editActivityDTO);

        
        qaMonitoringAction.prepareReflectionData(request, qaContent, qaService, null, false, "All");
        
		logger.debug("final qaMonitoringForm: " + qaMonitoringForm);
		logger.debug("final generalMonitoringDTO: " + generalMonitoringDTO );
		request.setAttribute(QA_GENERAL_MONITORING_DTO, generalMonitoringDTO);
		
		
		/*for Edit Activity screen, BasicTab-ViewOnly*/
		qaMonitoringAction.prepareEditActivityScreenData(request, qaContent);
		
		
		
		SessionMap sessionMap = new SessionMap();
	    sessionMap.put(ACTIVITY_TITLE_KEY, qaContent.getTitle());
	    sessionMap.put(ACTIVITY_INSTRUCTIONS_KEY, qaContent.getInstructions());
	    	    
	    qaMonitoringForm.setHttpSessionID(sessionMap.getSessionID());
	    request.getSession().setAttribute(sessionMap.getSessionID(), sessionMap);

	    
	    List listQuestionContentDTO= new  LinkedList();

		Iterator queIterator=qaContent.getQaQueContents().iterator();
		while (queIterator.hasNext())
		{
		    QaQuestionContentDTO qaQuestionContentDTO=new QaQuestionContentDTO();
		    
			QaQueContent qaQueContent=(QaQueContent) queIterator.next();
			if (qaQueContent != null)
			{
				logger.debug("question: " + qaQueContent.getQuestion());
				logger.debug("displayorder: " + new Integer(qaQueContent.getDisplayOrder()).toString());
				logger.debug("feedback: " + qaQueContent.getFeedback());
	    		
	    		qaQuestionContentDTO.setQuestion(qaQueContent.getQuestion());
	    		qaQuestionContentDTO.setDisplayOrder(new Integer(qaQueContent.getDisplayOrder()).toString());
	    		qaQuestionContentDTO.setFeedback(qaQueContent.getFeedback());
	    		listQuestionContentDTO.add(qaQuestionContentDTO);
			}
		}
		logger.debug("listQuestionContentDTO: " + listQuestionContentDTO);
		request.setAttribute(LIST_QUESTION_CONTENT_DTO,listQuestionContentDTO);
		sessionMap.put(LIST_QUESTION_CONTENT_DTO_KEY, listQuestionContentDTO);
		
		request.setAttribute(TOTAL_QUESTION_COUNT, new Integer(listQuestionContentDTO.size()));
		
		boolean notebookEntriesExist=MonitoringUtil.notebookEntriesExist(qaService, qaContent);
		logger.debug("notebookEntriesExist : " + notebookEntriesExist);
		
		if (notebookEntriesExist)
		{
		    request.setAttribute(NOTEBOOK_ENTRIES_EXIST, new Boolean(true).toString());
		    
		    String userExceptionNoToolSessions=(String)generalMonitoringDTO.getUserExceptionNoToolSessions();
		    logger.debug("userExceptionNoToolSessions : " + userExceptionNoToolSessions);
		    
		    if (userExceptionNoToolSessions.equals("true"))
		    {
		        logger.debug("there are no online student activity but there are reflections : ");
		        request.setAttribute(NO_SESSIONS_NOTEBOOK_ENTRIES_EXIST, new Boolean(true).toString());
		    }

		}
		else
		{
		    request.setAttribute(NOTEBOOK_ENTRIES_EXIST, new Boolean(false).toString());
		}
		
		MonitoringUtil.buildQaStatsDTO(request,qaService, qaContent);
		
		request.setAttribute("currentMonitoredToolSession", "All");
		MonitoringUtil.generateGroupsSessionData(request, qaService, qaContent,false);
		
		logger.debug("fwding to : " + LOAD_MONITORING);
		return (mapping.findForward(LOAD_MONITORING));	
	}

	
	/**
	 * initialises monitoring data mainly for jsp purposes 
	 * initialiseMonitoringData(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return boolean
	 */
	public boolean initialiseMonitoringData(ActionMapping mapping, QaMonitoringForm qaMonitoringForm, HttpServletRequest request, 
	        HttpServletResponse response, IQaService qaService, GeneralMonitoringDTO generalMonitoringDTO)
	{
		logger.debug("start initializing  monitoring data...: " + qaService);
		generalMonitoringDTO.setEditResponse(new Boolean(false).toString());
		generalMonitoringDTO.setUserExceptionNoToolSessions(new Boolean(true).toString());
	    		
		String toolContentID=qaMonitoringForm.getToolContentID();
		logger.debug("toolContentID:" + toolContentID);
	    
	    QaContent qaContent=qaService.loadQa(new Long(toolContentID).longValue());
		logger.debug("existing qaContent:" + qaContent);
		
		if (qaContent == null)
		{
			QaUtils.cleanUpSessionAbsolute(request);
			return false;
		}
		
		QaMonitoringAction qaMonitoringAction= new QaMonitoringAction();
		logger.debug("refreshing summary data...");
		
		GeneralLearnerFlowDTO generalLearnerFlowDTO= LearningUtil.buildGeneralLearnerFlowDTO(qaContent);
	    logger.debug("generalLearnerFlowDTO: " + generalLearnerFlowDTO);
		
	    qaMonitoringAction.refreshSummaryData(request, qaContent, qaService, true, false, null, null, 
		        generalLearnerFlowDTO, false , "All");
		
		logger.debug("refreshing stats data...");
		qaMonitoringAction.refreshStatsData(request, qaMonitoringForm, qaService, generalMonitoringDTO);
		

	    logger.debug("end initialising  monitoring data...");
		return true;
	}

	
	/**
	 * validates request paramaters based on tool contract
	 * validateParameters(HttpServletRequest request, ActionMapping mapping)
	 * 
	 * @param request
	 * @param mapping
	 * @return ActionForward
	 */
	protected ActionForward validateParameters(HttpServletRequest request, ActionMapping mapping, QaMonitoringForm qaMonitoringForm)
	{
		logger.debug("start validating monitoring parameters...");
    	
    	String strToolContentId=request.getParameter(AttributeNames.PARAM_TOOL_CONTENT_ID);
    	logger.debug("strToolContentId: " + strToolContentId);
    	 
	    if ((strToolContentId == null) || (strToolContentId.length() == 0)) 
	    {
	    	QaUtils.cleanUpSessionAbsolute(request);
			return (mapping.findForward(ERROR_LIST));
	    }
	    else
	    {
	    	try
			{
	    		long toolContentId=new Long(strToolContentId).longValue();
		    	logger.debug("passed TOOL_CONTENT_ID : " + new Long(toolContentId));

		    	qaMonitoringForm.setToolContentID(new Long(toolContentId).toString());
			}
	    	catch(NumberFormatException e)
			{
	    		logger.debug("add error.contentId.numberFormatException to ActionMessages.");
	    		QaUtils.cleanUpSessionAbsolute(request);
				return (mapping.findForward(ERROR_LIST));
			}
	    }
	    return null;
	}

	
	/**
     * persists error messages to request scope
     * @param request
     * @param message
     */
	public void persistError(HttpServletRequest request, String message)
	{
		ActionMessages errors= new ActionMessages();
		errors.add(Globals.ERROR_KEY, new ActionMessage(message));
		logger.debug("add " + message +"  to ActionMessages:");
		saveErrors(request,errors);	    	    
	}
}  

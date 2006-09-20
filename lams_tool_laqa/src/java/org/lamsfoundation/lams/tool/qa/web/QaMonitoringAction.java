/***************************************************************************
Copyright (C) 2005 LAMS Foundation (http://lamsfoundation.org)
License Information: http://lamsfoundation.org/licensing/lams/2.0/

This program is free software; you can redistribute it and/or modify
it under the terms of the GNU General Public License version 2 as
published by the Free Software Foundation.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program; if not, write to the Free Software
Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301
USA

http://www.gnu.org/licenses/gpl.txt
* ***********************************************************************/

 /**
 * @author Ozgur Demirtas
 * 
	 <action
	      path="/monitoring"
	      type="org.lamsfoundation.lams.tool.qa.web.QaMonitoringAction"
	      name="QaMonitoringForm"
	      scope="request"
	      parameter="dispatch"
	      unknown="false"
	      validate="true">
	      
	  	<forward
			name="loadMonitoring"
			path="/monitoring/MonitoringMaincontent.jsp"
			redirect="false"
	  	/>
	      
	  	<forward
		    name="refreshMonitoring"
		    path="/monitoring/MonitoringMaincontent.jsp"
		    redirect="false"
	  	/>

        <forward
          name="load"
          path="/monitoring/MonitoringMaincontent.jsp"
          redirect="false"
        />
	  	
	  	<forward
          name="loadViewOnly"
          path="/authoring/AuthoringTabsHolder.jsp"
          redirect="false"
        />

	    <forward
			name="newQuestionBox"
			path="/monitoring/newQuestionBox.jsp"
			redirect="false"
	    />

	    <forward
			name="editQuestionBox"
			path="/monitoring/editQuestionBox.jsp"
			redirect="false"
	    />
	  	
	    <forward
	        name="starter"
	        path="/index.jsp"
	        redirect="false"
	     />
	      

        <forward
          name="learnerNotebook"
          path="/monitoring/LearnerNotebook.jsp"
          redirect="false"
        />

   	  	<forward
		    name="errorList"
		    path="/QaErrorBox.jsp"
		    redirect="false"
	  	/>
	</action>


 */

/* $$Id$$ */

package org.lamsfoundation.lams.tool.qa.web;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.Globals;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.lamsfoundation.lams.notebook.model.NotebookEntry;
import org.lamsfoundation.lams.notebook.service.CoreNotebookConstants;
import org.lamsfoundation.lams.tool.exception.ToolException;
import org.lamsfoundation.lams.tool.qa.EditActivityDTO;
import org.lamsfoundation.lams.tool.qa.GeneralLearnerFlowDTO;
import org.lamsfoundation.lams.tool.qa.GeneralMonitoringDTO;
import org.lamsfoundation.lams.tool.qa.QaAppConstants;
import org.lamsfoundation.lams.tool.qa.QaContent;
import org.lamsfoundation.lams.tool.qa.QaGeneralAuthoringDTO;
import org.lamsfoundation.lams.tool.qa.QaQueContent;
import org.lamsfoundation.lams.tool.qa.QaQueUsr;
import org.lamsfoundation.lams.tool.qa.QaQuestionContentDTO;
import org.lamsfoundation.lams.tool.qa.QaSession;
import org.lamsfoundation.lams.tool.qa.QaUsrResp;
import org.lamsfoundation.lams.tool.qa.QaUtils;
import org.lamsfoundation.lams.tool.qa.ReflectionDTO;
import org.lamsfoundation.lams.tool.qa.service.IQaService;
import org.lamsfoundation.lams.tool.qa.service.QaServiceProxy;
import org.lamsfoundation.lams.util.WebUtil;
import org.lamsfoundation.lams.web.action.LamsDispatchAction;
import org.lamsfoundation.lams.web.util.AttributeNames;
import org.lamsfoundation.lams.web.util.SessionMap;


public class QaMonitoringAction extends LamsDispatchAction implements QaAppConstants
{
	static Logger logger = Logger.getLogger(QaMonitoringAction.class.getName());

	public static String SELECTBOX_SELECTED_TOOL_SESSION ="selectBoxSelectedToolSession";
	public static Integer READABLE_TOOL_SESSION_COUNT = new Integer(1);
 
    public ActionForward unspecified(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) 
        throws IOException, ServletException, ToolException{
    	logger.debug("dispatching unspecified...");
	 	return null;
    }
        
    
	/**
	 * switches to Stats tab of the Monitoring url
	 * getStats(ActionMapping mapping,
            ActionForm form,
            HttpServletRequest request,
            HttpServletResponse response) throws IOException,
                                         ServletException
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws IOException
	 * @throws ServletException
	 */
    public ActionForward getStats(ActionMapping mapping,
            ActionForm form,
            HttpServletRequest request,
            HttpServletResponse response) throws IOException,
                                         ServletException
	{
    	logger.debug("dispatching getStats..." + request);
    	
    	GeneralMonitoringDTO generalMonitoringDTO= new GeneralMonitoringDTO();
    	initStatsContent(mapping, form, request, response, generalMonitoringDTO);
    	return (mapping.findForward(LOAD_MONITORING));
	}

    
    
    public void initStatsContent(ActionMapping mapping,
            ActionForm form,
            HttpServletRequest request,
            HttpServletResponse response, GeneralMonitoringDTO generalMonitoringDTO) throws IOException,
                                         ServletException
	{
    	logger.debug("starting  initStatsContent...");
    	logger.debug("dispatching getStats..." + request);
    	
		QaMonitoringForm qaMonitoringForm = (QaMonitoringForm) form;
		logger.debug("qaMonitoringForm: " + qaMonitoringForm);
    	
    	IQaService qaService = null;
    	if (getServlet() != null) 
    	    qaService = QaServiceProxy.getQaService(getServlet().getServletContext());
    	else
			qaService=qaMonitoringForm.getQaService();
		
		logger.debug("qaService: " + qaService);

		String strToolContentID=request.getParameter(AttributeNames.PARAM_TOOL_CONTENT_ID);
		logger.debug("strToolContentID: " + strToolContentID);
		qaMonitoringForm.setToolContentID(strToolContentID);
		
		
		String contentFolderID = WebUtil.readStrParam(request, AttributeNames.PARAM_CONTENT_FOLDER_ID);
		logger.debug("contentFolderID: " + contentFolderID);
		qaMonitoringForm.setContentFolderID(contentFolderID);
		

		String editResponse=request.getParameter(EDIT_RESPONSE);
		logger.debug("editResponse: " + editResponse);
		qaMonitoringForm.setEditResponse(editResponse);

			    
	    QaContent qaContent=qaService.loadQa(new Long(strToolContentID).longValue());
		logger.debug("existing qaContent:" + qaContent);

		
		Map summaryToolSessions=MonitoringUtil.populateToolSessions(request, qaContent, qaService);
		logger.debug("summaryToolSessions: " + summaryToolSessions);
		request.setAttribute(SUMMARY_TOOL_SESSIONS, summaryToolSessions);
		logger.debug("SUMMARY_TOOL_SESSIONS: " + request.getAttribute(SUMMARY_TOOL_SESSIONS));
		
	    Map summaryToolSessionsId=MonitoringUtil.populateToolSessionsId(request, qaContent, qaService);
		logger.debug("summaryToolSessionsId: " + summaryToolSessionsId);
		request.setAttribute(SUMMARY_TOOL_SESSIONS_ID, summaryToolSessionsId);

		
		if (qaService.studentActivityOccurredGlobal(qaContent))
		{
			generalMonitoringDTO.setUserExceptionNoToolSessions(new Boolean(false).toString());
			logger.debug("USER_EXCEPTION_NO_TOOL_SESSIONS is set to false");
		}
		else
		{
			generalMonitoringDTO.setUserExceptionNoToolSessions(new Boolean(true).toString());
			logger.debug("USER_EXCEPTION_NO_TOOL_SESSIONS is set to true");
		}

    	refreshStatsData(request, qaMonitoringForm, qaService, generalMonitoringDTO);
    	generalMonitoringDTO.setEditResponse(new Boolean(false).toString());

    	EditActivityDTO editActivityDTO = new EditActivityDTO();
		boolean isContentInUse=QaUtils.isContentInUse(qaContent);
		logger.debug("isContentInUse:" + isContentInUse);
		if (isContentInUse == true)
		{
		    editActivityDTO.setMonitoredContentInUse(new Boolean(true).toString());
		}
		request.setAttribute(EDIT_ACTIVITY_DTO, editActivityDTO);
    	
        prepareReflectionData(request, qaContent, qaService, null, false);
        
		prepareEditActivityScreenData(request, qaContent);


		/*find out if there are any reflection entries, from here*/
		boolean notebookEntriesExist=MonitoringUtil.notebookEntriesExist(qaService, qaContent);
		logger.debug("notebookEntriesExist : " + notebookEntriesExist);
		
		if (notebookEntriesExist)
		{
		    request.setAttribute(NOTEBOOK_ENTRIES_EXIST, new Boolean(true).toString());
		}
		else
		{
		    request.setAttribute(NOTEBOOK_ENTRIES_EXIST, new Boolean(false).toString());
		}
		/* ... till here*/
	
        
		/**getting instructions screen content from here... */
		generalMonitoringDTO.setOnlineInstructions(qaContent.getOnlineInstructions());
		generalMonitoringDTO.setOfflineInstructions(qaContent.getOfflineInstructions());
		
        List attachmentList = qaService.retrieveQaUploadedFiles(qaContent);
        logger.debug("attachmentList: " + attachmentList);
        generalMonitoringDTO.setAttachmentList(attachmentList);
        generalMonitoringDTO.setDeletedAttachmentList(new ArrayList());
        /** ...till here **/

		
    	logger.debug("final generalMonitoringDTO: " + generalMonitoringDTO );
		request.setAttribute(QA_GENERAL_MONITORING_DTO, generalMonitoringDTO);
    	logger.debug("ending  initStatsContent...");
    	
    	MonitoringUtil.buildQaStatsDTO(request,qaService, qaContent);
	}

    
    /**
     * switches to instructions tab of the monitoring url.
     * getInstructions(ActionMapping mapping,
            ActionForm form,
            HttpServletRequest request,
            HttpServletResponse response) throws IOException,
                                         ServletException
     * 
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws IOException
     * @throws ServletException
     */
    public ActionForward getInstructions(ActionMapping mapping,
            ActionForm form,
            HttpServletRequest request,
            HttpServletResponse response) throws IOException,
                                         ServletException
	{
    	logger.debug("dispatching getInstructions..." + request);
    	initInstructionsContent(mapping, form, request, response);

    	return (mapping.findForward(LOAD_MONITORING));
	}

    
    public void initInstructionsContent(ActionMapping mapping,
            ActionForm form,
            HttpServletRequest request,
            HttpServletResponse response) throws IOException,
                                         ServletException
	{
    	logger.debug("starting initInstructionsContent...");
    	logger.debug("dispatching getInstructions..." + request);
    	
		QaMonitoringForm qaMonitoringForm = (QaMonitoringForm) form;
		logger.debug("qaMonitoringForm: " + qaMonitoringForm);

    	GeneralMonitoringDTO generalMonitoringDTO= new GeneralMonitoringDTO();

    	IQaService qaService = null;
    	if (getServlet() != null) 
    	    qaService = QaServiceProxy.getQaService(getServlet().getServletContext());
    	else
			qaService=qaMonitoringForm.getQaService();
		
		logger.debug("qaService: " + qaService);

		
		String strToolContentID=request.getParameter(AttributeNames.PARAM_TOOL_CONTENT_ID);
		logger.debug("strToolContentID: " + strToolContentID);
		qaMonitoringForm.setToolContentID(strToolContentID);
		
		String contentFolderID = WebUtil.readStrParam(request, AttributeNames.PARAM_CONTENT_FOLDER_ID);
		logger.debug("contentFolderID: " + contentFolderID);
		qaMonitoringForm.setContentFolderID(contentFolderID);

		
		String editResponse=request.getParameter(EDIT_RESPONSE);
		logger.debug("editResponse: " + editResponse);
		qaMonitoringForm.setEditResponse(editResponse);


	    QaContent qaContent=qaService.loadQa(new Long(strToolContentID).longValue());
		logger.debug("existing qaContent:" + qaContent);
		
		Map summaryToolSessions=MonitoringUtil.populateToolSessions(request, qaContent, qaService);
		logger.debug("summaryToolSessions: " + summaryToolSessions);
		request.setAttribute(SUMMARY_TOOL_SESSIONS, summaryToolSessions);
		logger.debug("SUMMARY_TOOL_SESSIONS: " + request.getAttribute(SUMMARY_TOOL_SESSIONS));

		
	    Map summaryToolSessionsId=MonitoringUtil.populateToolSessionsId(request, qaContent, qaService);
		logger.debug("summaryToolSessionsId: " + summaryToolSessionsId);
		request.setAttribute(SUMMARY_TOOL_SESSIONS_ID, summaryToolSessionsId);

		
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
		
		generalMonitoringDTO.setEditResponse(new Boolean(false).toString());
		
        prepareReflectionData(request, qaContent, qaService,null, false);
        
        prepareEditActivityScreenData(request, qaContent);

    	EditActivityDTO editActivityDTO = new EditActivityDTO();
		boolean isContentInUse=QaUtils.isContentInUse(qaContent);
		logger.debug("isContentInUse:" + isContentInUse);
		if (isContentInUse == true)
		{
		    editActivityDTO.setMonitoredContentInUse(new Boolean(true).toString());
		}
		request.setAttribute(EDIT_ACTIVITY_DTO, editActivityDTO);

		/*find out if there are any reflection entries, from here*/
		boolean notebookEntriesExist=MonitoringUtil.notebookEntriesExist(qaService, qaContent);
		logger.debug("notebookEntriesExist : " + notebookEntriesExist);
		
		if (notebookEntriesExist)
		{
		    request.setAttribute(NOTEBOOK_ENTRIES_EXIST, new Boolean(true).toString());
		}
		else
		{
		    request.setAttribute(NOTEBOOK_ENTRIES_EXIST, new Boolean(false).toString());
		}
		/* ... till here*/

		/**getting instructions screen content from here... */
		generalMonitoringDTO.setOnlineInstructions(qaContent.getOnlineInstructions());
		generalMonitoringDTO.setOfflineInstructions(qaContent.getOfflineInstructions());
		
        List attachmentList = qaService.retrieveQaUploadedFiles(qaContent);
        logger.debug("attachmentList: " + attachmentList);
        generalMonitoringDTO.setAttachmentList(attachmentList);
        generalMonitoringDTO.setDeletedAttachmentList(new ArrayList());
        /** ...till here **/

		
    	logger.debug("final generalMonitoringDTO: " + generalMonitoringDTO );
		request.setAttribute(QA_GENERAL_MONITORING_DTO, generalMonitoringDTO);
		
		MonitoringUtil.buildQaStatsDTO(request,qaService, qaContent);

    	logger.debug("ending  initInstructionsContent...");
	}

    /**
     * activates editActivity screen
     * ActionForward editActivity(ActionMapping mapping,
            ActionForm form,
            HttpServletRequest request,
            HttpServletResponse response) throws IOException,
                                         ServletException
     * 
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws IOException
     * @throws ServletException
     */
    public ActionForward editActivity(ActionMapping mapping,
            ActionForm form,
            HttpServletRequest request,
            HttpServletResponse response) throws IOException,
                                         ServletException
	{
    	logger.debug("dispatching editActivity...");
    	QaMonitoringForm qaMonitoringForm = (QaMonitoringForm) form;
    	logger.debug("qaMonitoringForm: " + qaMonitoringForm);

    	IQaService qaService = QaServiceProxy.getQaService(getServlet().getServletContext());
		logger.debug("qaService: " + qaService);
		
		if (qaService == null)
			qaService=qaMonitoringForm.getQaService();


		QaStarterAction qaStarterAction= new QaStarterAction();
		
		String strToolContentID=request.getParameter(AttributeNames.PARAM_TOOL_CONTENT_ID);
		logger.debug("strToolContentID: " + strToolContentID);
		qaMonitoringForm.setToolContentID(strToolContentID);
		
		String contentFolderID = WebUtil.readStrParam(request, AttributeNames.PARAM_CONTENT_FOLDER_ID);
		logger.debug("contentFolderID: " + contentFolderID);
		qaMonitoringForm.setContentFolderID(contentFolderID);

		
		String editResponse=request.getParameter(EDIT_RESPONSE);
		logger.debug("editResponse: " + editResponse);
		qaMonitoringForm.setEditResponse(editResponse);

	    	    
	    request.setAttribute(SOURCE_MC_STARTER, "monitoring");
	    logger.debug("SOURCE_MC_STARTER: monitoring");
	    
		/* it is possible that the content is being used by some learners. In this situation, the content  is marked as "in use" and 
		   content in use is not modifiable*/ 
		QaContent qaContent=qaService.loadQa(new Long(strToolContentID).longValue());
		logger.debug("qaContent:" + qaContent);
		
		Map summaryToolSessions=MonitoringUtil.populateToolSessions(request, qaContent, qaService);
		logger.debug("summaryToolSessions: " + summaryToolSessions);
		request.setAttribute(SUMMARY_TOOL_SESSIONS, summaryToolSessions);
		logger.debug("SUMMARY_TOOL_SESSIONS: " + request.getAttribute(SUMMARY_TOOL_SESSIONS));

	    Map summaryToolSessionsId=MonitoringUtil.populateToolSessionsId(request, qaContent, qaService);
		logger.debug("summaryToolSessionsId: " + summaryToolSessionsId);
		request.setAttribute(SUMMARY_TOOL_SESSIONS_ID, summaryToolSessionsId);

		GeneralMonitoringDTO generalMonitoringDTO= new GeneralMonitoringDTO();
		if (qaService.studentActivityOccurredGlobal(qaContent))
		{
			logger.debug("student activity occurred on this content:" + qaContent);
			generalMonitoringDTO.setUserExceptionContentInUse(new Boolean(true).toString());
			logger.debug("forwarding to: " + LOAD_MONITORING);
			return (mapping.findForward(LOAD_MONITORING));
		}
		
		if (qaContent.getTitle() == null)
		{
			generalMonitoringDTO.setActivityTitle("Questions and Answers");
			generalMonitoringDTO.setActivityInstructions("Please answer the questions.");
		}
		else
		{
			generalMonitoringDTO.setActivityTitle(qaContent.getTitle());
			generalMonitoringDTO.setActivityInstructions(qaContent.getInstructions());
		}
		
    	logger.debug("final generalMonitoringDTO: " + generalMonitoringDTO );
		request.setAttribute(QA_GENERAL_MONITORING_DTO, generalMonitoringDTO);

    	EditActivityDTO editActivityDTO = new EditActivityDTO();
		boolean isContentInUse=QaUtils.isContentInUse(qaContent);
		logger.debug("isContentInUse:" + isContentInUse);
		if (isContentInUse == true)
		{
		    editActivityDTO.setMonitoredContentInUse(new Boolean(true).toString());
		}
		request.setAttribute(EDIT_ACTIVITY_DTO, editActivityDTO);

		/**getting instructions screen content from here... */
		generalMonitoringDTO.setOnlineInstructions(qaContent.getOnlineInstructions());
		generalMonitoringDTO.setOfflineInstructions(qaContent.getOfflineInstructions());
		
        List attachmentList = qaService.retrieveQaUploadedFiles(qaContent);
        logger.debug("attachmentList: " + attachmentList);
        generalMonitoringDTO.setAttachmentList(attachmentList);
        generalMonitoringDTO.setDeletedAttachmentList(new ArrayList());
        /** ...till here **/

		logger.debug("final generalMonitoringDTO: " + generalMonitoringDTO );
		request.setAttribute(QA_GENERAL_MONITORING_DTO, generalMonitoringDTO);
		
        prepareReflectionData(request, qaContent, qaService,null, false);
        
        prepareEditActivityScreenData(request, qaContent);


		/*find out if there are any reflection entries, from here*/
		boolean notebookEntriesExist=MonitoringUtil.notebookEntriesExist(qaService, qaContent);
		logger.debug("notebookEntriesExist : " + notebookEntriesExist);
		
		if (notebookEntriesExist)
		{
		    request.setAttribute(NOTEBOOK_ENTRIES_EXIST, new Boolean(true).toString());
		}
		else
		{
		    request.setAttribute(NOTEBOOK_ENTRIES_EXIST, new Boolean(false).toString());
		}
		/* ... till here*/

		MonitoringUtil.buildQaStatsDTO(request,qaService, qaContent);
        
	    /* note that we are casting monitoring form subclass into Authoring form*/
	    logger.debug("watch here: note that we are casting monitoring form subclass into Authoring form");
	    return qaStarterAction.executeDefineLater(mapping, qaMonitoringForm, request, response, qaService);
	}

    
    
    /**
     * switches to summary tab of the monitoring url
     * 
     * getSummary(ActionMapping mapping,
            ActionForm form,
            HttpServletRequest request,
            HttpServletResponse response) throws IOException,
                                         ServletException
     * 
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws IOException
     * @throws ServletException
     */
    public ActionForward getSummary(ActionMapping mapping,
            ActionForm form,
            HttpServletRequest request,
            HttpServletResponse response) throws IOException,
                                         ServletException
	{
    	logger.debug("start getSummary...");
    	initSummaryContent(mapping, form, request, response);
 		return (mapping.findForward(LOAD_MONITORING));
	}
    

    
    public void initSummaryContent(ActionMapping mapping,
            ActionForm form,
            HttpServletRequest request,
            HttpServletResponse response) throws IOException,
                                         ServletException
	{
    	logger.debug("initSummaryContent...");
		QaMonitoringForm qaMonitoringForm = (QaMonitoringForm) form;
		logger.debug("qaMonitoringForm: " + qaMonitoringForm);

    	GeneralMonitoringDTO generalMonitoringDTO= new GeneralMonitoringDTO();
    	
    	IQaService qaService = null;
    	if (getServlet() != null) 
    	    qaService = QaServiceProxy.getQaService(getServlet().getServletContext());
    	else
			qaService=qaMonitoringForm.getQaService();
		
		logger.debug("qaService: " + qaService);
		
		
		String strToolContentID=request.getParameter(AttributeNames.PARAM_TOOL_CONTENT_ID);
		logger.debug("strToolContentID: " + strToolContentID);
		qaMonitoringForm.setToolContentID(strToolContentID);
		
		String contentFolderID = WebUtil.readStrParam(request, AttributeNames.PARAM_CONTENT_FOLDER_ID);
		logger.debug("contentFolderID: " + contentFolderID);
		qaMonitoringForm.setContentFolderID(contentFolderID);

		
		String editResponse=request.getParameter(EDIT_RESPONSE);
		logger.debug("editResponse: " + editResponse);
		qaMonitoringForm.setEditResponse(editResponse);


	    QaContent qaContent=qaService.loadQa(new Long(strToolContentID).longValue());
		logger.debug("existing qaContent:" + qaContent);
		
    	/* this section is related to summary tab. Starts here. */
		Map summaryToolSessions=MonitoringUtil.populateToolSessions(request, qaContent, qaService);
		logger.debug("summaryToolSessions: " + summaryToolSessions);

		request.setAttribute(SUMMARY_TOOL_SESSIONS, summaryToolSessions);
		logger.debug("SUMMARY_TOOL_SESSIONS: " + request.getAttribute(SUMMARY_TOOL_SESSIONS));
	    /* ends here. */
		
	    Map summaryToolSessionsId=MonitoringUtil.populateToolSessionsId(request, qaContent, qaService);
		logger.debug("summaryToolSessionsId: " + summaryToolSessionsId);
		request.setAttribute(SUMMARY_TOOL_SESSIONS_ID, summaryToolSessionsId);

		
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

		generalMonitoringDTO.setEditResponse(new Boolean(false).toString());

    	EditActivityDTO editActivityDTO = new EditActivityDTO();
		boolean isContentInUse=QaUtils.isContentInUse(qaContent);
		logger.debug("isContentInUse:" + isContentInUse);
		if (isContentInUse == true)
		{
		    editActivityDTO.setMonitoredContentInUse(new Boolean(true).toString());
		}
		request.setAttribute(EDIT_ACTIVITY_DTO, editActivityDTO);

        prepareReflectionData(request, qaContent, qaService,null, false);
        
        prepareEditActivityScreenData(request, qaContent);

        
		/*find out if there are any reflection entries, from here*/
		boolean notebookEntriesExist=MonitoringUtil.notebookEntriesExist(qaService, qaContent);
		logger.debug("notebookEntriesExist : " + notebookEntriesExist);
		
		if (notebookEntriesExist)
		{
		    request.setAttribute(NOTEBOOK_ENTRIES_EXIST, new Boolean(true).toString());
		}
		else
		{
		    request.setAttribute(NOTEBOOK_ENTRIES_EXIST, new Boolean(false).toString());
		}
		/* ... till here*/

		/*getting instructions screen content from here... */
		generalMonitoringDTO.setOnlineInstructions(qaContent.getOnlineInstructions());
		generalMonitoringDTO.setOfflineInstructions(qaContent.getOfflineInstructions());
		
        List attachmentList = qaService.retrieveQaUploadedFiles(qaContent);
        logger.debug("attachmentList: " + attachmentList);
        generalMonitoringDTO.setAttachmentList(attachmentList);
        generalMonitoringDTO.setDeletedAttachmentList(new ArrayList());
        /* ...till here */

		
    	logger.debug("final generalMonitoringDTO: " + generalMonitoringDTO );
		request.setAttribute(QA_GENERAL_MONITORING_DTO, generalMonitoringDTO);

		MonitoringUtil.buildQaStatsDTO(request,qaService, qaContent);
		
    	logger.debug("end  initSummaryContent...");
	}

    
    public ActionForward editActivityQuestions(ActionMapping mapping,
            ActionForm form,
            HttpServletRequest request,
            HttpServletResponse response) throws IOException,
                                         ServletException,
                                         ToolException
    {
    	logger.debug("dispatching editActivityQuestions...");
    	
		QaMonitoringForm qaMonitoringForm = (QaMonitoringForm) form;
		logger.debug("qaMonitoringForm: " + qaMonitoringForm);

    	IQaService qaService = QaServiceProxy.getQaService(getServlet().getServletContext());
		logger.debug("qaService: " + qaService);
		
		GeneralMonitoringDTO generalMonitoringDTO = new GeneralMonitoringDTO();
		
		generalMonitoringDTO.setMonitoredContentInUse(new Boolean(false).toString());
		
    	EditActivityDTO editActivityDTO = new EditActivityDTO();
	    editActivityDTO.setMonitoredContentInUse(new Boolean(false).toString());
	    request.setAttribute(EDIT_ACTIVITY_DTO, editActivityDTO);
		
		generalMonitoringDTO.setDefineLaterInEditMode(new Boolean(true).toString());
		
		
        logger.debug("final generalMonitoringDTO: " + generalMonitoringDTO );
		request.setAttribute(QA_GENERAL_MONITORING_DTO, generalMonitoringDTO);

		String strToolContentID=request.getParameter(AttributeNames.PARAM_TOOL_CONTENT_ID);
		logger.debug("strToolContentID: " + strToolContentID);
		qaMonitoringForm.setToolContentID(strToolContentID);
		
		String contentFolderID = WebUtil.readStrParam(request, AttributeNames.PARAM_CONTENT_FOLDER_ID);
		logger.debug("contentFolderID: " + contentFolderID);
		qaMonitoringForm.setContentFolderID(contentFolderID);
		
		String httpSessionID=request.getParameter("httpSessionID");
		logger.debug("httpSessionID: " + httpSessionID);
		qaMonitoringForm.setHttpSessionID(httpSessionID);

			    
	    QaContent qaContent=qaService.loadQa(new Long(strToolContentID).longValue());
		logger.debug("existing qaContent:" + qaContent);
		
		qaMonitoringForm.setTitle(qaContent.getTitle());

		QaUtils.setDefineLater(request, true,strToolContentID,  qaService);

		prepareEditActivityScreenData(request, qaContent);
		
		prepareReflectionData(request, qaContent, qaService, null, false);
		
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
		
		/**getting instructions screen content from here... */
		generalMonitoringDTO.setOnlineInstructions(qaContent.getOnlineInstructions());
		generalMonitoringDTO.setOfflineInstructions(qaContent.getOfflineInstructions());
		
        List attachmentList = qaService.retrieveQaUploadedFiles(qaContent);
        logger.debug("attachmentList: " + attachmentList);
        generalMonitoringDTO.setAttachmentList(attachmentList);
        generalMonitoringDTO.setDeletedAttachmentList(new ArrayList());
        /** ...till here **/

        
		
    	logger.debug("final generalMonitoringDTO: " + generalMonitoringDTO );
		request.setAttribute(QA_GENERAL_MONITORING_DTO, generalMonitoringDTO);
		
		
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

		request.setAttribute(TOTAL_QUESTION_COUNT, new Integer(listQuestionContentDTO.size()));
		
		QaGeneralAuthoringDTO qaGeneralAuthoringDTO = (QaGeneralAuthoringDTO)request.getAttribute(QA_GENERAL_AUTHORING_DTO);
		qaGeneralAuthoringDTO.setActiveModule(MONITORING);
		
		qaGeneralAuthoringDTO.setToolContentID(strToolContentID);
		qaGeneralAuthoringDTO.setContentFolderID(contentFolderID);
		qaGeneralAuthoringDTO.setHttpSessionID(httpSessionID);

		request.setAttribute(QA_GENERAL_AUTHORING_DTO, qaGeneralAuthoringDTO);
		
		
		/*find out if there are any reflection entries, from here*/
		boolean notebookEntriesExist=MonitoringUtil.notebookEntriesExist(qaService, qaContent);
		logger.debug("notebookEntriesExist : " + notebookEntriesExist);
		
		if (notebookEntriesExist)
		{
		    request.setAttribute(NOTEBOOK_ENTRIES_EXIST, new Boolean(true).toString());
		}
		else
		{
		    request.setAttribute(NOTEBOOK_ENTRIES_EXIST, new Boolean(false).toString());
		}
		/* ... till here*/

		MonitoringUtil.buildQaStatsDTO(request,qaService, qaContent);
		
		
        return (mapping.findForward(LOAD_MONITORING));
    }
    
    
    /**
     * gets called when the user selects a group from dropdown box in the summary tab 
     * 
     * submitSession(ActionMapping mapping,
            ActionForm form,
            HttpServletRequest request,
            HttpServletResponse response) throws IOException,
                                         ServletException
     * 
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws IOException
     * @throws ServletException
     */
    public ActionForward submitSession(ActionMapping mapping,
            ActionForm form,
            HttpServletRequest request,
            HttpServletResponse response) throws IOException,
                                         ServletException
	{
    	logger.debug("dispatching submitSession...");
    	
		IQaService qaService = QaServiceProxy.getQaService(getServlet().getServletContext());
		logger.debug("qaService: " + qaService);
    	
    	QaMonitoringForm qaMonitoringForm = (QaMonitoringForm) form;

	 	String currentMonitoredToolSession=qaMonitoringForm.getSelectedToolSessionId(); 
	    logger.debug("currentMonitoredToolSession: " + currentMonitoredToolSession);
	    
	    if (currentMonitoredToolSession.equals("All"))
	    {
		    request.setAttribute(SELECTION_CASE, new Long(2));
	    }
	    else
	    {
		    request.setAttribute(SELECTION_CASE, new Long(1));
	    }

	    logger.debug("SELECTION_CASE: " + request.getAttribute(SELECTION_CASE));

	    String strToolContentID=request.getParameter(AttributeNames.PARAM_TOOL_CONTENT_ID);
		logger.debug("strToolContentID: " + strToolContentID);
		qaMonitoringForm.setToolContentID(strToolContentID);

		String contentFolderID = WebUtil.readStrParam(request, AttributeNames.PARAM_CONTENT_FOLDER_ID);
		logger.debug("contentFolderID: " + contentFolderID);
		qaMonitoringForm.setContentFolderID(contentFolderID);

		
		String editResponse=request.getParameter(EDIT_RESPONSE);
		logger.debug("editResponse: " + editResponse);
		qaMonitoringForm.setEditResponse(editResponse);

		
	    QaContent qaContent=qaService.loadQa(new Long(strToolContentID).longValue());
		logger.debug("existing qaContent:" + qaContent);
		
		Map summaryToolSessions=MonitoringUtil.populateToolSessions(request, qaContent, qaService);
		logger.debug("summaryToolSessions: " + summaryToolSessions);
		request.setAttribute(SUMMARY_TOOL_SESSIONS, summaryToolSessions);
		logger.debug("SUMMARY_TOOL_SESSIONS: " + request.getAttribute(SUMMARY_TOOL_SESSIONS));


	    Map summaryToolSessionsId=MonitoringUtil.populateToolSessionsId(request, qaContent, qaService);
		logger.debug("summaryToolSessionsId: " + summaryToolSessionsId);
		request.setAttribute(SUMMARY_TOOL_SESSIONS_ID, summaryToolSessionsId);

		
		GeneralLearnerFlowDTO generalLearnerFlowDTO= LearningUtil.buildGeneralLearnerFlowDTO(qaContent);
	    logger.debug("generalLearnerFlowDTO: " + generalLearnerFlowDTO);
	    
	    prepareReflectionData(request, qaContent, qaService,null, false);
	    
	    prepareEditActivityScreenData(request, qaContent);

    	EditActivityDTO editActivityDTO = new EditActivityDTO();
		boolean isContentInUse=QaUtils.isContentInUse(qaContent);
		logger.debug("isContentInUse:" + isContentInUse);
		if (isContentInUse == true)
		{
		    editActivityDTO.setMonitoredContentInUse(new Boolean(true).toString());
		}
		request.setAttribute(EDIT_ACTIVITY_DTO, editActivityDTO);

		refreshSummaryData(request, qaContent, qaService, true, false, null, null, generalLearnerFlowDTO, false);

		
		/*find out if there are any reflection entries, from here*/
		boolean notebookEntriesExist=MonitoringUtil.notebookEntriesExist(qaService, qaContent);
		logger.debug("notebookEntriesExist : " + notebookEntriesExist);
		
		if (notebookEntriesExist)
		{
		    request.setAttribute(NOTEBOOK_ENTRIES_EXIST, new Boolean(true).toString());
		}
		else
		{
		    request.setAttribute(NOTEBOOK_ENTRIES_EXIST, new Boolean(false).toString());
		}
		/* ... till here*/

		MonitoringUtil.buildQaStatsDTO(request,qaService, qaContent);
		
    	return (mapping.findForward(LOAD_MONITORING));	
	}

    
	/**
	 * enables the user to edit responses
	 * ActionForward editResponse(ActionMapping mapping,
            ActionForm form,
            HttpServletRequest request,
            HttpServletResponse response) throws IOException,
                                         ServletException
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws IOException
	 * @throws ServletException
	 */
    public ActionForward editResponse(ActionMapping mapping,
            ActionForm form,
            HttpServletRequest request,
            HttpServletResponse response) throws IOException,
                                         ServletException
	{
    	logger.debug("dispatching editResponse...");

    	IQaService qaService = QaServiceProxy.getQaService(getServlet().getServletContext());
		logger.debug("qaService: " + qaService);
		
    	QaMonitoringForm qaMonitoringForm = (QaMonitoringForm) form;
    	
    	String editResponse=request.getParameter(EDIT_RESPONSE);
		logger.debug("editResponse: " + editResponse);
		qaMonitoringForm.setEditResponse(editResponse);


		String contentFolderID = WebUtil.readStrParam(request, AttributeNames.PARAM_CONTENT_FOLDER_ID);
		logger.debug("contentFolderID: " + contentFolderID);
		qaMonitoringForm.setContentFolderID(contentFolderID);

		
	    String responseId=qaMonitoringForm.getResponseId();
	    logger.debug("responseId: " + responseId);
	    request.getSession().setAttribute(EDITABLE_RESPONSE_ID, responseId);

	    QaUsrResp qaUsrResp= qaService.retrieveQaUsrResp(new Long(responseId).longValue());
	    logger.debug("qaUsrResp: " + qaUsrResp);

	    refreshUserInput(request, qaMonitoringForm);

	    QaContent qaContent=qaUsrResp.getQaQueContent().getQaContent();
	    logger.debug("qaContent: " + qaContent);
	    
    	String currentMonitoredToolSession=qaMonitoringForm.getSelectedToolSessionId(); 
	    logger.debug("currentMonitoredToolSession: " + currentMonitoredToolSession);

	    if (currentMonitoredToolSession.equals(""))
	    {
	        currentMonitoredToolSession="All";
	    }

		if (currentMonitoredToolSession.equals("All"))
	    {
		    request.setAttribute(SELECTION_CASE, new Long(2));
	    }
	    else
	    {
	        request.setAttribute(SELECTION_CASE, new Long(1));
	    }

		logger.debug("SELECTION_CASE: " + request.getAttribute(SELECTION_CASE));

	    
		Map summaryToolSessions=MonitoringUtil.populateToolSessions(request, qaContent, qaService);
		logger.debug("summaryToolSessions: " + summaryToolSessions);
		request.setAttribute(SUMMARY_TOOL_SESSIONS, summaryToolSessions);
		logger.debug("SUMMARY_TOOL_SESSIONS: " + request.getAttribute(SUMMARY_TOOL_SESSIONS));


	    Map summaryToolSessionsId=MonitoringUtil.populateToolSessionsId(request, qaContent, qaService);
		logger.debug("summaryToolSessionsId: " + summaryToolSessionsId);
		request.setAttribute(SUMMARY_TOOL_SESSIONS_ID, summaryToolSessionsId);
	    
		GeneralLearnerFlowDTO generalLearnerFlowDTO= LearningUtil.buildGeneralLearnerFlowDTO(qaContent);
	    logger.debug("generalLearnerFlowDTO: " + generalLearnerFlowDTO);
	    
	    refreshSummaryData(request, qaContent, qaService, true, false, null, null, generalLearnerFlowDTO, true);

        prepareReflectionData(request, qaContent, qaService,null, false);
        
        prepareEditActivityScreenData(request, qaContent);

	    EditActivityDTO editActivityDTO = new EditActivityDTO();
		boolean isContentInUse=QaUtils.isContentInUse(qaContent);
		logger.debug("isContentInUse:" + isContentInUse);
		if (isContentInUse == true)
		{
		    editActivityDTO.setMonitoredContentInUse(new Boolean(true).toString());
		}
		request.setAttribute(EDIT_ACTIVITY_DTO, editActivityDTO);
		
		
		/*find out if there are any reflection entries, from here*/
		boolean notebookEntriesExist=MonitoringUtil.notebookEntriesExist(qaService, qaContent);
		logger.debug("notebookEntriesExist : " + notebookEntriesExist);
		
		if (notebookEntriesExist)
		{
		    request.setAttribute(NOTEBOOK_ENTRIES_EXIST, new Boolean(true).toString());
		}
		else
		{
		    request.setAttribute(NOTEBOOK_ENTRIES_EXIST, new Boolean(false).toString());
		}
		/* ... till here*/

		MonitoringUtil.buildQaStatsDTO(request,qaService, qaContent);
        
	    return (mapping.findForward(LOAD_MONITORING));	
	}
    

    /**
     * enables the user to update responses 
     * ActionForward updateResponse(ActionMapping mapping,
            ActionForm form,
            HttpServletRequest request,
            HttpServletResponse response) throws IOException,
                                         ServletException
     * 
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws IOException
     * @throws ServletException
     */
    public ActionForward updateResponse(ActionMapping mapping,
            ActionForm form,
            HttpServletRequest request,
            HttpServletResponse response) throws IOException,
                                         ServletException
	{
    	logger.debug("dispatching updateResponse...");

    	IQaService qaService = QaServiceProxy.getQaService(getServlet().getServletContext());
		logger.debug("qaService: " + qaService);

    	QaMonitoringForm qaMonitoringForm = (QaMonitoringForm) form;


		String contentFolderID = WebUtil.readStrParam(request, AttributeNames.PARAM_CONTENT_FOLDER_ID);
		logger.debug("contentFolderID: " + contentFolderID);
		qaMonitoringForm.setContentFolderID(contentFolderID);

    	
    	String editResponse=request.getParameter(EDIT_RESPONSE);
		logger.debug("editResponse: " + editResponse);
		qaMonitoringForm.setEditResponse(editResponse);

	 	
	    String responseId=qaMonitoringForm.getResponseId();
	    logger.debug("responseId: " + responseId);
	    
	    String updatedResponse=request.getParameter("updatedResponse");
	    logger.debug("updatedResponse: " + updatedResponse);
	    QaUsrResp qaUsrResp= qaService.retrieveQaUsrResp(new Long(responseId).longValue());
	    logger.debug("qaUsrResp: " + qaUsrResp);
	    
	    /* write out the audit log entry. If you move this after the update of the response,
	     then make sure you update the audit call to use a copy of the original answer */
	    qaService.getAuditService().logChange(MY_SIGNATURE,
	    		qaUsrResp.getQaQueUser().getQueUsrId(),qaUsrResp.getQaQueUser().getUsername(),
	    		qaUsrResp.getAnswer(), updatedResponse);

	    qaUsrResp.setAnswer(updatedResponse);
	    qaService.updateQaUsrResp(qaUsrResp);
	    logger.debug("response updated.");
	    
	    refreshUserInput(request, qaMonitoringForm);
	    
	    
    	String currentMonitoredToolSession=qaMonitoringForm.getSelectedToolSessionId(); 
	    logger.debug("currentMonitoredToolSession: " + currentMonitoredToolSession);

	    if (currentMonitoredToolSession.equals(""))
	    {
	        currentMonitoredToolSession="All";
	    }

		if (currentMonitoredToolSession.equals("All"))
	    {
		    request.setAttribute(SELECTION_CASE, new Long(2));
	    }
	    else
	    {
	        request.setAttribute(SELECTION_CASE, new Long(1));
	    }
		logger.debug("SELECTION_CASE: " + request.getAttribute(SELECTION_CASE));

	    QaContent qaContent=qaUsrResp.getQaQueContent().getQaContent();
	    logger.debug("qaContent: " + qaContent);

		Map summaryToolSessions=MonitoringUtil.populateToolSessions(request, qaContent, qaService);
		logger.debug("summaryToolSessions: " + summaryToolSessions);
		request.setAttribute(SUMMARY_TOOL_SESSIONS, summaryToolSessions);
		logger.debug("SUMMARY_TOOL_SESSIONS: " + request.getAttribute(SUMMARY_TOOL_SESSIONS));


	    Map summaryToolSessionsId=MonitoringUtil.populateToolSessionsId(request, qaContent, qaService);
		logger.debug("summaryToolSessionsId: " + summaryToolSessionsId);
		request.setAttribute(SUMMARY_TOOL_SESSIONS_ID, summaryToolSessionsId);

		GeneralLearnerFlowDTO generalLearnerFlowDTO= LearningUtil.buildGeneralLearnerFlowDTO(qaContent);
	    logger.debug("generalLearnerFlowDTO: " + generalLearnerFlowDTO);
	    
	    refreshSummaryData(request, qaContent, qaService, true, false, null, null, generalLearnerFlowDTO, false);
	    
        prepareReflectionData(request, qaContent, qaService,null, false);
        
        prepareEditActivityScreenData(request, qaContent);
        
	    EditActivityDTO editActivityDTO = new EditActivityDTO();
		boolean isContentInUse=QaUtils.isContentInUse(qaContent);
		logger.debug("isContentInUse:" + isContentInUse);
		if (isContentInUse == true)
		{
		    editActivityDTO.setMonitoredContentInUse(new Boolean(true).toString());
		}
		request.setAttribute(EDIT_ACTIVITY_DTO, editActivityDTO);
		
		
		/*find out if there are any reflection entries, from here*/
		boolean notebookEntriesExist=MonitoringUtil.notebookEntriesExist(qaService, qaContent);
		logger.debug("notebookEntriesExist : " + notebookEntriesExist);
		
		if (notebookEntriesExist)
		{
		    request.setAttribute(NOTEBOOK_ENTRIES_EXIST, new Boolean(true).toString());
		}
		else
		{
		    request.setAttribute(NOTEBOOK_ENTRIES_EXIST, new Boolean(false).toString());
		}
		/* ... till here*/
		
		MonitoringUtil.buildQaStatsDTO(request,qaService, qaContent);
		
	    return (mapping.findForward(LOAD_MONITORING));	
	}

    /**
     * enables the user to delete responses
     * ActionForward deleteResponse(ActionMapping mapping,
            ActionForm form,
            HttpServletRequest request,
            HttpServletResponse response) throws IOException,
                                         ServletException
     * 
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws IOException
     * @throws ServletException
     */
    public ActionForward deleteResponse(ActionMapping mapping,
            ActionForm form,
            HttpServletRequest request,
            HttpServletResponse response) throws IOException,
                                         ServletException
	{
    	logger.debug("dispatching deleteResponse...");

    	IQaService qaService = QaServiceProxy.getQaService(getServlet().getServletContext());
		logger.debug("qaService: " + qaService);

    	QaMonitoringForm qaMonitoringForm = (QaMonitoringForm) form;
    	
		String contentFolderID = WebUtil.readStrParam(request, AttributeNames.PARAM_CONTENT_FOLDER_ID);
		logger.debug("contentFolderID: " + contentFolderID);
		qaMonitoringForm.setContentFolderID(contentFolderID);


    	String editResponse=request.getParameter(EDIT_RESPONSE);
		logger.debug("editResponse: " + editResponse);
		qaMonitoringForm.setEditResponse(editResponse);

    	
    	String currentMonitoredToolSession=qaMonitoringForm.getSelectedToolSessionId(); 
	    logger.debug("currentMonitoredToolSession: " + currentMonitoredToolSession);

	    if (currentMonitoredToolSession.equals(""))
	    {
	        currentMonitoredToolSession="All";
	    }

		if (currentMonitoredToolSession.equals("All"))
	    {
		    request.setAttribute(SELECTION_CASE, new Long(2));
	    }
	    else
	    {
	        request.setAttribute(SELECTION_CASE, new Long(1));
	    }

		logger.debug("SELECTION_CASE: " + request.getAttribute(SELECTION_CASE));
    	
	    String responseId=qaMonitoringForm.getResponseId();
	    logger.debug("responseId: " + responseId);
	    
	    QaUsrResp qaUsrResp= qaService.retrieveQaUsrResp(new Long(responseId).longValue());
	    logger.debug("qaUsrResp: " + qaUsrResp);
	  
	    qaService.removeUserResponse(qaUsrResp);
	    logger.debug("response deleted.");
	    
	    refreshUserInput(request, qaMonitoringForm);
	    
	    QaContent qaContent=qaUsrResp.getQaQueContent().getQaContent();
	    logger.debug("qaContent: " + qaContent);

	    
		Map summaryToolSessions=MonitoringUtil.populateToolSessions(request, qaContent, qaService);
		logger.debug("summaryToolSessions: " + summaryToolSessions);
		request.setAttribute(SUMMARY_TOOL_SESSIONS, summaryToolSessions);
		logger.debug("SUMMARY_TOOL_SESSIONS: " + request.getAttribute(SUMMARY_TOOL_SESSIONS));


	    Map summaryToolSessionsId=MonitoringUtil.populateToolSessionsId(request, qaContent, qaService);
		logger.debug("summaryToolSessionsId: " + summaryToolSessionsId);
		request.setAttribute(SUMMARY_TOOL_SESSIONS_ID, summaryToolSessionsId);

	    
		GeneralLearnerFlowDTO generalLearnerFlowDTO= LearningUtil.buildGeneralLearnerFlowDTO(qaContent);
	    logger.debug("generalLearnerFlowDTO: " + generalLearnerFlowDTO);
	    
	    refreshSummaryData(request, qaContent, qaService, true, false, null, null, generalLearnerFlowDTO, false);
	    
        prepareReflectionData(request, qaContent, qaService,null, false);
        
        prepareEditActivityScreenData(request, qaContent);
        
	    EditActivityDTO editActivityDTO = new EditActivityDTO();
		boolean isContentInUse=QaUtils.isContentInUse(qaContent);
		logger.debug("isContentInUse:" + isContentInUse);
		if (isContentInUse == true)
		{
		    editActivityDTO.setMonitoredContentInUse(new Boolean(true).toString());
		}
		request.setAttribute(EDIT_ACTIVITY_DTO, editActivityDTO);
		
		
		/*find out if there are any reflection entries, from here*/
		boolean notebookEntriesExist=MonitoringUtil.notebookEntriesExist(qaService, qaContent);
		logger.debug("notebookEntriesExist : " + notebookEntriesExist);
		
		if (notebookEntriesExist)
		{
		    request.setAttribute(NOTEBOOK_ENTRIES_EXIST, new Boolean(true).toString());
		}
		else
		{
		    request.setAttribute(NOTEBOOK_ENTRIES_EXIST, new Boolean(false).toString());
		}
		/* ... till here*/
		
		MonitoringUtil.buildQaStatsDTO(request,qaService, qaContent);
		
    	return (mapping.findForward(LOAD_MONITORING));	
	}

    
    /**
     * refreshUserInput(HttpServletRequest request)
     * @param request
     */
    public void refreshUserInput(HttpServletRequest request, QaMonitoringForm qaMonitoringForm)
    {
        logger.debug("starting refreshUserInput: " + qaMonitoringForm);

        IQaService qaService = null;
    	if (getServlet() != null) 
    	    qaService = QaServiceProxy.getQaService(getServlet().getServletContext());
    	else
			qaService=qaMonitoringForm.getQaService();
		
		logger.debug("qaService: " + qaService);
		logger.debug("qaMonitoringForm: " + qaMonitoringForm);
    	
	    String strToolContentID=request.getParameter(AttributeNames.PARAM_TOOL_CONTENT_ID);
		logger.debug("strToolContentID: " + strToolContentID);
		qaMonitoringForm.setToolContentID(strToolContentID);
		
		String contentFolderID = WebUtil.readStrParam(request, AttributeNames.PARAM_CONTENT_FOLDER_ID);
		logger.debug("contentFolderID: " + contentFolderID);
		qaMonitoringForm.setContentFolderID(contentFolderID);


	    QaContent qaContent=qaService.loadQa(new Long(strToolContentID).longValue());
		logger.debug("existing qaContent:" + qaContent);
    	
		Map summaryToolSessions=MonitoringUtil.populateToolSessions(request, qaContent, qaService);
		logger.debug("summaryToolSessions: " + summaryToolSessions);
		request.setAttribute(SUMMARY_TOOL_SESSIONS, summaryToolSessions);
		logger.debug("SUMMARY_TOOL_SESSIONS: " + request.getAttribute(SUMMARY_TOOL_SESSIONS));

	    Map summaryToolSessionsId=MonitoringUtil.populateToolSessionsId(request, qaContent, qaService);
		logger.debug("summaryToolSessionsId: " + summaryToolSessionsId);
		
		prepareReflectionData(request, qaContent, qaService,null, false);
		
		prepareEditActivityScreenData(request, qaContent);
		

	    EditActivityDTO editActivityDTO = new EditActivityDTO();
		boolean isContentInUse=QaUtils.isContentInUse(qaContent);
		logger.debug("isContentInUse:" + isContentInUse);
		if (isContentInUse == true)
		{
		    editActivityDTO.setMonitoredContentInUse(new Boolean(true).toString());
		}
		request.setAttribute(EDIT_ACTIVITY_DTO, editActivityDTO);		

		/*find out if there are any reflection entries, from here*/
		boolean notebookEntriesExist=MonitoringUtil.notebookEntriesExist(qaService, qaContent);
		logger.debug("notebookEntriesExist : " + notebookEntriesExist);
		
		if (notebookEntriesExist)
		{
		    request.setAttribute(NOTEBOOK_ENTRIES_EXIST, new Boolean(true).toString());
		}
		else
		{
		    request.setAttribute(NOTEBOOK_ENTRIES_EXIST, new Boolean(false).toString());
		}
		/* ... till here*/
		
		request.setAttribute(SUMMARY_TOOL_SESSIONS_ID, summaryToolSessionsId);
		
		MonitoringUtil.buildQaStatsDTO(request,qaService, qaContent);
    }
    
    
    
	/**
     * persists error messages to request scope
     * persistError(HttpServletRequest request, String message)
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
  
	
    
    /**
     * populates data for summary screen
     * refreshSummaryData(HttpServletRequest request, QaContent qaContent, IQaService qaService, 
			boolean isUserNamesVisible, boolean isLearnerRequest, String currentSessionId, String userId)
			
     * @param request
     * @param qaContent
     * @param qaService
     * @param isUserNamesVisible
     * @param isLearnerRequest
     * @param currentSessionId
     * @param userId
     */
    public void refreshSummaryData(HttpServletRequest request, QaContent qaContent, IQaService qaService, 
			boolean isUserNamesVisible, boolean isLearnerRequest, String currentSessionId, String userId, 
			GeneralLearnerFlowDTO generalLearnerFlowDTO, boolean setEditResponse)
	{
        logger.debug("starting refreshSummaryData: setEditResponse + " + setEditResponse);
	    logger.debug("generalLearnerFlowDTO: " + generalLearnerFlowDTO);
	    logger.debug("qaService: " + isUserNamesVisible);
		
		logger.debug("isUserNamesVisible: " + isUserNamesVisible);
		logger.debug("isLearnerRequest: " + isLearnerRequest);
		
		GeneralMonitoringDTO generalMonitoringDTO= new GeneralMonitoringDTO();
				
		/* this section is related to summary tab. Starts here. */
		Map summaryToolSessions=MonitoringUtil.populateToolSessions(request, qaContent, qaService);
		logger.debug("summaryToolSessions: " + summaryToolSessions);
		request.setAttribute(SUMMARY_TOOL_SESSIONS, summaryToolSessions);
		
		if (qaContent.getTitle() == null)
		{
			generalMonitoringDTO.setActivityTitle("Questions and Answers");
			generalMonitoringDTO.setActivityInstructions("Please answer the questions.");
		}
		else
		{
			generalMonitoringDTO.setActivityTitle(qaContent.getTitle());
			generalMonitoringDTO.setActivityInstructions(qaContent.getInstructions());
		}

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
		
		
		boolean isContentInUse=QaUtils.isContentInUse(qaContent);
		logger.debug("isContentInUse:" + isContentInUse);
		
		generalMonitoringDTO.setMonitoredContentInUse(new Boolean(false).toString());
		if (isContentInUse == true)
		{
			logger.debug("monitoring url does not allow editActivity since the content is in use.");
	    	persistError(request,"error.content.inUse");
	    	generalMonitoringDTO.setMonitoredContentInUse(new Boolean(true).toString());
		}

	    EditActivityDTO editActivityDTO = new EditActivityDTO();
		if (isContentInUse == true)
		{
		    editActivityDTO.setMonitoredContentInUse(new Boolean(true).toString());
		}
		request.setAttribute(EDIT_ACTIVITY_DTO, editActivityDTO);
		
	    Map summaryToolSessionsId=MonitoringUtil.populateToolSessionsId(request, qaContent, qaService);
		logger.debug("summaryToolSessionsId: " + summaryToolSessionsId);
		request.setAttribute(SUMMARY_TOOL_SESSIONS_ID, summaryToolSessionsId);
		
	    	
	    logger.debug("using allUsersData to retrieve data: " + isUserNamesVisible);
	    List listMonitoredAnswersContainerDTO=MonitoringUtil.buildGroupsQuestionData(request, qaContent, qaService, 
	    		isUserNamesVisible, isLearnerRequest, currentSessionId, userId);
	    
	    logger.debug("listMonitoredAnswersContainerDTO: " + listMonitoredAnswersContainerDTO);
	    
	    
		/*getting stats screen content from here... */
    	int countAllUsers=qaService.getTotalNumberOfUsers(qaContent);
		logger.debug("countAllUsers: " + countAllUsers);
		
		if (countAllUsers == 0)
		{
	    	logger.debug("error: countAllUsers is 0");
	    	generalMonitoringDTO.setUserExceptionNoStudentActivity(new Boolean(true).toString());
		}
		

		generalMonitoringDTO.setCountAllUsers(new Integer(countAllUsers).toString());
		
		int countSessionComplete=qaService.countSessionComplete(qaContent);
		logger.debug("countSessionComplete: " + countSessionComplete);

		generalMonitoringDTO.setCountSessionComplete(new Integer(countSessionComplete).toString());
		logger.debug("ending refreshStatsData with generalMonitoringDTO: " + generalMonitoringDTO);
		/* till here*/

		
		generalMonitoringDTO.setEditResponse(new Boolean(setEditResponse).toString());
		
		/*getting instructions screen content from here... */
		generalMonitoringDTO.setOnlineInstructions(qaContent.getOnlineInstructions());
		generalMonitoringDTO.setOfflineInstructions(qaContent.getOfflineInstructions());
		
		/*
	    if ((generalMonitoringDTO.getOnlineInstructions() == null) || (generalMonitoringDTO.getOnlineInstructions().length() == 0))
	    {
	        generalMonitoringDTO.setOnlineInstructions(DEFAULT_ONLINE_INST);
	    }
	        
	    if ((generalMonitoringDTO.getOfflineInstructions() == null) || (generalMonitoringDTO.getOfflineInstructions().length() == 0))
	    {
	        generalMonitoringDTO.setOfflineInstructions(DEFAULT_OFFLINE_INST);
	    }
	    */

        List attachmentList = qaService.retrieveQaUploadedFiles(qaContent);
        logger.debug("attachmentList: " + attachmentList);
        generalMonitoringDTO.setAttachmentList(attachmentList);
        generalMonitoringDTO.setDeletedAttachmentList(new ArrayList());
        /* ...till here **/

		
	    if (generalLearnerFlowDTO != null)
	    {
	        logger.debug("final generalLearnerFlowDTO: " + generalLearnerFlowDTO);
	        logger.debug("placing LIST_MONITORED_ANSWERS_CONTAINER_DTO within generalLearnerFlowDTO");
		    generalLearnerFlowDTO.setListMonitoredAnswersContainerDTO(listMonitoredAnswersContainerDTO);
		    
		    if (isLearnerRequest)
		    {
		        logger.debug("isLearnerRequest is true.");
		        generalLearnerFlowDTO.setRequestLearningReportProgress(new Boolean(true).toString());
		    }
		    
			logger.debug("end of refreshSummaryData,  generalLearnerFlowDTO : " + generalLearnerFlowDTO);
		    request.setAttribute(GENERAL_LEARNER_FLOW_DTO, generalLearnerFlowDTO);
	    }
	    
	    prepareReflectionData(request, qaContent, qaService,null, false);

	    prepareEditActivityScreenData(request, qaContent);
	    
		/*find out if there are any reflection entries, from here*/
		boolean notebookEntriesExist=MonitoringUtil.notebookEntriesExist(qaService, qaContent);
		logger.debug("notebookEntriesExist : " + notebookEntriesExist);
		
		if (notebookEntriesExist)
		{
		    request.setAttribute(NOTEBOOK_ENTRIES_EXIST, new Boolean(true).toString());
		}
		else
		{
		    request.setAttribute(NOTEBOOK_ENTRIES_EXIST, new Boolean(false).toString());
		}
		/* ... till here*/
	    
	    
    	logger.debug("final generalMonitoringDTO: " + generalMonitoringDTO );
		request.setAttribute(QA_GENERAL_MONITORING_DTO, generalMonitoringDTO);
		
		MonitoringUtil.buildQaStatsDTO(request,qaService, qaContent);
	}

	
    /**
     * populates data for stats screen
     * refreshStatsData(HttpServletRequest request)
     * @param request
     */
	public void refreshStatsData(HttpServletRequest request , QaMonitoringForm qaMonitoringForm, IQaService qaService, 
	        GeneralMonitoringDTO generalMonitoringDTO)
	{
	    logger.debug("starting refreshStatsData: " + qaService);
	    logger.debug("starting refreshStatsData with generalMonitoringDTO: " + generalMonitoringDTO);
		/* it is possible that no users has ever logged in for the activity yet*/

	    String strToolContentID=request.getParameter(AttributeNames.PARAM_TOOL_CONTENT_ID);
		logger.debug("strToolContentID: " + strToolContentID);
		qaMonitoringForm.setToolContentID(strToolContentID);

	    QaContent qaContent=qaService.loadQa(new Long(strToolContentID).longValue());
		logger.debug("existing qaContent:" + qaContent);

		Map summaryToolSessions=MonitoringUtil.populateToolSessions(request, qaContent, qaService);
		logger.debug("summaryToolSessions: " + summaryToolSessions);
		request.setAttribute(SUMMARY_TOOL_SESSIONS, summaryToolSessions);
		logger.debug("SUMMARY_TOOL_SESSIONS: " + request.getAttribute(SUMMARY_TOOL_SESSIONS));


	    Map summaryToolSessionsId=MonitoringUtil.populateToolSessionsId(request, qaContent, qaService);
		logger.debug("summaryToolSessionsId: " + summaryToolSessionsId);
		request.setAttribute(SUMMARY_TOOL_SESSIONS_ID, summaryToolSessionsId);


    	int countAllUsers=qaService.getTotalNumberOfUsers(qaContent);
		logger.debug("countAllUsers: " + countAllUsers);
		
		if (countAllUsers == 0)
		{
	    	logger.debug("error: countAllUsers is 0");
	    	generalMonitoringDTO.setUserExceptionNoStudentActivity(new Boolean(true).toString());
		}
		

		generalMonitoringDTO.setCountAllUsers(new Integer(countAllUsers).toString());
		
		int countSessionComplete=qaService.countSessionComplete(qaContent);
		logger.debug("countSessionComplete: " + countSessionComplete);

		generalMonitoringDTO.setCountSessionComplete(new Integer(countSessionComplete).toString());
		
		prepareReflectionData(request, qaContent, qaService,null, false);
		
		prepareEditActivityScreenData(request, qaContent);

	    EditActivityDTO editActivityDTO = new EditActivityDTO();
		boolean isContentInUse=QaUtils.isContentInUse(qaContent);
		logger.debug("isContentInUse:" + isContentInUse);
		if (isContentInUse == true)
		{
		    editActivityDTO.setMonitoredContentInUse(new Boolean(true).toString());
		}
		
		
        List attachmentList = qaService.retrieveQaUploadedFiles(qaContent);
        logger.debug("attachmentList: " + attachmentList);
        generalMonitoringDTO.setAttachmentList(attachmentList);
    	logger.debug("final generalMonitoringDTO: " + generalMonitoringDTO );
		
		
		request.setAttribute(EDIT_ACTIVITY_DTO, editActivityDTO);
		logger.debug("ending refreshStatsData with generalMonitoringDTO: " + generalMonitoringDTO);
		request.setAttribute(QA_GENERAL_MONITORING_DTO, generalMonitoringDTO);
		
		/*find out if there are any reflection entries, from here*/
		boolean notebookEntriesExist=MonitoringUtil.notebookEntriesExist(qaService, qaContent);
		logger.debug("notebookEntriesExist : " + notebookEntriesExist);
		
		if (notebookEntriesExist)
		{
		    request.setAttribute(NOTEBOOK_ENTRIES_EXIST, new Boolean(true).toString());
		}
		else
		{
		    request.setAttribute(NOTEBOOK_ENTRIES_EXIST, new Boolean(false).toString());
		}
		/* ... till here*/
		
		MonitoringUtil.buildQaStatsDTO(request,qaService, qaContent);
	}
	
	
    public ActionForward showResponse(ActionMapping mapping,
            ActionForm form,
            HttpServletRequest request,
            HttpServletResponse response) throws IOException,
                                         ServletException, ToolException
     {
        logger.debug("dispatching showResponse...");

        IQaService qaService = QaServiceProxy.getQaService(getServlet().getServletContext());
		logger.debug("qaService: " + qaService);
		
    	QaMonitoringForm qaMonitoringForm = (QaMonitoringForm) form;
    	
    	String currentUid=qaMonitoringForm.getCurrentUid();
    	logger.debug("currentUid: " + currentUid);
        QaUsrResp qaUsrResp =qaService.getAttemptByUID(new Long(currentUid));
        logger.debug("qaUsrResp: " + qaUsrResp);
        qaUsrResp.setVisible(true);
        qaService.updateUserResponse(qaUsrResp);
        qaService.showResponse(qaUsrResp);
        logger.debug("qaUsrResp: " + qaUsrResp);

        
	    String strToolContentID=request.getParameter(AttributeNames.PARAM_TOOL_CONTENT_ID);
		logger.debug("strToolContentID: " + strToolContentID);
		qaMonitoringForm.setToolContentID(strToolContentID);
		
		String contentFolderID = WebUtil.readStrParam(request, AttributeNames.PARAM_CONTENT_FOLDER_ID);
		logger.debug("contentFolderID: " + contentFolderID);
		qaMonitoringForm.setContentFolderID(contentFolderID);

		
    	String editResponse=request.getParameter(EDIT_RESPONSE);
		logger.debug("editResponse: " + editResponse);
		qaMonitoringForm.setEditResponse(editResponse);


	    QaContent qaContent=qaService.loadQa(new Long(strToolContentID).longValue());
		logger.debug("existing qaContent:" + qaContent);

		Map summaryToolSessions=MonitoringUtil.populateToolSessions(request, qaContent, qaService);
		logger.debug("summaryToolSessions: " + summaryToolSessions);
		request.setAttribute(SUMMARY_TOOL_SESSIONS, summaryToolSessions);
		logger.debug("SUMMARY_TOOL_SESSIONS: " + request.getAttribute(SUMMARY_TOOL_SESSIONS));

	    Map summaryToolSessionsId=MonitoringUtil.populateToolSessionsId(request, qaContent, qaService);
		logger.debug("summaryToolSessionsId: " + summaryToolSessionsId);
		request.setAttribute(SUMMARY_TOOL_SESSIONS_ID, summaryToolSessionsId);


    	String currentMonitoredToolSession=qaMonitoringForm.getSelectedToolSessionId(); 
	    logger.debug("currentMonitoredToolSession: " + currentMonitoredToolSession);

	    if (currentMonitoredToolSession.equals(""))
	    {
	        currentMonitoredToolSession="All";
	    }

		GeneralLearnerFlowDTO generalLearnerFlowDTO= LearningUtil.buildGeneralLearnerFlowDTO(qaContent);
	    logger.debug("generalLearnerFlowDTO: " + generalLearnerFlowDTO);
	    
	    refreshSummaryData(request, qaContent, qaService, true, false, null, null, generalLearnerFlowDTO, false);
		
	    logger.debug("currentMonitoredToolSession: " + currentMonitoredToolSession);
		if (currentMonitoredToolSession.equals("All"))
	    {
		    request.setAttribute(SELECTION_CASE, new Long(2));
	    }
	    else
	    {
	        request.setAttribute(SELECTION_CASE, new Long(1));
	    }

		logger.debug("SELECTION_CASE: " + request.getAttribute(SELECTION_CASE));
		
		prepareReflectionData(request, qaContent, qaService,null, false);
		
		prepareEditActivityScreenData(request, qaContent);

	    EditActivityDTO editActivityDTO = new EditActivityDTO();
		boolean isContentInUse=QaUtils.isContentInUse(qaContent);
		logger.debug("isContentInUse:" + isContentInUse);
		if (isContentInUse == true)
		{
		    editActivityDTO.setMonitoredContentInUse(new Boolean(true).toString());
		}
		request.setAttribute(EDIT_ACTIVITY_DTO, editActivityDTO);

		/*find out if there are any reflection entries, from here*/
		boolean notebookEntriesExist=MonitoringUtil.notebookEntriesExist(qaService, qaContent);
		logger.debug("notebookEntriesExist : " + notebookEntriesExist);
		
		if (notebookEntriesExist)
		{
		    request.setAttribute(NOTEBOOK_ENTRIES_EXIST, new Boolean(true).toString());
		}
		else
		{
		    request.setAttribute(NOTEBOOK_ENTRIES_EXIST, new Boolean(false).toString());
		}
		/* ... till here*/

		
		MonitoringUtil.buildQaStatsDTO(request,qaService, qaContent);
		
	    logger.debug("submitting session to refresh the data from the database: ");
	    return (mapping.findForward(LOAD_MONITORING));
     }
	

    public ActionForward hideResponse(ActionMapping mapping,
            ActionForm form,
            HttpServletRequest request,
            HttpServletResponse response) throws IOException,
                                         ServletException, ToolException
     {
        logger.debug("dispatching hideResponse...");
    	IQaService qaService = QaServiceProxy.getQaService(getServlet().getServletContext());
		logger.debug("qaService: " + qaService);
		
    	QaMonitoringForm qaMonitoringForm = (QaMonitoringForm) form;
    	
		String contentFolderID = WebUtil.readStrParam(request, AttributeNames.PARAM_CONTENT_FOLDER_ID);
		logger.debug("contentFolderID: " + contentFolderID);
		qaMonitoringForm.setContentFolderID(contentFolderID);


    	String currentUid=qaMonitoringForm.getCurrentUid();
    	logger.debug("currentUid: " + currentUid);
        QaUsrResp qaUsrResp =qaService.getAttemptByUID(new Long(currentUid));
        logger.debug("qaUsrResp: " + qaUsrResp);
        qaUsrResp.setVisible(false);
        qaService.updateUserResponse(qaUsrResp);
        qaService.hideResponse(qaUsrResp);
        logger.debug("qaUsrResp: " + qaUsrResp);
        
	    String strToolContentID=request.getParameter(AttributeNames.PARAM_TOOL_CONTENT_ID);
		logger.debug("strToolContentID: " + strToolContentID);
		qaMonitoringForm.setToolContentID(strToolContentID);
		
    	String editResponse=request.getParameter(EDIT_RESPONSE);
		logger.debug("editResponse: " + editResponse);
		qaMonitoringForm.setEditResponse(editResponse);


	    QaContent qaContent=qaService.loadQa(new Long(strToolContentID).longValue());
		logger.debug("existing qaContent:" + qaContent);
        
		Map summaryToolSessions=MonitoringUtil.populateToolSessions(request, qaContent, qaService);
		logger.debug("summaryToolSessions: " + summaryToolSessions);
		request.setAttribute(SUMMARY_TOOL_SESSIONS, summaryToolSessions);
		logger.debug("SUMMARY_TOOL_SESSIONS: " + request.getAttribute(SUMMARY_TOOL_SESSIONS));


	    Map summaryToolSessionsId=MonitoringUtil.populateToolSessionsId(request, qaContent, qaService);
		logger.debug("summaryToolSessionsId: " + summaryToolSessionsId);
		request.setAttribute(SUMMARY_TOOL_SESSIONS_ID, summaryToolSessionsId);


    	String currentMonitoredToolSession=qaMonitoringForm.getSelectedToolSessionId(); 
	    logger.debug("currentMonitoredToolSession: " + currentMonitoredToolSession);

	    if (currentMonitoredToolSession.equals(""))
	    {
	        currentMonitoredToolSession="All";
	    }

		if (currentMonitoredToolSession.equals("All"))
	    {
		    request.setAttribute(SELECTION_CASE, new Long(2));
	    }
	    else
	    {
	        request.setAttribute(SELECTION_CASE, new Long(1));
	    }

		logger.debug("SELECTION_CASE: " + request.getAttribute(SELECTION_CASE));

		GeneralLearnerFlowDTO generalLearnerFlowDTO= LearningUtil.buildGeneralLearnerFlowDTO(qaContent);
	    logger.debug("generalLearnerFlowDTO: " + generalLearnerFlowDTO);
	    
	    refreshSummaryData(request, qaContent, qaService, true, false, null, null, generalLearnerFlowDTO, false);
	    
	    prepareReflectionData(request, qaContent, qaService,null, false);
	    
	    prepareEditActivityScreenData(request, qaContent);
	    
	    EditActivityDTO editActivityDTO = new EditActivityDTO();
		boolean isContentInUse=QaUtils.isContentInUse(qaContent);
		logger.debug("isContentInUse:" + isContentInUse);
		if (isContentInUse == true)
		{
		    editActivityDTO.setMonitoredContentInUse(new Boolean(true).toString());
		}
		request.setAttribute(EDIT_ACTIVITY_DTO, editActivityDTO);

		/*find out if there are any reflection entries, from here*/
		boolean notebookEntriesExist=MonitoringUtil.notebookEntriesExist(qaService, qaContent);
		logger.debug("notebookEntriesExist : " + notebookEntriesExist);
		
		if (notebookEntriesExist)
		{
		    request.setAttribute(NOTEBOOK_ENTRIES_EXIST, new Boolean(true).toString());
		}
		else
		{
		    request.setAttribute(NOTEBOOK_ENTRIES_EXIST, new Boolean(false).toString());
		}
		
		MonitoringUtil.buildQaStatsDTO(request,qaService, qaContent);
		
	    logger.debug("submitting session to refresh the data from the database: ");
	    return (mapping.findForward(LOAD_MONITORING));
     }
    
    
	public ActionForward openNotebook(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws IOException,
            ServletException, ToolException
    {
        logger.debug("dispatching openNotebook...");
    	QaMonitoringForm qaMonitoringForm = (QaMonitoringForm) form;
    	
    	IQaService qaService = QaServiceProxy.getQaService(getServlet().getServletContext());
		logger.debug("qaService: " + qaService);


		String contentFolderID = WebUtil.readStrParam(request, AttributeNames.PARAM_CONTENT_FOLDER_ID);
		logger.debug("contentFolderID: " + contentFolderID);
		qaMonitoringForm.setContentFolderID(contentFolderID);

		
		String uid=request.getParameter("uid");
		logger.debug("uid: " + uid);
		
		String userId=request.getParameter("userId");
		logger.debug("userId: " + userId);
		
		String userName=request.getParameter("userName");
		logger.debug("userName: " + userName);

		String sessionId=request.getParameter("sessionId");
		logger.debug("sessionId: " + sessionId);
		
		
		NotebookEntry notebookEntry = qaService.getEntry(new Long(sessionId),
				CoreNotebookConstants.NOTEBOOK_TOOL,
				MY_SIGNATURE, new Integer(userId));
		
        logger.debug("notebookEntry: " + notebookEntry);
		
        GeneralLearnerFlowDTO generalLearnerFlowDTO= new GeneralLearnerFlowDTO();
		if (notebookEntry != null) {
		    String notebookEntryPresentable=QaUtils.replaceNewLines(notebookEntry.getEntry());
		    generalLearnerFlowDTO.setNotebookEntry(notebookEntryPresentable);
		    generalLearnerFlowDTO.setUserName(userName);
		}

		logger.debug("generalLearnerFlowDTO: " + generalLearnerFlowDTO);
		request.setAttribute(GENERAL_LEARNER_FLOW_DTO, generalLearnerFlowDTO);
		
		QaSession qaSession=qaService.retrieveQaSessionOrNullById(new Long(sessionId).longValue());
		logger.debug("qaSession: " + qaSession);
		
		QaContent qaContent=qaSession.getQaContent();
		logger.debug("qaContent: " + qaContent);
		
		prepareEditActivityScreenData(request, qaContent);
		
		GeneralMonitoringDTO generalMonitoringDTO= new GeneralMonitoringDTO();
        List attachmentList = qaService.retrieveQaUploadedFiles(qaContent);
        logger.debug("attachmentList: " + attachmentList);
        generalMonitoringDTO.setAttachmentList(attachmentList);
    	logger.debug("final generalMonitoringDTO: " + generalMonitoringDTO );
    	request.setAttribute(QA_GENERAL_MONITORING_DTO, generalMonitoringDTO);


		/*find out if there are any reflection entries, from here*/
		boolean notebookEntriesExist=MonitoringUtil.notebookEntriesExist(qaService, qaContent);
		logger.debug("notebookEntriesExist : " + notebookEntriesExist);
		
		if (notebookEntriesExist)
		{
		    request.setAttribute(NOTEBOOK_ENTRIES_EXIST, new Boolean(true).toString());
		}
		else
		{
		    request.setAttribute(NOTEBOOK_ENTRIES_EXIST, new Boolean(false).toString());
		}
		/* ... till here*/
		
		MonitoringUtil.buildQaStatsDTO(request,qaService, qaContent);
		
		return mapping.findForward(LEARNER_NOTEBOOK);
	}
	
	public void prepareReflectionData(HttpServletRequest request, QaContent qaContent, 
	        IQaService qaService, String userID, boolean exportMode)
	{
	    logger.debug("starting prepareReflectionData: " + qaContent);
	    logger.debug("userID: " + userID);
	    logger.debug("exportMode: " + exportMode);
	    
	    List reflectionsContainerDTO= new LinkedList();
	    if (userID == null)
	    {
	        logger.debug("all users mode");
		    for (Iterator sessionIter = qaContent.getQaSessions().iterator(); sessionIter.hasNext();) 
		    {
		       QaSession qaSession = (QaSession) sessionIter.next();
		       logger.debug("qaSession: " + qaSession);
		       for (Iterator userIter = qaSession.getQaQueUsers().iterator(); userIter.hasNext();) 
		       {
					QaQueUsr user = (QaQueUsr) userIter.next();
					logger.debug("user: " + user);

					NotebookEntry notebookEntry = qaService.getEntry(qaSession.getQaSessionId(),
							CoreNotebookConstants.NOTEBOOK_TOOL,
							MY_SIGNATURE, new Integer(user.getQueUsrId().toString()));
					
					logger.debug("notebookEntry: " + notebookEntry);
					
					if (notebookEntry != null) {
					    ReflectionDTO reflectionDTO = new ReflectionDTO();
					    reflectionDTO.setUserId(user.getQueUsrId().toString());
					    reflectionDTO.setSessionId(qaSession.getQaSessionId().toString());
					    reflectionDTO.setUserName(user.getUsername());
					    reflectionDTO.setReflectionUid (notebookEntry.getUid().toString());
					    String notebookEntryPresentable=QaUtils.replaceNewLines(notebookEntry.getEntry());
					    reflectionDTO.setEntry(notebookEntryPresentable);
					    reflectionsContainerDTO.add(reflectionDTO);
					} 
		       }
		   }
	   }
	   else
	   {
	       logger.debug("single user mode");
		    for (Iterator sessionIter = qaContent.getQaSessions().iterator(); sessionIter.hasNext();) 
		    {
		       QaSession qaSession = (QaSession) sessionIter.next();
		       logger.debug("qaSession: " + qaSession);
		       for (Iterator userIter = qaSession.getQaQueUsers().iterator(); userIter.hasNext();) 
		       {
					QaQueUsr user = (QaQueUsr) userIter.next();
					logger.debug("user: " + user);
					
					if (user.getQueUsrId().toString().equals(userID))
					{
					    logger.debug("getting reflection for user with  userID: " + userID);
						NotebookEntry notebookEntry = qaService.getEntry(qaSession.getQaSessionId(),
								CoreNotebookConstants.NOTEBOOK_TOOL,
								MY_SIGNATURE, new Integer(user.getQueUsrId().toString()));
						
						logger.debug("notebookEntry: " + notebookEntry);
						
						if (notebookEntry != null) {
						    ReflectionDTO reflectionDTO = new ReflectionDTO();
						    reflectionDTO.setUserId(user.getQueUsrId().toString());
						    reflectionDTO.setSessionId(qaSession.getQaSessionId().toString());
						    reflectionDTO.setUserName(user.getUsername());
						    reflectionDTO.setReflectionUid (notebookEntry.getUid().toString());
						    String notebookEntryPresentable=QaUtils.replaceNewLines(notebookEntry.getEntry());
						    reflectionDTO.setEntry(notebookEntryPresentable);
						    reflectionsContainerDTO.add(reflectionDTO);
						} 
					}
		       }		       
	   }
    }
	    
	    
	   logger.debug("reflectionsContainerDTO: " + reflectionsContainerDTO);
	   request.setAttribute(REFLECTIONS_CONTAINER_DTO, reflectionsContainerDTO);
	   
	   if (exportMode)
	   {
	       request.getSession().setAttribute(REFLECTIONS_CONTAINER_DTO, reflectionsContainerDTO);
	   }
	   
	}
	
	
	public void prepareEditActivityScreenData(HttpServletRequest request, QaContent qaContent)
	{
		logger.debug("starting prepareEditActivityScreenData: " + qaContent);
	    QaGeneralAuthoringDTO qaGeneralAuthoringDTO= new QaGeneralAuthoringDTO();
		
	    if (qaContent.getTitle() == null)
		{
			qaGeneralAuthoringDTO.setActivityTitle(DEFAULT_QA_TITLE);
		}
		else
		{
			qaGeneralAuthoringDTO.setActivityTitle(qaContent.getTitle());
		}

		
		if (qaContent.getInstructions() == null)
		{
		    qaGeneralAuthoringDTO.setActivityInstructions(DEFAULT_QA_INSTRUCTIONS);
		}
		else
		{
			qaGeneralAuthoringDTO.setActivityInstructions(qaContent.getInstructions());
		}
		
	
		logger.debug("final qaGeneralAuthoringDTO: " + qaGeneralAuthoringDTO);
		request.setAttribute(QA_GENERAL_AUTHORING_DTO, qaGeneralAuthoringDTO);
	}
	

	
    public ActionForward submitAllContent(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) 
    throws IOException, ServletException {
    	logger.debug("dispatching proxy submitAllContent...");

    	/*start authoring code*/
    	QaAuthoringForm qaAuthoringForm = (QaMonitoringForm) form;
    	logger.debug("dispathcing submitAllContent :" +form);

		IQaService qaService =QaServiceProxy.getQaService(getServlet().getServletContext());
		logger.debug("qaService: " + qaService);
		
		String httpSessionID=request.getParameter("httpSessionID");
		logger.debug("httpSessionID: " + httpSessionID);
		
		SessionMap sessionMap=(SessionMap)request.getSession().getAttribute(httpSessionID);
		logger.debug("sessionMap: " + sessionMap);

		String contentFolderID = WebUtil.readStrParam(request, AttributeNames.PARAM_CONTENT_FOLDER_ID);
		logger.debug("contentFolderID: " + contentFolderID);
		qaAuthoringForm.setContentFolderID(contentFolderID);

		String activeModule=request.getParameter(ACTIVE_MODULE);
		logger.debug("activeModule: " + activeModule);

		String strToolContentID=request.getParameter(AttributeNames.PARAM_TOOL_CONTENT_ID);
		logger.debug("strToolContentID: " + strToolContentID);

		String defaultContentIdStr=request.getParameter(DEFAULT_CONTENT_ID_STR);
		logger.debug("defaultContentIdStr: " + defaultContentIdStr);
		
        List listQuestionContentDTO=(List)sessionMap.get(LIST_QUESTION_CONTENT_DTO_KEY);
        logger.debug("listQuestionContentDTO: " + listQuestionContentDTO);
        
        Map mapQuestionContent=AuthoringUtil.extractMapQuestionContent(listQuestionContentDTO);
        logger.debug("extracted mapQuestionContent: " + mapQuestionContent);
                

        Map mapFeedback=AuthoringUtil.extractMapFeedback(listQuestionContentDTO);
        logger.debug("extracted mapFeedback: " + mapFeedback);

        ActionMessages errors = new ActionMessages();
        logger.debug("mapQuestionContent size: " + mapQuestionContent.size());
        
        if (mapQuestionContent.size() == 0)
        {
            ActionMessage error = new ActionMessage("questions.none.submitted");
			errors.add(ActionMessages.GLOBAL_MESSAGE, error);
        }
        logger.debug("errors: " + errors);
        
	    AuthoringUtil authoringUtil= new AuthoringUtil();
	 	
	 	QaGeneralAuthoringDTO qaGeneralAuthoringDTO= new QaGeneralAuthoringDTO();
		qaGeneralAuthoringDTO.setContentFolderID(contentFolderID);
		
	 	String richTextTitle = request.getParameter(TITLE);
        String richTextInstructions = request.getParameter(INSTRUCTIONS);
	 	
        logger.debug("richTextTitle: " + richTextTitle);
        logger.debug("richTextInstructions: " + richTextInstructions);
        
        qaGeneralAuthoringDTO.setActivityTitle(richTextTitle);
        qaAuthoringForm.setTitle(richTextTitle);
        
        qaGeneralAuthoringDTO.setActivityInstructions(richTextInstructions);
        
        sessionMap.put(ACTIVITY_TITLE_KEY, richTextTitle);
        sessionMap.put(ACTIVITY_INSTRUCTIONS_KEY, richTextInstructions);
        
	    qaGeneralAuthoringDTO.setMapQuestionContent(mapQuestionContent);
		logger.debug("qaGeneralAuthoringDTO: " + qaGeneralAuthoringDTO);
        
		
		logger.debug("qaGeneralAuthoringDTO now: " + qaGeneralAuthoringDTO);
		request.setAttribute(QA_GENERAL_AUTHORING_DTO, qaGeneralAuthoringDTO);
		
	 	logger.debug("there are no issues with input, continue and submit data");

        QaContent qaContentTest=qaService.loadQa(new Long(strToolContentID).longValue());
		logger.debug("qaContentTest: " + qaContentTest);

		logger.debug("errors: " + errors);
	 	if(!errors.isEmpty()){
			saveErrors(request, errors);
	        logger.debug("errors saved: " + errors);
		}
	 	

	 	GeneralMonitoringDTO qaGeneralMonitoringDTO=new GeneralMonitoringDTO();
	 	
	 	QaContent qaContent=qaContentTest;
	 	if(errors.isEmpty()){
	 	    logger.debug("errors is empty: " + errors);
		 	/*to remove deleted entries in the questions table based on mapQuestionContent */
	        authoringUtil.removeRedundantQuestions(mapQuestionContent, qaService, qaAuthoringForm, request, strToolContentID);
	        logger.debug("end of removing unused entries... ");

	        qaContent=authoringUtil.saveOrUpdateQaContent(mapQuestionContent, mapFeedback, qaService, qaAuthoringForm, request, qaContentTest, strToolContentID);
	        logger.debug("qaContent: " + qaContent);
	        
	        
	        long defaultContentID=0;
			logger.debug("attempt retrieving tool with signatute : " + MY_SIGNATURE);
	        defaultContentID=qaService.getToolDefaultContentIdBySignature(MY_SIGNATURE);
			logger.debug("retrieved tool default contentId: " + defaultContentID);
			
	        if (qaContent != null)
	        {
	            qaGeneralAuthoringDTO.setDefaultContentIdStr(new Long(defaultContentID).toString());
	        }
			logger.debug("updated qaGeneralAuthoringDTO to: " + qaGeneralAuthoringDTO);
			
		    authoringUtil.reOrganizeDisplayOrder(mapQuestionContent, qaService, qaAuthoringForm, qaContent);    
	        
		    logger.debug("strToolContentID: " + strToolContentID);
	        QaUtils.setDefineLater(request, false, strToolContentID, qaService);
	        logger.debug("define later set to false");
	        
			QaUtils.setFormProperties(request, qaService,  
		             qaAuthoringForm, qaGeneralAuthoringDTO, strToolContentID, defaultContentIdStr, activeModule, sessionMap, httpSessionID);

			qaGeneralMonitoringDTO.setDefineLaterInEditMode(new Boolean(false).toString());
	 	}
	 	else
	 	{
	 	   logger.debug("errors is not empty: " + errors);
	 	   
	 	   if (qaContent != null)
	 	   {
		        long defaultContentID=0;
				logger.debug("attempt retrieving tool with signatute : " + MY_SIGNATURE);
		        defaultContentID=qaService.getToolDefaultContentIdBySignature(MY_SIGNATURE);
				logger.debug("retrieved tool default contentId: " + defaultContentID);
				
		        if (qaContent != null)
		        {
		            qaGeneralAuthoringDTO.setDefaultContentIdStr(new Long(defaultContentID).toString());
		        }

				QaUtils.setFormProperties(request, qaService, 
			             qaAuthoringForm, qaGeneralAuthoringDTO, strToolContentID, defaultContentIdStr, activeModule, sessionMap, httpSessionID);
	 	       
	 	   }
	 	   qaGeneralMonitoringDTO.setDefineLaterInEditMode(new Boolean(true).toString());
	 	}
        
        qaGeneralAuthoringDTO.setSbmtSuccess(new Integer(1).toString());
        
        qaAuthoringForm.resetUserAction();
        qaGeneralAuthoringDTO.setMapQuestionContent(mapQuestionContent);
        
		logger.debug("before saving final qaGeneralAuthoringDTO: " + qaGeneralAuthoringDTO);
		request.setAttribute(QA_GENERAL_AUTHORING_DTO, qaGeneralAuthoringDTO);

		request.setAttribute(LIST_QUESTION_CONTENT_DTO,listQuestionContentDTO);
		sessionMap.put(LIST_QUESTION_CONTENT_DTO_KEY, listQuestionContentDTO);
	    request.getSession().setAttribute(httpSessionID, sessionMap);
	    
		request.setAttribute(TOTAL_QUESTION_COUNT, new Integer(listQuestionContentDTO.size()));
		
		
		qaGeneralAuthoringDTO.setToolContentID(strToolContentID);
		qaGeneralAuthoringDTO.setHttpSessionID(httpSessionID);
		qaGeneralAuthoringDTO.setActiveModule(activeModule);
		qaGeneralAuthoringDTO.setDefaultContentIdStr(defaultContentIdStr);
		
		qaAuthoringForm.setToolContentID(strToolContentID);
		qaAuthoringForm.setHttpSessionID(httpSessionID);
		qaAuthoringForm.setActiveModule(activeModule);
		qaAuthoringForm.setDefaultContentIdStr(defaultContentIdStr);
		qaAuthoringForm.setCurrentTab("3");
		

		/*start monitoring code*/

		if (qaService.studentActivityOccurredGlobal(qaContent))
		{
			logger.debug("USER_EXCEPTION_NO_TOOL_SESSIONS is set to false");
			qaGeneralMonitoringDTO.setUserExceptionNoToolSessions(new Boolean(false).toString());
		}
		else
		{
			logger.debug("USER_EXCEPTION_NO_TOOL_SESSIONS is set to true");
			qaGeneralMonitoringDTO.setUserExceptionNoToolSessions(new Boolean(true).toString());
		}

		qaGeneralMonitoringDTO.setOnlineInstructions(qaContent.getOnlineInstructions());
		qaGeneralMonitoringDTO.setOfflineInstructions(qaContent.getOfflineInstructions());

		List attachmentList = qaService.retrieveQaUploadedFiles(qaContent);
        logger.debug("attachmentList: " + attachmentList);
        qaGeneralMonitoringDTO.setAttachmentList(attachmentList);
    	logger.debug("final qaGeneralMonitoringDTO: " + qaGeneralMonitoringDTO );
		
    	logger.debug("final generalMonitoringDTO: " + qaGeneralMonitoringDTO );
		request.setAttribute(QA_GENERAL_MONITORING_DTO, qaGeneralMonitoringDTO);

		prepareReflectionData(request, qaContent, qaService, null, false);
		
		/*find out if there are any reflection entries, from here*/
		boolean notebookEntriesExist=MonitoringUtil.notebookEntriesExist(qaService, qaContent);
		logger.debug("notebookEntriesExist : " + notebookEntriesExist);
		
		if (notebookEntriesExist)
		{
		    request.setAttribute(NOTEBOOK_ENTRIES_EXIST, new Boolean(true).toString());
		}
		else
		{
		    request.setAttribute(NOTEBOOK_ENTRIES_EXIST, new Boolean(false).toString());
		}
		/* ... till here*/
		
		MonitoringUtil.buildQaStatsDTO(request,qaService, qaContent);

        return mapping.findForward(LOAD_MONITORING);
    }
    
    
    
	public ActionForward saveSingleQuestion(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) 
    throws IOException, ServletException {
		
		logger.debug("dispathcing proxy saveSingleQuestion");
    	
		/*start authoring code*/
    	QaAuthoringForm qaAuthoringForm = (QaMonitoringForm) form;
		logger.debug("dispathcing saveSingleQuestion");
	
		IQaService qaService =QaServiceProxy.getQaService(getServlet().getServletContext());
		logger.debug("qaService: " + qaService);
		
		String httpSessionID=request.getParameter("httpSessionID");
		logger.debug("httpSessionID: " + httpSessionID);
		
		
		SessionMap sessionMap=(SessionMap)request.getSession().getAttribute(httpSessionID);
		logger.debug("sessionMap: " + sessionMap);
		
		String contentFolderID = WebUtil.readStrParam(request, AttributeNames.PARAM_CONTENT_FOLDER_ID);
		logger.debug("contentFolderID: " + contentFolderID);
		qaAuthoringForm.setContentFolderID(contentFolderID);
		
		String editQuestionBoxRequest=request.getParameter("editQuestionBoxRequest");
		logger.debug("editQuestionBoxRequest: " + editQuestionBoxRequest);
	
	
		String activeModule=request.getParameter(ACTIVE_MODULE);
		logger.debug("activeModule: " + activeModule);
	
		String strToolContentID=request.getParameter(AttributeNames.PARAM_TOOL_CONTENT_ID);
		logger.debug("strToolContentID: " + strToolContentID);
		
		String defaultContentIdStr=request.getParameter(DEFAULT_CONTENT_ID_STR);
		logger.debug("defaultContentIdStr: " + defaultContentIdStr);
		
		QaContent qaContent=qaService.loadQa(new Long(strToolContentID).longValue());
		logger.debug("qaContent: " + qaContent);
		
		if (qaContent == null)
		{
			logger.debug("using defaultContentIdStr: " + defaultContentIdStr);
			qaContent=qaService.loadQa(new Long(defaultContentIdStr).longValue());
		}
		logger.debug("final qaContent: " + qaContent);
	
		QaGeneralAuthoringDTO qaGeneralAuthoringDTO= new QaGeneralAuthoringDTO();
		
		logger.debug("qaGeneralAuthoringDTO: " + qaGeneralAuthoringDTO);
		qaGeneralAuthoringDTO.setContentFolderID(contentFolderID);
		
		qaGeneralAuthoringDTO.setSbmtSuccess(new Integer(0).toString());
		
	    AuthoringUtil authoringUtil= new AuthoringUtil();
	    
        List listQuestionContentDTO=(List)sessionMap.get(LIST_QUESTION_CONTENT_DTO_KEY);
        logger.debug("listQuestionContentDTO: " + listQuestionContentDTO);
        
        
	    String newQuestion=request.getParameter("newQuestion");
	    logger.debug("newQuestion: " + newQuestion);
	    
	    String feedback=request.getParameter("feedback");
	    logger.debug("feedback: " + feedback);

	    
	    String editableQuestionIndex=request.getParameter("editableQuestionIndex");
	    logger.debug("editableQuestionIndex: " + editableQuestionIndex);
	    

	    if ((newQuestion != null) && (newQuestion.length() > 0))
	    {
		        if ((editQuestionBoxRequest != null) && (editQuestionBoxRequest.equals("false")))
		        {
		            logger.debug("request for add and save");
			        boolean duplicates=AuthoringUtil.checkDuplicateQuestions(listQuestionContentDTO, newQuestion);
			        logger.debug("duplicates: " + duplicates);
		            
			        if (!duplicates)
			        {
					    QaQuestionContentDTO qaQuestionContentDTO= null;
					    Iterator listIterator=listQuestionContentDTO.iterator();
					    while (listIterator.hasNext())
					    {
					        qaQuestionContentDTO= (QaQuestionContentDTO)listIterator.next();
					        logger.debug("qaQuestionContentDTO:" + qaQuestionContentDTO);
					        logger.debug("qaQuestionContentDTO question:" + qaQuestionContentDTO.getQuestion());
					        
					        String question=qaQuestionContentDTO.getQuestion();
					        String displayOrder=qaQuestionContentDTO.getDisplayOrder();
					        logger.debug("displayOrder:" + displayOrder);
					        
					        if ((displayOrder != null) && (!displayOrder.equals("")))
				    		{
					            if (displayOrder.equals(editableQuestionIndex))
					            {
					                break;
					            }
					            
				    		}
					    }
					    logger.debug("qaQuestionContentDTO found:" + qaQuestionContentDTO);
					    
					    qaQuestionContentDTO.setQuestion(newQuestion);
					    qaQuestionContentDTO.setFeedback(feedback);
					    qaQuestionContentDTO.setDisplayOrder(editableQuestionIndex);
					    
					    listQuestionContentDTO=AuthoringUtil.reorderUpdateListQuestionContentDTO(listQuestionContentDTO, qaQuestionContentDTO, editableQuestionIndex);
					    logger.debug("post reorderUpdateListQuestionContentDTO listQuestionContentDTO: " + listQuestionContentDTO);
			        }
			        else
			        {
			            logger.debug("duplicate question entry, not adding");
			        }
		        }
			    else
			    {
			        	logger.debug("request for edit and save.");
					    QaQuestionContentDTO qaQuestionContentDTO= null;
					    Iterator listIterator=listQuestionContentDTO.iterator();
					    while (listIterator.hasNext())
					    {
					        qaQuestionContentDTO= (QaQuestionContentDTO)listIterator.next();
					        logger.debug("qaQuestionContentDTO:" + qaQuestionContentDTO);
					        logger.debug("qaQuestionContentDTO question:" + qaQuestionContentDTO.getQuestion());
					        
					        String question=qaQuestionContentDTO.getQuestion();
					        String displayOrder=qaQuestionContentDTO.getDisplayOrder();
					        logger.debug("displayOrder:" + displayOrder);
					        
					        if ((displayOrder != null) && (!displayOrder.equals("")))
				    		{
					            if (displayOrder.equals(editableQuestionIndex))
					            {
					                break;
					            }
					            
				    		}
					    }
					    logger.debug("qaQuestionContentDTO found:" + qaQuestionContentDTO);
					    
					    qaQuestionContentDTO.setQuestion(newQuestion);
					    qaQuestionContentDTO.setFeedback(feedback);
					    qaQuestionContentDTO.setDisplayOrder(editableQuestionIndex);
					    
					    listQuestionContentDTO=AuthoringUtil.reorderUpdateListQuestionContentDTO(listQuestionContentDTO, qaQuestionContentDTO, editableQuestionIndex);
					    logger.debug("post reorderUpdateListQuestionContentDTO listQuestionContentDTO: " + listQuestionContentDTO);			        
			  }
	    }
        else
        {
            logger.debug("entry blank, not adding");
        }
	    

	    request.setAttribute(LIST_QUESTION_CONTENT_DTO,listQuestionContentDTO);
		sessionMap.put(LIST_QUESTION_CONTENT_DTO_KEY, listQuestionContentDTO);
	    logger.debug("listQuestionContentDTO now: " + listQuestionContentDTO);
	    
	    
		String richTextTitle = request.getParameter(TITLE);
	    String richTextInstructions = request.getParameter(INSTRUCTIONS);
	
	    logger.debug("richTextTitle: " + richTextTitle);
	    logger.debug("richTextInstructions: " + richTextInstructions);
	    qaGeneralAuthoringDTO.setActivityTitle(richTextTitle);
	    qaAuthoringForm.setTitle(richTextTitle);
	    
		qaGeneralAuthoringDTO.setActivityInstructions(richTextInstructions);
			
	    sessionMap.put(ACTIVITY_TITLE_KEY, richTextTitle);
	    sessionMap.put(ACTIVITY_INSTRUCTIONS_KEY, richTextInstructions);
	
	    qaGeneralAuthoringDTO.setEditActivityEditMode(new Boolean(true).toString());
	    
	    request.getSession().setAttribute(httpSessionID, sessionMap);
	    sessionMap.put(LIST_QUESTION_CONTENT_DTO_KEY, listQuestionContentDTO);
	    
		QaUtils.setFormProperties(request, qaService,  
	             qaAuthoringForm, qaGeneralAuthoringDTO, strToolContentID, defaultContentIdStr, activeModule, sessionMap, httpSessionID);
	
		
		qaGeneralAuthoringDTO.setToolContentID(strToolContentID);
		qaGeneralAuthoringDTO.setHttpSessionID(httpSessionID);
		qaGeneralAuthoringDTO.setActiveModule(activeModule);
		qaGeneralAuthoringDTO.setDefaultContentIdStr(defaultContentIdStr);
		
		qaAuthoringForm.setToolContentID(strToolContentID);
		qaAuthoringForm.setHttpSessionID(httpSessionID);
		qaAuthoringForm.setActiveModule(activeModule);
		qaAuthoringForm.setDefaultContentIdStr(defaultContentIdStr);
		qaAuthoringForm.setCurrentTab("3");
		
	    
		logger.debug("qaGeneralAuthoringDTO now: " + qaGeneralAuthoringDTO);
		request.setAttribute(QA_GENERAL_AUTHORING_DTO, qaGeneralAuthoringDTO);
	    
	    logger.debug("httpSessionID: " + httpSessionID);
	    logger.debug("sessionMap: " + sessionMap);
	    
	    request.getSession().setAttribute(httpSessionID, sessionMap);
	    logger.debug("qaGeneralAuthoringDTO.getMapQuestionContent(); " + qaGeneralAuthoringDTO.getMapQuestionContent());
		
	    request.setAttribute(TOTAL_QUESTION_COUNT, new Integer(listQuestionContentDTO.size()));
		
		/*start monitoring code*/
		GeneralMonitoringDTO qaGeneralMonitoringDTO=new GeneralMonitoringDTO();
		qaGeneralMonitoringDTO.setDefineLaterInEditMode(new Boolean(false).toString());

		if (qaService.studentActivityOccurredGlobal(qaContent))
		{
			logger.debug("USER_EXCEPTION_NO_TOOL_SESSIONS is set to false");
			qaGeneralMonitoringDTO.setUserExceptionNoToolSessions(new Boolean(false).toString());
		}
		else
		{
			logger.debug("USER_EXCEPTION_NO_TOOL_SESSIONS is set to true");
			qaGeneralMonitoringDTO.setUserExceptionNoToolSessions(new Boolean(true).toString());
		}
		qaGeneralMonitoringDTO.setDefineLaterInEditMode(new Boolean(true).toString());

		qaGeneralMonitoringDTO.setOnlineInstructions(qaContent.getOnlineInstructions());
		qaGeneralMonitoringDTO.setOfflineInstructions(qaContent.getOfflineInstructions());

		List attachmentList = qaService.retrieveQaUploadedFiles(qaContent);
        logger.debug("attachmentList: " + attachmentList);
        qaGeneralMonitoringDTO.setAttachmentList(attachmentList);
    	logger.debug("final qaGeneralMonitoringDTO: " + qaGeneralMonitoringDTO );

		
    	logger.debug("final generalMonitoringDTO: " + qaGeneralMonitoringDTO );
		request.setAttribute(QA_GENERAL_MONITORING_DTO, qaGeneralMonitoringDTO);

		prepareReflectionData(request, qaContent, qaService, null, false);
		
		/*find out if there are any reflection entries, from here*/
		boolean notebookEntriesExist=MonitoringUtil.notebookEntriesExist(qaService, qaContent);
		logger.debug("notebookEntriesExist : " + notebookEntriesExist);
		
		if (notebookEntriesExist)
		{
		    request.setAttribute(NOTEBOOK_ENTRIES_EXIST, new Boolean(true).toString());
		}
		else
		{
		    request.setAttribute(NOTEBOOK_ENTRIES_EXIST, new Boolean(false).toString());
		}
		/* ... till here*/
	
		MonitoringUtil.buildQaStatsDTO(request,qaService, qaContent);
		
        return mapping.findForward(LOAD_MONITORING);
	}

    
	
	public ActionForward addSingleQuestion(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) 
    throws IOException, ServletException 
    {
		logger.debug("dispathcing proxy addSingleQuestion");

		/*start authoring code*/
    	QaAuthoringForm qaAuthoringForm = (QaMonitoringForm) form;
		logger.debug("dispathcing  addSingleQuestion");
		
		IQaService qaService =QaServiceProxy.getQaService(getServlet().getServletContext());
		logger.debug("qaService: " + qaService);
		
		String httpSessionID=request.getParameter("httpSessionID");
		logger.debug("httpSessionID: " + httpSessionID);
		
		SessionMap sessionMap=(SessionMap)request.getSession().getAttribute(httpSessionID);
		logger.debug("sessionMap: " + sessionMap);
		
		String contentFolderID = WebUtil.readStrParam(request, AttributeNames.PARAM_CONTENT_FOLDER_ID);
		logger.debug("contentFolderID: " + contentFolderID);
		qaAuthoringForm.setContentFolderID(contentFolderID);
	
	
		String activeModule=request.getParameter(ACTIVE_MODULE);
		logger.debug("activeModule: " + activeModule);
	
		String strToolContentID=request.getParameter(AttributeNames.PARAM_TOOL_CONTENT_ID);
		logger.debug("strToolContentID: " + strToolContentID);
		
		String defaultContentIdStr=request.getParameter(DEFAULT_CONTENT_ID_STR);
		logger.debug("defaultContentIdStr: " + defaultContentIdStr);
		
		QaContent qaContent=qaService.loadQa(new Long(strToolContentID).longValue());
		logger.debug("qaContent: " + qaContent);
		
		if (qaContent == null)
		{
			logger.debug("using defaultContentIdStr: " + defaultContentIdStr);
			qaContent=qaService.loadQa(new Long(defaultContentIdStr).longValue());
		}
		logger.debug("final qaContent: " + qaContent);
	
		QaGeneralAuthoringDTO qaGeneralAuthoringDTO= new QaGeneralAuthoringDTO();
		logger.debug("qaGeneralAuthoringDTO: " + qaGeneralAuthoringDTO);
		qaGeneralAuthoringDTO.setContentFolderID(contentFolderID);
		
		qaGeneralAuthoringDTO.setSbmtSuccess(new Integer(0).toString());
		
	    AuthoringUtil authoringUtil= new AuthoringUtil();
	    
	    List listQuestionContentDTO=(List)sessionMap.get(LIST_QUESTION_CONTENT_DTO_KEY);
	    logger.debug("listQuestionContentDTO: " + listQuestionContentDTO);
	    
	    String newQuestion=request.getParameter("newQuestion");
	    logger.debug("newQuestion: " + newQuestion);
	    
	    String feedback=request.getParameter("feedback");
	    logger.debug("feedback: " + feedback);
	    
	    
	    int listSize=listQuestionContentDTO.size();
	    logger.debug("listSize: " + listSize);
	    
	    if ((newQuestion != null) && (newQuestion.length() > 0))
	    {
	        boolean duplicates=AuthoringUtil.checkDuplicateQuestions(listQuestionContentDTO, newQuestion);
	        logger.debug("duplicates: " + duplicates);
	        
	        if (!duplicates)
	        {
			    QaQuestionContentDTO qaQuestionContentDTO=new QaQuestionContentDTO();
			    qaQuestionContentDTO.setDisplayOrder(new Long(listSize+1).toString());
			    qaQuestionContentDTO.setFeedback(feedback);
			    qaQuestionContentDTO.setQuestion(newQuestion);
			    
			    listQuestionContentDTO.add(qaQuestionContentDTO);
			    logger.debug("updated listQuestionContentDTO: " + listQuestionContentDTO);	            
	        }
	        else
	        {
	            logger.debug("entry duplicate, not adding");
	        }
	    }
	    else
	    {
	        logger.debug("entry blank, not adding");
	    }
	    
	    
	    request.setAttribute(LIST_QUESTION_CONTENT_DTO,listQuestionContentDTO);
		sessionMap.put(LIST_QUESTION_CONTENT_DTO_KEY, listQuestionContentDTO);
	
	    
		String richTextTitle = request.getParameter(TITLE);
	    String richTextInstructions = request.getParameter(INSTRUCTIONS);
	
	    logger.debug("richTextTitle: " + richTextTitle);
	    logger.debug("richTextInstructions: " + richTextInstructions);
	    qaGeneralAuthoringDTO.setActivityTitle(richTextTitle);
	    qaAuthoringForm.setTitle(richTextTitle);
	    
		qaGeneralAuthoringDTO.setActivityInstructions(richTextInstructions);
			
	    sessionMap.put(ACTIVITY_TITLE_KEY, richTextTitle);
	    sessionMap.put(ACTIVITY_INSTRUCTIONS_KEY, richTextInstructions);
	
	    qaGeneralAuthoringDTO.setEditActivityEditMode(new Boolean(true).toString());
	    request.getSession().setAttribute(httpSessionID, sessionMap);
	    
		QaUtils.setFormProperties(request, qaService,  
	             qaAuthoringForm, qaGeneralAuthoringDTO, strToolContentID, defaultContentIdStr, activeModule, sessionMap, httpSessionID);
	
		
		qaGeneralAuthoringDTO.setToolContentID(strToolContentID);
		qaGeneralAuthoringDTO.setHttpSessionID(httpSessionID);
		qaGeneralAuthoringDTO.setActiveModule(activeModule);
		qaGeneralAuthoringDTO.setDefaultContentIdStr(defaultContentIdStr);
		
		qaAuthoringForm.setToolContentID(strToolContentID);
		qaAuthoringForm.setHttpSessionID(httpSessionID);
		qaAuthoringForm.setActiveModule(activeModule);
		qaAuthoringForm.setDefaultContentIdStr(defaultContentIdStr);
		qaAuthoringForm.setCurrentTab("3");
		
		logger.debug("qaGeneralAuthoringDTO now: " + qaGeneralAuthoringDTO);
		request.setAttribute(QA_GENERAL_AUTHORING_DTO, qaGeneralAuthoringDTO);
	    
	    logger.debug("httpSessionID: " + httpSessionID);
	    logger.debug("sessionMap: " + sessionMap);
	    
	    request.getSession().setAttribute(httpSessionID, sessionMap);
	
	    logger.debug("qaGeneralAuthoringDTO.getMapQuestionContent(); " + qaGeneralAuthoringDTO.getMapQuestionContent());
	    
	    request.setAttribute(TOTAL_QUESTION_COUNT, new Integer(listQuestionContentDTO.size()));
		
		/*start monitoring code*/
		GeneralMonitoringDTO qaGeneralMonitoringDTO=new GeneralMonitoringDTO();
		qaGeneralMonitoringDTO.setDefineLaterInEditMode(new Boolean(false).toString());

		if (qaService.studentActivityOccurredGlobal(qaContent))
		{
			logger.debug("USER_EXCEPTION_NO_TOOL_SESSIONS is set to false");
			qaGeneralMonitoringDTO.setUserExceptionNoToolSessions(new Boolean(false).toString());
		}
		else
		{
			logger.debug("USER_EXCEPTION_NO_TOOL_SESSIONS is set to true");
			qaGeneralMonitoringDTO.setUserExceptionNoToolSessions(new Boolean(true).toString());
		}

		qaGeneralMonitoringDTO.setOnlineInstructions(qaContent.getOnlineInstructions());
		qaGeneralMonitoringDTO.setOfflineInstructions(qaContent.getOfflineInstructions());

		
		List attachmentList = qaService.retrieveQaUploadedFiles(qaContent);
        logger.debug("attachmentList: " + attachmentList);
        qaGeneralMonitoringDTO.setAttachmentList(attachmentList);
    	logger.debug("final qaGeneralMonitoringDTO: " + qaGeneralMonitoringDTO );

		
		qaGeneralMonitoringDTO.setDefineLaterInEditMode(new Boolean(true).toString());
    	logger.debug("final generalMonitoringDTO: " + qaGeneralMonitoringDTO );
		request.setAttribute(QA_GENERAL_MONITORING_DTO, qaGeneralMonitoringDTO);

		prepareReflectionData(request, qaContent, qaService, null, false);
		
		/*find out if there are any reflection entries, from here*/
		boolean notebookEntriesExist=MonitoringUtil.notebookEntriesExist(qaService, qaContent);
		logger.debug("notebookEntriesExist : " + notebookEntriesExist);
		
		if (notebookEntriesExist)
		{
		    request.setAttribute(NOTEBOOK_ENTRIES_EXIST, new Boolean(true).toString());
		}
		else
		{
		    request.setAttribute(NOTEBOOK_ENTRIES_EXIST, new Boolean(false).toString());
		}
		/* ... till here*/
		
		MonitoringUtil.buildQaStatsDTO(request,qaService, qaContent);
		
        return mapping.findForward(LOAD_MONITORING);
    }
	
	
	
    public ActionForward newQuestionBox(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) 
    throws IOException, ServletException 
    {
		logger.debug("dispathcing proxy newQuestionBox");

		/*start authoring code*/
    	QaAuthoringForm qaAuthoringForm = (QaMonitoringForm) form;
		logger.debug("dispathcing  newQuestionBox");
		
		IQaService qaService =QaServiceProxy.getQaService(getServlet().getServletContext());
		logger.debug("qaService: " + qaService);
		
		String httpSessionID=request.getParameter("httpSessionID");
		logger.debug("httpSessionID: " + httpSessionID);
		
		SessionMap sessionMap=(SessionMap)request.getSession().getAttribute(httpSessionID);
		logger.debug("sessionMap: " + sessionMap);
		
		String contentFolderID = WebUtil.readStrParam(request, AttributeNames.PARAM_CONTENT_FOLDER_ID);
		logger.debug("contentFolderID: " + contentFolderID);
		qaAuthoringForm.setContentFolderID(contentFolderID);


		String activeModule=request.getParameter(ACTIVE_MODULE);
		logger.debug("activeModule: " + activeModule);

		String strToolContentID=request.getParameter(AttributeNames.PARAM_TOOL_CONTENT_ID);
		logger.debug("strToolContentID: " + strToolContentID);
		
		String defaultContentIdStr=request.getParameter(DEFAULT_CONTENT_ID_STR);
		logger.debug("defaultContentIdStr: " + defaultContentIdStr);
		
		QaContent qaContent=qaService.loadQa(new Long(strToolContentID).longValue());
		logger.debug("qaContent: " + qaContent);
		
		if (qaContent == null)
		{
			logger.debug("using defaultContentIdStr: " + defaultContentIdStr);
			qaContent=qaService.loadQa(new Long(defaultContentIdStr).longValue());
		}
		logger.debug("final qaContent: " + qaContent);
		
		QaGeneralAuthoringDTO qaGeneralAuthoringDTO= new QaGeneralAuthoringDTO();
		logger.debug("qaGeneralAuthoringDTO: " + qaGeneralAuthoringDTO);
		qaGeneralAuthoringDTO.setContentFolderID(contentFolderID);

		String richTextTitle = request.getParameter(TITLE);
	    String richTextInstructions = request.getParameter(INSTRUCTIONS);
	
	    logger.debug("richTextTitle: " + richTextTitle);
	    logger.debug("richTextInstructions: " + richTextInstructions);
	    qaGeneralAuthoringDTO.setActivityTitle(richTextTitle);
	    qaAuthoringForm.setTitle(richTextTitle);
	    
		qaGeneralAuthoringDTO.setActivityInstructions(richTextInstructions);
			
		
		QaUtils.setFormProperties(request, qaService,  
	             qaAuthoringForm, qaGeneralAuthoringDTO, strToolContentID, defaultContentIdStr, activeModule, sessionMap, httpSessionID);
		
        List listQuestionContentDTO=(List)sessionMap.get(LIST_QUESTION_CONTENT_DTO_KEY);
        logger.debug("listQuestionContentDTO: " + listQuestionContentDTO);

		request.setAttribute(TOTAL_QUESTION_COUNT, new Integer(listQuestionContentDTO.size()));

		/*start monitoring code*/
		GeneralMonitoringDTO qaGeneralMonitoringDTO=new GeneralMonitoringDTO();
		qaGeneralMonitoringDTO.setDefineLaterInEditMode(new Boolean(false).toString());

		if (qaService.studentActivityOccurredGlobal(qaContent))
		{
			logger.debug("USER_EXCEPTION_NO_TOOL_SESSIONS is set to false");
			qaGeneralMonitoringDTO.setUserExceptionNoToolSessions(new Boolean(false).toString());
		}
		else
		{
			logger.debug("USER_EXCEPTION_NO_TOOL_SESSIONS is set to true");
			qaGeneralMonitoringDTO.setUserExceptionNoToolSessions(new Boolean(true).toString());
		}

		
		qaGeneralMonitoringDTO.setOnlineInstructions(qaContent.getOnlineInstructions());
		qaGeneralMonitoringDTO.setOfflineInstructions(qaContent.getOfflineInstructions());

		List attachmentList = qaService.retrieveQaUploadedFiles(qaContent);
        logger.debug("attachmentList: " + attachmentList);
        qaGeneralMonitoringDTO.setAttachmentList(attachmentList);
    	logger.debug("final qaGeneralMonitoringDTO: " + qaGeneralMonitoringDTO );

		qaGeneralMonitoringDTO.setDefineLaterInEditMode(new Boolean(true).toString());
    	logger.debug("final generalMonitoringDTO: " + qaGeneralMonitoringDTO );
		request.setAttribute(QA_GENERAL_MONITORING_DTO, qaGeneralMonitoringDTO);

		prepareReflectionData(request, qaContent, qaService, null, false);
		
		/*find out if there are any reflection entries, from here*/
		boolean notebookEntriesExist=MonitoringUtil.notebookEntriesExist(qaService, qaContent);
		logger.debug("notebookEntriesExist : " + notebookEntriesExist);
		
		if (notebookEntriesExist)
		{
		    request.setAttribute(NOTEBOOK_ENTRIES_EXIST, new Boolean(true).toString());
		}
		else
		{
		    request.setAttribute(NOTEBOOK_ENTRIES_EXIST, new Boolean(false).toString());
		}
		/* ... till here*/
		
		MonitoringUtil.buildQaStatsDTO(request,qaService, qaContent);
		
		logger.debug("fwd ing to newQuestionBox: ");
        return (mapping.findForward("newQuestionBox"));
    }
	
	
    
    public ActionForward newEditableQuestionBox(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) 
    throws IOException, ServletException 
    {
		logger.debug("dispathcing proxy newEditableQuestionBox");

		/*start authoring code*/
    	QaAuthoringForm qaAuthoringForm = (QaMonitoringForm) form;
		logger.debug("dispathcing  newEditableQuestionBox");
		
		IQaService qaService =QaServiceProxy.getQaService(getServlet().getServletContext());
		logger.debug("qaService: " + qaService);
		
		String httpSessionID=request.getParameter("httpSessionID");
		logger.debug("httpSessionID: " + httpSessionID);
		
		SessionMap sessionMap=(SessionMap)request.getSession().getAttribute(httpSessionID);
		logger.debug("sessionMap: " + sessionMap);

		
		String questionIndex=request.getParameter("questionIndex");
		logger.debug("questionIndex: " + questionIndex);
		
		qaAuthoringForm.setEditableQuestionIndex(questionIndex);
		
        List listQuestionContentDTO=(List)sessionMap.get(LIST_QUESTION_CONTENT_DTO_KEY);
        logger.debug("listQuestionContentDTO: " + listQuestionContentDTO);

        String editableQuestion="";
        String editableFeedback="";
	    Iterator listIterator=listQuestionContentDTO.iterator();
	    while (listIterator.hasNext())
	    {
	        QaQuestionContentDTO qaQuestionContentDTO= (QaQuestionContentDTO)listIterator.next();
	        logger.debug("qaQuestionContentDTO:" + qaQuestionContentDTO);
	        logger.debug("qaQuestionContentDTO question:" + qaQuestionContentDTO.getQuestion());
	        String question=qaQuestionContentDTO.getQuestion();
	        String displayOrder=qaQuestionContentDTO.getDisplayOrder();
	        
	        if ((displayOrder != null) && (!displayOrder.equals("")))
    		{
	            if (displayOrder.equals(questionIndex))
	            {
	                editableFeedback=qaQuestionContentDTO.getFeedback();
	                editableQuestion=qaQuestionContentDTO.getQuestion();
	                logger.debug("editableFeedback found :" + editableFeedback);
	                break;
	            }
	            
    		}
	    }
	    logger.debug("editableFeedback found :" + editableFeedback);
	    logger.debug("editableQuestion found :" + editableQuestion);

        
		String contentFolderID = WebUtil.readStrParam(request, AttributeNames.PARAM_CONTENT_FOLDER_ID);
		logger.debug("contentFolderID: " + contentFolderID);
		qaAuthoringForm.setContentFolderID(contentFolderID);


		String activeModule=request.getParameter(ACTIVE_MODULE);
		logger.debug("activeModule: " + activeModule);

		String strToolContentID=request.getParameter(AttributeNames.PARAM_TOOL_CONTENT_ID);
		logger.debug("strToolContentID: " + strToolContentID);
		
		String defaultContentIdStr=request.getParameter(DEFAULT_CONTENT_ID_STR);
		logger.debug("defaultContentIdStr: " + defaultContentIdStr);
		
		QaContent qaContent=qaService.loadQa(new Long(strToolContentID).longValue());
		logger.debug("qaContent: " + qaContent);
		
		if (qaContent == null)
		{
			logger.debug("using defaultContentIdStr: " + defaultContentIdStr);
			qaContent=qaService.loadQa(new Long(defaultContentIdStr).longValue());
		}
		logger.debug("final qaContent: " + qaContent);
		
		QaGeneralAuthoringDTO qaGeneralAuthoringDTO= new QaGeneralAuthoringDTO();
		logger.debug("qaGeneralAuthoringDTO: " + qaGeneralAuthoringDTO);
		qaGeneralAuthoringDTO.setContentFolderID(contentFolderID);

		
		String richTextTitle = request.getParameter(TITLE);
	    String richTextInstructions = request.getParameter(INSTRUCTIONS);
	
	    logger.debug("richTextTitle: " + richTextTitle);
	    logger.debug("richTextInstructions: " + richTextInstructions);
	    qaGeneralAuthoringDTO.setActivityTitle(richTextTitle);
	    qaAuthoringForm.setTitle(richTextTitle);
	    
		qaGeneralAuthoringDTO.setActivityInstructions(richTextInstructions);
			
		QaUtils.setFormProperties(request, qaService,  
	             qaAuthoringForm, qaGeneralAuthoringDTO, strToolContentID, defaultContentIdStr, activeModule, sessionMap, httpSessionID);
		
		qaGeneralAuthoringDTO.setEditableQuestionText(editableQuestion);
		qaGeneralAuthoringDTO.setEditableQuestionFeedback (editableFeedback);
		qaAuthoringForm.setFeedback(editableFeedback);
		
		logger.debug("qaGeneralAuthoringDTO now: " + qaGeneralAuthoringDTO);
		request.setAttribute(QA_GENERAL_AUTHORING_DTO, qaGeneralAuthoringDTO);
		
		request.setAttribute(TOTAL_QUESTION_COUNT, new Integer(listQuestionContentDTO.size()));
		
		/*start monitoring code*/
		GeneralMonitoringDTO qaGeneralMonitoringDTO=new GeneralMonitoringDTO();
		qaGeneralMonitoringDTO.setDefineLaterInEditMode(new Boolean(false).toString());

		if (qaService.studentActivityOccurredGlobal(qaContent))
		{
			logger.debug("USER_EXCEPTION_NO_TOOL_SESSIONS is set to false");
			qaGeneralMonitoringDTO.setUserExceptionNoToolSessions(new Boolean(false).toString());
		}
		else
		{
			logger.debug("USER_EXCEPTION_NO_TOOL_SESSIONS is set to true");
			qaGeneralMonitoringDTO.setUserExceptionNoToolSessions(new Boolean(true).toString());
		}

		qaGeneralMonitoringDTO.setOnlineInstructions(qaContent.getOnlineInstructions());
		qaGeneralMonitoringDTO.setOfflineInstructions(qaContent.getOfflineInstructions());
		
		List attachmentList = qaService.retrieveQaUploadedFiles(qaContent);
        logger.debug("attachmentList: " + attachmentList);
        qaGeneralMonitoringDTO.setAttachmentList(attachmentList);
    	logger.debug("final qaGeneralMonitoringDTO: " + qaGeneralMonitoringDTO );

		
		qaGeneralMonitoringDTO.setDefineLaterInEditMode(new Boolean(true).toString());
    	logger.debug("final generalMonitoringDTO: " + qaGeneralMonitoringDTO );
		request.setAttribute(QA_GENERAL_MONITORING_DTO, qaGeneralMonitoringDTO);

		prepareReflectionData(request, qaContent, qaService, null, false);
		
		/*find out if there are any reflection entries, from here*/
		boolean notebookEntriesExist=MonitoringUtil.notebookEntriesExist(qaService, qaContent);
		logger.debug("notebookEntriesExist : " + notebookEntriesExist);
		
		if (notebookEntriesExist)
		{
		    request.setAttribute(NOTEBOOK_ENTRIES_EXIST, new Boolean(true).toString());
		}
		else
		{
		    request.setAttribute(NOTEBOOK_ENTRIES_EXIST, new Boolean(false).toString());
		}
		/* ... till here*/
		
		MonitoringUtil.buildQaStatsDTO(request,qaService, qaContent);
		
		logger.debug("fwd ing to editQuestionBox: ");
        return (mapping.findForward("editQuestionBox"));
    }
    
    
    
    
    public ActionForward removeQuestion(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) 
    throws IOException, ServletException {
    	logger.debug("dispathcing proxy removeQuestion");

		/*start authoring code*/
    	QaAuthoringForm qaAuthoringForm = (QaMonitoringForm) form;
		logger.debug("dispathcing  removeQuestion");
		
		IQaService qaService =QaServiceProxy.getQaService(getServlet().getServletContext());
		logger.debug("qaService: " + qaService);
	
		String httpSessionID=request.getParameter("httpSessionID");
		logger.debug("httpSessionID: " + httpSessionID);
		
		SessionMap sessionMap=(SessionMap)request.getSession().getAttribute(httpSessionID);
		logger.debug("sessionMap: " + sessionMap);
		
		String questionIndex=request.getParameter("questionIndex");
		logger.debug("questionIndex: " + questionIndex);
		
	    
	    List listQuestionContentDTO=(List)sessionMap.get(LIST_QUESTION_CONTENT_DTO_KEY);
	    logger.debug("listQuestionContentDTO: " + listQuestionContentDTO);
	
	    QaQuestionContentDTO qaQuestionContentDTO= null;
	    Iterator listIterator=listQuestionContentDTO.iterator();
	    while (listIterator.hasNext())
	    {
	        qaQuestionContentDTO= (QaQuestionContentDTO)listIterator.next();
	        logger.debug("qaQuestionContentDTO:" + qaQuestionContentDTO);
	        logger.debug("qaQuestionContentDTO question:" + qaQuestionContentDTO.getQuestion());
	        
	        String question=qaQuestionContentDTO.getQuestion();
	        String displayOrder=qaQuestionContentDTO.getDisplayOrder();
	        logger.debug("displayOrder:" + displayOrder);
	        
	        if ((displayOrder != null) && (!displayOrder.equals("")))
			{
	            if (displayOrder.equals(questionIndex))
	            {
	                break;
	            }
	            
			}
	    }
	    
	    logger.debug("qaQuestionContentDTO found:" + qaQuestionContentDTO);
	    qaQuestionContentDTO.setQuestion("");
	    logger.debug("listQuestionContentDTO after remove:" + listQuestionContentDTO);
	  
	    
	    listQuestionContentDTO=AuthoringUtil.reorderListQuestionContentDTO(listQuestionContentDTO, questionIndex );
	    logger.debug("listQuestionContentDTO reordered:" + listQuestionContentDTO);
	    
	
	    sessionMap.put(LIST_QUESTION_CONTENT_DTO_KEY, listQuestionContentDTO);
	    
		
		String contentFolderID = WebUtil.readStrParam(request, AttributeNames.PARAM_CONTENT_FOLDER_ID);
		logger.debug("contentFolderID: " + contentFolderID);
		qaAuthoringForm.setContentFolderID(contentFolderID);
	
		
		String activeModule=request.getParameter(ACTIVE_MODULE);
		logger.debug("activeModule: " + activeModule);
	
		String richTextTitle = request.getParameter(TITLE);
		logger.debug("richTextTitle: " + richTextTitle);
	    
		String richTextInstructions = request.getParameter(INSTRUCTIONS);
	    logger.debug("richTextInstructions: " + richTextInstructions);
	    
		
	    sessionMap.put(ACTIVITY_TITLE_KEY, richTextTitle);
	    sessionMap.put(ACTIVITY_INSTRUCTIONS_KEY, richTextInstructions);
	
	
		String strToolContentID=request.getParameter(AttributeNames.PARAM_TOOL_CONTENT_ID);
		logger.debug("strToolContentID: " + strToolContentID);
		
		String defaultContentIdStr=request.getParameter(DEFAULT_CONTENT_ID_STR);
		logger.debug("defaultContentIdStr: " + defaultContentIdStr);
		
		QaContent qaContent=qaService.loadQa(new Long(strToolContentID).longValue());
		logger.debug("qaContent: " + qaContent);
		
		if (qaContent == null)
		{
			logger.debug("using defaultContentIdStr: " + defaultContentIdStr);
			qaContent=qaService.loadQa(new Long(defaultContentIdStr).longValue());
		}
		logger.debug("final qaContent: " + qaContent);
	
		QaGeneralAuthoringDTO qaGeneralAuthoringDTO= new QaGeneralAuthoringDTO();
		logger.debug("qaGeneralAuthoringDTO: " + qaGeneralAuthoringDTO);
		qaGeneralAuthoringDTO.setContentFolderID(contentFolderID);
		
	    qaGeneralAuthoringDTO.setActivityTitle(richTextTitle);
	    qaAuthoringForm.setTitle(richTextTitle);
	    
		qaGeneralAuthoringDTO.setActivityInstructions(richTextInstructions);
	
	    
		AuthoringUtil authoringUtil= new AuthoringUtil();
	    
	    qaGeneralAuthoringDTO.setEditActivityEditMode(new Boolean(true).toString());
	    
	    request.getSession().setAttribute(httpSessionID, sessionMap);
	
		QaUtils.setFormProperties(request, qaService,  
	             qaAuthoringForm, qaGeneralAuthoringDTO, strToolContentID, defaultContentIdStr, activeModule, sessionMap, httpSessionID);
	
		
		qaGeneralAuthoringDTO.setToolContentID(strToolContentID);
		qaGeneralAuthoringDTO.setHttpSessionID(httpSessionID);
		qaGeneralAuthoringDTO.setActiveModule(activeModule);
		qaGeneralAuthoringDTO.setDefaultContentIdStr(defaultContentIdStr);
		qaAuthoringForm.setToolContentID(strToolContentID);
		qaAuthoringForm.setHttpSessionID(httpSessionID);
		qaAuthoringForm.setActiveModule(activeModule);
		qaAuthoringForm.setDefaultContentIdStr(defaultContentIdStr);
		qaAuthoringForm.setCurrentTab("3");
	
	    request.setAttribute(LIST_QUESTION_CONTENT_DTO,listQuestionContentDTO);
		logger.debug("qaQuestionContentDTO now: " + qaQuestionContentDTO);
	    logger.debug("listQuestionContentDTO now: " + listQuestionContentDTO);
	
		
		logger.debug("before saving final qaGeneralAuthoringDTO: " + qaGeneralAuthoringDTO);
		request.setAttribute(QA_GENERAL_AUTHORING_DTO, qaGeneralAuthoringDTO);
	
		request.setAttribute(TOTAL_QUESTION_COUNT, new Integer(listQuestionContentDTO.size()));
		
		/*start monitoring code*/
		GeneralMonitoringDTO qaGeneralMonitoringDTO=new GeneralMonitoringDTO();

		if (qaService.studentActivityOccurredGlobal(qaContent))
		{
			logger.debug("USER_EXCEPTION_NO_TOOL_SESSIONS is set to false");
			qaGeneralMonitoringDTO.setUserExceptionNoToolSessions(new Boolean(false).toString());
		}
		else
		{
			logger.debug("USER_EXCEPTION_NO_TOOL_SESSIONS is set to true");
			qaGeneralMonitoringDTO.setUserExceptionNoToolSessions(new Boolean(true).toString());
		}

		qaGeneralMonitoringDTO.setOnlineInstructions(qaContent.getOnlineInstructions());
		qaGeneralMonitoringDTO.setOfflineInstructions(qaContent.getOfflineInstructions());

		List attachmentList = qaService.retrieveQaUploadedFiles(qaContent);
        logger.debug("attachmentList: " + attachmentList);
        qaGeneralMonitoringDTO.setAttachmentList(attachmentList);
    	logger.debug("final qaGeneralMonitoringDTO: " + qaGeneralMonitoringDTO );

		
		qaGeneralMonitoringDTO.setDefineLaterInEditMode(new Boolean(true).toString());
    	logger.debug("final generalMonitoringDTO: " + qaGeneralMonitoringDTO );
		request.setAttribute(QA_GENERAL_MONITORING_DTO, qaGeneralMonitoringDTO);

		prepareReflectionData(request, qaContent, qaService, null, false);

		/*find out if there are any reflection entries, from here*/
		boolean notebookEntriesExist=MonitoringUtil.notebookEntriesExist(qaService, qaContent);
		logger.debug("notebookEntriesExist : " + notebookEntriesExist);
		
		if (notebookEntriesExist)
		{
		    request.setAttribute(NOTEBOOK_ENTRIES_EXIST, new Boolean(true).toString());
		}
		else
		{
		    request.setAttribute(NOTEBOOK_ENTRIES_EXIST, new Boolean(false).toString());
		}
		/* ... till here*/
		
		MonitoringUtil.buildQaStatsDTO(request,qaService, qaContent);
		
        return mapping.findForward(LOAD_MONITORING);
    }

    
    
    
    public ActionForward moveQuestionDown(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) 
    throws IOException, ServletException {
    	logger.debug("dispathcing proxy moveQuestionDown");

		/*start authoring code*/
    	QaAuthoringForm qaAuthoringForm = (QaMonitoringForm) form;
		logger.debug("dispathcing  moveQuestionDown");
		
		IQaService qaService =QaServiceProxy.getQaService(getServlet().getServletContext());
		logger.debug("qaService: " + qaService);
	
		String httpSessionID=request.getParameter("httpSessionID");
		logger.debug("httpSessionID: " + httpSessionID);
		
		SessionMap sessionMap=(SessionMap)request.getSession().getAttribute(httpSessionID);
		logger.debug("sessionMap: " + sessionMap);
		
		String questionIndex=request.getParameter("questionIndex");
		logger.debug("questionIndex: " + questionIndex);
		
	    
	    List listQuestionContentDTO=(List)sessionMap.get(LIST_QUESTION_CONTENT_DTO_KEY);
	    logger.debug("listQuestionContentDTO: " + listQuestionContentDTO);
	    
	    listQuestionContentDTO=AuthoringUtil.swapNodes(listQuestionContentDTO, questionIndex, "down");
	    logger.debug("listQuestionContentDTO after swap: " + listQuestionContentDTO);
	    
	    listQuestionContentDTO=AuthoringUtil.reorderSimpleListQuestionContentDTO(listQuestionContentDTO);
	    logger.debug("listQuestionContentDTO after reordersimple: " + listQuestionContentDTO);

	    
	    sessionMap.put(LIST_QUESTION_CONTENT_DTO_KEY, listQuestionContentDTO);
	    
	    
		String contentFolderID = WebUtil.readStrParam(request, AttributeNames.PARAM_CONTENT_FOLDER_ID);
		logger.debug("contentFolderID: " + contentFolderID);
		qaAuthoringForm.setContentFolderID(contentFolderID);
	
		
		String activeModule=request.getParameter(ACTIVE_MODULE);
		logger.debug("activeModule: " + activeModule);
	
		String richTextTitle = request.getParameter(TITLE);
		logger.debug("richTextTitle: " + richTextTitle);
	    
		String richTextInstructions = request.getParameter(INSTRUCTIONS);
	    logger.debug("richTextInstructions: " + richTextInstructions);
	    
		
	    sessionMap.put(ACTIVITY_TITLE_KEY, richTextTitle);
	    sessionMap.put(ACTIVITY_INSTRUCTIONS_KEY, richTextInstructions);
	
	
		String strToolContentID=request.getParameter(AttributeNames.PARAM_TOOL_CONTENT_ID);
		logger.debug("strToolContentID: " + strToolContentID);
		
		String defaultContentIdStr=request.getParameter(DEFAULT_CONTENT_ID_STR);
		logger.debug("defaultContentIdStr: " + defaultContentIdStr);
		
		QaContent qaContent=qaService.loadQa(new Long(strToolContentID).longValue());
		logger.debug("qaContent: " + qaContent);
		
		if (qaContent == null)
		{
			logger.debug("using defaultContentIdStr: " + defaultContentIdStr);
			qaContent=qaService.loadQa(new Long(defaultContentIdStr).longValue());
		}
		logger.debug("final qaContent: " + qaContent);
	
		QaGeneralAuthoringDTO qaGeneralAuthoringDTO= new QaGeneralAuthoringDTO();
		logger.debug("qaGeneralAuthoringDTO: " + qaGeneralAuthoringDTO);
		qaGeneralAuthoringDTO.setContentFolderID(contentFolderID);
		
	    qaGeneralAuthoringDTO.setActivityTitle(richTextTitle);
	    qaAuthoringForm.setTitle(richTextTitle);
	    
		qaGeneralAuthoringDTO.setActivityInstructions(richTextInstructions);
	
		AuthoringUtil authoringUtil= new AuthoringUtil();
	    
	    qaGeneralAuthoringDTO.setEditActivityEditMode(new Boolean(true).toString());
	    request.getSession().setAttribute(httpSessionID, sessionMap);
	
		QaUtils.setFormProperties(request, qaService,  
	             qaAuthoringForm, qaGeneralAuthoringDTO, strToolContentID, defaultContentIdStr, activeModule, sessionMap, httpSessionID);
	
		
		qaGeneralAuthoringDTO.setToolContentID(strToolContentID);
		qaGeneralAuthoringDTO.setHttpSessionID(httpSessionID);
		qaGeneralAuthoringDTO.setActiveModule(activeModule);
		qaGeneralAuthoringDTO.setDefaultContentIdStr(defaultContentIdStr);
		qaAuthoringForm.setToolContentID(strToolContentID);
		qaAuthoringForm.setHttpSessionID(httpSessionID);
		qaAuthoringForm.setActiveModule(activeModule);
		qaAuthoringForm.setDefaultContentIdStr(defaultContentIdStr);
		qaAuthoringForm.setCurrentTab("3");
	
	    request.setAttribute(LIST_QUESTION_CONTENT_DTO,listQuestionContentDTO);
		logger.debug("listQuestionContentDTO now: " + listQuestionContentDTO);
	
		
		logger.debug("before saving final qaGeneralAuthoringDTO: " + qaGeneralAuthoringDTO);
		request.setAttribute(QA_GENERAL_AUTHORING_DTO, qaGeneralAuthoringDTO);
	
		request.setAttribute(TOTAL_QUESTION_COUNT, new Integer(listQuestionContentDTO.size()));
		
		/*start monitoring code*/
		GeneralMonitoringDTO qaGeneralMonitoringDTO=new GeneralMonitoringDTO();

		if (qaService.studentActivityOccurredGlobal(qaContent))
		{
			logger.debug("USER_EXCEPTION_NO_TOOL_SESSIONS is set to false");
			qaGeneralMonitoringDTO.setUserExceptionNoToolSessions(new Boolean(false).toString());
		}
		else
		{
			logger.debug("USER_EXCEPTION_NO_TOOL_SESSIONS is set to true");
			qaGeneralMonitoringDTO.setUserExceptionNoToolSessions(new Boolean(true).toString());
		}

		qaGeneralMonitoringDTO.setOnlineInstructions(qaContent.getOnlineInstructions());
		qaGeneralMonitoringDTO.setOfflineInstructions(qaContent.getOfflineInstructions());

		List attachmentList = qaService.retrieveQaUploadedFiles(qaContent);
        logger.debug("attachmentList: " + attachmentList);
        qaGeneralMonitoringDTO.setAttachmentList(attachmentList);
    	logger.debug("final qaGeneralMonitoringDTO: " + qaGeneralMonitoringDTO );

		
		qaGeneralMonitoringDTO.setDefineLaterInEditMode(new Boolean(true).toString());
    	logger.debug("final generalMonitoringDTO: " + qaGeneralMonitoringDTO );
		request.setAttribute(QA_GENERAL_MONITORING_DTO, qaGeneralMonitoringDTO);

		prepareReflectionData(request, qaContent, qaService, null, false);

		/*find out if there are any reflection entries, from here*/
		boolean notebookEntriesExist=MonitoringUtil.notebookEntriesExist(qaService, qaContent);
		logger.debug("notebookEntriesExist : " + notebookEntriesExist);
		
		if (notebookEntriesExist)
		{
		    request.setAttribute(NOTEBOOK_ENTRIES_EXIST, new Boolean(true).toString());
		}
		else
		{
		    request.setAttribute(NOTEBOOK_ENTRIES_EXIST, new Boolean(false).toString());
		}
		/* ... till here*/
		
		MonitoringUtil.buildQaStatsDTO(request,qaService, qaContent);
		
        return mapping.findForward(LOAD_MONITORING);

    }
    
    
    
    public ActionForward moveQuestionUp(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) 
    throws IOException, ServletException {
    	logger.debug("dispathcing proxy moveQuestionUp");

		/*start authoring code*/
    	QaAuthoringForm qaAuthoringForm = (QaMonitoringForm) form;
		logger.debug("dispathcing  moveQuestionDown");
		
		IQaService qaService =QaServiceProxy.getQaService(getServlet().getServletContext());
		logger.debug("qaService: " + qaService);
	
		String httpSessionID=request.getParameter("httpSessionID");
		logger.debug("httpSessionID: " + httpSessionID);
		
		SessionMap sessionMap=(SessionMap)request.getSession().getAttribute(httpSessionID);
		logger.debug("sessionMap: " + sessionMap);
		
		String questionIndex=request.getParameter("questionIndex");
		logger.debug("questionIndex: " + questionIndex);
		
	    
	    List listQuestionContentDTO=(List)sessionMap.get(LIST_QUESTION_CONTENT_DTO_KEY);
	    logger.debug("listQuestionContentDTO: " + listQuestionContentDTO);
	    
	    listQuestionContentDTO=AuthoringUtil.swapNodes(listQuestionContentDTO, questionIndex, "up");
	    logger.debug("listQuestionContentDTO after swap: " + listQuestionContentDTO);	    
	    
	    listQuestionContentDTO=AuthoringUtil.reorderSimpleListQuestionContentDTO(listQuestionContentDTO);
	    logger.debug("listQuestionContentDTO after reordersimple: " + listQuestionContentDTO);
		    
	    sessionMap.put(LIST_QUESTION_CONTENT_DTO_KEY, listQuestionContentDTO);
	    
		String contentFolderID = WebUtil.readStrParam(request, AttributeNames.PARAM_CONTENT_FOLDER_ID);
		logger.debug("contentFolderID: " + contentFolderID);
		qaAuthoringForm.setContentFolderID(contentFolderID);
	
		
		String activeModule=request.getParameter(ACTIVE_MODULE);
		logger.debug("activeModule: " + activeModule);
	
		String richTextTitle = request.getParameter(TITLE);
		logger.debug("richTextTitle: " + richTextTitle);
	    
		String richTextInstructions = request.getParameter(INSTRUCTIONS);
	    logger.debug("richTextInstructions: " + richTextInstructions);
	    
		
	    sessionMap.put(ACTIVITY_TITLE_KEY, richTextTitle);
	    sessionMap.put(ACTIVITY_INSTRUCTIONS_KEY, richTextInstructions);
	
	
		String strToolContentID=request.getParameter(AttributeNames.PARAM_TOOL_CONTENT_ID);
		logger.debug("strToolContentID: " + strToolContentID);
		
		String defaultContentIdStr=request.getParameter(DEFAULT_CONTENT_ID_STR);
		logger.debug("defaultContentIdStr: " + defaultContentIdStr);
		
		QaContent qaContent=qaService.loadQa(new Long(strToolContentID).longValue());
		logger.debug("qaContent: " + qaContent);
		
		if (qaContent == null)
		{
			logger.debug("using defaultContentIdStr: " + defaultContentIdStr);
			qaContent=qaService.loadQa(new Long(defaultContentIdStr).longValue());
		}
		logger.debug("final qaContent: " + qaContent);
	
		QaGeneralAuthoringDTO qaGeneralAuthoringDTO= new QaGeneralAuthoringDTO();
		logger.debug("qaGeneralAuthoringDTO: " + qaGeneralAuthoringDTO);
		qaGeneralAuthoringDTO.setContentFolderID(contentFolderID);
		
	    qaGeneralAuthoringDTO.setActivityTitle(richTextTitle);
	    qaAuthoringForm.setTitle(richTextTitle);
	    
		qaGeneralAuthoringDTO.setActivityInstructions(richTextInstructions);
	
		AuthoringUtil authoringUtil= new AuthoringUtil();
	    
	    qaGeneralAuthoringDTO.setEditActivityEditMode(new Boolean(true).toString());
	    
	    request.getSession().setAttribute(httpSessionID, sessionMap);
	
		QaUtils.setFormProperties(request, qaService,  
	             qaAuthoringForm, qaGeneralAuthoringDTO, strToolContentID, defaultContentIdStr, activeModule, sessionMap, httpSessionID);
	
		
		qaGeneralAuthoringDTO.setToolContentID(strToolContentID);
		qaGeneralAuthoringDTO.setHttpSessionID(httpSessionID);
		qaGeneralAuthoringDTO.setActiveModule(activeModule);
		qaGeneralAuthoringDTO.setDefaultContentIdStr(defaultContentIdStr);
		qaAuthoringForm.setToolContentID(strToolContentID);
		qaAuthoringForm.setHttpSessionID(httpSessionID);
		qaAuthoringForm.setActiveModule(activeModule);
		qaAuthoringForm.setDefaultContentIdStr(defaultContentIdStr);
		qaAuthoringForm.setCurrentTab("3");
		
	
	    request.setAttribute(LIST_QUESTION_CONTENT_DTO,listQuestionContentDTO);
		logger.debug("listQuestionContentDTO now: " + listQuestionContentDTO);
	
		
		logger.debug("before saving final qaGeneralAuthoringDTO: " + qaGeneralAuthoringDTO);
		request.setAttribute(QA_GENERAL_AUTHORING_DTO, qaGeneralAuthoringDTO);
	
		request.setAttribute(TOTAL_QUESTION_COUNT, new Integer(listQuestionContentDTO.size()));
		
		/*start monitoring code*/
		GeneralMonitoringDTO qaGeneralMonitoringDTO=new GeneralMonitoringDTO();

		if (qaService.studentActivityOccurredGlobal(qaContent))
		{
			logger.debug("USER_EXCEPTION_NO_TOOL_SESSIONS is set to false");
			qaGeneralMonitoringDTO.setUserExceptionNoToolSessions(new Boolean(false).toString());
		}
		else
		{
			logger.debug("USER_EXCEPTION_NO_TOOL_SESSIONS is set to true");
			qaGeneralMonitoringDTO.setUserExceptionNoToolSessions(new Boolean(true).toString());
		}

		qaGeneralMonitoringDTO.setOnlineInstructions(qaContent.getOnlineInstructions());
		qaGeneralMonitoringDTO.setOfflineInstructions(qaContent.getOfflineInstructions());

		List attachmentList = qaService.retrieveQaUploadedFiles(qaContent);
        logger.debug("attachmentList: " + attachmentList);
        qaGeneralMonitoringDTO.setAttachmentList(attachmentList);
    	logger.debug("final qaGeneralMonitoringDTO: " + qaGeneralMonitoringDTO );

		
		qaGeneralMonitoringDTO.setDefineLaterInEditMode(new Boolean(true).toString());
    	logger.debug("final generalMonitoringDTO: " + qaGeneralMonitoringDTO );
		request.setAttribute(QA_GENERAL_MONITORING_DTO, qaGeneralMonitoringDTO);

		prepareReflectionData(request, qaContent, qaService, null, false);
		
		/*find out if there are any reflection entries, from here*/
		boolean notebookEntriesExist=MonitoringUtil.notebookEntriesExist(qaService, qaContent);
		logger.debug("notebookEntriesExist : " + notebookEntriesExist);
		
		if (notebookEntriesExist)
		{
		    request.setAttribute(NOTEBOOK_ENTRIES_EXIST, new Boolean(true).toString());
		}
		else
		{
		    request.setAttribute(NOTEBOOK_ENTRIES_EXIST, new Boolean(false).toString());
		}
		/* ... till here*/
		
		MonitoringUtil.buildQaStatsDTO(request,qaService, qaContent);
		
        return mapping.findForward(LOAD_MONITORING);
    }
	
}
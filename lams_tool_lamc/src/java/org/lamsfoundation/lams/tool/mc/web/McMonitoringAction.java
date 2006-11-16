/***************************************************************************
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
 * ***********************************************************************/
/* $$Id$$ */
package org.lamsfoundation.lams.tool.mc.web;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

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
import org.lamsfoundation.lams.tool.mc.EditActivityDTO;
import org.lamsfoundation.lams.tool.mc.ExportPortfolioDTO;
import org.lamsfoundation.lams.tool.mc.McAppConstants;
import org.lamsfoundation.lams.tool.mc.McApplicationException;
import org.lamsfoundation.lams.tool.mc.McCandidateAnswersDTO;
import org.lamsfoundation.lams.tool.mc.McComparator;
import org.lamsfoundation.lams.tool.mc.McGeneralAuthoringDTO;
import org.lamsfoundation.lams.tool.mc.McGeneralLearnerFlowDTO;
import org.lamsfoundation.lams.tool.mc.McGeneralMonitoringDTO;
import org.lamsfoundation.lams.tool.mc.McQuestionContentDTO;
import org.lamsfoundation.lams.tool.mc.McUtils;
import org.lamsfoundation.lams.tool.mc.ReflectionDTO;
import org.lamsfoundation.lams.tool.mc.pojos.McContent;
import org.lamsfoundation.lams.tool.mc.pojos.McQueContent;
import org.lamsfoundation.lams.tool.mc.pojos.McQueUsr;
import org.lamsfoundation.lams.tool.mc.pojos.McSession;
import org.lamsfoundation.lams.tool.mc.service.IMcService;
import org.lamsfoundation.lams.tool.mc.service.McServiceProxy;
import org.lamsfoundation.lams.util.WebUtil;
import org.lamsfoundation.lams.web.action.LamsDispatchAction;
import org.lamsfoundation.lams.web.util.AttributeNames;
import org.lamsfoundation.lams.web.util.SessionMap;

/**
 * * @author Ozgur Demirtas
 * 
 * <p>Action class that controls the logic of tool behavior. </p>
 * 
 * <p>Note that Struts action class only has the responsibility to navigate 
 * page flow. All database operation should go to service layer and data 
 * transformation from domain model to struts form bean should go to form 
 * bean class. This ensure clean and maintainable code.
 * </p>
 * 
 * <code>SystemException</code> is thrown whenever an known error condition is
 * identified. No system exception error handling code should appear in the 
 * Struts action class as all of them are handled in 
 * <code>CustomStrutsExceptionHandler<code>.
 * 
   <!--Monitoring Main Action: interacts with the Monitoring module user -->
   <action 	path="/monitoring" 
   			type="org.lamsfoundation.lams.tool.mc.web.McMonitoringAction" 
   			name="McMonitoringForm" 
	      	scope="request"
   			input="/monitoring/MonitoringMaincontent.jsp"
      		parameter="dispatch"
      		unknown="false"
      		validate="false"> 
	      
	  	<forward
			name="loadMonitoring"
			path="/monitoring/MonitoringMaincontent.jsp"
			redirect="false"
	  	/>

	  	<forward
		    name="loadMonitoringEditActivity"
		    path="/monitoring/MonitoringMaincontent.jsp"
		    redirect="false"
	  	/>
	
	  	<forward
			name="refreshMonitoring"
			path="/monitoring/MonitoringMaincontent.jsp"
			redirect="false"
	  	/>


        <forward
          name="learnerNotebook"
          path="/monitoring/LearnerNotebook.jsp"
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
		    name="errorList"
	        path="/McErrorBox.jsp"
		    redirect="false"
	  	/>
	</action>  
  
 * 
*/
public class McMonitoringAction extends LamsDispatchAction implements McAppConstants
{
	static Logger logger = Logger.getLogger(McMonitoringAction.class.getName());
	
	 /** 
     * <p>Default struts dispatch method.</p> 
     * 
     * <p>It is assuming that progress engine should pass in the tool access
     * mode and the tool session id as http parameters.</p>
     * 
     * @param mapping An ActionMapping class that will be used by the Action class to tell
     * the ActionServlet where to send the end-user.
     *
     * @param form The ActionForm class that will contain any data submitted
     * by the end-user via a form.
     * @param request A standard Servlet HttpServletRequest class.
     * @param response A standard Servlet HttpServletResponse class.
     * @return An ActionForward class that will be returned to the ActionServlet indicating where
     *         the user is to go next.
     * @throws IOException
     * @throws ServletException
     * @throws McApplicationException the known runtime exception 
     * 
	 * unspecified(ActionMapping mapping,
                               ActionForm form,
                               HttpServletRequest request,
                               HttpServletResponse response) throws IOException,
                                                            ServletException
                                                            
	 * main content/question content management and workflow logic
	 * 
	*/
    public ActionForward unspecified(ActionMapping mapping,
                               ActionForm form,
                               HttpServletRequest request,
                               HttpServletResponse response) throws IOException,
                                                            ServletException
    {
    	logger.debug("dispatching unspecified...");
	 	return null;
    }

   
    /**
     * submitSession
     * 
     * enables getting monitoring data for different sessions
     * 
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @param mcService
     * @param mcGeneralMonitoringDTO
     * @return
     * @throws IOException
     * @throws ServletException
     */
    public ActionForward submitSession(ActionMapping mapping,
            ActionForm form,
            HttpServletRequest request,
            HttpServletResponse response,
            IMcService mcService,
            McGeneralMonitoringDTO mcGeneralMonitoringDTO
            ) throws IOException,
                                         ServletException
	{
        logger.debug("calling submitSession...mcGeneralMonitoringDTO:" + mcGeneralMonitoringDTO);
        commonSubmitSessionCode(form, request, mcService,mcGeneralMonitoringDTO);
        logger.debug("post commonSubmitSessionCode: " +mcGeneralMonitoringDTO);
    	return (mapping.findForward(LOAD_MONITORING));
	}
    

    /**
     * commonSubmitSessionCode(ActionForm form, HttpServletRequest request,IMcService mcService,
            McGeneralMonitoringDTO mcGeneralMonitoringDTO)
     * 
     * @param form
     * @param request
     * @param mcService
     * @param mcGeneralMonitoringDTO
     * @throws IOException
     * @throws ServletException
     */
    protected void commonSubmitSessionCode(ActionForm form, HttpServletRequest request,IMcService mcService,
            McGeneralMonitoringDTO mcGeneralMonitoringDTO) throws IOException, ServletException
    {
    	logger.debug("starting  commonSubmitSessionCode...mcGeneralMonitoringDTO:" + mcGeneralMonitoringDTO);
    	
		logger.debug("mcService:" + mcService);
		McMonitoringForm mcMonitoringForm = (McMonitoringForm) form;
		
		repopulateRequestParameters(request, mcMonitoringForm, mcGeneralMonitoringDTO);
		logger.debug("done repopulateRequestParameters");

	    String currentMonitoredToolSession=request.getParameter("monitoredToolSessionId");
	    logger.debug("currentMonitoredToolSession: " + currentMonitoredToolSession);
	    
	    if (currentMonitoredToolSession == null)
	    {
	        logger.debug("default to All");
	        currentMonitoredToolSession="All";
	    }
	    
	    
	    String toolContentID =mcMonitoringForm.getToolContentID();
	    logger.debug("toolContentID: " + toolContentID);
	    
	    McContent mcContent=mcService.retrieveMc(new Long(toolContentID));
		logger.debug("existing mcContent:" + mcContent);


		if (currentMonitoredToolSession.equals("All"))
	    {
		    List listMcAllSessionsDTO = new LinkedList();
		    logger.debug("generate DTO for All sessions: ");
		    MonitoringUtil.setupAllSessionsData(request, mcContent,mcService);

		    mcGeneralMonitoringDTO.setSelectionCase(new Long(2));
		    request.setAttribute(SELECTION_CASE, new Long(2));
	    }
	    else
	    {
		    McSession mcSession=mcService.retrieveMcSession(new Long(currentMonitoredToolSession));
    		logger.debug("mcSession uid:" + mcSession.getUid());
		    
		    MonitoringUtil.setupAllSessionsData(request, mcContent,mcService);
		    
		    logger.debug("session_name: " + mcSession.getSession_name());
		    mcGeneralMonitoringDTO.setGroupName(mcSession.getSession_name());
		    mcGeneralMonitoringDTO.setSelectionCase(new Long(1));
		    request.setAttribute(SELECTION_CASE, new Long(1));
	    }
		logger.debug("SELECTION_CASE: " + mcGeneralMonitoringDTO.getSelectionCase());
		
	    logger.debug("SELECTION_CASE: " + request.getAttribute(SELECTION_CASE));
	    request.setAttribute(CURRENT_MONITORED_TOOL_SESSION, currentMonitoredToolSession);

	    
		mcGeneralMonitoringDTO.setCurrentMonitoredToolSession(currentMonitoredToolSession);
	    mcGeneralMonitoringDTO.setSbmtSuccess(new Boolean(false).toString());
	    mcGeneralMonitoringDTO.setRequestLearningReport(new Boolean(false).toString());
	    
		
		Map summaryToolSessions=MonitoringUtil.populateToolSessions(request, mcContent, mcService);
		logger.debug("summaryToolSessions: " + summaryToolSessions);
		mcGeneralMonitoringDTO.setSummaryToolSessions(summaryToolSessions);

		Map summaryToolSessionsId=MonitoringUtil.populateToolSessionsId(request, mcContent, mcService);
		logger.debug("summaryToolSessionsId: " + summaryToolSessionsId);
		mcGeneralMonitoringDTO.setSummaryToolSessionsId(summaryToolSessionsId);

		logger.debug("calling initStatsContent.");
		initStatsContent(toolContentID, request, mcService, mcGeneralMonitoringDTO);
		logger.debug("post initStatsContent, mcGeneralMonitoringDTO: " + mcGeneralMonitoringDTO);

		
		/*setting editable screen properties*/
		McGeneralAuthoringDTO mcGeneralAuthoringDTO= new McGeneralAuthoringDTO();
		mcGeneralAuthoringDTO.setActivityTitle(mcContent.getTitle());
		mcGeneralAuthoringDTO.setActivityInstructions(mcContent.getInstructions());

		Map mapOptionsContent= new TreeMap(new McComparator());
		Iterator queIterator=mcContent.getMcQueContents().iterator();
		Long mapIndex=new Long(1);
		logger.debug("mapOptionsContent: " + mapOptionsContent);
		while (queIterator.hasNext())
		{
			McQueContent mcQueContent=(McQueContent) queIterator.next();
			if (mcQueContent != null)
			{
				logger.debug("question: " + mcQueContent.getQuestion());
				mapOptionsContent.put(mapIndex.toString(),mcQueContent.getQuestion());
	    		
	    		mapIndex=new Long(mapIndex.longValue()+1);
			}
		}
		
		logger.debug("mapOptionsContent: " + mapOptionsContent);
	    int maxIndex=mapOptionsContent.size();
	    logger.debug("maxIndex: " + maxIndex);
		
		boolean isContentInUse=McUtils.isContentInUse(mcContent);
		logger.debug("isContentInUse:" + isContentInUse);
		mcGeneralMonitoringDTO.setIsMonitoredContentInUse(new Boolean(false).toString());
		if (isContentInUse == true)
		{
			logger.debug("monitoring url does not allow editActivity since the content is in use.");
			mcGeneralMonitoringDTO.setIsMonitoredContentInUse(new Boolean(true).toString());
		}

		logger.debug("mcGeneralAuthoringDTO: " + mcGeneralAuthoringDTO);
		request.setAttribute(MC_GENERAL_AUTHORING_DTO, mcGeneralAuthoringDTO);
		
		prepareReflectionData(request, mcContent, mcService, null, false, "All");

		if (mcService.studentActivityOccurredGlobal(mcContent))
		{
			logger.debug("USER_EXCEPTION_NO_TOOL_SESSIONS is set to false");
			mcGeneralMonitoringDTO.setUserExceptionNoToolSessions(new Boolean(false).toString());
		}
		else
		{
			logger.debug("USER_EXCEPTION_NO_TOOL_SESSIONS is set to true");
			mcGeneralMonitoringDTO.setUserExceptionNoToolSessions(new Boolean(true).toString());
		}
		
		/**getting instructions screen content from here... */
		mcGeneralMonitoringDTO.setOnlineInstructions(mcContent.getOnlineInstructions());
		mcGeneralMonitoringDTO.setOfflineInstructions(mcContent.getOfflineInstructions());
		
        List attachmentList = mcService.retrieveMcUploadedFiles(mcContent);
        logger.debug("attachmentList: " + attachmentList);
        mcGeneralMonitoringDTO.setAttachmentList(attachmentList);
        mcGeneralMonitoringDTO.setDeletedAttachmentList(new ArrayList());
        /** ...till here **/


		logger.debug("end of commonSubmitSessionCode, mcGeneralMonitoringDTO: " + mcGeneralMonitoringDTO);
		request.setAttribute(MC_GENERAL_MONITORING_DTO, mcGeneralMonitoringDTO);
		
    	EditActivityDTO editActivityDTO = new EditActivityDTO();
		logger.debug("isContentInUse:" + isContentInUse);
		if (isContentInUse == true)
		{
		    editActivityDTO.setMonitoredContentInUse(new Boolean(true).toString());
		}
		request.setAttribute(EDIT_ACTIVITY_DTO, editActivityDTO);
	
		
		/*find out if there are any reflection entries, from here*/
		boolean notebookEntriesExist=MonitoringUtil.notebookEntriesExist(mcService, mcContent);
		logger.debug("notebookEntriesExist : " + notebookEntriesExist);
		
		if (notebookEntriesExist)
		{
		    request.setAttribute(NOTEBOOK_ENTRIES_EXIST, new Boolean(true).toString());
		    
		    String userExceptionNoToolSessions=(String)mcGeneralMonitoringDTO.getUserExceptionNoToolSessions();
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
		/* ... till here*/
		
		MonitoringUtil.buildMcStatsDTO(request,mcService, mcContent);
		
		MonitoringUtil.setupAllSessionsData(request, mcContent,mcService);
		
        logger.debug("dumping jsp controllers: ");
        logger.debug("dumping jsp controlllers, mcGeneralMonitoringDTO, mcGeneralMonitoringDTO.getIsMonitoredContentInUse():" + mcGeneralMonitoringDTO.getIsMonitoredContentInUse());
        logger.debug("dumping jsp controlllers, mcGeneralMonitoringDTO, mcGeneralMonitoringDTO.getUserExceptionNoToolSessions():" + mcGeneralMonitoringDTO.getUserExceptionNoToolSessions());
        logger.debug("dumping jsp controlllers, mcGeneralMonitoringDTO, mcGeneralMonitoringDTO.getCurrentMonitoredToolSession():" + mcGeneralMonitoringDTO.getCurrentMonitoredToolSession());
        logger.debug("dumping jsp controlllers, mcGeneralMonitoringDTO, mcGeneralMonitoringDTO.getSelectionCase(): " + mcGeneralMonitoringDTO.getSelectionCase());
        
        
        logger.debug("dumping request level  controlllers, REQUEST selectionCase: " + request.getAttribute(SELECTION_CASE));
    }
    

    /**
     * refreshSummaryData(HttpServletRequest request, McContent mcContent, IMcService mcService, 
			boolean isUserNamesVisible, boolean isLearnerRequest, String currentSessionId, String userId, 
			boolean showUserEntriesBySession, McGeneralLearnerFlowDTO mcGeneralLearnerFlowDTO, 
			McGeneralMonitoringDTO mcGeneralMonitoringDTO, ExportPortfolioDTO exportPortfolioDTO)
			
     * refreshes summary data
     * 
     * @param request
     * @param mcContent
     * @param mcService
     * @param isUserNamesVisible
     * @param isLearnerRequest
     * @param currentSessionId
     * @param userId
     * @param showUserEntriesBySession
     * @param mcGeneralLearnerFlowDTO
     * @param mcGeneralMonitoringDTO
     * @param exportPortfolioDTO
     */
	public void refreshSummaryData(HttpServletRequest request, McContent mcContent, IMcService mcService, 
			boolean isUserNamesVisible, boolean isLearnerRequest, String currentSessionId, String userId, 
			boolean showUserEntriesBySession, McGeneralLearnerFlowDTO mcGeneralLearnerFlowDTO, 
			McGeneralMonitoringDTO mcGeneralMonitoringDTO, ExportPortfolioDTO exportPortfolioDTO)
	{
	    logger.debug("doing refreshSummaryData." + mcGeneralLearnerFlowDTO);
	    logger.debug("mcGeneralMonitoringDTO:" + mcGeneralMonitoringDTO);
	    logger.debug("exportPortfolioDTO:" + exportPortfolioDTO);
	    
		if (mcService == null)
		{
			logger.debug("will retrieve mcService");
			mcService = McServiceProxy.getMcService(getServlet().getServletContext());
			logger.debug("retrieving mcService from session: " + mcService);
		}
		logger.debug("mcService: " + mcService);
		
		logger.debug("isUserNamesVisible: " + isUserNamesVisible);
		logger.debug("isLearnerRequest: " + isLearnerRequest);
				
		/* this section is related to summary tab. Starts here. */
		Map summaryToolSessions=MonitoringUtil.populateToolSessions(request, mcContent, mcService);
		logger.debug("summaryToolSessions: " + summaryToolSessions);
		
		if (mcService.studentActivityOccurredGlobal(mcContent))
		{
		    if (mcGeneralMonitoringDTO != null)
		        mcGeneralMonitoringDTO.setUserExceptionNoToolSessions(new Boolean(false).toString());
				logger.debug("USER_EXCEPTION_NO_TOOL_SESSIONS is set to false");
		}
		else
		{
		    if (mcGeneralMonitoringDTO != null)
		        mcGeneralMonitoringDTO.setUserExceptionNoToolSessions(new Boolean(true).toString());
				logger.debug("USER_EXCEPTION_NO_TOOL_SESSIONS is set to true");
		}
		
		String userExceptionNoToolSessions=mcGeneralMonitoringDTO.getUserExceptionNoToolSessions();
		logger.debug("userExceptionNoToolSessions: " + userExceptionNoToolSessions);
		if (exportPortfolioDTO != null)
		{
		    exportPortfolioDTO.setUserExceptionNoToolSessions(userExceptionNoToolSessions);
		}
		
		Map summaryToolSessionsId=MonitoringUtil.populateToolSessionsId(request, mcContent, mcService);
		logger.debug("summaryToolSessionsId: " + summaryToolSessionsId);
	    logger.debug("currentSessionId: " + currentSessionId);
	    
	    if ((currentSessionId != null) && (!currentSessionId.equals("All")))
	    {
		    McSession mcSession= mcService.retrieveMcSession(new Long(currentSessionId));
		    logger.debug("mcSession:" + mcSession);
		    if (mcGeneralMonitoringDTO != null)
		        mcGeneralMonitoringDTO.setGroupName(mcSession.getSession_name());
	    }
	    else
	    {
	        mcGeneralMonitoringDTO.setGroupName("All Groups");
	    }
	    
	    logger.debug("using allUsersData to retrieve data: " + isUserNamesVisible);
	    
	    List listMonitoredAnswersContainerDTO = new LinkedList();
	    logger.debug("listMonitoredAnswersContainerDTO: " + listMonitoredAnswersContainerDTO);	    
	    /* ends here. */
	    
	    logger.debug("decide processing user entered values based on isLearnerRequest: " + isLearnerRequest);

	    
	    if (exportPortfolioDTO != null)
	    {
	        logger.debug("placing dtos within the exportPortfolioDTO: ");
	        exportPortfolioDTO.setListMonitoredAnswersContainerDto(listMonitoredAnswersContainerDTO);
	    }
	    
	    
	    if (mcGeneralMonitoringDTO != null)
	    {
	        mcGeneralMonitoringDTO.setSummaryToolSessions(summaryToolSessions);
	        mcGeneralMonitoringDTO.setSummaryToolSessionsId(summaryToolSessionsId);
	        mcGeneralMonitoringDTO.setSelectionCase(new Long(2));
	        mcGeneralMonitoringDTO.setCurrentMonitoredToolSession("All");
	        mcGeneralMonitoringDTO.setListMonitoredAnswersContainerDto(listMonitoredAnswersContainerDTO);
		    
		    mcGeneralMonitoringDTO.setExistsOpenMcs(new Boolean(false).toString());
	    }
	    
	    logger.debug("final mcGeneralLearnerFlowDTO: " + mcGeneralLearnerFlowDTO);
	    logger.debug("final mcGeneralMonitoringDTO: " + mcGeneralMonitoringDTO);
		
		boolean isContentInUse=McUtils.isContentInUse(mcContent);
		logger.debug("isContentInUse:" + isContentInUse);
		mcGeneralMonitoringDTO.setIsMonitoredContentInUse(new Boolean(false).toString());
		if (isContentInUse == true)
		{
			logger.debug("monitoring url does not allow editActivity since the content is in use.");
			mcGeneralMonitoringDTO.setIsMonitoredContentInUse(new Boolean(true).toString());
		}

		prepareReflectionData(request, mcContent, mcService, null, false, "All");
		
		if (mcService.studentActivityOccurredGlobal(mcContent))
		{
			logger.debug("USER_EXCEPTION_NO_TOOL_SESSIONS is set to false");
			mcGeneralMonitoringDTO.setUserExceptionNoToolSessions(new Boolean(false).toString());
		}
		else
		{
			logger.debug("USER_EXCEPTION_NO_TOOL_SESSIONS is set to true");
			mcGeneralMonitoringDTO.setUserExceptionNoToolSessions(new Boolean(true).toString());
		}

		/**getting instructions screen content from here... */
		mcGeneralMonitoringDTO.setOnlineInstructions(mcContent.getOnlineInstructions());
		mcGeneralMonitoringDTO.setOfflineInstructions(mcContent.getOfflineInstructions());
		
        List attachmentList = mcService.retrieveMcUploadedFiles(mcContent);
        logger.debug("attachmentList: " + attachmentList);
        mcGeneralMonitoringDTO.setAttachmentList(attachmentList);
        mcGeneralMonitoringDTO.setDeletedAttachmentList(new ArrayList());
        /** ...till here **/

		
	    logger.debug("end of refreshSummaryData, mcGeneralMonitoringDTO" + mcGeneralMonitoringDTO);
		request.setAttribute(MC_GENERAL_MONITORING_DTO, mcGeneralMonitoringDTO);
		
    	EditActivityDTO editActivityDTO = new EditActivityDTO();
		isContentInUse=McUtils.isContentInUse(mcContent);
		logger.debug("isContentInUse:" + isContentInUse);
		if (isContentInUse == true)
		{
		    editActivityDTO.setMonitoredContentInUse(new Boolean(true).toString());
		}
		request.setAttribute(EDIT_ACTIVITY_DTO, editActivityDTO);
		
		
		/*find out if there are any reflection entries, from here*/
		boolean notebookEntriesExist=MonitoringUtil.notebookEntriesExist(mcService, mcContent);
		logger.debug("notebookEntriesExist : " + notebookEntriesExist);
		
		if (notebookEntriesExist)
		{
		    request.setAttribute(NOTEBOOK_ENTRIES_EXIST, new Boolean(true).toString());
		    
		    userExceptionNoToolSessions=(String)mcGeneralMonitoringDTO.getUserExceptionNoToolSessions();
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
		/* ... till here*/
		
		MonitoringUtil.buildMcStatsDTO(request,mcService, mcContent);
		
		MonitoringUtil.setupAllSessionsData(request, mcContent,mcService);
	}


    
	/**
	 * ActionForward submitSession(ActionMapping mapping,
            ActionForm form,
            HttpServletRequest request,
            HttpServletResponse response)
	 * 
	 * submitSession
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
            HttpServletResponse response) 
    		throws IOException,ServletException
	{
        logger.debug("dispathcing submitSession..");
        IMcService mcService = McServiceProxy.getMcService(getServlet().getServletContext());
        logger.debug("mcService: " +mcService);
        
        McGeneralMonitoringDTO mcGeneralMonitoringDTO= new McGeneralMonitoringDTO();
        
        commonSubmitSessionCode(form, request, mcService,mcGeneralMonitoringDTO);
        logger.debug("post commonSubmitSessionCode: " + mcGeneralMonitoringDTO);

        return (mapping.findForward(LOAD_MONITORING_CONTENT));	
	}


    /**
     * editActivityQuestions
     * 
     * enables swiching to editable mode in the Edit Activity tab
     * 
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws IOException
     * @throws ServletException
     * @throws ToolException
     */
    public ActionForward editActivityQuestions(ActionMapping mapping,
            ActionForm form,
            HttpServletRequest request,
            HttpServletResponse response) throws IOException,
                                         ServletException,
                                         ToolException
    {
    	logger.debug("dispatching editActivityQuestions...");
    	
    	McAuthoringForm mcAuthoringForm = (McMonitoringForm) form;
		logger.debug("mcAuthoringForm: " + mcAuthoringForm);
		
		IMcService mcService =McServiceProxy.getMcService(getServlet().getServletContext());
		logger.debug("mcService: " + mcService);

		String httpSessionID=mcAuthoringForm.getHttpSessionID();
		logger.debug("httpSessionID: " + httpSessionID);
		
		SessionMap sessionMap=(SessionMap)request.getSession().getAttribute(httpSessionID);
		logger.debug("sessionMap: " + sessionMap);
		
		String contentFolderID = WebUtil.readStrParam(request, AttributeNames.PARAM_CONTENT_FOLDER_ID);
		logger.debug("contentFolderID: " + contentFolderID);
		mcAuthoringForm.setContentFolderID(contentFolderID);


		String activeModule=request.getParameter(ACTIVE_MODULE);
		logger.debug("activeModule: " + activeModule);
		
		String strToolContentID=request.getParameter(AttributeNames.PARAM_TOOL_CONTENT_ID);
		logger.debug("strToolContentID: " + strToolContentID);
		
		String defaultContentIdStr=new Long(mcService.getToolDefaultContentIdBySignature(MY_SIGNATURE)).toString();
		//String defaultContentIdStr=request.getParameter(DEFAULT_CONTENT_ID_STR);
		logger.debug("defaultContentIdStr: " + defaultContentIdStr);
		
		McContent mcContent=mcService.retrieveMc(new Long(strToolContentID));
		logger.debug("mcContent: " + mcContent);
		
		McGeneralAuthoringDTO mcGeneralAuthoringDTO= new McGeneralAuthoringDTO();
		logger.debug("mcGeneralAuthoringDTO: " + mcGeneralAuthoringDTO);
		mcGeneralAuthoringDTO.setContentFolderID(contentFolderID);

		
        logger.debug("title: " + mcContent.getTitle());
        logger.debug("instructions: " + mcContent.getInstructions());
        
        mcGeneralAuthoringDTO.setActivityTitle(mcContent.getTitle());
        mcAuthoringForm.setTitle(mcContent.getTitle());
        
        mcGeneralAuthoringDTO.setActivityInstructions(mcContent.getInstructions());
        
        sessionMap.put(ACTIVITY_TITLE_KEY, mcContent.getTitle());
        sessionMap.put(ACTIVITY_INSTRUCTIONS_KEY, mcContent.getInstructions());
    	
		
		/* determine whether the request is from Monitoring url Edit Activity*/
		String sourceMcStarter = (String) request.getAttribute(SOURCE_MC_STARTER);
		logger.debug("sourceMcStarter: " + sourceMcStarter);

		mcAuthoringForm.setDefineLaterInEditMode(new Boolean(true).toString());
     	mcGeneralAuthoringDTO.setDefineLaterInEditMode(new Boolean(true).toString());
     	
		boolean isContentInUse=McUtils.isContentInUse(mcContent);
		logger.debug("isContentInUse:" + isContentInUse);
		
		mcGeneralAuthoringDTO.setMonitoredContentInUse(new Boolean(false).toString());
		if (isContentInUse == true)
		{
			logger.debug("monitoring url does not allow editActivity since the content is in use.");
	    	persistError(request,"error.content.inUse");
	    	mcGeneralAuthoringDTO.setMonitoredContentInUse(new Boolean(true).toString());
		}
		
	    EditActivityDTO editActivityDTO = new EditActivityDTO();
		logger.debug("isContentInUse:" + isContentInUse);
		if (isContentInUse == true)
		{
		    editActivityDTO.setMonitoredContentInUse(new Boolean(true).toString());
		}
		request.setAttribute(EDIT_ACTIVITY_DTO, editActivityDTO);

		McUtils.setDefineLater(request, true, strToolContentID, mcService);

		McUtils.setFormProperties(request, mcService,  
	             mcAuthoringForm, mcGeneralAuthoringDTO, strToolContentID, defaultContentIdStr, activeModule, sessionMap, httpSessionID);


		mcGeneralAuthoringDTO.setToolContentID(strToolContentID);
		mcGeneralAuthoringDTO.setActiveModule(activeModule);
		mcGeneralAuthoringDTO.setDefaultContentIdStr(defaultContentIdStr);
		mcAuthoringForm.setToolContentID(strToolContentID);
		mcAuthoringForm.setActiveModule(activeModule);
		mcAuthoringForm.setDefaultContentIdStr(defaultContentIdStr);
		mcAuthoringForm.setCurrentTab("3");

		
	    AuthoringUtil authoringUtil= new AuthoringUtil();
	    List listQuestionContentDTO=authoringUtil.buildDefaultQuestionContent(mcContent, mcService);
	    logger.debug("listQuestionContentDTO: " + listQuestionContentDTO);

	    request.setAttribute(TOTAL_QUESTION_COUNT, new Integer(listQuestionContentDTO.size()));
		logger.debug("listQuestionContentDTO: " + listQuestionContentDTO);
		request.setAttribute(LIST_QUESTION_CONTENT_DTO,listQuestionContentDTO);
		sessionMap.put(LIST_QUESTION_CONTENT_DTO_KEY, listQuestionContentDTO);

		request.setAttribute(TOTAL_QUESTION_COUNT, new Integer(listQuestionContentDTO.size()));
		request.getSession().setAttribute(httpSessionID, sessionMap);

	    Map marksMap=authoringUtil.buildMarksMap();
	    logger.debug("marksMap: " + marksMap);
	    mcGeneralAuthoringDTO.setMarksMap(marksMap);

	    
	    Map correctMap=authoringUtil.buildCorrectMap();
	    logger.debug("correctMap: " + correctMap);
	    mcGeneralAuthoringDTO.setCorrectMap(correctMap);
	    
		logger.debug("before fwding to jsp, mcAuthoringForm: " + mcAuthoringForm);
		logger.debug("before saving final mcGeneralAuthoringDTO: " + mcGeneralAuthoringDTO);
		request.setAttribute(MC_GENERAL_AUTHORING_DTO, mcGeneralAuthoringDTO);
		
		
		
		/*setting up USER_EXCEPTION_NO_TOOL_SESSIONS, from here */
		McGeneralMonitoringDTO mcGeneralMonitoringDTO= new McGeneralMonitoringDTO();
		mcGeneralMonitoringDTO.setDefineLaterInEditMode(new Boolean(true).toString());
		
		if (mcService.studentActivityOccurredGlobal(mcContent))
		{
			logger.debug("USER_EXCEPTION_NO_TOOL_SESSIONS is set to false");
			mcGeneralMonitoringDTO.setUserExceptionNoToolSessions(new Boolean(false).toString());
		}
		else
		{
			logger.debug("USER_EXCEPTION_NO_TOOL_SESSIONS is set to true");
			mcGeneralMonitoringDTO.setUserExceptionNoToolSessions(new Boolean(true).toString());
		}
		
		
		mcGeneralMonitoringDTO.setOnlineInstructions(mcContent.getOnlineInstructions());
		mcGeneralMonitoringDTO.setOfflineInstructions(mcContent.getOfflineInstructions());
		
        List attachmentList = mcService.retrieveMcUploadedFiles(mcContent);
        logger.debug("attachmentList: " + attachmentList);
        mcGeneralMonitoringDTO.setAttachmentList(attachmentList);
        mcGeneralMonitoringDTO.setDeletedAttachmentList(new ArrayList());
        

		logger.debug("end of refreshSummaryData, mcGeneralMonitoringDTO" + mcGeneralMonitoringDTO);
		request.setAttribute(MC_GENERAL_MONITORING_DTO, mcGeneralMonitoringDTO);
		/*.. till here*/
		
		
		/*find out if there are any reflection entries, from here*/
		boolean notebookEntriesExist=MonitoringUtil.notebookEntriesExist(mcService, mcContent);
		logger.debug("notebookEntriesExist : " + notebookEntriesExist);
		
		if (notebookEntriesExist)
		{
		    request.setAttribute(NOTEBOOK_ENTRIES_EXIST, new Boolean(true).toString());
		    
		    String userExceptionNoToolSessions=(String)mcGeneralMonitoringDTO.getUserExceptionNoToolSessions();
		    logger.debug(": " + userExceptionNoToolSessions);
		    
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
		/* ... till here*/

		MonitoringUtil.buildMcStatsDTO(request,mcService, mcContent);
		MonitoringUtil.generateGroupsSessionData(request, mcService, mcContent);
		
		MonitoringUtil.setupAllSessionsData(request, mcContent,mcService);
		

		logger.debug("forwarding to LOAD_MONITORING_CONTENT: " + LOAD_MONITORING_CONTENT);
		return mapping.findForward(LOAD_MONITORING_CONTENT);
    }
    
    

    /**
     * persistError(HttpServletRequest request, String message)
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
    
    

    
    
    /**
     * 
     * ActionForward submitAllContent(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) 
        throws IOException, ServletException
        
        submits content into the tool database
     * 
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws IOException
     * @throws ServletException
     */
    public ActionForward submitAllContent(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) 
        throws IOException, ServletException {
    	
    	logger.debug("dispathcing submitAllContent :" +form);
    	
    	McAuthoringForm mcAuthoringForm = (McMonitoringForm) form;

		IMcService mcService =McServiceProxy.getMcService(getServlet().getServletContext());
		logger.debug("mcService: " + mcService);
		
		String httpSessionID=mcAuthoringForm.getHttpSessionID();
		logger.debug("httpSessionID: " + httpSessionID);
		
		SessionMap sessionMap=(SessionMap)request.getSession().getAttribute(httpSessionID);
		logger.debug("sessionMap: " + sessionMap);

		String contentFolderID = WebUtil.readStrParam(request, AttributeNames.PARAM_CONTENT_FOLDER_ID);
		logger.debug("contentFolderID: " + contentFolderID);
		mcAuthoringForm.setContentFolderID(contentFolderID);

		String activeModule=request.getParameter(ACTIVE_MODULE);
		logger.debug("activeModule: " + activeModule);

		String strToolContentID=request.getParameter(AttributeNames.PARAM_TOOL_CONTENT_ID);
		logger.debug("strToolContentID: " + strToolContentID);

		
		String defaultContentIdStr=new Long(mcService.getToolDefaultContentIdBySignature(MY_SIGNATURE)).toString();
		
        List listQuestionContentDTO=(List)sessionMap.get(LIST_QUESTION_CONTENT_DTO_KEY);
        logger.debug("listQuestionContentDTO: " + listQuestionContentDTO);
        
        Map mapQuestionContent=AuthoringUtil.extractMapQuestionContent(listQuestionContentDTO);
        logger.debug("extracted mapQuestionContent: " + mapQuestionContent);
         
        Map mapFeedback=AuthoringUtil.extractMapFeedback(listQuestionContentDTO);
        logger.debug("extracted mapFeedback: " + mapFeedback);
        
        Map mapWeights=new TreeMap(new McComparator());

	    String totalMarks=request.getParameter("totalMarks");
		logger.debug("totalMarks: " + totalMarks);

        Map mapMarks=AuthoringUtil.extractMapMarks(listQuestionContentDTO);
        logger.debug("extracted mapMarks: " + mapMarks);

        Map mapCandidatesList=AuthoringUtil.extractMapCandidatesList(listQuestionContentDTO);
        logger.debug("extracted mapCandidatesList: " + mapCandidatesList);
        
        
        ActionMessages errors = new ActionMessages();
        logger.debug("mapQuestionContent size: " + mapQuestionContent.size());
        
        if (mapQuestionContent.size() == 0)
        {
            ActionMessage error = new ActionMessage("questions.none.submitted");
			errors.add(ActionMessages.GLOBAL_MESSAGE, error);
        }
        logger.debug("errors: " + errors);
	    
	    AuthoringUtil authoringUtil= new AuthoringUtil();
	 	
	 	McGeneralAuthoringDTO mcGeneralAuthoringDTO= new McGeneralAuthoringDTO();
	 	
        logger.debug("activeModule: " + activeModule);
        if (activeModule.equals(AUTHORING))
        {
    		List attachmentListBackup= new  ArrayList();
            List attachmentList=(List)sessionMap.get(ATTACHMENT_LIST_KEY);
    	    logger.debug("attachmentList: " + attachmentList);
    	    attachmentListBackup=attachmentList;
    	    
    	    List deletedAttachmentListBackup= new  ArrayList();
    	    List deletedAttachmentList=(List)sessionMap.get(DELETED_ATTACHMENT_LIST_KEY);
    	    logger.debug("deletedAttachmentList: " + deletedAttachmentList);
    	    deletedAttachmentListBackup=deletedAttachmentList;
    	    
            String onlineInstructions=(String)sessionMap.get(ONLINE_INSTRUCTIONS_KEY);
            logger.debug("onlineInstructions: " + onlineInstructions);
    	    mcGeneralAuthoringDTO.setOnlineInstructions(onlineInstructions);

            String offlineInstructions=(String)sessionMap.get(OFFLINE_INSTRUCTIONS_KEY);
            logger.debug("offlineInstructions: " + offlineInstructions);
    	    mcGeneralAuthoringDTO.setOfflineInstructions(offlineInstructions);

    	    mcGeneralAuthoringDTO.setAttachmentList(attachmentList);
    		mcGeneralAuthoringDTO.setDeletedAttachmentList(deletedAttachmentList);
    		
    		String strOnlineInstructions= request.getParameter("onlineInstructions");
    		String strOfflineInstructions= request.getParameter("offlineInstructions");
    		logger.debug("onlineInstructions: " + strOnlineInstructions);
    		logger.debug("offlineInstructions: " + strOfflineInstructions);
    		mcAuthoringForm.setOfflineInstructions(strOfflineInstructions);
    		mcAuthoringForm.setOnlineInstructions(strOnlineInstructions);

        }
	 	
	 	
		mcGeneralAuthoringDTO.setContentFolderID(contentFolderID);
		
	 	String richTextTitle = request.getParameter(TITLE);
        String richTextInstructions = request.getParameter(INSTRUCTIONS);
	 	
        logger.debug("richTextTitle: " + richTextTitle);
        logger.debug("richTextInstructions: " + richTextInstructions);
        
        mcGeneralAuthoringDTO.setActivityTitle(richTextTitle);
        mcAuthoringForm.setTitle(richTextTitle);
        
        mcGeneralAuthoringDTO.setActivityInstructions(richTextInstructions);
        
        sessionMap.put(ACTIVITY_TITLE_KEY, richTextTitle);
        sessionMap.put(ACTIVITY_INSTRUCTIONS_KEY, richTextInstructions);
        
	    mcGeneralAuthoringDTO.setMapQuestionContent(mapQuestionContent);
		logger.debug("mcGeneralAuthoringDTO: " + mcGeneralAuthoringDTO);
        
		logger.debug("mcGeneralAuthoringDTO now: " + mcGeneralAuthoringDTO);
		request.setAttribute(MC_GENERAL_AUTHORING_DTO, mcGeneralAuthoringDTO);
		
	 	logger.debug("there are no issues with input, continue and submit data");

        McContent mcContentTest=mcService.retrieveMc(new Long(strToolContentID));
		logger.debug("mcContentTest: " + mcContentTest);

		logger.debug("errors: " + errors);
	 	if(!errors.isEmpty()){
			saveErrors(request, errors);
	        logger.debug("errors saved: " + errors);
		}
	 	
	 	McGeneralMonitoringDTO mcGeneralMonitoringDTO= new McGeneralMonitoringDTO();

	 	McContent mcContent=mcContentTest;
	 	if(errors.isEmpty()){
	 	    logger.debug("errors is empty: " + errors);
	        authoringUtil.removeRedundantQuestions(mapQuestionContent, mcService, mcAuthoringForm, request, strToolContentID);
	        logger.debug("end of removing unused entries... ");

	        mcContent=authoringUtil.saveOrUpdateMcContent(mapQuestionContent, mapFeedback, mapWeights, 
	                mapMarks, mapCandidatesList, mcService, mcAuthoringForm, request, mcContentTest, strToolContentID);
	        logger.debug("mcContent: " + mcContent);
	        
	        
	        long defaultContentID=0;
			logger.debug("attempt retrieving tool with signatute : " + MY_SIGNATURE);
	        defaultContentID=mcService.getToolDefaultContentIdBySignature(MY_SIGNATURE);
			logger.debug("retrieved tool default contentId: " + defaultContentID);
			
	        if (mcContent != null)
	        {
	            mcGeneralAuthoringDTO.setDefaultContentIdStr(new Long(defaultContentID).toString());
	        }
			logger.debug("updated mcGeneralAuthoringDTO to: " + mcGeneralAuthoringDTO);
			
		    authoringUtil.reOrganizeDisplayOrder(mapQuestionContent, mcService, mcAuthoringForm, mcContent);
		    logger.debug("post reOrganizeDisplayOrder: " + mcContent);

		    logger.debug("strToolContentID: " + strToolContentID);
	        McUtils.setDefineLater(request, false, strToolContentID, mcService);
	        logger.debug("define later set to false");
	        
			McUtils.setFormProperties(request, mcService,  
		             mcAuthoringForm, mcGeneralAuthoringDTO, strToolContentID, defaultContentIdStr, activeModule, sessionMap, httpSessionID);

		    logger.debug("go back to view only screen");
		    mcGeneralAuthoringDTO.setDefineLaterInEditMode(new Boolean(false).toString());
		    mcGeneralMonitoringDTO.setDefineLaterInEditMode(new Boolean(false).toString());
	 	}
	 	else
	 	{
	 	   logger.debug("errors is not empty: " + errors);
	 	   
	 	   if (mcContent != null)
	 	   {
		        long defaultContentID=0;
				logger.debug("attempt retrieving tool with signatute : " + MY_SIGNATURE);
		        defaultContentID=mcService.getToolDefaultContentIdBySignature(MY_SIGNATURE);
				logger.debug("retrieved tool default contentId: " + defaultContentID);
				
		        if (mcContent != null)
		        {
		            mcGeneralAuthoringDTO.setDefaultContentIdStr(new Long(defaultContentID).toString());
		        }

				McUtils.setFormProperties(request, mcService, 
			             mcAuthoringForm, mcGeneralAuthoringDTO, strToolContentID, defaultContentIdStr, activeModule, sessionMap, httpSessionID);

				mcGeneralMonitoringDTO.setDefineLaterInEditMode(new Boolean(true).toString());				
	 	   }
	 	}
	 	logger.debug("end of refreshSummaryData, mcGeneralMonitoringDTO" + mcGeneralMonitoringDTO);
    	request.setAttribute(MC_GENERAL_MONITORING_DTO, mcGeneralMonitoringDTO);
	 	

        mcGeneralAuthoringDTO.setSbmtSuccess(new Integer(1).toString());
        
        mcAuthoringForm.resetUserAction();
        mcGeneralAuthoringDTO.setMapQuestionContent(mapQuestionContent);
        
	    Map marksMap=authoringUtil.buildMarksMap();
	    logger.debug("marksMap: " + marksMap);
	    mcGeneralAuthoringDTO.setMarksMap(marksMap);
	    
	    
	    Map correctMap=authoringUtil.buildCorrectMap();
	    logger.debug("correctMap: " + correctMap);
	    mcGeneralAuthoringDTO.setCorrectMap(correctMap);

	    

		request.setAttribute(LIST_QUESTION_CONTENT_DTO,listQuestionContentDTO);
		sessionMap.put(LIST_QUESTION_CONTENT_DTO_KEY, listQuestionContentDTO);
	    request.getSession().setAttribute(httpSessionID, sessionMap);
	    
	    request.setAttribute(TOTAL_QUESTION_COUNT, new Integer(listQuestionContentDTO.size()));
	    
	    logger.debug("generating dyn pass map using listQuestionContentDTO: " +listQuestionContentDTO);
	    Map passMarksMap=authoringUtil.buildDynamicPassMarkMap(listQuestionContentDTO, false);
	    logger.debug("passMarksMap: " + passMarksMap);
	    mcGeneralAuthoringDTO.setPassMarksMap(passMarksMap);
	    
	    
        String totalMark=AuthoringUtil.getTotalMark(listQuestionContentDTO);
        logger.debug("totalMark: " + totalMark);
        mcAuthoringForm.setTotalMarks(totalMark);
        mcGeneralAuthoringDTO.setTotalMarks(totalMark);


        logger.debug("before saving final mcGeneralAuthoringDTO: " + mcGeneralAuthoringDTO);
		request.setAttribute(MC_GENERAL_AUTHORING_DTO, mcGeneralAuthoringDTO);

		
		mcGeneralAuthoringDTO.setToolContentID(strToolContentID);
		mcGeneralAuthoringDTO.setHttpSessionID(httpSessionID);
		mcGeneralAuthoringDTO.setActiveModule(activeModule);
		mcGeneralAuthoringDTO.setDefaultContentIdStr(defaultContentIdStr);
		
		mcAuthoringForm.setToolContentID(strToolContentID);
		mcAuthoringForm.setHttpSessionID(httpSessionID);
		mcAuthoringForm.setActiveModule(activeModule);
		mcAuthoringForm.setDefaultContentIdStr(defaultContentIdStr);
		mcAuthoringForm.setCurrentTab("3");
		
		
		/*common screen data*/
    	if (mcService.studentActivityOccurredGlobal(mcContent))
    	{
    		logger.debug("USER_EXCEPTION_NO_TOOL_SESSIONS is set to false");
    		mcGeneralMonitoringDTO.setUserExceptionNoToolSessions(new Boolean(false).toString());
    	}
    	else
    	{
    		logger.debug("USER_EXCEPTION_NO_TOOL_SESSIONS is set to true");
    		mcGeneralMonitoringDTO.setUserExceptionNoToolSessions(new Boolean(true).toString());
    	}
    	
    	
    	mcGeneralMonitoringDTO.setOnlineInstructions(mcContent.getOnlineInstructions());
    	mcGeneralMonitoringDTO.setOfflineInstructions(mcContent.getOfflineInstructions());
    	
        List attachmentList = mcService.retrieveMcUploadedFiles(mcContent);
        logger.debug("attachmentList: " + attachmentList);
        mcGeneralMonitoringDTO.setAttachmentList(attachmentList);
        mcGeneralMonitoringDTO.setDeletedAttachmentList(new ArrayList());
        

    	/*find out if there are any reflection entries, from here*/
    	boolean notebookEntriesExist=MonitoringUtil.notebookEntriesExist(mcService, mcContent);
    	logger.debug("notebookEntriesExist : " + notebookEntriesExist);
    	
    	if (notebookEntriesExist)
    	{
    	    request.setAttribute(NOTEBOOK_ENTRIES_EXIST, new Boolean(true).toString());
    	    
    	    String userExceptionNoToolSessions=(String)mcGeneralMonitoringDTO.getUserExceptionNoToolSessions();
    	    logger.debug(": " + userExceptionNoToolSessions);
    	    
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
    	/* ... till here*/

    	MonitoringUtil.buildMcStatsDTO(request,mcService, mcContent);
    	MonitoringUtil.generateGroupsSessionData(request, mcService, mcContent);
    	logger.debug("ending setupCommonScreenData, mcContent " + mcContent);
 
    	MonitoringUtil.setupAllSessionsData(request, mcContent,mcService);

		
        
		logger.debug("forwarding to :" + LOAD_MONITORING);
        return mapping.findForward(LOAD_MONITORING);
    }

    
    /**
     * 
     * saveSingleQuestion(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
     * 
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws IOException
     * @throws ServletException
     */
	public ActionForward saveSingleQuestion(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) 
    throws IOException, ServletException {
		
		logger.debug("dispathcing saveSingleQuestion");
		McAuthoringForm mcAuthoringForm = (McMonitoringForm) form;
	
		IMcService mcService =McServiceProxy.getMcService(getServlet().getServletContext());
		logger.debug("mcService: " + mcService);
		
		String httpSessionID=mcAuthoringForm.getHttpSessionID();
		logger.debug("httpSessionID: " + httpSessionID);
		
		SessionMap sessionMap=(SessionMap)request.getSession().getAttribute(httpSessionID);
		logger.debug("sessionMap: " + sessionMap);
		
		String contentFolderID = WebUtil.readStrParam(request, AttributeNames.PARAM_CONTENT_FOLDER_ID);
		logger.debug("contentFolderID: " + contentFolderID);
		mcAuthoringForm.setContentFolderID(contentFolderID);
	
	
		String activeModule=request.getParameter(ACTIVE_MODULE);
		logger.debug("activeModule: " + activeModule);
	
		String strToolContentID=request.getParameter(AttributeNames.PARAM_TOOL_CONTENT_ID);
		logger.debug("strToolContentID: " + strToolContentID);
		
		String defaultContentIdStr=new Long(mcService.getToolDefaultContentIdBySignature(MY_SIGNATURE)).toString();;
		logger.debug("defaultContentIdStr: " + defaultContentIdStr);
		
		String editQuestionBoxRequest=request.getParameter("editQuestionBoxRequest");
		logger.debug("editQuestionBoxRequest: " + editQuestionBoxRequest);

	    String totalMarks=request.getParameter("totalMarks");
		logger.debug("totalMarks: " + totalMarks);

		
		String mark=request.getParameter("mark");
		logger.debug("mark: " + mark);
		
		String passmark=request.getParameter("passmark");
		logger.debug("passmark: " + passmark);

		
		AuthoringUtil authoringUtil = new AuthoringUtil();
	    List caList=authoringUtil.repopulateCandidateAnswersBox(request, false);
	    logger.debug("repopulated caList: " + caList);
	    
	    caList=AuthoringUtil.removeBlankEntries(caList);
	    logger.debug("caList after removing blank entries: " + caList);
	    

		boolean validateSingleCorrectCandidate=authoringUtil.validateSingleCorrectCandidate(caList);
	    logger.debug("validateSingleCorrectCandidate: " + validateSingleCorrectCandidate);


		boolean validateOnlyOneCorrectCandidate=authoringUtil.validateOnlyOneCorrectCandidate(caList);
	    logger.debug("validateOnlyOneCorrectCandidate: " + validateOnlyOneCorrectCandidate);

	    
	    
        ActionMessages errors = new ActionMessages();
        
        
        if (!validateSingleCorrectCandidate)
        {
            ActionMessage error = new ActionMessage("candidates.none.correct");
			errors.add(ActionMessages.GLOBAL_MESSAGE, error);
        }

        if (!validateOnlyOneCorrectCandidate)
        {
            ActionMessage error = new ActionMessage("candidates.duplicate.correct");
			errors.add(ActionMessages.GLOBAL_MESSAGE, error);
        }
        
        logger.debug("errors: " + errors);

	 	if(!errors.isEmpty()){
			saveErrors(request, errors);
	        logger.debug("errors saved: " + errors);
		}

        List listQuestionContentDTO=(List)sessionMap.get(LIST_QUESTION_CONTENT_DTO_KEY);
        logger.debug("listQuestionContentDTO: " + listQuestionContentDTO);
		
        McGeneralAuthoringDTO mcGeneralAuthoringDTO= new McGeneralAuthoringDTO();

        logger.debug("entry using mark: " + mark);
	    mcGeneralAuthoringDTO.setMarkValue(mark);
	    mcGeneralAuthoringDTO.setPassMarkValue(passmark);

	    McContent mcContent=mcService.retrieveMc(new Long(strToolContentID));
		logger.debug("mcContent: " + mcContent);

	    
	 	if(errors.isEmpty())
	 	{
	        logger.debug("errors is empty: " + errors);
			
			logger.debug("mcGeneralAuthoringDTO: " + mcGeneralAuthoringDTO);
			mcGeneralAuthoringDTO.setContentFolderID(contentFolderID);
			
			mcGeneralAuthoringDTO.setSbmtSuccess(new Integer(0).toString());
			
		    String newQuestion=request.getParameter("newQuestion");
		    logger.debug("newQuestion: " + newQuestion);
		    
		    String feedback=request.getParameter("feedback");
		    logger.debug("feedback: " + feedback);

		    
		    String editableQuestionIndex=request.getParameter("editableQuestionIndex");
		    logger.debug("editableQuestionIndex: " + editableQuestionIndex);
		    mcAuthoringForm.setQuestionIndex(editableQuestionIndex);
		    

		    if ((newQuestion != null) && (newQuestion.length() > 0))
		    {
			        if ((editQuestionBoxRequest != null) && (editQuestionBoxRequest.equals("false")))
			        {
			            logger.debug("request for add and save");
				        boolean duplicates=AuthoringUtil.checkDuplicateQuestions(listQuestionContentDTO, newQuestion);
				        logger.debug("duplicates: " + duplicates);
			            
				        if (!duplicates)
				        {
						    McQuestionContentDTO mcQuestionContentDTO= null;
						    Iterator listIterator=listQuestionContentDTO.iterator();
						    while (listIterator.hasNext())
						    {
						        mcQuestionContentDTO= (McQuestionContentDTO)listIterator.next();
						        logger.debug("mcQuestionContentDTO:" + mcQuestionContentDTO);
						        logger.debug("mcQuestionContentDTO question:" + mcQuestionContentDTO.getQuestion());
						        
						        String question=mcQuestionContentDTO.getQuestion();
						        String displayOrder=mcQuestionContentDTO.getDisplayOrder();
						        logger.debug("displayOrder:" + displayOrder);
						        
						        if ((displayOrder != null) && (!displayOrder.equals("")))
					    		{
						            if (displayOrder.equals(editableQuestionIndex))
						            {
						                break;
						            }
						            
					    		}
						    }
						    logger.debug("mcQuestionContentDTO found:" + mcQuestionContentDTO);
						    
						    mcQuestionContentDTO.setQuestion(newQuestion);
						    mcQuestionContentDTO.setFeedback(feedback);
						    mcQuestionContentDTO.setDisplayOrder(editableQuestionIndex);
						    mcQuestionContentDTO.setListCandidateAnswersDTO(caList);
						    mcQuestionContentDTO.setMark(mark);
						    
						    logger.debug("caList size:" + mcQuestionContentDTO.getListCandidateAnswersDTO().size());
						    mcQuestionContentDTO.setCaCount(new Integer(mcQuestionContentDTO.getListCandidateAnswersDTO().size()).toString());
						    
						    
						    listQuestionContentDTO=AuthoringUtil.reorderUpdateListQuestionContentDTO(listQuestionContentDTO, mcQuestionContentDTO, editableQuestionIndex);
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
						    McQuestionContentDTO mcQuestionContentDTO= null;
						    Iterator listIterator=listQuestionContentDTO.iterator();
						    while (listIterator.hasNext())
						    {
						        mcQuestionContentDTO= (McQuestionContentDTO)listIterator.next();
						        logger.debug("mcQuestionContentDTO:" + mcQuestionContentDTO);
						        logger.debug("mcQuestionContentDTO question:" + mcQuestionContentDTO.getQuestion());
						        
						        String question=mcQuestionContentDTO.getQuestion();
						        String displayOrder=mcQuestionContentDTO.getDisplayOrder();
						        logger.debug("displayOrder:" + displayOrder);
						        
						        if ((displayOrder != null) && (!displayOrder.equals("")))
					    		{
						            if (displayOrder.equals(editableQuestionIndex))
						            {
						                break;
						            }
						            
					    		}
						    }
						    logger.debug("mcQuestionContentDTO found:" + mcQuestionContentDTO);
						    
						    mcQuestionContentDTO.setQuestion(newQuestion);
						    mcQuestionContentDTO.setFeedback(feedback);
						    mcQuestionContentDTO.setDisplayOrder(editableQuestionIndex);
						    mcQuestionContentDTO.setListCandidateAnswersDTO(caList);
						    mcQuestionContentDTO.setMark(mark);
						    
						    logger.debug("caList size:" + mcQuestionContentDTO.getListCandidateAnswersDTO().size());
						    mcQuestionContentDTO.setCaCount(new Integer(mcQuestionContentDTO.getListCandidateAnswersDTO().size()).toString());
						    
						    listQuestionContentDTO=AuthoringUtil.reorderUpdateListQuestionContentDTO(listQuestionContentDTO, mcQuestionContentDTO, editableQuestionIndex);
						    logger.debug("post reorderUpdateListQuestionContentDTO listQuestionContentDTO: " + listQuestionContentDTO);			        
				  }
		    }
	        else
	        {
	            logger.debug("entry blank, not adding");
	        }
		    
		    logger.debug("entryusing mark: " + mark);
		    mcGeneralAuthoringDTO.setMarkValue(mark);
		    
		    
		    request.setAttribute(LIST_QUESTION_CONTENT_DTO,listQuestionContentDTO);
			sessionMap.put(LIST_QUESTION_CONTENT_DTO_KEY, listQuestionContentDTO);
		    logger.debug("listQuestionContentDTO now: " + listQuestionContentDTO);

		    commonSaveCode(request, mcGeneralAuthoringDTO, 
	        mcAuthoringForm, sessionMap, activeModule, strToolContentID, 
	        defaultContentIdStr,  mcService, httpSessionID,listQuestionContentDTO);

		    request.setAttribute(TOTAL_QUESTION_COUNT, new Integer(listQuestionContentDTO.size()));
		    
		    setupCommonScreenData(mcContent, mcService,request);
		    MonitoringUtil.setupAllSessionsData(request, mcContent,mcService);

		    
			logger.debug("fwd ing to LOAD_MONITORING: " + LOAD_MONITORING);
		    return (mapping.findForward(LOAD_MONITORING));
	 	}
	 	else
	 	{
	        logger.debug("errors is not empty: " + errors);
	        
		    commonSaveCode(request, mcGeneralAuthoringDTO, 
			        mcAuthoringForm, sessionMap, activeModule, strToolContentID, 
			        defaultContentIdStr,  mcService, httpSessionID,listQuestionContentDTO);

		    
		    
		    logger.debug("generating dyn pass map using listQuestionContentDTO: " +listQuestionContentDTO);
		    Map passMarksMap=authoringUtil.buildDynamicPassMarkMap(listQuestionContentDTO, false);
		    logger.debug("passMarksMap: " + passMarksMap);
		    mcGeneralAuthoringDTO.setPassMarksMap(passMarksMap);
		    
	        String totalMark=AuthoringUtil.getTotalMark(listQuestionContentDTO);
	        logger.debug("totalMark: " + totalMark);
	        mcAuthoringForm.setTotalMarks(totalMark);
	        mcGeneralAuthoringDTO.setTotalMarks(totalMark);

		    		    
	        logger.debug("before saving final mcGeneralAuthoringDTO: " + mcGeneralAuthoringDTO);
	    	request.setAttribute(MC_GENERAL_AUTHORING_DTO, mcGeneralAuthoringDTO);

	        
		    request.setAttribute(LIST_QUESTION_CONTENT_DTO,listQuestionContentDTO);
			sessionMap.put(LIST_QUESTION_CONTENT_DTO_KEY, listQuestionContentDTO);
		    logger.debug("listQuestionContentDTO now: " + listQuestionContentDTO);
		    
		    setupCommonScreenData(mcContent, mcService,request);
		    MonitoringUtil.setupAllSessionsData(request, mcContent,mcService);


		    logger.debug("forwarding using newEditableQuestionBox");
			return newEditableQuestionBox(mapping, form,  request, response);
	 	}

	}

	
	/**
	 * commonSaveCode(HttpServletRequest request, McGeneralAuthoringDTO mcGeneralAuthoringDTO, 
	        McAuthoringForm mcAuthoringForm, SessionMap sessionMap, String activeModule, String strToolContentID, 
	        String defaultContentIdStr, IMcService mcService, String httpSessionID, List listQuestionContentDTO)
	 * 
	 * @param request
	 * @param mcGeneralAuthoringDTO
	 * @param mcAuthoringForm
	 * @param sessionMap
	 * @param activeModule
	 * @param strToolContentID
	 * @param defaultContentIdStr
	 * @param mcService
	 * @param httpSessionID
	 * @param listQuestionContentDTO
	 */
	protected void commonSaveCode(HttpServletRequest request, McGeneralAuthoringDTO mcGeneralAuthoringDTO, 
	        McAuthoringForm mcAuthoringForm, SessionMap sessionMap, String activeModule, String strToolContentID, 
	        String defaultContentIdStr, IMcService mcService, String httpSessionID, List listQuestionContentDTO)
	{
		String richTextTitle = request.getParameter(TITLE);
	    String richTextInstructions = request.getParameter(INSTRUCTIONS);

	    logger.debug("richTextTitle: " + richTextTitle);
	    logger.debug("richTextInstructions: " + richTextInstructions);
	    mcGeneralAuthoringDTO.setActivityTitle(richTextTitle);
	    mcAuthoringForm.setTitle(richTextTitle);
	    
		mcGeneralAuthoringDTO.setActivityInstructions(richTextInstructions);
			
	    sessionMap.put(ACTIVITY_TITLE_KEY, richTextTitle);
	    sessionMap.put(ACTIVITY_INSTRUCTIONS_KEY, richTextInstructions);

	    mcGeneralAuthoringDTO.setEditActivityEditMode(new Boolean(true).toString());
	    
	    request.getSession().setAttribute(httpSessionID, sessionMap);
	    sessionMap.put(LIST_QUESTION_CONTENT_DTO_KEY, listQuestionContentDTO);
	    
		McUtils.setFormProperties(request, mcService,  
	             mcAuthoringForm, mcGeneralAuthoringDTO, strToolContentID, defaultContentIdStr, activeModule, sessionMap, httpSessionID);

		
		mcGeneralAuthoringDTO.setToolContentID(strToolContentID);
		mcGeneralAuthoringDTO.setHttpSessionID(httpSessionID);
		mcGeneralAuthoringDTO.setActiveModule(activeModule);
		mcGeneralAuthoringDTO.setDefaultContentIdStr(defaultContentIdStr);
		
		mcAuthoringForm.setToolContentID(strToolContentID);
		mcAuthoringForm.setHttpSessionID(httpSessionID);
		mcAuthoringForm.setActiveModule(activeModule);
		mcAuthoringForm.setDefaultContentIdStr(defaultContentIdStr);
		mcAuthoringForm.setCurrentTab("3");
	 	
		mcGeneralAuthoringDTO.setDefineLaterInEditMode(new Boolean(true).toString());

		
		AuthoringUtil authoringUtil = new AuthoringUtil();
	    Map marksMap=authoringUtil.buildMarksMap();
	    logger.debug("marksMap: " + marksMap);
	    mcGeneralAuthoringDTO.setMarksMap(marksMap);
	    
	    
	    logger.debug("generating dyn pass map using listQuestionContentDTO: " +listQuestionContentDTO);
	    Map passMarksMap=authoringUtil.buildDynamicPassMarkMap(listQuestionContentDTO, false);
	    logger.debug("passMarksMap: " + passMarksMap);
	    mcGeneralAuthoringDTO.setPassMarksMap(passMarksMap);

	    
        String totalMark=AuthoringUtil.getTotalMark(listQuestionContentDTO);
        logger.debug("totalMark: " + totalMark);
        mcAuthoringForm.setTotalMarks(totalMark);
        mcGeneralAuthoringDTO.setTotalMarks(totalMark);

	    
	    Map correctMap=authoringUtil.buildCorrectMap();
	    logger.debug("correctMap: " + correctMap);
	    mcGeneralAuthoringDTO.setCorrectMap(correctMap);
	    
	    
		logger.debug("mcGeneralAuthoringDTO now: " + mcGeneralAuthoringDTO);
		request.setAttribute(MC_GENERAL_AUTHORING_DTO, mcGeneralAuthoringDTO);
	    
	    logger.debug("httpSessionID: " + httpSessionID);
	    logger.debug("sessionMap: " + sessionMap);
	    
	    request.getSession().setAttribute(httpSessionID, sessionMap);
	    logger.debug("mcGeneralAuthoringDTO.getMapQuestionContent(); " + mcGeneralAuthoringDTO.getMapQuestionContent());
	}

	
    
    /**
     * addSingleQuestion(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
     * 
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws IOException
     * @throws ServletException
     */
	public ActionForward addSingleQuestion(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) 
	    throws IOException, ServletException {
		
		logger.debug("dispathcing addSingleQuestion");
		McAuthoringForm mcAuthoringForm = (McMonitoringForm) form;
	
		IMcService mcService =McServiceProxy.getMcService(getServlet().getServletContext());
		logger.debug("mcService: " + mcService);
		
		String httpSessionID=mcAuthoringForm.getHttpSessionID();
		logger.debug("httpSessionID: " + httpSessionID);
		
		SessionMap sessionMap=(SessionMap)request.getSession().getAttribute(httpSessionID);
		logger.debug("sessionMap: " + sessionMap);
		
		String contentFolderID = WebUtil.readStrParam(request, AttributeNames.PARAM_CONTENT_FOLDER_ID);
		logger.debug("contentFolderID: " + contentFolderID);
		mcAuthoringForm.setContentFolderID(contentFolderID);
	

	    String totalMarks=request.getParameter("totalMarks");
		logger.debug("totalMarks: " + totalMarks);

		String activeModule=request.getParameter(ACTIVE_MODULE);
		logger.debug("activeModule: " + activeModule);
	
		String strToolContentID=request.getParameter(AttributeNames.PARAM_TOOL_CONTENT_ID);
		logger.debug("strToolContentID: " + strToolContentID);
		
		String defaultContentIdStr=new Long(mcService.getToolDefaultContentIdBySignature(MY_SIGNATURE)).toString();;
		logger.debug("defaultContentIdStr: " + defaultContentIdStr);
		
		McContent mcContent=mcService.retrieveMc(new Long(strToolContentID));
		logger.debug("mcContent: " + mcContent);
	
		McGeneralAuthoringDTO mcGeneralAuthoringDTO= new McGeneralAuthoringDTO();
		logger.debug("mcGeneralAuthoringDTO: " + mcGeneralAuthoringDTO);
		mcGeneralAuthoringDTO.setContentFolderID(contentFolderID);
		
		mcGeneralAuthoringDTO.setSbmtSuccess(new Integer(0).toString());
		
	    AuthoringUtil authoringUtil= new AuthoringUtil();
	    
        List listQuestionContentDTO=(List)sessionMap.get(LIST_QUESTION_CONTENT_DTO_KEY);
        logger.debug("listQuestionContentDTO: " + listQuestionContentDTO);
        
        List listAddableQuestionContentDTO = (List)sessionMap.get(NEW_ADDABLE_QUESTION_CONTENT_KEY);
		logger.debug("listAddableQuestionContentDTO: " + listAddableQuestionContentDTO);

	    McQuestionContentDTO mcQuestionContentDTONew = null; 
	    

	    int listSize=listQuestionContentDTO.size();
	    logger.debug("listSize: " + listSize);

	    logger.debug("listAddableQuestionContentDTO now: " + listAddableQuestionContentDTO);
		request.setAttribute(NEW_ADDABLE_QUESTION_CONTENT_LIST, listAddableQuestionContentDTO);

	    String newQuestion=request.getParameter("newQuestion");
	    logger.debug("newQuestion: " + newQuestion);
	    
	    String feedback=request.getParameter("feedback");
	    logger.debug("feedback: " + feedback);
	    
		String mark=request.getParameter("mark");
		logger.debug("mark: " + mark);
	    mcGeneralAuthoringDTO.setMarkValue(mark);

	    String passmark=request.getParameter("passmark");
		logger.debug("passmark: " + passmark);
	    mcGeneralAuthoringDTO.setPassMarkValue(passmark);
	    

	    List caList=authoringUtil.repopulateCandidateAnswersBox(request, false);
	    logger.debug("repopulated caList: " + caList);
	    
	    caList=AuthoringUtil.removeBlankEntries(caList);
	    logger.debug("caList after removing blank entries: " + caList);
	    

		boolean validateSingleCorrectCandidate=authoringUtil.validateSingleCorrectCandidate(caList);
	    logger.debug("validateSingleCorrectCandidate: " + validateSingleCorrectCandidate);
	    

		boolean validateOnlyOneCorrectCandidate=authoringUtil.validateOnlyOneCorrectCandidate(caList);
	    logger.debug("validateOnlyOneCorrectCandidate: " + validateOnlyOneCorrectCandidate);

	    
	    
        ActionMessages errors = new ActionMessages();
        
        
        if (!validateSingleCorrectCandidate)
        {
            ActionMessage error = new ActionMessage("candidates.none.correct");
			errors.add(ActionMessages.GLOBAL_MESSAGE, error);
        }

        if (!validateOnlyOneCorrectCandidate)
        {
            ActionMessage error = new ActionMessage("candidates.duplicate.correct");
			errors.add(ActionMessages.GLOBAL_MESSAGE, error);
        }

        logger.debug("errors: " + errors);

	 	if(!errors.isEmpty()){
			saveErrors(request, errors);
	        logger.debug("errors saved: " + errors);
		}
	    
        logger.debug("errors saved: " + errors);
	    
	 	if(errors.isEmpty())
	 	{
		    if ((newQuestion != null) && (newQuestion.length() > 0))
		    {
		        boolean duplicates=AuthoringUtil.checkDuplicateQuestions(listQuestionContentDTO, newQuestion);
		        logger.debug("duplicates: " + duplicates);
		        
		        if (!duplicates)
		        {
				    McQuestionContentDTO mcQuestionContentDTO=new McQuestionContentDTO();
				    mcQuestionContentDTO.setDisplayOrder(new Long(listSize+1).toString());
				    mcQuestionContentDTO.setFeedback(feedback);
				    mcQuestionContentDTO.setQuestion(newQuestion);
				    mcQuestionContentDTO.setMark(mark);
				    
				    mcQuestionContentDTO.setListCandidateAnswersDTO(caList);
				    logger.debug("caList size:" + mcQuestionContentDTO.getListCandidateAnswersDTO().size());
				    mcQuestionContentDTO.setCaCount(new Integer(mcQuestionContentDTO.getListCandidateAnswersDTO().size()).toString());
				    
				    listQuestionContentDTO.add(mcQuestionContentDTO);
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
	 	}
	 	else
	 	{
	        logger.debug("errors, not adding");
	        
	        logger.debug("errors is not empty: " + errors);
	        
		    commonSaveCode(request, mcGeneralAuthoringDTO, 
			        mcAuthoringForm, sessionMap, activeModule, strToolContentID, 
			        defaultContentIdStr,  mcService, httpSessionID,listQuestionContentDTO);

		    
		    
		    logger.debug("generating dyn pass map using listQuestionContentDTO: " +listQuestionContentDTO);
		    Map passMarksMap=authoringUtil.buildDynamicPassMarkMap(listQuestionContentDTO, false);
		    logger.debug("passMarksMap: " + passMarksMap);
		    mcGeneralAuthoringDTO.setPassMarksMap(passMarksMap);
		    
	        String totalMark=AuthoringUtil.getTotalMark(listQuestionContentDTO);
	        logger.debug("totalMark: " + totalMark);
	        mcAuthoringForm.setTotalMarks(totalMark);
	        mcGeneralAuthoringDTO.setTotalMarks(totalMark);

		    		    
	        logger.debug("before saving final mcGeneralAuthoringDTO: " + mcGeneralAuthoringDTO);
	    	request.setAttribute(MC_GENERAL_AUTHORING_DTO, mcGeneralAuthoringDTO);

	        
		    request.setAttribute(LIST_QUESTION_CONTENT_DTO,listQuestionContentDTO);
			sessionMap.put(LIST_QUESTION_CONTENT_DTO_KEY, listQuestionContentDTO);
		    logger.debug("listQuestionContentDTO now: " + listQuestionContentDTO);

		    logger.debug("forwarding using newQuestionBox");
			return newQuestionBox(mapping, form,  request, response);
	 	}
	    
	    
	    logger.debug("entry using mark: " + mark);
	    mcGeneralAuthoringDTO.setMarkValue(mark);
	    
	    logger.debug("generating dyn pass map using listQuestionContentDTO: " +listQuestionContentDTO);
	    Map passMarksMap=authoringUtil.buildDynamicPassMarkMap(listQuestionContentDTO, false);
	    logger.debug("passMarksMap: " + passMarksMap);
	    mcGeneralAuthoringDTO.setPassMarksMap(passMarksMap);
	    
	    
        String totalMark=AuthoringUtil.getTotalMark(listQuestionContentDTO);
        logger.debug("totalMark: " + totalMark);
        mcAuthoringForm.setTotalMarks(totalMark);
        mcGeneralAuthoringDTO.setTotalMarks(totalMark);


	    logger.debug("newQuestion: " + newQuestion);
	    mcGeneralAuthoringDTO.setEditableQuestionText(newQuestion);
	    
	    logger.debug("feedback: " + feedback);
	    mcAuthoringForm.setFeedback(feedback);
	    

		logger.debug("mark: " + mark);
	    mcGeneralAuthoringDTO.setMarkValue(mark);

        
        logger.debug("before saving final mcGeneralAuthoringDTO: " + mcGeneralAuthoringDTO);
    	request.setAttribute(MC_GENERAL_AUTHORING_DTO, mcGeneralAuthoringDTO);

	    request.setAttribute(LIST_QUESTION_CONTENT_DTO,listQuestionContentDTO);
		sessionMap.put(LIST_QUESTION_CONTENT_DTO_KEY, listQuestionContentDTO);
	    logger.debug("listQuestionContentDTO now: " + listQuestionContentDTO);

	    commonSaveCode(request, mcGeneralAuthoringDTO, 
        mcAuthoringForm, sessionMap, activeModule, strToolContentID, 
        defaultContentIdStr,  mcService, httpSessionID,listQuestionContentDTO);

	    request.setAttribute(TOTAL_QUESTION_COUNT, new Integer(listQuestionContentDTO.size()));
	    
        logger.debug("before forwarding mcGeneralAuthoringDTO: " + mcGeneralAuthoringDTO);
        
	    setupCommonScreenData(mcContent, mcService,request);
	    
	    MonitoringUtil.setupAllSessionsData(request, mcContent,mcService);

	    
		logger.debug("fwd LOAD_MONITORING");
	    return (mapping.findForward(LOAD_MONITORING));
 	}
	
    
    /**
     * newQuestionBox(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
     * 
     * opens up an new screen within the current page for adding a new question
     * 
     * newQuestionBox
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws IOException
     * @throws ServletException
     */
    public ActionForward newQuestionBox(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) 
    throws IOException, ServletException 
    {
        logger.debug("dispathcing newQuestionBox");
		McAuthoringForm mcAuthoringForm = (McMonitoringForm) form;
		
		IMcService mcService =McServiceProxy.getMcService(getServlet().getServletContext());
		logger.debug("mcService: " + mcService);
		
		String httpSessionID=mcAuthoringForm.getHttpSessionID();
		logger.debug("httpSessionID: " + httpSessionID);
		
		SessionMap sessionMap=(SessionMap)request.getSession().getAttribute(httpSessionID);
		logger.debug("sessionMap: " + sessionMap);
		
		String contentFolderID = WebUtil.readStrParam(request, AttributeNames.PARAM_CONTENT_FOLDER_ID);
		logger.debug("contentFolderID: " + contentFolderID);
		mcAuthoringForm.setContentFolderID(contentFolderID);

	    String totalMarks=request.getParameter("totalMarks");
		logger.debug("totalMarks: " + totalMarks);

		
		String activeModule=request.getParameter(ACTIVE_MODULE);
		logger.debug("activeModule: " + activeModule);

		String strToolContentID=request.getParameter(AttributeNames.PARAM_TOOL_CONTENT_ID);
		logger.debug("strToolContentID: " + strToolContentID);
		
		String defaultContentIdStr=new Long(mcService.getToolDefaultContentIdBySignature(MY_SIGNATURE)).toString();;
		logger.debug("defaultContentIdStr: " + defaultContentIdStr);
		
		
		/* create default mcContent object*/
		McContent mcContent=mcService.retrieveMc(new Long(defaultContentIdStr));
		logger.debug("mcContent: " + mcContent);
		
		McGeneralAuthoringDTO mcGeneralAuthoringDTO= new McGeneralAuthoringDTO();
		logger.debug("mcGeneralAuthoringDTO: " + mcGeneralAuthoringDTO);
		mcGeneralAuthoringDTO.setContentFolderID(contentFolderID);

		String richTextTitle = request.getParameter(TITLE);
	    String richTextInstructions = request.getParameter(INSTRUCTIONS);
	
	    logger.debug("richTextTitle: " + richTextTitle);
	    logger.debug("richTextInstructions: " + richTextInstructions);
	    mcGeneralAuthoringDTO.setActivityTitle(richTextTitle);
	    mcAuthoringForm.setTitle(richTextTitle);
	    
		mcGeneralAuthoringDTO.setActivityInstructions(richTextInstructions);
			
		
		McUtils.setFormProperties(request, mcService,  
	             mcAuthoringForm, mcGeneralAuthoringDTO, strToolContentID, defaultContentIdStr, activeModule, sessionMap, httpSessionID);

		
	    mcGeneralAuthoringDTO.setDefineLaterInEditMode(new Boolean(true).toString());

	    AuthoringUtil authoringUtil = new AuthoringUtil();
	    Map marksMap=authoringUtil.buildMarksMap();
	    logger.debug("marksMap: " + marksMap);
	    mcGeneralAuthoringDTO.setMarksMap(marksMap);

	    
        List listQuestionContentDTO=(List)sessionMap.get(LIST_QUESTION_CONTENT_DTO_KEY);
        logger.debug("listQuestionContentDTO: " + listQuestionContentDTO);


	    Map correctMap=authoringUtil.buildCorrectMap();
	    logger.debug("correctMap: " + correctMap);
	    mcGeneralAuthoringDTO.setCorrectMap(correctMap);

	    
		logger.debug("mcGeneralAuthoringDTO now: " + mcGeneralAuthoringDTO);
		request.setAttribute(MC_GENERAL_AUTHORING_DTO, mcGeneralAuthoringDTO);

		String requestType=request.getParameter("requestType");
		logger.debug("requestType: " + requestType);
		
		
		List listAddableQuestionContentDTO = (List)sessionMap.get(NEW_ADDABLE_QUESTION_CONTENT_KEY);
		logger.debug("listAddableQuestionContentDTO: " + listAddableQuestionContentDTO);
		
		if ((requestType != null) && (requestType.equals("direct")))
		{
			logger.debug("requestType is direct");
			listAddableQuestionContentDTO=authoringUtil.buildDefaultQuestionContent(mcContent, mcService);
			logger.debug("listAddableQuestionContentDTO from db: " + listAddableQuestionContentDTO);		    
		}
		    
		request.setAttribute(NEW_ADDABLE_QUESTION_CONTENT_LIST, listAddableQuestionContentDTO);
		sessionMap.put(NEW_ADDABLE_QUESTION_CONTENT_KEY, listAddableQuestionContentDTO);

		
	    logger.debug("listQuestionContentDTO: " + listQuestionContentDTO);

	    request.setAttribute(TOTAL_QUESTION_COUNT, new Integer(listQuestionContentDTO.size()));

	    logger.debug("generating dyn pass map using listQuestionContentDTO: " +listQuestionContentDTO);
	    Map passMarksMap=authoringUtil.buildDynamicPassMarkMap(listQuestionContentDTO, false);
	    logger.debug("passMarksMap: " + passMarksMap);
	    mcGeneralAuthoringDTO.setPassMarksMap(passMarksMap);
	    
	    
	    String newQuestion=request.getParameter("newQuestion");
	    logger.debug("newQuestion: " + newQuestion);
	    mcGeneralAuthoringDTO.setEditableQuestionText(newQuestion);
	    
	    String feedback=request.getParameter("feedback");
	    logger.debug("feedback: " + feedback);
	    mcAuthoringForm.setFeedback(feedback);
	    
		String mark=request.getParameter("mark");
		logger.debug("mark: " + mark);
	    mcGeneralAuthoringDTO.setMarkValue(mark);

	    
        logger.debug("before saving final mcGeneralAuthoringDTO: " + mcGeneralAuthoringDTO);
    	request.setAttribute(MC_GENERAL_AUTHORING_DTO, mcGeneralAuthoringDTO);

	    
        String totalMark=AuthoringUtil.getTotalMark(listQuestionContentDTO);
        logger.debug("totalMark: " + totalMark);
        mcAuthoringForm.setTotalMarks(totalMark);
        mcGeneralAuthoringDTO.setTotalMarks(totalMark);

		logger.debug("final listQuestionContentDTO: " + listQuestionContentDTO);
		request.setAttribute(LIST_QUESTION_CONTENT_DTO,listQuestionContentDTO);
		
		mcGeneralAuthoringDTO.setToolContentID(strToolContentID);
		mcGeneralAuthoringDTO.setHttpSessionID(httpSessionID);
		mcGeneralAuthoringDTO.setActiveModule(activeModule);
		mcGeneralAuthoringDTO.setDefaultContentIdStr(defaultContentIdStr);
		
		mcAuthoringForm.setToolContentID(strToolContentID);
		mcAuthoringForm.setHttpSessionID(httpSessionID);
		mcAuthoringForm.setActiveModule(activeModule);
		mcAuthoringForm.setDefaultContentIdStr(defaultContentIdStr);
		
		setupCommonScreenData(mcContent, mcService,request);
		
		MonitoringUtil.setupAllSessionsData(request, mcContent,mcService);
		
		logger.debug("fwd ing to newQuestionBox: ");
        return (mapping.findForward("newQuestionBox"));
    }


    /**
     * newEditableQuestionBox(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
     * 
     * opens up an new screen within the current page for editing a question
     * 
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws IOException
     * @throws ServletException
     */
    public ActionForward newEditableQuestionBox(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) 
    throws IOException, ServletException 
    {
        logger.debug("dispathcing newEditableQuestionBox");
		McAuthoringForm mcAuthoringForm = (McMonitoringForm) form;
		
		IMcService mcService =McServiceProxy.getMcService(getServlet().getServletContext());
		logger.debug("mcService: " + mcService);
		
		String httpSessionID=mcAuthoringForm.getHttpSessionID();
		logger.debug("httpSessionID: " + httpSessionID);

	    String totalMarks=request.getParameter("totalMarks");
		logger.debug("totalMarks: " + totalMarks);

		
		SessionMap sessionMap=(SessionMap)request.getSession().getAttribute(httpSessionID);
		logger.debug("sessionMap: " + sessionMap);

		
		String questionIndex=request.getParameter("questionIndex");
		logger.debug("questionIndex: " + questionIndex);
		mcAuthoringForm.setQuestionIndex(questionIndex);
		
		request.setAttribute(CURRENT_EDITABLE_QUESTION_INDEX,questionIndex); 
		
		mcAuthoringForm.setEditableQuestionIndex(questionIndex);
		
        List listQuestionContentDTO=(List)sessionMap.get(LIST_QUESTION_CONTENT_DTO_KEY);
        logger.debug("listQuestionContentDTO: " + listQuestionContentDTO);

        String editableQuestion="";
        String editableFeedback="";
        String editableMark="";
	    Iterator listIterator=listQuestionContentDTO.iterator();
	    while (listIterator.hasNext())
	    {
	        McQuestionContentDTO mcQuestionContentDTO= (McQuestionContentDTO)listIterator.next();
	        logger.debug("mcQuestionContentDTO:" + mcQuestionContentDTO);
	        logger.debug("mcQuestionContentDTO question:" + mcQuestionContentDTO.getQuestion());
	        String question=mcQuestionContentDTO.getQuestion();
	        String displayOrder=mcQuestionContentDTO.getDisplayOrder();
	        
	        if ((displayOrder != null) && (!displayOrder.equals("")))
    		{
	            if (displayOrder.equals(questionIndex))
	            {
	                editableFeedback=mcQuestionContentDTO.getFeedback();
	                editableQuestion=mcQuestionContentDTO.getQuestion();
	                editableMark=mcQuestionContentDTO.getMark();
	                logger.debug("editableFeedback found :" + editableFeedback);
	                
	                List candidates=mcQuestionContentDTO.getListCandidateAnswersDTO();
	                logger.debug("candidates found :" + candidates);
	                
	                break;
	            }
	            
    		}
	    }
	    logger.debug("editableFeedback found :" + editableFeedback);
	    logger.debug("editableQuestion found :" + editableQuestion);
	    logger.debug("editableMark found :" + editableMark);

	    String contentFolderID = WebUtil.readStrParam(request, AttributeNames.PARAM_CONTENT_FOLDER_ID);
		logger.debug("contentFolderID: " + contentFolderID);
		mcAuthoringForm.setContentFolderID(contentFolderID);


		String activeModule=request.getParameter(ACTIVE_MODULE);
		logger.debug("activeModule: " + activeModule);

		String strToolContentID=request.getParameter(AttributeNames.PARAM_TOOL_CONTENT_ID);
		logger.debug("strToolContentID: " + strToolContentID);
		
		String defaultContentIdStr=new Long(mcService.getToolDefaultContentIdBySignature(MY_SIGNATURE)).toString();;
		logger.debug("defaultContentIdStr: " + defaultContentIdStr);
		
		McContent mcContent=mcService.retrieveMc(new Long(strToolContentID));
		logger.debug("mcContent: " + mcContent);
		

		McGeneralAuthoringDTO mcGeneralAuthoringDTO= new McGeneralAuthoringDTO();
		
		mcGeneralAuthoringDTO.setMarkValue(editableMark);
		
		logger.debug("mcGeneralAuthoringDTO: " + mcGeneralAuthoringDTO);
		mcGeneralAuthoringDTO.setContentFolderID(contentFolderID);

		
		String richTextTitle = request.getParameter(TITLE);
	    String richTextInstructions = request.getParameter(INSTRUCTIONS);
	
	    logger.debug("richTextTitle: " + richTextTitle);
	    logger.debug("richTextInstructions: " + richTextInstructions);
	    mcGeneralAuthoringDTO.setActivityTitle(richTextTitle);
	    mcAuthoringForm.setTitle(richTextTitle);
	    
		mcGeneralAuthoringDTO.setActivityInstructions(richTextInstructions);
			
		McUtils.setFormProperties(request, mcService,  
	             mcAuthoringForm, mcGeneralAuthoringDTO, strToolContentID, defaultContentIdStr, activeModule, sessionMap, httpSessionID);
		
		mcGeneralAuthoringDTO.setEditableQuestionText(editableQuestion);
		mcGeneralAuthoringDTO.setEditableQuestionFeedback (editableFeedback);
		mcAuthoringForm.setFeedback(editableFeedback);
		
	    mcGeneralAuthoringDTO.setDefineLaterInEditMode(new Boolean(true).toString());

	    AuthoringUtil authoringUtil = new AuthoringUtil();
	    Map marksMap=authoringUtil.buildMarksMap();
	    logger.debug("marksMap: " + marksMap);
	    mcGeneralAuthoringDTO.setMarksMap(marksMap);

	    Map correctMap=authoringUtil.buildCorrectMap();
	    logger.debug("correctMap: " + correctMap);
	    mcGeneralAuthoringDTO.setCorrectMap(correctMap);

	    
	    logger.debug("mcGeneralAuthoringDTO now: " + mcGeneralAuthoringDTO);
		request.setAttribute(MC_GENERAL_AUTHORING_DTO, mcGeneralAuthoringDTO);
		
		request.setAttribute(TOTAL_QUESTION_COUNT, new Integer(listQuestionContentDTO.size()));
		
	    logger.debug("generating dyn pass map using listQuestionContentDTO: " +listQuestionContentDTO);
	    Map passMarksMap=authoringUtil.buildDynamicPassMarkMap(listQuestionContentDTO, false);
	    logger.debug("passMarksMap: " + passMarksMap);
	    mcGeneralAuthoringDTO.setPassMarksMap(passMarksMap);
	    
	    
        String totalMark=AuthoringUtil.getTotalMark(listQuestionContentDTO);
        logger.debug("totalMark: " + totalMark);
        mcAuthoringForm.setTotalMarks(totalMark);
        mcGeneralAuthoringDTO.setTotalMarks(totalMark);

        /*
	    String newQuestion=request.getParameter("newQuestion");
	    logger.debug("newQuestion: " + newQuestion);
	    mcGeneralAuthoringDTO.setEditableQuestionText(newQuestion);
	    
	    String feedback=request.getParameter("feedback");
	    logger.debug("feedback: " + feedback);
	    mcAuthoringForm.setFeedback(feedback);
	    
		String mark=request.getParameter("mark");
		logger.debug("mark: " + mark);
	    mcGeneralAuthoringDTO.setMarkValue(mark);
	    */
        

        logger.debug("before saving final mcGeneralAuthoringDTO: " + mcGeneralAuthoringDTO);
    	request.setAttribute(MC_GENERAL_AUTHORING_DTO, mcGeneralAuthoringDTO);

	    
	    logger.debug("final listQuestionContentDTO: " + listQuestionContentDTO);
	    request.setAttribute(LIST_QUESTION_CONTENT_DTO,listQuestionContentDTO);
		
	    mcGeneralAuthoringDTO.setToolContentID(strToolContentID);
		mcGeneralAuthoringDTO.setHttpSessionID(httpSessionID);
		mcGeneralAuthoringDTO.setActiveModule(activeModule);
		mcGeneralAuthoringDTO.setDefaultContentIdStr(defaultContentIdStr);
		
		mcAuthoringForm.setToolContentID(strToolContentID);
		mcAuthoringForm.setHttpSessionID(httpSessionID);
		mcAuthoringForm.setActiveModule(activeModule);
		mcAuthoringForm.setDefaultContentIdStr(defaultContentIdStr);
		
		setupCommonScreenData(mcContent, mcService,request);
		
		MonitoringUtil.setupAllSessionsData(request, mcContent,mcService);

	    logger.debug("fwd ing to editQuestionBox: ");
        return (mapping.findForward("editQuestionBox"));
    }

    
    
    /**
     * 
     * ActionForward removeQuestion(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) 
        throws IOException, ServletException
        
        removes a question from the questions map
     * 
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws IOException
     * @throws ServletException
     */
    public ActionForward removeQuestion(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) 
        throws IOException, ServletException {
    	logger.debug("dispatching removeQuestion");
    	McAuthoringForm mcAuthoringForm = (McMonitoringForm) form;
    	
		IMcService mcService =McServiceProxy.getMcService(getServlet().getServletContext());
		logger.debug("mcService: " + mcService);

		String httpSessionID=mcAuthoringForm.getHttpSessionID();
		logger.debug("httpSessionID: " + httpSessionID);
		
		SessionMap sessionMap=(SessionMap)request.getSession().getAttribute(httpSessionID);
		logger.debug("sessionMap: " + sessionMap);
		
		String questionIndex=request.getParameter("questionIndex");
		logger.debug("questionIndex: " + questionIndex);
		mcAuthoringForm.setQuestionIndex(questionIndex);
		
	    String totalMarks=request.getParameter("totalMarks");
		logger.debug("totalMarks: " + totalMarks);


		List listQuestionContentDTO=(List)sessionMap.get(LIST_QUESTION_CONTENT_DTO_KEY);
        logger.debug("listQuestionContentDTO: " + listQuestionContentDTO);

	    McQuestionContentDTO mcQuestionContentDTO= null;
	    Iterator listIterator=listQuestionContentDTO.iterator();
	    while (listIterator.hasNext())
	    {
	        mcQuestionContentDTO= (McQuestionContentDTO)listIterator.next();
	        logger.debug("mcQuestionContentDTO:" + mcQuestionContentDTO);
	        logger.debug("mcQuestionContentDTO question:" + mcQuestionContentDTO.getQuestion());
	        
	        String question=mcQuestionContentDTO.getQuestion();
	        String displayOrder=mcQuestionContentDTO.getDisplayOrder();
	        logger.debug("displayOrder:" + displayOrder);
	        
	        if ((displayOrder != null) && (!displayOrder.equals("")))
    		{
	            if (displayOrder.equals(questionIndex))
	            {
	                break;
	            }
	            
    		}
	    }
	    
	    logger.debug("mcQuestionContentDTO found:" + mcQuestionContentDTO);
	    mcQuestionContentDTO.setQuestion("");
	    logger.debug("listQuestionContentDTO after remove:" + listQuestionContentDTO);
	  
	    
	    listQuestionContentDTO=AuthoringUtil.reorderListQuestionContentDTO(listQuestionContentDTO, questionIndex );
	    logger.debug("listQuestionContentDTO reordered:" + listQuestionContentDTO);
        

	    sessionMap.put(LIST_QUESTION_CONTENT_DTO_KEY, listQuestionContentDTO);
	    
		String contentFolderID = WebUtil.readStrParam(request, AttributeNames.PARAM_CONTENT_FOLDER_ID);
		logger.debug("contentFolderID: " + contentFolderID);
		mcAuthoringForm.setContentFolderID(contentFolderID);

		
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
		
		String defaultContentIdStr=new Long(mcService.getToolDefaultContentIdBySignature(MY_SIGNATURE)).toString();;
		logger.debug("defaultContentIdStr: " + defaultContentIdStr);
		
		McContent mcContent=mcService.retrieveMc(new Long(strToolContentID));
		logger.debug("mcContent: " + mcContent);
		
		if (mcContent == null)
		{
			logger.debug("using defaultContentIdStr: " + defaultContentIdStr);
			mcContent=mcService.retrieveMc(new Long(defaultContentIdStr));
		}
		logger.debug("final mcContent: " + mcContent);

		McGeneralAuthoringDTO mcGeneralAuthoringDTO= new McGeneralAuthoringDTO();
		logger.debug("mcGeneralAuthoringDTO: " + mcGeneralAuthoringDTO);
		mcGeneralAuthoringDTO.setContentFolderID(contentFolderID);
		
        mcGeneralAuthoringDTO.setActivityTitle(richTextTitle);
        mcAuthoringForm.setTitle(richTextTitle);
        
  		mcGeneralAuthoringDTO.setActivityInstructions(richTextInstructions);

		AuthoringUtil authoringUtil= new AuthoringUtil();
        
        mcGeneralAuthoringDTO.setEditActivityEditMode(new Boolean(true).toString());
        
        request.getSession().setAttribute(httpSessionID, sessionMap);

		McUtils.setFormProperties(request, mcService,  
	             mcAuthoringForm, mcGeneralAuthoringDTO, strToolContentID, defaultContentIdStr, activeModule, sessionMap, httpSessionID);

		
		mcGeneralAuthoringDTO.setToolContentID(strToolContentID);
		mcGeneralAuthoringDTO.setHttpSessionID(httpSessionID);
		mcGeneralAuthoringDTO.setActiveModule(activeModule);
		mcGeneralAuthoringDTO.setDefaultContentIdStr(defaultContentIdStr);
		mcAuthoringForm.setToolContentID(strToolContentID);
		mcAuthoringForm.setHttpSessionID(httpSessionID);
		mcAuthoringForm.setActiveModule(activeModule);
		mcAuthoringForm.setDefaultContentIdStr(defaultContentIdStr);
		mcAuthoringForm.setCurrentTab("3");

	    request.setAttribute(LIST_QUESTION_CONTENT_DTO,listQuestionContentDTO);
		logger.debug("mcQuestionContentDTO now: " + mcQuestionContentDTO);
	    logger.debug("listQuestionContentDTO now: " + listQuestionContentDTO);
	
	    mcGeneralAuthoringDTO.setDefineLaterInEditMode(new Boolean(true).toString());

	    Map marksMap=authoringUtil.buildMarksMap();
	    logger.debug("marksMap: " + marksMap);
	    mcGeneralAuthoringDTO.setMarksMap(marksMap);

	    logger.debug("generating dyn pass map using listQuestionContentDTO: " +listQuestionContentDTO);
	    Map passMarksMap=authoringUtil.buildDynamicPassMarkMap(listQuestionContentDTO, false);
	    logger.debug("passMarksMap: " + passMarksMap);
	    mcGeneralAuthoringDTO.setPassMarksMap(passMarksMap);

	    
	    
        String totalMark=AuthoringUtil.getTotalMark(listQuestionContentDTO);
        logger.debug("totalMark: " + totalMark);
        mcAuthoringForm.setTotalMarks(totalMark);
        mcGeneralAuthoringDTO.setTotalMarks(totalMark);

	    Map correctMap=authoringUtil.buildCorrectMap();
	    logger.debug("correctMap: " + correctMap);
	    mcGeneralAuthoringDTO.setCorrectMap(correctMap);
	    
		logger.debug("before saving final mcGeneralAuthoringDTO: " + mcGeneralAuthoringDTO);
		request.setAttribute(MC_GENERAL_AUTHORING_DTO, mcGeneralAuthoringDTO);

		request.setAttribute(TOTAL_QUESTION_COUNT, new Integer(listQuestionContentDTO.size()));
		
	
		setupCommonScreenData(mcContent, mcService,request);
		
		MonitoringUtil.setupAllSessionsData(request, mcContent,mcService);		
		logger.debug("fwd ing to LOAD_MONITORING: " + LOAD_MONITORING);
        return (mapping.findForward(LOAD_MONITORING));
    }
    
    
    /**
     * moveQuestionDown(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
     * 
     * moves a question down in the list
     * 
     * moveQuestionDown
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws IOException
     * @throws ServletException
     */
    public ActionForward moveQuestionDown(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) 
    throws IOException, ServletException {
		logger.debug("dispatching moveQuestionDown");
		McAuthoringForm mcAuthoringForm = (McMonitoringForm) form;
		
		IMcService mcService =McServiceProxy.getMcService(getServlet().getServletContext());
		logger.debug("mcService: " + mcService);
	
		String httpSessionID=mcAuthoringForm.getHttpSessionID();
		logger.debug("httpSessionID: " + httpSessionID);
		
		SessionMap sessionMap=(SessionMap)request.getSession().getAttribute(httpSessionID);
		logger.debug("sessionMap: " + sessionMap);
		
		String questionIndex=request.getParameter("questionIndex");
		logger.debug("questionIndex: " + questionIndex);
		mcAuthoringForm.setQuestionIndex(questionIndex);
		
	    String totalMarks=request.getParameter("totalMarks");
		logger.debug("totalMarks: " + totalMarks);

	    
	    List listQuestionContentDTO=(List)sessionMap.get(LIST_QUESTION_CONTENT_DTO_KEY);
	    logger.debug("listQuestionContentDTO: " + listQuestionContentDTO);
	    
	    listQuestionContentDTO=AuthoringUtil.swapNodes(listQuestionContentDTO, questionIndex, "down");
	    logger.debug("listQuestionContentDTO after swap: " + listQuestionContentDTO);
	    
	    listQuestionContentDTO=AuthoringUtil.reorderSimpleListQuestionContentDTO(listQuestionContentDTO);
	    logger.debug("listQuestionContentDTO after reordersimple: " + listQuestionContentDTO);

	    
	    sessionMap.put(LIST_QUESTION_CONTENT_DTO_KEY, listQuestionContentDTO);
	    
	    
		String contentFolderID = WebUtil.readStrParam(request, AttributeNames.PARAM_CONTENT_FOLDER_ID);
		logger.debug("contentFolderID: " + contentFolderID);
		mcAuthoringForm.setContentFolderID(contentFolderID);
	
		
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
		
		String defaultContentIdStr=new Long(mcService.getToolDefaultContentIdBySignature(MY_SIGNATURE)).toString();;
		logger.debug("defaultContentIdStr: " + defaultContentIdStr);
		
		McContent mcContent=mcService.retrieveMc(new Long(strToolContentID));
		logger.debug("mcContent: " + mcContent);

		McGeneralAuthoringDTO mcGeneralAuthoringDTO= new McGeneralAuthoringDTO();
		logger.debug("mcGeneralAuthoringDTO: " + mcGeneralAuthoringDTO);
		mcGeneralAuthoringDTO.setContentFolderID(contentFolderID);
		
	    mcGeneralAuthoringDTO.setActivityTitle(richTextTitle);
	    mcAuthoringForm.setTitle(richTextTitle);
	    
		mcGeneralAuthoringDTO.setActivityInstructions(richTextInstructions);
	
		AuthoringUtil authoringUtil= new AuthoringUtil();
	    
	    mcGeneralAuthoringDTO.setEditActivityEditMode(new Boolean(true).toString());
	    
	    request.getSession().setAttribute(httpSessionID, sessionMap);
	
		McUtils.setFormProperties(request, mcService,  
	             mcAuthoringForm, mcGeneralAuthoringDTO, strToolContentID, defaultContentIdStr, activeModule, sessionMap, httpSessionID);
	
		
		mcGeneralAuthoringDTO.setToolContentID(strToolContentID);
		mcGeneralAuthoringDTO.setHttpSessionID(httpSessionID);
		mcGeneralAuthoringDTO.setActiveModule(activeModule);
		mcGeneralAuthoringDTO.setDefaultContentIdStr(defaultContentIdStr);
		mcAuthoringForm.setToolContentID(strToolContentID);
		mcAuthoringForm.setHttpSessionID(httpSessionID);
		mcAuthoringForm.setActiveModule(activeModule);
		mcAuthoringForm.setDefaultContentIdStr(defaultContentIdStr);
		mcAuthoringForm.setCurrentTab("3");
	
	    request.setAttribute(LIST_QUESTION_CONTENT_DTO,listQuestionContentDTO);
		logger.debug("listQuestionContentDTO now: " + listQuestionContentDTO);
	
		mcGeneralAuthoringDTO.setDefineLaterInEditMode(new Boolean(true).toString());

	    Map marksMap=authoringUtil.buildMarksMap();
	    logger.debug("marksMap: " + marksMap);
	    mcGeneralAuthoringDTO.setMarksMap(marksMap);

	    logger.debug("generating dyn pass map using listQuestionContentDTO: " +listQuestionContentDTO);
	    Map passMarksMap=authoringUtil.buildDynamicPassMarkMap(listQuestionContentDTO, false);
	    logger.debug("passMarksMap: " + passMarksMap);
	    mcGeneralAuthoringDTO.setPassMarksMap(passMarksMap);

        String totalMark=AuthoringUtil.getTotalMark(listQuestionContentDTO);
        logger.debug("totalMark: " + totalMark);
        mcAuthoringForm.setTotalMarks(totalMark);
        mcGeneralAuthoringDTO.setTotalMarks(totalMark);

	    
	    Map correctMap=authoringUtil.buildCorrectMap();
	    logger.debug("correctMap: " + correctMap);
	    mcGeneralAuthoringDTO.setCorrectMap(correctMap);
	    
		logger.debug("before saving final mcGeneralAuthoringDTO: " + mcGeneralAuthoringDTO);
		request.setAttribute(MC_GENERAL_AUTHORING_DTO, mcGeneralAuthoringDTO);
	
		request.setAttribute(TOTAL_QUESTION_COUNT, new Integer(listQuestionContentDTO.size()));
		
		setupCommonScreenData(mcContent, mcService,request);
		
		MonitoringUtil.setupAllSessionsData(request, mcContent,mcService);
		logger.debug("fwd ing to LOAD_MONITORING: " + LOAD_MONITORING);
	    return (mapping.findForward(LOAD_MONITORING));
    }

    
    /**
     * moveQuestionUp(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
     *
     *  moveQuestionUp(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)     * 
     * 
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws IOException
     * @throws ServletException
     */
    public ActionForward moveQuestionUp(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) 
    throws IOException, ServletException {
		logger.debug("dispatching moveQuestionUp");
		McAuthoringForm mcAuthoringForm = (McMonitoringForm) form;
		
		IMcService mcService =McServiceProxy.getMcService(getServlet().getServletContext());
		logger.debug("mcService: " + mcService);
	
		String httpSessionID=mcAuthoringForm.getHttpSessionID();
		logger.debug("httpSessionID: " + httpSessionID);
		
		SessionMap sessionMap=(SessionMap)request.getSession().getAttribute(httpSessionID);
		logger.debug("sessionMap: " + sessionMap);
		
		String questionIndex=request.getParameter("questionIndex");
		logger.debug("questionIndex: " + questionIndex);
		mcAuthoringForm.setQuestionIndex(questionIndex);
		
	    
	    List listQuestionContentDTO=(List)sessionMap.get(LIST_QUESTION_CONTENT_DTO_KEY);
	    logger.debug("listQuestionContentDTO: " + listQuestionContentDTO);
	    
	    listQuestionContentDTO=AuthoringUtil.swapNodes(listQuestionContentDTO, questionIndex, "up");
	    logger.debug("listQuestionContentDTO after swap: " + listQuestionContentDTO);	    
	    
	    listQuestionContentDTO=AuthoringUtil.reorderSimpleListQuestionContentDTO(listQuestionContentDTO);
	    logger.debug("listQuestionContentDTO after reordersimple: " + listQuestionContentDTO);
		    
	    sessionMap.put(LIST_QUESTION_CONTENT_DTO_KEY, listQuestionContentDTO);
	    
		String contentFolderID = WebUtil.readStrParam(request, AttributeNames.PARAM_CONTENT_FOLDER_ID);
		logger.debug("contentFolderID: " + contentFolderID);
		mcAuthoringForm.setContentFolderID(contentFolderID);
	
		
		String activeModule=request.getParameter(ACTIVE_MODULE);
		logger.debug("activeModule: " + activeModule);
	
		String richTextTitle = request.getParameter(TITLE);
		logger.debug("richTextTitle: " + richTextTitle);
		
	    String totalMarks=request.getParameter("totalMarks");
		logger.debug("totalMarks: " + totalMarks);

	    
		String richTextInstructions = request.getParameter(INSTRUCTIONS);
	    logger.debug("richTextInstructions: " + richTextInstructions);
	    
		
	    sessionMap.put(ACTIVITY_TITLE_KEY, richTextTitle);
	    sessionMap.put(ACTIVITY_INSTRUCTIONS_KEY, richTextInstructions);
	
	
		String strToolContentID=request.getParameter(AttributeNames.PARAM_TOOL_CONTENT_ID);
		logger.debug("strToolContentID: " + strToolContentID);
		
		String defaultContentIdStr=new Long(mcService.getToolDefaultContentIdBySignature(MY_SIGNATURE)).toString();;
		logger.debug("defaultContentIdStr: " + defaultContentIdStr);
		
		McContent mcContent=mcService.retrieveMc(new Long(strToolContentID));
		logger.debug("mcContent: " + mcContent);
		
		McGeneralAuthoringDTO mcGeneralAuthoringDTO= new McGeneralAuthoringDTO();
		logger.debug("mcGeneralAuthoringDTO: " + mcGeneralAuthoringDTO);
		mcGeneralAuthoringDTO.setContentFolderID(contentFolderID);
		
	    mcGeneralAuthoringDTO.setActivityTitle(richTextTitle);
	    mcAuthoringForm.setTitle(richTextTitle);
	    
		mcGeneralAuthoringDTO.setActivityInstructions(richTextInstructions);
	    
		AuthoringUtil authoringUtil= new AuthoringUtil();
	    
	    mcGeneralAuthoringDTO.setEditActivityEditMode(new Boolean(true).toString());
	    
	    request.getSession().setAttribute(httpSessionID, sessionMap);
	
		McUtils.setFormProperties(request, mcService,  
	             mcAuthoringForm, mcGeneralAuthoringDTO, strToolContentID, defaultContentIdStr, activeModule, sessionMap, httpSessionID);
	
		
		mcGeneralAuthoringDTO.setToolContentID(strToolContentID);
		mcGeneralAuthoringDTO.setHttpSessionID(httpSessionID);
		mcGeneralAuthoringDTO.setActiveModule(activeModule);
		mcGeneralAuthoringDTO.setDefaultContentIdStr(defaultContentIdStr);
		mcAuthoringForm.setToolContentID(strToolContentID);
		mcAuthoringForm.setHttpSessionID(httpSessionID);
		mcAuthoringForm.setActiveModule(activeModule);
		mcAuthoringForm.setDefaultContentIdStr(defaultContentIdStr);
		mcAuthoringForm.setCurrentTab("3");
	
	    request.setAttribute(LIST_QUESTION_CONTENT_DTO,listQuestionContentDTO);
		logger.debug("listQuestionContentDTO now: " + listQuestionContentDTO);
	
		mcGeneralAuthoringDTO.setDefineLaterInEditMode(new Boolean(true).toString());

	    Map marksMap=authoringUtil.buildMarksMap();
	    logger.debug("marksMap: " + marksMap);
	    mcGeneralAuthoringDTO.setMarksMap(marksMap);

	    logger.debug("generating dyn pass map using listQuestionContentDTO: " +listQuestionContentDTO);
	    Map passMarksMap=authoringUtil.buildDynamicPassMarkMap(listQuestionContentDTO, false);
	    logger.debug("passMarksMap: " + passMarksMap);
	    mcGeneralAuthoringDTO.setPassMarksMap(passMarksMap);

	    
        String totalMark=AuthoringUtil.getTotalMark(listQuestionContentDTO);
        logger.debug("totalMark: " + totalMark);
        mcAuthoringForm.setTotalMarks(totalMark);
        mcGeneralAuthoringDTO.setTotalMarks(totalMark);

	    
	    Map correctMap=authoringUtil.buildCorrectMap();
	    logger.debug("correctMap: " + correctMap);
	    mcGeneralAuthoringDTO.setCorrectMap(correctMap);
	    
		logger.debug("before saving final mcGeneralAuthoringDTO: " + mcGeneralAuthoringDTO);
		request.setAttribute(MC_GENERAL_AUTHORING_DTO, mcGeneralAuthoringDTO);
	
		request.setAttribute(TOTAL_QUESTION_COUNT, new Integer(listQuestionContentDTO.size()));
		
		setupCommonScreenData(mcContent, mcService,request);
		
		MonitoringUtil.setupAllSessionsData(request, mcContent,mcService);
		
		logger.debug("fwd ing to LOAD_MONITORING: " + LOAD_MONITORING);
	    return (mapping.findForward(LOAD_MONITORING));
    }


    
    /**
     * moveCandidateDown(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
     * 
     * moves a candidate dwn in the list
     * 
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws IOException
     * @throws ServletException
     */
    public ActionForward moveCandidateDown(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) 
    throws IOException, ServletException {
		logger.debug("dispatching moveCandidateDown");
		McAuthoringForm mcAuthoringForm = (McMonitoringForm) form;
		
		IMcService mcService =McServiceProxy.getMcService(getServlet().getServletContext());
		logger.debug("mcService: " + mcService);
	
		String httpSessionID=mcAuthoringForm.getHttpSessionID();
		logger.debug("httpSessionID: " + httpSessionID);
		
		SessionMap sessionMap=(SessionMap)request.getSession().getAttribute(httpSessionID);
		logger.debug("sessionMap: " + sessionMap);
		
		String questionIndex=request.getParameter("questionIndex");
		logger.debug("questionIndex: " + questionIndex);
		mcAuthoringForm.setQuestionIndex(questionIndex);
		
		String candidateIndex=request.getParameter("candidateIndex");
		logger.debug("candidateIndex: " + candidateIndex);
		mcAuthoringForm.setCandidateIndex(candidateIndex);
		
	    String totalMarks=request.getParameter("totalMarks");
		logger.debug("totalMarks: " + totalMarks);

		AuthoringUtil authoringUtil = new AuthoringUtil();

		boolean validateCandidateAnswersNotBlank =authoringUtil.validateCandidateAnswersNotBlank(request);
		logger.debug("validateCandidateAnswersNotBlank: " + validateCandidateAnswersNotBlank);

		ActionMessages errors = new ActionMessages();
		
        if (!validateCandidateAnswersNotBlank)
        {
            ActionMessage error = new ActionMessage("candidates.blank");
			errors.add(ActionMessages.GLOBAL_MESSAGE, error);
        }
        

	 	if(!errors.isEmpty()){
			saveErrors(request, errors);
	        logger.debug("errors saved: " + errors);
		}

		
	    List caList=authoringUtil.repopulateCandidateAnswersBox(request, false);
	    logger.debug("repopulated caList: " + caList);

		
	    List listQuestionContentDTO=(List)sessionMap.get(LIST_QUESTION_CONTENT_DTO_KEY);
	    logger.debug("listQuestionContentDTO: " + listQuestionContentDTO);
	  
	    if (errors.isEmpty())
	    {
		    List candidates =new LinkedList();
		    List listCandidates =new LinkedList();
	        String editableQuestion="";
		    Iterator listIterator=listQuestionContentDTO.iterator();
		    while (listIterator.hasNext())
		    {
		        McQuestionContentDTO mcQuestionContentDTO= (McQuestionContentDTO)listIterator.next();
		        logger.debug("mcQuestionContentDTO:" + mcQuestionContentDTO);
		        
		        logger.debug("mcQuestionContentDTO question:" + mcQuestionContentDTO.getQuestion());
		        String question=mcQuestionContentDTO.getQuestion();
		        String displayOrder=mcQuestionContentDTO.getDisplayOrder();
		        
		        if ((displayOrder != null) && (!displayOrder.equals("")))
	    		{
		            if (displayOrder.equals(questionIndex))
		            {
		                logger.debug("displayOrder equals questionIndex :" + questionIndex);
		                editableQuestion=mcQuestionContentDTO.getQuestion();
		                
		                candidates=mcQuestionContentDTO.getListCandidateAnswersDTO();
		                logger.debug("candidates found :" + candidates);
		                logger.debug("but we are using the repopulated caList here: " + caList);
		                
		                listCandidates=AuthoringUtil.swapCandidateNodes(caList, candidateIndex, "down");
		                logger.debug("swapped candidates :" + listCandidates);
		                
		                mcQuestionContentDTO.setListCandidateAnswersDTO(listCandidates);
		                
		                break;
		            }
		            
	    		}
		    }
		    logger.debug("candidates found :" + candidates);
		    logger.debug("swapped candidates is :" + listCandidates);
	    }

	    logger.debug("listQuestionContentDTO after swapped candidates :" + listQuestionContentDTO);

	    
	    sessionMap.put(LIST_QUESTION_CONTENT_DTO_KEY, listQuestionContentDTO);
	    
	    
		String contentFolderID = WebUtil.readStrParam(request, AttributeNames.PARAM_CONTENT_FOLDER_ID);
		logger.debug("contentFolderID: " + contentFolderID);
		mcAuthoringForm.setContentFolderID(contentFolderID);
	
		
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
		
		String defaultContentIdStr=new Long(mcService.getToolDefaultContentIdBySignature(MY_SIGNATURE)).toString();;
		logger.debug("defaultContentIdStr: " + defaultContentIdStr);
		
		McContent mcContent=mcService.retrieveMc(new Long(strToolContentID));
		logger.debug("mcContent: " + mcContent);

		McGeneralAuthoringDTO mcGeneralAuthoringDTO= new McGeneralAuthoringDTO();
		logger.debug("mcGeneralAuthoringDTO: " + mcGeneralAuthoringDTO);
		mcGeneralAuthoringDTO.setContentFolderID(contentFolderID);
		
	    mcGeneralAuthoringDTO.setActivityTitle(richTextTitle);
	    mcAuthoringForm.setTitle(richTextTitle);
	    
		mcGeneralAuthoringDTO.setActivityInstructions(richTextInstructions);
	
	    mcGeneralAuthoringDTO.setEditActivityEditMode(new Boolean(true).toString());
	    
	    request.getSession().setAttribute(httpSessionID, sessionMap);
	
		McUtils.setFormProperties(request, mcService,  
	             mcAuthoringForm, mcGeneralAuthoringDTO, strToolContentID, defaultContentIdStr, activeModule, sessionMap, httpSessionID);
	
		
		mcGeneralAuthoringDTO.setToolContentID(strToolContentID);
		mcGeneralAuthoringDTO.setHttpSessionID(httpSessionID);
		mcGeneralAuthoringDTO.setActiveModule(activeModule);
		mcGeneralAuthoringDTO.setDefaultContentIdStr(defaultContentIdStr);
		mcAuthoringForm.setToolContentID(strToolContentID);
		mcAuthoringForm.setHttpSessionID(httpSessionID);
		mcAuthoringForm.setActiveModule(activeModule);
		mcAuthoringForm.setDefaultContentIdStr(defaultContentIdStr);
		mcAuthoringForm.setCurrentTab("3");
	
	    request.setAttribute(LIST_QUESTION_CONTENT_DTO,listQuestionContentDTO);
		logger.debug("listQuestionContentDTO now: " + listQuestionContentDTO);
	
		mcGeneralAuthoringDTO.setDefineLaterInEditMode(new Boolean(true).toString());

	    Map marksMap=authoringUtil.buildMarksMap();
	    logger.debug("marksMap: " + marksMap);
	    mcGeneralAuthoringDTO.setMarksMap(marksMap);

	    logger.debug("generating dyn pass map using listQuestionContentDTO: " +listQuestionContentDTO);
	    Map passMarksMap=authoringUtil.buildDynamicPassMarkMap(listQuestionContentDTO, false);
	    logger.debug("passMarksMap: " + passMarksMap);
	    mcGeneralAuthoringDTO.setPassMarksMap(passMarksMap);
	    
	    
        String totalMark=AuthoringUtil.getTotalMark(listQuestionContentDTO);
        logger.debug("totalMark: " + totalMark);
        mcAuthoringForm.setTotalMarks(totalMark);
        mcGeneralAuthoringDTO.setTotalMarks(totalMark);

	    
	    Map correctMap=authoringUtil.buildCorrectMap();
	    logger.debug("correctMap: " + correctMap);
	    mcGeneralAuthoringDTO.setCorrectMap(correctMap);
	    
	    
	    String newQuestion=request.getParameter("newQuestion");
	    logger.debug("newQuestion: " + newQuestion);
	    mcGeneralAuthoringDTO.setEditableQuestionText(newQuestion);
	    
	    String feedback=request.getParameter("feedback");
	    logger.debug("feedback: " + feedback);
	    mcAuthoringForm.setFeedback(feedback);
	    
		String mark=request.getParameter("mark");
		logger.debug("mark: " + mark);
	    mcGeneralAuthoringDTO.setMarkValue(mark);

	    
		logger.debug("before saving final mcGeneralAuthoringDTO: " + mcGeneralAuthoringDTO);
		request.setAttribute(MC_GENERAL_AUTHORING_DTO, mcGeneralAuthoringDTO);
	
		request.setAttribute(TOTAL_QUESTION_COUNT, new Integer(listQuestionContentDTO.size()));
	
		
		String editQuestionBoxRequest=request.getParameter("editQuestionBoxRequest");
		logger.debug("editQuestionBoxRequest: " + editQuestionBoxRequest);

		setupCommonScreenData(mcContent, mcService,request);
		
		MonitoringUtil.setupAllSessionsData(request, mcContent,mcService);
		
		return newEditableQuestionBox(mapping, form,  request, response);
    }


    /**
     * moveCandidateUp(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
     * 
     * moves a candidate up in the list
     * 
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws IOException
     * @throws ServletException
     */
    public ActionForward moveCandidateUp(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) 
    throws IOException, ServletException {
		logger.debug("dispatching moveCandidateUp");
		McAuthoringForm mcAuthoringForm = (McAuthoringForm) form;
		
		IMcService mcService =McServiceProxy.getMcService(getServlet().getServletContext());
		logger.debug("mcService: " + mcService);
	
		String httpSessionID=mcAuthoringForm.getHttpSessionID();
		logger.debug("httpSessionID: " + httpSessionID);
		
		SessionMap sessionMap=(SessionMap)request.getSession().getAttribute(httpSessionID);
		logger.debug("sessionMap: " + sessionMap);
		
		String questionIndex=request.getParameter("questionIndex");
		logger.debug("questionIndex: " + questionIndex);
		mcAuthoringForm.setQuestionIndex(questionIndex);
		
	    String totalMarks=request.getParameter("totalMarks");
		logger.debug("totalMarks: " + totalMarks);

		
		String candidateIndex=request.getParameter("candidateIndex");
		logger.debug("candidateIndex: " + candidateIndex);
		mcAuthoringForm.setCandidateIndex(candidateIndex);

		AuthoringUtil authoringUtil = new AuthoringUtil();

		boolean validateCandidateAnswersNotBlank =authoringUtil.validateCandidateAnswersNotBlank(request);
		logger.debug("validateCandidateAnswersNotBlank: " + validateCandidateAnswersNotBlank);

		ActionMessages errors = new ActionMessages();
		
        if (!validateCandidateAnswersNotBlank)
        {
            ActionMessage error = new ActionMessage("candidates.blank");
			errors.add(ActionMessages.GLOBAL_MESSAGE, error);
        }
        

	 	if(!errors.isEmpty()){
			saveErrors(request, errors);
	        logger.debug("errors saved: " + errors);
		}

		
	    List caList=authoringUtil.repopulateCandidateAnswersBox(request, false);
	    logger.debug("repopulated caList: " + caList);

		
	    List listQuestionContentDTO=(List)sessionMap.get(LIST_QUESTION_CONTENT_DTO_KEY);
	    logger.debug("listQuestionContentDTO: " + listQuestionContentDTO);

	    
	    if (errors.isEmpty())
	    {
		    List candidates =new LinkedList();
		    List listCandidates =new LinkedList();
	        String editableQuestion="";
		    Iterator listIterator=listQuestionContentDTO.iterator();
		    while (listIterator.hasNext())
		    {
		        McQuestionContentDTO mcQuestionContentDTO= (McQuestionContentDTO)listIterator.next();
		        logger.debug("mcQuestionContentDTO:" + mcQuestionContentDTO);
		        
		        logger.debug("mcQuestionContentDTO question:" + mcQuestionContentDTO.getQuestion());
		        String question=mcQuestionContentDTO.getQuestion();
		        String displayOrder=mcQuestionContentDTO.getDisplayOrder();
		        
		        if ((displayOrder != null) && (!displayOrder.equals("")))
	    		{
		            if (displayOrder.equals(questionIndex))
		            {
		                logger.debug("displayOrder equals questionIndex :" + questionIndex);
		                editableQuestion=mcQuestionContentDTO.getQuestion();
		                
		                candidates=mcQuestionContentDTO.getListCandidateAnswersDTO();
		                logger.debug("candidates found :" + candidates);
		                
		                logger.debug("using repopulated caList:" + caList);
		                
		                listCandidates=AuthoringUtil.swapCandidateNodes(caList, candidateIndex, "up");
		                logger.debug("swapped candidates :" + listCandidates);
		                
		                
		                mcQuestionContentDTO.setListCandidateAnswersDTO(listCandidates);
		                mcQuestionContentDTO.setCaCount(new Integer(listCandidates.size()).toString());
		                
		                break;
		            }
		            
	    		}
		    }
		    logger.debug("candidates found :" + candidates);
		    logger.debug("swapped candidates is :" + listCandidates);
	    }
	    

	    logger.debug("listQuestionContentDTO after swapped candidates :" + listQuestionContentDTO);

	    
	    sessionMap.put(LIST_QUESTION_CONTENT_DTO_KEY, listQuestionContentDTO);
	    
	    
		String contentFolderID = WebUtil.readStrParam(request, AttributeNames.PARAM_CONTENT_FOLDER_ID);
		logger.debug("contentFolderID: " + contentFolderID);
		mcAuthoringForm.setContentFolderID(contentFolderID);
	
		
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
		
		String defaultContentIdStr=new Long(mcService.getToolDefaultContentIdBySignature(MY_SIGNATURE)).toString();;
		logger.debug("defaultContentIdStr: " + defaultContentIdStr);
		
		McContent mcContent=mcService.retrieveMc(new Long(strToolContentID));
		logger.debug("mcContent: " + mcContent);

		McGeneralAuthoringDTO mcGeneralAuthoringDTO= new McGeneralAuthoringDTO();
		logger.debug("mcGeneralAuthoringDTO: " + mcGeneralAuthoringDTO);
		mcGeneralAuthoringDTO.setContentFolderID(contentFolderID);
		
	    mcGeneralAuthoringDTO.setActivityTitle(richTextTitle);
	    mcAuthoringForm.setTitle(richTextTitle);
	    
		mcGeneralAuthoringDTO.setActivityInstructions(richTextInstructions);
	
		mcGeneralAuthoringDTO.setEditActivityEditMode(new Boolean(true).toString());
	    
	    request.getSession().setAttribute(httpSessionID, sessionMap);
	
		McUtils.setFormProperties(request, mcService,  
	             mcAuthoringForm, mcGeneralAuthoringDTO, strToolContentID, defaultContentIdStr, activeModule, sessionMap, httpSessionID);
	
		
		mcGeneralAuthoringDTO.setToolContentID(strToolContentID);
		mcGeneralAuthoringDTO.setHttpSessionID(httpSessionID);
		mcGeneralAuthoringDTO.setActiveModule(activeModule);
		mcGeneralAuthoringDTO.setDefaultContentIdStr(defaultContentIdStr);
		mcAuthoringForm.setToolContentID(strToolContentID);
		mcAuthoringForm.setHttpSessionID(httpSessionID);
		mcAuthoringForm.setActiveModule(activeModule);
		mcAuthoringForm.setDefaultContentIdStr(defaultContentIdStr);
		mcAuthoringForm.setCurrentTab("3");
	
	    request.setAttribute(LIST_QUESTION_CONTENT_DTO,listQuestionContentDTO);
		logger.debug("listQuestionContentDTO now: " + listQuestionContentDTO);
	
		mcGeneralAuthoringDTO.setDefineLaterInEditMode(new Boolean(true).toString());

	    Map marksMap=authoringUtil.buildMarksMap();
	    logger.debug("marksMap: " + marksMap);
	    mcGeneralAuthoringDTO.setMarksMap(marksMap);

	    logger.debug("generating dyn pass map using listQuestionContentDTO: " +listQuestionContentDTO);
	    Map passMarksMap=authoringUtil.buildDynamicPassMarkMap(listQuestionContentDTO, false);
	    logger.debug("passMarksMap: " + passMarksMap);
	    mcGeneralAuthoringDTO.setPassMarksMap(passMarksMap);


	    Map correctMap=authoringUtil.buildCorrectMap();
	    logger.debug("correctMap: " + correctMap);
	    mcGeneralAuthoringDTO.setCorrectMap(correctMap);
	    
        String totalMark=AuthoringUtil.getTotalMark(listQuestionContentDTO);
        logger.debug("totalMark: " + totalMark);
        mcAuthoringForm.setTotalMarks(totalMark);
        mcGeneralAuthoringDTO.setTotalMarks(totalMark);

	    String newQuestion=request.getParameter("newQuestion");
	    logger.debug("newQuestion: " + newQuestion);
	    mcGeneralAuthoringDTO.setEditableQuestionText(newQuestion);
	    
	    String feedback=request.getParameter("feedback");
	    logger.debug("feedback: " + feedback);
	    mcAuthoringForm.setFeedback(feedback);
	    
		String mark=request.getParameter("mark");
		logger.debug("mark: " + mark);
	    mcGeneralAuthoringDTO.setMarkValue(mark);

	    
		logger.debug("before saving final mcGeneralAuthoringDTO: " + mcGeneralAuthoringDTO);
		request.setAttribute(MC_GENERAL_AUTHORING_DTO, mcGeneralAuthoringDTO);
	
		request.setAttribute(TOTAL_QUESTION_COUNT, new Integer(listQuestionContentDTO.size()));
		
		String editQuestionBoxRequest=request.getParameter("editQuestionBoxRequest");
		logger.debug("editQuestionBoxRequest: " + editQuestionBoxRequest);
		
		setupCommonScreenData(mcContent, mcService,request);
		
		MonitoringUtil.setupAllSessionsData(request, mcContent,mcService);

		return newEditableQuestionBox(mapping, form,  request, response);

    }


    /**
     * removeCandidate(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
     * 
     * removes a candidate from the list
     * 
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws IOException
     * @throws ServletException
     */
    public ActionForward removeCandidate(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) 
    throws IOException, ServletException {
		logger.debug("dispatching removeCandidate");
		McAuthoringForm mcAuthoringForm = (McMonitoringForm) form;
		
		IMcService mcService =McServiceProxy.getMcService(getServlet().getServletContext());
		logger.debug("mcService: " + mcService);
	
		String httpSessionID=mcAuthoringForm.getHttpSessionID();
		logger.debug("httpSessionID: " + httpSessionID);
		
		SessionMap sessionMap=(SessionMap)request.getSession().getAttribute(httpSessionID);
		logger.debug("sessionMap: " + sessionMap);
		
		String questionIndex=request.getParameter("questionIndex");
		logger.debug("questionIndex: " + questionIndex);
		mcAuthoringForm.setQuestionIndex(questionIndex);

		String candidateIndex=request.getParameter("candidateIndex");
		logger.debug("candidateIndex: " + candidateIndex);
		mcAuthoringForm.setCandidateIndex(candidateIndex);
		
	    String totalMarks=request.getParameter("totalMarks");
		logger.debug("totalMarks: " + totalMarks);


		List listQuestionContentDTO=(List)sessionMap.get(LIST_QUESTION_CONTENT_DTO_KEY);
	    logger.debug("listQuestionContentDTO: " + listQuestionContentDTO);
	
	    AuthoringUtil authoringUtil = new AuthoringUtil();
	    List caList=authoringUtil.repopulateCandidateAnswersBox(request, false);
	    logger.debug("repopulated caList: " + caList);
	    
	    
	    McQuestionContentDTO mcQuestionContentDTO= null;
	    Iterator listIterator=listQuestionContentDTO.iterator();
	    while (listIterator.hasNext())
	    {
	        mcQuestionContentDTO= (McQuestionContentDTO)listIterator.next();
	        logger.debug("mcQuestionContentDTO:" + mcQuestionContentDTO);
	        logger.debug("mcQuestionContentDTO question:" + mcQuestionContentDTO.getQuestion());
	        
	        String question=mcQuestionContentDTO.getQuestion();
	        String displayOrder=mcQuestionContentDTO.getDisplayOrder();
	        logger.debug("displayOrder:" + displayOrder);
	        
	        if ((displayOrder != null) && (!displayOrder.equals("")))
			{
	            if (displayOrder.equals(questionIndex))
	            {
	                break;
	            }
	            
			}
	    }
	    
	    logger.debug("mcQuestionContentDTO found:" + mcQuestionContentDTO);
	    logger.debug("setting caList for the content:");
	    mcQuestionContentDTO.setListCandidateAnswersDTO(caList);
	    
	    List candidateAnswers=mcQuestionContentDTO.getListCandidateAnswersDTO();
	    logger.debug("candidateAnswers:" + candidateAnswers);
	    
	    McCandidateAnswersDTO mcCandidateAnswersDTO= null;
	    Iterator listCaIterator=candidateAnswers.iterator();
	    int caIndex=0;
	    while (listCaIterator.hasNext())
	    {
	        caIndex ++;
	        logger.debug("caIndex:" + caIndex);
	        mcCandidateAnswersDTO= (McCandidateAnswersDTO)listCaIterator.next();
	        logger.debug("mcCandidateAnswersDTO:" + mcCandidateAnswersDTO);
	        logger.debug("mcCandidateAnswersDTO question:" + mcCandidateAnswersDTO.getCandidateAnswer());
	        
	        if (caIndex == new Integer(candidateIndex).intValue())
	        {
	            logger.debug("candidateIndex found");
	            mcCandidateAnswersDTO.setCandidateAnswer("");
	            
	            break;
	        }
	    }
		logger.debug("candidateAnswers after resetting answer" + candidateAnswers);
	    
		candidateAnswers=AuthoringUtil.reorderListCandidatesDTO(candidateAnswers);
		logger.debug("candidateAnswers after reordering candidate nodes" + candidateAnswers);

		mcQuestionContentDTO.setListCandidateAnswersDTO(candidateAnswers);
		mcQuestionContentDTO.setCaCount(new Integer(candidateAnswers.size()).toString());
		
		logger.debug("listQuestionContentDTO after remove: " + listQuestionContentDTO);
	    
	    sessionMap.put(LIST_QUESTION_CONTENT_DTO_KEY, listQuestionContentDTO);
	    
		String contentFolderID = WebUtil.readStrParam(request, AttributeNames.PARAM_CONTENT_FOLDER_ID);
		logger.debug("contentFolderID: " + contentFolderID);
		mcAuthoringForm.setContentFolderID(contentFolderID);
	
		
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
		
		String defaultContentIdStr=new Long(mcService.getToolDefaultContentIdBySignature(MY_SIGNATURE)).toString();;
		logger.debug("defaultContentIdStr: " + defaultContentIdStr);
		
		McContent mcContent=mcService.retrieveMc(new Long(strToolContentID));
		logger.debug("mcContent: " + mcContent);
		
		if (mcContent == null)
		{
			logger.debug("using defaultContentIdStr: " + defaultContentIdStr);
			mcContent=mcService.retrieveMc(new Long(defaultContentIdStr));
		}
		logger.debug("final mcContent: " + mcContent);
	
		McGeneralAuthoringDTO mcGeneralAuthoringDTO= new McGeneralAuthoringDTO();
		logger.debug("mcGeneralAuthoringDTO: " + mcGeneralAuthoringDTO);
		mcGeneralAuthoringDTO.setContentFolderID(contentFolderID);
		
	    mcGeneralAuthoringDTO.setActivityTitle(richTextTitle);
	    mcAuthoringForm.setTitle(richTextTitle);
	    
		mcGeneralAuthoringDTO.setActivityInstructions(richTextInstructions);
	    
		mcGeneralAuthoringDTO.setEditActivityEditMode(new Boolean(true).toString());
	    
	    request.getSession().setAttribute(httpSessionID, sessionMap);
	
		McUtils.setFormProperties(request, mcService,  
	             mcAuthoringForm, mcGeneralAuthoringDTO, strToolContentID, defaultContentIdStr, activeModule, sessionMap, httpSessionID);
	
		
		mcGeneralAuthoringDTO.setToolContentID(strToolContentID);
		mcGeneralAuthoringDTO.setHttpSessionID(httpSessionID);
		mcGeneralAuthoringDTO.setActiveModule(activeModule);
		mcGeneralAuthoringDTO.setDefaultContentIdStr(defaultContentIdStr);
		mcAuthoringForm.setToolContentID(strToolContentID);
		mcAuthoringForm.setHttpSessionID(httpSessionID);
		mcAuthoringForm.setActiveModule(activeModule);
		mcAuthoringForm.setDefaultContentIdStr(defaultContentIdStr);
		mcAuthoringForm.setCurrentTab("3");
	
	    request.setAttribute(LIST_QUESTION_CONTENT_DTO,listQuestionContentDTO);
		logger.debug("mcQuestionContentDTO now: " + mcQuestionContentDTO);
	    logger.debug("listQuestionContentDTO now: " + listQuestionContentDTO);
	
	    mcGeneralAuthoringDTO.setDefineLaterInEditMode(new Boolean(true).toString());
	
	    Map marksMap=authoringUtil.buildMarksMap();
	    logger.debug("marksMap: " + marksMap);
	    mcGeneralAuthoringDTO.setMarksMap(marksMap);

	    logger.debug("generating dyn pass map using listQuestionContentDTO: " +listQuestionContentDTO);
	    Map passMarksMap=authoringUtil.buildDynamicPassMarkMap(listQuestionContentDTO, false);
	    logger.debug("passMarksMap: " + passMarksMap);
	    mcGeneralAuthoringDTO.setPassMarksMap(passMarksMap);

	    
        String totalMark=AuthoringUtil.getTotalMark(listQuestionContentDTO);
        logger.debug("totalMark: " + totalMark);
        mcAuthoringForm.setTotalMarks(totalMark);
        mcGeneralAuthoringDTO.setTotalMarks(totalMark);

        
	    Map correctMap=authoringUtil.buildCorrectMap();
	    logger.debug("correctMap: " + correctMap);
	    mcGeneralAuthoringDTO.setCorrectMap(correctMap);
	    

	    String newQuestion=request.getParameter("newQuestion");
	    logger.debug("newQuestion: " + newQuestion);
	    mcGeneralAuthoringDTO.setEditableQuestionText(newQuestion);
	    
	    String feedback=request.getParameter("feedback");
	    logger.debug("feedback: " + feedback);
	    mcAuthoringForm.setFeedback(feedback);
	    
		String mark=request.getParameter("mark");
		logger.debug("mark: " + mark);
	    mcGeneralAuthoringDTO.setMarkValue(mark);

		logger.debug("before saving final mcGeneralAuthoringDTO: " + mcGeneralAuthoringDTO);
		request.setAttribute(MC_GENERAL_AUTHORING_DTO, mcGeneralAuthoringDTO);
	
		request.setAttribute(TOTAL_QUESTION_COUNT, new Integer(listQuestionContentDTO.size()));
		
		
		String editQuestionBoxRequest=request.getParameter("editQuestionBoxRequest");
		logger.debug("editQuestionBoxRequest: " + editQuestionBoxRequest);
		
		setupCommonScreenData(mcContent, mcService,request);
		
		MonitoringUtil.setupAllSessionsData(request, mcContent,mcService);

		return newEditableQuestionBox(mapping, form,  request, response);

    }


    /**
     * newCandidateBox(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
     * 
     * enables adding a new candidate answer
     * 
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws IOException
     * @throws ServletException
     */
    public ActionForward newCandidateBox(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) 
    throws IOException, ServletException {
		logger.debug("dispatching newCandidateBox");
		McAuthoringForm mcAuthoringForm = (McMonitoringForm) form;
	    
		IMcService mcService =McServiceProxy.getMcService(getServlet().getServletContext());
		logger.debug("mcService: " + mcService);
	
		String httpSessionID=mcAuthoringForm.getHttpSessionID();
		logger.debug("httpSessionID: " + httpSessionID);
		
		SessionMap sessionMap=(SessionMap)request.getSession().getAttribute(httpSessionID);
		logger.debug("sessionMap: " + sessionMap);
		
		String questionIndex=request.getParameter("questionIndex");
		logger.debug("questionIndex: " + questionIndex);
		mcAuthoringForm.setQuestionIndex(questionIndex);

		String candidateIndex=request.getParameter("candidateIndex");
		logger.debug("candidateIndex: " + candidateIndex);
		mcAuthoringForm.setCandidateIndex(candidateIndex);

	    String totalMarks=request.getParameter("totalMarks");
		logger.debug("totalMarks: " + totalMarks);

	    
	    List listQuestionContentDTO=(List)sessionMap.get(LIST_QUESTION_CONTENT_DTO_KEY);
	    logger.debug("listQuestionContentDTO: " + listQuestionContentDTO);
	

		AuthoringUtil authoringUtil = new AuthoringUtil();
	    List caList=authoringUtil.repopulateCandidateAnswersBox(request, true);
	    logger.debug("repopulated caList: " + caList);
	    
	    int caCount=caList.size();
	    logger.debug("caCount: " + caCount);
	    

	    String newQuestion=request.getParameter("newQuestion");
	    logger.debug("newQuestion: " + newQuestion);
	    
	    String mark=request.getParameter("mark");
	    logger.debug("mark: " + mark);

	    String passmark=request.getParameter("passmark");
	    logger.debug("passmark: " + passmark);

	    
	    String feedback=request.getParameter("feedback");
	    logger.debug("feedback: " + feedback);
	    
	    int currentQuestionCount= listQuestionContentDTO.size();
	    logger.debug("currentQuestionCount: " + currentQuestionCount);
	    
	    
	    String editQuestionBoxRequest=request.getParameter("editQuestionBoxRequest");
		logger.debug("editQuestionBoxRequest: " + editQuestionBoxRequest);
		
		
	    McQuestionContentDTO mcQuestionContentDTOLocal= null;
	    Iterator listIterator=listQuestionContentDTO.iterator();
	    while (listIterator.hasNext())
	    {
	        mcQuestionContentDTOLocal= (McQuestionContentDTO)listIterator.next();
	        logger.debug("mcQuestionContentDTOLocal:" + mcQuestionContentDTOLocal);
	        logger.debug("mcQuestionContentDTOLocal question:" + mcQuestionContentDTOLocal.getQuestion());
	        
	        String question=mcQuestionContentDTOLocal.getQuestion();
	        String displayOrder=mcQuestionContentDTOLocal.getDisplayOrder();
	        logger.debug("displayOrder:" + displayOrder);
	        
	        if ((displayOrder != null) && (!displayOrder.equals("")))
			{
	            if (displayOrder.equals(questionIndex))
	            {
	                break;
	            }
	            
			}
	    }
	    
	    logger.debug("mcQuestionContentDTOLocal found:" + mcQuestionContentDTOLocal);
	    
	    if (mcQuestionContentDTOLocal != null)
	    {
		    mcQuestionContentDTOLocal.setListCandidateAnswersDTO(caList);
		    mcQuestionContentDTOLocal.setCaCount(new Integer(caList.size()).toString());
	    }
			
		
		logger.debug("listQuestionContentDTO after repopulating data: " + listQuestionContentDTO);
	    sessionMap.put(LIST_QUESTION_CONTENT_DTO_KEY, listQuestionContentDTO);
	    
		String contentFolderID = WebUtil.readStrParam(request, AttributeNames.PARAM_CONTENT_FOLDER_ID);
		logger.debug("contentFolderID: " + contentFolderID);
		mcAuthoringForm.setContentFolderID(contentFolderID);
	
		
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
		
		String defaultContentIdStr=new Long(mcService.getToolDefaultContentIdBySignature(MY_SIGNATURE)).toString();;
		logger.debug("defaultContentIdStr: " + defaultContentIdStr);
		
		McGeneralAuthoringDTO mcGeneralAuthoringDTO= new McGeneralAuthoringDTO();
		logger.debug("mcGeneralAuthoringDTO: " + mcGeneralAuthoringDTO);
		mcGeneralAuthoringDTO.setContentFolderID(contentFolderID);
		
	    mcGeneralAuthoringDTO.setActivityTitle(richTextTitle);
	    mcAuthoringForm.setTitle(richTextTitle);
	    
		mcGeneralAuthoringDTO.setActivityInstructions(richTextInstructions);
		
	    mcGeneralAuthoringDTO.setEditActivityEditMode(new Boolean(true).toString());
	    
	    request.getSession().setAttribute(httpSessionID, sessionMap);
	
		McUtils.setFormProperties(request, mcService,  
	             mcAuthoringForm, mcGeneralAuthoringDTO, strToolContentID, defaultContentIdStr, activeModule, sessionMap, httpSessionID);
	
		
		mcGeneralAuthoringDTO.setToolContentID(strToolContentID);
		mcGeneralAuthoringDTO.setHttpSessionID(httpSessionID);
		mcGeneralAuthoringDTO.setActiveModule(activeModule);
		mcGeneralAuthoringDTO.setDefaultContentIdStr(defaultContentIdStr);
		mcAuthoringForm.setToolContentID(strToolContentID);
		mcAuthoringForm.setHttpSessionID(httpSessionID);
		mcAuthoringForm.setActiveModule(activeModule);
		mcAuthoringForm.setDefaultContentIdStr(defaultContentIdStr);
		mcAuthoringForm.setCurrentTab("3");
	
	    request.setAttribute(LIST_QUESTION_CONTENT_DTO,listQuestionContentDTO);
		logger.debug("mcQuestionContentDTOLocal now: " + mcQuestionContentDTOLocal);
	    logger.debug("listQuestionContentDTO now: " + listQuestionContentDTO);
	
	    mcGeneralAuthoringDTO.setDefineLaterInEditMode(new Boolean(true).toString());
	
	    Map marksMap=authoringUtil.buildMarksMap();
	    logger.debug("marksMap: " + marksMap);
	    mcGeneralAuthoringDTO.setMarksMap(marksMap);

	    logger.debug("generating dyn pass map using listQuestionContentDTO: " +listQuestionContentDTO);
	    Map passMarksMap=authoringUtil.buildDynamicPassMarkMap(listQuestionContentDTO, false);
	    logger.debug("passMarksMap: " + passMarksMap);
	    mcGeneralAuthoringDTO.setPassMarksMap(passMarksMap);

        String totalMark=AuthoringUtil.getTotalMark(listQuestionContentDTO);
        logger.debug("totalMark: " + totalMark);
        mcAuthoringForm.setTotalMarks(totalMark);
        mcGeneralAuthoringDTO.setTotalMarks(totalMark);

	    
	    Map correctMap=authoringUtil.buildCorrectMap();
	    logger.debug("correctMap: " + correctMap);
	    mcGeneralAuthoringDTO.setCorrectMap(correctMap);


	    logger.debug("newQuestion: " + newQuestion);
	    mcGeneralAuthoringDTO.setEditableQuestionText(newQuestion);
	    

	    logger.debug("feedback: " + feedback);
	    mcAuthoringForm.setFeedback(feedback);
	    

		logger.debug("mark: " + mark);
	    mcGeneralAuthoringDTO.setMarkValue(mark);

	    
		logger.debug("before saving final mcGeneralAuthoringDTO: " + mcGeneralAuthoringDTO);
		request.setAttribute(MC_GENERAL_AUTHORING_DTO, mcGeneralAuthoringDTO);
	
		request.setAttribute(TOTAL_QUESTION_COUNT, new Integer(listQuestionContentDTO.size()));
		
		logger.debug("editQuestionBoxRequest: " + editQuestionBoxRequest);

		McContent mcContent=mcService.retrieveMc(new Long(strToolContentID));
		logger.debug("mcContent: " + mcContent);

		setupCommonScreenData(mcContent, mcService,request);
		
		MonitoringUtil.setupAllSessionsData(request, mcContent,mcService);
		return newEditableQuestionBox(mapping, form,  request, response);
    }

    
    public ActionForward moveAddedCandidateUp(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) 
    throws IOException, ServletException {
		logger.debug("dispatching moveAddedCandidateUp");
		McAuthoringForm mcAuthoringForm = (McMonitoringForm) form;
		
		IMcService mcService =McServiceProxy.getMcService(getServlet().getServletContext());
		logger.debug("mcService: " + mcService);
	
		String httpSessionID=mcAuthoringForm.getHttpSessionID();
		logger.debug("httpSessionID: " + httpSessionID);
		
		SessionMap sessionMap=(SessionMap)request.getSession().getAttribute(httpSessionID);
		logger.debug("sessionMap: " + sessionMap);
		
		
	    String totalMarks=request.getParameter("totalMarks");
		logger.debug("totalMarks: " + totalMarks);

		
		String candidateIndex=request.getParameter("candidateIndex");
		logger.debug("candidateIndex: " + candidateIndex);
		mcAuthoringForm.setCandidateIndex(candidateIndex);

		AuthoringUtil authoringUtil = new AuthoringUtil();

		boolean validateCandidateAnswersNotBlank =authoringUtil.validateCandidateAnswersNotBlank(request);
		logger.debug("validateCandidateAnswersNotBlank: " + validateCandidateAnswersNotBlank);

		ActionMessages errors = new ActionMessages();
		
        if (!validateCandidateAnswersNotBlank)
        {
            ActionMessage error = new ActionMessage("candidates.blank");
			errors.add(ActionMessages.GLOBAL_MESSAGE, error);
        }
        

	 	if(!errors.isEmpty()){
			saveErrors(request, errors);
	        logger.debug("errors saved: " + errors);
		}

	    List caList=authoringUtil.repopulateCandidateAnswersBox(request, false);
	    logger.debug("repopulated caList: " + caList);

		
	    List listQuestionContentDTO=(List)sessionMap.get(LIST_QUESTION_CONTENT_DTO_KEY);
	    logger.debug("listQuestionContentDTO: " + listQuestionContentDTO);

	    sessionMap.put(LIST_QUESTION_CONTENT_DTO_KEY, listQuestionContentDTO);
	    
	    
		List listAddableQuestionContentDTO = (List)sessionMap.get(NEW_ADDABLE_QUESTION_CONTENT_KEY);
		logger.debug("listAddableQuestionContentDTO: " + listAddableQuestionContentDTO);
		
		if (errors.isEmpty())
		{
		    List candidates =new LinkedList();
		    List listCandidates =new LinkedList();
		    
		    Iterator listIterator=listAddableQuestionContentDTO.iterator();
		    /*there is only 1 question dto*/
		    while (listIterator.hasNext())
		    {
		        McQuestionContentDTO mcQuestionContentDTO= (McQuestionContentDTO)listIterator.next();
		        logger.debug("mcQuestionContentDTO:" + mcQuestionContentDTO);
		        
	            candidates=mcQuestionContentDTO.getListCandidateAnswersDTO();
	            logger.debug("candidates found :" + candidates);
	            logger.debug("but we are using the repopulated caList here: " + caList);
	            
	            listCandidates=AuthoringUtil.swapCandidateNodes(caList, candidateIndex, "up");
	            logger.debug("swapped candidates :" + listCandidates);
	            
	            mcQuestionContentDTO.setListCandidateAnswersDTO(listCandidates);
		    }
		}

	    logger.debug("listAddableQuestionContentDTO after swapping (up) candidates: " + listAddableQuestionContentDTO);
		request.setAttribute(NEW_ADDABLE_QUESTION_CONTENT_LIST, listAddableQuestionContentDTO);
		sessionMap.put(NEW_ADDABLE_QUESTION_CONTENT_KEY, listAddableQuestionContentDTO);

	    
	    
		String contentFolderID = WebUtil.readStrParam(request, AttributeNames.PARAM_CONTENT_FOLDER_ID);
		logger.debug("contentFolderID: " + contentFolderID);
		mcAuthoringForm.setContentFolderID(contentFolderID);
	
		
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
		
		String defaultContentIdStr=new Long(mcService.getToolDefaultContentIdBySignature(MY_SIGNATURE)).toString();;
		logger.debug("defaultContentIdStr: " + defaultContentIdStr);
		
		McContent mcContent=mcService.retrieveMc(new Long(strToolContentID));
		logger.debug("mcContent: " + mcContent);

		McGeneralAuthoringDTO mcGeneralAuthoringDTO= new McGeneralAuthoringDTO();
		logger.debug("mcGeneralAuthoringDTO: " + mcGeneralAuthoringDTO);
		mcGeneralAuthoringDTO.setContentFolderID(contentFolderID);
		
	    mcGeneralAuthoringDTO.setActivityTitle(richTextTitle);
	    mcAuthoringForm.setTitle(richTextTitle);
	    
		mcGeneralAuthoringDTO.setActivityInstructions(richTextInstructions);
	
		mcGeneralAuthoringDTO.setEditActivityEditMode(new Boolean(true).toString());
	    
	    request.getSession().setAttribute(httpSessionID, sessionMap);
	
		McUtils.setFormProperties(request, mcService,  
	             mcAuthoringForm, mcGeneralAuthoringDTO, strToolContentID, defaultContentIdStr, activeModule, sessionMap, httpSessionID);
	
		
		mcGeneralAuthoringDTO.setToolContentID(strToolContentID);
		mcGeneralAuthoringDTO.setHttpSessionID(httpSessionID);
		mcGeneralAuthoringDTO.setActiveModule(activeModule);
		mcGeneralAuthoringDTO.setDefaultContentIdStr(defaultContentIdStr);
		mcAuthoringForm.setToolContentID(strToolContentID);
		mcAuthoringForm.setHttpSessionID(httpSessionID);
		mcAuthoringForm.setActiveModule(activeModule);
		mcAuthoringForm.setDefaultContentIdStr(defaultContentIdStr);
		mcAuthoringForm.setCurrentTab("3");
	
	    request.setAttribute(LIST_QUESTION_CONTENT_DTO,listQuestionContentDTO);
		logger.debug("listQuestionContentDTO now: " + listQuestionContentDTO);
	
		mcGeneralAuthoringDTO.setDefineLaterInEditMode(new Boolean(true).toString());

	    Map marksMap=authoringUtil.buildMarksMap();
	    logger.debug("marksMap: " + marksMap);
	    mcGeneralAuthoringDTO.setMarksMap(marksMap);

	    logger.debug("generating dyn pass map using listQuestionContentDTO: " +listQuestionContentDTO);
	    Map passMarksMap=authoringUtil.buildDynamicPassMarkMap(listQuestionContentDTO, false);
	    logger.debug("passMarksMap: " + passMarksMap);
	    mcGeneralAuthoringDTO.setPassMarksMap(passMarksMap);
	    

	    Map correctMap=authoringUtil.buildCorrectMap();
	    logger.debug("correctMap: " + correctMap);
	    mcGeneralAuthoringDTO.setCorrectMap(correctMap);
	    
        String totalMark=AuthoringUtil.getTotalMark(listQuestionContentDTO);
        logger.debug("totalMark: " + totalMark);
        mcAuthoringForm.setTotalMarks(totalMark);
        mcGeneralAuthoringDTO.setTotalMarks(totalMark);

        
	    String newQuestion=request.getParameter("newQuestion");
	    logger.debug("newQuestion: " + newQuestion);
	    mcGeneralAuthoringDTO.setEditableQuestionText(newQuestion);
	    
	    String feedback=request.getParameter("feedback");
	    logger.debug("feedback: " + feedback);
	    mcAuthoringForm.setFeedback(feedback);
	    
		String mark=request.getParameter("mark");
		logger.debug("mark: " + mark);
	    mcGeneralAuthoringDTO.setMarkValue(mark);

	    
		logger.debug("before saving final mcGeneralAuthoringDTO: " + mcGeneralAuthoringDTO);
		request.setAttribute(MC_GENERAL_AUTHORING_DTO, mcGeneralAuthoringDTO);
	
		request.setAttribute(TOTAL_QUESTION_COUNT, new Integer(listQuestionContentDTO.size()));
		
		String editQuestionBoxRequest=request.getParameter("editQuestionBoxRequest");
		logger.debug("editQuestionBoxRequest: " + editQuestionBoxRequest);
		
		setupCommonScreenData(mcContent, mcService,request);
		
		MonitoringUtil.setupAllSessionsData(request, mcContent,mcService);

		return newQuestionBox(mapping, form,  request, response);

    }
    
    

    public ActionForward moveAddedCandidateDown(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) 
    throws IOException, ServletException {
		logger.debug("dispatching moveAddedCandidateDown");
		McAuthoringForm mcAuthoringForm = (McMonitoringForm) form;
		
		IMcService mcService =McServiceProxy.getMcService(getServlet().getServletContext());
		logger.debug("mcService: " + mcService);
	
		String httpSessionID=mcAuthoringForm.getHttpSessionID();
		logger.debug("httpSessionID: " + httpSessionID);
		
		SessionMap sessionMap=(SessionMap)request.getSession().getAttribute(httpSessionID);
		logger.debug("sessionMap: " + sessionMap);
		
		
		String candidateIndex=request.getParameter("candidateIndex");
		logger.debug("candidateIndex: " + candidateIndex);
		mcAuthoringForm.setCandidateIndex(candidateIndex);
		
	    String totalMarks=request.getParameter("totalMarks");
		logger.debug("totalMarks: " + totalMarks);

		AuthoringUtil authoringUtil = new AuthoringUtil();

		boolean validateCandidateAnswersNotBlank =authoringUtil.validateCandidateAnswersNotBlank(request);
		logger.debug("validateCandidateAnswersNotBlank: " + validateCandidateAnswersNotBlank);

		ActionMessages errors = new ActionMessages();
		
        if (!validateCandidateAnswersNotBlank)
        {
            ActionMessage error = new ActionMessage("candidates.blank");
			errors.add(ActionMessages.GLOBAL_MESSAGE, error);
        }
        

	 	if(!errors.isEmpty()){
			saveErrors(request, errors);
	        logger.debug("errors saved: " + errors);
		}

	    List caList=authoringUtil.repopulateCandidateAnswersBox(request, false);
	    logger.debug("repopulated caList: " + caList);

		
	    List listQuestionContentDTO=(List)sessionMap.get(LIST_QUESTION_CONTENT_DTO_KEY);
	    logger.debug("listQuestionContentDTO: " + listQuestionContentDTO);
	    sessionMap.put(LIST_QUESTION_CONTENT_DTO_KEY, listQuestionContentDTO);
	    

		List listAddableQuestionContentDTO = (List)sessionMap.get(NEW_ADDABLE_QUESTION_CONTENT_KEY);
		logger.debug("listAddableQuestionContentDTO: " + listAddableQuestionContentDTO);

	    
		if (errors.isEmpty())
		{
		    List candidates =new LinkedList();
		    List listCandidates =new LinkedList();
		    
		    Iterator listIterator=listAddableQuestionContentDTO.iterator();
		    /*there is only 1 question dto*/
		    while (listIterator.hasNext())
		    {
		        McQuestionContentDTO mcQuestionContentDTO= (McQuestionContentDTO)listIterator.next();
		        logger.debug("mcQuestionContentDTO:" + mcQuestionContentDTO);
		        
	            candidates=mcQuestionContentDTO.getListCandidateAnswersDTO();
	            logger.debug("candidates found :" + candidates);
	            logger.debug("but we are using the repopulated caList here: " + caList);
	            
	            listCandidates=AuthoringUtil.swapCandidateNodes(caList, candidateIndex, "down");
	            logger.debug("swapped candidates :" + listCandidates);
	            
	            mcQuestionContentDTO.setListCandidateAnswersDTO(listCandidates);
		    }
		}
		

	    logger.debug("listAddableQuestionContentDTO after moving down candidates: ");
		request.setAttribute(NEW_ADDABLE_QUESTION_CONTENT_LIST, listAddableQuestionContentDTO);
		sessionMap.put(NEW_ADDABLE_QUESTION_CONTENT_KEY, listAddableQuestionContentDTO);
	    
	    
		String contentFolderID = WebUtil.readStrParam(request, AttributeNames.PARAM_CONTENT_FOLDER_ID);
		logger.debug("contentFolderID: " + contentFolderID);
		mcAuthoringForm.setContentFolderID(contentFolderID);
	
		
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
		
		String defaultContentIdStr=new Long(mcService.getToolDefaultContentIdBySignature(MY_SIGNATURE)).toString();;
		logger.debug("defaultContentIdStr: " + defaultContentIdStr);
		
		McContent mcContent=mcService.retrieveMc(new Long(strToolContentID));
		logger.debug("mcContent: " + mcContent);

		McGeneralAuthoringDTO mcGeneralAuthoringDTO= new McGeneralAuthoringDTO();
		logger.debug("mcGeneralAuthoringDTO: " + mcGeneralAuthoringDTO);
		mcGeneralAuthoringDTO.setContentFolderID(contentFolderID);
		
	    mcGeneralAuthoringDTO.setActivityTitle(richTextTitle);
	    mcAuthoringForm.setTitle(richTextTitle);
	    
		mcGeneralAuthoringDTO.setActivityInstructions(richTextInstructions);
	
	    mcGeneralAuthoringDTO.setEditActivityEditMode(new Boolean(true).toString());
	    
	    request.getSession().setAttribute(httpSessionID, sessionMap);
	
		McUtils.setFormProperties(request, mcService,  
	             mcAuthoringForm, mcGeneralAuthoringDTO, strToolContentID, defaultContentIdStr, activeModule, sessionMap, httpSessionID);
	
		
		mcGeneralAuthoringDTO.setToolContentID(strToolContentID);
		mcGeneralAuthoringDTO.setHttpSessionID(httpSessionID);
		mcGeneralAuthoringDTO.setActiveModule(activeModule);
		mcGeneralAuthoringDTO.setDefaultContentIdStr(defaultContentIdStr);
		mcAuthoringForm.setToolContentID(strToolContentID);
		mcAuthoringForm.setHttpSessionID(httpSessionID);
		mcAuthoringForm.setActiveModule(activeModule);
		mcAuthoringForm.setDefaultContentIdStr(defaultContentIdStr);
		mcAuthoringForm.setCurrentTab("3");
	
	    request.setAttribute(LIST_QUESTION_CONTENT_DTO,listQuestionContentDTO);
		logger.debug("listQuestionContentDTO now: " + listQuestionContentDTO);
	
		mcGeneralAuthoringDTO.setDefineLaterInEditMode(new Boolean(true).toString());

	    Map marksMap=authoringUtil.buildMarksMap();
	    logger.debug("marksMap: " + marksMap);
	    mcGeneralAuthoringDTO.setMarksMap(marksMap);

	    logger.debug("generating dyn pass map using listQuestionContentDTO: " +listQuestionContentDTO);
	    Map passMarksMap=authoringUtil.buildDynamicPassMarkMap(listQuestionContentDTO, false);
	    logger.debug("passMarksMap: " + passMarksMap);
	    mcGeneralAuthoringDTO.setPassMarksMap(passMarksMap);
	    
	    
        String totalMark=AuthoringUtil.getTotalMark(listQuestionContentDTO);
        logger.debug("totalMark: " + totalMark);
        mcAuthoringForm.setTotalMarks(totalMark);
        mcGeneralAuthoringDTO.setTotalMarks(totalMark);

	    
	    Map correctMap=authoringUtil.buildCorrectMap();
	    logger.debug("correctMap: " + correctMap);
	    mcGeneralAuthoringDTO.setCorrectMap(correctMap);
	    
	    
	    
	    String newQuestion=request.getParameter("newQuestion");
	    logger.debug("newQuestion: " + newQuestion);
	    mcGeneralAuthoringDTO.setEditableQuestionText(newQuestion);
	    
	    String feedback=request.getParameter("feedback");
	    logger.debug("feedback: " + feedback);
	    mcAuthoringForm.setFeedback(feedback);
	    
		String mark=request.getParameter("mark");
		logger.debug("mark: " + mark);
	    mcGeneralAuthoringDTO.setMarkValue(mark);
	    
	    
		logger.debug("before saving final mcGeneralAuthoringDTO: " + mcGeneralAuthoringDTO);
		request.setAttribute(MC_GENERAL_AUTHORING_DTO, mcGeneralAuthoringDTO);
	
		request.setAttribute(TOTAL_QUESTION_COUNT, new Integer(listQuestionContentDTO.size()));
	
		
		String editQuestionBoxRequest=request.getParameter("editQuestionBoxRequest");
		logger.debug("editQuestionBoxRequest: " + editQuestionBoxRequest);
		
		setupCommonScreenData(mcContent, mcService,request);
		
		MonitoringUtil.setupAllSessionsData(request, mcContent,mcService);

		return newQuestionBox(mapping, form,  request, response);
    }

    
    
    public ActionForward removeAddedCandidate(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) 
    throws IOException, ServletException {
		logger.debug("dispatching removeAddedCandidate");
		McAuthoringForm mcAuthoringForm = (McMonitoringForm) form;
		
		IMcService mcService =McServiceProxy.getMcService(getServlet().getServletContext());
		logger.debug("mcService: " + mcService);
	
		String httpSessionID=mcAuthoringForm.getHttpSessionID();
		logger.debug("httpSessionID: " + httpSessionID);
		
		SessionMap sessionMap=(SessionMap)request.getSession().getAttribute(httpSessionID);
		logger.debug("sessionMap: " + sessionMap);
		

		String candidateIndex=request.getParameter("candidateIndex");
		logger.debug("candidateIndex: " + candidateIndex);
		mcAuthoringForm.setCandidateIndex(candidateIndex);
		
	    String totalMarks=request.getParameter("totalMarks");
		logger.debug("totalMarks: " + totalMarks);


	    AuthoringUtil authoringUtil = new AuthoringUtil();
	    List caList=authoringUtil.repopulateCandidateAnswersBox(request, false);
	    logger.debug("repopulated caList: " + caList);

	    
	    List listQuestionContentDTO=(List)sessionMap.get(LIST_QUESTION_CONTENT_DTO_KEY);
	    logger.debug("listQuestionContentDTO: " + listQuestionContentDTO);
	    sessionMap.put(LIST_QUESTION_CONTENT_DTO_KEY, listQuestionContentDTO);

	    
		List listAddableQuestionContentDTO = (List)sessionMap.get(NEW_ADDABLE_QUESTION_CONTENT_KEY);
		logger.debug("listAddableQuestionContentDTO: " + listAddableQuestionContentDTO);

	    
	    List candidates =new LinkedList();
	    List listCandidates =new LinkedList();
	    
	    Iterator listIterator=listAddableQuestionContentDTO.iterator();
	    /*there is only 1 question dto*/
	    while (listIterator.hasNext())
	    {
	        McQuestionContentDTO mcQuestionContentDTO= (McQuestionContentDTO)listIterator.next();
	        logger.debug("mcQuestionContentDTO:" + mcQuestionContentDTO);
	        
            candidates=mcQuestionContentDTO.getListCandidateAnswersDTO();
            logger.debug("candidates found :" + candidates);
            
    	    logger.debug("mcQuestionContentDTO found:" + mcQuestionContentDTO);
    	    logger.debug("setting caList for the content:");
    	    mcQuestionContentDTO.setListCandidateAnswersDTO(caList);
    	    
    	    List candidateAnswers=mcQuestionContentDTO.getListCandidateAnswersDTO();
    	    logger.debug("candidateAnswers:" + candidateAnswers);
    	    
    	    McCandidateAnswersDTO mcCandidateAnswersDTO= null;
    	    Iterator listCaIterator=candidateAnswers.iterator();
    	    int caIndex=0;
    	    while (listCaIterator.hasNext())
    	    {
    	        caIndex ++;
    	        logger.debug("caIndex:" + caIndex);
    	        mcCandidateAnswersDTO= (McCandidateAnswersDTO)listCaIterator.next();
    	        logger.debug("mcCandidateAnswersDTO:" + mcCandidateAnswersDTO);
    	        logger.debug("mcCandidateAnswersDTO question:" + mcCandidateAnswersDTO.getCandidateAnswer());
    	        
    	        if (caIndex == new Integer(candidateIndex).intValue())
    	        {
    	            logger.debug("candidateIndex found");
    	            mcCandidateAnswersDTO.setCandidateAnswer("");
    	            
    	            
    	            break;
    	        }
    	    }
    		logger.debug("candidateAnswers after resetting answer" + candidateAnswers);
    	    
    		candidateAnswers=AuthoringUtil.reorderListCandidatesDTO(candidateAnswers);
    		logger.debug("candidateAnswers after reordering candidate nodes" + candidateAnswers);

    		mcQuestionContentDTO.setListCandidateAnswersDTO(candidateAnswers);
    		mcQuestionContentDTO.setCaCount(new Integer(candidateAnswers.size()).toString());
    		
    		logger.debug("listQuestionContentDTO after remove: " + listQuestionContentDTO);
	    }

	    
	    logger.debug("listAddableQuestionContentDTO : " + listAddableQuestionContentDTO);
		request.setAttribute(NEW_ADDABLE_QUESTION_CONTENT_LIST, listAddableQuestionContentDTO);
		sessionMap.put(NEW_ADDABLE_QUESTION_CONTENT_KEY, listAddableQuestionContentDTO);

	    
		String contentFolderID = WebUtil.readStrParam(request, AttributeNames.PARAM_CONTENT_FOLDER_ID);
		logger.debug("contentFolderID: " + contentFolderID);
		mcAuthoringForm.setContentFolderID(contentFolderID);
	
		
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
		
		String defaultContentIdStr=new Long(mcService.getToolDefaultContentIdBySignature(MY_SIGNATURE)).toString();;
		logger.debug("defaultContentIdStr: " + defaultContentIdStr);
		
		McContent mcContent=mcService.retrieveMc(new Long(strToolContentID));
		logger.debug("mcContent: " + mcContent);
		
		if (mcContent == null)
		{
			logger.debug("using defaultContentIdStr: " + defaultContentIdStr);
			mcContent=mcService.retrieveMc(new Long(defaultContentIdStr));
		}
		logger.debug("final mcContent: " + mcContent);
	
		McGeneralAuthoringDTO mcGeneralAuthoringDTO= new McGeneralAuthoringDTO();
		logger.debug("mcGeneralAuthoringDTO: " + mcGeneralAuthoringDTO);
		mcGeneralAuthoringDTO.setContentFolderID(contentFolderID);
		
	    mcGeneralAuthoringDTO.setActivityTitle(richTextTitle);
	    mcAuthoringForm.setTitle(richTextTitle);
	    
		mcGeneralAuthoringDTO.setActivityInstructions(richTextInstructions);
	
	    
		mcGeneralAuthoringDTO.setEditActivityEditMode(new Boolean(true).toString());
	    
	    request.getSession().setAttribute(httpSessionID, sessionMap);
	
		McUtils.setFormProperties(request, mcService,  
	             mcAuthoringForm, mcGeneralAuthoringDTO, strToolContentID, defaultContentIdStr, activeModule, sessionMap, httpSessionID);
	
		
		mcGeneralAuthoringDTO.setToolContentID(strToolContentID);
		mcGeneralAuthoringDTO.setHttpSessionID(httpSessionID);
		mcGeneralAuthoringDTO.setActiveModule(activeModule);
		mcGeneralAuthoringDTO.setDefaultContentIdStr(defaultContentIdStr);
		mcAuthoringForm.setToolContentID(strToolContentID);
		mcAuthoringForm.setHttpSessionID(httpSessionID);
		mcAuthoringForm.setActiveModule(activeModule);
		mcAuthoringForm.setDefaultContentIdStr(defaultContentIdStr);
		mcAuthoringForm.setCurrentTab("3");
	
	    request.setAttribute(LIST_QUESTION_CONTENT_DTO,listQuestionContentDTO);
	    logger.debug("listQuestionContentDTO now: " + listQuestionContentDTO);
	
	    mcGeneralAuthoringDTO.setDefineLaterInEditMode(new Boolean(true).toString());
	
	    Map marksMap=authoringUtil.buildMarksMap();
	    logger.debug("marksMap: " + marksMap);
	    mcGeneralAuthoringDTO.setMarksMap(marksMap);

	    logger.debug("generating dyn pass map using listQuestionContentDTO: " +listQuestionContentDTO);
	    Map passMarksMap=authoringUtil.buildDynamicPassMarkMap(listQuestionContentDTO, false);
	    logger.debug("passMarksMap: " + passMarksMap);
	    mcGeneralAuthoringDTO.setPassMarksMap(passMarksMap);
	    
	    
	    
        String totalMark=AuthoringUtil.getTotalMark(listQuestionContentDTO);
        logger.debug("totalMark: " + totalMark);
        mcAuthoringForm.setTotalMarks(totalMark);
        mcGeneralAuthoringDTO.setTotalMarks(totalMark);

        
	    Map correctMap=authoringUtil.buildCorrectMap();
	    logger.debug("correctMap: " + correctMap);
	    mcGeneralAuthoringDTO.setCorrectMap(correctMap);

	    
	    String newQuestion=request.getParameter("newQuestion");
	    logger.debug("newQuestion: " + newQuestion);
	    mcGeneralAuthoringDTO.setEditableQuestionText(newQuestion);
	    
	    String feedback=request.getParameter("feedback");
	    logger.debug("feedback: " + feedback);
	    mcAuthoringForm.setFeedback(feedback);
	    
		String mark=request.getParameter("mark");
		logger.debug("mark: " + mark);
	    mcGeneralAuthoringDTO.setMarkValue(mark);

	    
		logger.debug("before saving final mcGeneralAuthoringDTO: " + mcGeneralAuthoringDTO);
		request.setAttribute(MC_GENERAL_AUTHORING_DTO, mcGeneralAuthoringDTO);
	
		request.setAttribute(TOTAL_QUESTION_COUNT, new Integer(listQuestionContentDTO.size()));
		
		
		String editQuestionBoxRequest=request.getParameter("editQuestionBoxRequest");
		logger.debug("editQuestionBoxRequest: " + editQuestionBoxRequest);

		setupCommonScreenData(mcContent, mcService,request);
		
		MonitoringUtil.setupAllSessionsData(request, mcContent,mcService);
		return newQuestionBox(mapping, form,  request, response);

    }


    public ActionForward newAddedCandidateBox(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) 
    throws IOException, ServletException {
		logger.debug("dispatching newAddedCandidateBox");
		McAuthoringForm mcAuthoringForm = (McMonitoringForm) form;
	    
		IMcService mcService =McServiceProxy.getMcService(getServlet().getServletContext());
		logger.debug("mcService: " + mcService);
	
		String httpSessionID=mcAuthoringForm.getHttpSessionID();
		logger.debug("httpSessionID: " + httpSessionID);
		
		SessionMap sessionMap=(SessionMap)request.getSession().getAttribute(httpSessionID);
		logger.debug("sessionMap: " + sessionMap);
		

		String candidateIndex=request.getParameter("candidateIndex");
		logger.debug("candidateIndex: " + candidateIndex);
		mcAuthoringForm.setCandidateIndex(candidateIndex);

	    String totalMarks=request.getParameter("totalMarks");
		logger.debug("totalMarks: " + totalMarks);

	    
	    List listQuestionContentDTO=(List)sessionMap.get(LIST_QUESTION_CONTENT_DTO_KEY);
	    logger.debug("listQuestionContentDTO: " + listQuestionContentDTO);
	

		AuthoringUtil authoringUtil = new AuthoringUtil();
	    List caList=authoringUtil.repopulateCandidateAnswersBox(request, true);
	    logger.debug("repopulated caList: " + caList);
	    
	    int caCount=caList.size();
	    logger.debug("caCount: " + caCount);
	    

	    String newQuestion=request.getParameter("newQuestion");
	    logger.debug("newQuestion: " + newQuestion);
	    
	    String mark=request.getParameter("mark");
	    logger.debug("mark: " + mark);

	    String passmark=request.getParameter("passmark");
	    logger.debug("passmark: " + passmark);

	    
	    String feedback=request.getParameter("feedback");
	    logger.debug("feedback: " + feedback);
	    
	    int currentQuestionCount= listQuestionContentDTO.size();
	    logger.debug("currentQuestionCount: " + currentQuestionCount);
	    
	    
	    String editQuestionBoxRequest=request.getParameter("editQuestionBoxRequest");
		logger.debug("editQuestionBoxRequest: " + editQuestionBoxRequest);
		
		
		
		List listAddableQuestionContentDTO = (List)sessionMap.get(NEW_ADDABLE_QUESTION_CONTENT_KEY);
		logger.debug("listAddableQuestionContentDTO: " + listAddableQuestionContentDTO);

	    
	    List candidates =new LinkedList();
	    List listCandidates =new LinkedList();
	    
	    Iterator listIterator=listAddableQuestionContentDTO.iterator();
	    /*there is only 1 question dto*/
	    while (listIterator.hasNext())
	    {
	        McQuestionContentDTO mcQuestionContentDTO= (McQuestionContentDTO)listIterator.next();
	        logger.debug("mcQuestionContentDTO:" + mcQuestionContentDTO);
	        
	        logger.debug("caList:" + caList);
	        logger.debug("caList size:" + caList.size());
	        mcQuestionContentDTO.setListCandidateAnswersDTO(caList);
	        mcQuestionContentDTO.setCaCount(new Integer(caList.size()).toString());
	    }
	    

	    logger.debug("listAddableQuestionContentDTO after swapping (up) candidates: ");
		request.setAttribute(NEW_ADDABLE_QUESTION_CONTENT_LIST, listAddableQuestionContentDTO);
		sessionMap.put(NEW_ADDABLE_QUESTION_CONTENT_KEY, listAddableQuestionContentDTO);
		
		
		logger.debug("listQuestionContentDTO after repopulating data: " + listQuestionContentDTO);
	    sessionMap.put(LIST_QUESTION_CONTENT_DTO_KEY, listQuestionContentDTO);
	    
		String contentFolderID = WebUtil.readStrParam(request, AttributeNames.PARAM_CONTENT_FOLDER_ID);
		logger.debug("contentFolderID: " + contentFolderID);
		mcAuthoringForm.setContentFolderID(contentFolderID);
	
		
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
		
		String defaultContentIdStr=new Long(mcService.getToolDefaultContentIdBySignature(MY_SIGNATURE)).toString();;
		logger.debug("defaultContentIdStr: " + defaultContentIdStr);
		
		McGeneralAuthoringDTO mcGeneralAuthoringDTO= new McGeneralAuthoringDTO();
		logger.debug("mcGeneralAuthoringDTO: " + mcGeneralAuthoringDTO);
		mcGeneralAuthoringDTO.setContentFolderID(contentFolderID);
		
	    mcGeneralAuthoringDTO.setActivityTitle(richTextTitle);
	    mcAuthoringForm.setTitle(richTextTitle);
	    
		mcGeneralAuthoringDTO.setActivityInstructions(richTextInstructions);
		
	    mcGeneralAuthoringDTO.setEditActivityEditMode(new Boolean(true).toString());
	    
	    request.getSession().setAttribute(httpSessionID, sessionMap);
	
		McUtils.setFormProperties(request, mcService,  
	             mcAuthoringForm, mcGeneralAuthoringDTO, strToolContentID, defaultContentIdStr, activeModule, sessionMap, httpSessionID);
	
		
		mcGeneralAuthoringDTO.setToolContentID(strToolContentID);
		mcGeneralAuthoringDTO.setHttpSessionID(httpSessionID);
		mcGeneralAuthoringDTO.setActiveModule(activeModule);
		mcGeneralAuthoringDTO.setDefaultContentIdStr(defaultContentIdStr);
		mcAuthoringForm.setToolContentID(strToolContentID);
		mcAuthoringForm.setHttpSessionID(httpSessionID);
		mcAuthoringForm.setActiveModule(activeModule);
		mcAuthoringForm.setDefaultContentIdStr(defaultContentIdStr);
		mcAuthoringForm.setCurrentTab("3");
	
	    request.setAttribute(LIST_QUESTION_CONTENT_DTO,listQuestionContentDTO);
		logger.debug("listQuestionContentDTO now: " + listQuestionContentDTO);
	
	    mcGeneralAuthoringDTO.setDefineLaterInEditMode(new Boolean(true).toString());
	
	    Map marksMap=authoringUtil.buildMarksMap();
	    logger.debug("marksMap: " + marksMap);
	    mcGeneralAuthoringDTO.setMarksMap(marksMap);

	    logger.debug("generating dyn pass map using listQuestionContentDTO: " +listQuestionContentDTO);
	    Map passMarksMap=authoringUtil.buildDynamicPassMarkMap(listQuestionContentDTO, false);
	    logger.debug("passMarksMap: " + passMarksMap);
	    mcGeneralAuthoringDTO.setPassMarksMap(passMarksMap);
	    
        String totalMark=AuthoringUtil.getTotalMark(listQuestionContentDTO);
        logger.debug("totalMark: " + totalMark);
        mcAuthoringForm.setTotalMarks(totalMark);
        mcGeneralAuthoringDTO.setTotalMarks(totalMark);

	    
	    Map correctMap=authoringUtil.buildCorrectMap();
	    logger.debug("correctMap: " + correctMap);
	    mcGeneralAuthoringDTO.setCorrectMap(correctMap);

	    mcGeneralAuthoringDTO.setEditableQuestionText(newQuestion);
	    mcAuthoringForm.setFeedback(feedback);
	    mcGeneralAuthoringDTO.setMarkValue(mark);

	    
		logger.debug("before saving final mcGeneralAuthoringDTO: " + mcGeneralAuthoringDTO);
		request.setAttribute(MC_GENERAL_AUTHORING_DTO, mcGeneralAuthoringDTO);
	
		request.setAttribute(TOTAL_QUESTION_COUNT, new Integer(listQuestionContentDTO.size()));
		
		logger.debug("editQuestionBoxRequest: " + editQuestionBoxRequest);

		McContent mcContent=mcService.retrieveMc(new Long(strToolContentID));
		logger.debug("mcContent: " + mcContent);

		setupCommonScreenData(mcContent, mcService,request);
		
		MonitoringUtil.setupAllSessionsData(request, mcContent,mcService);
		return newQuestionBox(mapping, form,  request, response);
    }
    
    
    
    
    
    
    /**
     * prepareReflectionData(HttpServletRequest request, McContent mcContent, 
	        IMcService mcService, String userID, boolean exportMode)
     * 
     * prepares reflection data
     * 
     * @param request
     * @param mcContent
     * @param mcService
     * @param userID
     * @param exportMode
     */
	public void prepareReflectionData(HttpServletRequest request, McContent mcContent, 
	        IMcService mcService, String userID, boolean exportMode)
	{
	    logger.debug("starting prepareReflectionData: " + mcContent);
	    logger.debug("exportMode: " + exportMode);
	    List reflectionsContainerDTO= new LinkedList();
	    
	    if (userID == null)
	    {
	        logger.debug("all users mode");
		    for (Iterator sessionIter = mcContent.getMcSessions().iterator(); sessionIter.hasNext();) 
		    {
		        McSession mcSession = (McSession) sessionIter.next();
		        logger.debug("mcSession: " + mcSession);
		       for (Iterator userIter = mcSession.getMcQueUsers().iterator(); userIter.hasNext();) 
		       {
					McQueUsr user = (McQueUsr) userIter.next();
					logger.debug("user: " + user);

					NotebookEntry notebookEntry = mcService.getEntry(mcSession.getMcSessionId(),
							CoreNotebookConstants.NOTEBOOK_TOOL,
							MY_SIGNATURE, new Integer(user.getQueUsrId().toString()));
					
					logger.debug("notebookEntry: " + notebookEntry);
					
					if (notebookEntry != null) {
					    ReflectionDTO reflectionDTO = new ReflectionDTO();
					    reflectionDTO.setUserId(user.getQueUsrId().toString());
					    reflectionDTO.setSessionId(mcSession.getMcSessionId().toString());
					    reflectionDTO.setUserName(user.getUsername());
					    reflectionDTO.setReflectionUid (notebookEntry.getUid().toString());
					    String notebookEntryPresentable=McUtils.replaceNewLines(notebookEntry.getEntry());
					    reflectionDTO.setEntry(notebookEntryPresentable);
					    reflectionsContainerDTO.add(reflectionDTO);
					} 
					
		       }
		   }
	    }
	    else
	    {
			logger.debug("single user mode");
		    for (Iterator sessionIter = mcContent.getMcSessions().iterator(); sessionIter.hasNext();) 
		    {
		        McSession mcSession = (McSession) sessionIter.next();
		        logger.debug("mcSession: " + mcSession);
		       for (Iterator userIter = mcSession.getMcQueUsers().iterator(); userIter.hasNext();) 
		       {
					McQueUsr user = (McQueUsr) userIter.next();
					logger.debug("user: " + user);
					
					if (user.getQueUsrId().toString().equals(userID))
					{
						NotebookEntry notebookEntry = mcService.getEntry(mcSession.getMcSessionId(),
								CoreNotebookConstants.NOTEBOOK_TOOL,
								MY_SIGNATURE, new Integer(user.getQueUsrId().toString()));
						
						logger.debug("notebookEntry: " + notebookEntry);
						
						if (notebookEntry != null) {
						    ReflectionDTO reflectionDTO = new ReflectionDTO();
						    reflectionDTO.setUserId(user.getQueUsrId().toString());
						    reflectionDTO.setSessionId(mcSession.getMcSessionId().toString());
						    reflectionDTO.setUserName(user.getUsername());
						    reflectionDTO.setReflectionUid (notebookEntry.getUid().toString());
						    String notebookEntryPresentable=McUtils.replaceNewLines(notebookEntry.getEntry());
						    reflectionDTO.setEntry(notebookEntryPresentable);
						    reflectionsContainerDTO.add(reflectionDTO);
						} 
					    
					}
		       }
		   }	        
	        
	    }
	    
	    
	   logger.debug("reflectionsContainerDTO: " + reflectionsContainerDTO);
	   request.getSession().setAttribute(REFLECTIONS_CONTAINER_DTO, reflectionsContainerDTO);
	   
	   if (exportMode)
	   {
	       request.getSession().setAttribute(REFLECTIONS_CONTAINER_DTO, reflectionsContainerDTO);
	   }
	}


	/**
	 * ActionForward openNotebook(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response)
	 * 
	 * allows viewing users reflection data
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws IOException
	 * @throws ServletException
	 * @throws ToolException
	 */
	public ActionForward openNotebook(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws IOException,
            ServletException, ToolException
    {
        logger.debug("dispatching openNotebook...");
    	IMcService mcService = McServiceProxy.getMcService(getServlet().getServletContext());
		logger.debug("mcService: " + mcService);

		
		String uid=request.getParameter("uid");
		logger.debug("uid: " + uid);
		
		String userId=request.getParameter("userId");
		logger.debug("userId: " + userId);
		
		String userName=request.getParameter("userName");
		logger.debug("userName: " + userName);

		String sessionId=request.getParameter("sessionId");
		logger.debug("sessionId: " + sessionId);
		
		
		NotebookEntry notebookEntry = mcService.getEntry(new Long(sessionId),
				CoreNotebookConstants.NOTEBOOK_TOOL,
				MY_SIGNATURE, new Integer(userId));
		
        logger.debug("notebookEntry: " + notebookEntry);
		
        McGeneralLearnerFlowDTO mcGeneralLearnerFlowDTO= new McGeneralLearnerFlowDTO();
		if (notebookEntry != null) {
		    String notebookEntryPresentable=McUtils.replaceNewLines(notebookEntry.getEntry());
		    mcGeneralLearnerFlowDTO.setNotebookEntry(notebookEntryPresentable);
		    mcGeneralLearnerFlowDTO.setUserName(userName);
		}

		logger.debug("mcGeneralLearnerFlowDTO: " + mcGeneralLearnerFlowDTO);
		request.setAttribute(MC_GENERAL_LEARNER_FLOW_DTO, mcGeneralLearnerFlowDTO);
		
		return mapping.findForward(LEARNER_NOTEBOOK);
	}
	
	
	/**
	 * prepareReflectionData(HttpServletRequest request, McContent mcContent, 
	        IMcService mcService, String userID, boolean exportMode, String currentSessionId)
	 * 
	 * @param request
	 * @param mcContent
	 * @param mcService
	 * @param userID
	 * @param exportMode
	 * @param currentSessionId
	 */
    public void prepareReflectionData(HttpServletRequest request, McContent mcContent, 
	        IMcService mcService, String userID, boolean exportMode, String currentSessionId)
	{
	    logger.debug("starting prepareReflectionData: " + mcContent);
	    logger.debug("currentSessionId: " + currentSessionId);
	    logger.debug("userID: " + userID);
	    logger.debug("exportMode: " + exportMode);


	    List reflectionsContainerDTO= new LinkedList();
	    
	    reflectionsContainerDTO=getReflectionList(mcContent, userID, mcService);	    
	    
	   logger.debug("reflectionsContainerDTO: " + reflectionsContainerDTO);
	   request.setAttribute(REFLECTIONS_CONTAINER_DTO, reflectionsContainerDTO);
	   
	   if (exportMode)
	   {
	       request.getSession().setAttribute(REFLECTIONS_CONTAINER_DTO, reflectionsContainerDTO);
	   }
	}
    

    /**
     * List getReflectionList(McContent mcContent, String userID, IMcService mcService)
     * 
     * returns reflection data for all sessions
     * 
     * @param mcContent
     * @param userID
     * @param mcService
     * @return
     */
    public List getReflectionList(McContent mcContent, String userID, IMcService mcService)
    {
        logger.debug("getting reflections for all sessions");
        List reflectionsContainerDTO= new LinkedList();
        if (userID == null)
        {
            logger.debug("all users mode");
    	    for (Iterator sessionIter = mcContent.getMcSessions().iterator(); sessionIter.hasNext();) 
    	    {
    	       McSession mcSession = (McSession) sessionIter.next();
    	       logger.debug("mcSession: " + mcSession);
    	       logger.debug("mcSession sessionId: " + mcSession.getMcSessionId());
    	       
    	       for (Iterator userIter = mcSession.getMcQueUsers().iterator(); userIter.hasNext();) 
    	       {
    				McQueUsr user = (McQueUsr) userIter.next();
    				logger.debug("user: " + user);

    				NotebookEntry notebookEntry = mcService.getEntry(mcSession.getMcSessionId(),
    						CoreNotebookConstants.NOTEBOOK_TOOL,
    						MY_SIGNATURE, new Integer(user.getQueUsrId().toString()));
    				
    				logger.debug("notebookEntry: " + notebookEntry);
    				
    				if (notebookEntry != null) {
    				    ReflectionDTO reflectionDTO = new ReflectionDTO();
    				    reflectionDTO.setUserId(user.getQueUsrId().toString());
    				    reflectionDTO.setSessionId(mcSession.getMcSessionId().toString());
    				    reflectionDTO.setUserName(user.getFullname());
    				    reflectionDTO.setReflectionUid (notebookEntry.getUid().toString());
    				    String notebookEntryPresentable=McUtils.replaceNewLines(notebookEntry.getEntry());
    				    reflectionDTO.setEntry(notebookEntryPresentable);
    				    reflectionsContainerDTO.add(reflectionDTO);
    				} 
    	       }
    	   }
       }
       else
       {
           logger.debug("single user mode");
    	    for (Iterator sessionIter = mcContent.getMcSessions().iterator(); sessionIter.hasNext();) 
    	    {
    	       McSession mcSession = (McSession) sessionIter.next();
    	       logger.debug("mcSession: " + mcSession);
    	       for (Iterator userIter = mcSession.getMcQueUsers().iterator(); userIter.hasNext();) 
    	       {
    				McQueUsr user = (McQueUsr) userIter.next();
    				logger.debug("user: " + user);
    				
    				if (user.getQueUsrId().toString().equals(userID))
    				{
    				    logger.debug("getting reflection for user with  userID: " + userID);
    					NotebookEntry notebookEntry = mcService.getEntry(mcSession.getMcSessionId(),
    							CoreNotebookConstants.NOTEBOOK_TOOL,
    							MY_SIGNATURE, new Integer(user.getQueUsrId().toString()));
    					
    					logger.debug("notebookEntry: " + notebookEntry);
    					
    					if (notebookEntry != null) {
    					    ReflectionDTO reflectionDTO = new ReflectionDTO();
    					    reflectionDTO.setUserId(user.getQueUsrId().toString());
    					    reflectionDTO.setSessionId(mcSession.getMcSessionId().toString());
    					    reflectionDTO.setUserName(user.getFullname());
    					    reflectionDTO.setReflectionUid (notebookEntry.getUid().toString());
    					    String notebookEntryPresentable=McUtils.replaceNewLines(notebookEntry.getEntry());
    					    reflectionDTO.setEntry(notebookEntryPresentable);
    					    reflectionsContainerDTO.add(reflectionDTO);
    					} 
    				}
    	       }		       
    	    }
       }
        
        return reflectionsContainerDTO;
    }
    
    /**
     * 
     * List getReflectionListForSession(McContent mcContent, String userID, IMcService mcService, String currentSessionId)

     * returns reflection data for a specific session
     * 
     * @param mcContent
     * @param userID
     * @param mcService
     * @param currentSessionId
     * @return
     */
    public List getReflectionListForSession(McContent mcContent, String userID, IMcService mcService, String currentSessionId)
    {
        logger.debug("getting reflections for a specific session");
        logger.debug("currentSessionId: " + currentSessionId);
        
        List reflectionsContainerDTO= new LinkedList();
        if (userID == null)
        {
            logger.debug("all users mode");
    	    for (Iterator sessionIter = mcContent.getMcSessions().iterator(); sessionIter.hasNext();) 
    	    {
    	       McSession mcSession = (McSession) sessionIter.next();
    	       logger.debug("mcSession: " + mcSession);
    	       logger.debug("mcSession sessionId: " + mcSession.getMcSessionId());
    	       
    	       if (currentSessionId.equals(mcSession.getMcSessionId()))
    	       {
    	           
        	       for (Iterator userIter = mcSession.getMcQueUsers().iterator(); userIter.hasNext();) 
        	       {
        				McQueUsr user = (McQueUsr) userIter.next();
        				logger.debug("user: " + user);

        				NotebookEntry notebookEntry = mcService.getEntry(mcSession.getMcSessionId(),
        						CoreNotebookConstants.NOTEBOOK_TOOL,
        						MY_SIGNATURE, new Integer(user.getQueUsrId().toString()));
        				
        				logger.debug("notebookEntry: " + notebookEntry);
        				
        				if (notebookEntry != null) {
        				    ReflectionDTO reflectionDTO = new ReflectionDTO();
        				    reflectionDTO.setUserId(user.getQueUsrId().toString());
        				    reflectionDTO.setSessionId(mcSession.getMcSessionId().toString());
        				    reflectionDTO.setUserName(user.getFullname());
        				    reflectionDTO.setReflectionUid (notebookEntry.getUid().toString());
        				    String notebookEntryPresentable=McUtils.replaceNewLines(notebookEntry.getEntry());
        				    reflectionDTO.setEntry(notebookEntryPresentable);
        				    reflectionsContainerDTO.add(reflectionDTO);
        				} 
        	       }
    	       }
    	   }
       }
       else
       {
           logger.debug("single user mode");
    	    for (Iterator sessionIter = mcContent.getMcSessions().iterator(); sessionIter.hasNext();) 
    	    {
    	       McSession mcSession = (McSession) sessionIter.next();
    	       logger.debug("mcSession: " + mcSession);
    	       
    	       if (currentSessionId.equals(mcSession.getMcSessionId()))
    	       {
        	       for (Iterator userIter = mcSession.getMcQueUsers().iterator(); userIter.hasNext();) 
        	       {
        				McQueUsr user = (McQueUsr) userIter.next();
        				logger.debug("user: " + user);
        				
        				if (user.getQueUsrId().toString().equals(userID))
        				{
        				    logger.debug("getting reflection for user with  userID: " + userID);
        					NotebookEntry notebookEntry = mcService.getEntry(mcSession.getMcSessionId(),
        							CoreNotebookConstants.NOTEBOOK_TOOL,
        							MY_SIGNATURE, new Integer(user.getQueUsrId().toString()));
        					
        					logger.debug("notebookEntry: " + notebookEntry);
        					
        					if (notebookEntry != null) {
        					    ReflectionDTO reflectionDTO = new ReflectionDTO();
        					    reflectionDTO.setUserId(user.getQueUsrId().toString());
        					    reflectionDTO.setSessionId(mcSession.getMcSessionId().toString());
        					    reflectionDTO.setUserName(user.getFullname());
        					    reflectionDTO.setReflectionUid (notebookEntry.getUid().toString());
        					    String notebookEntryPresentable=McUtils.replaceNewLines(notebookEntry.getEntry());
        					    reflectionDTO.setEntry(notebookEntryPresentable);
        					    reflectionsContainerDTO.add(reflectionDTO);
        					} 
        				}
        	       }	
    	           
    	       }
    	    }
       }
        
        return reflectionsContainerDTO;
    }


    /**
     * prepareEditActivityScreenData(HttpServletRequest request, McContent mcContent)
     * 
     * prepareEditActivityScreenData
     * 
     * @param request
     * @param mcContent
     */
	public void prepareEditActivityScreenData(HttpServletRequest request, McContent mcContent)
	{
		logger.debug("starting prepareEditActivityScreenData: " + mcContent);
	    McGeneralAuthoringDTO mcGeneralAuthoringDTO= new McGeneralAuthoringDTO();
		
		mcGeneralAuthoringDTO.setActivityTitle(mcContent.getTitle());
		mcGeneralAuthoringDTO.setActivityInstructions(mcContent.getInstructions());
		
		logger.debug("final mcGeneralAuthoringDTO: " + mcGeneralAuthoringDTO);
		request.setAttribute(MC_GENERAL_AUTHORING_DTO, mcGeneralAuthoringDTO);
	}

	
	/**
	 * initSummaryContent(String toolContentID,
            HttpServletRequest request,
            IMcService mcService,
            McGeneralMonitoringDTO mcGeneralMonitoringDTO)
	 * 
	 * @param toolContentID
	 * @param request
	 * @param mcService
	 * @param mcGeneralMonitoringDTO
	 * @throws IOException
	 * @throws ServletException
	 */
    public void initSummaryContent(String toolContentID,
            HttpServletRequest request,
            IMcService mcService,
            McGeneralMonitoringDTO mcGeneralMonitoringDTO) throws IOException,
                                         ServletException
	{
    	logger.debug("start  initSummaryContent...toolContentID: " + toolContentID);
    	logger.debug("dispatching getSummary...mcGeneralMonitoringDTO:" + mcGeneralMonitoringDTO);
   	    	
		logger.debug("mcService: " + mcService);
	    logger.debug("toolContentID: " + toolContentID);
	    
	    McContent mcContent=mcService.retrieveMc(new Long(toolContentID));
		logger.debug("existing mcContent:" + mcContent);
		
    	/* this section is related to summary tab. Starts here. */
		Map summaryToolSessions=MonitoringUtil.populateToolSessions(request, mcContent, mcService);
		logger.debug("summaryToolSessions: " + summaryToolSessions);
		mcGeneralMonitoringDTO.setSummaryToolSessions(summaryToolSessions);
		
		Map summaryToolSessionsId=MonitoringUtil.populateToolSessionsId(request, mcContent, mcService);
		logger.debug("summaryToolSessionsId: " + summaryToolSessionsId);
		mcGeneralMonitoringDTO.setSummaryToolSessionsId(summaryToolSessionsId);
	    /* ends here. */
	    
		/*true means there is at least 1 response*/
		if (mcService.studentActivityOccurredGlobal(mcContent))
		{
		    mcGeneralMonitoringDTO.setUserExceptionNoToolSessions(new Boolean(false).toString());
			logger.debug("USER_EXCEPTION_NO_TOOL_SESSIONS is set to false");
		}
		else
		{
			mcGeneralMonitoringDTO.setUserExceptionNoToolSessions(new Boolean(true).toString());
			logger.debug("USER_EXCEPTION_NO_TOOL_SESSIONS is set to true");
		}

		prepareReflectionData(request, mcContent, mcService, null, false, "All");
		
    	EditActivityDTO editActivityDTO = new EditActivityDTO();
		boolean isContentInUse=McUtils.isContentInUse(mcContent);
		logger.debug("isContentInUse:" + isContentInUse);
		if (isContentInUse == true)
		{
		    editActivityDTO.setMonitoredContentInUse(new Boolean(true).toString());
		}
		request.setAttribute(EDIT_ACTIVITY_DTO, editActivityDTO);
		
		/**getting instructions screen content from here... */
		mcGeneralMonitoringDTO.setOnlineInstructions(mcContent.getOnlineInstructions());
		mcGeneralMonitoringDTO.setOfflineInstructions(mcContent.getOfflineInstructions());
		
        List attachmentList = mcService.retrieveMcUploadedFiles(mcContent);
        logger.debug("attachmentList: " + attachmentList);
        mcGeneralMonitoringDTO.setAttachmentList(attachmentList);
        mcGeneralMonitoringDTO.setDeletedAttachmentList(new ArrayList());
        /** ...till here **/

		
		mcGeneralMonitoringDTO.setCurrentMonitoringTab("summary");

	    logger.debug("end of refreshSummaryData, mcGeneralMonitoringDTO" + mcGeneralMonitoringDTO);
		request.setAttribute(MC_GENERAL_MONITORING_DTO, mcGeneralMonitoringDTO);

		/*find out if there are any reflection entries, from here*/
		boolean notebookEntriesExist=MonitoringUtil.notebookEntriesExist(mcService, mcContent);
		logger.debug("notebookEntriesExist : " + notebookEntriesExist);
		
		if (notebookEntriesExist)
		{
		    request.setAttribute(NOTEBOOK_ENTRIES_EXIST, new Boolean(true).toString());
		    
		    String userExceptionNoToolSessions=(String)mcGeneralMonitoringDTO.getUserExceptionNoToolSessions();
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
		/* ... till here*/
		
		MonitoringUtil.buildMcStatsDTO(request,mcService, mcContent);
		
		MonitoringUtil.setupAllSessionsData(request, mcContent,mcService);

    	logger.debug("end  initSummaryContent...");
	}
    

    /**
     * void initStatsContent(String toolContentID,
            HttpServletRequest request,
            IMcService mcService, 
            McGeneralMonitoringDTO mcGeneralMonitoringDTO)
     * 
     * @param toolContentID
     * @param request
     * @param mcService
     * @param mcGeneralMonitoringDTO
     * @throws IOException
     * @throws ServletException
     */
    public void initStatsContent(String toolContentID,
            HttpServletRequest request,
            IMcService mcService, 
            McGeneralMonitoringDTO mcGeneralMonitoringDTO) throws IOException,
                                         ServletException
	{
    	logger.debug("starting  initStatsContent...:" + toolContentID);
    	logger.debug("dispatching getStats..." + request);
		logger.debug("mcService: " + mcService);
		
	    McContent mcContent=mcService.retrieveMc(new Long(toolContentID));
		logger.debug("existing mcContent:" + mcContent);
		
		if (mcService.studentActivityOccurredGlobal(mcContent))
		{
			mcGeneralMonitoringDTO.setUserExceptionNoToolSessions(new Boolean(false).toString());
			logger.debug("USER_EXCEPTION_NO_TOOL_SESSIONS is set to false");
		}
		else
		{
			mcGeneralMonitoringDTO.setUserExceptionNoToolSessions(new Boolean(true).toString());
			logger.debug("USER_EXCEPTION_NO_TOOL_SESSIONS is set to true");
		}

		
    	refreshStatsData(request, mcService,mcGeneralMonitoringDTO);
    	logger.debug("post refreshStatsData, mcGeneralMonitoringDTO: " + mcGeneralMonitoringDTO );
    	
    	prepareReflectionData(request, mcContent, mcService, null, false, "All");

    	EditActivityDTO editActivityDTO = new EditActivityDTO();
		boolean isContentInUse=McUtils.isContentInUse(mcContent);
		logger.debug("isContentInUse:" + isContentInUse);
		if (isContentInUse == true)
		{
		    editActivityDTO.setMonitoredContentInUse(new Boolean(true).toString());
		}
		request.setAttribute(EDIT_ACTIVITY_DTO, editActivityDTO);

		/**getting instructions screen content from here... */
		mcGeneralMonitoringDTO.setOnlineInstructions(mcContent.getOnlineInstructions());
		mcGeneralMonitoringDTO.setOfflineInstructions(mcContent.getOfflineInstructions());
		
        List attachmentList = mcService.retrieveMcUploadedFiles(mcContent);
        logger.debug("attachmentList: " + attachmentList);
        mcGeneralMonitoringDTO.setAttachmentList(attachmentList);
        mcGeneralMonitoringDTO.setDeletedAttachmentList(new ArrayList());
        /** ...till here **/

		
    	mcGeneralMonitoringDTO.setCurrentMonitoringTab("stats");
	    
    	logger.debug("end of refreshSummaryData, mcGeneralMonitoringDTO" + mcGeneralMonitoringDTO);
		request.setAttribute(MC_GENERAL_MONITORING_DTO, mcGeneralMonitoringDTO);

    	
		/*find out if there are any reflection entries, from here*/
		boolean notebookEntriesExist=MonitoringUtil.notebookEntriesExist(mcService, mcContent);
		logger.debug("notebookEntriesExist : " + notebookEntriesExist);
		
		if (notebookEntriesExist)
		{
		    request.setAttribute(NOTEBOOK_ENTRIES_EXIST, new Boolean(true).toString());
		    
		    String userExceptionNoToolSessions=(String)mcGeneralMonitoringDTO.getUserExceptionNoToolSessions();
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
		/* ... till here*/
		
		MonitoringUtil.buildMcStatsDTO(request,mcService, mcContent);
		
		MonitoringUtil.setupAllSessionsData(request, mcContent,mcService);
		
    	logger.debug("ending  initStatsContent...");
	}

    
    /**
     * void refreshStatsData(HttpServletRequest request, IMcService mcService, 
	        McGeneralMonitoringDTO mcGeneralMonitoringDTO)
     * 
     * @param request
     * @param mcService
     * @param mcGeneralMonitoringDTO
     */
	public void refreshStatsData(HttpServletRequest request, IMcService mcService, 
	        McGeneralMonitoringDTO mcGeneralMonitoringDTO)
	{
	    logger.debug("starting refreshStatsData: " + mcService);
		/* it is possible that no users has ever logged in for the activity yet*/
		if (mcService == null)
		{
			logger.debug("will retrieve mcService");
			mcService = McServiceProxy.getMcService(getServlet().getServletContext());
			logger.debug("retrieving mcService from session: " + mcService);
		}
		
	    int countAllUsers=mcService.getTotalNumberOfUsers();
		logger.debug("countAllUsers: " + countAllUsers);
		
		if (countAllUsers == 0)
		{
	    	logger.debug("error: countAllUsers is 0");
	    	mcGeneralMonitoringDTO.setUserExceptionNoStudentActivity(new Boolean(true).toString());
		}
		
		mcGeneralMonitoringDTO.setCountAllUsers(new Integer(countAllUsers).toString());
 		
		int countSessionComplete=mcService.countSessionComplete();
		logger.debug("countSessionComplete: " + countSessionComplete);
		mcGeneralMonitoringDTO.setCountSessionComplete(new Integer(countSessionComplete).toString());
		

		logger.debug("end of refreshStatsData, mcGeneralMonitoringDTO" + mcGeneralMonitoringDTO);
		request.setAttribute(MC_GENERAL_MONITORING_DTO, mcGeneralMonitoringDTO);
	}
	

	
	/**
	 * void repopulateRequestParameters(HttpServletRequest request, McMonitoringForm mcMonitoringForm, 
            McGeneralMonitoringDTO mcGeneralMonitoringDTO)
	 * 
	 * @param request
	 * @param mcMonitoringForm
	 * @param mcGeneralMonitoringDTO
	 */
    protected void repopulateRequestParameters(HttpServletRequest request, McMonitoringForm mcMonitoringForm, 
            McGeneralMonitoringDTO mcGeneralMonitoringDTO)
    {
        logger.debug("starting repopulateRequestParameters");
	
        String toolContentID=request.getParameter(TOOL_CONTENT_ID);
        logger.debug("toolContentID: " + toolContentID);
        mcMonitoringForm.setToolContentID(toolContentID);
        mcGeneralMonitoringDTO.setToolContentID(toolContentID);
        
        String activeModule=request.getParameter(ACTIVE_MODULE);
        logger.debug("activeModule: " + activeModule);
        mcMonitoringForm.setActiveModule(activeModule);
        mcGeneralMonitoringDTO.setActiveModule(activeModule);
        
        String defineLaterInEditMode=request.getParameter(DEFINE_LATER_IN_EDIT_MODE);
        logger.debug("defineLaterInEditMode: " + defineLaterInEditMode);
        mcMonitoringForm.setDefineLaterInEditMode(defineLaterInEditMode);
        mcGeneralMonitoringDTO.setDefineLaterInEditMode(defineLaterInEditMode);

        
        String isToolSessionChanged=request.getParameter(IS_TOOL_SESSION_CHANGED);
        logger.debug("isToolSessionChanged: " + isToolSessionChanged);
        mcMonitoringForm.setIsToolSessionChanged(isToolSessionChanged);
        mcGeneralMonitoringDTO.setIsToolSessionChanged(isToolSessionChanged);

        String responseId=request.getParameter(RESPONSE_ID);
        logger.debug("responseId: " + responseId);
        mcMonitoringForm.setResponseId(responseId);
        mcGeneralMonitoringDTO.setResponseId(responseId);

        String currentUid=request.getParameter(CURRENT_UID);
        logger.debug("currentUid: " + currentUid);
        mcMonitoringForm.setCurrentUid(currentUid);
        mcGeneralMonitoringDTO.setCurrentUid(currentUid);
    }

    
    /**
     * void setupCommonScreenData(McContent mcContent, IMcService mcService, HttpServletRequest request)
     * 
     * @param mcContent
     * @param mcService
     * @param request
     */
    protected void setupCommonScreenData(McContent mcContent, IMcService mcService, HttpServletRequest request)
    {
        logger.debug("starting setupCommonScreenData, mcContent " + mcContent);
    	/*setting up USER_EXCEPTION_NO_TOOL_SESSIONS, from here */
    	McGeneralMonitoringDTO mcGeneralMonitoringDTO= new McGeneralMonitoringDTO();
    	mcGeneralMonitoringDTO.setDefineLaterInEditMode(new Boolean(true).toString());
    	
    	if (mcService.studentActivityOccurredGlobal(mcContent))
    	{
    		logger.debug("USER_EXCEPTION_NO_TOOL_SESSIONS is set to false");
    		mcGeneralMonitoringDTO.setUserExceptionNoToolSessions(new Boolean(false).toString());
    	}
    	else
    	{
    		logger.debug("USER_EXCEPTION_NO_TOOL_SESSIONS is set to true");
    		mcGeneralMonitoringDTO.setUserExceptionNoToolSessions(new Boolean(true).toString());
    	}
    	
    	
    	mcGeneralMonitoringDTO.setOnlineInstructions(mcContent.getOnlineInstructions());
    	mcGeneralMonitoringDTO.setOfflineInstructions(mcContent.getOfflineInstructions());
    	
        List attachmentList = mcService.retrieveMcUploadedFiles(mcContent);
        logger.debug("attachmentList: " + attachmentList);
        mcGeneralMonitoringDTO.setAttachmentList(attachmentList);
        mcGeneralMonitoringDTO.setDeletedAttachmentList(new ArrayList());
        

    	logger.debug("end of refreshSummaryData, mcGeneralMonitoringDTO" + mcGeneralMonitoringDTO);
    	request.setAttribute(MC_GENERAL_MONITORING_DTO, mcGeneralMonitoringDTO);
    	/*.. till here*/
    	
    	
    	/*find out if there are any reflection entries, from here*/
    	boolean notebookEntriesExist=MonitoringUtil.notebookEntriesExist(mcService, mcContent);
    	logger.debug("notebookEntriesExist : " + notebookEntriesExist);
    	
    	if (notebookEntriesExist)
    	{
    	    request.setAttribute(NOTEBOOK_ENTRIES_EXIST, new Boolean(true).toString());
    	    
    	    String userExceptionNoToolSessions=(String)mcGeneralMonitoringDTO.getUserExceptionNoToolSessions();
    	    logger.debug(": " + userExceptionNoToolSessions);
    	    
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
    	/* ... till here*/

    	MonitoringUtil.buildMcStatsDTO(request,mcService, mcContent);
    	MonitoringUtil.generateGroupsSessionData(request, mcService, mcContent);
    	MonitoringUtil.setupAllSessionsData(request, mcContent,mcService);
    	logger.debug("ending setupCommonScreenData, mcContent " + mcContent);
    }
    
}
    
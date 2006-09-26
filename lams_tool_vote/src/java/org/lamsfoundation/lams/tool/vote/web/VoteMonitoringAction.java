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

package org.lamsfoundation.lams.tool.vote.web;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.lamsfoundation.lams.notebook.model.NotebookEntry;
import org.lamsfoundation.lams.notebook.service.CoreNotebookConstants;
import org.lamsfoundation.lams.tool.exception.ToolException;
import org.lamsfoundation.lams.tool.vote.EditActivityDTO;
import org.lamsfoundation.lams.tool.vote.ExportPortfolioDTO;
import org.lamsfoundation.lams.tool.vote.ReflectionDTO;
import org.lamsfoundation.lams.tool.vote.VoteAppConstants;
import org.lamsfoundation.lams.tool.vote.VoteApplicationException;
import org.lamsfoundation.lams.tool.vote.VoteComparator;
import org.lamsfoundation.lams.tool.vote.VoteGeneralAuthoringDTO;
import org.lamsfoundation.lams.tool.vote.VoteGeneralLearnerFlowDTO;
import org.lamsfoundation.lams.tool.vote.VoteGeneralMonitoringDTO;
import org.lamsfoundation.lams.tool.vote.VoteMonitoredAnswersDTO;
import org.lamsfoundation.lams.tool.vote.VoteMonitoredUserDTO;
import org.lamsfoundation.lams.tool.vote.VoteNominationContentDTO;
import org.lamsfoundation.lams.tool.vote.VoteUtils;
import org.lamsfoundation.lams.tool.vote.pojos.VoteContent;
import org.lamsfoundation.lams.tool.vote.pojos.VoteQueContent;
import org.lamsfoundation.lams.tool.vote.pojos.VoteQueUsr;
import org.lamsfoundation.lams.tool.vote.pojos.VoteSession;
import org.lamsfoundation.lams.tool.vote.pojos.VoteUsrAttempt;
import org.lamsfoundation.lams.tool.vote.service.IVoteService;
import org.lamsfoundation.lams.tool.vote.service.VoteServiceProxy;
import org.lamsfoundation.lams.util.WebUtil;
import org.lamsfoundation.lams.web.action.LamsDispatchAction;
import org.lamsfoundation.lams.web.util.AttributeNames;
import org.lamsfoundation.lams.web.util.SessionMap;

/**
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
 *  @author Ozgur Demirtas
 * 
 *    <!--Monitoring Main Action: interacts with the Monitoring module user -->
   <action 	path="/monitoring" 
   			type="org.lamsfoundation.lams.tool.vote.web.VoteMonitoringAction" 
   			name="VoteMonitoringForm" 
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
        	name="voteNominationViewer" 
        	path="/monitoring/VoteNominationViewer.jsp" 
        	redirect="false"
       	/>	      

        <forward
          name="learnerNotebook"
          path="/monitoring/LearnerNotebook.jsp"
          redirect="false"
        />


	    <forward
			name="newNominationBox"
			path="/monitoring/newNominationBox.jsp"
			redirect="false"
	    />

	    <forward
			name="editNominationBox"
			path="/monitoring/editNominationBox.jsp"
			redirect="false"
	    />


	  	<forward
		    name="errorList"
	        path="/VoteErrorBox.jsp"
		    redirect="false"
	  	/>
	</action>  

 * 
*/
public class VoteMonitoringAction extends LamsDispatchAction implements VoteAppConstants
{
	static Logger logger = Logger.getLogger(VoteMonitoringAction.class.getName());
	
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
     * @throws VoteApplicationException the known runtime exception 
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
    	VoteUtils.cleanUpUserExceptions(request);
    	logger.debug("dispatching unspecified...");
    	return null;
    }

    
    public ActionForward submitSession(ActionMapping mapping,
            ActionForm form,
            HttpServletRequest request,
            HttpServletResponse response,
            IVoteService voteService,
            VoteGeneralMonitoringDTO voteGeneralMonitoringDTO
            ) throws IOException,
                                         ServletException
	{
        logger.debug("calling submitSession...voteGeneralMonitoringDTO:" + voteGeneralMonitoringDTO);
        commonSubmitSessionCode(form, request, voteService,voteGeneralMonitoringDTO);
        logger.debug("post commonSubmitSessionCode: " +voteGeneralMonitoringDTO);
    	return (mapping.findForward(LOAD_MONITORING));
	}
    

    protected void commonSubmitSessionCode(ActionForm form, HttpServletRequest request,IVoteService voteService,
            VoteGeneralMonitoringDTO voteGeneralMonitoringDTO) throws IOException, ServletException
    {
    	logger.debug("starting  commonSubmitSessionCode...voteGeneralMonitoringDTO:" + voteGeneralMonitoringDTO);
    	
		logger.debug("voteService:" + voteService);
		VoteMonitoringForm voteMonitoringForm = (VoteMonitoringForm) form;
		
		repopulateRequestParameters(request, voteMonitoringForm, voteGeneralMonitoringDTO);
		logger.debug("done repopulateRequestParameters");

    	String currentMonitoredToolSession=voteMonitoringForm.getSelectedToolSessionId(); 
	    logger.debug("currentMonitoredToolSession: " + currentMonitoredToolSession);

	    String toolContentID =voteMonitoringForm.getToolContentID();
	    logger.debug("toolContentID: " + toolContentID);
	    
	    VoteContent voteContent=voteService.retrieveVote(new Long(toolContentID));
		logger.debug("existing voteContent:" + voteContent);


		if (currentMonitoredToolSession.equals("All"))
	    {
		    voteGeneralMonitoringDTO.setSelectionCase(new Long(2));
		    request.setAttribute(SELECTION_CASE, new Long(2));
		    
		    logger.debug("generate DTO for All sessions: ");
		    List listVoteAllSessionsDTO=MonitoringUtil.prepareChartDTO(request, voteService, voteMonitoringForm, voteContent.getVoteContentId());
		    logger.debug("listVoteAllSessionsDTO: " + listVoteAllSessionsDTO);
		    voteGeneralMonitoringDTO.setListVoteAllSessionsDTO(listVoteAllSessionsDTO);
	    }
	    else
	    {
		    logger.debug("preparing chart data for content id: " + voteContent.getVoteContentId());
		    logger.debug("preparing chart data for currentMonitoredToolSession: " + currentMonitoredToolSession);
		    request.setAttribute(SELECTION_CASE, new Long(1));
		    
		    VoteSession voteSession=voteService.retrieveVoteSession(new Long(currentMonitoredToolSession));
    		logger.debug("voteSession uid:" + voteSession.getUid());
		    MonitoringUtil.prepareChartData(request, voteService, voteMonitoringForm, voteContent.getVoteContentId().toString(), 
		            voteSession.getUid().toString(), null, voteGeneralMonitoringDTO);
		    
		    logger.debug("post prepareChartData, voteGeneralMonitoringDTO:" + voteGeneralMonitoringDTO);

		    refreshSummaryData(request, voteContent, voteService, true, false, currentMonitoredToolSession, null, true, null, 
		            voteGeneralMonitoringDTO, null);
		    logger.debug("session_name: " + voteSession.getSession_name());
		    voteGeneralMonitoringDTO.setGroupName(voteSession.getSession_name());
		    logger.debug("post refreshSummaryData, voteGeneralMonitoringDTO:" + voteGeneralMonitoringDTO);
		    voteGeneralMonitoringDTO.setSelectionCase(new Long(1));
	    }
		logger.debug("SELECTION_CASE: " + voteGeneralMonitoringDTO.getSelectionCase());
		
	    logger.debug("SELECTION_CASE: " + request.getAttribute(SELECTION_CASE));
	    request.setAttribute(CURRENT_MONITORED_TOOL_SESSION, currentMonitoredToolSession);

	    
		voteGeneralMonitoringDTO.setCurrentMonitoredToolSession(currentMonitoredToolSession);
	    voteMonitoringForm.setSbmtSuccess(new Boolean(false).toString());
	    voteGeneralMonitoringDTO.setSbmtSuccess(new Boolean(false).toString());
	    voteGeneralMonitoringDTO.setRequestLearningReport(new Boolean(false).toString());
	    
		
		Map summaryToolSessions=MonitoringUtil.populateToolSessions(request, voteContent, voteService);
		logger.debug("summaryToolSessions: " + summaryToolSessions);
		voteGeneralMonitoringDTO.setSummaryToolSessions(summaryToolSessions);

		Map summaryToolSessionsId=MonitoringUtil.populateToolSessionsId(request, voteContent, voteService);
		logger.debug("summaryToolSessionsId: " + summaryToolSessionsId);
		voteGeneralMonitoringDTO.setSummaryToolSessionsId(summaryToolSessionsId);

		logger.debug("calling initInstructionsContent.");
		//initInstructionsContent(toolContentID, request, voteService, voteGeneralMonitoringDTO);
		logger.debug("post initInstructionsContent, voteGeneralMonitoringDTO: " + voteGeneralMonitoringDTO);
		
		logger.debug("calling initStatsContent.");
		initStatsContent(toolContentID, request, voteService, voteGeneralMonitoringDTO);
		logger.debug("post initStatsContent, voteGeneralMonitoringDTO: " + voteGeneralMonitoringDTO);

		
		/*setting editable screen properties*/
		VoteGeneralAuthoringDTO voteGeneralAuthoringDTO= new VoteGeneralAuthoringDTO();
		voteGeneralAuthoringDTO.setActivityTitle(voteContent.getTitle());
		voteGeneralAuthoringDTO.setActivityInstructions(voteContent.getInstructions());

		Map mapOptionsContent= new TreeMap(new VoteComparator());
		Iterator queIterator=voteContent.getVoteQueContents().iterator();
		Long mapIndex=new Long(1);
		logger.debug("mapOptionsContent: " + mapOptionsContent);
		while (queIterator.hasNext())
		{
			VoteQueContent voteQueContent=(VoteQueContent) queIterator.next();
			if (voteQueContent != null)
			{
				logger.debug("question: " + voteQueContent.getQuestion());
				mapOptionsContent.put(mapIndex.toString(),voteQueContent.getQuestion());
	    		/**
	    		 * make the first entry the default(first) one for jsp
	    		 */
	    		if (mapIndex.longValue() == 1)
	    		{
	    		    voteGeneralAuthoringDTO.setDefaultOptionContent(voteQueContent.getQuestion());
	    		}
	    		
	    		mapIndex=new Long(mapIndex.longValue()+1);
			}
		}
		
		logger.debug("mapOptionsContent: " + mapOptionsContent);
	    int maxIndex=mapOptionsContent.size();
	    logger.debug("maxIndex: " + maxIndex);
    	voteGeneralAuthoringDTO.setMaxOptionIndex(maxIndex);

		voteGeneralAuthoringDTO.setMapOptionsContent(mapOptionsContent);
		
		boolean isContentInUse=VoteUtils.isContentInUse(voteContent);
		logger.debug("isContentInUse:" + isContentInUse);
		voteGeneralMonitoringDTO.setIsMonitoredContentInUse(new Boolean(false).toString());
		if (isContentInUse == true)
		{
			logger.debug("monitoring url does not allow editActivity since the content is in use.");
			voteGeneralMonitoringDTO.setIsMonitoredContentInUse(new Boolean(true).toString());
		}

		logger.debug("voteGeneralAuthoringDTO: " + voteGeneralAuthoringDTO);
		request.setAttribute(VOTE_GENERAL_AUTHORING_DTO, voteGeneralAuthoringDTO);
		
		//prepareReflectionData(request, voteContent, voteService, null,false);
		prepareReflectionData(request, voteContent, voteService, null, false, "All");

		if (voteService.studentActivityOccurredGlobal(voteContent))
		{
			logger.debug("USER_EXCEPTION_NO_TOOL_SESSIONS is set to false");
			voteGeneralMonitoringDTO.setUserExceptionNoToolSessions(new Boolean(false).toString());
		}
		else
		{
			logger.debug("USER_EXCEPTION_NO_TOOL_SESSIONS is set to true");
			voteGeneralMonitoringDTO.setUserExceptionNoToolSessions(new Boolean(true).toString());
		}
		
		/**getting instructions screen content from here... */
		voteGeneralMonitoringDTO.setOnlineInstructions(voteContent.getOnlineInstructions());
		voteGeneralMonitoringDTO.setOfflineInstructions(voteContent.getOfflineInstructions());
		
        List attachmentList = voteService.retrieveVoteUploadedFiles(voteContent);
        logger.debug("attachmentList: " + attachmentList);
        voteGeneralMonitoringDTO.setAttachmentList(attachmentList);
        voteGeneralMonitoringDTO.setDeletedAttachmentList(new ArrayList());
        /** ...till here **/


		
		logger.debug("end of commonSubmitSessionCode, voteGeneralMonitoringDTO: " + voteGeneralMonitoringDTO);
		request.setAttribute(VOTE_GENERAL_MONITORING_DTO, voteGeneralMonitoringDTO);
		
    	EditActivityDTO editActivityDTO = new EditActivityDTO();
		logger.debug("isContentInUse:" + isContentInUse);
		if (isContentInUse == true)
		{
		    editActivityDTO.setMonitoredContentInUse(new Boolean(true).toString());
		}
		request.setAttribute(EDIT_ACTIVITY_DTO, editActivityDTO);
	
		
		/*find out if there are any reflection entries, from here*/
		boolean notebookEntriesExist=MonitoringUtil.notebookEntriesExist(voteService, voteContent);
		logger.debug("notebookEntriesExist : " + notebookEntriesExist);
		
		if (notebookEntriesExist)
		{
		    request.setAttribute(NOTEBOOK_ENTRIES_EXIST, new Boolean(true).toString());
		    
		    String userExceptionNoToolSessions=(String)voteGeneralMonitoringDTO.getUserExceptionNoToolSessions();
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
		
		MonitoringUtil.buildVoteStatsDTO(request,voteService, voteContent);

    }
    
    
    public ActionForward submitSession(ActionMapping mapping,
            ActionForm form,
            HttpServletRequest request,
            HttpServletResponse response) 
    		throws IOException,ServletException
	{
        logger.debug("dispathcing submitSession..");
        IVoteService voteService = VoteServiceProxy.getVoteService(getServlet().getServletContext());
        logger.debug("voteService: " +voteService);
        
        VoteGeneralMonitoringDTO voteGeneralMonitoringDTO= new VoteGeneralMonitoringDTO();
        
        commonSubmitSessionCode(form, request, voteService,voteGeneralMonitoringDTO);
        logger.debug("post commonSubmitSessionCode: " +voteGeneralMonitoringDTO);
        

        return (mapping.findForward(LOAD_MONITORING));	
	}

    
	public void refreshSummaryData(HttpServletRequest request, VoteContent voteContent, IVoteService voteService, 
			boolean isUserNamesVisible, boolean isLearnerRequest, String currentSessionId, String userId, 
			boolean showUserEntriesBySession, VoteGeneralLearnerFlowDTO voteGeneralLearnerFlowDTO, 
			VoteGeneralMonitoringDTO voteGeneralMonitoringDTO, ExportPortfolioDTO exportPortfolioDTO)
	{
	    logger.debug("doing refreshSummaryData." + voteGeneralLearnerFlowDTO);
	    logger.debug("voteGeneralMonitoringDTO:" + voteGeneralMonitoringDTO);
	    logger.debug("exportPortfolioDTO:" + exportPortfolioDTO);
	    
		if (voteService == null)
		{
			logger.debug("will retrieve voteService");
			voteService = VoteServiceProxy.getVoteService(getServlet().getServletContext());
			logger.debug("retrieving voteService from session: " + voteService);
		}
		logger.debug("voteService: " + voteService);
		
		logger.debug("isUserNamesVisible: " + isUserNamesVisible);
		logger.debug("isLearnerRequest: " + isLearnerRequest);
				
		/* this section is related to summary tab. Starts here. */
		Map summaryToolSessions=MonitoringUtil.populateToolSessions(request, voteContent, voteService);
		logger.debug("summaryToolSessions: " + summaryToolSessions);
		
		if (voteService.studentActivityOccurredStandardAndOpen(voteContent))
		{
		    if (voteGeneralMonitoringDTO != null)
		        voteGeneralMonitoringDTO.setUserExceptionNoToolSessions(new Boolean(false).toString());
				logger.debug("USER_EXCEPTION_NO_TOOL_SESSIONS is set to false");
		}
		else
		{
		    if (voteGeneralMonitoringDTO != null)
		        voteGeneralMonitoringDTO.setUserExceptionNoToolSessions(new Boolean(true).toString());
				logger.debug("USER_EXCEPTION_NO_TOOL_SESSIONS is set to true");
		}
		
		String userExceptionNoToolSessions=voteGeneralMonitoringDTO.getUserExceptionNoToolSessions();
		logger.debug("userExceptionNoToolSessions: " + userExceptionNoToolSessions);
		if (exportPortfolioDTO != null)
		{
		    exportPortfolioDTO.setUserExceptionNoToolSessions(userExceptionNoToolSessions);
		}
		
		Map summaryToolSessionsId=MonitoringUtil.populateToolSessionsId(request, voteContent, voteService);
		logger.debug("summaryToolSessionsId: " + summaryToolSessionsId);
	    logger.debug("currentSessionId: " + currentSessionId);
	    
	    if ((currentSessionId != null) && (!currentSessionId.equals("All")))
	    {
		    VoteSession voteSession= voteService.retrieveVoteSession(new Long(currentSessionId));
		    logger.debug("voteSession:" + voteSession);
		    if (voteGeneralMonitoringDTO != null)
		        voteGeneralMonitoringDTO.setGroupName(voteSession.getSession_name());
	    }
	    else
	    {
	        voteGeneralMonitoringDTO.setGroupName("All Groups");
	    }
	    
	    logger.debug("using allUsersData to retrieve data: " + isUserNamesVisible);
	    List listMonitoredAnswersContainerDTO=MonitoringUtil.buildGroupsQuestionData(request, voteContent, 
	    		isUserNamesVisible, isLearnerRequest, currentSessionId, userId, voteService);
	    logger.debug("listMonitoredAnswersContainerDTO: " + listMonitoredAnswersContainerDTO);	    
	    /* ends here. */
	    
	    logger.debug("decide processing user entered values based on isLearnerRequest: " + isLearnerRequest);

	    List listUserEntries=processUserEnteredNominations(voteService, voteContent, currentSessionId, showUserEntriesBySession, userId, isLearnerRequest);
	    logger.debug("listUserEntries: " + listUserEntries);
	    
	    if (voteGeneralLearnerFlowDTO != null)
	    {
	        logger.debug("placing dtos within the voteGeneralLearnerFlowDTO: ");
	        voteGeneralLearnerFlowDTO.setListMonitoredAnswersContainerDto(listMonitoredAnswersContainerDTO);
	        voteGeneralLearnerFlowDTO.setListUserEntries(listUserEntries);;
	    }

	    
	    if (exportPortfolioDTO != null)
	    {
	        logger.debug("placing dtos within the exportPortfolioDTO: ");
	        exportPortfolioDTO.setListMonitoredAnswersContainerDto(listMonitoredAnswersContainerDTO);
	        exportPortfolioDTO.setListUserEntries(listUserEntries);;
	    }
	    
	    
	    if (voteGeneralMonitoringDTO != null)
	    {
	        voteGeneralMonitoringDTO.setSummaryToolSessions(summaryToolSessions);
	        voteGeneralMonitoringDTO.setSummaryToolSessionsId(summaryToolSessionsId);
	        voteGeneralMonitoringDTO.setSelectionCase(new Long(2));
	        voteGeneralMonitoringDTO.setCurrentMonitoredToolSession("All");
	        voteGeneralMonitoringDTO.setListMonitoredAnswersContainerDto(listMonitoredAnswersContainerDTO);
		    voteGeneralMonitoringDTO.setListUserEntries(listUserEntries);
		    
		    voteGeneralMonitoringDTO.setExistsOpenVotes(new Boolean(false).toString());
		    if (listUserEntries.size() > 0)
		    {
		        voteGeneralMonitoringDTO.setExistsOpenVotes(new Boolean(true).toString());
		    }
	    }
	    
	    logger.debug("final voteGeneralLearnerFlowDTO: " + voteGeneralLearnerFlowDTO);
	    logger.debug("final voteGeneralMonitoringDTO: " + voteGeneralMonitoringDTO);
		
		boolean isContentInUse=VoteUtils.isContentInUse(voteContent);
		logger.debug("isContentInUse:" + isContentInUse);
		voteGeneralMonitoringDTO.setIsMonitoredContentInUse(new Boolean(false).toString());
		if (isContentInUse == true)
		{
			logger.debug("monitoring url does not allow editActivity since the content is in use.");
			voteGeneralMonitoringDTO.setIsMonitoredContentInUse(new Boolean(true).toString());
		}

		//prepareReflectionData(request, voteContent, voteService, null,false);
		prepareReflectionData(request, voteContent, voteService, null, false, "All");
		
		if (voteService.studentActivityOccurredGlobal(voteContent))
		{
			logger.debug("USER_EXCEPTION_NO_TOOL_SESSIONS is set to false");
			voteGeneralMonitoringDTO.setUserExceptionNoToolSessions(new Boolean(false).toString());
		}
		else
		{
			logger.debug("USER_EXCEPTION_NO_TOOL_SESSIONS is set to true");
			voteGeneralMonitoringDTO.setUserExceptionNoToolSessions(new Boolean(true).toString());
		}

		/**getting instructions screen content from here... */
		voteGeneralMonitoringDTO.setOnlineInstructions(voteContent.getOnlineInstructions());
		voteGeneralMonitoringDTO.setOfflineInstructions(voteContent.getOfflineInstructions());
		
        List attachmentList = voteService.retrieveVoteUploadedFiles(voteContent);
        logger.debug("attachmentList: " + attachmentList);
        voteGeneralMonitoringDTO.setAttachmentList(attachmentList);
        voteGeneralMonitoringDTO.setDeletedAttachmentList(new ArrayList());
        /** ...till here **/

		
	    logger.debug("end of refreshSummaryData, voteGeneralMonitoringDTO" + voteGeneralMonitoringDTO);
		request.setAttribute(VOTE_GENERAL_MONITORING_DTO, voteGeneralMonitoringDTO);
		
    	EditActivityDTO editActivityDTO = new EditActivityDTO();
		isContentInUse=VoteUtils.isContentInUse(voteContent);
		logger.debug("isContentInUse:" + isContentInUse);
		if (isContentInUse == true)
		{
		    editActivityDTO.setMonitoredContentInUse(new Boolean(true).toString());
		}
		request.setAttribute(EDIT_ACTIVITY_DTO, editActivityDTO);
		
		
		
		/*find out if there are any reflection entries, from here*/
		boolean notebookEntriesExist=MonitoringUtil.notebookEntriesExist(voteService, voteContent);
		logger.debug("notebookEntriesExist : " + notebookEntriesExist);
		
		if (notebookEntriesExist)
		{
		    request.setAttribute(NOTEBOOK_ENTRIES_EXIST, new Boolean(true).toString());
		    
		    userExceptionNoToolSessions=(String)voteGeneralMonitoringDTO.getUserExceptionNoToolSessions();
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
		
		MonitoringUtil.buildVoteStatsDTO(request,voteService, voteContent);

	}


	public List processUserEnteredNominations(IVoteService voteService, VoteContent voteContent, String currentSessionId, boolean showUserEntriesBySession, String userId, boolean showUserEntriesByUserId)
	{
	    logger.debug("start getting user entries, showUserEntriesBySession: " + showUserEntriesBySession);
	    logger.debug("start getting user entries, currentSessionId: " + currentSessionId);
	    logger.debug("start getting user entries, showUserEntriesByUserId: " + showUserEntriesByUserId);
	    logger.debug("start getting user entries, userId: " + userId);
	    logger.debug("start getting user entries, voteContent: " + voteContent);
	    logger.debug("start getting user entries, voteContent id: " + voteContent.getVoteContentId());

	    Set userEntries=voteService.getUserEntries();
	    logger.debug("userEntries: " + userEntries);
	    
	    List listUserEntries=new LinkedList();
	    
	    Iterator itListNominations = userEntries.iterator();
	    while (itListNominations.hasNext())
	    {
	    	String  userEntry =(String)itListNominations.next();
	    	logger.debug("userEntry:..." + userEntry);
	    	
	    	if ((userEntry != null) && (userEntry.length() > 0))
	    	{
	    		VoteMonitoredAnswersDTO voteMonitoredAnswersDTO= new VoteMonitoredAnswersDTO();
	    		logger.debug("adding user entry : " + userEntry);
	    		voteMonitoredAnswersDTO.setQuestion(userEntry);
	    		
	    		List userRecords=voteService.getUserRecords(userEntry);
	    		logger.debug("userRecords: " + userRecords);
	    		
	    		logger.debug("start processing user records: ");
	    		
	    		List listMonitoredUserContainerDTO= new LinkedList();
				
	    	    Iterator itUserRecords= userRecords.iterator();
	    	    while (itUserRecords.hasNext())
	    	    {
	    	        VoteMonitoredUserDTO voteMonitoredUserDTO = new VoteMonitoredUserDTO();
	    	        logger.debug("new DTO created");
	    	        VoteUsrAttempt voteUsrAttempt =(VoteUsrAttempt)itUserRecords.next();
	    	    	logger.debug("voteUsrAttempt: " + voteUsrAttempt);
	    	

    	    	    VoteSession localUserSession=voteUsrAttempt.getVoteQueUsr().getVoteSession();
    	    	    logger.debug("localUserSession: " + localUserSession);
    	    	    logger.debug("localUserSession's content id: " + localUserSession.getVoteContentId()); 
    	    	    logger.debug("incoming content uid versus localUserSession's content id: " + voteContent.getUid() + " versus " +  localUserSession.getVoteContentId());

	    	    	if (showUserEntriesBySession == false)
	    	    	{
	    	    	    logger.debug("showUserEntriesBySession is false");	    	    	    
	    	    	    logger.debug("show user  entries  by same content only");
	    	    	    if (voteContent.getUid().toString().equals(localUserSession.getVoteContentId().toString()))
	    	    	    {
			    	        voteMonitoredUserDTO.setAttemptTime(voteUsrAttempt.getAttemptTime());
			    			voteMonitoredUserDTO.setTimeZone(voteUsrAttempt.getTimeZone());
			    			voteMonitoredUserDTO.setUserName(voteUsrAttempt.getVoteQueUsr().getFullname());
			    			voteMonitoredUserDTO.setQueUsrId(voteUsrAttempt.getVoteQueUsr().getUid().toString());
			    			voteMonitoredUserDTO.setUserEntry(voteUsrAttempt.getUserEntry());
			    			voteMonitoredUserDTO.setUid(voteUsrAttempt.getUid().toString());
			    			voteMonitoredUserDTO.setVisible(new Boolean(voteUsrAttempt.isVisible()).toString());
	    	    	        listMonitoredUserContainerDTO.add(voteMonitoredUserDTO);
	    	    	    }
	    	    	}
	    	    	else
	    	    	{
	    	    	    logger.debug("showUserEntriesBySession is true: the case with learner export portfolio");
	    	    	    logger.debug("show user  entries  by same content and same session and same user");
	    	    	    String userSessionId=voteUsrAttempt.getVoteQueUsr().getVoteSession().getVoteSessionId().toString() ;
	    	    	    logger.debug("userSessionId versus currentSessionId: " + userSessionId + " versus " +  currentSessionId);
	    	    	    
	    	    	    if (showUserEntriesByUserId == true)
	    	    	    {
	    	    	        if (voteContent.getUid().toString().equals(localUserSession.getVoteContentId().toString()))
		    	    	    {
			    	    	    logger.debug("showUserEntriesByUserId is true");
			    	    	    if (userSessionId.equals(currentSessionId))
			    	    	    {
			    	    	        String localUserId=voteUsrAttempt.getVoteQueUsr().getQueUsrId().toString(); 
			    	    	        if (userId.equals(localUserId))
				    	    	    {
				    	    	        logger.debug("this is requested by user id: "  + userId);
				    	    	        voteMonitoredUserDTO.setAttemptTime(voteUsrAttempt.getAttemptTime());
						    			voteMonitoredUserDTO.setTimeZone(voteUsrAttempt.getTimeZone());
						    			voteMonitoredUserDTO.setUserName(voteUsrAttempt.getVoteQueUsr().getFullname());
						    			voteMonitoredUserDTO.setQueUsrId(voteUsrAttempt.getVoteQueUsr().getUid().toString());
						    			voteMonitoredUserDTO.setUserEntry(voteUsrAttempt.getUserEntry());
						    			listMonitoredUserContainerDTO.add(voteMonitoredUserDTO);
						    			voteMonitoredUserDTO.setUid(voteUsrAttempt.getUid().toString());
						    			voteMonitoredUserDTO.setVisible(new Boolean(voteUsrAttempt.isVisible()).toString());		
						    			logger.debug("overriding the nomination text with 'Nomination Hidden' if needed");
						    			logger.debug("is entry visible: " + voteUsrAttempt.isVisible());
						    			if (voteUsrAttempt.isVisible() == false)
						    			{
						    			    voteMonitoredAnswersDTO.setQuestion("Nomination Hidden");
						    			    logger.debug("overwrote the nomination text");
						    			}
						    			    
				    	    	    }
			    	    	    }
		    	    	    }
	    	    	    }
	    	    	    else
	    	    	    {
	    	    	        logger.debug("showUserEntriesByUserId is false");
	    	    	        logger.debug("show user  entries  by same content and same session");
	    	    	        logger.debug("voteContent.getVoteContentId() versus localUserSession.getVoteContentId().toString(): " + 
	    	    	                voteContent.getVoteContentId() + " versus " + localUserSession.getVoteContentId());
	    	    	        if (voteContent.getUid().toString().equals(localUserSession.getVoteContentId().toString()))
	    	    	        {
			    	    	    if (userSessionId.equals(currentSessionId))
			    	    	    {
			    	    	        logger.debug("this is requested by session id: "  + currentSessionId);
					    	        voteMonitoredUserDTO.setAttemptTime(voteUsrAttempt.getAttemptTime());
					    			voteMonitoredUserDTO.setTimeZone(voteUsrAttempt.getTimeZone());
					    			voteMonitoredUserDTO.setUserName(voteUsrAttempt.getVoteQueUsr().getFullname());
					    			voteMonitoredUserDTO.setQueUsrId(voteUsrAttempt.getVoteQueUsr().getUid().toString());
					    			voteMonitoredUserDTO.setUserEntry(voteUsrAttempt.getUserEntry());
			    	    	        listMonitoredUserContainerDTO.add(voteMonitoredUserDTO);
					    			voteMonitoredUserDTO.setUid(voteUsrAttempt.getUid().toString());
					    			voteMonitoredUserDTO.setVisible(new Boolean(voteUsrAttempt.isVisible()).toString());					    			
			    	    	    }	    	    	            
	    	    	        }
	    	    	    }
	    	    	}
	    	    	
	    	    }
	    	    
	    		logger.debug("final listMonitoredUserContainerDTO: " + listMonitoredUserContainerDTO);
	    		
	    		logger.debug("final listMonitoredUserContainerDTO size: " + listMonitoredUserContainerDTO.size());
	    		if (listMonitoredUserContainerDTO.size() > 0)
	    		{
		    		logger.debug("adding user entry's data");
		    		Map mapMonitoredUserContainerDTO=MonitoringUtil.convertToVoteMonitoredUserDTOMap(listMonitoredUserContainerDTO);
		    		logger.debug("final user entry mapMonitoredUserContainerDTO:..." + mapMonitoredUserContainerDTO);

		    	    voteMonitoredAnswersDTO.setQuestionAttempts(mapMonitoredUserContainerDTO);
		    		listUserEntries.add(voteMonitoredAnswersDTO);	    		    
	    		}
			}
		}
	    
	    logger.debug("finish getting user entries: " + listUserEntries);
	    return listUserEntries;
	}

	
    public void initSummaryContent(String toolContentID,
            HttpServletRequest request,
            IVoteService voteService,
            VoteGeneralMonitoringDTO voteGeneralMonitoringDTO) throws IOException,
                                         ServletException
	{
    	logger.debug("start  initSummaryContent...toolContentID: " + toolContentID);
    	logger.debug("dispatching getSummary...voteGeneralMonitoringDTO:" + voteGeneralMonitoringDTO);
   	    	
		logger.debug("voteService: " + voteService);
	    logger.debug("toolContentID: " + toolContentID);
	    
	    VoteContent voteContent=voteService.retrieveVote(new Long(toolContentID));
		logger.debug("existing voteContent:" + voteContent);
		
    	/* this section is related to summary tab. Starts here. */
		Map summaryToolSessions=MonitoringUtil.populateToolSessions(request, voteContent, voteService);
		logger.debug("summaryToolSessions: " + summaryToolSessions);
		voteGeneralMonitoringDTO.setSummaryToolSessions(summaryToolSessions);
		
		Map summaryToolSessionsId=MonitoringUtil.populateToolSessionsId(request, voteContent, voteService);
		logger.debug("summaryToolSessionsId: " + summaryToolSessionsId);
		voteGeneralMonitoringDTO.setSummaryToolSessionsId(summaryToolSessionsId);
	    /* ends here. */
	    
		/*true means there is at least 1 response*/
		if (voteService.studentActivityOccurredStandardAndOpen(voteContent))
		{
		    voteGeneralMonitoringDTO.setUserExceptionNoToolSessions(new Boolean(false).toString());
			logger.debug("USER_EXCEPTION_NO_TOOL_SESSIONS is set to false");
		}
		else
		{
			voteGeneralMonitoringDTO.setUserExceptionNoToolSessions(new Boolean(true).toString());
			logger.debug("USER_EXCEPTION_NO_TOOL_SESSIONS is set to true");
		}

		//prepareReflectionData(request, voteContent, voteService, null,false);
		prepareReflectionData(request, voteContent, voteService, null, false, "All");
		
    	EditActivityDTO editActivityDTO = new EditActivityDTO();
		boolean isContentInUse=VoteUtils.isContentInUse(voteContent);
		logger.debug("isContentInUse:" + isContentInUse);
		if (isContentInUse == true)
		{
		    editActivityDTO.setMonitoredContentInUse(new Boolean(true).toString());
		}
		request.setAttribute(EDIT_ACTIVITY_DTO, editActivityDTO);
		
		/**getting instructions screen content from here... */
		voteGeneralMonitoringDTO.setOnlineInstructions(voteContent.getOnlineInstructions());
		voteGeneralMonitoringDTO.setOfflineInstructions(voteContent.getOfflineInstructions());
		
        List attachmentList = voteService.retrieveVoteUploadedFiles(voteContent);
        logger.debug("attachmentList: " + attachmentList);
        voteGeneralMonitoringDTO.setAttachmentList(attachmentList);
        voteGeneralMonitoringDTO.setDeletedAttachmentList(new ArrayList());
        /** ...till here **/

		
		voteGeneralMonitoringDTO.setCurrentMonitoringTab("summary");

	    logger.debug("end of refreshSummaryData, voteGeneralMonitoringDTO" + voteGeneralMonitoringDTO);
		request.setAttribute(VOTE_GENERAL_MONITORING_DTO, voteGeneralMonitoringDTO);

		/*find out if there are any reflection entries, from here*/
		boolean notebookEntriesExist=MonitoringUtil.notebookEntriesExist(voteService, voteContent);
		logger.debug("notebookEntriesExist : " + notebookEntriesExist);
		
		if (notebookEntriesExist)
		{
		    request.setAttribute(NOTEBOOK_ENTRIES_EXIST, new Boolean(true).toString());
		    
		    String userExceptionNoToolSessions=(String)voteGeneralMonitoringDTO.getUserExceptionNoToolSessions();
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
		
		MonitoringUtil.buildVoteStatsDTO(request,voteService, voteContent);

    	logger.debug("end  initSummaryContent...");
	}
    
	    
    public void initStatsContent(String toolContentID,
            HttpServletRequest request,
            IVoteService voteService, 
            VoteGeneralMonitoringDTO voteGeneralMonitoringDTO) throws IOException,
                                         ServletException
	{
    	logger.debug("starting  initStatsContent...:" + toolContentID);
    	logger.debug("dispatching getStats..." + request);
		logger.debug("voteService: " + voteService);
		
	    VoteContent voteContent=voteService.retrieveVote(new Long(toolContentID));
		logger.debug("existing voteContent:" + voteContent);
		
		if (voteService.studentActivityOccurredStandardAndOpen(voteContent))
		{
			voteGeneralMonitoringDTO.setUserExceptionNoToolSessions(new Boolean(false).toString());
			logger.debug("USER_EXCEPTION_NO_TOOL_SESSIONS is set to false");
		}
		else
		{
			voteGeneralMonitoringDTO.setUserExceptionNoToolSessions(new Boolean(true).toString());
			logger.debug("USER_EXCEPTION_NO_TOOL_SESSIONS is set to true");
		}

		
    	refreshStatsData(request, voteService,voteGeneralMonitoringDTO);
    	logger.debug("post refreshStatsData, voteGeneralMonitoringDTO: " + voteGeneralMonitoringDTO );
    	
    	//prepareReflectionData(request, voteContent, voteService, null,false);
    	prepareReflectionData(request, voteContent, voteService, null, false, "All");

    	EditActivityDTO editActivityDTO = new EditActivityDTO();
		boolean isContentInUse=VoteUtils.isContentInUse(voteContent);
		logger.debug("isContentInUse:" + isContentInUse);
		if (isContentInUse == true)
		{
		    editActivityDTO.setMonitoredContentInUse(new Boolean(true).toString());
		}
		request.setAttribute(EDIT_ACTIVITY_DTO, editActivityDTO);

		/**getting instructions screen content from here... */
		voteGeneralMonitoringDTO.setOnlineInstructions(voteContent.getOnlineInstructions());
		voteGeneralMonitoringDTO.setOfflineInstructions(voteContent.getOfflineInstructions());
		
        List attachmentList = voteService.retrieveVoteUploadedFiles(voteContent);
        logger.debug("attachmentList: " + attachmentList);
        voteGeneralMonitoringDTO.setAttachmentList(attachmentList);
        voteGeneralMonitoringDTO.setDeletedAttachmentList(new ArrayList());
        /** ...till here **/

		
    	voteGeneralMonitoringDTO.setCurrentMonitoringTab("stats");
	    
    	logger.debug("end of refreshSummaryData, voteGeneralMonitoringDTO" + voteGeneralMonitoringDTO);
		request.setAttribute(VOTE_GENERAL_MONITORING_DTO, voteGeneralMonitoringDTO);

    	
		/*find out if there are any reflection entries, from here*/
		boolean notebookEntriesExist=MonitoringUtil.notebookEntriesExist(voteService, voteContent);
		logger.debug("notebookEntriesExist : " + notebookEntriesExist);
		
		if (notebookEntriesExist)
		{
		    request.setAttribute(NOTEBOOK_ENTRIES_EXIST, new Boolean(true).toString());
		    
		    String userExceptionNoToolSessions=(String)voteGeneralMonitoringDTO.getUserExceptionNoToolSessions();
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
		
		MonitoringUtil.buildVoteStatsDTO(request,voteService, voteContent);
		
    	logger.debug("ending  initStatsContent...");
	}
    
    
	public void refreshStatsData(HttpServletRequest request, IVoteService voteService, 
	        VoteGeneralMonitoringDTO voteGeneralMonitoringDTO)
	{
	    logger.debug("starting refreshStatsData: " + voteService);
		/* it is possible that no users has ever logged in for the activity yet*/
		if (voteService == null)
		{
			logger.debug("will retrieve voteService");
			voteService = VoteServiceProxy.getVoteService(getServlet().getServletContext());
			logger.debug("retrieving voteService from session: " + voteService);
		}
		
	    int countAllUsers=voteService.getTotalNumberOfUsers();
		logger.debug("countAllUsers: " + countAllUsers);
		
		if (countAllUsers == 0)
		{
	    	logger.debug("error: countAllUsers is 0");
	    	voteGeneralMonitoringDTO.setUserExceptionNoStudentActivity(new Boolean(true).toString());
		}
		
		voteGeneralMonitoringDTO.setCountAllUsers(new Integer(countAllUsers).toString());
 		
		int countSessionComplete=voteService.countSessionComplete();
		logger.debug("countSessionComplete: " + countSessionComplete);
		voteGeneralMonitoringDTO.setCountSessionComplete(new Integer(countSessionComplete).toString());
		

		logger.debug("end of refreshStatsData, voteGeneralMonitoringDTO" + voteGeneralMonitoringDTO);
		request.setAttribute(VOTE_GENERAL_MONITORING_DTO, voteGeneralMonitoringDTO);
	}
	
	
		
    /**
     * calls learning action endLearning functionality 
     * ActionForward endLearning(ActionMapping mapping,
            ActionForm form,
            HttpServletRequest request,
            HttpServletResponse response) throws IOException,
                                         ServletException, ToolException
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
    public ActionForward endLearning(ActionMapping mapping,
            ActionForm form,
            HttpServletRequest request,
            HttpServletResponse response) throws IOException,
                                         ServletException, ToolException
    {
    	logger.debug("dispatching proxy endLearning to learning module...");
    	
    	IVoteService voteService = VoteServiceProxy.getVoteService(getServlet().getServletContext());
    	logger.debug("voteService :" +voteService);
    	
    	VoteMonitoringForm voteMonitoringForm = (VoteMonitoringForm) form;
	    logger.debug("voteMonitoringForm :" +voteMonitoringForm);
	    voteMonitoringForm.setVoteService(voteService);

	    VoteGeneralMonitoringDTO voteGeneralMonitoringDTO = new VoteGeneralMonitoringDTO();
    	repopulateRequestParameters(request, voteMonitoringForm, voteGeneralMonitoringDTO);
    	
    	VoteLearningAction voteLearningAction= new VoteLearningAction();
    	
    	return null; 
    }


    public ActionForward viewOpenVotes(ActionMapping mapping,
            ActionForm form,
            HttpServletRequest request,
            HttpServletResponse response) throws IOException,
                                         ServletException, ToolException
     {
        logger.debug("dispatching viewOpenVotes...");

        IVoteService voteService = VoteServiceProxy.getVoteService(getServlet().getServletContext());
    	logger.debug("voteService :" +voteService);
    	
    	VoteMonitoringForm voteMonitoringForm = (VoteMonitoringForm) form;
	    logger.debug("voteMonitoringForm :" +voteMonitoringForm);
	    voteMonitoringForm.setVoteService(voteService);

        VoteGeneralMonitoringDTO voteGeneralMonitoringDTO=new VoteGeneralMonitoringDTO();

	    repopulateRequestParameters(request, voteMonitoringForm, voteGeneralMonitoringDTO);

	    if (voteService == null)
		{
			logger.debug("will retrieve voteService");
			voteService = VoteServiceProxy.getVoteService(getServlet().getServletContext());
		}
		logger.debug("voteService: " + voteService);

    	voteMonitoringForm.setShowOpenVotesSection(new Boolean(true).toString());
    	voteGeneralMonitoringDTO.setShowOpenVotesSection(new Boolean(true).toString());
    	
    	logger.debug("showOpen votes set to true: ");
    	
		String toolContentID =voteMonitoringForm.getToolContentID();
	    logger.debug("toolContentID: " + toolContentID);

	    VoteContent voteContent=voteService.retrieveVote(new Long(toolContentID));
		logger.debug("existing voteContent:" + voteContent);

    	String currentMonitoredToolSession=voteMonitoringForm.getSelectedToolSessionId(); 
	    logger.debug("currentMonitoredToolSession: " + currentMonitoredToolSession);

		refreshSummaryData(request, voteContent, voteService, true, false, currentMonitoredToolSession, 
		        null, true, null,voteGeneralMonitoringDTO, null);
		
		
		initSummaryContent(toolContentID , request, voteService, voteGeneralMonitoringDTO);
		logger.debug("post initSummaryContent, voteGeneralMonitoringDTO: " + voteGeneralMonitoringDTO);
		
		logger.debug("calling initInstructionsContent.");
		logger.debug("post initInstructionsContent, voteGeneralMonitoringDTO: " + voteGeneralMonitoringDTO);
		
		logger.debug("calling initStatsContent.");
		initStatsContent(toolContentID, request, voteService, voteGeneralMonitoringDTO);
		logger.debug("post initStatsContent, voteGeneralMonitoringDTO: " + voteGeneralMonitoringDTO);

		
		if (voteService.studentActivityOccurredGlobal(voteContent))
		{
			logger.debug("USER_EXCEPTION_NO_TOOL_SESSIONS is set to false");
			voteGeneralMonitoringDTO.setUserExceptionNoToolSessions(new Boolean(false).toString());
		}
		else
		{
			logger.debug("USER_EXCEPTION_NO_TOOL_SESSIONS is set to true");
			voteGeneralMonitoringDTO.setUserExceptionNoToolSessions(new Boolean(true).toString());
		}

		
	    logger.debug("voteGeneralMonitoringDTO: " + voteGeneralMonitoringDTO);
		request.setAttribute(VOTE_GENERAL_MONITORING_DTO, voteGeneralMonitoringDTO);

		
		if (currentMonitoredToolSession.equals("All"))
	    {
		    voteGeneralMonitoringDTO.setSelectionCase(new Long(2));
	    }
	    else
	    {
	        voteGeneralMonitoringDTO.setSelectionCase(new Long(1));
	    }

		voteGeneralMonitoringDTO.setCurrentMonitoredToolSession(currentMonitoredToolSession);
		voteGeneralMonitoringDTO.setIsMonitoredContentInUse(new Boolean(true).toString());
	    
		
		/**getting instructions screen content from here... */
		voteGeneralMonitoringDTO.setOnlineInstructions(voteContent.getOnlineInstructions());
		voteGeneralMonitoringDTO.setOfflineInstructions(voteContent.getOfflineInstructions());
		
        List attachmentList = voteService.retrieveVoteUploadedFiles(voteContent);
        logger.debug("attachmentList: " + attachmentList);
        voteGeneralMonitoringDTO.setAttachmentList(attachmentList);
        voteGeneralMonitoringDTO.setDeletedAttachmentList(new ArrayList());
        /** ...till here **/
		
		
	    logger.debug("ending editActivityQuestions, voteGeneralMonitoringDTO: " + voteGeneralMonitoringDTO);
		request.setAttribute(VOTE_GENERAL_MONITORING_DTO, voteGeneralMonitoringDTO);


		if (voteContent != null)
		{
			//prepareReflectionData(request, voteContent, voteService, null,false);
		    prepareReflectionData(request, voteContent, voteService, null, false, "All");
			
	    	EditActivityDTO editActivityDTO = new EditActivityDTO();
			boolean isContentInUse=VoteUtils.isContentInUse(voteContent);
			logger.debug("isContentInUse:" + isContentInUse);
			if (isContentInUse == true)
			{
			    editActivityDTO.setMonitoredContentInUse(new Boolean(true).toString());
			}
			request.setAttribute(EDIT_ACTIVITY_DTO, editActivityDTO);
		}
	
    	EditActivityDTO editActivityDTO = new EditActivityDTO();
		boolean isContentInUse=VoteUtils.isContentInUse(voteContent);
		logger.debug("isContentInUse:" + isContentInUse);
		if (isContentInUse == true)
		{
		    editActivityDTO.setMonitoredContentInUse(new Boolean(true).toString());
		}
		request.setAttribute(EDIT_ACTIVITY_DTO, editActivityDTO);

		
		/*find out if there are any reflection entries, from here*/
		boolean notebookEntriesExist=MonitoringUtil.notebookEntriesExist(voteService, voteContent);
		logger.debug("notebookEntriesExist : " + notebookEntriesExist);
		
		if (notebookEntriesExist)
		{
		    request.setAttribute(NOTEBOOK_ENTRIES_EXIST, new Boolean(true).toString());
		    
		    String userExceptionNoToolSessions=(String)voteGeneralMonitoringDTO.getUserExceptionNoToolSessions();
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
		
		MonitoringUtil.buildVoteStatsDTO(request,voteService, voteContent);

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

		
	    logger.debug("ending viewOpenVotes, voteGeneralMonitoringDTO: " + voteGeneralMonitoringDTO);
	    logger.debug("fwd'ing to LOAD_MONITORING: " + LOAD_MONITORING);
    	return (mapping.findForward(LOAD_MONITORING));
     }
    

    public ActionForward closeOpenVotes(ActionMapping mapping,
            ActionForm form,
            HttpServletRequest request,
            HttpServletResponse response) throws IOException,
                                         ServletException, ToolException
     {
        logger.debug("dispatching closeOpenVotes...");
        IVoteService voteService = VoteServiceProxy.getVoteService(getServlet().getServletContext());
    	logger.debug("voteService :" +voteService);
    	
    	VoteMonitoringForm voteMonitoringForm = (VoteMonitoringForm) form;
	    logger.debug("voteMonitoringForm :" +voteMonitoringForm);
	    voteMonitoringForm.setVoteService(voteService);

	    VoteGeneralMonitoringDTO voteGeneralMonitoringDTO=new VoteGeneralMonitoringDTO();
		
	    repopulateRequestParameters(request, voteMonitoringForm, voteGeneralMonitoringDTO);
	    
    	voteMonitoringForm.setShowOpenVotesSection(new Boolean(false).toString());
    	voteGeneralMonitoringDTO.setShowOpenVotesSection(new Boolean(false).toString());
    	
    	logger.debug("showOpen votes set to false: ");
    	
		String toolContentID =voteMonitoringForm.getToolContentID();
	    logger.debug("toolContentID: " + toolContentID);
    	
    	initSummaryContent(toolContentID , request, voteService, voteGeneralMonitoringDTO);
		logger.debug("post initSummaryContent, voteGeneralMonitoringDTO: " + voteGeneralMonitoringDTO);
		
		logger.debug("calling initInstructionsContent.");
		logger.debug("post initInstructionsContent, voteGeneralMonitoringDTO: " + voteGeneralMonitoringDTO);
		
		logger.debug("calling initStatsContent.");
		initStatsContent(toolContentID, request, voteService, voteGeneralMonitoringDTO);
		logger.debug("post initStatsContent, voteGeneralMonitoringDTO: " + voteGeneralMonitoringDTO);

		voteGeneralMonitoringDTO.setIsMonitoredContentInUse(new Boolean(true).toString());
		
	    VoteContent voteContent=voteService.retrieveVote(new Long(toolContentID));
		logger.debug("existing voteContent:" + voteContent);

		if (voteContent != null)
		{
			if (voteService.studentActivityOccurredGlobal(voteContent))
			{
				logger.debug("USER_EXCEPTION_NO_TOOL_SESSIONS is set to false");
				voteGeneralMonitoringDTO.setUserExceptionNoToolSessions(new Boolean(false).toString());
			}
			else
			{
				logger.debug("USER_EXCEPTION_NO_TOOL_SESSIONS is set to true");
				voteGeneralMonitoringDTO.setUserExceptionNoToolSessions(new Boolean(true).toString());
			}
		}
		
		/**getting instructions screen content from here... */
		voteGeneralMonitoringDTO.setOnlineInstructions(voteContent.getOnlineInstructions());
		voteGeneralMonitoringDTO.setOfflineInstructions(voteContent.getOfflineInstructions());
		
        List attachmentList = voteService.retrieveVoteUploadedFiles(voteContent);
        logger.debug("attachmentList: " + attachmentList);
        voteGeneralMonitoringDTO.setAttachmentList(attachmentList);
        voteGeneralMonitoringDTO.setDeletedAttachmentList(new ArrayList());
        /** ...till here **/
		
		
	    logger.debug("voteGeneralMonitoringDTO: " + voteGeneralMonitoringDTO);
		request.setAttribute(VOTE_GENERAL_MONITORING_DTO, voteGeneralMonitoringDTO);


		if (voteContent != null)
		{
			//prepareReflectionData(request, voteContent, voteService, null,false);
		    prepareReflectionData(request, voteContent, voteService, null, false, "All");
			
	    	EditActivityDTO editActivityDTO = new EditActivityDTO();
			boolean isContentInUse=VoteUtils.isContentInUse(voteContent);
			logger.debug("isContentInUse:" + isContentInUse);
			if (isContentInUse == true)
			{
			    editActivityDTO.setMonitoredContentInUse(new Boolean(true).toString());
			}
			request.setAttribute(EDIT_ACTIVITY_DTO, editActivityDTO);		
		}

    	EditActivityDTO editActivityDTO = new EditActivityDTO();
		boolean isContentInUse=VoteUtils.isContentInUse(voteContent);
		logger.debug("isContentInUse:" + isContentInUse);
		if (isContentInUse == true)
		{
		    editActivityDTO.setMonitoredContentInUse(new Boolean(true).toString());
		}
		request.setAttribute(EDIT_ACTIVITY_DTO, editActivityDTO);
		
		
		/*find out if there are any reflection entries, from here*/
		boolean notebookEntriesExist=MonitoringUtil.notebookEntriesExist(voteService, voteContent);
		logger.debug("notebookEntriesExist : " + notebookEntriesExist);
		
		if (notebookEntriesExist)
		{
		    request.setAttribute(NOTEBOOK_ENTRIES_EXIST, new Boolean(true).toString());
		    
		    String userExceptionNoToolSessions=(String)voteGeneralMonitoringDTO.getUserExceptionNoToolSessions();
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
		
		MonitoringUtil.buildVoteStatsDTO(request,voteService, voteContent);
    	logger.debug("ending closeOpenVotes, voteGeneralMonitoringDTO: " + voteGeneralMonitoringDTO);

    	String currentMonitoredToolSession=voteMonitoringForm.getSelectedToolSessionId();
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

    	
    	return (mapping.findForward(LOAD_MONITORING));
     }
    
    

    public ActionForward hideOpenVote(ActionMapping mapping,
            ActionForm form,
            HttpServletRequest request,
            HttpServletResponse response) throws IOException,
                                         ServletException, ToolException
     {
        logger.debug("dispatching hideOpenVote...");
        IVoteService voteService = VoteServiceProxy.getVoteService(getServlet().getServletContext());
    	logger.debug("voteService :" +voteService);
    	
    	VoteMonitoringForm voteMonitoringForm = (VoteMonitoringForm) form;
	    logger.debug("voteMonitoringForm :" +voteMonitoringForm);
	    voteMonitoringForm.setVoteService(voteService);

	    VoteGeneralMonitoringDTO voteGeneralMonitoringDTO= new VoteGeneralMonitoringDTO();
	    
	    repopulateRequestParameters(request, voteMonitoringForm, voteGeneralMonitoringDTO);
		
    	String currentUid=voteMonitoringForm.getCurrentUid();
    	logger.debug("currentUid: " + currentUid);
    	
        VoteUsrAttempt voteUsrAttempt =voteService.getAttemptByUID(new Long(currentUid));
        logger.debug("voteUsrAttempt: " + voteUsrAttempt);
        
        voteUsrAttempt.setVisible(false);
        voteService.updateVoteUsrAttempt(voteUsrAttempt);
        logger.debug("hiding the user entry : " + voteUsrAttempt.getUserEntry());
        voteService.hideOpenVote(voteUsrAttempt);
        
        String toolContentID=voteMonitoringForm.getToolContentID();
        logger.debug("toolContentID: " + toolContentID);
	    
	    VoteContent voteContent=voteService.retrieveVote(new Long(toolContentID));
		logger.debug("existing voteContent:" + voteContent);

    	initSummaryContent(toolContentID , request, voteService, voteGeneralMonitoringDTO);
		logger.debug("post initSummaryContent, voteGeneralMonitoringDTO: " + voteGeneralMonitoringDTO);
		
		logger.debug("calling initInstructionsContent.");
		logger.debug("post initInstructionsContent, voteGeneralMonitoringDTO: " + voteGeneralMonitoringDTO);
		
		logger.debug("calling initStatsContent.");
		initStatsContent(toolContentID, request, voteService, voteGeneralMonitoringDTO);
		logger.debug("post initStatsContent, voteGeneralMonitoringDTO: " + voteGeneralMonitoringDTO);

		
    	String currentMonitoredToolSession=voteMonitoringForm.getSelectedToolSessionId(); 
	    logger.debug("currentMonitoredToolSession: " + currentMonitoredToolSession);

		refreshSummaryData(request, voteContent, voteService, true, false, currentMonitoredToolSession, null, true, null, 
		        voteGeneralMonitoringDTO, null);
		

	    if (currentMonitoredToolSession.equals(""))
	    {
	        currentMonitoredToolSession="All";
	    }

		if (currentMonitoredToolSession.equals("All"))
	    {
		    voteGeneralMonitoringDTO.setSelectionCase(new Long(2));
		    request.setAttribute(SELECTION_CASE, new Long(2));
	    }
	    else
	    {
		    voteGeneralMonitoringDTO.setSelectionCase(new Long(1));
		    request.setAttribute(SELECTION_CASE, new Long(1));
	    }
		

	    voteGeneralMonitoringDTO.setCurrentMonitoredToolSession(currentMonitoredToolSession);
		
	    voteGeneralMonitoringDTO.setIsMonitoredContentInUse(new Boolean(true).toString());
	    
		if (voteService.studentActivityOccurredGlobal(voteContent))
		{
			logger.debug("USER_EXCEPTION_NO_TOOL_SESSIONS is set to false");
			voteGeneralMonitoringDTO.setUserExceptionNoToolSessions(new Boolean(false).toString());
		}
		else
		{
			logger.debug("USER_EXCEPTION_NO_TOOL_SESSIONS is set to true");
			voteGeneralMonitoringDTO.setUserExceptionNoToolSessions(new Boolean(true).toString());
		}
	    
		/**getting instructions screen content from here... */
		voteGeneralMonitoringDTO.setOnlineInstructions(voteContent.getOnlineInstructions());
		voteGeneralMonitoringDTO.setOfflineInstructions(voteContent.getOfflineInstructions());
		
        List attachmentList = voteService.retrieveVoteUploadedFiles(voteContent);
        logger.debug("attachmentList: " + attachmentList);
        voteGeneralMonitoringDTO.setAttachmentList(attachmentList);
        voteGeneralMonitoringDTO.setDeletedAttachmentList(new ArrayList());
        /** ...till here **/
		
		
	    logger.debug("ending editActivityQuestions, voteGeneralMonitoringDTO: " + voteGeneralMonitoringDTO);
		request.setAttribute(VOTE_GENERAL_MONITORING_DTO, voteGeneralMonitoringDTO);

		
	    logger.debug("voteGeneralMonitoringDTO: " + voteGeneralMonitoringDTO);
		request.setAttribute(VOTE_GENERAL_MONITORING_DTO, voteGeneralMonitoringDTO);

		if (voteContent != null)
		{
		    //prepareReflectionData(request, voteContent, voteService, null,false);
		    prepareReflectionData(request, voteContent, voteService, null, false, "All");
			
	    	EditActivityDTO editActivityDTO = new EditActivityDTO();
			boolean isContentInUse=VoteUtils.isContentInUse(voteContent);
			logger.debug("isContentInUse:" + isContentInUse);
			if (isContentInUse == true)
			{
			    editActivityDTO.setMonitoredContentInUse(new Boolean(true).toString());
			}
			request.setAttribute(EDIT_ACTIVITY_DTO, editActivityDTO);		
		}
		    
		/*find out if there are any reflection entries, from here*/
		boolean notebookEntriesExist=MonitoringUtil.notebookEntriesExist(voteService, voteContent);
		logger.debug("notebookEntriesExist : " + notebookEntriesExist);
		
		if (notebookEntriesExist)
		{
		    request.setAttribute(NOTEBOOK_ENTRIES_EXIST, new Boolean(true).toString());
		    
		    String userExceptionNoToolSessions=(String)voteGeneralMonitoringDTO.getUserExceptionNoToolSessions();
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
		
		MonitoringUtil.buildVoteStatsDTO(request,voteService, voteContent);

		
	    logger.debug("submitting session to refresh the data from the database: ");
    	return submitSession(mapping, form,  request, response, voteService, voteGeneralMonitoringDTO);
     }

    
    public ActionForward showOpenVote(ActionMapping mapping,
            ActionForm form,
            HttpServletRequest request,
            HttpServletResponse response) throws IOException,
                                         ServletException, ToolException
     {
        logger.debug("dispatching showOpenVote...");
        IVoteService voteService = VoteServiceProxy.getVoteService(getServlet().getServletContext());
    	logger.debug("voteService :" +voteService);
    	
    	VoteMonitoringForm voteMonitoringForm = (VoteMonitoringForm) form;
	    logger.debug("voteMonitoringForm :" +voteMonitoringForm);
	    voteMonitoringForm.setVoteService(voteService);

	    VoteGeneralMonitoringDTO voteGeneralMonitoringDTO= new VoteGeneralMonitoringDTO();
	    
	    repopulateRequestParameters(request, voteMonitoringForm, voteGeneralMonitoringDTO);
	    
    	String currentUid=voteMonitoringForm.getCurrentUid();
    	logger.debug("currentUid: " + currentUid);
    	
        VoteUsrAttempt voteUsrAttempt =voteService.getAttemptByUID(new Long(currentUid));
        logger.debug("voteUsrAttempt: " + voteUsrAttempt);
        voteUsrAttempt.setVisible(true);
        
        voteService.updateVoteUsrAttempt(voteUsrAttempt);
        voteService.showOpenVote(voteUsrAttempt);
        logger.debug("voteUsrAttempt: " + voteUsrAttempt);

        String toolContentID=voteMonitoringForm.getToolContentID();
        logger.debug("toolContentID: " + toolContentID);
	    
	    VoteContent voteContent=voteService.retrieveVote(new Long(toolContentID));
		logger.debug("existing voteContent:" + voteContent);
		
    	initSummaryContent(toolContentID , request, voteService, voteGeneralMonitoringDTO);
		logger.debug("post initSummaryContent, voteGeneralMonitoringDTO: " + voteGeneralMonitoringDTO);
		
		logger.debug("calling initInstructionsContent.");
		logger.debug("post initInstructionsContent, voteGeneralMonitoringDTO: " + voteGeneralMonitoringDTO);
		
		logger.debug("calling initStatsContent.");
		initStatsContent(toolContentID, request, voteService, voteGeneralMonitoringDTO);
		logger.debug("post initStatsContent, voteGeneralMonitoringDTO: " + voteGeneralMonitoringDTO);


    	String currentMonitoredToolSession=voteMonitoringForm.getSelectedToolSessionId(); 
	    logger.debug("currentMonitoredToolSession: " + currentMonitoredToolSession);

		refreshSummaryData(request, voteContent, voteService, true, false, currentMonitoredToolSession, null, 
		        true, null, voteGeneralMonitoringDTO, null);
		
	    if (currentMonitoredToolSession.equals(""))
	    {
	        currentMonitoredToolSession="All";
	    }

		if (currentMonitoredToolSession.equals("All"))
	    {
		    voteGeneralMonitoringDTO.setSelectionCase(new Long(2));
		    request.setAttribute(SELECTION_CASE, new Long(2));
	    }
	    else
	    {
		    voteGeneralMonitoringDTO.setSelectionCase(new Long(1));
		    request.setAttribute(SELECTION_CASE, new Long(1));
	    }
		
		logger.debug("SELECTION_CASE: " + request.getAttribute(SELECTION_CASE));
		

		voteGeneralMonitoringDTO.setCurrentMonitoredToolSession(currentMonitoredToolSession);
		voteGeneralMonitoringDTO.setIsMonitoredContentInUse(new Boolean(true).toString());
		
		if (voteService.studentActivityOccurredGlobal(voteContent))
		{
			logger.debug("USER_EXCEPTION_NO_TOOL_SESSIONS is set to false");
			voteGeneralMonitoringDTO.setUserExceptionNoToolSessions(new Boolean(false).toString());
		}
		else
		{
			logger.debug("USER_EXCEPTION_NO_TOOL_SESSIONS is set to true");
			voteGeneralMonitoringDTO.setUserExceptionNoToolSessions(new Boolean(true).toString());
		}

		/**getting instructions screen content from here... */
		voteGeneralMonitoringDTO.setOnlineInstructions(voteContent.getOnlineInstructions());
		voteGeneralMonitoringDTO.setOfflineInstructions(voteContent.getOfflineInstructions());
		
        List attachmentList = voteService.retrieveVoteUploadedFiles(voteContent);
        logger.debug("attachmentList: " + attachmentList);
        voteGeneralMonitoringDTO.setAttachmentList(attachmentList);
        voteGeneralMonitoringDTO.setDeletedAttachmentList(new ArrayList());
        /** ...till here **/
		
		
	    logger.debug("voteGeneralMonitoringDTO: " + voteGeneralMonitoringDTO);
		request.setAttribute(VOTE_GENERAL_MONITORING_DTO, voteGeneralMonitoringDTO);

		if (voteContent != null)
		{
		    //prepareReflectionData(request, voteContent, voteService, null,false);
		    prepareReflectionData(request, voteContent, voteService, null, false, "All");
			
	    	EditActivityDTO editActivityDTO = new EditActivityDTO();
			boolean isContentInUse=VoteUtils.isContentInUse(voteContent);
			logger.debug("isContentInUse:" + isContentInUse);
			if (isContentInUse == true)
			{
			    editActivityDTO.setMonitoredContentInUse(new Boolean(true).toString());
			}
			request.setAttribute(EDIT_ACTIVITY_DTO, editActivityDTO);		
		}

		/*find out if there are any reflection entries, from here*/
		boolean notebookEntriesExist=MonitoringUtil.notebookEntriesExist(voteService, voteContent);
		logger.debug("notebookEntriesExist : " + notebookEntriesExist);
		
		if (notebookEntriesExist)
		{
		    request.setAttribute(NOTEBOOK_ENTRIES_EXIST, new Boolean(true).toString());
		    
		    String userExceptionNoToolSessions=(String)voteGeneralMonitoringDTO.getUserExceptionNoToolSessions();
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
		
		MonitoringUtil.buildVoteStatsDTO(request,voteService, voteContent);
		
	    logger.debug("submitting session to refresh the data from the database: ");
	    return submitSession(mapping, form,  request, response, voteService, voteGeneralMonitoringDTO);
    }


    public ActionForward getVoteNomination(ActionMapping mapping, ActionForm form,
            HttpServletRequest request,HttpServletResponse response) throws IOException,
            ServletException, ToolException
    {
        logger.debug("dispatching getVoteNomination...");
        
        IVoteService voteService = VoteServiceProxy.getVoteService(getServlet().getServletContext());
    	logger.debug("voteService :" +voteService);
    	
    	VoteMonitoringForm voteMonitoringForm = (VoteMonitoringForm) form;
	    logger.debug("voteMonitoringForm :" +voteMonitoringForm);
	    voteMonitoringForm.setVoteService(voteService);

	    VoteGeneralMonitoringDTO voteGeneralMonitoringDTO= new VoteGeneralMonitoringDTO();
	    repopulateRequestParameters(request, voteMonitoringForm, voteGeneralMonitoringDTO);

        String questionUid=request.getParameter("questionUid");
        String sessionUid=request.getParameter("sessionUid");
        
        logger.debug("questionUid: " + questionUid);
        logger.debug("sessionUid: " + sessionUid);

		List userNames=voteService.getStandardAttemptUsersForQuestionContentAndSessionUid(new Long(questionUid), new Long(sessionUid));
		logger.debug("userNames: " + userNames);
		List listVotedLearnersDTO= new LinkedList();
		
		VoteContent voteContent=null;
        Iterator userIterator=userNames.iterator();
    	while (userIterator.hasNext())
    	{
    	    VoteUsrAttempt voteUsrAttempt=(VoteUsrAttempt)userIterator.next();
    	    
    	    if (voteUsrAttempt != null)
    	    {
    	        logger.debug("used voteContent is: ");
    	        voteContent = voteUsrAttempt.getVoteQueContent().getVoteContent();
    	    }
    	    
    	    VoteMonitoredUserDTO voteMonitoredUserDTO= new VoteMonitoredUserDTO();
    	    voteMonitoredUserDTO.setUserName(voteUsrAttempt.getVoteQueUsr().getFullname());
    	    voteMonitoredUserDTO.setAttemptTime(voteUsrAttempt.getAttemptTime());
    	    listVotedLearnersDTO.add(voteMonitoredUserDTO);
    	}
		logger.debug("listVoteAllSessionsDTO: " + listVotedLearnersDTO);
		logger.debug("voteContent: " + voteContent);
		
		
    	EditActivityDTO editActivityDTO = new EditActivityDTO();
		boolean isContentInUse=VoteUtils.isContentInUse(voteContent);
		logger.debug("isContentInUse:" + isContentInUse);
		if (isContentInUse == true)
		{
		    editActivityDTO.setMonitoredContentInUse(new Boolean(true).toString());
		}
		request.setAttribute(EDIT_ACTIVITY_DTO, editActivityDTO);

		
		voteGeneralMonitoringDTO.setMapStudentsVoted(listVotedLearnersDTO);
		voteGeneralMonitoringDTO.setIsMonitoredContentInUse(new Boolean(true).toString());
	    logger.debug("ending getVoteNomination, voteGeneralMonitoringDTO: " + voteGeneralMonitoringDTO);
		request.setAttribute(VOTE_GENERAL_MONITORING_DTO, voteGeneralMonitoringDTO);

		logger.debug("fdwing to: " + VOTE_NOMINATION_VIEWER);
		return (mapping.findForward(VOTE_NOMINATION_VIEWER));          
    }
    
    
	public ActionForward openNotebook(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws IOException,
            ServletException, ToolException
    {
        logger.debug("dispatching openNotebook...");
    	IVoteService VoteService = VoteServiceProxy.getVoteService(getServlet().getServletContext());
		logger.debug("VoteService: " + VoteService);

		
		String uid=request.getParameter("uid");
		logger.debug("uid: " + uid);
		
		String userId=request.getParameter("userId");
		logger.debug("userId: " + userId);
		
		String userName=request.getParameter("userName");
		logger.debug("userName: " + userName);

		String sessionId=request.getParameter("sessionId");
		logger.debug("sessionId: " + sessionId);
		
		
		NotebookEntry notebookEntry = VoteService.getEntry(new Long(sessionId),
				CoreNotebookConstants.NOTEBOOK_TOOL,
				MY_SIGNATURE, new Integer(userId));
		
        logger.debug("notebookEntry: " + notebookEntry);
		
        VoteGeneralLearnerFlowDTO voteGeneralLearnerFlowDTO= new VoteGeneralLearnerFlowDTO();
		if (notebookEntry != null) {
		    String notebookEntryPresentable=VoteUtils.replaceNewLines(notebookEntry.getEntry());
		    voteGeneralLearnerFlowDTO.setNotebookEntry(notebookEntryPresentable);
		    voteGeneralLearnerFlowDTO.setUserName(userName);
		}

		logger.debug("voteGeneralLearnerFlowDTO: " + voteGeneralLearnerFlowDTO);
		request.setAttribute(VOTE_GENERAL_LEARNER_FLOW_DTO, voteGeneralLearnerFlowDTO);
		
		
		logger.debug("fwding to : " + LEARNER_NOTEBOOK);
		return mapping.findForward(LEARNER_NOTEBOOK);
	}
	
    
    protected void repopulateRequestParameters(HttpServletRequest request, VoteMonitoringForm voteMonitoringForm, 
            VoteGeneralMonitoringDTO voteGeneralMonitoringDTO)
    {
        logger.debug("starting repopulateRequestParameters");
	
        String toolContentID=request.getParameter(TOOL_CONTENT_ID);
        logger.debug("toolContentID: " + toolContentID);
        voteMonitoringForm.setToolContentID(toolContentID);
        voteGeneralMonitoringDTO.setToolContentID(toolContentID);
        
        String activeModule=request.getParameter(ACTIVE_MODULE);
        logger.debug("activeModule: " + activeModule);
        voteMonitoringForm.setActiveModule(activeModule);
        voteGeneralMonitoringDTO.setActiveModule(activeModule);
        
        String defineLaterInEditMode=request.getParameter(DEFINE_LATER_IN_EDIT_MODE);
        logger.debug("defineLaterInEditMode: " + defineLaterInEditMode);
        voteMonitoringForm.setDefineLaterInEditMode(defineLaterInEditMode);
        voteGeneralMonitoringDTO.setDefineLaterInEditMode(defineLaterInEditMode);

        
        String isToolSessionChanged=request.getParameter(IS_TOOL_SESSION_CHANGED);
        logger.debug("isToolSessionChanged: " + isToolSessionChanged);
        voteMonitoringForm.setIsToolSessionChanged(isToolSessionChanged);
        voteGeneralMonitoringDTO.setIsToolSessionChanged(isToolSessionChanged);

        String responseId=request.getParameter(RESPONSE_ID);
        logger.debug("responseId: " + responseId);
        voteMonitoringForm.setResponseId(responseId);
        voteGeneralMonitoringDTO.setResponseId(responseId);

        String currentUid=request.getParameter(CURRENT_UID);
        logger.debug("currentUid: " + currentUid);
        voteMonitoringForm.setCurrentUid(currentUid);
        voteGeneralMonitoringDTO.setCurrentUid(currentUid);
    }
    

	
    protected void repopulateRequestParameters(HttpServletRequest request, VoteMonitoringForm voteMonitoringForm, 
            VoteGeneralAuthoringDTO voteGeneralAuthoringDTO)
    {
        logger.debug("starting repopulateRequestParameters");
        
		String contentFolderID = WebUtil.readStrParam(request, AttributeNames.PARAM_CONTENT_FOLDER_ID);
		logger.debug("contentFolderID: " + contentFolderID);
		voteMonitoringForm.setContentFolderID(contentFolderID);
		voteGeneralAuthoringDTO.setContentFolderID(contentFolderID);

	
        String toolContentID=request.getParameter(TOOL_CONTENT_ID);
        logger.debug("toolContentID: " + toolContentID);
        voteMonitoringForm.setToolContentID(toolContentID);
        voteGeneralAuthoringDTO.setToolContentID(toolContentID);
        
        String activeModule=request.getParameter(ACTIVE_MODULE);
        logger.debug("activeModule: " + activeModule);
        voteMonitoringForm.setActiveModule(activeModule);
        voteGeneralAuthoringDTO.setActiveModule(activeModule);
        
        String defineLaterInEditMode=request.getParameter(DEFINE_LATER_IN_EDIT_MODE);
        logger.debug("defineLaterInEditMode: " + defineLaterInEditMode);
        voteMonitoringForm.setDefineLaterInEditMode(defineLaterInEditMode);
        voteGeneralAuthoringDTO.setDefineLaterInEditMode(defineLaterInEditMode);
        
    }
    
    
    /**
     * used in define later mode to switch from view-only to editable screen
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
    	
		VoteMonitoringForm VoteMonitoringForm = (VoteMonitoringForm) form;
		logger.debug("VoteMonitoringForm: " + VoteMonitoringForm);

    	IVoteService voteService = VoteServiceProxy.getVoteService(getServlet().getServletContext());
		logger.debug("voteService: " + voteService);
		
		VoteGeneralMonitoringDTO generalMonitoringDTO = new VoteGeneralMonitoringDTO();
		
		generalMonitoringDTO.setMonitoredContentInUse(new Boolean(false).toString());
		
    	EditActivityDTO editActivityDTO = new EditActivityDTO();
	    editActivityDTO.setMonitoredContentInUse(new Boolean(false).toString());
	    request.setAttribute(EDIT_ACTIVITY_DTO, editActivityDTO);
		
		generalMonitoringDTO.setDefineLaterInEditMode(new Boolean(true).toString());
		
		
        logger.debug("final generalMonitoringDTO: " + generalMonitoringDTO );
		request.setAttribute(VOTE_GENERAL_MONITORING_DTO, generalMonitoringDTO);

		String strToolContentID=request.getParameter(AttributeNames.PARAM_TOOL_CONTENT_ID);
		logger.debug("strToolContentID: " + strToolContentID);
		VoteMonitoringForm.setToolContentID(strToolContentID);
		
		String contentFolderID = WebUtil.readStrParam(request, AttributeNames.PARAM_CONTENT_FOLDER_ID);
		logger.debug("contentFolderID: " + contentFolderID);
		VoteMonitoringForm.setContentFolderID(contentFolderID);
		
		String httpSessionID=request.getParameter("httpSessionID");
		logger.debug("httpSessionID: " + httpSessionID);
		VoteMonitoringForm.setHttpSessionID(httpSessionID);

		VoteContent voteContent=voteService.retrieveVote(new Long(strToolContentID));
		logger.debug("existing voteContent:" + voteContent);
		
		VoteMonitoringForm.setTitle(voteContent.getTitle());

		VoteUtils.setDefineLater(request, true,strToolContentID,  voteService);

		prepareEditActivityScreenData(request, voteContent);
		
		//prepareReflectionData(request, voteContent, voteService, null, false, "All");
		prepareReflectionData(request, voteContent, voteService, null, false, "All");
		
		if (voteService.studentActivityOccurredGlobal(voteContent))
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
		generalMonitoringDTO.setOnlineInstructions(voteContent.getOnlineInstructions());
		generalMonitoringDTO.setOfflineInstructions(voteContent.getOfflineInstructions());
		
        List attachmentList = voteService.retrieveVoteUploadedFiles(voteContent);
        logger.debug("attachmentList: " + attachmentList);
        generalMonitoringDTO.setAttachmentList(attachmentList);
        generalMonitoringDTO.setDeletedAttachmentList(new ArrayList());
        /** ...till here **/

		
    	logger.debug("final generalMonitoringDTO: " + generalMonitoringDTO );
		request.setAttribute(VOTE_GENERAL_MONITORING_DTO, generalMonitoringDTO);
		
		
	    List listNominationContentDTO= new  LinkedList();

		Iterator queIterator=voteContent.getVoteQueContents().iterator();
		while (queIterator.hasNext())
		{
		    VoteNominationContentDTO voteNominationContentDTO=new VoteNominationContentDTO();
		    
			VoteQueContent voteQueContent=(VoteQueContent) queIterator.next();
			if (voteQueContent != null)
			{
				logger.debug("question: " + voteQueContent.getQuestion());
				logger.debug("displayorder: " + new Integer(voteQueContent.getDisplayOrder()).toString());
				
	    		voteNominationContentDTO.setQuestion(voteQueContent.getQuestion());
	    		voteNominationContentDTO.setDisplayOrder(new Integer(voteQueContent.getDisplayOrder()).toString());
	    		listNominationContentDTO.add(voteNominationContentDTO);
			}
		}
		logger.debug("listNominationContentDTO: " + listNominationContentDTO);
		request.setAttribute(LIST_NOMINATION_CONTENT_DTO,listNominationContentDTO);

		request.setAttribute(TOTAL_NOMINATION_COUNT, new Integer(listNominationContentDTO.size()));
		
		VoteGeneralAuthoringDTO VoteGeneralAuthoringDTO = (VoteGeneralAuthoringDTO)request.getAttribute(VOTE_GENERAL_AUTHORING_DTO);
		VoteGeneralAuthoringDTO.setActiveModule(MONITORING);
		
		VoteGeneralAuthoringDTO.setToolContentID(strToolContentID);
		VoteGeneralAuthoringDTO.setContentFolderID(contentFolderID);
		VoteGeneralAuthoringDTO.setHttpSessionID(httpSessionID);

		request.setAttribute(VOTE_GENERAL_AUTHORING_DTO, VoteGeneralAuthoringDTO);
		
		
		/*find out if there are any reflection entries, from here*/
		boolean notebookEntriesExist=MonitoringUtil.notebookEntriesExist(voteService, voteContent);
		logger.debug("notebookEntriesExist : " + notebookEntriesExist);
		
		if (notebookEntriesExist)
		{
		    request.setAttribute(NOTEBOOK_ENTRIES_EXIST, new Boolean(true).toString());
		    
		    String userExceptionNoToolSessions=(String)generalMonitoringDTO.getUserExceptionNoToolSessions();
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

		MonitoringUtil.buildVoteStatsDTO(request,voteService, voteContent);
		MonitoringUtil.generateGroupsSessionData(request, voteService, voteContent);
		
        return (mapping.findForward(LOAD_MONITORING));
    }


	public void prepareEditActivityScreenData(HttpServletRequest request, VoteContent voteContent)
	{
		logger.debug("starting prepareEditActivityScreenData: " + voteContent);
	    VoteGeneralAuthoringDTO voteGeneralAuthoringDTO= new VoteGeneralAuthoringDTO();
		
	    if (voteContent.getTitle() == null)
		{
			voteGeneralAuthoringDTO.setActivityTitle(DEFAULT_VOTING_TITLE);
		}
		else
		{
			voteGeneralAuthoringDTO.setActivityTitle(voteContent.getTitle());
		}

		
		if (voteContent.getInstructions() == null)
		{
		    voteGeneralAuthoringDTO.setActivityInstructions(DEFAULT_VOTING_INSTRUCTIONS);
		}
		else
		{
			voteGeneralAuthoringDTO.setActivityInstructions(voteContent.getInstructions());
		}
		
	
		logger.debug("final voteGeneralAuthoringDTO: " + voteGeneralAuthoringDTO);
		request.setAttribute(VOTE_GENERAL_AUTHORING_DTO, voteGeneralAuthoringDTO);
	}
	
	

        
    
    /**
     * submits content into the tool database
     * ActionForward submitAllContent(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) 
        throws IOException, ServletException
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
    	
    	VoteMonitoringForm voteAuthoringForm = (VoteMonitoringForm) form;

		IVoteService voteService =VoteServiceProxy.getVoteService(getServlet().getServletContext());
		logger.debug("voteService: " + voteService);
		
		String httpSessionID=voteAuthoringForm.getHttpSessionID();
		logger.debug("httpSessionID: " + httpSessionID);
		
		SessionMap sessionMap=(SessionMap)request.getSession().getAttribute(httpSessionID);
		logger.debug("sessionMap: " + sessionMap);

		String contentFolderID = WebUtil.readStrParam(request, AttributeNames.PARAM_CONTENT_FOLDER_ID);
		logger.debug("contentFolderID: " + contentFolderID);
		voteAuthoringForm.setContentFolderID(contentFolderID);

		String activeModule=request.getParameter(ACTIVE_MODULE);
		logger.debug("activeModule: " + activeModule);

		String strToolContentID=request.getParameter(AttributeNames.PARAM_TOOL_CONTENT_ID);
		logger.debug("strToolContentID: " + strToolContentID);

		String defaultContentIdStr=request.getParameter(DEFAULT_CONTENT_ID_STR);
		logger.debug("defaultContentIdStr: " + defaultContentIdStr);
		
        List listNominationContentDTO=(List)sessionMap.get(LIST_NOMINATION_CONTENT_DTO_KEY);
        logger.debug("listNominationContentDTO: " + listNominationContentDTO);
        
        Map mapNominationContent=AuthoringUtil.extractMapNominationContent(listNominationContentDTO);
        logger.debug("extracted mapNominationContent: " + mapNominationContent);
                

        Map mapFeedback=AuthoringUtil.extractMapFeedback(listNominationContentDTO);
        logger.debug("extracted mapFeedback: " + mapFeedback);

        ActionMessages errors = new ActionMessages();
        logger.debug("mapNominationContent size: " + mapNominationContent.size());
        
        if (mapNominationContent.size() == 0)
        {
            ActionMessage error = new ActionMessage("nominations.none.submitted");
			errors.add(ActionMessages.GLOBAL_MESSAGE, error);
        }
        logger.debug("errors: " + errors);
	    
	    AuthoringUtil authoringUtil= new AuthoringUtil();
	 	
	 	VoteGeneralAuthoringDTO voteGeneralAuthoringDTO= new VoteGeneralAuthoringDTO();
	 	
        logger.debug("activeModule: " + activeModule);
		voteGeneralAuthoringDTO.setContentFolderID(contentFolderID);
		
	 	String richTextTitle = request.getParameter(TITLE);
        String richTextInstructions = request.getParameter(INSTRUCTIONS);
	 	
        logger.debug("richTextTitle: " + richTextTitle);
        logger.debug("richTextInstructions: " + richTextInstructions);
        
        voteGeneralAuthoringDTO.setActivityTitle(richTextTitle);
        voteAuthoringForm.setTitle(richTextTitle);
        
        voteGeneralAuthoringDTO.setActivityInstructions(richTextInstructions);
        
        sessionMap.put(ACTIVITY_TITLE_KEY, richTextTitle);
        sessionMap.put(ACTIVITY_INSTRUCTIONS_KEY, richTextInstructions);
        
	    voteGeneralAuthoringDTO.setMapNominationContent(mapNominationContent);
		logger.debug("voteGeneralAuthoringDTO: " + voteGeneralAuthoringDTO);
        
		logger.debug("voteGeneralAuthoringDTO now: " + voteGeneralAuthoringDTO);
		request.setAttribute(VOTE_GENERAL_AUTHORING_DTO, voteGeneralAuthoringDTO);
		
	 	logger.debug("there are no issues with input, continue and submit data");

        VoteContent voteContentTest=voteService.retrieveVote(new Long(strToolContentID));
		logger.debug("voteContentTest: " + voteContentTest);
		

		logger.debug("errors: " + errors);
	 	if(!errors.isEmpty()){
			saveErrors(request, errors);
	        logger.debug("errors saved: " + errors);
		}
	 	
	 	repopulateRequestParameters(request, voteAuthoringForm, voteGeneralAuthoringDTO);
	 	
	 	VoteGeneralMonitoringDTO voteGeneralMonitoringDTO = new VoteGeneralMonitoringDTO(); 
	 	

	 	VoteContent voteContent=voteContentTest;
	 	if(errors.isEmpty()){
	 	    logger.debug("errors is empty: " + errors);
		 	/*to remove deleted entries in the questions table based on mapNominationContent */
	        authoringUtil.removeRedundantNominations(mapNominationContent, voteService, voteAuthoringForm, request, strToolContentID);
	        logger.debug("end of removing unused entries... ");

	        voteContent=authoringUtil.saveOrUpdateVoteContent(mapNominationContent, mapFeedback, voteService, voteAuthoringForm, request, voteContentTest, strToolContentID);
	        logger.debug("voteContent: " + voteContent);
	        
	        
	        long defaultContentID=0;
			logger.debug("attempt retrieving tool with signatute : " + MY_SIGNATURE);
	        defaultContentID=voteService.getToolDefaultContentIdBySignature(MY_SIGNATURE);
			logger.debug("retrieved tool default contentId: " + defaultContentID);
			
	        if (voteContent != null)
	        {
	            voteGeneralAuthoringDTO.setDefaultContentIdStr(new Long(defaultContentID).toString());
	        }
			logger.debug("updated voteGeneralAuthoringDTO to: " + voteGeneralAuthoringDTO);
			
		    authoringUtil.reOrganizeDisplayOrder(mapNominationContent, voteService, voteAuthoringForm, voteContent);    

		    logger.debug("strToolContentID: " + strToolContentID);
	        VoteUtils.setDefineLater(request, false, strToolContentID, voteService);
	        logger.debug("define later set to false");
	        
			//VoteUtils.setFormProperties(request, voteService,  
		   //          voteAuthoringForm, voteGeneralAuthoringDTO, strToolContentID, defaultContentIdStr, activeModule, sessionMap, httpSessionID);

		    voteGeneralMonitoringDTO.setDefineLaterInEditMode(new Boolean(false).toString());
	 	}
	 	else
	 	{
	 	   logger.debug("errors is not empty: " + errors);
	 	   
	 	   if (voteContent != null)
	 	   {
		        long defaultContentID=0;
				logger.debug("attempt retrieving tool with signatute : " + MY_SIGNATURE);
		        defaultContentID=voteService.getToolDefaultContentIdBySignature(MY_SIGNATURE);
				logger.debug("retrieved tool default contentId: " + defaultContentID);
				
		        if (voteContent != null)
		        {
		            voteGeneralAuthoringDTO.setDefaultContentIdStr(new Long(defaultContentID).toString());
		        }

				//VoteUtils.setFormProperties(request, voteService, 
			    //         voteAuthoringForm, voteGeneralAuthoringDTO, strToolContentID, defaultContentIdStr, activeModule, sessionMap, httpSessionID);
	 	       
	 	   }
	 	   
	 	  voteGeneralMonitoringDTO.setDefineLaterInEditMode(new Boolean(true).toString());
	 	}
        

        voteGeneralAuthoringDTO.setSbmtSuccess(new Integer(1).toString());
        
        voteAuthoringForm.resetUserAction();
        voteGeneralAuthoringDTO.setMapNominationContent(mapNominationContent);
        

		request.setAttribute(LIST_NOMINATION_CONTENT_DTO,listNominationContentDTO);
		sessionMap.put(LIST_NOMINATION_CONTENT_DTO_KEY, listNominationContentDTO);
	    request.getSession().setAttribute(httpSessionID, sessionMap);
	    
	    request.setAttribute(TOTAL_NOMINATION_COUNT, new Integer(listNominationContentDTO.size()));
		
		voteGeneralAuthoringDTO.setToolContentID(strToolContentID);
		voteGeneralAuthoringDTO.setHttpSessionID(httpSessionID);
		voteGeneralAuthoringDTO.setActiveModule(activeModule);
		voteGeneralAuthoringDTO.setDefaultContentIdStr(defaultContentIdStr);
		
		voteAuthoringForm.setToolContentID(strToolContentID);
		voteAuthoringForm.setHttpSessionID(httpSessionID);
		voteAuthoringForm.setActiveModule(activeModule);
		voteAuthoringForm.setDefaultContentIdStr(defaultContentIdStr);
		voteAuthoringForm.setCurrentTab("3");

        logger.debug("before saving final voteGeneralAuthoringDTO: " + voteGeneralAuthoringDTO);
		request.setAttribute(VOTE_GENERAL_AUTHORING_DTO, voteGeneralAuthoringDTO);

		if (voteService.studentActivityOccurredGlobal(voteContent))
		{
			logger.debug("USER_EXCEPTION_NO_TOOL_SESSIONS is set to false");
			voteGeneralMonitoringDTO.setUserExceptionNoToolSessions(new Boolean(false).toString());
		}
		else
		{
			logger.debug("USER_EXCEPTION_NO_TOOL_SESSIONS is set to true");
			voteGeneralMonitoringDTO.setUserExceptionNoToolSessions(new Boolean(true).toString());
		}
		
		/**getting instructions screen content from here... */
		voteGeneralMonitoringDTO.setOnlineInstructions(voteContent.getOnlineInstructions());
		voteGeneralMonitoringDTO.setOfflineInstructions(voteContent.getOfflineInstructions());
		
        List attachmentList = voteService.retrieveVoteUploadedFiles(voteContent);
        logger.debug("attachmentList: " + attachmentList);
        voteGeneralMonitoringDTO.setAttachmentList(attachmentList);
        voteGeneralMonitoringDTO.setDeletedAttachmentList(new ArrayList());
        /** ...till here **/

		
		logger.debug("end of refreshSummaryData, voteGeneralMonitoringDTO" + voteGeneralMonitoringDTO);
		request.setAttribute(VOTE_GENERAL_MONITORING_DTO, voteGeneralMonitoringDTO);

        
		if (voteContent != null)
		{
		    //prepareReflectionData(request, voteContent, voteService, null,false);
		    prepareReflectionData(request, voteContent, voteService, null, false, "All");
			
	    	EditActivityDTO editActivityDTO = new EditActivityDTO();
			boolean isContentInUse=VoteUtils.isContentInUse(voteContent);
			logger.debug("isContentInUse:" + isContentInUse);
			if (isContentInUse == true)
			{
			    editActivityDTO.setMonitoredContentInUse(new Boolean(false).toString());
			}
			request.setAttribute(EDIT_ACTIVITY_DTO, editActivityDTO);		
		}
		
		/*find out if there are any reflection entries, from here*/
		boolean notebookEntriesExist=MonitoringUtil.notebookEntriesExist(voteService, voteContent);
		logger.debug("notebookEntriesExist : " + notebookEntriesExist);
		
		if (notebookEntriesExist)
		{
		    request.setAttribute(NOTEBOOK_ENTRIES_EXIST, new Boolean(true).toString());
		    
		    String userExceptionNoToolSessions=(String)voteGeneralMonitoringDTO.getUserExceptionNoToolSessions();
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
		
		MonitoringUtil.buildVoteStatsDTO(request,voteService, voteContent);

		
		logger.debug("forwarding to :" + LOAD_MONITORING);
        return mapping.findForward(LOAD_MONITORING);
    }

    
    /**
     * saveSingleNomination
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws IOException
     * @throws ServletException
     */
	public ActionForward saveSingleNomination(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) 
    throws IOException, ServletException {
		
		logger.debug("dispathcing saveSingleNomination");
		VoteMonitoringForm voteAuthoringForm = (VoteMonitoringForm) form;
	
		IVoteService voteService =VoteServiceProxy.getVoteService(getServlet().getServletContext());
		logger.debug("voteService: " + voteService);
		
		String httpSessionID=voteAuthoringForm.getHttpSessionID();
		logger.debug("httpSessionID: " + httpSessionID);
		
		SessionMap sessionMap=(SessionMap)request.getSession().getAttribute(httpSessionID);
		logger.debug("sessionMap: " + sessionMap);
		
		String contentFolderID = WebUtil.readStrParam(request, AttributeNames.PARAM_CONTENT_FOLDER_ID);
		logger.debug("contentFolderID: " + contentFolderID);
		voteAuthoringForm.setContentFolderID(contentFolderID);
	
	
		String activeModule=request.getParameter(ACTIVE_MODULE);
		logger.debug("activeModule: " + activeModule);
	
		String strToolContentID=request.getParameter(AttributeNames.PARAM_TOOL_CONTENT_ID);
		logger.debug("strToolContentID: " + strToolContentID);
		
		String defaultContentIdStr=request.getParameter(DEFAULT_CONTENT_ID_STR);
		logger.debug("defaultContentIdStr: " + defaultContentIdStr);
		
		String editNominationBoxRequest=request.getParameter("editNominationBoxRequest");
		logger.debug("editNominationBoxRequest: " + editNominationBoxRequest);
		
		VoteContent voteContent=voteService.retrieveVote(new Long(strToolContentID));
		logger.debug("voteContent: " + voteContent);
		
		VoteGeneralAuthoringDTO voteGeneralAuthoringDTO= new VoteGeneralAuthoringDTO();
		
		logger.debug("voteGeneralAuthoringDTO: " + voteGeneralAuthoringDTO);
		voteGeneralAuthoringDTO.setContentFolderID(contentFolderID);
		
		voteGeneralAuthoringDTO.setSbmtSuccess(new Integer(0).toString());
		
	    AuthoringUtil authoringUtil= new AuthoringUtil();
	    
        List listNominationContentDTO=(List)sessionMap.get(LIST_NOMINATION_CONTENT_DTO_KEY);
        logger.debug("listNominationContentDTO: " + listNominationContentDTO);
        
        
	    String newNomination=request.getParameter("newNomination");
	    logger.debug("newNomination: " + newNomination);
	    
	    
	    String editableNominationIndex=request.getParameter("editableNominationIndex");
	    logger.debug("editableNominationIndex: " + editableNominationIndex);
	    

	    if ((newNomination != null) && (newNomination.length() > 0))
	    {
		        if ((editNominationBoxRequest != null) && (editNominationBoxRequest.equals("false")))
		        {
		            logger.debug("request for add and save");
			        boolean duplicates=AuthoringUtil.checkDuplicateNominations(listNominationContentDTO, newNomination);
			        logger.debug("duplicates: " + duplicates);
		            
			        if (!duplicates)
			        {
					    VoteNominationContentDTO voteNominationContentDTO= null;
					    Iterator listIterator=listNominationContentDTO.iterator();
					    while (listIterator.hasNext())
					    {
					        voteNominationContentDTO= (VoteNominationContentDTO)listIterator.next();
					        logger.debug("voteNominationContentDTO:" + voteNominationContentDTO);
					        logger.debug("voteNominationContentDTO question:" + voteNominationContentDTO.getQuestion());
					        
					        String question=voteNominationContentDTO.getQuestion();
					        String displayOrder=voteNominationContentDTO.getDisplayOrder();
					        logger.debug("displayOrder:" + displayOrder);
					        
					        if ((displayOrder != null) && (!displayOrder.equals("")))
				    		{
					            if (displayOrder.equals(editableNominationIndex))
					            {
					                break;
					            }
					            
				    		}
					    }
					    logger.debug("voteNominationContentDTO found:" + voteNominationContentDTO);
					    
					    voteNominationContentDTO.setQuestion(newNomination);
					    voteNominationContentDTO.setDisplayOrder(editableNominationIndex);
					    
					    listNominationContentDTO=AuthoringUtil.reorderUpdateListNominationContentDTO(listNominationContentDTO, voteNominationContentDTO, editableNominationIndex);
					    logger.debug("post reorderUpdateListNominationContentDTO listNominationContentDTO: " + listNominationContentDTO);
			        }
			        else
			        {
			            logger.debug("duplicate question entry, not adding");
			        }
		        }
			    else
			    {
			        	logger.debug("request for edit and save.");
					    VoteNominationContentDTO voteNominationContentDTO= null;
					    Iterator listIterator=listNominationContentDTO.iterator();
					    while (listIterator.hasNext())
					    {
					        voteNominationContentDTO= (VoteNominationContentDTO)listIterator.next();
					        logger.debug("voteNominationContentDTO:" + voteNominationContentDTO);
					        logger.debug("voteNominationContentDTO question:" + voteNominationContentDTO.getQuestion());
					        
					        String question=voteNominationContentDTO.getQuestion();
					        String displayOrder=voteNominationContentDTO.getDisplayOrder();
					        logger.debug("displayOrder:" + displayOrder);
					        
					        if ((displayOrder != null) && (!displayOrder.equals("")))
				    		{
					            if (displayOrder.equals(editableNominationIndex))
					            {
					                break;
					            }
					            
				    		}
					    }
					    logger.debug("voteNominationContentDTO found:" + voteNominationContentDTO);
					    
					    voteNominationContentDTO.setQuestion(newNomination);
					    voteNominationContentDTO.setDisplayOrder(editableNominationIndex);
					    
					    listNominationContentDTO=AuthoringUtil.reorderUpdateListNominationContentDTO(listNominationContentDTO, voteNominationContentDTO, editableNominationIndex);
					    logger.debug("post reorderUpdateListQuestionContentDTO listQuestionContentDTO: " + listNominationContentDTO);			        
			  }
	    }
        else
        {
            logger.debug("entry blank, not adding");
        }
	    
	    
	    

	    request.setAttribute(LIST_NOMINATION_CONTENT_DTO,listNominationContentDTO);
		sessionMap.put(LIST_NOMINATION_CONTENT_DTO_KEY, listNominationContentDTO);
	    logger.debug("listNominationContentDTO now: " + listNominationContentDTO);
	    
	    
		String richTextTitle = request.getParameter(TITLE);
	    String richTextInstructions = request.getParameter(INSTRUCTIONS);
	
	    logger.debug("richTextTitle: " + richTextTitle);
	    logger.debug("richTextInstructions: " + richTextInstructions);
	    voteGeneralAuthoringDTO.setActivityTitle(richTextTitle);
	    voteAuthoringForm.setTitle(richTextTitle);
	    
		voteGeneralAuthoringDTO.setActivityInstructions(richTextInstructions);
			
	    sessionMap.put(ACTIVITY_TITLE_KEY, richTextTitle);
	    sessionMap.put(ACTIVITY_INSTRUCTIONS_KEY, richTextInstructions);
	
	    
	    logger.debug("activeModule: " + activeModule);
	    voteGeneralAuthoringDTO.setEditActivityEditMode(new Boolean(true).toString());
	    
	    request.getSession().setAttribute(httpSessionID, sessionMap);
	    sessionMap.put(LIST_NOMINATION_CONTENT_DTO_KEY, listNominationContentDTO);
	    
		//VoteUtils.setFormProperties(request, voteService,  
	    //         voteAuthoringForm, voteGeneralAuthoringDTO, strToolContentID, defaultContentIdStr, activeModule, sessionMap, httpSessionID);
	
		
		voteGeneralAuthoringDTO.setToolContentID(strToolContentID);
		voteGeneralAuthoringDTO.setHttpSessionID(httpSessionID);
		voteGeneralAuthoringDTO.setActiveModule(activeModule);
		voteGeneralAuthoringDTO.setDefaultContentIdStr(defaultContentIdStr);
		
		voteAuthoringForm.setToolContentID(strToolContentID);
		voteAuthoringForm.setHttpSessionID(httpSessionID);
		voteAuthoringForm.setActiveModule(activeModule);
		voteAuthoringForm.setDefaultContentIdStr(defaultContentIdStr);
		voteAuthoringForm.setCurrentTab("3");
     	
		voteGeneralAuthoringDTO.setDefineLaterInEditMode(new Boolean(true).toString());
		
		
		repopulateRequestParameters(request, voteAuthoringForm, voteGeneralAuthoringDTO);
		logger.debug("voteGeneralAuthoringDTO now: " + voteGeneralAuthoringDTO);
		request.setAttribute(VOTE_GENERAL_AUTHORING_DTO, voteGeneralAuthoringDTO);
	    
	    logger.debug("httpSessionID: " + httpSessionID);
	    logger.debug("sessionMap: " + sessionMap);
	    
	    request.getSession().setAttribute(httpSessionID, sessionMap);
	    logger.debug("voteGeneralAuthoringDTO.getMapNominationContent(); " + voteGeneralAuthoringDTO.getMapNominationContent());
		
	    request.setAttribute(TOTAL_NOMINATION_COUNT, new Integer(listNominationContentDTO.size()));

		if (voteContent != null)
		{
		    //prepareReflectionData(request, voteContent, voteService, null,false);
		    prepareReflectionData(request, voteContent, voteService, null, false, "All");
			
	    	EditActivityDTO editActivityDTO = new EditActivityDTO();
			boolean isContentInUse=VoteUtils.isContentInUse(voteContent);
			logger.debug("isContentInUse:" + isContentInUse);
			if (isContentInUse == true)
			{
			    editActivityDTO.setMonitoredContentInUse(new Boolean(false).toString());
			}
			request.setAttribute(EDIT_ACTIVITY_DTO, editActivityDTO);		
		}

		
		VoteGeneralMonitoringDTO voteGeneralMonitoringDTO= new VoteGeneralMonitoringDTO();
		voteGeneralMonitoringDTO.setDefineLaterInEditMode(new Boolean(true).toString());
		
		if (voteService.studentActivityOccurredGlobal(voteContent))
		{
			logger.debug("USER_EXCEPTION_NO_TOOL_SESSIONS is set to false");
			voteGeneralMonitoringDTO.setUserExceptionNoToolSessions(new Boolean(false).toString());
		}
		else
		{
			logger.debug("USER_EXCEPTION_NO_TOOL_SESSIONS is set to true");
			voteGeneralMonitoringDTO.setUserExceptionNoToolSessions(new Boolean(true).toString());
		}
		
		/**getting instructions screen content from here... */
		voteGeneralMonitoringDTO.setOnlineInstructions(voteContent.getOnlineInstructions());
		voteGeneralMonitoringDTO.setOfflineInstructions(voteContent.getOfflineInstructions());
		
        List attachmentList = voteService.retrieveVoteUploadedFiles(voteContent);
        logger.debug("attachmentList: " + attachmentList);
        voteGeneralMonitoringDTO.setAttachmentList(attachmentList);
        voteGeneralMonitoringDTO.setDeletedAttachmentList(new ArrayList());
        /** ...till here **/

		
		logger.debug("end of refreshSummaryData, voteGeneralMonitoringDTO" + voteGeneralMonitoringDTO);
		request.setAttribute(VOTE_GENERAL_MONITORING_DTO, voteGeneralMonitoringDTO);

		
		
		/*find out if there are any reflection entries, from here*/
		boolean notebookEntriesExist=MonitoringUtil.notebookEntriesExist(voteService, voteContent);
		logger.debug("notebookEntriesExist : " + notebookEntriesExist);
		
		if (notebookEntriesExist)
		{
		    request.setAttribute(NOTEBOOK_ENTRIES_EXIST, new Boolean(true).toString());
		    
		    String userExceptionNoToolSessions=(String)voteGeneralAuthoringDTO.getUserExceptionNoToolSessions();
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
		
		MonitoringUtil.buildVoteStatsDTO(request,voteService, voteContent);

		
		logger.debug("fwd ing to LOAD_MONITORING: " + LOAD_MONITORING);
	    return (mapping.findForward(LOAD_MONITORING));
	}

    
    
    /**
     * addSingleNomination
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws IOException
     * @throws ServletException
     */
	public ActionForward addSingleNomination(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) 
	    throws IOException, ServletException {
		
		logger.debug("dispathcing addSingleNomination");
		VoteMonitoringForm voteAuthoringForm = (VoteMonitoringForm) form;
	
		IVoteService voteService =VoteServiceProxy.getVoteService(getServlet().getServletContext());
		logger.debug("voteService: " + voteService);
		
		String httpSessionID=voteAuthoringForm.getHttpSessionID();
		logger.debug("httpSessionID: " + httpSessionID);
		
		SessionMap sessionMap=(SessionMap)request.getSession().getAttribute(httpSessionID);
		logger.debug("sessionMap: " + sessionMap);
		
		String contentFolderID = WebUtil.readStrParam(request, AttributeNames.PARAM_CONTENT_FOLDER_ID);
		logger.debug("contentFolderID: " + contentFolderID);
		voteAuthoringForm.setContentFolderID(contentFolderID);
	
	
		String activeModule=request.getParameter(ACTIVE_MODULE);
		logger.debug("activeModule: " + activeModule);
	
		String strToolContentID=request.getParameter(AttributeNames.PARAM_TOOL_CONTENT_ID);
		logger.debug("strToolContentID: " + strToolContentID);
		
		String defaultContentIdStr=request.getParameter(DEFAULT_CONTENT_ID_STR);
		logger.debug("defaultContentIdStr: " + defaultContentIdStr);
		
		VoteContent voteContent=voteService.retrieveVote(new Long(strToolContentID));
		logger.debug("voteContent: " + voteContent);
		
		
		VoteGeneralAuthoringDTO voteGeneralAuthoringDTO= new VoteGeneralAuthoringDTO();
		logger.debug("voteGeneralAuthoringDTO: " + voteGeneralAuthoringDTO);
		voteGeneralAuthoringDTO.setContentFolderID(contentFolderID);
		
		voteGeneralAuthoringDTO.setSbmtSuccess(new Integer(0).toString());
		
	    AuthoringUtil authoringUtil= new AuthoringUtil();
	    
        List listNominationContentDTO=(List)sessionMap.get(LIST_NOMINATION_CONTENT_DTO_KEY);
        logger.debug("listNominationContentDTO: " + listNominationContentDTO);
        
	    String newNomination=request.getParameter("newNomination");
	    logger.debug("newNomination: " + newNomination);
	    
	    int listSize=listNominationContentDTO.size();
	    logger.debug("listSize: " + listSize);
	    
	    if ((newNomination != null) && (newNomination.length() > 0))
	    {
	        boolean duplicates=AuthoringUtil.checkDuplicateNominations(listNominationContentDTO, newNomination);
	        logger.debug("duplicates: " + duplicates);
	        
	        if (!duplicates)
	        {
			    VoteNominationContentDTO voteNominationContentDTO=new VoteNominationContentDTO();
			    voteNominationContentDTO.setDisplayOrder(new Long(listSize+1).toString());
			    voteNominationContentDTO.setNomination(newNomination);
			    
			    listNominationContentDTO.add(voteNominationContentDTO);
			    logger.debug("updated listNominationContentDTO: " + listNominationContentDTO);	            
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
	    
	    
	    request.setAttribute(LIST_NOMINATION_CONTENT_DTO,listNominationContentDTO);
		sessionMap.put(LIST_NOMINATION_CONTENT_DTO_KEY, listNominationContentDTO);

	    
		String richTextTitle = request.getParameter(TITLE);
	    String richTextInstructions = request.getParameter(INSTRUCTIONS);
	
	    logger.debug("richTextTitle: " + richTextTitle);
	    logger.debug("richTextInstructions: " + richTextInstructions);
	    voteGeneralAuthoringDTO.setActivityTitle(richTextTitle);
	    voteAuthoringForm.setTitle(richTextTitle);
	    
		voteGeneralAuthoringDTO.setActivityInstructions(richTextInstructions);
			
	    sessionMap.put(ACTIVITY_TITLE_KEY, richTextTitle);
	    sessionMap.put(ACTIVITY_INSTRUCTIONS_KEY, richTextInstructions);
	
		logger.debug("activeModule: " + activeModule);
	
	    voteGeneralAuthoringDTO.setEditActivityEditMode(new Boolean(true).toString());
	    request.getSession().setAttribute(httpSessionID, sessionMap);
	
		
		voteGeneralAuthoringDTO.setToolContentID(strToolContentID);
		voteGeneralAuthoringDTO.setHttpSessionID(httpSessionID);
		voteGeneralAuthoringDTO.setActiveModule(activeModule);
		voteGeneralAuthoringDTO.setDefaultContentIdStr(defaultContentIdStr);
		
		voteAuthoringForm.setToolContentID(strToolContentID);
		voteAuthoringForm.setHttpSessionID(httpSessionID);
		voteAuthoringForm.setActiveModule(activeModule);
		voteAuthoringForm.setDefaultContentIdStr(defaultContentIdStr);
		voteAuthoringForm.setCurrentTab("3");
		
		voteGeneralAuthoringDTO.setDefineLaterInEditMode(new Boolean(true).toString());
	
	    repopulateRequestParameters(request, voteAuthoringForm, voteGeneralAuthoringDTO);
		logger.debug("voteGeneralAuthoringDTO now: " + voteGeneralAuthoringDTO);
		request.setAttribute(VOTE_GENERAL_AUTHORING_DTO, voteGeneralAuthoringDTO);
	    
	    logger.debug("httpSessionID: " + httpSessionID);
	    logger.debug("sessionMap: " + sessionMap);
	    
	    request.getSession().setAttribute(httpSessionID, sessionMap);

	    logger.debug("voteGeneralAuthoringDTO.getMapNominationContent(); " + voteGeneralAuthoringDTO.getMapNominationContent());
	    request.setAttribute(TOTAL_NOMINATION_COUNT, new Integer(listNominationContentDTO.size()));
		
		if (voteContent != null)
		{
		    //prepareReflectionData(request, voteContent, voteService, null,false);
		    prepareReflectionData(request, voteContent, voteService, null, false, "All");
		}

		
		VoteGeneralMonitoringDTO voteGeneralMonitoringDTO= new VoteGeneralMonitoringDTO();
		voteGeneralMonitoringDTO.setDefineLaterInEditMode(new Boolean(true).toString());
		
		if (voteService.studentActivityOccurredGlobal(voteContent))
		{
			logger.debug("USER_EXCEPTION_NO_TOOL_SESSIONS is set to false");
			voteGeneralMonitoringDTO.setUserExceptionNoToolSessions(new Boolean(false).toString());
		}
		else
		{
			logger.debug("USER_EXCEPTION_NO_TOOL_SESSIONS is set to true");
			voteGeneralMonitoringDTO.setUserExceptionNoToolSessions(new Boolean(true).toString());
		}
		
		/**getting instructions screen content from here... */
		voteGeneralMonitoringDTO.setOnlineInstructions(voteContent.getOnlineInstructions());
		voteGeneralMonitoringDTO.setOfflineInstructions(voteContent.getOfflineInstructions());
		
        List attachmentList = voteService.retrieveVoteUploadedFiles(voteContent);
        logger.debug("attachmentList: " + attachmentList);
        voteGeneralMonitoringDTO.setAttachmentList(attachmentList);
        voteGeneralMonitoringDTO.setDeletedAttachmentList(new ArrayList());
        /** ...till here **/

		
		logger.debug("end of refreshSummaryData, voteGeneralMonitoringDTO" + voteGeneralMonitoringDTO);
		request.setAttribute(VOTE_GENERAL_MONITORING_DTO, voteGeneralMonitoringDTO);
		
		
		
		/*find out if there are any reflection entries, from here*/
		boolean notebookEntriesExist=MonitoringUtil.notebookEntriesExist(voteService, voteContent);
		logger.debug("notebookEntriesExist : " + notebookEntriesExist);
		
		if (notebookEntriesExist)
		{
		    request.setAttribute(NOTEBOOK_ENTRIES_EXIST, new Boolean(true).toString());
		    
		    String userExceptionNoToolSessions=(String)voteGeneralAuthoringDTO.getUserExceptionNoToolSessions();
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
		
		MonitoringUtil.buildVoteStatsDTO(request,voteService, voteContent);
		
		logger.debug("fwd ing to LOAD_MONITORING: " + LOAD_MONITORING);
	    return (mapping.findForward(LOAD_MONITORING));
	}
    
    /**
     * opens up an new screen within the current page for adding a new question
     * 
     * newNominationBox
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws IOException
     * @throws ServletException
     */
    public ActionForward newNominationBox(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) 
    throws IOException, ServletException 
    {
        logger.debug("dispathcing newNominationBox");
        VoteMonitoringForm voteAuthoringForm = (VoteMonitoringForm) form;
		
		IVoteService voteService =VoteServiceProxy.getVoteService(getServlet().getServletContext());
		logger.debug("voteService: " + voteService);
		
		String httpSessionID=voteAuthoringForm.getHttpSessionID();
		logger.debug("httpSessionID: " + httpSessionID);
		
		SessionMap sessionMap=(SessionMap)request.getSession().getAttribute(httpSessionID);
		logger.debug("sessionMap: " + sessionMap);
		
		String contentFolderID = WebUtil.readStrParam(request, AttributeNames.PARAM_CONTENT_FOLDER_ID);
		logger.debug("contentFolderID: " + contentFolderID);
		voteAuthoringForm.setContentFolderID(contentFolderID);


		String activeModule=request.getParameter(ACTIVE_MODULE);
		logger.debug("activeModule: " + activeModule);

		String strToolContentID=request.getParameter(AttributeNames.PARAM_TOOL_CONTENT_ID);
		logger.debug("strToolContentID: " + strToolContentID);
		
		String defaultContentIdStr=request.getParameter(DEFAULT_CONTENT_ID_STR);
		logger.debug("defaultContentIdStr: " + defaultContentIdStr);
		
		VoteContent voteContent=voteService.retrieveVote(new Long(strToolContentID));
		logger.debug("voteContent: " + voteContent);
		
		
		VoteGeneralAuthoringDTO voteGeneralAuthoringDTO= new VoteGeneralAuthoringDTO();
		logger.debug("voteGeneralAuthoringDTO: " + voteGeneralAuthoringDTO);
		voteGeneralAuthoringDTO.setContentFolderID(contentFolderID);

		String richTextTitle = request.getParameter(TITLE);
	    String richTextInstructions = request.getParameter(INSTRUCTIONS);
	
	    logger.debug("richTextTitle: " + richTextTitle);
	    logger.debug("richTextInstructions: " + richTextInstructions);
	    voteGeneralAuthoringDTO.setActivityTitle(richTextTitle);
	    voteAuthoringForm.setTitle(richTextTitle);
	    
		voteGeneralAuthoringDTO.setActivityInstructions(richTextInstructions);
			
		logger.debug("activeModule: " + activeModule);
	    
	    voteGeneralAuthoringDTO.setDefineLaterInEditMode(new Boolean(true).toString());
	
		repopulateRequestParameters(request, voteAuthoringForm, voteGeneralAuthoringDTO);
		logger.debug("voteGeneralAuthoringDTO now: " + voteGeneralAuthoringDTO);
		request.setAttribute(VOTE_GENERAL_AUTHORING_DTO, voteGeneralAuthoringDTO);

	    
        List listNominationContentDTO=(List)sessionMap.get(LIST_NOMINATION_CONTENT_DTO_KEY);
        logger.debug("listNominationContentDTO: " + listNominationContentDTO);
		request.setAttribute(TOTAL_NOMINATION_COUNT, new Integer(listNominationContentDTO.size()));

		if (voteContent != null)
		{
		    //prepareReflectionData(request, voteContent, voteService, null,false);
		    prepareReflectionData(request, voteContent, voteService, null, false, "All");
			
	    	EditActivityDTO editActivityDTO = new EditActivityDTO();
			boolean isContentInUse=VoteUtils.isContentInUse(voteContent);
			logger.debug("isContentInUse:" + isContentInUse);
			if (isContentInUse == true)
			{
			    editActivityDTO.setMonitoredContentInUse(new Boolean(false).toString());
			}
			request.setAttribute(EDIT_ACTIVITY_DTO, editActivityDTO);		
		}

		VoteGeneralMonitoringDTO voteGeneralMonitoringDTO= new VoteGeneralMonitoringDTO();
		voteGeneralMonitoringDTO.setDefineLaterInEditMode(new Boolean(true).toString());
		
		if (voteService.studentActivityOccurredGlobal(voteContent))
		{
			logger.debug("USER_EXCEPTION_NO_TOOL_SESSIONS is set to false");
			voteGeneralMonitoringDTO.setUserExceptionNoToolSessions(new Boolean(false).toString());
		}
		else
		{
			logger.debug("USER_EXCEPTION_NO_TOOL_SESSIONS is set to true");
			voteGeneralMonitoringDTO.setUserExceptionNoToolSessions(new Boolean(true).toString());
		}

		/**getting instructions screen content from here... */
		voteGeneralMonitoringDTO.setOnlineInstructions(voteContent.getOnlineInstructions());
		voteGeneralMonitoringDTO.setOfflineInstructions(voteContent.getOfflineInstructions());
		
        List attachmentList = voteService.retrieveVoteUploadedFiles(voteContent);
        logger.debug("attachmentList: " + attachmentList);
        voteGeneralMonitoringDTO.setAttachmentList(attachmentList);
        voteGeneralMonitoringDTO.setDeletedAttachmentList(new ArrayList());
        /** ...till here **/

		
		logger.debug("end of refreshSummaryData, voteGeneralMonitoringDTO" + voteGeneralMonitoringDTO);
		request.setAttribute(VOTE_GENERAL_MONITORING_DTO, voteGeneralMonitoringDTO);

		
		/*find out if there are any reflection entries, from here*/
		boolean notebookEntriesExist=MonitoringUtil.notebookEntriesExist(voteService, voteContent);
		logger.debug("notebookEntriesExist : " + notebookEntriesExist);
		
		if (notebookEntriesExist)
		{
		    request.setAttribute(NOTEBOOK_ENTRIES_EXIST, new Boolean(true).toString());
		    
		    String userExceptionNoToolSessions=(String)voteGeneralAuthoringDTO.getUserExceptionNoToolSessions();
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
		
		MonitoringUtil.buildVoteStatsDTO(request,voteService, voteContent);

		
		logger.debug("fwd ing to newNominationBox: ");
        return (mapping.findForward("newNominationBox"));
    }


    /**
     * opens up an new screen within the current page for editing a question
     * newEditableNominationBox
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws IOException
     * @throws ServletException
     */
    public ActionForward newEditableNominationBox(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) 
    throws IOException, ServletException 
    {
        logger.debug("dispathcing newEditableNominationBox");
        VoteMonitoringForm voteAuthoringForm = (VoteMonitoringForm) form;
		
		IVoteService voteService =VoteServiceProxy.getVoteService(getServlet().getServletContext());
		logger.debug("voteService: " + voteService);
		
		String httpSessionID=voteAuthoringForm.getHttpSessionID();
		logger.debug("httpSessionID: " + httpSessionID);
		
		SessionMap sessionMap=(SessionMap)request.getSession().getAttribute(httpSessionID);
		logger.debug("sessionMap: " + sessionMap);

		
		String questionIndex=request.getParameter("questionIndex");
		logger.debug("questionIndex: " + questionIndex);
		
		voteAuthoringForm.setEditableNominationIndex(questionIndex);
		
        List listNominationContentDTO=(List)sessionMap.get(LIST_NOMINATION_CONTENT_DTO_KEY);
        logger.debug("listNominationContentDTO: " + listNominationContentDTO);

        String editableNomination="";
	    Iterator listIterator=listNominationContentDTO.iterator();
	    while (listIterator.hasNext())
	    {
	        VoteNominationContentDTO voteNominationContentDTO= (VoteNominationContentDTO)listIterator.next();
	        logger.debug("voteNominationContentDTO:" + voteNominationContentDTO);
	        logger.debug("voteNominationContentDTO question:" + voteNominationContentDTO.getNomination());
	        String question=voteNominationContentDTO.getNomination();
	        String displayOrder=voteNominationContentDTO.getDisplayOrder();
	        
	        if ((displayOrder != null) && (!displayOrder.equals("")))
    		{
	            if (displayOrder.equals(questionIndex))
	            {
	                editableNomination=voteNominationContentDTO.getNomination();
	                logger.debug("editableNomination found :" + editableNomination);
	                break;
	            }
	            
    		}
	    }
	    logger.debug("editableNomination found :" + editableNomination);

        
		String contentFolderID = WebUtil.readStrParam(request, AttributeNames.PARAM_CONTENT_FOLDER_ID);
		logger.debug("contentFolderID: " + contentFolderID);
		voteAuthoringForm.setContentFolderID(contentFolderID);


		String activeModule=request.getParameter(ACTIVE_MODULE);
		logger.debug("activeModule: " + activeModule);

		String strToolContentID=request.getParameter(AttributeNames.PARAM_TOOL_CONTENT_ID);
		logger.debug("strToolContentID: " + strToolContentID);
		
		String defaultContentIdStr=request.getParameter(DEFAULT_CONTENT_ID_STR);
		logger.debug("defaultContentIdStr: " + defaultContentIdStr);
		
		VoteContent voteContent=voteService.retrieveVote(new Long(strToolContentID));
		logger.debug("voteContent: " + voteContent);
		

		VoteGeneralAuthoringDTO voteGeneralAuthoringDTO= new VoteGeneralAuthoringDTO();
		logger.debug("voteGeneralAuthoringDTO: " + voteGeneralAuthoringDTO);
		voteGeneralAuthoringDTO.setContentFolderID(contentFolderID);

		
		String richTextTitle = request.getParameter(TITLE);
	    String richTextInstructions = request.getParameter(INSTRUCTIONS);
	
	    logger.debug("richTextTitle: " + richTextTitle);
	    logger.debug("richTextInstructions: " + richTextInstructions);
	    voteGeneralAuthoringDTO.setActivityTitle(richTextTitle);
	    voteAuthoringForm.setTitle(richTextTitle);
	    
		voteGeneralAuthoringDTO.setActivityInstructions(richTextInstructions);
		voteGeneralAuthoringDTO.setEditableNominationText(editableNomination);
	    voteGeneralAuthoringDTO.setDefineLaterInEditMode(new Boolean(true).toString());
	
		repopulateRequestParameters(request, voteAuthoringForm, voteGeneralAuthoringDTO);
		logger.debug("voteGeneralAuthoringDTO now: " + voteGeneralAuthoringDTO);
		request.setAttribute(VOTE_GENERAL_AUTHORING_DTO, voteGeneralAuthoringDTO);
		request.setAttribute(TOTAL_NOMINATION_COUNT, new Integer(listNominationContentDTO.size()));

		if (voteContent != null)
		{
		    //prepareReflectionData(request, voteContent, voteService, null,false);
		    prepareReflectionData(request, voteContent, voteService, null, false, "All");
			
	    	EditActivityDTO editActivityDTO = new EditActivityDTO();
			boolean isContentInUse=VoteUtils.isContentInUse(voteContent);
			logger.debug("isContentInUse:" + isContentInUse);
			if (isContentInUse == true)
			{
			    editActivityDTO.setMonitoredContentInUse(new Boolean(false).toString());
			}
			request.setAttribute(EDIT_ACTIVITY_DTO, editActivityDTO);		
		}

		VoteGeneralMonitoringDTO voteGeneralMonitoringDTO= new VoteGeneralMonitoringDTO();
		voteGeneralMonitoringDTO.setDefineLaterInEditMode(new Boolean(true).toString());
		
		if (voteService.studentActivityOccurredGlobal(voteContent))
		{
			logger.debug("USER_EXCEPTION_NO_TOOL_SESSIONS is set to false");
			voteGeneralMonitoringDTO.setUserExceptionNoToolSessions(new Boolean(false).toString());
		}
		else
		{
			logger.debug("USER_EXCEPTION_NO_TOOL_SESSIONS is set to true");
			voteGeneralMonitoringDTO.setUserExceptionNoToolSessions(new Boolean(true).toString());
		}

		/**getting instructions screen content from here... */
		voteGeneralMonitoringDTO.setOnlineInstructions(voteContent.getOnlineInstructions());
		voteGeneralMonitoringDTO.setOfflineInstructions(voteContent.getOfflineInstructions());
		
        List attachmentList = voteService.retrieveVoteUploadedFiles(voteContent);
        logger.debug("attachmentList: " + attachmentList);
        voteGeneralMonitoringDTO.setAttachmentList(attachmentList);
        voteGeneralMonitoringDTO.setDeletedAttachmentList(new ArrayList());
        /** ...till here **/

		
		logger.debug("end of refreshSummaryData, voteGeneralMonitoringDTO" + voteGeneralMonitoringDTO);
		request.setAttribute(VOTE_GENERAL_MONITORING_DTO, voteGeneralMonitoringDTO);

		
		/*find out if there are any reflection entries, from here*/
		boolean notebookEntriesExist=MonitoringUtil.notebookEntriesExist(voteService, voteContent);
		logger.debug("notebookEntriesExist : " + notebookEntriesExist);
		
		if (notebookEntriesExist)
		{
		    request.setAttribute(NOTEBOOK_ENTRIES_EXIST, new Boolean(true).toString());
		    
		    String userExceptionNoToolSessions=(String)voteGeneralAuthoringDTO.getUserExceptionNoToolSessions();
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
		
		MonitoringUtil.buildVoteStatsDTO(request,voteService, voteContent);
		
		logger.debug("fwd ing to editNominationBox: ");
        return (mapping.findForward("editNominationBox"));
    }

    
    
    /**
     * removes a question from the questions map
     * ActionForward removeNomination(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) 
        throws IOException, ServletException
     * 
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws IOException
     * @throws ServletException
     */
    public ActionForward removeNomination(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) 
        throws IOException, ServletException {
    	logger.debug("dispatching removeNomination");
    	VoteMonitoringForm voteAuthoringForm = (VoteMonitoringForm) form;
    	
		IVoteService voteService =VoteServiceProxy.getVoteService(getServlet().getServletContext());
		logger.debug("voteService: " + voteService);

		String httpSessionID=voteAuthoringForm.getHttpSessionID();
		logger.debug("httpSessionID: " + httpSessionID);
		
		SessionMap sessionMap=(SessionMap)request.getSession().getAttribute(httpSessionID);
		logger.debug("sessionMap: " + sessionMap);
		
		String questionIndex=request.getParameter("questionIndex");
		logger.debug("questionIndex: " + questionIndex);
		
	    
        List listNominationContentDTO=(List)sessionMap.get(LIST_NOMINATION_CONTENT_DTO_KEY);
        logger.debug("listNominationContentDTO: " + listNominationContentDTO);

	    VoteNominationContentDTO voteNominationContentDTO= null;
	    Iterator listIterator=listNominationContentDTO.iterator();
	    while (listIterator.hasNext())
	    {
	        voteNominationContentDTO= (VoteNominationContentDTO)listIterator.next();
	        logger.debug("voteNominationContentDTO:" + voteNominationContentDTO);
	        logger.debug("voteNominationContentDTO question:" + voteNominationContentDTO.getNomination());
	        
	        String question=voteNominationContentDTO.getNomination();
	        String displayOrder=voteNominationContentDTO.getDisplayOrder();
	        logger.debug("displayOrder:" + displayOrder);
	        
	        if ((displayOrder != null) && (!displayOrder.equals("")))
    		{
	            if (displayOrder.equals(questionIndex))
	            {
	                break;
	            }
	            
    		}
	    }
	    
	    logger.debug("voteNominationContentDTO found:" + voteNominationContentDTO);
	    voteNominationContentDTO.setNomination("");
	    logger.debug("listNominationContentDTO after remove:" + listNominationContentDTO);
	  
	    
	    listNominationContentDTO=AuthoringUtil.reorderListNominationContentDTO(listNominationContentDTO, questionIndex );
	    logger.debug("listNominationContentDTO reordered:" + listNominationContentDTO);
        
	    sessionMap.put(LIST_NOMINATION_CONTENT_DTO_KEY, listNominationContentDTO);
	    
		String contentFolderID = WebUtil.readStrParam(request, AttributeNames.PARAM_CONTENT_FOLDER_ID);
		logger.debug("contentFolderID: " + contentFolderID);
		voteAuthoringForm.setContentFolderID(contentFolderID);

		
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
		
		VoteContent voteContent=voteService.retrieveVote(new Long(strToolContentID));		
		logger.debug("voteContent: " + voteContent);
		
		if (voteContent == null)
		{
			logger.debug("using defaultContentIdStr: " + defaultContentIdStr);
			voteContent=voteService.retrieveVote(new Long(defaultContentIdStr));
			
		}
		logger.debug("final voteContent: " + voteContent);

		VoteGeneralAuthoringDTO voteGeneralAuthoringDTO= new VoteGeneralAuthoringDTO();
		logger.debug("voteGeneralAuthoringDTO: " + voteGeneralAuthoringDTO);
		voteGeneralAuthoringDTO.setContentFolderID(contentFolderID);
		
        voteGeneralAuthoringDTO.setActivityTitle(richTextTitle);
        voteAuthoringForm.setTitle(richTextTitle);
        
  		voteGeneralAuthoringDTO.setActivityInstructions(richTextInstructions);

		AuthoringUtil authoringUtil= new AuthoringUtil();
        
        voteGeneralAuthoringDTO.setEditActivityEditMode(new Boolean(true).toString());
        
        request.getSession().setAttribute(httpSessionID, sessionMap);

		
		voteGeneralAuthoringDTO.setToolContentID(strToolContentID);
		voteGeneralAuthoringDTO.setHttpSessionID(httpSessionID);
		voteGeneralAuthoringDTO.setActiveModule(activeModule);
		voteGeneralAuthoringDTO.setDefaultContentIdStr(defaultContentIdStr);
		voteAuthoringForm.setToolContentID(strToolContentID);
		voteAuthoringForm.setHttpSessionID(httpSessionID);
		voteAuthoringForm.setActiveModule(activeModule);
		voteAuthoringForm.setDefaultContentIdStr(defaultContentIdStr);
		voteAuthoringForm.setCurrentTab("3");

	    request.setAttribute(LIST_NOMINATION_CONTENT_DTO,listNominationContentDTO);
		logger.debug("voteNominationContentDTO now: " + voteNominationContentDTO);
	    logger.debug("listNominationContentDTO now: " + listNominationContentDTO);
	
	    voteGeneralAuthoringDTO.setDefineLaterInEditMode(new Boolean(true).toString());
	
	    repopulateRequestParameters(request, voteAuthoringForm, voteGeneralAuthoringDTO);
		logger.debug("before saving final voteGeneralAuthoringDTO: " + voteGeneralAuthoringDTO);
		request.setAttribute(VOTE_GENERAL_AUTHORING_DTO, voteGeneralAuthoringDTO);
		request.setAttribute(TOTAL_NOMINATION_COUNT, new Integer(listNominationContentDTO.size()));
		
		if (voteContent != null)
		{
		    //prepareReflectionData(request, voteContent, voteService, null,false);
		    prepareReflectionData(request, voteContent, voteService, null, false, "All");
			
	    	EditActivityDTO editActivityDTO = new EditActivityDTO();
			boolean isContentInUse=VoteUtils.isContentInUse(voteContent);
			logger.debug("isContentInUse:" + isContentInUse);
			if (isContentInUse == true)
			{
			    editActivityDTO.setMonitoredContentInUse(new Boolean(false).toString());
			}
			request.setAttribute(EDIT_ACTIVITY_DTO, editActivityDTO);		
		}

		/*find out if there are any reflection entries, from here*/
		boolean notebookEntriesExist=MonitoringUtil.notebookEntriesExist(voteService, voteContent);
		logger.debug("notebookEntriesExist : " + notebookEntriesExist);

		VoteGeneralMonitoringDTO voteGeneralMonitoringDTO= new VoteGeneralMonitoringDTO();
		voteGeneralMonitoringDTO.setDefineLaterInEditMode(new Boolean(true).toString());
		
		if (voteService.studentActivityOccurredGlobal(voteContent))
		{
			logger.debug("USER_EXCEPTION_NO_TOOL_SESSIONS is set to false");
			voteGeneralMonitoringDTO.setUserExceptionNoToolSessions(new Boolean(false).toString());
		}
		else
		{
			logger.debug("USER_EXCEPTION_NO_TOOL_SESSIONS is set to true");
			voteGeneralMonitoringDTO.setUserExceptionNoToolSessions(new Boolean(true).toString());
		}
		
		/**getting instructions screen content from here... */
		voteGeneralMonitoringDTO.setOnlineInstructions(voteContent.getOnlineInstructions());
		voteGeneralMonitoringDTO.setOfflineInstructions(voteContent.getOfflineInstructions());
		
        List attachmentList = voteService.retrieveVoteUploadedFiles(voteContent);
        logger.debug("attachmentList: " + attachmentList);
        voteGeneralMonitoringDTO.setAttachmentList(attachmentList);
        voteGeneralMonitoringDTO.setDeletedAttachmentList(new ArrayList());
        /** ...till here **/

		
		logger.debug("end of refreshSummaryData, voteGeneralMonitoringDTO" + voteGeneralMonitoringDTO);
		request.setAttribute(VOTE_GENERAL_MONITORING_DTO, voteGeneralMonitoringDTO);
		
		
		if (notebookEntriesExist)
		{
		    request.setAttribute(NOTEBOOK_ENTRIES_EXIST, new Boolean(true).toString());
		    
		    String userExceptionNoToolSessions=(String)voteGeneralAuthoringDTO.getUserExceptionNoToolSessions();
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
		
		MonitoringUtil.buildVoteStatsDTO(request,voteService, voteContent);
		
		logger.debug("fwd ing to LOAD_MONITORING: " + LOAD_MONITORING);
        return (mapping.findForward(LOAD_MONITORING));
    }
    
    
    /**
     * moves a question down in the list
     * moveNominationDown
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws IOException
     * @throws ServletException
     */
    public ActionForward moveNominationDown(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) 
    throws IOException, ServletException {
		logger.debug("dispatching moveNominationDown");
		VoteMonitoringForm voteAuthoringForm = (VoteMonitoringForm) form;
		
		IVoteService voteService =VoteServiceProxy.getVoteService(getServlet().getServletContext());
		logger.debug("voteService: " + voteService);
	
		String httpSessionID=voteAuthoringForm.getHttpSessionID();
		logger.debug("httpSessionID: " + httpSessionID);
		
		SessionMap sessionMap=(SessionMap)request.getSession().getAttribute(httpSessionID);
		logger.debug("sessionMap: " + sessionMap);
		
		String questionIndex=request.getParameter("questionIndex");
		logger.debug("questionIndex: " + questionIndex);
		
	    
	    List listNominationContentDTO=(List)sessionMap.get(LIST_NOMINATION_CONTENT_DTO_KEY);
	    logger.debug("listNominationContentDTO: " + listNominationContentDTO);
	    
	    listNominationContentDTO=AuthoringUtil.swapNodes(listNominationContentDTO, questionIndex, "down");
	    logger.debug("listNominationContentDTO after swap: " + listNominationContentDTO);
	    
	    listNominationContentDTO=AuthoringUtil.reorderSimpleListNominationContentDTO(listNominationContentDTO);
	    logger.debug("listNominationContentDTO after reordersimple: " + listNominationContentDTO);

	    
	    sessionMap.put(LIST_NOMINATION_CONTENT_DTO_KEY, listNominationContentDTO);
	    
	    
		String contentFolderID = WebUtil.readStrParam(request, AttributeNames.PARAM_CONTENT_FOLDER_ID);
		logger.debug("contentFolderID: " + contentFolderID);
		voteAuthoringForm.setContentFolderID(contentFolderID);
	
		
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
		
		VoteContent voteContent=voteService.retrieveVote(new Long(strToolContentID));		
		logger.debug("voteContent: " + voteContent);

	
		VoteGeneralAuthoringDTO voteGeneralAuthoringDTO= new VoteGeneralAuthoringDTO();
		logger.debug("voteGeneralAuthoringDTO: " + voteGeneralAuthoringDTO);
		voteGeneralAuthoringDTO.setContentFolderID(contentFolderID);
		
	    voteGeneralAuthoringDTO.setActivityTitle(richTextTitle);
	    voteAuthoringForm.setTitle(richTextTitle);
	    
		voteGeneralAuthoringDTO.setActivityInstructions(richTextInstructions);
	
		AuthoringUtil authoringUtil= new AuthoringUtil();
	    
	    voteGeneralAuthoringDTO.setEditActivityEditMode(new Boolean(true).toString());
	    
	    request.getSession().setAttribute(httpSessionID, sessionMap);
		
		voteGeneralAuthoringDTO.setToolContentID(strToolContentID);
		voteGeneralAuthoringDTO.setHttpSessionID(httpSessionID);
		voteGeneralAuthoringDTO.setActiveModule(activeModule);
		voteGeneralAuthoringDTO.setDefaultContentIdStr(defaultContentIdStr);
		voteAuthoringForm.setToolContentID(strToolContentID);
		voteAuthoringForm.setHttpSessionID(httpSessionID);
		voteAuthoringForm.setActiveModule(activeModule);
		voteAuthoringForm.setDefaultContentIdStr(defaultContentIdStr);
		voteAuthoringForm.setCurrentTab("3");
	
	    request.setAttribute(LIST_NOMINATION_CONTENT_DTO,listNominationContentDTO);
		logger.debug("listNominationContentDTO now: " + listNominationContentDTO);
	
		voteGeneralAuthoringDTO.setDefineLaterInEditMode(new Boolean(true).toString());
		
		repopulateRequestParameters(request, voteAuthoringForm, voteGeneralAuthoringDTO);
		logger.debug("before saving final voteGeneralAuthoringDTO: " + voteGeneralAuthoringDTO);
		request.setAttribute(VOTE_GENERAL_AUTHORING_DTO, voteGeneralAuthoringDTO);
		request.setAttribute(TOTAL_NOMINATION_COUNT, new Integer(listNominationContentDTO.size()));

		if (voteContent != null)
		{
		    //prepareReflectionData(request, voteContent, voteService, null,false);
		    prepareReflectionData(request, voteContent, voteService, null, false, "All");
			
	    	EditActivityDTO editActivityDTO = new EditActivityDTO();
			boolean isContentInUse=VoteUtils.isContentInUse(voteContent);
			logger.debug("isContentInUse:" + isContentInUse);
			if (isContentInUse == true)
			{
			    editActivityDTO.setMonitoredContentInUse(new Boolean(false).toString());
			}
			request.setAttribute(EDIT_ACTIVITY_DTO, editActivityDTO);		
		}

		/*find out if there are any reflection entries, from here*/
		boolean notebookEntriesExist=MonitoringUtil.notebookEntriesExist(voteService, voteContent);
		logger.debug("notebookEntriesExist : " + notebookEntriesExist);

		VoteGeneralMonitoringDTO voteGeneralMonitoringDTO= new VoteGeneralMonitoringDTO();
		voteGeneralMonitoringDTO.setDefineLaterInEditMode(new Boolean(true).toString());
		
		if (voteService.studentActivityOccurredGlobal(voteContent))
		{
			logger.debug("USER_EXCEPTION_NO_TOOL_SESSIONS is set to false");
			voteGeneralMonitoringDTO.setUserExceptionNoToolSessions(new Boolean(false).toString());
		}
		else
		{
			logger.debug("USER_EXCEPTION_NO_TOOL_SESSIONS is set to true");
			voteGeneralMonitoringDTO.setUserExceptionNoToolSessions(new Boolean(true).toString());
		}
		
		/**getting instructions screen content from here... */
		voteGeneralMonitoringDTO.setOnlineInstructions(voteContent.getOnlineInstructions());
		voteGeneralMonitoringDTO.setOfflineInstructions(voteContent.getOfflineInstructions());
		
        List attachmentList = voteService.retrieveVoteUploadedFiles(voteContent);
        logger.debug("attachmentList: " + attachmentList);
        voteGeneralMonitoringDTO.setAttachmentList(attachmentList);
        voteGeneralMonitoringDTO.setDeletedAttachmentList(new ArrayList());
        /** ...till here **/

		
		logger.debug("end of refreshSummaryData, voteGeneralMonitoringDTO" + voteGeneralMonitoringDTO);
		request.setAttribute(VOTE_GENERAL_MONITORING_DTO, voteGeneralMonitoringDTO);

		
		if (notebookEntriesExist)
		{
		    request.setAttribute(NOTEBOOK_ENTRIES_EXIST, new Boolean(true).toString());
		    
		    String userExceptionNoToolSessions=(String)voteGeneralAuthoringDTO.getUserExceptionNoToolSessions();
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
		
		MonitoringUtil.buildVoteStatsDTO(request,voteService, voteContent);
		
		logger.debug("fwd ing to LOAD_MONITORING: " + LOAD_MONITORING);
	    return (mapping.findForward(LOAD_MONITORING));
    }

    
    /**
     * moves a question up in the list
     * moveNominationUp
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws IOException
     * @throws ServletException
     */
    public ActionForward moveNominationUp(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) 
    throws IOException, ServletException {
		logger.debug("dispatching moveNominationUp");
		VoteMonitoringForm voteAuthoringForm = (VoteMonitoringForm) form;
		
		IVoteService voteService =VoteServiceProxy.getVoteService(getServlet().getServletContext());
		logger.debug("voteService: " + voteService);
	
		String httpSessionID=voteAuthoringForm.getHttpSessionID();
		logger.debug("httpSessionID: " + httpSessionID);
		
		SessionMap sessionMap=(SessionMap)request.getSession().getAttribute(httpSessionID);
		logger.debug("sessionMap: " + sessionMap);
		
		String questionIndex=request.getParameter("questionIndex");
		logger.debug("questionIndex: " + questionIndex);
		
	    
	    List listNominationContentDTO=(List)sessionMap.get(LIST_NOMINATION_CONTENT_DTO_KEY);
	    logger.debug("listNominationContentDTO: " + listNominationContentDTO);
	    
	    listNominationContentDTO=AuthoringUtil.swapNodes(listNominationContentDTO, questionIndex, "up");
	    logger.debug("listNominationContentDTO after swap: " + listNominationContentDTO);	    
	    
	    listNominationContentDTO=AuthoringUtil.reorderSimpleListNominationContentDTO(listNominationContentDTO);
	    logger.debug("listNominationContentDTO after reordersimple: " + listNominationContentDTO);
		    
	    sessionMap.put(LIST_NOMINATION_CONTENT_DTO_KEY, listNominationContentDTO);
	    
		String contentFolderID = WebUtil.readStrParam(request, AttributeNames.PARAM_CONTENT_FOLDER_ID);
		logger.debug("contentFolderID: " + contentFolderID);
		voteAuthoringForm.setContentFolderID(contentFolderID);
	
		
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
		
		VoteContent voteContent=voteService.retrieveVote(new Long(strToolContentID));
		logger.debug("voteContent: " + voteContent);
		
	
		VoteGeneralAuthoringDTO voteGeneralAuthoringDTO= new VoteGeneralAuthoringDTO();
		logger.debug("voteGeneralAuthoringDTO: " + voteGeneralAuthoringDTO);
		voteGeneralAuthoringDTO.setContentFolderID(contentFolderID);
		
	    voteGeneralAuthoringDTO.setActivityTitle(richTextTitle);
	    voteAuthoringForm.setTitle(richTextTitle);
	    
		voteGeneralAuthoringDTO.setActivityInstructions(richTextInstructions);

		AuthoringUtil authoringUtil= new AuthoringUtil();
	    
	    voteGeneralAuthoringDTO.setEditActivityEditMode(new Boolean(true).toString());
	    
	    request.getSession().setAttribute(httpSessionID, sessionMap);
	
		voteGeneralAuthoringDTO.setToolContentID(strToolContentID);
		voteGeneralAuthoringDTO.setHttpSessionID(httpSessionID);
		voteGeneralAuthoringDTO.setActiveModule(activeModule);
		voteGeneralAuthoringDTO.setDefaultContentIdStr(defaultContentIdStr);
		voteAuthoringForm.setToolContentID(strToolContentID);
		voteAuthoringForm.setHttpSessionID(httpSessionID);
		voteAuthoringForm.setActiveModule(activeModule);
		voteAuthoringForm.setDefaultContentIdStr(defaultContentIdStr);
		voteAuthoringForm.setCurrentTab("3");
	
	    request.setAttribute(LIST_NOMINATION_CONTENT_DTO,listNominationContentDTO);
		logger.debug("listNominationContentDTO now: " + listNominationContentDTO);
	
		voteGeneralAuthoringDTO.setDefineLaterInEditMode(new Boolean(true).toString());
		
		repopulateRequestParameters(request, voteAuthoringForm, voteGeneralAuthoringDTO);
		logger.debug("before saving final voteGeneralAuthoringDTO: " + voteGeneralAuthoringDTO);
		request.setAttribute(VOTE_GENERAL_AUTHORING_DTO, voteGeneralAuthoringDTO);
	
		request.setAttribute(TOTAL_NOMINATION_COUNT, new Integer(listNominationContentDTO.size()));
		

		if (voteContent != null)
		{
		    //prepareReflectionData(request, voteContent, voteService, null,false);
		    prepareReflectionData(request, voteContent, voteService, null, false, "All");
			
	    	EditActivityDTO editActivityDTO = new EditActivityDTO();
			boolean isContentInUse=VoteUtils.isContentInUse(voteContent);
			logger.debug("isContentInUse:" + isContentInUse);
			if (isContentInUse == true)
			{
			    editActivityDTO.setMonitoredContentInUse(new Boolean(false).toString());
			}
			request.setAttribute(EDIT_ACTIVITY_DTO, editActivityDTO);		
		}

		/*find out if there are any reflection entries, from here*/
		boolean notebookEntriesExist=MonitoringUtil.notebookEntriesExist(voteService, voteContent);
		logger.debug("notebookEntriesExist : " + notebookEntriesExist);
	
		VoteGeneralMonitoringDTO voteGeneralMonitoringDTO= new VoteGeneralMonitoringDTO();
		voteGeneralMonitoringDTO.setDefineLaterInEditMode(new Boolean(true).toString());
		
		if (voteService.studentActivityOccurredGlobal(voteContent))
		{
			logger.debug("USER_EXCEPTION_NO_TOOL_SESSIONS is set to false");
			voteGeneralMonitoringDTO.setUserExceptionNoToolSessions(new Boolean(false).toString());
		}
		else
		{
			logger.debug("USER_EXCEPTION_NO_TOOL_SESSIONS is set to true");
			voteGeneralMonitoringDTO.setUserExceptionNoToolSessions(new Boolean(true).toString());
		}
		
		/**getting instructions screen content from here... */
		voteGeneralMonitoringDTO.setOnlineInstructions(voteContent.getOnlineInstructions());
		voteGeneralMonitoringDTO.setOfflineInstructions(voteContent.getOfflineInstructions());
		
        List attachmentList = voteService.retrieveVoteUploadedFiles(voteContent);
        logger.debug("attachmentList: " + attachmentList);
        voteGeneralMonitoringDTO.setAttachmentList(attachmentList);
        voteGeneralMonitoringDTO.setDeletedAttachmentList(new ArrayList());
        /** ...till here **/

		
		logger.debug("end of refreshSummaryData, voteGeneralMonitoringDTO" + voteGeneralMonitoringDTO);
		request.setAttribute(VOTE_GENERAL_MONITORING_DTO, voteGeneralMonitoringDTO);

		
		if (notebookEntriesExist)
		{
		    request.setAttribute(NOTEBOOK_ENTRIES_EXIST, new Boolean(true).toString());
		    
		    String userExceptionNoToolSessions=(String)voteGeneralAuthoringDTO.getUserExceptionNoToolSessions();
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
		
		MonitoringUtil.buildVoteStatsDTO(request,voteService, voteContent);
		
		logger.debug("fwd ing to LOAD_MONITORING: " + LOAD_MONITORING);
	    return (mapping.findForward(LOAD_MONITORING));
    }
    
    
    
    public void prepareReflectionData(HttpServletRequest request, VoteContent voteContent, 
	        IVoteService voteService, String userID, boolean exportMode, String currentSessionId)
	{
	    logger.debug("starting prepareReflectionData: " + voteContent);
	    logger.debug("currentSessionId: " + currentSessionId);
	    logger.debug("userID: " + userID);
	    logger.debug("exportMode: " + exportMode);


	    List reflectionsContainerDTO= new LinkedList();
	    
	    reflectionsContainerDTO=getReflectionList(voteContent, userID, voteService);	    
	    
	   logger.debug("reflectionsContainerDTO: " + reflectionsContainerDTO);
	   request.setAttribute(REFLECTIONS_CONTAINER_DTO, reflectionsContainerDTO);
	   
	   if (exportMode)
	   {
	       request.getSession().setAttribute(REFLECTIONS_CONTAINER_DTO, reflectionsContainerDTO);
	   }
	}
    

    
    /**
     * returns reflection data for all sessions
     * 
     * getReflectionList
     * @param voteContent
     * @param userID
     * @param voteService
     * @return
     */
    public List getReflectionList(VoteContent voteContent, String userID, IVoteService voteService)
    {
        logger.debug("getting reflections for all sessions");
        List reflectionsContainerDTO= new LinkedList();
        if (userID == null)
        {
            logger.debug("all users mode");
    	    for (Iterator sessionIter = voteContent.getVoteSessions().iterator(); sessionIter.hasNext();) 
    	    {
    	       VoteSession voteSession = (VoteSession) sessionIter.next();
    	       logger.debug("voteSession: " + voteSession);
    	       logger.debug("voteSession sessionId: " + voteSession.getVoteSessionId());
    	       
    	       for (Iterator userIter = voteSession.getVoteQueUsers().iterator(); userIter.hasNext();) 
    	       {
    				VoteQueUsr user = (VoteQueUsr) userIter.next();
    				logger.debug("user: " + user);

    				NotebookEntry notebookEntry = voteService.getEntry(voteSession.getVoteSessionId(),
    						CoreNotebookConstants.NOTEBOOK_TOOL,
    						MY_SIGNATURE, new Integer(user.getQueUsrId().toString()));
    				
    				logger.debug("notebookEntry: " + notebookEntry);
    				
    				if (notebookEntry != null) {
    				    ReflectionDTO reflectionDTO = new ReflectionDTO();
    				    reflectionDTO.setUserId(user.getQueUsrId().toString());
    				    reflectionDTO.setSessionId(voteSession.getVoteSessionId().toString());
    				    reflectionDTO.setUserName(user.getFullname());
    				    reflectionDTO.setReflectionUid (notebookEntry.getUid().toString());
    				    String notebookEntryPresentable=VoteUtils.replaceNewLines(notebookEntry.getEntry());
    				    reflectionDTO.setEntry(notebookEntryPresentable);
    				    reflectionsContainerDTO.add(reflectionDTO);
    				} 
    	       }
    	   }
       }
       else
       {
           logger.debug("single user mode");
    	    for (Iterator sessionIter = voteContent.getVoteSessions().iterator(); sessionIter.hasNext();) 
    	    {
    	       VoteSession voteSession = (VoteSession) sessionIter.next();
    	       logger.debug("voteSession: " + voteSession);
    	       for (Iterator userIter = voteSession.getVoteQueUsers().iterator(); userIter.hasNext();) 
    	       {
    				VoteQueUsr user = (VoteQueUsr) userIter.next();
    				logger.debug("user: " + user);
    				
    				if (user.getQueUsrId().toString().equals(userID))
    				{
    				    logger.debug("getting reflection for user with  userID: " + userID);
    					NotebookEntry notebookEntry = voteService.getEntry(voteSession.getVoteSessionId(),
    							CoreNotebookConstants.NOTEBOOK_TOOL,
    							MY_SIGNATURE, new Integer(user.getQueUsrId().toString()));
    					
    					logger.debug("notebookEntry: " + notebookEntry);
    					
    					if (notebookEntry != null) {
    					    ReflectionDTO reflectionDTO = new ReflectionDTO();
    					    reflectionDTO.setUserId(user.getQueUsrId().toString());
    					    reflectionDTO.setSessionId(voteSession.getVoteSessionId().toString());
    					    reflectionDTO.setUserName(user.getFullname());
    					    reflectionDTO.setReflectionUid (notebookEntry.getUid().toString());
    					    String notebookEntryPresentable=VoteUtils.replaceNewLines(notebookEntry.getEntry());
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
     * returns reflection data for a specific session
     * 
     * getReflectionListForSession(VoteContent voteContent, String userID, IVoteService voteService, String currentSessionId)
     * @param voteContent
     * @param userID
     * @param voteService
     * @param currentSessionId
     * @return
     */
    public List getReflectionListForSession(VoteContent voteContent, String userID, IVoteService voteService, String currentSessionId)
    {
        logger.debug("getting reflections for a specific session");
        logger.debug("currentSessionId: " + currentSessionId);
        
        List reflectionsContainerDTO= new LinkedList();
        if (userID == null)
        {
            logger.debug("all users mode");
    	    for (Iterator sessionIter = voteContent.getVoteSessions().iterator(); sessionIter.hasNext();) 
    	    {
    	       VoteSession voteSession = (VoteSession) sessionIter.next();
    	       logger.debug("voteSession: " + voteSession);
    	       logger.debug("voteSession sessionId: " + voteSession.getVoteSessionId());
    	       
    	       if (currentSessionId.equals(voteSession.getVoteSessionId()))
    	       {
    	           
        	       for (Iterator userIter = voteSession.getVoteQueUsers().iterator(); userIter.hasNext();) 
        	       {
        				VoteQueUsr user = (VoteQueUsr) userIter.next();
        				logger.debug("user: " + user);

        				NotebookEntry notebookEntry = voteService.getEntry(voteSession.getVoteSessionId(),
        						CoreNotebookConstants.NOTEBOOK_TOOL,
        						MY_SIGNATURE, new Integer(user.getQueUsrId().toString()));
        				
        				logger.debug("notebookEntry: " + notebookEntry);
        				
        				if (notebookEntry != null) {
        				    ReflectionDTO reflectionDTO = new ReflectionDTO();
        				    reflectionDTO.setUserId(user.getQueUsrId().toString());
        				    reflectionDTO.setSessionId(voteSession.getVoteSessionId().toString());
        				    reflectionDTO.setUserName(user.getFullname());
        				    reflectionDTO.setReflectionUid (notebookEntry.getUid().toString());
        				    String notebookEntryPresentable=VoteUtils.replaceNewLines(notebookEntry.getEntry());
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
    	    for (Iterator sessionIter = voteContent.getVoteSessions().iterator(); sessionIter.hasNext();) 
    	    {
    	       VoteSession voteSession = (VoteSession) sessionIter.next();
    	       logger.debug("voteSession: " + voteSession);
    	       
    	       if (currentSessionId.equals(voteSession.getVoteSessionId()))
    	       {
        	       for (Iterator userIter = voteSession.getVoteQueUsers().iterator(); userIter.hasNext();) 
        	       {
        				VoteQueUsr user = (VoteQueUsr) userIter.next();
        				logger.debug("user: " + user);
        				
        				if (user.getQueUsrId().toString().equals(userID))
        				{
        				    logger.debug("getting reflection for user with  userID: " + userID);
        					NotebookEntry notebookEntry = voteService.getEntry(voteSession.getVoteSessionId(),
        							CoreNotebookConstants.NOTEBOOK_TOOL,
        							MY_SIGNATURE, new Integer(user.getQueUsrId().toString()));
        					
        					logger.debug("notebookEntry: " + notebookEntry);
        					
        					if (notebookEntry != null) {
        					    ReflectionDTO reflectionDTO = new ReflectionDTO();
        					    reflectionDTO.setUserId(user.getQueUsrId().toString());
        					    reflectionDTO.setSessionId(voteSession.getVoteSessionId().toString());
        					    reflectionDTO.setUserName(user.getFullname());
        					    reflectionDTO.setReflectionUid (notebookEntry.getUid().toString());
        					    String notebookEntryPresentable=VoteUtils.replaceNewLines(notebookEntry.getEntry());
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
}
    
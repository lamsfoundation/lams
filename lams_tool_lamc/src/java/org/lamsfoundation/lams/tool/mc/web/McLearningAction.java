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
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307
 * USA
 * 
 * http://www.gnu.org/licenses/gpl.txt
 * ***********************************************************************/
/* $$Id$$ */
package org.lamsfoundation.lams.tool.mc.web;

import java.io.IOException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts.Globals;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.lamsfoundation.lams.tool.exception.DataMissingException;
import org.lamsfoundation.lams.tool.exception.ToolException;
import org.lamsfoundation.lams.tool.mc.McAppConstants;
import org.lamsfoundation.lams.tool.mc.McApplicationException;
import org.lamsfoundation.lams.tool.mc.McComparator;
import org.lamsfoundation.lams.tool.mc.McGeneralLearnerFlowDTO;
import org.lamsfoundation.lams.tool.mc.McLearnerAnswersDTO;
import org.lamsfoundation.lams.tool.mc.McStringComparator;
import org.lamsfoundation.lams.tool.mc.McTempDataHolderDTO;
import org.lamsfoundation.lams.tool.mc.McUtils;
import org.lamsfoundation.lams.tool.mc.pojos.McContent;
import org.lamsfoundation.lams.tool.mc.pojos.McOptsContent;
import org.lamsfoundation.lams.tool.mc.pojos.McQueContent;
import org.lamsfoundation.lams.tool.mc.pojos.McQueUsr;
import org.lamsfoundation.lams.tool.mc.pojos.McSession;
import org.lamsfoundation.lams.tool.mc.pojos.McUsrAttempt;
import org.lamsfoundation.lams.tool.mc.service.IMcService;
import org.lamsfoundation.lams.tool.mc.service.McServiceProxy;
import org.lamsfoundation.lams.usermanagement.dto.UserDTO;
import org.lamsfoundation.lams.web.action.LamsDispatchAction;
import org.lamsfoundation.lams.web.session.SessionManager;
import org.lamsfoundation.lams.web.util.AttributeNames;

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
   <!--Learning Main Action: interacts with the Learning module user -->
	<action	path="/learning"
		    type="org.lamsfoundation.lams.tool.mc.web.McLearningAction"
			name="McLearningForm"
      		scope="session"
      		input=".answers"
      		parameter="method"
      		unknown="false"
      		validate="true">
	
		<exception
	        key="error.exception.McApplication"
	        type="org.lamsfoundation.lams.tool.mc.McApplicationException"
	        handler="org.lamsfoundation.lams.tool.mc.web.CustomStrutsExceptionHandler"
	        path=".mcErrorBox"
	        scope="request"
	      />

		<exception
	        key="error.exception.McApplication"
	        type="java.lang.NullPointerException"
	        handler="org.lamsfoundation.lams.tool.mc.web.CustomStrutsExceptionHandler"
	        path=".mcErrorBox"
	        scope="request"
	      />	         			      		

	  	<forward
		    name="loadLearner"
		    path=".answers"
		    redirect="true"
	  	/>
	  	
	  	<forward
		    name="individualReport"
		    path=".individualReport"
		    redirect="true"
	  	/>
	  	
	  	<forward
		    name="redoQuestions"
		    path=".redoQuestions"
		    redirect="true"
	  	/>

	  	<forward
		    name="viewAnswers"
		    path=".viewAnswers"
		    redirect="true"
	  	/>

	  	<forward
		    name="resultsSummary"
		    path=".resultsSummary"
		    redirect="true"
	  	/>

	  	<forward
		    name="errorList"
		    path=".mcErrorBox"
		    redirect="true"
	  	/>

	  	<forward
	        name="starter"
	        path=".starter"
	        redirect="true"
	     />

	  	<forward
		    name="learningStarter"
		    path=".learningStarter"
		    redirect="true"
	  	/>

	     <forward
	        name="preview"
	        path=".preview"
	        redirect="true"
	     />
    </action>

 * 
*/
public class McLearningAction extends LamsDispatchAction implements McAppConstants
{
	static Logger logger = Logger.getLogger(McLearningAction.class.getName());
	
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
	 * if the passed toolContentId exists in the db, we need to get the relevant data into the Map 
	 * if not, create the default Map 
	*/
    public ActionForward unspecified(ActionMapping mapping,
                               ActionForm form,
                               HttpServletRequest request,
                               HttpServletResponse response) throws IOException,
                                                            ServletException
    {
    	McUtils.cleanUpUserExceptions(request);
	 	McAuthoringForm mcAuthoringForm = (McAuthoringForm) form;
	 	AuthoringUtil.readData(request, mcAuthoringForm);	 	
	 	mcAuthoringForm.resetUserAction();
	 	return null;
    }

    
    /**
     *  responds to learner activity in learner mode.
     * 
     * ActionForward displayMc(ActionMapping mapping,
            ActionForm form,
            HttpServletRequest request,
            HttpServletResponse response) throws IOException,
                                      ServletException
     * 
     * 
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws IOException
     * @throws ServletException


     * 
     * ActionForward displayMc(ActionMapping mapping,
            ActionForm form,
            HttpServletRequest request,
            HttpServletResponse response) throws IOException,
                                      ServletException
     * 
     * 
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws IOException
     * @throws ServletException
     */
    public ActionForward displayMc(ActionMapping mapping,
            ActionForm form,
            HttpServletRequest request,
            HttpServletResponse response) throws IOException,
                                      ServletException
   {
    	McUtils.cleanUpUserExceptions(request);
    	McLearningForm mcLearningForm = (McLearningForm) form;
	 	IMcService mcService = McServiceProxy.getMcService(getServlet().getServletContext());
	 	
    	mcLearningForm.resetParameters();
    	String toolContentId=mcLearningForm.getToolContentId();
    	logger.debug("toolContentId: " + toolContentId);
    	
    	if (mcLearningForm.getContinueOptionsCombined() != null)
    	{
    		setContentInUse(request, toolContentId, mcService);
    		return continueOptionsCombined(mapping, form, request, response);
    	}
    	else if (mcLearningForm.getNextOptions() != null)
	 	{
    	    setContentInUse(request, toolContentId, mcService);
	 		return getNextOptions(mapping, form, request, response);
	 	}
    	else if (mcLearningForm.getRedoQuestions() != null)
    	{
    	    setContentInUse(request, toolContentId, mcService);
    		return redoQuestions(mapping, form, request, response);
    	}
    	else if (mcLearningForm.getRedoQuestionsOk() != null)
    	{
    	    setContentInUse(request, toolContentId, mcService);
    		logger.debug("requested redoQuestionsOk, user is sure to redo the questions.");
    		mcLearningForm.resetCommands();
    		return redoQuestions(request, mcLearningForm, mapping);
    	}
    	else if (mcLearningForm.getViewAnswers() != null)
    	{
    	    setContentInUse(request, toolContentId, mcService);
    		return viewAnswers(mapping, form, request, response);
    	}
    	else if (mcLearningForm.getViewSummary() != null)
    	{
    	    setContentInUse(request, toolContentId, mcService);
			return viewSummary(mapping, form, request, response);
    	}
    	else if (mcLearningForm.getLearnerFinished() != null)
    	{
    		logger.debug("requested learner finished, the learner should be directed to next activity.");
    		
    		String toolSessionId=mcLearningForm.getToolSessionId();
        	logger.debug("toolSessionId: " + toolSessionId);
        	
        	McSession mcSession=mcService.retrieveMcSession(new Long(toolSessionId));
            logger.debug("retrieving mcSession: " + mcSession);
    		
    	    String userID = "";
    	    HttpSession ss = SessionManager.getSession();
    	    logger.debug("ss: " + ss);
    	    
    	    if (ss != null)
    	    {
    		    UserDTO user = (UserDTO) ss.getAttribute(AttributeNames.USER);
    		    if ((user != null) && (user.getUserID() != null))
    		    {
    		    	userID = user.getUserID().toString();
    			    logger.debug("retrieved userId: " + userID);
    		    }
    	    }
    		
    		logger.debug("attempting to leave/complete session with toolSessionId:" + toolSessionId + " and userID:"+userID);
    		
    		McUtils.cleanUpSessionAbsolute(request);
    		
    		String nextUrl=null;
    		try
    		{
    			nextUrl=mcService.leaveToolSession(new Long(toolSessionId), new Long(userID));
    			logger.debug("nextUrl: "+ nextUrl);
    		}
    		catch (DataMissingException e)
    		{
    			logger.debug("failure getting nextUrl: "+ e);
        		mcLearningForm.resetCommands();
    			//throw new ServletException(e);
        		return (mapping.findForward(LEARNING_STARTER));
    		}
    		catch (ToolException e)
    		{
    			logger.debug("failure getting nextUrl: "+ e);
        		mcLearningForm.resetCommands();
    			//throw new ServletException(e);
        		return (mapping.findForward(LEARNING_STARTER));        		
    		}
    		catch (Exception e)
    		{
    			logger.debug("unknown exception getting nextUrl: "+ e);
        		mcLearningForm.resetCommands();
    			//throw new ServletException(e);
        		return (mapping.findForward(LEARNING_STARTER));        		
    		}

    		logger.debug("success getting nextUrl: "+ nextUrl);
    		mcLearningForm.resetCommands();
    		
    	    McQueUsr mcQueUsr=mcService.getMcUserBySession(new Long(userID), mcSession.getUid());
    	    logger.debug("mcQueUsr:" + mcQueUsr);
    	    
    	    McUsrAttempt mcUsrAttempt = mcService.getAttemptWithLastAttemptOrderForUserInSession(mcQueUsr.getUid(), mcSession.getUid());
        	logger.debug("mcUsrAttempt with highest attempt order: " + mcUsrAttempt);
        	String highestAttemptOrder="";

        	if (mcUsrAttempt != null)
        	{
            	highestAttemptOrder=mcUsrAttempt.getAttemptOrder().toString();
            	logger.debug("highestAttemptOrder: " + highestAttemptOrder);
        	    List userAttempts=mcService.getAttemptsForUserOnHighestAttemptOrderInSession(mcQueUsr.getUid(), mcSession.getUid(), new Integer(highestAttemptOrder));
        	    logger.debug("userAttempts:" + userAttempts);
        	    
    			Iterator itAttempts=userAttempts.iterator();
    			while (itAttempts.hasNext())
    			{
    	    		mcUsrAttempt=(McUsrAttempt)itAttempts.next();
    	    		logger.debug("mcUsrAttempt: " + mcUsrAttempt);
    	    		mcUsrAttempt.setFinished(true);
    	    		mcService.updateMcUsrAttempt(mcUsrAttempt);
    			}
    			logger.debug("updated user records to finished");
        	}

    	    
    		
    		/* pay attention here*/
    		logger.debug("redirecting to the nextUrl: "+ nextUrl);
    		response.sendRedirect(nextUrl);
    		
    		return null;
    	}
    	
    	mcLearningForm.resetCommands();	
 		return (mapping.findForward(LOAD_LEARNER));
   }
    
    
    protected Set parseLearnerInput(List learnerInput, McContent mcContent, IMcService mcService)
    {
        logger.debug("learnerInput: " + learnerInput);
        logger.debug("mcContent: " + mcContent);
        logger.debug("mcContent uid: " + mcContent.getUid());
        
        Set questionUids= new HashSet();

        Iterator listLearnerInputIterator=learnerInput.iterator();
    	while (listLearnerInputIterator.hasNext())
    	{
    		String input=(String)listLearnerInputIterator.next();
    		logger.debug("input: " + input);
    		int pos=input.indexOf("-");
    		logger.debug("pos: " + pos);
    		String questionUid=input.substring(0,pos);
    		logger.debug("questionUid: " + questionUid);
    		questionUids.add(questionUid);
    	}
    	logger.debug("final set questionUid: " + questionUids);
    	
    	
    	List questionEntriesOrdered=mcService.getAllQuestionEntries(mcContent.getUid());
    	logger.debug("questionEntriesOrdered: " + questionEntriesOrdered);
    	
    	Set questionOrderedUids= new TreeSet();
    	Iterator questionEntriesOrderedIterator=questionEntriesOrdered.iterator();
    	while (questionEntriesOrderedIterator.hasNext())
    	{
    	    McQueContent mcQueContent= (McQueContent)questionEntriesOrderedIterator.next();
    	    logger.debug("mcQueContent: " + mcQueContent);
    	    logger.debug("mcQueContent text: " + mcQueContent.getQuestion());
    	    
    	    
    	    Iterator questionUidsIterator=questionUids.iterator();
    	    while (questionUidsIterator.hasNext())
    	    {
    	        String questionUid= (String)questionUidsIterator.next();
    	        logger.debug("questionUid: " + questionUid);
    	        
    	        logger.debug("questionUid versus objects uid : " + questionUid + " versus " +  mcQueContent.getUid());
    	        if (questionUid.equals(mcQueContent.getUid().toString()))
    	        {
    	            questionOrderedUids.add(questionUid);
    	        }
    	    }
    	}
    	logger.debug("questionOrderedUids: " + questionOrderedUids);
    	return questionOrderedUids;
    }

    
    
    protected List buildSelectedQuestionAndCandidateAnswersDTO(List learnerInput, McTempDataHolderDTO mcTempDataHolderDTO, 
            IMcService mcService, McContent mcContent)
    {
        logger.debug("mcService: " + mcService);
        logger.debug("mcContent: " + mcContent);
        
        logger.debug("learnerInput: " + learnerInput);
        int mark=0;
        int userWeight=0;
        
        Set questionUids=parseLearnerInput(learnerInput, mcContent, mcService);
        logger.debug("set questionUids: " + questionUids);
        
        List questionAndCandidateAnswersList= new LinkedList();
        
        Iterator setQuestionUidsIterator=questionUids.iterator();
        while (setQuestionUidsIterator.hasNext())
    	{
            McLearnerAnswersDTO mcLearnerAnswersDTO= new McLearnerAnswersDTO();
            String questionUid=(String)setQuestionUidsIterator.next();
            logger.debug("questionUid: " + questionUid);

            McQueContent mcQueContent=mcService.findMcQuestionContentByUid(new Long(questionUid));
    		logger.debug("mcQueContent: " + mcQueContent);
    		
    		logger.debug("mcQueContent text: " + mcQueContent.getQuestion());
    		
    		mcLearnerAnswersDTO.setQuestion(mcQueContent.getQuestion());
    		mcLearnerAnswersDTO.setDisplayOrder(mcQueContent.getDisplayOrder().toString());
    		mcLearnerAnswersDTO.setWeight(mcQueContent.getWeight().toString());
    		mcLearnerAnswersDTO.setQuestionUid(mcQueContent.getUid().toString());
    		
    		Map caMap= new TreeMap(new McStringComparator());
    		Map caIdsMap= new TreeMap(new McStringComparator());
    		Long mapIndex=new Long(1);
    		
            Iterator listLearnerInputIterator=learnerInput.iterator();
        	while (listLearnerInputIterator.hasNext())
        	{
        		String input=(String)listLearnerInputIterator.next();
        		logger.debug("input: " + input);
        		int pos=input.indexOf("-");
        		logger.debug("pos: " + pos);
        		String localQuestionUid=input.substring(0,pos);
        		logger.debug("localQuestionUid: " + localQuestionUid);
        		
        		if (questionUid.equals(localQuestionUid))
        		{
        		    logger.debug("equal uids found : " + localQuestionUid);
            		String caUid=input.substring(pos+1);
            		logger.debug("caUid: " + caUid);
            		McOptsContent mcOptsContent= mcService.findMcOptionsContentByUid(new Long(caUid));
            		logger.debug("mcOptsContent: " + mcOptsContent);
            		logger.debug("mcOptsContent text: " + mcOptsContent.getMcQueOptionText());
            		caMap.put(mapIndex.toString(), mcOptsContent.getMcQueOptionText());
            		caIdsMap.put(mapIndex.toString(), mcOptsContent.getUid().toString() );
            		mapIndex=new Long(mapIndex.longValue()+1);
        		}
        	}
        	logger.debug("current caMap: " + caMap);
        	logger.debug("current caIdsMap: " + caIdsMap);
        	mcLearnerAnswersDTO.setCandidateAnswers(caMap);
        	
        	Long mcQueContentUid= mcQueContent.getUid(); 
        	logger.debug("mcQueContentUid: " + mcQueContentUid);
        	
            List correctOptions=(List) mcService.getPersistedSelectedOptions(mcQueContentUid);
            logger.debug("correctOptions: " +  correctOptions);
            Map mapCorrectOptionUids=LearningUtil.buildMapCorrectOptionUids(correctOptions);
            logger.debug("mapCorrectOptionUids: " +  mapCorrectOptionUids);
        	
            boolean isEqual=LearningUtil.compareMapItems(mapCorrectOptionUids, caIdsMap);
            logger.debug("isEqual: " +  isEqual);
            boolean isEqualCross=LearningUtil.compareMapsItemsCross(mapCorrectOptionUids, caIdsMap);
            logger.debug("isEqualCross: " +  isEqualCross);
            boolean compareResult= isEqual && isEqualCross; 
            logger.debug("compareResult: " +  compareResult);

            mcLearnerAnswersDTO.setAttemptCorrect(new Boolean(compareResult).toString());
            if (compareResult)
            {
            	mcLearnerAnswersDTO.setFeedbackCorrect(mcQueContent.getFeedbackCorrect());
            	++mark;
            	int weight=mcQueContent.getWeight().intValue();
            	logger.debug("weight: " +  weight);
            	userWeight=userWeight + weight;
            }
            else
            {
                mcLearnerAnswersDTO.setFeedbackIncorrect(mcQueContent.getFeedbackIncorrect());
            }
        	logger.debug("assesment complete");
        	logger.debug("mark:: " + mark);
        	
        	questionAndCandidateAnswersList.add(mcLearnerAnswersDTO);
    	}
        logger.debug("final questionAndCandidateAnswersList: " + questionAndCandidateAnswersList);
        logger.debug("final mark: " + mark);
        logger.debug("final userWeight: " + userWeight);
        
        mcTempDataHolderDTO.setLearnerMark(new Integer(mark).toString());
        mcTempDataHolderDTO.setTotalUserWeight(new Integer(userWeight).toString());
        
        return questionAndCandidateAnswersList;
    }
    

    /**
     * responses to learner when they answer all the questions on a single page
     * continueOptionsCombined(ActionMapping mapping,
            ActionForm form,
            HttpServletRequest request,
            HttpServletResponse response)
     * 
     * @param request
     * @param form
     * @param mapping
     * @return ActionForward
     */
    public ActionForward continueOptionsCombined(ActionMapping mapping,
            ActionForm form,
            HttpServletRequest request,
            HttpServletResponse response) throws IOException,
                                         ServletException
	{   
    	McUtils.cleanUpUserExceptions(request);
		logger.debug("dispatching continueOptionsCombined...");
		McLearningForm mcLearningForm = (McLearningForm) form;
	 	IMcService mcService = McServiceProxy.getMcService(getServlet().getServletContext());
	    logger.debug("retrieving mcService from proxy: " + mcService);
	 	
	 	String[] checkedCa=mcLearningForm.getCheckedCa();
	 	mcLearningForm.resetCa(mapping, request);

	 	
	 	logger.debug("checkedCa: " + checkedCa);
	 	logger.debug("checkedCa length: " + checkedCa.length );
	 	
	 	List learnerInput= new LinkedList();
	 	for (int i=0; i < checkedCa.length ; i++)
	 	{
	 	  String currentCa=checkedCa[i];
	 	  logger.debug("currentCa: " + currentCa);
	 	  learnerInput.add(currentCa);
	 	}
	 	logger.debug("learnerInput: " + learnerInput);

	 	String toolContentId=mcLearningForm.getToolContentId();
	 	logger.debug("toolContentId: " + toolContentId);

	 	/* process the answers */
    	McContent mcContent=mcService.retrieveMc(new Long(toolContentId));
    	logger.debug("mcContent: " + mcContent);

	 	McTempDataHolderDTO mcTempDataHolderDTO= new McTempDataHolderDTO();

	 	List selectedQuestionAndCandidateAnswersDTO=buildSelectedQuestionAndCandidateAnswersDTO(learnerInput,mcTempDataHolderDTO 
	 	        , mcService, mcContent);
	 	logger.debug("selectedQuestionAndCandidateAnswersDTO: " + selectedQuestionAndCandidateAnswersDTO);
	 	request.setAttribute(LIST_SELECTED_QUESTION_CANDIDATEANSWERS_DTO, selectedQuestionAndCandidateAnswersDTO);
		logger.debug("LIST_SELECTED_QUESTION_CANDIDATEANSWERS_DTO: " +  request.getAttribute(LIST_SELECTED_QUESTION_CANDIDATEANSWERS_DTO));
		
		logger.debug("mcTempDataHolderDTO becomes: " + mcTempDataHolderDTO);
		String learnerMark=mcTempDataHolderDTO.getLearnerMark(); 
		logger.debug("learnerMark: " + learnerMark);
		
		String totalUserWeight=mcTempDataHolderDTO.getTotalUserWeight();
		logger.debug("totalUserWeight: " + totalUserWeight);
		
		
		
    	
    	McGeneralLearnerFlowDTO mcGeneralLearnerFlowDTO=LearningUtil.buildMcGeneralLearnerFlowDTO(mcContent);
    	logger.debug("constructed a new mcGeneralLearnerFlowDTO");
    	
    	int totalQuestionCount=mcContent.getMcQueContents().size();
    	logger.debug("totalQuestionCount: " + totalQuestionCount);
    	mcGeneralLearnerFlowDTO.setTotalQuestionCount(new Integer(totalQuestionCount).toString());
    	
		mcGeneralLearnerFlowDTO.setLearnerMark(learnerMark.toString());
    	
		Integer passMark=mcContent.getPassMark();
		logger.debug("passMark: " + passMark);
    	mcGeneralLearnerFlowDTO.setUserOverPassMark(new Boolean(false).toString());
    	
    	mcGeneralLearnerFlowDTO.setPassMarkApplicable(new Boolean(false).toString());
    	boolean passed=false;
    	if ((passMark != null)) 
		{
    	    mcGeneralLearnerFlowDTO.setPassMarkApplicable(new Boolean(true).toString());

    	    logger.debug("totalUserWeight versus passMark: " + totalUserWeight + " versus " + passMark);
    	    if (new Integer(totalUserWeight).intValue()  < passMark.intValue())
    		{
    			logger.debug("USER FAILED");
    			logger.debug("totalUserWeight is less than passmark: " + totalUserWeight + " < " + passMark.intValue());
    			passed=false;
    		}
    		else
    		{
    			logger.debug("USER PASSED");
    			passed=true;
    			mcGeneralLearnerFlowDTO.setUserOverPassMark(new Boolean(true).toString());
    		}
		}
    	

    	if (passMark == null)
    	{
    		mcGeneralLearnerFlowDTO.setPassMarkApplicable(new Boolean(false).toString());
    	}
    	
    	/*
    	if (!mcContent.isRetries())
    	{
    		logger.debug("content is not isRetries. set passed to true");
    		passed=true;
    	}
    	*/
    	
    	String toolSessionId=mcLearningForm.getToolSessionId();
    	logger.debug("toolSessionId: " + toolSessionId);
    	
    	
    	McSession mcSession=mcService.retrieveMcSession(new Long(toolSessionId));
        logger.debug("retrieving mcSession: " + mcSession);
        
        Long toolSessionUid=mcSession.getUid();
        logger.debug("toolSessionUid: " + toolSessionUid);

    	
    	boolean isUserDefined=false;
    	
	    String userID = "";
	    HttpSession ss = SessionManager.getSession();
	    logger.debug("ss: " + ss);
	    
	    if (ss != null)
	    {
		    UserDTO user = (UserDTO) ss.getAttribute(AttributeNames.USER);
		    if ((user != null) && (user.getUserID() != null))
		    {
		    	userID = user.getUserID().toString();
			    logger.debug("retrieved userId: " + userID);
		    }
	    }
    	
    	
    	logger.debug("userID: " + userID);
    	McQueUsr existingMcQueUsr=mcService.getMcUserBySession(new Long(userID), mcSession.getUid());
    	logger.debug("existingMcQueUsr: " + existingMcQueUsr);
    	
    	if (existingMcQueUsr != null)
    	    isUserDefined=true;
    	
    	logger.debug("isUserDefined: " + isUserDefined);

    	McQueUsr mcQueUsr=null;
    	if (isUserDefined == false)
    	{
    	    mcQueUsr=LearningUtil.createUser(request, mcService, new Long(toolSessionId));
    		logger.debug("created user in the db: " + mcQueUsr);
    	}
    	else
    	{
    	    mcQueUsr=existingMcQueUsr;
    	    logger.debug("assign");
    	}
    	
    	logger.debug("final mcQueUsr: " + mcQueUsr);
    	
   	
    	String highestAttemptOrder="0";
    	McUsrAttempt mcUsrAttempt = mcService.getAttemptWithLastAttemptOrderForUserInSession(mcQueUsr.getUid(), toolSessionUid);
    	logger.debug("mcUsrAttempt with highest attempt order: " + mcUsrAttempt);
    	if (mcUsrAttempt != null)
    	{
        	highestAttemptOrder=mcUsrAttempt.getAttemptOrder().toString();
    	}
    	logger.debug("highestAttemptOrder: " + highestAttemptOrder);
    	
    	int intHighestAttemptOrder=0;
    	intHighestAttemptOrder=new Integer(highestAttemptOrder).intValue();
    	logger.debug("intHighestAttemptOrder: " + intHighestAttemptOrder);
    	logger.debug("new intHighestAttemptOrder: " + ++intHighestAttemptOrder);
    	
    	highestAttemptOrder = new Integer(intHighestAttemptOrder).toString();
    	logger.debug("new highestAttemptOrder: " + highestAttemptOrder);
    	
        
    	LearningUtil.createLearnerAttempt(request, mcQueUsr, selectedQuestionAndCandidateAnswersDTO, new Integer(learnerMark).intValue(), passed, new Integer(highestAttemptOrder).intValue(), null, mcService);
    	logger.debug("created user attempt in the db");
    		
        Map mapQuestionWeights=LearningUtil.buildWeightsMap(request, mcContent.getMcContentId(), mcService);
        logger.debug("mapQuestionWeights:" + mapQuestionWeights);
        
        int learnerMarkAtLeast=LearningUtil.getLearnerMarkAtLeast(passMark,mapQuestionWeights);
        logger.debug("learnerMarkAtLeast:" + learnerMarkAtLeast);
        mcGeneralLearnerFlowDTO.setLearnerMarkAtLeast (new Integer(learnerMarkAtLeast).toString());
		
        logger.debug("user over passmark:" + mcLearningForm.getUserOverPassMark());
        logger.debug("is passmark applicable:" + mcLearningForm.getPassMarkApplicable());
		mcLearningForm.resetCommands();
		
		request.setAttribute(MC_GENERAL_LEARNER_FLOW_DTO, mcGeneralLearnerFlowDTO);
		logger.debug("MC_GENERAL_LEARNER_FLOW_DTO: " +  request.getAttribute(MC_GENERAL_LEARNER_FLOW_DTO));
		
		return (mapping.findForward(INDIVIDUAL_REPORT));
    }
    

    /**
     * takes the learner to the next set of questions
     * continueOptionsCombined(ActionMapping mapping,
            ActionForm form,
            HttpServletRequest request,
            HttpServletResponse response)
     * 
     * @param request
     * @param form
     * @param mapping
     * @return ActionForward
     */
    public ActionForward getNextOptions(ActionMapping mapping,
            ActionForm form,
            HttpServletRequest request,
            HttpServletResponse response) throws IOException,
                                         ServletException
	{
    	McUtils.cleanUpUserExceptions(request);
		logger.debug("dispatching getNextOptions...");
		McLearningForm mcLearningForm = (McLearningForm) form;
		IMcService mcService = McServiceProxy.getMcService(getServlet().getServletContext());		
	 	
		String currentQuestionIndex=(String)request.getSession().getAttribute(CURRENT_QUESTION_INDEX);
    	logger.debug("currentQuestionIndex:" + currentQuestionIndex);
    	
    	String totalQuestionCount=(String)request.getSession().getAttribute(TOTAL_QUESTION_COUNT);
    	logger.debug("totalQuestionCount:" + totalQuestionCount);
    	
    	int intTotalQuestionCount=new Integer(totalQuestionCount).intValue();
    	int intCurrentQuestionIndex=new Integer(currentQuestionIndex).intValue();
    	
    	if (intTotalQuestionCount-1 == intCurrentQuestionIndex)
    	{
    			logger.debug("totalQuestionCount has been reached :" + totalQuestionCount);
        		request.getSession().setAttribute(TOTAL_COUNT_REACHED, new Boolean(true).toString());
       	}
    		
   		mcLearningForm.resetCommands();
   
       	int newQuestionIndex=new Integer(currentQuestionIndex).intValue() + 1;
       	request.getSession().setAttribute(CURRENT_QUESTION_INDEX, new Integer(newQuestionIndex).toString());
       	logger.debug("updated questionIndex:" + request.getSession().getAttribute(CURRENT_QUESTION_INDEX));

       	return (mapping.findForward(LOAD_LEARNER));
    }


    /**
     * allows the learner to take the activity again
     * redoQuestions(ActionMapping mapping,
            ActionForm form,
            HttpServletRequest request,
            HttpServletResponse response)
     * 
     * @param request
     * @param form
     * @param mapping
     * @return ActionForward
     */
    public ActionForward redoQuestions(ActionMapping mapping,
            ActionForm form,
            HttpServletRequest request,
            HttpServletResponse response) throws IOException,
                                         ServletException
	{
    	McUtils.cleanUpUserExceptions(request);
		logger.debug("dispatching redoQuestions...");
		McLearningForm mcLearningForm = (McLearningForm) form;
		IMcService mcService = McServiceProxy.getMcService(getServlet().getServletContext());
		
	 	String toolContentId=mcLearningForm.getToolContentId();
	 	logger.debug("toolContentId: " + toolContentId);

    	McContent mcContent=mcService.retrieveMc(new Long(toolContentId));
    	logger.debug("mcContent: " + mcContent);
		
		
		McGeneralLearnerFlowDTO mcGeneralLearnerFlowDTO=LearningUtil.buildMcGeneralLearnerFlowDTO(mcContent);
 		
		request.getSession().setAttribute(CURRENT_QUESTION_INDEX, "1");
		request.getSession().setAttribute(TOTAL_COUNT_REACHED, new Boolean(false).toString());
		
		String toolSessionId=mcLearningForm.getToolSessionId();
		logger.debug("toolSessionId: " + toolSessionId);
    	
    	McSession mcSession=mcService.retrieveMcSession(new Long(toolSessionId));
        logger.debug("retrieving mcSession: " + mcSession);
        
        String userID = "";
	    HttpSession ss = SessionManager.getSession();
	    logger.debug("ss: " + ss);
	    
	    if (ss != null)
	    {
		    UserDTO user = (UserDTO) ss.getAttribute(AttributeNames.USER);
		    if ((user != null) && (user.getUserID() != null))
		    {
		    	userID = user.getUserID().toString();
			    logger.debug("retrieved userId: " + userID);
		    }
	    }
        
    	
    	McQueUsr mcQueUsr=mcService.getMcUserBySession(new Long(userID), mcSession.getUid());
		logger.debug("mcQueUsr: " + mcQueUsr);
		
		Long queUsrId=mcQueUsr.getUid();
		logger.debug("queUsrId: " + queUsrId);
		
		int learnerBestMark=LearningUtil.getHighestMark(request, queUsrId, mcService);
		logger.debug("learnerBestMark: " + learnerBestMark);
		mcGeneralLearnerFlowDTO.setLearnerBestMark(new Integer(learnerBestMark).toString());
		
		request.setAttribute(MC_GENERAL_LEARNER_FLOW_DTO, mcGeneralLearnerFlowDTO);
		logger.debug("MC_GENERAL_LEARNER_FLOW_DTO: " +  request.getAttribute(MC_GENERAL_LEARNER_FLOW_DTO));
		
		mcLearningForm.resetCommands();
		return (mapping.findForward(REDO_QUESTIONS));
    }
        

    /**
     * allows the learner to view their answer history
     * viewAnswers(ActionMapping mapping,
            ActionForm form,
            HttpServletRequest request,
            HttpServletResponse response)
     * 
     * @param request
     * @param form
     * @param mapping
     * @return ActionForward
     */
    public ActionForward viewAnswers(ActionMapping mapping,
            ActionForm form,
            HttpServletRequest request,
            HttpServletResponse response) throws IOException,
                                         ServletException
	{
        
        
    	McUtils.cleanUpUserExceptions(request);
		logger.debug("dispatching viewAnswers...");
		McLearningForm mcLearningForm = (McLearningForm) form;
		IMcService mcService = McServiceProxy.getMcService(getServlet().getServletContext());
		
		String toolContentId=mcLearningForm.getToolContentId();
	 	logger.debug("toolContentId: " + toolContentId);
	 	
	 	String toolSessionId=mcLearningForm.getToolSessionId();
	 	logger.debug("toolSessionId: " + toolSessionId);
	 	
    	McSession mcSession=mcService.retrieveMcSession(new Long(toolSessionId));
        logger.debug("retrieving mcSession: " + mcSession);

    	McContent mcContent=mcService.retrieveMc(new Long(toolContentId));
    	logger.debug("mcContent: " + mcContent);
    	
    	McGeneralLearnerFlowDTO mcGeneralLearnerFlowDTO=LearningUtil.buildMcGeneralLearnerFlowDTO(mcContent);

    	int intTotalQuestionCount=mcContent.getMcQueContents().size();
    	String totalQuestionCount= new Integer(intTotalQuestionCount).toString();
    	logger.debug("totalQuestionCount: " + totalQuestionCount);
		
		/* this section is needed to separate learner progress view from standard attempts list. Goes from here.. */
		String learnerProgress=mcLearningForm.getLearnerProgress();
		logger.debug("learnerProgress: " + learnerProgress);
		mcGeneralLearnerFlowDTO.setLearnerProgress(learnerProgress);

		String learnerProgressUserId=mcLearningForm.getLearnerProgressUserId();
		logger.debug("learnerProgressUserId: " + learnerProgressUserId);
		mcGeneralLearnerFlowDTO.setLearnerProgressUserId(learnerProgressUserId);

    	Map mapQuestionsUidContent=AuthoringUtil.rebuildQuestionUidMapfromDB(request, new Long(toolContentId), mcService);
    	logger.debug("mapQuestionsUidContent:" + mapQuestionsUidContent);

    	Map mapStartupGeneralOptionsContent=AuthoringUtil.rebuildStartupGeneralOptionsContentMapfromDB(request, mapQuestionsUidContent, mcService);
    	logger.debug("mapStartupGeneralOptionsContent:" + mapStartupGeneralOptionsContent);
    	mcGeneralLearnerFlowDTO.setMapGeneralOptionsContent(mapStartupGeneralOptionsContent);
    	
    	Map mapQuestionsContent=AuthoringUtil.rebuildQuestionMapfromDB(request, new Long(toolContentId), mcService);
    	logger.debug("mapQuestionsContent:" + mapQuestionsContent);
    	mcGeneralLearnerFlowDTO.setMapQuestionsContent(mapQuestionsContent);
		
		
		boolean learnerProgressOn=false;
		if (learnerProgressUserId != null)
		{
			if ((learnerProgress.equalsIgnoreCase("true")) && 
					(learnerProgressUserId.length() > 0))
			{
				logger.debug("request is for learner progress: learnerProgress: " + learnerProgress);
				logger.debug("request is for learner progress: learnerProgressUserId: " + learnerProgressUserId);
				learnerProgressOn=true;;
			}
		}
		logger.debug("final learnerProgressOn:" + learnerProgressOn);
		/*..till here*/
		
		McQueUsr mcQueUsr=null;
		Long queUsrId=null;
		if (learnerProgressOn == false)
		{
			mcQueUsr=LearningUtil.getUser(request, mcService, toolSessionId);
			logger.debug("mcQueUsr: " + mcQueUsr);
			
			queUsrId=mcQueUsr.getUid();
			logger.debug("queUsrId: " + queUsrId);
		}
		else
		{
	        logger.debug("using mcSession: " + mcSession);
	        logger.debug("using learnerProgressUserId: " + learnerProgressUserId);
	        
		    mcQueUsr=mcService.getMcUserBySession(new Long(learnerProgressUserId), mcSession.getUid());
		    logger.debug("mcQueUsr: " + mcQueUsr);
			
			queUsrId=mcQueUsr.getUid();
			logger.debug("queUsrId: " + queUsrId);
		}
		logger.debug("final mcQueUsr: " + mcQueUsr);
		logger.debug("final queUsrId: " + queUsrId);
		
		Long toolContentUID=mcContent.getUid();
		logger.debug("toolContentUID: " + toolContentUID);
    	
		Map mapQueAttempts= new TreeMap(new McComparator());
		Map mapQueCorrectAttempts= new TreeMap(new McComparator());
		Map mapQueIncorrectAttempts= new TreeMap(new McComparator());
		for (int i=1; i<=  new Integer(totalQuestionCount).intValue(); i++)
		{
			logger.debug("doing question with display order: " + i);
			McQueContent mcQueContent=mcService.getQuestionContentByDisplayOrder(new Long(i), toolContentUID);
			logger.debug("mcQueContent uid: " + mcQueContent.getUid());

			Map mapAttemptOrderAttempts= new TreeMap(new McComparator());
			Map mapAttemptOrderCorrectAttempts= new TreeMap(new McComparator());
			Map mapAttemptOrderIncorrectAttempts= new TreeMap(new McComparator());
			for (int j=1; j <= MAX_ATTEMPT_HISTORY ; j++ )
    		{
				logger.debug("getting list for queUsrId: " + queUsrId);
    			List attemptsByAttemptOrder=mcService.getAttemptByAttemptOrder(queUsrId, mcQueContent.getUid(), new Integer(j));
	    		logger.debug("attemptsByAttemptOrder: " + j + " is: " + attemptsByAttemptOrder);
	    	
	    		Map mapAttempts= new TreeMap(new McComparator());
	    		Map mapAttemptsIncorrect= new TreeMap(new McComparator());
	    		Map mapAttemptsCorrect= new TreeMap(new McComparator());
	    		Iterator attemptIterator=attemptsByAttemptOrder.iterator();
	    		Long mapIndex=new Long(1);
    	    	while (attemptIterator.hasNext())
    	    	{
    	    		McUsrAttempt mcUsrAttempt=(McUsrAttempt)attemptIterator.next();
    	    		if (learnerProgressOn == false)
    	    		{
    	    			logger.debug("learnerProgressOn is false, populating map based on all the learners");
        	    		if (mcUsrAttempt.isAttemptCorrect())
        	    		{
        	    			mapAttemptsCorrect.put(mapIndex.toString(), mcUsrAttempt.getMcOptionsContent().getMcQueOptionText());
        	    		}
        	    		else
        	    		{
        	    			mapAttemptsIncorrect.put(mapIndex.toString(), mcUsrAttempt.getMcOptionsContent().getMcQueOptionText());
        	    		}
        	    		mapAttempts.put(mapIndex.toString(), mcUsrAttempt.getMcOptionsContent().getMcQueOptionText());
    	    			logger.debug("added attempt with order: " + mcUsrAttempt.getAttemptOrder() + " , option text is: " + mcUsrAttempt.getMcOptionsContent().getMcQueOptionText());
    	    			mapIndex=new Long(mapIndex.longValue()+1);

    	    		}
    	    		else
    	    		{
    	    			/* this section is needed to separate learner progress view from standard attempts list. */
    	    			logger.debug("learnerProgressOn is true, populating map based on the learner id: " + learnerProgressUserId);
    	    			logger.debug("retrieve user based on uid in the attempt: " + mcUsrAttempt.getQueUsrId());
    	    			McQueUsr mcQueUsrLocal=mcService.getMcUserByUID(mcUsrAttempt.getQueUsrId());
    	    			logger.debug("mcQueUsrLocal: " + mcQueUsrLocal);
    	    			
    	    			if (mcQueUsrLocal.getQueUsrId().toString().equals(learnerProgressUserId))
						{
							logger.debug("found learner progress user: " + learnerProgressUserId);
	        	    		if (mcUsrAttempt.isAttemptCorrect())
	        	    		{
	        	    			mapAttemptsCorrect.put(mapIndex.toString(), mcUsrAttempt.getMcOptionsContent().getMcQueOptionText());
	        	    		}
	        	    		else
	        	    		{
	        	    			mapAttemptsIncorrect.put(mapIndex.toString(), mcUsrAttempt.getMcOptionsContent().getMcQueOptionText());
	        	    		}
	        	    		mapAttempts.put(mapIndex.toString(), mcUsrAttempt.getMcOptionsContent().getMcQueOptionText());
	    	    			logger.debug("added attempt with order: " + mcUsrAttempt.getAttemptOrder() + " , option text is: " + mcUsrAttempt.getMcOptionsContent().getMcQueOptionText());
	    	    			mapIndex=new Long(mapIndex.longValue()+1);
						}
    	    		}
				
    	    	}    	    		

    	    	logger.debug("final mapAttempts is: " + mapAttempts);
    	    	if (mapAttempts.size() > 0)
    	    	{
    	    		mapAttemptOrderAttempts.put(new Integer(j).toString(), mapAttempts);	
    	    	}
    	    	if (mapAttemptsCorrect.size() > 0)
    	    	{
    	    		mapAttemptOrderCorrectAttempts.put(new Integer(j).toString(), mapAttemptsCorrect);	
    	    	}
    	    	if (mapAttemptsIncorrect.size() > 0)
    	    	{
    	    		mapAttemptOrderIncorrectAttempts.put(new Integer(j).toString(), mapAttemptsIncorrect);	
    	    	}
    		}
			
			logger.debug("final mapAttemptOrderAttempts is: " + mapAttemptOrderAttempts);
			if (mapAttemptOrderAttempts.size() > 0)
	    	{
				mapQueAttempts.put(new Integer(i).toString(), mapAttemptOrderAttempts);	
	    	}
			if (mapAttemptOrderCorrectAttempts.size() > 0)
	    	{
				mapQueCorrectAttempts.put(new Integer(i).toString(), mapAttemptOrderCorrectAttempts);	
	    	}    			
			if (mapAttemptOrderIncorrectAttempts.size() > 0)
	    	{
    			mapQueIncorrectAttempts.put(new Integer(i).toString(), mapAttemptOrderIncorrectAttempts);	
	    	}    			
		}

		logger.debug("final mapQueAttempts is: " + mapQueAttempts);
		logger.debug("final mapQueCorrectAttempts is: " + mapQueCorrectAttempts);
		logger.debug("final mapQueIncorrectAttempts is: " + mapQueIncorrectAttempts);

		mcGeneralLearnerFlowDTO.setMapQueAttempts(mapQueAttempts);
		mcGeneralLearnerFlowDTO.setMapQueCorrectAttempts(mapQueCorrectAttempts);
		mcGeneralLearnerFlowDTO.setMapQueIncorrectAttempts(mapQueIncorrectAttempts);

		request.setAttribute(MC_GENERAL_LEARNER_FLOW_DTO, mcGeneralLearnerFlowDTO);
		logger.debug("MC_GENERAL_LEARNER_FLOW_DTO: " +  request.getAttribute(MC_GENERAL_LEARNER_FLOW_DTO));
		
		mcLearningForm.resetCommands();
		return (mapping.findForward(VIEW_ANSWERS));
	}


    /**
     * allows the learner to view all the other learners' activity summary 
     * viewSummary(ActionMapping mapping,
            ActionForm form,
            HttpServletRequest request,
            HttpServletResponse response)
     * 
     * @param request
     * @param form
     * @param mapping
     * @return ActionForward
     */
    public ActionForward viewSummary(ActionMapping mapping,
            ActionForm form,
            HttpServletRequest request,
            HttpServletResponse response) throws IOException,
                                         ServletException
	{
    	McUtils.cleanUpUserExceptions(request);
		logger.debug("dispatching viewSummary...");
		McLearningForm mcLearningForm = (McLearningForm) form;
		IMcService mcService = McServiceProxy.getMcService(getServlet().getServletContext());		

	 	String toolContentId=mcLearningForm.getToolContentId();
	 	logger.debug("toolContentId: " + toolContentId);

    	McContent mcContent=mcService.retrieveMc(new Long(toolContentId));
    	logger.debug("mcContent: " + mcContent);

		McGeneralLearnerFlowDTO mcGeneralLearnerFlowDTO=LearningUtil.buildMcGeneralLearnerFlowDTO(mcContent);
		
		int countSessionComplete=0;
		Iterator sessionsIterator=mcContent.getMcSessions().iterator();
		while (sessionsIterator.hasNext())
        {
		    McSession mcSession= (McSession)sessionsIterator.next();
		    if  (mcSession != null)
		    {
			    logger.debug("mcSession: " + mcSession);
			    if (mcSession.getSessionStatus().equals(COMPLETED))
			    {
			        logger.debug("COMPLETED session found: " + mcSession);
			        ++countSessionComplete;
			    }
		    }
        }
		logger.debug("countSessionComplete: " + countSessionComplete);
		
		
		int topMark=LearningUtil.getTopMark(request, mcService);
		int lowestMark=LearningUtil.getLowestMark(request, mcService);
		int averageMark=LearningUtil.getAverageMark(request, mcService);
		
		logger.debug("countSessionComplete: " + countSessionComplete);
		logger.debug("topMark: " + topMark);
		logger.debug("lowestMark: " + lowestMark);
		logger.debug("averageMark: " + averageMark);
		
		mcGeneralLearnerFlowDTO.setCountSessionComplete(new Integer(countSessionComplete).toString());
		mcGeneralLearnerFlowDTO.setTopMark(new Integer(topMark).toString());
		mcGeneralLearnerFlowDTO.setLowestMark(new Integer(lowestMark).toString());
		mcGeneralLearnerFlowDTO.setAverageMark(new Integer(averageMark).toString());
		
		request.setAttribute(MC_GENERAL_LEARNER_FLOW_DTO, mcGeneralLearnerFlowDTO);
		logger.debug("MC_GENERAL_LEARNER_FLOW_DTO: " +  request.getAttribute(MC_GENERAL_LEARNER_FLOW_DTO));
		
		mcLearningForm.resetCommands();
		return (mapping.findForward(RESULTS_SUMMARY));	
    }
        
    
    /**
     * to indicate that some learners are using the content
     * marks the content as used content
     * setContentInUse(HttpServletRequest request)
     * 
     * @param request
     */
    protected void setContentInUse(HttpServletRequest request, String toolContentId, IMcService mcService)
    {
        logger.debug("starting setContentInUse");
        logger.debug("toolContentId:" + toolContentId);
    	
    	McContent mcContent=mcService.retrieveMc(new Long(toolContentId));
    	logger.debug("mcContent:" + mcContent);
    	mcContent.setContentInUse(true);
    	logger.debug("content has been set to inuse");
    	mcService.saveMcContent(mcContent);
    }
	
    
    /**
     * redoQuestions(HttpServletRequest request, McLearningForm mcLearningForm, ActionMapping mapping)
     * 
     * @param request
     * @param mcLearningForm
     * @param mapping
     * @return
     */
    public ActionForward redoQuestions(HttpServletRequest request, McLearningForm mcLearningForm, ActionMapping mapping)
    {
    	McUtils.cleanUpUserExceptions(request);
    	logger.debug("requested redoQuestions...");
		IMcService mcService = McServiceProxy.getMcService(getServlet().getServletContext());
		
    	/* reset the checked options MAP */
    	Map mapGeneralCheckedOptionsContent= new TreeMap(new McComparator());

    	String toolContentId=mcLearningForm.getToolContentId();
	 	logger.debug("toolContentId: " + toolContentId);

    	McContent mcContent=mcService.retrieveMc(new Long(toolContentId));
    	logger.debug("mcContent: " + mcContent);

    	
		List listQuestionAndCandidateAnswersDTO=LearningUtil.buildQuestionAndCandidateAnswersDTO(request, mcContent, mcService);
		logger.debug("listQuestionAndCandidateAnswersDTO: " + listQuestionAndCandidateAnswersDTO);
		request.setAttribute(LIST_QUESTION_CANDIDATEANSWERS_DTO, listQuestionAndCandidateAnswersDTO);
		logger.debug("LIST_QUESTION_CANDIDATEANSWERS_DTO: " +  request.getAttribute(LIST_QUESTION_CANDIDATEANSWERS_DTO));
		
		McGeneralLearnerFlowDTO mcGeneralLearnerFlowDTO=LearningUtil.buildMcGeneralLearnerFlowDTO(mcContent);
		request.setAttribute(MC_GENERAL_LEARNER_FLOW_DTO, mcGeneralLearnerFlowDTO);
		logger.debug("MC_GENERAL_LEARNER_FLOW_DTO: " +  request.getAttribute(MC_GENERAL_LEARNER_FLOW_DTO));

    	mcLearningForm.resetCommands();

		logger.debug("fwding to LOAD_LEARNER: " + LOAD_LEARNER);
    	return (mapping.findForward(LOAD_LEARNER));
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
    
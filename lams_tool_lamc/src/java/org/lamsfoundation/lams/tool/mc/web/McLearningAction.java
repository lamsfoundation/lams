/***************************************************************************
 * Copyright (C) 2005 LAMS Foundation (http://lamsfoundation.org)
 * =============================================================
 * 
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
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
package org.lamsfoundation.lams.tool.mc.web;

import java.io.IOException;
import java.util.Iterator;
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
import org.lamsfoundation.lams.tool.exception.DataMissingException;
import org.lamsfoundation.lams.tool.exception.ToolException;
import org.lamsfoundation.lams.tool.mc.McAppConstants;
import org.lamsfoundation.lams.tool.mc.McApplicationException;
import org.lamsfoundation.lams.tool.mc.McComparator;
import org.lamsfoundation.lams.tool.mc.McUtils;
import org.lamsfoundation.lams.tool.mc.pojos.McContent;
import org.lamsfoundation.lams.tool.mc.pojos.McQueContent;
import org.lamsfoundation.lams.tool.mc.pojos.McQueUsr;
import org.lamsfoundation.lams.tool.mc.pojos.McUsrAttempt;
import org.lamsfoundation.lams.tool.mc.service.IMcService;
import org.lamsfoundation.lams.web.action.LamsDispatchAction;

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
	 	IMcService mcService =McUtils.getToolService(request);
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
	 	IMcService mcService =McUtils.getToolService(request);
	 	
    	mcLearningForm.resetParameters();
    	LearningUtil.readParameters(request, mcLearningForm);
    	
    	if (mcLearningForm.getContinueOptionsCombined() != null)
    	{
    		setContentInUse(request);
    		return continueOptionsCombined(mapping, form, request, response);
    	}
    	else if (mcLearningForm.getNextOptions() != null)
	 	{
    		setContentInUse(request);
	 		return getNextOptions(mapping, form, request, response);
	 	}
    	else if (mcLearningForm.getOptionCheckBoxSelected() != null)
    	{
    		setContentInUse(request);
    		mcLearningForm.resetCommands();
    		LearningUtil.selectOptionsCheckBox(request,mcLearningForm, mcLearningForm.getQuestionIndex());
    	}
    	else if (mcLearningForm.getRedoQuestions() != null)
    	{
    		setContentInUse(request);
    		return redoQuestions(mapping, form, request, response);
    	}
    	else if (mcLearningForm.getRedoQuestionsOk() != null)
    	{
    		setContentInUse(request);
    		logger.debug("requested redoQuestionsOk, user is sure to redo the questions.");
    		mcLearningForm.resetCommands();
    		return redoQuestions(request, mcLearningForm, mapping);
    	}
    	else if (mcLearningForm.getViewAnswers() != null)
    	{
    		setContentInUse(request);
    		return viewAnswers(mapping, form, request, response);
    	}
    	else if (mcLearningForm.getViewSummary() != null)
    	{
    		setContentInUse(request);
			return viewSummary(mapping, form, request, response);
    	}
    	else if (mcLearningForm.getLearnerFinished() != null)
    	{
    		logger.debug("requested learner finished, the learner should be directed to next activity.");
    		
    		Long toolSessionId = (Long) request.getSession().getAttribute(TOOL_SESSION_ID);
    		String userID=(String) request.getSession().getAttribute(USER_ID);
    		logger.debug("attempting to leave/complete session with toolSessionId:" + toolSessionId + " and userID:"+userID);
    		
    		McUtils.cleanUpSessionAbsolute(request);
    		
    		String nextUrl=null;
    		try
    		{
    			nextUrl=mcService.leaveToolSession(toolSessionId, new Long(userID));
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
    		
    		/* pay attention here*/
    		logger.debug("redirecting to the nextUrl: "+ nextUrl);
    		response.sendRedirect(nextUrl);
    		
    		return null;
    	}
    	else if (mcLearningForm.getDonePreview() != null)
    	{
    		logger.debug("requested  donePreview.");
        	mcLearningForm.resetCommands();
        	McUtils.cleanUpSessionAbsolute(request);
        	return (mapping.findForward(LEARNING_STARTER));
    	}
    	else if (mcLearningForm.getDoneLearnerProgress() != null)
    	{
    		logger.debug("requested  doneLearnerProgress.");
        	mcLearningForm.resetCommands();
        	McUtils.cleanUpSessionAbsolute(request);
        	return (mapping.findForward(LEARNING_STARTER));
    	}
    	
    	mcLearningForm.resetCommands();	
 		return (mapping.findForward(LOAD_LEARNER));
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
	 	IMcService mcService =McUtils.getToolService(request);
	 	
	 	/* process the answers */
		Map mapGeneralCheckedOptionsContent=(Map) request.getSession().getAttribute(MAP_GENERAL_CHECKED_OPTIONS_CONTENT);
    	logger.debug("final mapGeneralCheckedOptionsContent: " + mapGeneralCheckedOptionsContent);
    	
    	Long toolContentId=(Long) request.getSession().getAttribute(TOOL_CONTENT_ID);
    	logger.debug("toolContentId: " + toolContentId);
    			
    	logger.debug("will assess");
    	Integer passMark=(Integer) request.getSession().getAttribute(PASSMARK);
    	logger.debug("passMark: " + passMark);
    	
    	Map mapLeanerAssessmentResults=LearningUtil.assess(request, mapGeneralCheckedOptionsContent, toolContentId);
    	logger.debug("mapLeanerAssessmentResults: " + mapLeanerAssessmentResults);
    	logger.debug("assesment complete");
    	
    	int mark=LearningUtil.getMark(mapLeanerAssessmentResults);
    	logger.debug("mark: " + mark);
    	request.getSession().setAttribute(LEARNER_MARK, new Integer(mark).toString());
    	    	
    	Map mapQuestionWeights =(Map) request.getSession().getAttribute(MAP_QUESTION_WEIGHTS);
    	logger.debug("mapQuestionWeights: " + mapQuestionWeights);
    	
    	boolean passed=false;
    	if ((passMark != null) && (passMark.intValue() != 0)) 
		{
    		int totalUserWeight=LearningUtil.calculateWeights(mapLeanerAssessmentResults, mapQuestionWeights);
    		logger.debug("totalUserWeight: " + totalUserWeight);
    		
    		if (totalUserWeight < passMark.intValue())
    		{
    			logger.debug("USER FAILED");
    			logger.debug("totalUserWeight is less than passmark: " + totalUserWeight + " < " + passMark.intValue());
    			passed=false;
    		}
    		else
    		{
    			logger.debug("USER PASSED");
    			passed=true;
    		}
		}

    	boolean isUserDefined=LearningUtil.doesUserExists(request);
    	logger.debug("isUserDefined");
    	if (isUserDefined == false)
    	{
    		LearningUtil.createUser(request);
    		logger.debug("created user in the db");
    	}
    	McQueUsr mcQueUsr=LearningUtil.getUser(request);
    	logger.debug("mcQueUsr: " + mcQueUsr);
    	
    	
    	String highestAttemptOrder=(String)request.getSession().getAttribute(LEARNER_LAST_ATTEMPT_ORDER);
        logger.debug("current highestAttemptOrder:" + highestAttemptOrder);
        
        if (highestAttemptOrder == null)
    		highestAttemptOrder="0";
        
        logger.debug("passed: " + passed);
    	LearningUtil.createAttempt(request, mcQueUsr, mapGeneralCheckedOptionsContent, mark, passed, new Integer(highestAttemptOrder).intValue(), mapLeanerAssessmentResults);
    	logger.debug("created user attempt in the db");
    	
    	int intHighestAttemptOrder=new Integer(highestAttemptOrder).intValue()+ 1 ;
        logger.debug("updated highestAttemptOrder:" + intHighestAttemptOrder);
        request.getSession().setAttribute(LEARNER_LAST_ATTEMPT_ORDER, new Integer(intHighestAttemptOrder).toString());
        
        logger.debug("before getLearnerMarkAtLeast: passMark" + passMark);
        logger.debug("before getLearnerMarkAtLeast: mapQuestionWeights" + mapQuestionWeights);
        
        int learnerMarkAtLeast=LearningUtil.getLearnerMarkAtLeast(passMark,mapQuestionWeights);
        logger.debug("learnerMarkAtLeast:" + learnerMarkAtLeast);
        request.getSession().setAttribute(LEARNER_MARK_ATLEAST, new Integer(learnerMarkAtLeast).toString());
		
		mcLearningForm.resetCommands();
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
	 	IMcService mcService =McUtils.getToolService(request);
		
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
 		IMcService mcService =McUtils.getToolService(request);
 		
		request.getSession().setAttribute(CURRENT_QUESTION_INDEX, "1");
		request.getSession().setAttribute(TOTAL_COUNT_REACHED, new Boolean(false).toString());
		
		McQueUsr mcQueUsr=LearningUtil.getUser(request);
		Long queUsrId=mcQueUsr.getUid();
		logger.debug("queUsrId: " + queUsrId);
		
		int learnerBestMark=LearningUtil.getHighestMark(request, queUsrId);
		logger.debug("learnerBestMark: " + learnerBestMark);
		request.getSession().setAttribute(LEARNER_BEST_MARK,new Integer(learnerBestMark).toString());
		
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
 		IMcService mcService =McUtils.getToolService(request);
		String totalQuestionCount= (String) request.getSession().getAttribute(TOTAL_QUESTION_COUNT);
		logger.debug("totalQuestionCount: " + totalQuestionCount);
		
		/* this section is needed to separate learner progress view from standard attempts list. Goes from here.. */
		String learnerProgress=(String)request.getSession().getAttribute(LEARNER_PROGRESS);
		logger.debug("learnerProgress: " + learnerProgress);
		
		String learnerProgressUserId=(String)request.getSession().getAttribute(LEARNER_PROGRESS_USERID);
		logger.debug("learnerProgressUserId: " + learnerProgressUserId);
		
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
			mcQueUsr=LearningUtil.getUser(request);
			logger.debug("mcQueUsr: " + mcQueUsr);
			
			queUsrId=mcQueUsr.getUid();
			logger.debug("queUsrId: " + queUsrId);
		}
		else
		{
			mcQueUsr=mcService.retrieveMcQueUsr(new Long(learnerProgressUserId));
			logger.debug("mcQueUsr: " + mcQueUsr);
			
			queUsrId=mcQueUsr.getUid();
			logger.debug("queUsrId: " + queUsrId);
		}
		logger.debug("final mcQueUsr: " + mcQueUsr);
		logger.debug("final queUsrId: " + queUsrId);
		
		Long toolContentUID= (Long) request.getSession().getAttribute(TOOL_CONTENT_UID);
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
		request.getSession().setAttribute(MAP_QUE_ATTEMPTS, mapQueAttempts);
		request.getSession().setAttribute(MAP_QUE_CORRECT_ATTEMPTS, mapQueCorrectAttempts);
		request.getSession().setAttribute(MAP_QUE_INCORRECT_ATTEMPTS, mapQueIncorrectAttempts);
		
		logger.debug("final mapQueAttempts is: " + mapQueAttempts);
		logger.debug("final mapQueCorrectAttempts is: " + mapQueCorrectAttempts);
		logger.debug("final mapQueIncorrectAttempts is: " + mapQueIncorrectAttempts);
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
 		IMcService mcService =McUtils.getToolService(request);
		
		int countSessionComplete=mcService.countSessionComplete();
		int topMark=LearningUtil.getTopMark(request);
		int lowestMark=LearningUtil.getLowestMark(request);
		int averageMark=LearningUtil.getAverageMark(request);
		
		logger.debug("countSessionComplete: " + countSessionComplete);
		logger.debug("topMark: " + topMark);
		logger.debug("lowestMark: " + lowestMark);
		logger.debug("averageMark: " + averageMark);
		
		request.getSession().setAttribute(COUNT_SESSION_COMPLETE, new Integer(countSessionComplete).toString());
		request.getSession().setAttribute(TOP_MARK, new Integer(topMark).toString());
		request.getSession().setAttribute(LOWEST_MARK, new Integer(lowestMark).toString());
		request.getSession().setAttribute(AVERAGE_MARK, new Integer(averageMark).toString());
		
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
    protected void setContentInUse(HttpServletRequest request)
    {
    	IMcService mcService =McUtils.getToolService(request);
    	Long toolContentId=(Long)request.getSession().getAttribute(TOOL_CONTENT_ID);
    	logger.debug("toolContentId:" + toolContentId);
    	
    	McContent mcContent=mcService.retrieveMc(toolContentId);
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
    	/* reset the checked options MAP */
    	Map mapGeneralCheckedOptionsContent= new TreeMap(new McComparator());
    	request.getSession().setAttribute(MAP_GENERAL_CHECKED_OPTIONS_CONTENT, mapGeneralCheckedOptionsContent);
    	mcLearningForm.resetCommands();
		
		String previewOnly=(String)request.getSession().getAttribute(PREVIEW_ONLY);
		logger.debug("previewOnly: " + previewOnly);
		if ((previewOnly != null) && (previewOnly.equalsIgnoreCase("true")))
		{
	    	logger.debug("request for preview.");
			return (mapping.findForward(PREVIEW));
		}
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
    
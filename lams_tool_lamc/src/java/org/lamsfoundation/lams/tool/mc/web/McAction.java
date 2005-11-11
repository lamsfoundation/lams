/*
 *Copyright (C) 2005 LAMS Foundation (http://lamsfoundation.org)
 *
 *This program is free software; you can redistribute it and/or modify
 *it under the terms of the GNU General Public License as published by
 *the Free Software Foundation; either version 2 of the License, or
 *(at your option) any later version.
 *
 *This program is distributed in the hope that it will be useful,
 *but WITHOUT ANY WARRANTY; without even the implied warranty of
 *MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *GNU General Public License for more details.
 *
 *You should have received a copy of the GNU General Public License
 *along with this program; if not, write to the Free Software
 *Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307
 *USA
 *
 *http://www.gnu.org/licenses/gpl.txt
 */
package org.lamsfoundation.lams.tool.mc.web;

import java.io.DataInputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
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
import org.apache.struts.actions.DispatchAction;
import org.lamsfoundation.lams.tool.mc.McAppConstants;
import org.lamsfoundation.lams.tool.mc.McComparator;
import org.lamsfoundation.lams.tool.mc.McContent;
import org.lamsfoundation.lams.tool.mc.McOptsContent;
import org.lamsfoundation.lams.tool.mc.McQueContent;
import org.lamsfoundation.lams.tool.mc.McUtils;
import org.lamsfoundation.lams.tool.mc.service.IMcService;
import org.lamsfoundation.lams.usermanagement.dto.UserDTO;
import org.lamsfoundation.lams.web.session.SessionManager;
import org.lamsfoundation.lams.web.util.AttributeNames;


/**
 * 
 * change the logic about completion status
 *
 */

/**
 * 
 * once lams_learning is ready and appContext file is src/ then FINISH toool session will work.
 * 
 */

/**
 * 
 * done: removed styling, except error messages and table centering
 * 
 */

/**
 * The tool's Spring configuration file: qaCompactApplicationContext.xml
 * Main service bean of the tool is: org.lamsfoundation.lams.tool.qa.service.McServicePOJO
 * 
 * done: config file is read from classpath
 */


/**
 * 
 * the tool's web.xml will be modified to have classpath to learning service.
 * This is how the tool gets the definition of "learnerService"
 */

/**
 * 
 * when to reset define later and synchin monitor etc..
 *  
 */

/** make sure the tool gets called on:
 *	setAsForceComplete(Long userId) throws McApplicationException 
 */


/**
 * 
 * User Issue:
 * Right now:
 * 1- the tool gets the request object from the container.
 * 2- Principal principal = req.getUserPrincipal();
 * 3- String username = principal.getName();
 * 4- User userCompleteData = qaService.getCurrentUserData(userName);
 * 5- write back userCompleteData.getUserId()
 */


/**
 * 
 * JBoss Issue: 
 * Currently getUserPrincipal() returns null and ServletRequest.isUserInRole() always returns false on unsecured pages, 
 * even after the user has been authenticated.
 * http://jira.jboss.com/jira/browse/JBWEB-19 
 */


/**
 * eliminate calls:
 * authoringUtil.simulatePropertyInspector_RunOffline(request);
 * authoringUtil.simulatePropertyInspector_setAsDefineLater(request);
 */


/**
 * 
 * @author ozgurd
 *
 * TOOL PARAMETERS: ?? (toolAccessMode) ??
 * Authoring environment: toolContentId
 * Learning environment: toolSessionId + toolContentId  
 * Monitoring environment: toolContentId / Contribute tab:toolSessionId(s)
 * 	 
 * 
 */

/**
 * Note: the tool must support deletion of an existing content from within the authoring environment.
 * The current support for this is by implementing the tool contract : removeToolContent(Long toolContentId)
 */


/**
 * 
 * We have had to simulate container bahaviour in development stage by calling 
 * createToolSession and leaveToolSession from the web layer. These will go once the tool is 
 * in deployment environment.
 * 
 * 
 * CHECK: leaveToolSession and relavent LearnerService may need to be defined in the spring config file.
 * 
 */


/**
 * 
 * GROUPING SUPPORT: Find out what to do.
 */


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
 * @author Ozgur Demirtas
 */
public class McAction extends DispatchAction implements McAppConstants
{
	static Logger logger = Logger.getLogger(McAction.class.getName());
	
    /** 
     * <p>Struts dispatch method.</p> 
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
     */
    
	/**
	 * loadQ(ActionMapping mapping,
                               ActionForm form,
                               HttpServletRequest request,
                               HttpServletResponse response) throws IOException,
                                                            ServletException
                                                            
	 * return ActionForward
	 * main content/question content management and workflow logic
	 * 
	 * if the passed toolContentId exists in the db, we need to get the relevant data into the Map 
	 * if not, create the default Map 
	*/

	
	
    public ActionForward loadQ(ActionMapping mapping,
                               ActionForm form,
                               HttpServletRequest request,
                               HttpServletResponse response) throws IOException,
                                                            ServletException
    {
    	
		logger.debug("loadQ started...");
	 	McAuthoringForm mcAuthoringForm = (McAuthoringForm) form;

	 	IMcService mcService =McUtils.getToolService(request);
	 	logger.debug("mcService:" + mcService);

	 	/** define the next tab as Basic tab by default*/
	 	request.getSession().setAttribute(EDIT_OPTIONS_MODE, new Integer(0));
		logger.debug("resetting  EDIT_OPTIONS_MODE to 0");
	 	
	 	McUtils.persistRichText(request);
	 	
	 	String selectedQuestion=request.getParameter(SELECTED_QUESTION);
		logger.debug("read parameter selectedQuestion: " + selectedQuestion);
		
		if ((selectedQuestion != null) && (selectedQuestion.length() > 0))
		{
			request.getSession().setAttribute(SELECTED_QUESTION,selectedQuestion);
			logger.debug("updated SELECTED_QUESTION");
		}
		
		
		mcAuthoringForm.setAddQuestion(null);
		mcAuthoringForm.setRemoveQuestion(null);
		mcAuthoringForm.setEditOptions(null);
		mcAuthoringForm.setMoveUp(null);
		mcAuthoringForm.setMoveDown(null);
		mcAuthoringForm.setAddOption(null);
		mcAuthoringForm.setRemoveOption(null);
		mcAuthoringForm.setViewFileItem(null);
		
		
		String addQuestion=request.getParameter("addQuestion");
		logger.debug("parameter addQuestion" + addQuestion);
		if ((addQuestion != null) && addQuestion.equals("1"))
		{
			logger.debug("parameter addQuestion is selected " + addQuestion);
			mcAuthoringForm.setAddQuestion("1");
		}
		
		
		String editOptions=request.getParameter("editOptions");
		logger.debug("parameter editOptions" + editOptions);
		if ((editOptions != null) && editOptions.equals("1"))
		{
			logger.debug("parameter editOptions is selected " + editOptions);
			mcAuthoringForm.setEditOptions("1");
		}
		
		String removeQuestion=request.getParameter("removeQuestion");
		logger.debug("parameter removeQuestion" + removeQuestion);
		if ((removeQuestion != null) && removeQuestion.equals("1"))
		{
			logger.debug("parameter removeQuestion is selected " + removeQuestion);
			mcAuthoringForm.setRemoveQuestion("1");
		}
		
		String moveDown=request.getParameter("moveDown");
		logger.debug("parameter moveDown" + moveDown);
		if ((moveDown != null) && moveDown.equals("1"))
		{
			logger.debug("parameter moveDown is selected " + moveDown);
			mcAuthoringForm.setMoveDown("1");
		}

		String moveUp=request.getParameter("moveUp");
		logger.debug("parameter moveUp" + moveUp);
		if ((moveUp != null) && moveUp.equals("1"))
		{
			logger.debug("parameter moveUp is selected " + moveUp);
			mcAuthoringForm.setMoveUp("1");
		}
		
		String addOption=request.getParameter("addOption");
		logger.debug("parameter addOption" + addOption);
		if ((addOption != null) && addOption.equals("1"))
		{
			logger.debug("parameter addOption is selected " + addOption);
			mcAuthoringForm.setAddOption("1");
		}
		
		String removeOption=request.getParameter("removeOption");
		logger.debug("parameter removeOption" + removeOption);
		if ((removeOption != null) && removeOption.equals("1"))
		{
			logger.debug("parameter removeOption is selected " + removeOption);
			mcAuthoringForm.setRemoveOption("1");
		}
		
		String viewFileItem=request.getParameter("viewFileItem");
		logger.debug("parameter viewFileItem" + viewFileItem);
		if ((viewFileItem != null) && viewFileItem.equals("1"))
		{
			logger.debug("parameter viewFileItem is selected " + viewFileItem);
			mcAuthoringForm.setViewFileItem("1");
		}
		

		String userAction=null;
	 	if (mcAuthoringForm.getAddQuestion() != null)
	 	{
	 		userAction="addQuestion";
	 		request.setAttribute(USER_ACTION, userAction);
	 		logger.debug("userAction:" + userAction);
	 		
	 		Map mapQuestionsContent=repopulateMap(request, "questionContent");
		 	logger.debug("mapQuestionsContent after shrinking: " + mapQuestionsContent);
		 	logger.debug("mapQuestionsContent size after shrinking: " + mapQuestionsContent.size());
		 	
		 	logger.debug("will validate questions are not empty");
	 		boolean questionsNotEmptyValid=validateQuestionsNotEmpty(mapQuestionsContent);
        	logger.debug("questionsNotEmptyValid:" + questionsNotEmptyValid);
        	if (questionsNotEmptyValid == false)
        	{
        		ActionMessages errors= new ActionMessages();
        		errors= new ActionMessages();
				errors.add(Globals.ERROR_KEY,new ActionMessage("error.question.empty"));
				saveErrors(request,errors);
    			mcAuthoringForm.resetUserAction();
				persistError(request,"error.question.empty");
				
				int maxQuestionIndex=mapQuestionsContent.size();
				request.getSession().setAttribute(MAX_QUESTION_INDEX, new Integer(maxQuestionIndex));
				logger.debug("MAX_QUESTION_INDEX: " +  request.getSession().getAttribute(MAX_QUESTION_INDEX));
				return (mapping.findForward(LOAD_QUESTIONS));
        	}
	 		
	 		logger.debug("will validate weights");
	 		boolean weightsValid=validateQuestionWeights(request,mcAuthoringForm);
        	logger.debug("weightsValid:" + weightsValid);
        	if (weightsValid == false)
        	{
        		request.getSession().setAttribute(CURRENT_TAB, new Long(1));
        		mcAuthoringForm.resetUserAction();
        		
        		int maxQuestionIndex=mapQuestionsContent.size();
    			request.getSession().setAttribute(MAX_QUESTION_INDEX, new Integer(maxQuestionIndex));
    			logger.debug("MAX_QUESTION_INDEX: " +  request.getSession().getAttribute(MAX_QUESTION_INDEX));
				return (mapping.findForward(LOAD_QUESTIONS));
        	}
        	
        	
        	logger.debug("will validate SubTotalWeights");
	 		boolean subWeightsValid=validateSubTotalWeights(request,mcAuthoringForm);
        	logger.debug("subWeightsValid:" + subWeightsValid);
        	if (subWeightsValid == false)
        	{
        		ActionMessages errors= new ActionMessages();
        		errors= new ActionMessages();
				errors.add(Globals.ERROR_KEY,new ActionMessage("error.question.weight.total"));
				saveErrors(request,errors);
    			mcAuthoringForm.resetUserAction();
				persistError(request,"error.question.weight.total");
        		
        		request.getSession().setAttribute(CURRENT_TAB, new Long(1));
        		
        		int maxQuestionIndex=mapQuestionsContent.size();
    			request.getSession().setAttribute(MAX_QUESTION_INDEX, new Integer(maxQuestionIndex));
    			logger.debug("MAX_QUESTION_INDEX: " +  request.getSession().getAttribute(MAX_QUESTION_INDEX));
				return (mapping.findForward(LOAD_QUESTIONS));
        	}
	 		
	 		
		 	Map mapWeights= repopulateMap(request, "questionWeight");
			request.getSession().setAttribute(MAP_WEIGHTS, mapWeights);
			System.out.print("MAP_WEIGHTS:" + request.getSession().getAttribute(MAP_WEIGHTS));
			
			//addQuestion(request, mcAuthoringForm, mapQuestionsContent, true);
			addQuestionMemory(request, mcAuthoringForm, mapQuestionsContent, true);
			logger.debug("after addQuestionMemory");
			
			request.getSession().setAttribute(EDIT_OPTIONS_MODE, new Integer(0));
			logger.debug("resetting  EDIT_OPTIONS_MODE to 0");
    	    mcAuthoringForm.resetUserAction();
    	    request.getSession().setAttribute(CURRENT_TAB, new Long(1));
    	    
    	    int maxQuestionIndex=mapQuestionsContent.size();
			request.getSession().setAttribute(MAX_QUESTION_INDEX, new Integer(maxQuestionIndex));
			logger.debug("MAX_QUESTION_INDEX: " +  request.getSession().getAttribute(MAX_QUESTION_INDEX));
    	    return (mapping.findForward(LOAD_QUESTIONS));
	 	}
	 	else if (mcAuthoringForm.getRemoveQuestion() != null)
	 	{
	 		userAction="removeQuestion";
	 		request.setAttribute(USER_ACTION, userAction);
	 		logger.debug("userAction:" + userAction);
	 		
	 		Map mapWeights= repopulateMap(request, "questionWeight");
			request.getSession().setAttribute(MAP_WEIGHTS, mapWeights);
			System.out.print("MAP_WEIGHTS:" + request.getSession().getAttribute(MAP_WEIGHTS));
			
	 		Map mapQuestionsContent=repopulateMap(request, "questionContent");
		 	logger.debug("mapQuestionsContent after shrinking: " + mapQuestionsContent);
		 	logger.debug("mapQuestionsContent size after shrinking: " + mapQuestionsContent.size());
		 	
	 		String questionIndex =mcAuthoringForm.getQuestionIndex();
			logger.debug("questionIndex:" + questionIndex);
			String deletableQuestionEntry=(String)mapQuestionsContent.get(questionIndex);
			logger.debug("deletableQuestionEntry:" + deletableQuestionEntry);
			
			if (deletableQuestionEntry != null)
			{
				if (!(deletableQuestionEntry.equals("")))
				{
					mapQuestionsContent.remove(questionIndex);
					logger.debug("removed entry:" + deletableQuestionEntry + " from the Map");
					request.getSession().setAttribute(MAP_QUESTIONS_CONTENT, mapQuestionsContent);
					logger.debug("updated Questions Map: " + request.getSession().getAttribute(MAP_QUESTIONS_CONTENT));
				}
			}
			else
			{
				request.getSession().setAttribute(MAP_QUESTIONS_CONTENT, mapQuestionsContent);
				logger.debug("updated Questions Map: " + request.getSession().getAttribute(MAP_QUESTIONS_CONTENT));
			}
			
			

			/*
			if (deletableQuestionEntry != null)
			{
				if (!(deletableQuestionEntry.equals("")))
				{
					mapQuestionsContent.remove(questionIndex);
					logger.debug("removed entry:" + deletableQuestionEntry + " from the Map");
					request.getSession().setAttribute(MAP_QUESTIONS_CONTENT, mapQuestionsContent);
					logger.debug("updated Questions Map: " + request.getSession().getAttribute(MAP_QUESTIONS_CONTENT));
					
					Long toolContentId=(Long)request.getSession().getAttribute(TOOL_CONTENT_ID);
					logger.debug("toolContentId:" + toolContentId);
					
					McContent mcContent =mcService.retrieveMc(toolContentId);
					logger.debug("mcContent:" + mcContent);
					if  (mcContent != null)
					{
						McQueContent mcQueContent =mcService.getQuestionContentByQuestionText(deletableQuestionEntry, mcContent.getUid());
						logger.debug("mcQueContent:" + mcQueContent);
						
						if (mcQueContent != null)
						{
							mcQueContent=mcService.retrieveMcQueContentByUID(mcQueContent.getUid());
							mcService.removeMcQueContent(mcQueContent);
							logger.debug("removed mcQueContent from DB:" + mcQueContent);
						}	
					}
				}
			}
			else
			{
				request.getSession().setAttribute(MAP_QUESTIONS_CONTENT, mapQuestionsContent);
				logger.debug("updated Questions Map: " + request.getSession().getAttribute(MAP_QUESTIONS_CONTENT));
			}
	 		*/
			
		 	request.getSession().setAttribute(EDIT_OPTIONS_MODE, new Integer(0));
			logger.debug("resetting  EDIT_OPTIONS_MODE to 0");
			mcAuthoringForm.resetUserAction();
			request.getSession().setAttribute(CURRENT_TAB, new Long(1));
			
			int maxQuestionIndex=mapQuestionsContent.size();
			request.getSession().setAttribute(MAX_QUESTION_INDEX, new Integer(maxQuestionIndex));
			logger.debug("MAX_QUESTION_INDEX: " +  request.getSession().getAttribute(MAX_QUESTION_INDEX));
			return (mapping.findForward(LOAD_QUESTIONS));
	 	}
	 	else if (mcAuthoringForm.getEditOptions() != null)
	 	{
	 		userAction="editOption";
	 		request.setAttribute(USER_ACTION, userAction);
	 		logger.debug("userAction:" + userAction);
	 		
	 		Map mapGeneralOptionsContent=(Map)request.getSession().getAttribute(MAP_GENERAL_OPTIONS_CONTENT);
    		logger.debug("initial test: current mapGeneralOptionsContent: " + mapGeneralOptionsContent);
    		
    		request.getSession().setAttribute(EDIT_OPTIONS_MODE, new Integer(1));
			logger.debug("setting  EDIT_OPTIONS_MODE to 1");
	 		
			Map mapQuestionsContent=repopulateMap(request, "questionContent");
		 	logger.debug("mapQuestionsContent after shrinking: " + mapQuestionsContent);
		 	logger.debug("mapQuestionsContent size after shrinking: " + mapQuestionsContent.size());
		 	request.getSession().setAttribute(MAP_QUESTIONS_CONTENT, mapQuestionsContent);
		 	
		 	Map mapWeights= repopulateMap(request, "questionWeight");
			request.getSession().setAttribute(MAP_WEIGHTS, mapWeights);
			System.out.print("MAP_WEIGHTS:" + request.getSession().getAttribute(MAP_WEIGHTS));
		 	
			String questionIndex =mcAuthoringForm.getQuestionIndex();
			logger.debug("questionIndex:" + questionIndex);
			request.getSession().setAttribute(SELECTED_QUESTION_INDEX, questionIndex);
			logger.debug("set SELECTED_QUESTION_INDEX to:" + questionIndex);
			
			String editableQuestionEntry=(String)mapQuestionsContent.get(questionIndex);
			logger.debug("editableQuestionEntry:" + editableQuestionEntry);
			
			if ((editableQuestionEntry == null) || (editableQuestionEntry.equals("")))
			{
				ActionMessages errors= new ActionMessages();
				errors.add(Globals.ERROR_KEY,new ActionMessage("error.emptyQuestion"));
    			logger.debug("add error.emptyQuestion to ActionMessages");
    			saveErrors(request,errors);
    			mcAuthoringForm.resetUserAction();
    			logger.debug("return to LOAD_QUESTIONS to fix error.");
    			
    			request.getSession().setAttribute(EDIT_OPTIONS_MODE, new Integer(0));
    			logger.debug("setting  EDIT_OPTIONS_MODE to 0");
    			request.getSession().setAttribute(CURRENT_TAB, new Long(1));
    			return (mapping.findForward(LOAD_QUESTIONS));
			}
			
			Long toolContentId=(Long)request.getSession().getAttribute(TOOL_CONTENT_ID);
			logger.debug("toolContentId:" + toolContentId);
			
			McContent mcContent=mcService.retrieveMc(toolContentId);
			logger.debug("mcContent:" + mcContent);
			
			McQueContent mcQueContent=null;
			if (mcContent != null)
			{
				logger.debug("mcContent is not null");
				mcQueContent=mcService.getQuestionContentByQuestionText(editableQuestionEntry, mcContent.getUid());
		    	logger.debug("mcQueContent:" + mcQueContent);
			}
			
			request.getSession().setAttribute(SELECTED_QUESTION, editableQuestionEntry);
	    	logger.debug("SELECTED_QUESTION:" + request.getSession().getAttribute(SELECTED_QUESTION));
	    	
	    	if (mcQueContent != null)
	    	{
	    		logger.debug("mcQueContent is not null " + mcQueContent.getMcQueContentId());
	    		List listOptionsContent=mcService.findMcOptionsContentByQueId(mcQueContent.getMcQueContentId());
	    		logger.debug("listOptionsContent: " + listOptionsContent);
	    		
	    		Map mapOptionsContent=(Map)request.getSession().getAttribute(MAP_OPTIONS_CONTENT);
	    		mapOptionsContent.clear();
	    		
	    		Map mapSelectedOptions=(Map)request.getSession().getAttribute(MAP_SELECTED_OPTIONS);
	    		mapSelectedOptions.clear();
	    		
	    		/* options have been persisted before */ 
	    		if (listOptionsContent != null)
		 		{
	    			logger.debug("listOptionsContent not null" );
		 			Iterator listIteratorOptions=listOptionsContent.iterator();
			    	Long mapIndex=new Long(1);
			    	while (listIteratorOptions.hasNext())
			    	{
			    		McOptsContent mcOptsContent=(McOptsContent)listIteratorOptions.next();
			    		logger.debug("option text:" + mcOptsContent.getMcQueOptionText());
			    		mapOptionsContent.put(mapIndex.toString(),mcOptsContent.getMcQueOptionText());
			    		mapIndex=new Long(mapIndex.longValue()+1);
			    	}
			    	request.getSession().setAttribute(MAP_OPTIONS_CONTENT, mapOptionsContent);
	    			logger.debug("MAP_OPTIONS_CONTENT reconstructed from db" );
	    			
	    			
	    			/* we have to assume that some of the optons are selected as this is forced in the ui.
	    			 * retrieve and present the selected options from the db
	    			 * */
	    			List listSelectedOptions=mcService.getPersistedSelectedOptions(mcQueContent.getUid());
	    	 		logger.debug("listSelectedOptions:" + listSelectedOptions);
	    	 		
	    	 		if (listSelectedOptions != null)
	    	 		{
	    	 			Iterator listIteratorSelectedOptions=listSelectedOptions.iterator();
	    		    	mapIndex=new Long(1);
	    		    	while (listIteratorSelectedOptions.hasNext())
	    		    	{
	    		    		McOptsContent mcOptsContent=(McOptsContent)listIteratorSelectedOptions.next();
	    		    		logger.debug("option text:" + mcOptsContent.getMcQueOptionText());
	    		    		mapSelectedOptions.put(mapIndex.toString(),mcOptsContent.getMcQueOptionText());
	    		    		mapIndex=new Long(mapIndex.longValue()+1);
	    		    	}	
	    	 		}
	    	    	request.getSession().setAttribute(MAP_SELECTED_OPTIONS, mapSelectedOptions);
	    	    	logger.debug("MAP_SELECTED_OPTIONS reconstructed from db:" + mapSelectedOptions);
	    	    	
	    	    	String richTextFeedbackInCorrect=mcQueContent.getFeedbackIncorrect();
					logger.debug("richTextFeedbackInCorrect:" + richTextFeedbackInCorrect);
					if (richTextFeedbackInCorrect == null) richTextFeedbackInCorrect="";
					request.getSession().setAttribute(RICHTEXT_FEEDBACK_INCORRECT,richTextFeedbackInCorrect);
					
					String richTextFeedbackCorrect=mcQueContent.getFeedbackCorrect();
					logger.debug("richTextFeedbackCorrect:" + richTextFeedbackCorrect);
					if (richTextFeedbackCorrect == null) richTextFeedbackCorrect="";
					request.getSession().setAttribute(RICHTEXT_FEEDBACK_CORRECT,richTextFeedbackCorrect);
		 		}
	    		else
	    		{
	    			logger.debug("listOptionsContent is null: no options persisted yet" );
	    			logger.debug("present default options content" );
	    			Long queContentUID=(Long)request.getSession().getAttribute(DEFAULT_QUESTION_UID);
	    			logger.debug("DEFAULT_QUESTION_UID: " + queContentUID);
	    			List listDefaultOption=mcService.findMcOptionsContentByQueId(queContentUID);
	    			logger.debug("listDefaultOption: " + listDefaultOption);
	    			
	    			/** normally iterates only once */
	    			Iterator itDefaultOption=listDefaultOption.iterator();
			    	Long mapIndex=new Long(1);
			    	while (itDefaultOption.hasNext())
			    	{
			    		McOptsContent mcOptsContent=(McOptsContent)itDefaultOption.next();
			    		logger.debug("option text:" + mcOptsContent.getMcQueOptionText());
			    		mapOptionsContent.put(mapIndex.toString(),mcOptsContent.getMcQueOptionText());
			    		mapIndex=new Long(mapIndex.longValue()+1);
			    	}
			    	logger.debug("mapOptionsContent from default content: " + mapOptionsContent);
			    	request.getSession().setAttribute(MAP_OPTIONS_CONTENT, mapOptionsContent);
	    			logger.debug("MAP_OPTIONS_CONTENT reconstructed from default option content" );
	    			
	    			request.getSession().setAttribute(MAP_SELECTED_OPTIONS, mapSelectedOptions);
	    	    	logger.debug("MAP_SELECTED_OPTIONS set as empty list :" + mapSelectedOptions);
	    		}

	    		/* present the feedback content the same way for the conditions above*/
	    		String richTextFeedbackInCorrect=mcQueContent.getFeedbackIncorrect();
				logger.debug("richTextFeedbackInCorrect: " + richTextFeedbackInCorrect);
				if (richTextFeedbackInCorrect == null) richTextFeedbackInCorrect="";
				request.getSession().setAttribute(RICHTEXT_FEEDBACK_INCORRECT,richTextFeedbackInCorrect);
	        	
				String richTextFeedbackCorrect=mcQueContent.getFeedbackCorrect();
				logger.debug("richTextFeedbackCorrect: " + richTextFeedbackCorrect);
				if (richTextFeedbackCorrect == null) richTextFeedbackCorrect="";
				request.getSession().setAttribute(RICHTEXT_FEEDBACK_CORRECT,richTextFeedbackCorrect);
	    	}
	    	else
	    	{
	    		logger.debug("mcQueContent is null, check the session for any activity");
	    		logger.debug("check if the current question's option data has been stored in the MAP_GENERAL_OPTIONS_CONTENT");
	    		
	    		String selectedQuestionIndex=(String) request.getSession().getAttribute(SELECTED_QUESTION_INDEX);
				logger.debug("SELECTED_QUESTION_INDEX to:" + selectedQuestionIndex);
				
	    		logger.debug("mapGeneralOptionsContent to be checked: "  + mapGeneralOptionsContent);
				Iterator itMapGeneral = mapGeneralOptionsContent.entrySet().iterator();
	    		boolean optionsPresentationValid=false;
	    		
	    		Map mapOptionsContent= new TreeMap(new McComparator());
				Map mapSelectedOptions= new TreeMap(new McComparator());
				Map mapGsoc=(Map)request.getSession().getAttribute(MAP_GENERAL_SELECTED_OPTIONS_CONTENT);
				logger.debug("mapGsoc from the cache: " + mapGsoc);
				
				/* extract the relavent question's option from the larger Map */
	        	while (itMapGeneral.hasNext()) 
	        	{
	            	Map.Entry pairs = (Map.Entry)itMapGeneral.next();
	            	logger.debug("using the  pair: " +  pairs);
	                logger.debug("using the  pair entries: " +  pairs.getKey() + " = " + pairs.getValue());
	                
	                if ((pairs.getKey() != null))
	                {
	                	if (pairs.getKey().equals(selectedQuestionIndex))
	                	{
	                		logger.debug("question found with options in the cache");
	                		optionsPresentationValid=true;
	                		
	                		mapOptionsContent=(Map) pairs.getValue();
	                		logger.debug("mapOptionsContent from the cache: " + mapOptionsContent);
	                		request.getSession().setAttribute(MAP_OPTIONS_CONTENT, mapOptionsContent);
	                		logger.debug("updated MAP_OPTIONS_CONTENT: " + mapOptionsContent);
	                		
	                		logger.debug("mapGsoc: " + mapGsoc);
	    	        		Iterator itMapSelected = mapGsoc.entrySet().iterator();
	    		    		
	    		        	while (itMapSelected.hasNext()) {
	    		            	Map.Entry spairs = (Map.Entry)itMapSelected.next();
	    		                logger.debug("using the  spairs entries: " +  spairs.getKey() + " = " + spairs.getValue());
	    		                
	    		                if ((spairs.getKey() != null))
	    		                {
	    		                	if (spairs.getKey().equals(selectedQuestionIndex))
	    		                	{
	    		                		logger.debug("selected options for question found in the cache");

	    		                		mapSelectedOptions=(Map) spairs.getValue();
	    		                		logger.debug("mapSelectedOptionsContent from the cache: " + mapSelectedOptions);
	    		                		request.getSession().setAttribute(MAP_SELECTED_OPTIONS, mapSelectedOptions);
	    		                		logger.debug("updated MAP_SELECTED_OPTIONS: " + mapSelectedOptions);
	    		                	}
	    		                }
	    		    		}
	                	}
	                }
	    		}

	        	
	        	/* present the default content*/
	        	if (optionsPresentationValid == false)
	        	{
            		logger.debug("optionsPresentationValid is false, present default content");
	        		logger.debug("listOptionsContent is null: no options persisted yet" );
	    			logger.debug("present default options content" );
	    			Long queContentUID=(Long)request.getSession().getAttribute(DEFAULT_QUESTION_UID);
	    			logger.debug("DEFAULT_QUESTION_UID: " + queContentUID);
	    			List listDefaultOption=mcService.findMcOptionsContentByQueId(queContentUID);
	    			logger.debug("listDefaultOption: " + listDefaultOption);
	    			
	    			/* normally iterates only once */
	    			Iterator itDefaultOption=listDefaultOption.iterator();
			    	Long mapIndex=new Long(1);
			    	while (itDefaultOption.hasNext())
			    	{
			    		McOptsContent mcOptsContent=(McOptsContent)itDefaultOption.next();
			    		logger.debug("option text:" + mcOptsContent.getMcQueOptionText());
			    		mapOptionsContent.put(mapIndex.toString(),mcOptsContent.getMcQueOptionText());
			    		mapIndex=new Long(mapIndex.longValue()+1);
			    	}
			    	logger.debug("mapOptionsContent from default content: " + mapOptionsContent);
			    	request.getSession().setAttribute(MAP_OPTIONS_CONTENT, mapOptionsContent);
	    			logger.debug("MAP_OPTIONS_CONTENT reconstructed from default option content" );
	    			
	    			request.getSession().setAttribute(MAP_SELECTED_OPTIONS, mapSelectedOptions);
	    	    	logger.debug("MAP_SELECTED_OPTIONS set as empty list :" + mapSelectedOptions);
	        	}
	        	
	        	
	        	Map mapFeedbackIncorrect =(Map)request.getSession().getAttribute(MAP_FEEDBACK_INCORRECT);
				logger.debug("cached MAP_FEEDBACK_INCORRECT :" + mapFeedbackIncorrect);
				if (mapFeedbackIncorrect != null)
				{
					String richTextFeedbackInCorrect=(String)mapFeedbackIncorrect.get(selectedQuestionIndex);
					logger.debug("cached richTextFeedbackInCorrect:" + richTextFeedbackInCorrect);
					request.getSession().setAttribute(RICHTEXT_FEEDBACK_INCORRECT,richTextFeedbackInCorrect);	
				}
				else
				{
					logger.debug("mapFeedbackIncorrect is null");
					request.getSession().setAttribute(RICHTEXT_FEEDBACK_INCORRECT,"");
				}
				
				Map mapFeedbackCorrect =(Map)request.getSession().getAttribute(MAP_FEEDBACK_CORRECT);
				logger.debug("Submit final MAP_FEEDBACK_CORRECT :" + mapFeedbackCorrect);
				if (mapFeedbackCorrect != null)
				{
					String richTextFeedbackCorrect=(String)mapFeedbackCorrect.get(selectedQuestionIndex);
					logger.debug("cached richTextFeedbackCorrect:" + richTextFeedbackCorrect);
					request.getSession().setAttribute(RICHTEXT_FEEDBACK_CORRECT,richTextFeedbackCorrect);
				}
				else
				{
					logger.debug("mapFeedbackCorrect is null");
					request.getSession().setAttribute(RICHTEXT_FEEDBACK_CORRECT,"");
				}
	    	}	
				
		 	request.getSession().setAttribute(EDIT_OPTIONS_MODE, new Integer(1));
			logger.debug("resetting  EDIT_OPTIONS_MODE to 1");
			mcAuthoringForm.resetUserAction();
			request.getSession().setAttribute(CURRENT_TAB, new Long(1));
			return (mapping.findForward(LOAD_QUESTIONS));
	 	
	 	}
	 	else if (mcAuthoringForm.getMoveDown() != null)
	 	{
	 		userAction="moveDown";
	 		request.setAttribute(USER_ACTION, userAction);
	 		logger.debug("userAction:" + userAction);
	 		
	 		Map mapQuestionsContent=repopulateMap(request, "questionContent");
		 	logger.debug("mapQuestionsContent before move down: " + mapQuestionsContent);
		 	
	 		String questionIndex =mcAuthoringForm.getQuestionIndex();
			logger.debug("questionIndex:" + questionIndex);
			String movableQuestionEntry=(String)mapQuestionsContent.get(questionIndex);
			logger.debug("movableQuestionEntry:" + movableQuestionEntry);
			
			mapQuestionsContent= shiftMap(mapQuestionsContent, questionIndex,movableQuestionEntry,  "down");
			logger.debug("mapQuestionsContent after move down: " + mapQuestionsContent);
			
		 	request.getSession().setAttribute(MAP_QUESTIONS_CONTENT, mapQuestionsContent);
			logger.debug("updated Questions Map: " + request.getSession().getAttribute(MAP_QUESTIONS_CONTENT));
	 		
	 		mcAuthoringForm.resetUserAction();
	 		request.getSession().setAttribute(EDIT_OPTIONS_MODE, new Integer(0));
			logger.debug("resetting  EDIT_OPTIONS_MODE to 0");
    	    request.getSession().setAttribute(CURRENT_TAB, new Long(1));
    	    
    	    int maxQuestionIndex=mapQuestionsContent.size();
			request.getSession().setAttribute(MAX_QUESTION_INDEX, new Integer(maxQuestionIndex));
			logger.debug("MAX_QUESTION_INDEX: " +  request.getSession().getAttribute(MAX_QUESTION_INDEX));
    	    return (mapping.findForward(LOAD_QUESTIONS));
	 	}
	 	else if (mcAuthoringForm.getMoveUp() != null)
	 	{
	 		userAction="moveUp";
	 		request.setAttribute(USER_ACTION, userAction);
	 		logger.debug("userAction:" + userAction);
	 		
	 		Map mapQuestionsContent=repopulateMap(request, "questionContent");
		 	logger.debug("mapQuestionsContent before move down: " + mapQuestionsContent);
		 	
	 		String questionIndex =mcAuthoringForm.getQuestionIndex();
			logger.debug("questionIndex:" + questionIndex);
			String movableQuestionEntry=(String)mapQuestionsContent.get(questionIndex);
			logger.debug("movableQuestionEntry:" + movableQuestionEntry);
			
			mapQuestionsContent= shiftMap(mapQuestionsContent, questionIndex,movableQuestionEntry,  "up");
			logger.debug("mapQuestionsContent after move down: " + mapQuestionsContent);
			
		 	request.getSession().setAttribute(MAP_QUESTIONS_CONTENT, mapQuestionsContent);
			logger.debug("updated Questions Map: " + request.getSession().getAttribute(MAP_QUESTIONS_CONTENT));
	 		
	 		mcAuthoringForm.resetUserAction();
	 		request.getSession().setAttribute(EDIT_OPTIONS_MODE, new Integer(0));
			logger.debug("resetting  EDIT_OPTIONS_MODE to 0");
    	    request.getSession().setAttribute(CURRENT_TAB, new Long(1));
    	    
    	    int maxQuestionIndex=mapQuestionsContent.size();
			request.getSession().setAttribute(MAX_QUESTION_INDEX, new Integer(maxQuestionIndex));
			logger.debug("MAX_QUESTION_INDEX: " +  request.getSession().getAttribute(MAX_QUESTION_INDEX));
    	    return (mapping.findForward(LOAD_QUESTIONS));
	 	}
	 	else if (mcAuthoringForm.getAddOption() != null)
	 	{
	 		userAction="addOption";
	 		request.setAttribute(USER_ACTION, userAction);
	 		logger.debug("userAction:" + userAction);
	 		
	 		request.getSession().setAttribute(EDIT_OPTIONS_MODE, new Integer(1));
			logger.debug("setting  EDIT_OPTIONS_MODE to 1");
	 		
	 		Map mapOptionsContent=repopulateMap(request,"optionContent");
		 	logger.debug("mapOptionsContent after shrinking: " + mapOptionsContent);
		 	logger.debug("mapOptionsContent size after shrinking: " + mapOptionsContent.size());
		 	
		 	boolean verifyDuplicatesOptionsMap=verifyDuplicatesOptionsMap(mapOptionsContent);
		 	logger.debug("verifyDuplicatesOptionsMap: " + verifyDuplicatesOptionsMap);
		 	if (verifyDuplicatesOptionsMap == false)
	 		{
	 			ActionMessages errors= new ActionMessages();
				errors.add(Globals.ERROR_KEY,new ActionMessage("error.answers.duplicate"));
    			logger.debug("add error.answers.duplicate to ActionMessages");
    			saveErrors(request,errors);
    			mcAuthoringForm.resetUserAction();
    			logger.debug("return to LOAD_QUESTIONS to fix error.");
    			
    			request.getSession().setAttribute(EDIT_OPTIONS_MODE, new Integer(1));
    			logger.debug("setting  EDIT_OPTIONS_MODE to 1");
    			request.getSession().setAttribute(CURRENT_TAB, new Long(1));
    			return (mapping.findForward(LOAD_QUESTIONS));	
	 		}
		 	
		 	String selectedQuestionIndex=(String)request.getSession().getAttribute(SELECTED_QUESTION_INDEX);
			logger.debug("selectedQuestionIndex:" + selectedQuestionIndex);
			
		 	int mapSize=mapOptionsContent.size();
	 		mapOptionsContent.put(new Long(++mapSize).toString(), "");
			logger.debug("updated mapOptionsContent Map size: " + mapOptionsContent.size());
			request.getSession().setAttribute(MAP_OPTIONS_CONTENT, mapOptionsContent);
    		logger.debug("updated Options Map: " + request.getSession().getAttribute(MAP_OPTIONS_CONTENT));
    		
    		
    		Map mapGeneralOptionsContent=(Map)request.getSession().getAttribute(MAP_GENERAL_OPTIONS_CONTENT);
    		logger.debug("current mapGeneralOptionsContent: " + mapGeneralOptionsContent);
    		mapGeneralOptionsContent.put(selectedQuestionIndex,mapOptionsContent);
    		request.getSession().setAttribute(MAP_GENERAL_OPTIONS_CONTENT, mapGeneralOptionsContent);
    		logger.debug("updated  MAP_GENERAL_OPTIONS_CONTENT after add: " + mapGeneralOptionsContent);
    		
    		
    		/*
    		Long selectedQuestionContentUid=(Long) request.getSession().getAttribute(SELECTED_QUESTION_CONTENT_UID);
	 		logger.debug("selectedQuestionContentUid:" + selectedQuestionContentUid);
	 		
	 		McQueContent mcQueContent = mcService.retrieveMcQueContentByUID(selectedQuestionContentUid);
			logger.debug("mcQueContent:" + mcQueContent);
			
			mcService.removeMcOptionsContentByQueId(selectedQuestionContentUid);
			logger.debug("removed all mcOptionsContents for mcQueContentId :" + selectedQuestionContentUid);
			
			
	 		if (mcQueContent != null)
	 		{
	 	    	Iterator itOptionsMap = mapOptionsContent.entrySet().iterator();
	 	        while (itOptionsMap.hasNext()) {
	 	            Map.Entry pairs = (Map.Entry)itOptionsMap.next();
	 	            logger.debug("adding the  pair: " +  pairs.getKey() + " = " + pairs.getValue());
	 	            if ((pairs.getValue() != null) && (!pairs.getValue().equals("")))
	 	            {
	 	            	McOptsContent mcOptionsContent= new McOptsContent(false,pairs.getValue().toString() , mcQueContent, new HashSet());
	 	    	        logger.debug("created mcOptionsContent: " + mcOptionsContent);
	 	       	        mcService.saveMcOptionsContent(mcOptionsContent);
	 	       	        logger.debug("persisted the answer: " + pairs.getValue().toString());
	 	            }
	 	        }
	 		}
	 		*/
	 		
	 		Map mapSelectedOptions= (Map) request.getSession().getAttribute(MAP_SELECTED_OPTIONS);
	 		mapSelectedOptions.clear();
	 		mapSelectedOptions = repopulateCurrentCheckBoxStatesMap(request);
	 		logger.debug("after add mapSelectedOptions: " + mapSelectedOptions);
	 		request.getSession().setAttribute(MAP_SELECTED_OPTIONS, mapSelectedOptions);
	 		
	 		Map mapGeneralSelectedOptionsContent=(Map)request.getSession().getAttribute(MAP_GENERAL_SELECTED_OPTIONS_CONTENT);
    		logger.debug("current mapGeneralSelectedOptionsContent: " + mapGeneralSelectedOptionsContent);
    		mapGeneralSelectedOptionsContent.put(selectedQuestionIndex,mapSelectedOptions);
    		request.getSession().setAttribute(MAP_GENERAL_SELECTED_OPTIONS_CONTENT, mapGeneralSelectedOptionsContent);
    		logger.debug("updated  MAP_GENERAL_SELECTED_OPTIONS_CONTENT after add: " + mapGeneralSelectedOptionsContent);
    		
    		/* update feedback Maps*/
			Map mapFeedbackIncorrect =(Map)request.getSession().getAttribute(MAP_FEEDBACK_INCORRECT);
			logger.debug("current mapFeedbackIncorrect:" +  mapFeedbackIncorrect);
			String richTextFeedbackInCorrect=(String) request.getSession().getAttribute(RICHTEXT_FEEDBACK_INCORRECT);
			logger.debug("richTextFeedbackInCorrect: " + richTextFeedbackInCorrect);
			
			if (richTextFeedbackInCorrect == null) richTextFeedbackInCorrect="";
        	mapFeedbackIncorrect.put(selectedQuestionIndex, richTextFeedbackInCorrect);
        	request.getSession().setAttribute(MAP_FEEDBACK_INCORRECT, mapFeedbackIncorrect);
			logger.debug("updated MAP_FEEDBACK_INCORRECT:" +  mapFeedbackIncorrect);
			
			Map mapFeedbackCorrect =(Map)request.getSession().getAttribute(MAP_FEEDBACK_CORRECT);
			logger.debug("current mapFeedbackCorrect:" +  mapFeedbackCorrect);
			String richTextFeedbackCorrect=(String) request.getSession().getAttribute(RICHTEXT_FEEDBACK_CORRECT);
        	logger.debug("richTextFeedbackCorrect: " + richTextFeedbackCorrect);
        	
        	if (richTextFeedbackCorrect == null) richTextFeedbackCorrect="";
        	mapFeedbackCorrect.put(selectedQuestionIndex, richTextFeedbackCorrect);
        	request.getSession().setAttribute(MAP_FEEDBACK_CORRECT, mapFeedbackCorrect);
			logger.debug("updated MAP_FEEDBACK_INCORRECT:" +  mapFeedbackCorrect);
	 		
	 		request.getSession().setAttribute(EDIT_OPTIONS_MODE, new Integer(1));
			logger.debug("resetting  EDIT_OPTIONS_MODE to 1");
	 		mcAuthoringForm.resetUserAction();
			request.getSession().setAttribute(CURRENT_TAB, new Long(1));
			return (mapping.findForward(LOAD_QUESTIONS));
	 	}
	 	else if (mcAuthoringForm.getRemoveOption() != null)
	 	{
	 		userAction="removeOption";
	 		request.setAttribute(USER_ACTION, userAction);
	 		logger.debug("userAction:" + userAction);
	 		
	 		request.getSession().setAttribute(EDIT_OPTIONS_MODE, new Integer(1));
			logger.debug("setting  EDIT_OPTIONS_MODE to 1");
	 		
	 		String optionIndex =mcAuthoringForm.getDeletableOptionIndex();
			logger.debug("optionIndex:" + optionIndex);
			
			Map mapOptionsContent=repopulateMap(request, "optionContent");
		 	logger.debug("mapOptionsContent after shrinking: " + mapOptionsContent);
		 	logger.debug("mapOptionsContent size after shrinking: " + mapOptionsContent.size());
		 	
			String deletableOptionEntry=(String)mapOptionsContent.get(optionIndex);
			logger.debug("deletableOptionEntry:" + deletableOptionEntry);

			/*
			if (deletableOptionEntry != null)
			{
				if (!(deletableOptionEntry.equals("")))
				{
					mapOptionsContent.remove(optionIndex);
					logger.debug("removed entry:" + deletableOptionEntry + " from the Map");
					request.getSession().setAttribute(MAP_OPTIONS_CONTENT, mapOptionsContent);
					logger.debug("updated Options Map: " + request.getSession().getAttribute(MAP_OPTIONS_CONTENT));
					
					Long selectedQuestionContentUid=(Long) request.getSession().getAttribute(SELECTED_QUESTION_CONTENT_UID);
			 		logger.debug("selectedQuestionContentUid:" + selectedQuestionContentUid);
					
					logger.debug("deletableOptionEntry: " + deletableOptionEntry + "mcQueContentUid: " +  selectedQuestionContentUid);
					McOptsContent mcOptsContent=mcService.getOptionContentByOptionText(deletableOptionEntry, selectedQuestionContentUid);
					logger.debug("mcOptsContent: " + mcOptsContent);
					
					if (mcOptsContent != null)
					{
						mcService.removeMcOptionsContent(mcOptsContent);
						logger.debug("removed mcOptsContent from DB:" + mcOptsContent);
					}
				}
			}
			*/
			
			if (deletableOptionEntry != null)
			{
				if (!(deletableOptionEntry.equals("")))
				{
					mapOptionsContent.remove(optionIndex);
					logger.debug("removed entry:" + deletableOptionEntry + " from the Map");
					request.getSession().setAttribute(MAP_OPTIONS_CONTENT, mapOptionsContent);
					logger.debug("updated Options Map: " + request.getSession().getAttribute(MAP_OPTIONS_CONTENT));
				}
			}

			String selectedQuestionIndex=(String)request.getSession().getAttribute(SELECTED_QUESTION_INDEX);
			logger.debug("selectedQuestionIndex:" + selectedQuestionIndex);
    		
    		Map mapGeneralOptionsContent=(Map)request.getSession().getAttribute(MAP_GENERAL_OPTIONS_CONTENT);
    		logger.debug("current mapGeneralOptionsContent: " + mapGeneralOptionsContent);
    		mapGeneralOptionsContent.put(selectedQuestionIndex,mapOptionsContent);
    		request.getSession().setAttribute(MAP_GENERAL_OPTIONS_CONTENT, mapGeneralOptionsContent);
    		logger.debug("updated  MAP_GENERAL_OPTIONS_CONTENT after remove: " + mapGeneralOptionsContent);
    		
    		
    		Map mapSelectedOptions= (Map) request.getSession().getAttribute(MAP_SELECTED_OPTIONS);
	 		mapSelectedOptions.clear();
	 		mapSelectedOptions = repopulateCurrentCheckBoxStatesMap(request);
	 		logger.debug("after add mapSelectedOptions: " + mapSelectedOptions);
	 		request.getSession().setAttribute(MAP_SELECTED_OPTIONS, mapSelectedOptions);
	 		
    		Map mapGeneralSelectedOptionsContent=(Map)request.getSession().getAttribute(MAP_GENERAL_SELECTED_OPTIONS_CONTENT);
    		logger.debug("current mapGeneralSelectedOptionsContent: " + mapGeneralSelectedOptionsContent);
    		mapGeneralSelectedOptionsContent.put(selectedQuestionIndex,mapSelectedOptions);
    		request.getSession().setAttribute(MAP_GENERAL_SELECTED_OPTIONS_CONTENT, mapGeneralSelectedOptionsContent);
    		logger.debug("updated  MAP_GENERAL_SELECTED_OPTIONS_CONTENT after add: " + mapGeneralSelectedOptionsContent);
    		
			
    		/* update feedback Maps*/
			Map mapFeedbackIncorrect =(Map)request.getSession().getAttribute(MAP_FEEDBACK_INCORRECT);
			logger.debug("current mapFeedbackIncorrect:" +  mapFeedbackIncorrect);
			String richTextFeedbackInCorrect=(String) request.getSession().getAttribute(RICHTEXT_FEEDBACK_INCORRECT);
			logger.debug("richTextFeedbackInCorrect: " + richTextFeedbackInCorrect);
			
			if (richTextFeedbackInCorrect == null) richTextFeedbackInCorrect="";
        	mapFeedbackIncorrect.put(selectedQuestionIndex, richTextFeedbackInCorrect);
        	request.getSession().setAttribute(MAP_FEEDBACK_INCORRECT, mapFeedbackIncorrect);
			logger.debug("updated MAP_FEEDBACK_INCORRECT:" +  mapFeedbackIncorrect);
			
			Map mapFeedbackCorrect =(Map)request.getSession().getAttribute(MAP_FEEDBACK_CORRECT);
			logger.debug("current mapFeedbackCorrect:" +  mapFeedbackCorrect);
			String richTextFeedbackCorrect=(String) request.getSession().getAttribute(RICHTEXT_FEEDBACK_CORRECT);
        	logger.debug("richTextFeedbackCorrect: " + richTextFeedbackCorrect);
        	
        	if (richTextFeedbackCorrect == null) richTextFeedbackCorrect="";
        	mapFeedbackCorrect.put(selectedQuestionIndex, richTextFeedbackCorrect);
        	request.getSession().setAttribute(MAP_FEEDBACK_CORRECT, mapFeedbackCorrect);
			logger.debug("updated MAP_FEEDBACK_INCORRECT:" +  mapFeedbackCorrect);
    		
    		
	 	 	request.getSession().setAttribute(EDIT_OPTIONS_MODE, new Integer(1));
			logger.debug("resetting  EDIT_OPTIONS_MODE to 1");
			mcAuthoringForm.resetUserAction();			
			request.getSession().setAttribute(CURRENT_TAB, new Long(1));
			return (mapping.findForward(LOAD_QUESTIONS));
	 	}
	 	else if (mcAuthoringForm.getDoneOptions() != null)
	 	{
	 		userAction="doneOptions";
	 		request.setAttribute(USER_ACTION, userAction);
	 		logger.debug("userAction:" + userAction);
	 		
	 		request.getSession().setAttribute(EDIT_OPTIONS_MODE, new Integer(0));
			logger.debug("setting  EDIT_OPTIONS_MODE to 0");
			
	 		boolean validateOptions=validateOptions(request);
	 		logger.debug("validateOptions:" + validateOptions);
	 		
	 		if (validateOptions == false)
	 		{
	 			ActionMessages errors= new ActionMessages();
				errors.add(Globals.ERROR_KEY,new ActionMessage("error.checkBoxes.empty"));
    			logger.debug("add error.checkBoxes.empty to ActionMessages");
    			saveErrors(request,errors);
    			mcAuthoringForm.resetUserAction();
    			logger.debug("return to LOAD_QUESTIONS to fix error.");
    			
    			request.getSession().setAttribute(EDIT_OPTIONS_MODE, new Integer(1));
    			logger.debug("setting  EDIT_OPTIONS_MODE to 1");
    			request.getSession().setAttribute(CURRENT_TAB, new Long(1));
    			return (mapping.findForward(LOAD_QUESTIONS));	
	 		}
	 			 		
	 		/*
	 		Long selectedQuestionContentUid=(Long)request.getSession().getAttribute(SELECTED_QUESTION_CONTENT_UID);
	 	    logger.debug("selectedQuestionContentUid:" + selectedQuestionContentUid);
	 	   
	 	    selectedQuestion=(String) request.getSession().getAttribute(SELECTED_QUESTION);
			logger.debug("final selectedQuestion:" + selectedQuestion);
	 		
	 		McQueContent mcQueContent = mcService.retrieveMcQueContentByUID(selectedQuestionContentUid);
			logger.debug("mcQueContent:" + mcQueContent);
			
			mcQueContent.setQuestion(selectedQuestion);
			logger.debug("updated question set");
			*/
			
	 		Map mapOptionsContent=repopulateMap(request, "optionContent");
		 	logger.debug("mapOptionsContent after shrinking: " + mapOptionsContent);
		 	request.getSession().setAttribute(MAP_OPTIONS_CONTENT, mapOptionsContent);
			logger.debug("final done  MAP_OPTIONS_CONTENT: " + mapOptionsContent);
		 	
	 		
	 		String selectedQuestionIndex=(String) request.getSession().getAttribute(SELECTED_QUESTION_INDEX);
			logger.debug("retrieved SELECTED_QUESTION_INDEX to:" + selectedQuestionIndex);
			
			/** update the questions Map with the new question*/
			Map mapQuestionsContent=(Map) request.getSession().getAttribute(MAP_QUESTIONS_CONTENT);
		 	logger.debug("mapQuestionsContent: " + mapQuestionsContent);
		 	selectedQuestion=(String) request.getSession().getAttribute(SELECTED_QUESTION);
			logger.debug("final selectedQuestion:" + selectedQuestion);
			mapQuestionsContent.put(selectedQuestionIndex,selectedQuestion);
			logger.debug("updated mapQuestionsContent with:" +  selectedQuestionIndex + " and " + selectedQuestion);
			request.getSession().setAttribute(MAP_QUESTIONS_CONTENT, mapQuestionsContent);
			logger.debug("updated MAP_QUESTIONS_CONTENT:" +  mapQuestionsContent);
			
			
			Map mapSelectedOptions= (Map) request.getSession().getAttribute(MAP_SELECTED_OPTIONS);
	 		mapSelectedOptions.clear();
	 		mapSelectedOptions = repopulateCurrentCheckBoxStatesMap(request);
	 		logger.debug("after add mapSelectedOptions: " + mapSelectedOptions);
	 		request.getSession().setAttribute(MAP_SELECTED_OPTIONS, mapSelectedOptions);
	 		
    		Map mapGeneralSelectedOptionsContent=(Map)request.getSession().getAttribute(MAP_GENERAL_SELECTED_OPTIONS_CONTENT);
    		logger.debug("current mapGeneralSelectedOptionsContent: " + mapGeneralSelectedOptionsContent);
    		mapGeneralSelectedOptionsContent.put(selectedQuestionIndex,mapSelectedOptions);
    		request.getSession().setAttribute(MAP_GENERAL_SELECTED_OPTIONS_CONTENT, mapGeneralSelectedOptionsContent);
    		logger.debug("updated  MAP_GENERAL_SELECTED_OPTIONS_CONTENT after add: " + mapGeneralSelectedOptionsContent);
			
			
			/* update feedback Maps*/
			Map mapFeedbackIncorrect =(Map)request.getSession().getAttribute(MAP_FEEDBACK_INCORRECT);
			logger.debug("current mapFeedbackIncorrect:" +  mapFeedbackIncorrect);
			String richTextFeedbackInCorrect=(String) request.getSession().getAttribute(RICHTEXT_FEEDBACK_INCORRECT);
			logger.debug("richTextFeedbackInCorrect: " + richTextFeedbackInCorrect);
			
			if (richTextFeedbackInCorrect == null) richTextFeedbackInCorrect="";
        	mapFeedbackIncorrect.put(selectedQuestionIndex, richTextFeedbackInCorrect);
        	request.getSession().setAttribute(MAP_FEEDBACK_INCORRECT, mapFeedbackIncorrect);
			logger.debug("updated MAP_FEEDBACK_INCORRECT:" +  mapFeedbackIncorrect);
			
			Map mapFeedbackCorrect =(Map)request.getSession().getAttribute(MAP_FEEDBACK_CORRECT);
			logger.debug("current mapFeedbackCorrect:" +  mapFeedbackCorrect);
			String richTextFeedbackCorrect=(String) request.getSession().getAttribute(RICHTEXT_FEEDBACK_CORRECT);
        	logger.debug("richTextFeedbackCorrect: " + richTextFeedbackCorrect);
        	
        	if (richTextFeedbackCorrect == null) richTextFeedbackCorrect="";
        	mapFeedbackCorrect.put(selectedQuestionIndex, richTextFeedbackCorrect);
        	request.getSession().setAttribute(MAP_FEEDBACK_CORRECT, mapFeedbackCorrect);
			logger.debug("updated MAP_FEEDBACK_CORRECT:" +  mapFeedbackCorrect);
			
			
			
			//FIX HERE
			/*
			String richTextFeedbackCorrect=(String) request.getSession().getAttribute(RICHTEXT_FEEDBACK_CORRECT);
        	logger.debug("richTextFeedbackCorrect: " + richTextFeedbackCorrect);
        	if (richTextFeedbackCorrect == null) richTextFeedbackCorrect=""; 
			mcQueContent.setFeedbackCorrect(richTextFeedbackCorrect);
        	
        	String richTextFeedbackInCorrect=(String) request.getSession().getAttribute(RICHTEXT_FEEDBACK_INCORRECT);
        	logger.debug("richTextFeedbackInCorrect: " + richTextFeedbackInCorrect);
        	if (richTextFeedbackInCorrect == null) richTextFeedbackInCorrect="";
        	mcQueContent.setFeedbackIncorrect(richTextFeedbackInCorrect);
			
			mcService.saveOrUpdateMcQueContent(mcQueContent);
			logger.debug("persisted  selectedQuestion" + selectedQuestion);
			
			
			
			Map mapQuestionsContent =rebuildQuestionMapfromDB(request);
			request.getSession().setAttribute(MAP_QUESTIONS_CONTENT, mapQuestionsContent);
			logger.debug("updated MAP_QUESTIONS_CONTENT with the changed question:" + mapQuestionsContent);
			
			mcService.removeMcOptionsContentByQueId(selectedQuestionContentUid);
			logger.debug("removed all mcOptionsContents for mcQueContentId :" + selectedQuestionContentUid);
			
			String isCheckBoxSelected=null;
	 		boolean isCorrect=false;
	    	for (int i=1; i <= MAX_OPTION_COUNT ; i++)
			{
	    		isCorrect=false;
	    		isCheckBoxSelected=request.getParameter("checkBoxSelected" + i);
    			logger.debug("isCheckBoxSelected: " + isCheckBoxSelected);
    			String selectedIndex=null;
    			
    			if (isCheckBoxSelected != null)
    			{
        			if (isCheckBoxSelected.equals("Correct"))
        			{
        				isCorrect=true;
        			}

        			logger.debug("looped isCorrect: " + isCorrect);
    				logger.debug("looped selectedIndex: " + i);
    		 		
    		 		long mapCounter=0;
    		 		String selectedAnswer=null;
    		 		selectedAnswer=request.getParameter("optionContent" + i);
	    			logger.debug("found selectedAnswer: " + selectedAnswer);
	    			
    				Map mapOptionsContent=repopulateMap(request,"optionContent");
    			 	logger.debug("mapOptionsContent after shrinking: " + mapOptionsContent);
    			 	logger.debug("mapOptionsContent size after shrinking: " + mapOptionsContent.size());
    		 		request.getSession().setAttribute(MAP_OPTIONS_CONTENT, mapOptionsContent);
    	    		logger.debug("Options Map: " + request.getSession().getAttribute(MAP_OPTIONS_CONTENT));
    	    		
    	    		if ((selectedAnswer != null) && (selectedAnswer.length() > 0))
    	    		{
    	    			McOptsContent mcOptionsContent= new McOptsContent(isCorrect,selectedAnswer , mcQueContent, new HashSet());
     	    	        logger.debug("created mcOptionsContent: " + mcOptionsContent);
     	       	        mcService.saveMcOptionsContent(mcOptionsContent);
     	       	        logger.debug("final persistance of option");	
    	    		}
    			}
			}
			
			*/
			
			Map mapGeneralOptionsContent=(Map)request.getSession().getAttribute(MAP_GENERAL_OPTIONS_CONTENT);
    		logger.debug("current mapGeneralOptionsContent: " + mapGeneralOptionsContent);
    		mapGeneralOptionsContent.put(selectedQuestionIndex,mapOptionsContent);
    		request.getSession().setAttribute(MAP_GENERAL_OPTIONS_CONTENT, mapGeneralOptionsContent);
    		logger.debug("updated  MAP_GENERAL_OPTIONS_CONTENT after done: " + mapGeneralOptionsContent);
			
	    	request.getSession().setAttribute(EDIT_OPTIONS_MODE, new Integer(0));
			logger.debug("setting  EDIT_OPTIONS_MODE to 0");
			mcAuthoringForm.resetUserAction();
			request.getSession().setAttribute(CURRENT_TAB, new Long(1));
			return (mapping.findForward(LOAD_QUESTIONS));
	 	}
	 	else if (mcAuthoringForm.getSubmitQuestions() != null)
	 	{
	 		/* persist the final Questions Map  */
	 		userAction="submitQuestions";
	 		request.setAttribute(USER_ACTION, userAction);
	 		logger.debug("userAction:" + userAction);
	 		
	 		request.getSession().setAttribute(EDIT_OPTIONS_MODE, new Integer(0));
			logger.debug("setting  EDIT_OPTIONS_MODE to 0");

			Map mapQuestionsContent=repopulateMap(request, "questionContent");
		 	logger.debug("mapQuestionsContent before move down: " + mapQuestionsContent);
		 	request.getSession().setAttribute(MAP_QUESTIONS_CONTENT, mapQuestionsContent);
		 	
	 		ActionMessages errors= new ActionMessages();

        	boolean weightsValid=validateQuestionWeights(request,mcAuthoringForm);
        	logger.debug("weightsValid:" + weightsValid);
        	if (weightsValid == false)
        	{
    			request.getSession().setAttribute(CURRENT_TAB, new Long(1));
    			mcAuthoringForm.resetUserAction();
    			
    			int maxQuestionIndex=mapQuestionsContent.size();
    			request.getSession().setAttribute(MAX_QUESTION_INDEX, new Integer(maxQuestionIndex));
    			logger.debug("MAX_QUESTION_INDEX: " +  request.getSession().getAttribute(MAX_QUESTION_INDEX));
				return (mapping.findForward(LOAD_QUESTIONS));
        	}
        	
        	boolean isTotalWeightsValid=validateTotalWeight(request);
        	logger.debug("isTotalWeightsValid:" + isTotalWeightsValid);
        	if (isTotalWeightsValid == false)
        	{
        		errors= new ActionMessages();
        		errors.add(Globals.ERROR_KEY,new ActionMessage("error.weights.total.invalid"));
				saveErrors(request,errors);
    			mcAuthoringForm.resetUserAction();
				persistError(request,"error.weights.total.invalid");
				
				request.getSession().setAttribute(EDIT_OPTIONS_MODE, new Integer(0));
    			logger.debug("setting  EDIT_OPTIONS_MODE to 0");
    			request.getSession().setAttribute(CURRENT_TAB, new Long(1));
    			
    			int maxQuestionIndex=mapQuestionsContent.size();
    			request.getSession().setAttribute(MAX_QUESTION_INDEX, new Integer(maxQuestionIndex));
    			logger.debug("MAX_QUESTION_INDEX: " +  request.getSession().getAttribute(MAX_QUESTION_INDEX));
				return (mapping.findForward(LOAD_QUESTIONS));
        	}
        	
        	boolean isQuestionsSequenced=false;
        	boolean isSynchInMonitor=false;
        	boolean isUsernameVisible=false;
        	boolean isRetries=false;
        	boolean isShowFeedback=false;
        	boolean isSln=false;
        	
        	String monitoringReportTitle="";
        	String reportTitle="";
        	String endLearningMessage="";
        	int passmark=0;
        	
        	logger.debug("isQuestionsSequenced: " +  mcAuthoringForm.getQuestionsSequenced());
        	if (mcAuthoringForm.getQuestionsSequenced().equalsIgnoreCase(ON))
        		isQuestionsSequenced=true;
        	
        	logger.debug("isSynchInMonitor: " +  mcAuthoringForm.getSynchInMonitor());
        	if (mcAuthoringForm.getSynchInMonitor().equalsIgnoreCase(ON))
        		isSynchInMonitor=true;
        	
        	logger.debug("isUsernameVisible: " +  mcAuthoringForm.getUsernameVisible());
        	if (mcAuthoringForm.getUsernameVisible().equalsIgnoreCase(ON))
        		isUsernameVisible=true;
        	
        	logger.debug("isRetries: " +  mcAuthoringForm.getRetries());
        	if (mcAuthoringForm.getRetries().equalsIgnoreCase(ON))
        		isRetries=true;
        	
        	logger.debug("isSln" +  mcAuthoringForm.getSln());
        	if (mcAuthoringForm.getSln().equalsIgnoreCase(ON))
        		isSln=true;
        	
        	logger.debug("passmark: " +  mcAuthoringForm.getPassmark());
        	
        	if (mcAuthoringForm.getPassmark() != null)
        	{
        		try
				{
	        		passmark= new Integer(mcAuthoringForm.getPassmark()).intValue();
	        		logger.debug("tried passmark: " +  passmark);
				}
	        	catch(Exception e)
				{
	        		errors= new ActionMessages();
					errors.add(Globals.ERROR_KEY,new ActionMessage("error.passmark.notInteger"));
					saveErrors(request,errors);
	    			mcAuthoringForm.resetUserAction();
					persistError(request,"error.passmark.notInteger");
					
					request.getSession().setAttribute(EDIT_OPTIONS_MODE, new Integer(0));
	    			logger.debug("setting  EDIT_OPTIONS_MODE to 0");
	    			request.getSession().setAttribute(CURRENT_TAB, new Long(1));
	    			
	    			int maxQuestionIndex=mapQuestionsContent.size();
	    			request.getSession().setAttribute(MAX_QUESTION_INDEX, new Integer(maxQuestionIndex));
	    			logger.debug("MAX_QUESTION_INDEX: " +  request.getSession().getAttribute(MAX_QUESTION_INDEX));
					return (mapping.findForward(LOAD_QUESTIONS));
				}
        	}
        	
        	
        	if ((mcAuthoringForm.getPassmark() != null) && (mcAuthoringForm.getPassmark().length() > 0))
        	{
        		passmark= new Integer(mcAuthoringForm.getPassmark()).intValue();
        		logger.debug("populated passmark: " +  passmark);
        	}
        	else
        	{
        		errors= new ActionMessages();
				errors.add(Globals.ERROR_KEY,new ActionMessage("error.passMark.empty"));
    			logger.debug("add error.passMark.empty to ActionMessages");
    			saveErrors(request,errors);
    			mcAuthoringForm.resetUserAction();
    			logger.debug("return to LOAD_QUESTIONS to fix error.");
    			
    			request.getSession().setAttribute(EDIT_OPTIONS_MODE, new Integer(0));
    			logger.debug("setting  EDIT_OPTIONS_MODE to 0");
    			request.getSession().setAttribute(CURRENT_TAB, new Long(1));
    			
    			int maxQuestionIndex=mapQuestionsContent.size();
    			request.getSession().setAttribute(MAX_QUESTION_INDEX, new Integer(maxQuestionIndex));
    			logger.debug("MAX_QUESTION_INDEX: " +  request.getSession().getAttribute(MAX_QUESTION_INDEX));
    			return (mapping.findForward(LOAD_QUESTIONS));
        	}
        	
        	logger.debug("isShowFeedback: " +  mcAuthoringForm.getShowFeedback());
        	if (mcAuthoringForm.getShowFeedback().equalsIgnoreCase(ON))
        		isShowFeedback=true;
        	    	
        	logger.debug("MONITORING_REPORT_TITLE: " +  mcAuthoringForm.getMonitoringReportTitle());
        	monitoringReportTitle=mcAuthoringForm.getMonitoringReportTitle();
        	if ((monitoringReportTitle == null) || (monitoringReportTitle.length() == 0)) 
        		monitoringReportTitle=(String)request.getSession().getAttribute(MONITORING_REPORT_TITLE);
        	
        	reportTitle=mcAuthoringForm.getReportTitle();
        	logger.debug("REPORT_TITLE: " +  mcAuthoringForm.getReportTitle());
        	if ((reportTitle == null) || (reportTitle.length() == 0)) 
        		reportTitle=(String)request.getSession().getAttribute(REPORT_TITLE);
        	
        	endLearningMessage=mcAuthoringForm.getEndLearningMessage();
        	logger.debug("END_LEARNING_MESSAGE: " +  mcAuthoringForm.getEndLearningMessage());
        	if ((endLearningMessage == null) || (endLearningMessage.length() == 0))
        		endLearningMessage=(String)request.getSession().getAttribute(END_LEARNING_MESSAGE);

        	
        	String richTextTitle=(String) request.getSession().getAttribute(RICHTEXT_TITLE);
        	logger.debug("richTextTitle: " + richTextTitle);
        	
        	String richTextInstructions=(String) request.getSession().getAttribute(RICHTEXT_INSTRUCTIONS);
        	logger.debug("richTextInstructions: " + richTextInstructions);
        	
	 		
	 		if ((richTextTitle == null) || (richTextTitle.length() == 0) || richTextTitle.equalsIgnoreCase(RICHTEXT_BLANK))
    		{
        		errors.add(Globals.ERROR_KEY,new ActionMessage("error.title"));
    			logger.debug("add title to ActionMessages");
    		}

    		if ((richTextInstructions == null) || (richTextInstructions.length() == 0) || richTextInstructions.equalsIgnoreCase(RICHTEXT_BLANK))
    		{
    			errors.add(Globals.ERROR_KEY, new ActionMessage("error.instructions"));
    			logger.debug("add instructions to ActionMessages: ");
    		}

    		if (errors.size() > 0)  
    		{
    			logger.debug("either title or instructions or both is missing. Returning back to from to fix errors:");
				request.getSession().setAttribute(EDIT_OPTIONS_MODE, new Integer(0));
    			logger.debug("setting  EDIT_OPTIONS_MODE to 0");
    			request.getSession().setAttribute(CURRENT_TAB, new Long(1));
    			mcAuthoringForm.resetUserAction();
    			
    			int maxQuestionIndex=mapQuestionsContent.size();
    			request.getSession().setAttribute(MAX_QUESTION_INDEX, new Integer(maxQuestionIndex));
    			logger.debug("MAX_QUESTION_INDEX: " +  request.getSession().getAttribute(MAX_QUESTION_INDEX));
    			return (mapping.findForward(LOAD_QUESTIONS));
    		}
    		
    		String richTextOfflineInstructions=(String) request.getSession().getAttribute(RICHTEXT_OFFLINEINSTRUCTIONS);
        	logger.debug("richTextOfflineInstructions: " + richTextOfflineInstructions);
        	if (richTextOfflineInstructions == null) richTextOfflineInstructions="";
        	
        	String richTextOnlineInstructions=(String) request.getSession().getAttribute(RICHTEXT_ONLINEINSTRUCTIONS);
        	logger.debug("richTextOnlineInstructions: " + richTextOnlineInstructions);
        	if (richTextOnlineInstructions == null) richTextOnlineInstructions="";
        	
        	String richTextReportTitle=(String)request.getSession().getAttribute(RICHTEXT_REPORT_TITLE);
    		logger.debug("richTextReportTitle: " + richTextReportTitle);
    		
    		String richTextEndLearningMessage=(String)request.getSession().getAttribute(RICHTEXT_END_LEARNING_MSG);
    		logger.debug("richTextEndLearningMessage: " + richTextEndLearningMessage);
        	
        	
	 		mapQuestionsContent=repopulateMap(request, "questionContent");
		 	logger.debug("FINAL mapQuestionsContent after shrinking: " + mapQuestionsContent);
		 	logger.debug("mapQuestionsContent size after shrinking: " + mapQuestionsContent.size());
		 	request.getSession().setAttribute(MAP_QUESTIONS_CONTENT, mapQuestionsContent);
		 	
		 	Long toolContentId=(Long)request.getSession().getAttribute(TOOL_CONTENT_ID);
	 	    logger.debug("toolContentId:" + toolContentId);
				
			McContent mcContent=mcService.retrieveMc(toolContentId);
			logger.debug("mcContent:" + mcContent);
			
						
			if (mcContent != null)
			{
				logger.debug("updating mcContent title and instructions:" + mcContent);
				mcContent.setTitle(richTextTitle);
				mcContent.setInstructions(richTextInstructions);

				mcContent.setQuestionsSequenced(isQuestionsSequenced); 
			    mcContent.setSynchInMonitor(isSynchInMonitor);
			    mcContent.setUsernameVisible(isUsernameVisible);
			    mcContent.setRetries(isRetries);
			    mcContent.setPassMark(new Integer(passmark));
			    mcContent.setShowFeedback(isShowFeedback);
			    mcContent.setShowReport(isSln);
			    mcContent.setEndLearningMessage(endLearningMessage);
			    mcContent.setReportTitle(richTextReportTitle);
			    mcContent.setMonitoringReportTitle(monitoringReportTitle);
			    mcContent.setEndLearningMessage(richTextEndLearningMessage);
			    mcContent.setOfflineInstructions(richTextOfflineInstructions);
			    mcContent.setOnlineInstructions(richTextOnlineInstructions);
			}
			else
			{
				mcContent=createContent(request, mcAuthoringForm);		
				logger.debug("mcContent created");
			}
					    
			mapQuestionsContent=(Map) request.getSession().getAttribute(MAP_QUESTIONS_CONTENT);
			logger.debug("Submit final MAP_QUESTIONS_CONTENT :" + mapQuestionsContent);
			
			Map mapFeedbackIncorrect =(Map)request.getSession().getAttribute(MAP_FEEDBACK_INCORRECT);
			logger.debug("Submit final MAP_FEEDBACK_INCORRECT :" + mapFeedbackIncorrect);

			Map mapFeedbackCorrect =(Map)request.getSession().getAttribute(MAP_FEEDBACK_CORRECT);
			logger.debug("Submit final MAP_FEEDBACK_CORRECT :" + mapFeedbackCorrect);
			
			cleanupExistingQuestions(request, mcContent);
			logger.debug("post cleanupExistingQuestions");
			
			
			persistQuestions(request, mapQuestionsContent, mapFeedbackIncorrect, mapFeedbackCorrect, mcContent);
			logger.debug("post persistQuestions");
			
			logger.debug("will do addUploadedFilesMetaData");
			McUtils.addUploadedFilesMetaData(request,mcContent);
			logger.debug("done addUploadedFilesMetaData");
			
			errors.clear();
			errors.add(Globals.ERROR_KEY,new ActionMessage("submit.successful"));
			logger.debug("add submit.successful to ActionMessages");
			saveErrors(request,errors);
			request.setAttribute(SUBMIT_SUCCESS, new Integer(1));
			logger.debug("set SUBMIT_SUCCESS to 1");
			
			request.getSession().setAttribute(EDIT_OPTIONS_MODE, new Integer(0));
			logger.debug("setting  EDIT_OPTIONS_MODE to 0");

			mcAuthoringForm.resetUserAction();
			request.getSession().setAttribute(CURRENT_TAB, new Long(1));
			
			int maxQuestionIndex=mapQuestionsContent.size();
			request.getSession().setAttribute(MAX_QUESTION_INDEX, new Integer(maxQuestionIndex));
			logger.debug("MAX_QUESTION_INDEX: " +  request.getSession().getAttribute(MAX_QUESTION_INDEX));
	   	    return (mapping.findForward(LOAD_QUESTIONS));
	 	}
	 	else if (mcAuthoringForm.getAdvancedTabDone() != null)
	 	{
	 		userAction="advancedTabDone";
	 		request.setAttribute(USER_ACTION, userAction);
	 		logger.debug("userAction:" + userAction);
	 		
	 		mcAuthoringForm.resetUserAction();
			request.getSession().setAttribute(CURRENT_TAB, new Long(1));
	   	    return (mapping.findForward(LOAD_QUESTIONS));
	 	}
	 	else if (mcAuthoringForm.getInstructionsTabDone() != null)
	 	{
	 		userAction="instructionsTabDone";
	 		request.setAttribute(USER_ACTION, userAction);
	 		logger.debug("userAction:" + userAction);
	 		mcAuthoringForm.resetUserAction();
			request.getSession().setAttribute(CURRENT_TAB, new Long(1));
	   	    return (mapping.findForward(LOAD_QUESTIONS));
	 	} 
	 	else if (mcAuthoringForm.getSubmitOfflineFile() != null)
        {
	 		userAction="submitOfflineFile";
	 		request.setAttribute(USER_ACTION, userAction);
	 		logger.debug("userAction:" + userAction);
	 		
	 		McUtils.addFileToContentRepository(request, mcAuthoringForm, true);
            logger.debug("offline file added to repository successfully.");
	 		
	 		mcAuthoringForm.resetUserAction();
	 		request.getSession().setAttribute(EDIT_OPTIONS_MODE, new Integer(2));
	 		logger.debug("setting EDIT_OPTIONS_MODE :" + 2 );
			request.getSession().setAttribute(CURRENT_TAB, new Long(3));
	   	    return (mapping.findForward(ALL_INSTRUCTIONS));
        }
	 	else if (mcAuthoringForm.getSubmitOnlineFile() != null)
        {
	 		userAction="submitOnlineFile";
	 		request.setAttribute(USER_ACTION, userAction);
	 		logger.debug("userAction:" + userAction);
	 		
	 		McUtils.addFileToContentRepository(request, mcAuthoringForm, false);
            logger.debug("online file added to repository successfully.");
            
            mcAuthoringForm.resetUserAction();
	 		request.getSession().setAttribute(EDIT_OPTIONS_MODE, new Integer(2));
	 		logger.debug("setting EDIT_OPTIONS_MODE :" + 2);
			request.getSession().setAttribute(CURRENT_TAB, new Long(3));
	   	    return (mapping.findForward(ALL_INSTRUCTIONS));
        }
	 	else if (mcAuthoringForm.getViewFileItem() != null)
        {
	 		userAction="viewFileItem";
	 		request.setAttribute(USER_ACTION, userAction);
	 		logger.debug("userAction:" + userAction);
	 		
	 		String filename= request.getParameter("fileItem");
	 		logger.debug("filename:" + filename);
	 		
	 		String uuid=mcService.getFileUuid(filename);
	 		logger.debug("uuid:" + uuid);
	 		
	 		if (uuid == null)
	 		{
	 			ActionMessages errors= new ActionMessages();
        		errors= new ActionMessages();
				errors.add(Globals.ERROR_KEY,new ActionMessage("error.file.notPersisted"));
				saveErrors(request,errors);
    			mcAuthoringForm.resetUserAction();
				persistError(request,"error.file.notPersisted");
				
				request.getSession().setAttribute(EDIT_OPTIONS_MODE, new Integer(2));
		 		logger.debug("setting EDIT_OPTIONS_MODE :" + 2);
				request.getSession().setAttribute(CURRENT_TAB, new Long(3));
		   	    return (mapping.findForward(ALL_INSTRUCTIONS));
	 		}
	 		
	 		InputStream fileInputStream=mcService.downloadFile(new Long(uuid), null);
	 		logger.debug("fileInputStream:" + fileInputStream);
	 		
	 		DataInputStream dis = new DataInputStream(fileInputStream);
	 		logger.debug("dis:" + dis);
	 		
	 		String allFileText="";
	 		try {
	 			String input="";
	 			while ((input = dis.readLine()) != null) {
	 		        logger.debug("input:" + input);
	 		        allFileText = allFileText + input + "\r\n";
	 		    }    
	 		} catch (EOFException e) {
	 			logger.debug("error reading the file :" + e);
	 			logger.debug("error msg reading the file :" + e.getMessage());
	 		}
	 		
	 		logger.debug("allFileText:" + allFileText);
	 		request.getSession().setAttribute(FILE_CONTENT, allFileText);
	 		
	 		request.setAttribute(FILE_CONTENT_READY, new Integer(1));
	 		logger.debug("set FILE_CONTENT_READY to 1");
	 		
	 		request.getSession().setAttribute(FILE_NAME, filename);
	 		
	 		mcAuthoringForm.resetUserAction();
	 		request.getSession().setAttribute(EDIT_OPTIONS_MODE, new Integer(2));
	 		logger.debug("setting EDIT_OPTIONS_MODE :" + 2);
			request.getSession().setAttribute(CURRENT_TAB, new Long(3));
	   	    return (mapping.findForward(ALL_INSTRUCTIONS));
        }
	 	
	 	mcAuthoringForm.resetUserAction();
	 	return (mapping.findForward(LOAD_FILE_CONTENT));
    }

    
    protected void cleanupExistingQuestions(HttpServletRequest request, McContent mcContent)
    {
    	IMcService mcService =McUtils.getToolService(request);
    	logger.debug("remove questions by  mcQueContent uid : " + mcContent.getUid());
    	mcService.cleanAllQuestionsSimple(mcContent.getUid());
    }
    
    protected void persistQuestions(HttpServletRequest request, Map mapQuestionsContent, Map mapFeedbackIncorrect, Map mapFeedbackCorrect, McContent mcContent)
	{
    	IMcService mcService =McUtils.getToolService(request);
    	logger.debug("mapQuestionsContent to be persisted :" + mapQuestionsContent);
    	logger.debug("mapFeedbackIncorrect :" + mapFeedbackIncorrect);
    	logger.debug("mapFeedbackCorrect :" + mapFeedbackCorrect);

    	
  	    Map mapGeneralOptionsContent=(Map)request.getSession().getAttribute(MAP_GENERAL_OPTIONS_CONTENT);
		logger.debug("final MAP_GENERAL_OPTIONS_CONTENT :" + mapGeneralOptionsContent);

		Map mapGeneralSelectedOptionsContent=(Map)request.getSession().getAttribute(MAP_GENERAL_SELECTED_OPTIONS_CONTENT);
		logger.debug("final MAP_GENERAL_SELECTED_OPTIONS_CONTENT :" + mapGeneralSelectedOptionsContent);

    	Map mapWeights= repopulateMap(request, "questionWeight");
		request.getSession().setAttribute(MAP_WEIGHTS, mapWeights);
		System.out.print("MAP_WEIGHTS:" + request.getSession().getAttribute(MAP_WEIGHTS));
		
    	
    	Iterator itQuestionsMap = mapQuestionsContent.entrySet().iterator();
        while (itQuestionsMap.hasNext()) {
            Map.Entry pairs = (Map.Entry)itQuestionsMap.next();
            logger.debug("using the  pair: " +  pairs.getKey() + " = " + pairs.getValue());
            if ((pairs.getValue() != null) && (!pairs.getValue().equals("")))
            {
            	logger.debug("checking existing question text: " +  pairs.getValue().toString() + " and mcContent uid():" + mcContent.getUid());
            	
            	String currentFeedbackIncorrect=(String)mapFeedbackIncorrect.get(pairs.getKey());
        	 	logger.debug("currentFeedbackIncorrect: " +  currentFeedbackIncorrect);
        	 	if (currentFeedbackIncorrect == null) currentFeedbackIncorrect="";
        	 	
        	 	String currentFeedbackCorrect=(String)mapFeedbackCorrect.get(pairs.getKey());
        	 	logger.debug("currentFeedbackCorrect: " +  currentFeedbackCorrect);
        	 	if (currentFeedbackCorrect == null) currentFeedbackCorrect=""; 
            	
            	String currentWeight=(String) mapWeights.get(pairs.getKey().toString());
            	logger.debug("currentWeight: " + currentWeight);

            	McQueContent mcQueContent=  new McQueContent(pairs.getValue().toString(),
              	        	 		new Integer(pairs.getKey().toString()),
              	        	 		new Integer(currentWeight),
									false,
									currentFeedbackIncorrect,
									currentFeedbackCorrect,
									mcContent,
									new HashSet(),
									new HashSet()
             						);
       	        mcService.createMcQue(mcQueContent);
       	        logger.debug("persisted mcQueContent: " + mcQueContent);
           	
        	    logger.debug("remove existing options for  mcQueContent : " + mcQueContent.getUid());
           	    mcService.removeMcOptionsContentByQueId(mcQueContent.getUid());
           	    logger.debug("removed all mcOptionsContents for mcQueContentId :" + mcQueContent.getUid());
     			
       	         if (mcQueContent != null)
       	         {
       	         	logger.debug("pre persistOptions for: " + mcQueContent);
       	         	logger.debug("sending :" + pairs.getKey().toString());
       	         	persistOptions(request, mapGeneralOptionsContent, mapGeneralSelectedOptionsContent, mcQueContent, pairs.getKey().toString());
       	         	logger.debug("post persistOptions"); 	
       	         }
            }
        }
    	
	}
    
    
    protected void persistOptions(HttpServletRequest request, Map mapGeneralOptionsContent, Map mapGeneralSelectedOptionsContent, McQueContent mcQueContent, String questionIndex)
    {
    	IMcService mcService =McUtils.getToolService(request);
    	
    	logger.debug("passed questionIndex: " +  questionIndex);
    	
    	Iterator itOptionsMap = mapGeneralOptionsContent.entrySet().iterator();
    	while (itOptionsMap.hasNext()) {
            Map.Entry pairs = (Map.Entry)itOptionsMap.next();
            logger.debug("checking the general options  pair: " +  pairs.getKey() + " = " + pairs.getValue());
            if ((pairs.getValue() != null) && (!pairs.getValue().equals("")))
            {
            	Iterator itSelectedOptionsMap = mapGeneralSelectedOptionsContent.entrySet().iterator();    	
            	while (itSelectedOptionsMap.hasNext()) 
            	{
            		Map.Entry selectedPairs = (Map.Entry)itSelectedOptionsMap.next();
                    logger.debug("checking the general selected options pair: " +  selectedPairs.getKey() + " = " + selectedPairs.getValue());
                    
                    logger.debug("dubuging: " +  pairs.getKey() + "---" + questionIndex);
                    if (pairs.getKey().equals(selectedPairs.getKey())  && questionIndex.equals(pairs.getKey())) 
            		{
                    	logger.debug("using updated equal question: " +  pairs.getKey());
                    	Map currentOptions=(Map) pairs.getValue();
                    	Map selectedOptions=(Map) selectedPairs.getValue();
             
                    	persistOptionsFinal(request, currentOptions,selectedOptions,mcQueContent);
            		}
            	}
            }
        }
    }
    
    
    protected void persistOptionsFinal(HttpServletRequest request, Map currentOptions, Map selectedOptions, McQueContent mcQueContent)
    {
    	IMcService mcService =McUtils.getToolService(request);
    	
        logger.debug("passed currentOptions: " + currentOptions);
        logger.debug("passed selectedOptions: " + selectedOptions);
        
        Iterator itCurrentOptions = currentOptions.entrySet().iterator();
        
        
        boolean selected=false;
        while (itCurrentOptions.hasNext()) 
        {
            Map.Entry pairs = (Map.Entry)itCurrentOptions.next();
            logger.debug("checking the current options  pair: " +  pairs.getKey() + " = " + pairs.getValue());
            
            selected=false;
            Iterator itSelectedOptions = selectedOptions.entrySet().iterator();    
            while (itSelectedOptions.hasNext())
            {
            	Map.Entry selectedPairs = (Map.Entry)itSelectedOptions.next();
            	logger.debug("checking the selected options  pair: " +  selectedPairs.getKey() + " = " + selectedPairs.getValue());
            	selected=false;
            	if (pairs.getValue().equals(selectedPairs.getValue()))
            	{
            		selected=true;
            		logger.debug("set selected to true for: " + pairs.getValue());
            		break;
            	}
            }
            
            logger.debug("pre-persist mcOptionsContent: " + pairs.getValue() + " " + selected);
            logger.debug("pre-persist mcOptionsContent, using mcQueContent: " + mcQueContent);
            McOptsContent mcOptionsContent= new McOptsContent(selected,pairs.getValue().toString() , mcQueContent, new HashSet());
	        logger.debug("created mcOptionsContent: " + mcOptionsContent);
   	        mcService.saveMcOptionsContent(mcOptionsContent);
   	        logger.debug("persisted the answer: " + pairs.getValue().toString());
        }
    }
    
    protected McContent createContent(HttpServletRequest request, McAuthoringForm mcAuthoringForm)
    {
    	IMcService mcService =McUtils.getToolService(request);
    	
    	/* the tool content id is passed from the container to the tool and placed into session in the McStarterAction */
    	Long toolContentId=(Long)request.getSession().getAttribute(TOOL_CONTENT_ID);
    	if ((toolContentId != null) && (toolContentId.longValue() != 0))
    	{
    		logger.debug("passed TOOL_CONTENT_ID : " + toolContentId);
    		/*delete the existing content in the database before applying new content*/
    		mcService.deleteMcById(toolContentId);  
    		logger.debug("post-deletion existing content");
		}
    	
		String title;
    	String instructions;
    	Long createdBy;
    	String monitoringReportTitle="";
    	String reportTitle="";
    	
    	String offlineInstructions="";
    	String onlineInstructions="";
    	String endLearningMessage="";
    	String creationDate="";
    	int passmark=0;
    	
    	boolean isQuestionsSequenced=false;
    	boolean isSynchInMonitor=false;
    	boolean isUsernameVisible=false;
    	boolean isRunOffline=false;
    	boolean isDefineLater=false;
    	boolean isContentInUse=false;
    	boolean isRetries=false;
    	boolean isShowFeedback=false;
    	
    	
    	logger.debug("isQuestionsSequenced: " +  mcAuthoringForm.getQuestionsSequenced());
    	if (mcAuthoringForm.getQuestionsSequenced().equalsIgnoreCase(ON))
    		isQuestionsSequenced=true;
    	
    	logger.debug("isSynchInMonitor: " +  mcAuthoringForm.getSynchInMonitor());
    	if (mcAuthoringForm.getSynchInMonitor().equalsIgnoreCase(ON))
    		isSynchInMonitor=true;
    	
    	logger.debug("isUsernameVisible: " +  mcAuthoringForm.getUsernameVisible());
    	if (mcAuthoringForm.getUsernameVisible().equalsIgnoreCase(ON))
    		isUsernameVisible=true;
    	
    	logger.debug("isRetries: " +  mcAuthoringForm.getRetries());
    	if (mcAuthoringForm.getRetries().equalsIgnoreCase(ON))
    		isRetries=true;
    	
    	logger.debug("passmark: " +  mcAuthoringForm.getPassmark());
    	if ((mcAuthoringForm.getPassmark() != null) && (mcAuthoringForm.getPassmark().length() > 0)) 
    		passmark= new Integer(mcAuthoringForm.getPassmark()).intValue();
    	
    	logger.debug("isShowFeedback: " +  mcAuthoringForm.getShowFeedback());
    	if (mcAuthoringForm.getShowFeedback().equalsIgnoreCase(ON))
    		isShowFeedback=true;
    	    	
    	logger.debug("MONITORING_REPORT_TITLE: " +  mcAuthoringForm.getMonitoringReportTitle());
    	monitoringReportTitle=mcAuthoringForm.getMonitoringReportTitle();
    	if ((monitoringReportTitle == null) || (monitoringReportTitle.length() == 0)) 
    		monitoringReportTitle=(String)request.getSession().getAttribute(MONITORING_REPORT_TITLE);
    	
    	reportTitle=mcAuthoringForm.getReportTitle();
    	logger.debug("REPORT_TITLE: " +  mcAuthoringForm.getReportTitle());
    	if ((reportTitle == null) || (reportTitle.length() == 0)) 
    		reportTitle=(String)request.getSession().getAttribute(REPORT_TITLE);
    	
    			
    	endLearningMessage=mcAuthoringForm.getEndLearningMessage();
    	logger.debug("END_LEARNING_MESSAGE: " +  mcAuthoringForm.getEndLearningMessage());
    	if ((endLearningMessage == null) || (endLearningMessage.length() == 0))
    		endLearningMessage=(String)request.getSession().getAttribute(END_LEARNING_MESSAGE);

    	String richTextTitle="";
    	richTextTitle = (String)request.getSession().getAttribute(RICHTEXT_TITLE);
    	logger.debug("createContent richTextTitle from session: " + richTextTitle);
    	if (richTextTitle == null) richTextTitle="";
    	
    	String richTextInstructions="";
    	richTextInstructions = (String)request.getSession().getAttribute(RICHTEXT_INSTRUCTIONS);
    	logger.debug("createContent richTextInstructions from session: " + richTextInstructions);
    	if (richTextInstructions == null) richTextInstructions="";

    	String richTextOfflineInstructions="";
    	richTextOfflineInstructions = (String)request.getSession().getAttribute(RICHTEXT_OFFLINEINSTRUCTIONS);
    	logger.debug("createContent richTextOfflineInstructions from session: " + richTextOfflineInstructions);
    	if (richTextOfflineInstructions == null) richTextOfflineInstructions="";
    	
    	String richTextOnlineInstructions="";
    	richTextOnlineInstructions = (String)request.getSession().getAttribute(RICHTEXT_ONLINEINSTRUCTIONS);
    	logger.debug("createContent richTextOnlineInstructions from session: " + richTextOnlineInstructions);
    	if (richTextOnlineInstructions == null) richTextOnlineInstructions="";
    	
    	creationDate=(String)request.getSession().getAttribute(CREATION_DATE);
		if (creationDate == null)
			creationDate=new Date(System.currentTimeMillis()).toString();
		
    		
    	/*obtain user object from the session*/
	    HttpSession ss = SessionManager.getSession();
	    /* get back login user DTO */
	    UserDTO toolUser = (UserDTO) ss.getAttribute(AttributeNames.USER);
    	logger.debug("retrieving toolUser: " + toolUser);
    	logger.debug("retrieving toolUser userId: " + toolUser.getUserID());
    	String fullName= toolUser.getFirstName() + " " + toolUser.getLastName();
    	logger.debug("retrieving toolUser fullname: " + fullName);
    	long userId=toolUser.getUserID().longValue();
    	logger.debug("userId: " + userId);
    	
    	/* create a new qa content and leave the default content intact*/
    	McContent mc = new McContent();
		mc.setMcContentId(toolContentId);
		mc.setTitle(richTextTitle);
		mc.setInstructions(richTextInstructions);
		mc.setCreationDate(creationDate); /*preserve this from the db*/ 
		mc.setUpdateDate(new Date(System.currentTimeMillis())); /* keep updating this one*/
		mc.setCreatedBy(userId); /* make sure we are setting the userId from the User object above*/
	    mc.setUsernameVisible(isUsernameVisible);
	    mc.setQuestionsSequenced(isQuestionsSequenced); /* the default question listing in learner mode will be all in the same page*/
	    mc.setOnlineInstructions(richTextOnlineInstructions);
	    mc.setOfflineInstructions(richTextOfflineInstructions);
	    mc.setRunOffline(false);
	    mc.setDefineLater(false);
	    mc.setSynchInMonitor(isSynchInMonitor);
	    mc.setContentInUse(isContentInUse);
	    mc.setEndLearningMessage(endLearningMessage);
	    mc.setRunOffline(isRunOffline);
	    mc.setReportTitle(reportTitle);
	    mc.setMonitoringReportTitle(monitoringReportTitle);
	    mc.setEndLearningMessage(endLearningMessage);
	    mc.setRetries(isRetries);
	    mc.setPassMark(new Integer(passmark));
	    mc.setShowFeedback(isShowFeedback);
	    mc.setMcQueContents(new TreeSet());
	    mc.setMcSessions(new TreeSet());
	    logger.debug("mc content :" +  mc);
    	
    	/*create the content in the db*/
        mcService.createMc(mc);
        logger.debug("mc created with content id: " + toolContentId);
        
        return mc;
    }
    
    
    protected void addQuestionMemory(HttpServletRequest request, McAuthoringForm mcAuthoringForm, Map mapQuestionsContent, boolean increaseMapSize)
    {
    	if (increaseMapSize)
    	{
    		int mapSize=mapQuestionsContent.size();
        	mapQuestionsContent.put(new Long(++mapSize).toString(), "");
        	logger.debug("updated Questions Map size: " + mapQuestionsContent.size());
        	request.getSession().setAttribute(MAP_QUESTIONS_CONTENT, mapQuestionsContent);
        	logger.debug("updated Questions Map: " + request.getSession().getAttribute(MAP_QUESTIONS_CONTENT));
        	
        	Map mapWeights = (Map) request.getSession().getAttribute(MAP_WEIGHTS);
        	logger.debug("current mapWeights: " + mapWeights);
        	int mapWeightsSize=mapWeights.size();
        	mapWeights.put(new Long(++mapWeightsSize).toString(), "");
        	logger.debug("updated mapWeights size: " + mapWeights.size());
        	request.getSession().setAttribute(MAP_WEIGHTS, mapWeights);
        	logger.debug("updated mapWeights: " + request.getSession().getAttribute(MAP_WEIGHTS));
    	}
    }
    
    protected void addQuestion(HttpServletRequest request, McAuthoringForm mcAuthoringForm, Map mapQuestionsContent, boolean increaseMapSize)
    {
    	IMcService mcService =McUtils.getToolService(request);
    	logger.debug("increaseMapSize: " +  increaseMapSize);
    	
    	if (increaseMapSize)
    	{
    		int mapSize=mapQuestionsContent.size();
        	mapQuestionsContent.put(new Long(++mapSize).toString(), "");
        	logger.debug("updated Questions Map size: " + mapQuestionsContent.size());
        	request.getSession().setAttribute(MAP_QUESTIONS_CONTENT, mapQuestionsContent);
        	logger.debug("updated Questions Map: " + request.getSession().getAttribute(MAP_QUESTIONS_CONTENT));
        	
        	Map mapWeights = (Map) request.getSession().getAttribute(MAP_WEIGHTS);
        	logger.debug("current mapWeights: " + mapWeights);
        	int mapWeightsSize=mapWeights.size();
        	mapWeights.put(new Long(++mapWeightsSize).toString(), "");
        	logger.debug("updated mapWeights size: " + mapWeights.size());
        	request.getSession().setAttribute(MAP_WEIGHTS, mapWeights);
        	logger.debug("updated mapWeights: " + request.getSession().getAttribute(MAP_WEIGHTS));
    	}
    	
    	
    	McContent mcContent=null;
    	Long toolContentId=(Long)request.getSession().getAttribute(TOOL_CONTENT_ID);
    	if ((toolContentId != null) && (toolContentId.longValue() != 0))
    	{
    		logger.debug("TOOL_CONTENT_ID : " + toolContentId);
    		mcContent= mcService.retrieveMc(toolContentId);  
    		logger.debug("mcContent: " + mcContent);
		}
    	
    	if (mcContent == null)
    	{
    		mcContent=createContent(request, mcAuthoringForm);
        	logger.debug("mcContent: " + mcContent);	
    	}
    	
    	Map mapWeights=repopulateCurrentWeightsMap(request,"questionWeight");
		logger.debug("mapWeights for add Question: " + mapWeights);
		
    	/* iterate the questions Map and persist the questions into the DB*/
    	Iterator itQuestionsMap = mapQuestionsContent.entrySet().iterator();
        while (itQuestionsMap.hasNext()) {
            Map.Entry pairs = (Map.Entry)itQuestionsMap.next();
            logger.debug("adding the  pair: " +  pairs.getKey() + " = " + pairs.getValue());
            if ((pairs.getValue() != null) && (!pairs.getValue().equals("")))
            {
            	logger.debug("checking existing question text: " +  pairs.getValue().toString() + " and mcContent uid():" + mcContent.getUid());
        	 	McQueContent mcQueContent=mcService.getQuestionContentByQuestionText(pairs.getValue().toString(), mcContent.getUid());
        	 	
        	 	logger.debug("mcQueContent: " +  mcQueContent);
        	 	Integer currentWeight= new Integer(mapWeights.get(pairs.getKey()).toString());
            	logger.debug("currentWeight:" + currentWeight);
        	 	
            	if (mcQueContent == null)
            	{
            		mcQueContent=  new McQueContent(pairs.getValue().toString(),
              	        	 		new Integer(pairs.getKey().toString()),
              	        	 		currentWeight,
									true,
									mcContent,
									new HashSet(),
									new HashSet()
             						);
           	        mcService.createMcQue(mcQueContent);
           	        logger.debug("persisted mcQueContent: " + mcQueContent);
            	}
            	else
            	{
            		logger.debug("is mcQueContent disabled: " +  mcQueContent.isDisabled());
            	}
            	
            }
        }
                
    }
    
    
    protected Map rebuildQuestionMapfromDB(HttpServletRequest request)
    {
    	Map mapQuestionsContent= new TreeMap(new McComparator());
    	
    	IMcService mcService =McUtils.getToolService(request);
    	Long toolContentId=(Long)request.getSession().getAttribute(TOOL_CONTENT_ID);
		logger.debug("toolContentId:" + toolContentId);

		McContent mcContent=mcService.retrieveMc(toolContentId);
		logger.debug("mcContent:" + mcContent);
		
    	List list=mcService.getAllQuestionEntries(mcContent.getUid());
    	logger.debug("list:" + list);
		
		Iterator listIterator=list.iterator();
    	Long mapIndex=new Long(1);
    	while (listIterator.hasNext())
    	{
    		McQueContent mcQueContent=(McQueContent)listIterator.next();
    		logger.debug("mcQueContent:" + mcQueContent);
    		mapQuestionsContent.put(mapIndex.toString(),mcQueContent.getQuestion());
    		mapIndex=new Long(mapIndex.longValue()+1);
    	}
    	
    	logger.debug("refreshed Map:" + mapQuestionsContent);
    	return mapQuestionsContent;
    }
    
    
    protected void removeRedundantQuestionEntries(HttpServletRequest request, Map mapQuestionsContent)
    {
    	IMcService mcService =McUtils.getToolService(request);
    	
    	logger.debug("main Map mapQuestionsContent:" + mapQuestionsContent);
    	
    	Long toolContentId=(Long)request.getSession().getAttribute(TOOL_CONTENT_ID);
		logger.debug("toolContentId:" + toolContentId);
		
		McContent mcContent=mcService.retrieveMc(toolContentId);
		logger.debug("mcContent:" + mcContent);
		
		if (mcContent != null)
		{
			List allQuestions=mcService.getAllQuestionEntries(mcContent.getUid());
	    	logger.debug("allQuestions:" + allQuestions);
	    	
	    	Iterator listIterator=allQuestions.iterator();
	    	
	    	while (listIterator.hasNext())
	    	{
	    		McQueContent mcQueContent=(McQueContent)listIterator.next();
	    		logger.debug("mcQueContent:" + mcQueContent);
	    		
	    		Iterator itQuestionsMap = mapQuestionsContent.entrySet().iterator();
	    		boolean matchFound=false;
	            while (itQuestionsMap.hasNext()) {
	            	Map.Entry pairs = (Map.Entry)itQuestionsMap.next();
	                logger.debug("comparing the  pair: " +  pairs.getKey() + " = " + pairs.getValue());
	                
	                if (pairs.getValue().toString().equals(mcQueContent.getQuestion()))
					{
	                    logger.debug("match found  the  pair: " + pairs.getValue().toString());
	                	matchFound=true;
	            	}
	            }
	            
	            if (matchFound == false)
	            {
	            	mcService.removeMcQueContent(mcQueContent);
	                logger.debug("removed mcQueContent: " + mcQueContent);
	            }
	    	}
		}
    }
    
    protected boolean validateOptions(HttpServletRequest request)
    {
    	logger.debug("will validateOptions");
    	String parameterType="checkBoxSelected";
    	for (int i=1; i <= MAX_OPTION_COUNT ; i++)
    	{
    		String isCorrect=request.getParameter(parameterType + i);
    		logger.debug("isCorrect: " + isCorrect);
    		
    		if (isCorrect != null)
    		{
    			if (isCorrect.equals("Correct"))
        			return true;	
    		}
    	}
    	return false;
    }
	
    
    protected boolean validateSubTotalWeights(HttpServletRequest request, McAuthoringForm mcAuthoringForm)
    {
    	Map mapWeights= repopulateCurrentWeightsMap(request, "questionWeight");
    	logger.debug("mapWeights: " + mapWeights);
    	
    	int totalWeight=0;
    	Iterator itMap = mapWeights.entrySet().iterator();
		while (itMap.hasNext()) {
        	Map.Entry pairs = (Map.Entry)itMap.next();
            logger.debug("using the  pair: " +  pairs.getKey() + " = " + pairs.getValue());
            
            int currentWeight=new Integer(pairs.getValue().toString()).intValue();
            totalWeight= totalWeight + currentWeight;
            logger.debug("sub totalWeight: " +  totalWeight);
		}
        logger.debug("final totalWeight: " +  totalWeight);
        
        if (totalWeight > 100)
        	return false;
        else
        	return true;
    }
    
    protected boolean validateQuestionWeights(HttpServletRequest request, McAuthoringForm mcAuthoringForm)
    {
    	Map mapWeights= repopulateCurrentWeightsMap(request, "questionWeight");
    	logger.debug("mapWeights: " + mapWeights);
    	
    	Iterator itMap = mapWeights.entrySet().iterator();
		while (itMap.hasNext()) {
        	Map.Entry pairs = (Map.Entry)itMap.next();
            logger.debug("using the  pair: " +  pairs.getKey() + " = " + pairs.getValue());
            
            if ((pairs.getValue() == null) || (pairs.getValue().toString().length() == 0)) 
            {
            	ActionMessages errors= new ActionMessages();
        		errors= new ActionMessages();
				errors.add(Globals.ERROR_KEY,new ActionMessage("error.weights.empty"));
				saveErrors(request,errors);
    			mcAuthoringForm.resetUserAction();
				persistError(request,"error.weights.empty");
				return false;
            }

    		try
			{
    			int weight= new Integer(pairs.getValue().toString()).intValue();
        		logger.debug("tried weight: " +  weight);
			}
        	catch(Exception e)
			{
        		ActionMessages errors= new ActionMessages();
        		errors= new ActionMessages();
				errors.add(Globals.ERROR_KEY,new ActionMessage("error.weights.notInteger"));
				saveErrors(request,errors);
    			mcAuthoringForm.resetUserAction();
				persistError(request,"error.weights.notInteger");
				return false;
			}
        	
        	
        	int weight= new Integer(pairs.getValue().toString()).intValue();
        	if (weight == 0) 
            {
            	ActionMessages errors= new ActionMessages();
        		errors= new ActionMessages();
				errors.add(Globals.ERROR_KEY,new ActionMessage("error.weights.zero"));
				saveErrors(request,errors);
    			mcAuthoringForm.resetUserAction();
				persistError(request,"error.weights.zero");
				return false;
            }
        }
		mcAuthoringForm.resetUserAction();
		return true;
    }
    
    protected boolean validateTotalWeight(HttpServletRequest request)
    {
    	Map mapWeights= repopulateCurrentWeightsMap(request, "questionWeight");
    	logger.debug("mapWeights: " + mapWeights);
    	
    	Iterator itMap = mapWeights.entrySet().iterator();
    	int totalWeight=0;
		while (itMap.hasNext()) {
        	Map.Entry pairs = (Map.Entry)itMap.next();
            logger.debug("using the  pair: " +  pairs.getKey() + " = " + pairs.getValue());
            
            if ((pairs.getValue() != null) || (pairs.getValue().toString().length() > 0)) 
            {
            	totalWeight=totalWeight+ new Integer(pairs.getValue().toString()).intValue();
            }
		}

            logger.debug("totalWeight: " +  totalWeight);
            
        if (totalWeight != 100)
        {
        	return false;
        }

        return true;
    }

    
    protected Map shiftMap(Map mapQuestionsContent, String questionIndex , String movableQuestionEntry, String direction)
    {
    	logger.debug("movableQuestionEntry: " +  movableQuestionEntry);
    	/* map to be returned */
    	Map mapTempQuestionsContent= new TreeMap(new McComparator());
    	
    	Iterator itMap = mapQuestionsContent.entrySet().iterator();
    	String shiftableEntry=null;
    	
    	int shiftableIndex=0;
    	if (direction.equals("down"))
        {
    		logger.debug("moving map down");
    		shiftableIndex=new Integer(questionIndex).intValue() + 1;
        }
    	else
    	{
    		logger.debug("moving map up");
    		shiftableIndex=new Integer(questionIndex).intValue() - 1;
        
    	}
    		
		logger.debug("shiftableIndex: " +  shiftableIndex);
    	shiftableEntry=(String)mapQuestionsContent.get(new Integer(shiftableIndex).toString());
    	logger.debug("shiftable entry: " +  shiftableEntry);
    	
    	if (shiftableEntry != null) 
    	{
    		Iterator itQuestionsMap = mapQuestionsContent.entrySet().iterator();
    		long mapCounter=0;
    		while (itQuestionsMap.hasNext()) {
            	Map.Entry pairs = (Map.Entry)itQuestionsMap.next();
                logger.debug("comparing the  pair: " +  pairs.getKey() + " = " + pairs.getValue());
                mapCounter++;
                logger.debug("mapCounter: " +  mapCounter);
                
                if (!pairs.getKey().equals(questionIndex) && !pairs.getKey().equals(new Integer(shiftableIndex).toString()))
                {
                	logger.debug("normal copy " +  questionIndex);
                	mapTempQuestionsContent.put(new Long(mapCounter).toString(), pairs.getValue());
                }
                else if (pairs.getKey().equals(questionIndex))
                {
                	logger.debug("move type 1 " +  questionIndex);
                	mapTempQuestionsContent.put(new Long(mapCounter).toString(), shiftableEntry);
                }
                else if (pairs.getKey().equals(new Integer(shiftableIndex).toString()))
                {
                	logger.debug("move type 2 " +  shiftableIndex);
                	mapTempQuestionsContent.put(new Long(mapCounter).toString(), movableQuestionEntry);
                }
            }
    	}
    	else
    	{
    		logger.debug("no change to map");
    		mapTempQuestionsContent=mapQuestionsContent;
    	}
    		return mapTempQuestionsContent;

    }
    
    
    /**
     * shrinks the size of the Map to only used entries
     * 
     * @param mapQuestionContent
     * @param request
     * @return
     */
    protected Map repopulateMap(HttpServletRequest request, String parameterType)
    {
    	Map mapTempQuestionsContent= new TreeMap(new McComparator());
    	logger.debug("parameterType: " + parameterType);
    	
    	long mapCounter=0;
    	for (long i=1; i <= MAX_QUESTION_COUNT ; i++)
		{
			String candidateEntry =request.getParameter(parameterType + i);
			if (
				(candidateEntry != null) && 
				(candidateEntry.length() > 0)   
				)
			{
				mapCounter++;
				mapTempQuestionsContent.put(new Long(mapCounter).toString(), candidateEntry);
			}
		}
    	logger.debug("return repopulated Map: " + mapTempQuestionsContent);
    	return mapTempQuestionsContent;
    }
    
	
    protected Map repopulateCurrentWeightsMap(HttpServletRequest request, String parameterType)
    {
    	Map mapTempQuestionsContent= new TreeMap(new McComparator());
    	logger.debug("parameterType: " + parameterType);
    	
    	long mapCounter=0;
    	for (long i=1; i <= MAX_QUESTION_COUNT ; i++)
		{
			String candidateEntry =request.getParameter(parameterType + i);
			String questionText =request.getParameter("questionContent" + i);
			if ((questionText != null) && (questionText.length() > 0))
			{
				logger.debug("questionText: " + questionText);
				if (candidateEntry != null)
				{
					mapCounter++;
					mapTempQuestionsContent.put(new Long(mapCounter).toString(), candidateEntry);
				}	
			}
			
		}
    	logger.debug("return repopulated Map: " + mapTempQuestionsContent);
    	return mapTempQuestionsContent;
    }

    
    protected boolean verifyDuplicatesOptionsMap(Map mapOptionsContent)
	{
    	Map originalMapOptionsContent=mapOptionsContent;
    	Map backupMapOptionsContent=mapOptionsContent;
    	
    	int optionCount=0;
    	for (long i=1; i <= MAX_OPTION_COUNT ; i++)
		{
    		String currentOption=(String)originalMapOptionsContent.get(new Long(i).toString());
    		logger.debug("verified currentOption  " + currentOption);
    		
    		optionCount=0;
    		for (long j=1; j <= MAX_OPTION_COUNT ; j++)
    		{
        		String backedOption=(String)backupMapOptionsContent.get(new Long(j).toString());
        		
        		if ((currentOption != null) && (backedOption !=null))
        		{
        			if (currentOption.equals(backedOption))
    				{
    					optionCount++;
    			    	logger.debug("optionCount for  " + currentOption + " is: " + optionCount);
    				}
    				
            		if (optionCount > 1)
            			return false;	
        		}
    		}	
		}
    	return true;
	}
    
    
    protected boolean validateQuestionsNotEmpty(Map mapQuestionsContent)
    {
    	Iterator itMap = mapQuestionsContent.entrySet().iterator();
    	while (itMap.hasNext()) {
        	Map.Entry pairs = (Map.Entry)itMap.next();
            logger.debug("using the  pair: " +  pairs.getKey() + " = " + pairs.getValue());
            
            if ((pairs.getValue() != null) && (pairs.getValue().toString().length() == 0))
            	return false;
            
		}
    	return true;
    }
    
    
    protected Map repopulateCurrentCheckBoxStatesMap(HttpServletRequest request)
    {
    	Map mapTempContent= new TreeMap(new McComparator());
    	
    	long mapCounter=0;
    	for (long i=1; i <= MAX_OPTION_COUNT ; i++)
		{
			String candidateEntry =request.getParameter("checkBoxSelected" + i);
			String optionText =request.getParameter("optionContent" + i);
			
			if (
					(candidateEntry != null) && 
					(candidateEntry.length() > 0)   
					)
				{
					if (candidateEntry.equals("Correct"))
					{
						mapCounter++;
						mapTempContent.put(new Long(mapCounter).toString(), optionText);
					}
				}
		}
    	logger.debug("return repopulated currentCheckBoxStatesMap: " + mapTempContent);
    	return mapTempContent;
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

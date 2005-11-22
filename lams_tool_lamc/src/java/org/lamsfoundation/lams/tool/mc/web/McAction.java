/* *Copyright (C) 2005 LAMS Foundation (http://lamsfoundation.org)
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
import org.lamsfoundation.lams.tool.mc.McApplicationException;
import org.lamsfoundation.lams.tool.mc.McComparator;
import org.lamsfoundation.lams.tool.mc.McContent;
import org.lamsfoundation.lams.tool.mc.McOptsContent;
import org.lamsfoundation.lams.tool.mc.McQueContent;
import org.lamsfoundation.lams.tool.mc.McQueUsr;
import org.lamsfoundation.lams.tool.mc.McUsrAttempt;
import org.lamsfoundation.lams.tool.mc.McUtils;
import org.lamsfoundation.lams.tool.mc.service.IMcService;
import org.lamsfoundation.lams.usermanagement.dto.UserDTO;
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
 
	<action
		path="/authoring"
		type="org.lamsfoundation.lams.tool.mc.web.McAction"
		name="McAuthoringForm"
		scope="session"
		input=".questions"
		parameter="method"
		unknown="false"
		validate="true"
	>
	<forward
		  name="load"
		  path=".questions"
		  redirect="true"
	/>
	
	<forward
		  name="starter"
		  path=".starter"
		  redirect="true"
	/>
	
	<forward
		  name="editOptsContent"
		  path=".editOptsContent"
		  redirect="true"
	/>
	
	 <forward
		  name="allInstructions"
		  path=".allInstructions"
		  redirect="true"
	/>
	</action>
*/
public class McAction extends DispatchAction implements McAppConstants
{
	/*
	 * change the logic about completion status

	 * once lams_learning is ready and appContext file is src/ then FINISH toool session will work.
	 * 
	 * when to reset define later and synchin monitor etc..
	 *
	 * make sure the tool gets called on:
	 * setAsForceComplete(Long userId) throws McApplicationException 
	 * 
	 * Note: the tool must support deletion of an existing content from within the authoring environment.
	 * The current support for this is by implementing the tool contract : removeToolContent(Long toolContentId)
	 */
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
	 	McAuthoringForm mcAuthoringForm = (McAuthoringForm) form;
	 	IMcService mcService =McUtils.getToolService(request);

	 	/** define the next tab as Basic tab by default*/
	 	request.getSession().setAttribute(EDIT_OPTIONS_MODE, new Integer(0));
		logger.debug("resetting  EDIT_OPTIONS_MODE to 0");
	 	
	 	McUtils.persistRichText(request);
	 	populateParameters(request, mcAuthoringForm);
	 	
		String userAction=null;
	 	if (mcAuthoringForm.getAddQuestion() != null)
	 	{
	 		return addNewQuestion(request, mcAuthoringForm, mapping);
	 	}
	 	else if (mcAuthoringForm.getRemoveQuestion() != null)
	 	{
	 		return removeQuestion(request, mcAuthoringForm, mapping);
	 	}
	 	else if (mcAuthoringForm.getEditOptions() != null)
	 	{
	 	    return editOptions(request,mcAuthoringForm, mapping);
	 	}
	 	else if (mcAuthoringForm.getMoveDown() != null)
	 	{
	 		return moveQuestionDown(request, mcAuthoringForm, mapping);
	 	}
	 	else if (mcAuthoringForm.getMoveUp() != null)
	 	{
	 		return moveQuestionUp(request, mcAuthoringForm, mapping);
	 	}
	 	else if (mcAuthoringForm.getAddOption() != null)
	 	{
	 		return addOption(request, mcAuthoringForm, mapping);
	 	}
	 	else if (mcAuthoringForm.getRemoveOption() != null)
	 	{
	 		return removeOption(request, mcAuthoringForm, mapping);
	 	}
	 	else if (mcAuthoringForm.getDoneOptions() != null)
	 	{
	 		return doneOptions(request, mcAuthoringForm, mapping);
	 	}
	 	else if (mcAuthoringForm.getSubmitQuestions() != null)
	 	{
	 		return submitQuestions(request, mcAuthoringForm, mapping);
	 	}
	 	else if (mcAuthoringForm.getAdvancedTabDone() != null)
	 	{
	 		return doneAdvancedTab(request, mcAuthoringForm, mapping);
	 	}
	 	else if (mcAuthoringForm.getInstructionsTabDone() != null)
	 	{
	 		return doneInstructionsTab(request, mcAuthoringForm, mapping);
	 	} 
	 	else if (mcAuthoringForm.getSubmitOfflineFile() != null)
        {
	 		return submitOfflineFiles(request, mcAuthoringForm, mapping);
        }
	 	else if (mcAuthoringForm.getSubmitOnlineFile() != null)
        {
	 		return submitOnlineFiles(request, mcAuthoringForm, mapping);
        }
	 	else if (mcAuthoringForm.getViewFileItem() != null)
        {
	 		return viewFileItem(request, mcAuthoringForm, mapping);
        }
	 	
	 	mcAuthoringForm.resetUserAction();
	 	return null;
    }

    
    /**
     * populates request parameters
     * populateParameters(HttpServletRequest request, McAuthoringForm mcAuthoringForm)
     * 
     * @param request
     * @param mcAuthoringForm
     */
    protected void populateParameters(HttpServletRequest request, McAuthoringForm mcAuthoringForm)
    {
        String selectedQuestion=request.getParameter(SELECTED_QUESTION);
    	logger.debug("read parameter selectedQuestion: " + selectedQuestion);
    	
    	if ((selectedQuestion != null) && (selectedQuestion.length() > 0))
    	{
    		request.getSession().setAttribute(SELECTED_QUESTION,selectedQuestion);
    		logger.debug("updated SELECTED_QUESTION");
    	}
    	
    	
    	mcAuthoringForm.setRemoveQuestion(null);
    	mcAuthoringForm.setEditOptions(null);
    	mcAuthoringForm.setMoveUp(null);
    	mcAuthoringForm.setMoveDown(null);
    	mcAuthoringForm.setRemoveOption(null);
    	mcAuthoringForm.setViewFileItem(null);
    	
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
    }


    /**
     * adds a new entry to the questions Map
     * addNewQuestion(HttpServletRequest request, McAuthoringForm mcAuthoringForm, ActionMapping mapping)
     * 
     * @param request
     * @param mcAuthoringForm
     * @param mapping
     * @return ActionForward
     */
    protected ActionForward addNewQuestion(HttpServletRequest request, McAuthoringForm mcAuthoringForm, ActionMapping mapping)
    {
    	String userAction="addQuestion";
 		request.setAttribute(USER_ACTION, userAction);
 		logger.debug("userAction:" + userAction);
 		
    	Map mapQuestionsContent=AuthoringUtil.repopulateMap(request, "questionContent");
     	logger.debug("mapQuestionsContent after shrinking: " + mapQuestionsContent);
     	logger.debug("mapQuestionsContent size after shrinking: " + mapQuestionsContent.size());
     	
     	logger.debug("will validate questions are not empty");
    	boolean questionsNotEmptyValid=AuthoringUtil.validateQuestionsNotEmpty(mapQuestionsContent);
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
    		
        	mcAuthoringForm.setAddQuestion(null);
    		mcAuthoringForm.setSubmitQuestions(null);
    		return (mapping.findForward(LOAD_QUESTIONS));
    	}
    		
    	logger.debug("will validate weights");
    	boolean weightsValid=validateQuestionWeights(request,mcAuthoringForm);
    	logger.debug("weightsValid:" + weightsValid);
    	if (weightsValid == false)
    	{
    		mcAuthoringForm.resetUserAction();
    		
    		int maxQuestionIndex=mapQuestionsContent.size();
    		request.getSession().setAttribute(MAX_QUESTION_INDEX, new Integer(maxQuestionIndex));
    		logger.debug("MAX_QUESTION_INDEX: " +  request.getSession().getAttribute(MAX_QUESTION_INDEX));
    		
        	mcAuthoringForm.setAddQuestion(null);
    		mcAuthoringForm.setSubmitQuestions(null);
    		return (mapping.findForward(LOAD_QUESTIONS));
    	}
    	
    	
    	logger.debug("will validate SubTotalWeights");
    	boolean subWeightsValid=AuthoringUtil.validateSubTotalWeights(request,mcAuthoringForm);
    	logger.debug("subWeightsValid:" + subWeightsValid);
    	if (subWeightsValid == false)
    	{
    		ActionMessages errors= new ActionMessages();
    		errors= new ActionMessages();
    		errors.add(Globals.ERROR_KEY,new ActionMessage("error.question.weight.total"));
    		saveErrors(request,errors);
    		mcAuthoringForm.resetUserAction();
    		persistError(request,"error.question.weight.total");
    		
    		
    		int maxQuestionIndex=mapQuestionsContent.size();
    		request.getSession().setAttribute(MAX_QUESTION_INDEX, new Integer(maxQuestionIndex));
    		logger.debug("MAX_QUESTION_INDEX: " +  request.getSession().getAttribute(MAX_QUESTION_INDEX));
    		
        	mcAuthoringForm.setAddQuestion(null);
    		mcAuthoringForm.setSubmitQuestions(null);
    		return (mapping.findForward(LOAD_QUESTIONS));
    	}
    		
    		
     	Map mapWeights= AuthoringUtil.repopulateMap(request, "questionWeight");
    	request.getSession().setAttribute(MAP_WEIGHTS, mapWeights);
    	System.out.print("MAP_WEIGHTS:" + request.getSession().getAttribute(MAP_WEIGHTS));
    	
    	addQuestionMemory(request, mcAuthoringForm, mapQuestionsContent, true);
    	logger.debug("after addQuestionMemory");
    	
    	request.getSession().setAttribute(EDIT_OPTIONS_MODE, new Integer(0));
    	logger.debug("resetting  EDIT_OPTIONS_MODE to 0");
        mcAuthoringForm.resetUserAction();
        
        int maxQuestionIndex=mapQuestionsContent.size();
    	request.getSession().setAttribute(MAX_QUESTION_INDEX, new Integer(maxQuestionIndex));
    	logger.debug("MAX_QUESTION_INDEX: " +  request.getSession().getAttribute(MAX_QUESTION_INDEX));
    	
    	mcAuthoringForm.setAddQuestion(null);
		mcAuthoringForm.setSubmitQuestions(null);
    	return (mapping.findForward(LOAD_QUESTIONS));	
    }

    /**
     * removes an entry from the questions Map
     * 
     * removeQuestion(HttpServletRequest request, McAuthoringForm mcAuthoringForm, ActionMapping mapping)
     * @param request
     * @param mcAuthoringForm
     * @param mapping
     * @return ActionForward
     */
    protected ActionForward removeQuestion(HttpServletRequest request, McAuthoringForm mcAuthoringForm, ActionMapping mapping)
    {
		String userAction="removeQuestion";
 		request.setAttribute(USER_ACTION, userAction);
 		logger.debug("userAction:" + userAction);
 		
 		Map mapWeights= AuthoringUtil.repopulateMap(request, "questionWeight");
		request.getSession().setAttribute(MAP_WEIGHTS, mapWeights);
		System.out.print("MAP_WEIGHTS:" + request.getSession().getAttribute(MAP_WEIGHTS));
		
 		Map mapQuestionsContent=AuthoringUtil.repopulateMap(request, "questionContent");
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
		
		
		
		
	 	request.getSession().setAttribute(EDIT_OPTIONS_MODE, new Integer(0));
		logger.debug("resetting  EDIT_OPTIONS_MODE to 0");
		mcAuthoringForm.resetUserAction();
		
		int maxQuestionIndex=mapQuestionsContent.size();
		request.getSession().setAttribute(MAX_QUESTION_INDEX, new Integer(maxQuestionIndex));
		logger.debug("MAX_QUESTION_INDEX: " +  request.getSession().getAttribute(MAX_QUESTION_INDEX));
		return (mapping.findForward(LOAD_QUESTIONS));
    }

    
    /**
     * prepares the UI so that candidate answers for a question can be edited
     * editQuestion(HttpServletRequest request, McAuthoringForm mcAuthoringForm, ActionMapping mapping)
     * 
     * @param request
     * @param mcAuthoringForm
     * @param mapping
     * @return
     */
    protected ActionForward editOptions(HttpServletRequest request, McAuthoringForm mcAuthoringForm, ActionMapping mapping)
    {
    	IMcService mcService =McUtils.getToolService(request);
        String userAction="editOption";
    	request.setAttribute(USER_ACTION, userAction);
    	logger.debug("userAction:" + userAction);
    	
    	Map mapGeneralOptionsContent=(Map)request.getSession().getAttribute(MAP_GENERAL_OPTIONS_CONTENT);
    	logger.debug("initial test: current mapGeneralOptionsContent: " + mapGeneralOptionsContent);
    	
    	request.getSession().setAttribute(EDIT_OPTIONS_MODE, new Integer(1));
    	logger.debug("setting  EDIT_OPTIONS_MODE to 1");
    		
    	Map mapQuestionsContent=AuthoringUtil.repopulateMap(request, "questionContent");
     	logger.debug("mapQuestionsContent after shrinking: " + mapQuestionsContent);
     	logger.debug("mapQuestionsContent size after shrinking: " + mapQuestionsContent.size());
     	request.getSession().setAttribute(MAP_QUESTIONS_CONTENT, mapQuestionsContent);
     	
     	Map mapWeights= AuthoringUtil.repopulateMap(request, "questionWeight");
    	request.getSession().setAttribute(MAP_WEIGHTS, mapWeights);
    	System.out.print("MAP_WEIGHTS:" + request.getSession().getAttribute(MAP_WEIGHTS));
     	
    	String questionIndex =mcAuthoringForm.getQuestionIndex();
    	logger.debug("questionIndex:" + questionIndex);
    	request.getSession().setAttribute(SELECTED_QUESTION_INDEX, questionIndex);
    	logger.debug("set SELECTED_QUESTION_INDEX to:" + questionIndex);
    	
    	String editableQuestionEntry=(String)mapQuestionsContent.get(questionIndex);
    	logger.debug("editableQuestionEntry:" + editableQuestionEntry);
    	request.getSession().setAttribute(SELECTED_QUESTION, editableQuestionEntry);
    	
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
    		return (mapping.findForward(LOAD_QUESTIONS));
    	}
    	
    	Long toolContentId=(Long)request.getSession().getAttribute(TOOL_CONTENT_ID);
    	logger.debug("toolContentId:" + toolContentId);
    	
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
		boolean optionsCacheDataFound=false;
    	while (itMapGeneral.hasNext()) 
    	{
    		optionsCacheDataFound=false;
        	Map.Entry pairs = (Map.Entry)itMapGeneral.next();
        	logger.debug("using the  pair: " +  pairs);
            logger.debug("using the  pair entries: " +  pairs.getKey() + " = " + pairs.getValue());
            
            if ((pairs.getKey() != null))
            {
            	if (pairs.getKey().equals(selectedQuestionIndex))
            	{
            		logger.debug("question found with options in the cache");
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
		        	optionsCacheDataFound=true;
		        	break;
            	}
            }
		}

    	logger.debug("optionsCacheDataFound to be checked: " +  optionsCacheDataFound);
    	if (optionsCacheDataFound == true)
    	{
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
    	else
    	{
    		McContent mcContent=mcService.retrieveMc(toolContentId);
        	logger.debug("mcContent:" + mcContent);
        	
        	McQueContent mcQueContent=null;
        	if (mcContent != null)
        	{
        		logger.debug("mcContent is not null");
        		mcQueContent=mcService.getQuestionContentByQuestionText(editableQuestionEntry, mcContent.getUid());
            	logger.debug("mcQueContent:" + mcQueContent);
        	}
        	
        	if (mcQueContent != null)
        	{
        		logger.debug("mcQueContent is not null " + mcQueContent.getUid());
        		List listOptionsContent=mcService.findMcOptionsContentByQueId(mcQueContent.getUid());
        		logger.debug("listOptionsContent: " + listOptionsContent);
        		
        		mapOptionsContent= new TreeMap(new McComparator());
        		mapSelectedOptions= new TreeMap(new McComparator());
        		
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
        		logger.debug("mcQueContent is null " + mcQueContent);
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
        }
     	request.getSession().setAttribute(EDIT_OPTIONS_MODE, new Integer(1));
    	logger.debug("resetting  EDIT_OPTIONS_MODE to 1");
    	mcAuthoringForm.resetUserAction();
    	return (mapping.findForward(LOAD_QUESTIONS));
    }

    
    /**
     * adds an option entry to the options Map
     * addOption(HttpServletRequest request, McAuthoringForm mcAuthoringForm, ActionMapping mapping)
     * 
     * @param request
     * @param mcAuthoringForm
     * @param mapping
     * @return ActionForward
     */
    protected ActionForward addOption(HttpServletRequest request, McAuthoringForm mcAuthoringForm, ActionMapping mapping)
    {
		String userAction="addOption";
 		request.setAttribute(USER_ACTION, userAction);
 		logger.debug("userAction:" + userAction);
 		
 		request.getSession().setAttribute(EDIT_OPTIONS_MODE, new Integer(1));
		logger.debug("setting  EDIT_OPTIONS_MODE to 1");
 		
 		Map mapOptionsContent=AuthoringUtil.repopulateMap(request,"optionContent");
	 	logger.debug("mapOptionsContent after shrinking: " + mapOptionsContent);
	 	logger.debug("mapOptionsContent size after shrinking: " + mapOptionsContent.size());
	 	
	 	boolean verifyDuplicatesOptionsMap=AuthoringUtil.verifyDuplicatesOptionsMap(mapOptionsContent);
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
		
		
		Map mapSelectedOptions= (Map) request.getSession().getAttribute(MAP_SELECTED_OPTIONS);
 		mapSelectedOptions.clear();
 		mapSelectedOptions = AuthoringUtil.repopulateCurrentCheckBoxStatesMap(request);
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
		return (mapping.findForward(LOAD_QUESTIONS));
    }
    
    
    /**
     * removes an option entry from the options Map
     * addOption(HttpServletRequest request, McAuthoringForm mcAuthoringForm, ActionMapping mapping)
     * 
     * @param request
     * @param mcAuthoringForm
     * @param mapping
     * @return ActionForward
     */
    protected ActionForward removeOption(HttpServletRequest request, McAuthoringForm mcAuthoringForm, ActionMapping mapping)
    {
		String userAction="removeOption";
 		request.setAttribute(USER_ACTION, userAction);
 		logger.debug("userAction:" + userAction);
 		
 		request.getSession().setAttribute(EDIT_OPTIONS_MODE, new Integer(1));
		logger.debug("setting  EDIT_OPTIONS_MODE to 1");
 		
 		String optionIndex =mcAuthoringForm.getDeletableOptionIndex();
		logger.debug("optionIndex:" + optionIndex);
		
		Map mapOptionsContent=AuthoringUtil.repopulateMap(request, "optionContent");
	 	logger.debug("mapOptionsContent after shrinking: " + mapOptionsContent);
	 	logger.debug("mapOptionsContent size after shrinking: " + mapOptionsContent.size());
	 	int mapSize=mapOptionsContent.size();
	 	if ((mapSize == 1) && (optionIndex.equals("1")))
		{
	 		ActionMessages errors= new ActionMessages();
    		errors= new ActionMessages();
			errors.add(Globals.ERROR_KEY,new ActionMessage("options.count.zero"));
			saveErrors(request,errors);
			mcAuthoringForm.resetUserAction();
			persistError(request,"options.count.zero");
			
			request.getSession().setAttribute(EDIT_OPTIONS_MODE, new Integer(1));
	 		logger.debug("setting EDIT_OPTIONS_MODE :" + 1);
			return (mapping.findForward(LOAD_QUESTIONS));
		}
	 	
	 	
		String deletableOptionEntry=(String)mapOptionsContent.get(optionIndex);
		logger.debug("deletableOptionEntry:" + deletableOptionEntry);

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
 		mapSelectedOptions = AuthoringUtil.repopulateCurrentCheckBoxStatesMap(request);
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
		return (mapping.findForward(LOAD_QUESTIONS));
    }
    
    
    /**
     * moves a question entry a step down the questions Map
     * moveQuestionDown(HttpServletRequest request, McAuthoringForm mcAuthoringForm, ActionMapping mapping)
     * 
     * @param request
     * @param mcAuthoringForm
     * @param mapping
     * @return ActionForward
     */
    protected ActionForward moveQuestionDown(HttpServletRequest request, McAuthoringForm mcAuthoringForm, ActionMapping mapping)
    {
    	String userAction="moveDown";
    	request.setAttribute(USER_ACTION, userAction);
    	logger.debug("userAction:" + userAction);
    	
    	Map mapQuestionsContent=AuthoringUtil.repopulateMap(request, "questionContent");
     	logger.debug("mapQuestionsContent before move down: " + mapQuestionsContent);
     	
    	String questionIndex =mcAuthoringForm.getQuestionIndex();
    	logger.debug("questionIndex:" + questionIndex);
    	String movableQuestionEntry=(String)mapQuestionsContent.get(questionIndex);
    	logger.debug("movableQuestionEntry:" + movableQuestionEntry);
    	
    	mapQuestionsContent= AuthoringUtil.shiftMap(mapQuestionsContent, questionIndex,movableQuestionEntry,  "down");
    	logger.debug("mapQuestionsContent after move down: " + mapQuestionsContent);
    	
     	request.getSession().setAttribute(MAP_QUESTIONS_CONTENT, mapQuestionsContent);
    	logger.debug("updated Questions Map: " + request.getSession().getAttribute(MAP_QUESTIONS_CONTENT));
    		
		mcAuthoringForm.resetUserAction();
		request.getSession().setAttribute(EDIT_OPTIONS_MODE, new Integer(0));
    	logger.debug("resetting  EDIT_OPTIONS_MODE to 0");
        
        int maxQuestionIndex=mapQuestionsContent.size();
    	request.getSession().setAttribute(MAX_QUESTION_INDEX, new Integer(maxQuestionIndex));
    	logger.debug("MAX_QUESTION_INDEX: " +  request.getSession().getAttribute(MAX_QUESTION_INDEX));
        return (mapping.findForward(LOAD_QUESTIONS));	
    }
    
    
    /**
     * moves a question entry a step up the questions Map
     * moveQuestionUp(HttpServletRequest request, McAuthoringForm mcAuthoringForm, ActionMapping mapping)
     * 
     * @param request
     * @param mcAuthoringForm
     * @param mapping
     * @return ActionForward
     */
    protected ActionForward moveQuestionUp(HttpServletRequest request, McAuthoringForm mcAuthoringForm, ActionMapping mapping)
    {
		String userAction="moveUp";
 		request.setAttribute(USER_ACTION, userAction);
 		logger.debug("userAction:" + userAction);
 		
 		Map mapQuestionsContent=AuthoringUtil.repopulateMap(request, "questionContent");
	 	logger.debug("mapQuestionsContent before move down: " + mapQuestionsContent);
	 	
 		String questionIndex =mcAuthoringForm.getQuestionIndex();
		logger.debug("questionIndex:" + questionIndex);
		String movableQuestionEntry=(String)mapQuestionsContent.get(questionIndex);
		logger.debug("movableQuestionEntry:" + movableQuestionEntry);
		
		mapQuestionsContent= AuthoringUtil.shiftMap(mapQuestionsContent, questionIndex,movableQuestionEntry,  "up");
		logger.debug("mapQuestionsContent after move down: " + mapQuestionsContent);
		
	 	request.getSession().setAttribute(MAP_QUESTIONS_CONTENT, mapQuestionsContent);
		logger.debug("updated Questions Map: " + request.getSession().getAttribute(MAP_QUESTIONS_CONTENT));
 		
 		mcAuthoringForm.resetUserAction();
 		request.getSession().setAttribute(EDIT_OPTIONS_MODE, new Integer(0));
		logger.debug("resetting  EDIT_OPTIONS_MODE to 0");
	    
	    int maxQuestionIndex=mapQuestionsContent.size();
		request.getSession().setAttribute(MAX_QUESTION_INDEX, new Integer(maxQuestionIndex));
		logger.debug("MAX_QUESTION_INDEX: " +  request.getSession().getAttribute(MAX_QUESTION_INDEX));
	    return (mapping.findForward(LOAD_QUESTIONS));
    }

    /**
     * completes the candidate options screen
     * doneOptions(HttpServletRequest request, McAuthoringForm mcAuthoringForm, ActionMapping mapping)
     * 
     * @param request
     * @param mcAuthoringForm
     * @param mapping
     * @return ActionForward
     */
    protected ActionForward doneOptions(HttpServletRequest request, McAuthoringForm mcAuthoringForm, ActionMapping mapping)
    {
		String userAction="doneOptions";
 		request.setAttribute(USER_ACTION, userAction);
 		logger.debug("userAction:" + userAction);
 		
 		request.getSession().setAttribute(EDIT_OPTIONS_MODE, new Integer(0));
		logger.debug("setting  EDIT_OPTIONS_MODE to 0");
		
 		boolean validateOptions=AuthoringUtil.validateOptions(request);
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
			return (mapping.findForward(LOAD_QUESTIONS));	
 		}
		
 		Map mapOptionsContent=AuthoringUtil.repopulateMap(request, "optionContent");
	 	logger.debug("mapOptionsContent after shrinking: " + mapOptionsContent);
	 	request.getSession().setAttribute(MAP_OPTIONS_CONTENT, mapOptionsContent);
		logger.debug("final done  MAP_OPTIONS_CONTENT: " + mapOptionsContent);
	 	
 		
 		String selectedQuestionIndex=(String) request.getSession().getAttribute(SELECTED_QUESTION_INDEX);
		logger.debug("retrieved SELECTED_QUESTION_INDEX to:" + selectedQuestionIndex);
		
		/** update the questions Map with the new question*/
		Map mapQuestionsContent=(Map) request.getSession().getAttribute(MAP_QUESTIONS_CONTENT);
	 	logger.debug("mapQuestionsContent: " + mapQuestionsContent);
	 	String selectedQuestion=(String) request.getSession().getAttribute(SELECTED_QUESTION);
		logger.debug("final selectedQuestion:" + selectedQuestion);
		mapQuestionsContent.put(selectedQuestionIndex,selectedQuestion);
		logger.debug("updated mapQuestionsContent with:" +  selectedQuestionIndex + " and " + selectedQuestion);
		request.getSession().setAttribute(MAP_QUESTIONS_CONTENT, mapQuestionsContent);
		logger.debug("updated MAP_QUESTIONS_CONTENT:" +  mapQuestionsContent);
		
		
		Map mapSelectedOptions= (Map) request.getSession().getAttribute(MAP_SELECTED_OPTIONS);
 		mapSelectedOptions.clear();
 		mapSelectedOptions = AuthoringUtil.repopulateCurrentCheckBoxStatesMap(request);
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
		
		
		Map mapGeneralOptionsContent=(Map)request.getSession().getAttribute(MAP_GENERAL_OPTIONS_CONTENT);
		logger.debug("current mapGeneralOptionsContent: " + mapGeneralOptionsContent);
		mapGeneralOptionsContent.put(selectedQuestionIndex,mapOptionsContent);
		request.getSession().setAttribute(MAP_GENERAL_OPTIONS_CONTENT, mapGeneralOptionsContent);
		logger.debug("updated  MAP_GENERAL_OPTIONS_CONTENT after done: " + mapGeneralOptionsContent);
		
    	request.getSession().setAttribute(EDIT_OPTIONS_MODE, new Integer(0));
		logger.debug("setting  EDIT_OPTIONS_MODE to 0");
		mcAuthoringForm.resetUserAction();
		return (mapping.findForward(LOAD_QUESTIONS));
    }

    
    /**
     * submits questions Map and persists questions as well as options information in the db.
     * submitQuestions(HttpServletRequest request, McAuthoringForm mcAuthoringForm, ActionMapping mapping)
     * 
     * @param request
     * @param mcAuthoringForm
     * @param mapping
     * @return ActionForward
     */
    protected ActionForward submitQuestions(HttpServletRequest request, McAuthoringForm mcAuthoringForm, ActionMapping mapping)
    {
     	IMcService mcService =McUtils.getToolService(request);
		/* persist the final Questions Map  */
		String userAction="submitQuestions";
		request.setAttribute(USER_ACTION, userAction);
		logger.debug("userAction:" + userAction);
		
		request.getSession().setAttribute(EDIT_OPTIONS_MODE, new Integer(0));
		logger.debug("setting  EDIT_OPTIONS_MODE to 0");
	
		ActionMessages errors= new ActionMessages();
		
		Map mapQuestionsContent=AuthoringUtil.repopulateMap(request, "questionContent");
	 	logger.debug("mapQuestionsContent before submit: " + mapQuestionsContent);
	 	request.getSession().setAttribute(MAP_QUESTIONS_CONTENT, mapQuestionsContent);

	 	/* make sure the questions Map is not empty*/ 
	 	int mapSize=mapQuestionsContent.size();	 	
	 	logger.debug("mapSize: " + mapSize);
	 	
		if (mapSize == 0)
		{
			errors= new ActionMessages();
			errors.add(Globals.ERROR_KEY,new ActionMessage("error.questions.submitted.none"));
			saveErrors(request,errors);
			mcAuthoringForm.resetUserAction();
			persistError(request,"error.questions.submitted.none");
			
	    	mcAuthoringForm.setAddQuestion(null);
			mcAuthoringForm.setSubmitQuestions(null);
			return (mapping.findForward(LOAD_QUESTIONS));
		}

		boolean weightsValid=validateQuestionWeights(request,mcAuthoringForm);
		logger.debug("weightsValid:" + weightsValid);
		if (weightsValid == false)
		{
			mcAuthoringForm.resetUserAction();
			
			int maxQuestionIndex=mapQuestionsContent.size();
			request.getSession().setAttribute(MAX_QUESTION_INDEX, new Integer(maxQuestionIndex));
			logger.debug("MAX_QUESTION_INDEX: " +  request.getSession().getAttribute(MAX_QUESTION_INDEX));
			
	    	mcAuthoringForm.setAddQuestion(null);
			mcAuthoringForm.setSubmitQuestions(null);
			return (mapping.findForward(LOAD_QUESTIONS));
		}
		
		boolean isTotalWeightsValid=AuthoringUtil.validateTotalWeight(request);
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

			int maxQuestionIndex=mapQuestionsContent.size();
			request.getSession().setAttribute(MAX_QUESTION_INDEX, new Integer(maxQuestionIndex));
			logger.debug("MAX_QUESTION_INDEX: " +  request.getSession().getAttribute(MAX_QUESTION_INDEX));
			
	    	mcAuthoringForm.setAddQuestion(null);
			mcAuthoringForm.setSubmitQuestions(null);
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

				int maxQuestionIndex=mapQuestionsContent.size();
				request.getSession().setAttribute(MAX_QUESTION_INDEX, new Integer(maxQuestionIndex));
				logger.debug("MAX_QUESTION_INDEX: " +  request.getSession().getAttribute(MAX_QUESTION_INDEX));
				
		    	mcAuthoringForm.setAddQuestion(null);
				mcAuthoringForm.setSubmitQuestions(null);
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

			int maxQuestionIndex=mapQuestionsContent.size();
			request.getSession().setAttribute(MAX_QUESTION_INDEX, new Integer(maxQuestionIndex));
			logger.debug("MAX_QUESTION_INDEX: " +  request.getSession().getAttribute(MAX_QUESTION_INDEX));
			
	    	mcAuthoringForm.setAddQuestion(null);
			mcAuthoringForm.setSubmitQuestions(null);
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

			mcAuthoringForm.resetUserAction();
			
			int maxQuestionIndex=mapQuestionsContent.size();
			request.getSession().setAttribute(MAX_QUESTION_INDEX, new Integer(maxQuestionIndex));
			logger.debug("MAX_QUESTION_INDEX: " +  request.getSession().getAttribute(MAX_QUESTION_INDEX));
			
	    	mcAuthoringForm.setAddQuestion(null);
			mcAuthoringForm.setSubmitQuestions(null);
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
		
		
		mapQuestionsContent=AuthoringUtil.repopulateMap(request, "questionContent");
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
		
		int maxQuestionIndex=mapQuestionsContent.size();
		request.getSession().setAttribute(MAX_QUESTION_INDEX, new Integer(maxQuestionIndex));
		logger.debug("MAX_QUESTION_INDEX: " +  request.getSession().getAttribute(MAX_QUESTION_INDEX));
		
    	mcAuthoringForm.setAddQuestion(null);
		mcAuthoringForm.setSubmitQuestions(null);
		return (mapping.findForward(LOAD_QUESTIONS));
    }


    /**
     * prepares data to view the contents of uploaded files
     * viewFileItem(HttpServletRequest request, McAuthoringForm mcAuthoringForm, ActionMapping mapping)
     * 
     * @param request
     * @param mcAuthoringForm
     * @param mapping
     * @return
     */
    protected ActionForward viewFileItem(HttpServletRequest request, McAuthoringForm mcAuthoringForm, ActionMapping mapping)
    {
    	IMcService mcService =McUtils.getToolService(request);
    	String userAction="viewFileItem";
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
			return (mapping.findForward(ALL_INSTRUCTIONS));
 		}
 		
 		InputStream fileInputStream=mcService.downloadFile(new Long(uuid), null);
 		logger.debug("fileInputStream:" + fileInputStream);
 		
 		DataInputStream dis = new DataInputStream(fileInputStream);
 		logger.debug("dis:" + dis);
 		
 		String allFileText="";
 		try 
		{
 			String input="";
 			while ((input = dis.readLine()) != null) 
 			{
 				logger.debug("input:" + input);
 				allFileText = allFileText + input + "\r\n";
 			}    
 		} catch (EOFException e) {
 			logger.debug("error reading the file :" + e);
 			logger.debug("error msg reading the file :" + e.getMessage());
 		}
    	catch (IOException e) {
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
   	    return (mapping.findForward(ALL_INSTRUCTIONS));
    }
    
    /**
     * moves from Advanced Tab to Basic Tab
     * doneAdvancedTabDone(HttpServletRequest request, McAuthoringForm mcAuthoringForm, ActionMapping mapping)
     * 
     * @param request
     * @param mcAuthoringForm
     * @param mapping
     * @return
     */
    protected ActionForward doneAdvancedTab(HttpServletRequest request, McAuthoringForm mcAuthoringForm, ActionMapping mapping)
    {
        String userAction="advancedTabDone";
    	request.setAttribute(USER_ACTION, userAction);
    	logger.debug("userAction:" + userAction);
    	
    	mcAuthoringForm.resetUserAction();
        return (mapping.findForward(LOAD_QUESTIONS));
    }
    
    /**
     * moves from Instructions Tab to Basic Tab
     * doneInstructionsTab(HttpServletRequest request, McAuthoringForm mcAuthoringForm, ActionMapping mapping)
     * 
     * @param request
     * @param mcAuthoringForm
     * @param mapping
     * @return
     */
    protected ActionForward doneInstructionsTab(HttpServletRequest request, McAuthoringForm mcAuthoringForm, ActionMapping mapping)
    {
		String userAction="instructionsTabDone";
		request.setAttribute(USER_ACTION, userAction);
		logger.debug("userAction:" + userAction);
		mcAuthoringForm.resetUserAction();
	    return (mapping.findForward(LOAD_QUESTIONS));
    }


    /**
     * adds the offline file information in the content repository.
     * submitOfflineFiles(HttpServletRequest request, McAuthoringForm mcAuthoringForm, ActionMapping mapping)
     * 
     * @param request
     * @param mcAuthoringForm
     * @param mapping
     * @return
     */
    protected ActionForward submitOfflineFiles(HttpServletRequest request, McAuthoringForm mcAuthoringForm, ActionMapping mapping)
    {
		String userAction="submitOfflineFile";
 		request.setAttribute(USER_ACTION, userAction);
 		logger.debug("userAction:" + userAction);
 		
 		McUtils.addFileToContentRepository(request, mcAuthoringForm, true);
        logger.debug("offline file added to repository successfully.");
 		
 		mcAuthoringForm.resetUserAction();
 		request.getSession().setAttribute(EDIT_OPTIONS_MODE, new Integer(2));
 		logger.debug("setting EDIT_OPTIONS_MODE :" + 2 );
   	    return (mapping.findForward(ALL_INSTRUCTIONS));    
    }
    
    /**
     * adds the online file information in the content repository.
     * submitOnlineFiles(HttpServletRequest request, McAuthoringForm mcAuthoringForm, ActionMapping mapping)
     * 
     * @param request
     * @param mcAuthoringForm
     * @param mapping
     * @return
     */
    protected ActionForward submitOnlineFiles(HttpServletRequest request, McAuthoringForm mcAuthoringForm, ActionMapping mapping)
    {
		String userAction="submitOnlineFile";
 		request.setAttribute(USER_ACTION, userAction);
 		logger.debug("userAction:" + userAction);
 		
 		McUtils.addFileToContentRepository(request, mcAuthoringForm, false);
        logger.debug("online file added to repository successfully.");
        
        mcAuthoringForm.resetUserAction();
 		request.getSession().setAttribute(EDIT_OPTIONS_MODE, new Integer(2));
 		logger.debug("setting EDIT_OPTIONS_MODE :" + 2);
   	    return (mapping.findForward(ALL_INSTRUCTIONS));
    }
    

    protected void cleanupExistingQuestions(HttpServletRequest request, McContent mcContent)
    {
    	IMcService mcService =McUtils.getToolService(request);
    	logger.debug("remove questions by  mcQueContent uid : " + mcContent.getUid());
    	mcService.cleanAllQuestionsSimple(mcContent.getUid());
    }
    
    
    /**
     * creates the questions from the user in the db and makes a call to persist options.
     * persistQuestions(HttpServletRequest request, Map mapQuestionsContent, Map mapFeedbackIncorrect, Map mapFeedbackCorrect, McContent mcContent)
     *  
     * @param request
     * @param mapQuestionsContent
     * @param mapFeedbackIncorrect
     * @param mapFeedbackCorrect
     * @param mcContent
     */
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

    	Map mapWeights= AuthoringUtil.repopulateMap(request, "questionWeight");
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
    
    /**
     * prepares the data to persist options in the db
     * persistOptions(HttpServletRequest request, Map mapGeneralOptionsContent, Map mapGeneralSelectedOptionsContent, McQueContent mcQueContent, String questionIndex)
     * 
     * @param request
     * @param mapGeneralOptionsContent
     * @param mapGeneralSelectedOptionsContent
     * @param mcQueContent
     * @param questionIndex
     */
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
    
    
    /**
     * creates the options for a particular question in the db
     * persistOptionsFinal(HttpServletRequest request, Map currentOptions, Map selectedOptions, McQueContent mcQueContent)
     * 
     * @param request
     * @param currentOptions
     * @param selectedOptions
     * @param mcQueContent
     */
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
    
    /**
     * creates the questions content in the db.
     * createContent(HttpServletRequest request, McAuthoringForm mcAuthoringForm)
     * 
     * @param request
     * @param mcAuthoringForm
     * @return
     */
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
    	boolean isSln=false;
    	
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
    	
    	
    	String richTextReportTitle=(String)request.getSession().getAttribute(RICHTEXT_REPORT_TITLE);
		logger.debug("richTextReportTitle: " + richTextReportTitle);
		
		String richTextEndLearningMessage=(String)request.getSession().getAttribute(RICHTEXT_END_LEARNING_MSG);
		logger.debug("richTextEndLearningMessage: " + richTextEndLearningMessage);
    	
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
	    mc.setReportTitle(richTextReportTitle);
	    mc.setMonitoringReportTitle(monitoringReportTitle);
	    mc.setEndLearningMessage(richTextEndLearningMessage);
	    mc.setRetries(isRetries);
	    mc.setPassMark(new Integer(passmark));
	    mc.setShowReport(isSln);
	    mc.setShowFeedback(isShowFeedback);
	    mc.setMcQueContents(new TreeSet());
	    mc.setMcSessions(new TreeSet());
	    logger.debug("mc content :" +  mc);
    	
    	/*create the content in the db*/
        mcService.createMc(mc);
        logger.debug("mc created with content id: " + toolContentId);
        
        return mc;
    }
    
    
    /**
     * includes the new question entry in the questions Map
     * addQuestionMemory(HttpServletRequest request, McAuthoringForm mcAuthoringForm, Map mapQuestionsContent, boolean increaseMapSize)
     * 
     * @param request
     * @param mcAuthoringForm
     * @param mapQuestionsContent
     * @param increaseMapSize
     */
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

    
    /**
     * ensures that the weight valued entered are valid
     * validateQuestionWeights(HttpServletRequest request, McAuthoringForm mcAuthoringForm)
     * 
     * @param request
     * @param mcAuthoringForm
     * @return
     */
    protected boolean validateQuestionWeights(HttpServletRequest request, McAuthoringForm mcAuthoringForm)
    {
    	Map mapWeights= AuthoringUtil.repopulateCurrentWeightsMap(request, "questionWeight");
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
     */
    public ActionForward displayMc(ActionMapping mapping,
            ActionForm form,
            HttpServletRequest request,
            HttpServletResponse response) throws IOException,
                                      ServletException
   {
    	logger.debug("displayMc starting..");
    	    	
    	McLearningForm mcLearningForm = (McLearningForm) form;
	 	IMcService mcService =McUtils.getToolService(request);
	 	
    	mcLearningForm.resetParameters();
    	LearningUtil.readParameters(request, mcLearningForm);
    	
    	if (mcLearningForm.getContinueOptionsCombined() != null)
    	{
    		logger.debug("continue options combined requested.");
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
        	request.getSession().setAttribute(LEARNER_MARK_ATLEAST, new Integer(mark+1).toString());
        	
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
        			request.getSession().setAttribute(USER_PASSED, new Boolean(false).toString());
        			logger.debug("totalUserWeight is less than passmark: " + totalUserWeight + " < " + passMark.intValue());
        			passed=false;
        		}
        		else
        		{
        			logger.debug("USER PASSED");
        			request.getSession().setAttribute(USER_PASSED, new Boolean(true).toString());
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
            
            logger.debug("passed: " + passed);
        	LearningUtil.createAttempt(request, mcQueUsr, mapGeneralCheckedOptionsContent, mark, passed, new Integer(highestAttemptOrder).intValue());
        	logger.debug("created user attempt in the db");
        	
        	int intHighestAttemptOrder=new Integer(highestAttemptOrder).intValue()+ 1 ;
            logger.debug("updated highestAttemptOrder:" + intHighestAttemptOrder);
            request.getSession().setAttribute(LEARNER_LAST_ATTEMPT_ORDER, new Integer(intHighestAttemptOrder).toString());
    		
    		mcLearningForm.resetCommands();
    		return (mapping.findForward(INDIVIDUAL_REPORT));
    	}
    	else if (mcLearningForm.getNextOptions() != null)
	 	{
    		logger.debug("requested next options...");
    		
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
    	else if (mcLearningForm.getOptionCheckBoxSelected() != null)
    	{
    		logger.debug("requested selectOptionsCheckBox...");
    		mcLearningForm.resetCommands();
    		LearningUtil.selectOptionsCheckBox(request,mcLearningForm, mcLearningForm.getQuestionIndex());
    	}
    	else if (mcLearningForm.getRedoQuestions() != null)
    	{
    		logger.debug("requested redoQuestions...");
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
    	else if (mcLearningForm.getRedoQuestionsOk() != null)
    	{
    		logger.debug("requested redoQuestionsOk, user is sure to redo the questions.");
    		mcLearningForm.resetCommands();
    		return redoQuestions(request, mcLearningForm, mapping);
    	}
    	else if (mcLearningForm.getViewAnswers() != null)
    	{
    		logger.debug("requested view Answers, listall the answers user has given.");
    		String totalQuestionCount= (String) request.getSession().getAttribute(TOTAL_QUESTION_COUNT);
    		logger.debug("totalQuestionCount: " + totalQuestionCount);
    		
    		Long toolContentUID= (Long) request.getSession().getAttribute(TOOL_CONTENT_UID);
    		logger.debug("toolContentUID: " + toolContentUID);
        	
    		McQueUsr mcQueUsr=LearningUtil.getUser(request);
    		Long queUsrId=mcQueUsr.getUid();
    		logger.debug("queUsrId: " + queUsrId);
    	
    		Map mapQueAttempts= new TreeMap(new McComparator());
    		for (int i=1; i<=  new Integer(totalQuestionCount).intValue(); i++)
    		{
    			logger.debug("doing question with display order: " + i);
    			McQueContent mcQueContent=mcService.getQuestionContentByDisplayOrder(new Long(i), toolContentUID);
    			logger.debug("mcQueContent uid: " + mcQueContent.getUid());
    			//List attempts=mcService.getAttemptForQueContent(queUsrId, mcQueContent.getUid());
    			//logger.debug("attempts for question: " + mcQueContent.getUid() + " are: " + attempts);
    			
    			Map mapAttemptOrderAttempts= new TreeMap(new McComparator());
    			for (int j=1; j < 11 ; j++ )
	    		{
	    			List attemptsByAttemptOrder=mcService.getAttemptByAttemptOrder(queUsrId, mcQueContent.getUid(), new Integer(j));
    	    		logger.debug("attemptsByAttemptOrder: " + j + " is: " + attemptsByAttemptOrder);
    	    	
    	    		Map mapAttempts= new TreeMap(new McComparator());
    	    		Iterator attemptIterator=attemptsByAttemptOrder.iterator();
    	    		Long mapIndex=new Long(1);
        	    	while (attemptIterator.hasNext())
        	    	{
        	    		McUsrAttempt mcUsrAttempt=(McUsrAttempt)attemptIterator.next();
        	    		mapAttempts.put(mapIndex.toString(),mcUsrAttempt.getMcOptionsContent().getMcQueOptionText());
    					
    	    			logger.debug("added attempt with order: " + mcUsrAttempt.getAttemptOrder() + " , option text is: " + mcUsrAttempt.getMcOptionsContent().getMcQueOptionText());
    	    			mapIndex=new Long(mapIndex.longValue()+1);
        	    	}    	    		
        	    	logger.debug("final mapAttempts is: " + mapAttempts);
        	    	mapAttemptOrderAttempts.put(new Integer(j).toString(), mapAttempts);	
	    		}
    			logger.debug("final mapAttemptOrderAttempts is: " + mapAttemptOrderAttempts);
    			mapQueAttempts.put(new Integer(i).toString(), mapAttemptOrderAttempts);
    		}
    		
    		logger.debug("final mapQueAttempts is: " + mapQueAttempts);
    		request.getSession().setAttribute(MAP_QUE_ATTEMPTS, mapQueAttempts);
    		
    		mcLearningForm.resetCommands();
    		return (mapping.findForward(VIEW_ANSWERS));
    	}
    	else if (mcLearningForm.getViewSummary() != null)
    	{
    		logger.debug("requested view summary...");
    		mcLearningForm.resetCommands();
    		return (mapping.findForward(VIEW_SUMMARY));
    	}
    	else if (mcLearningForm.getLearnerFinished() != null)
    	{
    		logger.debug("requested learner finished, the learner should be directed to next activity.");
    		mcLearningForm.resetCommands();
    		//fix this
    		return (mapping.findForward(VIEW_SUMMARY));
    	}

    	mcLearningForm.resetCommands();	
 		return (mapping.findForward(LOAD_LEARNER));
   }
    
    
    /**
     * continueOptions(HttpServletRequest request, McLearningForm mcLearningForm, ActionMapping mapping)
     * 
     * @param request
     * @param mcLearningForm
     * @param mapping
     * @return
     */
    protected ActionForward getNextOptions(HttpServletRequest request, McLearningForm mcLearningForm, ActionMapping mapping)
    {
    	logger.debug("requested continueOptions...");
    	boolean continueOptions=LearningUtil.continueOptions(request);
    	if (continueOptions == true)
    	{
    		/* get the next question */
    		mcLearningForm.resetCommands();
    		return (mapping.findForward(LOAD_LEARNER));
    	}
    	else
    	{
    		/* no more questions */
    		mcLearningForm.resetCommands();
    		return (mapping.findForward(INDIVIDUAL_REPORT));
    	}	
    }
    
    
    /**
     * redoQuestions(HttpServletRequest request, McLearningForm mcLearningForm, ActionMapping mapping)
     * 
     * @param request
     * @param mcLearningForm
     * @param mapping
     * @return
     */
    protected ActionForward redoQuestions(HttpServletRequest request, McLearningForm mcLearningForm, ActionMapping mapping)
    {
    	logger.debug("requested redoQuestions...");
    	/* reset the checked options MAP */
    	Map mapGeneralCheckedOptionsContent= new TreeMap(new McComparator());
    	request.getSession().setAttribute(MAP_GENERAL_CHECKED_OPTIONS_CONTENT, mapGeneralCheckedOptionsContent);
    	mcLearningForm.resetCommands();
    	return (mapping.findForward(LOAD_LEARNER));
    }

}
    

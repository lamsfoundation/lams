/* *Copyright (C) 2005 LAMS Foundation (http://lamsfoundation.org)
 *
 *This program is free software; you can redistribute it and/or modify
 *it under the terms of the GNU General Public License as published by
 *the Free Software Foundation; either version 2 of the License, or
 *(at your option) any later version.
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
import org.lamsfoundation.lams.contentrepository.RepositoryCheckedException;
import org.lamsfoundation.lams.tool.exception.ToolException;
import org.lamsfoundation.lams.tool.mc.McAppConstants;
import org.lamsfoundation.lams.tool.mc.McApplicationException;
import org.lamsfoundation.lams.tool.mc.McAttachmentDTO;
import org.lamsfoundation.lams.tool.mc.McComparator;
import org.lamsfoundation.lams.tool.mc.McUtils;
import org.lamsfoundation.lams.tool.mc.pojos.McContent;
import org.lamsfoundation.lams.tool.mc.pojos.McOptsContent;
import org.lamsfoundation.lams.tool.mc.pojos.McQueContent;
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
 
	<!--Authoring Main Action : interacts with the authoring module user-->
	<action path="/authoring"
	      	type="org.lamsfoundation.lams.tool.mc.web.McAction"
    	  	name="McAuthoringForm"
	      	scope="session"
    	  	input=".questions"
      		parameter="dispatch"
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
    </action>

*/
public class McAction extends LamsDispatchAction implements McAppConstants
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
	 	McAuthoringForm mcAuthoringForm = (McAuthoringForm) form;
	 	IMcService mcService =McUtils.getToolService(request);
	 	AuthoringUtil.readData(request, mcAuthoringForm);	 	
	 	mcAuthoringForm.resetUserAction();
	 	return null;
    }



     /**
     * adds a new entry to the questions Map
     * 
     * addNewQuestion(ActionMapping mapping,
            ActionForm form,
            HttpServletRequest request,
            HttpServletResponse response)
     * @param request
     * @param form
     * @param mapping
     * @return ActionForward
     */
    public ActionForward addNewQuestion(ActionMapping mapping,
            ActionForm form,
            HttpServletRequest request,
            HttpServletResponse response) throws IOException,
                                         ServletException
	{
    	logger.debug("dispatching addNewQuestion...");
    	McAuthoringForm mcAuthoringForm = (McAuthoringForm) form;
	 	IMcService mcService =McUtils.getToolService(request);
	 	
		/* determine whether the request is from Monitoring url Edit Activity*/
		String sourceMcStarter = (String) request.getAttribute(SOURCE_MC_STARTER);
		logger.debug("sourceMcStarter: " + sourceMcStarter);
		String destination=McUtils.getDestination(sourceMcStarter);
		logger.debug("destination: " + destination);
		
	 	AuthoringUtil.readData(request, mcAuthoringForm);
	 	
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
    		
        	return (mapping.findForward(destination));
    	}
    		
    	logger.debug("will validate weights");
    	Map mapWeights= AuthoringUtil.repopulateCurrentWeightsMap(request, "questionWeight");
    	logger.debug("mapWeights: " + mapWeights);
    	
    	boolean weightsValid=validateQuestionWeights(request,mapWeights, mcAuthoringForm);
    	logger.debug("weightsValid:" + weightsValid);
    	if (weightsValid == false)
    	{
    		mcAuthoringForm.resetUserAction();
    		
    		int maxQuestionIndex=mapQuestionsContent.size();
    		request.getSession().setAttribute(MAX_QUESTION_INDEX, new Integer(maxQuestionIndex));
    		logger.debug("MAX_QUESTION_INDEX: " +  request.getSession().getAttribute(MAX_QUESTION_INDEX));
    		
        	return (mapping.findForward(destination));
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
    		
        	return (mapping.findForward(destination));
    	}
    	request.getSession().setAttribute(MAP_WEIGHTS, mapWeights);
    	logger.debug("MAP_WEIGHTS:" + request.getSession().getAttribute(MAP_WEIGHTS));
    	
    	AuthoringUtil.addQuestionMemory(request, mcAuthoringForm, mapQuestionsContent, true);
    	logger.debug("after addQuestionMemory");
    	
    	request.getSession().setAttribute(EDIT_OPTIONS_MODE, new Integer(0));
    	logger.debug("resetting  EDIT_OPTIONS_MODE to 0");
        mcAuthoringForm.resetUserAction();
        
        int maxQuestionIndex=mapQuestionsContent.size();
    	request.getSession().setAttribute(MAX_QUESTION_INDEX, new Integer(maxQuestionIndex));
    	logger.debug("MAX_QUESTION_INDEX: " +  request.getSession().getAttribute(MAX_QUESTION_INDEX));
    	
    	
    	logger.debug("printing final maps...");
    	Map mapGeneralOptionsContent=(Map)request.getSession().getAttribute(MAP_GENERAL_OPTIONS_CONTENT);
		logger.debug("mapGeneralOptionsContent:"+ mapGeneralOptionsContent);
		
		Map mapGeneralSelectedOptionsContent=(Map)request.getSession().getAttribute(MAP_GENERAL_SELECTED_OPTIONS_CONTENT);
		logger.debug("mapGeneralSelectedOptionsContent " +  mapGeneralSelectedOptionsContent);
		
		logger.debug("sequencing final maps...");
		mapGeneralOptionsContent=AuthoringUtil.sequenceMap(mapGeneralOptionsContent);
		logger.debug("sequenced mapGeneralOptionsContent:"+ mapGeneralOptionsContent);
		request.getSession().setAttribute(MAP_GENERAL_OPTIONS_CONTENT, mapGeneralOptionsContent);
    	
		mapGeneralSelectedOptionsContent=AuthoringUtil.sequenceMap(mapGeneralSelectedOptionsContent);
		logger.debug("sequenced mapGeneralSelectedOptionsContent:"+ mapGeneralSelectedOptionsContent);
		request.getSession().setAttribute(MAP_GENERAL_SELECTED_OPTIONS_CONTENT, mapGeneralSelectedOptionsContent);
		
    	return (mapping.findForward(destination));	
	}
    

     /**
     * removes an entry from the questions Map
		removeQuestion(ActionMapping mapping,
            ActionForm form,
            HttpServletRequest request,
            HttpServletResponse response)
     * 
     * removeQuestion(HttpServletRequest request, McAuthoringForm mcAuthoringForm, ActionMapping mapping)
     * @param request
     * @param form
     * @param mapping
     * @return ActionForward
     */
    public ActionForward removeQuestion(ActionMapping mapping,
            ActionForm form,
            HttpServletRequest request,
            HttpServletResponse response) throws IOException,
                                         ServletException
	{
    	logger.debug("dispatching removeQuestion...");
    	McAuthoringForm mcAuthoringForm = (McAuthoringForm) form;
	 	IMcService mcService =McUtils.getToolService(request);
	 	
		/* determine whether the request is from Monitoring url Edit Activity*/
		String sourceMcStarter = (String) request.getAttribute(SOURCE_MC_STARTER);
		logger.debug("sourceMcStarter: " + sourceMcStarter);
		String destination=McUtils.getDestination(sourceMcStarter);
		logger.debug("destination: " + destination);
	 	
	 	AuthoringUtil.readData(request, mcAuthoringForm);
	 	
 		Map mapWeights= AuthoringUtil.repopulateMap(request, "questionWeight");
		request.getSession().setAttribute(MAP_WEIGHTS, mapWeights);
		logger.debug("MAP_WEIGHTS:" + request.getSession().getAttribute(MAP_WEIGHTS));
	
		
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
		
		
		Map mapGeneralOptionsContent=(Map)request.getSession().getAttribute(MAP_GENERAL_OPTIONS_CONTENT);
		if (mapGeneralOptionsContent.size() > 0)
    	{
			logger.debug("initial test: current mapGeneralOptionsContent: " + mapGeneralOptionsContent);
	    	mapGeneralOptionsContent= AuthoringUtil.removeFromMap(mapGeneralOptionsContent, questionIndex);
	    	logger.debug("mapGeneralOptionsContent after remove: " + mapGeneralOptionsContent);
	    	request.getSession().setAttribute(MAP_GENERAL_OPTIONS_CONTENT, mapGeneralOptionsContent);	
    	}
		
		Map mapGeneralSelectedOptionsContent=(Map)request.getSession().getAttribute(MAP_GENERAL_SELECTED_OPTIONS_CONTENT);
    	if (mapGeneralSelectedOptionsContent.size() > 0)
    	{
    		logger.debug("initial test: current mapGeneralSelectedOptionsContent: " + mapGeneralSelectedOptionsContent);
    		mapGeneralSelectedOptionsContent= AuthoringUtil.removeFromMap(mapGeneralSelectedOptionsContent, questionIndex);
        	logger.debug("mapGeneralSelectedOptionsContent after remove: " + mapGeneralSelectedOptionsContent);
        	request.getSession().setAttribute(MAP_GENERAL_SELECTED_OPTIONS_CONTENT, mapGeneralSelectedOptionsContent);	
    	}
		
    	mapWeights= (Map)request.getSession().getAttribute(MAP_WEIGHTS);
    	if (mapWeights.size() > 0)
    	{
    		logger.debug("initial test: current mapWeights: " + mapWeights);
    		mapWeights= AuthoringUtil.removeFromMap(mapWeights, questionIndex);
        	logger.debug("mapWeights after remove: " + mapWeights);
        	request.getSession().setAttribute(MAP_WEIGHTS, mapWeights);	
    	}
    	
	 	request.getSession().setAttribute(EDIT_OPTIONS_MODE, new Integer(0));
		logger.debug("resetting  EDIT_OPTIONS_MODE to 0");
		mcAuthoringForm.resetUserAction();
		
		int maxQuestionIndex=mapQuestionsContent.size();
		request.getSession().setAttribute(MAX_QUESTION_INDEX, new Integer(maxQuestionIndex));
		logger.debug("MAX_QUESTION_INDEX: " +  request.getSession().getAttribute(MAX_QUESTION_INDEX));
		
		logger.debug("printing final maps...");
    	mapGeneralOptionsContent=(Map)request.getSession().getAttribute(MAP_GENERAL_OPTIONS_CONTENT);
		logger.debug("mapGeneralOptionsContent:"+ mapGeneralOptionsContent);
		
		mapGeneralSelectedOptionsContent=(Map)request.getSession().getAttribute(MAP_GENERAL_SELECTED_OPTIONS_CONTENT);
		logger.debug("mapGeneralSelectedOptionsContent " +  mapGeneralSelectedOptionsContent);
		
		logger.debug("sequencing final maps...");
		mapGeneralOptionsContent=AuthoringUtil.sequenceMap(mapGeneralOptionsContent);
		logger.debug("sequenced mapGeneralOptionsContent:"+ mapGeneralOptionsContent);
		request.getSession().setAttribute(MAP_GENERAL_OPTIONS_CONTENT, mapGeneralOptionsContent);
    	
		mapGeneralSelectedOptionsContent=AuthoringUtil.sequenceMap(mapGeneralSelectedOptionsContent);
		logger.debug("sequenced mapGeneralSelectedOptionsContent:"+ mapGeneralSelectedOptionsContent);
		request.getSession().setAttribute(MAP_GENERAL_SELECTED_OPTIONS_CONTENT, mapGeneralSelectedOptionsContent);
		
		return (mapping.findForward(destination));
    }

    
    /**
     * prepares the UI so that candidate answers for a question can be edited
     * editOptions(ActionMapping mapping,
            ActionForm form,
            HttpServletRequest request,
            HttpServletResponse response)
     * 
     * @param request
     * @param form
     * @param mapping
     * @return ActionForward
     */
    public ActionForward editOptions(ActionMapping mapping,
            ActionForm form,
            HttpServletRequest request,
            HttpServletResponse response) throws IOException,
                                         ServletException
    {
    	logger.debug("dispatching editOptions...");
    	McAuthoringForm mcAuthoringForm = (McAuthoringForm) form;
	 	IMcService mcService =McUtils.getToolService(request);
	 	
		/* determine whether the request is from Monitoring url Edit Activity*/
		String sourceMcStarter = (String) request.getAttribute(SOURCE_MC_STARTER);
		logger.debug("sourceMcStarter: " + sourceMcStarter);
		String destination=McUtils.getDestination(sourceMcStarter);
		logger.debug("destination: " + destination);

	 	AuthoringUtil.readData(request, mcAuthoringForm);
	 	
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
    	logger.debug("MAP_WEIGHTS:" + request.getSession().getAttribute(MAP_WEIGHTS));
     	
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
    		logger.debug("return to destination to fix error.");
    		
    		request.getSession().setAttribute(EDIT_OPTIONS_MODE, new Integer(0));
    		logger.debug("setting  EDIT_OPTIONS_MODE to 0");
    		return (mapping.findForward(destination));
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
    	
    	logger.debug("final mapOptionsContent used is: " + mapOptionsContent);
    	if (mapOptionsContent.isEmpty())
    	{
    		logger.debug("mapOptionsContent is empty");
    		mapOptionsContent.put("1","sample answer 1");
    		mapSelectedOptions = mapOptionsContent;
    		request.getSession().setAttribute(MAP_OPTIONS_CONTENT, mapOptionsContent);
    		request.getSession().setAttribute(MAP_SELECTED_OPTIONS, mapSelectedOptions);
    		
    		mapGeneralOptionsContent=(Map)request.getSession().getAttribute(MAP_GENERAL_OPTIONS_CONTENT);
    		logger.debug("current mapGeneralOptionsContent:"+ mapGeneralOptionsContent);
    		mapGeneralOptionsContent.put(questionIndex,mapOptionsContent);
    		request.getSession().setAttribute(MAP_GENERAL_OPTIONS_CONTENT, mapGeneralOptionsContent);
    		
    		Map mapGeneralSelectedOptionsContent=mapGeneralOptionsContent;
    		request.getSession().setAttribute(MAP_GENERAL_SELECTED_OPTIONS_CONTENT, mapGeneralSelectedOptionsContent);
    	}
    	
    	
     	request.getSession().setAttribute(EDIT_OPTIONS_MODE, new Integer(1));
    	logger.debug("resetting  EDIT_OPTIONS_MODE to 1");
    	mcAuthoringForm.resetUserAction();
    	
    	logger.debug("printing final maps...");
    	mapGeneralOptionsContent=(Map)request.getSession().getAttribute(MAP_GENERAL_OPTIONS_CONTENT);
		logger.debug("mapGeneralOptionsContent:"+ mapGeneralOptionsContent);
		
		Map mapGeneralSelectedOptionsContent=(Map)request.getSession().getAttribute(MAP_GENERAL_SELECTED_OPTIONS_CONTENT);
		logger.debug("mapGeneralSelectedOptionsContent " +  mapGeneralSelectedOptionsContent);
		
		logger.debug("sequencing final maps...");
		mapGeneralOptionsContent=AuthoringUtil.sequenceMap(mapGeneralOptionsContent);
		logger.debug("sequenced mapGeneralOptionsContent:"+ mapGeneralOptionsContent);
		request.getSession().setAttribute(MAP_GENERAL_OPTIONS_CONTENT, mapGeneralOptionsContent);
    	
		mapGeneralSelectedOptionsContent=AuthoringUtil.sequenceMap(mapGeneralSelectedOptionsContent);
		logger.debug("sequenced mapGeneralSelectedOptionsContent:"+ mapGeneralSelectedOptionsContent);
		request.getSession().setAttribute(MAP_GENERAL_SELECTED_OPTIONS_CONTENT, mapGeneralSelectedOptionsContent);
		
    	return (mapping.findForward(destination));
    }

    
    /**
     * adds an option entry to the options Map
     * addOption(ActionMapping mapping,
            ActionForm form,
            HttpServletRequest request,
            HttpServletResponse response
     * 
     * @param request
     * @param form
     * @param mapping
     * @return ActionForward
     */
    public ActionForward addOption(ActionMapping mapping,
            ActionForm form,
            HttpServletRequest request,
            HttpServletResponse response) throws IOException,
                                         ServletException
    {
    	logger.debug("dispatching addOption...");
    	McAuthoringForm mcAuthoringForm = (McAuthoringForm) form;
	 	IMcService mcService =McUtils.getToolService(request);
	 	
		/* determine whether the request is from Monitoring url Edit Activity*/
		String sourceMcStarter = (String) request.getAttribute(SOURCE_MC_STARTER);
		logger.debug("sourceMcStarter: " + sourceMcStarter);
		String destination=McUtils.getDestination(sourceMcStarter);
		logger.debug("destination: " + destination);

	 	AuthoringUtil.readData(request, mcAuthoringForm);
	 	
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
			logger.debug("return to destination to fix error.");
			
			request.getSession().setAttribute(EDIT_OPTIONS_MODE, new Integer(1));
			logger.debug("setting  EDIT_OPTIONS_MODE to 1");
			return (mapping.findForward(destination));	
 		}
	 	
	 	boolean verifyMapNoEmptyString=AuthoringUtil.verifyMapNoEmptyString(mapOptionsContent);
	 	logger.debug("verifyMapNoEmptyString: " + verifyMapNoEmptyString);
	 	if (verifyDuplicatesOptionsMap == false)
 		{
 			ActionMessages errors= new ActionMessages();
			errors.add(Globals.ERROR_KEY,new ActionMessage("error.answers.empty"));
			logger.debug("add error.answers.empty to ActionMessages");
			saveErrors(request,errors);
			mcAuthoringForm.resetUserAction();
			logger.debug("return to destination to fix error.");
			
			request.getSession().setAttribute(EDIT_OPTIONS_MODE, new Integer(1));
			logger.debug("setting  EDIT_OPTIONS_MODE to 1");
			return (mapping.findForward(destination));	
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
 		
 		logger.debug("printing final maps...");
    	mapGeneralOptionsContent=(Map)request.getSession().getAttribute(MAP_GENERAL_OPTIONS_CONTENT);
		logger.debug("mapGeneralOptionsContent:"+ mapGeneralOptionsContent);
		
		mapGeneralSelectedOptionsContent=(Map)request.getSession().getAttribute(MAP_GENERAL_SELECTED_OPTIONS_CONTENT);
		logger.debug("mapGeneralSelectedOptionsContent " +  mapGeneralSelectedOptionsContent);
		
		logger.debug("sequencing final maps...");
		mapGeneralOptionsContent=AuthoringUtil.sequenceMap(mapGeneralOptionsContent);
		logger.debug("sequenced mapGeneralOptionsContent:"+ mapGeneralOptionsContent);
		request.getSession().setAttribute(MAP_GENERAL_OPTIONS_CONTENT, mapGeneralOptionsContent);
    	
		mapGeneralSelectedOptionsContent=AuthoringUtil.sequenceMap(mapGeneralSelectedOptionsContent);
		logger.debug("sequenced mapGeneralSelectedOptionsContent:"+ mapGeneralSelectedOptionsContent);
		request.getSession().setAttribute(MAP_GENERAL_SELECTED_OPTIONS_CONTENT, mapGeneralSelectedOptionsContent);
		
		return (mapping.findForward(destination));
    }
    
    
    /**
     * removes an option entry from the options Map
     * removeOption(ActionMapping mapping,
            ActionForm form,
            HttpServletRequest request,
            HttpServletResponse response)
     * 
     * @param request
     * @param form
     * @param mapping
     * @return ActionForward
     */
    public ActionForward removeOption(ActionMapping mapping,
            ActionForm form,
            HttpServletRequest request,
            HttpServletResponse response) throws IOException,
                                         ServletException
    {
    	logger.debug("dispatching removeOption...");
    	McAuthoringForm mcAuthoringForm = (McAuthoringForm) form;
	 	IMcService mcService =McUtils.getToolService(request);
	 	
		/* determine whether the request is from Monitoring url Edit Activity*/
		String sourceMcStarter = (String) request.getAttribute(SOURCE_MC_STARTER);
		logger.debug("sourceMcStarter: " + sourceMcStarter);
		String destination=McUtils.getDestination(sourceMcStarter);
		logger.debug("destination: " + destination);
	 	
	 	AuthoringUtil.readData(request, mcAuthoringForm);
	 	
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
			return (mapping.findForward(destination));
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
		
		logger.debug("printing final maps...");
    	mapGeneralOptionsContent=(Map)request.getSession().getAttribute(MAP_GENERAL_OPTIONS_CONTENT);
		logger.debug("mapGeneralOptionsContent:"+ mapGeneralOptionsContent);
		
		mapGeneralSelectedOptionsContent=(Map)request.getSession().getAttribute(MAP_GENERAL_SELECTED_OPTIONS_CONTENT);
		logger.debug("mapGeneralSelectedOptionsContent " +  mapGeneralSelectedOptionsContent);
		
		logger.debug("sequencing final maps...");
		mapGeneralOptionsContent=AuthoringUtil.sequenceMap(mapGeneralOptionsContent);
		logger.debug("sequenced mapGeneralOptionsContent:"+ mapGeneralOptionsContent);
		request.getSession().setAttribute(MAP_GENERAL_OPTIONS_CONTENT, mapGeneralOptionsContent);
    	
		mapGeneralSelectedOptionsContent=AuthoringUtil.sequenceMap(mapGeneralSelectedOptionsContent);
		logger.debug("sequenced mapGeneralSelectedOptionsContent:"+ mapGeneralSelectedOptionsContent);
		request.getSession().setAttribute(MAP_GENERAL_SELECTED_OPTIONS_CONTENT, mapGeneralSelectedOptionsContent);
		return (mapping.findForward(destination));
    }
    
    
    /**
     * moves a question entry a step down the questions Map
     * moveQuestionDown(ActionMapping mapping,
            ActionForm form,
            HttpServletRequest request,
            HttpServletResponse response)
     * 
     * @param request
     * @param form
     * @param mapping
     * @return ActionForward
     */
    public ActionForward moveQuestionDown(ActionMapping mapping,
            ActionForm form,
            HttpServletRequest request,
            HttpServletResponse response) throws IOException,
                                         ServletException
    {
    	logger.debug("dispatching moveQuestionDown...");
    	McAuthoringForm mcAuthoringForm = (McAuthoringForm) form;
	 	IMcService mcService =McUtils.getToolService(request);

		/* determine whether the request is from Monitoring url Edit Activity*/
		String sourceMcStarter = (String) request.getAttribute(SOURCE_MC_STARTER);
		logger.debug("sourceMcStarter: " + sourceMcStarter);
		String destination=McUtils.getDestination(sourceMcStarter);
		logger.debug("destination: " + destination);

	 	AuthoringUtil.readData(request, mcAuthoringForm);
	 	
    	Map mapQuestionsContent=AuthoringUtil.repopulateMap(request, "questionContent");
     	logger.debug("mapQuestionsContent before move down: " + mapQuestionsContent);
     	logger.debug("mapQuestionsContent size move down: " + mapQuestionsContent.size());

     	//perform a move down if there are at least 2 questions
     	if (mapQuestionsContent.size() > 1)
     	{
     		String questionIndex =mcAuthoringForm.getQuestionIndex();
        	logger.debug("questionIndex:" + questionIndex);
        	String movableQuestionEntry=(String)mapQuestionsContent.get(questionIndex);
        	logger.debug("movableQuestionEntry:" + movableQuestionEntry);
        	
        	if (movableQuestionEntry != null && (!movableQuestionEntry.equals("")))
        	{
            	mapQuestionsContent= AuthoringUtil.shiftMap(mapQuestionsContent, questionIndex,movableQuestionEntry,  "down");
            	logger.debug("mapQuestionsContent after move down: " + mapQuestionsContent);
            	request.getSession().setAttribute(MAP_QUESTIONS_CONTENT, mapQuestionsContent);
            	logger.debug("updated Questions Map: " + request.getSession().getAttribute(MAP_QUESTIONS_CONTENT));
            	
            	Map mapGeneralOptionsContent=(Map)request.getSession().getAttribute(MAP_GENERAL_OPTIONS_CONTENT);
            	if (mapGeneralOptionsContent.size() > 0)
            	{
            		logger.debug("initial test: current mapGeneralOptionsContent: " + mapGeneralOptionsContent);
                	mapGeneralOptionsContent= AuthoringUtil.shiftOptionsMap(mapGeneralOptionsContent, questionIndex, "down");
                	logger.debug("mapGeneralOptionsContent after move down: " + mapGeneralOptionsContent);
                	request.getSession().setAttribute(MAP_GENERAL_OPTIONS_CONTENT, mapGeneralOptionsContent);	
            	}
            	
            	Map mapGeneralSelectedOptionsContent=(Map)request.getSession().getAttribute(MAP_GENERAL_SELECTED_OPTIONS_CONTENT);
            	if (mapGeneralSelectedOptionsContent.size() > 0)
            	{
            		logger.debug("initial test: current mapGeneralSelectedOptionsContent: " + mapGeneralSelectedOptionsContent);
            		mapGeneralSelectedOptionsContent= AuthoringUtil.shiftOptionsMap(mapGeneralSelectedOptionsContent, questionIndex, "down");
                	logger.debug("mapGeneralSelectedOptionsContent after move down: " + mapGeneralSelectedOptionsContent);
                	request.getSession().setAttribute(MAP_GENERAL_SELECTED_OPTIONS_CONTENT, mapGeneralSelectedOptionsContent);	
            	}
            	
            	Map mapWeights= AuthoringUtil.repopulateCurrentWeightsMap(request, "questionWeight");
            	if (mapWeights.size() > 0)
            	{
            		logger.debug("initial test: current mapWeights: " + mapWeights);
            		mapWeights= AuthoringUtil.shiftWeightsMap(mapWeights, questionIndex, "down");
                	logger.debug("mapWeights after move down: " + mapWeights);
                	request.getSession().setAttribute(MAP_WEIGHTS, mapWeights);	
            	}
        	}
     	}
    	
    	mcAuthoringForm.resetUserAction();
		request.getSession().setAttribute(EDIT_OPTIONS_MODE, new Integer(0));
    	logger.debug("resetting  EDIT_OPTIONS_MODE to 0");
        
        int maxQuestionIndex=mapQuestionsContent.size();
    	request.getSession().setAttribute(MAX_QUESTION_INDEX, new Integer(maxQuestionIndex));
    	logger.debug("MAX_QUESTION_INDEX: " +  request.getSession().getAttribute(MAX_QUESTION_INDEX));
    	
    	logger.debug("printing final maps...");
    	Map mapGeneralOptionsContent=(Map)request.getSession().getAttribute(MAP_GENERAL_OPTIONS_CONTENT);
		logger.debug("mapGeneralOptionsContent:"+ mapGeneralOptionsContent);
		
		Map mapGeneralSelectedOptionsContent=(Map)request.getSession().getAttribute(MAP_GENERAL_SELECTED_OPTIONS_CONTENT);
		logger.debug("mapGeneralSelectedOptionsContent " +  mapGeneralSelectedOptionsContent);
		
		logger.debug("sequencing final maps...");
		mapGeneralOptionsContent=AuthoringUtil.sequenceMap(mapGeneralOptionsContent);
		logger.debug("sequenced mapGeneralOptionsContent:"+ mapGeneralOptionsContent);
		request.getSession().setAttribute(MAP_GENERAL_OPTIONS_CONTENT, mapGeneralOptionsContent);
    	
		mapGeneralSelectedOptionsContent=AuthoringUtil.sequenceMap(mapGeneralSelectedOptionsContent);
		logger.debug("sequenced mapGeneralSelectedOptionsContent:"+ mapGeneralSelectedOptionsContent);
		request.getSession().setAttribute(MAP_GENERAL_SELECTED_OPTIONS_CONTENT, mapGeneralSelectedOptionsContent);
        return (mapping.findForward(destination));	
    }
    
    
    /**
     * moves a question entry a step up the questions Map
     * moveQuestionUp(ActionMapping mapping,
            ActionForm form,
            HttpServletRequest request,
            HttpServletResponse response)
     * 
     * @param request
     * @param form
     * @param mapping
     * @return ActionForward
     */
    public ActionForward moveQuestionUp(ActionMapping mapping,
            ActionForm form,
            HttpServletRequest request,
            HttpServletResponse response) throws IOException,
                                         ServletException
    {
    	logger.debug("dispatching moveQuestionUp...");
    	McAuthoringForm mcAuthoringForm = (McAuthoringForm) form;
	 	IMcService mcService =McUtils.getToolService(request);

		/* determine whether the request is from Monitoring url Edit Activity*/
		String sourceMcStarter = (String) request.getAttribute(SOURCE_MC_STARTER);
		logger.debug("sourceMcStarter: " + sourceMcStarter);
		String destination=McUtils.getDestination(sourceMcStarter);
		logger.debug("destination: " + destination);
	 	
	 	AuthoringUtil.readData(request, mcAuthoringForm);
	 	
		Map mapQuestionsContent=AuthoringUtil.repopulateMap(request, "questionContent");
	 	logger.debug("mapQuestionsContent before move down: " + mapQuestionsContent);
	 	
     	//perform a move up if there are at least 2 questions
     	if (mapQuestionsContent.size() > 1)
     	{
     		String questionIndex =mcAuthoringForm.getQuestionIndex();
    		logger.debug("questionIndex:" + questionIndex);
    		String movableQuestionEntry=(String)mapQuestionsContent.get(questionIndex);
    		logger.debug("movableQuestionEntry:" + movableQuestionEntry);
    		
        	if (movableQuestionEntry != null && (!movableQuestionEntry.equals("")))
        	{
        		mapQuestionsContent= AuthoringUtil.shiftMap(mapQuestionsContent, questionIndex,movableQuestionEntry,  "up");
        		logger.debug("mapQuestionsContent after move down: " + mapQuestionsContent);
        		request.getSession().setAttribute(MAP_QUESTIONS_CONTENT, mapQuestionsContent);
        		logger.debug("updated Questions Map: " + request.getSession().getAttribute(MAP_QUESTIONS_CONTENT));
        		
        		Map mapGeneralOptionsContent=(Map)request.getSession().getAttribute(MAP_GENERAL_OPTIONS_CONTENT);
        		if (mapGeneralOptionsContent.size() > 0)
            	{
        			logger.debug("initial test: current mapGeneralOptionsContent: " + mapGeneralOptionsContent);
        	    	mapGeneralOptionsContent= AuthoringUtil.shiftOptionsMap(mapGeneralOptionsContent, questionIndex, "up");
        	    	logger.debug("mapGeneralOptionsContent after move up: " + mapGeneralOptionsContent);
        	    	request.getSession().setAttribute(MAP_GENERAL_OPTIONS_CONTENT, mapGeneralOptionsContent);	
            	}
        		
        		Map mapGeneralSelectedOptionsContent=(Map)request.getSession().getAttribute(MAP_GENERAL_SELECTED_OPTIONS_CONTENT);
            	if (mapGeneralSelectedOptionsContent.size() > 0)
            	{
            		logger.debug("initial test: current mapGeneralSelectedOptionsContent: " + mapGeneralSelectedOptionsContent);
            		mapGeneralSelectedOptionsContent= AuthoringUtil.shiftOptionsMap(mapGeneralSelectedOptionsContent, questionIndex, "up");
                	logger.debug("mapGeneralSelectedOptionsContent after move down: " + mapGeneralSelectedOptionsContent);
                	request.getSession().setAttribute(MAP_GENERAL_SELECTED_OPTIONS_CONTENT, mapGeneralSelectedOptionsContent);	
            	}
            	
            	Map mapWeights= AuthoringUtil.repopulateCurrentWeightsMap(request, "questionWeight");
            	if (mapWeights.size() > 0)
            	{
            		logger.debug("initial test: current mapWeights: " + mapWeights);
            		mapWeights= AuthoringUtil.shiftWeightsMap(mapWeights, questionIndex, "up");
                	logger.debug("mapWeights after move down: " + mapWeights);
                	request.getSession().setAttribute(MAP_WEIGHTS, mapWeights);	
            	}        		
        	}
     	}

     	mcAuthoringForm.resetUserAction();
 		request.getSession().setAttribute(EDIT_OPTIONS_MODE, new Integer(0));
		logger.debug("resetting  EDIT_OPTIONS_MODE to 0");
	    
	    int maxQuestionIndex=mapQuestionsContent.size();
		request.getSession().setAttribute(MAX_QUESTION_INDEX, new Integer(maxQuestionIndex));
		logger.debug("MAX_QUESTION_INDEX: " +  request.getSession().getAttribute(MAX_QUESTION_INDEX));
		
		logger.debug("printing final maps...");
    	Map mapGeneralOptionsContent=(Map)request.getSession().getAttribute(MAP_GENERAL_OPTIONS_CONTENT);
		logger.debug("mapGeneralOptionsContent:"+ mapGeneralOptionsContent);
		
		Map mapGeneralSelectedOptionsContent=(Map)request.getSession().getAttribute(MAP_GENERAL_SELECTED_OPTIONS_CONTENT);
		logger.debug("mapGeneralSelectedOptionsContent " +  mapGeneralSelectedOptionsContent);
		
		logger.debug("sequencing final maps...");
		mapGeneralOptionsContent=AuthoringUtil.sequenceMap(mapGeneralOptionsContent);
		logger.debug("sequenced mapGeneralOptionsContent:"+ mapGeneralOptionsContent);
		request.getSession().setAttribute(MAP_GENERAL_OPTIONS_CONTENT, mapGeneralOptionsContent);
    	
		mapGeneralSelectedOptionsContent=AuthoringUtil.sequenceMap(mapGeneralSelectedOptionsContent);
		logger.debug("sequenced mapGeneralSelectedOptionsContent:"+ mapGeneralSelectedOptionsContent);
		request.getSession().setAttribute(MAP_GENERAL_SELECTED_OPTIONS_CONTENT, mapGeneralSelectedOptionsContent);
		
	    return (mapping.findForward(destination));
    }
    

    /**
     * completes the candidate options screen
     * doneOptions(ActionMapping mapping,
            ActionForm form,
            HttpServletRequest request,
            HttpServletResponse response)
     * 
     * @param request
     * @param form
     * @param mapping
     * @return ActionForward
     */
    public ActionForward doneOptions(ActionMapping mapping,
            ActionForm form,
            HttpServletRequest request,
            HttpServletResponse response) throws IOException,
                                         ServletException
    {
    	logger.debug("dispatching doneOptions...");
    	McAuthoringForm mcAuthoringForm = (McAuthoringForm) form;
	 	IMcService mcService =McUtils.getToolService(request);

		/* determine whether the request is from Monitoring url Edit Activity*/
		String sourceMcStarter = (String) request.getAttribute(SOURCE_MC_STARTER);
		logger.debug("sourceMcStarter: " + sourceMcStarter);
		String destination=McUtils.getDestination(sourceMcStarter);
		logger.debug("destination: " + destination);
	 	
	 	AuthoringUtil.readData(request, mcAuthoringForm);

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
			logger.debug("return to destination to fix error.");
			
			request.getSession().setAttribute(EDIT_OPTIONS_MODE, new Integer(1));
			logger.debug("setting  EDIT_OPTIONS_MODE to 1");
			return (mapping.findForward(destination));	
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
		
		logger.debug("printing final maps...");
    	mapGeneralOptionsContent=(Map)request.getSession().getAttribute(MAP_GENERAL_OPTIONS_CONTENT);
		logger.debug("mapGeneralOptionsContent:"+ mapGeneralOptionsContent);
		
		mapGeneralSelectedOptionsContent=(Map)request.getSession().getAttribute(MAP_GENERAL_SELECTED_OPTIONS_CONTENT);
		logger.debug("mapGeneralSelectedOptionsContent " +  mapGeneralSelectedOptionsContent);
		
		logger.debug("sequencing final maps...");
		mapGeneralOptionsContent=AuthoringUtil.sequenceMap(mapGeneralOptionsContent);
		logger.debug("sequenced mapGeneralOptionsContent:"+ mapGeneralOptionsContent);
		request.getSession().setAttribute(MAP_GENERAL_OPTIONS_CONTENT, mapGeneralOptionsContent);
    	
		mapGeneralSelectedOptionsContent=AuthoringUtil.sequenceMap(mapGeneralSelectedOptionsContent);
		logger.debug("sequenced mapGeneralSelectedOptionsContent:"+ mapGeneralSelectedOptionsContent);
		request.getSession().setAttribute(MAP_GENERAL_SELECTED_OPTIONS_CONTENT, mapGeneralSelectedOptionsContent);
		
		return (mapping.findForward(destination));
    }

   
    /**
     * In the define later mode swithes the content view from view-only to editable and sets the defineLater flag of the content. 
     * 
     * ActionForward editActivityQuestions(ActionMapping mapping,
            ActionForm form,
            HttpServletRequest request,
            HttpServletResponse response) throws IOException,
                                         ServletException,
                                         ToolException
     * 
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
	 	IMcService mcService =McUtils.getToolService(request);
	 	
		/* determine whether the request is from Monitoring url Edit Activity*/
		String sourceMcStarter = (String) request.getAttribute(SOURCE_MC_STARTER);
		logger.debug("sourceMcStarter: " + sourceMcStarter);
		String destination=McUtils.getDestination(sourceMcStarter);
		logger.debug("destination: " + destination);

     	request.getSession().setAttribute(DEFINE_LATER_IN_EDIT_MODE, new Boolean(true));
		McUtils.setDefineLater(request, true);
		
		request.getSession().setAttribute(EDIT_OPTIONS_MODE, new Integer(0));
		logger.debug("setting  EDIT_OPTIONS_MODE to 0");
		
		return (mapping.findForward(destination));
    }
    
    
    /**
     * submits questions Map and persists questions as well as options information in the db.
     * submitQuestions(ActionMapping mapping,
            ActionForm form,
            HttpServletRequest request,
            HttpServletResponse response)
     * 
     * @param request
     * @param form
     * @param mapping
     * @return ActionForward
     */
    public ActionForward submitQuestions(ActionMapping mapping,
            ActionForm form,
            HttpServletRequest request,
            HttpServletResponse response) throws IOException,
                                         ServletException

    {
    	logger.debug("dispatching submitQuestions...");
    	McAuthoringForm mcAuthoringForm = (McAuthoringForm) form;
	 	IMcService mcService =McUtils.getToolService(request);

		/* determine whether the request is from Monitoring url Edit Activity*/
		String sourceMcStarter = (String) request.getAttribute(SOURCE_MC_STARTER);
		logger.debug("sourceMcStarter: " + sourceMcStarter);
		String destination=McUtils.getDestination(sourceMcStarter);
		logger.debug("destination: " + destination);

	 	AuthoringUtil.readData(request, mcAuthoringForm);
	 	
     	request.getSession().setAttribute(EDIT_OPTIONS_MODE, new Integer(0));
		logger.debug("setting  EDIT_OPTIONS_MODE to 0");
	
		ActionMessages errors= new ActionMessages();
		
		Map mapQuestionsContent=AuthoringUtil.repopulateMap(request, "questionContent");
	 	logger.debug("mapQuestionsContent before submit: " + mapQuestionsContent);
	 	request.getSession().setAttribute(MAP_QUESTIONS_CONTENT, mapQuestionsContent);
	 	
	 	
	 	Long toolContentId=(Long)request.getSession().getAttribute(TOOL_CONTENT_ID);
	 	logger.debug("toolContentId:" + toolContentId);
	 	AuthoringUtil.refreshMaps(request, toolContentId.longValue());
		logger.debug("refreshed maps...");
	 	
		Map mapStartupGeneralOptionsContent= (Map) request.getSession().getAttribute(MAP_STARTUP_GENERAL_OPTIONS_CONTENT);
		logger.debug("mapStartupGeneralOptionsContent: " + mapStartupGeneralOptionsContent);
		
		Map mapGeneralOptionsContent=(Map)request.getSession().getAttribute(MAP_GENERAL_OPTIONS_CONTENT);
		logger.debug("mapGeneralOptionsContent: " + mapGeneralOptionsContent);
		
		Boolean isRevisitingUser= (Boolean) request.getSession().getAttribute(IS_REVISITING_USER);
		logger.debug("isRevisitingUser: " + isRevisitingUser);
		
		Map mapTestableOptionsContent=new TreeMap(new McComparator()); 
		if (isRevisitingUser.booleanValue() == true)
		{
			logger.debug("this is a RevisitingUser, include startup general options content in the test");
			mapTestableOptionsContent=AuthoringUtil.mergeMaps(mapStartupGeneralOptionsContent, mapGeneralOptionsContent);
			logger.debug("returned merged map: " + mapTestableOptionsContent);
		}
		else
		{
			logger.debug("this is a NOT RevisitingUser, don't include startup general options content in the test");	
			mapTestableOptionsContent=mapGeneralOptionsContent;
		}
		logger.debug("final mapTestableOptionsContent: " + mapTestableOptionsContent);
		
		logger.debug("remove from mapQuestionsContent the questions with no options");
		/* remove from mapQuestionsContent the questions with no options */
		mapQuestionsContent=AuthoringUtil.updateQuestionsMapForNoOptions(request, mapQuestionsContent, mapTestableOptionsContent);
		logger.debug("returned mapQuestionsContent: " + mapQuestionsContent);
		request.getSession().setAttribute(MAP_QUESTIONS_CONTENT, mapQuestionsContent);
		logger.debug("persisted MAP_QUESTIONS_CONTENT: " + mapQuestionsContent);
		
		Boolean questionsWithNoOptions=(Boolean) request.getSession().getAttribute(QUESTIONS_WITHNO_OPTIONS);
		logger.debug("questionsWithNoOptions: " + questionsWithNoOptions);
		
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
			
			return (mapping.findForward(destination));
		}

		Map mapWeights= AuthoringUtil.repopulateCurrentWeightsMap(request, "questionWeight");
		mapWeights= AuthoringUtil.updateCurrentWeightsMapForQuestionWithNoOptions(request, mapWeights, mapQuestionsContent);
		logger.debug("returned updated mapWeights: " + mapWeights);
		
		logger.debug("MAP_WEIGHTS is valid, persist it to session");
		request.getSession().setAttribute(MAP_WEIGHTS, mapWeights);
		
		boolean weightsValid=validateQuestionWeights(request,mapWeights, mcAuthoringForm);
		logger.debug("weightsValid:" + weightsValid);
		if (weightsValid == false)
		{
			mcAuthoringForm.resetUserAction();
			
			int maxQuestionIndex=mapQuestionsContent.size();
			request.getSession().setAttribute(MAX_QUESTION_INDEX, new Integer(maxQuestionIndex));
			logger.debug("MAX_QUESTION_INDEX: " +  request.getSession().getAttribute(MAX_QUESTION_INDEX));
			
	    	return (mapping.findForward(destination));
		}
		
		
		boolean isTotalWeightsValid=AuthoringUtil.validateTotalWeight(mapWeights);
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
			
	    	return (mapping.findForward(destination));
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
				
				return (mapping.findForward(destination));
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
			logger.debug("return to destination to fix error.");
			
			request.getSession().setAttribute(EDIT_OPTIONS_MODE, new Integer(0));
			logger.debug("setting  EDIT_OPTIONS_MODE to 0");

			int maxQuestionIndex=mapQuestionsContent.size();
			request.getSession().setAttribute(MAX_QUESTION_INDEX, new Integer(maxQuestionIndex));
			logger.debug("MAX_QUESTION_INDEX: " +  request.getSession().getAttribute(MAX_QUESTION_INDEX));
			
	    	return (mapping.findForward(destination));
		}
		
		passmark= new Integer(mcAuthoringForm.getPassmark()).intValue();
		logger.debug("passmark: " + passmark);
		if (passmark > 100)
		{
			errors= new ActionMessages();
			errors.add(Globals.ERROR_KEY,new ActionMessage("error.passMark.greater100"));
			logger.debug("add error.passMark.greater100to ActionMessages");
			saveErrors(request,errors);
			mcAuthoringForm.resetUserAction();
			logger.debug("return to destination to fix error.");
			
			request.getSession().setAttribute(EDIT_OPTIONS_MODE, new Integer(0));
			logger.debug("setting  EDIT_OPTIONS_MODE to 0");

			int maxQuestionIndex=mapQuestionsContent.size();
			request.getSession().setAttribute(MAX_QUESTION_INDEX, new Integer(maxQuestionIndex));
			logger.debug("MAX_QUESTION_INDEX: " +  request.getSession().getAttribute(MAX_QUESTION_INDEX));
			
	    	return (mapping.findForward(destination));
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
			
	    	return (mapping.findForward(destination));
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
		
		McContent mcContent=mcService.retrieveMc(toolContentId);
		logger.debug("existing mcContent:" + mcContent);
		
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
		    mcContent.setEndLearningMessage("Thanks");
		    mcContent.setReportTitle(richTextReportTitle);
		    mcContent.setMonitoringReportTitle(monitoringReportTitle);
		    mcContent.setEndLearningMessage(richTextEndLearningMessage);
		    mcContent.setOfflineInstructions(richTextOfflineInstructions);
		    mcContent.setOnlineInstructions(richTextOnlineInstructions);
		}
		else
		{
			mcContent=AuthoringUtil.createContent(request, mcAuthoringForm);		
			logger.debug("mcContent created");
		}
				    
		mapQuestionsContent=(Map) request.getSession().getAttribute(MAP_QUESTIONS_CONTENT);
		logger.debug("Submit final MAP_QUESTIONS_CONTENT :" + mapQuestionsContent);
		
		Map mapFeedbackIncorrect =(Map)request.getSession().getAttribute(MAP_FEEDBACK_INCORRECT);
		logger.debug("Submit final MAP_FEEDBACK_INCORRECT :" + mapFeedbackIncorrect);
	
		Map mapFeedbackCorrect =(Map)request.getSession().getAttribute(MAP_FEEDBACK_CORRECT);
		logger.debug("Submit final MAP_FEEDBACK_CORRECT :" + mapFeedbackCorrect);
		
		AuthoringUtil.refreshMaps(request, toolContentId.longValue());
		logger.debug("refreshed maps...");
		
		logger.debug("start processing questions content...");
		Long mcContentId =mcContent.getUid();
		List existingQuestions = mcService.refreshQuestionContent(mcContentId);
		logger.debug("existingQuestions: " + existingQuestions);
		
		logger.debug("received MAP_QUESTIONS_CONTENT: " + mapQuestionsContent);
		
		logger.debug("will cleanupRedundantQuestions:");
		AuthoringUtil.cleanupRedundantQuestions(request, existingQuestions, mapQuestionsContent, mcContent);
		
		logger.debug("calling selectAndPersistQuestions: " + existingQuestions);
		AuthoringUtil.selectAndPersistQuestions(request, existingQuestions, mapQuestionsContent, mapFeedbackIncorrect, mapFeedbackCorrect, mapGeneralOptionsContent, mapWeights, mcContent);
		logger.debug("finished processing questions content...");
		
		logger.debug("start processing options content...");
		logger.debug("mapGeneralOptionsContent: " + mapGeneralOptionsContent);
		Map mapGeneralSelectedOptionsContent=(Map)request.getSession().getAttribute(MAP_GENERAL_SELECTED_OPTIONS_CONTENT);
		logger.debug("mapGeneralSelectedOptionsContent: " + mapGeneralSelectedOptionsContent);
		
		logger.debug("will refresh maps...");
		AuthoringUtil.refreshMaps(request, toolContentId.longValue());
		
		mapStartupGeneralOptionsContent= (Map) request.getSession().getAttribute(MAP_STARTUP_GENERAL_OPTIONS_CONTENT);
		logger.debug("mapStartupGeneralOptionsContent: " + mapStartupGeneralOptionsContent);
		Map mapStartupGeneralSelectedOptionsContent=(Map)request.getSession().getAttribute(MAP_STARTUP_GENERAL_SELECTED_OPTIONS_CONTENT);
		logger.debug("mapStartupGeneralSelectedOptionsContent: " + mapStartupGeneralSelectedOptionsContent);
		Map mapStartupGeneralOptionsQueId=(Map) request.getSession().getAttribute(MAP_STARTUP_GENERAL_OPTIONS_QUEID);
		logger.debug("mapStartupGeneralOptionsQueId: " + mapStartupGeneralOptionsQueId);
		
		logger.debug("calling cleanupRedundantOptions");
		AuthoringUtil.cleanupRedundantOptions(request, mapStartupGeneralOptionsContent, mapStartupGeneralSelectedOptionsContent, mapGeneralOptionsContent, mapGeneralSelectedOptionsContent, mapStartupGeneralOptionsQueId);
		
		logger.debug("calling selectAndPersistOptions");
		AuthoringUtil.selectAndPersistOptions(request, mapStartupGeneralOptionsContent, mapStartupGeneralSelectedOptionsContent, mapGeneralOptionsContent, mapGeneralSelectedOptionsContent, mapStartupGeneralOptionsQueId);
		
		logger.debug("start persisting offline files metadata");
		AuthoringUtil.persistFilesMetaData(request, true, mcContent);
		logger.debug("start persisting online files metadata");
		AuthoringUtil.persistFilesMetaData(request, false, mcContent);
		
		/* making sure only the filenames in the session cache are persisted and the others in the db are removed*/ 
		logger.debug("start removing redundant offline files metadata");
		AuthoringUtil.removeRedundantOfflineFileItems(request, mcContent);
		
		logger.debug("start removing redundant online files metadata");
		AuthoringUtil.removeRedundantOnlineFileItems(request, mcContent);
		
 		logger.debug("done removing redundant files");
		
		errors.clear();
		errors.add(Globals.ERROR_KEY,new ActionMessage("sbmt.successful"));
		logger.debug("add sbmt.successful to ActionMessages");
		saveErrors(request,errors);
		request.setAttribute(SUBMIT_SUCCESS, new Integer(1));
		logger.debug("set SUBMIT_SUCCESS to 1");
		
		request.getSession().setAttribute(EDIT_OPTIONS_MODE, new Integer(0));
		logger.debug("setting  EDIT_OPTIONS_MODE to 0");
	
		mcAuthoringForm.resetUserAction();
		
		int maxQuestionIndex=mapQuestionsContent.size();
		request.getSession().setAttribute(MAX_QUESTION_INDEX, new Integer(maxQuestionIndex));
		logger.debug("MAX_QUESTION_INDEX: " +  request.getSession().getAttribute(MAX_QUESTION_INDEX));
		
		logger.debug("printing final maps...");
    	mapGeneralOptionsContent=(Map)request.getSession().getAttribute(MAP_GENERAL_OPTIONS_CONTENT);
		logger.debug("mapGeneralOptionsContent:"+ mapGeneralOptionsContent);
		
		mapGeneralSelectedOptionsContent=(Map)request.getSession().getAttribute(MAP_GENERAL_SELECTED_OPTIONS_CONTENT);
		logger.debug("mapGeneralSelectedOptionsContent " +  mapGeneralSelectedOptionsContent);
		
		logger.debug("sequencing final maps...");
		mapGeneralOptionsContent=AuthoringUtil.sequenceMap(mapGeneralOptionsContent);
		logger.debug("sequenced mapGeneralOptionsContent:"+ mapGeneralOptionsContent);
		request.getSession().setAttribute(MAP_GENERAL_OPTIONS_CONTENT, mapGeneralOptionsContent);
    	
		mapGeneralSelectedOptionsContent=AuthoringUtil.sequenceMap(mapGeneralSelectedOptionsContent);
		logger.debug("sequenced mapGeneralSelectedOptionsContent:"+ mapGeneralSelectedOptionsContent);
		request.getSession().setAttribute(MAP_GENERAL_SELECTED_OPTIONS_CONTENT, mapGeneralSelectedOptionsContent);
		
		logger.debug("start unsetting define later with current toolContentId: " + toolContentId);
		mcService.unsetAsDefineLater(toolContentId);
		logger.debug("unset defineLater with toolContentId to true: " + toolContentId);
	
		logger.debug("resetting defineLater flag. ");
		McUtils.setDefineLater(request, false);
		
		/* switch to view-only mode in the monitoring url after the content is persisted*/
		if (destination.equals(LOAD_MONITORING))
		{
			request.getSession().setAttribute(ACTIVE_MODULE, DEFINE_LATER);
			request.getSession().setAttribute(DEFINE_LATER_IN_EDIT_MODE, new Boolean(false));	
		}
		return (mapping.findForward(destination));
    }


    /**
     * removes offline file data 
     * deleteFileItem(ActionMapping mapping,
            ActionForm form,
            HttpServletRequest request,
            HttpServletResponse response)
     * 
     * @param request
     * @param form
     * @param mapping
     * @return ActionForward
     */
    public ActionForward deleteOfflineFile(ActionMapping mapping,
            ActionForm form,
            HttpServletRequest request,
            HttpServletResponse response) throws IOException,
                                         ServletException

    {
    	logger.debug("dispatching deleteOfflineFile...");
    	McAuthoringForm mcAuthoringForm = (McAuthoringForm) form;
	 	IMcService mcService =McUtils.getToolService(request);
	 	
		/* determine whether the request is from Monitoring url Edit Activity*/
		String sourceMcStarter = (String) request.getAttribute(SOURCE_MC_STARTER);
		logger.debug("sourceMcStarter: " + sourceMcStarter);
		String destination=McUtils.getDestination(sourceMcStarter);
		logger.debug("destination: " + destination);
	 	
	 	AuthoringUtil.readData(request, mcAuthoringForm);
	 	
	 	String uuid =mcAuthoringForm.getUuid();
	 	logger.debug("uuid:" + uuid);
	 	
	 	List listOfflineFilesMetaData =(List)request.getSession().getAttribute(LIST_OFFLINEFILES_METADATA);
 		logger.debug("listOfflineFilesMetaData:" + listOfflineFilesMetaData);
 		listOfflineFilesMetaData=AuthoringUtil.removeFileItem(listOfflineFilesMetaData, uuid);
 		logger.debug("listOfflineFilesMetaData after remove:" + listOfflineFilesMetaData);
 		request.getSession().setAttribute(LIST_OFFLINEFILES_METADATA, listOfflineFilesMetaData);
	 	
        mcAuthoringForm.resetUserAction();
        return (mapping.findForward(destination));
    	
    }


    /**
     * removes online file data 
     * deleteFileItem(ActionMapping mapping,
            ActionForm form,
            HttpServletRequest request,
            HttpServletResponse response)
     * 
     * @param request
     * @param form
     * @param mapping
     * @return ActionForward
     */
    public ActionForward deleteOnlineFile(ActionMapping mapping,
            ActionForm form,
            HttpServletRequest request,
            HttpServletResponse response) throws IOException,
                                         ServletException

    {
    	logger.debug("dispatching deleteOnlineFile...");
    	McAuthoringForm mcAuthoringForm = (McAuthoringForm) form;
	 	IMcService mcService =McUtils.getToolService(request);

		/* determine whether the request is from Monitoring url Edit Activity*/
		String sourceMcStarter = (String) request.getAttribute(SOURCE_MC_STARTER);
		logger.debug("sourceMcStarter: " + sourceMcStarter);
		String destination=McUtils.getDestination(sourceMcStarter);
		logger.debug("destination: " + destination);

	 	AuthoringUtil.readData(request, mcAuthoringForm);
	 	
	 	String uuid =mcAuthoringForm.getUuid();
	 	logger.debug("uuid:" + uuid);
	 	
	 	List listOnlineFilesMetaData =(List)request.getSession().getAttribute(LIST_ONLINEFILES_METADATA);
 		logger.debug("listOnlineFilesMetaData:" + listOnlineFilesMetaData);
 		listOnlineFilesMetaData=AuthoringUtil.removeFileItem(listOnlineFilesMetaData, uuid);
 		logger.debug("listOnlineFilesMetaData after remove:" + listOnlineFilesMetaData);
 		request.getSession().setAttribute(LIST_ONLINEFILES_METADATA, listOnlineFilesMetaData);
	 	
        mcAuthoringForm.resetUserAction();
        return (mapping.findForward(destination));
    }

    
     /**
     * adds the offline file information in the content repository.
     * submitOfflineFiles(ActionMapping mapping,
            ActionForm form,
            HttpServletRequest request,
            HttpServletResponse response)
     * 
     * @param request
     * @param form
     * @param mapping
     * @return ActionForward
     */
    public ActionForward submitOfflineFiles(ActionMapping mapping,
            ActionForm form,
            HttpServletRequest request,
            HttpServletResponse response) throws IOException,
                                         ServletException,
                                         RepositoryCheckedException
    {
    	logger.debug("dispatching submitOfflineFile...");
    	McAuthoringForm mcAuthoringForm = (McAuthoringForm) form;
	 	IMcService mcService =McUtils.getToolService(request);

		/* determine whether the request is from Monitoring url Edit Activity*/
		String sourceMcStarter = (String) request.getAttribute(SOURCE_MC_STARTER);
		logger.debug("sourceMcStarter: " + sourceMcStarter);
		String destination=McUtils.getDestination(sourceMcStarter);
		logger.debug("destination: " + destination);

	 	AuthoringUtil.readData(request, mcAuthoringForm);
	 	
		logger.debug("will uploadFile for offline file:");
 		McAttachmentDTO mcAttachmentDTO=AuthoringUtil.uploadFile(request, mcAuthoringForm, true);
 		logger.debug("returned mcAttachmentDTO:" + mcAttachmentDTO);
 		
 		if (mcAttachmentDTO == null)
 		{
 			ActionMessages errors= new ActionMessages();
 			errors= new ActionMessages();
 			errors.add(Globals.ERROR_KEY,new ActionMessage("error.fileName.empty"));
 			saveErrors(request,errors);
 			mcAuthoringForm.resetUserAction();
 			persistError(request,"error.fileName.empty");
 			
 			request.getSession().setAttribute(EDIT_OPTIONS_MODE, new Integer(0));
 	 		logger.debug("setting EDIT_OPTIONS_MODE :" + 0);
 	   	    return (mapping.findForward(destination));	
 		}
 		
 		 		
 		List listOfflineFilesMetaData =(List)request.getSession().getAttribute(LIST_OFFLINEFILES_METADATA);
 		logger.debug("listOfflineFilesMetaData:" + listOfflineFilesMetaData);
 		listOfflineFilesMetaData.add(mcAttachmentDTO);
 		logger.debug("listOfflineFilesMetaData after add:" + listOfflineFilesMetaData);
 		request.getSession().setAttribute(LIST_OFFLINEFILES_METADATA, listOfflineFilesMetaData);
		
 		mcAuthoringForm.resetUserAction();
 		request.getSession().setAttribute(EDIT_OPTIONS_MODE, new Integer(0));
 		logger.debug("setting EDIT_OPTIONS_MODE :" + 0);
   	    return (mapping.findForward(destination));    
    }
    
    
    /**
     * adds the online file information in the content repository.
     * submitOnlineFiles(ActionMapping mapping,
            ActionForm form,
            HttpServletRequest request,
            HttpServletResponse response)
     * 
     * @param request
     * @param form
     * @param mapping
     * @return ActionForward
     */
    public ActionForward submitOnlineFiles(ActionMapping mapping,
            ActionForm form,
            HttpServletRequest request,
            HttpServletResponse response) throws IOException,
                                         ServletException,
                                         RepositoryCheckedException
    {
    	logger.debug("dispatching submitOnlineFiles...");
    	McAuthoringForm mcAuthoringForm = (McAuthoringForm) form;
	 	IMcService mcService =McUtils.getToolService(request);

		/* determine whether the request is from Monitoring url Edit Activity*/
		String sourceMcStarter = (String) request.getAttribute(SOURCE_MC_STARTER);
		logger.debug("sourceMcStarter: " + sourceMcStarter);
		String destination=McUtils.getDestination(sourceMcStarter);
		logger.debug("destination: " + destination);

	 	AuthoringUtil.readData(request, mcAuthoringForm);
	 	
		logger.debug("will uploadFile for online file:");
 		McAttachmentDTO mcAttachmentDTO=AuthoringUtil.uploadFile(request, mcAuthoringForm, false);
 		logger.debug("returned mcAttachmentDTO:" + mcAttachmentDTO);
 		
 		if (mcAttachmentDTO == null)
 		{
 			ActionMessages errors= new ActionMessages();
 			errors= new ActionMessages();
 			errors.add(Globals.ERROR_KEY,new ActionMessage("error.fileName.empty"));
 			saveErrors(request,errors);
 			mcAuthoringForm.resetUserAction();
 			persistError(request,"error.fileName.empty");
 			
 			request.getSession().setAttribute(EDIT_OPTIONS_MODE, new Integer(0));
 	 		logger.debug("setting EDIT_OPTIONS_MODE :" + 0);
 	   	    return (mapping.findForward(destination));	
 		}
 		 		
 		List listOnlineFilesMetaData =(List)request.getSession().getAttribute(LIST_ONLINEFILES_METADATA);
 		logger.debug("listOnlineFilesMetaData:" + listOnlineFilesMetaData);
 		listOnlineFilesMetaData.add(mcAttachmentDTO);
 		logger.debug("listOnlineFilesMetaData after add:" + listOnlineFilesMetaData);
 		request.getSession().setAttribute(LIST_ONLINEFILES_METADATA, listOnlineFilesMetaData);
 		
        mcAuthoringForm.resetUserAction();
        request.getSession().setAttribute(EDIT_OPTIONS_MODE, new Integer(0));
 		logger.debug("setting EDIT_OPTIONS_MODE :" + 0);
   	    return (mapping.findForward(destination));
    }
    
    
    /**
     * moves from Advanced Tab to Basic Tab
     * doneAdvancedTab(ActionMapping mapping,
            ActionForm form,
            HttpServletRequest request,
            HttpServletResponse response)
     * 
     * @param request
     * @param form
     * @param mapping
     * @return ActionForward
     */
    public ActionForward doneAdvancedTab(ActionMapping mapping,
            ActionForm form,
            HttpServletRequest request,
            HttpServletResponse response) throws IOException,
                                         ServletException
    {
    	logger.debug("dispatching doneAdvancedTab...");
    	McAuthoringForm mcAuthoringForm = (McAuthoringForm) form;
	 	IMcService mcService =McUtils.getToolService(request);

		/* determine whether the request is from Monitoring url Edit Activity*/
		String sourceMcStarter = (String) request.getAttribute(SOURCE_MC_STARTER);
		logger.debug("sourceMcStarter: " + sourceMcStarter);
		String destination=McUtils.getDestination(sourceMcStarter);
		logger.debug("destination: " + destination);

	 	AuthoringUtil.readData(request, mcAuthoringForm);

        mcAuthoringForm.resetUserAction();
        return (mapping.findForward(destination));
    }
    
    
    /**
     * moves from Instructions Tab to Basic Tab
     * doneInstructionsTab(ActionMapping mapping,
            ActionForm form,
            HttpServletRequest request,
            HttpServletResponse response)
     * 
     * @param request
     * @param form
     * @param mapping
     * @return ActionForward
     */
    public ActionForward doneInstructionsTab(ActionMapping mapping,
            ActionForm form,
            HttpServletRequest request,
            HttpServletResponse response) throws IOException,
                                         ServletException
    {
    	logger.debug("dispatching doneInstructionsTab...");
    	McAuthoringForm mcAuthoringForm = (McAuthoringForm) form;
	 	IMcService mcService =McUtils.getToolService(request);

	 	/* determine whether the request is from Monitoring url Edit Activity*/
		String sourceMcStarter = (String) request.getAttribute(SOURCE_MC_STARTER);
		logger.debug("sourceMcStarter: " + sourceMcStarter);
		String destination=McUtils.getDestination(sourceMcStarter);
		logger.debug("destination: " + destination);

	 	AuthoringUtil.readData(request, mcAuthoringForm);
	 	
		mcAuthoringForm.resetUserAction();
	    return (mapping.findForward(destination));
    }

    
    /**Double check: we assume that the author have to use Cancel button to exit the authoring module.
     * This is the place to remove any authoring modle related session attributes
     * 
     * author exiting, remove any session attributes that belong to authoring module here
     * cancelAuthoring(ActionMapping mapping,
            ActionForm form,
            HttpServletRequest request,
            HttpServletResponse response) throws IOException,
                                         ServletException
     * 
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return ActionForward
     * @throws IOException
     * @throws ServletException
     */
    public ActionForward cancelAuthoring(ActionMapping mapping,
            ActionForm form,
            HttpServletRequest request,
            HttpServletResponse response) throws IOException,
                                         ServletException
    {
    	logger.debug("dispatching cancelAuthoring...");
    	McAuthoringForm mcAuthoringForm = (McAuthoringForm) form;
	 	

	 	/* determine whether the request is from Monitoring url Edit Activity*/
		String sourceMcStarter = (String) request.getAttribute(SOURCE_MC_STARTER);
		logger.debug("sourceMcStarter: " + sourceMcStarter);
		
		mcAuthoringForm.resetUserAction();
		
		/*remove attribues */
		request.getSession().removeAttribute(SHOW_AUTHORING_TABS);
		request.getSession().removeAttribute(DEFINE_LATER_IN_EDIT_MODE);
		request.getSession().removeAttribute(ACTIVE_MODULE);
		logger.debug("removed attribues...");
		
		/* Check this: find out where to forward to*/
	    return (mapping.findForward(AUTHORING_STARTER));
    }
    
    	
	/**
     * ensures that the weight valued entered are valid
     * validateQuestionWeights(HttpServletRequest request, McAuthoringForm mcAuthoringForm)
     * 
     * @param request
     * @param mcAuthoringForm
     * @return
     */
    protected boolean validateQuestionWeights(HttpServletRequest request, Map mapWeights, McAuthoringForm mcAuthoringForm)
    {
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
    
}
    
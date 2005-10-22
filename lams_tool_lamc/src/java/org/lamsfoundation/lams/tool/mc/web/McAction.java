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

import java.io.IOException;
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
	 	
	 	McUtils.persistRichText(request);
	 	
	 	
	 	String userAction=null;
	 	if (mcAuthoringForm.getAddQuestion() != null)
	 	{
	 		userAction="addQuestion";
	 		request.setAttribute(USER_ACTION, userAction);
	 		logger.debug("userAction:" + userAction);
	 		
	 		Map mapQuestionsContent=repopulateMap(request, "questionContent");
		 	logger.debug("mapQuestionsContent after shrinking: " + mapQuestionsContent);
		 	logger.debug("mapQuestionsContent size after shrinking: " + mapQuestionsContent.size());
	 		
		 	addQuestion(request, mcAuthoringForm, mapQuestionsContent, true);
	 		logger.debug("after addQuestion");
	 		
    	    mcAuthoringForm.resetUserAction();
	 		return (mapping.findForward(LOAD_QUESTIONS));
	 	}
	 	else if (mcAuthoringForm.getRemoveQuestion() != null)
	 	{
	 		userAction="removeQuestion";
	 		request.setAttribute(USER_ACTION, userAction);
	 		logger.debug("userAction:" + userAction);
	 		
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
				/** just present the Map as it is */ 
				request.getSession().setAttribute(MAP_QUESTIONS_CONTENT, mapQuestionsContent);
				logger.debug("updated Questions Map: " + request.getSession().getAttribute(MAP_QUESTIONS_CONTENT));
			}
			
			
			mcAuthoringForm.resetUserAction();
			return (mapping.findForward(LOAD_QUESTIONS));
	 	}
	 	else if (mcAuthoringForm.getEditOptions() != null)
	 	{
	 		userAction="editOption";
	 		request.setAttribute(USER_ACTION, userAction);
	 		logger.debug("userAction:" + userAction);

	 		Map mapQuestionsContent=repopulateMap(request, "questionContent");
		 	logger.debug("mapQuestionsContent after shrinking: " + mapQuestionsContent);
		 	logger.debug("mapQuestionsContent size after shrinking: " + mapQuestionsContent.size());
		 	request.getSession().setAttribute(MAP_QUESTIONS_CONTENT, mapQuestionsContent);
	 		
	 		String questionIndex =mcAuthoringForm.getQuestionIndex();
			logger.debug("questionIndex:" + questionIndex);
			
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
    			return (mapping.findForward(LOAD_QUESTIONS));
			}
			
			
			Long toolContentId=(Long)request.getSession().getAttribute(TOOL_CONTENT_ID);
			logger.debug("toolContentId:" + toolContentId);
			
			McContent mcContent=mcService.retrieveMc(toolContentId);
			logger.debug("mcContent:" + mcContent);
			
			if (mcContent == null)
			{
				logger.debug("convenience add");
				addQuestion(request, mcAuthoringForm, mapQuestionsContent, false);
		 		logger.debug("after addQuestion");
		 		mcContent=mcService.retrieveMc(toolContentId);
		 		logger.debug("mcContent:" + mcContent);
			}
			logger.debug("mcContent uid :" + mcContent.getUid());
			
	    	McQueContent mcQueContent =mcService.getQuestionContentByQuestionText(editableQuestionEntry, mcContent.getUid());
	    	logger.debug("mcQueContent:" + mcQueContent);
	    	
	    	if (mcQueContent == null)
        	{
	    		logger.debug("convenience add mcQueContent");
        		mcQueContent=  new McQueContent(editableQuestionEntry,
          	        	 	new Integer(questionIndex),
         					mcContent,
         					new HashSet(),
         					new HashSet()
         					);
       	        mcService.createMcQue(mcQueContent);
       	        logger.debug("persisted convenience mcQueContent: " + mcQueContent);
        	}
	    	
	    	
	    	request.getSession().setAttribute(SELECTED_QUESTION, mcQueContent.getQuestion());
	    	logger.debug("SELECTED_QUESTION:" + request.getSession().getAttribute(SELECTED_QUESTION));
	    	
			request.getSession().setAttribute(SELECTED_QUESTION_CONTENT_UID, mcQueContent.getUid() );
			logger.debug("SELECTED_QUESTION_CONTENT_UID:" + request.getSession().getAttribute(SELECTED_QUESTION_CONTENT_UID));
			
			request.getSession().setAttribute(SELECTED_QUESTION_INDEX, questionIndex);
	    	logger.debug("SELECTED_QUESTION_INDEX:" + request.getSession().getAttribute(SELECTED_QUESTION_INDEX));
			
			Map mapOptionsContent= new TreeMap(new McComparator());
			logger.debug("initialized mapOptionsContent:" + mapOptionsContent);
			
			if (mcQueContent != null)
			{
				/** hold all he options for this question*/
				List list=mcService.findMcOptionsContentByQueId(mcQueContent.getUid());
		    	logger.debug("options list:" + list);
		    	
		    	if (list.size() == 0)
		    	{
				 	logger.debug("empty options list: this is a new question content created by the user and has no options yet");
					mapOptionsContent=(Map) request.getSession().getAttribute(MAP_DEFAULTOPTIONS_CONTENT);
				 	logger.debug("mapOptionsContent is the default options Map: " + mapOptionsContent);
		    	}
		    	else
		    	{
		    	 	logger.debug("not an empty options list");
		    		Iterator listIterator=list.iterator();
			    	Long mapIndex=new Long(1);
			    	while (listIterator.hasNext())
			    	{
			    		McOptsContent mcOptsContent=(McOptsContent)listIterator.next();
			    		logger.debug("option text:" + mcOptsContent.getMcQueOptionText());
			    		mapOptionsContent.put(mapIndex.toString(),mcOptsContent.getMcQueOptionText());
			    		mapIndex=new Long(mapIndex.longValue()+1);
			    	}
		    	}
		    	request.getSession().setAttribute(MAP_OPTIONS_CONTENT, mapOptionsContent);
				logger.debug("updated the Options Map: " + request.getSession().getAttribute(MAP_OPTIONS_CONTENT));
			}
			else
			{
				/** this is a new question content created by the user and has no options yet  */
			 	logger.debug("this is a new question content created by the user and has no options yet");
				mapOptionsContent=(Map) request.getSession().getAttribute(MAP_DEFAULTOPTIONS_CONTENT);
			 	logger.debug("mapOptionsContent is the default options Map: " + mapOptionsContent);
			 	request.getSession().setAttribute(MAP_OPTIONS_CONTENT, mapOptionsContent);
				logger.debug("updated the Options Map with the default Map: " + request.getSession().getAttribute(MAP_OPTIONS_CONTENT));
			}
			
			mcAuthoringForm.resetUserAction();
			return (mapping.findForward(EDIT_OPTS_CONTENT));
	 		
	 	}
	 	else if (mcAuthoringForm.getAddOption() != null)
	 	{
	 		userAction="addOption";
	 		request.setAttribute(USER_ACTION, userAction);
	 		logger.debug("userAction:" + userAction);
	 		
	 		Map mapOptionsContent=repopulateMap(request,"optionContent");
		 	logger.debug("mapOptionsContent after shrinking: " + mapOptionsContent);
		 	logger.debug("mapOptionsContent size after shrinking: " + mapOptionsContent.size());
	 		
	 		int mapSize=mapOptionsContent.size();
	 		mapOptionsContent.put(new Long(++mapSize).toString(), "");
			logger.debug("updated mapOptionsContent Map size: " + mapOptionsContent.size());
			request.getSession().setAttribute(MAP_OPTIONS_CONTENT, mapOptionsContent);
    		logger.debug("updated Options Map: " + request.getSession().getAttribute(MAP_OPTIONS_CONTENT));
    		
    		Long selectedQuestionContentUid=(Long) request.getSession().getAttribute(SELECTED_QUESTION_CONTENT_UID);
	 		logger.debug("selectedQuestionContentUid:" + selectedQuestionContentUid);
	 		
	 		McQueContent mcQueContent = mcService.retrieveMcQueContentByUID(selectedQuestionContentUid);
			logger.debug("mcQueContent:" + mcQueContent);
			
			mcService.removeMcOptionsContentByQueId(selectedQuestionContentUid);
			logger.debug("removed all mcOptionsContents for mcQueContentId :" + selectedQuestionContentUid);
			
	 		if (mcQueContent != null)
	 		{
	 			/** iterate the options Map and persist the options into the DB*/
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
	 		
	 		
	 		mcAuthoringForm.resetUserAction();
			return (mapping.findForward(EDIT_OPTS_CONTENT));
	 	}
	 	else if (mcAuthoringForm.getRemoveOption() != null)
	 	{
	 		userAction="removeOption";
	 		request.setAttribute(USER_ACTION, userAction);
	 		logger.debug("userAction:" + userAction);
	 		
	 		String optionIndex =mcAuthoringForm.getOptionIndex();
			logger.debug("optionIndex:" + optionIndex);
			
			Map mapOptionsContent=repopulateMap(request, "optionContent");
		 	logger.debug("mapOptionsContent after shrinking: " + mapOptionsContent);
		 	logger.debug("mapOptionsContent size after shrinking: " + mapOptionsContent.size());
		 	
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
			
			mcAuthoringForm.resetUserAction();			
			return (mapping.findForward(EDIT_OPTS_CONTENT));
	 	}
	 	else if (mcAuthoringForm.getDoneOptions() != null)
	 	{
	 		userAction="doneOptions";
	 		request.setAttribute(USER_ACTION, userAction);
	 		logger.debug("userAction:" + userAction);
	 		
	 		/** make the particular question content in the main page disabled for user access*/
	 		Long selectedQuestionContentUid=(Long) request.getSession().getAttribute(SELECTED_QUESTION_CONTENT_UID);
	 		logger.debug("selectedQuestionContentUid:" + selectedQuestionContentUid);
	 		
	 		McQueContent mcQueContent = mcService.retrieveMcQueContentByUID(selectedQuestionContentUid);
			logger.debug("mcQueContent:" + mcQueContent);
			
			
			/** parse all the options and persist them */
			Map mapOptionsContent=repopulateMap(request,"optionContent");
		 	logger.debug("mapOptionsContent after shrinking: " + mapOptionsContent);
		 	logger.debug("mapOptionsContent size after shrinking: " + mapOptionsContent.size());
	 		request.getSession().setAttribute(MAP_OPTIONS_CONTENT, mapOptionsContent);
    		logger.debug("Options Map: " + request.getSession().getAttribute(MAP_OPTIONS_CONTENT));
    		
			mcService.removeMcOptionsContentByQueId(selectedQuestionContentUid);
			logger.debug("removed all mcOptionsContents for mcQueContentId :" + selectedQuestionContentUid);
			
			if (mcQueContent != null)
	 		{
	 			/** iterate the options Map and persist the options into the DB*/
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
			logger.debug("doneOptions persists all the options");
			
			
			
			mcAuthoringForm.resetUserAction();
			return (mapping.findForward(LOAD_QUESTIONS));
	 	}
	 	else if (mcAuthoringForm.getSubmitQuestions() != null)
	 	{
	 		/** persist the final Questions Map  */
	 		userAction="submitQuestions";
	 		request.setAttribute(USER_ACTION, userAction);
	 		logger.debug("userAction:" + userAction);

        	ActionMessages errors= new ActionMessages();
	 		
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
    			logger.debug("either title or instructions or both is missingr. Returning back to from to fix errors:");
    			return (mapping.findForward(LOAD_QUESTIONS));
    		}
    		
    		
	 		Map mapQuestionsContent=repopulateMap(request, "questionContent");
		 	logger.debug("FINAL mapQuestionsContent after shrinking: " + mapQuestionsContent);
		 	logger.debug("mapQuestionsContent size after shrinking: " + mapQuestionsContent.size());
		 	request.getSession().setAttribute(MAP_QUESTIONS_CONTENT, mapQuestionsContent);
		 	
		 	Long toolContentId=(Long)request.getSession().getAttribute(TOOL_CONTENT_ID);
	 	    logger.debug("toolContentId:" + toolContentId);
				
			McContent mcContent=mcService.retrieveMc(toolContentId);
			logger.debug("mcContent:" + mcContent);
			
			logger.debug("updating mcContent title and instructions:" + mcContent);
			mcContent.setTitle(richTextTitle);
			mcContent.setInstructions(richTextInstructions);
			
			
			mcService.resetAllQuestions(mcContent.getUid());
			logger.debug("all question reset for :" + mcContent.getUid());
			 
			Iterator itMap = mapQuestionsContent.entrySet().iterator();
			while (itMap.hasNext()) {
			    Map.Entry pairs = (Map.Entry)itMap.next();
			    if ((pairs.getValue() != null) && (!pairs.getValue().equals("")))
			    {
			    	McQueContent mcQueContent = mcService.getQuestionContentByQuestionText(pairs.getValue().toString(), mcContent.getUid());
			    	logger.debug("retrieved mcQueContent: " + mcQueContent);
			    	
			    	if (mcQueContent != null)
			    	{
			    		mcQueContent.setDisabled(false);
				    	logger.debug("enabled mcQueContent for question: " + pairs.getValue().toString());
				    	mcService.saveOrUpdateMcQueContent(mcQueContent);
			    	}
			    }
 	        }
			
			/** attend here later again, for the moment we are not deleting unused question physically from the DB, 
			 * we are just marking them as disabled */
			mcService.cleanAllQuestions(mcContent.getUid());
			logger.debug("all questions cleaned for :" + mcContent.getUid());
			
			errors.clear();
			errors.add(Globals.ERROR_KEY,new ActionMessage("submit.successful"));
			logger.debug("add submit.successful to ActionMessages");
			saveErrors(request,errors);
			
			mcAuthoringForm.resetUserAction();
	   	    return (mapping.findForward(LOAD_QUESTIONS));
	 	}
	 	
	 	
	 	mcAuthoringForm.resetUserAction();
   	    return (mapping.findForward(LOAD_QUESTIONS));
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
			String candidateQuestionEntry =request.getParameter(parameterType + i);
			if (
				(candidateQuestionEntry != null) && 
				(candidateQuestionEntry.length() > 0)   
				)
			{
				mapCounter++;
				mapTempQuestionsContent.put(new Long(mapCounter).toString(), candidateQuestionEntry);
			}
		}
    	logger.debug("return repopulated Map: " + mapTempQuestionsContent);
    	return mapTempQuestionsContent;
    }
    
    
    protected McContent createContent(HttpServletRequest request, McAuthoringForm mcAuthoringForm)
    {
    	IMcService mcService =McUtils.getToolService(request);
    	
    	/** the tool content id is passed from the container to the tool and placed into session in the McStarterAction */
    	Long toolContentId=(Long)request.getSession().getAttribute(TOOL_CONTENT_ID);
    	if ((toolContentId != null) && (toolContentId.longValue() != 0))
    	{
    		logger.debug("passed TOOL_CONTENT_ID : " + toolContentId);
    		/**delete the existing content in the database before applying new content*/
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
    	
    	logger.debug("isShowFeedback: " +  mcAuthoringForm.getShowFeedback());
    	if (mcAuthoringForm.getShowFeedback().equalsIgnoreCase(ON))
    		isShowFeedback=true;
    	    	
    	logger.debug("MONITORING_REPORT_TITLE: " +  mcAuthoringForm.getMonitoringReportTitle());
    	if (mcAuthoringForm.getMonitoringReportTitle() == null)
    		monitoringReportTitle=(String)request.getSession().getAttribute(MONITORING_REPORT_TITLE);
    	
    	logger.debug("REPORT_TITLE: " +  mcAuthoringForm.getReportTitle());
    	if (mcAuthoringForm.getReportTitle() == null)
    		reportTitle=(String)request.getSession().getAttribute(REPORT_TITLE);
    	
    	logger.debug("OFFLINE_INSTRUCTIONS: " +  mcAuthoringForm.getOfflineInstructions());
    	if (mcAuthoringForm.getOfflineInstructions() == null)
    		offlineInstructions=(String)request.getSession().getAttribute(OFFLINE_INSTRUCTIONS);

    	logger.debug("ONLINE_INSTRUCTIONS: " +  mcAuthoringForm.getOnlineInstructions());
    	if (mcAuthoringForm.getOnlineInstructions() == null)
    		onlineInstructions=(String)request.getSession().getAttribute(ONLINE_INSTRUCTIONS);
		
    	logger.debug("END_LEARNING_MESSAGE: " +  mcAuthoringForm.getEndLearningMessage());
    	if (mcAuthoringForm.getEndLearningMessage() == null)
    		endLearningMessage=(String)request.getSession().getAttribute(END_LEARNING_MESSAGE);

    	String richTextTitle="";
    	richTextTitle = (String)request.getSession().getAttribute(RICHTEXT_TITLE);
    	logger.debug("createContent richTextTitle from session: " + richTextTitle);
    	if (richTextTitle == null) richTextTitle="";
    	
    	String richTextInstructions="";
    	richTextInstructions = (String)request.getSession().getAttribute(RICHTEXT_INSTRUCTIONS);
    	logger.debug("createContent richTextInstructions from session: " + richTextInstructions);
    	if (richTextInstructions == null) richTextInstructions="";
    	
    	creationDate=(String)request.getSession().getAttribute(CREATION_DATE);
		if (creationDate == null)
			creationDate=new Date(System.currentTimeMillis()).toString();
		
    		
    	/**obtain user object from the session*/
	    HttpSession ss = SessionManager.getSession();
	    //get back login user DTO
	    UserDTO toolUser = (UserDTO) ss.getAttribute(AttributeNames.USER);
    	logger.debug("retrieving toolUser: " + toolUser);
    	logger.debug("retrieving toolUser userId: " + toolUser.getUserID());
    	String fullName= toolUser.getFirstName() + " " + toolUser.getLastName();
    	logger.debug("retrieving toolUser fullname: " + fullName);
    	long userId=toolUser.getUserID().longValue();
    	logger.debug("userId: " + userId);
    	
    	/** create a new qa content and leave the default content intact*/
    	McContent mc = new McContent();
		mc.setMcContentId(toolContentId);
		mc.setTitle(richTextTitle);
		mc.setInstructions(richTextInstructions);
		mc.setCreationDate(creationDate); /**preserve this from the db*/ 
		mc.setUpdateDate(new Date(System.currentTimeMillis())); /**keep updating this one*/
		mc.setCreatedBy(userId); /**make sure we are setting the userId from the User object above*/
	    mc.setUsernameVisible(isUsernameVisible);
	    mc.setQuestionsSequenced(isQuestionsSequenced); /**the default question listing in learner mode will be all in the same page*/
	    mc.setOnlineInstructions(onlineInstructions);
	    mc.setOfflineInstructions(offlineInstructions);
	    mc.setRunOffline(false);
	    mc.setDefineLater(false);
	    mc.setSynchInMonitor(isSynchInMonitor);
	    mc.setEndLearningMessage(endLearningMessage);
	    mc.setReportTitle(reportTitle);
	    mc.setMonitoringReportTitle(monitoringReportTitle);
	    mc.setEndLearningMessage(endLearningMessage);
	    mc.setRetries(isRetries);
	    mc.setPassMark(new Integer(passmark));
	    mc.setShowFeedback(isShowFeedback);
	    mc.setMcQueContents(new TreeSet());
	    mc.setMcSessions(new TreeSet());
	    logger.debug("mc content :" +  mc);
    	
    	/**create the content in the db*/
        mcService.createMc(mc);
        logger.debug("mc created with content id: " + toolContentId);
        
        return mc;
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
    	
    	/** iterate the questions Map and persist the questions into the DB*/
    	Iterator itQuestionsMap = mapQuestionsContent.entrySet().iterator();
        while (itQuestionsMap.hasNext()) {
            Map.Entry pairs = (Map.Entry)itQuestionsMap.next();
            logger.debug("adding the  pair: " +  pairs.getKey() + " = " + pairs.getValue());
            if ((pairs.getValue() != null) && (!pairs.getValue().equals("")))
            {
            	logger.debug("checking existing question text: " +  pairs.getValue().toString() + " and mcContent uid():" + mcContent.getUid());
        	 	McQueContent mcQueContent=mcService.getQuestionContentByQuestionText(pairs.getValue().toString(), mcContent.getUid());
        	 	
        	 	logger.debug("mcQueContent: " +  mcQueContent);
            	if (mcQueContent == null)
            	{
            		mcQueContent=  new McQueContent(pairs.getValue().toString(),
              	        	 new Integer(pairs.getKey().toString()),
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

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
package org.lamsfoundation.lams.tool.mc;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.lamsfoundation.lams.tool.mc.pojos.McContent;
import org.lamsfoundation.lams.tool.mc.pojos.McOptsContent;
import org.lamsfoundation.lams.tool.mc.pojos.McUploadedFile;
import org.lamsfoundation.lams.tool.mc.service.IMcService;
import org.lamsfoundation.lams.tool.mc.web.McAuthoringForm;
import org.lamsfoundation.lams.usermanagement.User;
import org.lamsfoundation.lams.usermanagement.dto.UserDTO;
import org.lamsfoundation.lams.web.session.SessionManager;
import org.lamsfoundation.lams.web.util.AttributeNames;
import org.lamsfoundation.lams.web.util.SessionMap;


/**
 * @author Ozgur Demirtas
 * Common MCQ utility functions live here.
 */
public abstract class McUtils implements McAppConstants {

	static Logger logger = Logger.getLogger(McUtils.class.getName());

    public static String replaceNewLines(String text)
    {
        logger.debug("using text: " + text);
        String newText = "";
        if (text != null)
        {
            newText = text.replaceAll("\n","<br>");
            logger.debug("newText: " + newText);
        }
        
        return newText;

    }


	/**
	 * 
	 * getGMTDateTime(HttpServletRequest request)
	 * 
	 * @param request
	 * @return
	 */
	/* fix this */
    public static Date getGMTDateTime()
    {
    	Date date=new Date(System.currentTimeMillis());
    	logger.debug("date: " + date);
    	return date;
    }

	
	public static UserDTO getToolUser()
	{
		/*obtain user object from the session*/
	    HttpSession ss = SessionManager.getSession();
	    /* get back login user DTO */
	    UserDTO toolUser = (UserDTO) ss.getAttribute(AttributeNames.USER);
		logger.debug("retrieving toolUser: " + toolUser);
		return 	toolUser;
	}
	
	
	public static Long getUserId()
	{
		UserDTO toolUser=getToolUser();
		long userId=toolUser.getUserID().longValue();
		logger.debug("userId: " + userId);
		return new Long(userId);
	}
	
	public static String getUserName()
	{
		/* double check if username and login is the same */
		UserDTO toolUser=getToolUser();
		String userName=toolUser.getLogin();
		logger.debug("userName: " + userName);
		return userName;
	}
	
	public static String getUserFullName()
	{
		UserDTO toolUser=getToolUser();
		String fullName=toolUser.getFirstName() + " " + toolUser.getLastName();  
		logger.debug("fullName: " + fullName);
		return fullName;
	}
	
	public static String getFormattedDateString(Date date)
	{
		logger.debug("getFormattedDateString: " +  
				DateFormat.getDateTimeInstance(DateFormat.LONG, DateFormat.LONG).format(date));
		return (DateFormat.getDateTimeInstance(DateFormat.LONG, DateFormat.LONG).format(date));
	}
	
	
	
	
	/**
	 * generateOptionsMap(List listMcOptions)
	 * 
	 * returns a Map of options
	 * 
	 * @param listMcOptions
	 * @return Map
	 */
	public static Map generateOptionsMap(List listMcOptions)
	{
		logger.debug("incoming listMcOptions" + listMcOptions);
		Map mapOptionsContent= new TreeMap(new McStringComparator());
		
		Iterator listIterator=listMcOptions.iterator();
    	Long mapIndex=new Long(1);
    	while (listIterator.hasNext())
    	{
    		McOptsContent mcOptionsContent = (McOptsContent)listIterator.next();
    		logger.debug("mcOptionsContent:" + mcOptionsContent);
    		mapOptionsContent.put(mapIndex.toString(),mcOptionsContent.getMcQueOptionText());
    		mapIndex=new Long(mapIndex.longValue()+1);
    	}
    	logger.debug("generated mcOptionsContent: " + mapOptionsContent);
    	return mapOptionsContent;
	}

	public static void configureContentRepository(HttpServletRequest request, IMcService mcService)
	{
		logger.debug("attempt configureContentRepository");
    	mcService.configureContentRepository();
	    logger.debug("configureContentRepository ran successfully");
	}
	
    /**
     * temporary function
     * @return
     */
    public static long generateId()
	{
		Random generator = new Random();
		long longId=generator.nextLong();
		if (longId < 0) longId=longId * (-1) ;
		return longId;
	}
	
    /**
     * temporary function
     * @return
     */
	public static int generateIntegerId()
	{
		Random generator = new Random();
		int intId=generator.nextInt();
		if (intId < 0) intId=intId * (-1) ;
		return intId;
	}
	

    /**
     * temporary function
     * @return
     */
	public static int getCurrentUserId(HttpServletRequest request) throws McApplicationException
    {
	    HttpSession ss = SessionManager.getSession();
	    UserDTO user = (UserDTO) ss.getAttribute(AttributeNames.USER);
		logger.debug(logger + " " + "McUtils" +  " Current user is: " + user + " with id: " + user.getUserID());
		return user.getUserID().intValue();
    }
	

    /**
     * temporary function
     * @return
     */
	public static User createSimpleUser(Integer userId)
	{
		User user=new User();
		user.setUserId(userId);
		return user;
	}

    /**
     * temporary function
     * @return
     */
	public static boolean getDefineLaterStatus()
	{
		return false;
	}
	
	
	/**
	 * builds a map from a list  
	 * convertToMap(List sessionsList)
	 * 
	 * @param sessionsList
	 * @return Map
	 */
	public static Map convertToMap(List sessionsList, String listType)
	{
		Map map= new TreeMap(new McComparator());
		logger.debug("listType: " + listType);
		
		Iterator listIterator=sessionsList.iterator();
    	Long mapIndex=new Long(1);
    	
    	
    	while (listIterator.hasNext())
    	{
    		if (listType.equals("String"))
    		{
    			String text=(String)listIterator.next();
    			map.put(mapIndex.toString(), text);
    		}
    		else if (listType.equals("Long"))
    		{
    			Long LongValue=(Long)listIterator.next();
    			map.put(mapIndex.toString(), LongValue);
    		}
    		mapIndex=new Long(mapIndex.longValue()+1);
    	}
    	return map;
	}
	
	
	/**
	 * convertToMap(List sessionsList)
	 * 
	 * builds a String based map from a list
	 * 
	 * @param sessionsList
	 * @return Map
	 */
	public static Map convertToStringMap(List sessionsList, String listType)
	{
		Map map= new TreeMap(new McComparator());
		logger.debug("listType: " + listType);
		
		Iterator listIterator=sessionsList.iterator();
    	Long mapIndex=new Long(1);
    	
    	
    	while (listIterator.hasNext())
    	{
    		if (listType.equals("String"))
    		{
    			logger.debug("listType String");
    			String text=(String)listIterator.next();
    			map.put(mapIndex.toString(), text);
    		}
    		else if (listType.equals("Long"))
    		{
    			logger.debug("listType Long");
    			Long LongValue=(Long)listIterator.next();
    			map.put(mapIndex.toString(), LongValue.toString());
    		}
    		mapIndex=new Long(mapIndex.longValue()+1);
    	}
    	return map;
	}
	
	
	/**
	 * find out if the content is in use or not. If it is in use, the author can not modify it.
	 * The idea of content being in use is, once any one learner starts using a particular content
	 * that content should become unmodifiable. 
	 * 
	 * isContentInUse(McContent mcContent)
	 * @param mcContent
	 * @return boolean
	 */
	public static boolean isContentInUse(McContent mcContent)
	{
		logger.debug("is content inuse: " + mcContent.isContentInUse());
		return  mcContent.isContentInUse();
	}
	
	
	/**
	 * find out if the content is being edited in monitoring interface or not. If it is, the author can not modify it.
	 * 
	 * isDefineLater(McContent mcContent)
	 * @param mcContent
	 * @return boolean
	 */
	public static boolean isDefineLater(McContent mcContent)
	{
		logger.debug("is define later: " + mcContent.isDefineLater());
		return  mcContent.isDefineLater();
	}
	
	
	/**
	 * find out if the content is set to run offline or online. If it is set to run offline , the learners are informed about that..
	 * isRubnOffline(McContent mcContent)
	 * 
	 * @param mcContent
	 * @return boolean
	 */
	public static boolean isRunOffline(McContent mcContent)
	{
		logger.debug("is run offline: " + mcContent.isRunOffline());
		return mcContent.isRunOffline();
	}

	
    
	public static String getDestination(String sourceMcStarter)
	{
		logger.debug("sourceMcStarter: " + sourceMcStarter);
		
		if ((sourceMcStarter != null) && !sourceMcStarter.equals("monitoring"))
		{
			logger.debug("request is from authoring or define Later url. return to: " + LOAD_QUESTIONS);
			return LOAD_QUESTIONS;	
		}
		else if (sourceMcStarter == null)
		{
			logger.debug("request is from authoring url. return to: " + LOAD_QUESTIONS);
			return LOAD_QUESTIONS;	
		}
		else
		{
			logger.debug("request is from monitoring url. return to: " + LOAD_MONITORING_CONTENT_EDITACTIVITY);
			return LOAD_MONITORING_CONTENT_EDITACTIVITY;	
		}
	}
	
	
	
	/**
	 * the only attributes kept are TOOL_SESSION and TOOL_CONTENT_ID and CURRENT_MONITORED_TOOL_SESSION
	 * cleanUpSessionAbsolute(HttpServletRequest request)
	 * @param request
	 */
    public static void cleanUpSessionAbsolute(HttpServletRequest request)
    {
    	request.getSession().removeAttribute(MY_SIGNATURE);
    	request.getSession().removeAttribute(DEFAULT_CONTENT_ID);
    	request.getSession().removeAttribute(ERROR_MCAPPLICATION);
    	request.getSession().removeAttribute(LOAD);
    	request.getSession().removeAttribute(LOAD_QUESTIONS);
    	request.getSession().removeAttribute(LOAD_STARTER);
    	request.getSession().removeAttribute(AUTHORING_STARTER);
    	request.getSession().removeAttribute(LEARNING_STARTER);
    	request.getSession().removeAttribute(MONITORING_STARTER);
    	request.getSession().removeAttribute(LOAD_LEARNER);
    	request.getSession().removeAttribute(LOAD_MONITORING_CONTENT);
    	request.getSession().removeAttribute(INDIVIDUAL_REPORT);
    	request.getSession().removeAttribute(VIEW_SUMMARY);
    	request.getSession().removeAttribute(REDO_QUESTIONS);
    	request.getSession().removeAttribute(SINGLE_QUESTION_ANSWERS);
    	request.getSession().removeAttribute(RESULTS_SUMMARY);
    	request.getSession().removeAttribute(ERROR_LIST);
    	request.getSession().removeAttribute(PREVIEW);
    	request.getSession().removeAttribute(LEARNER_PROGRESS);
    	request.getSession().removeAttribute(LEARNER_PROGRESS_USERID);
    	request.getSession().removeAttribute(AUTHORING);
    	request.getSession().removeAttribute(SOURCE_MC_STARTER);
    	request.getSession().removeAttribute(AUTHORING_CANCELLED);
    	request.getSession().removeAttribute(DEFINE_LATER_EDIT_ACTIVITY);
    	request.getSession().removeAttribute(EDIT_OPTIONS_MODE);
    	request.getSession().removeAttribute(DEFINE_LATER_IN_EDIT_MODE);
    	request.getSession().removeAttribute(IS_ADD_QUESTION);
    	request.getSession().removeAttribute(IS_REMOVE_QUESTION);
    	request.getSession().removeAttribute(SUBMIT_SUCCESS);
    	request.getSession().removeAttribute(MAP_QUESTIONS_CONTENT);
    	request.getSession().removeAttribute(IS_REMOVE_CONTENT);
    	request.getSession().removeAttribute(IS_REVISITING_USER);
    	request.getSession().removeAttribute(USER);
    	request.getSession().removeAttribute(TOOL_CONTENT_UID);
    	request.getSession().removeAttribute(TOOL_SESSION_ID);
    	request.getSession().removeAttribute(USER_ID);
    	request.getSession().removeAttribute(MAX_QUESTION_INDEX);
    	request.getSession().removeAttribute(COPY_TOOL_CONTENT);
    	request.getSession().removeAttribute(REMOVE_TOOL_CONTENT);
    	request.getSession().removeAttribute(MAP_OPTIONS_CONTENT);
    	request.getSession().removeAttribute(MAP_DEFAULTOPTIONS_CONTENT);
    	request.getSession().removeAttribute(MAP_DISABLED_QUESTIONS);
    	request.getSession().removeAttribute(MAP_GENERAL_OPTIONS_CONTENT);
    	request.getSession().removeAttribute(MAP_GENERAL_SELECTED_OPTIONS_CONTENT);
    	request.getSession().removeAttribute(MAP_STARTUP_GENERAL_OPTIONS_CONTENT);
    	request.getSession().removeAttribute(MAP_STARTUP_GENERAL_SELECTED_OPTIONS_CONTENT);
    	request.getSession().removeAttribute(MAP_STARTUP_GENERAL_OPTIONS_QUEID);
    	request.getSession().removeAttribute(QUESTIONS_WITHNO_OPTIONS);
    	request.getSession().removeAttribute(MAP_GENERAL_CHECKED_OPTIONS_CONTENT);
    	request.getSession().removeAttribute(MAP_LEARNER_QUESTIONS_CONTENT);
    	request.getSession().removeAttribute(MAP_LEARNER_CHECKED_OPTIONS_CONTENT);
    	request.getSession().removeAttribute(MAP_LEARNER_ASSESSMENT_RESULTS);
    	request.getSession().removeAttribute(MAP_LEARNER_FEEDBACK_INCORRECT);
    	request.getSession().removeAttribute(MAP_LEARNER_FEEDBACK_CORRECT);
    	request.getSession().removeAttribute(MAP_QUESTION_WEIGHTS);
    	request.getSession().removeAttribute(MAP_QUE_ATTEMPTS);
    	request.getSession().removeAttribute(MAP_QUE_CORRECT_ATTEMPTS);
    	request.getSession().removeAttribute(MAP_QUE_INCORRECT_ATTEMPTS);
    	request.getSession().removeAttribute(MAP_WEIGHTS);
    	request.getSession().removeAttribute(MAP_CHECKBOX_STATES);
    	request.getSession().removeAttribute(MAP_SELECTED_OPTIONS);
    	request.getSession().removeAttribute(MAP_FEEDBACK_INCORRECT);
    	request.getSession().removeAttribute(MAP_FEEDBACK_CORRECT);
    	request.getSession().removeAttribute(SELECTED_QUESTION);
    	request.getSession().removeAttribute(SELECTED_QUESTION_INDEX);
    	request.getSession().removeAttribute(DEFAULT_QUESTION_UID);
    	request.getSession().removeAttribute(TITLE);
    	request.getSession().removeAttribute(INSTRUCTIONS);
    	request.getSession().removeAttribute(CREATION_DATE);
    	request.getSession().removeAttribute(DEFINE_LATER);
    	request.getSession().removeAttribute(RICHTEXT_FEEDBACK_CORRECT);
    	request.getSession().removeAttribute(RETRIES);
    	request.getSession().removeAttribute(ON);
    	request.getSession().removeAttribute(OFF);
    	request.getSession().removeAttribute(RICHTEXT_FEEDBACK_INCORRECT);
    	request.getSession().removeAttribute(RICHTEXT_OFFLINEINSTRUCTIONS);
    	request.getSession().removeAttribute(PASSMARK);
    	request.getSession().removeAttribute(VIEW_ANSWERS);
    	request.getSession().removeAttribute(SHOW_AUTHORING_TABS);
    	request.getSession().removeAttribute(RICHTEXT_ONLINEINSTRUCTIONS);
    	request.getSession().removeAttribute(RICHTEXT_REPORT_TITLE);
    	//request.getSession().removeAttribute(RICHTEXT_END_LEARNING_MSG);
    	request.getSession().removeAttribute(RICHTEXT_TITLE);
    	request.getSession().removeAttribute(RICHTEXT_INSTRUCTIONS);
    	request.getSession().removeAttribute(RICHTEXT_BLANK);
    	request.getSession().removeAttribute(SUBMIT_OFFLINE_FILE);
    	request.getSession().removeAttribute(SUBMIT_ONLINE_FILE);
    	request.getSession().removeAttribute(LIST_UPLOADED_OFFLINE_FILENAMES);
    	request.getSession().removeAttribute(LIST_UPLOADED_ONLINE_FILENAMES);
    	request.getSession().removeAttribute(LIST_OFFLINEFILES_METADATA);
    	request.getSession().removeAttribute(LIST_ONLINEFILES_METADATA);
    	request.getSession().removeAttribute(COUNT_SESSION_COMPLETE);
    	request.getSession().removeAttribute(COUNT_ALL_USERS);
    	request.getSession().removeAttribute(COUNT_MAX_ATTEMPT);
    	request.getSession().removeAttribute(TOP_MARK);
    	request.getSession().removeAttribute(LOWEST_MARK);
    	request.getSession().removeAttribute(AVERAGE_MARK);
    	request.getSession().removeAttribute(ACTIVE_MODULE);
    	request.getSession().removeAttribute(NOT_ATTEMPTED);
    	request.getSession().removeAttribute(INCOMPLETE);
    	request.getSession().removeAttribute(COMPLETED);
    	request.getSession().removeAttribute(MAP_TOOL_SESSIONS);
    	request.getSession().removeAttribute(MAX_TOOL_SESSION_COUNT.toString());
    	request.getSession().removeAttribute(IS_TOOL_SESSION_CHANGED);
    	request.getSession().removeAttribute(ADD_NEW_QUESTION);
    	request.getSession().removeAttribute(OPTION_OFF);
    	request.getSession().removeAttribute(REMOVE_QUESTION);
    	request.getSession().removeAttribute(REMOVE_ALL_CONTENT);
    	request.getSession().removeAttribute(SUBMIT_ALL_CONTENT);
    	request.getSession().removeAttribute(SUBMIT_TAB_DONE);
    	request.getSession().removeAttribute(MAP_QUESTION_CONTENT_LEARNER);
    	request.getSession().removeAttribute(CURRENT_QUESTION_INDEX);
    	request.getSession().removeAttribute(TOTAL_QUESTION_COUNT);
    	request.getSession().removeAttribute(LEARNER_MARK);
    	request.getSession().removeAttribute(LEARNER_BEST_MARK);
    	request.getSession().removeAttribute(LEARNER_LAST_ATTEMPT_ORDER);
    	request.getSession().removeAttribute(LEARNER_MARK_ATLEAST);
    	request.getSession().removeAttribute(MAP_ANSWERS);
    	request.getSession().removeAttribute(CURRENT_ANSWER);
    	request.getSession().removeAttribute(USER_FEEDBACK);
    	request.getSession().removeAttribute(REPORT_TITLE_LEARNER);
    	request.getSession().removeAttribute(TOTAL_COUNT_REACHED);
    	request.getSession().removeAttribute(IS_TOOL_ACTIVITY_OFFLINE);
    	request.getSession().removeAttribute(IS_USERNAME_VISIBLE);
    	request.getSession().removeAttribute(IS_CONTENT_IN_USE);
    	request.getSession().removeAttribute(IS_RETRIES);
    	request.getSession().removeAttribute(IS_SHOW_FEEDBACK);
    	request.getSession().removeAttribute(IS_SHOW_LEARNERS_REPORT);
    	request.getSession().removeAttribute(IS_ALL_SESSIONS_COMPLETED);
    	request.getSession().removeAttribute(CHECK_ALL_SESSIONS_COMPLETED);
    	request.getSession().removeAttribute(FROM_TOOL_CONTENT_ID);
    	request.getSession().removeAttribute(TO_TOOL_CONTENT_ID);
    	request.getSession().removeAttribute(LEARNER_REPORT);
    	request.getSession().removeAttribute(MAP_USER_RESPONSES);
    	request.getSession().removeAttribute(MAP_MAIN_REPORT);
    	request.getSession().removeAttribute(MAP_STATS);
    	request.getSession().removeAttribute(REPORT_TITLE_MONITOR);
    	request.getSession().removeAttribute(MONITOR_USER_ID);
    	request.getSession().removeAttribute(MONITORING_REPORT);
    	request.getSession().removeAttribute(MONITORING_ERROR);
    	request.getSession().removeAttribute(MAP_MONITORING_QUESTIONS);
    	request.getSession().removeAttribute(SUMMARY_TOOL_SESSIONS);
    	request.getSession().removeAttribute(MONITORED_CONTENT_ID);
    	request.getSession().removeAttribute(EDITACTIVITY_EDITMODE);
    	request.getSession().removeAttribute(FORM_INDEX);
    	request.getSession().removeAttribute(SELECTION_CASE);
    	request.getSession().removeAttribute(LIST_MONITORED_ANSWERS_CONTAINER_DTO);
    	request.getSession().removeAttribute(QUESTION_LISTING_MODE);
    	request.getSession().removeAttribute(QUESTION_LISTING_MODE_SEQUENTIAL);
    	request.getSession().removeAttribute(QUESTION_LISTING_MODE_COMBINED);
    	request.getSession().removeAttribute(PREVIEW_ONLY);
    	request.getSession().removeAttribute(TIMEZONE);
    	request.getSession().removeAttribute(MODE);
    	request.getSession().removeAttribute(LEARNING_MODE);
    	request.getSession().removeAttribute(EXPORT_USER_ID);
    	request.getSession().removeAttribute(MAP_INCORRECT_FEEDBACK);
    	request.getSession().removeAttribute(MAP_CORRECT_FEEDBACK);
    	request.getSession().removeAttribute(MAP_INCORRECT_FEEDBACK_LEARNER);
    	request.getSession().removeAttribute(MAP_CORRECT_FEEDBACK_LEARNER);
    	request.getSession().removeAttribute(ACTIVITY_TITLE);
    	request.getSession().removeAttribute(ACTIVITY_INSTRUCTIONS);
    	request.getSession().removeAttribute(SUMMARY_TOOL_SESSIONS_ID);
    	logger.debug("completely cleaned the session.");
    }
  
    
	/**
	 * String getDestination(String sourceMcStarter, String requestedModule)
	 * 
	 * determines the struts level location to return
	 *   
	 * @param sourceMcStarter
	 * @param requestedModule
	 * @return
	 */
	public static String getDestination(String sourceMcStarter, String requestedModule)
	{
		logger.debug("sourceMcStarter: " + sourceMcStarter + " and requestedModule:" + requestedModule);
		
		if (requestedModule.equals(DEFINE_LATER))
		{
			logger.debug("request is from define Later url. return to: " + LOAD_VIEW_ONLY);
			return LOAD_VIEW_ONLY;	
		}
		else if (requestedModule.equals(AUTHORING))
		{
			logger.debug("request is from authoring url. return to: " + LOAD_QUESTIONS);
			return LOAD_QUESTIONS;	
		}
		else
		{
			logger.debug("request is from an unknown source. return null");
			return null;
		}
	}

	
	/**
	 * populateAuthoringDTO(HttpServletRequest request, McContent defaultMcContent, 
            McGeneralAuthoringDTO mcGeneralAuthoringDTO)
	 * 
	 * @param request
	 * @param defaultMcContent
	 * @param mcGeneralAuthoringDTO
	 */
    public static void populateAuthoringDTO(HttpServletRequest request, McContent defaultMcContent, 
            McGeneralAuthoringDTO mcGeneralAuthoringDTO)
	{
        mcGeneralAuthoringDTO.setActivityTitle(defaultMcContent.getTitle());
		mcGeneralAuthoringDTO.setActivityInstructions(defaultMcContent.getInstructions());
		
	    mcGeneralAuthoringDTO.setOnlineInstructions(defaultMcContent.getOnlineInstructions());
	    mcGeneralAuthoringDTO.setOfflineInstructions(defaultMcContent.getOfflineInstructions());
	}

    
	/**
	 * setDefineLater(HttpServletRequest request, boolean value, String toolContentID)
	 * @param request
	 * @param value
	 * @param toolContentID
	 */
	public static void setDefineLater(HttpServletRequest request, boolean value, String strToolContentID, IMcService mcService)
    {
		logger.debug("mcService: " + mcService);
    	logger.debug("value:" + value);
    	logger.debug("strToolContentID:" + strToolContentID);
    	
		McContent mcContent=mcService.retrieveMc(new Long(strToolContentID));
    	logger.debug("mcContent:" + mcContent);
    	if (mcContent != null)
    	{
    		mcContent.setDefineLater(value);
        	logger.debug("defineLater has been set to:" + value);
        	mcService.updateMc(mcContent);	
    	}
    }

    
	/**
	 * setFormProperties(HttpServletRequest request, IMcService mcService,  
            McAuthoringForm  mcAuthoringForm, McGeneralAuthoringDTO mcGeneralAuthoringDTO, String strToolContentID, String defaultContentIdStr, 
            String activeModule, SessionMap sessionMap, String httpSessionID)
	 * 
	 * @param request
	 * @param mcService
	 * @param mcAuthoringForm
	 * @param mcGeneralAuthoringDTO
	 * @param strToolContentID
	 * @param defaultContentIdStr
	 * @param activeModule
	 * @param sessionMap
	 * @param httpSessionID
	 */
    public static void setFormProperties(HttpServletRequest request, IMcService mcService,  
            McAuthoringForm  mcAuthoringForm, McGeneralAuthoringDTO mcGeneralAuthoringDTO, String strToolContentID, String defaultContentIdStr, 
            String activeModule, SessionMap sessionMap, String httpSessionID)
    {
    	logger.debug("setFormProperties: ");
    	logger.debug("using strToolContentID: " + strToolContentID);
    	logger.debug("using defaultContentIdStr: " + defaultContentIdStr);
    	logger.debug("using activeModule: " + activeModule);
    	logger.debug("using httpSessionID: " + httpSessionID);

    	mcAuthoringForm.setHttpSessionID(httpSessionID);
    	mcGeneralAuthoringDTO.setHttpSessionID(httpSessionID);
    	
    	mcAuthoringForm.setToolContentID(strToolContentID);
    	
    	if ((defaultContentIdStr != null) && (defaultContentIdStr.length() > 0))
    	{
    	    mcAuthoringForm.setDefaultContentIdStr(new Long(defaultContentIdStr).toString());
    	    mcGeneralAuthoringDTO.setDefaultContentIdStr(new Long(defaultContentIdStr).toString());
    	}
    	    
    	
    	mcAuthoringForm.setActiveModule(activeModule);
    	mcGeneralAuthoringDTO.setActiveModule(activeModule);
    	
    	String sln=request.getParameter("sln");
    	logger.debug("sln: " + sln);
		mcAuthoringForm.setSln(sln);
		mcGeneralAuthoringDTO.setSln(sln);
		
		String questionsSequenced=request.getParameter("questionsSequenced");
		logger.debug("questionsSequenced: " + questionsSequenced);
		mcAuthoringForm.setQuestionsSequenced(questionsSequenced);
		mcGeneralAuthoringDTO.setQuestionsSequenced(questionsSequenced);

		String randomize=request.getParameter("randomize");
		logger.debug("randomize: " + randomize);
		mcAuthoringForm.setRandomize(randomize);
		mcGeneralAuthoringDTO.setRandomize(randomize);

		
		String showMarks=request.getParameter("showMarks");
		logger.debug("showMarks: " + questionsSequenced);
		mcAuthoringForm.setShowMarks(showMarks);
		mcGeneralAuthoringDTO.setShowMarks(showMarks);
		
		
		String retries=request.getParameter("retries");
		logger.debug("retries: " + retries);
		mcAuthoringForm.setRetries(retries);
		mcGeneralAuthoringDTO.setRetries(retries);
		
		
		String offlineInstructions=request.getParameter(OFFLINE_INSTRUCTIONS);
		logger.debug("offlineInstructions: " + offlineInstructions);
		mcAuthoringForm.setOfflineInstructions(offlineInstructions);
		mcGeneralAuthoringDTO.setOfflineInstructions(offlineInstructions);

		String onlineInstructions=request.getParameter(ONLINE_INSTRUCTIONS);
		logger.debug("onlineInstructions: " + onlineInstructions);
		mcAuthoringForm.setOnlineInstructions(onlineInstructions);
		mcGeneralAuthoringDTO.setOnlineInstructions(onlineInstructions);


		String reflect=request.getParameter(REFLECT);
		logger.debug("reflect: " + reflect);
		mcAuthoringForm.setReflect(reflect);
		mcGeneralAuthoringDTO.setReflect(reflect);
		

		String reflectionSubject=request.getParameter(REFLECTION_SUBJECT);
		logger.debug("reflectionSubject: " + reflectionSubject);
		mcAuthoringForm.setReflectionSubject(reflectionSubject);
		mcGeneralAuthoringDTO.setReflectionSubject(reflectionSubject);
		
		
     	String passmark= request.getParameter("passmark");
     	logger.debug("passmark: " + passmark);
		mcGeneralAuthoringDTO.setPassMarkValue(passmark);

		
		logger.debug("ending setFormProperties with mcAuthoringForm: " + mcAuthoringForm);
		logger.debug("ending setFormProperties with mcGeneralAuthoringDTO: " + mcGeneralAuthoringDTO);
    }

    
    /** If this file exists in attachments map, move it to the deleted attachments map.
     * Returns the updated deletedAttachments map, creating a new one if needed. If uuid supplied
     * then tries to match on that, otherwise uses filename and isOnline. */
    public static List moveToDelete(String uuid, List attachmentsList, List deletedAttachmentsList ) {

        List deletedList = deletedAttachmentsList != null ? deletedAttachmentsList : new ArrayList();
        
        if ( attachmentsList != null ) {
            Iterator iter = attachmentsList.iterator();
            McUploadedFile attachment = null;
            while ( iter.hasNext() && attachment == null ) {
                McUploadedFile value = (McUploadedFile) iter.next();
                if ( uuid.equals(value.getUuid()) ) {
                    attachment = value;
                }

            }
            if ( attachment != null ) {
                deletedList.add(attachment);
                attachmentsList.remove(attachment);
            }
        }
        
        return deletedList;
    }
    
    
    /** If this file exists in attachments map, move it to the deleted attachments map.
     * Returns the updated deletedAttachments map, creating a new one if needed. If uuid supplied
     * then tries to match on that, otherwise uses filename and isOnline. */
    public static List moveToDelete(String filename, boolean isOnline, List attachmentsList, List deletedAttachmentsList ) {

        List deletedList = deletedAttachmentsList != null ? deletedAttachmentsList : new ArrayList();
        
        if ( attachmentsList != null ) {
            Iterator iter = attachmentsList.iterator();
            McUploadedFile attachment = null;
            while ( iter.hasNext() && attachment == null ) {
                McUploadedFile value = (McUploadedFile) iter.next();
                if ( filename.equals(value.getFileName()) && isOnline == value.isFileOnline()) {
                    attachment = value;
                }

            }
            if ( attachment != null ) {
                deletedList.add(attachment);
                attachmentsList.remove(attachment);
            }
        }
        
        return deletedList;
    }
	
}

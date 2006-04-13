
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

package org.lamsfoundation.lams.tool.vote;

import java.text.DateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.TimeZone;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.lamsfoundation.lams.tool.vote.pojos.VoteContent;
import org.lamsfoundation.lams.tool.vote.pojos.VoteOptsContent;
import org.lamsfoundation.lams.tool.vote.pojos.VoteSession;
import org.lamsfoundation.lams.tool.vote.service.IVoteService;
import org.lamsfoundation.lams.tool.vote.web.VoteAuthoringForm;
import org.lamsfoundation.lams.usermanagement.User;
import org.lamsfoundation.lams.usermanagement.dto.UserDTO;
import org.lamsfoundation.lams.web.session.SessionManager;
import org.lamsfoundation.lams.web.util.AttributeNames;


/**
 * @author Ozgur Demirtas
 * Common MCQ utility functions live here.
 */
public abstract class VoteUtils implements VoteAppConstants {

	static Logger logger = Logger.getLogger(VoteUtils.class.getName());

	/**
	 * returns the service object from the session cache
	 * IVoteService getToolService(HttpServletRequest request)
	 * 
	 * @param request
	 * @return
	 */
	public static IVoteService getToolService(HttpServletRequest request)
	{
		IVoteService mcService=(IVoteService)request.getSession().getAttribute(TOOL_SERVICE);
	    return mcService;
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
	
	public static void persistTimeZone(HttpServletRequest request)
	{
		TimeZone timeZone=TimeZone.getDefault();
	    logger.debug("current timezone: " + timeZone.getDisplayName());
	    request.getSession().setAttribute(TIMEZONE, timeZone.getDisplayName());
	    logger.debug("current timezone id: " + timeZone.getID());
	}
	
	public static String getCurrentTimeZone()
	{
		TimeZone timeZone=TimeZone.getDefault();
	    logger.debug("current timezone: " + timeZone.getDisplayName());
	    return timeZone.getDisplayName();
	}
	
	
	/**
	 * existsContent(long toolContentId)
	 * @param long toolContentId
	 * @return boolean
	 * determine whether a specific toolContentId exists in the db
	 */
	public static boolean existsContent(Long toolContentId, HttpServletRequest request)
	{
		IVoteService mcService =VoteUtils.getToolService(request);
	    /*
    	VoteContent mcContent=mcService.retrieveVote(toolContentId);
	    logger.debug("retrieving mcContent: " + mcContent);
	    if (mcContent == null) 
	    	return false;
	    */
	    
		return true;	
	}

	/**
	 * it is expected that the tool session id already exists in the tool sessions table
	 * existsSession(long toolSessionId)
	 * @param toolSessionId
	 * @return boolean
	 */
	public static boolean existsSession(Long toolSessionId, HttpServletRequest request)
	{
		logger.debug("existsSession");
    	IVoteService mcService =VoteUtils.getToolService(request);
	    VoteSession mcSession=mcService.retrieveVoteSession(toolSessionId);
	    logger.debug("mcSession:" + mcSession);
    	
	    if (mcSession == null) 
	    	return false;
	    
		return true;	
	}
	
	
	/**
	 * returns a Map of options
	 * generateOptionsMap(List listVoteOptions)
	 * 
	 * @param listVoteOptions
	 * @return Map
	 */
	public static Map generateOptionsMap(List listVoteOptions)
	{
		logger.debug("incoming listVoteOptions" + listVoteOptions);
		Map mapOptionsContent= new TreeMap(new VoteStringComparator());
		
		Iterator listIterator=listVoteOptions.iterator();
    	Long mapIndex=new Long(1);
    	while (listIterator.hasNext())
    	{
    		VoteOptsContent mcOptionsContent = (VoteOptsContent)listIterator.next();
    		logger.debug("mcOptionsContent:" + mcOptionsContent);
    		mapOptionsContent.put(mapIndex.toString(),mcOptionsContent.getVoteQueOptionText());
    		mapIndex=new Long(mapIndex.longValue()+1);
    	}
    	logger.debug("generated mcOptionsContent: " + mapOptionsContent);
    	return mapOptionsContent;
	}


    public static void setDefaultSessionAttributes(HttpServletRequest request, VoteContent defaultVoteContent, VoteAuthoringForm voteAuthoringForm)
	{
		/*should never be null anyway as default content MUST exist in the db*/
        if(defaultVoteContent == null)
            throw new NullPointerException("Default VoteContent cannot be null");

        voteAuthoringForm.setTitle(defaultVoteContent.getTitle());
        voteAuthoringForm.setInstructions(defaultVoteContent.getInstructions());
		request.getSession().setAttribute(ACTIVITY_TITLE, defaultVoteContent.getTitle());
		request.getSession().setAttribute(ACTIVITY_INSTRUCTIONS, defaultVoteContent.getInstructions());
		
	    logger.debug("ACTIVITY_INSTRUCTIONS: " + defaultVoteContent.getInstructions());
		
	    voteAuthoringForm.setReportTitle(defaultVoteContent.getReportTitle());
	    voteAuthoringForm.setMonitoringReportTitle(defaultVoteContent.getMonitoringReportTitle());
	    voteAuthoringForm.setEndLearningMessage(defaultVoteContent.getEndLearningMessage());
	    voteAuthoringForm.setOnlineInstructions(defaultVoteContent.getOnlineInstructions());
	    voteAuthoringForm.setOfflineInstructions(defaultVoteContent.getOfflineInstructions());
	    voteAuthoringForm.setMonitoringReportTitle(defaultVoteContent.getMonitoringReportTitle());
		
         //determine the status of radio boxes
	    voteAuthoringForm.setUsernameVisible(defaultVoteContent.isUsernameVisible()?ON:OFF);
	    voteAuthoringForm.setQuestionsSequenced(defaultVoteContent.isQuestionsSequenced()?ON:OFF);
	}

	
	public static void persistRichText(HttpServletRequest request)
	{
		String richTextOfflineInstructions=request.getParameter(RICHTEXT_OFFLINEINSTRUCTIONS);
		logger.debug("read parameter richTextOfflineInstructions: " + richTextOfflineInstructions);
		String richTextOnlineInstructions=request.getParameter(RICHTEXT_ONLINEINSTRUCTIONS);
		logger.debug("read parameter richTextOnlineInstructions: " + richTextOnlineInstructions);
		
		if ((richTextOfflineInstructions != null) && (richTextOfflineInstructions.length() > 0))
		{
			request.getSession().setAttribute(RICHTEXT_OFFLINEINSTRUCTIONS,richTextOfflineInstructions);	
		}
		
		if ((richTextOnlineInstructions != null) && (richTextOnlineInstructions.length() > 0))
		{
			request.getSession().setAttribute(RICHTEXT_ONLINEINSTRUCTIONS,richTextOnlineInstructions);	
		}
		
	
		String richTextTitle=request.getParameter(RICHTEXT_TITLE);
		logger.debug("read parameter richTextTitle: " + richTextTitle);
		String richTextInstructions=request.getParameter(RICHTEXT_INSTRUCTIONS);
		logger.debug("read parameter richTextInstructions: " + richTextInstructions);
		
		
		if ((richTextTitle != null) && (richTextTitle.length() > 0))
		{
			request.getSession().setAttribute(RICHTEXT_TITLE,richTextTitle);
		}
		
		if ((richTextInstructions != null) && (richTextInstructions.length() > 0))
		{
			request.getSession().setAttribute(RICHTEXT_INSTRUCTIONS,richTextInstructions);
		}
		
		String richTextIncorrectFeedback=request.getParameter(RICHTEXT_INCORRECT_FEEDBACK);
		logger.debug("read parameter richTextIncorrectFeedback: " + richTextIncorrectFeedback);
		
		if ((richTextIncorrectFeedback != null) && (richTextIncorrectFeedback.length() > 0))
		{
			request.getSession().setAttribute(RICHTEXT_INCORRECT_FEEDBACK,richTextIncorrectFeedback);
		}
		

		String richTextCorrectFeedback=request.getParameter(RICHTEXT_CORRECT_FEEDBACK);
		logger.debug("read parameter richTextCorrectFeedback: " + richTextCorrectFeedback);
		
		if ((richTextCorrectFeedback != null) && (richTextCorrectFeedback.length() > 0))
		{
			request.getSession().setAttribute(RICHTEXT_CORRECT_FEEDBACK,richTextCorrectFeedback);
		}

		
		String richTextReportTitle=request.getParameter(RICHTEXT_REPORT_TITLE);
		logger.debug("read parameter richTextReportTitle: " + richTextReportTitle);
		
		String richTextEndLearningMessage=request.getParameter(RICHTEXT_END_LEARNING_MSG);
		logger.debug("read parameter richTextEndLearningMessage: " + richTextEndLearningMessage);
		
		if ((richTextReportTitle != null) && (richTextReportTitle.length() > 0))
		{
			request.getSession().setAttribute(RICHTEXT_REPORT_TITLE,richTextReportTitle);
		}
		
		if ((richTextEndLearningMessage != null) && (richTextEndLearningMessage.length() > 0))
		{
			request.getSession().setAttribute(RICHTEXT_END_LEARNING_MSG,richTextEndLearningMessage);
		}
		
		Map mapIncorrectFeedback=(Map)request.getSession().getAttribute(MAP_INCORRECT_FEEDBACK);
	}
	
	
	public static void configureContentRepository(HttpServletRequest request, IVoteService mcService)
	{
		logger.debug("attempt configureContentRepository");
    	mcService.configureContentRepository();
	    logger.debug("configureContentRepository ran successfully");
	}
	
	/**
	 * retrieves existing updated file information
	 * populateUploadedFilesData(HttpServletRequest request, VoteContent defaultVoteContent)
	 * 
	 * @param request
	 * @param defaultVoteContent
	 */
	public static void populateUploadedFilesData(HttpServletRequest request, VoteContent defaultVoteContent)
	{
		logger.debug("attempt populateUploadedFilesData for: " + defaultVoteContent);
		IVoteService mcService =VoteUtils.getToolService(request);
    	logger.debug("mcService: " + mcService);

    	/** read the uploaded offline uuid + file name pair */
	    List listOffFilesName=mcService.retrieveVoteUploadedOfflineFilesName(defaultVoteContent.getUid());
	    logger.debug("initial listOfflineFilesName: " + listOffFilesName);
	    
	    /** read the uploaded online uuid + file name pair */
	    List listOnFilesName=mcService.retrieveVoteUploadedOnlineFilesName(defaultVoteContent.getUid());
	    logger.debug("initial listOnlineFilesName: " + listOnFilesName);
	    
	    request.getSession().setAttribute(LIST_UPLOADED_OFFLINE_FILENAMES, listOffFilesName);
	    request.getSession().setAttribute(LIST_UPLOADED_ONLINE_FILENAMES, listOnFilesName);
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
	public static int getCurrentUserId(HttpServletRequest request) throws VoteApplicationException
    {
	    HttpSession ss = SessionManager.getSession();
	    UserDTO user = (UserDTO) ss.getAttribute(AttributeNames.USER);
		logger.debug(logger + " " + "VoteUtils" +  " Current user is: " + user + " with id: " + user.getUserID());
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
		Map map= new TreeMap(new VoteComparator());
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
	 * builds a String based map from a list 
	 * convertToMap(List sessionsList)
	 * 
	 * @param sessionsList
	 * @return Map
	 */
	public static Map convertToStringMap(List sessionsList, String listType)
	{
		Map map= new TreeMap(new VoteComparator());
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
	 * isContentInUse(VoteContent mcContent)
	 * @param mcContent
	 * @return boolean
	 */
	public static boolean isContentInUse(VoteContent mcContent)
	{
		logger.debug("is content inuse: " + mcContent.isContentInUse());
		return  mcContent.isContentInUse();
	}
	
	
	/**
	 * find out if the content is being edited in monitoring interface or not. If it is, the author can not modify it.
	 * 
	 * isDefineLater(VoteContent mcContent)
	 * @param mcContent
	 * @return boolean
	 */
	public static boolean isDefineLater(VoteContent mcContent)
	{
		logger.debug("is define later: " + mcContent.isDefineLater());
		return  mcContent.isDefineLater();
	}
	
	
	/**
	 * find out if the content is set to run offline or online. If it is set to run offline , the learners are informed about that..
	 * isRubnOffline(VoteContent mcContent)
	 * 
	 * @param mcContent
	 * @return boolean
	 */
	public static boolean isRunOffline(VoteContent mcContent)
	{
		logger.debug("is run offline: " + mcContent.isRunOffline());
		return mcContent.isRunOffline();
	}

	
    
	public static String getDestination(String sourceVoteStarter)
	{
		logger.debug("sourceVoteStarter: " + sourceVoteStarter);
		
		if ((sourceVoteStarter != null) && !sourceVoteStarter.equals("monitoring"))
		{
			logger.debug("request is from authoring or define Later url. return to: " + LOAD_QUESTIONS);
			return LOAD_QUESTIONS;	
		}
		else if (sourceVoteStarter == null)
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

	public static void setDefineLater(HttpServletRequest request, boolean value)
    {
    	IVoteService voteService = (IVoteService)request.getSession().getAttribute(TOOL_SERVICE);
    	logger.debug("voteService:" + voteService);
    	
    	Long toolContentId=(Long)request.getSession().getAttribute(TOOL_CONTENT_ID);
    	logger.debug("toolContentId:" + toolContentId);
    	
    	VoteContent voteContent=voteService.retrieveVote(toolContentId);
    	logger.debug("voteContent:" + voteContent);
    	if (voteContent != null)
    	{
    		voteContent.setDefineLater(value);
        	logger.debug("defineLater has been set to true");
        	voteService.updateVote(voteContent);	
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

    	
    	cleanUpUserExceptions(request);
    	logger.debug("completely cleaned the session.");
    }
    
    /**
     *removes attributes except USER_EXCEPTION_NO_STUDENT_ACTIVITY 
     */
    public static void cleanUpUserExceptions(HttpServletRequest request)
    {
    	request.getSession().removeAttribute(USER_EXCEPTION_WRONG_FORMAT);
    	request.getSession().removeAttribute(USER_EXCEPTION_INCOMPATIBLE_IDS);
    	request.getSession().removeAttribute(USER_EXCEPTION_NUMBERFORMAT);
    	request.getSession().removeAttribute(USER_EXCEPTION_CONTENT_DOESNOTEXIST);
    	request.getSession().removeAttribute(USER_EXCEPTION_TOOLSESSION_DOESNOTEXIST);
    	request.getSession().removeAttribute(USER_EXCEPTION_TOOLCONTENT_DOESNOTEXIST);
    	request.getSession().removeAttribute(USER_EXCEPTION_LEARNER_REQUIRED);    	
    	request.getSession().removeAttribute(USER_EXCEPTION_CONTENTID_REQUIRED);
    	request.getSession().removeAttribute(USER_EXCEPTION_TOOLSESSIONID_REQUIRED);
    	request.getSession().removeAttribute(USER_EXCEPTION_TOOLSESSIONID_INCONSISTENT);
    	request.getSession().removeAttribute(USER_EXCEPTION_DEFAULTCONTENT_NOT_AVAILABLE);
    	request.getSession().removeAttribute(USER_EXCEPTION_DEFAULTQUESTIONCONTENT_NOT_AVAILABLE);
    	request.getSession().removeAttribute(USER_EXCEPTION_DEFAULTOPTIONSCONTENT_NOT_AVAILABLE);
    	request.getSession().removeAttribute(USER_EXCEPTION_USERID_NOTAVAILABLE);    	
    	request.getSession().removeAttribute(USER_EXCEPTION_USERID_NOTNUMERIC);
    	request.getSession().removeAttribute(USER_EXCEPTION_ONLYCONTENT_ANDNOSESSIONS);
    	request.getSession().removeAttribute(USER_EXCEPTION_USERID_EXISTING);
    	request.getSession().removeAttribute(USER_EXCEPTION_USER_DOESNOTEXIST);
    	request.getSession().removeAttribute(USER_EXCEPTION_MONITORINGTAB_CONTENTID_REQUIRED);
    	request.getSession().removeAttribute(USER_EXCEPTION_DEFAULTCONTENT_NOTSETUP);
    	request.getSession().removeAttribute(USER_EXCEPTION_NO_TOOL_SESSIONS);    	
    	request.getSession().removeAttribute(USER_EXCEPTION_MODE_REQUIRED);
    	request.getSession().removeAttribute(USER_EXCEPTION_CONTENT_IN_USE);
    	request.getSession().removeAttribute(USER_EXCEPTION_CONTENT_BEING_MODIFIED);
    	request.getSession().removeAttribute(USER_EXCEPTION_CONTENT_RUNOFFLINE);
    	request.getSession().removeAttribute(USER_EXCEPTION_CONTENT_DEFINE_LATER);
    	request.getSession().removeAttribute(USER_EXCEPTION_MODE_INVALID);
    	request.getSession().removeAttribute(USER_EXCEPTION_QUESTION_EMPTY);
    	request.getSession().removeAttribute(USER_EXCEPTION_ANSWER_EMPTY);
    	request.getSession().removeAttribute(USER_EXCEPTION_WEIGHT_TOTAL);
    	request.getSession().removeAttribute(USER_EXCEPTION_WEIGHT_EMPTY);
    	request.getSession().removeAttribute(USER_EXCEPTION_WEIGHT_NOTINTEGER);
    	request.getSession().removeAttribute(USER_EXCEPTION_WEIGHT_ZERO);
    	request.getSession().removeAttribute(USER_EXCEPTION_ANSWERS_DUPLICATE);    	
    	request.getSession().removeAttribute(USER_EXCEPTION_OPTIONS_COUNT_ZERO);
    	request.getSession().removeAttribute(USER_EXCEPTION_CHKBOXES_EMPTY);
    	request.getSession().removeAttribute(USER_EXCEPTION_SUBMIT_NONE);
    	request.getSession().removeAttribute(USER_EXCEPTION_PASSMARK_NOTINTEGER);
    	request.getSession().removeAttribute(USER_EXCEPTION_NUMBERFORMAT);
    	request.getSession().removeAttribute(USER_EXCEPTION_PASSMARK_EMPTY);
    	request.getSession().removeAttribute(USER_EXCEPTION_PASSMARK_GREATER100);    	
    	request.getSession().removeAttribute(USER_EXCEPTION_FILENAME_EMPTY);
    	request.getSession().removeAttribute(USER_EXCEPTION_WEIGHT_MUST_EQUAL100);
    	request.getSession().removeAttribute(USER_EXCEPTION_SINGLE_OPTION);
    }
}

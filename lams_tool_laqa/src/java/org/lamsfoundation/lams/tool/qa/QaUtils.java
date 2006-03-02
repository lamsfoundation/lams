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
package org.lamsfoundation.lams.tool.qa;

import java.security.Principal;
import java.text.DateFormat;
import java.util.ArrayList;
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
import org.lamsfoundation.lams.tool.qa.service.IQaService;
import org.lamsfoundation.lams.tool.qa.web.QaAuthoringForm;
import org.lamsfoundation.lams.usermanagement.User;
import org.lamsfoundation.lams.usermanagement.dto.UserDTO;
import org.lamsfoundation.lams.web.session.SessionManager;
import org.lamsfoundation.lams.web.util.AttributeNames;

/**
 * 
 * 
 * The session attributes ATTR_USERDATA refer to the same User object.
 * 
 * Verify the assumption:
 * We make the assumption that the obtained User object will habe a userId property ready in it. 
 * We use the same  userId property as the user table key when we are saving learner responses and associated user data. 
 *   * 
 */

/**
 * @author Ozgur Demirtas
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */

/**
 * Common utility functions live here.  
 */
public abstract class QaUtils implements QaAppConstants {
	static Logger logger = Logger.getLogger(QaUtils.class.getName());
	
	public static IQaService getToolService(HttpServletRequest request)
	{
		IQaService qaService=(IQaService)request.getSession().getAttribute(TOOL_SERVICE);
	    return qaService;
	}
	
	public static long generateId()
	{
		Random generator = new Random();
		long longId=generator.nextLong();
		if (longId < 0) longId=longId * (-1) ;
		return longId;
	}
	
	/**
	 * helps create a mock user object in development time.
	 * static long generateIntegerId()
	 * @return long
	 */
	public static int generateIntegerId()
	{
		Random generator = new Random();
		int intId=generator.nextInt();
		if (intId < 0) intId=intId * (-1) ;
		return intId;
	}
	
	
	/**
     * cleanupSession(TreeMap mapQuestionContent, HttpServletRequest request)
     * return void
     * cleans up the session of the content details
     */
    public static void cleanupSession(HttpServletRequest request)
    {
    	/* remove session attributes in Authoring mode */ 
		request.getSession().removeAttribute(DEFAULT_QUESTION_CONTENT);
		request.getSession().removeAttribute(MAP_QUESTION_CONTENT);
		request.getSession().removeAttribute(CHOICE);
		request.getSession().removeAttribute(IS_DEFINE_LATER);
		request.getSession().removeAttribute(DISABLE_TOOL);
		request.getSession().removeAttribute(CHOICE_TYPE_BASIC);
	    request.getSession().removeAttribute(CHOICE_TYPE_ADVANCED);
	    request.getSession().removeAttribute(CHOICE_TYPE_INSTRUCTIONS);
	    request.getSession().removeAttribute(REPORT_TITLE);
	    request.getSession().removeAttribute(INSTRUCTIONS);
	    request.getSession().removeAttribute(TITLE);
	    request.getSession().removeAttribute(CONTENT_LOCKED);
	    
		/* remove session attributes in Learner mode */
		request.getSession().removeAttribute(MAP_ANSWERS);
		request.getSession().removeAttribute(MAP_QUESTION_CONTENT_LEARNER);
		request.getSession().removeAttribute(CURRENT_QUESTION_INDEX);
		request.getSession().removeAttribute(TOTAL_QUESTION_COUNT);
		request.getSession().removeAttribute(CURRENT_ANSWER);
		request.getSession().removeAttribute(USER_FEEDBACK);
		request.getSession().removeAttribute(AttributeNames.PARAM_TOOL_SESSION_ID);
		request.getSession().removeAttribute(QUESTION_LISTING_MODE);
		request.getSession().removeAttribute(QUESTION_LISTING_MODE_SEQUENTIAL);
		request.getSession().removeAttribute(QUESTION_LISTING_MODE_COMBINED);
		request.getSession().removeAttribute(MAP_USER_RESPONSES);
		request.getSession().removeAttribute(MAP_MAIN_REPORT);
		request.getSession().removeAttribute(REPORT_TITLE_LEARNER);
		request.getSession().removeAttribute(END_LEARNING_MESSAGE);
		request.getSession().removeAttribute(IS_TOOL_ACTIVITY_OFFLINE);
		
		/* remove session attributes in Monitoring mode */
		request.getSession().removeAttribute(MAP_TOOL_SESSIONS);
		request.getSession().removeAttribute(MAP_MONITORING_QUESTIONS);
		
		/* remove session attributes used commonly */
		request.getSession().removeAttribute(IS_USERNAME_VISIBLE);
		request.getSession().removeAttribute(REPORT_TITLE_MONITOR);
		request.getSession().removeAttribute(IS_ALL_SESSIONS_COMPLETED);
		request.getSession().removeAttribute(CHECK_ALL_SESSIONS_COMPLETED);
		request.getSession().removeAttribute(AttributeNames.PARAM_TOOL_CONTENT_ID);
		request.getSession().removeAttribute(ATTR_USERDATA);
		request.getSession().removeAttribute(TARGET_MODE);
    }

    /**
     * setDefaultSessionAttributes(HttpServletRequest request, QaContent defaultQaContent, QaAuthoringForm qaAuthoringForm)
     * 
     * @param request
     * @param defaultQaContent
     * @param qaAuthoringForm
     */
    public static void setDefaultSessionAttributes(HttpServletRequest request, QaContent defaultQaContent, QaAuthoringForm qaAuthoringForm)
	{
		/*should never be null anyway as default content MUST exist in the db*/
        if(defaultQaContent == null)
            throw new NullPointerException("Default QaContent cannot be null");

		qaAuthoringForm.setTitle(defaultQaContent.getTitle());
		qaAuthoringForm.setInstructions(defaultQaContent.getInstructions());
		qaAuthoringForm.setReportTitle(defaultQaContent.getReportTitle());
        qaAuthoringForm.setMonitoringReportTitle(defaultQaContent.getMonitoringReportTitle());
		qaAuthoringForm.setEndLearningMessage(defaultQaContent.getEndLearningMessage());
		qaAuthoringForm.setOnlineInstructions(defaultQaContent.getOnlineInstructions());
		qaAuthoringForm.setOfflineInstructions(defaultQaContent.getOfflineInstructions());
		qaAuthoringForm.setMonitoringReportTitle(defaultQaContent.getMonitoringReportTitle());
		
         //determine the status of radio boxes
        qaAuthoringForm.setUsernameVisible(defaultQaContent.isUsernameVisible()?ON:OFF);
        qaAuthoringForm.setSynchInMonitor(defaultQaContent.isSynchInMonitor()?ON:OFF);
        qaAuthoringForm.setQuestionsSequenced(defaultQaContent.isQuestionsSequenced()?ON:OFF);
	}
    
    
    /**
     * Helper method to retrieve the user data. We always load up from http
     * session first to optimize the performance. If no session cache available,
     * we load it from data source.
     * @param request A standard Servlet HttpServletRequest class.
     * @param surveyService the service facade of qa tool
     * @return the user data value object
     */
	public static User getUserData(HttpServletRequest request,IQaService qaService) throws QaApplicationException
    {
        User userCompleteData = (User) request.getSession().getAttribute(ATTR_USERDATA);
	    logger.debug(logger + " " + "QaUtils" +  "retrieving userCompleteData: " + userCompleteData);
        /*
         * if no session cache available, retrieve it from data source
         */
        if (userCompleteData == null)
        {	
        	/*
             * WebUtil.getUsername(request,DEVELOPMENT_FLAG) returns the current learner's username based on 
             * user principals defined in the container. If no username is defined in the container, we get a RunTimeException.
             */
        	
        	/*
        	 * pass testing flag as false to obtain user principal 
        	 */
        	try
			{
        		String userName=getUsername(request,false);
        		userCompleteData = qaService.getCurrentUserData(userName);
        	}
        	catch(QaApplicationException e)
			{
        		logger.debug(logger + " " + "QaUtils" +  " Exception occured: Tool expects the current user is an authenticated user and he has a security principal defined. Can't continue!: " + e);
        		throw new QaApplicationException("Exception occured: " +
    			"Tool expects the current user is an authenticated user and he has a security principal defined. Can't continue!");	
			}
        	
            logger.debug(logger + " " + "QaUtils" +  "retrieving userCompleteData from service: " + userCompleteData);
            /* this can be redundant as we keep the User data in TOOL_USER */
            request.getSession().setAttribute(ATTR_USERDATA, userCompleteData);
        }
        return userCompleteData;
    }

	public static int getCurrentUserId(HttpServletRequest request) throws QaApplicationException
    {
	    HttpSession ss = SessionManager.getSession();
	    /* get back login user DTO */
	    UserDTO user = (UserDTO) ss.getAttribute(AttributeNames.USER);
		logger.debug(logger + " " + "QaUtils" +  " Current user is: " + user + " with id: " + user.getUserID());
		return user.getUserID().intValue();
    }
	
	
	/**
	 * Modified to throw QaApplicationException insteadof RuntimeException
	 * String getUsername(HttpServletRequest req,boolean isTesting) throws RuntimeException
	 * is normally lives in package org.lamsfoundation.lams.util. It generates Runtime exception when the user principal 
	 * is not found. We find this not too usefulespeciaaly in teh development time. Below is a local and modified version
	 * of this function. 
	 * 
	 * @return username from principal object
	 */
	public static String getUsername(HttpServletRequest req,boolean isTesting) throws QaApplicationException
	{
	    if(isTesting)
	        return "test";
	    
		Principal principal = req.getUserPrincipal();
		if (principal == null)
		{
			throw new QaApplicationException("Trying to get username but principal object missing. Request is "
					+ req.toString());
		}
			
		String username = principal.getName();
		if (username == null)
		{
			throw new QaApplicationException("Name missing from principal object. Request is "
					+ req.toString()
					+ " Principal object is "
					+ principal.toString());
		}
		return username;
	}
	
	
	public static boolean getDefineLaterStatus()
	{
		return false;
	}
	
	
	/**
	 * existsContent(long toolContentId)
	 * @param long toolContentId
	 * @return boolean
	 * determine whether a specific toolContentId exists in the db
	 */
	public static boolean existsContent(long toolContentId, IQaService qaService)
	{    
    	QaContent qaContent=qaService.loadQa(toolContentId);
	    logger.debug(logger + " " + "QaUtils " +  "retrieving qaContent: " + qaContent);
	    if (qaContent == null) 
	    	return false;
	    
		return true;	
	}

	/**
	 * it is expected that the tool session id already exists in the tool sessions table
	 * existsSession(long toolSessionId)
	 * @param toolSessionId
	 * @return boolean
	 */
	public static boolean existsSession(long toolSessionId, IQaService qaService)
	{
		logger.debug("existsSession");
	    QaSession qaSession=qaService.retrieveQaSessionOrNullById(toolSessionId);
	    logger.debug("qaSession:" + qaSession);
    	
	    if (qaSession == null) 
	    	return false;
	    
		return true;	
	}
	

	public static String getFormattedDateString(Date date)
	{
		logger.debug(logger + " " + " QaUtils getFormattedDateString: " +  
				DateFormat.getDateTimeInstance(DateFormat.LONG, DateFormat.LONG).format(date));
		return (DateFormat.getDateTimeInstance(DateFormat.LONG, DateFormat.LONG).format(date));
	}
	
	public static void persistTimeZone(HttpServletRequest request)
	{
		TimeZone timeZone=TimeZone.getDefault();
	    logger.debug("current timezone: " + timeZone.getDisplayName());
	    request.getSession().setAttribute(TIMEZONE, timeZone.getDisplayName());
	    logger.debug("current timezone id: " + timeZone.getID());
	    request.getSession().setAttribute(TIMEZONE_ID, timeZone.getID());
	}

	/**
	 * stores the rich text values on the forms into the session scope
	 * persistRichText(HttpServletRequest request)
	 * @param request
	 */
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
	}
	
	
	public static void configureContentRepository(HttpServletRequest request)
	{
		logger.debug("attempt configureContentRepository");
    	IQaService qaService =QaUtils.getToolService(request);
    	logger.debug("retrieving qaService from session: " + qaService);
    	logger.debug("calling configureContentRepository()");
	    qaService.configureContentRepository();
	    logger.debug("configureContentRepository ran successfully");
	}
	
	
    public static void populateUploadedFilesData(HttpServletRequest request, QaContent qaContent, IQaService qaService) {
        List attachmentList = qaService.retrieveQaUploadedFiles(qaContent); 
        request.getSession().setAttribute(ATTACHMENT_LIST, attachmentList);
        
        if(request.getSession().getAttribute(DELETED_ATTACHMENT_LIST)==null)
            request.getSession().setAttribute(DELETED_ATTACHMENT_LIST, new ArrayList());
        
        logger.debug("populated UploadedFilesData");
    }
    
    /**
     * <p>This method is used in authoring and monitoring to display the list of files that have been uploaded.
     * The current files are included in the attachmentList, files that the user has nominated to delete are 
     * in the deletedAttachementList.</p>
     * 
     * <p>If the input collections are null, then the session variables are not modified. This
     * is particularly useful for the deleted files.</p>
     * 
     * @param request the HttpServletRequest which is used to obtain the HttpSession
     * @param attachmentList
     * @param deletedAttachmentList
     */
    public static void addUploadsToSession(HttpServletRequest request, List attachmentList, List deletedAttachmentList)
    {
        if ( attachmentList != null ) {
            request.getSession().setAttribute(ATTACHMENT_LIST, attachmentList);
        }
        
        // deleted will be empty most of the time
        if ( deletedAttachmentList != null ) {
            request.getSession().setAttribute(DELETED_ATTACHMENT_LIST, deletedAttachmentList);
        }

    }
    
    /** If this file exists in attachments map, move it to the deleted attachments map.
     * Returns the updated deletedAttachments map, creating a new one if needed. If uuid supplied
     * then tries to match on that, otherwise uses filename and isOnline. */
    public static List moveToDelete(String uuid, List attachmentsList, List deletedAttachmentsList ) {

        List deletedList = deletedAttachmentsList != null ? deletedAttachmentsList : new ArrayList();
        
        if ( attachmentsList != null ) {
            Iterator iter = attachmentsList.iterator();
            QaUploadedFile attachment = null;
            while ( iter.hasNext() && attachment == null ) {
                QaUploadedFile value = (QaUploadedFile) iter.next();
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
            QaUploadedFile attachment = null;
            while ( iter.hasNext() && attachment == null ) {
                QaUploadedFile value = (QaUploadedFile) iter.next();
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
    
    
	/**
	 * find out if the content is set to run offline or online. If it is set to run offline , the learners are informed about that..
	 * isRubnOffline(QaContent qaContent)
	 * 
	 * @param qaContent
	 * @return boolean
	 */
	public static boolean isRunOffline(QaContent qaContent)
	{
		logger.debug("is run offline: " + qaContent.isRunOffline());
		return qaContent.isRunOffline();
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
		Map map= new TreeMap(new QaComparator());
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

}

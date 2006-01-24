/*
 * Created on 21/04/2005
 *
 */
package org.lamsfoundation.lams.tool.mc;

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
import org.lamsfoundation.lams.tool.mc.pojos.McContent;
import org.lamsfoundation.lams.tool.mc.pojos.McOptsContent;
import org.lamsfoundation.lams.tool.mc.pojos.McSession;
import org.lamsfoundation.lams.tool.mc.service.IMcService;
import org.lamsfoundation.lams.tool.mc.web.McAuthoringForm;
import org.lamsfoundation.lams.usermanagement.User;
import org.lamsfoundation.lams.usermanagement.dto.UserDTO;
import org.lamsfoundation.lams.web.session.SessionManager;
import org.lamsfoundation.lams.web.util.AttributeNames;


/**
 * @author Ozgur Demirtas
 * Common MCQ utility functions live here.
 */
public abstract class McUtils implements McAppConstants {

	static Logger logger = Logger.getLogger(McUtils.class.getName());

	/**
	 * returns the service object from the session cache
	 * IMcService getToolService(HttpServletRequest request)
	 * 
	 * @param request
	 * @return
	 */
	public static IMcService getToolService(HttpServletRequest request)
	{
		IMcService mcService=(IMcService)request.getSession().getAttribute(TOOL_SERVICE);
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
		IMcService mcService =McUtils.getToolService(request);
	    
    	McContent mcContent=mcService.retrieveMc(toolContentId);
	    logger.debug("retrieving mcContent: " + mcContent);
	    if (mcContent == null) 
	    	return false;
	    
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
    	IMcService mcService =McUtils.getToolService(request);
	    McSession mcSession=mcService.retrieveMcSession(toolSessionId);
	    logger.debug("mcSession:" + mcSession);
    	
	    if (mcSession == null) 
	    	return false;
	    
		return true;	
	}
	
	public static void setDefaultSessionAttributes(HttpServletRequest request, McContent defaultMcContent, McAuthoringForm mcAuthoringForm)
	{
		/*should never be null anyway as default content MUST exist in the db*/
		if (defaultMcContent != null)
		{		
			mcAuthoringForm.setTitle(defaultMcContent.getTitle());
			mcAuthoringForm.setInstructions(defaultMcContent.getInstructions());
			mcAuthoringForm.setOfflineInstructions (defaultMcContent.getOfflineInstructions());
			mcAuthoringForm.setOnlineInstructions (defaultMcContent.getOnlineInstructions());
			
			if (defaultMcContent.getPassMark() != null) 
				mcAuthoringForm.setPassmark((defaultMcContent.getPassMark()).toString());
			else
				mcAuthoringForm.setPassmark(new Integer(0).toString());
			
		}
	}
	
	
	/**
	 * returns a Map of options
	 * generateOptionsMap(List listMcOptions)
	 * 
	 * @param listMcOptions
	 * @return Map
	 */
	public static Map generateOptionsMap(List listMcOptions)
	{
		logger.debug("incoming listMcOptions" + listMcOptions);
		Map mapOptionsContent= new TreeMap(new McComparator());
		
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
		
		String richTextFeedbackCorrect=request.getParameter(RICHTEXT_FEEDBACK_CORRECT);
		logger.debug("read parameter richTextFeedbackCorrect: " + richTextFeedbackCorrect);
		
		String richTextFeedbackInCorrect=request.getParameter(RICHTEXT_FEEDBACK_INCORRECT);
		logger.debug("read parameter richTextFeedbackInCorrect: " + richTextFeedbackInCorrect);
		
		if ((richTextFeedbackCorrect != null) && (richTextFeedbackCorrect.length() > 0))
		{
			request.getSession().setAttribute(RICHTEXT_FEEDBACK_CORRECT,richTextFeedbackCorrect);
		}
		
		if ((richTextFeedbackInCorrect != null) && (richTextFeedbackInCorrect.length() > 0))
		{
			request.getSession().setAttribute(RICHTEXT_FEEDBACK_INCORRECT,richTextFeedbackInCorrect);
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
	}
	
	
	public static void configureContentRepository(HttpServletRequest request, IMcService mcService)
	{
		logger.debug("attempt configureContentRepository");
    	mcService.configureContentRepository();
	    logger.debug("configureContentRepository ran successfully");
	}
	
	public static void populateUploadedFilesData(HttpServletRequest request, McContent defaultMcContent)
	{
		logger.debug("attempt populateUploadedFilesData for: " + defaultMcContent);
		IMcService mcService =McUtils.getToolService(request);
    	logger.debug("mcService: " + mcService);

    	/** read the uploaded offline uuid + file name pair */
	    List listOffFilesName=mcService.retrieveMcUploadedOfflineFilesName(defaultMcContent.getUid());
	    logger.debug("initial listOfflineFilesName: " + listOffFilesName);
	    
	    /** read the uploaded online uuid + file name pair */
	    List listOnFilesName=mcService.retrieveMcUploadedOnlineFilesName(defaultMcContent.getUid());
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
	 * builds a String based map from a list 
	 * convertToMap(List sessionsList)
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

	
	/**
     * sets/resets the define later flag of the content
     * setDefineLater(HttpServletRequest request, boolean value)
     * 
     * @param request
     * @param value
     */
    public static void setDefineLater(HttpServletRequest request, boolean value)
    {
    	IMcService mcService =McUtils.getToolService(request);
    	Long toolContentId=(Long)request.getSession().getAttribute(TOOL_CONTENT_ID);
    	logger.debug("toolContentId:" + toolContentId);
    	logger.debug("value:" + value);
    	
    	McContent mcContent=mcService.retrieveMc(toolContentId);
    	logger.debug("mcContent:" + mcContent);
    	mcContent.setDefineLater(value);
    	logger.debug("defineLater has been set to true");
    	mcService.saveMcContent(mcContent);
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
			logger.debug("request is from amonitoring url. return to: " + LOAD_MONITORING);
			return LOAD_MONITORING;	
		}
	}
}

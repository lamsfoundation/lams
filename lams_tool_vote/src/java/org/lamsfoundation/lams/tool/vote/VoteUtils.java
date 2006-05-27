/***************************************************************************
 * Copyright (C) 2005 LAMS Foundation (http://lamsfoundation.org)
 * =============================================================
 * License Information: http://lamsfoundation.org/licensing/lams/2.0/
 * 
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License version 2.0
 * as published by the Free Software Foundation.
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
import java.util.TimeZone;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.lamsfoundation.lams.tool.vote.pojos.VoteContent;
import org.lamsfoundation.lams.tool.vote.pojos.VoteSession;
import org.lamsfoundation.lams.tool.vote.service.IVoteService;
import org.lamsfoundation.lams.tool.vote.web.VoteAuthoringForm;
import org.lamsfoundation.lams.usermanagement.User;
import org.lamsfoundation.lams.usermanagement.dto.UserDTO;
import org.lamsfoundation.lams.web.session.SessionManager;
import org.lamsfoundation.lams.web.util.AttributeNames;


/**
 * <p> Common Voting utility functions live here. </p>
 * 
 * @author Ozgur Demirtas
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
		IVoteService voteService=(IVoteService)request.getSession().getAttribute(TOOL_SERVICE);
	    return voteService;
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
		IVoteService voteService =VoteUtils.getToolService(request);

    	VoteContent voteContent=voteService.retrieveVote(toolContentId);
	    logger.debug("retrieving voteContent: " + voteContent);
	    if (voteContent == null) 
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
    	IVoteService voteService =VoteUtils.getToolService(request);
	    VoteSession voteSession=voteService.retrieveVoteSession(toolSessionId);
	    logger.debug("voteSession:" + voteSession);
    	
	    if (voteSession == null) 
	    	return false;
	    
		return true;	
	}
	
	
    public static void setDefaultSessionAttributes(HttpServletRequest request, VoteContent defaultVoteContent, VoteAuthoringForm voteAuthoringForm)
	{
		/*should never be null anyway as default content MUST exist in the db*/
        if(defaultVoteContent == null)
            throw new NullPointerException("Default VoteContent cannot be null");

        voteAuthoringForm.setTitle(defaultVoteContent.getTitle());
        voteAuthoringForm.setInstructions(defaultVoteContent.getInstructions());
	    voteAuthoringForm.setOnlineInstructions(defaultVoteContent.getOnlineInstructions());
	    voteAuthoringForm.setOfflineInstructions(defaultVoteContent.getOfflineInstructions());

        //determine the status of radio boxes
	    voteAuthoringForm.setAllowText(defaultVoteContent.isAllowText()?ON:OFF);
	    voteAuthoringForm.setVoteChangable(defaultVoteContent.isVoteChangable()?ON:OFF);
	    voteAuthoringForm.setLockOnFinish(defaultVoteContent.isLockOnFinish()?ON:OFF);

	    String maxNomcount= defaultVoteContent.getMaxNominationCount();
	    logger.debug("maxNomcount: " + maxNomcount);
	    if (maxNomcount.equals(""))
	        maxNomcount="0";
	    voteAuthoringForm.setMaxNominationCount(maxNomcount);
	}


    public static String stripFCKTags(String htmlText)
    {
        String noHTMLText = htmlText.replaceAll("\\<.*?\\>","").replaceAll("&nbsp;","").replaceAll("&#[0-9][0-9][0-9][0-9];","");
        String[] htmlTokens = noHTMLText.split("\n");
        String noHtmlNoNewLineStr="";
        for (int i=0; i < htmlTokens.length ; i++)
        {
            if (!htmlTokens[i].trim().equals(""))
            {
                noHtmlNoNewLineStr= noHtmlNoNewLineStr + " " + htmlTokens[i];
            }
        }
        
        if (noHtmlNoNewLineStr.length() > 50)
            return noHtmlNoNewLineStr.substring(0,51);
        
        return noHtmlNoNewLineStr;
    }

    
	public static void persistRichText(HttpServletRequest request)
	{
	    logger.debug("doing persistRichText: ");
		String richTextTitle = request.getParameter(TITLE);
	    String richTextInstructions = request.getParameter(INSTRUCTIONS);
	    
	    logger.debug("richTextTitle: " + richTextTitle);
	    logger.debug("richTextInstructions: " + richTextInstructions);
	    
	    
	    if (richTextTitle != null)
	    {
			request.getSession().setAttribute(ACTIVITY_TITLE, richTextTitle);
	    }
	    String noHTMLTitle = stripFCKTags(richTextTitle);
	    logger.debug("noHTMLTitle: " + noHTMLTitle);
	    

	
	    if (richTextInstructions != null)
	    {
			request.getSession().setAttribute(ACTIVITY_INSTRUCTIONS, richTextInstructions);
	    }
	    
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
	}
	
	
	public static void configureContentRepository(HttpServletRequest request, IVoteService voteService)
	{
		logger.debug("attempt configureContentRepository");
    	voteService.configureContentRepository();
	    logger.debug("configureContentRepository ran successfully");
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
	 * isContentInUse(VoteContent voteContent)
	 * @param voteContent
	 * @return boolean
	 */
	public static boolean isContentInUse(VoteContent voteContent)
	{
		logger.debug("is content inuse: " + voteContent.isContentInUse());
		return  voteContent.isContentInUse();
	}
	
	
	/**
	 * find out if the content is being edited in monitoring interface or not. If it is, the author can not modify it.
	 * 
	 * isDefineLater(VoteContent voteContent)
	 * @param voteContent
	 * @return boolean
	 */
	public static boolean isDefineLater(VoteContent voteContent)
	{
		logger.debug("is define later: " + voteContent.isDefineLater());
		return  voteContent.isDefineLater();
	}
	
	
	/**
	 * find out if the content is set to run offline or online. If it is set to run offline , the learners are informed about that..
	 * isRubnOffline(VoteContent voteContent)
	 * 
	 * @param voteContent
	 * @return boolean
	 */
	public static boolean isRunOffline(VoteContent voteContent)
	{
		logger.debug("is run offline: " + voteContent.isRunOffline());
		return voteContent.isRunOffline();
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
    	request.getSession().removeAttribute(USER_EXCEPTION_ANSWERS_DUPLICATE);    	
    	request.getSession().removeAttribute(USER_EXCEPTION_OPTIONS_COUNT_ZERO);
    	request.getSession().removeAttribute(USER_EXCEPTION_CHKBOXES_EMPTY);
    	request.getSession().removeAttribute(USER_EXCEPTION_SUBMIT_NONE);
    	request.getSession().removeAttribute(USER_EXCEPTION_NUMBERFORMAT);
    	request.getSession().removeAttribute(USER_EXCEPTION_FILENAME_EMPTY);
    	request.getSession().removeAttribute(USER_EXCEPTION_WEIGHT_MUST_EQUAL100);
    	request.getSession().removeAttribute(USER_EXCEPTION_SINGLE_OPTION);
    }
}

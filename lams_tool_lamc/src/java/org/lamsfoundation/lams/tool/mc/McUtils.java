/*
 * Created on 21/04/2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package org.lamsfoundation.lams.tool.mc;

import java.text.DateFormat;
import java.util.Date;
import java.util.Random;
import java.util.TimeZone;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.lamsfoundation.lams.tool.mc.service.IMcService;
import org.lamsfoundation.lams.tool.mc.web.McAuthoringForm;
import org.lamsfoundation.lams.usermanagement.User;
import org.lamsfoundation.lams.usermanagement.dto.UserDTO;
import org.lamsfoundation.lams.web.session.SessionManager;
import org.lamsfoundation.lams.web.util.AttributeNames;


/**
 * @author Ozgur Demirtas
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */

/**
 * Common utility functions live here.  
 */
public abstract class McUtils implements McAppConstants {

	static Logger logger = Logger.getLogger(McUtils.class.getName());
	
	public static IMcService getToolService(HttpServletRequest request)
	{
		IMcService mcService=(IMcService)request.getSession().getAttribute(TOOL_SERVICE);
	    return mcService;
	}
	
	
	/**
	 * generateId()
	 * return long
	 * IMPORTANT: The way we obtain either content id or tool session id must be modified
	 * so that we only use lams common to get these ids. This functionality is not 
	 * available yet in the lams common as of 21/04/2005.
	 */
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
	
	
	public static int getCurrentUserId(HttpServletRequest request) throws McApplicationException
    {
	    HttpSession ss = SessionManager.getSession();
	    UserDTO user = (UserDTO) ss.getAttribute(AttributeNames.USER);
		logger.debug(logger + " " + "McUtils" +  " Current user is: " + user + " with id: " + user.getUserID());
		return user.getUserID().intValue();
    }
	
	
	/**
	 * This method exists temporarily until we have the user data is passed properly from teh container to the tool
	 * createMockUser()
	 * @return User 
	 */
	public static User createMockUser()
	{
		logger.debug(logger + " " + "McUtils" +  " request for new new mock user");
		int randomUserId=generateIntegerId();
		User mockUser=new User();
		mockUser.setUserId(new Integer(randomUserId));
		mockUser.setFirstName(MOCK_USER_NAME + randomUserId);
		mockUser.setLastName(MOCK_USER_LASTNAME + randomUserId);
		mockUser.setLogin(MOCK_LOGIN_NAME + randomUserId); //we assume login and username refers to the same property
		logger.debug(logger + " " + "McUtils" +  " created mockuser: " + mockUser);
		return mockUser;
	}
	
	public static User createSimpleUser(Integer userId)
	{
		User user=new User();
		user.setUserId(userId);
		return user;
	}
	
	public static User createUser(Integer userId)
	{
		User user=new User();
		user.setUserId(userId);
		
		int randomUserId=generateIntegerId();
		user.setFirstName(MOCK_USER_NAME + randomUserId);
		user.setLastName(MOCK_USER_LASTNAME + randomUserId);
		user.setLogin(MOCK_LOGIN_NAME + randomUserId); 
		return user;
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
	public static boolean existsContent(Long toolContentId, HttpServletRequest request)
	{
		/**
		 * retrive the service
		 */
		IMcService mcService =McUtils.getToolService(request);
	    
    	McContent mcContent=mcService.retrieveMc(toolContentId);
	    logger.debug(logger + " " + "McUtils " +  "retrieving mcContent: " + mcContent);
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
		/**
		 * get the service
		 */
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
		/**should never be null anyway as default content MUST exist in the db*/
		if (defaultMcContent != null)
		{		
			mcAuthoringForm.setTitle(defaultMcContent.getTitle());
			mcAuthoringForm.setInstructions(defaultMcContent.getInstructions());
			mcAuthoringForm.setOfflineInstructions (defaultMcContent.getOfflineInstructions());
			mcAuthoringForm.setOnlineInstructions (defaultMcContent.getOnlineInstructions());
		}
	}
	
	
	public static String getFormattedDateString(Date date)
	{
		logger.debug(logger + " " + " McUtils getFormattedDateString: " +  
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
	
	public static void configureContentRepository(HttpServletRequest request)
	{
		logger.debug("attempt configureContentRepository");
    	IMcService mcService =McUtils.getToolService(request);
    	logger.debug("retrieving mcService from session: " + mcService);
    	logger.debug("calling configureContentRepository()");
	    mcService.configureContentRepository();
	    logger.debug("configureContentRepository ran successfully");
	}
		
}

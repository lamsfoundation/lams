package org.lamsfoundation.lams.util;

import java.security.Principal;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

import org.lamsfoundation.lams.tool.ToolAccessMode;

/**
 * helper methods useful for servlets
 */
public class WebUtil
{
    
    public static final String CURRENT_TASK_ID = "currentTaskId";
    public static final String COMPLETED_TASK_ID = "completedTaskId";
    public static final String TASK_TO_DISPLAY = "tasktodisplay";
    public static final String SEQUENCE_COMPLETED = "sequenceCompleted";
    public static final String REFRESH_PROGRESS_DATA = "refresh";
    public static final String IS_REUSABLE="isReusable";
    

	private static Logger log = Logger.getLogger(WebUtil.class);

	/**
	 */
	public static boolean isTokenValid(HttpServletRequest req, String tokenName)
	{
		if (req.getSession() != null)
		{
			String valueSession = (String) req.getSession()
											  .getAttribute(tokenName);
			String valueRequest = (String) req.getParameter(tokenName);
			log.debug("(Session Token) name : " + tokenName + " value : "
					+ valueSession);
			log.debug("(Request Token) name : " + tokenName + " value : "
					+ valueRequest);
			if ((valueSession != null) && (valueRequest != null))
			{
				if (valueSession.equals(valueRequest))
				{
					return true;
				}
			}
		}
		return false;
	}

	/**
	 */
	public static void saveTaskURL(HttpServletRequest req, String[] urls)
	{
		if (urls != null)
		{
			if (urls.length == 1)
			{
				req.setAttribute("urlA", urls[0]);
			}
			else if (urls.length == 2)
			{
				req.setAttribute("urlA", urls[0]);
				req.setAttribute("urlB", urls[1]);
			}
		}
	}

	/**
	 * 
	 * 
	 * @param HttpServletRequest
	 */
	public static void setRefresh(HttpServletRequest req, boolean refreshValue)
	{
		req.getSession().setAttribute(REFRESH_PROGRESS_DATA,
									  Boolean.toString(refreshValue));
	}

	/**
	 * This method is called each time a task has to be displayed. In this
	 * method we are setting some attributes in the Http Session.
	 * 
	 * @param HttpServletRequest
	 * @param DisplayTaskData -
	 *            contains the data's task.
	 */
	public static void initTaskAttributes(HttpServletRequest req)
	{
		req.getSession().setAttribute(CURRENT_TASK_ID, "-1");
		req.getSession().setAttribute(COMPLETED_TASK_ID, "-1");
		req.getSession().setAttribute(TASK_TO_DISPLAY, null);
		req.getSession().setAttribute(SEQUENCE_COMPLETED, "false");
		req.getSession().setAttribute(REFRESH_PROGRESS_DATA, "false");
		req.getSession().setAttribute(IS_REUSABLE,"false");
	}

	/**
	 */
	public static void saveToken(HttpServletRequest req,
								 String tokenName,
								 String tokenValue)
	{
		if (req.getSession().getAttribute(tokenName) != null)
		{
			resetToken(req, tokenName);
		}
		req.getSession().setAttribute(tokenName, tokenValue);
		log.debug("(Save Session Token) name : " + tokenName + " value : "
				+ tokenValue);

	}

	/**
	 */
	public static String retrieveToken(HttpServletRequest req, String tokenName)
	{
		return (String) req.getSession().getAttribute(tokenName);
	}

	/**
	 */
	public static void resetToken(HttpServletRequest req, String tokenName)
	{
		req.getSession().removeAttribute(tokenName);
	}

	/**
	 * @exception IllegalArgumentException -
	 *                if not set
	 */
	public static void checkString(String paramName, String paramValue) throws IllegalArgumentException
	{
		if ((paramValue == null) || (paramValue.length() < 1))
		{
			throw new IllegalArgumentException(paramName + " is required '"
					+ paramValue + "'");
		}
	}

	/**
	 * @return integer value of paramValue
	 * @exception IllegalArgumentException -
	 *                if not set or not integer
	 */
	public static int checkInteger(String paramName, String paramValue) throws IllegalArgumentException
	{
		try
		{
			checkString(paramName, paramValue);
			return Integer.parseInt(paramValue.trim());

		}
		catch (NumberFormatException e)
		{
			throw new IllegalArgumentException(paramName
					+ " should be an integer '" + paramValue + "'");
		}
	}

	/**
	 * @return long value of paramValue
	 * @exception IllegalArgumentException -
	 *                if not set or not long
	 */
	public static long checkLong(String paramName, String paramValue) throws IllegalArgumentException
	{
		try
		{
			checkString(paramName, paramValue);
			return Long.parseLong(paramValue.trim());

		}
		catch (NumberFormatException e)
		{
			throw new IllegalArgumentException(paramName
					+ " should be a long '" + paramValue + "'");
		}
	}

	/**
	 * @return boolean value of paramValue
	 * @exception IllegalArgumentException -
	 *                if not set or not boolean
	 */
	public static boolean checkBoolean(String paramName, String paramValue) throws IllegalArgumentException
	{

		checkString(paramName, paramValue);
		return Boolean.valueOf(paramValue.trim()).booleanValue();
	}

	/**
	 * @param req -
	 * @param paramName -
	 * @return parameter value
	 */
	public static int readIntParam(HttpServletRequest req, String paramName)
	{
		return checkInteger(paramName, req.getParameter(paramName));
	}

	/**
	 * The helper method to get integer parameter
	 * @param req
	 * @param paramName
	 * @return
	 */
	public static int readIntParamFromUserSession(HttpServletRequest req,
												  String paramName)
	{
		HttpSession session = req.getSession();
		if (session.getAttribute(paramName)!= null)
		{
			return checkInteger(paramName,String.valueOf(((Integer)session.getAttribute(paramName)).intValue()));
		}
		else
		{
			return -1;
		}
        
	}

	/**
	 * @param req -
	 * @param paramName -
	 * @return parameter value
	 */
	public static long readLongParam(HttpServletRequest req, String paramName)
	{
		return checkLong(paramName, req.getParameter(paramName));
	}

	/**
	 * @param req -
	 * @param paramName -
	 * @return parameter value
	 */
	public static String readStrParam(HttpServletRequest req, String paramName)
	{
		return readStrParam(req, paramName, false);
	}

	/**
	 * @param req -
	 * @param paramName -
	 * @param isOptional 
	 * @return parameter value
	 */
	public static String readStrParam(HttpServletRequest req, String paramName, boolean isOptional)
	{
		if (!isOptional)
			checkString(paramName, req.getParameter(paramName));
		return req.getParameter(paramName);
	}


	/**
	 * @param req -
	 * @param paramName -
	 * @return parameter value
	 * @exception IllegalArgumentException -
	 *                if valid boolean parameter value is not found
	 */
	public static boolean readBooleanParam(HttpServletRequest req,
										   String paramName) throws IllegalArgumentException
	{
		return checkBoolean(paramName, req.getParameter(paramName));
	}

	/**
	 * @param req -
	 * @param paramName -
	 * @param defaultValue -
	 *            if valid boolean parameter value is not found, return this
	 *            value
	 * @return parameter value
	 */
	public static boolean readBooleanParam(HttpServletRequest req,
										   String paramName,
										   boolean defaultValue)
	{
		try
		{
			return checkBoolean(paramName, req.getParameter(paramName));
		}
		catch (IllegalArgumentException e)
		{
			return defaultValue;
		}
	}

	/**
	 * TODO default proper exception at lams level to replace RuntimeException
	 * @param req -
	 * @return username from principal object
	 */
	public static String getUsername(HttpServletRequest req) throws RuntimeException
	{
		Principal prin = req.getUserPrincipal();
		if (prin == null)
			throw new RuntimeException("Trying to get username but principal object missing. Request is "
					+ req.toString());

		String username = prin.getName();
		if (username == null)
			throw new RuntimeException("Name missing from principal object. Request is "
					+ req.toString()
					+ " Principal object is "
					+ prin.toString());

		return username;
	}
	
    /**
     * Retrieve the tool access mode from http request
     * @param request
     * @param param_mode
     * @return the ToolAccessMode object
     */
    public static ToolAccessMode readToolAccessModeParam(HttpServletRequest request, String param_mode,boolean optional)
    {
        String mode = readStrParam(request, param_mode,optional);
        if(mode.equals(ToolAccessMode.AUTHOR.toString()))
            return ToolAccessMode.AUTHOR;
        else if(mode.equals(ToolAccessMode.LEARNER.toString()))
            return ToolAccessMode.LEARNER;
        else if(mode.equals(ToolAccessMode.TEACHER.toString()))
            return ToolAccessMode.TEACHER;
        else
            throw new IllegalArgumentException("["+mode+"] is not a legal mode" +
            		"in LAMS");
        
    }

}
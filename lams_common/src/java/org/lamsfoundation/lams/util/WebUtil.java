package org.lamsfoundation.lams.util;

import java.security.Principal;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

import org.lamsfoundation.lams.tool.ToolAccessMode;
import org.apache.commons.lang.StringUtils;

/**
 * helper methods useful for servlets
 */
public class WebUtil
{
    
    //---------------------------------------------------------------------
    // Class level constants - Session attributs
    //---------------------------------------------------------------------
    public static final String PARAM_MODE = "mode";
    public static final String PARAM_SESSION_STATUS = "sessionStatus";
    public static final String PARAM_CONTENT_ID = "content_id";
    
    public static final String ATTR_MODE = "mode";
    public static final String ATTR_USERNAME = "username";
    public static final String ATTR_UPDATE_PROGRESS_BAR = "updateProgressBar";
    public static final String ATTR_SESSION_STATUS = "sessionStatus";
    public static final String ATTR_LESSON_ID = "lesson_id";
    
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
	
	public static boolean readBooleanAttr(HttpServletRequest req,
										   String attrName)
	{
		return checkBoolean(attrName, (String)req.getSession().getAttribute(attrName));
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

    /**
     * <p>This helper method create the struts action forward name using the path.
     * It will chop all path related characters, such as "/" and ".do".</p>
     * 
     * <p>For example: 
     * 	  <li><code>getStrutsForwardNameFromPath("/DisplayParallelActivity.do")<code>
     * 	  = displayParallelActivity</li>
     * </p>
     * 
     * @param path
     * @return
     */
    public static String getStrutsForwardNameFromPath(String path)
    {
        String pathWithoutSlash = StringUtils.substringAfter(path,"/");
        String orginalForwardName = StringUtils.chomp(pathWithoutSlash,".do");
        
        return StringUtils.uncapitalize(orginalForwardName);
        
    }
}
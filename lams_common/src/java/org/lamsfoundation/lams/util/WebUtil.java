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
    public static final String PARAM_TOOL_SESSION_ID = "sessionId"; //works with survey but doesnt work with qa and nb which expects toolSessionId
    //public static final String PARAM_TOOL_SESSION_ID = "toolSessionId";
    public static final String PARAM_USER_ID_NEW = "userId";
    
    public static final String PARAM_CONTENT_ID = "content_id";
    public static final String PARAM_USER_ID = "user_id";
    public static final String PARAM_LESSON_ID = "lesson_id";
    public static final String PARAM_TOOL_CONTENT_ID = "contentId";
    public static final String PARAM_DIRECTORY_NAME = "directoryName";
    public static final String PARAM_FILENAME = "filename";
    
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
	public static void checkObject(String paramName, Object paramValue) throws IllegalArgumentException
	{
		boolean isNull = ( paramValue == null );
		if ( ! isNull && String.class.isInstance(paramValue) ) {
			String str = (String) paramValue;
			isNull = ( str.trim().length() == 0 );
		}
		if ( isNull ) {
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
			checkObject(paramName, paramValue);
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
			checkObject(paramName, paramValue);
			return Long.parseLong(paramValue.trim());

		}
		catch (NumberFormatException e)
		{
			throw new IllegalArgumentException(paramName
					+ " should be a long '" + paramValue + "'");
		}
	}

	/**
	 * Get a long version of paramValue, throwing an IllegalArgumentException
	 * if the parameter is null
	 * @return long value of paramValue
	 * @exception IllegalArgumentException -
	 *                if not set or not long
	 */
	public static long checkLong(String paramName, Long paramValue) throws IllegalArgumentException
	{
		checkObject(paramName, paramValue);
		return paramValue.longValue();
	}

	/**
	 * @return boolean value of paramValue
	 * @exception IllegalArgumentException -
	 *                if not set or not boolean
	 */
	public static boolean checkBoolean(String paramName, String paramValue) throws IllegalArgumentException
	{

		checkObject(paramName, paramValue);
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
			checkObject(paramName, req.getParameter(paramName));
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
	 * TODO isTesting should be removed when login is done properly.
	 * @param req -
	 * @return username from principal object
	 */
	public static String getUsername(HttpServletRequest req,boolean isTesting) throws RuntimeException
	{
	    if(isTesting)
	        return "test";
	    
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
        String orginalForwardName = StringUtils.substringBeforeLast(pathWithoutSlash,".do");
        
        return StringUtils.uncapitalize(orginalForwardName);
        
    }

    /**
     * Append a parameter to a requested url.
     * @param parameterName the name of the parameter
     * @param parameterValue the value of the parameter
     * @param learnerUrl the target url
     * @return the url with parameter appended.
     */
    public static String appendParameterToURL(String url, String parameterName, String parameterValue)
    {
        return appendParameterDeliminator(url)
        	   + parameterName
        	   + "="
        	   + parameterValue;
    }
    
    /**
     * <p>This helper append the parameter deliminator for a url.</p> 
     * It is using a null safe String util method to checkup the url String and 
     * append proper deliminator if necessary.
     * @param url the url needs to append deliminator.
     * @return target url with the deliminator;
     */
    public static String appendParameterDeliminator(String url)
    {
        if(StringUtils.containsNone(url,"?"))
            return url+"?";
        else
            return url+"&";
    }


}
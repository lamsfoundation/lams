package org.verisign.joid.util;


import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 * User: treeder
 * Date: Aug 8, 2007
 * Time: 5:18:08 PM
 */
public class CookieUtils
{
    /** Default age is 30 days */
    private static final int DEFAULT_AGE = 60 * 60 * 24 * 30;


    /**
     * Sets the cookie
     * @param response
     * @param cookieName
     * @param value
     */
    public static void setCookie( HttpServletResponse response,
                                 String cookieName, String value )
    {
        Cookie cookie = new Cookie( cookieName, value );
        cookie.setMaxAge( DEFAULT_AGE );
        response.addCookie( cookie );
    }


    /**
     * Returns the value of the cookie specified by cookieName or defaultValue if
     * Cookie does not exist.
     *
     * @param request
     * @param cookieName
     * @param defaultValue
     * @return
     */
    public static String getCookieValue( HttpServletRequest request,
                                        String cookieName,
                                        String defaultValue )
    {
        Cookie cookie = getCookie( request, cookieName );
        if ( cookie == null )
            return defaultValue;
        else
            return cookie.getValue();
    }


    public static Cookie getCookie( HttpServletRequest request, String cookieName )
    {
        Cookie[] cookies = request.getCookies();
        if ( cookies == null )
            return null;
        for ( int i = 0; i < cookies.length; i++ )
        {
            Cookie cookie = cookies[i];
            if ( cookieName.equals( cookie.getName() ) )
                return cookie;
        }
        return null;
    }
}

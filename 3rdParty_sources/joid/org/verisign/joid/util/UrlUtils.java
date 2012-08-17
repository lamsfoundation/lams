package org.verisign.joid.util;


import javax.servlet.http.HttpServletRequest;


/**
 * User: treeder
 * Date: Jul 19, 2007
 * Time: 4:05:35 PM
 */
public class UrlUtils
{

    public static String getFullUrl( HttpServletRequest request )
    {
        StringBuffer b = getServletUrl( request );
        String queryString = request.getQueryString();
        if ( queryString != null )
        {
            b.append( "?" ).append( queryString );
        }
        return b.toString();
    }


    public static StringBuffer getServletUrl( HttpServletRequest request )
    {
        StringBuffer b = new StringBuffer( getBaseUrl( request ) );
        String servletPath = request.getServletPath();
        if ( servletPath != null )
        {
            b.append( servletPath );
        }
        return b;
    }


    /**
     *
     * @param request
     * @return the url of the local host including the context, not including a trailing "/"
     * // todo: make these return StringBuffer instead
     */
    public static String getBaseUrl( HttpServletRequest request )
    {
        StringBuffer b = new StringBuffer();
        b.append( getHostUrl( request ) );
        String context = request.getContextPath();
        if ( context != null )
        {
            b.append( context );
        }
        return b.toString();
    }


    /**
     *
     * @param request
     * @return the host url without the context
     * // todo: make these return StringBuffer instead
     */
    public static String getHostUrl( HttpServletRequest request )
    {
        String scheme = request.getScheme();
        String serverName = request.getServerName();
        String port = request.getServerPort() == 80 || request.getServerPort() == 443 ? "" : ":"
            + request.getServerPort();
        String start = scheme + "://" + serverName + port;
        return start;
    }
}

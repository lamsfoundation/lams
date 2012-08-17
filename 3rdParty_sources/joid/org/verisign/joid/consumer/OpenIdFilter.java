package org.verisign.joid.consumer;


import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.verisign.joid.OpenIdRuntimeException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;


/**
 * This filter will log a user in automatically if it sees the required openid
 * parameters in the request.
 *
 * User: treeder
 * Date: Jun 8, 2007
 * Time: 6:50:15 PM
 */
public class OpenIdFilter implements Filter
{
    private static Log log = LogFactory.getLog( OpenIdFilter.class );
    private static JoidConsumer joid = new JoidConsumer();
    public static final String OPENID_ATTRIBUTE = "openid.identity"; // @TODO: remove one of these
    public static final String OPENID_IDENTITY = OPENID_ATTRIBUTE;
    boolean saveIdentityUrlAsCookie = false;
    private String cookieDomain;
    private List<String> ignorePaths = new ArrayList<String>();
    private static boolean configuredProperly = false;
    private Integer cookieMaxAge;


    public void init( FilterConfig filterConfig ) throws ServletException
    {
        log.info( "init OpenIdFilter" );
        String saveInCookie = filterConfig.getInitParameter( "saveInCookie" );
        if ( saveInCookie != null )
        {
            saveIdentityUrlAsCookie = org.verisign.joid.util.Boolean.parseBoolean( saveInCookie );
            //saveIdentityUrlAsCookie = Boolean.parseBoolean(saveInCookie);
            log.debug( "saving identities in cookie: " + saveIdentityUrlAsCookie );
        }
        cookieDomain = filterConfig.getInitParameter( "cookieDomain" );
        String cookieMaxAgeString = filterConfig.getInitParameter( "cookieMaxAge" );
        if ( cookieMaxAgeString != null )
        {
            cookieMaxAge = Integer.valueOf( cookieMaxAgeString );
        }
        String ignorePaths = filterConfig.getInitParameter( "ignorePaths" );
        if ( ignorePaths != null )
        {
            String paths[] = ignorePaths.split( "," );
            for ( int i = 0; i < paths.length; i++ )
            {
                String path = paths[i].trim();
                this.ignorePaths.add( path );
            }
        }
        configuredProperly = true;
        log.debug( "end init OpenIdFilter" );
    }


    /**
     * This is to check to make sure the OpenIdFilter is setup propertly in the
     * web.xml.
     */
    private static void ensureFilterConfiguredProperly()
    {
        if ( !configuredProperly )
        {
            //            log.warn("OpenIdFilter Not Configured Properly!");
            throw new OpenIdRuntimeException(
                "OpenIdFilter Not Configured Properly! Check your web.xml for OpenIdFilter." );
        }
    }


    public void doFilter( ServletRequest servletRequest, ServletResponse servletResponse,
                         FilterChain filterChain ) throws IOException, ServletException
    {
        // basically just check for openId parameters
        HttpServletRequest request = ( HttpServletRequest ) servletRequest;
        if ( servletRequest.getParameter( OPENID_IDENTITY ) != null && !ignored( request ) )
        {
            try
            {
                @SuppressWarnings("unchecked")
                AuthenticationResult result = joid.authenticate( convertToStringValueMap( servletRequest
                    .getParameterMap() ) );
                String identity = result.getIdentity();
                if ( identity != null )
                {
                    HttpServletRequest req = ( HttpServletRequest ) servletRequest;
                    req.getSession( true ).setAttribute( OpenIdFilter.OPENID_ATTRIBUTE, identity );
                    HttpServletResponse resp = ( HttpServletResponse ) servletResponse; // could check this before setting
                    Cookie cookie = new Cookie( OPENID_IDENTITY, identity );
                    if ( cookieDomain != null )
                    {
                        cookie.setDomain( cookieDomain );
                    }
                    if ( cookieMaxAge != null )
                    {
                        cookie.setMaxAge( cookieMaxAge.intValue() );
                    }
                    resp.addCookie( cookie );
                    // redirect to get rid of the long url
                    resp.sendRedirect( result.getResponse().getReturnTo() );
                    return;
                }
            }
            catch ( AuthenticationException e )
            {
                e.printStackTrace();
                log.info( "auth failed: " + e.getMessage() );
                // should this be handled differently?
            }
            catch ( Exception e )
            {
                e.printStackTrace();
            }
        }
        filterChain.doFilter( servletRequest, servletResponse );
    }


    private boolean ignored( HttpServletRequest request )
    {
        String servletPath = request.getServletPath();
        for ( int i = 0; i < ignorePaths.size(); i++ )
        {
            String s = ignorePaths.get( i );
            if ( servletPath.startsWith( s ) )
            {
                //                System.out.println("IGNORING: " + servletPath);
                return true;
            }
        }
        return false;
    }


    public static void logout( HttpSession session )
    {
        session.removeAttribute( OPENID_ATTRIBUTE );
    }


    private Map<String, String> convertToStringValueMap( Map<String, String[]> parameterMap )
    {
        Map<String,String> ret = new HashMap<String, String>();
        Set<Map.Entry<String, String[]>> set = parameterMap.entrySet();
        for ( Iterator<Map.Entry<String, String[]>> iter = set.iterator(); iter.hasNext(); )
        {
            Map.Entry<String,String[]> mapEntry = iter.next();
            String key = mapEntry.getKey();
            String[] value = mapEntry.getValue();
            ret.put( key, value[0] );
        }
        return ret;
    }


    public void destroy()
    {

    }


    public static JoidConsumer joid()
    {
        return joid;
    }


    public static String getCurrentUser( HttpSession session )
    {
        ensureFilterConfiguredProperly();
        String openid = ( String ) session.getAttribute( OpenIdFilter.OPENID_ATTRIBUTE );
        if ( openid != null )
        {
            return openid;
        }
        // @TODO: THIS COOKIE THING CAN'T WORK BECAUSE SOMEONE COULD FAKE IT, NEEDS AN AUTH TOKEN ALONG WITH IT
        return openid;
    }
}

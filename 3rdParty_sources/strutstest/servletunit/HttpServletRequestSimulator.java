package servletunit;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletInputStream;
import javax.servlet.ServletRequest;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.File;
import java.security.Principal;
import java.util.*;
import java.text.SimpleDateFormat;
import java.text.DateFormat;
import java.text.ParseException;


//  StrutsTestCase - a JUnit extension for testing Struts actions
//  within the context of the ActionServlet.
//  Copyright (C) 2002 Deryl Seale
//
//  This library is free software; you can redistribute it and/or
//  modify it under the terms of the Apache Software License as
//  published by the Apache Software Foundation; either version 1.1
//  of the License, or (at your option) any later version.
//
//  This library is distributed in the hope that it will be useful,
//  but WITHOUT ANY WARRANTY; without even the implied warranty of
//  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
//  Apache Software Foundation Licens for more details.
//
//  You may view the full text here: http://www.apache.org/LICENSE.txt

public class HttpServletRequestSimulator implements HttpServletRequest
{
    private Hashtable attributes;
    private String scheme;
    private String protocol = "HTTP/1.1";
    private String requestURI;
    private String requestURL;
    private String contextPath = "";
    private String servletPath;
    private String pathInfo;
    private String queryString;
    private String method;
    private String contentType;
    private Locale locale;
    private Principal principal;
    String remoteAddr;
    String localAddr;
    String remoteHost;
    String localName;
    int remotePort;
    int localPort;
    private String remoteUser;
    private String userRole;
    private String reqSessionId;
    String authType;
    String charEncoding;
    private String serverName;
    private int port;

    private Hashtable parameters;
    private Hashtable headers;
    private Vector cookies;

    private HttpSession session;
    private ServletContext context;

    /**
     * Constant used by {@link #setMethod} to indicate that the GET method
     * made this request.
     */

    public final static int GET = 0;

    /**
     * Constant used by {@link #setMethod} to indicate that the POST method
     * made this request.
     */
    public final static int POST = 1;

    /**
     * Constant used by {@link #setMethod} to indicate that the PUT method
     * made this request.
     */
    public final static int PUT = 2;

    public HttpServletRequestSimulator(ServletContext context)
    {
        scheme = "http";
        attributes = new Hashtable();
        parameters = new Hashtable();
        headers = new Hashtable();
        cookies = new Vector();
        this.context = context;
        //if (getHeader("Accept")==null)
        //setHeader("Accept","dummy accept");
    }

    /**
     * Adds a parameter to this object's list of parameters
     *
     * @param   key     The name of the parameter
     * @param   value   The value of the parameter
     */
    public void addParameter( String key, String value )
    {
        if ((key != null) && (value != null))
            this.parameters.put( key, value );
    }

    /**
     * Adds a parameter as a String array to this object's list of parameters
     */
    public void addParameter(String name, String[] values) {
        if ((name != null) && (values != null))
            parameters.put(name,values);
    }

    /**
     * Returns a java.util.Map of the parameters of this request.
     * Request parameters
     * are extra information sent with the request.  For HTTP servlets,
     * parameters are contained in the query string or posted form data.
     *
     * @return an immutable java.util.Map containing parameter names as
     * keys and parameter values as map values. The keys in the parameter
     * map are of type String. The values in the parameter map are of type
     * String array.
     *
     */
    public Map getParameterMap() {
        return this.parameters;
    }

    /**
     *
     * Returns the value of the named attribute as an <code>Object</code>,
     * or <code>null</code> if no attribute of the given name exists.
     *
     * <p> Attributes can be set two ways.  The servlet container may set
     * attributes to make available custom information about a request.
     * For example, for requests made using HTTPS, the attribute
     * <code>javax.servlet.request.X509Certificate</code> can be used to
     * retrieve information on the certificate of the client.  Attributes
     * can also be set programatically using
     * {@link ServletRequest#setAttribute}.  This allows information to be
     * embedded into a request before a {@link RequestDispatcher} call.
     *
     * <p>Attribute names should follow the same conventions as package
     * names. This specification reserves names matching <code>java.*</code>,
     * <code>javax.*</code>, and <code>sun.*</code>.
     *
     * @param s	a <code>String</code> specifying the name of
     *			the attribute
     *
     * @return		an <code>Object</code> containing the value
     *			of the attribute, or <code>null</code> if
     *			the attribute does not exist
     *
     */
    public Object getAttribute(String s)
    {
        return attributes.get(s);
    }

    /**
     * Returns an <code>Enumeration</code> containing the
     * names of the attributes available to this request.
     * This method returns an empty <code>Enumeration</code>
     * if the request has no attributes available to it.
     *
     *
     * @return		an <code>Enumeration</code> of strings
     *			containing the names
     * 			of the request's attributes
     *
     */
    public Enumeration getAttributeNames()
    {
        return attributes.keys();
    }

    /**
     * Returns the name of the authentication scheme used to protect
     * the servlet. All servlet containers support basic, form and client
     * certificate authentication, and may additionally support digest
     * authentication.
     * If the servlet is not authenticated <code>null</code> is returned.
     *
     * <p>Same as the value of the CGI variable AUTH_TYPE.
     *
     *
     * @return		one of the static members BASIC_AUTH,
     *			FORM_AUTH, CLIENT_CERT_AUTH, DIGEST_AUTH
     *			(suitable for == comparison)
     *			indicating the authentication scheme, or
     *			<code>null</code> if the request was
     *			not authenticated.
     *
     */
    public String getAuthType()
    {
        return authType;
    }

    /**
     * Returns the name of the character encoding used in the body of this
     * request. This method returns <code>null</code> if the request
     * does not specify a character encoding
     *
     *
     * @return		a <code>String</code> containing the name of
     *			the chararacter encoding, or <code>null</code>
     *			if the request does not specify a character encoding
     */
    public String getCharacterEncoding()
    {
        return charEncoding;
    }

    /**
     * Returns the length, in bytes, of the request body
     * and made available by the input stream, or -1 if the
     * length is not known. For HTTP servlets, same as the value
     * of the CGI variable CONTENT_LENGTH.
     *
     * @return		-1, since this is a mock container
     */
    public int getContentLength()
    {
        return -1;
    }

    /**
     * Returns the MIME type of the body of the request, or
     * <code>null</code> if the type is not known. For HTTP servlets,
     * same as the value of the CGI variable CONTENT_TYPE.
     *
     * @return		a <code>String</code> containing the name
     *			of the MIME type of
     * 			the request, or null if the type is not known
     *
     */
    public String getContentType()
    {
        return contentType;
    }

    /**
     *
     * Returns the portion of the request URI that indicates the context
     * of the request.  The context path always comes first in a request
     * URI.  The path starts with a "/" character but does not end with a "/"
     * character.  For servlets in the default (root) context, this method
     * returns "". The container does not decode this string.
     *
     *
     * @return		a <code>String</code> specifying the
     *			portion of the request URI that indicates the context
     *			of the request
     *
     *
     */
    public String getContextPath()
    {
        return contextPath;
    }

    /**
     * Adds a cookie that can be retrieved from this request via the
     * getCookies() method.
     *
     * @param cookie a Cookie object to be retrieved from this
     * request.
     *
     * @see #getCookies
     */
    public void addCookie(Cookie cookie) {
        cookies.addElement(cookie);
    }

    /**
     * Adds a set of cookies that can be retrieved from this request via the
     * getCookies() method.
     *
     * @param cookies an array of Cookie object to be retrieved from this
     * request.
     *
     * @see #getCookies
     */
    public void setCookies(Cookie[] cookies) {
        for (int i = 0; i < cookies.length; i++)
            this.cookies.addElement(cookies[i]);
    }

    /**
     *
     * Returns an array containing all of the <code>Cookie</code>
     * objects the client sent with this request.
     * This method returns <code>null</code> if no cookies were sent.
     *
     * @return		an array of all the <code>Cookies</code>
     *			included with this request, or <code>null</code>
     *			if the request has no cookies
     *
     *
     */
    public Cookie [] getCookies()
    {
        if (cookies.isEmpty())
            return null;
        else {
            Cookie[] cookieArray = new Cookie[cookies.size()];
            return (Cookie []) cookies.toArray(cookieArray);
        }
    }

    /**
     * Returns the value of the specified request header as a long value that represents a Date object. Use this
     * method with headers that contain dates, such as If-Modified-Since.
     * <br><br>
     * The date is returned as the number of milliseconds since January 1, 1970 GMT. The header name is case insensitive.
     * <br><br>
     * If the request did not have a header of the specified name, this method returns -1. If the header can't be converted to a date, the method throws an IllegalArgumentException.
     * @param name a String specifying the name of the header
     * @return a <code>long</code> value representing the date specified in the header expressed as the number of milliseconds since January 1, 1970 GMT, or -1 if the named header was not included with the reqest.
     */
    public long getDateHeader(String name)
    {
        String s1 = getHeader(name);
        if(s1 == null)
            return -1L;
        try
        {
            DateFormat dateFormat = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss z");
            return dateFormat.parse(s1).getTime();
        }
        catch(ParseException exception) {
            throw new IllegalArgumentException("Cannot parse date: " + s1);
        }
    }

    /**
     * Sets a header with the appropriate date string given the time in milliseconds.
     * @param name the name of the header
     * @param millis the time in milliseconds
     */
    public void setDateHeader(String name, long millis)
    {
        String dateString = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss z").format(new Date(millis));
        setHeader(name, dateString);
    }

    /**
     *
     * Returns the value of the specified request header
     * as a <code>String</code>. If the request did not include a header
     * of the specified name, this method returns <code>null</code>.
     * The header name is case insensitive. You can use
     * this method with any request header.
     *
     * @param s		a <code>String</code> specifying the
     *				header name
     *
     * @return			a <code>String</code> containing the
     *				value of the requested
     *				header, or <code>null</code>
     *				if the request does not
     *				have a header of that name
     *
     */
    public String getHeader(String s)
    {
        return (String) headers.get(s);
    }

    /**
     *
     * Returns an enumeration of all the header names
     * this request contains. If the request has no
     * headers, this method returns an empty enumeration.
     *
     * <p>Some servlet containers do not allow do not allow
     * servlets to access headers using this method, in
     * which case this method returns <code>null</code>
     *
     * @return			an enumeration of all the
     *				header names sent with this
     *				request; if the request has
     *				no headers, an empty enumeration;
     *				if the servlet container does not
     *				allow servlets to use this method,
     *				<code>null</code>
     *
     *
     */
    public Enumeration getHeaderNames()
    {
        return headers.keys();
    }

    /**
     * This operation is not supported.
     */
    public Enumeration getHeaders(String s)
    {
        throw new UnsupportedOperationException("getHeaders operation is not supported!");
    }

    /**
     * This operation is not supported.
     */
    public ServletInputStream getInputStream() throws IOException {
        throw new UnsupportedOperationException("getInputStream operation is not supported!");
    }

    /**
     *
     * Returns the value of the specified request header
     * as an <code>int</code>. If the request does not have a header
     * of the specified name, this method returns -1. If the
     * header cannot be converted to an integer, this method
     * throws a <code>NumberFormatException</code>.
     *
     * <p>The header name is case insensitive.
     *
     * @param s		a <code>String</code> specifying the name
     *				of a request header
     *
     * @return			an integer expressing the value
     * 				of the request header or -1
     *				if the request doesn't have a
     *				header of this name
     *
     * @exception	NumberFormatException		If the header value
     *							can't be converted
     *							to an <code>int</code>
     */
    public int getIntHeader(String s)
    {
        Object header = headers.get(s);
        if (header != null) {
            try {
                Integer intHeader = (Integer) header;
                return intHeader.intValue();
            } catch (ClassCastException e) {
                throw new NumberFormatException("header '" + s + "' cannot be converted to number format.");
            }
        } else
            return -1;
    }

    /**
     *
     * Returns the preferred <code>Locale</code> that the client will
     * accept content in, based on the Accept-Language header.
     * If the client request doesn't provide an Accept-Language header,
     * this method returns the default locale for the server.
     *
     *
     * @return		the preferred <code>Locale</code> for the client,
     *                  defaults to Locale.US if {@link #setLocale} has
     *                  not been called.
     *
     */
    public  Locale getLocale()
    {
        if (this.locale == null)
            return Locale.US;
        else
            return this.locale;
    }

    /**
     * Returns an Enumeration of Locale objects indicating, in decreasing order starting with the preferred locale, the locales that are acceptable to the client based on the Accept-Language header. If the client request doesn't provide an Accept-Language header, this method returns an Enumeration containing one Locale, the default locale for the server.
     * @return an <code>Enumeration</code> of preferred Locale objects for the client
     */



    public Enumeration getLocales()
    {
        return java.util.Collections.enumeration(Collections.singleton(getLocale()));
    }

    /**
     *
     * Returns the name of the HTTP method with which this
     * request was made, for example, GET, POST, or PUT.
     * Same as the value of the CGI variable REQUEST_METHOD.
     *
     * @return			a <code>String</code>
     *				specifying the name
     *				of the method with which
     *				this request was made
     *
     */
    public String getMethod()
    {
        if (method == null)
            return "POST";
        else
            return method;
    }

    /**
     * Returns the value of a request parameter as a <code>String</code>,
     * or <code>null</code> if the parameter does not exist. Request parameters
     * are extra information sent with the request.  For HTTP servlets,
     * parameters are contained in the query string or posted form data.
     *
     * <p>You should only use this method when you are sure the
     * parameter has only one value. If the parameter might have
     * more than one value, use {@link #getParameterValues}.
     *
     * <p>If you use this method with a multivalued
     * parameter, the value returned is equal to the first value
     * in the array returned by <code>getParameterValues</code>.
     *
     * <p>If the parameter data was sent in the request body, such as occurs
     * with an HTTP POST request, then reading the body directly via {@link
     * #getInputStream} or {@link #getReader} can interfere
     * with the execution of this method.
     *
     * @param s 	a <code>String</code> specifying the
     *			name of the parameter
     *
     * @return		a <code>String</code> representing the
     *			single value of the parameter
     *
     * @see 		#getParameterValues
     *
     */
    public String getParameter( String s )
    {
        if (s == null)
            return null;

        Object param = parameters.get(s);
        if( null == param )
            return null;
        if( param.getClass().isArray() )
            return ((String[]) param)[0];
        return (String)param;
    }

    /**
     *
     * Returns an <code>Enumeration</code> of <code>String</code>
     * objects containing the names of the parameters contained
     * in this request. If the request has
     * no parameters, the method returns an
     * empty <code>Enumeration</code>.
     *
     * @return		an <code>Enumeration</code> of <code>String</code>
     *			objects, each <code>String</code> containing
     * 			the name of a request parameter; or an
     *			empty <code>Enumeration</code> if the
     *			request has no parameters
     *
     */
    public Enumeration getParameterNames()
    {
        return parameters.keys();
    }

    /**
     * Returns an array of <code>String</code> objects containing
     * all of the values the given request parameter has, or
     * <code>null</code> if the parameter does not exist.
     *
     * <p>If the parameter has a single value, the array has a length
     * of 1.
     *
     * @param s	a <code>String</code> containing the name of
     *			the parameter whose value is requested
     *
     * @return		an array of <code>String</code> objects
     *			containing the parameter's values
     *
     * @see		#getParameter
     *
     */
    public String[] getParameterValues( String s )
    {
        if (s == null)
            return null;
        Object param = parameters.get( s );
        if( null == param )
            return null;
        else {
            if (param.getClass().isArray()) {
                return (String[]) param;
            } else {
                return new String[] {(String) param};
            }
        }
    }

    /**
     *
     * Returns any extra path information associated with
     * the URL the client sent when it made this request.
     * The extra path information follows the servlet path
     * but precedes the query string.
     * This method returns <code>null</code> if there
     * was no extra path information.
     *
     * <p>Same as the value of the CGI variable PATH_INFO.
     *
     *
     * @return		a <code>String</code>, decoded by the
     *			web container, specifying
     *			extra path information that comes
     *			after the servlet path but before
     *			the query string in the request URL;
     *			or <code>null</code> if the URL does not have
     *			any extra path information
     *
     */
    public String getPathInfo()
    {
        return pathInfo;
    }

    /**
     * This operation is not supported.
     */
    public String getPathTranslated()
    {
        throw new UnsupportedOperationException("getPathTranslated operation is not supported!");
    }

    /**
     * Returns the name and version of the protocol the request uses
     * in the form <i>protocol/majorVersion.minorVersion</i>, for
     * example, HTTP/1.1. For HTTP servlets, the value
     * returned is the same as the value of the CGI variable
     * <code>SERVER_PROTOCOL</code>.
     *
     * @return		a <code>String</code> containing the protocol
     *			name and version number
     *
     */
    public String getProtocol()
    {
        return protocol;
    }

    /**
     *
     * Returns the query string that is contained in the request
     * URL after the path. This method returns <code>null</code>
     * if the URL does not have a query string. Same as the value
     * of the CGI variable QUERY_STRING.
     *
     * @return		a <code>String</code> containing the query
     *			string or <code>null</code> if the URL
     *			contains no query string. The value is not
     *			decoded by the container.
     *
     */
    public String getQueryString()
    {
        return queryString;
    }

    /**
     * This operation is not supported.
     */
    public BufferedReader getReader() throws IOException {
        throw new UnsupportedOperationException("getReader operation is not supported!");
    }

    /**
     *
     * @deprecated 	As of Version 2.1 of the Java Servlet API,
     * 			use {@link ServletContext#getRealPath} instead.
     *
     */
    public String getRealPath(String path)
    {
        File contextDirectory = ((ServletContextSimulator) context).getContextDirectory();
        if ((contextDirectory == null) || (path == null))
            return null;
        else
            return (new File(contextDirectory, path)).getAbsolutePath();
    }

    /**
     * Returns the Internet Protocol (IP) address of the client
     * that sent the request.  For HTTP servlets, same as the value of the
     * CGI variable <code>REMOTE_ADDR</code>.
     *
     * @return		a <code>String</code> containing the
     *			IP address of the client that sent the request
     *
     */
    public String getRemoteAddr() {
        return remoteAddr;
    }

    /**
     * Returns the fully qualified name of the client that sent the
     * request. If the engine cannot or chooses not to resolve the hostname
     * (to improve performance), this method returns the dotted-string form of
     * the IP address. For HTTP servlets, same as the value of the CGI variable
     * <code>REMOTE_HOST</code>.
     *
     * @return		a <code>String</code> containing the fully
     * qualified name of the client
     *
     */
    public String getRemoteHost() {
        return remoteHost;
    }

    /**
     * Returns the fully qualified name of the client that sent the
     * request. If the engine cannot or chooses not to resolve the hostname
     * (to improve performance), this method returns the dotted-string form of
     * the IP address. For HTTP servlets, same as the value of the CGI variable
     * <code>REMOTE_HOST</code>.
     *
     * @return		a <code>String</code> containing the fully
     * qualified name of the client
     *
     */
    public String getRemoteUser()
    {
        return remoteUser;
    }

    /**
     *
     * Returns a {@link RequestDispatcher} object that acts as a wrapper for
     * the resource located at the given path.
     * A <code>RequestDispatcher</code> object can be used to forward
     * a request to the resource or to include the resource in a response.
     * The resource can be dynamic or static.
     *
     * <p>The pathname specified may be relative, although it cannot extend
     * outside the current servlet context.  If the path begins with
     * a "/" it is interpreted as relative to the current context root.
     * This method returns <code>null</code> if the servlet container
     * cannot return a <code>RequestDispatcher</code>.
     *
     * <p>The difference between this method and {@link
     * ServletContext#getRequestDispatcher} is that this method can take a
     * relative path.
     *
     * @param url      a <code>String</code> specifying the pathname
     *                  to the resource
     *
     * @return          a <code>RequestDispatcher</code> object
     *                  that acts as a wrapper for the resource
     *                  at the specified path
     *
     * @see             RequestDispatcherSimulator
     * @see             ServletContextSimulator#getRequestDispatcher
     *
     */
    public  RequestDispatcher getRequestDispatcher( String url )
    {
        return context.getRequestDispatcher(url);
    }

    /**
     *
     * Returns the session ID specified by the client. This may
     * not be the same as the ID of the actual session in use.
     * For example, if the request specified an old (expired)
     * session ID and the server has started a new session, this
     * method gets a new session with a new ID. If the request
     * did not specify a session ID, this method returns
     * <code>null</code>.
     *
     *
     * @return		a <code>String</code> specifying the session
     *			ID, or <code>null</code> if the request did
     *			not specify a session ID
     *
     * @see		#isRequestedSessionIdValid
     *
     */
    public String getRequestedSessionId()
    {
        return reqSessionId;
    }

    /**
     *
     * Returns the part of this request's URL from the protocol
     * name up to the query string in the first line of the HTTP request.
     * The web container does not decode this String.
     * For example:
     *
     *
     * <table>
     * <tr align=left><th>First line of HTTP request      </th>
     * <th>     Returned Value</th>
     * <tr><td>POST /some/path.html HTTP/1.1<td><td>/some/path.html
     * <tr><td>GET http://foo.bar/a.html HTTP/1.0
     * <td><td>/a.html
     * <tr><td>HEAD /xyz?a=b HTTP/1.1<td><td>/xyz
     * </table>
     *
     *
     * @return		a <code>String</code> containing
     *			the part of the URL from the
     *			protocol name up to the query string
     *
     *
     */
    public String getRequestURI()
    {
        return requestURI;
    }


    /**
     * Reconstructs the URL the client used to make the request. The returned URL contains a protocol, server name, port number, and server path, but it does not include query string parameters.
     * <br><br>
     * Because this method returns a StringBuffer, not a string, you can modify the URL easily, for example, to append query parameters.
     * <br><br>
     * This method is useful for creating redirect messages and for reporting errors.
     * @return a <code>StringBuffer</code> object containing the reconstructed URL
     */

    public StringBuffer getRequestURL()
    {
        return new StringBuffer(requestURL);
    }

    /**
     * Returns the name of the scheme used to make this request,
     * for example,
     * <code>http</code>, <code>https</code>, or <code>ftp</code>.
     * Different schemes have different rules for constructing URLs,
     * as noted in RFC 1738.
     *
     * @return		a <code>String</code> containing the name
     *			of the scheme used to make this request
     *
     */
    public String getScheme()
    {
        return scheme;
    }

    /**
     *     Returns the host name of the server that received
     *     the request. For HTTP servlets, same as the value of
     *     the CGI variable SERVER_NAME.
     *     @return the name of the server to which the request was sent
     */
    public String getServerName() {
        return serverName;
    }

    /**
     * Returns the port number on which this request was received. For HTTP servlets, same as the value of the CGI variable SERVER_PORT.
     * @return an integer specifying the port number
     */
    public int getServerPort() {
        return this.port;
    }

    /**
     * Sets the server port to be used with {@link#getServerPort}.
     */
    public void setServerPort(int port) {
        this.port = port;
    }

    /**
     *
     * Returns the part of this request's URL that calls
     * the servlet. This includes either the servlet name or
     * a path to the servlet, but does not include any extra
     * path information or a query string. Same as the value
     * of the CGI variable SCRIPT_NAME.
     *
     *
     * @return		a <code>String</code> containing
     *			the name or path of the servlet being
     *			called, as specified in the request URL,
     *			decoded.
     *
     *
     */
    public String getServletPath()
    {
        return servletPath;
    }

    /**
     *
     * Returns the current session associated with this request,
     * or if the request does not have a session, creates one.
     *
     * @return		the <code>HttpSession</code> associated
     *			with this request
     *
     * @see	#getSession(boolean)
     *
     */
    public HttpSession getSession()
    {
        return getSession(true);
    }

    /**
     *
     * Returns the current <code>HttpSession</code>
     * associated with this request or, if if there is no
     * current session and <code>create</code> is true, returns
     * a new session.
     *
     * <p>If <code>create</code> is <code>false</code>
     * and the request has no valid <code>HttpSession</code>,
     * this method returns <code>null</code>.
     *
     * <p>To make sure the session is properly maintained,
     * you must call this method before
     * the response is committed. If the container is using cookies
     * to maintain session integrity and is asked to create a new session
     * when the response is committed, an IllegalStateException is thrown.
     *
     *
     *
     *
     * @param b	<code>true</code> to create
     *			a new session for this request if necessary;
     *			<code>false</code> to return <code>null</code>
     *			if there's no current session
     *
     *
     * @return 		the <code>HttpSession</code> associated
     *			with this request or <code>null</code> if
     * 			<code>create</code> is <code>false</code>
     *			and the request has no valid session
     *
     * @see	#getSession()
     *
     *
     */
    public HttpSession getSession(boolean b)
    {
        if ((session == null) && (b))
            this.session = new HttpSessionSimulator(context);
        else if ((session != null) && (!((HttpSessionSimulator) session).isValid()) && (b))
            this.session = new HttpSessionSimulator(context);
        if ((session != null) && (((HttpSessionSimulator) session).isValid()))
            return this.session;
        else
            return null;
    }

    /**
     *
     * Returns a <code>java.security.Principal</code> object containing
     * the name of the current authenticated user. If the user has not been
     * authenticated, the method returns <code>null</code>.
     *
     * @return		a <code>java.security.Principal</code> containing
     *			the name of the user making this request;
     *			<code>null</code> if the user has not been
     *			authenticated
     *
     */
    public Principal getUserPrincipal()
    {
        return this.principal;
    }

    /**
     *
     * Checks whether the requested session ID came in as a cookie.
     *
     * @return			<code>true</code> in all cases
     *
     * @see			#getSession
     *
     */
    public boolean isRequestedSessionIdFromCookie()
    {
        return true;
    }

    /**
     *
     * @deprecated		As of Version 2.1 of the Java Servlet
     *				API, use {@link #isRequestedSessionIdFromURL}
     *				instead.
     *
     */
    public boolean isRequestedSessionIdFromUrl()
    {
        return isRequestedSessionIdFromURL();
    }

    /**
     *
     * Checks whether the requested session ID came in as part of the
     * request URL.
     *
     * @return			<code>false</code> in all cases.
     *
     * @see			#getSession
     *
     */
    public boolean isRequestedSessionIdFromURL()
    {
        return false;
    }

    /**
     *
     * Checks whether the requested session ID is still valid.
     *
     * @return			<code>true</code> if this
     *				request has an id for a valid session
     *				in the current session context;
     *				<code>false</code> otherwise
     *
     * @see			#getRequestedSessionId
     * @see			#getSession
     *
     */
    public boolean isRequestedSessionIdValid()
    {
        if (session != null) {
            try {
                session.getId();
                return true;
            } catch (IllegalStateException e) {
                return false;
            }
        } else
            return false;
    }

    /**
     *
     * Returns a boolean indicating whether this request was made using a
     * secure channel, such as HTTPS.
     *
     *
     * @return true if scheme has been set to HTTPS (ignoring case)
     *
     */
    public boolean isSecure()
    {
        if(scheme==null){
            return false;
        } else{
            return scheme.equalsIgnoreCase("HTTPS");
        }
    }

    /**
     *
     * Returns a boolean indicating whether the authenticated user is included
     * in the specified logical "role".  Roles and role membership can be
     * defined using deployment descriptors.  If the user has not been
     * authenticated, the method returns <code>false</code>.
     *
     * @param s		a <code>String</code> specifying the name
     *				of the role
     *
     * @return		<code>false</code> in all cases
     *
     */
    public boolean isUserInRole(String s)
    {
        return s.equals(userRole);
    }

    /**
     * Sets user role to be used in {@link #isUserInRole}
     */
    public void setUserRole(String role) {
        this.userRole = role;
    }

    /**
     *
     * Removes an attribute from this request.  This method is not
     * generally needed as attributes only persist as long as the request
     * is being handled.
     *
     * <p>Attribute names should follow the same conventions as
     * package names. Names beginning with <code>java.*</code>,
     * <code>javax.*</code>, and <code>com.sun.*</code>, are
     * reserved for use by Sun Microsystems.
     *
     *
     * @param s			a <code>String</code> specifying
     *					the name of the attribute to remove
     *
     */
    public void removeAttribute(String s)
    {
        attributes.remove(s);
    }

    /**
     *
     * Stores an attribute in this request.
     * Attributes are reset between requests.  This method is most
     * often used in conjunction with {@link RequestDispatcher}.
     *
     * <p>Attribute names should follow the same conventions as
     * package names. Names beginning with <code>java.*</code>,
     * <code>javax.*</code>, and <code>com.sun.*</code>, are
     * reserved for use by Sun Microsystems.
     *<br> If the value passed in is null, the effect is the same as
     * calling {@link #removeAttribute}.
     *
     *
     *
     * @param name			a <code>String</code> specifying
     *					the name of the attribute
     *
     * @param o				the <code>Object</code> to be stored
     *
     */
    public void setAttribute(String name, Object o)
    {
        if (o == null)
            attributes.remove(name);
        else
            attributes.put(name, o);
    }


    /**
     * Sets authentication scheme to be used in {@link #getAuthType}.
     */
    public void setAuthType(String s)
    {
        authType = s;
    }

    /**
     * Sets character encoding to be used in {@link #getCharacterEncoding}.
     */
    public void setCharacterEncoding(String s)
    {
        charEncoding = s;
    }

    /**
     * Sets content type to be used in {@link #getContentType}.
     */
    public void setContentType(String s) {
        contentType = s;
    }

    /**
     * Sets a header to be used in {@link #getHeader}.
     */
    public void setHeader(String key, String value)
    {
        headers.put(key,value);
    }

    /**
     * Sets the name of the HTTP method with which this request
     * was made.  This value will be returned in the getMethod
     * method.
     *
     *
     * @param methodType one of the following constant values
     * defined in this class: {@link #GET}, {@link #POST}, and {@link #PUT}
     *
     */
    public void setMethod(int methodType)
    {
        switch (methodType)
        {
            case GET:method="GET";break;
            case PUT:method="PUT";break;
            case POST:method="POST";break;
            default:method="POST";
        }
    }

    /**
     * Sets parameter value to be used by {@link #getParameter}.
     */
    public void setParameterValue( String key, String[] value )
    {
        parameters.put( key, value );
    }

    /**
     * Sets path information to be used by {@link #getPathInfo}.
     */
    public void setPathInfo(String s)
    {
        pathInfo = s;
    }

    /**
     * Sets query string to be used by {@link #getQueryString}.
     */
    public void setQueryString(String s) {
        this.queryString = s;
    }

    /**
     * Sets remote user to be used by {@link #getRemoteUser}.
     */
    public void setRemoteUser(String remoteUser)
    {
        this.remoteUser = remoteUser;
    }

    /**
     * Sets remote address to be used by {@link #getRemoteAddr}.
     */
    public void setRemoteAddr(String remoteAddr) {
        this.remoteAddr = remoteAddr;
    }

    /**
     * Sets remote host to be used by {@link #getRemoteHost}.
     */
    public void setRemoteHost(String remoteHost) {
        this.remoteHost = remoteHost;
    }

    /**
     * Sets requested session ID to be used by {@link #getRequestedSessionId}.
     */
    public void setRequestedSessionId(String s)
    {
        reqSessionId = s;
    }

    /**
     * Sets request URI to be used by {@link #getRequestURI}.
     */
    public void setRequestURI(String requestURI)
    {
        this.requestURI = requestURI;
    }

    /**
     * Sets the request URL to be used in this test.  This method uses
     * the given request URL to also set the scheme, server name, server
     * port, request URI, and query string.
     */
    public void setRequestURL(String url) {

        // set request url
        int queryIndex = url.lastIndexOf('?');
        if (queryIndex < 0)
            queryIndex = url.length();
        this.requestURL = url.substring(0,queryIndex);

        // set query string
        if (queryIndex != url.length())
            setQueryString(url.substring(queryIndex + 1));

        // set scheme
        int schemeIndex = url.lastIndexOf("://");
        setScheme(url.substring(0,schemeIndex));

        // set uri
        setRequestURI(url.substring(url.indexOf('/',schemeIndex + 3),queryIndex));

        // set server name and port
        int portIndex = url.indexOf(':',schemeIndex + 2);
        if (portIndex > 0) {
            setServerName(url.substring(schemeIndex + 3, portIndex));
            setServerPort(Integer.parseInt(url.substring(portIndex + 1,url.indexOf('/',schemeIndex + 3))));
        } else {
            setServerName(url.substring(schemeIndex + 3,url.indexOf('/',schemeIndex + 3)));
            if (isSecure())
                setServerPort(443);
            else
                setServerPort(80);
        }
    }

    /**
     * Sets scheme to be used by {@link #getScheme}.
     */
    public void setScheme(String s)
    {
        scheme = s;
    }

    /**
     * Sets servlet path to be used by {@link #getServletPath}.
     */
    public void setServletPath(String s)
    {
        servletPath = s;
    }

    /**
     * Sets server name to be used by {@link #getServerName}.
     */
    public void setServerName(String s)
    {
        serverName = s;
    }

    /**
     * Sets the context path to be used by {@link #getContextPath}.
     */
    public void setContextPath(String s)
    {
        contextPath = s;
    }


    /**
     * Sets the locale to be used by {@link #getLocale}.
     */
    public void setLocale(Locale locale) {
        this.locale = locale;
    }

    /**
     * Sets the Principal used by {@link #getUserPrincipal}.
     */
    public void setUserPrincipal(Principal principal) {
        this.principal = principal;
    }

    public int getRemotePort() {
        return remotePort;
    }

    public void setRemotePort(int remotePort) {
        this.remotePort = remotePort;
    }

    public String getLocalAddr() {
        return localAddr;
    }

    public void setLocalAddr(String localAddr) {
        this.localAddr = localAddr;
    }

    public String getLocalName() {
        return localName;
    }

    public void setLocalName(String localName) {
        this.localName = localName;
    }

    public int getLocalPort() {
        return localPort;
    }

    public void setLocalPort(int localPort) {
        this.localPort = localPort;
    }


}

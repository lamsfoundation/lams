package com.meterware.servletunit;
/********************************************************************************************************************

*
* Copyright (c) 2000-2008, Russell Gold
*
* Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated
* documentation files (the "Software"), to deal in the Software without restriction, including without limitation
* the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and
* to permit persons to whom the Software is furnished to do so, subject to the following conditions:
*
* The above copyright notice and this permission notice shall be included in all copies or substantial portions
* of the Software.
*
* THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO
* THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
* AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF
* CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER
* DEALINGS IN THE SOFTWARE.
*
*******************************************************************************************************************/
import com.meterware.httpunit.WebRequest;
import com.meterware.httpunit.WebClient;
import com.meterware.httpunit.Base64;
import com.meterware.httpunit.HttpUnitUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.util.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpSession;
import javax.servlet.ServletInputStream;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;


/**
 * This class represents a servlet request created from a WebRequest.
 **/
class ServletUnitHttpRequest implements HttpServletRequest {

    private ServletInputStreamImpl _inputStream;
    // TODO remove when test case for [ 1509117 ] getContentType()
    // gets available
    private String                 _contentType;
    private Vector                 _locales;
    private String                 _protocol;
    private boolean                _secure;
    private RequestContext         _requestContext;
    private String                 _charset;
    private boolean                _gotReader;
    private boolean                _gotInputStream;
    private BufferedReader         _reader;
    


    /**
     * Constructs a ServletUnitHttpRequest from a WebRequest object.
     **/
    ServletUnitHttpRequest( ServletMetaData servletRequest, WebRequest request, ServletUnitContext context, Dictionary clientHeaders, byte[] messageBody ) throws MalformedURLException {
        if (context == null) throw new IllegalArgumentException( "Context must not be null" );

        _servletRequest = servletRequest;
        _request = request;
        _context = context;
        _headers = new WebClient.HeaderDictionary();
        _headers.addEntries( clientHeaders );
        _headers.addEntries( request.getHeaders() );
        setCookiesFromHeader( _headers );
        _messageBody = messageBody;
        _protocol=request.getURL().getProtocol().toLowerCase();
        _secure = _protocol.endsWith("s" );

        _requestContext = new RequestContext( request.getURL() );
        String contentTypeHeader = (String) _headers.get( "Content-Type" );
        if (contentTypeHeader != null) {
            String[] res = HttpUnitUtils.parseContentTypeHeader( contentTypeHeader );
            _contentType = res[0];
            _charset     = res[1];
            _requestContext.setMessageEncoding( _charset );
        }
        if (_headers.get( "Content-Length") == null) _headers.put( "Content-Length", Integer.toString( messageBody.length ) );

        boolean setBody=
        // pre [ 1509117 ] getContentType()
        // _messageBody != null && (_contentType == null || _contentType.indexOf( "x-www-form-urlencoded" ) >= 0 );
        // patch version:
        	_messageBody != null && (contentTypeHeader == null ||	contentTypeHeader.indexOf( "x-www-form-urlencoded" ) >= 0 );
        if (setBody) {
            _requestContext.setMessageBody( _messageBody );
        }
    }


//----------------------------------------- HttpServletRequest methods --------------------------


    /**
     * Returns the name of the authentication scheme used to protect the servlet, for example, "BASIC" or "SSL,"
     * or null if the servlet was not protected.
     **/
    public String getAuthType() {
        return null;
    }


    /**
     * Returns the query string that is contained in the request URL after the path.
     **/
    public String getQueryString() {
        return _request.getQueryString();
    }


    /**
     * Returns an array containing all of the Cookie objects the client sent with this request.
     * This method returns null if no cookies were sent.
     **/
    public Cookie[] getCookies() {
        if (_cookies.size() == 0) {
            return null;
        } else {
            Cookie[] result = new Cookie[ _cookies.size() ];
            _cookies.copyInto( result );
            return result;
        }
    }


    /**
     * Returns the value of the specified request header as an int. If the request does not have a header
     * of the specified name, this method returns -1. If the header cannot be converted to an integer,
     * this method throws a NumberFormatException.
     **/
    public int getIntHeader( String name ) {
        return Integer.parseInt( getHeader( name ) );
    }


    /**
     * Returns the value of the specified request header as a long value that represents a Date object.
     * Use this method with headers that contain dates, such as If-Modified-Since.
     * <br>
     * The date is returned as the number of milliseconds since January 1, 1970 GMT. The header name is case insensitive.
     *
     * If the request did not have a header of the specified name, this method returns -1.
     * If the header can't be converted to a date, the method throws an IllegalArgumentException.
     **/
    public long getDateHeader( String name ) {
    	try {
    		String dateString=getHeader(name);
    		Date headerDate=new Date(dateString);
    		return headerDate.getTime();
    	} catch (Exception e) {	
        return -1;
    	}  
    }


    /**
     * Returns the value of the specified request header as a String. If the request did not include
     * a header of the specified name, this method returns null. The header name is case insensitive.
     * You can use this method with any request header.
     **/
    public String getHeader( String name ) {
        return (String) _headers.get( name );
    }


    /**
     * Returns an enumeration of all the header names this request contains. If the request has no headers,
     * this method returns an empty enumeration.
     *
     * Some servlet containers do not allow do not allow servlets to access headers using this method,
     * in which case this method returns null.
     **/
    public Enumeration getHeaderNames() {
        return _headers.keys();
    }


    /**
     * Returns the part of this request's URL that calls the servlet. This includes either the servlet name
     * or a path to the servlet, but does not include any extra path information or a query string.
     **/
    public String getServletPath() {
        return _servletRequest.getServletPath();
    }


    /**
     * Returns the name of the HTTP method with which this request was made, for example, GET, POST, or PUT.
     **/
    public String getMethod() {
        return _request.getMethod();
    }


    /**
     * Returns any extra path information associated with the URL the client sent when it made this request.
     * The extra path information follows the servlet path but precedes the query string.
     * This method returns null if there was no extra path information.
     **/
    public String getPathInfo() {
        return _servletRequest.getPathInfo();
    }


    /**
     * Returns any extra path information after the servlet name but before the query string,
     * and translates it to a real path. If the URL does not have any extra path information, this method returns null.
     **/
    public String getPathTranslated() {
        return null;
    }


    /**
     * Checks whether the requested session ID came in as a cookie.
     **/
    public boolean isRequestedSessionIdFromCookie() {
        return _sessionID != null;
    }


    /**
     * Returns the login of the user making this request, if the user has been authenticated,
     * or null if the user has not been authenticated.
     * Whether the user name is sent with each subsequent request depends on the browser and type of authentication.
     **/
    public String getRemoteUser() {
        return _userName;
    }


    /**
     * Returns the session ID specified by the client. This may not be the same as the ID of the actual session in use.
     * For example, if the request specified an old (expired) session ID and the server has started a new session,
     * this method gets a new session with a new ID. If the request did not specify a session ID, this method returns null.
     **/
    public String getRequestedSessionId() {
        return _sessionID;
    }


    /**
     * Returns the part of this request's URL from the protocol name up to the query string in the first line of the HTTP request.
     **/
    public String getRequestURI() {
        return _requestContext.getRequestURI();
    }


    /**
     * Returns the current HttpSession associated with this request or, if there is no current session
     * and create is true, returns a new session.
     * <br>
     * If create is false and the request has no valid HttpSession, this method returns null.
     **/
    public HttpSession getSession( boolean create ) {
        _session = _context.getValidSession( getRequestedSessionId(), _session, create );
        return _session;
    }


    /**
     * Returns the current session associated with this request, or if the request does not have a session, creates one.
     **/
    public HttpSession getSession() {
        return getSession( true );
    }


    /**
     * Checks whether the requested session ID is still valid.
     **/
    public boolean isRequestedSessionIdValid() {
        return false;
    }


    /**
     * Checks whether the requested session ID came in as part of the request URL.
     **/
    public boolean isRequestedSessionIdFromURL() {
        return false;
    }


    /**
     * @deprecated use #isRequestedSessionIdFromURL
     **/
    public boolean isRequestedSessionIdFromUrl() {
        return isRequestedSessionIdFromURL();
    }


//--------------------------------- ServletRequest methods ----------------------------------------------------


    /**
     * Returns the length, in bytes, of the content contained in the
     * request and sent by way of the input stream or -1 if the
     * length is not known.
     **/
    public int getContentLength() {
        return getIntHeader( "Content-length" );
    }


    /**
     *
     * Returns the value of the named attribute as an <code>Object</code>.
     * This method allows the servlet engine to give the servlet
     * custom information about a request. This method returns
     * <code>null</code> if no attribute of the given name exists.
     **/
    public Object getAttribute( String name ) {
        return _attributes.get( name );
    }


    /**
     * Returns an <code>Enumeration</code> containing the
     * names of the attributes available to this request.
     * This method returns an empty <code>Enumeration</code>
     * if the request has no attributes available to it.
     **/
    public Enumeration getAttributeNames() {
        return _attributes.keys();
    }


    /**
     * Retrieves binary data from the body of the request as
     * a {@link ServletInputStream}, which
     * gives you the ability to read one line at a time.
     *
     * @return					a {@link ServletInputStream} object containing
     * 						the body of the request
     *
     * @exception IllegalStateException   	if the {@link #getReader} method
     * 						has already been called for this request
     *
     * @exception IOException    		if an input or output exception occurred
     *
     */
    public ServletInputStream getInputStream() throws IOException {
      if (_gotReader) {
        throw new IllegalStateException("getReader() has already been called for this request");
      }
      initializeInputStream();
      _gotInputStream = true;
      return _inputStream;
    }

    /**
     * initialize the inputStream
     */
    private void initializeInputStream() {
   	  if (_inputStream == null) {
         _inputStream = new ServletInputStreamImpl( _messageBody );
      }
    }

    /**
     * Returns the name of the character encoding style used in this
     * request. This method returns <code>null</code> if the request
     * does not use character encoding.
     **/
    public String getCharacterEncoding() {
        return _charset;
    }


    /**
     *
     * Returns an <code>Enumeration</code> of <code>String</code>
     * objects containing the names of the parameters contained
     * in this request. If the request has
     * no parameters or if the input stream is empty, returns an
     * empty <code>Enumeration</code>. The input stream is empty
     * when all the data returned by {@link #getInputStream} has
     * been read.
     **/
    public Enumeration getParameterNames() {
        return _requestContext.getParameterNames();
    }


    /**
     * Returns the MIME type of the content of the request, or
     * <code>null</code> if the type is not known. Same as the value
     * of the CGI variable CONTENT_TYPE.
     **/
    public String getContentType() {
    	String result;
    	result=_contentType;
    	/**
    	 * suggested fix by bug report 
    	 * [ 1509117 ] getContentType()
    	 * by Tom Parker     
    	 */
    	result=this.getHeader( "Content-Type" );
      return result;
    }


    /**
     * Returns the value of a request parameter as a <code>String</code>,
     * or <code>null</code> if the parameter does not exist. Request parameters
     * are extra information sent with the request.
     **/
    public String getParameter( String name ) {
        String[] parameters = getParameterValues( name );
        return parameters == null ? null : parameters[0];
    }


    /**
     * Returns an array of <code>String</code> objects containing
     * all of the values the
     * given request parameter has, or <code>null</code> if the
     * parameter does not exist. For example, in an HTTP servlet,
     * this method returns an array of <code>String</code> objects
     * containing the values of a query string or posted form.
     **/
    public String[] getParameterValues( String name ) {
        return _requestContext.getParameterValues( name );
    }


    /**
     * Returns the name and version of the protocol the request uses
     * in the form <i>protocol/majorVersion.minorVersion</i>, for
     * example, HTTP/1.1.
     **/
    public String getProtocol() {
        return "HTTP/1.1";
    }


    /**
     * Returns the name of the scheme used to make this request,
     * for example,
     * <code>http</code>, <code>https</code>, or <code>ftp</code>.
     * Different schemes have different rules for constructing URLs,
     * as noted in RFC 1738.
     **/
    public String getScheme() {
    	String result= isSecure() ? "https" : "http";
    	result=_protocol;
    	return result;
    }


    /**
     * Returns the fully qualified name of the client that sent the
     * request.
     **/
    public String getRemoteHost() {
        return "localhost";
    }


    /**
     * Returns the host name of the server that received the request.
     **/
    public String getServerName() {
        return "localhost";
    }


    /**
     * Returns the port number on which this request was received.
     **/
    public int getServerPort() {
        return 0;
    }


    /**
     *
     * @deprecated 	As of Version 2.1 of the Java Servlet API,
     * 			use {@link javax.servlet.ServletContext#getRealPath} instead.
     *
     */
    public String getRealPath( String path ) {
        throwNotImplementedYet();
        return "";
    }


    /**
     * Returns the body of the request as a <code>BufferedReader</code>
     * that translates character set encodings.
     * @since [ 1221537 ] Patch: ServletUnitHttpRequest.getReader not implemented yet
     * @author Tim  - timmorrow (SourceForge)
     * @return the reader
     **/
    public BufferedReader getReader() throws IOException {
    	if (_gotInputStream) {
    	     throw new IllegalStateException("getInputStream() has already been called on this request");
    	}
    	if (_reader == null) {
    	  initializeInputStream();
    	  String encoding = getCharacterEncoding();
    	  if (encoding == null) {
    	    encoding = HttpUnitUtils.DEFAULT_CHARACTER_SET;
    	  }
    	  _reader = new BufferedReader(new InputStreamReader(_inputStream, encoding));
    	  _gotReader = true;
    	}
    	return _reader;
    }


    /**
     * Returns the Internet Protocol (IP) address of the client
     * that sent the request.
     **/
    public String getRemoteAddr() {
        return LOOPBACK_ADDRESS;
    }


    /**
     *
     * Stores an attribute in the context of this request.
     * Attributes are reset between requests.
     **/
    public void setAttribute( String key, Object o ) {
        if (o == null) _attributes.remove( key );
        else _attributes.put( key, o );
    }


//--------------------------------- methods added to ServletRequest in Servlet API 2.2 ------------------------------------------------


    /**
     * Returns a boolean indicating whether this request was made using a secure channel, such as HTTPS.
     **/
    public boolean isSecure() {
        return _secure;
    }


    /**
     * Returns the preferred Locale that the client will accept content in, based on the Accept-Language header.
     * If the client request doesn't provide an Accept-Language header, this method returns the default locale for the server.
     **/
    public Locale getLocale() {
        return (Locale) getPreferredLocales().firstElement();
    }


    /**
     * Returns an Enumeration of Locale objects indicating, in decreasing order starting with the preferred locale,
     * the locales that are acceptable to the client based on the Accept-Language header.
     * If the client request doesn't provide an Accept-Language header, this
     * method returns an Enumeration containing one Locale, the default locale for the server.
     **/
    public java.util.Enumeration getLocales() {
        return getPreferredLocales().elements();
    }


    /**
     * Parses the accept-language header to obtain a vector of preferred locales
     * @return the preferred locales, sorted by qvalue
     */
    private Vector getPreferredLocales() {
        if (_locales == null) {
            _locales = new Vector();
            String languages = getHeader( "accept-language" );
            if (languages == null) {
                _locales.add( Locale.getDefault() );
            } else {
                 StringTokenizer st = new StringTokenizer( languages, "," );
                 ArrayList al = new ArrayList();
                 while (st.hasMoreTokens()) {
                     String token = st.nextToken();
                     al.add( new PrioritizedLocale( token ) );
                 }
                 Collections.sort( al );
                 for (Iterator iterator = al.iterator(); iterator.hasNext();) {
                     _locales.add( ((PrioritizedLocale) iterator.next()).getLocale() );
                 }
            }
         }
        return _locales;
    }


    /**
     * Removes an attribute from this request. This method is not generally needed
     * as attributes only persist as long as the request is being handled.
     **/
    public void removeAttribute( String name ) {
        _attributes.remove( name );
    }


    /**
     * Returns a RequestDispatcher object that acts as a wrapper for the resource located at the given path.
     * A RequestDispatcher object can be used to forward a request to the resource or to include the
     * resource in a response. The resource can be dynamic or static.
     *
     * The pathname specified may be relative, although it cannot extend outside the current servlet
     * context. If the path begins with a "/" it is interpreted as relative to the current context root.
     * This method returns null if the servlet container cannot return a RequestDispatcher.
     *
     * The difference between this method and ServletContext.getRequestDispatcher(java.lang.String)
     * is that this method can take a relative path.
     **/
    public RequestDispatcher getRequestDispatcher( String path ) {
        try {
            if (!path.startsWith( "/" )) path = combinedPath( getServletPath(), path );
            return _servletRequest.getServlet().getServletConfig().getServletContext().getRequestDispatcher( path );
        } catch (ServletException e) {
            return null;
        }
    }


    private String combinedPath( String basePath, String relativePath ) {
        if (basePath.indexOf( '/' ) < 0) return relativePath;
        return basePath.substring( 0, basePath.lastIndexOf( '/' ) ) + '/' + relativePath;
    }



//--------------------------------- methods added to HttpServletRequest in Servlet API 2.2 ------------------------------------------------


    /**
     * Returns a java.security.Principal object containing the name of the current authenticated user.
     * If the user has not been authenticated, the method returns null.
     **/
    public java.security.Principal getUserPrincipal() {
        return null;
    }


    /**
     * Returns a boolean indicating whether the authenticated user is included in the specified
     * logical "role". Roles and role membership can be defined using deployment descriptors.
     * If the user has not been authenticated, the method returns false.
     **/
    public boolean isUserInRole( String role ) {
        if (_roles == null) return false;
        for (int i = 0; i < _roles.length; i++) {
            if (role.equals( _roles[i] )) return true;
        }
        return false;
    }


    /**
     * Returns all the values of the specified request header as an Enumeration of String objects.
     **/
    public java.util.Enumeration getHeaders( String name ) {
        Vector list = new Vector();
        if (_headers.containsKey( name )) list.add( _headers.get( name ));
        return list.elements();
    }


    /**
     * Returns the portion of the request URI that indicates the context of the request.
     * The context path always comes first in a request URI. The path starts with a "/" character
     * but does not end with a "/" character. For servlets in the default (root) context,
     * this method returns "".
     **/
    public String getContextPath() {
        return _context.getContextPath();
    }


//--------------------------------------- methods added to ServletRequest in Servlet API 2.3 ----------------------------

    /**
     * Returns a java.util.Map of the parameters of this request.
     * Request parameters are extra information sent with the request. For HTTP servlets, parameters are contained
     * in the query string or posted form data.
     *
     * @since 1.3
     **/
    public Map getParameterMap() {
        return _requestContext.getParameterMap();
    }


    /**
     * Overrides the name of the character encoding used in the body of this request.
     * This method must be called prior to reading request parameters or reading input using getReader().
     *
     * @since 1.3
     **/
    public void setCharacterEncoding( String charset ) throws UnsupportedEncodingException {
        _charset = charset;
        _requestContext.setMessageEncoding( charset );
    }


//--------------------------------------- methods added to HttpServletRequest in Servlet API 2.3 ----------------------------


    /**
     * Reconstructs the URL the client used to make the request.
     * The returned URL contains a protocol, server name, port number, and server path, but
     * it does not include query string parameters.
     *
     * Because this method returns a StringBuffer, not a string, you can modify the URL easily, for example,
     * to append query parameters.
     *
     * This method is useful for creating redirect messages and for reporting errors.
     *
     * @since 1.3
     */
    public StringBuffer getRequestURL() {
        StringBuffer url = new StringBuffer();
        try {
            url.append( _request.getURL().getProtocol() ).append( "://" );
            url.append( _request.getURL().getHost() );
            url.append( _request.getURL().getPath() );
        } catch (MalformedURLException e) {
            throw new RuntimeException( "unable to read URL from request: " + _request );
        }
        return url;
    }


//--------------------------------------- methods added to ServletRequest in Servlet API 2.4 ----------------------------

    public int getRemotePort() {
        return 0;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public String getLocalName() {
        return "localhost";
    }

    public String getLocalAddr() {
        return "127.0.0.1";
    }

    public int getLocalPort() {
        return 0;  //To change body of implemented methods use File | Settings | File Templates.
    }

//--------------------------------------------- package members ----------------------------------------------


    private void addCookie( Cookie cookie ) {
        _cookies.addElement( cookie );
        if (cookie.getName().equalsIgnoreCase( ServletUnitHttpSession.SESSION_COOKIE_NAME )) {
            _sessionID = cookie.getValue();
        }
    }


    private ServletUnitHttpSession getServletSession() {
        return (ServletUnitHttpSession) getSession();
    }


    void readFormAuthentication() {
        if (getSession( /* create */ false ) != null) {
            recordAuthenticationInfo( getServletSession().getUserName(), getServletSession().getRoles() );
        }
    }


    void readBasicAuthentication() {
        String authorizationHeader = (String) _headers.get( "Authorization" );

        if (authorizationHeader != null) {
            String userAndPassword = Base64.decode( authorizationHeader.substring( authorizationHeader.indexOf( ' ' ) + 1 ) );
            int colonPos = userAndPassword.indexOf( ':' );
            recordAuthenticationInfo( userAndPassword.substring( 0, colonPos ),
                                      toArray( userAndPassword.substring( colonPos+1 ) ) );
        }
    }

    static String[] toArray( String roleList ) {
        StringTokenizer st = new StringTokenizer( roleList, "," );
        String[] result = new String[ st.countTokens() ];
        for (int i = 0; i < result.length; i++) {
            result[i] = st.nextToken();
        }
        return result;
    }


    void recordAuthenticationInfo( String userName, String[] roles ) {
        _userName = userName;
        _roles    = roles;
    }


//--------------------------------------------- private members ----------------------------------------------

    final static private String LOOPBACK_ADDRESS = "127.0.0.1";

    private WebRequest                 _request;
    private ServletMetaData            _servletRequest;
    private WebClient.HeaderDictionary _headers;
    private ServletUnitContext         _context;
    private ServletUnitHttpSession     _session;
    private Hashtable                  _attributes = new Hashtable();
    private Vector                     _cookies    = new Vector();
    private String                     _sessionID;
    private byte[]                     _messageBody;

    private String                     _userName;
    private String[]                   _roles;


    private void throwNotImplementedYet() {
        throw new RuntimeException( "Not implemented yet" );
    }


    private void setCookiesFromHeader( Dictionary clientHeaders ) {
        String cookieHeader = (String) clientHeaders.get( "Cookie" );
        if (cookieHeader == null) return;

        StringTokenizer st = new StringTokenizer( cookieHeader, ",;=", true );
        String lastToken = st.nextToken();
        while (st.hasMoreTokens()) {
            String token = st.nextToken();
            if (token.equals( "=" )) {
                if (st.hasMoreTokens()) addCookie( new Cookie( lastToken.trim(), st.nextToken().trim() ) );
            }
            lastToken = token;
        }
    }


    static class PrioritizedLocale implements Comparable {

        private Locale _locale;
        private float  _priority;


        PrioritizedLocale( String languageSpec ) {
            int semiIndex = languageSpec.indexOf( ';' );
            if (semiIndex < 0) {
                _priority = 1;
                _locale = parseLocale( languageSpec );
            } else {
                _priority = Float.parseFloat( languageSpec.substring( languageSpec.indexOf( '=', semiIndex )+1 ) );
                _locale = parseLocale( languageSpec.substring( 0, semiIndex ) );
            }
        }


        private Locale parseLocale( String range ) {
            range = range.trim();
            int dashIndex = range.indexOf( '-' );
            if (dashIndex < 0) {
                return new Locale( range, "" );
            } else {
                return new Locale( range.substring( 0, dashIndex ), range.substring( dashIndex+1 ) );
            }
        }


        public Locale getLocale() {
            return _locale;
        }


        public int compareTo( Object o ) {
            if (!(o instanceof PrioritizedLocale)) throw new IllegalArgumentException( "may only combine with other prioritized locales" );
            PrioritizedLocale other = (PrioritizedLocale) o;
            return _priority == other._priority ? _locale.getLanguage().compareTo( other._locale.getLanguage() )
                                                : (_priority < other._priority ? +1 : -1 );
        }

    }
}



package com.meterware.httpunit;
/********************************************************************************************************************

*
* Copyright (c) 2000-2004,2007 Russell Gold
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
import java.io.IOException;
import java.io.OutputStream;

import java.net.HttpURLConnection;
import java.net.URL;
import java.net.PasswordAuthentication;

import java.util.*;

import org.xml.sax.SAXException;

import com.meterware.httpunit.cookies.Cookie;
import com.meterware.httpunit.cookies.CookieJar;


/**
 * The context for a series of web requests. This class manages cookies used to maintain
 * session context, computes relative URLs, and generally emulates the browser behavior
 * needed to build an automated test of a web site.
 *
 * @author <a href="mailto:russgold@httpunit.org">Russell Gold</a>
 * @author Jan Ohrstrom
 * @author Seth Ladd
 * @author Oliver Imbusch
 **/
abstract
public class WebClient {

    private ArrayList _openWindows = new ArrayList();

    /** The current main window. **/
    private WebWindow _mainWindow = new WebWindow( this );

    /** An authorization string to be sent with every request, whether challenged or not. May be null. **/
    private String _fixedAuthorizationString;

    /** An authorization string to be sent with the next request only. May be null. **/
    private String _authorizationString;

    private String _proxyAuthorizationString;
    private Hashtable _credentials = new Hashtable();


    public WebWindow getMainWindow() {
        return _mainWindow;
    }


    public void setMainWindow( WebWindow mainWindow ) {
        if (!_openWindows.contains( mainWindow )) throw new IllegalArgumentException( "May only select an open window owned by this client" );
        _mainWindow = mainWindow;
    }


    public WebWindow[] getOpenWindows() {
        return (WebWindow[]) _openWindows.toArray( new WebWindow[ _openWindows.size() ] );
    }


    public WebWindow getOpenWindow( String name ) {
        if (name == null || name.length() == 0) return null;
        for (Iterator i = _openWindows.iterator(); i.hasNext();) {
            WebWindow window = (WebWindow) i.next();
            if (name.equals( window.getName() )) return window;
        }
        return null;
    }


    /**
     * Submits a GET method request and returns a response.
     * @exception SAXException thrown if there is an error parsing the retrieved page
     **/
    public WebResponse getResponse( String urlString ) throws IOException, SAXException {
        return _mainWindow.getResponse( urlString );
    }


    /**
     * Submits a web request and returns a response. This is an alternate name for the getResponse method.
     */
    public WebResponse sendRequest( WebRequest request ) throws IOException, SAXException {
        return _mainWindow.sendRequest( request );
    }


    /**
     * Returns the response representing the current top page in the main window.
     */
    public WebResponse getCurrentPage() {
        return _mainWindow.getCurrentPage();
    }


    /**
     * Submits a web request and returns a response, using all state developed so far as stored in
     * cookies as requested by the server.
     * @exception SAXException thrown if there is an error parsing the retrieved page
     **/
    public WebResponse getResponse( WebRequest request ) throws IOException, SAXException {
        return _mainWindow.getResponse( request );
    }


    /**
     * Returns the name of the currently active frames in the main window.
     **/
    public String[] getFrameNames() {
        return _mainWindow.getFrameNames();
    }


    /**
     * Returns the response associated with the specified frame name in the main window.
     * Throws a runtime exception if no matching frame is defined.
     **/
    public WebResponse getFrameContents( String frameName ) {
        return _mainWindow.getFrameContents( frameName );
    }


    /**
     * Returns the response associated with the specified frame name in the main window.
     * Throws a runtime exception if no matching frame is defined.
     *
     * @since 1.6
     **/
    public WebResponse getFrameContents( FrameSelector targetFrame ) {
        return _mainWindow.getFrameContents( targetFrame );
    }


    /**
     * Returns the resource specified by the request. Does not update the client or load included framesets or scripts.
     * May return null if the resource is a JavaScript URL which would normally leave the client unchanged.
     */
    public WebResponse getResource( WebRequest request ) throws IOException {
        return _mainWindow.getResource( request );
    }


    /**
     * Resets the state of this client, removing all cookies, frames, and per-client headers. This does not affect
     * any listeners or preferences which may have been set.
     **/
    public void clearContents() {
        _mainWindow = new WebWindow( this );
        _cookieJar.clear();
        _headers = new HeaderDictionary();
    }


    /**
     * Defines a cookie to be sent to the server on every request.
     * @deprecated as of 1.6, use #putCookie instead.
     **/
    public void addCookie( String name, String value ) {
        _cookieJar.addCookie( name, value );
    }


    /**
     * Defines a cookie to be sent to the server on every request. This overrides any previous setting for this cookie name.
     **/
    public void putCookie( String name, String value ) {
        _cookieJar.putCookie( name, value );
    }


    /**
     * Returns the name of all the active cookies which will be sent to the server.
     **/
    public String[] getCookieNames() {
        return _cookieJar.getCookieNames();
    }

    /**
     * Returns an object containing the details of the named cookie
     * @since [ 1488617 ] alternate patch for cookie bug #1371204
     */
    public Cookie getCookieDetails( String name ) {
    	return _cookieJar.getCookie( name );
    }
    
    /**
     * Returns the value of the specified cookie.
     **/
    public String getCookieValue( String name ) {
        return _cookieJar.getCookieValue( name );
    }


    /**
     * Returns the properties associated with this client.
     */
    public ClientProperties getClientProperties() {
        if (_clientProperties == null) {
             _clientProperties = ClientProperties.getDefaultProperties().cloneProperties();
        }
        return _clientProperties;
    }


    /**
     * Specifies the user agent identification. Used to trigger browser-specific server behavior.
     * @deprecated as of 1.4.6. Use ClientProperties#setUserAgent instead.
     **/
    public void setUserAgent( String userAgent ) {
        getClientProperties().setUserAgent( userAgent );
    }


    /**
     * Returns the current user agent setting.
     * @deprecated as of 1.4.6. Use ClientProperties#getUserAgent instead.
     **/
    public String getUserAgent() {
        return getClientProperties().getUserAgent();
    }


    /**
     * Sets a username and password for a basic authentication scheme.
     * @deprecated as of 1.7. Use #setAuthentication for more accurate emulation of browser behavior.
     **/
    public void setAuthorization( String userName, String password ) {
        _fixedAuthorizationString = "Basic " + Base64.encode( userName + ':' + password );
    }


    /**
     * Specifies a username and password for on-demand authentication. Will only send
     * the authorization header when challenged for the specified realm.
     * @param realm the realm for which the credentials apply.
     * @param username the user to authenticate
     * @param password the credentials for the user
     */
    public void setAuthentication( String realm, String username, String password ) {
        _credentials.put( realm, new PasswordAuthentication( username, password.toCharArray() ) );
    }


    PasswordAuthentication getCredentialsForRealm( String realm ) {
        return ((PasswordAuthentication) _credentials.get( realm ));
    }

    /**
     * Specifies a proxy server to use for requests from this client.
     */
    public abstract void setProxyServer( String proxyHost, int proxyPort );

    /**
     * Specifies a proxy server to use, along with a user and password for authentication.
     *
     * @since 1.6
     */
    public void setProxyServer( String proxyHost, int proxyPort, String userName, String password ) {
        setProxyServer( proxyHost, proxyPort );
        _proxyAuthorizationString = "Basic " + Base64.encode( userName + ':' + password );
    }


    /**
     * Clears the proxy server settings.
     */
    public void clearProxyServer() {
    }


    /**
     * Returns the name of the active proxy server.
     */
    public String getProxyHost() {
        return System.getProperty( "proxyHost" );
    }


    /**
     * Returns the number of the active proxy port, or 0 is none is specified.
     */
    public int getProxyPort() {
        try {
            return Integer.parseInt( System.getProperty( "proxyPort" ) );
        } catch (NumberFormatException e) {
            return 0;
        }
    }




    /**
     * Sets the value for a header field to be sent with all requests. If the value set is null,
     * removes the header from those to be sent.
     **/
    public void setHeaderField( String fieldName, String fieldValue ) {
        _headers.put( fieldName, fieldValue );
    }


    /**
     * Returns the value for the header field with the specified name. This method will ignore the case of the field name.
     */
    public String getHeaderField( String fieldName ) {
        return (String) _headers.get( fieldName );
    }


    /**
     * Specifies whether an exception will be thrown when an error status (4xx or 5xx) is detected on a response.
     * Defaults to the value returned by HttpUnitOptions.getExceptionsThrownOnErrorStatus.
     **/
    public void setExceptionsThrownOnErrorStatus( boolean throwExceptions ) {
        _exceptionsThrownOnErrorStatus = throwExceptions;
    }


    /**
     * Returns true if an exception will be thrown when an error status (4xx or 5xx) is detected on a response.
     **/
    public boolean getExceptionsThrownOnErrorStatus() {
        return _exceptionsThrownOnErrorStatus;
    }


    /**
     * Adds a listener to watch for requests and responses.
     */
    public void addClientListener( WebClientListener listener ) {
        synchronized (_clientListeners) {
            if (listener != null && !_clientListeners.contains( listener )) _clientListeners.add( listener );
        }
    }


    /**
     * Removes a listener to watch for requests and responses.
     */
    public void removeClientListener( WebClientListener listener ) {
        synchronized (_clientListeners) {
            _clientListeners.remove( listener );
        }
    }


    /**
     * Adds a listener to watch for window openings and closings.
     */
    public void addWindowListener( WebWindowListener listener ) {
        synchronized (_windowListeners) {
            if (listener != null && !_windowListeners.contains( listener )) _windowListeners.add( listener );
        }
    }


    /**
     * Removes a listener to watch for window openings and closings.
     */
    public void removeWindowListener( WebWindowListener listener ) {
        synchronized (_windowListeners) {
            _windowListeners.remove( listener );
        }
    }


    /**
     * Returns the next javascript alert without removing it from the queue.
     */
    public String getNextAlert() {
        return _alerts.isEmpty() ? null : (String) _alerts.getFirst();
    }


    /**
     * Returns the next javascript alert and removes it from the queue. If the queue is empty,
     * will return an empty string.
     */
    public String popNextAlert() {
        if (_alerts.isEmpty()) return "";
        return (String) _alerts.removeFirst();
    }


    /**
     * Specifies the object which will respond to all dialogs.
     **/
    public void setDialogResponder( DialogResponder responder ) {
        _dialogResponder = responder;
    }


//------------------------------------------ protected members -----------------------------------


    protected WebClient() {
        _openWindows.add( _mainWindow );
    }


    /**
     * Creates a web response object which represents the response to the specified web request.
     * @param request the request to which the response should be generated
     * @param targetFrame the frame in which the response should be stored
     **/
    abstract
    protected WebResponse newResponse( WebRequest request, FrameSelector targetFrame ) throws IOException;


    /**
     * Writes the message body for the request.
     **/
    final protected void writeMessageBody( WebRequest request, OutputStream stream ) throws IOException {
        request.writeMessageBody( stream );
    }


    /**
     * Returns the value of all current header fields.
     **/
    protected Dictionary getHeaderFields( URL targetURL ) {
        Hashtable result = (Hashtable) _headers.clone();
        result.put( "User-Agent", getClientProperties().getUserAgent() );
        if (getClientProperties().isAcceptGzip()) result.put( "Accept-Encoding", "gzip" );
        AddHeaderIfNotNull( result, "Cookie", _cookieJar.getCookieHeaderField( targetURL ) );
        if (_authorizationString == null) _authorizationString = _fixedAuthorizationString;
        AddHeaderIfNotNull( result, "Authorization", _authorizationString );
        AddHeaderIfNotNull( result, "Proxy-Authorization", _proxyAuthorizationString );
        _authorizationString = null;
        return result;
    }


    private void AddHeaderIfNotNull( Hashtable result, final String headerName, final String headerValue ) {
        if (headerValue != null) result.put( headerName, headerValue );
    }


    /**
     * Updates this web client based on a received response. This includes updating
     * cookies and frames.  This method is required by ServletUnit, which cannot call the updateWindow method directly.
     **/
    final
    protected void updateMainWindow( FrameSelector frame, WebResponse response ) throws IOException, SAXException {
        getMainWindow().updateWindow( frame.getName(), response, new RequestContext() );
    }


//------------------------------------------------- package members ----------------------------------------------------


    void tellListeners( WebRequest request ) {
        List listeners;

        synchronized (_clientListeners) {
            listeners = new ArrayList( _clientListeners );
        }

        for (Iterator i = listeners.iterator(); i.hasNext();) {
            ((WebClientListener) i.next()).requestSent( this, request );
        }
    }


    void tellListeners( WebResponse response ) {
        List listeners;

        synchronized (_clientListeners) {
            listeners = new ArrayList( _clientListeners );
        }

        for (Iterator i = listeners.iterator(); i.hasNext();) {
            ((WebClientListener) i.next()).responseReceived( this, response );
        }
    }


    void updateClient( WebResponse response ) throws IOException {
        if (getClientProperties().isAcceptCookies()) _cookieJar.updateCookies( response.getCookieJar() );
        validateHeaders( response );
    }


    /**
     * Support Request [ 1288796 ] getCookieJar() in WebClient
     * @deprecated - use with care - was not public in the past
     * @return the cookie jar
     */
    public CookieJar getCookieJar() {
        return _cookieJar;
    }


    void updateFrameContents( WebWindow requestWindow, String requestTarget, WebResponse response, RequestContext requestContext ) throws IOException, SAXException {
        if (response.getFrame() == FrameSelector.NEW_FRAME) {
            WebWindow window = new WebWindow( this, requestWindow.getCurrentPage() );
            if (!WebRequest.NEW_WINDOW.equalsIgnoreCase( requestTarget )) window.setName( requestTarget );
            response.setFrame( window.getTopFrame() );
            window.updateFrameContents( response, requestContext );
            _openWindows.add( window );
            reportWindowOpened( window );
        } else if (response.getFrame().getWindow() != null && response.getFrame().getWindow() != requestWindow) {
            response.getFrame().getWindow().updateFrameContents( response, requestContext );
        } else {
            if (response.getFrame() == FrameSelector.TOP_FRAME) response.setFrame( requestWindow.getTopFrame() );
            requestWindow.updateFrameContents( response, requestContext );
        }
    }


    void close( WebWindow window ) {
        if (!_openWindows.contains( window )) throw new IllegalStateException( "Window is already closed" );
        _openWindows.remove( window );
        if (_openWindows.isEmpty()) _openWindows.add( new WebWindow( this ) );
        if (window.equals( _mainWindow )) _mainWindow = (WebWindow) _openWindows.get(0);
        reportWindowClosed( window );
    }


    private void reportWindowOpened( WebWindow window ) {
        List listeners;

        synchronized (_windowListeners) {
            listeners = new ArrayList( _windowListeners );
        }

        for (Iterator i = listeners.iterator(); i.hasNext();) {
            ((WebWindowListener) i.next()).windowOpened( this, window );
        }
    }


    private void reportWindowClosed( WebWindow window ) {
        List listeners;

        synchronized (_windowListeners) {
            listeners = new ArrayList( _windowListeners );
        }

        for (Iterator i = listeners.iterator(); i.hasNext();) {
            ((WebWindowListener) i.next()).windowClosed( this, window );
        }
    }

//------------------------------------------ package members ------------------------------------


    boolean getConfirmationResponse( String message ) {
        return _dialogResponder.getConfirmation( message );
    }


    String getUserResponse( String message, String defaultResponse ) {
        return _dialogResponder.getUserResponse( message, defaultResponse );
    }


    /**
     * simulate an alert by remembering the alert message on a Stack
     * @param message - the alert message to post
     */
    void postAlert( String message ) {
        _alerts.addLast( message );
    }

//------------------------------------------ private members -------------------------------------

    /** The list of alerts generated by JavaScript. **/
    private LinkedList _alerts = new LinkedList();

    /** The currently defined cookies. **/
    private CookieJar _cookieJar = new CookieJar();


    /** A map of header names to values. **/
    private HeaderDictionary _headers = new HeaderDictionary();

    private boolean _exceptionsThrownOnErrorStatus = HttpUnitOptions.getExceptionsThrownOnErrorStatus();

    private final List _clientListeners = new ArrayList();

    private final List _windowListeners = new ArrayList();

    private DialogResponder _dialogResponder = new DialogAdapter();

    private ClientProperties _clientProperties;


    /**
     * Examines the headers in the response and throws an exception if appropriate.
     * @parm response - the response to validate
     **/
    private void validateHeaders( WebResponse response ) throws HttpException {
        if (!getExceptionsThrownOnErrorStatus()) 
        	return;
        // see feature request [ 914314 ] Add HttpException.getResponse for better reporting
        // for possible improvements here
        if (response.getResponseCode() == HttpURLConnection.HTTP_INTERNAL_ERROR) {
            throw new HttpInternalErrorException( response.getURL() );
        } else if (response.getResponseCode() == HttpURLConnection.HTTP_NOT_FOUND) {
            throw new HttpNotFoundException( response.getResponseMessage(), response.getURL() );
        } else if (response.getResponseCode() >= HttpURLConnection.HTTP_BAD_REQUEST) {
            throw new HttpException( response.getResponseCode(), response.getResponseMessage(), response.getURL() );
        }
    }


    FrameSelector findFrame( String target ) {
        for (int i = 0; i < _openWindows.size(); i++) {
            WebWindow webWindow = (WebWindow) _openWindows.get( i );
            FrameSelector frame = webWindow.getFrame( target );
            if (frame != null) return frame;
        }
        return null;
    }


    /**
     * Sends a request and returns a response after dealing with any authentication challenge. If challenged and able
     * to respond, resends the request after setting the authentication header (which will apply only for the that request).
     * @param request the original request
     * @param targetFrame the frame into which the result will be stored
     * @return a response from the server
     * @throws IOException if an exception (including authorization failure) occurs
     */
    WebResponse createResponse( WebRequest request, FrameSelector targetFrame ) throws IOException {
        WebResponse response = newResponse( request, targetFrame );
        AuthenticationChallenge challenge = new AuthenticationChallenge( this, request, response.getHeaderField( "WWW-Authenticate" ) );
        if (!challenge.needToAuthenticate()) {
            return response;
        } else {
            setOnetimeAuthenticationHeader( challenge.createAuthenticationHeader() );
            WebResponse response2 = newResponse( request, targetFrame );
            if (response2.getHeaderField( "WWW-Authenticate" ) != null && getExceptionsThrownOnErrorStatus()) {
                throw AuthenticationChallenge.createException( response2.getHeaderField( "WWW-Authenticate" ) );
            }
            return response2;
        }
    }


    private void setOnetimeAuthenticationHeader( String authorizationHeader ) {
        _authorizationString = authorizationHeader;
    }

//==================================================================================================


    static public class HeaderDictionary extends Hashtable {

        public void addEntries( Dictionary source ) {
            for (Enumeration e = source.keys(); e.hasMoreElements(); ) {
                Object key = e.nextElement();
                put( key, source.get( key ) );
            }
        }


        public boolean containsKey( Object key ) {
            return super.containsKey( matchPreviousFieldName( key.toString() ) );
        }


        public Object get( Object fieldName ) {
            return super.get( matchPreviousFieldName( fieldName.toString() ) );
        }


        public Object put( Object fieldName, Object fieldValue ) {
            fieldName = matchPreviousFieldName( fieldName.toString() );
            Object oldValue = super.get( fieldName );
            if (fieldValue == null) {
                remove( fieldName );
            } else {
                super.put( fieldName, fieldValue );
            }
            return oldValue;
        }


        /**
         * If a matching field name with different case is already known, returns the older name.
         * Otherwise, returns the specified name.
         **/
        private String matchPreviousFieldName( String fieldName ) {
            for (Enumeration e = keys(); e.hasMoreElements(); ) {
                String key = (String) e.nextElement();
                if (key.equalsIgnoreCase( fieldName )) return key;
            }
            return fieldName;
        }


    }

}


//==================================================================================================


class RedirectWebRequest extends WebRequest {


    RedirectWebRequest( WebResponse response ) {
        super( response.getURL(), response.getHeaderField( "Location" ), response.getFrame(), response.getFrameName() );
        if (response.getReferer() != null) setHeaderField( "Referer", response.getReferer() );
    }


    /**
     * Returns the HTTP method defined for this request.
     **/
    public String getMethod() {
        return "GET";
    }
}







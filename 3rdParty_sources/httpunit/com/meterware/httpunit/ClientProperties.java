package com.meterware.httpunit;
/********************************************************************************************************************
 *
 *
 * Copyright (c) 2002-2003, Russell Gold
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

/**
 * A class which represents the properties of a web client.
 *
 * @author <a href="mailto:russgold@httpunit.org">Russell Gold</a>
 **/
public class ClientProperties {


    /**
     * Returns the current defaults for newly created web clients.
     */
    public static ClientProperties getDefaultProperties() {
        return _defaultProperties;
    }


    /**
     * Specifies the ID information for a client.
     */
    public void setApplicationID( String applicationName, String applicationCodeName, String applicationVersion ) {
        _applicationCodeName = applicationCodeName;
        _applicationName     = applicationName;
        _applicationVersion  = applicationVersion;
    }


    public String getApplicationCodeName() {
        return _applicationCodeName;
    }


    public void setApplicationCodeName( String applicationCodeName ) {
        _applicationCodeName = applicationCodeName;
    }


    public String getApplicationName() {
        return _applicationName;
    }


    public void setApplicationName( String applicationName ) {
        _applicationName = applicationName;
    }


    public String getApplicationVersion() {
        return _applicationVersion;
    }


    public void setApplicationVersion( String applicationVersion ) {
        _applicationVersion = applicationVersion;
    }


    /**
     * Returns the user agent identification. Unless this has been set explicitly, it will default to the
     * application code name followed by a slash and the application version.
     */
    public String getUserAgent() {
        return _userAgent != null ? _userAgent : _applicationCodeName + '/' + _applicationVersion;
    }


    public void setUserAgent( String userAgent ) {
        _userAgent = userAgent;
    }


    public String getPlatform() {
        return _platform;
    }


    public void setPlatform( String platform ) {
        _platform = platform;
    }


    /**
     * A shortcut for setting both availableScreenWidth and availableScreenHeight at one time.
     */
    public void setAvailableScreenSize( int width, int height ) {
        _availWidth = width;
        _availHeight = height;
    }


    public int getAvailableScreenWidth() {
        return _availWidth;
    }


    public void setAvailableScreenWidth( int availWidth ) {
        _availWidth = availWidth;
    }


    public int getAvailHeight() {
        return _availHeight;
    }


    public void setAvailHeight( int availHeight ) {
        _availHeight = availHeight;
    }


    /**
     * Returns true if the client should accept and transmit cookies. The default is to accept them.
     */
    public boolean isAcceptCookies() {
        return _acceptCookies;
    }


    /**
     * Specifies whether the client should accept and send cookies.
     */
    public void setAcceptCookies( boolean acceptCookies ) {
        _acceptCookies = acceptCookies;
    }


    /**
     * Returns true if the client will accept GZIP encoding of responses. The default is to accept GZIP encoding.
     **/
    public boolean isAcceptGzip() {
        return _acceptGzip;
    }


    /**
     * Specifies whether the client will accept GZIP encoded responses. The default is true.
     */
    public void setAcceptGzip( boolean acceptGzip ) {
        _acceptGzip = acceptGzip;
    }


    /**
     * Returns true if the client should automatically follow page redirect requests (status 3xx).
     * By default, this is true.
     **/
    public boolean isAutoRedirect() {
        return _autoRedirect;
    }


    /**
     * Determines whether the client should automatically follow page redirect requests (status 3xx).
     * By default, this is true in order to simulate normal browser operation.
     **/
    public void setAutoRedirect( boolean autoRedirect ) {
        _autoRedirect = autoRedirect;
    }


    /**
     * Returns true if the client should automatically follow page refresh requests.
     * By default, this is false, so that programs can verify the redirect page presented
     * to users before the browser switches to the new page.
     **/
    public boolean isAutoRefresh() {
        return _autoRefresh;
    }


    /**
     * Specifies whether the client should automatically follow page refresh requests.
     * By default, this is false, so that programs can verify the redirect page presented
     * to users before the browser switches to the new page. Setting this to true can
     * cause an infinite loop on pages that refresh themselves.
     **/
    public void setAutoRefresh( boolean autoRefresh ) {
        _autoRefresh = autoRefresh;
    }


    public boolean isIframeSupported() {
        return _iframeSupported;
    }


    public void setIframeSupported( boolean iframeSupported ) {
        _iframeSupported = iframeSupported;
    }


    /**
     * Returns the context type (if any) to use instead of the one specified by the server. Defaults to null.
     * @return the overriding context type, or null if none is specified.
     */
    public String getOverrideContextType() {
        return _overrideContextType;
    }


    /**
     * All responses to this client will use the specified content type rather than the one specified by the server.
     * Setting this to "text/html" will force all reponses to be interpreted as HTML.
     * @param overrideContextType the new override to apply to context types.
     */
    public void setOverrideContextType( String overrideContextType ) {
        _overrideContextType = overrideContextType;
    }


    /**
     * Specifies a listener for DNS requests from the client.
     * @param dnsListener the new listener.
     */
    public void setDnsListener( DNSListener dnsListener ) {
        _dnsListener = dnsListener;
    }


    /**
     * Returns the listener for DNS requests to be used by the client.
     * @return the currently specified DNS listener, or null if none is specified.
     */
    DNSListener getDnsListener() {
        return _dnsListener;
    }
    
		/**
		 * @return the whether Referer information should be stripped from the
		 * header
		 */
		public boolean isSendReferer() {
			return _sendReferer;
		}


		/**
		 * set whether Referer information should be stripped
		 * @param referer the _sendReferer to set
		 */
		public void setSendReferer(boolean referer) {
			_sendReferer = referer;
		}


    ClientProperties cloneProperties() {
        return new ClientProperties( this );
    }


    private String _applicationCodeName = "httpunit";
    private String _applicationName     = "HttpUnit";
    private String _applicationVersion  = "1.5";
    private String _userAgent;
    private String _platform            = "Java";
    private String _overrideContextType = null;
    private int    _availWidth          = 800;
    private int    _availHeight         = 600;

    private boolean _iframeSupported = true;
    private boolean _acceptCookies = true;
    private boolean _acceptGzip    = true;
    private boolean _autoRedirect  = true;
    private boolean _autoRefresh   = false;

    private DNSListener _dnsListener;
    private boolean _sendReferer;

    private static ClientProperties _defaultProperties = new ClientProperties();


    /**
     * default Constructor
     *
     */
    private ClientProperties() {
    	_sendReferer=true;
    }


    /**
     * copy constructor
     * @param source - the ClientProperties to copy from
     */
    private ClientProperties( ClientProperties source ) {
        _applicationCodeName = source._applicationCodeName;
        _applicationName     = source._applicationName;
        _applicationVersion  = source._applicationVersion;
        _userAgent           = source._userAgent;
        _platform            = source._platform;
        _overrideContextType = source._overrideContextType;
        _iframeSupported     = source._iframeSupported;
        _acceptCookies       = source._acceptCookies;
        _acceptGzip          = source._acceptGzip;
        _autoRedirect        = source._autoRedirect;
        _autoRefresh         = source._autoRefresh;
        _sendReferer         = source._sendReferer;
    }


}

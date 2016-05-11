package com.meterware.httpunit;
/********************************************************************************************************************

*
* Copyright (c) 2000-2004, Russell Gold
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

import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import java.util.Dictionary;
import java.util.Enumeration;
import java.util.Properties;


/**
 * The context for a series of HTTP requests. This class manages cookies used to maintain
 * session context, computes relative URLs, and generally emulates the browser behavior
 * needed to build an automated test of a web site.
 *
 * @author Russell Gold
 **/
public class WebConversation extends WebClient {

    private String _proxyHost;
    private int _proxyPort;
    private int _connectTimeout = -1;
    private int _readTimeout = -1;    


    /**
     * Creates a new web conversation.
     **/
    public WebConversation() {
    }


//---------------------------------- protected members --------------------------------


    /**
     * Creates a web response object which represents the response to the specified web request.
     **/
    protected WebResponse newResponse( WebRequest request, FrameSelector targetFrame ) throws MalformedURLException, IOException {
        Properties savedProperties = (Properties) System.getProperties().clone();
        try {
            if (_proxyHost != null) {
                System.setProperty( "proxyHost", _proxyHost );
                System.setProperty( "proxyPort", Integer.toString( _proxyPort ) );
            }
            URLConnection connection = openConnection( getRequestURL( request ) );
            // [ 1518901 ] enable http connect and read timeouts (needs JDK 1.5)
            // XXX enable for 1.7 release in 2008
            // comment out if you need this and have JDK 1.5
            // if (_connectTimeout>=0) connection.setConnectTimeout( _connectTimeout );
            // if (_readTimeout>=0)    connection.setReadTimeout( _readTimeout );            
            if (HttpUnitOptions.isLoggingHttpHeaders()) {
                String urlString = request.getURLString();
                System.out.println( "\nConnecting to " + request.getURL().getHost() );
                System.out.println( "Sending:: " + request.getMethod() + " " + urlString );
            }
            sendHeaders( connection, getHeaderFields( request.getURL() ) );
            sendHeaders( connection, request.getHeaderDictionary() );
            request.completeRequest( connection );
            return new HttpWebResponse( this, targetFrame, request, connection, getExceptionsThrownOnErrorStatus() );
        } finally {
            System.setProperties( savedProperties );
        }
    }


    public void clearProxyServer() {
        _proxyHost = null;
    }


    /**
     * set the proxy server to the given proxyHost with the given proxy Port
     * @param proxyHost - the hostname of the proxy e.g. proxy.somedomain.org
     * @param proxyPort - the number of the port to use e.g. 8080
     */
    public void setProxyServer( String proxyHost, int proxyPort ) {
        _proxyHost = proxyHost;
        _proxyPort = proxyPort;
    }


    /**
		 * @return the _connectTimeout -1 means it is not set (the default)
		 */
		public int get_connectTimeout() {
			return _connectTimeout;
		}


		/**
		 * set the connectionTimout -1 means it is not set (the default)
		 * @param timeout the _connectTimeout to set
		 */
		public void set_connectTimeout(int timeout) {
			_connectTimeout = timeout;
		}


		/**
		 * @return the _readTimeout -1 means it is not set (the default)
		 */
		public int get_readTimeout() {
			return _readTimeout;
		}


		/**
		 * @param timeout the _readTimeout to set -1 means it is not set (the default)
		 */
		public void set_readTimeout(int timeout) {
			_readTimeout = timeout;
		}


		/**
		 * get the Uniform Resource Locator for this request
		 * @param request
		 * @return the URL
		 * @throws MalformedURLException
		 */
		private URL getRequestURL( WebRequest request ) throws MalformedURLException {
        DNSListener dnsListener = getClientProperties().getDnsListener();
        if (dnsListener == null) return request.getURL();

        String hostName = request.getURL().getHost();
        String portPortion = request.getURL().getPort() == -1 ? "" : (":" + request.getURL().getPort());
        setHeaderField( "Host", hostName + portPortion );
        String actualHost = dnsListener.getIpAddress( hostName );
        if (HttpUnitOptions.isLoggingHttpHeaders()) System.out.println( "Rerouting request to :: " + actualHost );
        return new URL( request.getURL().getProtocol(), actualHost, request.getURL().getPort(), request.getURL().getFile() );
    }


//---------------------------------- private members --------------------------------


    /**
     * open a connection for the given uniform resource locator
     * @param url - the url to use
     */
    private URLConnection openConnection( URL url ) throws MalformedURLException, IOException {
        URLConnection connection = url.openConnection();
        if (connection instanceof HttpURLConnection) ((HttpURLConnection) connection).setInstanceFollowRedirects( false );
        connection.setUseCaches( false );
        return connection;
    }


    /**
     * send the headers for the given connection based on the given Dictionary of headers
     * @param connection
     * @param headers
     */
    private void sendHeaders( URLConnection connection, Dictionary headers ) {
    		boolean sendReferer = getClientProperties().isSendReferer();
        for (Enumeration e = headers.keys(); e.hasMoreElements();) {
            String key = (String) e.nextElement();
            if ( sendReferer || !"referer".equalsIgnoreCase( key ) ) {
	            connection.setRequestProperty( key, (String) headers.get( key ) );
	            if (HttpUnitOptions.isLoggingHttpHeaders()) {
	                if (key.equalsIgnoreCase( "authorization" ) || key.equalsIgnoreCase( "proxy-authorization") ) {
	                    System.out.println( "Sending:: " + key + ": " + headers.get( key ) );
	                } else {
	                    System.out.println( "Sending:: " + key + ": " + connection.getRequestProperty( key ) );
	                }
	            }
        		} else if (HttpUnitOptions.isLoggingHttpHeaders()) {
        				System.out.println( "Blocked sending referer:: "+ connection.getRequestProperty( key ) );
        		}		
        } // for
    }
}

package com.meterware.pseudoserver;
/********************************************************************************************************************

*
* Copyright (c) 2001-2004, Russell Gold
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
import com.meterware.httpunit.HttpUnitUtils;

import java.io.InputStream;
import java.io.IOException;

import java.util.StringTokenizer;
import java.util.Hashtable;


/**
 * Represents a single HTTP request, extracted from the input stream.
 *
 * @since 1.6
 */
public class HttpRequest extends ReceivedHttpMessage {

    private String         _protocol;
    private String         _command;
    private String         _uri;
    private Hashtable      _parameters;


    HttpRequest( InputStream inputStream ) throws IOException {
        super( inputStream );
    }


    void interpretMessageHeader( String messageHeader ) {
        StringTokenizer st = new StringTokenizer( messageHeader );
        _command  = st.nextToken();
        _uri      = st.nextToken();
        _protocol = st.nextToken();
    }


    void appendMessageHeader( StringBuffer sb ) {
        sb.append( _command ).append( ' ' ).append( _uri ).append( ' ' ).append( _protocol );
    }


    /**
     * Returns the command associated with this request.
     */
    public String getCommand() {
        return _command;
    }


    /**
     * Returns the URI specified in the message header for this request.
     */
    public String getURI() {
        return _uri;
    }


    /**
     * Returns the protocol string specified in the message header for this request.
     */
    public String getProtocol() {
        return _protocol;
    }


    /**
     * Returns the parameter with the specified name. If no such parameter exists, will
     * return null.
     **/
    public String[] getParameter( String name ) {
        if (_parameters == null) {
            if (_command.equalsIgnoreCase( "GET" ) || _command.equalsIgnoreCase( "HEAD" )) {
                _parameters = readParameters( getParameterString( _uri ) );
            } else {
                _parameters = readParameters( new String( getBody() ) );
            }
        }
        return (String[]) _parameters.get( name );
    }


    private String getParameterString( String uri ) {
        return uri.indexOf( '?' ) < 0 ? "" : uri.substring( uri.indexOf( '?' )+1 );
    }


    boolean wantsKeepAlive() {
        if ("Keep-alive".equalsIgnoreCase( getConnectionHeader() )) {
            return true;
        } else if (_protocol.equals( "HTTP/1.1" )) {
            return !"Close".equalsIgnoreCase( getConnectionHeader() );
        } else {
            return false;
        }
    }


    private Hashtable readParameters( String content ) {
        Hashtable parameters = new Hashtable();
	    if (content == null || content.trim().length() == 0) return parameters;

        StringTokenizer st = new StringTokenizer( content, "&=" );
        while (st.hasMoreTokens()) {
            String name = st.nextToken();
            if (st.hasMoreTokens()) {
                addParameter( parameters, HttpUnitUtils.decode( name ), HttpUnitUtils.decode( st.nextToken() ) );
            }
        }
        return parameters;
    }


    private void addParameter( Hashtable parameters, String name, String value ) {
        String[] oldValues = (String[]) parameters.get( name );
        if (oldValues == null) {
            parameters.put( name, new String[] { value } );
        } else {
            String[] values = new String[ oldValues.length+1 ];
            System.arraycopy( oldValues, 0, values, 0, oldValues.length );
            values[ oldValues.length ] = value;
            parameters.put( name, values );
        }
    }


    private String getConnectionHeader() {
        return getHeader( "Connection" );
    }


}


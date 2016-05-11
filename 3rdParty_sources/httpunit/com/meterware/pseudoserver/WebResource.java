package com.meterware.pseudoserver;
/********************************************************************************************************************

*
* Copyright (c) 2000-2005, Russell Gold
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

import java.io.OutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.util.Vector;


/**
 * A resource to be returned from the simulated server.
 **/
public class WebResource {


    final static String DEFAULT_CONTENT_TYPE = "text/html";

    final static String DEFAULT_CHARACTER_SET = "iso-8859-1";
    private boolean _closesConnection;


    public WebResource( String contents ) {
        this( contents, DEFAULT_CONTENT_TYPE );
    }


    public WebResource( String contents, String contentType ) {
        this( contents, contentType, HttpURLConnection.HTTP_OK );
    }


    public WebResource( byte[] contents, String contentType ) {
        this( contents, contentType, HttpURLConnection.HTTP_OK );
    }


    public void addHeader( String header ) {
        _headers.addElement( header );
        if (header.toLowerCase().startsWith( "content-type" )) _hasExplicitContentTypeHeader = true;
        if (header.toLowerCase().startsWith( "content-length" )) _hasExplicitContentLengthHeader = true;
        if (header.trim().toLowerCase().startsWith( "connection" ) &&
            header.trim().toLowerCase().endsWith( "close") ) _closesConnection = true;
        if (header.trim().toLowerCase().startsWith( "transfer-encoding" ) &&
            header.trim().toLowerCase().endsWith( "chunked") ) _isChunked = true;
    }


    public void setCharacterSet( String characterSet ) {
        _characterSet = characterSet;
    }


    public void setSendCharacterSet( boolean enabled ) {
        _sendCharacterSet = enabled;
    }


    public void suppressAutomaticLengthHeader() {
        _hasExplicitContentLengthHeader = true;
    }


    public void suppressAutomaticContentTypeHeader() {
        _hasExplicitContentTypeHeader = true;
    }
    

    public WebResource( String contents, int responseCode ) {
        this( contents, DEFAULT_CONTENT_TYPE, responseCode );
    }


    public WebResource( String contents, String contentType, int responseCode ) {
        _string      = contents;
        _contentType = contentType;
        _responseCode = responseCode;
    }


    public WebResource( byte[] contents, String contentType, int responseCode ) {
        _contents    = contents;
        _contentType = contentType;
        _responseCode = responseCode;
    }


    public WebResource( InputStream stream, String contentType, int responseCode ) {
        _stream = stream;
        _contentType = contentType;
        _responseCode = responseCode;
        addHeader( "Connection: close" );
    }


    String[] getHeaders() throws UnsupportedEncodingException {
        final Vector effectiveHeaders = (Vector) _headers.clone();
        if (!_hasExplicitContentTypeHeader) effectiveHeaders.add( getContentTypeHeader() );
        if (_stream == null && !_hasExplicitContentLengthHeader && !isChunked()) effectiveHeaders.add( getContentLengthHeader() );
        String[] headers = new String[ effectiveHeaders.size() ];
        effectiveHeaders.copyInto( headers );
        return headers;
    }


    private boolean isChunked() {
        return _isChunked;
    }


    boolean closesConnection() {
        return _closesConnection;
    }


    void writeTo( OutputStream outputStream ) throws IOException {
        if (_stream == null) {
            outputStream.write( getContentsAsBytes() );
        } else if (_stream != null) {
            byte[] buffer = new byte[8 * 1024];
            int count = 0;
            do {
                outputStream.write( buffer, 0, count );
                count = _stream.read( buffer, 0, buffer.length );
            } while (count != -1);
        }
    }


    static String toString( byte[] contentsAsBytes ) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < contentsAsBytes.length; i++) {
            byte contentsAsByte = contentsAsBytes[i];
            sb.append( Integer.toHexString( contentsAsByte ) ).append( ' ' );
        }
        return sb.toString();
    }


    private byte[] getContentsAsBytes() throws UnsupportedEncodingException {
        if (_contents != null) {
            return _contents;
        } else if (_string != null) {
            return _string.getBytes( getCharacterSet() );
        } else {
            throw new IllegalStateException( "Cannot get bytes from stream" );
        }
    }


    private String getContentTypeHeader() {
        return "Content-Type: " + _contentType + getCharacterSetParameter();
    }


    private String getContentLengthHeader() throws UnsupportedEncodingException {
        return "Content-Length: " + getContentsAsBytes().length;
    }


    String getCharacterSet() {
        return HttpUnitUtils.stripQuotes( _characterSet );
    }


    String getCharacterSetParameter() {
        if (!_sendCharacterSet) {
            return "";
        } else {
            return "; charset=" + _characterSet;
        }
    }


    int getResponseCode() {
        return _responseCode;
    }


    public String toString() {
        return "WebResource [code=" + _responseCode + "; type = " + _contentType
	     + "; charset = " + _characterSet + "]\n" + getContentsAsString();
    }

    private String getContentsAsString() {
        if (_string != null) {
            return _string;
        } else {
            return "<< hex bytes >>";
        }
    }


    private byte[]      _contents;
    private String      _string;
    private InputStream _stream;

    private int     _responseCode;
    private boolean _sendCharacterSet;
    private String  _contentType = DEFAULT_CONTENT_TYPE;
    private String  _characterSet = DEFAULT_CHARACTER_SET;
    private boolean _hasExplicitContentTypeHeader;
    private boolean _hasExplicitContentLengthHeader;
    private Vector  _headers = new Vector();
    private boolean _isChunked;
}




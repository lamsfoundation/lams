package com.meterware.httpunit.protocol;
/********************************************************************************************************************
 *
 *
 * Copyright (c) 2001-2002, 2007, Russell Gold
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
import java.net.URLEncoder;

/**
 * @author <a href="mailto:russgold@httpunit.org">Russell Gold</a>
*/
public class URLEncodedString implements ParameterProcessor {

    public static final int DEFAULT_BUFFER_SIZE = 128;
    
    private StringBuffer _buffer = new StringBuffer( DEFAULT_BUFFER_SIZE );

    private boolean _haveParameters = false;


    public String getString() {
        return _buffer.toString();
    }


    public void addParameter( String name, String value, String characterSet ) {
        if (_haveParameters) _buffer.append( '&' );
        _buffer.append( encode( name, characterSet ) );
        if (value != null) _buffer.append( '=' ).append( encode( value, characterSet ) );
        _haveParameters = true;
    }


    public void addFile( String parameterName, UploadFileSpec fileSpec ) {
        throw new RuntimeException( "May not URL-encode a file upload request" );
    }


    /**
     * Returns a URL-encoded version of the string.
     **/
    private String encode( String source, String characterSet ) {
        try {
            return URLEncoder.encode( source, characterSet );
        } catch (java.io.UnsupportedEncodingException e) {
            return "???";    // XXX should pass the exception through as IOException ultimately
        }
    }

}

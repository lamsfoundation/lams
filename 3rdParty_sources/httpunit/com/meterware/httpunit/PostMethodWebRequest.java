package com.meterware.httpunit;
/********************************************************************************************************************

*
* Copyright (c) 2000-2002, 2004, 2007 Russell Gold
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
import com.meterware.httpunit.protocol.MessageBody;
import com.meterware.httpunit.protocol.URLEncodedString;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;


/**
 * An HTTP request using the POST method.
 **/
public class PostMethodWebRequest extends MessageBodyWebRequest {

    /**
     * Constructs a web request using a specific absolute url string.
     **/
    public PostMethodWebRequest( String urlString ) {
        this( urlString, false );
    }


    /**
     * Constructs a web request using a specific absolute url string, with optional mime encoding.
     **/
    public PostMethodWebRequest( String urlString, boolean mimeEncoded ) {
        super( urlString, mimeEncoded );
    }


    /**
     * Constructs a web request with a specific target.
     **/
    public PostMethodWebRequest( URL urlBase, String urlString, String target ) {
        this( urlBase, urlString, target, false );
    }


    /**
     * Constructs a web request with a specific target, with optional mime encoding.
     **/
    public PostMethodWebRequest( URL urlBase, String urlString, String target, boolean mimeEncoded ) {
        super( urlBase, urlString, target, mimeEncoded );
    }


    /**
     * Constructs a web request using a specific absolute url string and input stream.
     * @param urlString the URL to which the request should be issued
     * @param source    an input stream which will provide the body of this request
     * @param contentType the MIME content type of the body, including any character set
     **/
    public PostMethodWebRequest( String urlString, InputStream source, String contentType ) {
        super( urlString, false );
        _body = new InputStreamMessageBody( source, contentType );
    }


    /**
     * Returns the HTTP method defined for this request.
     **/
    public String getMethod() {
        return "POST";
    }


    /**
     * Returns the query string defined for this request.
     **/
    public String getQueryString() {
        try {
            URLEncodedString encoder = new URLEncodedString();
            getParameterHolder().recordPredefinedParameters( encoder );
            return encoder.getString();
        } catch (IOException e) {
            throw new RuntimeException( "Programming error: " + e );   // should never happen
        }
    }


    /**
     * Returns true if selectFile may be called with this parameter.
     */
    protected boolean maySelectFile( String parameterName ) {
        return isMimeEncoded() && isFileParameter( parameterName );
    }

//----------------------------- MessageBodyWebRequest methods ---------------------------


    protected MessageBody getMessageBody() {
        if (_body == null) {
            _body = MessageBody.createPostMethodMessageBody( isMimeEncoded(), getCharacterSet() );
        }
        return _body;
    }

//----------------------------------- package members -----------------------------------


    /**
     * Constructs a web request for a form submitted by clicking a button.
     **/
    PostMethodWebRequest( WebForm sourceForm, SubmitButton button, int x, int y ) {
        this( sourceForm, sourceForm, button, x, y );
    }


    PostMethodWebRequest( WebForm sourceForm, ParameterHolder parameterHolder, SubmitButton button, int x, int y ) {
        super( sourceForm, parameterHolder, button, x, y );
    }


    /**
     * Constructs a web request for a form submitted via a script.
     **/
    PostMethodWebRequest( WebForm sourceForm ) {
        super( sourceForm );
    }

}


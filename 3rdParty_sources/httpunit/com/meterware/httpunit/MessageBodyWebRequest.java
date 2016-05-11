package com.meterware.httpunit;
/********************************************************************************************************************

*
* Copyright (c) 2001-2004,2007 Russell Gold
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

import com.meterware.httpunit.protocol.ParameterCollection;
import com.meterware.httpunit.protocol.MessageBody;

import java.io.InputStream;
import java.io.IOException;
import java.io.OutputStream;

import java.net.URL;
import java.net.URLConnection;


/**
 * A web request which contains a non-empty message body. Note that such requests
 * <em>must</em> use the <code>http</code> or <code>https</code> protocols.
 **/
abstract
public class MessageBodyWebRequest extends WebRequest {

    protected MessageBody _body;
    private boolean _mimeEncoded;


    /**
     * Constructs a web request using a specific absolute url string.
     **/
    protected MessageBodyWebRequest( String urlString, boolean mimeEncoded ) {
        super( urlString );
        _mimeEncoded = mimeEncoded;
    }


    /**
     * Constructs a web request using a specific absolute url string.
     **/
    protected MessageBodyWebRequest( String urlString, MessageBody messageBody ) {
        super( urlString );
        _body = messageBody;
    }


    /**
     * Constructs a web request with a specific target.
     **/
    protected MessageBodyWebRequest( URL urlBase, String urlString, String target, boolean mimeEncoded ) {
        super( urlBase, urlString, target );
        _mimeEncoded = mimeEncoded;
    }


    /**
     * Constructs a web request for a form submitted via a button.
     *
     * @since 1.6
     **/
    protected MessageBodyWebRequest( WebForm sourceForm, ParameterHolder parameterHolder, SubmitButton button, int x, int y ) {
        super( sourceForm, parameterHolder, button, x, y );
        _mimeEncoded = parameterHolder.isSubmitAsMime();
        setHeaderField( REFERER_HEADER_NAME, sourceForm.getBaseURL().toExternalForm() );
    }


    /**
     * Constructs a web request for a form submitted via script.
     **/
    protected MessageBodyWebRequest( WebForm sourceForm ) {
        super( sourceForm, WebRequest.newParameterHolder( sourceForm ) );
        _mimeEncoded = sourceForm.isSubmitAsMime();
        setHeaderField( REFERER_HEADER_NAME, sourceForm.getBaseURL().toExternalForm() );
    }


    /**
     * Subclasses may override this method to provide a message body for the
     * request.
     **/
    protected MessageBody getMessageBody() {
        return _body;
    }


//---------------------------------- WebRequest methods --------------------------------


    protected void writeMessageBody( OutputStream stream ) throws IOException {
        getMessageBody().writeTo( stream, getParameterHolder() );
    }


    /**
     * Performs any additional processing necessary to complete the request.
     **/
    protected void completeRequest( URLConnection connection ) throws IOException {
        super.completeRequest( connection );
        connection.setDoInput( true );
        connection.setDoOutput( true );

        OutputStream stream = connection.getOutputStream();
        writeMessageBody( stream );
        stream.flush();
        stream.close();
    }


    protected String getContentType() {
        return getMessageBody().getContentType();
    }


    public boolean isMimeEncoded() {
        return _mimeEncoded;
    }

//============================= class InputStreamMessageBody ======================================

    /**
     * A method request message body read directly from an input stream.
     **/
    public static class InputStreamMessageBody extends MessageBody {


        public InputStreamMessageBody( InputStream source, String contentType ) {
            super( null );
            _source = source;
            _contentType = contentType;
        }


        /**
         * Returns the content type of this message body.
         **/
        public String getContentType() {
            return _contentType;
        }


        /**
         * Transmits the body of this request as a sequence of bytes.
         **/
        public void writeTo( OutputStream outputStream, ParameterCollection parameters ) throws IOException {
            byte[] buffer = new byte[8 * 1024];
            int count = 0;
            do {
                outputStream.write( buffer, 0, count );
                count = _source.read( buffer, 0, buffer.length );
            } while (count != -1);

            _source.close();
        }


        private InputStream _source;
        private String      _contentType;
    }
}

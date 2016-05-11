package com.meterware.httpunit.protocol;
/********************************************************************************************************************

*
* Copyright (c) 2000-2001, 2003, 2007 Russell Gold
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

/**
 * An abstract class representing the body of a web request.
 **/
abstract
public class MessageBody {

    private String _characterSet;


    /**
     * Creates a message body for a POST request, selecting an appropriate encoding.
     * @param mimeEncoded if true, indicates that the request is using mime encoding.
     * @param characterSet the character set of the request.
     * @return an appropriate message body.
     */
    public static MessageBody createPostMethodMessageBody( boolean mimeEncoded, String characterSet ) {
        return mimeEncoded ? (MessageBody) new MimeEncodedMessageBody( characterSet )
                           : (MessageBody) new URLEncodedMessageBody( characterSet );
    }


    public MessageBody( String characterSet ) {
        _characterSet = characterSet;
    }

    /**
     * Returns the character set associated with this message body.
     */
    public String getCharacterSet() {
        return _characterSet;
    }


    /**
     * Returns the content type of this message body. For text messages, this
     * should include the character set.
     **/
    abstract
    public String getContentType();


    /**
     * Transmits the body of this request as a sequence of bytes.
     **/
    abstract
    public void writeTo( OutputStream outputStream, ParameterCollection parameters ) throws IOException;
}

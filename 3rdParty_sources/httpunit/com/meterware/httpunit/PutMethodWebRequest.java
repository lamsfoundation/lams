package com.meterware.httpunit;
/********************************************************************************************************************

*
* Copyright (c) 2001,2007 Russell Gold
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

import java.io.InputStream;

/**
 * A web request using the PUT protocol.
 *
 * The objectives of this class are to suport an HTTP PUT petition
 * so we can test this HTTP requests.
 *
 * <B>Documentation</B> See the HTTP 1.1 [<a href="http://www.w3.org/Protocols/HTTP/">spec</a>]
 *
 * @author Tom Watkins
 * @author Deepa Dihr
 * @author Marcos Tarruella
 * @author Russell Gold
 *
 **/
public class PutMethodWebRequest extends MessageBodyWebRequest {


    /**
     * Constructs a web request using a specific absolute url string and input stream.
     * @param url         the URL to which the request should be issued
     * @param source      an input stream which will provide the body of this request
     * @param contentType the MIME content type of the body, including any character set
     **/
    public PutMethodWebRequest( String url, InputStream source, String contentType ) {
        super( url, new InputStreamMessageBody( source, contentType ) );
    }


    /**
     * Returns 'PUT' to indicate the method.
     **/
    public String getMethod() {
        return "PUT";
    }
}

package com.meterware.httpunit;
/********************************************************************************************************************

*
* Copyright (c) 2000-2001,2008 Russell Gold
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
import java.net.HttpURLConnection;
import java.net.URL;


/**
 * This exception is thrown when the desired URL is not found.
 * @author Seth Ladd
 * @author Russell Gold
 **/
public class HttpNotFoundException extends HttpException {

	  /**
	   * construct a HttpNotFoundException (404 Error)
	   * @param responseMessage
	   * @param baseURL
	   */
    public HttpNotFoundException( String responseMessage, URL baseURL ) {
        super( HttpURLConnection.HTTP_NOT_FOUND, responseMessage, baseURL );
    }


    /**
     * construct a HttpNotFoundException (404 Error)
     * @param url
     * @param t
     */
    public HttpNotFoundException( URL url, Throwable t ) {
        this( t.toString(), url );
    }


}

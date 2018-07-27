package com.meterware.httpunit;
/********************************************************************************************************************

*
* Copyright (c) 2000-2001, 2008 Russell Gold
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
 * This exception is thrown when an internal error is found on the server.
 * @author Seth Ladd
 * @author Russell Gold
 **/
public class HttpInternalErrorException extends HttpException {


	  /**
	   * construct an internal http error form an url
	   * @param url
	   */
    public HttpInternalErrorException( URL url ) {
        super( HttpURLConnection.HTTP_INTERNAL_ERROR, "Internal Error", url );
    }


    /**
     * construct an internal HTTP Error from a URL and a throwable
     * @param url
     * @param t
     */
    public HttpInternalErrorException( URL url, Throwable t ) {
        super( HttpURLConnection.HTTP_INTERNAL_ERROR, t.toString(), url,t  );
    }

}

package com.meterware.servletunit;
/********************************************************************************************************************
 *
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
import com.meterware.httpunit.WebResponse;
import com.meterware.httpunit.FrameSelector;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URL;
import java.net.HttpURLConnection;

import javax.servlet.http.HttpServletResponse;


/**
 * A response from to a request from the simulated servlet environment.
 **/
class ServletUnitWebResponse extends WebResponse {

    /**
     * Constructs a response object from a servlet response.
     * @param frame the target frame on which the response will be displayed
     * @param url the url from which the response was received
     * @param response the response populated by the servlet
     **/
    ServletUnitWebResponse( ServletUnitClient client, FrameSelector frame, URL url, HttpServletResponse response, boolean throwExceptionOnError ) throws IOException {
        super( client, frame, url );
        _response = (ServletUnitHttpResponse) response;
        /** make sure that any IO exception for HTML received page happens here, not later. **/
        if (getResponseCode() < HttpURLConnection.HTTP_BAD_REQUEST || !throwExceptionOnError) {
            defineRawInputStream( new ByteArrayInputStream( _response.getContents() ) );
            if (getContentType().startsWith( "text" )) loadResponseText();
        }
    }


    /**
     * Constructs a response object from a servlet response.
     * @param frame the target frame on which the response will be displayed
     * @param url the url from which the response was received
     * @param response the response populated by the servlet
     **/
    ServletUnitWebResponse( ServletUnitClient client, FrameSelector frame, URL url, HttpServletResponse response ) throws IOException {
        this( client, frame, url, response, true );
    }


    /**
     * Returns the response code associated with this response.
     **/
    public int getResponseCode() {
        return _response.getStatus();
    }


    /**
     * Returns the response message associated with this response.
     **/
    public String getResponseMessage() {
        return _response.getMessage();
    }


    public String[] getHeaderFieldNames() {
        return _response.getHeaderFieldNames();
    }


    /**
     * Returns the value for the specified header field. If no such field is defined, will return null.
     **/
    public String getHeaderField( String fieldName ) {
        return _response.getHeaderField( fieldName );
    }


    public String[] getHeaderFields( String fieldName ) {
        return _response.getHeaderFields( fieldName );
    }


    public String toString() {
        return "[ _response = " + _response + "]";
    }


//-------------------------------------------- private members ------------------------------------------------


    private ServletUnitHttpResponse _response;

}


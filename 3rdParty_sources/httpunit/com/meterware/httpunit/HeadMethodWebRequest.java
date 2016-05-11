package com.meterware.httpunit;

import java.net.URL;

/********************************************************************************************************************
 *
 *
 * Copyright (c) 2002-2008, Russell Gold
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

/**
 * A web request using the HEAD method. This request is used to obtain header information for a resource
 * without necessarily waiting for the data to be computed or transmitted.
 *
 * @author <a href="mailto:russgold@httpunit.org">Russell Gold</a>
 **/ 
public class HeadMethodWebRequest extends HeaderOnlyWebRequest {

    /**
     * Creates a new head request from a complete URL string.
     * @param urlString the URL desired, including the protocol.
     */
    public HeadMethodWebRequest( String urlString ) {
        super( urlString );
        this.setMethod("HEAD");
    }


    /**
     * Creates a new head request using a relative URL and base.
     * @param urlBase the base URL.
     * @param urlString the relative URL
     */
    public HeadMethodWebRequest( URL urlBase, String urlString ) {
        super( urlBase, urlString );
        this.setMethod("HEAD");
    }

}

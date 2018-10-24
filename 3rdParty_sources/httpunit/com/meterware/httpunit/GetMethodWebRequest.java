package com.meterware.httpunit;
/********************************************************************************************************************

*
* Copyright (c) 2000-2002, 2004, Russell Gold
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
import org.w3c.dom.Element;

import java.net.URL;

/**
 * An HTTP request using the GET method.
 **/
public class GetMethodWebRequest extends HeaderOnlyWebRequest {


    /**
     * Constructs a web request using a specific absolute url string.
     **/
    public GetMethodWebRequest( String urlString ) {
        super( urlString );
    }


    /**
     * Constructs a web request using a base URL and a relative url string.
     **/
    public GetMethodWebRequest( URL urlBase, String urlString ) {
        super( urlBase, urlString );
    }


    /**
     * Constructs a web request with a specific target.
     **/
    public GetMethodWebRequest( URL urlBase, String urlString, String target ) {
        super( urlBase, urlString, target );
    }


    /**
     * Returns the HTTP method defined for this request.
     **/
    public String getMethod() {
        return "GET";
    }


//--------------------------------------- package members ---------------------------------------------


    /**
     * Constructs a web request for a form submitted from JavaScript.
     **/
    GetMethodWebRequest( WebForm sourceForm ) {
        super( sourceForm );
    }


    /**
     * Constructs a web request for a link or image.
     **/
    GetMethodWebRequest( FixedURLWebRequestSource source ) {
        super( source );
    }


    /**
     * Constructs a web request with a specific target.
     **/
    GetMethodWebRequest( WebResponse referer, Element sourceElement, URL urlBase, String urlString, String target ) {
        super( referer, sourceElement, urlBase, urlString, target );
    }


    /**
     * Constructs an initial web request for a frame.
     **/
    GetMethodWebRequest( URL urlBase, String urlString, FrameSelector frame ) {
        super( urlBase, urlString, frame );
    }


    /**
     * Constructs a web request for a javascript open call.
     **/
    GetMethodWebRequest( URL urlBase, String urlString, FrameSelector frame, String target ) {
        super( urlBase, urlString, frame, target );
    }


    /**
     * Constructs a web request for a form.
     **/
    GetMethodWebRequest( WebForm sourceForm, ParameterHolder parameterHolder, SubmitButton button, int x, int y ) {
        super( sourceForm, parameterHolder, button, x, y );
    }


}





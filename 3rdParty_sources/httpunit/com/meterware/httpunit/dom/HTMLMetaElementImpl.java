package com.meterware.httpunit.dom;
/********************************************************************************************************************
 *
 *
 * Copyright (c) 2004, Russell Gold
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
import org.w3c.dom.html.HTMLMetaElement;

/**
 *
 * @author <a href="mailto:russgold@httpunit.org">Russell Gold</a>
 **/
public class HTMLMetaElementImpl extends HTMLElementImpl implements HTMLMetaElement {

    ElementImpl create() {
        return new HTMLMetaElementImpl();
    }


    public String getContent() {
        return getAttributeWithNoDefault( "content" );
    }


    public String getHttpEquiv() {
        return getAttributeWithNoDefault( "http-equiv" );
    }


    public String getName() {
        return getAttributeWithNoDefault( "name" );
    }


    public String getScheme() {
        return getAttributeWithNoDefault( "scheme" );
    }


    public void setContent( String content ) {
        setAttribute( "content", content );
    }


    public void setHttpEquiv( String httpEquiv ) {
        setAttribute( "http-equiv", httpEquiv );
    }


    public void setName( String name ) {
        setAttribute( "name", name );
    }


    public void setScheme( String scheme ) {
        setAttribute( "scheme", scheme );
    }
}

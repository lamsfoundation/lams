package com.meterware.httpunit.dom;
/********************************************************************************************************************
 *
 *
 * Copyright (c) 2006, Russell Gold
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
import org.w3c.dom.html.HTMLAreaElement;

import java.net.URL;
import java.net.MalformedURLException;

/**
 *
 * @author <a href="mailto:russgold@httpunit.org">Russell Gold</a>
 **/
public class HTMLAreaElementImpl extends HTMLElementImpl implements HTMLAreaElement {

    ElementImpl create() {
        return new HTMLAreaElementImpl();
    }


    public String getHref() {
        try {
            return new URL( ((HTMLDocumentImpl) getOwnerDocument()).getWindow().getUrl(), getAttributeWithNoDefault( "href" ) ).toExternalForm();
        } catch (MalformedURLException e) {
            return e.toString();
        }
    }


    public String getTarget() {
        return getAttributeWithNoDefault( "target" );
    }


    public void setHref( String href ) {
        setAttribute( "href", href );
    }


    public void setTarget( String target ) {
        setAttribute( "target", target );
    }


    public String getAccessKey() {
        return getAttributeWithNoDefault( "accesskey" );
    }


    public String getCoords() {
        return getAttributeWithNoDefault( "coords" );
    }


    public String getShape() {
        return getAttributeWithNoDefault( "shape" );
    }


    public int getTabIndex() {
        return getIntegerAttribute( "tabindex" );
    }


    public void setAccessKey( String accessKey ) {
        setAttribute( "accesskey", accessKey );
    }


    public void setCoords( String coords ) {
        setAttribute( "coords", coords );
    }


    public void setShape( String shape ) {
        setAttribute( "shape", shape );
    }


    public void setTabIndex( int tabIndex ) {
        setAttribute( "tabindex", tabIndex );
    }


    public String getAlt() {
        return getAttributeWithNoDefault( "alt" );
    }


    public boolean getNoHref() {
        return getBooleanAttribute( "nohref" );
    }


    public void setAlt( String alt ) {
        setAttribute( "alt", alt );
    }


    public void setNoHref( boolean noHref ) {
        setAttribute( "nohref", noHref );
    }
}

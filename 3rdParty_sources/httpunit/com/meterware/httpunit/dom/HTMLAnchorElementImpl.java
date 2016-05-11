package com.meterware.httpunit.dom;
/********************************************************************************************************************
 *
 *
 * Copyright (c) 2006-2007, Russell Gold
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
import org.w3c.dom.html.HTMLAnchorElement;

import java.net.URL;
import java.net.MalformedURLException;

/**
 *
 * @author <a href="mailto:russgold@httpunit.org">Russell Gold</a>
 **/
public class HTMLAnchorElementImpl extends HTMLElementImpl implements HTMLAnchorElement {

    ElementImpl create() {
        return new HTMLAnchorElementImpl();
    }


    public String getCharset() {
        return getAttributeWithNoDefault( "charset" );
    }


    public String getHref() {
        String relativeLocation = getAttributeWithNoDefault( "href" );
        if (relativeLocation.indexOf( ':' ) > 0 || relativeLocation.equals( "#" )) {
            return relativeLocation;
        } else {
            try {
                return new URL( ((HTMLDocumentImpl) getOwnerDocument()).getBaseUrl(), relativeLocation ).toExternalForm();
            } catch (MalformedURLException e) {
                return e.toString();
            }
        }
    }


    public String getHreflang() {
        return getAttributeWithNoDefault( "hreflang" );
    }


    public String getRel() {
        return getAttributeWithNoDefault( "rel" );
    }


    public String getRev() {
        return getAttributeWithNoDefault( "rev" );
    }


    public String getTarget() {
        return getAttributeWithNoDefault( "target" );
    }


    public String getType() {
        return getAttributeWithNoDefault( "type" );
    }


    public void setCharset( String charset ) {
        setAttribute( "charset", charset );
    }


    public void setHref( String href ) {
        setAttribute( "href", href );
    }


    public void setHreflang( String hreflang ) {
        setAttribute( "hreflang", hreflang );
    }


    public void setRel( String rel ) {
        setAttribute( "rel", rel );
    }


    public void setRev( String rev ) {
        setAttribute( "rev", rev );
    }


    public void setTarget( String target ) {
        setAttribute( "target", target );
    }


    public void setType( String type ) {
        setAttribute( "type", type );
    }

    /**
     * simulate blur
     */
    public void blur() {
    	handleEvent("onblur");
    }

    /**
     * simulate focus;
     */
    public void focus() {
    	handleEvent("onfocus");
    }

    public String getAccessKey() {
        return getAttributeWithNoDefault( "accesskey" );
    }


    public String getCoords() {
        return getAttributeWithNoDefault( "coords" );
    }


    public String getName() {
        return getAttributeWithNoDefault( "name" );
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


    public void setName( String name ) {
        setAttribute( "name", name );
    }


    public void setShape( String shape ) {
        setAttribute( "shape", shape );
    }


    public void setTabIndex( int tabIndex ) {
        setAttribute( "tabindex", tabIndex );
    }


    public void doClickAction() {
        if (null == getHref() || getHref().startsWith( "#" )) return;
        try {
            ((HTMLDocumentImpl) getOwnerDocument()).getWindow().submitRequest( this, "GET", getHref(), getTarget(), new byte[0] );
        } catch (Exception e) {
            throw new RuntimeException( "Error clicking link: " + e );
        }
    }
}

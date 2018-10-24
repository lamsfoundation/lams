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
import org.w3c.dom.html.HTMLLinkElement;

/**
 *
 * @author <a href="mailto:russgold@httpunit.org">Russell Gold</a>
 **/
public class HTMLLinkElementImpl extends HTMLElementImpl implements HTMLLinkElement {

    ElementImpl create() {
        return new HTMLLinkElementImpl();
    }


    public String getCharset() {
        return getAttributeWithNoDefault( "charset" );
    }


    public boolean getDisabled() {
        return getBooleanAttribute( "disabled" );
    }


    public String getHref() {
        return getAttributeWithNoDefault( "href" );
    }


    public String getHreflang() {
        return getAttributeWithNoDefault( "hreflang" );
    }


    public String getMedia() {
        return getAttributeWithDefault( "media", "screen" );
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


    public void setDisabled( boolean disabled ) {
        setAttribute( "disabled", disabled );
    }


    public void setHref( String href ) {
        setAttribute( "href", href );
    }


    public void setHreflang( String hreflang ) {
        setAttribute( "hreflang", hreflang );
    }


    public void setMedia( String media ) {
        setAttribute( "media", media );
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

}

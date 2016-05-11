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
import org.w3c.dom.html.HTMLImageElement;

/**
 *
 * @author <a href="mailto:russgold@httpunit.org">Russell Gold</a>
 **/
public class HTMLImageElementImpl extends HTMLElementImpl implements HTMLImageElement {

    ElementImpl create() {
        return new HTMLImageElementImpl();
    }


    public String getAlign() {
        return getAttributeWithNoDefault( "align" );
    }


    public String getAlt() {
        return getAttributeWithNoDefault( "alt" );
    }


    public String getBorder() {
        return getAttributeWithNoDefault( "border" );
    }


    public String getHeight() {
        return getAttributeWithNoDefault( "height" );
    }


    public String getHspace() {
        return getAttributeWithNoDefault( "hspace" );
    }


    public boolean getIsMap() {
        return getBooleanAttribute( "ismap" );
    }


    public String getLongDesc() {
        return getAttributeWithNoDefault( "longdesc" );
    }


    public String getLowSrc() {
        return null;
    }


    public String getName() {
        return getAttributeWithNoDefault( "name" );
    }


    public String getSrc() {
        return getAttributeWithNoDefault( "src" );
    }


    public String getUseMap() {
        return getAttributeWithNoDefault( "usemap" );
    }


    public String getVspace() {
        return getAttributeWithNoDefault( "vspace" );
    }


    public String getWidth() {
        return getAttributeWithNoDefault( "width" );
    }


    public void setAlign( String align ) {
        setAttribute( "align", align );
    }


    public void setAlt( String alt ) {
        setAttribute( "alt", alt );
    }


    public void setBorder( String border ) {
        setAttribute( "border", border );
    }


    public void setHeight( String height ) {
        setAttribute( "height", height );
    }


    public void setHspace( String hspace ) {
        setAttribute( "hspace", hspace );
    }


    public void setIsMap( boolean isMap ) {
        setAttribute( "ismap", isMap );
    }


    public void setLongDesc( String longDesc ) {
        setAttribute( "longdesc", longDesc );
    }


    public void setLowSrc( String lowSrc ) {
    }


    public void setName( String name ) {
        setAttribute( "name", name );
    }


    public void setSrc( String src ) {
        setAttribute( "src", src );
    }


    public void setUseMap( String useMap ) {
        setAttribute( "usemap", useMap );
    }


    public void setVspace( String vspace ) {
        setAttribute( "vspace", vspace );
    }


    public void setWidth( String width ) {
        setAttribute( "width", width );
    }
}

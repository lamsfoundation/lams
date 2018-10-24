package com.meterware.httpunit.dom;
/********************************************************************************************************************
 *
 *
 * Copyright (c) 2007, Russell Gold
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
import org.w3c.dom.html.HTMLTableCellElement;
import org.w3c.dom.html.HTMLCollection;

/**
 * @author <a href="mailto:russgold@gmail.com">Russell Gold</a>
 */
public class HTMLTableCellElementImpl extends HTMLElementImpl implements HTMLTableCellElement, HTMLContainerElement, AttributeNameAdjusted {

    ElementImpl create() {
        return new HTMLTableCellElementImpl();
    }


//------------------------------------------ HTMLContainerElement methods ----------------------------------------------


    public HTMLCollection getLinks() {
        return getHtmlDocument().getContainerDelegate().getLinks( this );
    }


    public HTMLCollection getImages() {
        return getHtmlDocument().getContainerDelegate().getImages( this );
    }


    public HTMLCollection getApplets() {
        return getHtmlDocument().getContainerDelegate().getApplets( this );
    }


    public HTMLCollection getForms() {
        return getHtmlDocument().getContainerDelegate().getForms( this );
    }


    public HTMLCollection getAnchors() {
        return getHtmlDocument().getContainerDelegate().getAnchors( this );
    }


//-------------------------------------------- HTMLTableCellElement methods --------------------------------------------


    public String getAbbr() {
        return getAttributeWithNoDefault( "abbr" );
    }


    public void setAbbr( String abbr ) {
        setAttribute( "abbr", abbr );
    }


    public String getAlign() {
        return getAttributeWithNoDefault( "align" );
    }


    public void setAlign( String align ) {
        setAttribute( "align", align );
    }


    public String getAxis() {
        return getAttributeWithNoDefault( "axis" );
    }


    public void setAxis( String axis ) {
        setAttribute( "axis", axis );
    }


    public String getBgColor() {
        return getAttributeWithNoDefault( "bgColor" );
    }


    public void setBgColor( String bgColor ) {
        setAttribute( "bgColor", bgColor );
    }


    public int getCellIndex() {
        return 0;  //To change body of implemented methods use File | Settings | File Templates.
    }


    public String getCh() {
        return getAttributeWithDefault( "char", "." );
    }


    public void setCh( String ch ) {
        setAttribute( "char", ch );
    }


    public String getChOff() {
        return getAttributeWithNoDefault( "charoff" );
    }


    public void setChOff( String chOff ) {
        setAttribute( "charoff", chOff );
    }


    public int getColSpan() {
        return getIntegerAttribute( "colspan", 1 );
    }


    public void setColSpan( int colSpan ) {
        setAttribute( "colspan", colSpan );
    }


    public String getHeaders() {
        return getAttributeWithNoDefault( "headers" );
    }


    public void setHeaders( String headers ) {
        setAttribute( "headers", headers );
    }


    public String getHeight() {
        return getAttributeWithNoDefault( "height" );
    }


    public void setHeight( String height ) {
        setAttribute( "height", height );
    }


    public boolean getNoWrap() {
        return getBooleanAttribute( "nowrap" );
    }


    public void setNoWrap( boolean noWrap ) {
        setAttribute( "nowrap", noWrap );
    }


    public int getRowSpan() {
        return getIntegerAttribute( "rowspan", 1 );
    }


    public void setRowSpan( int rowSpan ) {
        setAttribute( "rowspan", rowSpan );
    }


    public String getScope() {
        return getAttributeWithNoDefault( "scope" );
    }


    public void setScope( String scope ) {
        setAttribute( "scope", scope );
    }


    public String getVAlign() {
        return getAttributeWithDefault( "valign", "middle" );
    }


    public void setVAlign( String vAlign ) {
        setAttribute( "valign", vAlign );
    }


    public String getWidth() {
        return getAttributeWithNoDefault( "width" );
    }


    public void setWidth( String width ) {
        setAttribute( "width", width );
    }


    public String getJavaAttributeName( String attributeName ) {
        if (attributeName.equals( "char" )) return "ch";
        if (attributeName.equals( "charoff" )) return "choff";
        return attributeName;
    }
}

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
import org.w3c.dom.html.HTMLTableRowElement;
import org.w3c.dom.html.HTMLCollection;
import org.w3c.dom.html.HTMLElement;
import org.w3c.dom.DOMException;

/**
 * @author <a href="mailto:russgold@gmail.com">Russell Gold</a>
 */
public class HTMLTableRowElementImpl extends HTMLElementImpl implements HTMLTableRowElement, AttributeNameAdjusted {

    ElementImpl create() {
        return new HTMLTableRowElementImpl();
    }


    public String getAlign() {
        return getAttributeWithNoDefault( "align" );
    }


    public void setAlign( String align ) {
        setAttribute( "align", align );
    }


    public String getBgColor() {
        return getAttributeWithNoDefault( "bgColor" );
    }


    public void setBgColor( String bgColor ) {
        setAttribute( "bgColor", bgColor );
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


    public String getVAlign() {
        return getAttributeWithDefault( "valign", "middle" );
    }


    public void setVAlign( String vAlign ) {
        setAttribute( "valign", vAlign );
    }


    public void deleteCell( int index ) throws DOMException {
        //To change body of implemented methods use File | Settings | File Templates.
    }


    public HTMLCollection getCells() {
        return HTMLCollectionImpl.createHTMLCollectionImpl( getElementsByTagNames( new String[] { "td", "th " } ) );
    }


    public int getRowIndex() {
        return 0;  //To change body of implemented methods use File | Settings | File Templates.
    }


    public int getSectionRowIndex() {
        return 0;  //To change body of implemented methods use File | Settings | File Templates.
    }


    public HTMLElement insertCell( int index ) throws DOMException {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }


    public String getJavaAttributeName( String attributeName ) {
        if (attributeName.equals( "char" )) return "ch";
        if (attributeName.equals( "charoff" )) return "choff";
        return attributeName;
    }
}

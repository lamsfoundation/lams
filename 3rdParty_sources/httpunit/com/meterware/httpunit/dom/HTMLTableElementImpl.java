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
import org.w3c.dom.html.*;
import org.w3c.dom.DOMException;

/**
 * @author <a href="mailto:russgold@gmail.com">Russell Gold</a>
 */
public class HTMLTableElementImpl extends HTMLElementImpl implements HTMLTableElement {

    ElementImpl create() {
        return new HTMLTableElementImpl();
    }


    public String getAlign() {
        return getAttributeWithDefault( "align", "center" );
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


    public String getBorder() {
        return getAttributeWithNoDefault( "border" );
    }


    public void setBorder( String border ) {
        setAttribute( "border", border );
    }


    public String getCellPadding() {
        return getAttributeWithNoDefault( "cellpadding" );
    }


    public void setCellPadding( String cellPadding ) {
        setAttribute( "cellpadding", cellPadding );
    }


    public String getCellSpacing() {
        return getAttributeWithNoDefault( "cellspacing" );
    }


    public void setCellSpacing( String cellSpacing ) {
        setAttribute( "cellspacing", cellSpacing );
    }


    public String getFrame() {
        return getAttributeWithDefault( "frame", "void" );
    }


    public void setFrame( String frame ) {
        setAttribute( "frame", frame );
    }


    public String getRules() {
        return getAttributeWithDefault( "rules", "none" );
    }


    public void setRules( String rules ) {
        setAttribute( "rules", rules );
    }


    public String getSummary() {
        return getAttributeWithNoDefault( "summary" );
    }


    public void setSummary( String summary ) {
        setAttribute( "summary", summary );
    }


    public String getWidth() {
        return getAttributeWithNoDefault( "width" );
    }


    public void setWidth( String width ) {
        setAttribute( "width", width );
    }


    public HTMLElement createCaption() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }


    public HTMLElement createTFoot() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }


    public HTMLElement createTHead() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }


    public void deleteCaption() {
        //To change body of implemented methods use File | Settings | File Templates.
    }


    public void deleteRow( int index ) throws DOMException {
        //To change body of implemented methods use File | Settings | File Templates.
    }


    public void deleteTFoot() {
        //To change body of implemented methods use File | Settings | File Templates.
    }


    public void deleteTHead() {
        //To change body of implemented methods use File | Settings | File Templates.
    }


    public HTMLTableCaptionElement getCaption() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }


    public HTMLCollection getRows() {
        return HTMLCollectionImpl.createHTMLCollectionImpl( getElementsByTagName( "tr" ) );
    }


    public HTMLCollection getTBodies() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }


    public HTMLTableSectionElement getTFoot() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }


    public HTMLTableSectionElement getTHead() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }


    public HTMLElement insertRow( int index ) throws DOMException {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }


    public void setCaption( HTMLTableCaptionElement caption ) {
        //To change body of implemented methods use File | Settings | File Templates.
    }


    public void setTFoot( HTMLTableSectionElement tFoot ) {
        //To change body of implemented methods use File | Settings | File Templates.
    }


    public void setTHead( HTMLTableSectionElement tHead ) {
        //To change body of implemented methods use File | Settings | File Templates.
    }
}

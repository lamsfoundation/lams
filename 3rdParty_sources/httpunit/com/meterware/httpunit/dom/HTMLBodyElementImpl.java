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
import org.w3c.dom.html.HTMLBodyElement;
import org.mozilla.javascript.Function;
import org.mozilla.javascript.Scriptable;

/**
 *
 * @author <a href="mailto:russgold@httpunit.org">Russell Gold</a>
 **/
public class HTMLBodyElementImpl extends HTMLElementImpl implements HTMLBodyElement {

    private HTMLEventHandler _onLoad = new HTMLEventHandler( this, "onload" );

    ElementImpl create() {
        return new HTMLBodyElementImpl();
    }

    /**
     * 
     * @return the onload event
     */
    public Function getOnloadEvent() {
        if (getParentScope() == null && getOwnerDocument() instanceof Scriptable)
        	setParentScope( (Scriptable) getOwnerDocument() );
        return _onLoad.getHandler();
    }

//----------------------------------------- HTMLBodyElement methods ----------------------------------------------------

    public String getALink() {
        return getAttributeWithNoDefault( "aLink" );
    }


    public String getBackground() {
        return getAttributeWithNoDefault( "background" );
    }


    public String getBgColor() {
        return getAttributeWithNoDefault( "bgColor" );
    }


    public String getLink() {
        return getAttributeWithNoDefault( "link" );
    }


    public String getText() {
        return getAttributeWithNoDefault( "text" );
    }


    public String getVLink() {
        return getAttributeWithNoDefault( "vLink" );
    }


    public void setALink( String aLink ) {
        setAttribute( "aLink", aLink );
    }


    public void setBackground( String background ) {
        setAttribute( "background", background );
    }


    public void setBgColor( String bgColor ) {
        setAttribute( "bgColor", bgColor );
    }


    public void setLink( String link ) {
        setAttribute( "link", link );
    }


    public void setText( String text ) {
        setAttribute( "text", text );
    }


    public void setVLink( String vLink ) {
        setAttribute( "vLink", vLink );
    }
}

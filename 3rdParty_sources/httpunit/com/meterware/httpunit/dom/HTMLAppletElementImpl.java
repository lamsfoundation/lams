package com.meterware.httpunit.dom;
/********************************************************************************************************************
 *
 *
 * Copyright (c) 2007, 2008 Russell Gold
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
import org.w3c.dom.html.HTMLAppletElement;

/**
 * @author <a href="mailto:russgold@gmail.com">Russell Gold</a>
 */
public class HTMLAppletElementImpl extends HTMLElementImpl implements HTMLAppletElement {

    ElementImpl create() {
        return new HTMLAppletElementImpl();
    }


    public String getAlign() {
        return getAttributeWithNoDefault( "align" );
    }


    public void setAlign( String align ) {
        setAttribute( "align", align );
    }


    public String getAlt() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }


    public void setAlt( String alt ) {
        //To change body of implemented methods use File | Settings | File Templates.
    }


    public String getArchive() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }


    public void setArchive( String archive ) {
        //To change body of implemented methods use File | Settings | File Templates.
    }


    public String getCode() {
        return getAttributeWithNoDefault( "code" );
    }


    public void setCode( String code ) {
        setAttribute( "code", code );
    }


    /**
     * get the codebase of this applet
     * modified for bug report [ 1895501 ] Handling no codebase attribute in APPLET tag
     */
    public String getCodeBase() {
        return getAttributeWithDefault( "codebase", "." );
    }


    public void setCodeBase( String codeBase ) {
        setAttribute( "codebase", codeBase );
    }


    public String getHeight() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }


    public void setHeight( String height ) {
        //To change body of implemented methods use File | Settings | File Templates.
    }


    public String getHspace() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }


    public void setHspace( String hspace ) {
        //To change body of implemented methods use File | Settings | File Templates.
    }


    public String getName() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }


    public void setName( String name ) {
        //To change body of implemented methods use File | Settings | File Templates.
    }


    public String getObject() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }


    public void setObject( String object ) {
        //To change body of implemented methods use File | Settings | File Templates.
    }


    public String getVspace() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }


    public void setVspace( String vspace ) {
        //To change body of implemented methods use File | Settings | File Templates.
    }


    public String getWidth() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }


    public void setWidth( String width ) {
        //To change body of implemented methods use File | Settings | File Templates.
    }
}

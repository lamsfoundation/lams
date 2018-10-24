package com.meterware.httpunit.dom;
/********************************************************************************************************************
 *
 *
 * Copyright (c) 2004-2007, Russell Gold
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
import org.w3c.dom.html.HTMLTextAreaElement;
import org.w3c.dom.Node;
import org.w3c.dom.Text;

/**
 *
 * @author <a href="mailto:russgold@httpunit.org">Russell Gold</a>
 **/
public class HTMLTextAreaElementImpl extends HTMLControl implements HTMLTextAreaElement {

    private String _value;

    ElementImpl create() {
        return new HTMLTextAreaElementImpl();
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


    public int getCols() {
        return getIntegerAttribute( "cols" );
    }


    public String getDefaultValue() {
        Node node = getFirstChild();
        if (node == null || node.getNodeType() != Node.TEXT_NODE) return null;
        return node.getNodeValue();
    }


    public int getRows() {
        return getIntegerAttribute( "rows" );
    }


    public void select() {
    }


    public void setAccessKey( String accessKey ) {
        setAttribute( "accesskey", accessKey );
    }


    public void setCols( int cols ) {
        setAttribute( "cols", cols );
    }


    public void setDefaultValue( String defaultValue ) {
        Text textNode = getOwnerDocument().createTextNode( defaultValue );
        Node child = getFirstChild();
        if (child == null) {
            appendChild( textNode );
        } else {
            replaceChild( textNode, child );
        }
    }


    public void setRows( int rows ) {
        setAttribute( "rows", rows );
    }


    public String getValue() {
        return _value != null ? _value : getDefaultValue();
    }


    public void setValue( String value ) {
        _value = value;
    }


    public void reset() {
        _value = null;
    }
}

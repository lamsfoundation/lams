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
import org.w3c.dom.Text;
import org.w3c.dom.DOMException;
import org.w3c.dom.Node;

/**
 *
 * @author <a href="mailto:russgold@httpunit.org">Russell Gold</a>
 **/
public class TextImpl extends CharacterDataImpl implements Text {


    static TextImpl createText( DocumentImpl ownerDocument, String data ) {
        TextImpl text = new TextImpl();
        text.initialize( ownerDocument, data );
        return text;
    }


    public String getNodeName() {
        return "#text";
    }


    public String getNodeValue() throws DOMException {
        return getData();
    }


    public void setNodeValue( String nodeValue ) throws DOMException {
        setData( nodeValue );
    }


    public short getNodeType() {
        return TEXT_NODE;
    }


    protected NodeImpl getChildIfPermitted( Node proposedChild ) {
        throw new DOMException( DOMException.HIERARCHY_REQUEST_ERR, "Text nodes may not have children" );
    }


    public Text splitText( int offset ) throws DOMException {
        return null;
    }


    public static Node importNode( DocumentImpl document, Text text ) {
        return document.createTextNode( text.getData() );
    }


    void appendContents( StringBuffer sb ) {
        sb.append( getData() );
    }

//------------------------------------- DOM level 3 methods ------------------------------------------------------------

    public boolean isElementContentWhitespace() {
        return false;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public String getWholeText() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public Text replaceWholeText( String content ) throws DOMException {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }
}

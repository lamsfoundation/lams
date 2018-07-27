package com.meterware.servletunit;
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

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

import java.util.ArrayList;

/**
 *
 * @author <a href="mailto:russgold@httpunit.org">Russell Gold</a>
 **/
abstract class XMLUtils {

    static String getChildNodeValue( Element root, String childNodeName ) throws SAXException {
        return getChildNodeValue( root, childNodeName, null );
    }


    static String getChildNodeValue( Element root, String childNodeName, String defaultValue ) throws SAXException {
        NodeList nl = root.getElementsByTagName( childNodeName );
        if (nl.getLength() == 1) {
            return getTextValue( nl.item( 0 ) ).trim();
        } else if (defaultValue == null) {
            throw new SAXException( "Node <" + root.getNodeName() + "> has no child named <" + childNodeName + ">" );
        } else {
            return defaultValue;
        }
    }


    static String getTextValue( Node node ) throws SAXException {
        Node textNode = node.getFirstChild();
        if (textNode == null) return "";
        if (textNode.getNodeType() != Node.TEXT_NODE) throw new SAXException( "No text value found for <" + node.getNodeName() + "> node" );
        return textNode.getNodeValue();
    }


    static boolean hasChildNode( Element root, String childNodeName ) {
        NodeList nl = root.getElementsByTagName( childNodeName );
        return (nl.getLength() > 0);
    }

}

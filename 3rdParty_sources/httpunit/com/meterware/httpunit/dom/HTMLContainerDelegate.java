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
import org.w3c.dom.html.HTMLCollection;
import org.w3c.dom.Node;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.meterware.httpunit.ParsedHTML;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * @author <a href="mailto:russgold@httpunit.org">Russell Gold</a>
 */
class HTMLContainerDelegate {

    private NodeImpl.IteratorMask _iteratorMask = NodeImpl.SKIP_IFRAMES;


    HTMLContainerDelegate( NodeImpl.IteratorMask iteratorMask ) {
        _iteratorMask = iteratorMask;
    }


    /**
     * get Links for a given Node
     * @param rootNode
     * @return
     */
    HTMLCollection getLinks( NodeImpl rootNode ) {
        ArrayList elements = new ArrayList();
        for (Iterator each = rootNode.preOrderIteratorAfterNode( _iteratorMask ); each.hasNext();) {
            Node node = (Node) each.next();
            if (node.getNodeType() != Node.ELEMENT_NODE) continue;

            if (ParsedHTML.isWebLink(node)) {
                elements.add( node );
            }
        }
        return HTMLCollectionImpl.createHTMLCollectionImpl( new NodeListImpl( elements ) );
    }


    HTMLCollection getForms( NodeImpl rootNode ) {
        ArrayList elements = new ArrayList();
        for (Iterator each = rootNode.preOrderIteratorAfterNode( _iteratorMask ); each.hasNext();) {
            Node node = (Node) each.next();
            if (node.getNodeType() != Node.ELEMENT_NODE) continue;

            if ("form".equalsIgnoreCase( ((Element) node).getTagName() )) {
                elements.add( node );
            }
        }
        return HTMLCollectionImpl.createHTMLCollectionImpl( new NodeListImpl( elements ) );
    }


    HTMLCollection getAnchors( NodeImpl rootNode ) {
        NodeList nodeList = rootNode.getElementsByTagName( "A" );
        ArrayList elements = new ArrayList();
        for (int i = 0; i < nodeList.getLength(); i++) {
            Node node = nodeList.item( i );
            if (node.getAttributes().getNamedItem( "name" ) != null) {
                elements.add( node );
            }
        }
        return HTMLCollectionImpl.createHTMLCollectionImpl( new NodeListImpl( elements ) );
    }


    HTMLCollection getImages( NodeImpl rootNode ) {
        ArrayList elements = new ArrayList();
        rootNode.appendElementsWithTags( new String[] {"img"}, elements );
        return HTMLCollectionImpl.createHTMLCollectionImpl( new NodeListImpl( elements ) );
    }


    HTMLCollection getApplets( NodeImpl rootNode ) {
        ArrayList elements = new ArrayList();
        rootNode.appendElementsWithTags( new String[] {"applet"}, elements );
        return HTMLCollectionImpl.createHTMLCollectionImpl( new NodeListImpl( elements ) );
    }
}

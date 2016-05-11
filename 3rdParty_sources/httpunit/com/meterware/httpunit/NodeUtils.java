package com.meterware.httpunit;
/********************************************************************************************************************

*
* Copyright (c) 2000-2008, Russell Gold
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
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.Stack;
import java.util.Iterator;
import java.util.ListIterator;

import com.meterware.httpunit.parsing.HTMLParserFactory;


/**
 * Some common utilities for manipulating DOM nodes.
 **/
public class NodeUtils {

  /**
   * get the attribute with the given name from the given node as an int value
   * @param node - the node to look in
   * @param attributeName - the attribute's name to look for
   * @param defaultValue
   * @return - the value - defaultValue as default 
   */
    public static int getAttributeValue( Node node, String attributeName, int defaultValue ) {
        NamedNodeMap nnm = node.getAttributes();
        Node attribute = nnm.getNamedItem( attributeName );
        if (attribute == null) {
            return defaultValue;
        } else try {
            return Integer.parseInt( attribute.getNodeValue() );
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }


    /**
     * get the attribute with the given name from the given node
     * @param node - the node to look in
     * @param attributeName - the attribute's name to look for
     * @return - the value - "" as default
     */
    public static String getNodeAttribute( Node node, String attributeName ) {
        return getNodeAttribute( node, attributeName, "" );
    }


    /**
     * get the attribute with the given name from the given node
     * @param node - the node to look in
     * @param attributeName - the attribute's name to look for
     * @param defaultValue
     * @return - the value - defaultValue as default
     */
    public static String getNodeAttribute( Node node, String attributeName, String defaultValue ) {
        NamedNodeMap attributes = node.getAttributes();
        if (attributes == null) return defaultValue;

        Node attribute = attributes.getNamedItem( attributeName );
        return (attribute == null) ? defaultValue : attribute.getNodeValue();
    }

    /**
     * set the attribute with the given attribute to the given value in the given node
     * @param node
     * @param attributeName - the attribute's name to look for
     * @param value - the value to set
     */
    static void setNodeAttribute( Node node, String attributeName, String value ) {
    	((Element)node).setAttributeNS(null, attributeName, value );
    }
    
    /**
     * remove the given attribute from the given node based on the attribute's name
     * @param node
     * @param attributeName
     */
    static void removeNodeAttribute( Node node, String attributeName ) {
    	((Element)node).removeAttribute( attributeName );
    }
    
    /**
     * check whether the given Attribute in the Node is Present
     * @param node - the node to check
     * @param attributeName - the attribute name to check
     * @return true if the attribute is present
     */
    public static boolean isNodeAttributePresent( Node node, final String attributeName ) {
        return node.getAttributes().getNamedItem( attributeName ) != null;
    }


    /**
     * common Node action methods
     */
    interface NodeAction {
        /**
         * Does appropriate processing on specified element. Will return false if the subtree below the element
         * should be skipped.
         */
        public boolean processElement( PreOrderTraversal traversal, Element element );

        /**
         * Processes a text node.
         */
        public void processTextNode( PreOrderTraversal traversal, Node textNode );
    }

    /**
     * Converts the DOM trees rooted at the specified nodes to text, ignoring
     * any HTML tags.
     **/
    public static String asText( NodeList rootNodes ) {
        final StringBuffer sb = new StringBuffer(HttpUnitUtils.DEFAULT_TEXT_BUFFER_SIZE);
        NodeAction action = new NodeAction() {
            public boolean processElement( PreOrderTraversal traversal, Element node ) {
                String nodeName = node.getNodeName().toLowerCase();
                if (nodeName.equals( "p" ) || nodeName.equals( "br" ) || nodeName.equalsIgnoreCase( "tr" )) {
                    sb.append( "\n" );
                } else if (nodeName.equals( "td" ) || nodeName.equalsIgnoreCase( "th" )) {
                    sb.append( " | " );
                } else if (nodeName.equals( "img" ) && HttpUnitOptions.getImagesTreatedAsAltText()) {
                    sb.append( getNodeAttribute( node, "alt" ) );
                }
                return true;
            }
            public void processTextNode( PreOrderTraversal traversal, Node textNode ) {
                sb.append( HTMLParserFactory.getHTMLParser().getCleanedText( textNode.getNodeValue() ) );
            }
        };
        new PreOrderTraversal( rootNodes ).perform( action );
        return sb.toString();
    }


    static class PreOrderTraversal {

        private Stack _pendingNodes = new Stack();
        private Stack _traversalContext = new Stack();
        private static final Object POP_CONTEXT = new Object();


        public PreOrderTraversal( NodeList rootNodes ) {
            pushNodeList( rootNodes );
        }


        public PreOrderTraversal( Node rootNode ) {
            pushNodeList( rootNode.getLastChild() );
        }


        public void pushBaseContext( Object context ) {
            _traversalContext.push( context );
        }


        public void pushContext( Object context ) {
            _traversalContext.push( context );
            _pendingNodes.push( POP_CONTEXT );
        }


        public Iterator getContexts() {
            Stack stack = _traversalContext;
            return getTopDownIterator( stack );
        }


        public Object getRootContext() {
            return _traversalContext.firstElement();
        }


        private Iterator getTopDownIterator( final Stack stack ) {
            return new Iterator() {
                private ListIterator _forwardIterator = stack.listIterator( stack.size() );

                public boolean hasNext() {
                    return _forwardIterator.hasPrevious();
                }

                public Object next() {
                    return _forwardIterator.previous();
                }

                public void remove() {
                    _forwardIterator.remove();
                }
            };
        }


        /**
         * Returns the most recently pushed context which implements the specified class.
         * Will return null if no matching context is found.
         */
        public Object getClosestContext( Class matchingClass ) {
            for (int i = _traversalContext.size()-1; i >= 0; i-- ) {
                Object o = _traversalContext.elementAt( i );
                if (matchingClass.isInstance(o)) return o;
            }
            return null;
        }


        public void perform( NodeAction action ) {
            while (!_pendingNodes.empty()) {
                final Object object = _pendingNodes.pop();
                if (object == POP_CONTEXT) {
                    _traversalContext.pop();
                } else {
                    Node node = (Node) object;
                    if (node.getNodeType() == Node.TEXT_NODE) {
                        action.processTextNode( this, node );
                    } else if (node.getNodeType() != Node.ELEMENT_NODE) {
                        continue;
                    } else
                        action.processElement( this, (Element) node );
                    pushNodeList( node.getLastChild() );
                }
            }
        }


        private void pushNodeList( NodeList nl ) {
            if (nl != null) {
                for (int i = nl.getLength()-1; i >= 0; i--) {
                    _pendingNodes.push( nl.item(i) );
                }
            }
        }


        private void pushNodeList( Node lastChild ) {
            for (Node node = lastChild; node != null; node = node.getPreviousSibling()) { _pendingNodes.push( node ); }
        }
    }

}
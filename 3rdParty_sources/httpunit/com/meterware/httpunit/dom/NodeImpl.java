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
import org.w3c.dom.*;
import org.w3c.dom.html.HTMLIFrameElement;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;

/**
 *
 * @author <a href="mailto:russgold@httpunit.org">Russell Gold</a>
 **/
abstract public class NodeImpl extends AbstractDomComponent implements Node {
 
    private DocumentImpl _ownerDocument;
    private NodeImpl     _parentNode;
    private NodeImpl     _firstChild;
    private NodeImpl     _nextSibling;
    private NodeImpl     _previousSibling;
    private Hashtable    _userData = new Hashtable( );

    static IteratorMask SKIP_IFRAMES = new IteratorMask() {
        public boolean skipSubtree( Node subtreeRoot ) {
            return subtreeRoot instanceof HTMLIFrameElement;
        }
    };


    protected void initialize( DocumentImpl ownerDocument ) {
        if (_ownerDocument != null) throw new IllegalStateException( "NodeImpl already initialized" );
        if (ownerDocument == null) throw new IllegalArgumentException( "No owner document specified" );
        _ownerDocument = ownerDocument;
    }


//------------------------------------------ ScriptableObject methods --------------------------------------------------

//------------------------------------------ ScriptingEngine methods --------------------------------------------------

//----------------------------------------------- Node methods ---------------------------------------------------------


    public Node getParentNode() {
        return _parentNode;
    }


    public NodeList getChildNodes() {
        ArrayList list = new ArrayList();
        for (NodeImpl child = _firstChild; child != null; child = child._nextSibling) {
            list.add( child );
        }
        return new NodeListImpl( list );
    }


    public Node getFirstChild() {
        return _firstChild;
    }


    public Node getLastChild() {
        if (_firstChild == null) return null;

        Node child = _firstChild;
        while (child.getNextSibling() != null) child = child.getNextSibling();
        return child;
    }


    public Node getPreviousSibling() {
        return _previousSibling;
    }


    public Node getNextSibling() {
        return _nextSibling;
    }


    public NamedNodeMap getAttributes() {
        return null;
    }


    public Document getOwnerDocument() {
        return _ownerDocument;
    }


    public Node insertBefore( Node newChild, Node refChild ) throws DOMException {
        NodeImpl refChildNode = (NodeImpl) refChild;
        if (refChildNode.getParentNode() != this) throw new DOMException( DOMException.NOT_FOUND_ERR, "Must specify an existing child as the reference" );
        NodeImpl newChildNode = getChildIfPermitted( newChild );
        removeFromTree( newChildNode );
        newChildNode._parentNode = this;
        if (refChildNode._previousSibling == null) {
            _firstChild = newChildNode;
        } else {
            refChildNode._previousSibling.setNextSibling( newChildNode );
        }
        newChildNode.setNextSibling( refChildNode );
        return newChildNode;
    }


    private void removeFromTree( NodeImpl childNode ) {
        if (childNode._parentNode != null) {
            if (childNode._previousSibling != null) {
                childNode._previousSibling.setNextSibling( childNode._nextSibling );
            } else {
                childNode._parentNode._firstChild = childNode._nextSibling;
                childNode._nextSibling._previousSibling = null;
            }
            childNode._parentNode = null;
        }
    }


    public Node replaceChild( Node newChild, Node oldChild ) throws DOMException {
        insertBefore( newChild, oldChild );
        return removeChild( oldChild );
    }


    public Node removeChild( Node oldChild ) throws DOMException {
        if (oldChild.getParentNode() != this) throw new DOMException( DOMException.NOT_FOUND_ERR, "May only remove a node from its own parent" );
        removeFromTree( (NodeImpl) oldChild );
        return oldChild;
    }


    public Node appendChild( Node newChild ) throws DOMException {
        if (newChild == null) throw new IllegalArgumentException( "child to append may not be null" );

        NodeImpl childNode = getChildIfPermitted( newChild );
        removeFromTree( childNode );
        childNode._parentNode = this;
        if (_firstChild == null) {
            _firstChild = childNode;
        } else {
            ((NodeImpl) getLastChild()).setNextSibling( childNode );
        }
        return newChild;
    }


    protected NodeImpl getChildIfPermitted( Node proposedChild ) {
        if (!(proposedChild instanceof NodeImpl)) throw new DOMException( DOMException.WRONG_DOCUMENT_ERR, "Specified node is from a different DOM implementation" );
        NodeImpl childNode = (NodeImpl) proposedChild;
        if (getOwnerDocument() != childNode._ownerDocument) throw new DOMException( DOMException.WRONG_DOCUMENT_ERR, "Specified node is from a different document" );
        for (Node parent = this; parent != null; parent = parent.getParentNode()) {
            if (proposedChild == parent) throw new DOMException( DOMException.HIERARCHY_REQUEST_ERR, "May not add node as its own descendant" );
        }

        return childNode;
    }


    private void setNextSibling( NodeImpl sibling ) {
        _nextSibling = sibling;
        if (sibling != null) sibling._previousSibling = this;
    }


    public boolean hasChildNodes() {
        return _firstChild != null;
    }


    public Node cloneNode( boolean deep ) {
        return getOwnerDocument().importNode( this, deep );
    }


    public void normalize() {
    }


    public boolean isSupported( String feature, String version ) {
        return false;
    }


    public String getNamespaceURI() {
        return null;
    }


    public String getPrefix() {
        return null;
    }


    public void setPrefix( String prefix ) throws DOMException {
    }


    public String getLocalName() {
        return null;
    }


    public boolean hasAttributes() {
        return false;
    }

//------------------------------------ DOM level 3 methods -------------------------------------------------------------

    public Object setUserData( String key, Object data, UserDataHandler handler ) {
        return _userData.put( key, data );
    }


    public Object getUserData( String key ) {
        return _userData.get( key );
    }


    public Object getFeature( String feature, String version ) {
        return null;
    }


    public boolean isEqualNode( Node arg ) {
        return false;  //To change body of implemented methods use File | Settings | File Templates.
    }


    public String lookupNamespaceURI( String prefix ) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }


    public String getBaseURI() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }


    public short compareDocumentPosition( Node other ) throws DOMException {
        return 0;  //To change body of implemented methods use File | Settings | File Templates.
    }


    public String getTextContent() throws DOMException {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }


    public void setTextContent( String textContent ) throws DOMException {
        //To change body of implemented methods use File | Settings | File Templates.
    }


    public boolean isSameNode( Node other ) {
        return this == other;
    }


    public String lookupPrefix( String namespaceURI ) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }


    public boolean isDefaultNamespace( String namespaceURI ) {
        return false;  //To change body of implemented methods use File | Settings | File Templates.
    }

//----------------------------------------- implementation internals ---------------------------------------------------

    public NodeList getElementsByTagName( String name ) {
        ArrayList matchingElements = new ArrayList();
        appendElementsWithTag( name, matchingElements );
        return new NodeListImpl( matchingElements );
    }


    private void appendElementsWithTag( String name, ArrayList matchingElements ) {
        for (Node child = getFirstChild(); child != null; child = child.getNextSibling()) {
            if (child.getNodeType() != ELEMENT_NODE) continue;
            if (name.equals( "*" ) || ((Element) child).getTagName().equalsIgnoreCase( name )) matchingElements.add( child );
            ((NodeImpl) child).appendElementsWithTag( name, matchingElements );
        }
    }


    protected NodeList getElementsByTagNames( String[] names ) {
        ArrayList matchingElements = new ArrayList();
        appendElementsWithTags( names, matchingElements );
        return new NodeListImpl( matchingElements );
    }


    void appendElementsWithTags( String[] names, ArrayList matchingElements ) {
        for (Node child = getFirstChild(); child != null; child = child.getNextSibling()) {
            if (child.getNodeType() != ELEMENT_NODE) continue;
            String tagName = ((Element) child).getTagName();
            for (int i = 0; i < names.length; i++) {
                if (tagName.equalsIgnoreCase( names[i] )) matchingElements.add( child );
            }
            ((NodeImpl) child).appendElementsWithTags( names, matchingElements );
        }
    }


    String asText() {
        StringBuffer sb = new StringBuffer();
        appendContents( sb );
        return sb.toString();
    }


    void appendContents( StringBuffer sb ) {
        NodeList nl = getChildNodes();
        for (int i = 0; i < nl.getLength(); i++) {
            ((NodeImpl) nl.item(i)).appendContents( sb );
        }
    }


    public Iterator preOrderIterator() {
        return new PreOrderIterator( this );
    }


    public Iterator preOrderIterator( IteratorMask mask ) {
        return new PreOrderIterator( this, mask );
    }


    public Iterator preOrderIteratorAfterNode() {
        return new PreOrderIterator( PreOrderIterator.nextNode( this ) );
    }


    public Iterator preOrderIteratorAfterNode( IteratorMask mask ) {
        return new PreOrderIterator( PreOrderIterator.nextNode( this ), mask );
    }


    protected String getJavaPropertyName( String propertyName ) {
        if (propertyName.equals( "document" )) {
            return "ownerDocument";
        } else {
            return propertyName;
        }
    }


    interface IteratorMask {
        boolean skipSubtree( Node subtreeRoot );
    }


    static class PreOrderIterator implements Iterator {
        private NodeImpl _nextNode;
        private IteratorMask _mask;


        PreOrderIterator( NodeImpl currentNode ) {
            _nextNode = currentNode;
        }


        PreOrderIterator( NodeImpl currentNode, IteratorMask mask ) {
            this( currentNode );
            _mask = mask;
        }


        public boolean hasNext() {
            return null != _nextNode;
        }


        public Object next() {
            NodeImpl currentNode = _nextNode;
            _nextNode = nextNode( _nextNode );
            while (_mask != null && _nextNode != null && _mask.skipSubtree( _nextNode )) _nextNode = nextSubtree( _nextNode );
            return currentNode;
        }


        public void remove() {
            throw new java.lang.UnsupportedOperationException();
        }


        static NodeImpl nextNode( NodeImpl node ) {
            if (node._firstChild != null) return node._firstChild;
            return nextSubtree( node );
        }


        private static NodeImpl nextSubtree( NodeImpl node ) {
            if (node._nextSibling != null) return node._nextSibling;
            while (node._parentNode != null) {
                node = node._parentNode;
                if (node._nextSibling != null) return node._nextSibling;
            }
            return null;
        }
    }
}

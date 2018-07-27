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
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.DOMException;

import java.util.Hashtable;

/**
 *
 * @author <a href="mailto:russgold@httpunit.org">Russell Gold</a>
 **/
public class NamedNodeMapImpl implements NamedNodeMap {


    private Hashtable _items;
    private Node[]    _itemArray;


    NamedNodeMapImpl( Hashtable items ) {
        _items     = (Hashtable) items.clone();
        _itemArray = (Node[]) _items.values().toArray( new Node[ _items.size() ] );
    }


    public Node getNamedItem( String name ) {
        return (Node) _items.get( name );
    }


    public Node setNamedItem( Node arg ) throws DOMException {
        return null;
    }


    public Node removeNamedItem( String name ) throws DOMException {
        return null;
    }


    public Node item( int index ) {
        return _itemArray[ index ];
    }


    public int getLength() {
        return _items.size();
    }


    public Node getNamedItemNS( String namespaceURI, String localName ) {
        return null;
    }


    public Node setNamedItemNS( Node arg ) throws DOMException {
        return null;
    }


    public Node removeNamedItemNS( String namespaceURI, String localName ) throws DOMException {
        return null;
    }
}

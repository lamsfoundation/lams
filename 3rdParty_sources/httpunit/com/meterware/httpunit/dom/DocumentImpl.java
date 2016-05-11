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
import org.w3c.dom.*;
import org.w3c.dom.html.HTMLElement;

import java.util.Iterator;

/**
 *
 * @author <a href="mailto:russgold@httpunit.org">Russell Gold</a>
 **/
public class DocumentImpl extends NodeImpl implements Document {

    private Element _documentElement;


    static DocumentImpl createDocument() {
        DocumentImpl document = new DocumentImpl();
        document.initialize();
        return document;
    }


    protected void initialize() {}


    public String getNodeName() {
        return "#document";
    }


    public String getNodeValue() throws DOMException {
        return null;
    }


    public void setNodeValue( String nodeValue ) throws DOMException {
    }


    public short getNodeType() {
        return DOCUMENT_NODE;
    }


    public Document getOwnerDocument() {
        return this;
    }


    public DocumentType getDoctype() {
        return null;
    }


    public DOMImplementation getImplementation() {
        return null;
    }


    public Element getDocumentElement() {
        return _documentElement;
    }


    void setDocumentElement( Element documentElement ) {
        if (_documentElement != null) throw new IllegalStateException( "A document may have only one root" );
        _documentElement = documentElement;
        appendChild( documentElement );
    }


    public Element createElement( String tagName ) throws DOMException {
        return ElementImpl.createElement( this, tagName );
    }


    public DocumentFragment createDocumentFragment() {
        throw new UnsupportedOperationException( "DocumentFragment creation not supported ");
    }


    public Text createTextNode( String data ) {
        return TextImpl.createText( this, data );
    }


    public Comment createComment( String data ) {
        return CommentImpl.createComment( this, data );
    }


    public CDATASection createCDATASection( String data ) throws DOMException {
        return CDATASectionImpl.createCDATASection( this, data );
    }


    public ProcessingInstruction createProcessingInstruction( String target, String data ) throws DOMException {
        return ProcessingInstructionImpl.createProcessingImpl( this, target, data );
    }


    public Attr createAttribute( String name ) throws DOMException {
        return AttrImpl.createAttribute( this, name );
    }


    public EntityReference createEntityReference( String name ) throws DOMException {
        throw new UnsupportedOperationException( "EntityReference creation not supported ");
    }


    public Node importNode( Node importedNode, boolean deep ) throws DOMException {
        switch (importedNode.getNodeType()) {
            case Node.ATTRIBUTE_NODE:
                return AttrImpl.importNode( this, (Attr) importedNode );
            case Node.CDATA_SECTION_NODE:
                return CDATASectionImpl.importNode( this, (CDATASection) importedNode );
            case Node.COMMENT_NODE:
                return CommentImpl.importNode( this, (Comment) importedNode );
            case Node.ELEMENT_NODE:
                return ElementImpl.importNode( this, (Element) importedNode, deep );
            case Node.PROCESSING_INSTRUCTION_NODE:
                return ProcessingInstructionImpl.importNode( this, (ProcessingInstruction) importedNode );
            case Node.TEXT_NODE:
                return TextImpl.importNode( this, (Text) importedNode );
            default:
                throw new DOMException( DOMException.NOT_SUPPORTED_ERR, "Cannot import node type " + importedNode.getNodeType() );
        }
    }


    public Element getElementById( String elementId ) {
        for (Iterator each = preOrderIterator(); each.hasNext();) {
            Node node = (Node) each.next();
            if (!(node instanceof HTMLElement)) continue;
            HTMLElement element = (HTMLElement) node;
            if (elementId.equals( element.getId() )) return element;
        }
        return null;
    }


    public Element createElementNS( String namespaceURI, String qualifiedName ) throws DOMException {
        return ElementImpl.createElement( this, namespaceURI, qualifiedName );
    }


    public Attr createAttributeNS( String namespaceURI, String qualifiedName ) throws DOMException {
        return AttrImpl.createAttribute( this, namespaceURI, qualifiedName );
    }


    public NodeList getElementsByTagNameNS( String namespaceURI, String localName ) {
        if (namespaceURI != null) throw new UnsupportedOperationException( "Namespaces are not supported" );
        return getElementsByTagName( localName );
    }


    void importChildren( Node original, Node copy ) {
        NodeList children = original.getChildNodes();
        for (int i = 0; i < children.getLength(); i++) {
            Node childCopy = importNode( children.item(i), /* deep */ true );
            copy.appendChild( childCopy );
        }
    }


//------------------------------------- DOM level 3 methods ------------------------------------------------------------

    public String getInputEncoding() {
        return null;
    }

    public String getXmlEncoding() {
        return null;
    }

    public Node renameNode( Node n, String namespaceURI, String qualifiedName ) throws DOMException {
        return null;
    }

    public boolean getXmlStandalone() {
        return false;
    }

    public void setXmlStandalone( boolean xmlStandalone ) throws DOMException {
    }

    public String getXmlVersion() {
        return null;
    }

    public void setXmlVersion( String xmlVersion ) throws DOMException {
    }

    public boolean getStrictErrorChecking() {
        return false;
    }

    public void setStrictErrorChecking( boolean strictErrorChecking ) {
    }

    public String getDocumentURI() {
        return null;
    }

    public void setDocumentURI( String documentURI ) {
    }

    public Node adoptNode( Node source ) throws DOMException {
        return null;
    }

    public DOMConfiguration getDomConfig() {
        return null;
    }

    public void normalizeDocument() {
    }

}

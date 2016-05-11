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

/**
 *
 * @author <a href="mailto:russgold@httpunit.org">Russell Gold</a>
 **/
public class AttrImpl extends NodeImpl implements Attr {

    private String  _name;
    private String  _value = "";
    private boolean _specified = false;
    private Element _ownerElement;


    static AttrImpl createAttribute( DocumentImpl owner, String name ) {
        AttrImpl attribute = new AttrImpl();
        attribute.initialize( owner, name );
        return attribute;
    }


    public static Attr createAttribute( DocumentImpl owner, String namespaceURI, String qualifiedName ) {
        AttrImpl attribute = new AttrImpl();
        attribute.initialize( owner, qualifiedName );
        return attribute;
    }


    protected void initialize( DocumentImpl owner, String name ) {
        super.initialize( owner );
        _name = name;
    }


    public String getNodeName() {
        return getName();
    }


    public String getNodeValue() throws DOMException {
        return getValue();
    }


    public void setNodeValue( String nodeValue ) throws DOMException {
        setValue( nodeValue );
    }


    public short getNodeType() {
        return ATTRIBUTE_NODE;
    }


    public String getName() {
        return _name;
    }


    public boolean getSpecified() {
        return _specified;
    }


    public String getValue() {
        return _value;
    }


    public void setValue( String value ) throws DOMException {
        _value = value;
        _specified = true;
    }


    public Element getOwnerElement() {
        return _ownerElement;
    }


    void setOwnerElement( Element ownerElement ) {
        _ownerElement = ownerElement;
    }


    public static Node importNode( Document document, Attr attr ) {
        Attr attribute = document.createAttributeNS( attr.getNamespaceURI(), attr.getName() );
        attribute.setValue( attr.getValue() );
        return attribute;
    }

//------------------------------------- DOM level 3 methods ------------------------------------------------------------

    public TypeInfo getSchemaTypeInfo() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }


    public boolean isId() {
        return false;  //To change body of implemented methods use File | Settings | File Templates.
    }
}

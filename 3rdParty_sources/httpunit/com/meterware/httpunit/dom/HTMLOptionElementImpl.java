package com.meterware.httpunit.dom;
/********************************************************************************************************************
 *
 *
 * Copyright (c) 2006-2007, Russell Gold
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
import org.w3c.dom.html.HTMLOptionElement;
import org.w3c.dom.Node;
import com.meterware.httpunit.protocol.ParameterProcessor;

import java.io.IOException;

/**
 *
 * @author <a href="mailto:russgold@httpunit.org">Russell Gold</a>
 **/
public class HTMLOptionElementImpl extends HTMLControl implements HTMLOptionElement {

    private Boolean _selected;

    ElementImpl create() {
        return new HTMLOptionElementImpl();
    }


    public boolean getDefaultSelected() {
        return getBooleanAttribute( "selected" );
    }


    public int getIndex() {
        return getSelect().getIndexOf( this );
    }


    public void setIndex( int i ) {};    // obsolete - required for compatibility with JDK 1.3


    public String getLabel() {
        return getAttributeWithNoDefault( "label" );
    }


    public boolean getSelected() {
        return _selected != null ? _selected.booleanValue() : getDefaultSelected();
    }


    public String getText() {
        return asText();
    }


    public void setDefaultSelected( boolean defaultSelected ) {
    }


    public void setLabel( String label ) {
        setAttribute( "label", label );
    }


    public void setSelected( boolean selected ) {
        if (selected && getSelect().getType().equals( HTMLSelectElementImpl.TYPE_SELECT_ONE)) getSelect().clearSelected();
        _selected = selected ? Boolean.TRUE : Boolean.FALSE;
    }


    private HTMLSelectElementImpl getSelect() {
        Node parent = getParentNode();
        while (parent != null && !("select".equalsIgnoreCase( parent.getNodeName() ))) parent = parent.getParentNode();
        return (HTMLSelectElementImpl) parent;
    }


    public String getValue() {
        return getAttributeWithNoDefault( "value" );
    }


    public void setValue( String value ) {
        setAttribute( "value", value );
    }


    public void reset() {
        _selected = null;
    }


    void addValueIfSelected( ParameterProcessor processor, String name, String characterSet ) throws IOException {
        if (getSelected()) {
            String value = getValue();
            if (value == null) value = readDisplayedValue();
            processor.addParameter( name, value, characterSet );
        }
    }

    private String readDisplayedValue() {
        Node nextSibling = getNextSibling();
        while (nextSibling != null && nextSibling.getNodeType() != Node.TEXT_NODE && nextSibling.getNodeType() != Node.ELEMENT_NODE) nextSibling = nextSibling.getNextSibling();
        if (nextSibling == null || nextSibling.getNodeType() != Node.TEXT_NODE) return "";
        return nextSibling.getNodeValue();
    }

}

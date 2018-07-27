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
import org.w3c.dom.Node;
import org.w3c.dom.html.HTMLFormElement;
import org.w3c.dom.html.HTMLCollection;

import java.util.Iterator;
import java.io.IOException;

import com.meterware.httpunit.protocol.ParameterProcessor;

/**
 *
 * @author <a href="mailto:russgold@httpunit.org">Russell Gold</a>
 **/
public class HTMLControl extends HTMLElementImpl {

    public boolean getDisabled() {
        return getBooleanAttribute( "disabled" );
    }


    public HTMLFormElement getForm() {
        Node parent = getParentNode();
        while (parent != null && !("form".equalsIgnoreCase( parent.getNodeName() ))) parent = parent.getParentNode();
        if (parent != null) return (HTMLFormElement) parent;

        for (Iterator here = preOrderIterator(); here.hasNext();) {
            Object o = here.next();
            if (o instanceof HTMLFormElement) return getPreviousForm( (HTMLFormElement) o );
        }
        return getLastFormInDocument();
    }


    private HTMLFormElement getPreviousForm( HTMLFormElement nextForm ) {
        HTMLCollection forms = getHtmlDocument().getForms();
        for (int i = 0; i < forms.getLength(); i++) {
            if (nextForm == forms.item( i )) return i == 0 ? null : (HTMLFormElement) forms.item( i-1 );
        }
        return null;
    }


    private HTMLFormElement getLastFormInDocument() {
        HTMLCollection forms = getHtmlDocument().getForms();
        return forms.getLength() == 0 ? null : (HTMLFormElement) forms.item( forms.getLength()-1 );
    }


    public String getName() {
        return getAttributeWithNoDefault( "name" );
    }


    public boolean getReadOnly() {
        return getBooleanAttribute( "readonly" );
    }


    public int getTabIndex() {
        return getIntegerAttribute( "tabindex" );
    }


    public String getType() {
        return getAttributeWithDefault( "type", "text" );
    }


    public void setDisabled( boolean disabled ) {
        setAttribute( "disabled", disabled );
    }


    public void setName( String name ) {
        setAttribute( "name", name );
    }


    public void setReadOnly( boolean readOnly ) {
        setAttribute( "readonly", readOnly );
    }


    public void setTabIndex( int tabIndex ) {
        setAttribute( "tabindex", tabIndex );
    }


    public void reset() {}


    void addValues( ParameterProcessor processor, String characterSet ) throws IOException {}


    public void silenceSubmitButton() {}
}

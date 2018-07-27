package com.meterware.httpunit.dom;
/********************************************************************************************************************
 *
 *
 * Copyright (c) 2004,2006-2007, Russell Gold
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
import org.w3c.dom.html.HTMLSelectElement;
import org.w3c.dom.html.HTMLElement;
import org.w3c.dom.html.HTMLCollection;
import org.w3c.dom.html.HTMLOptionElement;
import org.w3c.dom.DOMException;
import com.meterware.httpunit.protocol.ParameterProcessor;

import java.io.IOException;

/**
 *
 * @author <a href="mailto:russgold@httpunit.org">Russell Gold</a>
 **/
public class HTMLSelectElementImpl extends HTMLControl implements HTMLSelectElement {

    public static final String TYPE_SELECT_ONE = "select-one";
    public static final String TYPE_SELECT_MULTIPLE = "select-multiple";


    ElementImpl create() {
        return new HTMLSelectElementImpl();
    }


    public void add( HTMLElement element, HTMLElement before ) throws DOMException {
    }



    /**
     * simulate blur
     */
    public void blur() {
    	handleEvent("onblur");
    }


    /**
     * simulate focus;
     */
    public void focus() {
    	handleEvent("onfocus");
    }

    public String getType() {
        return isMultiSelect() ? TYPE_SELECT_MULTIPLE : TYPE_SELECT_ONE;
    }


    private boolean isMultiSelect() {
        return (getMultiple() && getSize() > 1);
    }

    public int getLength() {
        return getOptions().getLength();
    }


    public boolean getMultiple() {
        return getBooleanAttribute( "multiple" );
    }


    public HTMLCollection getOptions() {
        return HTMLCollectionImpl.createHTMLCollectionImpl( getElementsByTagName( getHtmlDocument().toNodeCase( "option" ) ) );
    }


    public int getSelectedIndex() {
        HTMLCollection options = getOptions();
        for (int i = 0; i < options.getLength(); i++) {
            if (((HTMLOptionElement)options.item(i)).getSelected()) return i;
        }
        return isMultiSelect() ? -1 : 0;
    }


    public String getValue() {
        HTMLCollection options = getOptions();
        for (int i = 0; i < options.getLength(); i++) {
            HTMLOptionElement optionElement = ((HTMLOptionElement)options.item(i));
            if (optionElement.getSelected()) return optionElement.getValue();
        }
        return (isMultiSelect() || options.getLength() == 0) ? null : ((HTMLOptionElement)options.item(0)).getValue();
    }


    public int getSize() {
        return getIntegerAttribute( "size" );
    }


    public void remove( int index ) {
    }


    public void setMultiple( boolean multiple ) {
        setAttribute( "multiple", multiple );
    }


    public void setSelectedIndex( int selectedIndex ) {
        HTMLCollection options = getOptions();
        for (int i = 0; i < options.getLength(); i++) {
            HTMLOptionElementImpl optionElement = (HTMLOptionElementImpl) options.item(i);
            optionElement.setSelected( i == selectedIndex );
        }
    }


    public void setSize( int size ) {
        setAttribute( "size", size );
    }


    int getIndexOf( HTMLOptionElementImpl option ) {
        HTMLCollection options = getOptions();
        for (int i = 0; i < options.getLength(); i++) {
            if (options.item(i) == option) return i;
        }
        throw new IllegalStateException( "option is not part of this select" );
    }


    void clearSelected() {
        setSelectedIndex( -1 );
    }


    void addValues( ParameterProcessor processor, String characterSet ) throws IOException {
        HTMLCollection options = getOptions();
        String name = getName();
        for (int i = 0; i < options.getLength();i++) {
            ((HTMLOptionElementImpl) options.item( i )).addValueIfSelected( processor, name, characterSet );
        }
    }


    public void setValue( String value ) {
        setAttribute( "value", value );
    }


    public void reset() {
        HTMLCollection options = getOptions();
        for (int i = 0; i < options.getLength(); i++) {
            HTMLControl optionElement = (HTMLControl) options.item(i);
            optionElement.reset();
        }
    }
}

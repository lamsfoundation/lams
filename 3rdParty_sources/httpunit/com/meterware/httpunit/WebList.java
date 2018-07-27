package com.meterware.httpunit;
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

import java.net.URL;
import java.util.ArrayList;

import com.meterware.httpunit.scripting.ScriptableDelegate;

/**
 * Represents an HTML list.  Experimental.
 *
 * @author <a href="mailto:russgold@httpunit.org">Russell Gold</a>
 * @since 1.6
 **/
public class WebList extends HTMLElementBase {

    /** Indicator of an ordered list (HTML tag &lt;ol&gt;) */
    public static final int ORDERED_LIST = 1;

    /** Indicator of a bullet list (HTML tag &lt;ul&gt;) */
    public static final int BULLET_LIST = 2;

    private WebResponse _response;
    private FrameSelector _frame;
    private URL _baseURL;
    private String _baseTarget;
    private String _characterSet;

    private ArrayList _items = new ArrayList();
    private int _listType;


    public WebList( WebResponse response, FrameSelector frame, URL baseURL, String baseTarget, Element element, String characterSet ) {
        super( element );
        if (element.getNodeName().equalsIgnoreCase( "ol" )) {
            _listType = ORDERED_LIST;
        } else if (element.getNodeName().equalsIgnoreCase( "ul" )) {
            _listType = BULLET_LIST;
        }
        _response = response;
        _frame = frame;
        _baseURL = baseURL;
        _baseTarget = baseTarget;
        _characterSet = characterSet;
    }


    public int getListType() {
        return _listType;
    }


    public TextBlock[] getItems() {
        return (TextBlock[]) _items.toArray( new TextBlock[ _items.size() ] );
    }


    public ScriptableDelegate newScriptable() {
        return new HTMLElementScriptable( this );
    }


    public ScriptableDelegate getParentDelegate() {
        return _response.getDocumentScriptable();
    }


    TextBlock addNewItem( Element element ) {
        TextBlock listItem = new TextBlock( _response, _frame, _baseURL, _baseTarget, element, _characterSet );
        _items.add( listItem );
        return listItem;
    }
}

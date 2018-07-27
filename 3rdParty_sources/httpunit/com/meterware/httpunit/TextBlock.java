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
import org.w3c.dom.Node;

import java.net.URL;
import java.util.ArrayList;

/**
 * A class which represents a block of text in a web page. Experimental.
 *
 * @author <a href="mailto:russgold@httpunit.org">Russell Gold</a>
 * @since 1.6
 **/
public class TextBlock extends BlockElement {

    private ArrayList _lists = new ArrayList();
    /** Predicate to match part or all of a block's class attribute. **/
    public final static HTMLElementPredicate MATCH_CLASS;
    /** Predicate to match the tag associated with a block (case insensitive). **/
    public final static HTMLElementPredicate MATCH_TAG;


    public TextBlock( WebResponse response, FrameSelector frame, URL baseURL, String baseTarget, Node rootNode, String characterSet ) {
        super( response, frame, baseURL, baseTarget, rootNode, characterSet );
    }


    /**
     * Returns any lists embedded in this text block.
     */
    public WebList[] getLists() {
        return (WebList[]) (_lists.toArray( new WebList[ _lists.size() ] ) );
    }


    void addList( WebList webList ) {
        _lists.add( webList );
    }


    String[] getFormats( int characterPosition ) {
        return null;
    }


    static {
        MATCH_CLASS = new HTMLElementPredicate() {
            public boolean matchesCriteria( Object htmlElement, Object criteria ) {
                if (criteria == null) criteria = "";
                return ((BlockElement) htmlElement).getClassName().equalsIgnoreCase( criteria.toString() );
            };
        };


        MATCH_TAG = new HTMLElementPredicate() {
            public boolean matchesCriteria( Object htmlElement, Object criteria ) {
                if (criteria == null) criteria = "";
                return criteria.toString().equalsIgnoreCase( ((BlockElement) htmlElement).getTagName() );
            };
        };
    }
}

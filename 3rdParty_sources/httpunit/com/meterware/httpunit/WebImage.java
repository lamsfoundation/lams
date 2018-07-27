package com.meterware.httpunit;
/********************************************************************************************************************
 *
 *
 * Copyright (c) 2002-2006, Russell Gold
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
import com.meterware.httpunit.scripting.NamedDelegate;
import com.meterware.httpunit.scripting.ScriptableDelegate;

import java.net.URL;

import org.w3c.dom.Node;
import org.w3c.dom.html.HTMLImageElement;


/**
 * Represents an image in an HTML document.
 *
 * @author <a href="mailto:russgold@httpunit.org">Russell Gold</a>
 **/
public class WebImage extends FixedURLWebRequestSource {

    private HTMLImageElement _element;
    private ParsedHTML       _parsedHTML;


    WebImage( WebResponse response, ParsedHTML parsedHTML, URL baseURL, HTMLImageElement element, FrameSelector sourceFrame, String defaultTarget, String characterSet ) {
        super( response, element, baseURL, "src", sourceFrame, defaultTarget, characterSet );
        _element = element;
        _parsedHTML = parsedHTML;
    }


    public String getName() {
        return _element.getName();
    }


    public String getSource() {
        return _element.getSrc();
    }


    public String getAltText() {
        return _element.getAlt();
    }


    public WebLink getLink() {
        return _parsedHTML.getFirstMatchingLink( new HTMLElementPredicate() {

            public boolean matchesCriteria( Object link, Object parentNode ) {
                for (Node parent = (Node) parentNode; parent != null; parent = parent.getParentNode()) {
                    if (parent.equals( ((WebLink) link).getElement() )) return true;
                }
                return false;
            }
        }, _element.getParentNode() );
    }



    public class Scriptable extends HTMLElementScriptable implements NamedDelegate {

        public Scriptable() {
            super( WebImage.this );
        }


        public String getName() {
            return WebImage.this.getID().length() != 0 ? WebImage.this.getID() : WebImage.this.getName();
        }


        public Object get( String propertyName ) {
            if (propertyName.equalsIgnoreCase( "src" )) {
                return getSource();
            } else if(propertyName.equalsIgnoreCase( "name" )) {
                return getName();
            } else {
               return super.get( propertyName );
            }
        }


        public void set( String propertyName, Object value ) {
            if (propertyName.equalsIgnoreCase( "src" )) {
                if (value != null) _element.setSrc( value.toString() );
            } else {
                super.set( propertyName, value );
            }
        }
    }

//---------------------------------- WebRequestSource methods ------------------------------------------

    public ScriptableDelegate newScriptable() {
        return new Scriptable();
    }

}

package com.meterware.httpunit;
/********************************************************************************************************************

*
* Copyright (c) 2000-2007, Russell Gold
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
import com.meterware.httpunit.scripting.ScriptingHandler;
import com.meterware.httpunit.parsing.HTMLParserFactory;
import com.meterware.httpunit.parsing.DocumentAdapter;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.w3c.dom.html.HTMLDocument;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.net.URL;
import java.util.Vector;


/**
 * This class represents an HTML page returned from a request.
 *
 * @author <a href="mailto:russgold@httpunit.org">Russell Gold</a>
 * @author <a href="mailto:bx@bigfoot.com">Benoit Xhenseval</a>
 **/
public class HTMLPage extends ParsedHTML {

    private Scriptable _scriptable;


    HTMLPage( WebResponse response, FrameSelector frame, URL baseURL, String baseTarget, String characterSet ) {
        super( response, frame, baseURL, baseTarget, null, characterSet );
    }


    /**
     * Returns the title of the page.
     **/
    public String getTitle() throws SAXException {
        NodeList nl = ((Document) getOriginalDOM()).getElementsByTagName( "title" );
        if (nl.getLength() == 0) return "";
        if (!nl.item(0).hasChildNodes()) return "";
        return nl.item(0).getFirstChild().getNodeValue();
    }


    /**
     * Returns the onLoad event script.
     */
    public String getOnLoadEvent() throws SAXException {
        Element mainElement = getMainElement( ((Document) getOriginalDOM()) );
        return mainElement == null ? "" : mainElement.getAttribute( "onload" );
    }


    private Element getMainElement( Document document ) {
        NodeList nl = document.getElementsByTagName( "frameset" );
        if (nl.getLength() == 0) nl = document.getElementsByTagName( "body" );
        return nl.getLength() == 0 ? null : ((Element) nl.item(0));
    }


    /**
     * Returns the location of the linked stylesheet in the head
     * <code>
     * <link type="text/css" rel="stylesheet" href="/mystyle.css" />
     * </code>
     **/
    public String getExternalStyleSheet() throws SAXException {
        NodeList nl = ((Document) getOriginalDOM()).getElementsByTagName( "link" );
        int length = nl.getLength();
        if (length == 0) return "";

        for (int i = 0; i < length; i++) {
            if ("stylesheet".equalsIgnoreCase(NodeUtils.getNodeAttribute( nl.item(i), "rel" )))
                return NodeUtils.getNodeAttribute( nl.item(i), "href" );
        }
        return "";
    }


    /**
     * Retrieves the "content" of the meta tags for a key pair attribute-attributeValue.
     * <code>
     *  <meta name="robots" content="index" />
     *  <meta name="robots" content="follow" />
     *  <meta http-equiv="Expires" content="now" />
     * </code>
     * this can be used like this
     * <code>
     *      getMetaTagContent("name","robots") will return { "index","follow" }
     *      getMetaTagContent("http-equiv","Expires") will return { "now" }
     * </code>
     **/
    public String[] getMetaTagContent(String attribute, String attributeValue) {
        Vector matches = new Vector();
        NodeList nl = ((Document) getOriginalDOM()).getElementsByTagName("meta");
        int length = nl.getLength();

        for (int i = 0; i < length; i++) {
            if (attributeValue.equalsIgnoreCase(NodeUtils.getNodeAttribute(nl.item(i), attribute))) {
                matches.addElement( NodeUtils.getNodeAttribute( nl.item(i), "content" ) );
            }
        }
        String[] result = new String[ matches.size() ];
        matches.copyInto( result );
        return result;
    }

    /**
     * scriptable for HTML Page
     */

    public class Scriptable extends ScriptableDelegate {

    	  /**
    	   * get the Object with the given propertyName
    	   * @param propertyName - the name of the property
    	   */
        public Object get( String propertyName ) {
            NamedDelegate delegate = getNamedItem( getForms(), propertyName );
            if (delegate != null) return delegate;

            delegate = getNamedItem( getLinks(), propertyName );
            if (delegate != null) return delegate;

            delegate = getNamedItem( getImages(), propertyName );
            if (delegate != null) return delegate;

            return null;
        }


        private NamedDelegate getNamedItem( ScriptingHandler[] items, String name ) {
            if (name ==  null) return null;
            for (int i = 0; i < items.length; i++) {
                if (items[i] instanceof NamedDelegate && name.equals( ((NamedDelegate) items[i]).getName() )) return (NamedDelegate) items[i];
            }
            return null;
        }


        /**
         * Sets the value of the named property. Will throw a runtime exception if the property does not exist or
         * cannot accept the specified value.
         **/
        public void set( String propertyName, Object value ) {
            if (propertyName.equalsIgnoreCase( "location" )) {
                getResponse().getScriptableObject().set( "location", value );
            } else {
                super.set( propertyName, value );
             }
        }


        public WebResponse.Scriptable getParent() {
            return getResponse().getScriptableObject();
        }


        public String getTitle() throws SAXException {
            return HTMLPage.this.getTitle();
        }


        public ScriptingHandler[] getLinks() {
            WebLink[] links = HTMLPage.this.getLinks();
            ScriptingHandler[] result = new WebLink.Scriptable[ links.length ];
            for (int i = 0; i < links.length; i++) {
                result[i] = links[i].getScriptingHandler();
            }
            return result;
        }


        public ScriptingHandler[] getForms() {
            WebForm[] forms = HTMLPage.this.getForms();
            ScriptingHandler[] result = new ScriptingHandler[ forms.length ];
            for (int i = 0; i < forms.length; i++) {
                result[i] = forms[i].getScriptingHandler();
            }
            return result;
        }


        public ScriptingHandler[] getImages() {
            WebImage[] images = HTMLPage.this.getImages();
            ScriptingHandler[] result = new WebImage.Scriptable[ images.length ];
            for (int i = 0; i < images.length; i++) {
                result[i] = images[i].getScriptingHandler();
            }
            return result;
        }


        Scriptable() {}


        public boolean replaceText( String text, String contentType ) {
            return getResponse().replaceText( text, contentType );
        }


        public void setCookie( String name, String value ) {
            getResponse().setCookie( name, value );
        }


        public String getCookie() {
            return emptyIfNull( getResponse().getCookieHeader() );
        }


        private String emptyIfNull( String string ) {
            return string == null ? "" : string;
        }


        public ScriptableDelegate getElementWithID( String id ) {
            final HTMLElement elementWithID = HTMLPage.this.getElementWithID( id );
            return elementWithID == null ? null : (ScriptableDelegate) elementWithID.getScriptingHandler();
        }


        public ScriptableDelegate[] getElementsByName( String name ) {
            return getDelegates( HTMLPage.this.getElementsWithName( name ) );
        }


        public ScriptableDelegate[] getElementsByTagName( String name ) {
            return getDelegates( HTMLPage.this.getElementsByTagName( HTMLPage.this.getRootNode(), name ) );
        }
    }


    Scriptable getScriptableObject() {
        if (_scriptable == null) {
            _scriptable = new Scriptable();
            _scriptable.setScriptEngine( getResponse().getScriptableObject().getScriptEngine( _scriptable ) );
        }
        return _scriptable;
    }


    /**
     * parse the given test with the given URL
     * @param text
     * @param pageURL
     * @throws SAXException
     * @throws IOException
     */
    public void parse( String text, URL pageURL ) throws SAXException, IOException {
        HTMLParserFactory.getHTMLParser().parse( pageURL, text, new DocumentAdapter() {
            public void setDocument(HTMLDocument document ) { HTMLPage.this.setRootNode( document ); }
            public String getIncludedScript( String srcAttribute ) throws IOException { return HTMLPage.this.getIncludedScript( srcAttribute ); }
            public ScriptingHandler getScriptingHandler() { return getResponse().getScriptingHandler(); }
        });
    }


}
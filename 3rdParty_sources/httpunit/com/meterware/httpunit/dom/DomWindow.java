package com.meterware.httpunit.dom;
/********************************************************************************************************************

*
* Copyright (c) 2007, Russell Gold
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
import java.io.IOException;
import java.net.URL;

import org.xml.sax.SAXException;
import org.mozilla.javascript.Scriptable;
import org.w3c.dom.html.HTMLDocument;

import com.meterware.httpunit.scripting.ScriptingHandler;

/**
 * @author <a href="mailto:russgold@httpunit.org">Russell Gold</a>
 */
public class DomWindow extends AbstractDomComponent implements Scriptable {

    private DomWindowProxy _proxy;
    private HTMLDocumentImpl _document;

    /**
     * construct me from a document
     * @param document
     */
    public DomWindow( HTMLDocumentImpl document ) {
        _document = document;
    }


    public DomWindow( DomWindowProxy implementation ) {
        _proxy = implementation;
    }


    public void setProxy( DomWindowProxy proxy ) {
        _proxy = proxy;
    }


    public DomWindow getWindow() {
        return this;
    }


    public DomWindow getSelf() {
        return this;
    }


    public HTMLDocument getDocument() {
        return _document;
    }


    /**
     * Returns the document associated with this window. Uses the same name as that used by elements in the DOM.
     */
    public HTMLDocument getOwnerDocument() {
        return _document;
    }


    /**
     * Opens a named window.
     * @param urlString the location (relative to the current page) from which to populate the window.
     * @param name      the name of the window.
     * @param features  special features for the window.
     * @param replace   if true, replaces the contents of an existing window.
     * @return a new populated window object
     */
    public DomWindow open( String urlString, String name, String features, boolean replace ) {
        try {
        	if (_proxy==null) {
        		throw new RuntimeException("DomWindow.open failed for '"+name+"' _proxy is null");
        	}
        	
        	DomWindowProxy newWindow=_proxy.openNewWindow( name, urlString );
        	if (newWindow==null) {
        		// throw new RuntimeException("DomWindow.open failed for '"+name+"','"+urlString+"' openNewWindow returned null");
        		return null;
        	}	
        	ScriptingHandler result=newWindow.getScriptingHandler();
          return (DomWindow) result;
        } catch (IOException e) {
            return null;
        } catch (SAXException e) {
            return null;
        }
    }


    /**
     * Closes the current window. Has no effect if this "window" is actually a nested frame.
     */
    public void close() {
        _proxy.close();
    }


    /**
     * Displays an alert box with the specified message.
     * @param message the message to display
     */
    public void alert( String message ) {
        _proxy.alert( message );
    }


    /**
     * Displays a prompt, asking for a yes or no answer and returns the answer.
     * @param prompt the prompt text to display
     * @return true if the user answered 'yes'
     */
    public boolean confirm( String prompt ) {
        return _proxy.confirm( prompt );
    }


    /**
     * Displays a promptand returns the user's textual reply, which could be the default reply.
     * @param message the prompt text to display
     * @param defaultResponse the response to return if the user doesn't enter anything
     * @return the reply selected by the user.
     */
    public String prompt( String message, String defaultResponse ) {
        return _proxy.prompt( message, defaultResponse );
    }


    public void setTimeout( int timeout ) {
    }


    public void focus() {
    }


    public void moveTo( int x, int y ) {
    }


    protected String getDocumentWriteBuffer() {
        return _document.getWriteBuffer().toString();
    }


    protected void discardDocumentWriteBuffer() {
        _document.clearWriteBuffer();
    }


    boolean replaceText( String string, String mimeType ) {
        return _proxy.replaceText( string, mimeType );
    }


    URL getUrl() {
        return _proxy.getURL();
    }


    void submitRequest( HTMLElementImpl sourceElement, String method, String location, String target, byte[] requestBody ) throws IOException, SAXException {
        _proxy.submitRequest( sourceElement, method, location, target, null );
    }
}

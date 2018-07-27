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
import com.meterware.httpunit.scripting.ScriptingHandler;
import com.meterware.httpunit.protocol.MessageBody;

import java.io.IOException;
import java.net.URL;

import org.xml.sax.SAXException;

/**
 * @author <a href="mailto:russgold@httpunit.org">Russell Gold</a>
 */
public interface DomWindowProxy {

    DomWindowProxy openNewWindow( String name, String relativeUrl ) throws IOException, SAXException;


    ScriptingHandler getScriptingHandler();


    void close();


    void alert( String message );


    boolean confirm( String message );


    String prompt( String prompt, String defaultResponse );


    /**
     * Returns the URL associated with the window.
     * @return the URL associated with the window.
     */
    URL getURL();


    /**
     * Replaces the text in the window with the specified text and content type. Returns false if unable
     * to do the replacement.
     */
    boolean replaceText( String text, String contentType );


    DomWindowProxy submitRequest( HTMLElementImpl sourceElement, String method, String location, String target, MessageBody requestBody ) throws IOException, SAXException;
}

package com.meterware.httpunit;
/********************************************************************************************************************

*
* Copyright (c) 2000-2004, Russell Gold
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
import com.meterware.httpunit.scripting.ScriptableDelegate;

import java.net.URL;

import org.w3c.dom.Node;

/**
 * A frame in a web page.
 **/
class WebFrame extends HTMLElementBase {

    private FrameSelector _selector;

    private WebResponse _response;
    private Node        _element;

    private URL         _baseURL;


    public ScriptableDelegate getParentDelegate() {
        return _response.getDocumentScriptable();
    }


//---------------------------------------- package methods -----------------------------------------


    WebFrame( WebResponse response, URL baseURL, Node frameNode, FrameSelector parentFrame ) {
        super( frameNode );
        _response = response;
        _element = frameNode;
        _baseURL = baseURL;
        _selector = getFrameSelector( parentFrame );
    }


    String getFrameName() {
        return _selector.getName();
    }


    FrameSelector getSelector() {
        return _selector;
    }


    private FrameSelector getFrameSelector( FrameSelector parentFrame ) {
        return FrameHolder.newNestedFrame( parentFrame, super.getName() );
    }


    WebRequest getInitialRequest() {
        return new GetMethodWebRequest( _baseURL,
                                        HttpUnitUtils.trimFragment( NodeUtils.getNodeAttribute( _element, "src" ) ),
                                        _selector );
    }


    boolean hasInitialRequest() {
        return NodeUtils.getNodeAttribute( _element, "src" ).length() > 0;
    }

}


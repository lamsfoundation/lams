package com.meterware.servletunit;
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
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.util.ArrayList;
import java.util.Hashtable;

/**
 *
 * @author <a href="mailto:russgold@httpunit.org">Russell Gold</a>
 **/
abstract class WebResourceConfiguration {

    private String _className;
    private Hashtable _initParams = new Hashtable();


    WebResourceConfiguration( String className ) {
        _className = className;
    }


    WebResourceConfiguration( String className, Hashtable initParams ) {
        _className = className;
        if (initParams != null) _initParams = initParams;
    }


    WebResourceConfiguration( Element resourceElement, String resourceNodeName ) throws SAXException {
        this( XMLUtils.getChildNodeValue( resourceElement, resourceNodeName ) );
        final NodeList initParams = resourceElement.getElementsByTagName( "init-param" );
        for (int i = initParams.getLength() - 1; i >= 0; i--) {
            _initParams.put( XMLUtils.getChildNodeValue( (Element) initParams.item( i ), "param-name" ),
                             XMLUtils.getChildNodeValue( (Element) initParams.item( i ), "param-value" ) );
        }
    }


    abstract void destroyResource();


    String getClassName() {
        return _className;
    }


    Hashtable getInitParams() {
        return _initParams;
    }


    abstract boolean isLoadOnStartup();

}

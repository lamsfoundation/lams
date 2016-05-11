package com.meterware.httpunit.dom;
/********************************************************************************************************************
 *
 *
 * Copyright (c) 2006-2008, Russell Gold
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


/**
 *
 * @author <a href="mailto:russgold@httpunit.org">Russell Gold</a>
 **/

public abstract class NamespaceAwareNodeImpl extends NodeImpl {

    private String _tagName;
    private String _localName;
    private String _namespaceUri;


    protected void initialize( DocumentImpl owner, String tagName ) {
        initialize( owner );
        _localName = _tagName = tagName;
    }


    /**
     * initialize the name space
     * @param owner
     * @param namespaceURI
     * @param qualifiedName
     */
    protected void initialize( DocumentImpl owner, String namespaceURI, String qualifiedName ) {
        initialize( owner );
        _tagName = qualifiedName;
        _namespaceUri = namespaceURI;
        if (qualifiedName.indexOf(':') < 0) {
            _localName = qualifiedName;
        } else {
            _localName = qualifiedName.substring( qualifiedName.indexOf(':') + 1 );
        }
        setParentScope(owner); 
    }

    public String getNodeName() {
        return getTagName();
    }


    public String getTagName() {
        return _tagName;
    }


    public String getNamespaceURI() {
        return _namespaceUri;
    }


    public String getLocalName() {
        return _localName;
    }
}

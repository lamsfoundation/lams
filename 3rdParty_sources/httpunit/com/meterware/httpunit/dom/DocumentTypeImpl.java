package com.meterware.httpunit.dom;
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

import org.w3c.dom.DocumentType;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.DOMException;

import java.util.ArrayList;

/**
 *
 * @author <a href="mailto:russgold@httpunit.org">Russell Gold</a>
 **/
public class DocumentTypeImpl extends NodeImpl implements DocumentType {

//---------------------------------------------- DocumentType methods --------------------------------------------------

    public NamedNodeMap getEntities() {
        return null;
    }


    public String getInternalSubset() {
        return null;
    }


    public String getName() {
        return null;
    }


    public NamedNodeMap getNotations() {
        return null;
    }


    public String getPublicId() {
        return null;
    }


    public String getSystemId() {
        return null;
    }

//------------------------------------------------ NodeImpl methods ----------------------------------------------------


    public String getNodeName() {
        return null;
    }


    public short getNodeType() {
        return 0;
    }


    public String getNodeValue() throws DOMException {
        return null;
    }


    public void setNodeValue( String nodeValue ) throws DOMException {
    }
}

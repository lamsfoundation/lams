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

import org.w3c.dom.CharacterData;
import org.w3c.dom.DOMException;
import org.w3c.dom.Node;

import java.util.ArrayList;

/**
 *
 * @author <a href="mailto:russgold@httpunit.org">Russell Gold</a>
 **/
abstract public class CharacterDataImpl extends NodeImpl implements CharacterData {

    private String _data;


    protected void initialize( DocumentImpl ownerDocument, String data ) {
        super.initialize( ownerDocument );
        _data = data;
    }


    public String getData() throws DOMException {
        return _data;
    }


    public void setData( String data ) throws DOMException {
        if (data == null) data = "";
        _data = data;
    }


    public int getLength() {
        return _data.length();
    }


    public String substringData( int offset, int count ) throws DOMException {
        return null;
    }


    public void appendData( String arg ) throws DOMException {
    }


    public void insertData( int offset, String arg ) throws DOMException {
    }


    public void deleteData( int offset, int count ) throws DOMException {
    }


    public void replaceData( int offset, int count, String arg ) throws DOMException {
    }

}

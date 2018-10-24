package com.meterware.httpunit.dom;
/********************************************************************************************************************
 *
 *
 * Copyright (c) 2006, Russell Gold
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

import org.w3c.dom.ProcessingInstruction;
import org.w3c.dom.DOMException;
import org.w3c.dom.Node;

/**
 *
 * @author <a href="mailto:russgold@httpunit.org">Russell Gold</a>
 **/
public class ProcessingInstructionImpl extends NodeImpl implements ProcessingInstruction {

    private String _target;
    private String _data;


    public static ProcessingInstruction createProcessingImpl( DocumentImpl ownerDocument, String target, String data ) {
        ProcessingInstructionImpl instruction = new ProcessingInstructionImpl();
        instruction.initialize( ownerDocument, target, data );
        return instruction;
    }


    private void initialize( DocumentImpl ownerDocument, String target, String data ) {
        super.initialize( ownerDocument );
        _target = target;
        _data = data;
    }


    public static Node importNode( DocumentImpl document, ProcessingInstruction processingInstruction ) {
        return createProcessingImpl( document, processingInstruction.getTarget(), processingInstruction.getData() );
    }


    public String getNodeName() {
        return _target;
    }


    public String getNodeValue() throws DOMException {
        return _data;
    }


    public void setNodeValue( String string ) throws DOMException {
        setData( string );
    }


    public short getNodeType() {
        return PROCESSING_INSTRUCTION_NODE;
    }


    public String getTarget() {
        return _target;
    }


    public String getData() {
        return _data;
    }


    public void setData( String string ) throws DOMException {
        _data = string;
    }
}

package com.meterware.httpunit;
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

/**
 * An immutable class which describes the position of a frame in the window hierarchy.
 *
 * @author <a href="mailto:russgold@httpunit.org">Russell Gold</a>
 *
 * @since 1.6
 **/
public class FrameSelector {

    public static FrameSelector TOP_FRAME = new FrameSelector( WebRequest.TOP_FRAME );
    static FrameSelector NEW_FRAME = new FrameSelector( WebRequest.TOP_FRAME );

    private String _name;
    private WebWindow _window;
    private FrameSelector _parent;


    FrameSelector() {
        _name = super.toString();
    }


    FrameSelector( String name ) {
        _name = name;
    }


    FrameSelector( String name, FrameSelector parent ) {
        _name = name;
        _parent = parent;
    }


    String getName() {
        return _name;
    }


    FrameSelector getParent() {
        return _parent;
    }


    public String toString() {
        return "Frame Selector: [ " + getFullName() + " ]";
    }


    private String getFullName() {
        return _name + (_parent == null ? "" : " in " + _parent.getFullName() );
    }


    WebWindow getWindow() {
        return _window != null ? _window
                               : (_parent == null ? null : _parent.getWindow());
    }


    static FrameSelector newTopFrame( WebWindow window ) {
        return new FrameSelector( WebRequest.TOP_FRAME, window );
    }


    private FrameSelector( String name, WebWindow window ) {
        _name = name;
        _window = window;
    }


}



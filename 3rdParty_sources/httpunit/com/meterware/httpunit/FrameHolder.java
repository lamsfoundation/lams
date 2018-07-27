package com.meterware.httpunit;
/********************************************************************************************************************
 *
 *
 * Copyright (c) 2002-2004, Russell Gold
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
import java.net.MalformedURLException;
import java.util.*;

import org.xml.sax.SAXException;


/**
 *
 * @author <a href="mailto:russgold@httpunit.org">Russell Gold</a>
 **/
class FrameHolder {

    /** Map from a frame selector to its corresponding web response. **/
    private Hashtable   _contents = new Hashtable();

    /** Map from a frame selector to its subframe selectors. **/
    private Hashtable   _subframes = new Hashtable();

    /** The window which owns this frame holder. **/
    private WebWindow _window;

    /** The topmost frame in this frameholder. **/
    private FrameSelector _topFrame;


    FrameHolder( WebWindow window ) {
        _window = window;
        _topFrame = FrameSelector.newTopFrame( window );
        DefaultWebResponse blankResponse = new DefaultWebResponse( window.getClient(), null, WebResponse.BLANK_HTML );
        _contents.put( _topFrame, blankResponse );
        HttpUnitOptions.getScriptingEngine().associate( blankResponse );
    }


    FrameSelector getTopFrame() {
        return _topFrame;
    }


    WebResponse getFrameContents( FrameSelector targetFrame ) {
        if (targetFrame == FrameSelector.TOP_FRAME) targetFrame = getTopFrame();
        WebResponse response = get( targetFrame );
        if (response == null) throw new NoSuchFrameException( targetFrame.getName() );
        return response;
    }


    WebResponse getSubframeContents( FrameSelector frame, String subFrameName ) {
        FrameSelector[] subframes = (FrameSelector[]) _subframes.get( frame );
        if (subframes == null) throw new NoSuchFrameException( subFrameName );

        for (int i = 0; i < subframes.length; i++) {
            FrameSelector subframe = subframes[i];
            if (subframe.getName().equalsIgnoreCase( subFrameName ) ) return get( subframe );
        }
        throw new NoSuchFrameException( subFrameName );
    }


    WebResponse getParentFrameContents( FrameSelector frame ) {
        return get( frame.getParent() == null ? _topFrame : frame.getParent() );
    }


    WebResponse get( FrameSelector targetFrame ) {
        return (WebResponse) _contents.get( targetFrame );
    }


    WebResponse get( String target ) {
        FrameSelector frame = getFrame( _topFrame, target );
        return frame == null ? null : (WebResponse) _contents.get( frame );
    }


    FrameSelector getFrame( String target ) {
        return target.equals( _window.getName() ) ? _topFrame : getFrame( _topFrame, target );
    }


    private FrameSelector getFrame( FrameSelector rootFrame, String target ) {
        if (target.equalsIgnoreCase( WebRequest.TOP_FRAME )) return _topFrame;
        if (target.equalsIgnoreCase( rootFrame.getName() )) return rootFrame;

        return lookupFrame( rootFrame, target );
    }


    private FrameSelector lookupFrame( FrameSelector rootFrame, String target ) {
        FrameSelector result = getFromSubframe( rootFrame, target );
        if (result != null) {
            return result;
        } else if (rootFrame.getName().equals( target )) {
            return rootFrame;
        } else if (rootFrame.getParent() != null) {
            return lookupFrame( rootFrame.getParent(), target );
        } else {
            return null;
        }
    }


    private FrameSelector getFromSubframe( FrameSelector rootFrame, String target ) {
        FrameSelector[] subframes = (FrameSelector[]) _subframes.get( rootFrame );
        if (subframes == null) return null;

        for (int i = 0; i < subframes.length; i++) {
            if (subframes[i].getName().equalsIgnoreCase( target )) return subframes[i];
        }
        for (int i = 0; i < subframes.length; i++) {
            FrameSelector result = getFromSubframe( subframes[i], target );
            if (result != null) return result;
        }
        return null;
    }


    List getActiveFrameNames() {
        List result = new ArrayList();
        for (Enumeration e = _contents.keys(); e.hasMoreElements();) {
            result.add( ((FrameSelector) e.nextElement()).getName() );
        }

        return result;
    }


    /**
     * Determines the frame in which the reply to a request will be stored.
     */
    FrameSelector getTargetFrame( WebRequest request ) {
        if (WebRequest.NEW_WINDOW.equalsIgnoreCase( request.getTarget() )) {
            return FrameSelector.NEW_FRAME;
        } else if (WebRequest.TOP_FRAME.equalsIgnoreCase( request.getTarget() )) {
            return _topFrame;
        } else if (WebRequest.SAME_FRAME.equalsIgnoreCase( request.getTarget() )) {
            return request.getSourceFrame();
        } else if (WebRequest.PARENT_FRAME.equalsIgnoreCase( request.getTarget() )) {
            return request.getSourceFrame().getParent() == null ? _topFrame : request.getSourceFrame().getParent();
        } else if (request.getSourceFrame().getName().equalsIgnoreCase( request.getTarget() )) {
            return request.getSourceFrame();
        } else {
            FrameSelector targetFrame = getFrame( request.getSourceFrame(), request.getTarget() );
            if (targetFrame == null) targetFrame =_window.getClient().findFrame( request.getTarget() );
            return (targetFrame != null) ? targetFrame : FrameSelector.NEW_FRAME;
        }
    }


    void updateFrames( WebResponse response, FrameSelector frame, RequestContext requestContext ) throws MalformedURLException, IOException, SAXException {
        removeSubFrames( frame );
        _contents.put( frame, response );

        if (response.isHTML()) {
            HttpUnitOptions.getScriptingEngine().associate( response );
            requestContext.addNewResponse( response );
            WebRequest[] requests = response.getFrameRequests();
            if (requests.length > 0) {
                createSubFrames( frame, response.getFrameSelectors() );
                for (int i = 0; i < requests.length; i++) {
                    if (requests[i].getURLString().length() != 0) {
                        response.getWindow().getSubframeResponse( requests[i], requestContext );
                    }
                }
            }
        }
    }


    private void removeSubFrames( FrameSelector frame ) {
        FrameSelector[] subframes = (FrameSelector[]) _subframes.get( frame );
        if (subframes == null) return;

        _subframes.remove( frame );
        for (int i = 0; i < subframes.length; i++) {
            removeSubFrames( subframes[ i ] );
            _contents.remove( subframes[ i ] );
        }
    }


    private void createSubFrames( FrameSelector frame, FrameSelector[] subframes ) {
        _subframes.put( frame, subframes );
        for (int i = 0; i < subframes.length; i++) {
            _contents.put( subframes[ i ], WebResponse.createBlankResponse() );
        }
    }


    /**
     * Given the qualified name of a frame and the name of a nested frame, returns the qualified name of the nested frame.
     */
    static FrameSelector newNestedFrame( FrameSelector parentFrame, final String relativeName ) {
        if (relativeName == null || relativeName.length() == 0) return new FrameSelector();
        return new FrameSelector( relativeName, parentFrame );
    }


}


package com.meterware.httpunit;
/********************************************************************************************************************
 *
 *
 * Copyright (c) 2002-2008, Russell Gold
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
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.xml.sax.SAXException;
import com.meterware.httpunit.scripting.ScriptingHandler;

/**
 * A window managed by a {@link com.meterware.httpunit.WebClient WebClient}.
 *
 * @author <a href="mailto:russgold@httpunit.org">Russell Gold</a>
 **/
public class WebWindow {

    /** The client which created this window. **/
    private WebClient _client;

    /** A map of frame names to current contents. **/
    private FrameHolder _frameContents;

    /** The name of the window, set via JavaScript. **/
    private String _name = "";

    /** The web response containing the reference that opened this window **/
    private WebResponse _opener;

    /** True if this window has been closed. **/
    private boolean _closed;


    static final String NO_NAME = "$$HttpUnit_Window$$_";
    
    /**
     * The urls that have been encountered as redirect locations in the course 
     * of a single client-initiated request
     * @since patch [ 1155415 ] Handle redirect instructions which can lead to a loop
     */
    private final Set _redirects;
    
    /** True if seen initial request 
     * @since patch [ 1155415 ] Handle redirect instructions which can lead to a loop
     */
    private boolean _isInitialRequest = true;
    
    /** 
     * Cache the initial client request to ensure that the _redirects 
     * structure gets reset. 
     * @since patch [ 1155415 ] Handle redirect instructions which can lead to a loop
     */
    private WebRequest _initialRequest;
        

    /**
     * Returns the web client associated with this window.
     */
    public WebClient getClient() {
        return _client;
    }


    /**
     * Returns true if this window has been closed.
     */
    public boolean isClosed() {
        return _closed;
    }


    /**
     * Closes this window.
     */
    public void close() {
        if (!_closed) _client.close( this );
        _closed = true;
    }


    /**
     * Returns the name of this window. Windows created through normal HTML or browser commands have empty names,
     * but JavaScript can set the name. A name may be used as a target for a request.
     */
    public String getName() {
        return _name;
    }


    /**
     * Returns the web response that contained the script which opened this window.
     */
    public WebResponse getOpener() {
        return _opener;
    }


    /**
     * Submits a GET method request and returns a response.
     * @exception SAXException thrown if there is an error parsing the retrieved page
     **/
    public WebResponse getResponse( String urlString ) throws IOException, SAXException {
        return getResponse( new GetMethodWebRequest( urlString ) );
    }


    /**
     * Submits a web request and returns a response. This is an alternate name for the getResponse method.
     * @return the WebResponse or null
     **/
    public WebResponse sendRequest( WebRequest request ) throws IOException, SAXException {
        return getResponse( request );
    }


    /**
     * Submits a web request and returns a response, using all state developed so far as stored in
     * cookies as requested by the server.
     * see patch [ 1155415 ] Handle redirect instructions which can lead to a loop
     * @exception SAXException thrown if there is an error parsing the retrieved page
     * @return the WebResponse or null
     **/
    public WebResponse getResponse( WebRequest request ) throws IOException, SAXException {
			//  Need to have some sort of ExecuteAroundMethod to ensure that the 
			//  redirects data structure gets cleared down upon exit - not 
			//  straightforward, since this could be a recursive call
	    if (_isInitialRequest) {
	        _initialRequest = request;
	        _isInitialRequest = false;
	    }
	
	    WebResponse result = null;
	
	    try {
	      final RequestContext requestContext = new RequestContext();
	      final WebResponse response = getSubframeResponse( request, requestContext );
	      requestContext.runScripts();
	      result = response == null ? null : response.getWindow().getFrameContents( response.getFrame() ); // javascript might replace the response in its frame
	    } finally {
	       if (null != request && request.equals(_initialRequest)) {
	          _redirects.clear();
	          _initialRequest = null;
	          _isInitialRequest = true;
	      }
	    }
	    return result;    
		}


    /**
     * get a Response from a SubFrame
     * @param request
     * @param requestContext
     * @return the WebResponse or null
     * @throws IOException
     * @throws SAXException
     */
    WebResponse getSubframeResponse( WebRequest request, RequestContext requestContext ) throws IOException, SAXException {
        WebResponse response = getResource( request );

        return response == null ? null : updateWindow( request.getTarget(), response, requestContext );
    }


    /**
     * Updates this web client based on a received response. This includes updating
     * cookies and frames.
     **/
    WebResponse updateWindow( String requestTarget, WebResponse response, RequestContext requestContext ) throws IOException, SAXException {
        _client.updateClient( response );
        if (getClient().getClientProperties().isAutoRefresh() && response.getRefreshRequest() != null) {
        	WebRequest request=response.getRefreshRequest();
        	WebResponse result=getResponse( request );
          return result; 
        } else if (shouldFollowRedirect( response )) {
            delay( HttpUnitOptions.getRedirectDelay() );
            return getResponse( new RedirectWebRequest( response ) );
        } else {
            _client.updateFrameContents( this, requestTarget, response, requestContext );
            return response;
        }
    }


    /**
     * Returns the resource specified by the request. Does not update the window or load included framesets.
     * May return null if the resource is a JavaScript URL which would normally leave the client unchanged.
     */
    public WebResponse getResource( WebRequest request ) throws IOException {
        _client.tellListeners( request );

        WebResponse response = null;
        String urlString = request.getURLString().trim();
        FrameSelector targetFrame = _frameContents.getTargetFrame( request );
        if (urlString.startsWith( "about:" )) {
            response = new DefaultWebResponse( _client, targetFrame, null, "" );
        } else if (!HttpUnitUtils.isJavaScriptURL( urlString )) {
            response = _client.createResponse( request, targetFrame );
        } else {
            ScriptingHandler handler = request.getSourceScriptingHandler();
            if (handler == null)  handler = getCurrentPage().getScriptingHandler();
            Object result = handler.evaluateExpression( urlString );
            if (result != null) {
                response = new DefaultWebResponse( _client, targetFrame, request.getURL(), result.toString() );
            }
        }

        if (response != null) _client.tellListeners( response );
        return response;
    }


    /**
     * Returns the name of the currently active frames.
     **/
    public String[] getFrameNames() {
        final List names = _frameContents.getActiveFrameNames();
        return (String[]) names.toArray( new String[ names.size() ] );
    }


    /**
     * Returns true if the specified frame name is defined in this window.
     */
    public boolean hasFrame( String frameName ) {
        return _frameContents.get( frameName ) != null;
    }


    boolean hasFrame( FrameSelector frame ) {
        return _frameContents.get( frame ) != null;
    }


    /**
     * Returns the response associated with the specified frame name.
     * Throws a runtime exception if no matching frame is defined.
     **/
    public WebResponse getFrameContents( String frameName ) {
        WebResponse response = _frameContents.get( frameName );
        if (response == null) throw new NoSuchFrameException( frameName );
        return response;
    }


    /**
     * Returns the response associated with the specified frame target.
     * Throws a runtime exception if no matching frame is defined.
     **/
    WebResponse getFrameContents( FrameSelector targetFrame ) {
        return _frameContents.getFrameContents( targetFrame );
    }


    WebResponse getSubframeContents( FrameSelector frame, String subFrameName ) {
        return _frameContents.getSubframeContents( frame, subFrameName );
    }


    WebResponse getParentFrameContents( FrameSelector frame ) {
        return _frameContents.getParentFrameContents( frame );
    }


    /**
     * Returns the response representing the main page in this window.
     */
    public WebResponse getCurrentPage() {
        return getFrameContents( WebRequest.TOP_FRAME );
    }


    /**
     * construct a WebWindow from a given client
     * @param client - the client to construct me from
     */
    WebWindow( WebClient client ) {
        _client = client;
        _frameContents = new FrameHolder( this );
        _name = NO_NAME + _client.getOpenWindows().length;
        _redirects = new HashSet();
    }


    WebWindow( WebClient client, WebResponse opener ) {
        this( client );
        _opener = opener;
    }


    void updateFrameContents( WebResponse response, RequestContext requestContext ) throws IOException, SAXException {
        response.setWindow( this );
        _frameContents.updateFrames( response, response.getFrame(), requestContext );
    }


    void setName( String name ) {
        _name = name;
    }


    /**
     * Delays the specified amount of time.
     **/
    private void delay( int numMilliseconds ) {
        if (numMilliseconds == 0) return;
        try {
            Thread.sleep( numMilliseconds );
        } catch (InterruptedException e) {
            // ignore the exception
        }
    }

    /**
     * check whether redirect is configured
     * @param response
     * @return
     */
    private boolean redirectConfigured( WebResponse response ) {
    	boolean isAutoredirect=getClient().getClientProperties().isAutoRedirect();
    	boolean hasLocation=response.getHeaderField( "Location" ) != null;
    	int responseCode=response.getResponseCode();
    	boolean result=isAutoredirect
      	&& responseCode >= HttpURLConnection.HTTP_MOVED_PERM
      	&& responseCode <= HttpURLConnection.HTTP_MOVED_TEMP
      	&& hasLocation;
      return result;
    }

    /**
     * check wether we should follow the redirect given in the response
     * make sure we don't run into a recursion
     * @param response
     * @return
     */
    private boolean shouldFollowRedirect( WebResponse response ) {
    	// first check whether redirect is configured for this response
    	// this is the old pre [ 1155415 ] Handle redirect instructions which can lead to a loop
    	// shouldFollowRedirect method - just renamed
      if (!redirectConfigured(response))
      	return false;
      // now do the recursion check
      String redirectLocation = response.getHeaderField("Location");
      
      URL url = null;

      try {
          if (redirectLocation != null) {
              url = new URL(response.getURL(), redirectLocation);
          }
      } catch (MalformedURLException e) {
          // Fall through and allow existing exception handling code deal 
          // with any exception - we don't know at this stage whether it is
          // a redirect instruction, although it is highly likely, given 
          // there is a location header present in the response!
      }
      
      switch (response.getResponseCode()) {
      	case HttpURLConnection.HTTP_MOVED_PERM:
  	    case HttpURLConnection.HTTP_MOVED_TEMP:	// Fall through
  	        if (null != url && _redirects.contains(url)) {
  	            // We have already been instructed to redirect to that location in
  	            // the course of this attempt to resolve the resource
  	            throw new RecursiveRedirectionException(url, 
  	                    "Unable to process request due to redirection loop");
  	        }
  	        _redirects.add(url);
      		break;
      }    
      return redirectLocation != null;
    }  

    FrameSelector getTopFrame() {
        return _frameContents.getTopFrame();
    }


    FrameSelector getFrame( String target ) {
        return _frameContents.getFrame( target );
    }

}

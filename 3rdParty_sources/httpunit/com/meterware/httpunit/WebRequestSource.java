/********************************************************************************************************************
 *
 *
 * Copyright (c) 2001-2008, Russell Gold
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
package com.meterware.httpunit;

import org.w3c.dom.Node;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

import java.net.URL;
import java.util.StringTokenizer;
import java.io.IOException;

import com.meterware.httpunit.scripting.ScriptableDelegate;
import com.meterware.httpunit.scripting.ScriptingHandler;


/**
 * Base class for objects which can be clicked to generate new web requests.
 *
 * @author <a href="mailto:russgold@httpunit.org">Russell Gold</a>
 */
abstract
public class WebRequestSource extends ParameterHolder implements HTMLElement {

    private FrameSelector _frame;

    /** The name of the destination attribute used to create for the request, including anchors and parameters. **/
    private String         _destinationAttribute;
    private ScriptingHandler _scriptable;


    /**
     * Returns the ID associated with this request source.
     **/
    public String getID() {
        return getAttribute( "id" );
    }


    /**
     * Returns the class associated with this request source.
     **/
    public String getClassName() {
        return getAttribute( "class" );
    }


    /**
     * Returns the name associated with this request source.
     **/
    public String getName() {
        return getAttribute( "name" );
    }


    /**
     * Returns the title associated with this request source.
     **/
    public String getTitle() {
        return getAttribute( "title" );
    }


    /**
     * Returns the target for this request source.
     */
    public String getTarget() {
        if (getSpecifiedTarget().length() == 0) {
            return _defaultTarget;
        } else {
            return getSpecifiedTarget();
        }
    }


    /**
     * Returns the name of the frame containing this request source.
     * @deprecated as of 1.6, use #getFrame
     */
    public String getPageFrame() {
        return _frame.getName();
    }


    /**
     * Returns the frame containing this request source.
     */
    public FrameSelector getFrame() {
        return _frame;
    }


    /**
     * Returns the fragment identifier for this request source, used to identifier an element within an HTML document.
     */
    public String getFragmentIdentifier() {
        final int hashIndex = getDestination().indexOf( '#' );
        if (hashIndex < 0) {
            return "";
        } else {
            return getDestination().substring( hashIndex+1 );
        }
    }


    /**
     * Returns a copy of the domain object model subtree associated with this entity.
     **/
    public Node getDOMSubtree() {
        return _node.cloneNode( /* deep */ true );
    }


    /**
     * Creates and returns a web request from this request source.
     **/
    abstract
    public WebRequest getRequest();


    /**
     * Returns an array containing the names of any parameters to be sent on a request based on this request source.
     **/
    abstract
    public String[] getParameterNames();


    /**
     * Returns the values of the named parameter.
     **/
    abstract
    public String[] getParameterValues( String name );


    /**
     * Returns the URL relative to the current page which will handle the request.
     */
    String getRelativePage() {
        final String url = getRelativeURL();
        if (HttpUnitUtils.isJavaScriptURL( url )) return url;
        final int questionMarkIndex = url.indexOf("?");
        if (questionMarkIndex >= 1 && questionMarkIndex < url.length() - 1) {
            return url.substring(0, questionMarkIndex);
        }
        return url;
    }


    /**
     * get the relative URL for a weblink
     * change spaces to %20
     * @return the relative URL as a string
     */
    protected String getRelativeURL() {
        String result = HttpUnitUtils.encodeSpaces( HttpUnitUtils.trimFragment( getDestination() ) );
        if (result.trim().length() == 0) result = getBaseURL().getFile();
        return result;
    }


//----------------------------- protected members ---------------------------------------------

    /**
     * Contructs a web request source.
     * @param response the response from which this request source was extracted
     * @param node     the DOM subtree defining this request source
     * @param baseURL  the URL on which to base all releative URL requests
     * @param attribute the attribute which defines the relative URL to which requests will be directed
     **/
    WebRequestSource( WebResponse response, Node node, URL baseURL, String attribute, FrameSelector frame, String defaultTarget ) {
        if (node == null) throw new IllegalArgumentException( "node must not be null" );
        _baseResponse  = response;
        _node          = node;
        _baseURL       = baseURL;
        _destinationAttribute = attribute;
        _frame         = frame;
        _defaultTarget = defaultTarget;
    }


    protected URL getBaseURL() {
        return _baseURL;
    }


    protected String getDestination() {
        return getElement().getAttribute( _destinationAttribute );
    }


    protected void setDestination( String destination ) {
        getElement().setAttribute( _destinationAttribute, destination );
    }


    /**
     * Returns the actual DOM for this request source, not a copy.
     **/
    protected Element getElement() {
        return (Element) _node;
    }


    /**
     * Returns the HTMLPage associated with this request source.
     */
    protected HTMLPage getHTMLPage() throws SAXException {
        return _baseResponse.getReceivedPage();
    }


    /**
     * Extracts any parameters specified as part of the destination URL, calling addPresetParameter for each one
     * in the order in which they are found.
     */
    final
    protected void loadDestinationParameters() {
        StringTokenizer st = new StringTokenizer( getParametersString(), PARAM_DELIM );
        while (st.hasMoreTokens()) 
        	stripOneParameter( st.nextToken() );
    }


    /**
     * submit the given event for the given request
     * @param event
     * @param request
     * @return the response for the submitted Request
     * @throws IOException
     * @throws SAXException
     */
    protected WebResponse submitRequest( String event, final WebRequest request ) throws IOException, SAXException {
        WebResponse response = null;
        if (doEventScript( event )) 
        	response = submitRequest( request );
        if (response == null) response = getCurrentFrameContents();
        return response;
    }
    
    /**
     * handle the event that has the given script attached
     * by compiling the eventScript as a function and  executing it
     * @param eventScript - the script to use
     * @deprecated since 1.7 - use doEventScript instead
     */
    public boolean doEvent( String eventScript ) {
    	return doEventScript(eventScript);
    }
    
    /**
     * optional do the event if it's defined
     * @param eventScript - the script to handle
     * @return whether the script was handled
     */
    public boolean doEventScript(String eventScript) {
    	return this.getScriptingHandler().doEventScript(eventScript);
    }
    

    public boolean handleEvent(String eventName) {
    	return this.getScriptingHandler().handleEvent(eventName);
    }

    protected WebResponse getCurrentFrameContents() {
        return getCurrentFrame( getBaseResponse().getWindow(), _frame );
    }


    private WebResponse getCurrentFrame( WebWindow window, FrameSelector pageFrame ) {
        return window.hasFrame( pageFrame ) ? window.getFrameContents( pageFrame ) : window.getCurrentPage();
    }


    /**
     * Submits a request to the web client from which this request source was originally obtained.
     **/
    final
    protected WebResponse submitRequest( WebRequest request ) throws IOException, SAXException {
        return getDestination().equals( "#" )
                    ? _baseResponse
                    : _baseResponse.getWindow().sendRequest( request );
    }


    /**
     * Returns the web response containing this request source.
     */
    final
    protected WebResponse getBaseResponse() {
        return _baseResponse;
    }


    /**
     * Records a parameter defined by including it in the destination URL.
     * The value can be null, if the parameter name was not specified with an equals sign.
     **/
    abstract
    protected void addPresetParameter( String name, String value );


    /**
     * get the attribute value for the given name
     * @param name - the name of the attribute to get
     */
    public String getAttribute( final String name ) {
        return NodeUtils.getNodeAttribute( _node, name );
    }
    
    /**
     * set the attribute with the given name to the given value
     * @param name - the name of the attribute
     * @param value - the value to use
     */
    public void setAttribute( final String name, final Object value ) {
    	NodeUtils.setNodeAttribute( getNode(), name,  (value == null) ? null : value.toString() );
    }

    /**
     * remove the given attribute
     * @param name - the name of the attribute to remove
     */
    public void removeAttribute( final String name ) {
    	NodeUtils.removeNodeAttribute( getNode(), name );
    }    


    public boolean isSupportedAttribute( String name ) {
        return false;
    }


    public Node getNode() {
        return _node;
    }


    /**
     * Returns the text value of this block.
     **/
    public String getText() {
        if (_node.getNodeType() == Node.TEXT_NODE) {
            return _node.getNodeValue().trim();
        } else if (_node == null || !_node.hasChildNodes()) {
            return "";
        } else {
            return NodeUtils.asText( _node.getChildNodes() ).trim();
        }
    }


    public String getTagName() {
        return _node.getNodeName();
    }


    String getAttribute( final String name, String defaultValue ) {
        return NodeUtils.getNodeAttribute( _node, name, defaultValue );
    }


//----------------------------- private members -----------------------------------------------


    /**
     * parameter Delimiter for URL parameters
     * bug report
     * [ 1052037 ] Semicolon not supported as URL param delimiter
     * asks for this to be extended to &;
     * @see http://www.w3.org/TR/html4/appendix/notes.html#h-B.2 section B2.2
     * 
     */
    private static final String PARAM_DELIM = "&";

    /** The web response containing this request source. **/
    private WebResponse    _baseResponse;

    /** The name of the frame in which the response containing this request source is rendered. **/
    private String         _defaultTarget;

    /** The URL of the page containing this entity. **/
    private URL            _baseURL;

    /** The DOM node representing this entity. **/
    private Node           _node;


    private String getSpecifiedTarget() {
        return getAttribute( "target" );
    }


    protected void setTargetAttribute( String value ) {
        ((Element) _node).setAttribute( "target", value );
    }


    /**
     * Gets all parameters from a URL
     **/
    private String getParametersString() {
        String url = HttpUnitUtils.trimFragment( getDestination() );
        if (url.trim().length() == 0) url = getBaseURL().toExternalForm();
        if (HttpUnitUtils.isJavaScriptURL( url )) return "";
        final int questionMarkIndex = url.indexOf("?");
        if (questionMarkIndex >= 1 && questionMarkIndex < url.length() - 1) {
            return url.substring( questionMarkIndex + 1 );
        }
        return "";
    }


    /**
     * Extracts a parameter of the form <name>[=[<value>]].
     **/
    private void stripOneParameter( String param ) {
        final int index = param.indexOf( "=" );
        String value = ((index < 0)
                           ? null
                           : ((index == param.length() - 1)
                                    ? getEmptyParameterValue()
                                    : decode( param.substring( index + 1 ) ) ));
        String name = (index < 0) ? decode( param ) : decode( param.substring( 0, index ) );
        addPresetParameter( name, value );
    }


    private String decode( String string ) {
        return HttpUnitUtils.decode( string, _baseResponse.getCharacterSet() ).trim();
    }


    abstract protected String getEmptyParameterValue();

    /**
     * Returns the scriptable delegate.
     */
    public ScriptingHandler getScriptingHandler() {
        if (_scriptable == null) {
            _scriptable = HttpUnitOptions.getScriptingEngine().createHandler( this );
        }
        return _scriptable;
    }

    public ScriptableDelegate getParentDelegate() {
        return getBaseResponse().getDocumentScriptable();
    }


}

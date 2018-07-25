package com.meterware.httpunit;
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
import org.w3c.dom.Node;

import java.net.URL;

import com.meterware.httpunit.scripting.ScriptableDelegate;
import com.meterware.httpunit.scripting.ScriptingHandler;

/**
 * Represents a block-level element such as a paragraph or table cell, which can contain other elements.
 *
 * @author <a href="mailto:russgold@httpunit.org">Russell Gold</a>
 *
 * @since 1.6
 **/
abstract public class BlockElement extends ParsedHTML implements HTMLSegment, HTMLElement {


    private ScriptingHandler _scriptable;
    private Node             _node;


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


    /**
     * Returns the tag for this block.
     */
    public String getTagName() {
        return _node == null ? "p" : _node.getNodeName();
    }


    /**
     * Returns a copy of the domain object model associated with this HTML segment.
     **/
    public Node getDOM() {
        return super.getDOM();
    }


//-------------------------------- HTMLElement methods ---------------------------------------


    /**
     * Returns the ID associated with this element. IDs are unique throughout the HTML document.
     **/
    public String getID() {
        return getAttribute( "id" );
    }


    /**
     * Returns the class attribute associated with this element.
     */
    public String getClassName() {
        return getAttribute( "class" );
    }


    /**
     * Returns the name associated with this element.
     **/
    public String getName() {
        return getAttribute( "name" );
    }


    /**
     * Returns the title associated with this element.
     **/
    public String getTitle() {
        return getAttribute( "title" );
    }


    /**
     * Returns the delegate which supports scripting this element.
     */
    public ScriptingHandler getScriptingHandler() {
        if (_scriptable == null) {
            _scriptable = HttpUnitOptions.getScriptingEngine().createHandler( this );
        }
        return _scriptable;
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
     * @param eventScript - the script to work on
     * @return true if the event script was handled
     */
    public boolean doEventScript(String eventScript) {
    	return this.getScriptingHandler().doEventScript(eventScript);
    }
    

    public boolean handleEvent(String eventName) {
    	return this.getScriptingHandler().handleEvent(eventName);
    }

    public ScriptableDelegate getParentDelegate() {
        return getResponse().getDocumentScriptable();
    }


    public ScriptableDelegate newScriptable() {
        return new HTMLElementScriptable( this );
    }


    /**
     * get the attribute with the given name
     * @param name - the name of the attribute to get
     */
    public String getAttribute( final String name ) {
        return NodeUtils.getNodeAttribute( _node, name );
    }

    /**
     * set the attribute with the given name to the given value
     * @param name - the name of the attribute to set
     * @param value - the value to use
     */
    public void setAttribute( final String name, final Object value ) {
    	NodeUtils.setNodeAttribute( _node, name,  (value == null) ? null : value.toString() );
    }

    /**
     * remove the attribute with the given name
     * @param name - the name of the attribute
     */
    public void removeAttribute( final String name ) {
    	NodeUtils.removeNodeAttribute( _node, name );
    }    

    /**
     * Returns true if this element may have an attribute with the specified name.
     */
    public boolean isSupportedAttribute( String name ) {
        return false;
    }


    public Node getNode() {
        return _node;
    }

//----------------------------------------------- Object methods -------------------------------------------------------


    public boolean equals( Object obj ) {
        return getClass().equals( obj.getClass() ) && equals( (BlockElement) obj );
    }


    private boolean equals( BlockElement block ) {
        return _node.equals( block._node );
    }


    public int hashCode() {
        return _node.hashCode();
    }


//------------------------------------- protected members --------------------------------------------------------------


    protected BlockElement( WebResponse response, FrameSelector frame, URL baseURL, String baseTarget, Node rootNode, String characterSet ) {
        super( response, frame, baseURL, baseTarget, rootNode, characterSet );
        _node = rootNode;
    }


    protected int getAttributeValue( Node node, String attributeName, int defaultValue ) {
        return NodeUtils.getAttributeValue( node, attributeName, defaultValue );
    }
}

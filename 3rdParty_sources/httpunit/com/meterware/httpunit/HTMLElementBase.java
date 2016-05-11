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
import org.w3c.dom.Node;
import com.meterware.httpunit.scripting.ScriptableDelegate;
import com.meterware.httpunit.scripting.ScriptingHandler;

import java.util.HashSet;
import java.util.List;
import java.util.ArrayList;
import java.util.Set;


/**
 *
 * @since 1.5.2
 * @author <a href="mailto:russgold@httpunit.org">Russell Gold</a>
 **/ 
abstract
class HTMLElementBase implements HTMLElement {

    private Node        _node;
    private ScriptingHandler _scriptable;
    private Set _supportedAttributes = new HashSet();


    public String getID() {
        return getAttribute( "id" );
    }


    public String getClassName() {
        return getAttribute( "class" );
    }


    public String getTitle() {
        return getAttribute( "title" );
    }


    public String getName() {
        return getAttribute( "name" );
    }


    /**
     * Returns a scriptable object which can act as a proxy for this control.
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
     */
    public boolean doEventScript(String eventScript) {
    	return this.getScriptingHandler().doEventScript(eventScript);
    }
    

    public boolean handleEvent(String eventName) {
    	return this.getScriptingHandler().handleEvent(eventName);
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


    /**
     * construct me from a node
     * @param node - the node to get me from
     */
    protected HTMLElementBase( Node node ) {
        _node = node;
        // default attributes every html element can have
        supportAttribute( "id" );
        supportAttribute( "class" );
        supportAttribute( "title" );
        supportAttribute( "name" );
    }


    /**
     * get the Attribute with the given name - by delegating to
     * NodeUtils
     * @param name - the name of the attribute to get
     * @return the attribute
     */
    public String getAttribute( final String name ) {
        return NodeUtils.getNodeAttribute( getNode(), name );
    }
    
    /**
     * set the Attribute with the given name - by delegating to NodeUtils
     * @param name - the name of the attribute to set
     * @param value - the value to set
     */
    public void setAttribute( final String name, final Object value ) {
       NodeUtils.setNodeAttribute( getNode(), name,  (value == null) ? null : value.toString() );
    }
         
    /**
     * remove the Attribute with the given name - by delegating to NodeUtils
     * @param name - the name of the attribute to remove
     */   
    public void removeAttribute( final String name ) {
       NodeUtils.removeNodeAttribute( getNode(), name );
    }    


    public boolean isSupportedAttribute( String name ) {
        return _supportedAttributes.contains( name );
    }


    protected String getAttribute( final String name, String defaultValue ) {
        return NodeUtils.getNodeAttribute( getNode(), name, defaultValue );
    }


    public Node getNode() {
        return _node;
    }


    protected void supportAttribute( String name ) {
        _supportedAttributes.add( name );
    }


    /**
     * Creates and returns a scriptable object for this control. Subclasses should override this if they use a different
     * implementation of Scriptable.
     */
    public ScriptableDelegate newScriptable() {
        return new HTMLElementScriptable( this );
    }


}

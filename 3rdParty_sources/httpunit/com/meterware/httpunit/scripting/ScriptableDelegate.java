package com.meterware.httpunit.scripting;
/********************************************************************************************************************
 *
 *
 * Copyright (c) 2002-2007, Russell Gold
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
import com.meterware.httpunit.HTMLElement;

/**
 * An interface for objects which will be accessible via scripting.
 *
 * @author <a href="mailto:russgold@httpunit.org">Russell Gold</a>
 **/
abstract public class ScriptableDelegate implements ScriptingHandler {

    private ScriptingEngine _scriptEngine;


    /**
     * a dummy ScriptingEngine implementation
     */
    public static final ScriptingEngine NULL_SCRIPT_ENGINE = new ScriptingEngine() {
        public boolean supportsScriptLanguage( String language ) { return false; }
        public String runScript( String language, String script ) { return ""; }
        public boolean doEventScript( String eventScript ) { return true; }
        public boolean doEvent(String eventScript ){ return true; }
        public boolean handleEvent(String eventName) { return true; }
        public Object evaluateExpression( String urlString ) { return null; }
        public ScriptingEngine newScriptingEngine( ScriptableDelegate child ) { return this; }
        public void clearCaches() {}
    };


    public boolean supportsScriptLanguage( String language ) {
        return getScriptEngine().supportsScriptLanguage( language );
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
     * Executes the specified scripted event.
     * @param eventScript - the eventScript to execute
     * @return true if the event has been handled.
     **/
    public boolean doEventScript( String eventScript ) {
        return eventScript.length() == 0 || getScriptEngine().doEventScript( eventScript );
    }
    
    /**
     * Executes the event Handler script for the specified event (such as onchange, onmousedown, onclick, onmouseup) if it is defined.
     * @param eventName the name of the event for which a handler should be run.
     * @return whether the event with the given name was handled
     */
    public boolean handleEvent(String eventName) {
    	String eventScript = (String) get(eventName);
    	return doEventScript( eventScript );
    }

    /**
     * Executes the specified script, returning any intended replacement text.
     * @return the replacement text, which may be empty.
     **/
    public String runScript( String language, String script ) {
        return (script.length() == 0) ? "" : getScriptEngine().runScript( language, script );
    }


    /**
     * Evaluates the specified javascript expression, returning its value.
     **/
    public Object evaluateExpression( String urlString ) {
        if (urlString.length() == 0) return null;
        return getScriptEngine().evaluateExpression( urlString );
    }


    public void clearCaches() {
        getScriptEngine().clearCaches();
    }


    /**
     * Returns the value of the named property. Will return null if the property does not exist.
     **/
    public Object get( String propertyName ) {
      return null;
    }


    /**
     * Returns the value of the index property. Will return null if the property does not exist.
     **/
    public Object get( int index ) {
        return null;
    }


    /**
     * Sets the value of the named property. Will throw a runtime exception if the property does not exist or
     * cannot accept the specified value.
     **/
    public void set( String propertyName, Object value ) {
        throw new RuntimeException( "No such property: " + propertyName );
    }


    /**
     * Specifies the scripting engine to be used.
     */
    public void setScriptEngine( ScriptingEngine scriptEngine ) {
        _scriptEngine = scriptEngine;
    }


    public ScriptingEngine getScriptEngine() {
        return _scriptEngine != null ? _scriptEngine : NULL_SCRIPT_ENGINE;
    }


    public ScriptingEngine getScriptEngine( ScriptableDelegate child ) {
        return getScriptEngine().newScriptingEngine( child );
    }


    protected ScriptableDelegate[] getDelegates( final HTMLElement[] elements ) {
        ScriptableDelegate[] result = new ScriptableDelegate[ elements.length ];
        for (int i = 0; i < elements.length; i++) {
            result[i] = (ScriptableDelegate) elements[i].getScriptingHandler();
        }
        return result;
    }
}

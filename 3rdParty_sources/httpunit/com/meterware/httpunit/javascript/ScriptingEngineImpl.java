package com.meterware.httpunit.javascript;
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

import org.mozilla.javascript.*;
import com.meterware.httpunit.scripting.ScriptingEngine;
import com.meterware.httpunit.HttpUnitUtils;
import com.meterware.httpunit.ScriptException;

import java.util.ArrayList;


/**
 *
 * @author <a href="mailto:russgold@httpunit.org">Russell Gold</a>
 **/
public abstract class ScriptingEngineImpl extends ScriptableObject implements ScriptingEngine {

    private final static Object[] NO_ARGS = new Object[0];

    private static ArrayList _errorMessages = new ArrayList();


    static public void clearErrorMessages() {
        _errorMessages.clear();
    }


    static public String[] getErrorMessages() {
        return (String[]) _errorMessages.toArray( new String[ _errorMessages.size() ] );
    }


    /**
     * handle Exceptions 
     * @param e - the exception to handle
     * @param badScript - the script that caused the problem
     */
    static public void handleScriptException( Exception e, String badScript ) {
        final String errorMessage = badScript + " failed: " + e;
        if (!(e instanceof EcmaError) && !(e instanceof EvaluatorException)) {
          HttpUnitUtils.handleException(e);
            throw new RuntimeException( errorMessage );
        } else if (JavaScript.isThrowExceptionsOnError()) {
          HttpUnitUtils.handleException(e);
            throw new ScriptException( errorMessage );
        } else {
            _errorMessages.add( errorMessage );
        }
    }

//--------------------------------------- ScriptingEngine methods ------------------------------------------------------

    public boolean supportsScriptLanguage( String language ) {
        return language == null || language.toLowerCase().startsWith( "javascript" );
    }


    /**
     * run the given script
     * @param language - the language of the script
     * @param script - the script to run
     */
    public String runScript( String language, String script ) {
        if (!supportsScriptLanguage( language )) return "";
        try {
            script = script.trim();
            if (script.startsWith( "<!--" )) {
                script = withoutFirstLine( script );
                if (script.endsWith( "-->" )) script = script.substring( 0, script.lastIndexOf( "-->" ));
            }
            Context context = Context.enter();
            context.initStandardObjects( null );
            context.evaluateString( this, script, "httpunit", 0, null );
            return getDocumentWriteBuffer();
        } catch (Exception e) {
            handleScriptException( e, "Script '" + script + "'" );
            return "";
        } finally {
            discardDocumentWriteBuffer();
            Context.exit();
        }
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
    * handle the event that has the given script attached
    * by compiling the eventScript as a function and  executing it
    * @param eventScript - the script to use
    */
    public boolean doEventScript( String eventScript ) { 	
    	if (eventScript.length() == 0) { 
      	return true;
      }	else { 	
        try {
            Context context = Context.enter();
            context.initStandardObjects( null );
            context.setOptimizationLevel( -1 );
            // wrap the eventScript into a function
            Function f = context.compileFunction( this, "function x() { " + eventScript + "}", "httpunit", 0, null );
            // call the function with no arguments
            Object result = f.call( context, this, this, NO_ARGS );
            // return the result of the function or false if it is not boolean
            return (!(result instanceof Boolean)) || ((Boolean) result).booleanValue();
        } catch (Exception e) {
            handleScriptException( e, "Event '" + eventScript + "'" );
            return false;
        } finally {
            Context.exit();
        }
      } // if
    }

    /**
     * get the event Handler script for the event e.g. onchange, onmousedown, onclick, onmouseup
     * execute the script if it's assigned by calling doEvent for the script
     * @param eventName
     * @return
     */
    public boolean handleEvent(String eventName) {
      throw new RuntimeException("pseudo - abstract handleEvent called ");
    }

    /**
     * Evaluates the specified string as JavaScript. Will return null if the script has no return value.
     * @param expression - the expression to evaluate
     */
    public Object evaluateExpression( String expression ) {
        try {
            Context context = Context.enter();
            context.initStandardObjects( null );
            Object result = context.evaluateString( this, expression, "httpunit", 0, null );
            return (result == null || result instanceof Undefined) ? null : result;
        } catch (Exception e) {
            handleScriptException( e, "URL '" + expression + "'" );
            return null;
        } finally {
            Context.exit();
        }
    }

//------------------------------------------ protected methods ---------------------------------------------------------

    protected String getDocumentWriteBuffer() {
        throw new IllegalStateException( "may not run runScript() from " + getClass() );
    }

    protected void discardDocumentWriteBuffer() {
        throw new IllegalStateException( "may not run runScript() from " + getClass() );
    }

    private String withoutFirstLine( String script ) {
        for (int i=0; i < script.length(); i++) {
            if (isLineTerminator( script.charAt(i) )) return script.substring( i ).trim();
        }
        return "";
    }

    private boolean isLineTerminator( char c ) {
        return c == 0x0A || c == 0x0D;
    }
}

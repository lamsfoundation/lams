package com.meterware.httpunit.javascript;
/********************************************************************************************************************
 *
 *
 * Copyright (c) 2002,2008 Russell Gold
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
import com.meterware.httpunit.HttpUnitUtils;
import com.meterware.httpunit.WebResponse;
import com.meterware.httpunit.HTMLElement;
import com.meterware.httpunit.scripting.ScriptingEngineFactory;
import com.meterware.httpunit.scripting.ScriptableDelegate;
import com.meterware.httpunit.scripting.ScriptingHandler;


/**
 * An implementation of the scripting engine factory which selects a Rhino-based implementation of JavaScript.
 *
 * @author <a href="mailto:russgold@httpunit.org">Russell Gold</a>
 **/
public class JavaScriptEngineFactory implements ScriptingEngineFactory {

    public boolean isEnabled() {
        try {
            Class.forName( "org.mozilla.javascript.Context" );
            return true;
        } catch (Exception e) {
            System.err.println( "Rhino classes (js.jar) not found - Javascript disabled" );
            return false;
        }
    }


    public void associate( WebResponse response ) {
        try {
            JavaScript.run( response );
        } catch (RuntimeException e) {
            throw e;
        } catch (Exception e) {
        	HttpUnitUtils.handleException(e);
           throw new RuntimeException( e.toString() );
        }
    }


    public void load( WebResponse response ) {
        try {
            JavaScript.load( response );
        } catch (RuntimeException e) {
            throw e;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException( e.toString() );
        }
    }


    public void setThrowExceptionsOnError( boolean throwExceptions ) {
        JavaScript.setThrowExceptionsOnError( throwExceptions );
    }


    public boolean isThrowExceptionsOnError() {
        return JavaScript.isThrowExceptionsOnError();
    }


    public String[] getErrorMessages() {
        return ScriptingEngineImpl.getErrorMessages();
    }


    public void clearErrorMessages() {
        ScriptingEngineImpl.clearErrorMessages();
    }


    public ScriptingHandler createHandler( HTMLElement elementBase ) {
        ScriptableDelegate delegate = elementBase.newScriptable();
        delegate.setScriptEngine( elementBase.getParentDelegate().getScriptEngine( delegate ) );
        return delegate;
    }


    public ScriptingHandler createHandler( WebResponse response ) {
        return response.createJavascriptScriptingHandler();
    }
}

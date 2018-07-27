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
import com.meterware.httpunit.WebResponse;
import com.meterware.httpunit.HTMLElement;

/**
 *
 * @author <a href="mailto:russgold@httpunit.org">Russell Gold</a>
 **/
public interface ScriptingEngineFactory {

    /**
     * Returns true if this engine is enabled.
     */
    public boolean isEnabled();

    /**
     * Associates a scripting engine with the specified HTML web response.
     **/
    public void associate( WebResponse response );

    /**
     * Runs the 'onload' event (if any) for the specified HTML web response. Will associate a scripting engine with
     * the response if that has not already been done.
     **/
    public void load( WebResponse response );

    /**
     * Determines whether script errors result in exceptions or warning messages.
     */
    public void setThrowExceptionsOnError( boolean throwExceptions );

    /**
     * Returns true if script errors cause exceptions to be thrown.
     */
    public boolean isThrowExceptionsOnError();

    /**
     * Returns the accumulated script error messages encountered. Error messages are accumulated only
     * if 'throwExceptionsOnError' is disabled.
     */
    public String[] getErrorMessages();

    /**
     * Clears the accumulated script error messages.
     */
    public void clearErrorMessages();


    ScriptingHandler createHandler( HTMLElement elementBase );


    ScriptingHandler createHandler( WebResponse response );
}

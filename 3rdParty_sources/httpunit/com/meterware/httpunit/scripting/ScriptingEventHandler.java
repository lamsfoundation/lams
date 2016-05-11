package com.meterware.httpunit.scripting;
/********************************************************************************************************************
 *
 *
 * Copyright (c) 2006-2007, Russell Gold
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
 * interface for every object that may have excutable events
 * and their scripts attached
 * @author <a href="mailto:russgold@httpunit.org">Russell Gold</a>
 **/
public interface ScriptingEventHandler {
	 /**
    * run the Script for the given Event
    * @param eventScript
    * @deprecated since 1.7
    * @return true if the script is empty or the result of the script
    */
  	boolean doEvent( String eventScript );
  	
    /**
     * run the Script for the given Event
     * @param eventScript
     * @since 1.7
     * @return true if the script is empty or the result of the script
     */
    boolean doEventScript( String eventScript );
    
    /**
     * handle the event with the given name by getting the
     * attribute and then executing the eventScript for it
     * 
     * @param eventName
     * @return the result of doEventScript
     */
    boolean handleEvent( String eventName);

}

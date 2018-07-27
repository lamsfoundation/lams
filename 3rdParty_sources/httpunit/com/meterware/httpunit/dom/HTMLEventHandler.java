package com.meterware.httpunit.dom;
/********************************************************************************************************************
 * $Header$
 *
 * Copyright (c) 2007-2008, Russell Gold
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
import org.mozilla.javascript.Function;
import org.mozilla.javascript.Context;

/**
 * the handler for HTML events
 * @author <a href="mailto:russgold@httpunit.org">Russell Gold</a>
 */
class HTMLEventHandler {

    private HTMLElementImpl _baseElement;
    private String _handlerName;

    private Function _handler;


    /**
     * create a handler for the given HTML Event
     * @param baseElement
     * @param handlerName
     */
    public HTMLEventHandler( HTMLElementImpl baseElement, String handlerName ) {
        _baseElement = baseElement;
        _handlerName = handlerName;
    }


    /**
     * set the handler Function for this event Handler
     * @param handler
     */
    void setHandler( Function handler ) {
        _handler = handler;
    }


    /**
     * get the (cached) handler Function for this event Handler
     * on first access compile the function
     * @return
     */
    Function getHandler() {
        if (_handler == null) {
            String attribute = _baseElement.getAttributeWithNoDefault( _handlerName );
            if (attribute != null && Context.getCurrentContext() != null) {
                _handler = Context.getCurrentContext().compileFunction( _baseElement, "function " + AbstractDomComponent.createAnonymousFunctionName() + "() { " + attribute + "}", "httpunit", 0, null );
            }
        }
        return _handler;
    }
}

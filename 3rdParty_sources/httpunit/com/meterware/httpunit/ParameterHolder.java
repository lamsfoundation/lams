package com.meterware.httpunit;
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
import com.meterware.httpunit.protocol.UploadFileSpec;
import com.meterware.httpunit.protocol.ParameterProcessor;
import com.meterware.httpunit.protocol.ParameterCollection;

import java.io.IOException;


/**
 * This abstract class is extended by classes which hold parameters for web requests. Note that it is an abstract class
 * rather than an interface in order to keep its methods package-local.
 *
 * @author <a href="mailto:russgold@httpunit.org">Russell Gold</a>
 **/
abstract class ParameterHolder implements ParameterCollection {

    /**
     * Specifies the position at which an image button (if any) was clicked. This default implementation does nothing.
     **/
    void selectImageButtonPosition( SubmitButton imageButton, int x, int y ) {}


    /**
     * Iterates through the fixed, predefined parameters in this holder, recording them in the supplied parameter processor.\
     * These parameters always go on the URL, no matter what encoding method is used.
     **/
    abstract
    void recordPredefinedParameters( ParameterProcessor processor ) throws IOException;


    /**
     * Returns an array of all parameter names in this collection.
     **/
    abstract
    String[] getParameterNames();


    /**
     * Returns the multiple default values of the named parameter.
     **/
    abstract
    String[] getParameterValues( String name );


    /**
     * Removes a parameter name from this collection.
     **/
    abstract
    void removeParameter( String name );


    /**
     * Sets the value of a parameter in a web request.
     **/
    abstract
    void setParameter( String name, String value );


    /**
     * Sets the multiple values of a parameter in a web request.
     **/
    abstract
    void setParameter( String name, String[] values );


    /**
     * Sets the multiple values of a file upload parameter in a web request.
     **/
    abstract
    void setParameter( String name, UploadFileSpec[] files );


    /**
     * Returns true if the specified name is that of a file parameter. The default implementation returns false.
     */
    boolean isFileParameter( String name ) {
        return false;
    }


    /**
     * Returns the character set encoding for the request.
     **/
    String getCharacterSet() {
        return HttpUnitUtils.DEFAULT_CHARACTER_SET;
    }


    abstract
    boolean isSubmitAsMime();
}

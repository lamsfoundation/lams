package com.meterware.servletunit;
/********************************************************************************************************************

*
* Copyright (c) 2000-2003, Russell Gold
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
import java.util.Enumeration;
import java.util.Hashtable;

import javax.servlet.Servlet;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;


/**
 * This class acts as a test environment for servlets.
 **/
class ServletUnitServletConfig implements ServletConfig {


    ServletUnitServletConfig( String name, WebApplication application, Hashtable initParams ) {
        _name = name;
        _initParameters = initParams;
        _context = application.getServletContext();
    }


//-------------------------------------------- ServletConfig methods ---------------------------------------------------


    /**
     * Returns the value of the specified init parameter, or null if no such init parameter is defined.
     **/
    public String getInitParameter( String name ) {
        return (String) _initParameters.get( name );
    }


    /**
     * Returns an enumeration over the names of the init parameters.
     **/
    public Enumeration getInitParameterNames() {
        return _initParameters.keys();
    }


    /**
     * Returns the current servlet context.
     **/
    public ServletContext getServletContext() {
        return _context;
    }


    /**
     * Returns the registered name of the servlet, or its class name if it is not registered.
     **/
    public java.lang.String getServletName() {
        return _name;
    }


//----------------------------------------------- private members ------------------------------------------------------


    private String _name;

    private final Hashtable _initParameters;

    private final ServletContext _context;

}

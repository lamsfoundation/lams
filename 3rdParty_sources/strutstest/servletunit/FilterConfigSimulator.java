//  StrutsTestCase - a JUnit extension for testing Struts actions
//  within the context of the ActionServlet.
//  Copyright (C) 2002 Deryl Seale
//
//  This library is free software; you can redistribute it and/or
//  modify it under the terms of the Apache Software License as
//  published by the Apache Software Foundation; either version 1.1
//  of the License, or (at your option) any later version.
//
//  This library is distributed in the hope that it will be useful,
//  but WITHOUT ANY WARRANTY; without even the implied warranty of
//  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
//  Apache Software Foundation Licens for more details.
//
//  You may view the full text here: http://www.apache.org/LICENSE.txt

package servletunit;

import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import java.util.Enumeration;

//  StrutsTestCase - a JUnit extension for testing Struts actions
//  within the context of the ActionServlet.
//  Copyright (C) 2002 Deryl Seale
//
//  This library is free software; you can redistribute it and/or
//  modify it under the terms of the Apache Software License as
//  published by the Apache Software Foundation; either version 1.1
//  of the License, or (at your option) any later version.
//
//  This library is distributed in the hope that it will be useful,
//  but WITHOUT ANY WARRANTY; without even the implied warranty of
//  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
//  Apache Software Foundation Licens for more details.
//
//  You may view the full text here: http://www.apache.org/LICENSE.txt

public class FilterConfigSimulator implements FilterConfig {

    private ServletContext context = null;


    /**
     *  Constructor for the FilterConfigSimulator object
     *
     * @param  context  The ServletContext to be returned by getServletContext
     */
    public FilterConfigSimulator(ServletContext context) {
        this.context = context;
    }


    /**
     *  Gets the filterName attribute of the FilterConfigSimulator object
     *
     * currently not supported
     *
     * @return    The filterName value
     */
    public String getFilterName() {

        throw new java.lang.UnsupportedOperationException("Method getFilterName() not yet implemented.");
    }


    /**
     *  Gets the initParameter attribute of the FilterConfigSimulator object
     *
     * currently not supported
     *
     * @param  parm1  Description of the Parameter
     * @return        The initParameter value
     */
    public String getInitParameter(String parm1) {

        throw new java.lang.UnsupportedOperationException("Method getInitParameter() not yet implemented.");
    }


    /**
     *  Gets the initParameterNames attribute of the FilterConfigSimulator
     *  object
     *
     * currently not supported
     *
     * @return    The initParameterNames value
     */
    public Enumeration getInitParameterNames() {

        throw new java.lang.UnsupportedOperationException("Method getInitParameterNames() not yet implemented.");
    }


    /**
     *  Gets the servletContext attribute of the FilterConfigSimulator object
     *
     * @return    The servletContext value
     */
    public ServletContext getServletContext() {
        return context;
    }
}

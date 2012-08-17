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

package servletunit.struts;

import org.apache.cactus.server.ServletConfigWrapper;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;

/**
 * A wrapper for the ServletConfig class.  This is used in
 * CactusStrutsTestCase so that we can use out own ServletContext
 * wrapper class.  This allows us to to use the ActionServlet
 * as a black box, rather than mimic its behavior as was previously
 * the case.
 */
public class StrutsServletConfigWrapper extends ServletConfigWrapper {

    private ServletContext context;
    
    public StrutsServletConfigWrapper(ServletConfig config) {
	super(config);
    }

    public ServletContext getServletContext() {
	return this.context;
    }

    public void setServletContext(ServletContext context) {
	this.context = context;
    }

}

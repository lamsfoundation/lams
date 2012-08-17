package servletunit;

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

import javax.servlet.*;
import java.io.IOException;

/**
 * Simulates a <code>javax.servlet.RequestDispatcher</code> object.
 */
public class RequestDispatcherSimulator implements RequestDispatcher
{
    private Object dispatchedResource;
    /**
     *@param    dispatchedResource      The <code>dispatchedResource</code> object represents the resource that
     *                                  <code>this</code> <code>javax.servlet.RequestDispatcher</code> is tied to.
     *                                  Currently this class only supports <code>javax.servlet.Servlet</code> objects
     *                                  and <code>java.lang.String</code> objects.  If the parameter passed in is not
     *                                  a <code>javax.servlet.Servlet</code> object when forward or include is called
     *                                  the parameter's toString method is called and sent to <code>System.out</code>.
     *                                  Otherwise, the appropriate service method is called.
     */
    public RequestDispatcherSimulator( Object dispatchedResource )
    {
        this.dispatchedResource = dispatchedResource;
    }
    /**
     * Simulates the forward method of the <code>javax.servlet.RequestDispatcher</code> interface
     */
    public void forward( ServletRequest request, ServletResponse response ) throws ServletException, IOException
    {
        if( dispatchedResource instanceof Servlet )
            ((Servlet)dispatchedResource).service( request, response );
    }
    public void include( ServletRequest request, ServletResponse response ) throws ServletException, IOException
    {
        System.out.println( dispatchedResource.toString() );
    }

    public String getForward() {
        if (dispatchedResource instanceof String)
            return (String) dispatchedResource;
        else
            return dispatchedResource.getClass().toString();
    }
}

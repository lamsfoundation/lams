package servletunit;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionContext;
import java.util.Enumeration;
import java.util.Hashtable;

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

/**
 * This class simulates an HttpSession object. You can actually work with sessions. The simulation is done using a static
 * session object inside HttpServletRequest.
 */
public class HttpSessionSimulator implements HttpSession
{
    private Hashtable values;
    private boolean valid = true;
    private ServletContext context;

    public HttpSessionSimulator(ServletContext context)
    {
        this.context = context;
        values = new Hashtable();
    }

    public Object getAttribute(String s) throws IllegalStateException
    {
        checkValid();
        return values.get(s);
    }

    public Enumeration getAttributeNames() throws IllegalStateException
    {
        checkValid();
        return values.keys();
    }

    public long getCreationTime() throws IllegalStateException
    {
        checkValid();
        return -1;
    }

    public String getId()
    {
        return "-9999";
    }

    public long getLastAccessedTime()
    {
        return -1;
    }

    public int getMaxInactiveInterval() throws IllegalStateException
    {
        checkValid();
        return -1;
    }

    /**
     * This method is not supported.
     */
    public HttpSessionContext getSessionContext()
    {
        throw new UnsupportedOperationException("getSessionContext not supported!");
    }

    public Object getValue(String s) throws IllegalStateException
    {
        checkValid();
        return values.get(s);
    }

    public String[] getValueNames() throws IllegalStateException
    {
        checkValid();
        return (String[]) values.keySet().toArray();
    }

    public void invalidate() throws IllegalStateException
    {
        checkValid();
        this.valid = false;
    }

    public boolean isNew() throws IllegalStateException
    {
        checkValid();
        return false;
    }

    public void putValue(String s, Object obj) throws IllegalStateException
    {
        checkValid();
        values.put(s,obj);
    }

    public void removeAttribute(String s) throws IllegalStateException
    {
        checkValid();
        values.remove(s);
    }

    public void removeValue(String s) throws IllegalStateException
    {
        checkValid();
        values.remove(s);
    }

    public void setAttribute(String s, Object obj) throws IllegalStateException
    {
        checkValid();
        if (obj == null)
            removeValue(s);
        else
            values.put(s,obj);
    }

    public void setMaxInactiveInterval(int i)
    {
    }

    public ServletContext getServletContext() {
        return this.context;
    }

    private void checkValid() throws IllegalStateException {
        if (!valid)
            throw new IllegalStateException("session has been invalidated!");
    }

    protected boolean isValid() {
        return valid;
    }
}

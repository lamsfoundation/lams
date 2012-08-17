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

import org.apache.cactus.ServletURL;
import org.apache.cactus.server.HttpServletRequestWrapper;

import java.util.*;

/**
 * A wrapper for the HttpServletRequest class.  This is used in
 * CactusStrutsTestCase so that we can add our own request parameters
 * outside of the beginXXX and endXXX methods.  This allows us to
 * to use the ActionServlet as a black box, rather than mimic its
 * behavior as was previously the case.
 */
public class StrutsRequestWrapper extends HttpServletRequestWrapper {

    private String pathInfo;
    private String servletPath;
    private Map parameters;

    public StrutsRequestWrapper(HttpServletRequestWrapper request) {
        super(request,new ServletURL(request.getServerName(),request.getContextPath(),request.getServletPath(),request.getPathInfo(),request.getQueryString()));
        parameters = new HashMap();
    }

    public void setPathInfo(String pathInfo) {
        this.pathInfo = pathInfo;
    }

    public String getPathInfo() {
        if (this.pathInfo == null)
            return super.getPathInfo();
        else
            return this.pathInfo;
    }

    public void setServletPath(String servletPath) {
        this.servletPath = servletPath;
    }

    public String getServletPath() {
        if (this.servletPath == null)
            return super.getServletPath();
        else
            return this.servletPath;
    }


    public String getParameter(String name) {
        String[] result = getParameterValues(name);
        if ((result != null) && (result.length > 0)) {
            return result[0];
        } else
            return null;
    }

    public String[] getParameterValues(String name) {
        Object result = super.getParameterValues(name);
        if ((result == null) && (parameters.containsKey(name))) {
            result = parameters.get(name);
            if (!(result instanceof String[])) {
                String[] resultArray = { result.toString() };
                result = resultArray;
            }
        }
        return (String[]) result;
    }

    public Enumeration getParameterNames() {
        Enumeration superNames = super.getParameterNames();
        List nameList = new ArrayList(parameters.keySet());
        while (superNames.hasMoreElements()) {
            nameList.add(superNames.nextElement());
        }
        return Collections.enumeration(nameList);
    }

    public void addParameter(String name, String value) {
        if ((super.getParameter(name) == null) && (name != null) && (value != null))
            parameters.put(name,value);
    }

    public void addParameter(String name, String[] values) {
        if ((super.getParameter(name) == null) && (name != null) && (values != null))
            parameters.put(name,values);
    }

    public Map getParameterMap() {
        Map result = new HashMap();
        result.putAll(super.getParameterMap());
        result.putAll(parameters);
        return result;
    }

    public void clearRequestParameters() {
        this.parameters.clear();
//        super.request.getParameterMap().clear();
    }

}



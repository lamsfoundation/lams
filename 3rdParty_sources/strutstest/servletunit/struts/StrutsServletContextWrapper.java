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

import org.apache.cactus.server.ServletContextWrapper;
import servletunit.RequestDispatcherSimulator;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import java.net.URL;
import java.net.MalformedURLException;
import java.io.File;
import java.io.InputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

/**
 * A wrapper for the ServletContext class.  This is used in
 * CactusStrutsTestCase so that we can retrieve the forward
 * processed by the ActionServlet and use absolute paths
 * for Struts resources.  This allows us to to use
 * the ActionServlet as a black box, rather than mimic its
 * behavior as was previously the case.
 */
public class StrutsServletContextWrapper extends ServletContextWrapper {

    boolean processRequest = false;
    private String dispatchedResource;

    public StrutsServletContextWrapper(ServletContext context) {
        super(context);
    }

    public void setProcessRequest(boolean flag) {
        this.processRequest = flag;
    }

    public RequestDispatcher getRequestDispatcher(String path) {
        dispatchedResource = path;
        if (!processRequest)
            return new RequestDispatcherSimulator(path);
        else
            return super.getRequestDispatcher(path);
    }

    public String getForward() {
        return dispatchedResource;
    }

    /**
     * Override the getResource method to look for resources in the file system, allowing
     * the use of absolute paths for Struts configuration files.  If the resource path exists
     * in the file system, this method will return a URL based on the supplied path; otherwise,
     * it defers to the ServletContext loader.
     */
    public URL getResource(String pathname) throws MalformedURLException {
        File file = new File(pathname);
        if (file.exists())
            return file.toURL();
        else
            return super.getResource(pathname);
    }

    /**
     * Override the getResourceAsStream method to look for resources in the file system, allowing
     * the use of absolute paths for Struts configuration files. If the resource path exists
     * in the file system, this method will return a URL based on the supplied path; otherwise,
     * it defers to the ServletContext loader.
     */
    public InputStream getResourceAsStream(String pathname) {
        File file = new File(pathname);
        if (file.exists())
            try {
                return new FileInputStream(file);
            } catch (FileNotFoundException e) {
                return super.getResourceAsStream(pathname);
            }
        else
            return super.getResourceAsStream(pathname);
    }

}


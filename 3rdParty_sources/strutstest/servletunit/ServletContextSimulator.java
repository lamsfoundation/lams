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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.servlet.RequestDispatcher;
import javax.servlet.Servlet;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import java.io.InputStream;
import java.io.File;
import java.io.FileInputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Set;

/**
 * This class simulates a ServletContext.
 */
public class ServletContextSimulator implements ServletContext
{

    private Hashtable initParameters;
    private Hashtable attributes;
    private RequestDispatcherSimulator dispatcher = null;
    private static Log logger = LogFactory.getLog( ServletContextSimulator.class );
    private File contextDirectory;

    public ServletContextSimulator() {
        this.initParameters = new Hashtable();
        this.attributes = new Hashtable();
    }

    /**
     * Returns the servlet container attribute with the given name,
     * or <code>null</code> if there is no attribute by that name.
     * An attribute allows a servlet container to give the
     * servlet additional information not
     * already provided by this interface. See your
     * server documentation for information about its attributes.
     * A list of supported attributes can be retrieved using
     * <code>getAttributeNames</code>.
     *
     * <p>The attribute is returned as a <code>java.lang.Object</code>
     * or some subclass.
     * Attribute names should follow the same convention as package
     * names. The Java Servlet API specification reserves names
     * matching <code>java.*</code>, <code>javax.*</code>,
     * and <code>sun.*</code>.
     *
     *
     * @param name      a <code>String</code> specifying the name
     *                  of the attribute
     *
     * @return          an <code>Object</code> containing the value
     *                  of the attribute, or <code>null</code>
     *                  if no attribute exists matching the given
     *                  name
     *
     * @see             ServletContext#getAttributeNames
     *
     */
    public Object getAttribute(String name)
    {
        return attributes.get(name);
    }

    /**
     * Returns an <code>Enumeration</code> containing the
     * attribute names available
     * within this servlet context. Use the
     * {@link #getAttribute} method with an attribute name
     * to get the value of an attribute.
     *
     * @return          an <code>Enumeration</code> of attribute
     *                  names
     *
     * @see             #getAttribute
     *
     */
    public Enumeration getAttributeNames()
    {
        return attributes.keys();
    }

    /**
     * Unsupported in this version.
     */
    public ServletContext getContext(String uripath)
    {
        throw new UnsupportedOperationException("getContext operation is not supported!");
    }

    /**
     * Returns a <code>String</code> containing the value of the named
     * context-wide initialization parameter, or <code>null</code> if the
     * parameter does not exist.
     *
     * <p>This method can make available configuration information useful
     * to an entire "web application".  For example, it can provide a
     * webmaster's email address or the name of a system that holds
     * critical data.
     *
     * @param   s    a <code>String</code> containing the name of the
     *                  parameter whose value is requested
     *
     * @return          a <code>String</code> containing at least the
     *                  servlet container name and version number
     *
     * @see javax.servlet.ServletConfig#getInitParameter
     */
    public String getInitParameter(String s)
    {
        return (String) initParameters.get(s);
    }

    /**
     * Returns the names of the context's initialization parameters as an
     * <code>Enumeration</code> of <code>String</code> objects, or an
     * empty <code>Enumeration</code> if the context has no initialization
     * parameters.
     *
     * @return          an <code>Enumeration</code> of <code>String</code>
     *                  objects containing the names of the context's
     *                  initialization parameters
     *
     * @see javax.servlet.ServletConfig#getInitParameter
     */
    public Enumeration getInitParameterNames()
    {
        return initParameters.keys();
    }

    /**
     * Sets a named initialization parameter with the supplied
     * <code>String</code> value.
     *
     * @param key      a <code>String</code> specifying the name
     *                  of the initialization parameter
     *
     * @param value     a <code>String</code> value for this initialization
     *                  parameter
     *
     */
    public void setInitParameter(String key,String value)
    {
        initParameters.put(key,value);
    }

    /**
     * Returns the major version of the Java Servlet API that this
     * Web server supports. All implementations that comply
     * with Version 2.3 must have this method
     * return the integer 2.
     *
     * @return              2
     *
     */
    public int getMajorVersion()
    {
        return 2;
    }

    /**
     * Unsupported in this version.
     */
    public String getMimeType(String file)
    {
        throw new UnsupportedOperationException("getMimeType operation is not supported!");
    }

    /**
     * Returns the minor version of the Servlet API that this
     * Web server supports. All implementations that comply
     * with Version 2.3 must have this method
     * return the integer 1.
     *
     * @return              3
     *
     */
    public int getMinorVersion()
    {
        return 3;
    }

    public RequestDispatcher getNamedDispatcher(String s)
    {
        throw new UnsupportedOperationException("getNamedDispatcher operation is not supported!");
    }

    public String getRealPath(String path)
    {
        if ((contextDirectory == null) || (path == null))
            return null;
        else
            return (new File(contextDirectory, path)).getAbsolutePath();
    }

    /**
     *
     * Returns a {@link RequestDispatcher} object that acts
     * as a wrapper for the resource located at the given path.
     * A <code>RequestDispatcher</code> object can be used to forward
     * a request to the resource or to include the resource in a response.
     * The resource can be dynamic or static.
     *
     * <p>The pathname must begin with a "/" and is interpreted as relative
     * to the current context root.  Use <code>getContext</code> to obtain
     * a <code>RequestDispatcher</code> for resources in foreign contexts.
     * This method returns <code>null</code> if the <code>ServletContext</code>
     * cannot return a <code>RequestDispatcher</code>.
     *
     * @param urlpath      a <code>String</code> specifying the pathname
     *                  to the resource
     *
     * @return          a <code>RequestDispatcher</code> object
     *                  that acts as a wrapper for the resource
     *                  at the specified path
     *
     * @see             RequestDispatcher
     * @see             ServletContext#getContext
     *
     */
    public RequestDispatcher getRequestDispatcher(String urlpath)
    {
        dispatcher =  new RequestDispatcherSimulator(urlpath);
        return dispatcher;
    }

    /**
     * Returns the mock RequestDispatcher object used in this test.
     * The RequestDispatcherSimulator contains forwarding information
     * that can be used in test validation.
     */
    public RequestDispatcherSimulator getRequestDispatcherSimulator() {
        return dispatcher;
    }

    /**
     * TODO: add appropriate comments
     */
    public URL getResource(String path) throws MalformedURLException
    {
        try {
            File file = getResourceAsFile(path);

            if (file.exists()) {
                return file.toURL();
            }
            else {
                if(!path.startsWith("/")){
                    path = "/" + path;
                }
                return this.getClass().getResource(path);
            }
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Returns the resource located at the named path as
     * an <code>InputStream</code> object.
     *
     * <p>The data in the <code>InputStream</code> can be
     * of any type or length. The path must be specified according
     * to the rules given in <code>getResource</code>.
     * This method returns <code>null</code> if no resource exists at
     * the specified path.
     *
     * <p>Meta-information such as content length and content type
     * that is available via <code>getResource</code>
     * method is lost when using this method.
     *
     * <p>The servlet container must implement the URL handlers
     * and <code>URLConnection</code> objects necessary to access
     * the resource.
     *
     * <p>In this mock implementation, this method first looks for
     * the supplied pathname in the underlying filesystem; if it
     * does not exist there, the default Java classloader is used.
     *
     *
     * @param path     a <code>String</code> specifying the path
     *                  to the resource
     *
     * @return          the <code>InputStream</code> returned to the
     *                  servlet, or <code>null</code> if no resource
     *                  exists at the specified path
     *
     *
     */
    public InputStream getResourceAsStream(String path)
    {
        try {
            File file = getResourceAsFile(path);

            if (file.exists()) {
                return new FileInputStream(file);
            }
            else {
                if(!path.startsWith("/")){
                    path = "/" + path;
                }
                return this.getClass().getResourceAsStream(path);
            }
        } catch (Exception e) {
            System.out.println("caught error: " + e);
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Attempts to load a resource from the underlying file system
     * and return a file handle to it.
     * It first treats the path as an absolute path.  If no file is found,
     * it attempts to treat the path as relative to the context directory.
     * If no file is found, it attempts to treat the path as relative to
     * the current directory.
     * If all these options fail, the returned file will return false()
     * to calls to File.exists().
     * @param path the relative or context-relative path to the file
     * @return the refernce to the file (which may or may not exist)
     */
    public File getResourceAsFile(String path){
        File file = new File(path);

        // If the path is relative then apply the contextDirectory path if it exists.
        if (!file.exists())
        {
            if(!path.startsWith("/")){
                path = "/" + path;
            }
            if((getContextDirectory() != null))
            {
                file = new File(getContextDirectory().getAbsolutePath() + path);
            }else{
                //try using current directory
                file = new File(new File(".").getAbsolutePath() + path);
            }
        }
        return file;

    }

    /**
     * Unsupported in this version.
     */
    public Set getResourcePaths()
    {
        throw new UnsupportedOperationException("getResourcePaths operation is not supported!");
    }

    /**
     * Returns the name and version of the servlet container on which
     * the servlet is running.
     *
     * <p>The form of the returned string is
     * <i>servername</i>/<i>versionnumber</i>.
     * For example, the JavaServer Web Development Kit may return the string
     * <code>JavaServer Web Dev Kit/1.0</code>.
     *
     * <p>The servlet container may return other optional information
     * after the primary string in parentheses, for example,
     * <code>JavaServer Web Dev Kit/1.0 (JDK 1.1.6; Windows NT 4.0 x86)</code>.
     *
     *
     * @return          a <code>String</code> containing at least the
     *                  servlet container name and version number
     *
     */
    public String getServerInfo()
    {
        return "MockServletEngine/1.9.5";
    }

    /**
     * Unsupported in this version.
     */
    public Servlet getServlet(String name) throws ServletException
    {
        throw new UnsupportedOperationException("getServlet operation is not supported!");
    }

    /**
     * Unsupported in this version.
     */
    public String getServletContextName()
    {
        throw new UnsupportedOperationException("getServletContextName operation is not supported!");
    }

    /**
     * Unsupported in this version.
     */
    public Enumeration getServletNames()
    {
        throw new UnsupportedOperationException("getServletNames operation is not supported!");
    }

    /**
     * Unsupported in this version.
     */
    public Enumeration getServlets()
    {
        throw new UnsupportedOperationException("getServlets operation is not supported!");
    }

    /**
     * @deprecated  As of Java Servlet API 2.1, use
     *                      @link ServletContext.log(String message, Throwable throwable)
     *                      instead.
     *
     * <p>This method was originally defined to write an
     * exception's stack trace and an explanatory error message
     * to the servlet log file.
     *
     */
    public void log(Exception exception, String msg)
    {
        logger.info(msg + "\n" + exception.getClass() + " - " + exception.getMessage());
    }

    /**
     *
     * Writes the specified message to a servlet log file, which is usually
     * an event log. The message provides explanatory information about
     * an exception or error or an action the servlet engine takes. The name
     * and type of the servlet log file is specific to the servlet engine.
     *
     *
     * @param msg   a <code>String</code> specifying the explanatory
     *                      message to be written to the log file
     *
     */
    public void log(String msg)
    {
        logger.info(msg);
    }

    /**
     * Writes the stack trace and an explanatory message
     * for a given <code>Throwable</code> exception
     * to the servlet log file.  The name and type of the servlet log
     * file is specific to the servlet engine, but it is usually an event log.
     *
     *
     * @param message               a <code>String</code> that
     *                              describes the error or exception
     *
     * @param throwable     the <code>Throwable</code> error
     *                              or exception
     *
     */
    public void log(String message, Throwable throwable)
    {
        logger.info(message + "\n" + throwable.getClass() + " - " + throwable.getMessage());
    }

    /**
     * Removes the attribute with the given name from
     * the servlet context. After removal, subsequent calls to
     * {@link #getAttribute} to retrieve the attribute's value
     * will return <code>null</code>.

     * <p>If listeners are configured on the <code>ServletContext</code> the
     * container notifies them accordingly.

     *
     *
     * @param name      a <code>String</code> specifying the name
     *                  of the attribute to be removed
     *
     */
    public void removeAttribute(String name)
    {
        attributes.remove(name);
    }

    /**
     *
     * Binds an object to a given attribute name in this servlet context. If
     * the name specified is already used for an attribute, this
     * method will replace the attribute with the new to the new attribute.
     * <p>If listeners are configured on the <code>ServletContext</code> the
     * container notifies them accordingly.
     * <p>
     * If a null value is passed, the effect is the same as calling
     * <code>removeAttribute()</code>.
     *
     * <p>Attribute names should follow the same convention as package
     * names. The Java Servlet API specification reserves names
     * matching <code>java.*</code>, <code>javax.*</code>, and
     * <code>sun.*</code>.
     *
     *
     * @param name      a <code>String</code> specifying the name
     *                  of the attribute
     *
     * @param object    an <code>Object</code> representing the
     *                  attribute to be bound
     *
     *
     *
     */
    public void setAttribute(String name, Object object)
    {
        attributes.put(name,object);
    }

    /**
     * Unsupported in this version.
     */
    public Set getResourcePaths(String path) {
        throw new UnsupportedOperationException("getResourcePaths operation is not supported!");
    }

    /**
     * Sets the absolute context directory to be used in the getRealPath() method.
     * @param contextDirectory the absolute path of the root context directory for this application.
     */
    public void setContextDirectory(File contextDirectory) {
        this.contextDirectory = contextDirectory;
    }

    public File getContextDirectory() {
        return contextDirectory;
    }


}

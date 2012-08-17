/*
 * JBoss, Home of Professional Open Source
 * Copyright 2006, JBoss Inc., and individual contributors as indicated
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */

package org.jboss.web.php;

import java.io.IOException;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.UnavailableException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.catalina.Globals;
import org.apache.catalina.util.StringManager;

/**
 * Handler.
 *
 * @author Mladen Turk
 * @version $Revision$, $Date$
 * @since 1.0
 */
public class Handler extends HttpServlet
{

    /** the debugging detail level for this servlet. */
    private int debug = 0;

    /** Buffer size. */
    private int bufferSize = 4096;

    /**
     * The Servlet configuration object we are associated with.  If this value
     * is null, this filter instance is not currently configured.
     */
    private ServletConfig servletConfig = null;

    /**
     * The string manager for this package.
     */
    private StringManager sm =
        StringManager.getManager(Constants.Package);


    /** Are doing source sysntax highlight. */
    protected boolean syntaxHighlight = false;

    /** the encoding to use for parameters */
    private String parameterEncoding = System.getProperty("file.encoding",
                                                          "UTF-8");

    /**
     *  The Script search path will start at
     *    webAppRootDir + File.separator + scriptPathPrefix
     *    (or webAppRootDir alone if scriptPathPrefix is
     *    null)
     */
    private String scriptPathPrefix = null;

    /**
     * Sets instance variables.
     * <P>
     * Modified from Craig R. McClanahan's InvokerServlet
     * </P>
     *
     * @param config                    a <code>ServletConfig</code> object
     *                                  containing the servlet's
     *                                  configuration and initialization
     *                                  parameters
     *
     * @exception ServletException      if an exception has occurred that
     *                                  interferes with the servlet's normal
     *                                  operation
     */
    public void init(ServletConfig servletConfig)
        throws ServletException
    {
        super.init(servletConfig);

        if (!Library.isInitialized()) {
            // try to load the library.
            try {
                Library.initialize(null);
            } catch(Exception e) {
                e.printStackTrace();
            }
        }

        if (!Library.isInitialized())
            throw new UnavailableException
                (sm.getString("handler.missing"));

        this.servletConfig = servletConfig;

        // Verify that we were not accessed using the invoker servlet
        String servletName = servletConfig.getServletName();
        if (servletName == null)
            servletName = "";
        if (servletName.startsWith("org.apache.catalina.INVOKER."))
            throw new UnavailableException
                ("Cannot invoke Handler through the invoker");


        // Set our properties from the initialization parameters
        String value = null;
        try {
            value = servletConfig.getInitParameter("debug");
            debug = Integer.parseInt(value);
            scriptPathPrefix =
                servletConfig.getInitParameter("scriptPathPrefix");
            value = servletConfig.getInitParameter("bufferSize");
            if (value != null) {
                bufferSize = Integer.parseInt(value);
                if (bufferSize < 1024)
                    bufferSize = 1024;
                log("init: bufferSize set to " + bufferSize);
            }
        } catch (Throwable t) {
            // Nothing.
        }
        log("init: loglevel set to " + debug);

        value = servletConfig.getInitParameter("parameterEncoding");
        if (value != null) {
            parameterEncoding = value;
        }
    }

    /**
     * Finalize this servlet.
     */
    public void destroy()
    {
        this.servletConfig = null;
    }

    private static native int php(byte[] buf,
                                  ScriptEnvironment env,
                                  HttpServletRequest req,
                                  HttpServletResponse res,
                                  String requestMethod,
                                  String queryString,
                                  String contentType,
                                  String authUser,
                                  String requestURI,
                                  String pathTranslated,
                                  int contentLength,
                                  boolean syntaxHighlight);

    /**
     * Provides PHP Gateway service
     *
     * @param  req   HttpServletRequest passed in by servlet container
     * @param  res   HttpServletResponse passed in by servlet container
     *
     * @exception  ServletException  if a servlet-specific exception occurs
     * @exception  IOException  if a read/write exception occurs
     *
     * @see javax.servlet.http.HttpServlet
     *
     */
    protected void service(HttpServletRequest req, HttpServletResponse res)
        throws ServletException, IOException
    {

        // Verify that we were not accessed using the invoker servlet
        if (req.getAttribute(Globals.INVOKED_ATTR) != null)
            throw new UnavailableException
                ("Cannot invoke PHP Gateway Handler through the invoker");

        ScriptEnvironment env = new ScriptEnvironment(req,
                                                      getServletContext(),
                                                      scriptPathPrefix);
        if (env.isValid()) {
            byte[] buf = new byte[bufferSize];
            int rv = php(buf,
                         env,
                         req,
                         res,
                         req.getMethod(),
                         req.getQueryString(),
                         req.getContentType(),
                         req.getRemoteUser(),
                         req.getRequestURI(),
                         env.getFullPath(),
                         req.getContentLength(),
                         syntaxHighlight);
        }
        else {
            res.setStatus(HttpServletResponse.SC_NOT_FOUND);
        }
    }
    
    public static void log(Handler handler, String msg)
    {
        // TODO: Log the message    
    }
}

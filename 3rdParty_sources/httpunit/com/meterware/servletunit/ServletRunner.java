package com.meterware.servletunit;
/********************************************************************************************************************

*
* Copyright (c) 2000-2004, Russell Gold
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
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.util.Dictionary;
import java.util.Hashtable;

import com.meterware.httpunit.HttpUnitUtils;
import com.meterware.httpunit.WebRequest;
import com.meterware.httpunit.WebResponse;
import com.meterware.httpunit.FrameSelector;

import org.w3c.dom.Document;
import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.servlet.http.HttpSession;
import javax.xml.parsers.DocumentBuilder;


/**
 * This class acts as a test environment for servlets.
 *
 * @author <a href="mailto:russgold@httpunit.org">Russell Gold</a>
 **/
public class ServletRunner {

    /**
     * Default constructor, which defines no servlets.
     */
    public ServletRunner() {
        _application = new WebApplication();
        completeInitialization( null );
    }

    /**
     * Constructor which expects the full path to the web.xml for the
     * application.
     * @deprecated as of 1.6, use {@link #ServletRunner(File)}
     *
     * @param webXMLFileSpec the full path to the web.xml file
     */
    public ServletRunner( String webXMLFileSpec ) throws IOException, SAXException {
        _application = new WebApplication( HttpUnitUtils.newParser().parse( webXMLFileSpec ) );
        completeInitialization( null );
    }


    /**
     * Constructor which expects the full path to the web.xml for the
     * application and a context path under which to mount it.
     * @deprecated as of 1.6, use {@link #ServletRunner(File,String)}
     *
     * @param webXMLFileSpec the full path to the web.xml file
     * @param contextPath the context path
     */
    public ServletRunner( String webXMLFileSpec, String contextPath ) throws IOException, SAXException {
        this( new File( webXMLFileSpec ), contextPath );
    }


    /**
     * Constructor which expects a File object representing the web.xml for the
     * application.
     *
     * @param webXml the web.xml file
     * @since 1.6
     */
    public ServletRunner( File webXml ) throws IOException, SAXException {
        _application = new WebApplication( HttpUnitUtils.newParser().parse( webXml ) );
        completeInitialization( null );
    }


    /**
     * Constructor which expects a File object representing the web.xml for the
     * application and a context path under which to mount it.
     *
     * @param webXml the web.xml file
     * @param contextPath the context path
     * @since 1.6
     */
    public ServletRunner( File webXml, String contextPath ) throws IOException, SAXException {
        _application = new WebApplication( HttpUnitUtils.newParser().parse( webXml ), webXml.getParentFile().getParentFile(), contextPath );
        completeInitialization( contextPath );
    }
    
    /**
     * constructor with entity Resolver
     * as asked for in Bug report 1222269 by jim - jafergus 
     * @param webXMLFileSpec
     * @param resolver
     * @throws IOException
     * @throws SAXException
     * @since 1.7
     */
    public ServletRunner( String webXMLFileSpec,EntityResolver resolver )	throws IOException, SAXException	{
  		DocumentBuilder parser = HttpUnitUtils.newParser();
  		parser.setEntityResolver(resolver);
  		_application = new WebApplication( parser.parse(
  		webXMLFileSpec ) );
  		completeInitialization( null );
 		}

    /**
     * Constructor which expects an input stream containing the web.xml for the application.
     **/
    public ServletRunner( InputStream webXML ) throws IOException, SAXException {
        this( webXML, null );
    }

    /**
     * Constructor which expects an input stream containing the web.xml for the application.
     * @param webXML
     * @param contextPath
     * @throws IOException
     * @throws SAXException
     */
    public ServletRunner( InputStream webXML, String contextPath ) throws IOException, SAXException {
 			InputSource inputSource=new InputSource( webXML );
 			Document doc=HttpUnitUtils.parse(inputSource);
 			try {
 	 			_application = new WebApplication( doc, contextPath );
 	 			completeInitialization( contextPath );
 			} catch (java.net.MalformedURLException mue) {
 				throw mue;
 			}
    }


    /**
     * Registers a servlet class to be run.
     **/
    public void registerServlet( String resourceName, String servletClassName ) {
        _application.registerServlet( resourceName, servletClassName, null );
    }


    private void completeInitialization( String contextPath ) {
        _context = new ServletUnitContext( contextPath, _application.getServletContext(), _application );
        _application.registerServlet( "*.jsp", _jspServletDescriptor.getClassName(), _jspServletDescriptor.getInitializationParameters( null, null ) );
    }


    /**
     * Registers a servlet class to be run, specifying initialization parameters.
     **/
    public void registerServlet( String resourceName, String servletClassName, Hashtable initParameters ) {
        _application.registerServlet( resourceName, servletClassName, initParameters );
    }


    /**
     * Returns the response from the specified servlet.
     * @exception SAXException thrown if there is an error parsing the response
     **/
    public WebResponse getResponse( WebRequest request ) throws MalformedURLException, IOException, SAXException {
        return getClient().getResponse( request );
    }


    /**
     * Returns the response from the specified servlet using GET.
     * @exception SAXException thrown if there is an error parsing the response
     **/
    public WebResponse getResponse( String url ) throws MalformedURLException, IOException, SAXException {
        return getClient().getResponse( url );
    }


    /**
     * Returns the session to be used by the next request.
     * @param create if true, will create a new session if no valid session is defined.
     * @since 1.6
     */
    public HttpSession getSession( boolean create ) {
        return getClient().getSession( create );
    }


    /**
     * Returns the value of the named context parameter found in the application definition.
     * @param name - the name of the parameter to get
     * @return - the context parameter with the given name
     */
    public String getContextParameter( String name ) {
        Object value = _application.getContextParameters().get( name );
        return value == null ? null : value.toString();
    }

    /**
     * Sets a application context parameter.
     * @param name - the name of the parameter to set
     * @param value - the value of the parameter to set
     * @deprecated - test case for this function deactivated wf 2007-12-30
     */
    public void setContextParameter(String name, Object value){
     	getApplication().getServletContext().setAttribute(name, value);
    }

    /**
     * Shuts down the servlet container, returning any resources held by it.
     * Calls the destroy method of each active servlet, then notifies
     * ContextListeners of server shutdown.
     */
    public void shutDown() {
        _application.shutDown();
    }


    /**
     * Creates and returns a new web client that communicates with this servlet runner.
     **/
    public ServletUnitClient newClient() {
        return ServletUnitClient.newClient( _factory );
    }


    public static class JasperJSPServletDescriptor implements JSPServletDescriptor {

        public String getClassName() {
            return "org.apache.jasper.servlet.JspServlet";
        }


        public Hashtable getInitializationParameters( String classPath, String workingDirectory ) {
            Hashtable params = new Hashtable();
            if (classPath != null) params.put( "classpath", classPath );
            if (workingDirectory != null) params.put( "scratchdir", workingDirectory );
            return params;
        }
    }


    public final static JSPServletDescriptor JASPER_DESCRIPTOR = new JasperJSPServletDescriptor();


//-------------------------------------------- package methods ---------------------------------------------------------


    ServletUnitContext getContext() {
        return _context;
    }


    WebApplication getApplication() {
        return _application;
    }


//---------------------------- private members ------------------------------------

    private static JSPServletDescriptor _jspServletDescriptor = JASPER_DESCRIPTOR;

    private WebApplication     _application;

    private ServletUnitClient  _client;

    private ServletUnitContext _context;

    private InvocationContextFactory _factory = new InvocationContextFactory() {
        public InvocationContext newInvocation( ServletUnitClient client, FrameSelector targetFrame, WebRequest request, Dictionary clientHeaders, byte[] messageBody ) throws IOException, MalformedURLException {
            return new InvocationContextImpl( client, ServletRunner.this, targetFrame, request, clientHeaders, messageBody );
        }

        public HttpSession getSession( String sessionId, boolean create ) {
            return _context.getValidSession( sessionId, null, create );
        }
    };


    private ServletUnitClient getClient() {
        if (_client == null) _client = newClient();
        return _client;
    }


}

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

import junit.framework.AssertionFailedError;
import junit.framework.TestCase;
import org.apache.commons.digester.Digester;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.Globals;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionServlet;
import servletunit.HttpServletRequestSimulator;
import servletunit.HttpServletResponseSimulator;
import servletunit.ServletConfigSimulator;
import servletunit.ServletContextSimulator;

import javax.servlet.http.*;
import java.io.File;
import java.io.InputStream;
import java.net.URL;

/**
 * MockStrutsTestCase is an extension of the base JUnit testcase that
 * provides additional methods to aid in testing Struts Action
 * objects.  It uses a mock object approach to simulate a servlet
 * container, and tests the execution of Action objects as they
 * are actually run through the Struts ActionServlet.  MockStrutsTestCase
 * provides methods that set up the request path, request parameters
 * for ActionForm subclasses, as well as methods that can verify
 * that the correct ActionForward was used and that the proper
 * ActionError messages were supplied.
 *
 *<br><br>
 *<b>NOTE:</b> By default, the Struts ActionServlet will look for the
 * file <code>WEB-INF/struts-config.xml</code>, so you must place
 * the directory that <i>contains</i> WEB-INF in your CLASSPATH.  If
 * you would like to use an alternate configuration file, please see
 * the setConfigFile() method for details on how this file is located.
 */
public class MockStrutsTestCase extends TestCase {

    protected ActionServlet actionServlet;
    protected HttpServletRequestSimulator request;
    protected HttpServletResponseSimulator response;
    protected ServletContextSimulator context;
    protected ServletConfigSimulator config;
    protected String actionPath;
    protected boolean isInitialized = false;
    protected boolean actionServletIsInitialized = false;
    protected boolean requestPathSet = false;

    /**
     * The set of public identifiers, and corresponding resource names, for
     * the versions of the configuration file DTDs that we know about.  There
     * <strong>MUST</strong> be an even number of Strings in this list!
     */
    protected String registrations[] = {
        "-//Sun Microsystems, Inc.//DTD Web Application 2.2//EN",
        "/org/apache/struts/resources/web-app_2_2.dtd",
        "-//Sun Microsystems, Inc.//DTD Web Application 2.3//EN",
        "/org/apache/struts/resources/web-app_2_3.dtd"
    };


    protected static Log logger = LogFactory.getLog(MockStrutsTestCase.class);

    /**
     * Default constructor.
     */
    public MockStrutsTestCase() {
        super();
    }

    /**
     * Constructor that takes test name parameter, for backwards compatibility with older versions on JUnit.
     */
    public MockStrutsTestCase(String testName) {
        super(testName);
    }

    /**
     * A check that every method should run to ensure that the
     * base class setUp method has been called.
     */
    private void init() {
        if (!isInitialized)
            throw new AssertionFailedError("You are overriding the setUp() method without calling super.setUp().  You must call the superclass setUp() method in your TestCase subclass to ensure proper initialization.");
    }

    /**
     * Sets up the test fixture for this test.  This method creates
     * an instance of the ActionServlet, initializes it to validate
     * forms and turn off debugging, and creates a mock HttpServletRequest
     * and HttpServletResponse object to use in this test.
     */
    protected void setUp() throws Exception {
        if (logger.isDebugEnabled())
            logger.debug("Entering");
        if (actionServlet == null)
            actionServlet = new ActionServlet();
        config = new ServletConfigSimulator();
        request = new HttpServletRequestSimulator(config.getServletContext());
        response = new HttpServletResponseSimulator();
        context = (ServletContextSimulator) config.getServletContext();
        isInitialized = true;
        if (logger.isDebugEnabled())
            logger.debug("Exiting");
    }

    protected void tearDown() throws Exception {
        ActionServlet servlet = getActionServlet();
        servlet.destroy();
        setActionServlet(servlet);
    }

    /**
     * Returns an HttpServletRequest object that can be used in
     * this test.
     */
    public HttpServletRequest getRequest() {
        if (logger.isDebugEnabled())
            logger.debug("Entering");
        init();
        if (logger.isDebugEnabled())
            logger.debug("Exiting");
        return this.request;
    }

    /**
     * Clears all request parameters previously set.
     */
    public void clearRequestParameters() {
        if (logger.isTraceEnabled())
            logger.trace("Entering");
        this.request.getParameterMap().clear();

        // also, clear out the redirect header if it's there.
        response.removeHeader("Location");
        if (logger.isTraceEnabled())
            logger.trace("Exiting");
    }

    /**
     * Returns an HttpServletResponse object that can be used in
     * this test.
     */
    public HttpServletResponse getResponse() {
        if (logger.isDebugEnabled())
            logger.debug("Entering");
        init();
        if (logger.isDebugEnabled())
            logger.debug("Exiting");
        return this.response;
    }

    /**
     * Returns the mock HttpServletRequest object used in this test.  This allows
     * access to methods for setting up test preconditions that are otherwise
     * unavailable through the normal Servlet API.
     */
    public HttpServletRequestSimulator getMockRequest() {
        if (logger.isTraceEnabled())
            logger.trace("Entering");
        init();
        if (logger.isTraceEnabled())
            logger.trace("Exiting");
        return this.request;
    }

    /**
     * Returns the mock HttpServletResponse object used in this test.  This allows
     * access to methods for setting up test preconditions that are otherwise
     * unavailable through the normal Servlet API.
     */
    public HttpServletResponseSimulator getMockResponse() {
        if (logger.isTraceEnabled())
            logger.trace("Entering");
        init();
        if (logger.isTraceEnabled())
            logger.trace("Exiting");
        return this.response;
    }

    /**
     * Returns an HttpSession object that can be used in this
     * test.
     */
    public HttpSession getSession() {
        if (logger.isDebugEnabled())
            logger.debug("Entering");
        init();
        if (logger.isDebugEnabled())
            logger.debug("Exiting");
        return this.request.getSession(true);
    }

    /**
     * Returns the ActionServlet controller used in this
     * test.
     *
     */
    public ActionServlet getActionServlet() {
        if (logger.isDebugEnabled())
            logger.debug("Entering");
        init();
        try {
            if (!actionServletIsInitialized) {
                if (logger.isDebugEnabled()) {
                    logger.debug("intializing actionServlet");
                }
                this.actionServlet.init(config);
                actionServletIsInitialized = true;
            }
        } catch (Exception e) {
            logger.error("Error initializing action servlet",e);
            if(e.getMessage().equals("java.lang.NullPointerException")){
                String message = "Error initializing action servlet: Unable to find /WEB-INF/web.xml.  "
                               + "TestCase is running from " + System.getProperty("user.dir") + " directory.  "
                               + "Context directory ";
                if(this.context.getContextDirectory()==null){
                    message += "has not been set.  Try calling setContextDirectory() with a relative or absolute path";
                }else{
                    message = message + "is " + this.context.getContextDirectory().getAbsolutePath();
                }
                message = message + ".  /WEB-INF/web.xml must be found under the context directory, "
                        + "the directory the test case is running from, or in the classpath.";
                fail(message);
            }else{
                throw new AssertionFailedError(e.getMessage());
            }
        }
        if (logger.isDebugEnabled())
            logger.debug("Exiting");
        return actionServlet;
    }

    /**
     * Sets the ActionServlet to be used in this test execution.  This
     * method should only be used if you plan to use a customized
     * version different from that provided in the Struts distribution.
     */
    public void setActionServlet(ActionServlet servlet) {
        if (logger.isDebugEnabled())
            logger.debug("Entering - servlet = " + servlet);
        init();
        if (servlet == null)
            throw new AssertionFailedError("Cannot set ActionServlet to null");
        this.actionServlet = servlet;
        if (logger.isDebugEnabled())
            logger.debug("Exiting");
        actionServletIsInitialized = false;
    }

    /**
     * Executes the Action instance to be tested.  This method
     * calls the ActionServlet.doPost() method to execute the
     * Action instance to be tested, passing along any parameters
     * set in the HttpServletRequest object.  It stores any results
     * for further validation.
     *
     * @exception AssertionFailedError if there are any execution
     * errors while calling Action.execute()
     *
     */
    public void actionPerform() {
        if (logger.isDebugEnabled())
            logger.debug("Entering");
        if(!this.requestPathSet){
            throw new IllegalStateException("You must call setRequestPathInfo() prior to calling actionPerform().");
        }
        init();
        HttpServletRequest request = this.request;
        HttpServletResponse response = this.response;
        try {
            this.getActionServlet().doPost(request,response);
        }catch (NullPointerException npe) {
                String message = "A NullPointerException was thrown.  This may indicate an error in your ActionForm, or "
                               + "it may indicate that the Struts ActionServlet was unable to find struts config file.  "
                               + "TestCase is running from " + System.getProperty("user.dir") + " directory.  "
                               + "Context directory ";
                if(this.context.getContextDirectory()==null){
                    message += "has not been set.  Try calling setContextDirectory() with a relative or absolute path";
                }else{
                    message = message + "is " + this.context.getContextDirectory().getAbsolutePath();
                }
                message = message + ".  struts config file must be found under the context directory, "
                        + "the directory the test case is running from, or in the classpath.";
                throw new ExceptionDuringTestError(message, npe);
        }catch(Exception e){
                throw new ExceptionDuringTestError("An uncaught exception was thrown during actionExecute()", e);
        }
        if (logger.isDebugEnabled())
            logger.debug("Exiting");
    }

    /**
     * Adds an HttpServletRequest parameter to be used in setting up the
     * ActionForm instance to be used in this test.  Each parameter added
     * should correspond to an attribute in the ActionForm instance used
     * by the Action instance being tested.
     */
    public void addRequestParameter(String parameterName, String parameterValue)
    {
        if (logger.isDebugEnabled())
            logger.debug("Entering - parameterName = " + parameterName + ", parameterValue = " + parameterValue);
        init();
        this.request.addParameter(parameterName,parameterValue);
        if (logger.isDebugEnabled())
            logger.debug("Exiting");
    }

    /**
     * Adds an HttpServletRequest parameter that is an array of String values
     * to be used in setting up the ActionForm instance to be used in this test.
     * Each parameter added should correspond to an attribute in the ActionForm
     * instance used by the Action instance being tested.
     */
    public void addRequestParameter(String parameterName, String[] parameterValues)
    {
        if (logger.isDebugEnabled())
            logger.debug("Entering - parameterName = " + parameterName + ", parameteValue = " + parameterValues);
        init();
        this.request.addParameter(parameterName,parameterValues);
        if (logger.isDebugEnabled())
            logger.debug("Exiting");
    }

    /**
     * Sets the request path instructing the ActionServlet to used a
     * particual ActionMapping.
     *
     * @param pathInfo the request path to be processed.  This should
     * correspond to a particular action mapping, as would normally
     * appear in an HTML or JSP source file.
     */
    public void setRequestPathInfo(String pathInfo) {
        if (logger.isDebugEnabled())
            logger.debug("Entering - pathInfo = " + pathInfo);
        init();
        this.setRequestPathInfo("",pathInfo);
        if (logger.isDebugEnabled())
            logger.debug("Exiting");
    }

    /**
     * Sets the request path instructing the ActionServlet to used a
     * particual ActionMapping.  Also sets the ServletPath property
     * on the request.
     *
     * @param moduleName the name of the Struts sub-application with
     * which this request is associated, or null if it is the default
     * application.
     * @param pathInfo the request path to be processed.  This should
     * correspond to a particular action mapping, as would normally
     * appear in an HTML or JSP source file.  If this request is part
     * of a sub-application, the module name should not appear in the
     * request path.
     */
    public void setRequestPathInfo(String moduleName, String pathInfo) {
        if (logger.isDebugEnabled())
            logger.debug("Entering - moduleName = " + moduleName + ", pathInfo = " + pathInfo);
        init();
        this.actionPath = Common.stripActionPath(pathInfo);
        if (moduleName != null) {
            if (!moduleName.equals("")) {
                if (!moduleName.startsWith("/"))
                    moduleName = "/" + moduleName;
                if (!moduleName.endsWith("/"))
                    moduleName = moduleName + "/";
            }
            if (logger.isDebugEnabled()) {
                logger.debug("setting request attribute - name = " + Common.INCLUDE_SERVLET_PATH + ", value = " + moduleName);
            }
            this.request.setAttribute(Common.INCLUDE_SERVLET_PATH, moduleName);
        }
        this.request.setPathInfo(actionPath);
        this.requestPathSet = true;
        if (logger.isDebugEnabled())
            logger.debug("Exiting");
    }

    /**
     * Sets an initialization parameter on the
     * ActionServlet.  Allows you to simulate an init parameter
     * that would normally have been found in web.xml,
     * but is not available while testing with mock objects.
     * @param key the name of the initialization parameter
     * @param value the value of the intialization parameter
     */
    public void setInitParameter(String key, String value){
        if (logger.isDebugEnabled())
            logger.debug("Entering - key = " + key + ", value = " + value);
        init();
        config.setInitParameter(key, value);
        actionServletIsInitialized = false;
        if (logger.isDebugEnabled())
            logger.debug("Exiting");
    }

    /**
     * Sets the context directory to be used with the getRealPath() methods in
     * the ServletContext and HttpServletRequest API.
     * @param contextDirectory a File object representing the root context directory
     * for this application.
     */
    public void setContextDirectory(File contextDirectory) {
        if (logger.isDebugEnabled())
            logger.debug("Entering - contextDirectory = " + contextDirectory);
        init();
        context.setContextDirectory(contextDirectory);
        actionServletIsInitialized = false;
        if (logger.isDebugEnabled())
            logger.debug("Exiting");
    }

    /**
     * Sets the location of the Struts configuration file for the default module.
     * This method can take either an absolute path, or a relative path.  If an
     * absolute path is supplied, the configuration file will be loaded from the
     * underlying filesystem; otherwise, the ServletContext loader will be used.
     */
    public void setConfigFile(String pathname) {
        if (logger.isDebugEnabled())
            logger.debug("Entering - pathName = " + pathname);
        init();
        setConfigFile(null,pathname);
        if (logger.isDebugEnabled())
            logger.debug("Exiting");
    }

    /**
     * Sets the struts configuration file for a given sub-application. This method
     * can take either an absolute path, or a relative path.  If an absolute path
     * is supplied, the configuration file will be loaded from the underlying
     * filesystem; otherwise, the ServletContext loader will be used.
     *
     * @param moduleName the name of the sub-application, or null if this is the default application
     * @param pathname the location of the configuration file for this sub-application
     */
    public void setConfigFile(String moduleName, String pathname) {
        if (logger.isDebugEnabled())
            logger.debug("Entering - moduleName = " + moduleName + ", pathname =" + pathname);
        init();
        if (moduleName == null)
            this.config.setInitParameter("config",pathname);
        else
            this.config.setInitParameter("config/" + moduleName,pathname);
        actionServletIsInitialized = false;
        if (logger.isDebugEnabled())
            logger.debug("Exiting");
    }

    /**
     * Sets the location of the web.xml configuration file to be used
     * to set up the servlet context and configuration for this test.
     * This method supports both init-param and context-param tags,
     * setting the ServletConfig and ServletContext appropriately.
     * This method can take either an absolute path, or a relative path.  If an
     * absolute path is supplied, the configuration file will be loaded from the
     * underlying filesystem; otherwise, the ServletContext loader will be used.
     */
    public void setServletConfigFile(String pathname) {
        if (logger.isDebugEnabled())
            logger.debug("Entering - pathname = " + pathname);
        init();

        // pull in the appropriate parts of the
        // web.xml file -- first the init-parameters
        Digester digester = new Digester();
        digester.push(this.config);
        digester.setValidating(true);
        digester.addCallMethod("web-app/servlet/init-param", "setInitParameter", 2);
        digester.addCallParam("web-app/servlet/init-param/param-name", 0);
        digester.addCallParam("web-app/servlet/init-param/param-value", 1);
        try {
            for (int i = 0; i < registrations.length; i += 2) {
                URL url = context.getResource(registrations[i + 1]);
                if (url != null)
                    digester.register(registrations[i], url.toString());
            }
            InputStream input = context.getResourceAsStream(pathname);
            if(input==null)
                throw new AssertionFailedError("Invalid pathname: " + pathname);
            digester.parse(input);
            input.close();
        } catch (Exception e) {
            throw new AssertionFailedError("Received an exception while loading web.xml - " + e.getClass() + " : " + e.getMessage());
        }

        // now the context parameters..
        digester = new Digester();
        digester.setValidating(true);
        digester.push(this.context);
        digester.addCallMethod("web-app/context-param", "setInitParameter", 2);
        digester.addCallParam("web-app/context-param/param-name", 0);
        digester.addCallParam("web-app/context-param/param-value", 1);
        try {
            for (int i = 0; i < registrations.length; i += 2) {
                URL url = context.getResource(registrations[i + 1]);
                if (url != null)
                    digester.register(registrations[i], url.toString());
            }
            InputStream input = context.getResourceAsStream(pathname);
            if(input==null)
                throw new AssertionFailedError("Invalid pathname: " + pathname);
            digester.parse(input);
            input.close();
        } catch (Exception e) {
            throw new AssertionFailedError("Received an exception while loading web.xml - " + e.getClass() + " : " + e.getMessage());
        }
        actionServletIsInitialized = false;
        if (logger.isDebugEnabled())
            logger.debug("Exiting");
    }

    /**
     * Returns the forward sent to RequestDispatcher.
     */
    protected String getActualForward() {
        if (logger.isDebugEnabled())
            logger.debug("Entering");
        if (response.containsHeader("Location")) {
            return Common.stripJSessionID(response.getHeader("Location"));
        } else
            try  {
                String strippedForward = request.getContextPath() + Common.stripJSessionID(((ServletContextSimulator) config.getServletContext()).getRequestDispatcherSimulator().getForward());
                if (logger.isDebugEnabled()) {
                    logger.debug("stripped forward and added context path - " + strippedForward);
                }
                if (logger.isDebugEnabled())
                    logger.debug("Exiting");
                return strippedForward;
            } catch (NullPointerException npe) {
                if (logger.isDebugEnabled()) {
                    logger.debug("caught NullPointerException - returning null",npe);
                }
                return null;
            }
    }

    /**
     * Verifies if the ActionServlet controller used this forward.
     *
     * @param forwardName the logical name of a forward, as defined
     * in the Struts configuration file.  This can either refer to a
     * global forward, or one local to the ActionMapping.
     *
     * @exception AssertionFailedError if the ActionServlet controller
     * used a different forward than <code>forwardName</code> after
     * executing an Action object.
     */
    public void verifyForward(String forwardName) throws AssertionFailedError {
        if (logger.isDebugEnabled())
            logger.debug("Entering - forwardName = " + forwardName);
        init();
        Common.verifyForwardPath(actionPath,forwardName,getActualForward(),false,request,config.getServletContext(),config);
        if (logger.isDebugEnabled())
            logger.debug("Exiting");
    }

    /**
     * Verifies if the ActionServlet controller used this actual path
     * as a forward.
     *
     * @param forwardPath an absolute pathname to which the request
     * is to be forwarded.
     *
     * @exception AssertionFailedError if the ActionServlet controller
     * used a different forward path than <code>forwardPath</code> after
     * executing an Action object.
     */
    public void verifyForwardPath(String forwardPath) throws AssertionFailedError {
        if (logger.isDebugEnabled())
            logger.debug("Entering - forwardPath = " + forwardPath);
        init();
        String actualForward = getActualForward();
        if ((actualForward == null) && (forwardPath == null)) {
            // actions can send null forwards, which is fine.
            return;
        }

        forwardPath = request.getContextPath() + forwardPath;

        if (actualForward == null) {
            if (logger.isDebugEnabled()) {
                logger.debug("actualForward is null - this usually means it is not mapped properly.");
            }
            throw new AssertionFailedError("Was expecting '" + forwardPath + "' but it appears the Action has tried to return an ActionForward that is not mapped correctly.");
        }
        if (logger.isDebugEnabled()) {
            logger.debug("expected forward = '" + forwardPath + "' - actual forward = '" + actualForward + "'");
        }
        if (!(actualForward.equals(forwardPath)))
            throw new AssertionFailedError("was expecting '" + forwardPath + "' but received '" + actualForward + "'");
        if (logger.isDebugEnabled())
            logger.debug("Exiting");
    }

    /**
     * Verifies if the ActionServlet controller forwarded to the defined
     * input path.
     *
     * @exception AssertionFailedError if the ActionServlet controller
     * used a different forward than the defined input path after
     * executing an Action object.
     */
    public void verifyInputForward() {
        if (logger.isDebugEnabled())
            logger.debug("Entering");
        init();
        Common.verifyForwardPath(actionPath,null,getActualForward(),true,request,config.getServletContext(),config);
        if (logger.isDebugEnabled())
            logger.debug("Exiting");
    }

    /**
     * Verifies that the ActionServlet controller used this forward and Tiles definition.
     *
     * @param forwardName the logical name of a forward, as defined
     * in the Struts configuration file.  This can either refer to a
     * global forward, or one local to the ActionMapping.
     *
     * @param definitionName the name of a Tiles definition, as defined
     * in the Tiles configuration file.
     *
     * @exception AssertionFailedError if the ActionServlet controller
     * used a different forward or tiles definition than those given after
     * executing an Action object.
     */
    public void verifyTilesForward(String forwardName, String definitionName) {
        if (logger.isTraceEnabled())
            logger.trace("Entering - forwardName=" + forwardName + ", definitionName=" + definitionName);
        init();
        Common.verifyTilesForward(actionPath,forwardName,definitionName,false,request,config.getServletContext(),config);
        if (logger.isTraceEnabled())
            logger.trace("Exiting");
    }

    /**
     * Verifies that the ActionServlet controller forwarded to the defined
     * input Tiles definition.
     *
     * @param definitionName the name of a Tiles definition, as defined
     * in the Tiles configuration file.
     *
     * @exception AssertionFailedError if the ActionServlet controller
     * used a different forward than the defined input path after
     * executing an Action object.
     */
    public void verifyInputTilesForward(String definitionName) {
        if (logger.isTraceEnabled())
            logger.trace("Entering - definitionName=" + definitionName);
        init();
        Common.verifyTilesForward(actionPath,null,definitionName,true,request,config.getServletContext(),config);
        if (logger.isTraceEnabled())
            logger.trace("Exiting");
    }

    /**
     * Verifies if the ActionServlet controller sent these error messages.
     * There must be an exact match between the provided error messages, and
     * those sent by the controller, in both name and number.
     *
     * @param errorNames a String array containing the error message keys
     * to be verified, as defined in the application resource properties
     * file.
     *
     * @exception AssertionFailedError if the ActionServlet controller
     * sent different error messages than those in <code>errorNames</code>
     * after executing an Action object.
     */

    public void verifyActionErrors(String[] errorNames) {
        if (logger.isDebugEnabled())
            logger.debug("errorNames = " + errorNames);
        init();
        Common.verifyActionMessages(request,errorNames,Globals.ERROR_KEY,"error");
        if (logger.isDebugEnabled())
            logger.debug("Exiting");
    }


    /**
     * Verifies that the ActionServlet controller sent no error messages upon
     * executing an Action object.
     *
     * @exception AssertionFailedError if the ActionServlet controller
     * sent any error messages after excecuting and Action object.
     */
    public void verifyNoActionErrors() {
        if (logger.isDebugEnabled())
            logger.debug("Entering");
        init();
        Common.verifyNoActionMessages(request,Globals.ERROR_KEY,"error");
        if (logger.isDebugEnabled())
            logger.debug("Exiting");
    }

    /**
     * Verifies if the ActionServlet controller sent these action messages.
     * There must be an exact match between the provided action messages, and
     * those sent by the controller, in both name and number.
     *
     * @param messageNames a String array containing the action message keys
     * to be verified, as defined in the application resource properties
     * file.
     *
     * @exception AssertionFailedError if the ActionServlet controller
     * sent different action messages than those in <code>messageNames</code>
     * after executing an Action object.
     */
    public void verifyActionMessages(String[] messageNames) {
        if (logger.isDebugEnabled())
            logger.debug("Entering - messageNames = " + messageNames);
        init();
        Common.verifyActionMessages(request,messageNames,Globals.MESSAGE_KEY,"action");
        if (logger.isDebugEnabled())
            logger.debug("Exiting");
    }

    /**
     * Verifies that the ActionServlet controller sent no action messages upon
     * executing an Action object.
     *
     * @exception AssertionFailedError if the ActionServlet controller
     * sent any action messages after excecuting and Action object.
     */
    public void verifyNoActionMessages() {
        if (logger.isDebugEnabled())
            logger.debug("Entering");
        init();
        Common.verifyNoActionMessages(request,Globals.MESSAGE_KEY,"action");
        if (logger.isDebugEnabled())
            logger.debug("Exiting");
    }

    /**
     * Returns the ActionForm instance stored in either the request or session.  Note
     * that no form will be returned if the Action being tested cleans up the form
     * instance.
     *
     * @ return the ActionForm instance used in this test, or null if it does not exist.
     */
    public ActionForm getActionForm() {
        if (logger.isDebugEnabled())
            logger.debug("Entering");
        init();
        if (logger.isDebugEnabled())
            logger.debug("Exiting");
        return Common.getActionForm(actionPath,request,context);
    }

    /**
     * Sets an ActionForm instance to be used in this test.  The given ActionForm instance
     * will be stored in the scope specified in the Struts configuration file (ie: request
     * or session).  Note that while this ActionForm instance is passed to the test, Struts
     * will still control how it is used.  In particular, it will call the ActionForm.reset()
     * method, so if you override this method in your ActionForm subclass, you could potentially
     * reset attributes in the form passed through this method.
     *
     * @param form the ActionForm instance to be used in this test.
     */
    public void setActionForm(ActionForm form) {
        if (logger.isDebugEnabled())
            logger.debug("Entering - form = " + form);
        init();
        // make sure action servlet is intialized
        getActionServlet();
        Common.setActionForm(form,request,actionPath,context);
        if (logger.isDebugEnabled())
            logger.debug("Exiting");
    }




}


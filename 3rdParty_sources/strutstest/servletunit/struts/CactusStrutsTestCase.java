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
import org.apache.cactus.ServletTestCase;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.Globals;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionServlet;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletConfig;
import javax.servlet.http.*;
import java.util.Enumeration;
import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * CactusStrutsTestCase is an extension of the Cactus ServletTestCase
 * base class that provides additional methods to aid in testing
 * Struts Action objects.  It uses an in-container approach to run
 * the servlet container, and tests the execution of Action objects as they
 * are actually run through the Struts ActionServlet.  CactusStrutsTestCase
 * provides methods that set up the request path, request parameters
 * for ActionForm subclasses, as well as methods that can verify
 * that the correct ActionForward was used and that the proper
 * ActionError messages were supplied.
 * <br>
 * <br>
 * <b>Please note</b> that this class is meant to run in the Cactus
 * framework, and you must configure your test environment
 * accordingly.  Please see <a href="http://jakarta.apache.org/cactus">http://jakarta.apache.org/cactus</a>
 * for more details.
 *
 */
public class CactusStrutsTestCase extends ServletTestCase {

    protected ActionServlet actionServlet;
    protected HttpServletRequestWrapper requestWrapper;
    protected HttpServletResponseWrapper responseWrapper;
    protected boolean isInitialized = false;
    protected boolean actionServletIsInitialized = false;
    protected boolean requestPathIsSet = false;
    protected String moduleName;
    protected String servletMapping = "*.do";

    protected static Log logger = LogFactory.getLog(CactusStrutsTestCase.class);

    /**
     * Default constructor.
     */
    public CactusStrutsTestCase() {
        super();
    }

    /**
     * Constructor that takes test name parameter, for backwards compatibility with older versions on JUnit.
     */
    public CactusStrutsTestCase(String testName) {
        super(testName);
    }

    /**
     * A check that every method should run to ensure that the
     * base class setUp method has been called.
     */
    private void init() {
        if (!isInitialized) {
            throw new AssertionFailedError("You are overriding the setUp() method without calling super.setUp().  You must call the superclass setUp() and tearDown() methods in your TestCase subclass to ensure proper initialization.");
        }
    }

    /**
     * Sets up the test fixture for this test.  This method creates
     * an instance of the ActionServlet, initializes it to validate
     * forms and turn off debugging, and clears all other parameters.
     */
    protected void setUp() throws Exception {
        if (logger.isDebugEnabled())
            logger.debug("Entering");
        try {

            if (actionServlet == null)
                actionServlet = new ActionServlet();
            requestWrapper = null;
            responseWrapper = null;
            ServletContext servletContext = new StrutsServletContextWrapper(this.config.getServletContext());
            this.config = new StrutsServletConfigWrapper(this.config);
            ((StrutsServletConfigWrapper) this.config).setServletContext(servletContext);
            this.request = new StrutsRequestWrapper(this.request);
            this.response = new StrutsResponseWrapper(this.response);
            isInitialized = true;
            if (logger.isDebugEnabled())
                logger.debug("Exiting");
        } catch (Exception e) {
            if (logger.isDebugEnabled())
                logger.debug(e);
            throw new AssertionFailedError("Error trying to set up test fixture: " + e.getClass() + " - " + e.getMessage());
        }
    }

    /**
     * Sets the servlet mapping used to map requests to the Struts controller.  This is used to restore
     * proper setting after the test has been executed. By default, the stanard "*.do" mapping will be
     * restored, so only use this method for non-standard servlet mappings.
     * @param servletMapping
     */
    public void setServletMapping(String servletMapping) {
        this.servletMapping = servletMapping;
    }

    protected void tearDown () throws Exception {
        if (logger.isTraceEnabled())
            logger.trace("Entering");

        // remove all RequestProcessor instances from context
        // so that web application will function normally.
        ServletContext context = this.config.getServletContext();
        List nameList = new ArrayList();
        Enumeration attributeNames = context.getAttributeNames();
        while (attributeNames.hasMoreElements()) {
            String name = (String) attributeNames.nextElement();
            if (name.startsWith(Globals.REQUEST_PROCESSOR_KEY))
                nameList.add(name);
        }

        // restore the original servlet mapping
        ServletConfig originalConfig = ((org.apache.cactus.server.ServletConfigWrapper) this.config).getOriginalConfig();
        ServletContext originalContext = originalConfig.getServletContext();
        originalContext.setAttribute(Globals.SERVLET_KEY,servletMapping);
        ((StrutsServletConfigWrapper) config).setServletContext(originalContext);


        for (Iterator iterator = nameList.iterator(); iterator.hasNext();) {
            context.removeAttribute((String) iterator.next());
        }

        if (logger.isTraceEnabled())
            logger.trace("Exiting");
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
     * Returns a HttpServletRequestWrapper object that can be used
     * in this test. Note that if {@link #setRequestWrapper} has not been
     * called, this method will return an instance of
     * javax.servlet.http.HttpServletRequestWrapper.
     */
    public HttpServletRequestWrapper getRequestWrapper() {
        if (logger.isDebugEnabled())
            logger.debug("Entering");
        init();
        if (requestWrapper == null) {
            if (logger.isDebugEnabled())
                logger.debug("Exiting");
            return new HttpServletRequestWrapper(this.request);
        } else {
            if (logger.isDebugEnabled()) {
                logger.debug("wrapper class is '" + requestWrapper.getClass() + "'");
            }
            if (logger.isDebugEnabled())
                logger.debug("Exiting");
            return requestWrapper;
        }
    }

    /**
     * Set this TestCase to use a given HttpServletRequestWrapper
     * class when calling Action.execute().  Note that if this
     * method is not called, then the normal HttpServletRequest
     * object is used.
     *
     * @param wrapper an HttpServletRequestWrapper object to be
     * used when calling Action.execute().
     */
    public void setRequestWrapper(HttpServletRequestWrapper wrapper) {
        if (logger.isDebugEnabled())
            logger.debug("Entering - wrapper = " + wrapper);
        init();
        if (wrapper == null) {
            throw new IllegalArgumentException("wrapper class cannot be null!");
        } else {
            if (wrapper.getRequest() == null)
                wrapper.setRequest(this.request);
            this.requestWrapper = wrapper;
        }
        if (logger.isDebugEnabled())
            logger.debug("Exiting");
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
     * Returns an HttpServletResponseWrapper object that can be used in
     * this test.  Note that if {@link #setResponseWrapper} has not been
     * called, this method will return an instance of
     * javax.servlet.http.HttpServletResponseWrapper.
     */
    public HttpServletResponseWrapper getResponseWrapper() {
        if (logger.isDebugEnabled())
            logger.debug("Entering");
        init();
        if (responseWrapper == null) {
            if (logger.isDebugEnabled())
                logger.debug("Exiting getResponseWrapper()");
            return new HttpServletResponseWrapper(this.response);
        } else {
            if (logger.isDebugEnabled()) {
                logger.debug("wrapper class is '" + responseWrapper.getClass() + "'");
            }
            if (logger.isDebugEnabled())
                logger.debug("getResponseWrapper()");
            return responseWrapper;
        }
    }

    /**
     * Set this TestCase to use a given HttpServletResponseWrapper
     * class when calling Action.execute().  Note that if this
     * method is not called, then the normal HttpServletResponse
     * object is used.
     *
     * @param wrapper an HttpServletResponseWrapper object to be
     * used when calling Action.execute().
     */
    public void setResponseWrapper(HttpServletResponseWrapper wrapper) {
        if (logger.isDebugEnabled())
            logger.debug("Entering - wrapper = " + wrapper.getClass());
        init();
        if (wrapper == null)
            throw new IllegalArgumentException("wrapper class cannot be null!");
        else {
            if (wrapper.getResponse() == null)
                wrapper.setResponse(this.response);
            if (logger.isDebugEnabled())
                logger.debug("Exiting");
            this.responseWrapper = wrapper;
        }
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
        return this.session;
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
            logger.debug("Entering - paramaterName = " + parameterName + ", parameterValue = " + parameterValue);
        init();
        ((StrutsRequestWrapper) this.request).addParameter(parameterName,parameterValue);
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
            logger.debug("Entering - paramaterName = " + parameterName + ", parameterValue = " + parameterValues);
        init();
        ((StrutsRequestWrapper) this.request).addParameter(parameterName,parameterValues);
        if (logger.isDebugEnabled())
            logger.debug("Exiting");
    }

    /**
     * Clears all request parameters previously set.  NOTE: This will <strong>not</strong> clear
     * parameters set using Cactus beginXXX methods!
     */
    public void clearRequestParameters() {
        init();
        ((StrutsRequestWrapper) request).clearRequestParameters();
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
        ((StrutsRequestWrapper) this.request).setPathInfo(Common.stripActionPath(pathInfo));
        if (moduleName != null) {
            if (!moduleName.equals("")) {
                if (!moduleName.startsWith("/"))
                    moduleName = "/" + moduleName;
                if (!moduleName.endsWith("/"))
                    moduleName = moduleName + "/";
            }
            if (logger.isDebugEnabled()) {
                logger.debug("setting request.ServletPath value = " + moduleName);
            }
            ((StrutsRequestWrapper) this.request).setServletPath(moduleName);
            this.requestPathIsSet = true;
        }
        if (logger.isDebugEnabled())
            logger.debug("Exiting");
    }

    /**
     * Sets an initialization parameter on the
     * ActionServlet.  Allows you to simulate an init parameter
     * that would normally have been found in web.xml.
     * @param key the name of the initialization parameter
     * @param value the value of the intialization parameter
     */
    public void setInitParameter(String key, String value){
        if (logger.isDebugEnabled())
            logger.debug("Entering -  key = " + key + ", value = " + value);
        this.config.setInitParameter(key, value);
        this.actionServletIsInitialized = false;
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
            logger.debug("Entering - pathname = " + pathname);
        init();
        setConfigFile(null,pathname);
        if (logger.isDebugEnabled())
            logger.debug("Exiting");
    }

    /**
     * Sets the struts configuration file for a given sub-application.
     * This method can take either an absolute path, or a relative path.  If an
     * absolute path is supplied, the configuration file will be loaded from the
     * underlying filesystem; otherwise, the ServletContext loader will be used.
     *
     * @param moduleName the name of the sub-application, or null if this is the default application
     * @param pathname the location of the configuration file for this sub-application
     */
    public void setConfigFile(String moduleName, String pathname) {
        if (logger.isDebugEnabled())
            logger.debug("Entering - moduleName = " + moduleName + ", pathname = " + pathname);
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
                // the RequestProcessor holds on to the ServletContext, so
                // we need to ensure that it is replaced for each test.
                ServletContext context = config.getServletContext();
                String name = "org.apache.struts.action.REQUEST_PROCESSOR";

                // remove request processor for default module
                Object obj = context.getAttribute(name);
                if (obj != null) {
                    config.getServletContext().removeAttribute(name);
                }

                // remove request processor for sub-applications, if used.
                // todo: this seems pretty redundant.. may want to make this cleaner.
                String moduleName = request.getServletPath() != null ? request.getServletPath() : "";

                if (moduleName.endsWith("/"))
                    moduleName = moduleName.substring(0,moduleName.lastIndexOf("/"));

                obj = context.getAttribute(name + moduleName);
                if (obj != null) {
                    config.getServletContext().removeAttribute(name + moduleName);
                }

                this.actionServlet.init(config);
                actionServletIsInitialized = true;
            }
            if (logger.isDebugEnabled())
                logger.debug("Exiting");
            return this.actionServlet;
        } catch (ServletException e) {
            if (logger.isDebugEnabled())
                logger.debug("Error in getActionServlet()",e.getRootCause());
            throw new AssertionFailedError("Error while initializing ActionServlet: " + e.getMessage());
        }
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
        actionServletIsInitialized = false;
        if (logger.isDebugEnabled())
            logger.debug("Exiting");
    }

    /**
     * Executes the Action instance to be tested.  This method initializes
     * the ActionServlet, sets up and optionally validates the ActionForm
     * bean associated with the Action to be tested, and then calls the
     * Action.execute() method.  Results are stored for further validation.
     *
     * @exception AssertionFailedError if there are any execution
     * errors while calling Action.execute() or ActionForm.validate().
     *
     */
    public void actionPerform() {
        if (logger.isDebugEnabled())
            logger.debug("Entering");
        if(!this.requestPathIsSet){
            throw new IllegalStateException("You must call setRequestPathInfo() prior to calling actionPerform().");
        }
        init();
        try {
            HttpServletRequest request = this.request;
            HttpServletResponse response = this.response;
            // make sure errors are cleared from last test.
            request.removeAttribute(Globals.ERROR_KEY);
            request.removeAttribute(Globals.MESSAGE_KEY);

            if (this.requestWrapper != null)
                request = this.requestWrapper;
            if (this.responseWrapper != null)
                response = this.responseWrapper;

            ActionServlet actionServlet = this.getActionServlet();
            actionServlet.doPost(request,response);
            if (logger.isDebugEnabled())
                logger.debug("Exiting");
        } catch (NullPointerException npe) {
            String message = "A NullPointerException was thrown.  This may indicate an error in your ActionForm, or "
                    + "it may indicate that the Struts ActionServlet was unable to find struts config file.  "
                    + "TestCase is running from " + System.getProperty("user.dir") + " directory.";
            throw new ExceptionDuringTestError(message, npe);
        } catch(Exception e){
            throw new ExceptionDuringTestError("An uncaught exception was thrown during actionExecute()", e);
        }
    }

    /**
     * Returns the forward sent to RequestDispatcher.
     */
    protected String getActualForward() {
        if (logger.isDebugEnabled())
            logger.debug("Entering");
        if (response.containsHeader("Location")) {
            return Common.stripJSessionID(((StrutsResponseWrapper) response).getRedirectLocation());
        } else {
            String forward = ((StrutsServletContextWrapper) this.actionServlet.getServletContext()).getForward();
            if (logger.isDebugEnabled()) {
                logger.debug("actual forward = " + forward);
            }
            if (forward == null) {
                if (logger.isDebugEnabled())
                    logger.debug("Exiting");
                return null;
            } else {
                String temp = Common.stripJSessionID(forward);
                if (!temp.startsWith("/"))
                    temp = "/" + temp;
                String strippedForward = request.getContextPath() + temp;
                if (logger.isDebugEnabled()) {
                    logger.debug("stripped forward and added context path = " + strippedForward);
                }
                if (logger.isDebugEnabled())
                    logger.debug("Exiting");
                return strippedForward;
            }
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
        Common.verifyForwardPath(request.getPathInfo(),forwardName,getActualForward(),false,request,config.getServletContext(),config);
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
// actions can return null forwards.
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
        Common.verifyForwardPath(request.getPathInfo(),null,getActualForward(),true,request,config.getServletContext(),config);
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
        Common.verifyTilesForward(request.getPathInfo(),forwardName,definitionName,false,request,config.getServletContext(),config);
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
            logger.trace("Entering - defintionName=" + definitionName);
        init();
        Common.verifyTilesForward(request.getPathInfo(),null,definitionName,true,request,config.getServletContext(),config);
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
            logger.debug("Entering - errorNames = " + errorNames);
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
        return Common.getActionForm(request.getPathInfo(),request,config.getServletContext());
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
        // make sure ActionServlet is initialized.
        Common.setActionForm(form,request,request.getPathInfo(),config.getServletContext());
        if (logger.isDebugEnabled())
            logger.debug("Exiting");
    }

    /**
     * Instructs StrutsTestCase to fully process a forward request. By default, StrutsTestCase
     * stops processing a request as soon as the forward path has been collected, in order to avoid
     * side effects; calling this method overrides this behavior.
     * @param flag set to true to fully process forward requests
     */
    public void processRequest(boolean flag) {
        if (logger.isTraceEnabled())
            logger.trace("Entering - flag=" + flag);
        ((StrutsServletContextWrapper) this.config.getServletContext()).setProcessRequest(flag);
        if (logger.isTraceEnabled())
            logger.trace("Exiting");
    }

}


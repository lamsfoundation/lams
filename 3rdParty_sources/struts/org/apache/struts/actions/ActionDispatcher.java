/*
 *
 *
 * Copyright 2005-2006 The Apache Software Foundation.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.struts.actions;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.util.MessageResources;
import org.apache.struts.Globals;

/**
 * <p>Action <i>helper</i> class that dispatches to a public method in an Action.</p>
 * <p/>
 * <p>This class is provided as an alternative mechanism to using DispatchAction
 * and its various flavours and means <i>Dispatch</i> behaviour can be
 * easily implemented into any <code>Action</code> without having to
 * inherit from a particular super <code>Action</code>.</p>
 * <p/>
 * <p>To implement <i>dispatch</i> behaviour</i> in an <code>Action</code> class,
 * create your custom Action as follows, along with the methods you
 * require (and optionally "cancelled" and "unspecified" methods):</p>
 * <p/>
 * <pre>
 *   public class MyCustomAction extends Action {
 * 
 *       protected ActionDispatcher dispatcher
 *                = new ActionDispatcher(this, ActionDispatcher.MAPPING_FLAVOR);
 * 
 *       public ActionForward execute(ActionMapping mapping,
 *                                    ActionForm form,
 *                                    HttpServletRequest request,
 *                                    HttpServletResponse response)
 *                           throws Exception {
 *           return dispatcher.execute(mapping, form, request, response);
 *       }
 *   }
 * </pre>
 * <p/>
 * <p>It provides three flavours of determing the name of the method:</p>
 * <p/>
 * <ul>
 * <li><strong>{@link #DEFAULT_FLAVOR}</strong> - uses the parameter specified in
 * the struts-config.xml to get the method name from the Request
 * (equivalent to <code>DispatchAction</code> <b>except</b> uses "method"
 * as a default if the <code>parameter</code> is not specified
 * in the struts-config.xml).</li>
 * <p/>
 * <li><strong>{@link #DISPATCH_FLAVOR}</strong> - uses the parameter specified in
 * the struts-config.xml to get the method name from the Request
 * (equivalent to <code>DispatchAction</code>).</li>
 * <p/>
 * <li><strong>{@link #MAPPING_FLAVOR}</strong> - uses the parameter specified in
 * the struts-config.xml as the method name
 * (equivalent to <code>MappingDispatchAction</code>).</li>
 * <p/>
 * </ul>
 *
 * @since Struts 1.2.7
 * @version $Revision$ $Date$
 */
public class ActionDispatcher {


    // ----------------------------------------------------- Instance Variables

    /**
     * Indicates "default" dispatch flavor
     */
    public static final int DEFAULT_FLAVOR = 0;

    /**
     * Indicates "mapping" dispatch flavor
     */
    public static final int MAPPING_FLAVOR = 1;

    /**
     * Indicates flavor compatible with DispatchAction
     */
    public static final int DISPATCH_FLAVOR = 2;


    /**
     * The associated Action to dispatch to.
     */
    protected Action actionInstance;

    /**
     * Indicates dispatch <i>flavor</i>
     */
    protected int flavor;

    /**
     * The Class instance of this <code>DispatchAction</code> class.
     */
    protected Class clazz;

    /**
     * Commons Logging instance.
     */
    protected static Log log = LogFactory.getLog(ActionDispatcher.class);


    /**
     * The message resources for this package.
     */
    protected static MessageResources messages =
            MessageResources.getMessageResources
            ("org.apache.struts.actions.LocalStrings");


    /**
     * The set of Method objects we have introspected for this class,
     * keyed by method name.  This collection is populated as different
     * methods are called, so that introspection needs to occur only
     * once per method name.
     */
    protected HashMap methods = new HashMap();

    /**
     * The set of argument type classes for the reflected method call.  These
     * are the same for all calls, so calculate them only once.
     */
    protected Class[] types = {
        ActionMapping.class,
        ActionForm.class,
        HttpServletRequest.class,
        HttpServletResponse.class};

    // ----------------------------------------------------- Constructors

    public ActionDispatcher(Action actionInstance) {
        this(actionInstance, DEFAULT_FLAVOR);
    }


    public ActionDispatcher(Action actionInstance, int flavor) {

        this.actionInstance = actionInstance;
        this.flavor = flavor;

        clazz = actionInstance.getClass();

    }


    // --------------------------------------------------------- Public Methods


    /**
     * Process the specified HTTP request, and create the corresponding HTTP
     * response (or forward to another web component that will create it).
     * Return an <code>ActionForward</code> instance describing where and how
     * control should be forwarded, or <code>null</code> if the response has
     * already been completed.
     *
     * @param mapping  The ActionMapping used to select this instance
     * @param form     The optional ActionForm bean for this request (if any)
     * @param request  The HTTP request we are processing
     * @param response The HTTP response we are creating
     * @throws Exception if an exception occurs
     */
    public ActionForward execute(ActionMapping mapping,
                                 ActionForm form,
                                 HttpServletRequest request,
                                 HttpServletResponse response)
            throws Exception {

        // Process "cancelled"
        if (isCancelled(request)) {
            ActionForward af = cancelled(mapping, form, request, response);
            if (af != null) {
                return af;
            }
        }
        // Identify the request parameter containing the method name
        String parameter = getParameter(mapping, form, request, response);

        // Get the method's name. This could be overridden in subclasses.
        String name = getMethodName(mapping, form, request, response, parameter);


        // Prevent recursive calls
        if ("execute".equals(name) || "perform".equals(name)) {
            String message =
                    messages.getMessage("dispatch.recursive", mapping.getPath());

            log.error(message);
            throw new ServletException(message);
        }


        // Invoke the named method, and return the result
        return dispatchMethod(mapping, form, request, response, name);

    }


    /**
     * <p>Dispatches to the target class' <code>unspecified</code> method,  
     * if present, otherwise throws a ServletException. Classes utilizing 
     * <code>ActionDispatcher</code> should provide an <code>unspecified</code> 
     * method if they wish to provide behavior different than 
     * throwing a ServletException..</p>
     */
    protected ActionForward unspecified(ActionMapping mapping,
                                        ActionForm form,
                                        HttpServletRequest request,
                                        HttpServletResponse response)
            throws Exception {


        // Identify if there is an "unspecified" method to be dispatched to
        String name = "unspecified";
        Method method = null;
        try {
            method = getMethod(name);

        } catch (NoSuchMethodException e) {
            String message = messages.getMessage("dispatch.parameter",
                    mapping.getPath(),
                    mapping.getParameter());

            log.error(message);

            throw new ServletException(message);
        }

        return dispatchMethod(mapping, form, request, response, name, method);

    }

    /**
     * <p>Dispatches to the target class' cancelled method, if present, 
     * otherwise returns null. Classes utilizing <code>ActionDispatcher</code> 
     * should provide a <code>cancelled</code> method if they wish to provide 
     * behavior different than returning null.</p>
     */
    protected ActionForward cancelled(ActionMapping mapping,
                                      ActionForm form,
                                      HttpServletRequest request,
                                      HttpServletResponse response)
            throws Exception {

        // Identify if there is an "cancelled" method to be dispatched to
        String name = "cancelled";
        Method method = null;
        try {
            method = getMethod(name);

        } catch (NoSuchMethodException e) {
            return null;
        }

        return dispatchMethod(mapping, form, request, response, name, method);

    }

    // ----------------------------------------------------- Protected Methods


    /**
     * Dispatch to the specified method.
     */
    protected ActionForward dispatchMethod(ActionMapping mapping,
                                           ActionForm form,
                                           HttpServletRequest request,
                                           HttpServletResponse response,
                                           String name) throws Exception {

        // Make sure we have a valid method name to call.
        // This may be null if the user hacks the query string.
        if (name == null) {
            return this.unspecified(mapping, form, request, response);
        }

        // Identify the method object to be dispatched to
        Method method = null;
        try {
            method = getMethod(name);

        } catch (NoSuchMethodException e) {
            String message =
                    messages.getMessage("dispatch.method", mapping.getPath(), name);
            log.error(message, e);

            String userMsg =
                messages.getMessage("dispatch.method.user", mapping.getPath());
            throw new NoSuchMethodException(userMsg);
        }

        return dispatchMethod(mapping, form, request, response, name, method);

    }

    /**
     * Dispatch to the specified method.
     */
    protected ActionForward dispatchMethod(ActionMapping mapping,
                                           ActionForm form,
                                           HttpServletRequest request,
                                           HttpServletResponse response,
                                           String name,
                                           Method method) throws Exception {

        ActionForward forward = null;
        try {
            Object args[] = {mapping, form, request, response};
            forward = (ActionForward) method.invoke(actionInstance, args);

        } catch (ClassCastException e) {
            String message =
                    messages.getMessage("dispatch.return", mapping.getPath(), name);
            log.error(message, e);
            throw e;

        } catch (IllegalAccessException e) {
            String message =
                    messages.getMessage("dispatch.error", mapping.getPath(), name);
            log.error(message, e);
            throw e;

        } catch (InvocationTargetException e) {
            // Rethrow the target exception if possible so that the
            // exception handling machinery can deal with it
            Throwable t = e.getTargetException();
            if (t instanceof Exception) {
                throw ((Exception) t);
            } else {
                String message =
                        messages.getMessage("dispatch.error", mapping.getPath(), name);
                log.error(message, e);
                throw new ServletException(t);
            }
        }

        // Return the returned ActionForward instance
        return (forward);
    }


    /**
     * Introspect the current class to identify a method of the specified
     * name that accepts the same parameter types as the <code>execute</code>
     * method does.
     *
     * @param name Name of the method to be introspected
     * @throws NoSuchMethodException if no such method can be found
     */
    protected Method getMethod(String name)
            throws NoSuchMethodException {

        synchronized (methods) {
            Method method = (Method) methods.get(name);
            if (method == null) {
                method = clazz.getMethod(name, types);
                methods.put(name, method);
            }
            return (method);
        }

    }

    /**
     * <p>Returns the parameter value as influenced by the selected
     * {@link #flavor} specified for this <code>ActionDispatcher</code>.</p>
     *
     * @param mapping  The ActionMapping used to select this instance
     * @param form     The optional ActionForm bean for this request (if any)
     * @param request  The HTTP request we are processing
     * @param response The HTTP response we are creating
     * @return The <code>ActionMapping</code> parameter's value
     */
    protected String getParameter(ActionMapping mapping,
                                  ActionForm form,
                                  HttpServletRequest request,
                                  HttpServletResponse response)
            throws Exception {

        String parameter = mapping.getParameter();
        if ("".equals(parameter)) {
            parameter = null;
        }
        
        if ((parameter == null) && (flavor == DEFAULT_FLAVOR)) {
            // use "method" for DEFAULT_FLAVOR if no parameter was provided
            return "method";
        }

        if ((parameter == null) &&
                ((flavor == MAPPING_FLAVOR || flavor == DISPATCH_FLAVOR))) {
            String message =
                    messages.getMessage("dispatch.handler", mapping.getPath());

            log.error(message);

            throw new ServletException(message);
        }

        return parameter;

    }

    /**
     * Returns the method name, given a parameter's value.
     *
     * @param mapping   The ActionMapping used to select this instance
     * @param form      The optional ActionForm bean for this request (if any)
     * @param request   The HTTP request we are processing
     * @param response  The HTTP response we are creating
     * @param parameter The <code>ActionMapping</code> parameter's name
     * @return The method's name.
     */
    protected String getMethodName(ActionMapping mapping,
                                   ActionForm form,
                                   HttpServletRequest request,
                                   HttpServletResponse response,
                                   String parameter)
            throws Exception {


        // "Mapping" flavor, defaults to "method"
        if (flavor == MAPPING_FLAVOR) {
            return parameter;
        }

        // default behaviour
        return request.getParameter(parameter);
    }

    /**
     * <p>Returns <code>true</code> if the current form's cancel button was
     * pressed.  This method will check if the <code>Globals.CANCEL_KEY</code>
     * request attribute has been set, which normally occurs if the cancel
     * button generated by <strong>CancelTag</strong> was pressed by the user
     * in the current request.  If <code>true</code>, validation performed
     * by an <strong>ActionForm</strong>'s <code>validate()</code> method
     * will have been skipped by the controller servlet.</p>
     *
     * @param request The servlet request we are processing
     * @see org.apache.struts.taglib.html.CancelTag
     */
    protected boolean isCancelled(HttpServletRequest request) {

        return (request.getAttribute(Globals.CANCEL_KEY) != null);

    }

}


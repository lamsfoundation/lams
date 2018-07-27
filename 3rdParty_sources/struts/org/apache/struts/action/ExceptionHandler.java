/*
 *
 *
 * Copyright 2001-2004 The Apache Software Foundation.
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
 
package org.apache.struts.action;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.Globals;
import org.apache.struts.config.ExceptionConfig;
import org.apache.struts.util.MessageResources;
import org.apache.struts.util.ModuleException;

/**
 * <p>An <strong>ExceptionHandler</strong> is configured in the Struts
 * configuration file to handle a specific type of exception thrown
 * by an <code>Action.execute</code> method.</p>
 * 
 * @since Struts 1.1
 */
public class ExceptionHandler {
    

    /**
     * <p>Commons logging instance.</p>
     */
    private static final Log log = LogFactory.getLog(ExceptionHandler.class);
    

    /**
     * <p>The message resources for this package.</p>
     */
    private static MessageResources messages =
        MessageResources.getMessageResources(
            "org.apache.struts.action.LocalStrings");
    

    /**
     * <p>Handle the <code>Exception</code>.
     * Return the <code>ActionForward</code> instance (if any) returned by
     * the called <code>ExceptionHandler</code>.
     *
     * @param ex The exception to handle
     * @param ae The ExceptionConfig corresponding to the exception
     * @param mapping The ActionMapping we are processing
     * @param formInstance The ActionForm we are processing
     * @param request The servlet request we are processing
     * @param response The servlet response we are creating
     *
     * @exception ServletException if a servlet exception occurs
     *
     * @since Struts 1.1
     */
    public ActionForward execute(
        Exception ex,
        ExceptionConfig ae,
        ActionMapping mapping,
        ActionForm formInstance,
        HttpServletRequest request,
        HttpServletResponse response)
        throws ServletException {

        ActionForward forward = null;
        ActionMessage error = null;
        String property = null;

        // Build the forward from the exception mapping if it exists
        // or from the form input
        if (ae.getPath() != null) {
            forward = new ActionForward(ae.getPath());
        } else {
            forward = mapping.getInputForward();
        }

        // Figure out the error
        if (ex instanceof ModuleException) {
            error = ((ModuleException) ex).getActionMessage();
            property = ((ModuleException) ex).getProperty();
        } else {
            error = new ActionMessage(ae.getKey(), ex.getMessage());
            property = error.getKey();
        }

        this.logException(ex);

        // Store the exception
        request.setAttribute(Globals.EXCEPTION_KEY, ex);
        this.storeException(request, property, error, forward, ae.getScope());

        return forward;

    }


    /**
     * <p>Logs the <code>Exception</code> using commons-logging.</p>
     * @param e The Exception to log.
     * @since Struts 1.2
     */
    protected void logException(Exception e){

        log.debug(messages.getMessage("exception.log"), e);

    }


    /**
     * <p>Default implementation for handling an <code>ActionError</code> generated
     * from an <code>Exception</code> during <code>Action</code> delegation. The default
     * implementation is to set an attribute of the request or session, as
     * defined by the scope provided (the scope from the exception mapping). An
     * <code>ActionErrors</code> instance is created, the error is added to the collection
     * and the collection is set under the <code>Globals.ERROR_KEY</code>.</p>
     *
     * @param request The request we are handling
     * @param property The property name to use for this error
     * @param error The error generated from the exception mapping
     * @param forward The forward generated from the input path (from the form or exception mapping)
     * @param scope The scope of the exception mapping.
     * @deprecated Use storeException(HttpServletRequest, String, ActionMessage, ActionForward, String)
     * instead. This will be removed after Struts 1.2.
     */
    protected void storeException(
        HttpServletRequest request,
        String property,
        ActionError error,
        ActionForward forward,
        String scope) {

        this.storeException(request, property, (ActionMessage) error, forward, scope);
        // :TODO: Remove after Struts 1.2

    }


    /**
     * <p>Default implementation for handling an <code>ActionMessage</code> generated
     * from an <code>Exception</code> during <code>Action</code> delegation. The default
     * implementation is to set an attribute of the request or session, as
     * defined by the scope provided (the scope from the exception mapping). An
     * <code>ActionMessages</code> instance is created, the error is added to the
     * collection and the collection is set under the <code>Globals.ERROR_KEY</code>.</p>
     *
     * @param request The request we are handling
     * @param property The property name to use for this error
     * @param error The error generated from the exception mapping
     * @param forward The forward generated from the input path (from the form or exception mapping)
     * @param scope The scope of the exception mapping.
     * @since Struts 1.2
     */
    protected void storeException(
        HttpServletRequest request,
        String property,
        ActionMessage error,
        ActionForward forward,
        String scope) {

        ActionMessages errors = new ActionMessages();
        errors.add(property, error);

        if ("request".equals(scope)) {
            request.setAttribute(Globals.ERROR_KEY, errors);
        } else {
            request.getSession().setAttribute(Globals.ERROR_KEY, errors);
        }
    }

}


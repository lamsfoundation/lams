/*
 *
 *
 * Copyright 2006 The Apache Software Foundation.
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

import java.util.StringTokenizer;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionForward;

/**
 * <p>An <strong>Action</strong> that dispatches to to one of the public methods
 * that are named in the <code>parameter</code> attribute of the corresponding
 * ActionMapping and matches a submission parameter. This is useful for
 * developers who prefer to use many submit buttons, images, or submit links
 * on a single form and whose related actions exist in a single Action class.</p>
 *
 * <p>The method(s) must have the same signature (other than method name) of the
 * standard Action.execute method.</p>
 *
 * <p>To configure the use of this action in your
 * <code>struts-config.xml</code> file, create an entry like this:</p>
 *
 * <pre><code>
 *   &lt;action path="/saveSubscription"
 *           type="org.example.SubscriptionAction"
 *           name="subscriptionForm"
 *          scope="request"
 *          input="/subscription.jsp"
 *      parameter="save,back,recalc=recalculate,default=save"/&gt;
 * </code></pre>
 *
 * <p>where <code>parameter</code> contains three possible methods and one
 * default method if nothing matches (such as the user pressing the enter key).</p>
 *
 * <p>For utility purposes, you can use the <code>key=value</code> notation to
 * alias methods so that they are exposed as different form element names, in the
 * event of a naming conflict or otherwise. In this example, the <em>recalc</em>
 * button (via a request parameter) will invoke the <code>recalculate</code>
 * method. The security-minded person may find this feature valuable to
 * obfuscate and not expose the methods.</p>
 *
 * <p>The <em>default</em> key is purely optional. If this is not specified
 * and no parameters match the list of method keys, <code>null</code> is
 * returned which means the <code>unspecified</code> method will be invoked.</p>
 *
 * <p>The order of the parameters are guaranteed to be iterated in the order
 * specified. If multiple buttons were accidently submitted, the first match in
 * the list will be dispatched.</p>
 *
 * @since Struts 1.2.9
 */
public class EventDispatchAction extends DispatchAction {

    /**
     * Commons Logging instance.
     */
    private static final Log LOG = LogFactory.getLog(EventDispatchAction.class);

    /**
     * The method key, if present, to use if other specified method keys
     * do not match a request parameter.
     */
    private static final String DEFAULT_METHOD_KEY = "default";

    // --------------------------------------------------------- Protected Methods

    /**
     * Method which is dispatched to when there is no value for specified
     * request parameter included in the request.  Subclasses of
     * <code>DispatchAction</code> should override this method if they wish to
     * provide default behavior different than throwing a ServletException.
     *
     * @param mapping  The ActionMapping used to select this instance
     * @param form     The optional ActionForm bean for this request (if any)
     * @param request  The non-HTTP request we are processing
     * @param response The non-HTTP response we are creating
     * @return The forward to which control should be transferred, or
     *         <code>null</code> if the response has been completed.
     * @throws Exception if the application business logic throws an
     *                   exception.
     */
    protected ActionForward unspecified(ActionMapping mapping, ActionForm form,
        HttpServletRequest request, HttpServletResponse response)
        throws Exception {
        String message =
            messages.getMessage("event.parameter", mapping.getPath(),
                mapping.getParameter());

        LOG.error(message + " " + mapping.getParameter());

        throw new ServletException(message);
    }

    /**
     * Returns the method name, given a parameter's value.
     *
     * @param mapping   The ActionMapping used to select this instance
     * @param form      The optional ActionForm bean for this request (if
     *                  any)
     * @param request   The HTTP request we are processing
     * @param response  The HTTP response we are creating
     * @param parameter The <code>ActionMapping</code> parameter's name
     * @return The method's name.
     * @throws Exception if an error occurs.
     */
    protected String getMethodName(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response,
            String parameter) throws Exception {

        StringTokenizer st = new StringTokenizer(parameter, ",");
        String defaultMethodName = null;

        while (st.hasMoreTokens()) {
            String methodKey = st.nextToken().trim();
            String methodName = methodKey;

            // The key can either be a direct method name or an alias
            // to a method as indicated by a "key=value" signature
            int equals = methodKey.indexOf('=');
            if (equals > -1) {
                methodName = methodKey.substring(equals + 1).trim();
                methodKey = methodKey.substring(0, equals).trim();
            }

            // Set the default if it passes by
            if (methodKey.equals(DEFAULT_METHOD_KEY)) {
                defaultMethodName = methodName;
            }

            // If the method key exists as a standalone parameter or with
            // the image suffixes (.x/.y), the method name has been found.
            if ((request.getParameter(methodKey) != null)
                  || (request.getParameter(methodKey + ".x") != null)) {
                return methodName;
            }
        }

        return defaultMethodName;
    }
}

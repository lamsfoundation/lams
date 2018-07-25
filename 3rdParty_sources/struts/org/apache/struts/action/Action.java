/*
 *
 *
 * Copyright 2000-2005 The Apache Software Foundation.
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

import java.util.Locale;

import javax.servlet.ServletContext;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.DataSource;

import org.apache.struts.Globals;
import org.apache.struts.config.ModuleConfig;
import org.apache.struts.util.MessageResources;
import org.apache.struts.util.ModuleUtils;
import org.apache.struts.util.RequestUtils;
import org.apache.struts.util.TokenProcessor;

/**
 * <p>An <strong>Action</strong> is an adapter between the contents of an incoming
 * HTTP request and the corresponding business logic that should be executed to
 * process this request. The controller (RequestProcessor) will select an
 * appropriate Action for each request, create an instance (if necessary),
 * and call the <code>execute</code> method.</p>
 *
 * <p>Actions must be programmed in a thread-safe manner, because the
 * controller will share the same instance for multiple simultaneous
 * requests. This means you should design with the following items in mind:
 * </p>
 * <ul>
 * <li>Instance and static variables MUST NOT be used to store information
 *     related to the state of a particular request. They MAY be used to
 *     share global resources across requests for the same action.</li>
 * <li>Access to other resources (JavaBeans, session variables, etc.) MUST
 *     be synchronized if those resources require protection. (Generally,
 *     however, resource classes should be designed to provide their own
 *     protection where necessary.</li>
 * </ul>
 *
 * <p>When an <code>Action</code> instance is first created, the controller
 * will call <code>setServlet</code> with a non-null argument to
 * identify the servlet instance to which this Action is attached.
 * When the servlet is to be shut down (or restarted), the
 * <code>setServlet</code> method will be called with a <code>null</code>
 * argument, which can be used to clean up any allocated resources in use
 * by this Action.</p>
 *
 * @version $Rev: 164530 $ $Date$
 */
public class Action {

    /**
     * <p>An instance of <code>TokenProcessor</code> to use for token functionality.</p>
     */
    private static TokenProcessor token = TokenProcessor.getInstance();
    // :TODO: We can make this variable protected and remove Action's token methods
    // or leave it private and allow the token methods to delegate their calls.


    // ----------------------------------------------------- Instance Variables


    /**
     * <p>The system default Locale.</p>
     *
     * @deprecated Use Locale.getDefault directly.  This will be removed after
     * Struts 1.2.
     */
    protected static Locale defaultLocale = Locale.getDefault();
    // :TODO: Remove after Struts 1.2


    /**
     * <p>The servlet to which we are attached.</p>
     */
    protected ActionServlet servlet = null;


    // ------------------------------------------------------------- Properties


    /**
     * <p>Return the servlet instance to which we are attached.</p>
     */
    public ActionServlet getServlet() {

        return (this.servlet);

    }


    /**
     * <p>Set the servlet instance to which we are attached (if
     * <code>servlet</code> is non-null), or release any allocated resources
     * (if <code>servlet</code> is null).</p>
     *
     * @param servlet The new controller servlet, if any
     */
    public void setServlet(ActionServlet servlet) {

        this.servlet = servlet;
        // :FIXME: Is this suppose to release resources?


    }


    // --------------------------------------------------------- Public Methods


    /**
     * <p>Process the specified non-HTTP request, and create the
     * corresponding non-HTTP response (or forward to another web
     * component that will create it), with provision for handling
     * exceptions thrown by the business logic.
     * Return an {@link ActionForward} instance describing where and how
     * control should be forwarded, or <code>null</code> if the response has
     * already been completed.</p>
     *
     * <p>The default implementation attempts to forward to the HTTP
     * version of this method.</p>
     *
     * @param mapping The ActionMapping used to select this instance
     * @param form The optional ActionForm bean for this request (if any)
     * @param request The non-HTTP request we are processing
     * @param response The non-HTTP response we are creating
     *
     * @exception Exception if the application business logic throws
     *  an exception.
     * @since Struts 1.1
     */
    public ActionForward execute(
        ActionMapping mapping,
        ActionForm form,
        ServletRequest request,
        ServletResponse response)
        throws Exception {

        try {
            return execute(
                mapping,
                form,
                (HttpServletRequest) request,
                (HttpServletResponse) response);

        } catch (ClassCastException e) {
            return null;
        }

    }


    /**
     * <p>Process the specified HTTP request, and create the corresponding HTTP
     * response (or forward to another web component that will create it),
     * with provision for handling exceptions thrown by the business logic.
     * Return an {@link ActionForward} instance describing where and how
     * control should be forwarded, or <code>null</code> if the response
     * has already been completed.</p>
     *
     * @param mapping The ActionMapping used to select this instance
     * @param form The optional ActionForm bean for this request (if any)
     * @param request The HTTP request we are processing
     * @param response The HTTP response we are creating
     *
     * @exception Exception if the application business logic throws
     *  an exception
     * @since Struts 1.1
     */
    public ActionForward execute(
        ActionMapping mapping,
        ActionForm form,
        HttpServletRequest request,
        HttpServletResponse response)
        throws Exception {

        return null;

    }


    // ---------------------------------------------------- Protected Methods


	/**
	 * Adds the specified messages keys into the appropriate request
	 * attribute for use by the &lt;html:messages&gt; tag (if
	 * messages="true" is set), if any messages are required.
	 * Initialize the attribute if it has not already been.
	 * Otherwise, ensure that the request attribute is not set.
	 *
	 * @param request   The servlet request we are processing
	 * @param messages  Messages object
	 * @since Struts 1.2.1
	 */
	protected void addMessages(
		HttpServletRequest request,
		ActionMessages messages) {

		if (messages == null){
			//	bad programmer! *slap*
			return;
		}

		// get any existing messages from the request, or make a new one
		ActionMessages requestMessages = (ActionMessages) request.getAttribute(Globals.MESSAGE_KEY);
		if (requestMessages == null){
			requestMessages = new ActionMessages();
		}
		// add incoming messages
		requestMessages.add(messages);

		// if still empty, just wipe it out from the request
		if (requestMessages.isEmpty()) {
			request.removeAttribute(Globals.MESSAGE_KEY);
			return;
		}

		// Save the messages
		request.setAttribute(Globals.MESSAGE_KEY, requestMessages);
	}


	/**
	 * Adds the specified errors keys into the appropriate request attribute
     * for use by the &lt;html:errors&gt; tag, if any messages are required.
	 * Initialize the attribute if it has not already been. Otherwise, ensure
     * that the request attribute is not set.
	 *
	 * @param request   The servlet request we are processing
	 * @param errors  Errors object
	 * @since Struts 1.2.1
	 */
	protected void addErrors(
		HttpServletRequest request,
		ActionMessages errors) {

		if (errors == null){
			//	bad programmer! *slap*
			return;
		}

		// get any existing errors from the request, or make a new one
		ActionMessages requestErrors = (ActionMessages)request.getAttribute(Globals.ERROR_KEY);
		if (requestErrors == null){
			requestErrors = new ActionMessages();
		}
		// add incoming errors
		requestErrors.add(errors);

		// if still empty, just wipe it out from the request
		if (requestErrors.isEmpty()) {
			request.removeAttribute(Globals.ERROR_KEY);
			return;
		}

		// Save the errors
		request.setAttribute(Globals.ERROR_KEY, requestErrors);
	}


    /**
     * <p>Generate a new transaction token, to be used for enforcing a single
     * request for a particular transaction.</p>
     *
     * @param request The request we are processing
     */
    protected String generateToken(HttpServletRequest request) {
        return token.generateToken(request);
    }


    /**
     * <p>Return the default data source for the current module.</p>
     *
     * @param request The servlet request we are processing
     *
     * @since Struts 1.1
     */
    protected DataSource getDataSource(HttpServletRequest request) {

        return (getDataSource(request, Globals.DATA_SOURCE_KEY));

    }



    /**
     * <p>Return the specified data source for the current module.</p>
     *
     * @param request The servlet request we are processing
     * @param key     The key specified in the <code>&lt;data-sources&gt;</code>
     *                element.
     *
     * @since Struts 1.1
     */
    protected DataSource getDataSource(HttpServletRequest request, String key) {

        // Identify the current module
        ServletContext context = getServlet().getServletContext();
        ModuleConfig moduleConfig =
            ModuleUtils.getInstance().getModuleConfig(request, context);

        return (DataSource) context.getAttribute(key + moduleConfig.getPrefix());
    }


    /**
     * Retrieves any existing errors placed in the request by previous actions.  This method could be called instead
     * of creating a <code>new ActionMessages()<code> at the beginning of an <code>Action<code>
     * This will prevent saveErrors() from wiping out any existing Errors
     *
     * @return the Errors that already exist in the request, or a new ActionMessages object if empty.
     * @param request The servlet request we are processing
     * @since Struts 1.2.1
     */
    protected ActionMessages getErrors(HttpServletRequest request) {
        ActionMessages errors =
            (ActionMessages) request.getAttribute(Globals.ERROR_KEY);
        if (errors == null) {
            errors = new ActionMessages();
        }
        return errors;
    }


    /**
     * <p>Return the user's currently selected Locale.</p>
     *
     * @param request The request we are processing
     */
    protected Locale getLocale(HttpServletRequest request) {

        return RequestUtils.getUserLocale(request, null);

    }


	/**
	 * Retrieves any existing messages placed in the request by previous actions.  This method could be called instead
	 * of creating a <code>new ActionMessages()<code> at the beginning of an <code>Action<code>
	 * This will prevent saveMessages() from wiping out any existing Messages
	 *
	 * @return the Messages that already exist in the request, or a new ActionMessages object if empty.
	 * @param request The servlet request we are processing
     * @since Struts 1.2.1
	 */
	protected ActionMessages getMessages(HttpServletRequest request) {
		ActionMessages messages =
			(ActionMessages) request.getAttribute(Globals.MESSAGE_KEY);
		if (messages == null) {
			messages = new ActionMessages();
		}
		return messages;
	}


    /**
     * <p>Return the default message resources for the current module.</p>
     *
     * @param request The servlet request we are processing
     * @since Struts 1.1
     */
    protected MessageResources getResources(HttpServletRequest request) {

        return ((MessageResources) request.getAttribute(Globals.MESSAGES_KEY));

    }


    /**
     * <p>Return the specified message resources for the current module.</p>
     *
     * @param request The servlet request we are processing
     * @param key The key specified in the
     *  <code>&lt;message-resources&gt;</code> element for the
     *  requested bundle
     *
     * @since Struts 1.1
     */
    protected MessageResources getResources(
        HttpServletRequest request,
        String key) {

        // Identify the current module
        ServletContext context = getServlet().getServletContext();
        ModuleConfig moduleConfig =
            ModuleUtils.getInstance().getModuleConfig(request, context);

        // Return the requested message resources instance
        return (MessageResources) context.getAttribute(
            key + moduleConfig.getPrefix());

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


    /**
     * <p>Return <code>true</code> if there is a transaction token stored in
     * the user's current session, and the value submitted as a request
     * parameter with this action matches it. Returns <code>false</code>
     * under any of the following circumstances:</p>
     * <ul>
     * <li>No session associated with this request</li>
     * <li>No transaction token saved in the session</li>
     * <li>No transaction token included as a request parameter</li>
     * <li>The included transaction token value does not match the
     *     transaction token in the user's session</li>
     * </ul>
     *
     * @param request The servlet request we are processing
     */
    protected boolean isTokenValid(HttpServletRequest request) {

        return token.isTokenValid(request, false);

    }


    /**
     * <p>Return <code>true</code> if there is a transaction token stored in
     * the user's current session, and the value submitted as a request
     * parameter with this action matches it. Returns <code>false</code> under
     * any of the following circumstances:</p>
     * <ul>
     * <li>No session associated with this request</li>
     * <li>No transaction token saved in the session</li>
     * <li>No transaction token included as a request parameter</li>
     * <li>The included transaction token value does not match the
     *     transaction token in the user's session</li>
     * </ul>
     *
     * @param request The servlet request we are processing
     * @param reset Should we reset the token after checking it?
     */
    protected boolean isTokenValid(HttpServletRequest request, boolean reset) {

        return token.isTokenValid(request, reset);

    }


    /**
     * <p>Reset the saved transaction token in the user's session. This
     * indicates that transactional token checking will not be needed
     * on the next request that is submitted.</p>
     *
     * @param request The servlet request we are processing
     */
    protected void resetToken(HttpServletRequest request) {

        token.resetToken(request);

    }


    /**
     * <p>Save the specified error messages keys into the appropriate request
     * attribute for use by the &lt;html:errors&gt; tag, if any messages
     * are required. Otherwise, ensure that the request attribute is not
     * created.</p>
     *
     * @param request The servlet request we are processing
     * @param errors Error messages object
     * @deprecated Use saveErrors(HttpServletRequest, ActionMessages) instead.
     * This will be removed after Struts 1.2.
     */
    protected void saveErrors(HttpServletRequest request, ActionErrors errors) {

        this.saveErrors(request,(ActionMessages)errors);
        // :TODO: Remove after Struts 1.2.

    }


    /**
     * <p>Save the specified error messages keys into the appropriate request
     * attribute for use by the &lt;html:errors&gt; tag, if any messages
     * are required. Otherwise, ensure that the request attribute is not
     * created.</p>
     *
     * @param request The servlet request we are processing
     * @param errors Error messages object
     * @since Struts 1.2
     */
    protected void saveErrors(HttpServletRequest request, ActionMessages errors) {

        // Remove any error messages attribute if none are required
        if ((errors == null) || errors.isEmpty()) {
            request.removeAttribute(Globals.ERROR_KEY);
            return;
        }

        // Save the error messages we need
        request.setAttribute(Globals.ERROR_KEY, errors);

    }


    /**
     * <p>Save the specified messages keys into the appropriate request
     * attribute for use by the &lt;html:messages&gt; tag (if
     * messages="true" is set), if any messages are required. Otherwise,
     * ensure that the request attribute is not created.</p>
     *
     * @param request The servlet request we are processing.
     * @param messages The messages to save. <code>null</code> or empty
     * messages removes any existing ActionMessages in the request.
     *
     * @since Struts 1.1
     */
    protected void saveMessages(
        HttpServletRequest request,
        ActionMessages messages) {

        // Remove any messages attribute if none are required
        if ((messages == null) || messages.isEmpty()) {
            request.removeAttribute(Globals.MESSAGE_KEY);
            return;
        }

        // Save the messages we need
        request.setAttribute(Globals.MESSAGE_KEY, messages);
    }


    /**
     * <p>Save the specified messages keys into the appropriate session
     * attribute for use by the &lt;html:messages&gt; tag (if
     * messages="true" is set), if any messages are required. Otherwise,
     * ensure that the session attribute is not created.</p>
     *
     * @param session The session to save the messages in.
     * @param messages The messages to save. <code>null</code> or empty
     * messages removes any existing ActionMessages in the session.
     *
     * @since Struts 1.2
     */
    protected void saveMessages(
        HttpSession session,
        ActionMessages messages) {

        // Remove any messages attribute if none are required
        if ((messages == null) || messages.isEmpty()) {
            session.removeAttribute(Globals.MESSAGE_KEY);
            return;
        }

        // Save the messages we need
        session.setAttribute(Globals.MESSAGE_KEY, messages);
    }


    /**
     * <p>Save the specified error messages keys into the appropriate session
     * attribute for use by the &lt;html:messages&gt; tag (if messages="false") 
     * or &lt;html:errors&gt;, if any error messages are required. Otherwise, 
     * ensure that the session attribute is empty.</p>
     *
     * @param session The session to save the error messages in.
     * @param errors The error messages to save. <code>null</code> or empty
     * messages removes any existing error ActionMessages in the session.
     *
     * @since Struts 1.2.7
     */
    protected void saveErrors(
        HttpSession session,
        ActionMessages errors) {

        // Remove the error attribute if none are required
        if ((errors == null) || errors.isEmpty()) {
            session.removeAttribute(Globals.ERROR_KEY);
            return;
        }

        // Save the errors we need
        session.setAttribute(Globals.ERROR_KEY, errors);
    }


    /**
     * <p>Save a new transaction token in the user's current session, creating
     * a new session if necessary.</p>
     *
     * @param request The servlet request we are processing
     */
    protected void saveToken(HttpServletRequest request) {
        token.saveToken(request);
    }


    /**
     * <p>Set the user's currently selected <code>Locale</code> into their
     * <code>HttpSession</code>.</p>
     *
     * @param request The request we are processing
     * @param locale The user's selected Locale to be set, or null
     *  to select the server's default Locale
     */
    protected void setLocale(HttpServletRequest request, Locale locale) {

        HttpSession session = request.getSession();
        if (locale == null) {
            locale = Locale.getDefault();
        }
        session.setAttribute(Globals.LOCALE_KEY, locale);

    }

}

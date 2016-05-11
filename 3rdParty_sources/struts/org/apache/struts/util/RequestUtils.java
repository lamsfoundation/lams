/*
 *
 *
 * Copyright 1999-2006 The Apache Software Foundation.
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

package org.apache.struts.util;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Locale;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.DynaBean;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.Globals;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.action.ActionServlet;
import org.apache.struts.action.ActionServletWrapper;
import org.apache.struts.action.DynaActionForm;
import org.apache.struts.action.DynaActionFormClass;
import org.apache.struts.config.ActionConfig;
import org.apache.struts.config.FormBeanConfig;
import org.apache.struts.config.ForwardConfig;
import org.apache.struts.config.ModuleConfig;
import org.apache.struts.taglib.TagUtils;
import org.apache.struts.upload.MultipartRequestHandler;
import org.apache.struts.upload.MultipartRequestWrapper;

/**
 * <p>General purpose utility methods related to processing a servlet request
 * in the Struts controller framework.</p>
 *
 * @version $Rev: 384422 $ $Date$
 */
public class RequestUtils {


    // ------------------------------------------------------- Static Variables


    /**
     * <p>Commons Logging instance.</p>
     */
    protected static Log log = LogFactory.getLog(RequestUtils.class);


    // --------------------------------------------------------- Public Methods


    /**
     * <p>Create and return an absolute URL for the specified context-relative
     * path, based on the server and context information in the specified
     * request.</p>
     *
     * @param request The servlet request we are processing
     * @param path The context-relative path (must start with '/')
     *
     * @return  absolute URL based on context-relative path
     *
     * @exception MalformedURLException if we cannot create an absolute URL
     */
    public static URL absoluteURL(HttpServletRequest request, String path)
            throws MalformedURLException {

        return (new URL(serverURL(request), request.getContextPath() + path));

    }


    /**
     * <p>Return the <code>Class</code> object for the specified fully qualified
     * class name, from this web application's class loader.</p>
     *
     * @param className Fully qualified class name to be loaded
     * @return Class object
     *
     * @exception ClassNotFoundException if the class cannot be found
     */
    public static Class applicationClass(String className) throws ClassNotFoundException {

        // Look up the class loader to be used
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        if (classLoader == null) {
            classLoader = RequestUtils.class.getClassLoader();
        }

        // Attempt to load the specified class
        return (classLoader.loadClass(className));

    }


    /**
     * <p>Return a new instance of the specified fully qualified class name,
     * after loading the class from this web application's class loader.
     * The specified class <strong>MUST</strong> have a public zero-arguments
     * constructor.</p>
     *
     * @param className Fully qualified class name to use
     *
     * @return new instance of class
     * @exception ClassNotFoundException if the class cannot be found
     * @exception IllegalAccessException if the class or its constructor
     *  is not accessible
     * @exception InstantiationException if this class represents an
     *  abstract class, an interface, an array class, a primitive type,
     *  or void
     * @exception InstantiationException if this class has no
     *  zero-arguments constructor
     */
    public static Object applicationInstance(String className)
            throws ClassNotFoundException, IllegalAccessException, InstantiationException {

        return (applicationClass(className).newInstance());

    }

    /**
     * <p>Create (if necessary) and return an <code>ActionForm</code> instance appropriate
     * for this request.  If no <code>ActionForm</code> instance is required, return
     * <code>null</code>.</p>
     *
     * @param request The servlet request we are processing
     * @param mapping The action mapping for this request
     * @param moduleConfig The configuration for this module
     * @param servlet The action servlet
     *
     * @return ActionForm instance associated with this request
     */
    public static ActionForm createActionForm(
            HttpServletRequest request,
            ActionMapping mapping,
            ModuleConfig moduleConfig,
            ActionServlet servlet) {

        // Is there a form bean associated with this mapping?
        String attribute = mapping.getAttribute();
        if (attribute == null) {
            return (null);
        }

        // Look up the form bean configuration information to use
        String name = mapping.getName();
        FormBeanConfig config = moduleConfig.findFormBeanConfig(name);
        if (config == null) {
            log.warn("No FormBeanConfig found under '" + name + "'");
            return (null);
        }

        ActionForm instance = lookupActionForm(request, attribute, mapping.getScope());

        // Can we recycle the existing form bean instance (if there is one)?
        try {
            if (instance != null && canReuseActionForm(instance, config)) {
                return (instance);
            }
        } catch(ClassNotFoundException e) {
            log.error(servlet.getInternal().getMessage("formBean", config.getType()), e);
            return (null);
        }

        return createActionForm(config, servlet);
    }



    private static ActionForm lookupActionForm(HttpServletRequest request, String attribute, String scope)
    {
        // Look up any existing form bean instance
        if (log.isDebugEnabled()) {
            log.debug(
                    " Looking for ActionForm bean instance in scope '"
                    + scope
                    + "' under attribute key '"
                    + attribute
                    + "'");
        }
        ActionForm instance = null;
        HttpSession session = null;
        if ("request".equals(scope)) {
            instance = (ActionForm) request.getAttribute(attribute);
        } else {
            session = request.getSession();
            instance = (ActionForm) session.getAttribute(attribute);
        }

        return (instance);
    }

    /**
     * <p>Determine whether <code>instance</code> of <code>ActionForm</code> is
     * suitable for re-use as an instance of the form described by
     * <code>config</code>.</p>
     * @param instance an instance of <code>ActionForm</code> which was found,
     * probably in either request or session scope.
     * @param config the configuration for the ActionForm which is needed.
     * @return true if the instance found is "compatible" with the type required
     * in the <code>FormBeanConfig</code>; false if not, or if <code>instance</code>
     * is null.
     * @throws ClassNotFoundException if the <code>type</code> property of
     * <code>config</code> is not a valid Class name.
     */
    private static boolean canReuseActionForm(ActionForm instance, FormBeanConfig config)
            throws ClassNotFoundException
    {
        if (instance == null) {
            return (false);
        }

        boolean canReuse = false;
        String formType = null;
        String className = null;

        if (config.getDynamic()) {
            className = ((DynaBean) instance).getDynaClass().getName();
            canReuse = className.equals(config.getName());
            formType = "DynaActionForm";
        } else {
            Class configClass = applicationClass(config.getType());
            className = instance.getClass().getName();
            canReuse = configClass.isAssignableFrom(instance.getClass());
            formType = "ActionForm";
        }

        if (log.isDebugEnabled()) {
            log.debug(
                    " Can recycle existing "
                    + formType
                    + " instance "
                    + "of type '"
                    + className
                    + "'?: "
                    + canReuse);
            log.trace(" --> " + instance);
        }
        return (canReuse);
    }

    /**
     * <p>Create and return an <code>ActionForm</code> instance appropriate
     * to the information in <code>config</code>.</p>
     *
     * <p>Does not perform any checks to see if an existing ActionForm exists
     * which could be reused.</p>
     *
     * @param config The configuration for the Form bean which is to be created.
     * @param servlet The action servlet
     *
     * @return ActionForm instance associated with this request
     */
    public static ActionForm createActionForm(FormBeanConfig config, ActionServlet servlet)
    {
        if (config == null)
        {
            return (null);
        }

        ActionForm instance = null;

        // Create and return a new form bean instance
        try {

            instance = config.createActionForm(servlet);
            if (log.isDebugEnabled()) {
                log.debug(
                        " Creating new "
                        + (config.getDynamic() ? "DynaActionForm" : "ActionForm")
                        + " instance of type '"
                        + config.getType()
                        + "'");
                log.trace(" --> " + instance);
            }

        } catch(Throwable t) {
            log.error(servlet.getInternal().getMessage("formBean", config.getType()), t);
        }

        return (instance);

    }


    /**
     * <p>Look up and return current user locale, based on the specified parameters.</p>
     *
     * @param request The request used to lookup the Locale
     * @param locale Name of the session attribute for our user's Locale.  If this is
     * <code>null</code>, the default locale key is used for the lookup.
     * @return current user locale
     * @since Struts 1.2
     */
    public static Locale getUserLocale(HttpServletRequest request, String locale) {

        Locale userLocale = null;
        HttpSession session = request.getSession(false);

        if (locale == null) {
            locale = Globals.LOCALE_KEY;
        }

        // Only check session if sessions are enabled
        if (session != null) {
            userLocale = (Locale) session.getAttribute(locale);
        }

        if (userLocale == null) {
            // Returns Locale based on Accept-Language header or the server default
            userLocale = request.getLocale();
        }

        return userLocale;

    }


    /**
     * <p>Populate the properties of the specified JavaBean from the specified
     * HTTP request, based on matching each parameter name against the
     * corresponding JavaBeans "property setter" methods in the bean's class.
     * Suitable conversion is done for argument types as described under
     * <code>convert()</code>.</p>
     *
     * @param bean The JavaBean whose properties are to be set
     * @param request The HTTP request whose parameters are to be used
     *                to populate bean properties
     *
     * @exception ServletException if an exception is thrown while setting
     *            property values
     */
    public static void populate(Object bean, HttpServletRequest request) throws ServletException {

        populate(bean, null, null, request);

    }


    /**
     * <p>Populate the properties of the specified JavaBean from the specified
     * HTTP request, based on matching each parameter name (plus an optional
     * prefix and/or suffix) against the corresponding JavaBeans "property
     * setter" methods in the bean's class. Suitable conversion is done for
     * argument types as described under <code>setProperties</code>.</p>
     *
     * <p>If you specify a non-null <code>prefix</code> and a non-null
     * <code>suffix</code>, the parameter name must match <strong>both</strong>
     * conditions for its value(s) to be used in populating bean properties.
     * If the request's content type is "multipart/form-data" and the
     * method is "POST", the <code>HttpServletRequest</code> object will be wrapped in
     * a <code>MultipartRequestWrapper</code object.</p>
     *
     * @param bean The JavaBean whose properties are to be set
     * @param prefix The prefix (if any) to be prepend to bean property
     *               names when looking for matching parameters
     * @param suffix The suffix (if any) to be appended to bean property
     *               names when looking for matching parameters
     * @param request The HTTP request whose parameters are to be used
     *                to populate bean properties
     *
     * @exception ServletException if an exception is thrown while setting
     *            property values
     */
    public static void populate(
            Object bean,
            String prefix,
            String suffix,
            HttpServletRequest request)
            throws ServletException {

        // Build a list of relevant request parameters from this request
        HashMap properties = new HashMap();
        // Iterator of parameter names
        Enumeration names = null;
        // Map for multipart parameters
        Map multipartParameters = null;

        String contentType = request.getContentType();
        String method = request.getMethod();
        boolean isMultipart = false;

        if (bean instanceof ActionForm) {
            ((ActionForm) bean).setMultipartRequestHandler(null);
        }

        MultipartRequestHandler multipartHandler = null;
        if ((contentType != null)
                && (contentType.startsWith("multipart/form-data"))
                && (method.equalsIgnoreCase("POST"))) {

            // Get the ActionServletWrapper from the form bean
            ActionServletWrapper servlet;
            if (bean instanceof ActionForm) {
                servlet = ((ActionForm) bean).getServletWrapper();
            } else {
                throw new ServletException(
                        "bean that's supposed to be "
                        + "populated from a multipart request is not of type "
                        + "\"org.apache.struts.action.ActionForm\", but type "
                        + "\""
                        + bean.getClass().getName()
                        + "\"");
            }

            // Obtain a MultipartRequestHandler
            multipartHandler = getMultipartHandler(request);

            if (multipartHandler != null) {
                isMultipart = true;
                // Set servlet and mapping info
                servlet.setServletFor(multipartHandler);
                multipartHandler.setMapping(
                        (ActionMapping) request.getAttribute(Globals.MAPPING_KEY));
                // Initialize multipart request class handler
                multipartHandler.handleRequest(request);
                //stop here if the maximum length has been exceeded
                Boolean maxLengthExceeded =
                        (Boolean) request.getAttribute(
                                MultipartRequestHandler.ATTRIBUTE_MAX_LENGTH_EXCEEDED);
                if ((maxLengthExceeded != null) && (maxLengthExceeded.booleanValue())) {
                    ((ActionForm) bean).setMultipartRequestHandler(multipartHandler);
                    return;
                }
                //retrieve form values and put into properties
                multipartParameters = getAllParametersForMultipartRequest(
                        request, multipartHandler);
                names = Collections.enumeration(multipartParameters.keySet());
            }
        }

        if (!isMultipart) {
            names = request.getParameterNames();
        }

        while (names.hasMoreElements()) {
            String name = (String) names.nextElement();
            String stripped = name;
            if (prefix != null) {
                if (!stripped.startsWith(prefix)) {
                    continue;
                }
                stripped = stripped.substring(prefix.length());
            }
            if (suffix != null) {
                if (!stripped.endsWith(suffix)) {
                    continue;
                }
                stripped = stripped.substring(0, stripped.length() - suffix.length());
            }
            Object parameterValue = null;
            if (isMultipart) {
                parameterValue = multipartParameters.get(name);
            } else {
                parameterValue = request.getParameterValues(name);
            }

            // Populate parameters, except "standard" struts attributes
            // such as 'org.apache.struts.action.CANCEL'
            if (!(stripped.startsWith("org.apache.struts."))) {
                properties.put(stripped, parameterValue);
            }
        }

        // Set the corresponding properties of our bean
        try {
            BeanUtils.populate(bean, properties);
        } catch(Exception e) {
            throw new ServletException("BeanUtils.populate", e);
        } finally {
            if (multipartHandler != null) {
                // Set the multipart request handler for our ActionForm.
                // If the bean isn't an ActionForm, an exception would have been
                // thrown earlier, so it's safe to assume that our bean is
                // in fact an ActionForm.
                ((ActionForm) bean).setMultipartRequestHandler(multipartHandler);
            }
        }

    }


    /**
     * <p>Try to locate a multipart request handler for this request. First, look
     * for a mapping-specific handler stored for us under an attribute. If one
     * is not present, use the global multipart handler, if there is one.</p>
     *
     * @param request The HTTP request for which the multipart handler should
     *                be found.
     * @return the multipart handler to use, or null if none is
     *         found.
     *
     * @exception ServletException if any exception is thrown while attempting
     *                             to locate the multipart handler.
     */
    private static MultipartRequestHandler getMultipartHandler(HttpServletRequest request)
            throws ServletException {

        MultipartRequestHandler multipartHandler = null;
        String multipartClass = (String) request.getAttribute(Globals.MULTIPART_KEY);
        request.removeAttribute(Globals.MULTIPART_KEY);

        // Try to initialize the mapping specific request handler
        if (multipartClass != null) {
            try {
                multipartHandler = (MultipartRequestHandler) applicationInstance(multipartClass);
            } catch(ClassNotFoundException cnfe) {
                log.error(
                        "MultipartRequestHandler class \""
                        + multipartClass
                        + "\" in mapping class not found, "
                        + "defaulting to global multipart class");
            } catch(InstantiationException ie) {
                log.error(
                        "InstantiationException when instantiating "
                        + "MultipartRequestHandler \""
                        + multipartClass
                        + "\", "
                        + "defaulting to global multipart class, exception: "
                        + ie.getMessage());
            } catch(IllegalAccessException iae) {
                log.error(
                        "IllegalAccessException when instantiating "
                        + "MultipartRequestHandler \""
                        + multipartClass
                        + "\", "
                        + "defaulting to global multipart class, exception: "
                        + iae.getMessage());
            }

            if (multipartHandler != null) {
                return multipartHandler;
            }
        }

        ModuleConfig moduleConfig =
                ModuleUtils.getInstance().getModuleConfig(request);

        multipartClass = moduleConfig.getControllerConfig().getMultipartClass();

        // Try to initialize the global request handler
        if (multipartClass != null) {
            try {
                multipartHandler = (MultipartRequestHandler) applicationInstance(multipartClass);

            } catch(ClassNotFoundException cnfe) {
                throw new ServletException(
                        "Cannot find multipart class \""
                        + multipartClass
                        + "\""
                        + ", exception: "
                        + cnfe.getMessage());

            } catch(InstantiationException ie) {
                throw new ServletException(
                        "InstantiationException when instantiating "
                        + "multipart class \""
                        + multipartClass
                        + "\", exception: "
                        + ie.getMessage());

            } catch(IllegalAccessException iae) {
                throw new ServletException(
                        "IllegalAccessException when instantiating "
                        + "multipart class \""
                        + multipartClass
                        + "\", exception: "
                        + iae.getMessage());
            }

            if (multipartHandler != null) {
                return multipartHandler;
            }
        }

        return multipartHandler;
    }


    /**
     *<p>Create a <code>Map</code> containing all of the parameters supplied for a multipart
     * request, keyed by parameter name. In addition to text and file elements
     * from the multipart body, query string parameters are included as well.</p>
     *
     * @param request The (wrapped) HTTP request whose parameters are to be
     *                added to the map.
     * @param multipartHandler The multipart handler used to parse the request.
     *
     * @return the map containing all parameters for this multipart request.
     */
    private static Map getAllParametersForMultipartRequest(
            HttpServletRequest request,
            MultipartRequestHandler multipartHandler) {

        Map parameters = new HashMap();
        Hashtable elements = multipartHandler.getAllElements();
        Enumeration e = elements.keys();
        while (e.hasMoreElements()) {
            String key = (String) e.nextElement();
            parameters.put(key, elements.get(key));
        }

        if (request instanceof MultipartRequestWrapper) {
            request = ((MultipartRequestWrapper) request).getRequest();
            e = request.getParameterNames();
            while (e.hasMoreElements()) {
                String key = (String) e.nextElement();
                parameters.put(key, request.getParameterValues(key));
            }
        } else {
            log.debug("Gathering multipart parameters for unwrapped request");
        }

        return parameters;
    }


    /**
     * <p>Compute the printable representation of a URL, leaving off the
     * scheme/host/port part if no host is specified. This will typically
     * be the case for URLs that were originally created from relative
     * or context-relative URIs.</p>
     *
     * @param url URL to render in a printable representation
     * @return printable representation of a URL
     */
    public static String printableURL(URL url) {

        if (url.getHost() != null) {
            return (url.toString());
        }

        String file = url.getFile();
        String ref = url.getRef();
        if (ref == null) {
            return (file);
        } else {
            StringBuffer sb = new StringBuffer(file);
            sb.append('#');
            sb.append(ref);
            return (sb.toString());
        }

    }


    /**
     * <p>Return the context-relative URL that corresponds to the specified
     * {@link ActionConfig}, relative to the module associated
     * with the current modules's {@link ModuleConfig}.</p>
     *
     * @param request The servlet request we are processing
     * @param action ActionConfig to be evaluated
     * @param pattern URL pattern used to map the controller servlet

     * @return  context-relative URL relative to the module
     *
     * @since Struts 1.1
     */
    public static String actionURL(
            HttpServletRequest request,
            ActionConfig action,
            String pattern) {

        StringBuffer sb = new StringBuffer();
        if (pattern.endsWith("/*")) {
            sb.append(pattern.substring(0, pattern.length() - 2));
            sb.append(action.getPath());

        } else if (pattern.startsWith("*.")) {
            ModuleConfig appConfig =
                    ModuleUtils.getInstance().getModuleConfig(request);
            sb.append(appConfig.getPrefix());
            sb.append(action.getPath());
            sb.append(pattern.substring(1));

        } else {
            throw new IllegalArgumentException(pattern);
        }

        return sb.toString();

    }
  /**
     * <p>Return the context-relative URL that corresponds to the specified
     * <code>ForwardConfig</code>. The URL is calculated based on the properties
     * of the {@link ForwardConfig} instance as follows:</p>
     * <ul>
     * <li>If the <code>contextRelative</code> property is set, it is
     *     assumed that the <code>path</code> property contains a path
     *     that is already context-relative:
     *     <ul>
     *     <li>If the <code>path</code> property value starts with a slash,
     *         it is returned unmodified.</li>
     *     <li>If the <code>path</code> property value does not start
     *         with a slash, a slash is prepended.</li>
     *     </ul></li>
     * <li>Acquire the <code>forwardPattern</code> property from the
     *     <code>ControllerConfig</code> for the application module used
     *     to process this request. If no pattern was configured, default
     *     to a pattern of <code>$M$P</code>, which is compatible with the
     *     hard-coded mapping behavior in Struts 1.0.</li>
     * <li>Process the acquired <code>forwardPattern</code>, performing the
     *     following substitutions:
     *     <ul>
     *     <li><strong>$M</strong> - Replaced by the module prefix for the
     *         application module processing this request.</li>
     *     <li><strong>$P</strong> - Replaced by the <code>path</code>
     *         property of the specified {@link ForwardConfig}, prepended
     *         with a slash if it does not start with one.</li>
     *     <li><strong>$$</strong> - Replaced by a single dollar sign
     *         character.</li>
     *     <li><strong>$x</strong> (where "x" is any charater not listed
     *         above) - Silently omit these two characters from the result
     *         value.  (This has the side effect of causing all other
     *         $+letter combinations to be reserved.)</li>
     *     </ul></li>
     * </ul>
     *
     * @param request The servlet request we are processing
     * @param forward ForwardConfig to be evaluated
     *
     * @return context-relative URL
     * @since Struts 1.1
     */
    public static String forwardURL(HttpServletRequest request, ForwardConfig forward) {
         return forwardURL(request,forward,null);
    }

	/**
	 * <p>Return the context-relative URL that corresponds to the specified
	 * <code>ForwardConfig</code>. The URL is calculated based on the properties
	 * of the {@link ForwardConfig} instance as follows:</p>
	 * <ul>
	 * <li>If the <code>contextRelative</code> property is set, it is
	 *     assumed that the <code>path</code> property contains a path
	 *     that is already context-relative:
	 *     <ul>
	 *     <li>If the <code>path</code> property value starts with a slash,
	 *         it is returned unmodified.</li>
	 *     <li>If the <code>path</code> property value does not start
	 *         with a slash, a slash is prepended.</li>
	 *     </ul></li>
	 * <li>Acquire the <code>forwardPattern</code> property from the
	 *     <code>ControllerConfig</code> for the application module used
	 *     to process this request. If no pattern was configured, default
	 *     to a pattern of <code>$M$P</code>, which is compatible with the
	 *     hard-coded mapping behavior in Struts 1.0.</li>
	 * <li>Process the acquired <code>forwardPattern</code>, performing the
	 *     following substitutions:
	 *     <ul>
	 *     <li><strong>$M</strong> - Replaced by the module prefix for the
	 *         application module processing this request.</li>
	 *     <li><strong>$P</strong> - Replaced by the <code>path</code>
	 *         property of the specified {@link ForwardConfig}, prepended
	 *         with a slash if it does not start with one.</li>
	 *     <li><strong>$$</strong> - Replaced by a single dollar sign
	 *         character.</li>
	 *     <li><strong>$x</strong> (where "x" is any charater not listed
	 *         above) - Silently omit these two characters from the result
	 *         value.  (This has the side effect of causing all other
	 *         $+letter combinations to be reserved.)</li>
	 *     </ul></li>
	 * </ul>
	 *
	 * @param request The servlet request we are processing
	 * @param forward ForwardConfig to be evaluated
   * @param moduleConfig Base forward on this module config.
	 *
	 * @return context-relative URL
	 * @since Struts 1.2
	 */
	public static String forwardURL(HttpServletRequest request, ForwardConfig forward, ModuleConfig moduleConfig) {
		//load the current moduleConfig, if null
		if(moduleConfig == null) {
			moduleConfig = ModuleUtils.getInstance().getModuleConfig(request);
		}
		      
		String path = forward.getPath();
		//load default prefix
		String prefix = moduleConfig.getPrefix();
		
		//override prefix if supplied by forward
		if(forward.getModule() != null) {
			prefix = forward.getModule();
            if ("/".equals(prefix)) {
                prefix = "";
            }    
		}

		// Handle a ForwardConfig marked as context relative
		StringBuffer sb = new StringBuffer();
		if (forward.getContextRelative()) {
			if (!path.startsWith("/")) {
				sb.append("/");
			}
			sb.append(path);
			return (sb.toString());
		}

		// Calculate a context relative path for this ForwardConfig
		String forwardPattern = moduleConfig.getControllerConfig().getForwardPattern();
		if (forwardPattern == null) {
			// Performance optimization for previous default behavior
			sb.append(prefix);
			// smoothly insert a '/' if needed
			if (!path.startsWith("/")) {
				sb.append("/");
			}
			sb.append(path);

		} else {
			boolean dollar = false;
			for (int i = 0; i < forwardPattern.length(); i++) {
				char ch = forwardPattern.charAt(i);
				if (dollar) {
					switch (ch) {
						case 'M':
							sb.append(prefix);
							break;
						case 'P':
							// add '/' if needed
							if (!path.startsWith("/")) {
								sb.append("/");
							}
							sb.append(path);
							break;
						case '$':
							sb.append('$');
							break;
						default :
							; // Silently swallow
					}
					dollar = false;
					continue;
				} else if (ch == '$') {
					dollar = true;
				} else {
					sb.append(ch);
				}
			}
		}

		return (sb.toString());

	}


    /**
     * <p>Return the URL representing the current request. This is equivalent
     * to <code>HttpServletRequest.getRequestURL</code> in Servlet 2.3.</p>
     *
     * @param request The servlet request we are processing

     * @return URL representing the current request
     * @exception MalformedURLException if a URL cannot be created
     */
    public static URL requestURL(HttpServletRequest request) throws MalformedURLException {

        StringBuffer url = requestToServerUriStringBuffer(request);
        return (new URL(url.toString()));

    }


    /**
     * <p>Return the URL representing the scheme, server, and port number of
     * the current request. Server-relative URLs can be created by simply
     * appending the server-relative path (starting with '/') to this.</p>
     *
     * @param request The servlet request we are processing
     *
     * @return URL representing the scheme, server, and port number of
     *     the current request
     * @exception MalformedURLException if a URL cannot be created
     */
    public static URL serverURL(HttpServletRequest request) throws MalformedURLException {

        StringBuffer url = requestToServerStringBuffer(request);
        return (new URL(url.toString()));

    }


    /**
     * <p>Return the string representing the scheme, server, and port number of
     * the current request. Server-relative URLs can be created by simply
     * appending the server-relative path (starting with '/') to this.</p>
     *
     * @param request The servlet request we are processing

     * @return URL representing the scheme, server, and port number of
     *     the current request
     * @since Struts 1.2.0
     */
    public static StringBuffer requestToServerUriStringBuffer(HttpServletRequest request) {

        StringBuffer serverUri = createServerUriStringBuffer(request.getScheme(),request.getServerName(),
        request.getServerPort(),request.getRequestURI());
        return serverUri;

    }

    /**
     * <p>Return <code>StringBuffer</code> representing the scheme, server, and port number of
     * the current request. Server-relative URLs can be created by simply
     * appending the server-relative path (starting with '/') to this.</p>
     *
     * @param request The servlet request we are processing
     *
     * @return URL representing the scheme, server, and port number of
     *     the current request
     * @since Struts 1.2.0
     */
    public static StringBuffer requestToServerStringBuffer(HttpServletRequest request) {

        return createServerStringBuffer(request.getScheme(),request.getServerName(),request.getServerPort());

    }


    /**
     * <p>Return <code>StringBuffer</code> representing the scheme, server, and port number of
     * the current request.</p>
     *
     * @param scheme The scheme name to use
     * @param server The server name to use
     * @param port The port value to use
     *
     * @return StringBuffer in the form scheme: server: port
     * @since Struts 1.2.0
     */
    public static StringBuffer createServerStringBuffer(String scheme,String server,int port) {

        StringBuffer url = new StringBuffer();
        if (port < 0) {
            port = 80; // Work around java.net.URL bug
        }
        url.append(scheme);
        url.append("://");
        url.append(server);
        if ((scheme.equals("http") && (port != 80)) || (scheme.equals("https") && (port != 443))) {
            url.append(':');
            url.append(port);
        }
        return url;

    }


    /**
     * <p>Return <code>StringBuffer</code> representing the scheme, server, and port number of
     * the current request.</p>
     *
     * @param scheme The scheme name to use
     * @param server The server name to use
     * @param port The port value to use
     * @param uri The uri value to use
     *
     * @return StringBuffer in the form scheme: server: port
     * @since Struts 1.2.0
     */
    public static StringBuffer createServerUriStringBuffer(String scheme,String server,int port,String uri) {

        StringBuffer serverUri = createServerStringBuffer(scheme,server,port);
        serverUri.append(uri);
        return serverUri;

    }


    // ------------------------------------- Deprecated in favor of ModuleUtils



    /**
     * <p>Select the module to which the specified request belongs, and
     * add corresponding request attributes to this request.</p>
     *
     * @param prefix The module prefix of the desired module
     * @param request The servlet request we are processing
     * @param context The ServletContext for this web application
     *
     * @since Struts 1.1
     * @deprecated Use {@link org.apache.struts.util.ModuleUtils#selectModule(String,HttpServletRequest,ServletContext)} instead.
     * This will be removed after Struts 1.2.
     */
    public static void selectModule(
            String prefix,
            HttpServletRequest request,
            ServletContext context) {
        // :TODO: Remove after Struts 1.2

        ModuleUtils.getInstance().selectModule(prefix, request, context);

    }


    /**
     * <p>Select the module to which the specified request belongs, and
     * add corresponding request attributes to this request.</p>
     *
     * @param request The servlet request we are processing
     * @param context The ServletContext for this web application
     *
     * @deprecated Use {@link org.apache.struts.util.ModuleUtils#selectModule(HttpServletRequest,ServletContext)} instead.
     * This will be removed after Struts 1.2.
     */
    public static void selectModule(HttpServletRequest request, ServletContext context) {
        // :TODO: Remove after Struts 1.2

        ModuleUtils.getInstance().selectModule(request, context);

    }


    /**
     * Get the module name to which the specified request belong.
     * @param request The servlet request we are processing
     * @param context The ServletContext for this web application
     * @return The module prefix or ""
     * @deprecated Use Use {@link org.apache.struts.util.ModuleUtils#getModuleName(HttpServletRequest,ServletContext)} instead.
     * This will be removed after Struts 1.2.
     */
    public static String getModuleName(HttpServletRequest request, ServletContext context) {
        // :TODO: Remove after Struts 1.2

        return ModuleUtils.getInstance().getModuleName(request, context);

    }


    /**
     * <p>Get the module name to which the specified uri belong.</p>
     *
     * @param matchPath The uri from which we want the module name.
     * @param context The ServletContext for this web application
     *
     * @return The module prefix or ""
     * @deprecated Use {@link org.apache.struts.util.ModuleUtils#getModuleName(String,ServletContext)} instead.
     * This will be removed after Struts 1.2.
     */
    public static String getModuleName(String matchPath, ServletContext context) {
        // :TODO: Remove after Struts 1.2

        return ModuleUtils.getInstance().getModuleName(matchPath, context);

    }


    /**
     * <p>Return the current <code>ModuleConfig</code> object stored in request,
     * if it exists, null otherwise.
     * This method can be used by a {@link org.apache.struts.action.PlugIn} to
     * retrieve the current module config object. If no moduleConfig is found,
     * this means that the request hasn't hit the server through the Struts servlet.
     * The appropriate module config can be set and found with
     * <code>{@link RequestUtils#selectModule(HttpServletRequest, ServletContext)} </code>.
     *
     * @param request The servlet request we are processing
     *
     * @return the ModuleConfig object from request, or null if none is set in
     * the request.
     * @since Struts 1.1
     * @deprecated Use {@link org.apache.struts.util.ModuleUtils#getModuleConfig(HttpServletRequest)} instead.
     * This will be removed after Struts 1.2.
     */
    public static ModuleConfig getRequestModuleConfig(HttpServletRequest request) {
        // :TODO: Remove after Struts 1.2

        return ModuleUtils.getInstance().getModuleConfig(request);

    }


    /**
     * <p>Return the ModuleConfig object is it exists, null otherwise.</p>
     *
     * @param request The servlet request we are processing
     * @param context The ServletContext for this web application
     *
     * @return the ModuleConfig object
     * @since Struts 1.1
     * @deprecated Use {@link org.apache.struts.util.ModuleUtils#getModuleConfig(HttpServletRequest,ServletContext)} instead.
     * This will be removed after Struts 1.2.
     */
    public static ModuleConfig getModuleConfig(
            HttpServletRequest request,
            ServletContext context) {
        // :TODO: Remove after Struts 1.2

        return ModuleUtils.getInstance().getModuleConfig(request, context);

    }


    /**
     * <p>Return the list of module prefixes that are defined for
     * this web application. <strong>NOTE</strong> -
     * the "" prefix for the default module is not included in this list.</p>
     *
     * @param context The ServletContext for this web application.
     *
     * @return An array of module prefixes.
     * @since Struts 1.1
     * @deprecated Use {@link org.apache.struts.util.ModuleUtils#getModulePrefixes(ServletContext)} instead.
     * This will be removed after Struts 1.2.
     */
    public static String[] getModulePrefixes(ServletContext context) {
        // :TODO: Remove after Struts 1.2

        return ModuleUtils.getInstance().getModulePrefixes(context);

    }


    // ---------------------------------------- Deprecated in favor of TagUtils


    /**
     * <p>Compute a set of query parameters that will be dynamically added to
     * a generated URL. The returned Map is keyed by parameter name, and the
     * values are either null (no value specified), a String (single value
     * specified), or a String[] array (multiple values specified). Parameter
     * names correspond to the corresponding attributes of the
     * <code>&lt;html:link&gt;</code> tag.  If no query parameters are
     * identified, return <code>null</code>.</p>
     *
     * @param pageContext PageContext we are operating in

     * @param paramId Single-value request parameter name (if any)
     * @param paramName Bean containing single-value parameter value
     * @param paramProperty Property (of bean named by <code>paramName</code>
     *  containing single-value parameter value
     * @param paramScope Scope containing bean named by
     *  <code>paramName</code>
     *
     * @param name Bean containing multi-value parameters Map (if any)
     * @param property Property (of bean named by <code>name</code>
     *  containing multi-value parameters Map
     * @param scope Scope containing bean named by
     *  <code>name</code>
     *
     * @param transaction Should we add our transaction control token?
     * @return Map of query parameters
     * @exception JspException if we cannot look up the required beans
     * @exception JspException if a class cast exception occurs on a
     *  looked-up bean or property
     * @deprecated This will be removed after Struts 1.2.
     * Use {@link org.apache.struts.taglib.TagUtils#computeParameters(PageContext,String,String,String,String,String,String,String,boolean)} instead.
     */
    public static Map computeParameters(
            PageContext pageContext,
            String paramId,
            String paramName,
            String paramProperty,
            String paramScope,
            String name,
            String property,
            String scope,
            boolean transaction)
            throws JspException {
        // :TODO: Remove after Struts 1.2

        return TagUtils.getInstance().computeParameters(pageContext, paramId, paramName, paramProperty, paramScope,
                                                        name, property, scope, transaction);

    }


    /**
     * <p>Compute a hyperlink URL based on the <code>forward</code>,
     * <code>href</code> or <code>page</code> parameter
     * that is not null.</p>
     *
     * @param pageContext PageContext for the tag making this call
     * @param forward Logical forward name for which to look up
     *  the context-relative URI (if specified)
     * @param href URL to be utilized unmodified (if specified)
     * @param page Module-relative page for which a URL should
     *  be created (if specified)
     * @param params Map of parameters to be dynamically included (if any)
     * @param anchor Anchor to be dynamically included (if any)
     * @param redirect Is this URL for a <code>response.sendRedirect()</code>?

     * @return URL with session identifier
     * @exception MalformedURLException if a URL cannot be created
     *  for the specified parameters
     * @deprecated This will be removed after Struts 1.2
     * Use {@link RequestUtils#computeURL(PageContext, String, String, String, String, Map, String, boolean)} instead.
     */
    public static String computeURL(
            PageContext pageContext,
            String forward,
            String href,
            String page,
            Map params,
            String anchor,
            boolean redirect)
            throws MalformedURLException {
        // :TODO: Remove after Struts 1.2

        return (TagUtils.getInstance().computeURLWithCharEncoding(
                pageContext, forward, href, page, null, null, params, anchor, redirect, false));
    }


    /**
     * <p>Compute a hyperlink URL based on the <code>forward</code>,
     * <code>href</code>, <code>action</code> or <code>page</code> parameter
     * that is not null.
     * The returned URL will have already been passed to
     * <code>response.encodeURL()</code> for adding a session identifier.</p>
     *
     * @param pageContext PageContext for the tag making this call
     * @param forward Logical forward name for which to look up
     *  the context-relative URI (if specified)
     * @param href URL to be utilized unmodified (if specified)
     * @param page Module-relative page for which a URL should
     *  be created (if specified)
     * @param action Logical action name for which to look up
     *  the context-relative URI (if specified)
     * @param params Map of parameters to be dynamically included (if any)
     * @param anchor Anchor to be dynamically included (if any)
     * @param redirect Is this URL for a <code>response.sendRedirect()</code>?

     * @return URL with session identifier
     * @exception MalformedURLException if a URL cannot be created
     *  for the specified parameters
     * @deprecated This will be removed after Struts 1.2.
     * Use {@link org.apache.struts.taglib.TagUtils#computeURL(PageContext,String,String,String,String,String,Map,String,	boolean)} instead.
     */
    public static String computeURL(
            PageContext pageContext,
            String forward,
            String href,
            String page,
            String action,
            Map params,
            String anchor,
            boolean redirect)
            throws MalformedURLException {
        // :TODO: Remove after Struts 1.2

        return TagUtils.getInstance().computeURL(
                pageContext,
                forward,
                href,
                page,
                action,
                null,
                params,
                anchor,
                redirect);
    }


    /**
     * <p>Compute a hyperlink URL based on the <code>forward</code>,
     * <code>href</code>, <code>action</code> or <code>page</code> parameter
     * that is not null.
     * The returned URL will have already been passed to
     * <code>response.encodeURL()</code> for adding a session identifier.
     * </p>
     *
     * @param pageContext PageContext for the tag making this call
     * @param forward Logical forward name for which to look up
     *  the context-relative URI (if specified)
     * @param href URL to be utilized unmodified (if specified)
     * @param page Module-relative page for which a URL should
     *  be created (if specified)
     * @param action Logical action name for which to look up
     *  the context-relative URI (if specified)
     * @param params Map of parameters to be dynamically included (if any)
     * @param anchor Anchor to be dynamically included (if any)
     * @param redirect Is this URL for a <code>response.sendRedirect()</code>?
     * @param encodeSeparator This is only checked if redirect is set to false (never
     * encoded for a redirect).  If true, query string parameter separators are encoded
     * as &gt;amp;, else &amp; is used.

     * @return URL with session identifier
     * @exception MalformedURLException if a URL cannot be created
     *  for the specified parameters
     * @deprecated This will be removed after Struts 1.2.
     * Use {@link org.apache.struts.taglib.TagUtils#computeURL(PageContext,String,String,String,String,String,Map,String,boolean,boolean)} instead.
     */
    public static String computeURL(
            PageContext pageContext,
            String forward,
            String href,
            String page,
            String action,
            Map params,
            String anchor,
            boolean redirect,
            boolean encodeSeparator)
            throws MalformedURLException {
        // :TODO: Remove after Struts 1.2

        return (TagUtils.getInstance().computeURL(
                pageContext,
                forward,
                href,
                page,
                action,
                null,
                params,
                anchor,
                redirect,
                encodeSeparator));
    }


    /**
     * <p>Return the form action converted into an action mapping path.  The
     * value of the <code>action</code> property is manipulated as follows in
     * computing the name of the requested mapping:</p>
     * <ul>
     * <li>Any filename extension is removed (on the theory that extension
     *     mapping is being used to select the controller servlet).</li>
     * <li>If the resulting value does not start with a slash, then a
     *     slash is prepended.</li>
     * </ul>
     * @deprecated  This will be removed after Struts 1.2.
     * Use {@link org.apache.struts.taglib.TagUtils#getActionMappingName(String)} instead.
     */
    public static String getActionMappingName(String action) {
        // :TODO: Remove after Struts 1.2

        return TagUtils.getInstance().getActionMappingName(action);

    }


    /**
     * <p>Return the form action converted into a server-relative URL.</p>
     * @deprecated This will be removed after Struts 1.2.
     * Use {@link org.apache.struts.taglib.TagUtils#getActionMappingURL(String,PageContext)} instead.
     */
    public static String getActionMappingURL(
            String action,
            PageContext pageContext) {
        // :TODO: Remove after Struts 1.2

        return TagUtils.getInstance().getActionMappingURL(action, pageContext);

    }


    /**
     * <p>Locate and return the specified bean, from an optionally specified
     * scope, in the specified page context. If no such bean is found,
     * return <code>null</code> instead. If an exception is thrown, it will
     * have already been saved via a call to <code>saveException</code>.</p>
     *
     * @param pageContext Page context to be searched
     * @param name Name of the bean to be retrieved
     * @param scopeName Scope to be searched (page, request, session, application)
     *  or <code>null</code> to use <code>findAttribute()</code> instead
     *
     * @return JavaBean in the specified page context
     * @exception JspException if an invalid scope name
     *  is requested
     * @deprecated This will be removed after Struts 1.2.
     * Use {@link org.apache.struts.taglib.TagUtils#lookup(PageContext,String,String)} instead.
     */
    public static Object lookup(PageContext pageContext, String name, String scopeName)
            throws JspException {
        // :TODO: Remove after Struts 1.2

        return TagUtils.getInstance().lookup(pageContext, name, scopeName);

    }


    /**
     * <p>Converts the scope name into its corresponding PageContext constant value.</p>
     *
     * @param scopeName Can be "page", "request", "session", or "application" in any
     * case
     *
     * @return The constant representing the scope (ie. PageContext.REQUEST_SCOPE).
     * @throws JspException if the scopeName is not a valid name.
     * @since Struts 1.1
     * @deprecated This will be removed after Struts 1.2.
     * Use {@link org.apache.struts.taglib.TagUtils#getScope(String)} instead.

     */
    public static int getScope(String scopeName) throws JspException {
        // :TODO: Remove after Struts 1.2

        return TagUtils.getInstance().getScope(scopeName);

    }


    /**
     * <p>Locate and return the specified property of the specified bean, from
     * an optionally specified scope, in the specified page context. If an
     * exception is thrown, it will have already been saved via a call to
     * <code>saveException</code>.</p>
     *
     * @param pageContext Page context to be searched
     * @param name Name of the bean to be retrieved
     * @param property Name of the property to be retrieved, or
     *  <code>null</code> to retrieve the bean itself
     * @param scope Scope to be searched (page, request, session, application)
     *  or <code>null</code> to use <code>findAttribute()</code> instead
     *
     * @return property of specified JavaBean
     * @exception JspException if an invalid scope name
     *  is requested
     * @exception JspException if the specified bean is not found
     * @exception JspException if accessing this property causes an
     *  IllegalAccessException, IllegalArgumentException,
     *  InvocationTargetException, or NoSuchMethodException
     * @deprecated This will be removed after Struts 1.2.
     * Use {@link org.apache.struts.taglib.TagUtils#lookup(PageContext,String,String,String)} instead.

     */
    public static Object lookup(
            PageContext pageContext,
            String name,
            String property,
            String scope)
            throws JspException {
        // :TODO: Remove after Struts 1.2

        return TagUtils.getInstance().lookup(pageContext, name, property, scope);

    }


    /**
     * <p>Look up and return current user locale, based on the specified parameters.</p>
     *
     * @param pageContext The PageContext associated with this request
     * @param locale Name of the session attribute for our user's Locale.  If this is
     * <code>null</code>, the default locale key is used for the lookup.
     *
     * @return current user locale
     * @deprecated This will be removed after Struts 1.2.
     * Use {@link org.apache.struts.taglib.TagUtils#getUserLocale(PageContext,String)} instead.
     */
    public static Locale retrieveUserLocale(PageContext pageContext, String locale) {
        // :TODO: Remove after Struts 1.2

        return TagUtils.getInstance().getUserLocale(pageContext, locale);

    }


    /**
     * <p>Look up and return a message string, based on the specified parameters.</p>
     *
     * @param pageContext The PageContext associated with this request
     * @param bundle Name of the servlet context attribute for our
     *  message resources bundle
     * @param locale Name of the session attribute for our user's Locale
     * @param key Message key to be looked up and returned
     *
     * @return message string
     * @exception JspException if a lookup error occurs (will have been
     *  saved in the request already)
     * @deprecated Use {@link org.apache.struts.taglib.TagUtils#message(PageContext,String,String,String)} instead.
     * This will be removed after Struts 1.2.
     */
    public static String message(
            PageContext pageContext,
            String bundle,
            String locale,
            String key)
            throws JspException {
        // :TODO: Remove afer Struts 1.2

        return TagUtils.getInstance().message(pageContext, bundle, locale, key);

    }


    /**
     * Look up and return a message string, based on the specified parameters.
     *
     * @param pageContext The PageContext associated with this request
     * @param bundle Name of the servlet context attribute for our
     *  message resources bundle
     * @param locale Name of the session attribute for our user's Locale
     * @param key Message key to be looked up and returned
     * @param args Replacement parameters for this message
     * @return message string
     * @exception JspException if a lookup error occurs (will have been
     *  saved in the request already)
     * @deprecated Use {@link org.apache.struts.taglib.TagUtils#message(PageContext,String,String,String,Object[])} instead.
     * This will be removed after Struts 1.2.
     */
    public static String message(
            PageContext pageContext,
            String bundle,
            String locale,
            String key,
            Object args[])
            throws JspException {
        // :TODO: Remove afer Struts 1.2

        return TagUtils.getInstance().message(
                pageContext,
                bundle,
                locale,
                key,
                args);
    }


    /**
     * <p>Return true if a message string for the specified message key
     * is present for the specified Locale.</p>
     *
     * @param pageContext The PageContext associated with this request
     * @param bundle Name of the servlet context attribute for our
     *  message resources bundle
     * @param locale Name of the session attribute for our user's Locale
     * @param key Message key to be looked up and returned
     *
     * @return true if a message string for message key exists
     * @exception JspException if a lookup error occurs (will have been
     *  saved in the request already)
     * @deprecated Use {@link org.apache.struts.taglib.TagUtils#present(PageContext ,String,String,String)} instead.
     * This will be removed after Struts 1.2.
     */
    public static boolean present(
            PageContext pageContext,
            String bundle,
            String locale,
            String key)
            throws JspException {
        // :TODO: Remove after Struts 1.2

        return TagUtils.getInstance().present(pageContext, bundle, locale, key);

    }


    /**
     * <p>Return the context-relative URL that corresponds to the specified
     * <code>page</code> attribute value, calculated based on the
     * <code>pagePattern</code> property of the current module's
     * {@link ModuleConfig}.</p>
     *
     * @param request The servlet request we are processing
     * @param page The module-relative URL to be substituted in
     *  to the <code>pagePattern</code> pattern for the current module
     *  (<strong>MUST</strong> start with a slash)

     * @return context-relative URL
     * @since Struts 1.1
     * @deprecated Use {@link org.apache.struts.taglib.TagUtils#pageURL(HttpServletRequest request, String page, ModuleConfig moduleConfig)} instead.
     * This will be removed after Struts 1.2.
     */
    public static String pageURL(HttpServletRequest request, String page) {
    	//load the current moduleConfig
		ModuleConfig moduleConfig = ModuleUtils.getInstance().getModuleConfig(request);
		
        return TagUtils.getInstance().pageURL(request, page, moduleConfig);
        //:TODO: Remove after Struts 1.2

    }


    /**
     * <p>Save the specified exception as a request attribute for later use.</p>
     *
     * @param pageContext The PageContext for the current page
     * @param exception The exception to be saved
     *
     * @deprecated Use {@link org.apache.struts.taglib.TagUtils#saveException(PageContext,Throwable)} instead.
     * This will be removed after Struts 1.2.
     */
    public static void saveException(PageContext pageContext, Throwable exception) {

        TagUtils.getInstance().saveException(pageContext, exception);
        // :TODO: Remove after Struts 1.2

    }


    /**
     * <p>Return the <code>ModuleConfig</code> object if it exists, null if otherwise.</p>
     *
     * @param pageContext The page context.
     *
     * @return the ModuleConfig object
     * @since Struts 1.1
     * @deprecated Use {@link org.apache.struts.taglib.TagUtils#getModuleConfig(PageContext)} instead.
     * This will be removed after Struts 1.2.
     */
    public static ModuleConfig getModuleConfig(PageContext pageContext) {
        // :TODO: Remove after Struts 1.2

        return TagUtils.getInstance().getModuleConfig(pageContext);

    }


    /**
     * <p>Retrieves the value from request scope and if it isn't already an
     * <code>ActionMessages</code> some classes are converted to one.</p>
     *
     * @param pageContext   The PageContext for the current page
     * @param paramName     Key for parameter value
     *
     * @return ActionErros in page context.
     * @throws JspException
     * @deprecated Use {@link org.apache.struts.taglib.TagUtils#getActionMessages(PageContext,String)} instead.
     * This will be removed after Struts 1.2.
     */
    public static ActionMessages getActionMessages(PageContext pageContext, String paramName)
            throws JspException {
        // :TODO: Remove after Struts 1.2

        return TagUtils.getInstance().getActionMessages(pageContext, paramName);
    }


    /**
     * <p>Retrieves the value from request scope and if it isn't already an
     *  <code>ErrorMessages</code> some classes are converted to one.</p>
     *
     * @param pageContext   The PageContext for the current page
     * @param paramName     Key for parameter value
     *
     *
     * @return ActionErrors from request scope
     * @exception JspException
     * @deprecated Use {@link org.apache.struts.taglib.TagUtils#getActionErrors(PageContext,String)} instead.
     * This will be removed after Struts 1.2.
     */
    public static ActionErrors getActionErrors(PageContext pageContext, String paramName)
            throws JspException {
        // :TODO: Remove after Struts 1.2

        return TagUtils.getInstance().getActionErrors(pageContext, paramName);
    }


    /**
     * <p>Use the new <code>URLEncoder.encode</code> method from Java 1.4 if available, else
     * use the old deprecated version. This method uses reflection to find the appropriate
     * method; if the reflection operations throw exceptions, this will return the url
     * encoded with the old <code>URLEncoder.encode</code> method.
     * @return String - the encoded url.
     * @deprecated Use {@link org.apache.struts.taglib.TagUtils#encodeURL(String)} instead.
     * This will be removed after Struts 1.2.
     */
    public static String encodeURL(String url) {
        // :TODO: Remove after Struts 1.2

        return TagUtils.getInstance().encodeURL(url);

    }


    /**
     * <p>Returns true if the custom tags are in XHTML mode.</p>
     *
     * @since Struts 1.1
     * @deprecated Use {@link org.apache.struts.taglib.TagUtils#isXhtml(PageContext)} instead.
     * This will be removed after Struts 1.2.
     */
    public static boolean isXhtml(PageContext pageContext) {
        // :TODO: Remove after Struts 1.2

        String xhtml =
                (String) pageContext.getAttribute(
                        Globals.XHTML_KEY,
                        PageContext.PAGE_SCOPE);

        return "true".equalsIgnoreCase(xhtml);

    }

}

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

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.MissingResourceException;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.UnavailableException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.beanutils.converters.BigDecimalConverter;
import org.apache.commons.beanutils.converters.BigIntegerConverter;
import org.apache.commons.beanutils.converters.BooleanConverter;
import org.apache.commons.beanutils.converters.ByteConverter;
import org.apache.commons.beanutils.converters.CharacterConverter;
import org.apache.commons.beanutils.converters.DoubleConverter;
import org.apache.commons.beanutils.converters.FloatConverter;
import org.apache.commons.beanutils.converters.IntegerConverter;
import org.apache.commons.beanutils.converters.LongConverter;
import org.apache.commons.beanutils.converters.ShortConverter;
import org.apache.commons.collections.FastHashMap;
import org.apache.commons.digester.Digester;
import org.apache.commons.digester.RuleSet;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.Globals;
import org.apache.struts.config.ConfigRuleSet;
import org.apache.struts.config.DataSourceConfig;
import org.apache.struts.config.FormBeanConfig;
import org.apache.struts.config.MessageResourcesConfig;
import org.apache.struts.config.ModuleConfig;
import org.apache.struts.config.ModuleConfigFactory;
import org.apache.struts.config.PlugInConfig;
import org.apache.struts.util.MessageResources;
import org.apache.struts.util.MessageResourcesFactory;
import org.apache.struts.util.ModuleUtils;
import org.apache.struts.util.RequestUtils;
import org.apache.struts.util.ServletContextWriter;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/**
 * <p><strong>ActionServlet</strong> provides the "controller" in the
 * Model-View-Controller (MVC) design pattern for web applications that is
 * commonly known as "Model 2".  This nomenclature originated with a
 * description in the JavaServerPages Specification, version 0.92, and has
 * persisted ever since (in the absence of a better name).</p>
 *
 * <p>Generally, a "Model 2" application is architected as follows:</p>
 * <ul>
 * <li>The user interface will generally be created with server pages, which
 *     will not themselves contain any business logic. These pages represent
 *     the "view" component of an MVC architecture.</li>
 * <li>Forms and hyperlinks in the user interface that require business logic
 *     to be executed will be submitted to a request URI that is mapped to this
 *     servlet.</li>
 * <li>There can be <b>one</b> instance of this servlet class,
 *     which receives and processes all requests that change the state of
 *     a user's interaction with the application.  The servlet delegates the
 *     handling of a request to a {@link RequestProcessor} object. This component
 *     represents the "controller" component of an MVC architecture.</li>
 * <li>The <code>RequestProcessor</code> selects and invokes an {@link Action} class to perform
 *     the requested business logic, or delegates the response to another resource.</li>
 * <li>The <code>Action</code> classes can manipulate the state of the application's
 *     interaction with the user, typically by creating or modifying JavaBeans
 *     that are stored as request or session attributes (depending on how long
 *     they need to be available). Such JavaBeans represent the "model"
 *     component of an MVC architecture.</li>
 * <li>Instead of producing the next page of the user interface directly,
 *     <code>Action</code> classes generally return an {@link ActionForward} to indicate
 *     which resource should handle the response. If the <code>Action</code>
 *     does not return null, the <code>RequestProcessor</code> forwards or
 *     redirects to the specified resource (by utilizing
 *     <code>RequestDispatcher.forward</code> or <code>Response.sendRedirect</code>)
 *     so as to produce the next page of the user interface.</li>
 * </ul>
 *
 * <p>The standard version of <code>RequestsProcessor</code> implements the
 *    following logic for each incoming HTTP request. You can override
 *    some or all of this functionality by subclassing this object and
 *    implementing your own version of the processing.</p>
 * <ul>
 * <li>Identify, from the incoming request URI, the substring that will be
 *     used to select an action procedure.</li>
 * <li>Use this substring to map to the Java class name of the corresponding
 *     action class (an implementation of the <code>Action</code> interface).
 *     </li>
 * <li>If this is the first request for a particular <code>Action</code> class,
 *     instantiate an instance of that class and cache it for future use.</li>
 * <li>Optionally populate the properties of an <code>ActionForm</code> bean
 *     associated with this mapping.</li>
 * <li>Call the <code>execute</code> method of this <code>Action</code> class, passing
 *     on a reference to the mapping that was used, the relevant form-bean
 *     (if any), and the request and the response that were passed to the
 *     controller by the servlet container (thereby providing access to any
 *     specialized properties of the mapping itself as well as to the
 *     ServletContext).
 *     </li>
 * </ul>
 *
 * <p>The standard version of <code>ActionServlet</code> is configured based
 * on the following servlet initialization parameters, which you will specify
 * in the web application deployment descriptor (<code>/WEB-INF/web.xml</code>)
 * for your application.  Subclasses that specialize this servlet are free to
 * define additional initialization parameters. </p>
 * <ul>
 * <li><strong>config</strong> - Comma-separated list of context-relative
 *     path(s) to the XML resource(s) containing the configuration information
 *     for the default module.  (Multiple files support since Struts 1.1)
 *     [/WEB-INF/struts-config.xml].</li>
 * <li><strong>config/${module}</strong> - Comma-separated list of
 *     Context-relative path(s) to the XML resource(s)
 *     containing the configuration information for the module that
 *     will use the specified prefix (/${module}). This can be repeated as many
 *     times as required for multiple modules. (Since Struts 1.1)</li>
 * <li><strong>configFactory</strong> - The Java class name of the
 *     <code>ModuleConfigFactory</code> used to create the implementation of the
 *     <code>ModuleConfig</code> interface.
 *     [org.apache.struts.config.impl.DefaultModuleConfigFactory]
 * </li>
 * <li><strong>convertNull</strong> - Force simulation of the Struts 1.0 behavior
 *     when populating forms. If set to true, the numeric Java wrapper class types
 *     (like <code>java.lang.Integer</code>) will default to null (rather than 0).
 *     (Since Struts 1.1) [false] </li>
 * <li><strong>rulesets</strong> - Comma-delimited list of fully qualified
 *     classnames of additional <code>org.apache.commons.digester.RuleSet</code>
 *     instances that should be added to the <code>Digester</code> that will
 *     be processing <code>struts-config.xml</code> files.  By default, only
 *     the <code>RuleSet</code> for the standard configuration elements is
 *     loaded.  (Since Struts 1.1)</li>
 * <li><strong>validating</strong> - Should we use a validating XML parser to
 *     process the configuration file (strongly recommended)? [true]</li>
 * </ul>
 *
 * @version $Rev: 264684 $ $Date$
 */
public class ActionServlet extends HttpServlet {


    // ----------------------------------------------------- Instance Variables


    /**
     * <p>Comma-separated list of context-relative path(s) to our configuration
     * resource(s) for the default module.</p>
     */
    protected String config = "/WEB-INF/struts-config.xml";


    /**
     * <p>The Digester used to produce ModuleConfig objects from a
     * Struts configuration file.</p>
     *
     * @since Struts 1.1
     */
    protected Digester configDigester = null;


    /**
     * <p>The flag to request backwards-compatible conversions for form bean
     * properties of the Java wrapper class types.</p>
     *
     * @since Struts 1.1
     */
    protected boolean convertNull = false;


    /**
     * <p>The JDBC data sources that has been configured for this module,
     * if any, keyed by the servlet context attribute under which they are
     * stored.</p>
     */
    protected FastHashMap dataSources = new FastHashMap();


    /**
     * <p>The resources object for our internal resources.</p>
     */
    protected MessageResources internal = null;


    /**
     * <p>The Java base name of our internal resources.</p>
     * @since Struts 1.1
     */
    protected String internalName = "org.apache.struts.action.ActionResources";


    /**
     * <p>Commons Logging instance.</p>
     *
     * @since Struts 1.1
     */
    protected static Log log = LogFactory.getLog(ActionServlet.class);


    /**
     * <p>The <code>RequestProcessor</code> instance we will use to process
     * all incoming requests.</p>
     *
     * @since Struts 1.1
     */
    protected RequestProcessor processor = null;


    /**
     * <p>The set of public identifiers, and corresponding resource names, for
     * the versions of the configuration file DTDs that we know about.  There
     * <strong>MUST</strong> be an even number of Strings in this list!</p>
     */
    protected String registrations[] = {
        "-//Apache Software Foundation//DTD Struts Configuration 1.0//EN",
        "/org/apache/struts/resources/struts-config_1_0.dtd",
        "-//Apache Software Foundation//DTD Struts Configuration 1.1//EN",
        "/org/apache/struts/resources/struts-config_1_1.dtd",
        "-//Apache Software Foundation//DTD Struts Configuration 1.2//EN",
        "/org/apache/struts/resources/struts-config_1_2.dtd",
        "-//Sun Microsystems, Inc.//DTD Web Application 2.2//EN",
        "/org/apache/struts/resources/web-app_2_2.dtd",
        "-//Sun Microsystems, Inc.//DTD Web Application 2.3//EN",
        "/org/apache/struts/resources/web-app_2_3.dtd"
    };


    /**
     * <p>The URL pattern to which we are mapped in our web application
     * deployment descriptor.</p>
     */
    protected String servletMapping = null; // :FIXME: - multiples?


    /**
     * <p>The servlet name under which we are registered in our web application
     * deployment descriptor.</p>
     */
    protected String servletName = null;


    // ---------------------------------------------------- HttpServlet Methods


    /**
     * <p>Gracefully shut down this controller servlet, releasing any resources
     * that were allocated at initialization.</p>
     */
    public void destroy() {

        if (log.isDebugEnabled()) {
            log.debug(internal.getMessage("finalizing"));
        }

        destroyModules();
        destroyInternal();
        getServletContext().removeAttribute(Globals.ACTION_SERVLET_KEY);

        // Release our LogFactory and Log instances (if any)
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        if (classLoader == null) {
            classLoader = ActionServlet.class.getClassLoader();
        }
        try {
            LogFactory.release(classLoader);
        } catch (Throwable t) {
            ; // Servlet container doesn't have the latest version
            ; // of commons-logging-api.jar installed

            // :FIXME: Why is this dependent on the container's version of commons-logging?
            // Shouldn't this depend on the version packaged with Struts?
            /*
              Reason: LogFactory.release(classLoader); was added as
              an attempt to investigate the OutOfMemory error reported on Bugzilla #14042.
              It was committed for version 1.136 by craigmcc
            */
        }

        PropertyUtils.clearDescriptors();

    }


    /**
     * <p>Initialize this servlet.  Most of the processing has been factored into
     * support methods so that you can override particular functionality at a
     * fairly granular level.</p>
     *
     * @exception ServletException if we cannot configure ourselves correctly
     */
    public void init() throws ServletException {

        // Wraps the entire initialization in a try/catch to better handle
        // unexpected exceptions and errors to provide better feedback
        // to the developer
        try {
            initInternal();
            initOther();
            initServlet();
    
            getServletContext().setAttribute(Globals.ACTION_SERVLET_KEY, this);
            initModuleConfigFactory();
            // Initialize modules as needed
            ModuleConfig moduleConfig = initModuleConfig("", config);
            initModuleMessageResources(moduleConfig);
            initModuleDataSources(moduleConfig);
            initModulePlugIns(moduleConfig);
            moduleConfig.freeze();
    
            Enumeration names = getServletConfig().getInitParameterNames();
            while (names.hasMoreElements()) {
                String name = (String) names.nextElement();
                if (!name.startsWith("config/")) {
                    continue;
                }
                String prefix = name.substring(6);
                moduleConfig = initModuleConfig
                    (prefix, getServletConfig().getInitParameter(name));
                initModuleMessageResources(moduleConfig);
                initModuleDataSources(moduleConfig);
                initModulePlugIns(moduleConfig);
                moduleConfig.freeze();
            }
    
            this.initModulePrefixes(this.getServletContext());
    
            this.destroyConfigDigester();
        } catch (UnavailableException ex) {
            throw ex;
        } catch (Throwable t) {

            // The follow error message is not retrieved from internal message
            // resources as they may not have been able to have been 
            // initialized
            log.error("Unable to initialize Struts ActionServlet due to an "
                + "unexpected exception or error thrown, so marking the "
                + "servlet as unavailable.  Most likely, this is due to an "
                + "incorrect or missing library dependency.", t);
            throw new UnavailableException(t.getMessage());
        }    
    }

    /**
     * <p>Saves a String[] of module prefixes in the ServletContext under
     * Globals.MODULE_PREFIXES_KEY.  <strong>NOTE</strong> -
     * the "" prefix for the default module is not included in this list.</p>
     *
     * @param context The servlet context.
     * @since Struts 1.2
     */
    protected void initModulePrefixes(ServletContext context) {
        ArrayList prefixList = new ArrayList();

        Enumeration names = context.getAttributeNames();
        while (names.hasMoreElements()) {
            String name = (String) names.nextElement();
            if (!name.startsWith(Globals.MODULE_KEY)) {
                continue;
            }

            String prefix = name.substring(Globals.MODULE_KEY.length());
            if (prefix.length() > 0) {
                prefixList.add(prefix);
            }
        }

        String[] prefixes = (String[]) prefixList.toArray(new String[prefixList.size()]);
        context.setAttribute(Globals.MODULE_PREFIXES_KEY, prefixes);
    }


    /**
     * <p>Process an HTTP "GET" request.</p>
     *
     * @param request The servlet request we are processing
     * @param response The servlet response we are creating
     *
     * @exception IOException if an input/output error occurs
     * @exception ServletException if a servlet exception occurs
     */
    public void doGet(HttpServletRequest request,
              HttpServletResponse response)
        throws IOException, ServletException {

        process(request, response);

    }


    /**
     * <p>Process an HTTP "POST" request.</p>
     *
     * @param request The servlet request we are processing
     * @param response The servlet response we are creating
     *
     * @exception IOException if an input/output error occurs
     * @exception ServletException if a servlet exception occurs
     */
    public void doPost(HttpServletRequest request,
               HttpServletResponse response)
        throws IOException, ServletException {

        process(request, response);

    }


    // --------------------------------------------------------- Public Methods


    /**
     * <p>Remember a servlet mapping from our web application deployment
     * descriptor, if it is for this servlet.</p>
     *
     * @param servletName The name of the servlet being mapped
     * @param urlPattern The URL pattern to which this servlet is mapped
     */
    public void addServletMapping(String servletName, String urlPattern) {

        if (log.isDebugEnabled()) {
            log.debug("Process servletName=" + servletName +
                      ", urlPattern=" + urlPattern);
        }
        if (servletName == null) {
            return;
        }
        if (servletName.equals(this.servletName)) {
            this.servletMapping = urlPattern;
        }

    }


    /**
     * <p>Return the <code>MessageResources</code> instance containing our
     * internal message strings.</p>
     *
     * @since Struts 1.1
     */
    public MessageResources getInternal() {

        return (this.internal);

    }


    // ------------------------------------------------------ Protected Methods

    /**
     * <p>Gracefully terminate use of any modules associated with this
     * application (if any).</p>
     *
     * @since Struts 1.1
     */
    protected void destroyModules() {

        ArrayList values = new ArrayList();
        Enumeration names = getServletContext().getAttributeNames();
        while (names.hasMoreElements()) {
            values.add(names.nextElement());
        }

        Iterator keys = values.iterator();
        while (keys.hasNext()) {
            String name = (String) keys.next();
            Object value = getServletContext().getAttribute(name);

            if (!(value instanceof ModuleConfig)) {
                continue;
            }

            ModuleConfig config = (ModuleConfig) value;

            if (this.getProcessorForModule(config) != null) {
                this.getProcessorForModule(config).destroy();
            }

            getServletContext().removeAttribute(name);

            PlugIn plugIns[] =
                (PlugIn[]) getServletContext().getAttribute(
                    Globals.PLUG_INS_KEY + config.getPrefix());

            if (plugIns != null) {
                for (int i = 0; i < plugIns.length; i++) {
                    int j = plugIns.length - (i + 1);
                    plugIns[j].destroy();
                }

                getServletContext().removeAttribute(
                    Globals.PLUG_INS_KEY + config.getPrefix());
            }

        }

    }


    /**
     * <p>Gracefully release any configDigester instance that we have created.</p>
     *
     * @since Struts 1.1
     */
    protected void destroyConfigDigester() {

        configDigester = null;

    }


    /**
     * <p>Gracefully terminate use of the internal MessageResources.</p>
     */
    protected void destroyInternal() {

        internal = null;

    }

    /**
     * <p>Return the module configuration object for the currently selected
     * module.</p>
     *
     * @param request The servlet request we are processing
     * @since Struts 1.1
     */
    protected ModuleConfig getModuleConfig
        (HttpServletRequest request) {

        ModuleConfig config = (ModuleConfig)
            request.getAttribute(Globals.MODULE_KEY);
        if (config == null) {
            config = (ModuleConfig)
                getServletContext().getAttribute(Globals.MODULE_KEY);
        }
        return (config);

    }


    /**
     * <p>Look up and return the {@link RequestProcessor} responsible for the
     * specified module, creating a new one if necessary.</p>
     *
     * @param config The module configuration for which to
     *  acquire and return a RequestProcessor.
     *
     * @exception ServletException if we cannot instantiate a RequestProcessor
     *  instance
     * @since Struts 1.1
     */
    protected synchronized RequestProcessor getRequestProcessor(ModuleConfig config)
        throws ServletException {

        // :FIXME: Document UnavailableException?

        RequestProcessor processor = this.getProcessorForModule(config);

        if (processor == null) {
            try {
                processor =
                    (RequestProcessor) RequestUtils.applicationInstance(
                        config.getControllerConfig().getProcessorClass());

            } catch (Exception e) {
                throw new UnavailableException(
                    "Cannot initialize RequestProcessor of class "
                        + config.getControllerConfig().getProcessorClass()
                        + ": "
                        + e);
            }

            processor.init(this, config);

            String key = Globals.REQUEST_PROCESSOR_KEY + config.getPrefix();
            getServletContext().setAttribute(key, processor);

        }

        return (processor);

    }


    /**
     * <p>Returns the RequestProcessor for the given module or null if one does not
     * exist.  This method will not create a RequestProcessor.</p>
     *
     * @param config The ModuleConfig.
     */
    private RequestProcessor getProcessorForModule(ModuleConfig config) {
        String key = Globals.REQUEST_PROCESSOR_KEY + config.getPrefix();
        return (RequestProcessor) getServletContext().getAttribute(key);
    }


    /**
     * <p>Initialize the factory used to create the module configuration.</p>
     * @since Struts 1.2
     */
    protected void initModuleConfigFactory(){
        String configFactory = getServletConfig().getInitParameter("configFactory");
        if (configFactory != null) {
            ModuleConfigFactory.setFactoryClass(configFactory);
        }
    }


    /**
     * <p>Initialize the module configuration information for the
     * specified module.</p>
     *
     * @param prefix Module prefix for this module
     * @param paths Comma-separated list of context-relative resource path(s)
     *  for this modules's configuration resource(s)
     *
     * @exception ServletException if initialization cannot be performed
     * @since Struts 1.1
     */
    protected ModuleConfig initModuleConfig(String prefix, String paths)
        throws ServletException {

        // :FIXME: Document UnavailableException? (Doesn't actually throw anything)

        if (log.isDebugEnabled()) {
            log.debug(
                "Initializing module path '"
                    + prefix
                    + "' configuration from '"
                    + paths
                    + "'");
        }

        // Parse the configuration for this module
        ModuleConfigFactory factoryObject = ModuleConfigFactory.createFactory();
        ModuleConfig config = factoryObject.createModuleConfig(prefix);

        // Configure the Digester instance we will use
        Digester digester = initConfigDigester();

        // Process each specified resource path
        while (paths.length() > 0) {
            digester.push(config);
            String path = null;
            int comma = paths.indexOf(',');
            if (comma >= 0) {
                path = paths.substring(0, comma).trim();
                paths = paths.substring(comma + 1);
            } else {
                path = paths.trim();
                paths = "";
            }

            if (path.length() < 1) {
                break;
            }

            this.parseModuleConfigFile(digester, path);
        }

        getServletContext().setAttribute(
            Globals.MODULE_KEY + config.getPrefix(),
            config);

        // Force creation and registration of DynaActionFormClass instances
        // for all dynamic form beans we wil be using
        FormBeanConfig fbs[] = config.findFormBeanConfigs();
        for (int i = 0; i < fbs.length; i++) {
            if (fbs[i].getDynamic()) {
                fbs[i].getDynaActionFormClass();
            }
        }

        return config;
    }


    /**
     * <p>Parses one module config file.</p>
     *
     * @param digester Digester instance that does the parsing
     * @param path The path to the config file to parse.
     *
     * @throws UnavailableException if file cannot be read or parsed
     * @since Struts 1.2
     */
    protected void parseModuleConfigFile(Digester digester, String path)
        throws UnavailableException {

        InputStream input = null;
        try {
            URL url = getServletContext().getResource(path);

            // If the config isn't in the servlet context, try the class loader
            // which allows the config files to be stored in a jar
            if (url == null) {
                url = getClass().getResource(path);
            }
            
            if (url == null) {
                String msg = internal.getMessage("configMissing", path);
                log.error(msg);
                throw new UnavailableException(msg);
            }
	    
            InputSource is = new InputSource(url.toExternalForm());
            input = url.openStream();
            is.setByteStream(input);
            digester.parse(is);

        } catch (MalformedURLException e) {
            handleConfigException(path, e);
        } catch (IOException e) {
            handleConfigException(path, e);
        } catch (SAXException e) {
            handleConfigException(path, e);
        } finally {
            if (input != null) {
                try {
                    input.close();
                } catch (IOException e) {
                    throw new UnavailableException(e.getMessage());
                }
            }
        }
    }


    /**
     * <p>Simplifies exception handling in the <code>parseModuleConfigFile</code> method.<p>
     * @param path
     * @param e
     * @throws UnavailableException as a wrapper around Exception
     */
    private void handleConfigException(String path, Exception e)
        throws UnavailableException {

        String msg = internal.getMessage("configParse", path);
        log.error(msg, e);
        throw new UnavailableException(msg);
    }


    /**
     * <p>Initialize the data sources for the specified module.</p>
     *
     * @param config ModuleConfig information for this module
     *
     * @exception ServletException if initialization cannot be performed
     * @since Struts 1.1
     */
    protected void initModuleDataSources(ModuleConfig config) throws ServletException {

        // :FIXME: Document UnavailableException?

        if (log.isDebugEnabled()) {
            log.debug("Initializing module path '" + config.getPrefix() +
                "' data sources");
        }

        ServletContextWriter scw =
            new ServletContextWriter(getServletContext());
        DataSourceConfig dscs[] = config.findDataSourceConfigs();
        if (dscs == null) {
            dscs = new DataSourceConfig[0];
        }

        dataSources.setFast(false);
        for (int i = 0; i < dscs.length; i++) {
            if (log.isDebugEnabled()) {
                log.debug("Initializing module path '" + config.getPrefix() +
                    "' data source '" + dscs[i].getKey() + "'");
            }
            DataSource ds = null;
            try {
                ds = (DataSource)
                    RequestUtils.applicationInstance(dscs[i].getType());
                BeanUtils.populate(ds, dscs[i].getProperties());
                ds.setLogWriter(scw);

            } catch (Exception e) {
                log.error(internal.getMessage("dataSource.init", dscs[i].getKey()), e);
                throw new UnavailableException
                    (internal.getMessage("dataSource.init", dscs[i].getKey()));
            }
            getServletContext().setAttribute
                (dscs[i].getKey() + config.getPrefix(), ds);
            dataSources.put(dscs[i].getKey(), ds);
        }

        dataSources.setFast(true);

    }


    /**
     * <p>Initialize the plug ins for the specified module.</p>
     *
     * @param config ModuleConfig information for this module
     *
     * @exception ServletException if initialization cannot be performed
     * @since Struts 1.1
     */
    protected void initModulePlugIns
        (ModuleConfig config) throws ServletException {

        if (log.isDebugEnabled()) {
            log.debug("Initializing module path '" + config.getPrefix() + "' plug ins");
        }

        PlugInConfig plugInConfigs[] = config.findPlugInConfigs();
        PlugIn plugIns[] = new PlugIn[plugInConfigs.length];

        getServletContext().setAttribute(Globals.PLUG_INS_KEY + config.getPrefix(), plugIns);
        for (int i = 0; i < plugIns.length; i++) {
            try {
                plugIns[i] =
                    (PlugIn)RequestUtils.applicationInstance(plugInConfigs[i].getClassName());
                 BeanUtils.populate(plugIns[i], plugInConfigs[i].getProperties());
                  // Pass the current plugIn config object to the PlugIn.
                  // The property is set only if the plugin declares it.
                  // This plugin config object is needed by Tiles
                try {
                    PropertyUtils.setProperty(
                        plugIns[i],
                        "currentPlugInConfigObject",
                        plugInConfigs[i]);
                } catch (Exception e) {
                  // FIXME Whenever we fail silently, we must document a valid reason
                  // for doing so.  Why should we fail silently if a property can't be set on
                  // the plugin?
                    /**
                     * Between version 1.138-1.140 cedric made these changes.
                     * The exceptions are caught to deal with containers applying strict security.
                     * This was in response to bug #15736
                     *
                     * Recommend that we make the currentPlugInConfigObject part of the PlugIn Interface if we can, Rob
                     */
                }
                plugIns[i].init(this, config);

            } catch (ServletException e) {
                throw e;
            } catch (Exception e) {
                String errMsg =
                    internal.getMessage(
                        "plugIn.init",
                        plugInConfigs[i].getClassName());

                log(errMsg, e);
                throw new UnavailableException(errMsg);
            }
        }

    }


    /**
     * <p>Initialize the application <code>MessageResources</code> for the specified
     * module.</p>
     *
     * @param config ModuleConfig information for this module
     *
     * @exception ServletException if initialization cannot be performed
     * @since Struts 1.1
     */
    protected void initModuleMessageResources(ModuleConfig config)
        throws ServletException {

        MessageResourcesConfig mrcs[] = config.findMessageResourcesConfigs();
        for (int i = 0; i < mrcs.length; i++) {
            if ((mrcs[i].getFactory() == null)
                || (mrcs[i].getParameter() == null)) {
                continue;
            }
            if (log.isDebugEnabled()) {
                log.debug(
                    "Initializing module path '"
                        + config.getPrefix()
                        + "' message resources from '"
                        + mrcs[i].getParameter()
                        + "'");
            }

            String factory = mrcs[i].getFactory();
            MessageResourcesFactory.setFactoryClass(factory);
            MessageResourcesFactory factoryObject =
                MessageResourcesFactory.createFactory();
            factoryObject.setConfig(mrcs[i]);

            MessageResources resources =
                factoryObject.createResources(mrcs[i].getParameter());
            resources.setReturnNull(mrcs[i].getNull());
            resources.setEscape(mrcs[i].isEscape());
            getServletContext().setAttribute(
                mrcs[i].getKey() + config.getPrefix(),
                resources);
        }

    }


    /**
     * <p>Create (if needed) and return a new <code>Digester</code>
     * instance that has been initialized to process Struts module
     * configuration files and configure a corresponding <code>ModuleConfig</code>
     * object (which must be pushed on to the evaluation stack before parsing
     * begins).</p>
     *
     * @exception ServletException if a Digester cannot be configured
     * @since Struts 1.1
     */
    protected Digester initConfigDigester() throws ServletException {

        // :FIXME: Where can ServletException be thrown?

        // Do we have an existing instance?
        if (configDigester != null) {
            return (configDigester);
        }

        // Create a new Digester instance with standard capabilities
        configDigester = new Digester();
        configDigester.setNamespaceAware(true);
        configDigester.setValidating(this.isValidating());
        configDigester.setUseContextClassLoader(true);
        configDigester.addRuleSet(new ConfigRuleSet());

        for (int i = 0; i < registrations.length; i += 2) {
            URL url = this.getClass().getResource(registrations[i+1]);
            if (url != null) {
                configDigester.register(registrations[i], url.toString());
            }
        }

        this.addRuleSets();

        // Return the completely configured Digester instance
        return (configDigester);
    }


    /**
     * <p>Add any custom RuleSet instances to configDigester that have
     * been specified in the <code>rulesets</code> init parameter.</p>
     *
     * @throws ServletException
     */
    private void addRuleSets() throws ServletException {

        String rulesets = getServletConfig().getInitParameter("rulesets");
        if (rulesets == null) {
            rulesets = "";
        }

        rulesets = rulesets.trim();
        String ruleset = null;
        while (rulesets.length() > 0) {
            int comma = rulesets.indexOf(",");
            if (comma < 0) {
                ruleset = rulesets.trim();
                rulesets = "";
            } else {
                ruleset = rulesets.substring(0, comma).trim();
                rulesets = rulesets.substring(comma + 1).trim();
            }

            if (log.isDebugEnabled()) {
                log.debug("Configuring custom Digester Ruleset of type " + ruleset);
            }

            try {
                RuleSet instance = (RuleSet) RequestUtils.applicationInstance(ruleset);
                this.configDigester.addRuleSet(instance);
            } catch (Exception e) {
                log.error("Exception configuring custom Digester RuleSet", e);
                throw new ServletException(e);
            }
        }
    }


    /**
     * <p>Check the status of the <code>validating</code> initialization parameter.</p>
     *
     * @return true if the module Digester should validate.
     */
    private boolean isValidating() {

        boolean validating = true;
        String value = getServletConfig().getInitParameter("validating");

        if ("false".equalsIgnoreCase(value)
            || "no".equalsIgnoreCase(value)
            || "n".equalsIgnoreCase(value)
            || "0".equalsIgnoreCase(value)) {

            validating = false;
        }

        return validating;
    }



    /**
     * <p>Initialize our internal MessageResources bundle.</p>
     *
     * @exception ServletException if we cannot initialize these resources
     */
    protected void initInternal() throws ServletException {

        // :FIXME: Document UnavailableException

        try {
            internal = MessageResources.getMessageResources(internalName);
        } catch (MissingResourceException e) {
            log.error("Cannot load internal resources from '" + internalName + "'",
                e);
            throw new UnavailableException
                ("Cannot load internal resources from '" + internalName + "'");
        }

    }


    /**
     * <p>Initialize other global characteristics of the controller servlet.</p>
     *
     * @exception ServletException if we cannot initialize these resources
     */
    protected void initOther() throws ServletException {

        String value = null;
        value = getServletConfig().getInitParameter("config");
        if (value != null) {
            config = value;
        }

        // Backwards compatibility for form beans of Java wrapper classes
        // Set to true for strict Struts 1.0 compatibility
        value = getServletConfig().getInitParameter("convertNull");
        if ("true".equalsIgnoreCase(value)
            || "yes".equalsIgnoreCase(value)
            || "on".equalsIgnoreCase(value)
            || "y".equalsIgnoreCase(value)
            || "1".equalsIgnoreCase(value)) {

            convertNull = true;
        }

        if (convertNull) {
            ConvertUtils.deregister();
            ConvertUtils.register(new BigDecimalConverter(null), BigDecimal.class);
            ConvertUtils.register(new BigIntegerConverter(null), BigInteger.class);
            ConvertUtils.register(new BooleanConverter(null), Boolean.class);
            ConvertUtils.register(new ByteConverter(null), Byte.class);
            ConvertUtils.register(new CharacterConverter(null), Character.class);
            ConvertUtils.register(new DoubleConverter(null), Double.class);
            ConvertUtils.register(new FloatConverter(null), Float.class);
            ConvertUtils.register(new IntegerConverter(null), Integer.class);
            ConvertUtils.register(new LongConverter(null), Long.class);
            ConvertUtils.register(new ShortConverter(null), Short.class);
        }

    }


    /**
     * <p>Initialize the servlet mapping under which our controller servlet
     * is being accessed.  This will be used in the <code>&html:form&gt;</code>
     * tag to generate correct destination URLs for form submissions.</p>
     *
     * @throws ServletException if error happens while scanning web.xml
     */
    protected void initServlet() throws ServletException {

        // Remember our servlet name
        this.servletName = getServletConfig().getServletName();

        // Prepare a Digester to scan the web application deployment descriptor
        Digester digester = new Digester();
        digester.push(this);
        digester.setNamespaceAware(true);
        digester.setValidating(false);

        // Register our local copy of the DTDs that we can find
        for (int i = 0; i < registrations.length; i += 2) {
            URL url = this.getClass().getResource(registrations[i+1]);
            if (url != null) {
                digester.register(registrations[i], url.toString());
            }
        }

        // Configure the processing rules that we need
        digester.addCallMethod("web-app/servlet-mapping",
                               "addServletMapping", 2);
        digester.addCallParam("web-app/servlet-mapping/servlet-name", 0);
        digester.addCallParam("web-app/servlet-mapping/url-pattern", 1);

        // Process the web application deployment descriptor
        if (log.isDebugEnabled()) {
            log.debug("Scanning web.xml for controller servlet mapping");
        }

        InputStream input =
            getServletContext().getResourceAsStream("/WEB-INF/web.xml");

        if (input == null) {
            log.error(internal.getMessage("configWebXml"));
            throw new ServletException(internal.getMessage("configWebXml"));
        }

        try {
            digester.parse(input);

        } catch (IOException e) {
            log.error(internal.getMessage("configWebXml"), e);
            throw new ServletException(e);

        } catch (SAXException e) {
            log.error(internal.getMessage("configWebXml"), e);
            throw new ServletException(e);

        } finally {
            try {
                input.close();
            } catch (IOException e) {
                log.error(internal.getMessage("configWebXml"), e);
                throw new ServletException(e);
            }
        }

        // Record a servlet context attribute (if appropriate)
        if (log.isDebugEnabled()) {
            log.debug("Mapping for servlet '" + servletName + "' = '" +
                servletMapping + "'");
        }

        if (servletMapping != null) {
            getServletContext().setAttribute(Globals.SERVLET_KEY, servletMapping);
        }

    }


    /**
     * <p>Perform the standard request processing for this request, and create
     * the corresponding response.</p>
     *
     * @param request The servlet request we are processing
     * @param response The servlet response we are creating
     *
     * @exception IOException if an input/output error occurs
     * @exception ServletException if a servlet exception is thrown
     */
    protected void process(HttpServletRequest request, HttpServletResponse response)
        throws IOException, ServletException {

        ModuleUtils.getInstance().selectModule(request, getServletContext());
        ModuleConfig config = getModuleConfig(request);

        RequestProcessor processor = getProcessorForModule(config);
        if (processor == null) {
           processor = getRequestProcessor(config);
        }
        processor.process(request, response);

    }

}

/*
 *
 *
 * Copyright 1999-2004 The Apache Software Foundation.
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

package org.apache.struts.config.impl;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;

import org.apache.struts.config.ActionConfig;
import org.apache.struts.config.ActionConfigMatcher;
import org.apache.struts.config.ControllerConfig;
import org.apache.struts.config.DataSourceConfig;
import org.apache.struts.config.ExceptionConfig;
import org.apache.struts.config.FormBeanConfig;
import org.apache.struts.config.ForwardConfig;
import org.apache.struts.config.MessageResourcesConfig;
import org.apache.struts.config.ModuleConfig;
import org.apache.struts.config.PlugInConfig;

/**
 * <p>The collection of static configuration information that describes a
 * Struts-based module.  Multiple modules are identified by
 * a <em>prefix</em> at the beginning of the context
 * relative portion of the request URI.  If no module prefix can be
 * matched, the default configuration (with a prefix equal to a zero-length
 * string) is selected, which is elegantly backwards compatible with the
 * previous Struts behavior that only supported one module.</p>
 *
 * @version $Rev: 170858 $ $Date$
 * @since Struts 1.1
 */
public class ModuleConfigImpl implements Serializable, ModuleConfig {

    /**
     * Constructor for ModuleConfigImpl.  Assumes default configuration.
     *
     * @since Struts 1.2.8
     */
    public ModuleConfigImpl() {
        this("/");
    }
    
    /**
     * Construct an ModuleConfigImpl object according to the specified
     * parameter values.
     *
     * @param prefix Context-relative URI prefix for this module
     */
    public ModuleConfigImpl(String prefix) {
        super();
        this.prefix = prefix;
        this.actionConfigs = new HashMap();
        this.actionConfigList = new ArrayList();
        this.actionFormBeanClass = "org.apache.struts.action.ActionFormBean";
        this.actionMappingClass = "org.apache.struts.action.ActionMapping";
        this.actionForwardClass = "org.apache.struts.action.ActionForward";
        this.configured = false;
        this.controllerConfig = null;
        this.dataSources = new HashMap();
        this.exceptions = new HashMap();
        this.formBeans = new HashMap();
        this.forwards = new HashMap();
        this.messageResources = new HashMap();
        this.plugIns = new ArrayList();
    }


    // --------------------------------------------------------- Public Methods

    /**
     * Has this module been completely configured yet.  Once this flag
     * has been set, any attempt to modify the configuration will return an
     * IllegalStateException.
     */
    public boolean getConfigured() {
        return (this.configured);
    }

    /**
     * The controller configuration object for this module.
     */
    public ControllerConfig getControllerConfig() {
        if (this.controllerConfig == null) {
            this.controllerConfig = new ControllerConfig();
        }
        return (this.controllerConfig);
    }

    /**
     * The controller configuration object for this module.
     * @param cc   The controller configuration object for this module.
     */
    public void setControllerConfig(ControllerConfig cc) {
        if (configured) {
            throw new IllegalStateException("Configuration is frozen");
        }
        this.controllerConfig = cc;
    }

    /**
     * The prefix of the context-relative portion of the request URI, used to
     * select this configuration versus others supported by the controller
     * servlet.  A configuration with a prefix of a zero-length String is the
     * default configuration for this web module.
     */
    public String getPrefix() {
        return (this.prefix);
    }

    /**
     * The prefix of the context-relative portion of the request URI, used to
     * select this configuration versus others supported by the controller
     * servlet.  A configuration with a prefix of a zero-length String is the
     * default configuration for this web module.
     */
    public void setPrefix(String prefix) {
        if (configured) {
            throw new IllegalStateException("Configuration is frozen");
        }
        this.prefix = prefix;
    }

    /**
     * The default class name to be used when creating action form bean
     * instances.
     */
    public String getActionFormBeanClass() {
        return this.actionFormBeanClass;
    }

    /**
     * The default class name to be used when creating action form bean
     * instances.
     *
     * @param actionFormBeanClass default class name to be used when creating 
     *                            action form bean instances.
     */
    public void setActionFormBeanClass(String actionFormBeanClass) {
        this.actionFormBeanClass = actionFormBeanClass;
    }

    /**
     * The default class name to be used when creating action mapping instances.
     */
    public String getActionMappingClass() {
        return this.actionMappingClass;
    }

    /**
     * The default class name to be used when creating action mapping instances.
     *
     * @param actionMappingClass default class name to be used when creating 
     *                           action mapping instances.
     */
    public void setActionMappingClass(String actionMappingClass) {
        this.actionMappingClass = actionMappingClass;
    }

    /**
     * Add a new <code>ActionConfig</code> instance to the set associated
     * with this module.
     *
     * @param config The new configuration instance to be added
     *
     * @exception java.lang.IllegalStateException if this module configuration
     *  has been frozen
     */
    public void addActionConfig(ActionConfig config) {

        if (configured) {
            throw new IllegalStateException("Configuration is frozen");
        }
        config.setModuleConfig(this);
        actionConfigs.put(config.getPath(), config);
        actionConfigList.add(config);

    }

    /**
     * Add a new <code>DataSourceConfig</code> instance to the set associated
     * with this module.
     *
     * @param config The new configuration instance to be added
     *
     * @exception java.lang.IllegalStateException if this module configuration
     *  has been frozen
     */
    public void addDataSourceConfig(DataSourceConfig config) {

        if (configured) {
            throw new IllegalStateException("Configuration is frozen");
        }
        dataSources.put(config.getKey(), config);

    }

    /**
     * Add a new <code>ExceptionConfig</code> instance to the set associated
     * with this module.
     *
     * @param config The new configuration instance to be added
     *
     * @exception java.lang.IllegalStateException if this module configuration
     *  has been frozen
     */
    public void addExceptionConfig(ExceptionConfig config) {

        if (configured) {
            throw new IllegalStateException("Configuration is frozen");
        }
        exceptions.put(config.getType(), config);

    }

    /**
     * Add a new <code>FormBeanConfig</code> instance to the set associated
     * with this module.
     *
     * @param config The new configuration instance to be added
     *
     * @exception java.lang.IllegalStateException if this module configuration
     *  has been frozen
     */
    public void addFormBeanConfig(FormBeanConfig config) {

        if (configured) {
            throw new IllegalStateException("Configuration is frozen");
        }
        formBeans.put(config.getName(), config);

    }

    /**
     * The default class name to be used when creating action forward instances.
     */
    public String getActionForwardClass() {
        return this.actionForwardClass;
    }

    /**
     * The default class name to be used when creating action forward instances.
     *
     * @param actionForwardClass default class name to be used when creating
     *                           action forward instances.
     */
    public void setActionForwardClass(String actionForwardClass) {
        this.actionForwardClass= actionForwardClass;
    }

    /**
     * Add a new <code>ForwardConfig</code> instance to the set of global
     * forwards associated with this module.
     *
     * @param config The new configuration instance to be added
     *
     * @exception java.lang.IllegalStateException if this module configuration
     *  has been frozen
     */
    public void addForwardConfig(ForwardConfig config) {

        if (configured) {
            throw new IllegalStateException("Configuration is frozen");
        }
        forwards.put(config.getName(), config);

    }

    /**
     * Add a new <code>MessageResourcesConfig</code> instance to the set
     * associated with this module.
     *
     * @param config The new configuration instance to be added
     *
     * @exception java.lang.IllegalStateException if this module configuration
     *  has been frozen
     */
    public void addMessageResourcesConfig(MessageResourcesConfig config) {

        if (configured) {
            throw new IllegalStateException("Configuration is frozen");
        }
        messageResources.put(config.getKey(), config);

    }

    /**
     * Add a newly configured {@link org.apache.struts.config.PlugInConfig} instance to the set of
     * plug-in Actions for this module.
     *
     * @param plugInConfig The new configuration instance to be added
     */
    public void addPlugInConfig(PlugInConfig plugInConfig) {

        if (configured) {
            throw new IllegalStateException("Configuration is frozen");
        }
        plugIns.add(plugInConfig);

    }

    /**
     * Return the action configuration for the specified path, first looking
     * a direct match, then if none found, a wildcard pattern match;
     * otherwise return <code>null</code>.
     *
     * @param path Path of the action configuration to return
     */
    public ActionConfig findActionConfig(String path) {

        ActionConfig config = (ActionConfig) actionConfigs.get(path);
        
        // If a direct match cannot be found, try to match action configs 
        // containing wildcard patterns
        if (config == null) {
            config = matcher.match(path);
        }
        
        return config;

    }

    /**
     * Return the action configurations for this module.  If there are
     * none, a zero-length array is returned.
     */
    public ActionConfig[] findActionConfigs() {

        ActionConfig results[] = new ActionConfig[actionConfigList.size()];
        return ((ActionConfig[]) actionConfigList.toArray(results));

    }

    /**
     * Return the data source configuration for the specified key, if any;
     * otherwise return <code>null</code>.
     *
     * @param key Key of the data source configuration to return
     */
    public DataSourceConfig findDataSourceConfig(String key) {

        return ((DataSourceConfig) dataSources.get(key));

    }

    /**
     * Return the data source configurations for this module.  If there
     * are none, a zero-length array is returned.
     */
    public DataSourceConfig[] findDataSourceConfigs() {

        DataSourceConfig results[] = new DataSourceConfig[dataSources.size()];
        return ((DataSourceConfig[]) dataSources.values().toArray(results));

    }

    /**
     * Return the exception configuration for the specified type, if any;
     * otherwise return <code>null</code>.
     *
     * @param type Exception class name to find a configuration for
     */
    public ExceptionConfig findExceptionConfig(String type) {

        return ((ExceptionConfig) exceptions.get(type));

    }

    /**
     * Return the exception configurations for this module.  If there
     * are none, a zero-length array is returned.
     */
    public ExceptionConfig[] findExceptionConfigs() {

        ExceptionConfig results[] = new ExceptionConfig[exceptions.size()];
        return ((ExceptionConfig[]) exceptions.values().toArray(results));

    }

    /**
     * Return the form bean configuration for the specified key, if any;
     * otherwise return <code>null</code>.
     *
     * @param name Name of the form bean configuration to return
     */
    public FormBeanConfig findFormBeanConfig(String name) {

        return ((FormBeanConfig) formBeans.get(name));

    }

    /**
     * Return the form bean configurations for this module.  If there
     * are none, a zero-length array is returned.
     */
    public FormBeanConfig[] findFormBeanConfigs() {

        FormBeanConfig results[] = new FormBeanConfig[formBeans.size()];
        return ((FormBeanConfig[]) formBeans.values().toArray(results));

    }

    /**
     * Return the forward configuration for the specified key, if any;
     * otherwise return <code>null</code>.
     *
     * @param name Name of the forward configuration to return
     */
    public ForwardConfig findForwardConfig(String name) {

        return ((ForwardConfig) forwards.get(name));

    }

    /**
     * Return the form bean configurations for this module.  If there
     * are none, a zero-length array is returned.
     */
    public ForwardConfig[] findForwardConfigs() {

        ForwardConfig results[] = new ForwardConfig[forwards.size()];
        return ((ForwardConfig[]) forwards.values().toArray(results));

    }

    /**
     * Return the message resources configuration for the specified key,
     * if any; otherwise return <code>null</code>.
     *
     * @param key Key of the data source configuration to return
     */
    public MessageResourcesConfig findMessageResourcesConfig(String key) {

        return ((MessageResourcesConfig) messageResources.get(key));

    }

    /**
     * Return the message resources configurations for this module.
     * If there are none, a zero-length array is returned.
     */
    public MessageResourcesConfig[] findMessageResourcesConfigs() {

        MessageResourcesConfig results[] =
                new MessageResourcesConfig[messageResources.size()];
        return ((MessageResourcesConfig[])
                messageResources.values().toArray(results));

    }

    /**
     * Return the configured plug-in actions for this module.  If there
     * are none, a zero-length array is returned.
     */
    public PlugInConfig[] findPlugInConfigs() {

        PlugInConfig results[] = new PlugInConfig[plugIns.size()];
        return ((PlugInConfig[]) plugIns.toArray(results));

    }

    /**
     * Freeze the configuration of this module.  After this method
     * returns, any attempt to modify the configuration will return
     * an IllegalStateException.
     */
    public void freeze() {

        this.configured = true;

        ActionConfig[] aconfigs = findActionConfigs();
        for (int i = 0; i < aconfigs.length; i++) {
            aconfigs[i].freeze();
        }
        matcher = new ActionConfigMatcher(aconfigs);

        getControllerConfig().freeze();

        DataSourceConfig[] dsconfigs = findDataSourceConfigs();
        for (int i = 0; i < dsconfigs.length; i++) {
            dsconfigs[i].freeze();
        }

        ExceptionConfig[] econfigs = findExceptionConfigs();
        for (int i = 0; i < econfigs.length; i++) {
            econfigs[i].freeze();
        }

        FormBeanConfig[] fbconfigs = findFormBeanConfigs();
        for (int i = 0; i < fbconfigs.length; i++) {
            fbconfigs[i].freeze();
        }

        ForwardConfig[] fconfigs = findForwardConfigs();
        for (int i = 0; i < fconfigs.length; i++) {
            fconfigs[i].freeze();
        }

        MessageResourcesConfig[] mrconfigs = findMessageResourcesConfigs();
        for (int i = 0; i < mrconfigs.length; i++) {
            mrconfigs[i].freeze();
        }

        PlugInConfig[] piconfigs = findPlugInConfigs();
        for (int i = 0; i < piconfigs.length; i++) {
            piconfigs[i].freeze();
        }
    }

    /**
     * Remove the specified action configuration instance.
     *
     * @param config ActionConfig instance to be removed
     *
     * @exception IllegalStateException if this module configuration
     *  has been frozen
     */
    public void removeActionConfig(ActionConfig config) {

        if (configured) {
            throw new IllegalStateException("Configuration is frozen");
        }
        config.setModuleConfig(null);
        actionConfigs.remove(config.getPath());
        actionConfigList.remove(config);

    }

    /**
     * Remove the specified exception configuration instance.
     *
     * @param config ActionConfig instance to be removed
     *
     * @exception IllegalStateException if this module configuration
     *  has been frozen
     */
    public void removeExceptionConfig(ExceptionConfig config) {

        if (configured) {
            throw new IllegalStateException("Configuration is frozen");
        }
        exceptions.remove(config.getType());

    }

    /**
     * Remove the specified data source configuration instance.
     *
     * @param config DataSourceConfig instance to be removed
     *
     * @exception IllegalStateException if this module configuration
     *  has been frozen
     */
    public void removeDataSourceConfig(DataSourceConfig config) {

        if (configured) {
            throw new IllegalStateException("Configuration is frozen");
        }
        dataSources.remove(config.getKey());

    }

    /**
     * Remove the specified form bean configuration instance.
     *
     * @param config FormBeanConfig instance to be removed
     *
     * @exception IllegalStateException if this module configuration
     *  has been frozen
     */
    public void removeFormBeanConfig(FormBeanConfig config) {

        if (configured) {
            throw new IllegalStateException("Configuration is frozen");
        }
        formBeans.remove(config.getName());

    }

    /**
     * Remove the specified forward configuration instance.
     *
     * @param config ForwardConfig instance to be removed
     *
     * @exception IllegalStateException if this module configuration
     *  has been frozen
     */
    public void removeForwardConfig(ForwardConfig config) {

        if (configured) {
            throw new IllegalStateException("Configuration is frozen");
        }
        forwards.remove(config.getName());

    }

    /**
     * Remove the specified message resources configuration instance.
     *
     * @param config MessageResourcesConfig instance to be removed
     *
     * @exception IllegalStateException if this module configuration
     *  has been frozen
     */
    public void removeMessageResourcesConfig(MessageResourcesConfig config) {

        if (configured) {
            throw new IllegalStateException("Configuration is frozen");
        }
        messageResources.remove(config.getKey());

    }

    // ----------------------------------------------------- Instance Variables
    // Instance Variables at end to make comparing Interface and implementation easier.

    
    /**
     * The set of action configurations for this module, if any,
     * keyed by the <code>path</code> property.
     */
    protected HashMap actionConfigs = null;
    
    /**
     * The set of action configurations for this module, if any,
     * listed in the order in which they are added.
     */
    protected List actionConfigList = null; 
    
    /**
     * The set of JDBC data source configurations for this
     * module, if any, keyed by the <code>key</code> property.
     */
    protected HashMap dataSources = null;
    
    /**
     * The set of exception handling configurations for this
     * module, if any, keyed by the <code>type</code> property.
     */
    protected HashMap exceptions = null;
    
    /**
     * The set of form bean configurations for this module, if any,
     * keyed by the <code>name</code> property.
     */
    protected HashMap formBeans = null;
    
    /**
     * The set of global forward configurations for this module, if any,
     * keyed by the <code>name</code> property.
     */
    protected HashMap forwards = null;
    
    /**
     * The set of message resources configurations for this
     * module, if any, keyed by the <code>key</code> property.
     */
    protected HashMap messageResources = null;
    
    /**
     * The set of configured plug-in Actions for this module,
     * if any, in the order they were declared and configured.
     */
    protected ArrayList plugIns = null;
    
    /**
     * Has this module been completely configured yet.  Once this flag
     * has been set, any attempt to modify the configuration will return an
     * IllegalStateException.
     */
    protected boolean configured = false;
    
    /**
     * The controller configuration object for this module.
     */
    protected ControllerConfig controllerConfig = null;
    
    /**
     * The prefix of the context-relative portion of the request URI, used to
     * select this configuration versus others supported by the controller
     * servlet.  A configuration with a prefix of a zero-length String is the
     * default configuration for this web module.
     */
    protected String prefix = null;
    
    /**
     * The default class name to be used when creating action form bean
     * instances.
     */
    protected String actionFormBeanClass = "org.apache.struts.action.ActionFormBean";
    
    /**
     * The default class name to be used when creating action mapping instances.
     */
    protected String actionMappingClass = "org.apache.struts.action.ActionMapping";
    
    /**
     * The default class name to be used when creating action forward instances.
     */
    protected String actionForwardClass = "org.apache.struts.action.ActionForward";
    
    /**
     * Matches action config paths against compiled wildcard patterns
     */
    protected ActionConfigMatcher matcher = null;

}

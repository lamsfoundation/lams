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
package org.apache.struts.config;

/**
 * <p>The collection of static configuration information that describes a
 * Struts-based module.  Multiple modules are identified by
 * a <em>prefix</em> at the beginning of the context
 * relative portion of the request URI.  If no module prefix can be
 * matched, the default configuration (with a prefix equal to a zero-length
 * string) is selected, which is elegantly backwards compatible with the
 * previous Struts behavior that only supported one module.</p>
 *
 * @version $Rev: 54929 $ $Date$
 * @since Struts 1.1
 */
public interface ModuleConfig {
    /**
     * Has this module been completely configured yet.  Once this flag
     * has been set, any attempt to modify the configuration will return an
     * IllegalStateException.
     */
    boolean getConfigured();

    /**
     * The controller configuration object for this module.
     */
    ControllerConfig getControllerConfig();
    /**
     * The controller configuration object for this module.
     * @param cc   The controller configuration object for this module.
     */

    void setControllerConfig(ControllerConfig cc);

    /**
     * The prefix of the context-relative portion of the request URI, used to
     * select this configuration versus others supported by the controller
     * servlet.  A configuration with a prefix of a zero-length String is the
     * default configuration for this web module.
     */
    String getPrefix();

    /**
     * The prefix of the context-relative portion of the request URI, used to
     * select this configuration versus others supported by the controller
     * servlet.  A configuration with a prefix of a zero-length String is the
     * default configuration for this web module.
     * @param prefix The prefix of the context-relative portion of the request URI.
     */
    public void setPrefix(String prefix);

    /**
     * The default class name to be used when creating action form bean
     * instances.
     */
    String getActionFormBeanClass();

    /**
     * The default class name to be used when creating action form bean
     * instances.
     *
     * @param actionFormBeanClass default class name to be used when creating
     *                           action form bean instances.
     */
    void setActionFormBeanClass(String actionFormBeanClass);

    /**
     * The default class name to be used when creating action mapping instances.
     */
    String getActionMappingClass();

    /**
     * The default class name to be used when creating action mapping instances.
     *
     * @param actionMappingClass default class name to be used when creating
     *                           action mapping instances.
     */
    void setActionMappingClass(String actionMappingClass);

    /**
     * Add a new <code>ActionConfig</code> instance to the set associated
     * with this module.
     *
     * @param config The new configuration instance to be added
     *
     * @exception java.lang.IllegalStateException if this module configuration
     *  has been frozen
     */
    void addActionConfig(ActionConfig config);

    /**
     * Add a new <code>DataSourceConfig</code> instance to the set associated
     * with this module.
     *
     * @param config The new configuration instance to be added
     *
     * @exception java.lang.IllegalStateException if this module configuration
     *  has been frozen
     */
    void addDataSourceConfig(DataSourceConfig config);

    /**
     * Add a new <code>ExceptionConfig</code> instance to the set associated
     * with this module.
     *
     * @param config The new configuration instance to be added
     *
     * @exception java.lang.IllegalStateException if this module configuration
     *  has been frozen
     */
    void addExceptionConfig(ExceptionConfig config);

    /**
     * Add a new <code>FormBeanConfig</code> instance to the set associated
     * with this module.
     *
     * @param config The new configuration instance to be added
     *
     * @exception java.lang.IllegalStateException if this module configuration
     *  has been frozen
     */
    void addFormBeanConfig(FormBeanConfig config);

    /**
     * The default class name to be used when creating action forward instances.
     */
    String getActionForwardClass();

    /**
     * The default class name to be used when creating action forward instances.
     *
     * @param actionForwardClass default class name to be used when creating
     *                           action forward instances.
     */
    void setActionForwardClass(String actionForwardClass);

    /**
     * Add a new <code>ForwardConfig</code> instance to the set of global
     * forwards associated with this module.
     *
     * @param config The new configuration instance to be added
     *
     * @exception java.lang.IllegalStateException if this module configuration
     *  has been frozen
     */
    void addForwardConfig(ForwardConfig config);

    /**
     * Add a new <code>MessageResourcesConfig</code> instance to the set
     * associated with this module.
     *
     * @param config The new configuration instance to be added
     *
     * @exception IllegalStateException if this module configuration
     *  has been frozen
     */
    void addMessageResourcesConfig(MessageResourcesConfig config);

    /**
     * Add a newly configured {@link org.apache.struts.config.PlugInConfig} instance to the set of
     * plug-in Actions for this module.
     *
     * @param plugInConfig The new configuration instance to be added
     */
    void addPlugInConfig(PlugInConfig plugInConfig);

    /**
     * Return the action configuration for the specified path, if any;
     * otherwise return <code>null</code>.
     *
     * @param path Path of the action configuration to return
     */
    ActionConfig findActionConfig(String path);

    /**
     * Return the action configurations for this module.  If there are
     * none, a zero-length array is returned.
     */
    ActionConfig[] findActionConfigs();

    /**
     * Return the data source configuration for the specified key, if any;
     * otherwise return <code>null</code>.
     *
     * @param key Key of the data source configuration to return
     */
    DataSourceConfig findDataSourceConfig(String key);

    /**
     * Return the data source configurations for this module.  If there
     * are none, a zero-length array is returned.
     */
    DataSourceConfig[] findDataSourceConfigs();

    /**
     * Return the exception configuration for the specified type, if any;
     * otherwise return <code>null</code>.
     *
     * @param type Exception class name to find a configuration for
     */
    ExceptionConfig findExceptionConfig(String type);

    /**
     * Return the exception configurations for this module.  If there
     * are none, a zero-length array is returned.
     */
    ExceptionConfig[] findExceptionConfigs();

    /**
     * Return the form bean configuration for the specified key, if any;
     * otherwise return <code>null</code>.
     *
     * @param name Name of the form bean configuration to return
     */
    FormBeanConfig findFormBeanConfig(String name);

    /**
     * Return the form bean configurations for this module.  If there
     * are none, a zero-length array is returned.
     */
    FormBeanConfig[] findFormBeanConfigs();

    /**
     * Return the forward configuration for the specified key, if any;
     * otherwise return <code>null</code>.
     *
     * @param name Name of the forward configuration to return
     */
    ForwardConfig findForwardConfig(String name);

    /**
     * Return the form bean configurations for this module.  If there
     * are none, a zero-length array is returned.
     */
    ForwardConfig[] findForwardConfigs();

    /**
     * Return the message resources configuration for the specified key,
     * if any; otherwise return <code>null</code>.
     *
     * @param key Key of the data source configuration to return
     */
    MessageResourcesConfig findMessageResourcesConfig(String key);

    /**
     * Return the message resources configurations for this module.
     * If there are none, a zero-length array is returned.
     */
    MessageResourcesConfig[] findMessageResourcesConfigs();

    /**
     * Return the configured plug-in actions for this module.  If there
     * are none, a zero-length array is returned.
     */
    PlugInConfig[] findPlugInConfigs();

    /**
     * Freeze the configuration of this module.  After this method
     * returns, any attempt to modify the configuration will return
     * an IllegalStateException.
     */
    void freeze();

    /**
     * Remove the specified action configuration instance.
     *
     * @param config ActionConfig instance to be removed
     *
     * @exception java.lang.IllegalStateException if this module configuration
     *  has been frozen
     */
    void removeActionConfig(ActionConfig config);

    /**
     * Remove the specified exception configuration instance.
     *
     * @param config ActionConfig instance to be removed
     *
     * @exception java.lang.IllegalStateException if this module configuration
     *  has been frozen
     */
    void removeExceptionConfig(ExceptionConfig config);

    /**
     * Remove the specified data source configuration instance.
     *
     * @param config DataSourceConfig instance to be removed
     *
     * @exception java.lang.IllegalStateException if this module configuration
     *  has been frozen
     */
    void removeDataSourceConfig(DataSourceConfig config);

    /**
     * Remove the specified form bean configuration instance.
     *
     * @param config FormBeanConfig instance to be removed
     *
     * @exception java.lang.IllegalStateException if this module configuration
     *  has been frozen
     */
    void removeFormBeanConfig(FormBeanConfig config);

    /**
     * Remove the specified forward configuration instance.
     *
     * @param config ForwardConfig instance to be removed
     *
     * @exception java.lang.IllegalStateException if this module configuration
     *  has been frozen
     */
    void removeForwardConfig(ForwardConfig config);

    /**
     * Remove the specified message resources configuration instance.
     *
     * @param config MessageResourcesConfig instance to be removed
     *
     * @exception java.lang.IllegalStateException if this module configuration
     *  has been frozen
     */
    void removeMessageResourcesConfig(MessageResourcesConfig config);
}

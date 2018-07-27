/*
 *
 *
 * Copyright 2000-2004 The Apache Software Foundation.
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


import org.apache.commons.digester.AbstractObjectCreationFactory;
import org.apache.commons.digester.Digester;
import org.apache.commons.digester.Rule;
import org.apache.commons.digester.RuleSetBase;
import org.apache.struts.util.RequestUtils;
import org.xml.sax.Attributes;


/**
 * <p>The set of Digester rules required to parse a Struts
 * configuration file (<code>struts-config.xml</code>).</p>
 *
 * @version $Rev: 54929 $ $Date$
 * @since Struts 1.1
 */

public class ConfigRuleSet extends RuleSetBase {


    // --------------------------------------------------------- Public Methods


    /**
     * <p>Add the set of Rule instances defined in this RuleSet to the
     * specified <code>Digester</code> instance, associating them with
     * our namespace URI (if any).  This method should only be called
     * by a Digester instance.  These rules assume that an instance of
     * <code>org.apache.struts.config.ModuleConfig</code> is pushed
     * onto the evaluation stack before parsing begins.</p>
     *
     * @param digester Digester instance to which the new Rule instances
     *  should be added.
     */
    public void addRuleInstances(Digester digester) {

        digester.addObjectCreate
            ("struts-config/data-sources/data-source",
             "org.apache.struts.config.DataSourceConfig",
             "className");
        digester.addSetProperties
            ("struts-config/data-sources/data-source");
        digester.addSetNext
            ("struts-config/data-sources/data-source",
             "addDataSourceConfig",
             "org.apache.struts.config.DataSourceConfig");

        digester.addRule
            ("struts-config/data-sources/data-source/set-property",
             new AddDataSourcePropertyRule());

        digester.addRule
            ("struts-config/action-mappings",
             new SetActionMappingClassRule());

        digester.addFactoryCreate
            ("struts-config/action-mappings/action",
             new ActionMappingFactory());
        digester.addSetProperties
            ("struts-config/action-mappings/action");
        digester.addSetNext
            ("struts-config/action-mappings/action",
             "addActionConfig",
             "org.apache.struts.config.ActionConfig");

        digester.addSetProperty
            ("struts-config/action-mappings/action/set-property",
             "property", "value");

        digester.addObjectCreate
            ("struts-config/action-mappings/action/exception",
             "org.apache.struts.config.ExceptionConfig",
             "className");
        digester.addSetProperties
            ("struts-config/action-mappings/action/exception");
        digester.addSetNext
            ("struts-config/action-mappings/action/exception",
             "addExceptionConfig",
             "org.apache.struts.config.ExceptionConfig");

        digester.addSetProperty
            ("struts-config/action-mappings/action/exception/set-property",
             "property", "value");

        digester.addFactoryCreate
            ("struts-config/action-mappings/action/forward",
             new ActionForwardFactory());
        digester.addSetProperties
            ("struts-config/action-mappings/action/forward");
        digester.addSetNext
            ("struts-config/action-mappings/action/forward",
             "addForwardConfig",
             "org.apache.struts.config.ForwardConfig");

        digester.addSetProperty
            ("struts-config/action-mappings/action/forward/set-property",
             "property", "value");

        digester.addObjectCreate
            ("struts-config/controller",
             "org.apache.struts.config.ControllerConfig",
             "className");
        digester.addSetProperties
            ("struts-config/controller");
        digester.addSetNext
            ("struts-config/controller",
             "setControllerConfig",
             "org.apache.struts.config.ControllerConfig");

        digester.addSetProperty
            ("struts-config/controller/set-property",
             "property", "value");

        digester.addRule
            ("struts-config/form-beans",
             new SetActionFormBeanClassRule());

        digester.addFactoryCreate
            ("struts-config/form-beans/form-bean",
             new ActionFormBeanFactory());
        digester.addSetProperties
            ("struts-config/form-beans/form-bean");
        digester.addSetNext
            ("struts-config/form-beans/form-bean",
             "addFormBeanConfig",
             "org.apache.struts.config.FormBeanConfig");

        digester.addObjectCreate
            ("struts-config/form-beans/form-bean/form-property",
             "org.apache.struts.config.FormPropertyConfig",
             "className");
        digester.addSetProperties
            ("struts-config/form-beans/form-bean/form-property");
        digester.addSetNext
            ("struts-config/form-beans/form-bean/form-property",
             "addFormPropertyConfig",
             "org.apache.struts.config.FormPropertyConfig");

        digester.addSetProperty
            ("struts-config/form-beans/form-bean/form-property/set-property",
             "property", "value");

        digester.addSetProperty
            ("struts-config/form-beans/form-bean/set-property",
             "property", "value");

        digester.addObjectCreate
            ("struts-config/global-exceptions/exception",
             "org.apache.struts.config.ExceptionConfig",
             "className");
        digester.addSetProperties
            ("struts-config/global-exceptions/exception");
        digester.addSetNext
            ("struts-config/global-exceptions/exception",
             "addExceptionConfig",
             "org.apache.struts.config.ExceptionConfig");

        digester.addSetProperty
            ("struts-config/global-exceptions/exception/set-property",
             "property", "value");

        digester.addRule
            ("struts-config/global-forwards",
             new SetActionForwardClassRule());

        digester.addFactoryCreate
            ("struts-config/global-forwards/forward",
             new GlobalForwardFactory());
        digester.addSetProperties
            ("struts-config/global-forwards/forward");
        digester.addSetNext
            ("struts-config/global-forwards/forward",
             "addForwardConfig",
             "org.apache.struts.config.ForwardConfig");

        digester.addSetProperty
            ("struts-config/global-forwards/forward/set-property",
             "property", "value");

        digester.addObjectCreate
            ("struts-config/message-resources",
             "org.apache.struts.config.MessageResourcesConfig",
             "className");
        digester.addSetProperties
            ("struts-config/message-resources");
        digester.addSetNext
            ("struts-config/message-resources",
             "addMessageResourcesConfig",
             "org.apache.struts.config.MessageResourcesConfig");

        digester.addSetProperty
            ("struts-config/message-resources/set-property",
             "property", "value");

        digester.addObjectCreate
            ("struts-config/plug-in",
             "org.apache.struts.config.PlugInConfig");
        digester.addSetProperties
            ("struts-config/plug-in");
        digester.addSetNext
            ("struts-config/plug-in",
             "addPlugInConfig",
             "org.apache.struts.config.PlugInConfig");

        digester.addRule
            ("struts-config/plug-in/set-property",
             new PlugInSetPropertyRule());

    }

}


/**
 * Class that calls <code>addProperty()</code> for the top object
 * on the stack, which must be a
 * <code>org.apache.struts.config.DataSourceConfig</code>.
 */

final class AddDataSourcePropertyRule extends Rule {

    public AddDataSourcePropertyRule() {
        super();
    }

    public void begin(String namespace, String name, Attributes attributes) throws Exception {
        DataSourceConfig dsc = (DataSourceConfig) digester.peek();
        dsc.addProperty(attributes.getValue("property"),
                        attributes.getValue("value"));
    }

}


/**
 * Class that records the name and value of a configuration property to be
 * used in configuring a <code>PlugIn</code> instance when instantiated.
 */

final class PlugInSetPropertyRule extends Rule {

    public PlugInSetPropertyRule() {
        super();
    }

    public void begin(String namespace, String names, Attributes attributes) throws Exception {
        PlugInConfig plugInConfig = (PlugInConfig) digester.peek();
        plugInConfig.addProperty(attributes.getValue("property"),
                                 attributes.getValue("value"));
    }

}


/**
 * Class that sets the name of the class to use when creating action form bean
 * instances. The value is set on the object on the top of the stack, which
 * must be a <code>org.apache.struts.config.ModuleConfig</code>.
 */
final class SetActionFormBeanClassRule extends Rule {

    public SetActionFormBeanClassRule() {
        super();
    }

    public void begin(String namespace, String name, Attributes attributes) throws Exception {
        String className = attributes.getValue("type");
        if (className != null) {
            ModuleConfig mc = (ModuleConfig) digester.peek();
            mc.setActionFormBeanClass(className);
        }
    }

}


/**
 * An object creation factory which creates action form bean instances, taking
 * into account the default class name, which may have been specified on the
 * parent element and which is made available through the object on the top
 * of the stack, which must be a
 * <code>org.apache.struts.config.ModuleConfig</code>.
 */
final class ActionFormBeanFactory extends AbstractObjectCreationFactory {

    public Object createObject(Attributes attributes) {

        // Identify the name of the class to instantiate
        String className = attributes.getValue("className");
        if (className == null) {
            ModuleConfig mc = (ModuleConfig) digester.peek();
            className = mc.getActionFormBeanClass();
        }

        // Instantiate the new object and return it
        Object actionFormBean = null;
        try {
            actionFormBean =
                RequestUtils.applicationInstance(className);
        } catch (Exception e) {
            digester.getLogger().error(
                    "ActionFormBeanFactory.createObject: ", e);
        }

        return actionFormBean;
    }

}


/**
 * Class that sets the name of the class to use when creating action mapping
 * instances. The value is set on the object on the top of the stack, which
 * must be a <code>org.apache.struts.config.ModuleConfig</code>.
 */
final class SetActionMappingClassRule extends Rule {

    public SetActionMappingClassRule() {
        super();
    }

    public void begin(String namespace, String name, Attributes attributes) throws Exception {
        String className = attributes.getValue("type");
        if (className != null) {
            ModuleConfig mc = (ModuleConfig) digester.peek();
            mc.setActionMappingClass(className);
        }
    }

}


/**
 * An object creation factory which creates action mapping instances, taking
 * into account the default class name, which may have been specified on the
 * parent element and which is made available through the object on the top
 * of the stack, which must be a
 * <code>org.apache.struts.config.ModuleConfig</code>.
 */
final class ActionMappingFactory extends AbstractObjectCreationFactory {

    public Object createObject(Attributes attributes) {

        // Identify the name of the class to instantiate
        String className = attributes.getValue("className");
        if (className == null) {
            ModuleConfig mc = (ModuleConfig) digester.peek();
            className = mc.getActionMappingClass();
        }

        // Instantiate the new object and return it
        Object actionMapping = null;
        try {
            actionMapping =
                RequestUtils.applicationInstance(className);
        } catch (Exception e) {
            digester.getLogger().error(
                    "ActionMappingFactory.createObject: ", e);
        }

        return actionMapping;
    }

}


/**
 * Class that sets the name of the class to use when creating global forward
 * instances. The value is set on the object on the top of the stack, which
 * must be a <code>org.apache.struts.config.ModuleConfig</code>.
 */
final class SetActionForwardClassRule extends Rule {

    public SetActionForwardClassRule() {
        super();
    }

    public void begin(String namespace, String name, Attributes attributes) throws Exception {
        String className = attributes.getValue("type");
        if (className != null) {
            ModuleConfig mc = (ModuleConfig) digester.peek();
            mc.setActionForwardClass(className);
        }
    }

}


/**
 * An object creation factory which creates global forward instances, taking
 * into account the default class name, which may have been specified on the
 * parent element and which is made available through the object on the top
 * of the stack, which must be a
 * <code>org.apache.struts.config.ModuleConfig</code>.
 */
final class GlobalForwardFactory extends AbstractObjectCreationFactory {

    public Object createObject(Attributes attributes) {

        // Identify the name of the class to instantiate
        String className = attributes.getValue("className");
        if (className == null) {
            ModuleConfig mc = (ModuleConfig) digester.peek();
            className = mc.getActionForwardClass();
        }

        // Instantiate the new object and return it
        Object globalForward = null;
        try {
            globalForward =
                RequestUtils.applicationInstance(className);
        } catch (Exception e) {
            digester.getLogger().error(
                    "GlobalForwardFactory.createObject: ", e);
        }

        return globalForward;
    }

}


/**
 * An object creation factory which creates action forward instances, taking
 * into account the default class name, which may have been specified on the
 * parent element and which is made available through the object on the top
 * of the stack, which must be a
 * <code>org.apache.struts.config.ModuleConfig</code>.
 */
final class ActionForwardFactory extends AbstractObjectCreationFactory {

    public Object createObject(Attributes attributes) {

        // Identify the name of the class to instantiate
        String className = attributes.getValue("className");
        if (className == null) {
            ModuleConfig mc = (ModuleConfig) digester.peek(1);
            className = mc.getActionForwardClass();
        }

        // Instantiate the new object and return it
        Object actionForward = null;
        try {
            actionForward =
                RequestUtils.applicationInstance(className);
        } catch (Exception e) {
            digester.getLogger().error(
                    "ActionForwardFactory.createObject: ", e);
        }

        return actionForward;
    }

}

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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;


/**
 * <p>A JavaBean representing the configuration information of an
 * <code>&lt;action&gt;</code> element from a Struts
 * module configuration file.</p>
 *
 * @version $Rev: 377805 $ $Date$
 * @since Struts 1.1
 */
public class ActionConfig implements Serializable {


    // ----------------------------------------------------- Instance Variables


    /**
     * Indicates if configuration of this component been completed.
     */
    protected boolean configured = false;


    /**
     * The set of exception handling configurations for this
     * action, if any, keyed by the <code>type</code> property.
     */
    protected HashMap exceptions = new HashMap();


    /**
     * The set of local forward configurations for this action, if any,
     * keyed by the <code>name</code> property.
     */
    protected HashMap forwards = new HashMap();


    // ------------------------------------------------------------- Properties


    /**
     * The module configuration with which we are associated.
     */
    protected ModuleConfig moduleConfig = null;

    /**
     * The module configuration with which we are associated.
     */
    public ModuleConfig getModuleConfig() {
        return (this.moduleConfig);
    }

    /**
     * The module configuration with which we are associated.
     */
    public void setModuleConfig(ModuleConfig moduleConfig) {
        if (configured) {
            throw new IllegalStateException("Configuration is frozen");
        }

        this.moduleConfig = moduleConfig;
    }


    /**
     * The request-scope or session-scope attribute name under which our
     * form bean is accessed, if it is different from the form bean's
     * specified <code>name</code>.
     */
    protected String attribute = null;

    /**
     * Returns the request-scope or session-scope attribute name under which our
     * form bean is accessed, if it is different from the form bean's
     * specified <code>name</code>.
     * @return  attribute name under which our form bean is accessed.
     */
    public String getAttribute() {
        if (this.attribute == null) {
            return (this.name);
        } else {
            return (this.attribute);
        }
    }

    /**
     * Set the request-scope or session-scope attribute name under which our
     * form bean is accessed, if it is different from the form bean's
     * specified <code>name</code>.
     * @param  attribute  the request-scope or session-scope attribute name under which our
     * form bean is access.
     */
    public void setAttribute(String attribute) {
        if (configured) {
            throw new IllegalStateException("Configuration is frozen");
        }
        this.attribute = attribute;
    }


    /**
     * Context-relative path of the web application resource that will process
     * this request via RequestDispatcher.forward(), instead of instantiating
     * and calling the <code>Action</code> class specified by "type".
     * Exactly one of <code>forward</code>, <code>include</code>, or
     * <code>type</code> must be specified.
     */
    protected String forward = null;

    /**
     * Returns context-relative path of the web application resource that will process
     * this request.
     * @return  context-relative path of the web application resource that will process
     * this request.
     */
    public String getForward() {
        return (this.forward);
    }

    /**
     * Set the context-relative path of the web application resource that will process
     * this request.
     * Exactly one of <code>forward</code>, <code>include</code>, or
     * <code>type</code> must be specified.
     * @param forward context-relative path of the web application resource that will process
     * this request.
     */
    public void setForward(String forward) {
        if (configured) {
            throw new IllegalStateException("Configuration is frozen");
        }
        this.forward = forward;
    }


    /**
     * Context-relative path of the web application resource that will process
     * this request via RequestDispatcher.include(), instead of instantiating
     * and calling the <code>Action</code> class specified by "type".
     * Exactly one of <code>forward</code>, <code>include</code>, or
     * <code>type</code> must be specified.
     */
    protected String include = null;

    /**
     * Context-relative path of the web application resource that will process
     * this request.
     * @return  Context-relative path of the web application resource that will process
     * this request.
     */
    public String getInclude() {
        return (this.include);
    }

    /**
     * Set context-relative path of the web application resource that will process
     * this request.
     * Exactly one of <code>forward</code>, <code>include</code>, or
     * <code>type</code> must be specified.
     * @param include context-relative path of the web application resource that will process
     * this request.
     */
    public void setInclude(String include) {
        if (configured) {
            throw new IllegalStateException("Configuration is frozen");
        }
        this.include = include;
    }


    /**
     * Context-relative path of the input form to which control should be
     * returned if a validation error is encountered.  Required if "name"
     * is specified and the input bean returns validation errors.
     */
    protected String input = null;

    /**
     * Get the context-relative path of the input form to which control should be
     * returned if a validation error is encountered.
     * @return  context-relative path of the input form to which control should be
     * returned if a validation error is encountered.
     */
    public String getInput() {
        return (this.input);
    }

    /**
     * Set the context-relative path of the input form to which control should be
     * returned if a validation error is encountered.  Required if "name"
     * is specified and the input bean returns validation errors.
     * @param input context-relative path of the input form to which control should be
     * returned if a validation error is encountered.
     */
    public void setInput(String input) {
        if (configured) {
            throw new IllegalStateException("Configuration is frozen");
        }
        this.input = input;
    }


    /**
     * Fully qualified Java class name of the
     * <code>MultipartRequestHandler</code> implementation class used to
     * process multi-part request data for this Action.
     */
    protected String multipartClass = null;

    /**
     * Return the fully qualified Java class name of the
     * <code>MultipartRequestHandler</code> implementation class used to
     * process multi-part request data for this Action.
     */
    public String getMultipartClass() {
        return (this.multipartClass);
    }

    /**
     * Set the fully qualified Java class name of the
     * <code>MultipartRequestHandler</code> implementation class used to
     * process multi-part request data for this Action.
     * @param multipartClass fully qualified class name of the
     * <code>MultipartRequestHandler</code> implementation class.
     */
    public void setMultipartClass(String multipartClass) {
        if (configured) {
            throw new IllegalStateException("Configuration is frozen");
        }
        this.multipartClass = multipartClass;
    }


    /**
     * Name of the form bean, if any, associated with this Action.
     */
    protected String name = null;

    /**
     * Return name of the form bean, if any, associated with this Action.
     */
    public String getName() {
        return (this.name);
    }

    /**
     * @param name name of the form bean associated with this Action.
     */
    public void setName(String name) {
        if (configured) {
            throw new IllegalStateException("Configuration is frozen");
        }
        this.name = name;
    }


    /**
     * General purpose configuration parameter that can be used to pass
     * extra information to the Action instance selected by this Action.
     * Struts does not itself use this value in any way.
     */
    protected String parameter = null;

    /**
      * Return general purpose configuration parameter that can be used to pass
      * extra information to the Action instance selected by this Action.
      * Struts does not itself use this value in any way.
      */
    public String getParameter() {
        return (this.parameter);
    }

    /**
      * General purpose configuration parameter that can be used to pass
      * extra information to the Action instance selected by this Action.
      * Struts does not itself use this value in any way.
     * @param   parameter General purpose configuration parameter.
      */
    public void setParameter(String parameter) {
        if (configured) {
            throw new IllegalStateException("Configuration is frozen");
        }
        this.parameter = parameter;
    }


    /**
     * Context-relative path of the submitted request, starting with a
     * slash ("/") character, and omitting any filename extension if
     * extension mapping is being used.
     */
    protected String path = null;

    /**
     * Return context-relative path of the submitted request, starting with a
     * slash ("/") character, and omitting any filename extension if
     * extension mapping is being used.
     */
    public String getPath() {
        return (this.path);
    }

    /**
     * Set context-relative path of the submitted request, starting with a
     * slash ("/") character, and omitting any filename extension if
     * extension mapping is being used.
     * @param  path context-relative path of the submitted request.
     */
    public void setPath(String path) {
        if (configured) {
            throw new IllegalStateException("Configuration is frozen");
        }
        this.path = path;
    }


    /**
     * Prefix used to match request parameter names to form bean property
     * names, if any.
     */
    protected String prefix = null;

    /**
     * Retruns prefix used to match request parameter names to form bean property
     * names, if any.
     */
    public String getPrefix() {
        return (this.prefix);
    }

    /**
     * @param prefix Prefix used to match request parameter names to
     * form bean property names, if any.
     */
    public void setPrefix(String prefix) {
        if (configured) {
            throw new IllegalStateException("Configuration is frozen");
        }
        this.prefix = prefix;
    }


    /**
     * Comma-delimited list of security role names allowed to request
     * this Action.
     */
    protected String roles = null;

    public String getRoles() {
        return (this.roles);
    }

    public void setRoles(String roles) {
        if (configured) {
            throw new IllegalStateException("Configuration is frozen");
        }
        this.roles = roles;
        if (roles == null) {
            roleNames = new String[0];
            return;
        }
        ArrayList list = new ArrayList();
        while (true) {
            int comma = roles.indexOf(',');
            if (comma < 0)
                break;
            list.add(roles.substring(0, comma).trim());
            roles = roles.substring(comma + 1);
        }
        roles = roles.trim();
        if (roles.length() > 0)
            list.add(roles);
        roleNames = (String[]) list.toArray(new String[list.size()]);
    }


    /**
     * The set of security role names used to authorize access to this
     * Action, as an array for faster access.
     */
    protected String[] roleNames = new String[0];

    /**
     * Get array of security role names used to authorize access to this
     * Action.
     */
    public String[] getRoleNames() {
        return (this.roleNames);
    }


    /**
     * Identifier of the scope ("request" or "session") within which
     * our form bean is accessed, if any.
     */
    protected String scope = "session";

    /**
     * Get the scope ("request" or "session") within which
     * our form bean is accessed, if any.
     */
    public String getScope() {
        return (this.scope);
    }

    /**
     * @param scope scope ("request" or "session") within which
     * our form bean is accessed, if any.
     */
    public void setScope(String scope) {
        if (configured) {
            throw new IllegalStateException("Configuration is frozen");
        }
        this.scope = scope;
    }


    /**
     * Suffix used to match request parameter names to form bean property
     * names, if any.
     */
    protected String suffix = null;

    /**
     * Return suffix used to match request parameter names to form bean property
     * names, if any.
     */
    public String getSuffix() {
        return (this.suffix);
    }

    /**
     * @param suffix Suffix used to match request parameter names to form bean property
     * names, if any.
     */
    public void setSuffix(String suffix) {
        if (configured) {
            throw new IllegalStateException("Configuration is frozen");
        }
        this.suffix = suffix;
    }


    /**
     * Fully qualified Java class name of the <code>Action</code> class
     * to be used to process requests for this mapping if the
     * <code>forward</code> and <code>include</code> properties are not set.
     * Exactly one of <code>forward</code>, <code>include</code>, or
     * <code>type</code> must be specified.
     */
    protected String type = null;

    public String getType() {
        return (this.type);
    }

    public void setType(String type) {
        if (configured) {
            throw new IllegalStateException("Configuration is frozen");
        }
        this.type = type;
    }


    /**
     * Indicates Action be configured as the default one for this
     * module, when true.
     */
    protected boolean unknown = false;

    /**
      * Determine whether Action is configured as the default one for this
      * module.
      */
    public boolean getUnknown() {
        return (this.unknown);
    }

    /**
      * @param unknown Indicates Action is configured as the default one for this
      * module, when true.
      */
    public void setUnknown(boolean unknown) {
        if (configured) {
            throw new IllegalStateException("Configuration is frozen");
        }
        this.unknown = unknown;
    }

    /**
     * Should the <code>validate()</code> method of the form bean associated
     * with this action be called?
     */
    protected boolean validate = true;

    public boolean getValidate() {
        return (this.validate);
    }

    public void setValidate(boolean validate) {
        if (configured) {
            throw new IllegalStateException("Configuration is frozen");
        }
        this.validate = validate;
    }
    
    /**
     * <p>Can this Action be cancelled? [false]</p> <p> By default, when an
     * Action is cancelled, validation is bypassed and the Action should not
     * execute the business operation. If a request tries to cancel an Action
     * when cancellable is not set, a "InvalidCancelException" is thrown.</p>
     * @since Struts 1.2.9
     */ 
    protected boolean cancellable = false;

    /**
     * <p>Accessor for cancellable property</p>
     *
     * @return True if Action can be cancelled
     * @since  Struts 1.2.9
     */
    public boolean getCancellable() {
        return (this.cancellable);
    }

    /**
     * <p>Mutator for for cancellable property</p>
     *
     * @param cancellable
     * @since Struts 1.2.9
     */
    public void setCancellable(boolean cancellable) {
        if (configured) {
            throw new IllegalStateException("Configuration is frozen");
        }
        this.cancellable = cancellable;
    }
    

    // --------------------------------------------------------- Public Methods


    /**
     * Add a new <code>ExceptionConfig</code> instance to the set associated
     * with this action.
     *
     * @param config The new configuration instance to be added
     *
     * @exception IllegalStateException if this module configuration
     *  has been frozen
     */
    public void addExceptionConfig(ExceptionConfig config) {

        if (configured) {
            throw new IllegalStateException("Configuration is frozen");
        }
        exceptions.put(config.getType(), config);

    }


    /**
     * Add a new <code>ForwardConfig</code> instance to the set of global
     * forwards associated with this action.
     *
     * @param config The new configuration instance to be added
     *
     * @exception IllegalStateException if this module configuration
     *  has been frozen
     */
    public void addForwardConfig(ForwardConfig config) {

        if (configured) {
            throw new IllegalStateException("Configuration is frozen");
        }
        forwards.put(config.getName(), config);

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
     * Return the exception configurations for this action.  If there
     * are none, a zero-length array is returned.
     */
    public ExceptionConfig[] findExceptionConfigs() {

        ExceptionConfig results[] = new ExceptionConfig[exceptions.size()];
        return ((ExceptionConfig[]) exceptions.values().toArray(results));

    }

    /**
     * <p>Find and return the <code>ExceptionConfig</code> instance defining
     * how <code>Exceptions</code> of the specified type should be handled.
     * This is performed by checking local and then global configurations for
     * the specified exception's class, and then looking up the superclass chain
     * (again checking local and then global configurations). If no handler
     * configuration can be found, return <code>null</code>.</p>
     *
     * <p>Introduced in <code>ActionMapping</code> in Struts 1.1, but pushed
     * up to <code>ActionConfig</code> in Struts 1.2.0.</p>
     *
     * @param type Exception class for which to find a handler
     * @since Struts 1.2.0
     */
    public ExceptionConfig findException(Class type) {

        // Check through the entire superclass hierarchy as needed
        ExceptionConfig config = null;
        while (true) {

            // Check for a locally defined handler
            String name = type.getName();
            config = findExceptionConfig(name);
            if (config != null) {
                return (config);
            }

            // Check for a globally defined handler
            config = getModuleConfig().findExceptionConfig(name);
            if (config != null) {
                return (config);
            }

            // Loop again for our superclass (if any)
            type = type.getSuperclass();
            if (type == null) {
                break;
            }

        }
        return (null); // No handler has been configured

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
     * Return all forward configurations for this module.  If there
     * are none, a zero-length array is returned.
     */
    public ForwardConfig[] findForwardConfigs() {

        ForwardConfig results[] = new ForwardConfig[forwards.size()];
        return ((ForwardConfig[]) forwards.values().toArray(results));

    }


    /**
     * Freeze the configuration of this action.
     */
    public void freeze() {

        configured = true;

        ExceptionConfig[] econfigs = findExceptionConfigs();
        for (int i = 0; i < econfigs.length; i++) {
            econfigs[i].freeze();
        }

        ForwardConfig[] fconfigs = findForwardConfigs();
        for (int i = 0; i < fconfigs.length; i++) {
            fconfigs[i].freeze();
        }

    }


    /**
     * Remove the specified exception configuration instance.
     *
     * @param config ExceptionConfig instance to be removed
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
     * Return a String representation of this object.
     */
    public String toString() {

        StringBuffer sb = new StringBuffer("ActionConfig[");
        sb.append("path=");
        sb.append(path);
        if (attribute != null) {
            sb.append(",attribute=");
            sb.append(attribute);
        }
        if (forward != null) {
            sb.append(",forward=");
            sb.append(forward);
        }
        if (include != null) {
            sb.append(",include=");
            sb.append(include);
        }
        if (input != null) {
            sb.append(",input=");
            sb.append(input);
        }
        if (multipartClass != null) {
            sb.append(",multipartClass=");
            sb.append(multipartClass);
        }
        if (name != null) {
            sb.append(",name=");
            sb.append(name);
        }
        if (parameter != null) {
            sb.append(",parameter=");
            sb.append(parameter);
        }
        if (prefix != null) {
            sb.append(",prefix=");
            sb.append(prefix);
        }
        if (roles != null) {
            sb.append(",roles=");
            sb.append(roles);
        }
        if (scope != null) {
            sb.append(",scope=");
            sb.append(scope);
        }
        if (suffix != null) {
            sb.append(",suffix=");
            sb.append(suffix);
        }
        if (type != null) {
            sb.append(",type=");
            sb.append(type);
        }
        sb.append(",validate=");
        sb.append(validate);
        sb.append(",cancellable=");
        sb.append(cancellable);
        return (sb.toString());

    }


}

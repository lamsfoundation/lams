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


/**
 * <p>A JavaBean representing the configuration information of an
 * <code>&lt;exception&gt;</code> element from a Struts
 * configuration file.</p>
 *
 * @version $Rev: 54929 $ $Date$
 * @since Struts 1.1
 */

public class ExceptionConfig implements Serializable {


    // ----------------------------------------------------- Instance Variables


    /**
     * Has this component been completely configured?
     */
    protected boolean configured = false;


    // ------------------------------------------------------------- Properties


    /**
     * The servlet context attribute under which the message resources bundle
     * to be used for this exception is located.  If not set, the default
     * message resources for the current module is assumed.
     */
    protected String bundle = null;

    public String getBundle() {
        return (this.bundle);
    }

    public void setBundle(String bundle) {
        if (configured) {
            throw new IllegalStateException("Configuration is frozen");
        }
        this.bundle = bundle;
    }


    /**
     * The fully qualified Java class name of the exception handler class
     * which should be instantiated to handle this exception.
     */
    protected String handler = "org.apache.struts.action.ExceptionHandler";

    public String getHandler() {
        return (this.handler);
    }

    public void setHandler(String handler) {
        if (configured) {
            throw new IllegalStateException("Configuration is frozen");
        }
        this.handler = handler;
    }


    /**
     * The message resources key specifying the error message
     * associated with this exception.
     */
    protected String key = null;

    public String getKey() {
        return (this.key);
    }

    public void setKey(String key) {
        if (configured) {
            throw new IllegalStateException("Configuration is frozen");
        }
        this.key = key;
    }


    /**
     * The module-relative path of the resource to forward to if this
     * exception occurs during an <code>Action</code>.
     */
    protected String path = null;

    public String getPath() {
        return (this.path);
    }

    public void setPath(String path) {
        if (configured) {
            throw new IllegalStateException("Configuration is frozen");
        }
        this.path = path;
    }


    /**
     * The scope in which we should expose the ActionError for this exception
     * handler.
     */
    protected String scope = "request";

    public String getScope() {
        return (this.scope);
    }

    public void setScope(String scope) {
        if (configured) {
            throw new IllegalStateException("Configuration is frozen");
        }
        this.scope = scope;
    }


    /**
     * The fully qualified Java class name of the exception that is to be
     * handled by this handler.
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


    // --------------------------------------------------------- Public Methods


    /**
     * Freeze the configuration of this component.
     */
    public void freeze() {

        configured = true;

    }


    /**
     * Return a String representation of this object.
     */
    public String toString() {

        StringBuffer sb = new StringBuffer("ExceptionConfig[");
        sb.append("type=");
        sb.append(this.type);
        if (this.bundle != null) {
            sb.append(",bundle=");
            sb.append(this.bundle);
        }
        sb.append(",key=");
        sb.append(this.key);
        sb.append(",path=");
        sb.append(this.path);
        sb.append(",scope=");
        sb.append(this.scope);
        sb.append("]");
        return (sb.toString());

    }


}

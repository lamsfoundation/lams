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
import java.util.HashMap;
import java.util.Map;


/**
 * <p>A JavaBean representing the configuration information of a
 * <code>&lt;plug-in&gt;</code> element in a Struts
 * configuration file.</p>
 *
 * @version $Rev: 54929 $ $Date$
 * @since Struts 1.1
 */

public class PlugInConfig implements Serializable {


    // ----------------------------------------------------- Instance Variables


    /**
     * Has this component been completely configured?
     */
    protected boolean configured = false;


    /**
     * A <code>Map</code> of the name-value pairs that will be used to
     * configure the property values of a <code>PlugIn</code> instance.
     */
    protected Map properties = new HashMap();


    // ------------------------------------------------------------- Properties


    /**
     * The fully qualified Java class name of the <code>PlugIn</code>
     * implementation class being configured.
     */
    protected String className = null;

    public String getClassName() {
        return (this.className);
    }

    public void setClassName(String className) {
        this.className = className;
    }


    // --------------------------------------------------------- Public Methods


    /**
     * Add a new property name and value to the set that will be used to
     * configure the <code>PlugIn</code> instance.
     *
     * @param name Property name
     * @param value Property value
     */
    public void addProperty(String name, String value) {

        if (configured) {
            throw new IllegalStateException("Configuration is frozen");
        }
        properties.put(name, value);

    }


    /**
     * Freeze the configuration of this component.
     */
    public void freeze() {

        configured = true;

    }


    /**
     * Return the properties that will be used to configure a
     * <code>PlugIn</code> instance.
     */
    public Map getProperties() {

        return (properties);

    }


}

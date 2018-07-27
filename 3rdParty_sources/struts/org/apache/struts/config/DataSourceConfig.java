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
import java.util.Iterator;
import java.util.Map;
import org.apache.struts.Globals;


/**
 * <p>A JavaBean representing the configuration information of a
 * <code>&lt;data-source&gt;</code> element from a Struts
 * configuration file.</p>
 *
 * <p><strong>WARNING</strong> - The properties of this configuration bean
 * are recognized by the default data source implementation, but some or all
 * of them may be ignored by custom data source implementations.</p>
 *
 * @version $Rev: 54929 $ $Date$
 * @since Struts 1.1
 */

public class DataSourceConfig implements Serializable {



    // ----------------------------------------------------- Instance Variables


    /**
     * Has this component been completely configured?
     */
    protected boolean configured = false;


    // ------------------------------------------------------------- Properties


    /**
     * The servlet context attribute key under which this data source
     * is stored and made available.
     */
    protected String key = Globals.DATA_SOURCE_KEY;

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
     * The custom configuration properties for this data source implementation.
     */
    protected HashMap properties = new HashMap();

    public Map getProperties() {
        return (this.properties);
    }


    /**
     * The fully qualified class name of the <code>javax.sql.DataSource</code>
     * implementation class.
     */
    protected String type;

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
     * Add a new custom configuration property.
     *
     * @param name Custom property name
     * @param value Custom property value
     */
    public void addProperty(String name, String value) {

        if (configured) {
            throw new IllegalStateException("Configuration is frozen");
        }
        properties.put(name, value);

    }


    /**
     * Freeze the configuration of this data source.
     */
    public void freeze() {

        configured = true;

    }


    /**
     * Return a String representation of this object.
     */
    public String toString() {

        StringBuffer sb = new StringBuffer("DataSourceConfig[");
        sb.append("key=");
        sb.append(key);
        sb.append(",type=");
        sb.append(type);
        Iterator names = properties.keySet().iterator();
        while (names.hasNext()) {
            String name = (String) names.next();
            String value = (String) properties.get(name);
            sb.append(',');
            sb.append(name);
            sb.append('=');
            sb.append(value);
        }
        sb.append("]");
        return (sb.toString());

    }


}

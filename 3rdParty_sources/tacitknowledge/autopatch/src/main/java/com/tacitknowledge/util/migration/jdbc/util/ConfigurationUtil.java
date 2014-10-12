/* Copyright 2004 Tacit Knowledge
 *  
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.tacitknowledge.util.migration.jdbc.util;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import java.util.Iterator;
import java.util.Properties;

/**
 * A utility class for various configuration needs
 *
 * @author Mike Hardy (mike@tacitknowledge.com)
 */
public class ConfigurationUtil
{
    /**
     * Class logger
     */
    private static Log log = LogFactory.getLog(ConfigurationUtil.class);

    /**
     * Shouldn't be used
     */
    private ConfigurationUtil()
    {
        // do nothing
    }

    /**
     * Returns the value of the specified servlet context initialization parameter.
     *
     * @param param      the parameter to return
     * @param properties the <code>Properties</code> for the Java system
     * @param arguments  optionally takes the arguments passed into the main to
     *                   use as the migration system name
     * @return the value of the specified system initialization parameter
     * @throws IllegalArgumentException if the parameter does not exist
     */
    public static String getRequiredParam(String param, Properties properties,
            String[] arguments) throws IllegalArgumentException
    {
        return getRequiredParam(param, properties, arguments, 0);
    }

    /**
     * Returns the value of the specified servlet context initialization parameter identifed by the
     * supplied index in the supplied array.
     *
     * @param param      the parameter to return
     * @param properties the <code>Properties</code> for the Java system
     * @param arguments  optionally takes the arguments passed into the main to use as the
     *                   migration system name
     * @param index      the index to use in the supplied array
     * @return the value of the specified system initialization parameter
     * @throws IllegalArgumentException if the parameter does not exist
     */
    public static String getRequiredParam(String param, Properties properties,
            String[] arguments, int index) throws IllegalArgumentException
    {
        return getPropertyValue(param, properties, arguments, index, true);
    }

    /**
     * Returns the value of the specified servlet context initialization parameter identifed by the
     * supplied index in the supplied array. Since it is an optional parameter then
     * <code>null</code> is returned instead of an exception if an error occurs.
     *
     * @param param      the parameter to return
     * @param properties the <code>Properties</code> for the Java system
     * @param arguments  optionally takes the arguments passed into the main to use as the
     *                   migration system name
     * @param index      the index to use in the supplied array
     * @return the value of the specified system initialization parameter
     */
    public static String getOptionalParam(String param, Properties properties, String[] arguments,
            int index)
    {
        return getPropertyValue(param, properties, arguments, index, false);
    }

    /**
     * Returns the value of the specified configuration parameter.
     *
     * @param props the properties file containing the values
     * @param param the parameter to return
     * @return the value of the specified configuration parameter
     * @throws IllegalArgumentException if the parameter does not exist
     */
    public static String getRequiredParam(Properties props, String param)
            throws IllegalArgumentException
    {
        String value = props.getProperty(param);
        if (value == null)
        {
            log.warn("Parameter named: " + param + " was not found.");
            log.warn("-----Parameters found-----");
            Iterator propNameIterator = props.keySet().iterator();
            while (propNameIterator.hasNext())
            {
                String name = (String) propNameIterator.next();
                String val = props.getProperty(name);
                log.warn(name + " = " + val);
            }
            log.warn("--------------------------");
            throw new IllegalArgumentException("'" + param + "' is a required "
                    + "initialization parameter.  Aborting.");
        }
        return value;
    }

    /**
     * Returns the value of the specified configuration parameter.
     *
     * @param props     the properties file containing the values
     * @param param     the parameter to return
     * @param alternate the alternate parameter to return
     * @return the value of the specified configuration parameter
     */
    public static String getRequiredParam(Properties props, String param, String alternate)
    {
        try
        {
            return getRequiredParam(props, param);
        }
        catch (IllegalArgumentException e1)
        {
            try
            {
                return getRequiredParam(props, alternate);
            }
            catch (IllegalArgumentException e2)
            {
                throw new IllegalArgumentException("Either '" + param + "' or '" + alternate
                        + "' must be specified as an initialization parameter.  Aborting.");
            }
        }
    }


    /**
     * Returns the value of the specified servlet context initialization parameter.
     * Since it is an optional parameter then <code>null</code> is returned
     * instead of an exception if an error occurs.
     *
     * @param param  the parameter to return
     * @param sce    the <code>ServletContextEvent</code> being handled
     * @param caller calling object, used for printing information if there is a problem
     * @return the value of the specified servlet context initialization parameter
     * @throws IllegalArgumentException if the parameter does not exist
     */
    public static String getOptionalParam(String param, ServletContextEvent sce, Object caller)
            throws IllegalArgumentException
    {
        return getServletContextParam(param, sce, caller, false);
    }

    /**
     * Returns the value of the specified servlet context initialization parameter.
     *
     * @param param  the parameter to return
     * @param sce    the <code>ServletContextEvent</code> being handled
     * @param caller calling object, used for printing information if there is a problem
     * @return the value of the specified servlet context initialization parameter
     * @throws IllegalArgumentException if the parameter does not exist
     */
    public static String getRequiredParam(String param, ServletContextEvent sce, Object caller)
            throws IllegalArgumentException
    {
        return getServletContextParam(param, sce, caller, true);
    }

    /**
     * Returns the value of the specified servlet context initialization parameter.
     *
     * @param param          the parameter to return
     * @param sce            the <code>ServletContextEvent</code> being handled
     * @param caller         calling object, used for printing information if there is a problem
     * @param throwException if <code>true</code> then the method will throw an exception; if
     *                       <code>false</code> is supplied then it will return <code>null</code>
     * @return the value of the specified servlet context initialization parameter if found;
     *         <code>null</code> otherwise
     * @throws IllegalArgumentException if the parameter does not exist
     */
    private static String getServletContextParam(String param, ServletContextEvent sce,
            Object caller, boolean throwException)
            throws IllegalArgumentException
    {
        ServletContext context = sce.getServletContext();
        String value = context.getInitParameter(param);

        if (value == null && throwException)
        {
            throw new IllegalArgumentException("'" + param + "' is a required "
                    + "servlet context initialization parameter for the \""
                    + caller.getClass().getName() + "\" class.  Aborting.");
        }

        return value;
    }

    /**
     * Gets the value of the supplied property name. First it searches in system properties. If
     * not found then it examines the supplied array of command line arguments.
     *
     * @param propertyName   the property naem to get
     * @param properties     the <code>Properties</code> for the Java system
     * @param arguments      the array of command line arguments
     * @param index          the index of the property in the command line arguments
     * @param throwException if <code>true</code> then the method will throw an exception; if
     *                       <code>false</code> is supplied then it will return <code>null</code>
     * @return the property value if found; <code>null</code> otherwise
     */
    private static String getPropertyValue(String propertyName, Properties properties,
            String[] arguments, int index, boolean throwException)
    {
        String value = properties.getProperty(propertyName);

        if (value == null)
        {
            if ((arguments != null) && (arguments.length > 0) && (index < arguments.length))
            {
                value = arguments[index].trim();
            }
            else if (throwException)
            {
                throw new IllegalArgumentException("The " + propertyName
                        + " system property is required");
            }
            else
            {
                value = null;
            }
        }

        return value;
    }
}

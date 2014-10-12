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

package com.tacitknowledge.util.discovery;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Convenience methods for using the class discovery API.
 * 
 * @author  Scott Askew (scott@tacitknowledge.com)
 */
public final class ClassDiscoveryUtil
{
    /**
     * A ResourceListSource for both directories and archives in the classpath
     */
    private static AggregateResourceListSource classpathResources = null;
    static
    {
        classpathResources =
            new AggregateResourceListSource(
                new ResourceListSource[] {
                    new DirectoryResourceListSource(),
                    new ArchiveResourceListSource(),
                });
    }
    
    /**
     * Hidden constructor for utility class
     */
    private ClassDiscoveryUtil()
    {
        // Hidden
    }
    
    /**
     * Adds the given <code>ResourceListSource</code> to the list of sources
     * used by <code>getResources</code>.
     * 
     * @param source the source to add
     */
    public static void addResourceListSource(ResourceListSource source)
    {
        classpathResources.addResourceListSource(source);
    }
    
    /**
     * Returns the names of the file resources in the given directory.
     * 
     * @param  basePath the directory containing the resources
     * @return the names of the file resources in the given directory
     */
    public static String[] getResources(String basePath)
    {
        return getResources(basePath, null);
    }

    /**
     * Returns the names of the resources in the given directory that match the
     * given regular expression.
     * 
     * @param  basePath the directory containing the resources
     * @param  regex the regular expression used to filter the resources
     * @return the names of the resources in the given directory that match the
     *         given regular expression
     */
    public static String[] getResources(String basePath, String regex)
    {
        RegexResourceCriteria criteria = new RegexResourceCriteria(regex);
        String[] resourcesNames = classpathResources.getResources(basePath, criteria);
        return resourcesNames;
    }

    /**
     * Returns an array of concrete classes in the given package that implement the
     * specified interface.
     * 
     * @param  basePackage the name of the package containing the classes to discover
     * @param  requiredInterface the inteface that the returned classes must implement
     * @return an array of concrete classes in the given package that implement the
     *         specified interface 
     */
    public static Class[] getClasses(String basePackage, Class requiredInterface)
    {
        return getClasses(basePackage, new Class[] {requiredInterface});
    }
    
    /**
     * Returns an array of concrete classes in the given package that implement
     * all of the specified interfaces.
     * 
     * @param  basePackage the name of the package containing the classes to discover
     * @param  requiredInterfaces the intefaces that the returned classes must implement
     * @return an array of concrete classes in the given package that implement the
     *         specified interface 
     */
    public static Class[] getClasses(String basePackage, Class[] requiredInterfaces)
    {
        List classes = new ArrayList();
        ClassCriteria criteria = new ClassCriteria(requiredInterfaces);
        String basePath = basePackage.replace('.', File.separatorChar);
        String[] resourcesNames = classpathResources.getResources(basePath, criteria);
        for (int i = 0; i < resourcesNames.length; i++)
        {
            String resourceName = resourcesNames[i];
            if (resourceName == null)
            {
                continue;
            }
            String className = getClassName(resourceName);
            try
            {
                Class c = Class.forName(className);
                classes.add(c);
            }
            catch (ClassNotFoundException e)
            {
                // This should not happen since we've already validated the class
            }
        }
        return (Class[]) classes.toArray(new Class[classes.size()]);
    }
    
    /**
     * Returns the fully qualified class name represented by the given resource.
     * 
     * @param  resourceName the file name of the resource to convert to a class
     *         name; may not be <code>null</code>
     * @return the fully qualified class name represented by the given resource
     */
    public static String getClassName(String resourceName)
    {
        String className = resourceName.replace(File.separatorChar, '.');
        if (className.length() > 7)
        {
            className = className.substring(0, className.length() - 6);
        }
        return className;
    }
}

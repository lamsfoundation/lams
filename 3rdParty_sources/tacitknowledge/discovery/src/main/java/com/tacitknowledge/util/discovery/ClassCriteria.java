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
import java.lang.reflect.Modifier;

/**
 * Used to search on classes that implement one or more specified interfaces.
 * 
 * @author  Scott Askew (scott@tacitknowledge.com)
 */
public class ClassCriteria implements ResourceCriteria
{
    /**
     * The interfaces that the subject class must implement
     */
    private Class[] interfaces = null;

    /**
     * Creates new <code>ClassCritera</code> object.
     * 
     * @param c the interface that the subject class must implement
     */
    public ClassCriteria(Class c)
    {
        this(new Class[] {c});
    }
    
    /**
     * Creates new <code>ClassCritera</code> object.
     * 
     * @param interfaces the set of interfaces that the subject class must
     *        implement
     */
    public ClassCriteria(Class[] interfaces)
    {
        this.interfaces = interfaces;
    }
    
    /**
     * @see ResourceCriteria#matches(String)  
     */
    public boolean matches(String resourceName)
    {
        if (resourceName.endsWith(".class"))
        {
            String className = resourceName.substring(0, resourceName.length() - 6);
            className = className.replace(File.separatorChar, '.');
            try
            {
                Class c = Class.forName(className);
                boolean isRightType = isConcrete(c);
                for (int i = 0; isRightType && i < interfaces.length; i++)
                {
                    Class interfaceClass = interfaces[i];
                    if (!interfaceClass.isAssignableFrom(c))
                    {
                        isRightType = false;
                    }
                }
                return isRightType;
            }
            catch (ClassNotFoundException e)
            {
                return false;
            }
        }
        return false;
    }
    
    /**
     * Determines if the given class is concrete.
     * 
     * @param  c the class to examine
     * @return <code>true</code> if the given class is concrete (in other words,
     *         is not an interface or abstract class)
     */
    private boolean isConcrete(Class c)
    {
        return !(c.isInterface() || Modifier.isAbstract(c.getModifiers()));
    }
}
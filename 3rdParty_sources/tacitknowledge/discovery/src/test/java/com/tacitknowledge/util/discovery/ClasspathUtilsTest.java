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
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import junit.framework.TestCase;

public class ClasspathUtilsTest extends TestCase {

    protected void setUp() throws Exception {
        super.setUp();
    }
    
    /**
     * return the number of times o appears in c
     * @param c collection
     * @param o object
     * @return number of times o appears in c
     */
    protected int frequency(Collection c, Object o)
    {
        int count = 0;
        for (Iterator iter = c.iterator(); iter.hasNext();) 
        {
            Object element = iter.next();
            if (o.equals(element))
            {
                ++count;
            }
        }
        
        return count;
    }

    public void testGetClasspathComponentsOnSystemClasspath() 
    {
        String classpath = System.getProperty("java.class.path");
        try 
        {
            String pathSep = System.getProperty("path.separator");
            String fileSep = System.getProperty("file.separator");
            
            List components = ClasspathUtils.getClasspathComponents();
            Collections.sort(components);
            String myAdditionalDir = "some" + fileSep + "package" + fileSep + "dir";
            // first make sure my package isn't in the classpath yet
            assertTrue(Collections.binarySearch(components, myAdditionalDir) < 0);
            // add my package to the classpath
            System.setProperty("java.class.path", classpath + pathSep + myAdditionalDir);
            components = ClasspathUtils.getClasspathComponents();
            Collections.sort(components);
            assertTrue(Collections.binarySearch(components, myAdditionalDir) >= 0);
        }
        finally
        {
            // set classpath back regardless of the outcome of the test
            System.setProperty("java.class.path", classpath);
        }
    }
    
    public void testGetClasspathComponentsFromClassLoader() throws MalformedURLException
    {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        try
        {
            File file = new File("/some/package/dir");
            URL[] urls = new URL[] { file.toURL() };            
            // assert my package can't be found yet
            List components = ClasspathUtils.getClasspathComponents();
            Collections.sort(components);
            assertTrue(Collections.binarySearch(components, file.getPath()) < 0);
            // test my package is found
            URLClassLoader ucl = new URLClassLoader(urls, classLoader);
            Thread.currentThread().setContextClassLoader(ucl);
            components = ClasspathUtils.getClasspathComponents();
            Collections.sort(components);
            assertTrue( Collections.binarySearch(components, file.getAbsolutePath()) >= 0);
        }
        finally
        {
            Thread.currentThread().setContextClassLoader(classLoader);
        }
    }
    
    public void testGetClasspathComponentsFromParentClassLoader() throws MalformedURLException
    {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        try
        {
            File file = new File("/some/package/dir");
            URL[] urls = new URL[] { file.toURL() };            
            // assert my package can't be found yet
            List components = ClasspathUtils.getClasspathComponents();
            Collections.sort(components);
            assertTrue(Collections.binarySearch(components, file.getPath()) < 0);
            // test my package is found
            URLClassLoader parentClassLoader = new URLClassLoader(urls, classLoader);
            URLClassLoader childClassLoader = new URLClassLoader(new URL[] {}, parentClassLoader);
            Thread.currentThread().setContextClassLoader(childClassLoader);
            components = ClasspathUtils.getClasspathComponents();
            Collections.sort(components);
            assertTrue(Collections.binarySearch(components, file.getAbsolutePath()) >= 0);
        }
        finally
        {
            Thread.currentThread().setContextClassLoader(classLoader);
        }
    }
    
    public void testGetClasspathComponentsDontHaveDuplicates() throws MalformedURLException
    {
        String classpath = System.getProperty("java.class.path");
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        try
        {
            String pathSep = System.getProperty("path.separator");
            File file = new File("/some/package/dir");
            URL[] urls = new URL[] { file.toURL() };            
            // assert my package can't be found yet
            List components = ClasspathUtils.getClasspathComponents();
            Collections.sort(components);
            assertTrue(Collections.binarySearch(components, file.getPath()) < 0);
            // test my package is found and only appears once
            URLClassLoader parentClassLoader = new URLClassLoader(urls, classLoader);
            URLClassLoader childClassLoader = new URLClassLoader(new URL[] {}, parentClassLoader);
            Thread.currentThread().setContextClassLoader(childClassLoader);
            System.setProperty("java.class.path", classpath + pathSep + file.getPath());
            components = ClasspathUtils.getClasspathComponents();
            Collections.sort(components);
            assertTrue(Collections.binarySearch(components, file.getPath()) >= 0);
            assertTrue(frequency(components, file.getPath()) == 1);
        }
        finally
        {
            Thread.currentThread().setContextClassLoader(classLoader);
            System.setProperty("java.class.path", classpath);
        }
        
    }

}

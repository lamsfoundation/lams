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

import junit.framework.TestCase;

/**
 * Tests the <code>ArchiveResourceListSourceTest</code> class.
 * 
 * @author  Scott Askew (scott@tacitknowledge.com)
 */
public class ArchiveResourceListSourceTest extends TestCase
{
    /**
     * Constructor for ArchiveResourceListSourceTest.
     * 
     * @param name the name of the test to run
     */
    public ArchiveResourceListSourceTest(String name)
    {
        super(name);
    }
    
    /**
     * Tests basic functionality
     */
    public void testGetResources()
    {
        ArchiveResourceListSource source = new ArchiveResourceListSource();
        String[] names = source.getResources("junit/framework");
        
        // As of 3.8.1, the core JUnit framework has 12 classes in it.  JUnit
        // is very stable (no new releases in two years); however, if a new version
        // were to come out, this test may fail if new classes are added in new
        // versions
        // Futhermore, if the JUnit library is in the path more than once, 12
        // framework classes will be found per jar.
        assertEquals(0, names.length % 12);
        
        // Make sure one of these is a class
        try
        {
            // Build out the package name from the directory path
            String className = names[0].replace(File.separatorChar, '.');
            // Chop off the .class
            className = className.substring(0, className.length() - 6);
            
            Class.forName(className);
        }
        catch (ClassNotFoundException e)
        {
            e.printStackTrace();
            fail("Could not load class found in getResources(String)");
        }
    }
    
    /**
     * Ensures that <code>getResources</code> returns an empty array whenever
     * no resources are found
     */
    public void testGetResourcesAlwaysReturnsNotNull()
    {
        ArchiveResourceListSource source = new ArchiveResourceListSource();
        String[] names = source.getResources("foo/bar");
        assertNotNull(names);
        assertEquals(0, names.length);
    }
    
    /**
     * A <code>null</code> path argument should return all resources in the
     * root of any archive in the classpath.  Unfortunately there's no way to
     * know exactly how many this should be, so a test cannot be made for the
     * number. 
     */
    public void testGetResourcesHandlesNullPath()
    {
        ArchiveResourceListSource source = new ArchiveResourceListSource();
        String[] names = source.getResources(null);
        assertNotNull(names);
    }
}

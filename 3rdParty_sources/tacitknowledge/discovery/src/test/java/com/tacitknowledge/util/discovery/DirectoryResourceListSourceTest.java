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
 * Tests the <code>DirectoryResourceListSourceTest</code> class.
 * 
 * @author  Scott Askew (scott@tacitknowledge.com)
 */
public class DirectoryResourceListSourceTest extends TestCase
{
    /**
     * Constructor for DirectoryResourceListSourceTest.
     * 
     * @param name the name of the test to run
     */
    public DirectoryResourceListSourceTest(String name)
    {
        super(name);
    }
    
    /**
     * Tests the normal operation of <code>getResources</code> 
     */
    public void testGetResources()
    {
        DirectoryResourceListSource source = new DirectoryResourceListSource();
        String dir = getClass().getPackage().getName().replace('.', File.separatorChar);
        String[] names = source.getResources(dir);
        assertNotNull(names);
        assertTrue(names.length > 0);
    }
    
    /**
     * Ensures that empty resuls are represented by an empty array, not null
     */
    public void testGetResourcesHandlesEmptyResults()
    {
        DirectoryResourceListSource source = new DirectoryResourceListSource();
        String dir = "foo/bar";
        String[] names = source.getResources(dir);
        assertNotNull(names);
        assertEquals(0, names.length);
    }
    
   /**
    * A <code>null</code> path argument should return all resources in the
    * root of any directory in the classpath.  Unfortunately there's no way to
    * know exactly how many this should be, so a test cannot be made for the
    * number. 
    */
   public void testGetResourcesAcceptsNull()
    {
        DirectoryResourceListSource source = new DirectoryResourceListSource();
        String[] names = source.getResources(null);
        assertNotNull(names);
    }
}

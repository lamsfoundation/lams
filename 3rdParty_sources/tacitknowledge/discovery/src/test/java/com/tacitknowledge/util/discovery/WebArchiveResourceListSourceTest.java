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
import java.util.List;

import junit.framework.TestCase;

/**
 * 
 * 
 * @author  Scott Askew (scott@tacitknowledge.com)
 */
public class WebArchiveResourceListSourceTest extends TestCase
{
    /**
     * Constructor for WebArchiveResourceListSourceTest.
     * 
     * @param name the name of the test to run
     */
    public WebArchiveResourceListSourceTest(String name)
    {
        super(name);
    }
    
    /**
     * Tests the <code>getJars</code> method.
     */
    public void testJars()
    {
        final String path = File.separator + "test";
        final String libPath = path + File.separator + "lib";
        WebArchiveResourceListSource s = new WebArchiveResourceListSource(path) {
            /**
             * {@inheritDoc}
             */
            protected String[] getLibEntries(String dir)
            {
                assertEquals(dir, libPath);
                String[] files = new String[] {
                    "test.jar",
                    "test.zip",
                    "fail.txt",
                };
                return files;
            }
        };
        
        List jars = s.getJars();
        assertEquals(2, jars.size());
        assertTrue(jars.contains(libPath + File.separator + "test.jar"));
        assertTrue(jars.contains(libPath + File.separator + "test.zip"));
    }
}

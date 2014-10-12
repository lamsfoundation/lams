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

import junit.framework.TestCase;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;

/**
 * Test the ClasspathReader
 *
 * @author Mike Hardy <mailto:mike@tacitknowledge.com/>
 */
public class ClasspathReaderTest extends TestCase
{
    /** The reader we're testing */
    private ClasspathFileReader reader = new ClasspathFileReader();

    /**
     * Test loading a non-existent class
     */
    public void testLoadBogusFile()
    {
        try
        {
            reader.getFile("bogus");
            fail("Should have gotten a FileNotFoundException");
        }
        catch (FileNotFoundException fnfe)
        {
            // this is good
        }
        catch (Exception e)
        {
            fail("Should have been a FileNotFoundException, was: " + e.getClass().getName());
        }
    }

    /**
     * Test loading a file from the classpath
     *
     * @exception Exception if anything goes wrong
     */
    public void testBasicFileLoad() throws Exception
    {
        // load ourselves up
        reader.getFile("com" + File.separator
                       + "tacitknowledge" + File.separator
                       + "util" + File.separator
                       + "discovery" + File.separator
                       + "ClasspathFileReader.class");

        // TODO actually assert something - harder than it sounds
    }

    /**
     * Test file loading as a stream
     *
     * @exception Exception if anything goes wrong
     */
    public void testBasicStreamLoad() throws Exception
    {
        // load as a stream
        InputStream stream = reader.getResourceAsStream("com" + File.separator
                                                        + "tacitknowledge" + File.separator
                                                        + "util" + File.separator
                                                        + "discovery" + File.separator
                                                        + "ClasspathFileReader.class");
        stream.close();

        // TODO actually assert something - harder than it sounds
    }

    /**
     * Test modification access
     *
     * @exception Exception if anything goes wrong
     */
    public void testModification() throws Exception
    {
        // load ourselves up
        File file = reader.getFile("com" + File.separator
                                   + "tacitknowledge" + File.separator
                                   + "util" + File.separator
                                   + "discovery" + File.separator
                                   + "ClasspathFileReader.class");

        // no reload time, but no modification, should be false
        reader.setReloadSeconds(0);
        boolean modified = reader.isModified("com" + File.separator
                                             + "tacitknowledge" + File.separator
                                             + "util" + File.separator
                                             + "discovery" + File.separator
                                             + "ClasspathFileReader.class");
        assertFalse(modified);

        // alter the file, set a reload time, should be false
        file.setLastModified(System.currentTimeMillis());
        reader.setReloadSeconds(1);
        assertEquals(1, reader.getReloadSeconds());
        modified = reader.isModified("com" + File.separator
                                     + "tacitknowledge" + File.separator
                                     + "util" + File.separator
                                     + "discovery" + File.separator
                                     + "ClasspathFileReader.class");
        assertFalse(modified);

        // wait for reload time to expire, should be true
        Thread.sleep(1500);
        modified = reader.isModified("com" + File.separator
                                     + "tacitknowledge" + File.separator
                                     + "util" + File.separator
                                     + "discovery" + File.separator
                                     + "ClasspathFileReader.class");
        assertTrue(modified);
    }
}

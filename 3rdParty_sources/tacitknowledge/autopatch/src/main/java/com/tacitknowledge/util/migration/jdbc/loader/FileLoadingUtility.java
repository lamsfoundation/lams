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

package com.tacitknowledge.util.migration.jdbc.loader;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

/**
 * This is a very simple utility that looks for a file
 * based upon its existence in the classpath or the
 * absolute path if provided.
 *
 * @author Chris A. (chris@tacitknowledge.com)
 */
public class FileLoadingUtility
{
    /**
     * Class logger
     */
    private static Log log = LogFactory.getLog(FileLoadingUtility.class);

    /**
     * The name of the file to load
     */
    private String fileName = null;

    /**
     * Creates a new <code>FileLoadingUtility</code>.
     *
     * @param fileName the name of the file to load
     */
    public FileLoadingUtility(String fileName)
    {
        this.fileName = fileName;
    }

    /**
     * Gets an input stream by first checking the current classloader,
     * then trying to use the system classloader, and finally, trying
     * to access the file on the file system.  If the file is not found,
     * an <code>IllegalArgumentException</code> will be thrown.
     *
     * @return the file as an input stream
     */
    public InputStream getResourceAsStream()
    {
        InputStream stream =
                Thread.currentThread().getContextClassLoader().getResourceAsStream(fileName);
        if (stream == null)
        {
            stream = ClassLoader.getSystemResourceAsStream(fileName);

        }
        if (stream == null)
        {
            File f = new File(fileName);
            try
            {
                stream = new FileInputStream(f);
            }
            catch (FileNotFoundException e)
            {
                log.error("The file: " + fileName + " was not found.", e);
                throw new IllegalArgumentException("Must have a valid file name.");
            }
        }
        return stream;
    }
}

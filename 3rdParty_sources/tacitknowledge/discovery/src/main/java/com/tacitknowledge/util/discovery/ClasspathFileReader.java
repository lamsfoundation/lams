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
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Used to load files from the classpath, similar to
 * <code>ClassLoader.getResourceAsStream()</code>, but with the ability to
 * detect file modifications.  This class can be used to allow applications to
 * respond to changes in resource files loaded via the classpath without needing
 * to restart the VM.
 * <p>
 * For example:
 * <code>
 *    // <i>Assume that <strong>xmlContent</strong> has been populated previously</i>
 *    
 *    if (classpathFileReader.isModified(pathToXml))
 *    {
 *       InputStream is = classpathFileReader.getResourceAsStream(pathToXml);
 * 
 *       // <i>Re-read <strong>is</strong> into <strong>xmlContent</strong> </i>
 *    }
 * </code>
 * 
 * @author  Scott Askew (scott@tacitknowledge.com)
 */
public class ClasspathFileReader
{
    /**
     * The number of milliseconds per second
     */
    private static final int MILLIS_PER_SECOND = 1000;
    
    /** Used to log messages **/
    private Log log = LogFactory.getLog(getClass());
    
    /**
     * File locations and last modified times of all previously loaded files.
     * Keys are file names relative to the classpath; values are
     * <code>CacheEntry</code>s. 
     */
    private Map cache = new HashMap();
    
    /**
     * The last time the disk was stat'd.  <code>isModified</code> uses this
     * to reduce frequent file access.  
     */
    private long lastTimeDiskWasAccessed = 0L;
    
    /**
     * The number of milliseconds between file accesses.
     */
    private long reloadMillis = 0L;
   
    /**
     * Contains information pertinent to previously loaded files.
     * 
     * @author  Scott Askew (scott@tacitknowledge.com)
     */
    private class CacheEntry
    {
        /**
         * The full path to the file; used to speed subsequent lookups
         */
        private String fullPath = null;
        
        /**
         * The file's last modification time
         */
        private long lastModTime = 0L;
        
        /**
         * Determines if the file is an actual file on disk, as opposed to a
         * file inside a JAR
         */
        private boolean isFileDirectlyOnDisk = true;

        /**
         * Creates a new <code>CacheEntry</code> for a file contained in a JAR.
         */
        public CacheEntry()
        {
            isFileDirectlyOnDisk = false;
        }
        
        /**
         * Creates a new <code>CacheEntry</code> for a file located directly on
         * disk and not in any archive (.zip, .jar).
         * 
         * @param file the file on disk; may not be <code>null</code>
         */
        public CacheEntry(File file)
        {
            fullPath = file.getAbsolutePath();
            lastModTime = file.lastModified();
        }
        
        /**
         * Determines if the file has been modified since the last time it was
         * loaded
         * 
         * @return <code>true</code> if the file has been modified since the
         *         last time it was loaded; if the file has never been loaded,
         *         or is in a JAR, <code>true</code> will always be returned
         */
        public boolean isModified()
        {
            if (isFileDirectlyOnDisk)
            {
                File file = new File(fullPath);
                if (!file.exists())
                {
                    return true; 
                }
                else
                {
                    return (lastModTime != file.lastModified());
                }
            }
            else
            {
                return false;
            }
        }
    }

    /**
     * Resolves the given file name, relative to the classpath, and returns a
     * corresponding <code>File</code> object.  Only files that actually exist
     * on their own on disk (as opposed to being in a JAR) will be resolved.
     * 
     * @param  fileName the name of the file to lookup, relative to the classpath
     * @return the requested file
     * @throws FileNotFoundException if the file could not be found
     */
    public File getFile(String fileName) throws FileNotFoundException
    {
        final String methodName = "getFile(): ";
        lastTimeDiskWasAccessed = System.currentTimeMillis();
        List classpath = ClasspathUtils.getClasspathDirectories();
        
        log.debug(methodName + "looking for file '" + fileName + "' in classpath");
        File file = findFile(fileName, classpath);
        if (file != null)
        {
            log.debug(methodName + " found file '" + fileName + "'");
            return file;
        }
        log.debug(methodName + " did not find file '" + fileName + "'");
        throw new FileNotFoundException("Could not locate file in classpath");
    }

    /**
     * Finds a file if it exists in a list of provided paths
     * 
     * @param fileName the name of the file 
     * @param path a list of file paths
     * @return a file or null if none is found
     */
    private File findFile(String fileName, List path)
    {
        final String methodName = "findFile(): ";
        String fileSeparator = System.getProperty("file.separator");
        for (Iterator i = path.iterator(); i.hasNext();)
        {
            String directory = (String) i.next();
            if (!directory.endsWith(fileSeparator) && !fileName.startsWith(fileSeparator))
            {
                directory = directory + fileSeparator;
            }
            File file = new File(directory + fileName);
            if (log.isDebugEnabled())
            {
                log.debug(methodName + " checking '" + file.getAbsolutePath() + "'");
            }
            if (file.exists() && file.canRead())
            {
                log.debug(methodName + " found it - file is '" + file.getAbsolutePath() + "'");
                cache.put(fileName, new CacheEntry(file));
                return file;
            }
        }
        return null;
    }
    
    
    /**
     * Returns an input stream for reading the specified resource.  This method
     * functions similarly to
     * <code>ClassLoader.getResourceAsStream</code>. <B>Make sure you
     * close the stream when you are done with it, as it allocates a
     * file handle on the system, and leaving them open degrades
     * performance or causes failures.</B>
     * 
     * @param  fileName the name of the resource to resolve
     * @return an input stream opened to the requested resource, or <code>null</code>
     *         if the resource could not be found
     */
    public InputStream getResourceAsStream(String fileName)
    {
        try
        {
            File file = getFile(fileName);
            cache.put(fileName, new CacheEntry(file));
            return new FileInputStream(file);
        }
        catch (FileNotFoundException e)
        {
            InputStream is = 
                Thread.currentThread().getContextClassLoader().getResourceAsStream(fileName);
            if (is != null)
            {
                cache.put(fileName, new CacheEntry());
            }
            return is;
        }
    }

    /**
     * Returns <code>true</code> if the given resource has been modified since
     * the last time it was loaded with <code>getResourceAsStream</code>.
     * 
     * @param  fileName the name of the file to check
     * @return <code>true</code> if the given resource has been modified since
     *         the last time it was loaded with <code>getResourceAsStream</code>;
     *         otherwise <code>false</code>.  <code>true</code> will be returned
     *         if the file has never been loaded by <code>getResourceAsStream</code>
     * @see    #getResourceAsStream(String)
     */
    public boolean isModified(String fileName)
    {
        if ((lastTimeDiskWasAccessed + reloadMillis) > System.currentTimeMillis())
        {
            return false;
        }
        lastTimeDiskWasAccessed = System.currentTimeMillis();
        CacheEntry cacheEntry = (CacheEntry) cache.get(fileName);
        if (cacheEntry != null)
        {
             return cacheEntry.isModified();
        }
        else
        {
            return true;
        }
    }
    
    /**
     * Sets the number of seconds between up-to-date checks.  The default is
     * <code>0</code>.
     * 
     * @param seconds the number of seconds between up-to-date checks
     */
    public void setReloadSeconds(int seconds)
    {
        reloadMillis = seconds * MILLIS_PER_SECOND;
    }
    
    /**
     * Returns the number of seconds between up-to-date checks.  The default is
     * <code>0</code>.
     * 
     * @return the number of seconds between up-to-date checks
     */
    public int getReloadSeconds()
    {
        return (int) reloadMillis / MILLIS_PER_SECOND;
    }
}

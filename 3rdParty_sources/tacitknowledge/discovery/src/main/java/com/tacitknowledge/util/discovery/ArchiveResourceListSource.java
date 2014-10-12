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
import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

/**
 * <code>ResourceListSource</code> that returns all resources in a specified
 * directory that exist in an archive (.jar or .zip) in the classpath.  Files
 * that can be found in <strong>directories</strong> in the classpath are
 * ignored. 
 * <p>
 * The performance of this class should be a consideration when using it.  It
 * performs an O(n) search through <strong>every</strong> entry in
 * <strong>every</strong> .jar or .zip file in the classpath.  On a medium-
 * horsepower machine (1.2GHz Pentium III running Windows XP) with the JDK,
 * Eclipse, and the full complement of libraries required for a typical web appliation,
 * <code>getResources(Strin)</code> takes about 120 milliseconds to execute.
 * 
 * @author  Scott Askew (scott@tacitknowledge.com)
 * @see     DirectoryResourceListSource
 */
public class ArchiveResourceListSource extends ResourceListSourceSupport
{
    /**
     * @see ResourceListSourceSupport#getResources(String)
     */
    protected String[] getResources(String basePath)
    {
        String root = (basePath == null) ? "" : basePath;
        
        // Archives paths are always delimited by Unix-like forward slashes
        // Convert any Windows paths over.
        root = root.replace('\\', '/');
        
        List resourceNames = new ArrayList();
        List jars = getJars();
        
        for (Iterator i = jars.iterator(); i.hasNext();)
        {
            String jarName = (String) i.next();
            try
            {
                ZipFile file = new ZipFile(jarName);
                resourceNames.addAll(getResources(file, root));
            }
            catch (IOException e)
            {
                // Handle
            }
        }
        
        return (String[]) resourceNames.toArray(new String[resourceNames.size()]);
    }

    /**
     * Returns a list of JARs to search through.
     * 
     * @return a list of JARs to search through
     */
    protected List getJars()
    {
        return ClasspathUtils.getClasspathArchives();
    }

    /**
     * Returns a list of file resources contained in the specified directory
     * within a given Zip'd archive file.
     * 
     * @param  file the zip file containing the resources to return
     * @param  root the directory within the zip file containing the resources
     * @return a list of file resources contained in the specified directory
     *         within a given Zip'd archive file
     */
    private List getResources(ZipFile file, String root)
    {
        List resourceNames = new ArrayList();
        Enumeration e = file.entries();
        while (e.hasMoreElements())
        {
            ZipEntry entry = (ZipEntry) e.nextElement();
            String name = entry.getName();
            if (name.startsWith(root)
                && !(name.indexOf('/') > root.length())
                && !entry.isDirectory())
            {
                // Calling File.getPath() cleans up the path so that it's using
                // the proper path separators for the host OS
                name = new File(name).getPath();
                resourceNames.add(name);
            }
        }
        return resourceNames;
    }
}

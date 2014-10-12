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
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * <code>ResourceListSource</code> that returns all resources in a specified
 * directory that exist unpacked on disk in the classpath.  In other words, only
 * files that can be found in <strong>directories</strong> in the classpath are
 * considered; files in archives (.jar, .zip) are not examined. 
 * 
 * @author  Scott Askew (scott@tacitknowledge.com)
 * @see     ArchiveResourceListSource
 */
public class DirectoryResourceListSource extends ResourceListSourceSupport
{
    /**
     * Filter used to remove directories from the result list
     */
    private FileFilter directoryFilter = new DirectoryFilter();
    
    /**
     * @see ResourceListSourceSupport#getResources(String)
     */
    protected String[] getResources(String basePath)
    {
        String root = (basePath == null) ? "" : basePath;
        List resourceNames = new ArrayList();
        List directories = getDirectories();
        for (Iterator i = directories.iterator(); i.hasNext();)
        {
            String cpDirName = (String) i.next();
            String dirName = null;
            if (root.startsWith(File.separator) || cpDirName.endsWith(File.separator))
            {
                dirName = cpDirName + root; 
            }
            else
            {
                dirName = cpDirName + File.separatorChar + root;
            }
            
            File dir = new File(dirName);
            if (dir.exists() && dir.isDirectory())
            {
                File[] files = dir.listFiles(directoryFilter);
                for (int j = 0; j < files.length; j++)
                {
                    String fileName = files[j].getName();
                    String resourceName = root + File.separator + fileName;
                    resourceName = new File(resourceName).getPath();
                    resourceNames.add(resourceName);
                }
            }
        }
        
        return (String[]) resourceNames.toArray(new String[resourceNames.size()]);
    }
    
    /**
     * Returns the directories to search through
     * 
     * @return the directories to search through
     */
    protected List getDirectories()
    {
        return ClasspathUtils.getClasspathDirectories();
    }
}

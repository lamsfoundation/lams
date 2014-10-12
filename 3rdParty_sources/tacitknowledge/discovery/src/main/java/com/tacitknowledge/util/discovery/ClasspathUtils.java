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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.StringTokenizer;

/**
 * Utility class for dealing with the classpath. 
 * 
 * @author  Scott Askew (scott@tacitknowledge.com)
 */
public final class ClasspathUtils
{
    private static Log log = LogFactory.getLog(ClasspathUtils.class);

    /**
     * Hidden constructor for utility class.
     */
    private ClasspathUtils()
    {
        // Hidden constructor
    }
    
    /**
     * Returns the classpath as a list of directories.  Any classpath component
     * that is not a directory will be ignored. 
     * 
     * @return the classpath as a list of directories; if no directories can
     *         be found then an empty list will be returned
     */
    public static List getClasspathDirectories()
    {
        List directories = new ArrayList();
        List components = getClasspathComponents();
        for (Iterator i = components.iterator(); i.hasNext();)
        {
            String possibleDir = (String) i.next();
            File file = new File(possibleDir);
            if (file.isDirectory())
            {
                directories.add(possibleDir);
            }
        }
        List tomcatPaths = getTomcatPaths();
        if (tomcatPaths != null)
        {
            directories.addAll(tomcatPaths);
        }
        return directories;
    }

    /**
     * Returns the classpath as a list of the names of archive files.  Any
     * classpath component that is not an archive will be ignored.
     *
     * @return the classpath as a list of archive file names; if no archives can
     *         be found then an empty list will be returned
     */
    public static List getClasspathArchives()
    {
        List archives = new ArrayList();
        List components = getClasspathComponents();
        for (Iterator i = components.iterator(); i.hasNext();)
        {
            String possibleDir = (String) i.next();
            File file = new File(possibleDir);
            if (file.isFile()
                && (file.getName().endsWith(".jar") || file.getName().endsWith(".zip")))
            {
            	log.debug("Found archive " + file.getAbsolutePath());
                archives.add(possibleDir);
            }
        }
        return archives;
    }

    /**
     * Returns the classpath as a list directory and archive names.
     *
     * @return the classpath as a list of directory and archive file names; if
     *         no components can be found then an empty list will be returned
     */
    public static List getClasspathComponents()
    {
        List components = new LinkedList();

        // walk the classloader hierarchy, trying to get all the components we can
        ClassLoader cl = Thread.currentThread().getContextClassLoader();

        while ((null != cl) && (cl instanceof URLClassLoader))
        {
            URLClassLoader ucl = (URLClassLoader) cl;
            components.addAll(getUrlClassLoaderClasspathComponents(ucl));

            try
            {
                cl = ucl.getParent();
            }
            catch (SecurityException se)
            {
                cl = null;
            }
        }

        // walking the hierarchy doesn't guarantee we get everything, so
        // lets grab the system classpath for good measure.
        String classpath = System.getProperty("java.class.path");
        String separator = System.getProperty("path.separator");
        StringTokenizer st = new StringTokenizer(classpath, separator);
        while (st.hasMoreTokens())
        {
            String component = st.nextToken();
            // Calling File.getPath() cleans up the path so that it's using
            // the proper path separators for the host OS
            component = getCanonicalPath(component);
            log.debug("System classpath: " + component);
            components.add(component);
        }
        
        // search jars in current directory as this library
		URL url = ClasspathUtils.class.getProtectionDomain().getCodeSource().getLocation();
		String path = url.getPath();
		String realPath = path.substring(path.indexOf(':') + 1);
		File thisJar = new File(realPath);
		if (thisJar != null) {
			File currentDir = thisJar.getParentFile();
			if (currentDir.isDirectory()) {
				File[] jars = currentDir.listFiles(new JarFilter());
				for (File jar : jars) {
					try {
						components.add(jar.getCanonicalPath());
					} catch (IOException e) {
						log.error(e.getMessage());
						e.printStackTrace();
					}
				}
			}
		}
 	 	
        // Set removes any duplicates, return a list for the api.
        return new LinkedList(new HashSet(components));
    }

    public static String getCanonicalPath(String path) {
        File file = new File(path);
        String canonicalPath = null;
        if (file.exists()) {
            try {
                canonicalPath = file.getCanonicalPath();
            } catch (IOException e) {
                log.warn("Error resolving filename to canonical file: " + e.toString());
            }
        }

        if (canonicalPath == null) {
            canonicalPath = file.getPath();
        }

        return canonicalPath;
    }

    /**
     * Get the list of classpath components
     *
     * @param ucl url classloader
     * @return List of classpath components
     */
    private static List getUrlClassLoaderClasspathComponents(URLClassLoader ucl)
    {
        List components = new ArrayList();

        URL[] urls = new URL[0];

        log.debug("URLClassLoader.getName() = " + ucl.getClass().getName());
        
        // Workaround for running on JBoss with UnifiedClassLoader3 usage
        // We need to invoke getClasspath() method instead of getURLs()
        if (ucl.getClass().getName().equals("org.jboss.mx.loading.UnifiedClassLoader3"))
        {
            try
            {
                Method classPathMethod = ucl.getClass().getMethod("getClasspath", new Class[] {});
                urls = (URL[]) classPathMethod.invoke(ucl, new Object[0]);
            }
            catch(Exception e)
            {
                LogFactory.getLog(ClasspathUtils.class).debug("Error invoking getClasspath on UnifiedClassLoader3: ", e);
            }
        }
        else
        {
        	// Use regular ClassLoader method to get classpath
            urls = ucl.getURLs();
        }

        for (int i = 0; i < urls.length; i++)
        {
            URL url = urls[i];
            
            String path = getCanonicalPath(url.getPath());
            components.add(path);
            log.debug("URLClassLoader found path: " + path);
        }

        return components;
    }

    /**
     * If the system is running on Tomcat, this method will parse the
     * <code>common.loader</code> property to reach deeper into the
     * classpath to get Tomcat common paths
     *
     * @return a list of paths or null if tomcat paths not found
     */
    private static List getTomcatPaths()
    {
        String tomcatPath = System.getProperty("catalina.home");
        if (tomcatPath == null)
        {
            //not running Tomcat
            return null;
        }
        String commonClasspath = System.getProperty("common.loader");
        if (commonClasspath == null)
        {
            //didn't find the common classpath
            return null;
        }
        StringBuffer buffer = new StringBuffer(commonClasspath);
        String pathDeclaration = "${catalina.home}";
        int length = pathDeclaration.length();
        boolean doneReplace = false;
        do
        {
            int start = commonClasspath.indexOf(pathDeclaration);
            if (start >= 0)
            {
                buffer.replace(start, (start + length), tomcatPath);
                commonClasspath = buffer.toString();
            }
            else
            {
                doneReplace = true;
            }
        }
        while (!doneReplace);
        String[] paths = commonClasspath.split(",");

        List pathList = new ArrayList(paths.length);
        for (int i = 0; i < paths.length; i++) {
            String path = paths[i];
            pathList.add(getCanonicalPath(path));
        }
        return pathList;
    }
}
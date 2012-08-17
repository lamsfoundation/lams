/*
 *  Licensed to the Apache Software Foundation (ASF) under one or more
 *  contributor license agreements.  See the NOTICE file distributed with
 *  this work for additional information regarding copyright ownership.
 *  The ASF licenses this file to You under the Apache License, Version 2.0
 *  (the "License"); you may not use this file except in compliance with
 *  the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package org.apache.tomcat.jni;

/** LibraryLoader
 *
 * @author Mladen Turk
 * @version $Revision$, $Date$
 */


import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public final class LibraryLoader {


    public static String getDefaultPlatformName()
    {
        String name = System.getProperty("os.name");
        String platform = "unknown";

        if (name.startsWith("Windows"))
            platform = "windows";
        else if (name.startsWith("Mac OS"))
            platform = "macosx";
        else if (name.endsWith("BSD"))
            platform = "bsd";
        else if (name.equals("Linux"))
            platform = "linux2";
        else if (name.equals("Solaris"))
            platform = "solaris";
        else if (name.equals("SunOS"))
            platform = "solaris";
        else if (name.equals("HP-UX"))
            platform = "hpux";
        else if (name.equals("AIX"))
            platform = "aix";

        return platform;
    }

    public static String getDefaultPlatformCpu()
    {
        String cpu;
        String arch = System.getProperty("os.arch");

        if (arch.endsWith("86"))
            cpu = "x86";
        else if (arch.startsWith("PA_RISC"))
            cpu = "parisc2";
        else if (arch.startsWith("IA64"))
            cpu = "i64";
        else if (arch.startsWith("sparc"))
            cpu = "sparcv9";
        else if (arch.equals("x86_64"))
            cpu = "x64";
        else if (arch.equals("amd64"))
            cpu = "x64";
        else
            cpu = arch;
        return cpu;
    }

    public static String getDefaultLibraryPath()
    {
        String name = getDefaultPlatformName();
        String arch = getDefaultPlatformCpu();

        return name + File.separator + arch;
    }

    public static String getDefaultMetaPath()
    {
        return "META-INF" + File.separator + "lib" + File.separator;
    }

    private LibraryLoader()
    {
        // Disallow creation
    }

    protected static void load(String rootPath)
        throws SecurityException, IOException, UnsatisfiedLinkError
    {
        int count = 0;
        String name = getDefaultPlatformName();
        String path = getDefaultLibraryPath();
        Properties props = new Properties();

        File root = new File(rootPath);
        String basePath = root.getCanonicalPath().toString();
        if (!basePath.endsWith(File.separator)) {
            basePath += File.separator;
        }
        String metaPath = basePath + getDefaultMetaPath();
        File meta = new File(metaPath);
        if (!meta.exists()) {
            /* Try adding bin prefix to rootPath.
             * Used if we pass catalina.base property
             */
            metaPath = basePath + "bin" + File.separator +
                       getDefaultMetaPath();
        }
        try {
            InputStream is = LibraryLoader.class.getResourceAsStream
                ("/org/apache/tomcat/jni/Library.properties");
            props.load(is);
            is.close();
            count = Integer.parseInt(props.getProperty(name + ".count"));
        }
        catch (Throwable t) {
            throw new UnsatisfiedLinkError("Can't use Library.properties");
        }
        for (int i = 0; i < count; i++) {
            boolean optional = false;
            String dlibName = props.getProperty(name + "." + i);
            if (dlibName.startsWith("?")) {
                dlibName = dlibName.substring(1);
                optional = true;
            }
            String fullPath = metaPath + path +
                              File.separator + dlibName;
            try {
                Runtime.getRuntime().load(fullPath);
            }
            catch (Throwable d) {
                if (!optional) {
                    java.io.File fd = new java.io.File(fullPath);
                    if (fd.exists()) {
                        throw new UnsatisfiedLinkError(" Error: " + d.getMessage() + " " );
                    } else {
                        throw new UnsatisfiedLinkError(" Can't find: " + fullPath + " ");
                    }
                }
            }
        }
    }

}

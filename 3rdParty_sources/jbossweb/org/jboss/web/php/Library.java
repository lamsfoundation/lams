/*
 * JBoss, Home of Professional Open Source
 * Copyright 2006, JBoss Inc., and individual contributors as indicated
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */

package org.jboss.web.php;

/**
 * Library class.
 *
 * @author Mladen Turk
 * @version $Revision$, $Date$
 * @since 1.0
 */
public class Library {

    /* Default library names */
    private static String [] NAMES = { "php5servlet", "libphp5servlet" };

    /*
     * A handle to the unique Library singleton instance.
     */
    private static Library engine = null;
    private static boolean inited = false;

    static PhpThread phpthread = null;

    private Library()
    {
        boolean loaded = false;
        String err = "";
        for (int i = 0; i < NAMES.length; i++) {
            try {
                System.loadLibrary(NAMES[i]);
                loaded = true;
            } catch (Throwable e) {
                if ( i > 0)
                    err += ", ";
                err +=  e.getMessage();
            }
            if (loaded)
                break;
        }
        if (!loaded) {
            err += "(";
            err += System.getProperty("java.library.path");
            err += ")";
            throw new UnsatisfiedLinkError(err);
        }
    }

    private Library(String libraryName)
    {
        System.loadLibrary(libraryName);
    }

    /* PHP_MAJOR_VERSION */
    public static int PHP_MAJOR_VERSION  = 0;
    /* PHP_MINOR_VERSION */
    public static int PHP_MINOR_VERSION  = 0;
    /* PHP_PATCH_VERSION */
    public static int PHP_PATCH_VERSION  = 0;

    /* Initialize PHP Engine
     * This has to be the first call to PHP Module.
     */
    public static native boolean startup();

    /* destroy global PHP Engine
     * This has to be the last call to PHP Module.
     */
    public static native void shutdown();

    /* Internal function for obtaining PHP Module version */
    private static native int version(int index);

    /**
     * Setup any PHP internal data structures.  This MUST be the
     * first function called for PHP module.
     * @param libraryName the name of the library to load
     */
    public static boolean initialize(String libraryName)
        throws Exception
    {
        if (engine == null) {
            if (libraryName == null)
                engine = new Library();
            else
                engine = new Library(libraryName);
            PHP_MAJOR_VERSION  = version(1);
            PHP_MINOR_VERSION  = version(2);
            PHP_PATCH_VERSION  = version(3);
        }
        
        phpthread = new PhpThread();
        phpthread.setDaemon(true);
        phpthread.start();
        // Wait until the startup is done.
        while (!inited &&  phpthread.isAlive()) {
            Thread.currentThread().sleep(3000);
        }
        return inited;
    }

    /**
     * Check if PHP module is initialized.
     */
    public static boolean isInitialized()
    {
        return inited;
    }

    /**
     * Terminate PHP Engine.
     */
    public static void terminate()
    {
        if (engine != null) {
            if (phpthread != null && phpthread.isAlive()) {
                phpthread.interrupt();
            }
            engine = null;
            inited = false;
        }
    }
    public static void StartUp() {
        inited = startup();
    }
}

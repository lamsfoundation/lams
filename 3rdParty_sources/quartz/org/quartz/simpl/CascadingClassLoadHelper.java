/* 
 * Copyright 2004-2005 OpenSymphony 
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not 
 * use this file except in compliance with the License. You may obtain a copy 
 * of the License at 
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0 
 *   
 * Unless required by applicable law or agreed to in writing, software 
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT 
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the 
 * License for the specific language governing permissions and limitations 
 * under the License.
 * 
 */

/*
 * Previously Copyright (c) 2001-2004 James House
 */
package org.quartz.simpl;

import java.util.Iterator;
import java.util.LinkedList;
import java.net.URL;
import java.io.InputStream;

import org.quartz.spi.ClassLoadHelper;

/**
 * A <code>ClassLoadHelper</code> uses all of the <code>ClassLoadHelper</code>
 * types that are found in this package in its attempts to load a class, when
 * one scheme is found to work, it is promoted to the scheme that will be used
 * first the next time a class is loaded (in order to improve perfomance).
 * 
 * <p>
 * This approach is used because of the wide variance in class loader behavior
 * between the various environments in which Quartz runs (e.g. disparate 
 * application servers, stand-alone, mobile devices, etc.).  Because of this
 * disparity, Quartz ran into difficulty with a one class-load style fits-all 
 * design.  Thus, this class loader finds the approach that works, then 
 * 'remembers' it.  
 * </p>
 * 
 * @see org.quartz.spi.ClassLoadHelper
 * @see org.quartz.simpl.LoadingLoaderClassLoadHelper
 * @see org.quartz.simpl.SimpleClassLoadHelper
 * @see org.quartz.simpl.ThreadContextClassLoadHelper
 * @see org.quartz.simpl.InitThreadContextClassLoadHelper
 * 
 * @author jhouse
 */
public class CascadingClassLoadHelper implements ClassLoadHelper {

    
    /*
     * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     * 
     * Data members.
     * 
     * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     */

    private LinkedList loadHelpers;

    private ClassLoadHelper bestCandidate;

    /*
     * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     * 
     * Interface.
     * 
     * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     */

    /**
     * Called to give the ClassLoadHelper a chance to initialize itself,
     * including the oportunity to "steal" the class loader off of the calling
     * thread, which is the thread that is initializing Quartz.
     */
    public void initialize() {
        loadHelpers = new LinkedList();

        loadHelpers.add(new LoadingLoaderClassLoadHelper());
        loadHelpers.add(new SimpleClassLoadHelper());
        loadHelpers.add(new ThreadContextClassLoadHelper());
        loadHelpers.add(new InitThreadContextClassLoadHelper());
        
        Iterator iter = loadHelpers.iterator();
        while (iter.hasNext()) {
            ClassLoadHelper loadHelper = (ClassLoadHelper) iter.next();
            loadHelper.initialize();
        }
    }

    /**
     * Return the class with the given name.
     */
    public Class loadClass(String name) throws ClassNotFoundException {

        if (bestCandidate != null) {
            try {
                return bestCandidate.loadClass(name);
            } catch (Exception e) {
                bestCandidate = null;
            }
        }

        ClassNotFoundException cnfe = null;
        Class clazz = null;
        ClassLoadHelper loadHelper = null;

        Iterator iter = loadHelpers.iterator();
        while (iter.hasNext()) {
            loadHelper = (ClassLoadHelper) iter.next();

            try {
                clazz = loadHelper.loadClass(name);
                break;
            } catch (ClassNotFoundException e) {
                cnfe = e;
            }
        }

        if (clazz == null) throw cnfe;

        bestCandidate = loadHelper;

        return clazz;
    }

    /**
     * Finds a resource with a given name. This method returns null if no
     * resource with this name is found.
     * @param name name of the desired resource
     * @return a java.net.URL object
     */
    public URL getResource(String name) {

        if (bestCandidate != null) {
            try {
                return bestCandidate.getResource(name);
            } catch (Exception e) {
                bestCandidate = null;
            }
        }

        URL result = null;
        ClassLoadHelper loadHelper = null;

        Iterator iter = loadHelpers.iterator();
        while (iter.hasNext()) {
            loadHelper = (ClassLoadHelper) iter.next();

            result = loadHelper.getResource(name);
            if (result != null) {
                break;
            }
        }

        bestCandidate = loadHelper;
        return result;

    }

    /**
     * Finds a resource with a given name. This method returns null if no
     * resource with this name is found.
     * @param name name of the desired resource
     * @return a java.io.InputStream object
     */
    public InputStream getResourceAsStream(String name) {

        if (bestCandidate != null) {
            try {
                return bestCandidate.getResourceAsStream(name);
            } catch (Exception e) {
                bestCandidate = null;
            }
        }

        InputStream result = null;
        ClassLoadHelper loadHelper = null;

        Iterator iter = loadHelpers.iterator();
        while (iter.hasNext()) {
            loadHelper = (ClassLoadHelper) iter.next();

            result = loadHelper.getResourceAsStream(name);
            if (result != null) {
                break;
            }
        }

        bestCandidate = loadHelper;
        return result;

    }

}

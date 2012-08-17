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

import java.lang.reflect.Method;
import org.apache.catalina.Lifecycle;
import org.apache.catalina.LifecycleEvent;
import org.apache.catalina.util.StringManager;
import org.jboss.logging.Logger;
import org.jboss.logging.Logger;

/**
 * Implementation of <code>LifecycleListener</code> that will init and
 * and destroy PHP.
 *
 * @author Mladen Turk
 * @version $Revision$ $Date$
 * @since 1.0
 */

public class LifecycleListener
    implements org.apache.catalina.LifecycleListener {

    private static Logger log = Logger.getLogger(LifecycleListener.class);

    /**
     * The string manager for this package.
     */
    protected StringManager sm =
        StringManager.getManager(Constants.Package);

    // -------------------------------------------------------------- Constants


    protected static final int REQUIRED_MAJOR = 5;
    protected static final int REQUIRED_MINOR = 2;
    protected static final int REQUIRED_PATCH = 3;


    // ---------------------------------------------- LifecycleListener Methods


    /**
     * Primary entry point for startup and shutdown events.
     *
     * @param event The event that has occurred
     */
    public void lifecycleEvent(LifecycleEvent event) {

        if (Lifecycle.INIT_EVENT.equals(event.getType())) {
            int major = 0;
            int minor = 0;
            int patch = 0;
            try {
                String methodName = "initialize";
                Class paramTypes[] = new Class[1];
                paramTypes[0] = String.class;
                Object paramValues[] = new Object[1];
                paramValues[0] = null;
                Class clazz = Class.forName("org.jboss.web.php.Library");
                Method method = clazz.getMethod(methodName, paramTypes);
                // TODO: Use sm to obtain optional library name.
                method.invoke(null, paramValues);
                major = clazz.getField("PHP_MAJOR_VERSION").getInt(null);
                minor = clazz.getField("PHP_MINOR_VERSION").getInt(null);
                patch = clazz.getField("PHP_PATCH_VERSION").getInt(null);
            } catch (Throwable t) {
                if (!log.isDebugEnabled()) {
                    log.info(sm.getString("listener.initialize",
                             System.getProperty("java.library.path")));
                }
                else {
                    log.debug(sm.getString("listener.initialize",
                              System.getProperty("java.library.path")), t);
                }
                return;
            }
            // Check if the PHP Native module matches required version.
            if ((major != REQUIRED_MAJOR) ||
                (minor != REQUIRED_MINOR) ||
                (patch <  REQUIRED_PATCH)) {
                log.error(sm.getString("listener.invalid", major + "."
                          + minor + "." + patch, REQUIRED_MAJOR + "."
                          + REQUIRED_MINOR + "."
                          + REQUIRED_PATCH));
            }
        }
        else if (Lifecycle.AFTER_STOP_EVENT.equals(event.getType())) {
            try {
                String methodName = "terminate";
                Method method = Class.forName("org.jboss.php.servlets.php.Library")
                    .getMethod(methodName, (Class [])null);
                method.invoke(null, (Object []) null);
            }
            catch (Throwable t) {
                if (!log.isDebugEnabled()) {
                    log.info(sm.getString("listener.terminate"));
                }
                else {
                    log.debug(sm.getString("listener.terminate"), t);
                }
            }
        }
    }
}

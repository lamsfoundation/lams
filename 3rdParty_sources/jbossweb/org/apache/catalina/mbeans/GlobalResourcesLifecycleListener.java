/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.catalina.mbeans;


import javax.naming.Binding;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.OperationNotSupportedException;

import org.apache.catalina.Lifecycle;
import org.apache.catalina.LifecycleEvent;
import org.apache.catalina.LifecycleListener;
import org.apache.tomcat.util.modeler.Registry;
import org.jboss.logging.Logger;
import org.jboss.logging.Logger;


/**
 * Implementation of <code>LifecycleListener</code> that instantiates the
 * set of MBeans associated with global JNDI resources that are subject to
 * management.
 *
 * @author Craig R. McClanahan
 * @version $Revision$ $Date$
 * @since 4.1
 */

public class GlobalResourcesLifecycleListener
    implements LifecycleListener {
    private static Logger log = Logger.getLogger(GlobalResourcesLifecycleListener.class);

    // ----------------------------------------------------- Instance Variables


    /**
     * The owning Catalina component that we are attached to.
     */
    protected Lifecycle component = null;


    /**
     * The configuration information registry for our managed beans.
     */
    protected static Registry registry = MBeanUtils.createRegistry();


    // ---------------------------------------------- LifecycleListener Methods


    /**
     * Primary entry point for startup and shutdown events.
     *
     * @param event The event that has occurred
     */
    public void lifecycleEvent(LifecycleEvent event) {

        if (Lifecycle.START_EVENT.equals(event.getType())) {
            component = event.getLifecycle();
            createMBeans();
        } else if (Lifecycle.STOP_EVENT.equals(event.getType())) {
            destroyMBeans();
            component = null;
        }

    }


    // ------------------------------------------------------ Protected Methods


    /**
     * Create the MBeans for the interesting global JNDI resources.
     */
    protected void createMBeans() {

        // Look up our global naming context
        Context context = null;
        try {
            context = (Context) (new InitialContext()).lookup("java:/");
        } catch (NamingException e) {
            log.error("No global naming context defined for server");
            return;
        }

        // Recurse through the defined global JNDI resources context
        try {
            createMBeans("", context);
        } catch (NamingException e) {
            log.error("Exception processing Global JNDI Resources", e);
        }

    }


    /**
     * Create the MBeans for the interesting global JNDI resources in
     * the specified naming context.
     *
     * @param prefix Prefix for complete object name paths
     * @param context Context to be scanned
     *
     * @exception NamingException if a JNDI exception occurs
     */
    protected void createMBeans(String prefix, Context context)
        throws NamingException {

        if (log.isDebugEnabled()) {
            log.debug("Creating MBeans for Global JNDI Resources in Context '" +
                prefix + "'");
        }

        try {
            NamingEnumeration bindings = context.listBindings("");
            while (bindings.hasMore()) {
                Binding binding = (Binding) bindings.next();
                String name = prefix + binding.getName();
                Object value = context.lookup(binding.getName());
                if (log.isDebugEnabled()) {
                    log.debug("Checking resource " + name);
                }
                if (value instanceof Context) {
                    createMBeans(name + "/", (Context) value);
                }
            }
        } catch( RuntimeException ex) {
            log.error("RuntimeException " + ex);
        } catch( OperationNotSupportedException ex) {
            log.error("Operation not supported " + ex);
        }

    }


    /**
     * Destroy the MBeans for the interesting global JNDI resources.
     */
    protected void destroyMBeans() {

        if (log.isDebugEnabled()) {
            log.debug("Destroying MBeans for Global JNDI Resources");
        }

    }

}

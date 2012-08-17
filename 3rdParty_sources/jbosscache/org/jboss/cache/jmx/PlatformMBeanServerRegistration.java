/*
 * JBoss, Home of Professional Open Source.
 * Copyright 2000 - 2008, Red Hat Middleware LLC, and individual contributors
 * as indicated by the @author tags. See the copyright.txt file in the
 * distribution for a full listing of individual contributors.
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
package org.jboss.cache.jmx;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jboss.cache.CacheSPI;
import org.jboss.cache.config.Configuration;
import org.jboss.cache.factories.annotations.Inject;
import org.jboss.cache.factories.annotations.NonVolatile;
import org.jboss.cache.factories.annotations.Start;
import org.jboss.cache.factories.annotations.Stop;

/**
 * If {@link org.jboss.cache.config.Configuration#getExposeManagementStatistics()} is true, then class will register
 * all the MBeans from the ConfigurationRegistry to the pltform MBean server.
 * <p/>
 * Note: to enable platform MBeanServer the following system property should be passet to the JVM:
 * <b>-Dcom.sun.management.jmxremote</b>.
 *
 * @author Mircea.Markus@jboss.com
 * @see java.lang.management.ManagementFactory#getPlatformMBeanServer()
 * @since 3.0
 */
@NonVolatile
public class PlatformMBeanServerRegistration
{
   private static final Log log = LogFactory.getLog(PlatformMBeanServerRegistration.class);

   private CacheSPI cache;

   private JmxRegistrationManager jmxRegistrationManager;
   @Inject
   public void initialize(CacheSPI cache)
   {
      this.cache = cache;
   }

   /**
    * Here is where the registration is being performed.
    */
   @Start(priority = 14)
   public void registerToPlatformMBeanServer()
   {      
      if (cache == null)
         throw new IllegalStateException("The cache should had been injected before a call to this method");
      Configuration config = cache.getConfiguration();
      if (config.getExposeManagementStatistics())
      {
         jmxRegistrationManager = new JmxRegistrationManager(cache);
         jmxRegistrationManager.registerAllMBeans();
         log.info("JBossCache MBeans were successfully registered to the platform mbean server.");
      }
   }

   /**
    * Unregister when the cache is being stoped.
    */
   @Stop
   public void unregisterMBeans()
   {
      //this method might get called several times.
      // After the first call the cache will become null, so we guard this
      if (cache == null) return;
      Configuration config = cache.getConfiguration();
      if (config.getExposeManagementStatistics())
      {
         jmxRegistrationManager.unregisterAllMBeans();
         log.trace("JBossCache MBeans were successfully unregistered from the platform mbean server.");
      }
      cache = null;
   }
}

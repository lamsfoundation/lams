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
import org.jboss.cache.Cache;
import org.jboss.cache.CacheException;
import org.jboss.cache.CacheSPI;
import org.jboss.cache.config.Configuration;
import org.jboss.cache.factories.ComponentRegistry;

import javax.management.InstanceAlreadyExistsException;
import javax.management.MBeanServer;
import javax.management.MalformedObjectNameException;
import javax.management.ObjectName;
import java.lang.management.ManagementFactory;
import java.util.ArrayList;
import java.util.List;

/**
 * Registers all the <b>MBean</b>s from an <b>Cache</b> instance to a <b>MBeanServer</b>.
 * It iterates over all the components within <b>ComponentRegistry</b> and registers all the components
 * annotated with <b>ManagedAttribute</b>, <b>ManagedOperation</b> or/and <b>MBean</b>.
 * If no <b>MBean</b> server is provided, then the {@link java.lang.management.ManagementFactory#getPlatformMBeanServer()}
 * is being used.
 * <p/>
 * It is immutable: both cache instance and MBeanServer are being passed as arguments to the constructor.
 * <p />
 * <p>
 * Note that by default object names used are prefixed with <tt>jboss.cache:service=JBossCache</tt>.  While this format
 * works for and is consistent with JBoss AS and the JMX console, it has been known to cause problems with other JMX
 * servers such as Websphere.  To work around this, you can provide the following VM system property to override this
 * prefix with a prefix of your choice:
 * <tt><b>-Djbosscache.jmx.prefix=JBossCache</b></tt>
 * </p>
 *
 * @author Mircea.Markus@jboss.com
 * @author <a href="mailto:galder.zamarreno@jboss.com">Galder Zamarreno</a>
 * @since 3.0
 */
public class JmxRegistrationManager
{

   private static final Log log = LogFactory.getLog(JmxRegistrationManager.class);

   private static final String GENERAL_PREFIX = System.getProperty("jbosscache.jmx.prefix", "jboss.cache:service=JBossCache");

   /**
    * default ObjectName for clusterd caches. Cluster name should pe appended.
    */
   public static final String REPLICATED_CACHE_PREFIX = GENERAL_PREFIX + ",cluster=";

   /**
    * default ObjectName for non clustered caches. An unique identifier should be appended.
    */
   public static final String LOCAL_CACHE_PREFIX = GENERAL_PREFIX + ",uniqueId=";

   /**
    * Key for every Dynamic mbean added.
    */
   public static final String JMX_RESOURCE_KEY = ",jmx-resource=";

   private MBeanServer mBeanServer;

   private String objectNameBase;

   private CacheSPI cacheSpi;

   /**
    * C-tor.
    *
    * @param mBeanServer    the server where mbeans are being registered
    * @param cache          cache that needs to be monitored
    * @param objectNameBase path in the MBeanServer where to register cache MBeans
    */
   public JmxRegistrationManager(MBeanServer mBeanServer, Cache cache, ObjectName objectNameBase)
   {
      this.mBeanServer = mBeanServer;
      this.cacheSpi = (CacheSPI) cache;
      processBaseName(objectNameBase);
      log.debug("Base name is: " + this.objectNameBase);
   }

   /**
    * @throws IllegalArgumentException if the supplied objectNameBase name isn't valid
    * @see #JmxRegistrationManager(javax.management.MBeanServer, org.jboss.cache.Cache, javax.management.ObjectName)
    */
   public JmxRegistrationManager(MBeanServer mBeanServer, Cache cache, String objectNameBase)
   {
      this.mBeanServer = mBeanServer;
      this.cacheSpi = (CacheSPI) cache;
      try
      {
         processBaseName(new ObjectName(objectNameBase));
         log.debug("Base name is: " + this.objectNameBase);
      }
      catch (MalformedObjectNameException e)
      {
         throw new IllegalArgumentException("Invalid Object Name : " + objectNameBase, e);
      }
   }

   /**
    * Defaults to platform to platform MBeanServer.
    *
    * @see java.lang.management.ManagementFactory#getPlatformMBeanServer()
    * @see <a href="http://java.sun.com/j2se/1.5.0/docs/guide/management/mxbeans.html#mbean_server">platform MBeanServer</a>
    */
   public JmxRegistrationManager(Cache cache, ObjectName objectNameBase)
   {
      this(ManagementFactory.getPlatformMBeanServer(), cache, objectNameBase);
   }

   public JmxRegistrationManager(Cache cache)
   {
      this(cache, null);
   }

   /**
    * Performs the MBean registration.
    */
   public void registerAllMBeans() throws CacheException
   {
      try
      {
         List<ResourceDMBean> resourceDMBeans = getResourceDMBeans();
         for (ResourceDMBean resource : resourceDMBeans)
         {
            String resourceName = resource.getObjectName();
            ObjectName objectName = new ObjectName(getObjectName(resourceName));
            if (!mBeanServer.isRegistered(objectName))
            {
               try
               {
                  mBeanServer.registerMBean(resource, objectName);
               }
               catch (InstanceAlreadyExistsException e)
               {
                  log.warn("There same instance is already registred!", e);
               }
            }
         }
      }
      catch (Exception e)
      {
         throw new CacheException("Failure while registering mbeans", e);
      }
   }

   /**
    * Unregisters all the MBeans registered through {@link #registerAllMBeans()}.
    */
   public void unregisterAllMBeans() throws CacheException
   {
      log.trace("Unregistering jmx resources..");
      try
      {
         List<ResourceDMBean> resourceDMBeans = getResourceDMBeans();
         for (ResourceDMBean resource : resourceDMBeans)
         {
            String resourceName = resource.getObjectName();
            ObjectName objectName = new ObjectName(getObjectName(resourceName));
            if (mBeanServer.isRegistered(objectName))
            {
               mBeanServer.unregisterMBean(objectName);
            }
         }
      }
      catch (Exception e)
      {
         throw new CacheException("Failure while unregistering mbeans", e);
      }
   }


   private List<ResourceDMBean> getResourceDMBeans()
   {
      List<ResourceDMBean> resourceDMBeans = new ArrayList<ResourceDMBean>();
      for (ComponentRegistry.Component component : cacheSpi.getComponentRegistry().getRegisteredComponents())
      {
         ResourceDMBean resourceDMBean = new ResourceDMBean(component.getInstance());
         if (resourceDMBean.isManagedResource())
         {
            resourceDMBeans.add(resourceDMBean);
         }
      }
      return resourceDMBeans;
   }

   private void processBaseName(ObjectName baseName)
   {
      if (baseName != null)
      {
         this.objectNameBase = baseName.getCanonicalName();
         return;
      }
      if (cacheSpi.getConfiguration().getCacheMode().equals(Configuration.CacheMode.LOCAL))
      {
         // CurrentTimeMillis is not good enaugh as an unique id generator. I am constantly
         // getting conflicts in several parallel tests on my box. Maybe some more sofisticated
         // unique id generator should be provided?
         // For example: use identity hashcode in hex format.
         objectNameBase = LOCAL_CACHE_PREFIX + Integer.toHexString(System.identityHashCode(cacheSpi));         
      }
      else //the cache is clustered
      {
         objectNameBase = REPLICATED_CACHE_PREFIX + cacheSpi.getConfiguration().getClusterName();
      }
   }

   public String getObjectName(String resourceName)
   {
      return objectNameBase + JMX_RESOURCE_KEY + resourceName;
   }

   public String getObjectNameBase()
   {
      return objectNameBase;
   }
}

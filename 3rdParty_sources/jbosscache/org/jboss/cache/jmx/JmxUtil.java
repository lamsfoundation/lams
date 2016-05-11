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

import org.jboss.cache.config.Configuration;

import javax.management.JMException;
import javax.management.MBeanServer;
import javax.management.ObjectName;

/**
 * Various JMX related utilities
 *
 * @author Jerry Gauthier
 * @author Manik Surtani
 *
 */
@Deprecated
public class JmxUtil
{
   public static final String JBOSS_SERVER_DOMAIN = "jboss";
   public static final String JBOSS_CACHE_DOMAIN = "jboss.cache";
   public static final String SERVICE_KEY_NAME = "service";
   public static final String BASE_PREFIX = JBOSS_CACHE_DOMAIN + ":" + SERVICE_KEY_NAME + "=JBossCache";
   public static final String CLUSTER_KEY = "cluster";
   public static final String PREFIX = BASE_PREFIX + "," + CLUSTER_KEY + "=";
   public static final String UNIQUE_ID_KEY = "uniqueId";
   public static final String NO_CLUSTER_PREFIX = BASE_PREFIX + "," + UNIQUE_ID_KEY + "=";
   public static final String CACHE_TYPE_KEY = "cacheType";
   public static final String PLAIN_CACHE_TYPE = "Cache";
   public static final String MBEAN_CLASS_SUFFIX = "MBean";
   public static final String JMX_RESOURCE_KEY = ",jmx-resource=";

   public static void registerCacheMBean(MBeanServer server, CacheJmxWrapperMBean cache, String cacheObjectName)
         throws JMException
   {
      ObjectName on = new ObjectName(cacheObjectName);
      if (!server.isRegistered(on))
      {
         server.registerMBean(cache, on);
      }
   }

   public static String getDefaultCacheObjectName(org.jboss.cache.Cache cache)
   {
      // get the cache's registration name
      return getDefaultCacheObjectName(cache.getConfiguration(), cache.getClass().getName());
   }

   public static String getDefaultCacheObjectName(Configuration config, String cacheImplClass)
   {
      // get the cache's registration name
      String tmpName;
      if (config.getClusterName() == null)
      {
         tmpName = NO_CLUSTER_PREFIX + getUniqueId(cacheImplClass);
      }
      else
      {
         tmpName = PREFIX + config.getClusterName();
      }
      return tmpName;
   }

   public static String getUniqueId(String cacheImplClass)
   {
      return cacheImplClass + System.currentTimeMillis();
   }

   public static void unregisterCacheMBean(MBeanServer server, String cacheObjectName)
         throws Exception
   {
      server.unregisterMBean(new ObjectName(cacheObjectName));
   }
}

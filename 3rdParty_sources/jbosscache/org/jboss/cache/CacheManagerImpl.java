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
package org.jboss.cache;

import org.jboss.cache.config.Configuration;
import org.jboss.cache.config.ConfigurationRegistry;
import org.jboss.cache.config.XmlParsingConfigurationRegistry;
import org.jgroups.ChannelFactory;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * Basic implementation of {@link CacheManager}.
 *
 * @author <a href="brian.stansberry@jboss.com">Brian Stansberry</a>
 * @version $Revision$
 */
public class CacheManagerImpl implements CacheManager
{
   private ConfigurationRegistry configRegistry;

   private boolean configRegistryInjected;

   private final Map<String, Cache<Object, Object>> caches = new HashMap<String, Cache<Object, Object>>();

   private final Map<String, Integer> checkouts = new HashMap<String, Integer>();

   private ChannelFactory channelFactory;

   private boolean started;

   /**
    * Create a new CacheRegistryImpl.
    */
   public CacheManagerImpl()
   {
   }

   /**
    * Create a new CacheRegistryImpl using the provided ConfigurationRegistry
    * and ChannelFactory.
    */
   public CacheManagerImpl(ConfigurationRegistry configRegistry, ChannelFactory factory)
   {
      this.configRegistry = configRegistry;
      this.configRegistryInjected = true;
      this.channelFactory = factory;
   }

   /**
    * Create a new CacheRegistryImpl using the provided ChannelFactory and
    * using the provided file name to create an
    * {@link XmlParsingConfigurationRegistry}.
    */
   public CacheManagerImpl(String configFileName, ChannelFactory factory)
   {
      configRegistry = new XmlParsingConfigurationRegistry(configFileName);
      this.channelFactory = factory;
   }

   // ----------------------------------------------------------  CacheRegistry

   public ChannelFactory getChannelFactory()
   {
      return channelFactory;
   }

   public Set<String> getConfigurationNames()
   {
      synchronized (caches)
      {
         Set<String> configNames = configRegistry == null ? new HashSet<String>()
               : configRegistry.getConfigurationNames();
         configNames.addAll(getCacheNames());
         return configNames;
      }
   }

   public Set<String> getCacheNames()
   {
      synchronized (caches)
      {
         return new HashSet<String>(caches.keySet());
      }
   }

   public Cache<Object, Object> getCache(String configName, boolean create) throws Exception
   {
      Cache<Object, Object> cache;
      synchronized (caches)
      {
         cache = caches.get(configName);
         if (cache == null && create)
         {
            Configuration config = configRegistry.getConfiguration(configName);
            if (channelFactory != null && config.getMultiplexerStack() != null)
            {
               config.getRuntimeConfig().setMuxChannelFactory(channelFactory);
            }
            cache = createCache(config);
            registerCache(cache, configName);
         }
         else if (cache != null)
         {
            incrementCheckout(configName);
         }
      }

      return cache;
   }

   /**
    * Extension point for subclasses, where we actually use a
    * {@link CacheFactory} to create a cache. This default implementation
    * uses {@link DefaultCacheFactory}.
    *
    * @param config the Configuration for the cache
    * @return the Cache
    */
   protected Cache<Object, Object> createCache(Configuration config)
   {
      return new DefaultCacheFactory<Object, Object>().createCache(config, false);
   }

   public void releaseCache(String configName)
   {
      synchronized (caches)
      {
         if (!caches.containsKey(configName))
            throw new IllegalStateException(configName + " not registered");
         if (decrementCheckout(configName) == 0)
         {
            Cache<Object, Object> cache = caches.remove(configName);
            destroyCache(cache);
         }
      }
   }

   // -----------------------------------------------------------------  Public

   public ConfigurationRegistry getConfigurationRegistry()
   {
      return configRegistry;
   }

   public void setConfigurationRegistry(ConfigurationRegistry configRegistry)
   {
      this.configRegistry = configRegistry;
      this.configRegistryInjected = true;
   }

   public void setChannelFactory(ChannelFactory channelFactory)
   {
      this.channelFactory = channelFactory;
   }

   public void registerCache(Cache<Object, Object> cache, String configName)
   {
      synchronized (caches)
      {
         if (caches.containsKey(configName))
            throw new IllegalStateException(configName + " already registered");
         caches.put(configName, cache);
         incrementCheckout(configName);
      }
   }

   public void start() throws Exception
   {
      if (!started)
      {
         if (configRegistry == null)
            throw new IllegalStateException("Must configure a ConfigurationRegistry before calling start()");
         if (channelFactory == null)
            throw new IllegalStateException("Must provide a ChannelFactory before calling start()");

         if (!configRegistryInjected)
         {
            ((XmlParsingConfigurationRegistry) configRegistry).start();
         }

         started = true;
      }
   }

   public void stop()
   {
      if (started)
      {
         synchronized (caches)
         {
            for (Iterator<Map.Entry<String, Cache<Object, Object>>> it = caches.entrySet().iterator(); it.hasNext();)
            {
               Map.Entry<String, Cache<Object, Object>> entry = it.next();
               destroyCache(entry.getValue());
               it.remove();
            }
            caches.clear();
            checkouts.clear();
         }

         if (!configRegistryInjected)
         {
            ((XmlParsingConfigurationRegistry) configRegistry).stop();
         }

         started = false;
      }
   }

   // ----------------------------------------------------------------  Private

   private int incrementCheckout(String configName)
   {
      synchronized (checkouts)
      {
         Integer count = checkouts.get(configName);
         if (count == null)
            count = 0;
         Integer newVal = count + 1;
         checkouts.put(configName, newVal);
         return newVal;
      }
   }

   private int decrementCheckout(String configName)
   {
      synchronized (checkouts)
      {
         Integer count = checkouts.get(configName);
         if (count == null || count < 1)
            throw new IllegalStateException("invalid count of " + count + " for " + configName);

         Integer newVal = count - 1;
         checkouts.put(configName, newVal);
         return newVal;
      }
   }

   private void destroyCache(Cache<Object, Object> cache)
   {
      if (cache.getCacheStatus() == CacheStatus.STARTED)
      {
         cache.stop();
      }
      if (cache.getCacheStatus() != CacheStatus.DESTROYED && cache.getCacheStatus() != CacheStatus.INSTANTIATED)
      {
         cache.destroy();
      }
   }
}

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

import org.jboss.cache.annotations.Compat;
import org.jboss.cache.config.Configuration;
import org.jboss.cache.config.ConfigurationException;
import org.jboss.cache.config.LegacyConfigurationException;
import org.jboss.cache.config.parsing.XmlConfigurationParser;
import org.jboss.cache.config.parsing.XmlConfigurationParser2x;
import org.jboss.cache.factories.ComponentFactory;
import org.jboss.cache.factories.ComponentRegistry;
import org.jboss.cache.invocation.CacheInvocationDelegate;
import org.jboss.cache.jmx.PlatformMBeanServerRegistration;

import java.io.InputStream;

/**
 * Default implementation of the {@link org.jboss.cache.CacheFactory} interface.
 * <p/>
 * This is a special instance of a {@link ComponentFactory} which contains bootstrap information for the
 * {@link org.jboss.cache.factories.ComponentRegistry}.
 * <p/>
 *
 * @author <a href="mailto:manik AT jboss DOT org">Manik Surtani (manik AT jboss DOT org)</a>
 * @see org.jboss.cache.factories.ComponentFactory
 */
public class DefaultCacheFactory<K, V> extends ComponentFactory implements CacheFactory<K, V>
{
   private ClassLoader defaultClassLoader;


   /**
    * This is here for backward compatibility only.  Use <tt>new DefaultCacheFactory()</tt> instead.
    *
    * @deprecated
    */
   @SuppressWarnings("unchecked")
   @Deprecated
   @Compat
   public static CacheFactory getInstance()
   {
      return new DefaultCacheFactory();
   }

   public Cache<K, V> createCache() throws ConfigurationException
   {
      return createCache(true);
   }

   public Cache<K, V> createCache(boolean start) throws ConfigurationException
   {
      return createCache(new Configuration(), start);
   }

   public Cache<K, V> createCache(String configFileName) throws ConfigurationException
   {
      return createCache(configFileName, true);
   }

   public Cache<K, V> createCache(String configFileName, boolean start) throws ConfigurationException
   {
      XmlConfigurationParser parser = new XmlConfigurationParser();
      Configuration c;
      try
      {
         c = parser.parseFile(configFileName);
      }
      catch (ConfigurationException e)
      {
         XmlConfigurationParser2x oldParser = new XmlConfigurationParser2x();
         c = oldParser.parseFile(configFileName);
      }
      return createCache(c, start);
   }

   /**
    * This implementation clones the configuration passed in before using it.
    *
    * @param configuration to use
    * @return a cache
    * @throws ConfigurationException if there are problems with the cfg
    */
   public Cache<K, V> createCache(Configuration configuration) throws ConfigurationException
   {
      return createCache(configuration, true);
   }

   /**
    * This implementation clones the configuration passed in before using it.
    *
    * @param configuration to use
    * @param start         whether to start the cache
    * @return a cache
    * @throws ConfigurationException if there are problems with the cfg
    */
   public Cache<K, V> createCache(Configuration configuration, boolean start) throws ConfigurationException
   {
      try
      {
         CacheSPI<K, V> cache = createAndWire(configuration);
         if (start) cache.start();
         return cache;
      }
      catch (ConfigurationException ce)
      {
         throw ce;
      }
      catch (RuntimeException re)
      {
         throw re;
      }
      catch (Exception e)
      {
         throw new RuntimeException(e);
      }
   }

   protected CacheSPI<K, V> createAndWire(Configuration configuration) throws Exception
   {
      CacheSPI<K, V> spi = new CacheInvocationDelegate<K, V>();
      bootstrap(spi, configuration);
      return spi;
   }

   /**
    * Bootstraps this factory with a Configuration and a ComponentRegistry.
    */
   private void bootstrap(CacheSPI spi, Configuration configuration)
   {
      // injection bootstrap stuff
      componentRegistry = new ComponentRegistry(configuration, spi);
      componentRegistry.registerDefaultClassLoader(defaultClassLoader);
      this.configuration = configuration;

      componentRegistry.registerComponent(spi, CacheSPI.class);
      componentRegistry.registerComponent(new PlatformMBeanServerRegistration(), PlatformMBeanServerRegistration.class);
   }

   /**
    * Allows users to specify a default class loader to use for both the construction and running of the cache.
    *
    * @param loader class loader to use as a default.
    */
   public void setDefaultClassLoader(ClassLoader loader)
   {
      this.defaultClassLoader = loader;
   }

   public Cache<K, V> createCache(InputStream is) throws ConfigurationException
   {
      XmlConfigurationParser parser = new XmlConfigurationParser();
      Configuration c = null;
      try
      {
         c = parser.parseStream(is);
      }
      catch (ConfigurationException e)
      {
         XmlConfigurationParser2x oldParser = new XmlConfigurationParser2x();
         c = oldParser.parseStream(is);
         if (c != null && log.isInfoEnabled())
            log.info("Detected legacy configuration file format when parsing configuration file.  Migrating to the new (3.x) file format is recommended.  See FAQs for details.");
      }
      return createCache(c);
   }

   public Cache<K, V> createCache(InputStream is, boolean start) throws ConfigurationException
   {
      XmlConfigurationParser parser = new XmlConfigurationParser();
      Configuration c = parser.parseStream(is);
      return createCache(c, start);
   }

   @Override
   protected <T> T construct(Class<T> componentType)
   {
      throw new UnsupportedOperationException("Should never be invoked - this is a bootstrap factory.");
   }
}

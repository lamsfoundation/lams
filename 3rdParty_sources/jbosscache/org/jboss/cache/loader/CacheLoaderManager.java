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
package org.jboss.cache.loader;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jboss.cache.CacheException;
import org.jboss.cache.CacheSPI;
import org.jboss.cache.Fqn;
import org.jboss.cache.RegionManager;
import org.jboss.cache.config.CacheLoaderConfig;
import org.jboss.cache.config.CacheLoaderConfig.IndividualCacheLoaderConfig;
import org.jboss.cache.config.CacheLoaderConfig.IndividualCacheLoaderConfig.SingletonStoreConfig;
import org.jboss.cache.config.Configuration;
import org.jboss.cache.config.ConfigurationException;
import org.jboss.cache.factories.ComponentRegistry;
import org.jboss.cache.factories.annotations.Inject;
import org.jboss.cache.factories.annotations.Start;
import org.jboss.cache.factories.annotations.Stop;
import org.jboss.cache.util.reflect.ReflectionUtil;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Set;
import java.util.StringTokenizer;

/**
 * Manages all cache loader functionality.  This class is typically initialised with an XML DOM Element,
 * represeting a cache loader configuration, or a {@link org.jboss.cache.config.CacheLoaderConfig} object.
 * <p/>
 * Usage:
 * <p/>
 * <code>
 * CacheLoaderManager manager = new CacheLoaderManager();
 * manager.setConfig(myXmlSnippet, myTreeCache);
 * CacheLoader loader = manager.getCacheLoader();
 * </code>
 * <p/>
 * The XML configuration passed in would typically look like:
 * <p/>
 * <code><![CDATA[
 * <p/>
 * <config>
 * <passivation>false</passivation>
 * <preload>/</preload>
 * <p/>
 * <cacheloader>
 * <class>org.jboss.cache.loader.FileCacheLoader</class>
 * <async>true</async>
 * <fetchPersistentState>false</fetchPersistentState>
 * <ignoreModifications>false</ignoreModifications>
 * <properties>
 * location=/tmp/file
 * </properties>
 * </cacheloader>
 * </config>
 * ]]>
 * </code>
 *
 * @author <a href="mailto:manik AT jboss DOT org">Manik Surtani (manik AT jboss DOT org)</a>
 * @author <a href="mailto:galder.zamarreno@jboss.com">Galder Zamarreno</a>
 */
public class CacheLoaderManager
{
   private static final Log log = LogFactory.getLog(CacheLoaderManager.class);
   private CacheLoaderConfig config;
   private CacheSPI cache;
   private CacheLoader loader;
   private boolean fetchPersistentState;
   private Configuration configuration;
   private RegionManager regionManager;
   private ComponentRegistry registry;

   @Inject
   public void injectDependencies(CacheSPI cache, Configuration configuration, RegionManager regionManager, ComponentRegistry registry)
   {
      this.regionManager = regionManager;
      this.config = configuration.getCacheLoaderConfig();
      this.cache = cache;
      this.configuration = configuration;
      this.registry = registry;

      if (config != null)
      {
         try
         {
            loader = createCacheLoader();
         }
         catch (Exception e)
         {
            throw new CacheException("Unable to create cache loaders", e);
         }
      }
   }

   /**
    * Sets a configuration object and creates a cacheloader accordingly.  Primarily used for testing.
    *
    * @param config
    * @param cache
    * @throws CacheException
    */
   public void setConfig(CacheLoaderConfig config, CacheSPI cache, Configuration configuration) throws CacheException
   {
      this.config = config == null ? configuration.getCacheLoaderConfig() : config;
      this.cache = cache;
      this.configuration = configuration;

      if (config != null)
      {
         try
         {
            loader = createCacheLoader();
         }
         catch (Exception e)
         {
            throw new CacheException("Unable to create cache loaders", e);
         }
      }
   }

   /**
    * Creates the cache loader based on a cache loader config passed in.
    *
    * @return a configured cacheloader
    * @throws IllegalAccessException
    * @throws InstantiationException
    * @throws ClassNotFoundException
    */
   private CacheLoader createCacheLoader() throws Exception
   {
      CacheLoader tmpLoader;
      // if we only have a single cache loader configured in the chaining cacheloader then
      // don't use a chaining cache loader at all.

      ArrayList<IndividualCacheLoaderConfig> finalConfigs =
            new ArrayList<IndividualCacheLoaderConfig>();

      // also if we are using passivation then just directly use the first cache loader.
      if (config.useChainingCacheLoader())
      {
         // create chaining cache loader.
         tmpLoader = new ChainingCacheLoader();
         ChainingCacheLoader ccl = (ChainingCacheLoader) tmpLoader;
         Iterator it = config.getIndividualCacheLoaderConfigs().iterator();

         // only one cache loader may have fetchPersistentState to true.
         int numLoadersWithFetchPersistentState = 0;
         while (it.hasNext())
         {
            CacheLoaderConfig.IndividualCacheLoaderConfig cfg = (CacheLoaderConfig.IndividualCacheLoaderConfig) it.next();
            if (cfg.isFetchPersistentState())
            {
               numLoadersWithFetchPersistentState++;
               fetchPersistentState = true;
            }
            if (numLoadersWithFetchPersistentState > 1)
            {
               throw new Exception("Invalid cache loader configuration!!  Only ONE cache loader may have fetchPersistentState set to true.  Cache will not start!");
            }

            assertNotSingletonAndShared(cfg);

            CacheLoader l = createCacheLoader(cfg, cache);
            cfg = l.getConfig();
            finalConfigs.add(cfg);
            // Only loaders that deal w/ state transfer factor into
            // whether the overall chain supports ExtendedCacheLoader
            ccl.addCacheLoader(l, cfg);

         }
      }
      else
      {
         CacheLoaderConfig.IndividualCacheLoaderConfig cfg = config.getIndividualCacheLoaderConfigs().get(0);
         tmpLoader = createCacheLoader(cfg, cache);
         finalConfigs.add(tmpLoader.getConfig() == null ? cfg : tmpLoader.getConfig());
         fetchPersistentState = cfg.isFetchPersistentState();
         assertNotSingletonAndShared(cfg);
      }

      // Update the config with those actually used by the loaders
      ReflectionUtil.setValue(config, "accessible", true);
      config.setIndividualCacheLoaderConfigs(finalConfigs);

      return tmpLoader;
   }

   private void assertNotSingletonAndShared(IndividualCacheLoaderConfig cfg)
   {
      SingletonStoreConfig ssc = cfg.getSingletonStoreConfig();
      if (ssc != null && ssc.isSingletonStoreEnabled() && config.isShared())
         throw new ConfigurationException("Invalid cache loader configuration!!  If a cache loader is configured as a singleton, the cache loader cannot be shared in a cluster!");
   }

   /**
    * Creates the cache loader based on the configuration.
    *
    * @param cfg
    * @param cache
    * @return a cache loader
    * @throws Exception
    */
   @SuppressWarnings("deprecation")
   private CacheLoader createCacheLoader(CacheLoaderConfig.IndividualCacheLoaderConfig cfg, CacheSPI cache) throws Exception
   {
      // create loader
      CacheLoader tmpLoader = cfg.getCacheLoader() == null ? createInstance(cfg.getClassName()) : cfg.getCacheLoader();

      if (tmpLoader != null)
      {
         // async?
         if (cfg.isAsync())
         {
            CacheLoader asyncDecorator;
            asyncDecorator = new AsyncCacheLoader(tmpLoader);
            tmpLoader = asyncDecorator;
         }

         if (cfg.isIgnoreModifications())
         {
            AbstractDelegatingCacheLoader readOnlyDecorator;
            readOnlyDecorator = new ReadOnlyDelegatingCacheLoader(tmpLoader);
            tmpLoader = readOnlyDecorator;
         }

         // singleton?
         SingletonStoreConfig ssc = cfg.getSingletonStoreConfig();
         if (ssc != null && ssc.isSingletonStoreEnabled())
         {
            Object decorator = createInstance(ssc.getSingletonStoreClass());

            /* class providing singleton store functionality must extend AbstractDelegatingCacheLoader so that
            * underlying cacheloader can be set. */
            if (decorator instanceof AbstractDelegatingCacheLoader)
            {
               AbstractDelegatingCacheLoader singletonDecorator = (AbstractDelegatingCacheLoader) decorator;
               /* set the cache loader to where calls will be delegated by the class providing the singleton
               * store functionality. */
               singletonDecorator.setCacheLoader(tmpLoader);
               tmpLoader = singletonDecorator;
            }
            else
            {
               throw new Exception("Invalid cache loader configuration!! Singleton store implementation class must extend org.jboss.cache.loader.AbstractDelegatingCacheLoader");
            }
         }

         // load props
         tmpLoader.setConfig(cfg);

         setCacheInLoader(cache, tmpLoader);
         // we should not be creating/starting the cache loader here - this should be done in the separate
         // startCacheLoader() method.
         //           tmpLoader.create();
         //           tmpLoader.start();
         if (configuration != null && configuration.isUseRegionBasedMarshalling())
         {
            tmpLoader.setRegionManager(regionManager);
         }
      }
      return tmpLoader;
   }

   /**
    * Sets the cache instance associated with the given cache loader. This method was created for testing purpouses
    * so that it can be overriden in the mock version of the CacheLoaderManager.
    *
    * @param c      instance of cache to be set in cache loader
    * @param loader cache loader to which assign the cache instance
    */
   protected void setCacheInLoader(CacheSPI c, CacheLoader loader)
   {
      loader.setCache(c);
   }

   private CacheLoader createInstance(String className) throws ClassNotFoundException, IllegalAccessException, InstantiationException
   {
      if (log.isTraceEnabled()) log.trace("instantiating class " + className);
      Class cl = Thread.currentThread().getContextClassLoader().loadClass(className);
      return (CacheLoader) cl.newInstance();
   }

   /**
    * Performs a preload on the cache based on the cache loader preload configs used when configuring the cache.
    *
    * @throws Exception
    */
   @Start(priority = 50)
   public void preloadCache() throws CacheException
   {
      if (loader != null)
      {
         if (config.getPreload() == null || config.getPreload().equals("")) return;
         if (log.isDebugEnabled()) log.debug("preloading transient state from cache loader " + loader);
         StringTokenizer st = new StringTokenizer(config.getPreload(), ",");
         String tok;
         Fqn fqn;
         long start, stop, total;
         start = System.currentTimeMillis();
         while (st.hasMoreTokens())
         {
            tok = st.nextToken();
            fqn = Fqn.fromString(tok.trim());
            if (log.isTraceEnabled()) log.trace("preloading " + fqn);
            preload(fqn, true, true);
         }

         stop = System.currentTimeMillis();
         total = stop - start;
         if (log.isDebugEnabled())
         {
            log.debug("preloading transient state from cache loader was successful (in " + total + " milliseconds)");
         }
      }
   }

   /**
    * Preloads a specific Fqn into the cache from the configured cacheloader
    *
    * @param fqn             fqn to preload
    * @param preloadParents  whether we preload parents
    * @param preloadChildren whether we preload children
    * @throws CacheException if we are unable to preload
    */
   public void preload(Fqn fqn, boolean preloadParents, boolean preloadChildren) throws CacheException
   {

      cache.getInvocationContext().getOptionOverrides().setSkipDataGravitation(true);
      cache.getInvocationContext().getOptionOverrides().setSkipCacheStatusCheck(true);
      // 1. Load the attributes first
      //  but this will go down the entire damn chain!!  :S
      cache.get(fqn, "bla");

      // 2. Then load the parents
      if (preloadParents)
      {
         Fqn tmp_fqn = Fqn.ROOT;
         for (int i = 0; i < fqn.size() - 1; i++)
         {
            tmp_fqn = Fqn.fromRelativeElements(tmp_fqn, fqn.get(i));
            cache.get(tmp_fqn, "bla");
         }
      }

      if (preloadChildren)
      {
         // 3. Then recursively for all child nodes, preload them as well
         Set children;
         try
         {
            children = loader.getChildrenNames(fqn);
         }
         catch (Exception e)
         {
            throw new CacheException("Unable to preload from cache loader", e);
         }
         if (children != null)
         {
            for (Object aChildren : children)
            {
               String child_name = (String) aChildren;
               Fqn child_fqn = Fqn.fromRelativeElements(fqn, child_name);
               preload(child_fqn, false, true);
            }
         }
      }
   }

   /**
    * Returns the configuration element of the cache loaders
    */
   public CacheLoaderConfig getCacheLoaderConfig()
   {
      return config;
   }

   /**
    * Returns the cache loader
    */
   public CacheLoader getCacheLoader()
   {
      return loader;
   }

   /**
    * Tests if we're using passivation
    */
   public boolean isPassivation()
   {
      return config.isPassivation();
   }

   /**
    * Returns true if at least one of the configured cache loaders has set fetchPersistentState to true.
    */
   public boolean isFetchPersistentState()
   {
      return fetchPersistentState;
   }

   @Stop
   public void stopCacheLoader()
   {
      if (loader != null)
      {
         // stop the cache loader
         loader.stop();
         // destroy the cache loader
         loader.destroy();
      }
   }

   @Start
   public void startCacheLoader() throws CacheException
   {
      if (config == null) config = configuration.getCacheLoaderConfig();

      if (config != null && loader == null)
      {
         try
         {
            loader = createCacheLoader();
         }
         catch (Exception e)
         {
            throw new CacheException("Unable to create cache loaders", e);
         }
      }


      if (loader != null)
      {
         try
         {
            // wire any deps.
            registry.wireDependencies(loader);

            // create the cache loader
            loader.create();
            // start the cache loader
            loader.start();

            purgeLoaders(false);
         }
         catch (Exception e)
         {
            throw new CacheException("Unable to start cache loaders", e);
         }
         fetchPersistentState = fetchPersistentState || (loader.getConfig() != null && loader.getConfig().isFetchPersistentState());
         fetchPersistentState = fetchPersistentState || (config != null && config.isFetchPersistentState());
      }
   }

   public void purgeLoaders(boolean force) throws Exception
   {
      if ((loader instanceof ChainingCacheLoader) && !force)
      {
         ((ChainingCacheLoader) loader).purgeIfNecessary();
      }
      else
      {
         CacheLoaderConfig.IndividualCacheLoaderConfig first = getCacheLoaderConfig().getFirstCacheLoaderConfig();
         if (force ||
               (first != null && first.isPurgeOnStartup()))
         {
            loader.remove(Fqn.ROOT);
         }
      }
   }
}

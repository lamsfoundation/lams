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
package org.jboss.cache.config;

import org.jboss.cache.loader.CacheLoader;
import org.jboss.cache.loader.SingletonStoreCacheLoader;
import org.jboss.cache.util.Util;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * Holds the configuration of the cache loader chain.  ALL cache loaders should be defined using this class, adding
 * individual cache loaders to the chain by calling {@link CacheLoaderConfig#addIndividualCacheLoaderConfig}
 *
 * @author <a href="mailto:manik AT jboss DOT org">Manik Surtani (manik AT jboss DOT org)</a>
 * @author Brian Stansberry
 * @author <a href="mailto:galder.zamarreno@jboss.com">Galder Zamarreno</a>
 */
public class CacheLoaderConfig extends ConfigurationComponent
{
   private static final long serialVersionUID = 2210349340378984424L;

   private boolean passivation;
   private String preload;
   private List<IndividualCacheLoaderConfig> cacheLoaderConfigs = new ArrayList<IndividualCacheLoaderConfig>();

   private boolean shared;

   public String getPreload()
   {
      return preload;
   }

   public void setPreload(String preload)
   {
      testImmutability("preload");
      this.preload = preload;
   }

   public void setPassivation(boolean passivation)
   {
      testImmutability("passivation");
      this.passivation = passivation;
   }

   public boolean isPassivation()
   {
      return passivation;
   }

   public void addIndividualCacheLoaderConfig(IndividualCacheLoaderConfig clc)
   {
      testImmutability("cacheLoaderConfigs");
      cacheLoaderConfigs.add(clc);
      // Ensure this config gets our back ref to the cache
      addChildConfig(clc);
   }

   public List<IndividualCacheLoaderConfig> getIndividualCacheLoaderConfigs()
   {
      return cacheLoaderConfigs;
   }

   public void setIndividualCacheLoaderConfigs(List<IndividualCacheLoaderConfig> configs)
   {
      testImmutability("cacheLoaderConfigs");
      // Ensure these configs get our back ref to the cache
      replaceChildConfigs(this.cacheLoaderConfigs, configs);
      this.cacheLoaderConfigs = configs == null ? new ArrayList<IndividualCacheLoaderConfig>() : configs;
   }

   public IndividualCacheLoaderConfig getFirstCacheLoaderConfig()
   {
      if (cacheLoaderConfigs.size() == 0) return null;
      return cacheLoaderConfigs.get(0);
   }

   public boolean useChainingCacheLoader()
   {
      return !isPassivation() && cacheLoaderConfigs.size() > 1;
   }

   @Override
   public String toString()
   {
      return new StringBuilder().append("CacheLoaderConfig{").append("shared=").append(shared).append(", passivation=").append(passivation).append(", preload='").append(preload).append('\'').append(", cacheLoaderConfigs.size()=").append(cacheLoaderConfigs.size()).append('}').toString();
   }

   public void setShared(boolean shared)
   {
      testImmutability("shared");
      this.shared = shared;
   }

   public boolean isShared()
   {
      return shared;
   }

   @Override
   public boolean equals(Object obj)
   {
      if (this == obj)
         return true;

      if (obj instanceof CacheLoaderConfig)
      {
         CacheLoaderConfig other = (CacheLoaderConfig) obj;
         return (this.passivation == other.passivation)
               && (this.shared == other.shared)
               && Util.safeEquals(this.preload, other.preload)
               && Util.safeEquals(this.cacheLoaderConfigs, other.cacheLoaderConfigs);
      }
      return false;
   }

   @Override
   public int hashCode()
   {
      int result = 19;
      result = 51 * result + (passivation ? 0 : 1);
      result = 51 * result + (shared ? 0 : 1);
      result = 51 * result + (preload == null ? 0 : preload.hashCode());
      result = 51 * result + (cacheLoaderConfigs == null ? 0 : cacheLoaderConfigs.hashCode());
      return result;
   }


   @Override
   public CacheLoaderConfig clone() throws CloneNotSupportedException
   {
      CacheLoaderConfig clone = (CacheLoaderConfig) super.clone();
      if (cacheLoaderConfigs != null)
      {
         List<IndividualCacheLoaderConfig> clcs = new ArrayList<IndividualCacheLoaderConfig>(cacheLoaderConfigs.size());
         for (IndividualCacheLoaderConfig clc : cacheLoaderConfigs)
         {
            clcs.add(clc.clone());
         }
         clone.setIndividualCacheLoaderConfigs(clcs);
      }
      return clone;
   }

   /**
    * Loops through all individual cache loader configs and checks if fetchPersistentState is set on any of them
    */
   public boolean isFetchPersistentState()
   {
      for (IndividualCacheLoaderConfig iclc : cacheLoaderConfigs)
      {
         if (iclc.isFetchPersistentState()) return true;
      }
      return false;
   }


   /**
    * Configuration object that holds the confguration of an individual cache loader.
    *
    * @author <a href="mailto:manik AT jboss DOT org">Manik Surtani (manik AT jboss DOT org)</a>
    * @author <a href="mailto:galder.zamarreno@jboss.com">Galder Zamarreno</a>
    */
   public static class IndividualCacheLoaderConfig extends PluggableConfigurationComponent
   {
      private static final long serialVersionUID = -2282396799100828593L;

      private boolean async;
      private boolean ignoreModifications;
      private boolean fetchPersistentState;

      private boolean purgeOnStartup;

      private SingletonStoreConfig singletonStoreConfig;
      private transient CacheLoader cacheLoader;

      protected void populateFromBaseConfig(IndividualCacheLoaderConfig base)
      {
         if (base != null)
         {
            setAsync(base.isAsync());
            setIgnoreModifications(base.isIgnoreModifications());
            setFetchPersistentState(base.isFetchPersistentState());
            setSingletonStoreConfig(base.getSingletonStoreConfig());
            setPurgeOnStartup(base.isPurgeOnStartup());
            setProperties(base.getProperties());
         }
      }

      public boolean isPurgeOnStartup()
      {
         return purgeOnStartup;
      }

      public boolean isFetchPersistentState()
      {
         return fetchPersistentState;
      }

      public void setFetchPersistentState(boolean fetchPersistentState)
      {
         testImmutability("fetchPersistentState");
         this.fetchPersistentState = fetchPersistentState;
      }

      public void setAsync(boolean async)
      {
         testImmutability("async");
         this.async = async;
      }

      public boolean isAsync()
      {
         return async;
      }

      public void setIgnoreModifications(boolean ignoreModifications)
      {
         testImmutability("ignoreModifications");
         this.ignoreModifications = ignoreModifications;
      }

      public boolean isIgnoreModifications()
      {
         return ignoreModifications;
      }

      public void setPurgeOnStartup(boolean purgeOnStartup)
      {
         testImmutability("purgeOnStartup");
         this.purgeOnStartup = purgeOnStartup;
      }

      public SingletonStoreConfig getSingletonStoreConfig()
      {
         return singletonStoreConfig;
      }

      public void setSingletonStoreConfig(SingletonStoreConfig singletonStoreConfig)
      {
         testImmutability("singletonStoreConfig");
         replaceChildConfig(this.singletonStoreConfig, singletonStoreConfig);
         this.singletonStoreConfig = singletonStoreConfig;
      }

      /**
       * Provides the ability to get and set a running cache loader, which, if exists, will be used rather than
       * constructing a new one.  Primarily to facilitate testing with mock objects.
       *
       * @return cache loader, if one exists
       * @since 2.1.0
       */
      public CacheLoader getCacheLoader()
      {
         return cacheLoader;
      }

      /**
       * Provides the ability to get and set a running cache loader, which, if exists, will be used rather than
       * constructing a new one.  Primarily to facilitate testing with mock objects.
       *
       * @param cacheLoader cacheLoader to set
       * @since 2.1.0
       */
      public void setCacheLoader(CacheLoader cacheLoader)
      {
         this.cacheLoader = cacheLoader;
      }

      @Override
      public boolean equals(Object obj)
      {
         if (super.equals(obj))
         {
            IndividualCacheLoaderConfig i = (IndividualCacheLoaderConfig) obj;
            return equalsExcludingProperties(i);
         }
         return false;
      }

      protected boolean equalsExcludingProperties(Object obj)
      {
         if (!(obj instanceof IndividualCacheLoaderConfig))
            return false;
         IndividualCacheLoaderConfig other = (IndividualCacheLoaderConfig) obj;

         return Util.safeEquals(this.className, other.className)
               && (this.async == other.async)
               && (this.ignoreModifications == other.ignoreModifications)
               && (this.fetchPersistentState == other.fetchPersistentState)
               && Util.safeEquals(this.singletonStoreConfig, other.singletonStoreConfig);

      }

      @Override
      public int hashCode()
      {
         return 31 * hashCodeExcludingProperties() + (properties == null ? 0 : properties.hashCode());
      }

      protected int hashCodeExcludingProperties()
      {
         int result = 17;
         result = 31 * result + (className == null ? 0 : className.hashCode());
         result = 31 * result + (async ? 0 : 1);
         result = 31 * result + (ignoreModifications ? 0 : 1);
         result = 31 * result + (fetchPersistentState ? 0 : 1);
         result = 31 * result + (singletonStoreConfig == null ? 0 : singletonStoreConfig.hashCode());
         result = 31 * result + (purgeOnStartup ? 0 : 1);
         return result;
      }

      @Override
      public String toString()
      {
         return new StringBuilder().append("IndividualCacheLoaderConfig{").append("className='").append(className).append('\'')
               .append(", async=").append(async)
               .append(", ignoreModifications=").append(ignoreModifications)
               .append(", fetchPersistentState=").append(fetchPersistentState)
               .append(", properties=").append(properties)
               .append(", purgeOnStartup=").append(purgeOnStartup).append("},")
               .append("SingletonStoreConfig{").append(singletonStoreConfig).append('}')
               .toString();
      }

      @Override
      public IndividualCacheLoaderConfig clone() throws CloneNotSupportedException
      {
         IndividualCacheLoaderConfig clone = (IndividualCacheLoaderConfig) super.clone();
         if (singletonStoreConfig != null)
            clone.setSingletonStoreConfig(singletonStoreConfig.clone());
         clone.cacheLoader = cacheLoader;
         return clone;
      }

      /**
       * Configuration for a SingletonStoreCacheLoader
       */
      public static class SingletonStoreConfig extends PluggableConfigurationComponent
      {
         private static final long serialVersionUID = 824251894176131850L;

         /**
          * Indicates whether the singleton store functionality is enabled or not.
          */
         private boolean singletonStoreEnabled;

         public SingletonStoreConfig()
         {
            // default value
            className = SingletonStoreCacheLoader.class.getName();
         }

         public boolean isSingletonStoreEnabled()
         {
            return singletonStoreEnabled;
         }

         public void setSingletonStoreEnabled(boolean singletonStoreEnabled)
         {
            testImmutability("singletonStoreEnabled");
            this.singletonStoreEnabled = singletonStoreEnabled;
         }

         public String getSingletonStoreClass()
         {
            return className;
         }

         public void setSingletonStoreClass(String className)
         {
            setClassName(className);
         }

         public Properties getSingletonStoreproperties()
         {
            return properties;
         }

         public void setSingletonStoreproperties(Properties properties)
         {
            setProperties(properties);
         }

         @Override
         public boolean equals(Object obj)
         {
            if (this == obj)
               return true;

            if (super.equals(obj))
            {
               SingletonStoreConfig other = (SingletonStoreConfig) obj;
               return this.singletonStoreEnabled == other.singletonStoreEnabled;
            }
            return false;
         }

         @Override
         public int hashCode()
         {
            int result = 19;
            result = 41 * result + super.hashCode();
            result = 41 * result + (singletonStoreEnabled ? 0 : 1);
            return result;
         }

         @Override
         public String toString()
         {
            return super.toString() + " enabled=" + singletonStoreEnabled +
                  " class=" + className +
                  " properties=" + properties;
         }

         @Override
         public SingletonStoreConfig clone() throws CloneNotSupportedException
         {
            return (SingletonStoreConfig) super.clone();
         }
      }
   }
}

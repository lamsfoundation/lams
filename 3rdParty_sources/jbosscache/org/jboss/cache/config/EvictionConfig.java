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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jboss.cache.Fqn;
import org.jboss.cache.RegionManagerImpl;
import org.jboss.cache.annotations.Compat;
import org.jboss.cache.eviction.DefaultEvictionActionPolicy;
import org.jboss.cache.eviction.EvictionAlgorithm;
import org.jboss.cache.eviction.EvictionPolicy;
import org.jboss.cache.eviction.ModernizablePolicy;
import org.jboss.cache.util.Util;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class EvictionConfig extends ConfigurationComponent
{
   /**
    * The serialVersionUID
    */
   private static final long serialVersionUID = -7979639000026975201L;

   public static final int WAKEUP_DEFAULT = 5000;
   public static final int EVENT_QUEUE_SIZE_DEFAULT = 200000;
   public static final String EVICTION_ACTION_POLICY_CLASS_DEFAULT = DefaultEvictionActionPolicy.class.getName();

   /**
    * value expressed in millis
    */
   @Dynamic
   private long wakeupInterval = WAKEUP_DEFAULT;

   private int defaultEventQueueSize = EVENT_QUEUE_SIZE_DEFAULT;

   // Dynamic to support runtime adds/removes of regions
   @Dynamic
   private List<EvictionRegionConfig> evictionRegionConfigs;
   private EvictionRegionConfig defaultEvictionRegionConfig;
   @Deprecated
   private String defaultEvictionPolicyClass;

   public EvictionConfig()
   {
      evictionRegionConfigs = new LinkedList<EvictionRegionConfig>();
      defaultEvictionRegionConfig = new EvictionRegionConfig(Fqn.ROOT);
      defaultEvictionRegionConfig.setEventQueueSize(EVENT_QUEUE_SIZE_DEFAULT);
      defaultEvictionRegionConfig.setEvictionActionPolicyClassName(DefaultEvictionActionPolicy.class.getName());
   }

   /**
    * @deprecated Use {@link #EvictionConfig(EvictionRegionConfig)} instead.
    */
   @Deprecated
   public EvictionConfig(String defaultEvictionPolicyClass)
   {
      this();
      setDefaultEvictionPolicyClass(defaultEvictionPolicyClass);
   }

   public EvictionConfig(EvictionRegionConfig defaultEvictionRegionConfig)
   {
      evictionRegionConfigs = new LinkedList<EvictionRegionConfig>();
      try
      {
         this.defaultEvictionRegionConfig = defaultEvictionRegionConfig.clone();
      }
      catch (CloneNotSupportedException e)
      {
         throw new ConfigurationException(e);
      }
      this.defaultEvictionRegionConfig.setEventQueueSize(EVENT_QUEUE_SIZE_DEFAULT);
      if (this.defaultEvictionRegionConfig.getEvictionActionPolicyClassName() == null)
         this.defaultEvictionRegionConfig.setEvictionActionPolicyClassName(DefaultEvictionActionPolicy.class.getName());
   }

   public EvictionConfig(EvictionRegionConfig defaultEvictionRegionConfig, int wakeupInterval)
   {
      this(defaultEvictionRegionConfig);
      this.wakeupInterval = wakeupInterval;
   }

   public boolean isValidConfig()
   {
      return (defaultEvictionRegionConfig != null && defaultEvictionRegionConfig.getEvictionActionPolicyClassName() != null && defaultEvictionRegionConfig.getEvictionAlgorithmConfig() != null)
            || (evictionRegionConfigs != null && evictionRegionConfigs.size() > 0);
   }

   public EvictionRegionConfig getDefaultEvictionRegionConfig()
   {
      return defaultEvictionRegionConfig;
   }

   public void setDefaultEvictionRegionConfig(EvictionRegionConfig defaultEvictionRegionConfig)
   {
      this.defaultEvictionRegionConfig = defaultEvictionRegionConfig;
      this.defaultEvictionRegionConfig.setEventQueueSizeIfUnset(EVENT_QUEUE_SIZE_DEFAULT);
   }

   /**
    * @deprecated use {@link #getDefaultEvictionRegionConfig()} instead.
    */
   @Deprecated
   public String getDefaultEvictionPolicyClass()
   {
      return defaultEvictionPolicyClass;
   }

   /**
    * @deprecated use {@link #setDefaultEvictionRegionConfig(EvictionRegionConfig)} instead.
    */
   @Deprecated
   public void setDefaultEvictionPolicyClass(String defaultEvictionPolicyClass)
   {
      assertIsTransformable(defaultEvictionPolicyClass);
      try
      {
         EvictionPolicy policy = (EvictionPolicy) Util.getInstance(defaultEvictionPolicyClass);
         EvictionAlgorithm ea = policy.getEvictionAlgorithm();
         defaultEvictionRegionConfig.setEvictionAlgorithmConfig(Util.getInstance(ea.getConfigurationClass()));
         this.defaultEvictionPolicyClass = defaultEvictionPolicyClass;
      }
      catch (Exception e)
      {
         throw new ConfigurationException(e);
      }
   }

   /**
    * Tests whether an eviction policy class can be transformed from the legacy format to the new interfaces introduced
    * in 3.x.  If not, this methow throws a {@link UnsupportedEvictionImplException}.
    *
    * @param evictionPolicyClass class to test
    * @throws UnsupportedEvictionImplException
    *          thrown if the eviction policy passed in is unusable
    */
   @Compat(notes = "here to test legacy impls")
   @Deprecated
   public static final void assertIsTransformable(String evictionPolicyClass) throws UnsupportedEvictionImplException
   {
      boolean throwException = true;
      if (evictionPolicyClass.indexOf("org.jboss.cache.eviction") > -1)
      {
         EvictionPolicy ep = null;
         try
         {
            ep = (EvictionPolicy) Util.getInstance(evictionPolicyClass);
            if (ep instanceof ModernizablePolicy) throwException = false;
         }
         catch (Exception e)
         {
            // do nothing
            Log l = LogFactory.getLog(EvictionConfig.class);
            if (l.isTraceEnabled()) l.trace(e);
         }
      }
      if (throwException)
         throw new UnsupportedEvictionImplException("Unsupported custom eviction policy [" + evictionPolicyClass +
               "]. Starting with 3.x the eviction API has changed, the code needs to be manually migrated.  Please re-implement your custom policy.");
   }

   /**
    * Creates an EvictionRegionConfig for the
    * {@link org.jboss.cache.RegionManagerImpl#DEFAULT_REGION "_default_"} region using the
    * {@link #getDefaultEvictionPolicyClass()}  default eviction policy class}. Throws a
    * {@link ConfigurationException} if
    * {@link #setDefaultEvictionPolicyClass(String) a default eviction policy class}
    * has not been set.
    *
    * @return an EvictionRegionConfig whose FQN is {@link org.jboss.cache.RegionManagerImpl#DEFAULT_REGION}
    *         and whose EvictionPolicyConfig is the default config for the
    *         default eviction policy class.
    * @throws ConfigurationException if a
    *                                {@link #setDefaultEvictionPolicyClass(String) a default eviction policy class}
    *                                has not been set or there is a problem instantiating the configuration.
    * @deprecated the default region is now created when this instance is constructed.  Use {@link #getDefaultEvictionRegionConfig()} instead.
    */
   @Deprecated
   public EvictionRegionConfig createDefaultEvictionRegionConfig()
   {
      return getDefaultEvictionRegionConfig();
   }

   public List<EvictionRegionConfig> getEvictionRegionConfigs()
   {
      return evictionRegionConfigs;
   }

   public void setEvictionRegionConfigs(List<EvictionRegionConfig> evictionRegionConfigs)
   {
      testImmutability("evictionRegionConfigs");
      EvictionRegionConfig toRemove = null;
      for (EvictionRegionConfig erc : evictionRegionConfigs)
      {
         if (erc.getRegionFqn().isRoot() || erc.getRegionFqn().equals(RegionManagerImpl.DEFAULT_REGION))
         {
            mergeWithDefault(erc);
            toRemove = erc;
            break;
         }
      }

      if (toRemove != null) evictionRegionConfigs.remove(toRemove);

      this.evictionRegionConfigs = evictionRegionConfigs;
   }

   private void mergeWithDefault(EvictionRegionConfig erc)
   {
      erc.setEventQueueSizeIfUnset(defaultEvictionRegionConfig.getEventQueueSize());
      if (erc.getEvictionAlgorithmConfig() == null)
         erc.setEvictionAlgorithmConfig(defaultEvictionRegionConfig.getEvictionAlgorithmConfig());
      defaultEvictionRegionConfig = erc;
   }

   public void addEvictionRegionConfig(EvictionRegionConfig evictionRegionConfig)
   {
      testImmutability("evictionRegionConfigs");
      if (evictionRegionConfig.getRegionFqn().isRoot() || evictionRegionConfig.getRegionFqn().equals(RegionManagerImpl.DEFAULT_REGION))
      {
         mergeWithDefault(evictionRegionConfig);
      }
      else
      {
         evictionRegionConfigs.add(evictionRegionConfig);
      }
   }

   /**
    * @return the wake up interval of the eviction thread, in milliseconds.
    */
   public long getWakeupInterval()
   {
      return wakeupInterval;
   }

   /**
    * Set the wake up interval for the eviction thread. 0 or a negative number disables the eviction thread.
    *
    * @param wakeupInterval interval, in milliseconds.
    */
   public void setWakeupInterval(long wakeupInterval)
   {
      testImmutability("wakeupInterval");
      this.wakeupInterval = wakeupInterval;
   }

   /**
    * Set the wake up interval for the eviction thread. 0 or a negative number disables the eviction thread.
    *
    * @param wakeupInterval interval
    * @param timeUnit       for the interval provided
    */
   public void setWakeupInterval(long wakeupInterval, TimeUnit timeUnit)
   {
      testImmutability("wakeupInterval");
      this.wakeupInterval = timeUnit.toMillis(wakeupInterval);
   }

   /**
    * @deprecated Use {@link #getWakeupIntervalSeconds()}.
    */
   @Deprecated
   public int getWakeupIntervalSeconds()
   {
      return (int) TimeUnit.MILLISECONDS.toSeconds(wakeupInterval);
   }

   /**
    * @deprecated Use {@link #setWakeupInterval(long)}.
    */
   @Deprecated
   public void setWakeupIntervalSeconds(int wakeupIntervalSeconds)
   {
      setWakeupInterval(wakeupIntervalSeconds, TimeUnit.SECONDS);
   }

   public boolean equals(Object o)
   {
      if (this == o) return true;
      if (!(o instanceof EvictionConfig)) return false;

      EvictionConfig that = (EvictionConfig) o;

      if (defaultEventQueueSize != that.defaultEventQueueSize) return false;
      if (wakeupInterval != that.wakeupInterval) return false;
      if (defaultEvictionRegionConfig != null ? !defaultEvictionRegionConfig.equals(that.defaultEvictionRegionConfig) : that.defaultEvictionRegionConfig != null)
         return false;
      if (evictionRegionConfigs != null ? !evictionRegionConfigs.equals(that.evictionRegionConfigs) : that.evictionRegionConfigs != null)
         return false;

      return true;
   }

   public int hashCode()
   {
      int result;
      result = 31 + (int) (wakeupInterval ^ (wakeupInterval >>> 32));
      result = 31 * result + defaultEventQueueSize;
      result = 31 * result + (evictionRegionConfigs != null ? evictionRegionConfigs.hashCode() : 0);
      return result;
   }

   @Override
   public EvictionConfig clone() throws CloneNotSupportedException
   {
      EvictionConfig clone = (EvictionConfig) super.clone();
      if (evictionRegionConfigs != null)
      {
         // needs to be a deep copy
         clone.evictionRegionConfigs = new LinkedList<EvictionRegionConfig>();
         for (EvictionRegionConfig erc : evictionRegionConfigs)
            clone.addEvictionRegionConfig(erc.clone());
      }
      return clone;
   }


   /**
    * Returns the <code>EvictionRegionConfig</code> coresponding to given region fqn, or <code>null</code> if no
    * match is found.
    */
   public EvictionRegionConfig getEvictionRegionConfig(String region)
   {
      Fqn fqn = Fqn.fromString(region);
      for (EvictionRegionConfig evConfig : getEvictionRegionConfigs())
      {
         if (evConfig.getRegionFqn().equals(fqn))
         {
            return evConfig;
         }
      }
      return null;
   }

   /**
    * Applies defaults to a config passed in
    *
    * @param config config to apply defaults to
    */
   public void applyDefaults(EvictionRegionConfig config)
   {
      if (config == null) return; // no op
      config.setDefaults(defaultEvictionRegionConfig);
   }

   /**
    * @deprecated set these attributes on the default eviction region config.
    */
   @Deprecated
   public void setDefaultEventQueueSize(int queueSize)
   {
      defaultEvictionRegionConfig.setEventQueueSize(queueSize);
   }
}

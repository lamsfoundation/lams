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

import org.apache.commons.logging.LogFactory;
import org.jboss.cache.CacheException;
import org.jboss.cache.Fqn;
import org.jboss.cache.eviction.ModernizableConfig;
import org.jboss.cache.util.Util;

import java.lang.reflect.Method;

/**
 * It is imperative that a region Fqn is set, either via one of the constructors or using {@link #setRegionFqn(org.jboss.cache.Fqn)}.
 */
public class EvictionRegionConfig extends ConfigurationComponent
{
   /**
    * The serialVersionUID
    */
   private static final long serialVersionUID = -5482474634995601400L;

   public static final String NAME = "name";
   public static final String REGION = "region";

   private Fqn regionFqn;
   @Dynamic
   private Integer eventQueueSize;
   private EvictionAlgorithmConfig evictionAlgorithmConfig;
   @Deprecated
   private EvictionPolicyConfig deprecatedConfig;
   private String evictionActionPolicyClassName;

   public EvictionRegionConfig()
   {
   }

   /**
    * @deprecated use {@link #EvictionRegionConfig(org.jboss.cache.Fqn, EvictionAlgorithmConfig)} instead.
    */
   @Deprecated
   @SuppressWarnings("deprecation")
   public EvictionRegionConfig(Fqn regionFqn, EvictionPolicyConfig evictionPolicyConfig)
   {
      this.regionFqn = regionFqn;
      if (evictionPolicyConfig instanceof ModernizableConfig)
      {
         this.evictionAlgorithmConfig = ((ModernizableConfig) evictionPolicyConfig).modernizeConfig();
         deprecatedConfig = evictionPolicyConfig;
      }
      else
      {
         throw new ConfigurationException("Unable to convert " + evictionPolicyConfig.getClass().getName() +
               " to a more modern format, implementing " + EvictionAlgorithmConfig.class.getSimpleName() + ".  Please use " +
               EvictionAlgorithmConfig.class.getSimpleName() + " which replaces the deprecated " + EvictionPolicyConfig.class.getSimpleName());
      }
   }

   public EvictionRegionConfig(Fqn regionFqn, EvictionAlgorithmConfig evictionAlgorithmConfig)
   {
      this.regionFqn = regionFqn;
      this.evictionAlgorithmConfig = evictionAlgorithmConfig;
   }

   public EvictionRegionConfig(Fqn regionFqn, EvictionAlgorithmConfig evictionAlgorithmConfig, int queueSize)
   {
      this.regionFqn = regionFqn;
      this.evictionAlgorithmConfig = evictionAlgorithmConfig;
      this.eventQueueSize = queueSize;
   }

   public EvictionRegionConfig(Fqn fqn)
   {
      this.regionFqn = fqn;
   }

   /**
    * @deprecated use {@link #getEvictionAlgorithmConfig()} instead.
    */
   @Deprecated
   public EvictionPolicyConfig getEvictionPolicyConfig()
   {
      if (deprecatedConfig != null)
         return deprecatedConfig;
      else
         throw new CacheException("Not supported.  Please use " + EvictionAlgorithmConfig.class.getSimpleName() +
               " instead of " + EvictionPolicyConfig.class.getSimpleName());
   }

   public EvictionAlgorithmConfig getEvictionAlgorithmConfig()
   {
      return evictionAlgorithmConfig;
   }

   /**
    * @deprecated see {@link #setEvictionAlgorithmConfig(EvictionAlgorithmConfig)}
    */
   @Deprecated
   public void setEvictionPolicyConfig(EvictionPolicyConfig evictionPolicyConfig)
   {
      if (evictionPolicyConfig instanceof ModernizableConfig)
      {
         deprecatedConfig = evictionPolicyConfig;
         setEvictionAlgorithmConfig(((ModernizableConfig) evictionPolicyConfig).modernizeConfig());
      }
      else
      {
         throw new UnsupportedEvictionImplException("Unable to convert " + evictionPolicyConfig.getClass().getName() +
               " to a more modern format, implementing " + EvictionAlgorithmConfig.class.getSimpleName() + ".  Please use " +
               EvictionAlgorithmConfig.class.getSimpleName() + " which replaces the deprecated " + EvictionPolicyConfig.class.getSimpleName());
      }
   }

   public void setEvictionAlgorithmConfig(EvictionAlgorithmConfig config)
   {
      testImmutability("evictionAlgorithmConfig");
      this.evictionAlgorithmConfig = config;
   }

   public Fqn getRegionFqn()
   {
      return regionFqn;
   }

   public void setRegionFqn(Fqn regionFqn)
   {
      testImmutability("regionFqn");
      this.regionFqn = regionFqn;
   }

   public String getRegionName()
   {
      return regionFqn == null ? null : regionFqn.toString();
   }

   public void setRegionName(String name)
   {
      setRegionFqn(name == null ? null : Fqn.fromString(name));
   }

   public int getEventQueueSize()
   {
      return eventQueueSize == null ? EvictionConfig.EVENT_QUEUE_SIZE_DEFAULT : eventQueueSize;
   }

   public void setEventQueueSize(int queueSize)
   {
      testImmutability("eventQueueSize");
      if (queueSize <= 0)
      {
         LogFactory.getLog(EvictionRegionConfig.class).warn("Ignoring invalid queue capacity " +
               queueSize + " -- using " +
               EvictionConfig.EVENT_QUEUE_SIZE_DEFAULT);
         queueSize = EvictionConfig.EVENT_QUEUE_SIZE_DEFAULT;
      }
      this.eventQueueSize = queueSize;
   }

   public void setDefaults(EvictionRegionConfig defaults)
   {
      // go thru each element that is unset here and copy from "defaults"
      if (eventQueueSize == null) eventQueueSize = defaults.getEventQueueSize();
      if (evictionAlgorithmConfig == null) evictionAlgorithmConfig = defaults.getEvictionAlgorithmConfig();
      if (evictionActionPolicyClassName == null)
         evictionActionPolicyClassName = defaults.getEvictionActionPolicyClassName();
   }

   @Override
   public boolean equals(Object obj)
   {
      if (this == obj)
         return true;

      if (obj instanceof EvictionRegionConfig)
      {
         EvictionRegionConfig other = (EvictionRegionConfig) obj;
         boolean equalRegions = Util.safeEquals(this.regionFqn, other.regionFqn);
         boolean equalConfigurations = Util.safeEquals(this.evictionAlgorithmConfig, other.evictionAlgorithmConfig);
         boolean equalEventQueuSizes = this.getEventQueueSize() == other.getEventQueueSize();
         return equalRegions && equalConfigurations && equalConfigurations && equalEventQueuSizes;
      }
      return false;
   }

   @Override
   public int hashCode()
   {
      int result = 17;
      result = 31 * result + (regionFqn == null ? 0 : regionFqn.hashCode());

      return result;
   }

   @Override
   public EvictionRegionConfig clone() throws CloneNotSupportedException
   {
      EvictionRegionConfig clone = (EvictionRegionConfig) super.clone();
      if (evictionAlgorithmConfig != null)
      {
         if (evictionAlgorithmConfig instanceof ConfigurationComponent)
         {
            clone.setEvictionAlgorithmConfig((EvictionAlgorithmConfig) ((ConfigurationComponent) evictionAlgorithmConfig).clone());
         }
         else
         {
            try
            {
               Method cloneMethod = this.evictionAlgorithmConfig.getClass().getDeclaredMethod("clone");
               EvictionAlgorithmConfig evictionAlgorithmConfig = (EvictionAlgorithmConfig) cloneMethod.invoke(this.evictionAlgorithmConfig);
               clone.setEvictionAlgorithmConfig(evictionAlgorithmConfig);
            }
            catch (Exception e)
            {
               CloneNotSupportedException cnse = new CloneNotSupportedException("Cannot invoke clone() on " + evictionAlgorithmConfig);
               cnse.initCause(e);
               throw cnse;
            }
         }
      }

      if (deprecatedConfig != null)
      {
         if (!(deprecatedConfig instanceof Cloneable))
         {
            throw new CloneNotSupportedException(deprecatedConfig + " is not Cloneable");
         }

         if (deprecatedConfig instanceof ConfigurationComponent)
         {
            clone.setEvictionAlgorithmConfig((EvictionAlgorithmConfig) ((ConfigurationComponent) deprecatedConfig).clone());
         }
         else
         {
            try
            {
               Method cloneMethod = this.deprecatedConfig.getClass().getDeclaredMethod("clone");
               EvictionAlgorithmConfig evictionAlgorithmConfig = (EvictionAlgorithmConfig) cloneMethod.invoke(this.deprecatedConfig);
               clone.setEvictionAlgorithmConfig(evictionAlgorithmConfig);
            }
            catch (Exception e)
            {
               CloneNotSupportedException cnse = new CloneNotSupportedException("Cannot invoke clone() on " + deprecatedConfig);
               cnse.initCause(e);
               throw cnse;
            }
         }
      }

      clone.evictionActionPolicyClassName = evictionActionPolicyClassName;

      return clone;
   }

   public boolean isDefaultRegion()
   {
      return regionFqn.isRoot();
   }

   public String getEvictionActionPolicyClassName()
   {
      return evictionActionPolicyClassName == null ? EvictionConfig.EVICTION_ACTION_POLICY_CLASS_DEFAULT : evictionActionPolicyClassName;
   }

   public void setEvictionActionPolicyClassName(String evictionActionPolicyClassName)
   {
      this.evictionActionPolicyClassName = evictionActionPolicyClassName;
   }

   public void setEventQueueSizeIfUnset(int eventQueueSize)
   {
      if (this.eventQueueSize == null) this.eventQueueSize = eventQueueSize;
   }

   /**
    * Ensure this is a valid eviction region configuration.
    */
   public void validate()
   {
      if (eventQueueSize < 1)
         throw new ConfigurationException("Eviction event queue size cannot be less than 1!");

      if (evictionAlgorithmConfig == null)
         throw new MissingPolicyException("Eviction algorithm configuration cannot be null!");

      evictionAlgorithmConfig.validate();
   }

   @Override
   public String toString()
   {
      return "EvictionRegionConfig{" +
            "regionFqn=" + regionFqn +
            ", eventQueueSize=" + eventQueueSize +
            ", evictionAlgorithmConfig=" + evictionAlgorithmConfig +
            ", evictionActionPolicyClassName='" + evictionActionPolicyClassName + '\'' +
            '}';
   }
}
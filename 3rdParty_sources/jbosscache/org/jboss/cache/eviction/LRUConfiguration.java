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
package org.jboss.cache.eviction;

import org.jboss.cache.config.ConfigurationException;
import org.jboss.cache.config.Dynamic;
import org.jboss.cache.config.EvictionAlgorithmConfig;

import java.util.concurrent.TimeUnit;

/**
 * Configuration implementation for {@link LRUPolicy}.
 * <p/>
 * If configured via XML, expects the following:
 * <p/>
 * <pre>
 * <region name="/maxAgeTest/">
 *    <attribute name="maxNodes">10000</attribute>
 *    <attribute name="timeToLiveSeconds">8</attribute>
 *    <attribute name="maxAgeSeconds">10</attribute>
 * </region>
 * </pre>
 *
 * @author Daniel Huang (dhuang@jboss.org)
 * @version $Revision$
 * @deprecated see {@link org.jboss.cache.eviction.LRUAlgorithmConfig}
 */
@Deprecated
public class LRUConfiguration extends EvictionPolicyConfigBase implements ModernizableConfig
{
   /**
    * The serialVersionUID
    */
   private static final long serialVersionUID = -3426716488271559729L;

   @Dynamic
   private int timeToLiveSeconds;
   @Dynamic
   private int maxAgeSeconds;

   public LRUConfiguration()
   {
      super();
      // Force config of ttls
      setTimeToLiveSeconds(-1);
   }

   public EvictionAlgorithmConfig modernizeConfig()
   {
      LRUAlgorithmConfig modernCfg = new LRUAlgorithmConfig();
      modernCfg.setMaxNodes(getMaxNodes());
      modernCfg.setMinTimeToLive(getMinTimeToLiveSeconds(), TimeUnit.SECONDS);
      modernCfg.setTimeToLive(getTimeToLiveSeconds(), TimeUnit.SECONDS);
      modernCfg.setMaxAge(getMaxAgeSeconds(), TimeUnit.SECONDS);
      return modernCfg;
   }

   @Override
   protected void setEvictionPolicyClassName()
   {
      setEvictionPolicyClass(LRUPolicy.class.getName());
   }

   public int getTimeToLiveSeconds()
   {
      return timeToLiveSeconds;
   }

   public void setTimeToLiveSeconds(int timeToLiveSeconds)
   {
      testImmutability("timeToLiveSeconds");
      this.timeToLiveSeconds = timeToLiveSeconds;
   }

   public int getMaxAgeSeconds()
   {
      return maxAgeSeconds;
   }

   public void setMaxAgeSeconds(int maxAgeSeconds)
   {
      testImmutability("maxAgeSeconds");
      this.maxAgeSeconds = maxAgeSeconds;
   }

   /**
    * Requires a positive timeToLive value or ConfigurationException
    * is thrown.
    */
   @Override
   public void validate() throws ConfigurationException
   {
      if (timeToLiveSeconds < 0)
      {
         throw new ConfigurationException("timeToLive must be " +
               "configured to a value greater than or equal to 0 for " + getEvictionPolicyClass());
      }
   }


   public String toString()
   {
      return "LRUConfiguration{" +
            "timeToLiveSeconds=" + timeToLiveSeconds +
            ", timeToLiveSeconds=" + timeToLiveSeconds +
            ", maxAgeSeconds=" + maxAgeSeconds +
            '}';
   }

   public boolean equals(Object o)
   {
      if (this == o) return true;
      if (!(o instanceof LRUConfiguration)) return false;
      if (!super.equals(o)) return false;

      LRUConfiguration that = (LRUConfiguration) o;

      if (maxAgeSeconds != that.maxAgeSeconds) return false;
      if (maxAgeSeconds != that.maxAgeSeconds) return false;
      if (timeToLiveSeconds != that.timeToLiveSeconds) return false;
      if (timeToLiveSeconds != that.timeToLiveSeconds) return false;

      return true;
   }

   public int hashCode()
   {
      int result = super.hashCode();
      result = 31 * result + timeToLiveSeconds;
      result = 31 * result + maxAgeSeconds;
      result = 31 * result + (timeToLiveSeconds ^ (timeToLiveSeconds >>> 7));
      result = 31 * result + (maxAgeSeconds ^ (maxAgeSeconds >>> 7));
      return result;
   }

   @Override
   public void reset()
   {
      super.reset();
      setTimeToLiveSeconds(-1);
   }

   @Override
   public LRUConfiguration clone() throws CloneNotSupportedException
   {
      return (LRUConfiguration) super.clone();
   }

}

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

import org.jboss.cache.annotations.Compat;
import org.jboss.cache.config.ConfigurationException;
import org.jboss.cache.config.Dynamic;

import java.util.concurrent.TimeUnit;

/**
 * Configuration implementation for {@link LRUAlgorithm}.
 * <p/>
 *
 * @author Manik Surtani
 * @since 3.0
 */
public class LRUAlgorithmConfig extends EvictionAlgorithmConfigBase
{
   /**
    * The serialVersionUID
    */
   private static final long serialVersionUID = -3426716488271559729L;

   /**
    * value expressed in millis
    */
   @Dynamic
   private long timeToLive = -1;

   /**
    * value expressed in millis
    */
   @Dynamic
   private long maxAge = -1;

   public LRUAlgorithmConfig()
   {
      evictionAlgorithmClassName = LRUAlgorithm.class.getName();
      // Force config of ttls
      setTimeToLive(-1);
      setMaxAge(-1);
   }

   public LRUAlgorithmConfig(long timeToLive, long maxAge)
   {
      this();
      this.timeToLive = timeToLive;
      this.maxAge = maxAge;
   }

   public LRUAlgorithmConfig(long timeToLive, long maxAge, int maxNodes)
   {
      this(timeToLive, maxAge);
      this.maxNodes = maxNodes;
   }

   /**
    * @return the time to live, in milliseconds
    */
   public long getTimeToLive()
   {
      return timeToLive;
   }

   /**
    * Sets the time to live
    *
    * @param timeToLive the time to live, in milliseconds
    */
   public void setTimeToLive(long timeToLive)
   {
      testImmutability("timeToLive");
      this.timeToLive = timeToLive;
   }

   public void setTimeToLive(long timeToLive, TimeUnit timeUnit)
   {
      testImmutability("timeToLive");
      this.timeToLive = timeUnit.toMillis(timeToLive);
   }

   @Deprecated
   @Compat
   public void setTimeToLiveSeconds(long time)
   {
      setTimeToLive(time, TimeUnit.SECONDS);
   }

   /**
    * @return the max age per element, in milliseconds
    */
   public long getMaxAge()
   {
      return maxAge;
   }

   /**
    * Sets the max age per element
    *
    * @param maxAge value in milliseconds
    */
   public void setMaxAge(long maxAge)
   {
      testImmutability("maxAge");
      this.maxAge = maxAge;
   }

   public void setMaxAge(long maxAge, TimeUnit timeUnit)
   {
      testImmutability("maxAge");
      this.maxAge = timeUnit.toMillis(maxAge);
   }

   /**
    * Requires a positive timeToLive value or ConfigurationException
    * is thrown.
    */
   @Override
   public void validate() throws ConfigurationException
   {
      super.validate();
      if (timeToLive < -1) timeToLive = -1;
   }

   public String toString()
   {
      return getClass().getSimpleName() + "{" +
            "algorithmClassName=" + evictionAlgorithmClassName +
            ", timeToLive=" + timeToLive +
            ", maxAge=" + maxAge +
            ", minTTL=" + minTimeToLive +
            ", maxNodes=" + maxNodes +
            '}';
   }

   public boolean equals(Object o)
   {
      if (this == o) return true;
      if (!(o instanceof LRUAlgorithmConfig)) return false;
      if (!super.equals(o)) return false;

      LRUAlgorithmConfig that = (LRUAlgorithmConfig) o;

      if (maxAge != that.maxAge) return false;
      if (timeToLive != that.timeToLive) return false;

      return true;
   }

   public int hashCode()
   {
      int result = super.hashCode();
      result = 31 * result + (int) (timeToLive ^ (timeToLive >>> 32));
      result = 31 * result + (int) (maxAge ^ (maxAge >>> 32));
      return result;
   }

   @Override
   public void reset()
   {
      super.reset();
      setTimeToLive(-1);
      setMaxAge(-1);
      evictionAlgorithmClassName = LRUAlgorithm.class.getName();
   }

   @Override
   public LRUAlgorithmConfig clone() throws CloneNotSupportedException
   {
      return (LRUAlgorithmConfig) super.clone();
   }
}
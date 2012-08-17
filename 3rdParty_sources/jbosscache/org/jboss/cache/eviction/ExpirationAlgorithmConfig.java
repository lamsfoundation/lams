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
import org.jboss.cache.config.Dynamic;

import java.util.concurrent.TimeUnit;

/**
 * Configuration for indicating the Node key for setting a specific eviction time.
 */
public class ExpirationAlgorithmConfig extends EvictionAlgorithmConfigBase
{

   private static final long serialVersionUID = 47338798734219507L;

   /**
    * Default key name for indicating expiration time.
    */
   public static final String EXPIRATION_KEY = "expiration";

   /**
    * Node key name used to indicate the expiration of a node.
    */
   @Dynamic
   private String expirationKeyName = EXPIRATION_KEY;

   @Dynamic
   private boolean warnNoExpirationKey = true;

   @Dynamic
   private long timeToLive = -1;

   public ExpirationAlgorithmConfig()
   {
      evictionAlgorithmClassName = ExpirationAlgorithm.class.getName();
   }

   /**
    * Returns the expirationKeyName.
    * This key should point to a java.lang.Long value in the Node data.
    */
   public String getExpirationKeyName()
   {
      return expirationKeyName;
   }

   /**
    * Sets the expirationKeyName.
    */
   public void setExpirationKeyName(String expirationKeyName)
   {
      this.expirationKeyName = expirationKeyName;
   }

   /**
    * Returns true if the algorithm should warn if a expiration key is missing for a node.
    */
   public boolean isWarnNoExpirationKey()
   {
      return warnNoExpirationKey;
   }

   /**
    * Sets if the algorithm should warn if a expiration key is missing for a node.
    */
   public void setWarnNoExpirationKey(boolean warnNoExpirationKey)
   {
      this.warnNoExpirationKey = warnNoExpirationKey;
   }

   /**
    * @return time to live, in milliseconds
    */
   public long getTimeToLive()
   {
      return timeToLive;
   }

   /**
    * Sets the time to live
    *
    * @param timeToLive value in milliseconds
    */
   public void setTimeToLive(long timeToLive)
   {
      this.timeToLive = timeToLive;
   }

   public void setTimeToLive(long timeToLive, TimeUnit timeUnit)
   {
      this.timeToLive = timeUnit.toMillis(timeToLive);
   }

   @Deprecated
   @Compat
   public void setTimeToLiveSeconds(long time)
   {
      setTimeToLive(time, TimeUnit.SECONDS);
   }

   @Override
   public ExpirationAlgorithmConfig clone() throws CloneNotSupportedException
   {
      return (ExpirationAlgorithmConfig) super.clone();
   }

   public boolean equals(Object o)
   {
      if (this == o) return true;
      if (o == null || getClass() != o.getClass()) return false;
      if (!super.equals(o)) return false;

      ExpirationAlgorithmConfig that = (ExpirationAlgorithmConfig) o;

      if (timeToLive != that.timeToLive) return false;
      if (warnNoExpirationKey != that.warnNoExpirationKey) return false;
      if (expirationKeyName != null ? !expirationKeyName.equals(that.expirationKeyName) : that.expirationKeyName != null)
         return false;

      return true;
   }

   public int hashCode()
   {
      int result = super.hashCode();
      result = 31 * result + (expirationKeyName != null ? expirationKeyName.hashCode() : 0);
      result = 31 * result + (warnNoExpirationKey ? 1 : 0);
      result = 31 * result + (int) (timeToLive ^ (timeToLive >>> 32));
      return result;
   }

   @Override
   public void reset()
   {
      super.reset();
      evictionAlgorithmClassName = ExpirationAlgorithm.class.getName();
      warnNoExpirationKey = true;
      timeToLive = -1;
   }


   public String toString()
   {
      return "ExpirationAlgorithmConfig{" +
            "expirationKeyName='" + expirationKeyName + '\'' +
            ", warnNoExpirationKey=" + warnNoExpirationKey +
            ", timeToLive=" + timeToLive +
            ", maxNodes=" + maxNodes +
            ", minTTL=" + minTimeToLive +
            '}';
   }
}
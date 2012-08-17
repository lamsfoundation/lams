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
import org.jboss.cache.config.ConfigurationComponent;
import org.jboss.cache.config.ConfigurationException;
import org.jboss.cache.config.Dynamic;
import org.jboss.cache.config.EvictionAlgorithmConfig;

import java.util.concurrent.TimeUnit;

/**
 * A base class used for configuring eviction algorithms.
 *
 * @author Manik Surtani (<a href="mailto:manik AT jboss DOT org">manik AT jboss DOT org</a>)
 * @since 3.0
 */
public abstract class EvictionAlgorithmConfigBase extends ConfigurationComponent implements EvictionAlgorithmConfig
{
   private static final long serialVersionUID = 4591691674370188932L;

   protected String evictionAlgorithmClassName;
   @Dynamic
   protected int maxNodes = -1;
   @Dynamic
   protected long minTimeToLive = -1;

   /**
    * Can only be instantiated by a subclass.
    */
   protected EvictionAlgorithmConfigBase()
   {
   }

   public String getEvictionAlgorithmClassName()
   {
      return evictionAlgorithmClassName;
   }

   public int getMaxNodes()
   {
      return maxNodes;
   }

   public void setMaxNodes(int maxNodes)
   {
      testImmutability("maxNodes");
      this.maxNodes = maxNodes;
   }

   /**
    * @return The minimum time to live, in milliseconds.
    */
   public long getMinTimeToLive()
   {
      return minTimeToLive;
   }

   /**
    * @param minTimeToLive time to live, in milliseconds
    */
   public void setMinTimeToLive(long minTimeToLive)
   {
      testImmutability("minTimeToLive");
      this.minTimeToLive = minTimeToLive;
   }

   public void setMinTimeToLive(long time, TimeUnit timeUnit)
   {
      testImmutability("minTimeToLive");
      minTimeToLive = timeUnit.toMillis(time);
   }

   @Deprecated
   @Compat
   public void setMinTimeToLiveSeconds(long time)
   {
      setMinTimeToLive(time, TimeUnit.SECONDS);
   }

   public void validate() throws ConfigurationException
   {
      if (evictionAlgorithmClassName == null)
         throw new ConfigurationException("Eviction algorithm class name cannot be null!");
   }

   public boolean equals(Object o)
   {
      if (this == o) return true;
      if (!(o instanceof EvictionAlgorithmConfigBase)) return false;

      EvictionAlgorithmConfigBase that = (EvictionAlgorithmConfigBase) o;

      if (maxNodes != that.maxNodes) return false;
      if (minTimeToLive != that.minTimeToLive) return false;
      if (evictionAlgorithmClassName != null ? !evictionAlgorithmClassName.equals(that.evictionAlgorithmClassName) : that.evictionAlgorithmClassName != null)
         return false;

      return true;
   }

   public int hashCode()
   {
      int result;
      result = (evictionAlgorithmClassName != null ? evictionAlgorithmClassName.hashCode() : 0);
      result = 31 * result + maxNodes;
      result = (int) (31 * result + minTimeToLive);
      result = 31 * result + (int) (minTimeToLive ^ (minTimeToLive >>> 32));
      return result;
   }

   public void reset()
   {
      maxNodes = -1;
      minTimeToLive = -1;
   }

   public EvictionAlgorithmConfig clone() throws CloneNotSupportedException
   {
      return (EvictionAlgorithmConfig) super.clone();
   }
}

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

import org.jboss.cache.config.CacheLoaderConfig.IndividualCacheLoaderConfig;
import org.jboss.cache.config.Dynamic;

import java.util.Properties;

public class ClusteredCacheLoaderConfig extends IndividualCacheLoaderConfig
{
   /**
    * The serialVersionUID
    */
   private static final long serialVersionUID = -3425487656984237468L;

   @Dynamic
   private long timeout = 10000;

   public ClusteredCacheLoaderConfig()
   {
      setClassName(ClusteredCacheLoader.class.getName());
   }

   /**
    * For use by {@link ClusteredCacheLoader}.
    *
    * @param base generic config object created by XML parsing.
    */
   ClusteredCacheLoaderConfig(IndividualCacheLoaderConfig base)
   {
      setClassName(ClusteredCacheLoader.class.getName());
      populateFromBaseConfig(base);
   }

   public long getTimeout()
   {
      return timeout;
   }

   public void setTimeout(long timeout)
   {
      testImmutability("timeout");
      this.timeout = timeout;
   }

   @Override
   public void setProperties(Properties props)
   {
      super.setProperties(props);
      try
      {
         timeout = Long.valueOf(props.getProperty("timeout"));
      }
      catch (Exception e)
      {
         log.info("Using default value for config property 'timeout' - " + timeout);
      }
   }

   @Override
   public boolean equals(Object obj)
   {
      if (obj instanceof ClusteredCacheLoaderConfig && equalsExcludingProperties(obj))
      {
         ClusteredCacheLoaderConfig other = (ClusteredCacheLoaderConfig) obj;
         return (this.timeout == other.timeout);
      }
      return false;
   }

   @Override
   public int hashCode()
   {
      return 31 * hashCodeExcludingProperties() + (int) timeout;
   }

   @Override
   public ClusteredCacheLoaderConfig clone() throws CloneNotSupportedException
   {
      return (ClusteredCacheLoaderConfig) super.clone();
   }

}
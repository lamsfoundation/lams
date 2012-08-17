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
package org.jboss.cache.loader.jdbm;

import org.jboss.cache.config.CacheLoaderConfig.IndividualCacheLoaderConfig;
import org.jboss.cache.util.Util;

import java.util.Properties;

public class JdbmCacheLoaderConfig extends IndividualCacheLoaderConfig
{
   private static final long serialVersionUID = 4626734068542420865L;

   private String location;
   
   public JdbmCacheLoaderConfig()
   {
      setClassName();
   }
   
   void setClassName()
   {
      setClassName(JdbmCacheLoader.class.getName());
   }

   /**
    * For use by {@link JdbmCacheLoader}.
    *
    * @param base generic config object created by XML parsing.
    */
   JdbmCacheLoaderConfig(IndividualCacheLoaderConfig base)
   {
      setClassName();
      populateFromBaseConfig(base);
   }

   public String getLocation()
   {
      return location;
   }

   public void setLocation(String location)
   {
      testImmutability("location");
      this.location = location;
   }

   @Override
   public void setProperties(Properties props)
   {
      super.setProperties(props);
      setLocation(props != null ? props.getProperty("location") : null);
   }

   @Override
   public boolean equals(Object obj)
   {
      if (obj instanceof JdbmCacheLoaderConfig && equalsExcludingProperties(obj))
      {
         return Util.safeEquals(location, ((JdbmCacheLoaderConfig) obj).location);
      }
      return false;
   }

   @Override
   public int hashCode()
   {
      return 31 * hashCodeExcludingProperties() + (location == null ? 0 : location.hashCode());
   }

   @Override
   public JdbmCacheLoaderConfig clone() throws CloneNotSupportedException
   {
      return (JdbmCacheLoaderConfig) super.clone();
   }

}
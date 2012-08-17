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

import org.jboss.cache.Cache;
import org.jboss.cache.config.CacheLoaderConfig.IndividualCacheLoaderConfig;

public class LocalDelegatingCacheLoaderConfig extends IndividualCacheLoaderConfig
{
   private static final long serialVersionUID = 4626734068542420865L;

   private transient Cache delegate;

   public LocalDelegatingCacheLoaderConfig()
   {
      setClassName(LocalDelegatingCacheLoader.class.getName());
   }

   /**
    * For use by {@link org.jboss.cache.loader.FileCacheLoader}.
    *
    * @param base generic config object created by XML parsing.
    */
   LocalDelegatingCacheLoaderConfig(IndividualCacheLoaderConfig base)
   {
      setClassName(LocalDelegatingCacheLoader.class.getName());
      populateFromBaseConfig(base);
   }

   @Override
   public boolean equals(Object obj)
   {
      return obj instanceof LocalDelegatingCacheLoaderConfig && equalsExcludingProperties(obj) && delegate == ((LocalDelegatingCacheLoaderConfig) obj).delegate;
   }

   @Override
   public int hashCode()
   {
      return 31 * hashCodeExcludingProperties() + (delegate == null ? 0 : delegate.hashCode());
   }

   @Override
   public LocalDelegatingCacheLoaderConfig clone() throws CloneNotSupportedException
   {
      LocalDelegatingCacheLoaderConfig clone = (LocalDelegatingCacheLoaderConfig) super.clone();
      clone.delegate = delegate;
      return clone;
   }

   public Cache getDelegate()
   {
      return delegate;
   }

   public void setDelegate(Cache delegate)
   {
      this.delegate = delegate;
   }
}
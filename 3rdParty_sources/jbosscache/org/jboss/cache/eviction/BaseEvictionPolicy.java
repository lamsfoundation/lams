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

import org.jboss.cache.CacheSPI;
import org.jboss.cache.Fqn;

/**
 * Base class implementation of EvictionPolicy and TreeCacheListener.
 *
 * @author Ben Wang  2-2004
 * @author Daniel Huang - dhuang@jboss.org
 * @version $Revision$
 * @deprecated see {@link org.jboss.cache.eviction.EvictionActionPolicy}
 */
@Deprecated
public abstract class BaseEvictionPolicy implements EvictionPolicy
{
   protected CacheSPI cache_;

   /** EvictionPolicy interface implementation */

   /**
    * Evict the node under given Fqn from cache.
    *
    * @param fqn The fqn of a node in cache.
    * @throws Exception
    */
   public void evict(Fqn fqn) throws Exception
   {
      cache_.evict(fqn, false);
   }

   public void setCache(CacheSPI cache)
   {
      this.cache_ = cache;
   }

   public CacheSPI getCache()
   {
      return this.cache_;
   }

   /*
   * (non-Javadoc)
   * @see org.jboss.cache.eviction.EvictionPolicy#canIgnoreEvent(org.jboss.cache.Fqn)
   *
   */
   public boolean canIgnoreEvent(Fqn fqn, EvictionEventType eventType)
   {
      return false;
   }
}

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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jboss.cache.Cache;
import org.jboss.cache.Fqn;

/**
 * Default eviction action policy that calls {@link org.jboss.cache.Cache#evict(org.jboss.cache.Fqn)} to evict a node.
 *
 * @author Manik Surtani (<a href="mailto:manik AT jboss DOT org">manik AT jboss DOT org</a>)
 * @since 3.0
 */
public class DefaultEvictionActionPolicy implements EvictionActionPolicy
{
   Cache<?, ?> cache;
   private static final Log log = LogFactory.getLog(DefaultEvictionActionPolicy.class);

   public void setCache(Cache<?, ?> cache)
   {
      this.cache = cache;
   }

   public boolean evict(Fqn fqn)
   {
      try
      {
         if (log.isTraceEnabled()) log.trace("Evicting Fqn " + fqn);
         cache.evict(fqn);
         return true;
      }
      catch (Exception e)
      {
         if (log.isDebugEnabled()) log.debug("Unable to evict " + fqn, e);
         return false;
      }
   }
}

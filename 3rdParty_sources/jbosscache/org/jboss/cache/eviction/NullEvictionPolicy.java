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
import org.jboss.cache.CacheSPI;
import org.jboss.cache.Fqn;
import org.jboss.cache.config.EvictionPolicyConfig;

/**
 * Eviction policy that does nothing and always tells the eviction
 * interceptor an event can be ignored, saving the overhead of
 * constructing and processing event objects. Basically useful
 * as a default policy for a cache or subtree that is
 * shared between multiple usages, some of which don't
 * want eviction.
 *
 * @author Brian Stansberry
 * @deprecated see NullEvictionAlgorithm
 */
@Deprecated
public class NullEvictionPolicy implements EvictionPolicy, ModernizablePolicy
{
   private static final Log log = LogFactory.getLog(NullEvictionPolicy.class);

   private CacheSPI cache;

   /**
    * Returns <code>true</code>
    */
   public boolean canIgnoreEvent(Fqn fqn, EvictionEventType eventType)
   {
      return true;
   }

   /**
    * No-op
    */
   public void evict(Fqn fqn) throws Exception
   {
      log.debug("evict should not be called on NullEvictionPolicy");
   }

   /**
    * Returns {@link NullEvictionAlgorithm#INSTANCE}.
    */
   public EvictionAlgorithm getEvictionAlgorithm()
   {
      return NullEvictionAlgorithm.getInstance();
   }

   /**
    * Returns {@link NullEvictionPolicyConfig}.
    */
   public Class<? extends EvictionPolicyConfig> getEvictionConfigurationClass()
   {
      return NullEvictionPolicyConfig.class;
   }

   public CacheSPI getCache()
   {
      return cache;
   }

   public void setCache(CacheSPI cache)
   {
      this.cache = cache;
   }

   public Class<? extends EvictionAlgorithm> modernizePolicy()
   {
      return NullEvictionAlgorithm.class;
   }
}

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
import org.jboss.cache.RegionManager;

/**
 * Provider to provide eviction policy. This one is based on LRU algorithm that a user
 * can specify either maximum number of nodes or the idle time of a node to be evicted.
 *
 * @author Ben Wang 02-2004
 * @author Daniel Huang - dhuang@jboss.org
 * @version $Revision$
 * @deprecated see LRUAlgorithm
 */
@Deprecated
public class LRUPolicy extends BaseEvictionPolicy implements ModernizablePolicy
{
   protected RegionManager regionManager_;

   protected EvictionAlgorithm algorithm;

   public LRUPolicy()
   {
      super();
      algorithm = new LRUAlgorithm();
   }

   public EvictionAlgorithm getEvictionAlgorithm()
   {
      return algorithm;
   }

   public Class<LRUConfiguration> getEvictionConfigurationClass()
   {
      return LRUConfiguration.class;
   }

   @Override
   public void setCache(CacheSPI cache)
   {
      super.setCache(cache);
      regionManager_ = cache_.getRegionManager();
   }

   public Class<? extends EvictionAlgorithm> modernizePolicy()
   {
      return LRUAlgorithm.class;
   }
}

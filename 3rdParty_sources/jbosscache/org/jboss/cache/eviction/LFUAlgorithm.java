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
import org.jboss.cache.config.EvictionAlgorithmConfig;

/**
 * Least Frequently Used algorithm for cache eviction.
 * Note that this algorithm is not thread-safe.
 * <p/>
 * This algorithm relies on maxNodes and minNodes to operate correctly.
 * Eviction takes place using Least Frequently Used algorithm. A node A
 * that is used less than a node B is evicted sooner.
 * <p/>
 * The minNodes property defines a threshold for eviction. If minNodes = 100,
 * the LFUAlgorithm will not evict the cache to anything less than 100 elements
 * still left in cache. The maxNodes property defines the maximum number of nodes
 * the cache will accept before eviction. maxNodes = 0 means that this region is
 * unbounded. minNodes = 0 means that the eviction queue will attempt to bring
 * the cache of this region to 0 elements (evict all elements) whenever it is run.
 * <p/>
 * This algorithm uses a sorted eviction queue. The eviction queue is sorted in
 * ascending order based on the number of times a node is visited. The more frequently
 * a node is visited, the less likely it will be evicted.
 *
 * @author Daniel Huang - dhuang@jboss.org 10/2005
 * @version $Revision$
 */
public class LFUAlgorithm extends BaseSortedEvictionAlgorithm
{
   private static final Log log = LogFactory.getLog(LFUAlgorithm.class);
   private static final boolean trace = log.isTraceEnabled();

   @Override
   protected boolean shouldEvictNode(NodeEntry ne)
   {
      if (trace)
      {
         log.trace("Deciding whether node in queue " + ne.getFqn() + " requires eviction.");
      }

      // check the minimum time to live and see if we should not evict the node.  This check will
      // ensure that, if configured, nodes are kept alive for at least a minimum period of time.
      if (isYoungerThanMinimumTimeToLive(ne)) return false;

      LFUAlgorithmConfig config = (LFUAlgorithmConfig) evictionAlgorithmConfig;
      int size = this.getEvictionQueue().getNumberOfNodes();
      if (config.getMaxNodes() > -1 && size > config.getMaxNodes())
      {
         return true;
      }
      else if (size > config.getMinNodes())
      {
         return true;
      }

      return false;
   }

   /**
    * Will create a LFUQueue to be used as the underlying eviction queue.
    *
    * @return The created LFUQueue.
    * @throws EvictionException
    */
   @Override
   protected EvictionQueue setupEvictionQueue() throws EvictionException
   {
      return new LFUQueue();
   }

   @Override
   protected void prune() throws EvictionException
   {
      super.prune();

      // clean up the Queue's eviction removals
      ((LFUQueue) this.evictionQueue).prune();
   }

   public Class<? extends EvictionAlgorithmConfig> getConfigurationClass()
   {
      return LFUAlgorithmConfig.class;
   }
}

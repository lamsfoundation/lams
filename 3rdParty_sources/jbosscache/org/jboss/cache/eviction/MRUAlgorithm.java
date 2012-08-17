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

import org.jboss.cache.config.EvictionAlgorithmConfig;

/**
 * Most Recently Used Algorithm.
 * <p/>
 * This algorithm will evict the most recently used cache entries from cache.
 * <p/>
 * Note: None of the Eviction classes are thread safe. It is assumed that an individual instance of an EvictionPolicy/
 * EvictionAlgorithm/EvictionQueue/EvictionConfiguration are only operated on by one thread at any given time.
 *
 * @author Daniel Huang (dhuang@jboss.org)
 * @version $Revision$
 */
public class MRUAlgorithm extends BaseEvictionAlgorithm
{
   @Override
   protected EvictionQueue setupEvictionQueue() throws EvictionException
   {
      return new MRUQueue();
   }

   @Override
   protected boolean shouldEvictNode(NodeEntry ne)
   {
      // check the minimum time to live and see if we should not evict the node.  This check will
      // ensure that, if configured, nodes are kept alive for at least a minimum period of time.
      if (isYoungerThanMinimumTimeToLive(ne)) return false;

      MRUAlgorithmConfig config = (MRUAlgorithmConfig) evictionAlgorithmConfig;
      return evictionQueue.getNumberOfNodes() > config.getMaxNodes();
   }

   @Override
   protected void processVisitedNodes(EvictionEvent evictedEventNode) throws EvictionException
   {
      super.processVisitedNodes(evictedEventNode);
      ((MRUQueue) evictionQueue).moveToTopOfStack(evictedEventNode.getFqn());
   }

   public Class<? extends EvictionAlgorithmConfig> getConfigurationClass()
   {
      return MRUAlgorithmConfig.class;
   }
}

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
 * First-in-first-out algorithm used to evict nodes.
 *
 * @author Daniel Huang - dhuang@jboss.org
 * @author Morten Kvistgaard
 * @version $Revision$
 */
public class FIFOAlgorithm extends BaseEvictionAlgorithm
{
   private static final Log log = LogFactory.getLog(FIFOAlgorithm.class);
   private static final boolean trace = log.isTraceEnabled();


   @Override
   protected EvictionQueue setupEvictionQueue() throws EvictionException
   {
      return new FIFOQueue();
   }

   /**
    * For FIFO, a node should be evicted if the queue size is >= to the configured maxNodes size.
    */
   @Override
   protected boolean shouldEvictNode(NodeEntry ne)
   {
      // check the minimum time to live and see if we should not evict the node.  This check will
      // ensure that, if configured, nodes are kept alive for at least a minimum period of time.
      if (isYoungerThanMinimumTimeToLive(ne)) return false;

      FIFOAlgorithmConfig config = (FIFOAlgorithmConfig) evictionAlgorithmConfig;
      if (trace)
      {
         log.trace("Deciding whether node in queue " + ne.getFqn() + " requires eviction.");
      }

      int size = this.getEvictionQueue().getNumberOfNodes();
      return config.getMaxNodes() != 0 && size > config.getMaxNodes();
   }

   public Class<? extends EvictionAlgorithmConfig> getConfigurationClass()
   {
      return FIFOAlgorithmConfig.class;
   }
}


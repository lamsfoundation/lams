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

import java.util.Iterator;

/**
 * Least recently Used algorithm to purge old data.
 * Note that this algorithm is not thread-safe.
 *
 * @author Ben Wang 02-2004
 * @author Daniel Huang - dhuang@jboss.org
 */
public class LRUAlgorithm extends BaseEvictionAlgorithm
{
   private static final Log log = LogFactory.getLog(LRUAlgorithm.class);
   private static final boolean trace = log.isTraceEnabled();

   @Override
   protected EvictionQueue setupEvictionQueue() throws EvictionException
   {
      return new LRUQueue();
   }

   @Override
   protected boolean shouldEvictNode(NodeEntry entry)
   {
      LRUAlgorithmConfig config = (LRUAlgorithmConfig) evictionAlgorithmConfig;
      // check the minimum time to live and see if we should not evict the node.  This check will
      // ensure that, if configured, nodes are kept alive for at least a minimum period of time.
      if (isYoungerThanMinimumTimeToLive(entry))
      {
         if (trace) log.trace("Do not evict - is younger than minimum TTL");
         return false;
      }


      // no idle or max time limit
      if (config.getTimeToLive() < 0 && config.getMaxAge() < 0)
      {
         log.trace("No idle or max time limit!");
         return false;
      }

      long currentTime = System.currentTimeMillis();
      if (config.getTimeToLive() > -1)
      {
         long idleTime = currentTime - entry.getModifiedTimeStamp();
         if (trace)
         {
            log.trace("Node " + entry.getFqn() + " has been idle for " + idleTime + "ms");
         }
         if ((idleTime >= (config.getTimeToLive())))
         {
            if (trace)
            {
               log.trace("Node " + entry.getFqn() + " should be evicted because of idle time");
               log.trace("Time to live in millies is: " + (config.getTimeToLive()));
               log.trace("Config instance is: " + System.identityHashCode(config));
            }
            return true;
         }
      }

      if (config.getMaxAge() > -1)
      {
         long objectLifeTime = currentTime - entry.getCreationTimeStamp();
         if (trace)
         {
            log.trace("Node " + entry.getFqn() + " has been alive for " + objectLifeTime + "ms");
         }
         if ((objectLifeTime >= config.getMaxAge()))
         {
            if (trace)
            {
               log.trace("Node " + entry.getFqn() + " should be evicted because of max age");
            }
            return true;
         }
      }

      if (trace)
      {
         log.trace("Node " + entry.getFqn() + " should not be evicted");
      }
      return false;
   }

   @Override
   protected void evict(NodeEntry ne)
   {
      if (trace)
      {
         if (ne == null) log.trace("Got a NULL node entry!");
         else log.trace("About to evict " + ne.getFqn());
      }

      if (ne != null && !this.evictCacheNode(ne.getFqn()))
      {
         try
         {
            if (trace) log.trace("Could not evict " + ne.getFqn() + " so adding to recycle queue");
            recycleQueue.put(ne.getFqn());
         }
         catch (InterruptedException e)
         {
            log.debug("InterruptedException", e);
         }
      }
   }

   @Override
   protected void prune() throws EvictionException
   {
      LRUQueue lruQueue = (LRUQueue) evictionQueue;
      NodeEntry ne;
      Iterator it = lruQueue.iterateLRUQueue();
      while (it.hasNext())
      {
         ne = (NodeEntry) it.next();
         if (isNodeInUseAndNotTimedOut(ne))
         {
            continue;
         }

         if (this.shouldEvictNode(ne))
         {
            it.remove();
            lruQueue.removeNodeEntryFromMaxAge(ne);
            this.evict(ne);
         }
         else
         {
            break;
         }
      }

      it = lruQueue.iterateMaxAgeQueue();
      while (it.hasNext())
      {
         ne = (NodeEntry) it.next();
         if (isNodeInUseAndNotTimedOut(ne))
         {
            continue;
         }

         if (this.shouldEvictNode(ne))
         {
            it.remove();
            lruQueue.removeNodeEntryFromLRU(ne);
            this.evict(ne);
         }
         else
         {
            break;
         }
      }

      int maxNodes = ((LRUAlgorithmConfig) evictionAlgorithmConfig).getMaxNodes();
      if (maxNodes <= 0)
      {
         return;
      }

      it = lruQueue.iterateLRUQueue();
      while (evictionQueue.getNumberOfNodes() > maxNodes)
      {
         ne = (NodeEntry) it.next();
         if (trace)
         {
            log.trace("Node " + ne.getFqn() + " will be evicted because of exceeding the maxNode limit." +
                  " maxNode: " + maxNodes + " but current queue size is: " + evictionQueue.getNumberOfNodes());
         }

         if (!this.isNodeInUseAndNotTimedOut(ne))
         {
            it.remove();
            lruQueue.removeNodeEntryFromMaxAge(ne);
            this.evict(ne);
         }
      }
   }

   public Class<? extends EvictionAlgorithmConfig> getConfigurationClass()
   {
      return LRUAlgorithmConfig.class;
   }
}


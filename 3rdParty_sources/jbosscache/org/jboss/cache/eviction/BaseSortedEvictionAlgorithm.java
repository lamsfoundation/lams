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

import java.util.concurrent.BlockingQueue;


/**
 * An abstract SortedEvictionAlgorithm.
 * <p/>
 * This class supports early termination of the eviction queue processing. Because the eviction
 * queue is sorted by first to evict to last to evict, when iterating the eviction queue, the first time
 * a node is encountered that does not require eviction will terminate the loop early. This way we don't incur
 * the full breadth of the O(n) = n operation everytime we need to check for eviction (defined by eviction poll time
 * interval).
 *
 * @author Daniel Huang - dhuang@jboss.org - 10/2005
 */
public abstract class BaseSortedEvictionAlgorithm extends BaseEvictionAlgorithm
{
   private static final Log log = LogFactory.getLog(BaseSortedEvictionAlgorithm.class);
   private static final boolean trace = log.isTraceEnabled();

   @Override
   protected void processQueues(BlockingQueue<EvictionEvent> queue) throws EvictionException
   {
      boolean evictionNodesModified = false;

      EvictionEvent node;
      int count = 0;
      while ((node = getNextInQueue(queue)) != null)
      {
         count++;
         switch (node.getEventType())
         {
            case ADD_NODE_EVENT:
               this.processAddedNodes(node);
               evictionNodesModified = true;
               break;
            case REMOVE_NODE_EVENT:
               this.processRemovedNodes(node);
               break;
            case VISIT_NODE_EVENT:
               this.processVisitedNodes(node);
               evictionNodesModified = true;
               break;
            case ADD_ELEMENT_EVENT:
               this.processAddedElement(node);
               evictionNodesModified = true;
               break;
            case REMOVE_ELEMENT_EVENT:
               this.processRemovedElement(node);
               evictionNodesModified = true;
               break;
            default:
               throw new RuntimeException("Illegal Eviction Event type " + node.getEventType());
         }
      }

      if (trace)
      {
         log.trace("Eviction nodes visited or added requires resort of queue " + evictionNodesModified);
      }

      this.resortEvictionQueue(evictionNodesModified);


      if (trace)
      {
         log.trace("processed " + count + " node events");
      }

   }

   /**
    * This method is called to resort the queue after add or visit events have occurred.
    * <p/>
    * If the parameter is true, the queue needs to be resorted. If it is false, the queue does not
    * need resorting.
    *
    * @param evictionQueueModified True if the queue was added to or visisted during event processing.
    */
   protected void resortEvictionQueue(boolean evictionQueueModified)
   {
      if (!evictionQueueModified)
      {
         if (log.isDebugEnabled())
         {
            log.debug("Eviction queue not modified. Resort unnecessary.");
         }
         return;
      }
      long begin = System.currentTimeMillis();
      ((SortedEvictionQueue) evictionQueue).resortEvictionQueue();
      long end = System.currentTimeMillis();

      if (trace)
      {
         long diff = end - begin;
         log.trace("Took " + diff + "ms to sort queue with " + getEvictionQueue().getNumberOfNodes() + " elements");
      }
   }

}

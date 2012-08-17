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
import org.jboss.cache.config.Configuration;
import org.jboss.cache.config.Configuration.NodeLockingScheme;
import org.jboss.cache.config.EvictionAlgorithmConfig;
import org.jboss.cache.eviction.EvictionEvent.Type;
import org.jboss.cache.lock.TimeoutException;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 * Abstract Event Processing Eviction Algorithm.
 * This class is used to implement basic event processing for Eviction Algorithms.
 * To extend this abstract class to make an Eviction Algorithm, implement the
 * abstract methods and a policy.
 *
 * @author Daniel Huang - dhuang@jboss.org 10/2005
 * @author <a href="mailto:galder.zamarreno@jboss.com">Galder Zamarreno</a>
 * @version $Revision$
 */
public abstract class BaseEvictionAlgorithm implements EvictionAlgorithm
{
   private static final Log log = LogFactory.getLog(BaseEvictionAlgorithm.class);
   private static final boolean trace = log.isTraceEnabled();
   protected EvictionActionPolicy evictionActionPolicy;
   protected EvictionAlgorithmConfig evictionAlgorithmConfig;
   /**
    * Contains Fqn instances.
    */
   protected BlockingQueue<Fqn> recycleQueue;
   /**
    * Contains NodeEntry instances.
    */
   protected EvictionQueue evictionQueue;
   protected boolean allowTombstones = false;
   protected Configuration configuration;
   protected Fqn regionFqn;
   protected CacheSPI<?, ?> cache;

   /**
    * This method will create an EvictionQueue implementation and prepare it for use.
    *
    * @return The created EvictionQueue to be used as the eviction queue for this algorithm.
    * @throws EvictionException if there are problems
    * @see EvictionQueue
    */
   protected abstract EvictionQueue setupEvictionQueue() throws EvictionException;

   /**
    * This method will check whether the given node should be evicted or not.
    *
    * @param ne NodeEntry to test eviction for.
    * @return True if the given node should be evicted. False if the given node should not be evicted.
    */
   protected abstract boolean shouldEvictNode(NodeEntry ne);

   protected BaseEvictionAlgorithm()
   {
      recycleQueue = new LinkedBlockingQueue<Fqn>(500000);
   }

   public synchronized void initialize()
   {
      if (evictionQueue == null)
      {
         evictionQueue = setupEvictionQueue();
         if (log.isDebugEnabled()) log.debug("Initialized: " + this);
         Configuration.CacheMode cm = configuration != null ? configuration.getCacheMode() : Configuration.CacheMode.LOCAL;
         allowTombstones = configuration != null && configuration.getNodeLockingScheme() == NodeLockingScheme.OPTIMISTIC &&
               (cm == Configuration.CacheMode.INVALIDATION_ASYNC || cm == Configuration.CacheMode.INVALIDATION_SYNC);
      }
   }

   public EvictionActionPolicy getEvictionActionPolicy()
   {
      return evictionActionPolicy;
   }

   public void setEvictionActionPolicy(EvictionActionPolicy evictionActionPolicy)
   {
      this.evictionActionPolicy = evictionActionPolicy;
   }

   public EvictionAlgorithmConfig getEvictionAlgorithmConfig()
   {
      return evictionAlgorithmConfig;
   }

   public void assignToRegion(Fqn fqn, CacheSPI<?, ?> cache, EvictionAlgorithmConfig evictionAlgorithmConfig, Configuration configuration)
   {
      if (log.isTraceEnabled()) log.trace(getClass().getSimpleName() + " instantiated and assigned to region " + fqn + " with cfg " + evictionAlgorithmConfig);
      this.regionFqn = fqn;
      this.cache = cache;
      this.evictionAlgorithmConfig = evictionAlgorithmConfig;
      this.configuration = configuration;
   }

   public boolean canIgnoreEvent(Type eventType)
   {
      return false; // don't ignore anything!
   }

   /**
    * Process the given eviction event queue.  Eviction Processing encompasses the following:
    * <p/>
    * - Add/Remove/Visit Nodes
    * - Prune according to Eviction Algorithm
    * - Empty/Retry the recycle queue of previously evicted but locked (during actual cache eviction) nodes.
    *
    * @param eventQueue queue containing eviction events
    * @throws EvictionException
    */
   public void process(BlockingQueue<EvictionEvent> eventQueue) throws EvictionException
   {
      if (trace) log.trace("process(): region: " + regionFqn);
      initialize();
      this.processQueues(eventQueue);
      this.emptyRecycleQueue();
      this.prune();
   }

   public void resetEvictionQueue()
   {
      // a no-op
   }

   /**
    * Get the underlying EvictionQueue implementation.
    *
    * @return the EvictionQueue used by this algorithm
    * @see EvictionQueue
    */
   public EvictionQueue getEvictionQueue()
   {
      return this.evictionQueue;
   }

   protected EvictionEvent getNextInQueue(BlockingQueue<EvictionEvent> queue)
   {
      try
      {
         return queue.poll(0, TimeUnit.SECONDS);
      }
      catch (InterruptedException e)
      {
         Thread.currentThread().interrupt();
      }
      return null;
   }

   /**
    * Event processing for Evict/Add/Visiting of nodes.
    * <p/>
    * - On AddEvents a new element is added into the eviction queue
    * - On RemoveEvents, the removed element is removed from the eviction queue.
    * - On VisitEvents, the visited node has its eviction statistics updated (idleTime, numberOfNodeVisists, etc..)
    *
    * @param queue queue to inspect
    * @throws EvictionException in the event of problems
    */
   protected void processQueues(BlockingQueue<EvictionEvent> queue) throws EvictionException
   {
      EvictionEvent node;
      int count = 0;
      while ((node = getNextInQueue(queue)) != null)
      {
         count++;
         switch (node.getEventType())
         {
            case ADD_NODE_EVENT:
               this.processAddedNodes(node);
               break;
            case REMOVE_NODE_EVENT:
               this.processRemovedNodes(node);
               break;
            case VISIT_NODE_EVENT:
               this.processVisitedNodes(node);
               break;
            case ADD_ELEMENT_EVENT:
               this.processAddedElement(node);
               break;
            case REMOVE_ELEMENT_EVENT:
               this.processRemovedElement(node);
               break;
            case MARK_IN_USE_EVENT:
               this.processMarkInUseNodes(node.getFqn(), node.getInUseTimeout());
               break;
            case UNMARK_USE_EVENT:
               this.processUnmarkInUseNodes(node.getFqn());
               break;
            default:
               throw new RuntimeException("Illegal Eviction Event type " + node.getEventType());
         }
      }

      if (trace) log.trace("processed " + count + " node events");
   }

   protected void evict(NodeEntry ne)
   {
//      NodeEntry ne = evictionQueue.getNodeEntry(fqn);
      if (ne != null)
      {
         evictionQueue.removeNodeEntry(ne);
         if (!this.evictCacheNode(ne.getFqn()))
         {
            try
            {
               boolean result = recycleQueue.offer(ne.getFqn(), 5, TimeUnit.SECONDS);
               if (!result)
               {
                  log.warn("Unable to add Fqn[" + ne.getFqn() + "] to recycle " +
                        "queue because it's full. This is often sign that " +
                        "evictions are not occurring and nodes that should be " +
                        "evicted are piling up waiting to be evicted.");
               }
            }
            catch (InterruptedException e)
            {
               log.debug("InterruptedException", e);
            }
         }
      }
   }

   /**
    * Evict a node from cache.
    *
    * @param fqn node corresponds to this fqn
    * @return True if successful
    */
   protected boolean evictCacheNode(Fqn fqn)
   {
      if (trace) log.trace("Attempting to evict cache node with fqn of " + fqn);

      try
      {
         evictionActionPolicy.evict(fqn);
      }
      catch (TimeoutException e)
      {
         log.warn("Eviction of " + fqn + " timed out, retrying later");
         log.debug(e, e);
         return false;
      }
      catch (Exception e)
      {
         log.error("Eviction of " + fqn + " failed", e);
         return false;
      }

      if (trace)
      {
         log.trace("Eviction of cache node with fqn of " + fqn + " successful");
      }

      return true;
   }

   protected void processMarkInUseNodes(Fqn fqn, long inUseTimeout) throws EvictionException
   {
      if (trace)
      {
         log.trace("Marking node " + fqn + " as in use with a usage timeout of " + inUseTimeout);
      }

      NodeEntry ne = evictionQueue.getNodeEntry(fqn);
      if (ne != null)
      {
         ne.setCurrentlyInUse(true, inUseTimeout);
      }
   }

   protected void processUnmarkInUseNodes(Fqn fqn) throws EvictionException
   {
      if (trace)
      {
         log.trace("Unmarking node " + fqn + " as in use");
      }

      NodeEntry ne = evictionQueue.getNodeEntry(fqn);
      if (ne != null)
      {
         ne.setCurrentlyInUse(false, 0);
      }
   }

   /**
    * Convenience method, which calls {@link #processAddedNodes(EvictionEvent, int)}  using values in the
    * evictedEventNode for number of added elements and the resetElementCount flag.
    *
    * @param evictedEventNode an evictedEventNode to process
    * @throws EvictionException on problems
    */
   protected void processAddedNodes(EvictionEvent evictedEventNode) throws EvictionException
   {
      processAddedNodes(evictedEventNode, evictedEventNode.getElementDifference());
   }

   protected void processAddedNodes(EvictionEvent evictedEventNode, int numAddedElements) throws EvictionException
   {
      Fqn fqn = evictedEventNode.getFqn();

      if (trace)
      {
         log.trace("Adding node " + fqn + " with " + numAddedElements + " elements to eviction queue");
      }

      NodeEntry ne = evictionQueue.getNodeEntry(fqn);
      if (ne != null)
      {
         ne.setModifiedTimeStamp(evictedEventNode.getCreationTimestamp());
         ne.setNumberOfNodeVisits(ne.getNumberOfNodeVisits() + 1);
         ne.setNumberOfElements(ne.getNumberOfElements() + numAddedElements);
         if (trace)
         {
            log.trace("Queue already contains " + ne.getFqn() + " processing it as visited");
         }
         processVisitedNodes(evictedEventNode);
         return;
      }

      ne = new NodeEntry(fqn);
      ne.setModifiedTimeStamp(evictedEventNode.getCreationTimestamp());
      ne.setNumberOfNodeVisits(1);
      ne.setNumberOfElements(numAddedElements);

      evictionQueue.addNodeEntry(ne);

      if (trace)
      {
         log.trace(ne.getFqn() + " added successfully to eviction queue");
      }
   }

   /**
    * Remove a node from cache.
    * <p/>
    * This method will remove the node from the eviction queue as well as
    * evict the node from cache.
    * <p/>
    * If a node cannot be removed from cache, this method will remove it from the eviction queue
    * and place the element into the recycleQueue. Each node in the recycle queue will get retried until
    * proper cache eviction has taken place.
    * <p/>
    * Because EvictionQueues are collections, when iterating them from an iterator, use iterator.remove()
    * to avoid ConcurrentModificationExceptions. Use the boolean parameter to indicate the calling context.
    *
    * @throws EvictionException
    */
   protected void processRemovedNodes(EvictionEvent evictedEventNode) throws EvictionException
   {
      Fqn fqn = evictedEventNode.getFqn();

      if (trace)
      {
         log.trace("Removing node " + fqn + " from eviction queue and attempting eviction");
      }

      NodeEntry ne = evictionQueue.getNodeEntry(fqn);
      if (ne != null)
      {
         if (allowTombstones)
         {
            // don't remove from the queue - deleting a node results in a tombstone which means the nodes
            // still need to be considered for eviction!
            return;
         }
         else
         {
            // a removeNode operation will simply remove the node.  Nothing to worry about.
            evictionQueue.removeNodeEntry(ne);
         }
      }
      else
      {
         if (trace)
         {
            log.trace("processRemoveNodes(): Can't find node associated with fqn: " + fqn
                  + "Could have been evicted earlier. Will just continue.");
         }
         return;
      }

      if (trace)
      {
         log.trace(fqn + " removed from eviction queue");
      }
   }

   /**
    * Visit a node in cache.
    * <p/>
    * This method will update the numVisits and modifiedTimestamp properties of the Node.
    * These properties are used as statistics to determine eviction (LRU, LFU, MRU, etc..)
    * <p/>
    * *Note* that this method updates Node Entries by reference and does not put them back
    * into the queue. For some sorted collections, a remove, and a re-add is required to
    * maintain the sorted order of the elements.
    *
    * @throws EvictionException
    */
   protected void processVisitedNodes(EvictionEvent evictedEventNode) throws EvictionException
   {
      Fqn fqn = evictedEventNode.getFqn();
      NodeEntry ne = evictionQueue.getNodeEntry(fqn);
      if (ne == null)
      {
         if (log.isDebugEnabled())
         {
            log.debug("Visiting node that was not added to eviction queues. Assuming that it has 1 element.");
         }
         this.processAddedNodes(evictedEventNode, 1);
         return;
      }
      // note this method will visit and modify the node statistics by reference!
      // if a collection is only guaranteed sort order by adding to the collection,
      // this implementation will not guarantee sort order.
      ne.setNumberOfNodeVisits(ne.getNumberOfNodeVisits() + 1);
      ne.setModifiedTimeStamp(evictedEventNode.getCreationTimestamp());
   }

   protected void processRemovedElement(EvictionEvent evictedEventNode) throws EvictionException
   {
      Fqn fqn = evictedEventNode.getFqn();
      NodeEntry ne = evictionQueue.getNodeEntry(fqn);

      if (ne == null)
      {
         if (log.isDebugEnabled())
         {
            log.debug("Removing element from " + fqn + " but eviction queue does not contain this node. " +
                  "Ignoring removeElement event.");
         }
         return;
      }

      ne.setNumberOfElements(ne.getNumberOfElements() - 1);
      // also treat it as a node visit.
      ne.setNumberOfNodeVisits(ne.getNumberOfNodeVisits() + 1);
      ne.setModifiedTimeStamp(evictedEventNode.getCreationTimestamp());
   }

   protected void processAddedElement(EvictionEvent evictedEventNode) throws EvictionException
   {
      Fqn fqn = evictedEventNode.getFqn();
      NodeEntry ne = evictionQueue.getNodeEntry(fqn);
      if (ne == null)
      {
         if (trace)
         {
            log.trace("Adding element " + fqn + " for a node that doesn't exist yet. Process as an add.");
         }
         this.processAddedNodes(evictedEventNode, 1);
         return;
      }

      ne.setNumberOfElements(ne.getNumberOfElements() + 1);

      // also treat it as a node visit.
      ne.setNumberOfNodeVisits(ne.getNumberOfNodeVisits() + 1);
      ne.setModifiedTimeStamp(evictedEventNode.getCreationTimestamp());
//      log.error ("*** Processing nodeAdded for fqn " + fqn + " NodeEntry's hashcode is " + ne.hashCode());
   }


   /**
    * Empty the Recycle Queue.
    * <p/>
    * This method will go through the recycle queue and retry to evict the nodes from cache.
    *
    * @throws EvictionException
    */
   protected void emptyRecycleQueue() throws EvictionException
   {
      while (true)
      {
         Fqn fqn;

         try
         {
            //fqn = (Fqn) recycleQueue.take();
            fqn = recycleQueue.poll(0, TimeUnit.SECONDS);
         }
         catch (InterruptedException e)
         {
            log.debug(e, e);
            break;
         }

         if (fqn == null)
         {
            if (trace)
            {
               log.trace("Recycle queue is empty");
            }
            break;
         }

         if (trace)
         {
            log.trace("emptying recycle bin. Evict node " + fqn);
         }

         // Still doesn't work
         if (!evictCacheNode(fqn))
         {
            try
            {
               recycleQueue.put(fqn);
            }
            catch (InterruptedException e)
            {
               log.debug(e, e);
            }
            break;
         }
      }
   }

   protected boolean isNodeInUseAndNotTimedOut(NodeEntry ne)
   {
      if (ne.isCurrentlyInUse())
      {
         if (ne.getInUseTimeoutTimestamp() == 0)
         {
            return true;
         }

         if (System.currentTimeMillis() < ne.getInUseTimeoutTimestamp())
         {
            return true;
         }
      }
      return false;
   }

   protected void prune() throws EvictionException
   {
      NodeEntry entry;
      while ((entry = evictionQueue.getFirstNodeEntry()) != null)
      {
         if (this.shouldEvictNode(entry))
         {
            this.evict(entry);
         }
         else
         {
            break;
         }
      }
   }

   /**
    * Returns debug information.
    */
   @Override
   public String toString()
   {
      return super.toString() +
            " recycle=" + recycleQueue.size() +
            " evict=" + evictionQueue.getNumberOfNodes();
   }

   /**
    * Tests whether a node entry is younger than the minimum time to live - if one is configured.
    *
    * @param entry the node entry being examined
    * @return true if the node is younger than - or exactly equal to - the minimum time to live, if one is configured for the given region.  False otherwise.
    */
   protected boolean isYoungerThanMinimumTimeToLive(NodeEntry entry)
   {
      if (evictionAlgorithmConfig instanceof EvictionAlgorithmConfigBase)
      {
         EvictionAlgorithmConfigBase cfg = (EvictionAlgorithmConfigBase) evictionAlgorithmConfig;
         long minTTL = cfg.getMinTimeToLive();
         return minTTL >= 1 && (entry.getModifiedTimeStamp() + minTTL > System.currentTimeMillis());
      }
      else
      {
         log.trace("Eviction policy implementation does not support minimum TTL!");
         return false;
      }
   }
}

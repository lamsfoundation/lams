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
import org.jboss.cache.Fqn;
import org.jboss.cache.NodeSPI;
import org.jboss.cache.config.EvictionAlgorithmConfig;
import org.jboss.cache.eviction.EvictionEvent.Type;

import java.util.Iterator;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.concurrent.BlockingQueue;

/**
 * Eviction algorithm that uses a key in the Node data that indicates the time
 * the node should be evicted.  The key must be a java.lang.Long object, with
 * the time to expire as milliseconds past midnight January 1st, 1970 UTC (the
 * same relative time as provided by {@link
 * java.lang.System#currentTimeMillis()}).
 * <p/>
 * This algorithm also obeys the configuration key {@link
 * ExpirationAlgorithmConfig#getMaxNodes()}, and will evict the soonest to
 * expire entires first to reduce the region size.  If there are not enough
 * nodes with expiration keys set, a warning is logged.
 * <p/>
 * If a node in the eviction region does not have an expiration value, then
 * {@link ExpirationAlgorithmConfig#getTimeToLive()} (if set) will be used.
 * The expiration is updated when a node is added or updated.
 * <p/>
 * If there is no time-to-live set, and a node in the eviction region does not
 * have an expiration value, then that node will never be evicted.  As
 * forgetting to indicate an expiration value is likely a mistake, a warning
 * message is logged by this class. This warning, however, can be disabled
 * through {@link ExpirationAlgorithmConfig#setWarnNoExpirationKey(boolean)}.
 * <p/>
 * A node's expiration time can be changed by setting a new value in the node.
 * <p/>
 * Example usage:
 * <pre>
 * Cache cache;
 * Fqn fqn1 = Fqn.fromString("/node/1");
 * Long future = new Long(System.currentTimeMillis() + 2000);
 * cache.put(fqn1, ExpirationConfiguration.EXPIRATION_KEY, future);
 * cache.put(fqn1, "foo");
 * assertTrue(cache.get(fqn1) != null);
 * <p/>
 * Thread.sleep(5000); // 5 seconds
 * assertTrue(cache.get(fqn1) == null);
 * <p/>
 * </pre>
 */
public class ExpirationAlgorithm extends BaseEvictionAlgorithm
{

   private static final Log log = LogFactory.getLog(ExpirationAlgorithm.class);
   private static final boolean trace = log.isTraceEnabled();

   private ExpirationAlgorithmConfig config;

   private SortedSet<ExpirationEntry> set;

   /**
    * Constructs a new algorithm with a policy.
    */
   public ExpirationAlgorithm()
   {
      this.set = new TreeSet<ExpirationEntry>();
   }

   private void addEvictionEntry(EvictionEvent node)
   {
      Fqn fqn = node.getFqn();
      addEvictionEntry(fqn);
   }

   private void addEvictionEntry(Fqn fqn)
   {
      Long l = getExpiration(fqn);
      if (l == null)
      {
         if (config.isWarnNoExpirationKey() && log.isWarnEnabled())
            log.warn("No expiration key '" + config.getExpirationKeyName() + "' for Node: " + fqn);
         else if (log.isDebugEnabled())
            log.debug("No expiration key for Node: " + fqn);
      }
      else
      {
         setExpiration(fqn, l);
      }
   }

   private void setExpiration(Fqn fqn, Long l)
   {
      ExpirationEntry ee = new ExpirationEntry(fqn, l);
      if (trace)
         log.trace("adding eviction entry: " + ee);
      set.add(ee);
   }

   @SuppressWarnings("unchecked")
   private Long getExpiration(Fqn fqn)
   {
      NodeSPI n = cache.peek(fqn, false);
      if (n == null)
         return null;
      return (Long) n.getDirect(config.getExpirationKeyName());
   }

   @Override
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
            case ADD_ELEMENT_EVENT:
               addEvictionEntry(node);
               break;
            case REMOVE_ELEMENT_EVENT:
            case REMOVE_NODE_EVENT:
            case UNMARK_USE_EVENT:
               // Removals will be noticed when double-checking expiry time
               // removeEvictionEntry(node);
               break;
            case VISIT_NODE_EVENT:
               // unused
               break;
            case MARK_IN_USE_EVENT:
               markInUse(node);
               break;
            default:
               throw new RuntimeException("Illegal Eviction Event type " + node.getEventType());
         }
      }

      if (trace) log.trace("processed " + count + " node events in region: " + regionFqn);
   }

   private void markInUse(EvictionEvent node)
   {
      long expiration = node.getInUseTimeout() + System.currentTimeMillis();
      setExpiration(node.getFqn(), expiration);
   }

   @Override
   protected void prune() throws EvictionException
   {
      if (set.isEmpty())
         return;
      long now = System.currentTimeMillis();
      int max = config.getMaxNodes();
      for (Iterator<ExpirationEntry> i = set.iterator(); i.hasNext();)
      {
         ExpirationEntry ee = i.next();
         Long ce = getExpiration(ee.getFqn());
         if (ce == null || ce > ee.getExpiration())
         {
            // Expiration now older
            i.remove();
            continue;
         }
         if (ee.getExpiration() < now || (max != -1 && set.size() > max))
         {
            i.remove();
            evictCacheNode(ee.getFqn());
         }
         else
         {
            break;
         }
      }
      if (max != 0 && max > set.size())
         log.warn("Unable to remove nodes to reduce region size below " +
               config.getMaxNodes() + ".  " +
               "Set expiration for nodes in this region");
   }

   @Override
   public void resetEvictionQueue()
   {
      for (ExpirationEntry ee : set)
      {
         addEvictionEntry(ee.getFqn());
      }
   }

   @Override
   protected EvictionQueue setupEvictionQueue() throws EvictionException
   {
      this.config = (ExpirationAlgorithmConfig) evictionAlgorithmConfig;
      return new DummyEvictionQueue();
   }

   @Override
   protected boolean shouldEvictNode(NodeEntry ne)
   {
      throw new UnsupportedOperationException();
   }

   @Override
   public boolean canIgnoreEvent(Type eventType)
   {
      return (eventType == EvictionEvent.Type.VISIT_NODE_EVENT);
   }

   public Class<? extends EvictionAlgorithmConfig> getConfigurationClass()
   {
      return ExpirationAlgorithmConfig.class;
   }

   /**
    * Ordered list of FQN, with the expiration taken from the Map at the time
    * of processing.
    */
   static class ExpirationEntry implements Comparable<ExpirationEntry>
   {

      private long expiration;

      private Fqn fqn;

      public ExpirationEntry(Fqn fqn)
      {
         this.fqn = fqn;
      }

      public ExpirationEntry(Fqn fqn, long expiration)
      {
         this.fqn = fqn;
         this.expiration = expiration;
      }

      /**
       * Compares expiration, then FQN order.
       */
      public int compareTo(ExpirationEntry ee)
      {
         long n = expiration - ee.expiration;
         if (n < 0)
            return -1;
         if (n > 0)
            return 1;
         return fqn.compareTo(ee.fqn);
      }

      /**
       * @return the expiration
       */
      public long getExpiration()
      {
         return expiration;
      }

      /**
       * @return the fqn
       */
      public Fqn getFqn()
      {
         return fqn;
      }

      @Override
      public boolean equals(Object o)
      {
         if (!(o instanceof ExpirationEntry))
            return false;
         ExpirationEntry ee = (ExpirationEntry) o;
         return expiration == ee.expiration && fqn.equals(ee.fqn);
      }

      @Override
      public int hashCode()
      {
         return (int) expiration ^ fqn.hashCode();
      }

      @Override
      public String toString()
      {
         long now = System.currentTimeMillis();
         long ttl = expiration - now;
         String sttl;
         if (ttl > 1000 * 60)
            sttl = (ttl / (1000 * 60)) + "min";
         else if (ttl > 1000)
            sttl = (ttl / 1000) + "s";
         else
            sttl = ttl + "ms";
         return "EE fqn=" + fqn + " ttl=" + sttl;
      }
   }

   class DummyEvictionQueue implements EvictionQueue
   {

      public void addNodeEntry(NodeEntry entry)
      {
         throw new UnsupportedOperationException();
      }

      public void clear()
      {
         set.clear();
      }

      public boolean containsNodeEntry(NodeEntry entry)
      {
         return false;
      }

      public NodeEntry getFirstNodeEntry()
      {
         return null;
      }

      public NodeEntry getNodeEntry(Fqn fqn)
      {
         return null;
      }

      public NodeEntry getNodeEntry(String fqn)
      {
         return null;
      }

      public int getNumberOfElements()
      {
         return set.size();
      }

      public int getNumberOfNodes()
      {
         return set.size();
      }

      public Iterator<NodeEntry> iterator()
      {
         return null;
      }

      public void modifyElementCount(int difference)
      {
      }

      public void removeNodeEntry(NodeEntry entry)
      {
         throw new UnsupportedOperationException();
      }
   }

}

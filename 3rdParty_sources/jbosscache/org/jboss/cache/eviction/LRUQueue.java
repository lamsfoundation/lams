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

import org.jboss.cache.Fqn;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.logging.Log;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * LRU Eviction Queue implementation.
 * <p/>
 * This eviction queue will iterate properly through two sorted lists.
 * One sorted by maxAge and the other sorted by idleTime.
 *
 * @author Daniel Huang (dhuang@jboss.org)
 * @version $Revision$
 */
public class LRUQueue implements EvictionQueue
{

   private static Log log = LogFactory.getLog(LRUQueue.class);
   private Map<Fqn, NodeEntry> maxAgeQueue;
   private Map<Fqn, NodeEntry> lruQueue;
   private long alternatingCount = 0;
   private int numElements = 0;

   protected LRUQueue()
   {
      maxAgeQueue = new LinkedHashMap<Fqn, NodeEntry>();
      lruQueue = new LinkedHashMap<Fqn, NodeEntry>(16, 0.75f, true);
   }

   protected void reorderByLRU(Fqn fqn)
   {
      // leave the max age queue alone - it is like a fifo.

      // the lru queue is access ordered. meaning the most recently read item is moved to the bottom of the queue.
      // simply calling get against it visits it and will cause LinkedHashMap to move it to the bottom of the queue.
      lruQueue.get(fqn);
   }

   public NodeEntry getFirstNodeEntry()
   {
      // because the underlying queue is in two differently sorted queues, we alternate between them when calling
      // a generic getFirstNodeEntry.
      // we must alternate to keep things balanced when evicting nodes based on the maxNodes attribute. We don't
      // want to just prune from one queue but rather we want to be able to prune from both.
      NodeEntry ne;
      if (alternatingCount % 2 == 0)
      {
         ne = this.getFirstLRUNodeEntry();
         if (ne == null)
         {
            ne = this.getFirstMaxAgeNodeEntry();
         }
      }
      else
      {
         ne = this.getFirstMaxAgeNodeEntry();
         if (ne == null)
         {
            ne = this.getFirstLRUNodeEntry();
         }
      }
      alternatingCount++;
      return ne;
   }

   public NodeEntry getFirstLRUNodeEntry()
   {
      if (lruQueue.size() > 0)
      {
         return lruQueue.values().iterator().next();
      }

      return null;
   }

   public NodeEntry getFirstMaxAgeNodeEntry()
   {
      if (maxAgeQueue.size() > 0)
      {
         return maxAgeQueue.values().iterator().next();
      }

      return null;
   }

   public NodeEntry getNodeEntry(Fqn fqn)
   {
      return lruQueue.get(fqn);
   }

   public NodeEntry getNodeEntry(String fqn)
   {
      return this.getNodeEntry(Fqn.fromString(fqn));
   }

   public boolean containsNodeEntry(NodeEntry entry)
   {
      return this.maxAgeQueue.containsKey(entry.getFqn());
   }

   protected void removeNodeEntryFromLRU(NodeEntry entry)
   {
      Fqn fqn = entry.getFqn();
      lruQueue.remove(fqn);
   }

   protected void removeNodeEntryFromMaxAge(NodeEntry entry)
   {
      Fqn fqn = entry.getFqn();
      maxAgeQueue.remove(fqn);
   }

   public void removeNodeEntry(NodeEntry entry)
   {
      if (!this.containsNodeEntry(entry))
      {
         return;
      }
      Fqn fqn = entry.getFqn();
      NodeEntry ne1 = lruQueue.remove(fqn);
      NodeEntry ne2 = maxAgeQueue.remove(fqn);

      if (ne1 == null || ne2 == null)
      {
         throw new RuntimeException("The queues are out of sync.");
      }

      this.numElements -= ne1.getNumberOfElements();

   }

   public void addNodeEntry(NodeEntry entry)
   {
      if (!this.containsNodeEntry(entry))
      {
         Fqn fqn = entry.getFqn();
         entry.queue = this;
         maxAgeQueue.put(fqn, entry);
         lruQueue.put(fqn, entry);
         this.numElements += entry.getNumberOfElements();
      }
   }

   public int getNumberOfNodes()
   {
      if (log.isTraceEnabled()) log.trace("LRUQUeue.size() = " + maxAgeQueue.size());
      return maxAgeQueue.size();
   }

   public int getNumberOfElements()
   {
      return this.numElements;
   }

   public void clear()
   {
      maxAgeQueue.clear();
      lruQueue.clear();
      this.numElements = 0;
   }

   public void modifyElementCount(int difference)
   {
      this.numElements += difference;
   }

   public Iterator<NodeEntry> iterator()
   {
      return lruQueue.values().iterator();
   }

   protected final Iterator<NodeEntry> iterateMaxAgeQueue()
   {
      return maxAgeQueue.values().iterator();
   }

   protected final Iterator<NodeEntry> iterateLRUQueue()
   {
      return lruQueue.values().iterator();
   }

}

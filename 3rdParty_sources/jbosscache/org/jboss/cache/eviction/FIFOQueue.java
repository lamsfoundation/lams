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

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * FIFO Eviction Queue implementation for FIFO Policy.
 *
 * @author Daniel Huang (dhuang@jboss.org)
 * @version $Revision$
 */
public class FIFOQueue implements EvictionQueue
{
   private Map<Fqn, NodeEntry> nodeMap;
   private int numElements = 0;

   protected FIFOQueue()
   {
      nodeMap = new LinkedHashMap<Fqn, NodeEntry>();
      // We use a LinkedHashMap here because we want to maintain FIFO ordering and still get the benefits of
      // O(n) = 1 for add/remove/search.
   }

   public NodeEntry getFirstNodeEntry()
   {
/*      Iterator it = nodeMap.keySet().iterator();
      if(it.hasNext()) {
         return (NodeEntry) nodeMap.get(it.next());
      }

      return null; */

      // this code path is *slightly* faster when profiling. 20ms faster iterating over 200000 entries in queue.
      if (nodeMap.size() > 0)
      {
         return nodeMap.values().iterator().next();
      }

      return null;
   }

   public NodeEntry getNodeEntry(Fqn fqn)
   {
      return nodeMap.get(fqn);
   }

   public NodeEntry getNodeEntry(String fqn)
   {
      return this.getNodeEntry(Fqn.fromString(fqn));
   }

   public boolean containsNodeEntry(NodeEntry entry)
   {
      Fqn fqn = entry.getFqn();
      return this.getNodeEntry(fqn) != null;
   }

   public void removeNodeEntry(NodeEntry entry)
   {
      NodeEntry e = nodeMap.remove(entry.getFqn());
      this.numElements -= e.getNumberOfElements();
   }

   public void addNodeEntry(NodeEntry entry)
   {
      if (!this.containsNodeEntry(entry))
      {
         entry.queue = this;
         nodeMap.put(entry.getFqn(), entry);
         this.numElements += entry.getNumberOfElements();
      }
   }

   public int getNumberOfNodes()
   {
      return nodeMap.size();
   }

   public int getNumberOfElements()
   {
      return this.numElements;
   }

   public void modifyElementCount(int difference)
   {
      this.numElements += difference;
   }

   public void clear()
   {
      nodeMap.clear();
      this.numElements = 0;
   }

   public Iterator<NodeEntry> iterator()
   {
      return nodeMap.values().iterator();
   }
}

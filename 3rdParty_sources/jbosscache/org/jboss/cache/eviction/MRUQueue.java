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

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.NoSuchElementException;

/**
 * MRU Eviction Queue implementation.
 * <p/>
 * This nodeMap is sorted by MRU. The first entry in the nodeMap
 * will also be the most recently used entry. The sort is implicit
 * based on a Stack that we can implicitly sort to the top by moving
 * a node that is used to the top of the eviction stack.
 *
 * @author Daniel Huang (dhuang@jboss.org)
 * @version $Revision$
 */
public class MRUQueue implements EvictionQueue
{
   // we use our own Stack/Linked List implementation here because it guarantees O(n) = 1 for add, remove, get and
   // we can sort it in order of MRU implicitly while still getting O(n) = 1 access time
   // throughout.
   Map<Fqn, EvictionListEntry> nodeMap;
   EvictionQueueList list;
   private int numElements = 0;

   protected MRUQueue()
   {
      nodeMap = new HashMap<Fqn, EvictionListEntry>();
      list = new EvictionQueueList();
   }

   /**
    * This call moves a NodeEntry to the top of the stack.
    * <p/>
    * When a node is visited this method should be called to guarantee MRU ordering.
    *
    * @param fqn Fqn of the nodeEntry to move to the top of the stack.
    */
   protected void moveToTopOfStack(Fqn fqn)
   {
      EvictionListEntry le = nodeMap.remove(fqn);
      if (le != null)
      {
         list.remove(le);
         list.addToTop(le);
         nodeMap.put(le.node.getFqn(), le);
      }
   }

   /**
    * Will return the first entry in the nodeMap.
    * <p/>
    * The first entry in this nodeMap will also be the most recently used entry.
    *
    * @return The first node entry in nodeMap.
    */
   public NodeEntry getFirstNodeEntry()
   {
      try
      {
         return list.getFirst().node;
      }
      catch (NoSuchElementException e)
      {
         return null;
      }
   }

   public NodeEntry getNodeEntry(Fqn fqn)
   {
      EvictionListEntry le = nodeMap.get(fqn);
      if (le != null)
         return le.node;

      return null;
   }

   public NodeEntry getNodeEntry(String fqn)
   {
      return this.getNodeEntry(Fqn.fromString(fqn));
   }

   public boolean containsNodeEntry(NodeEntry entry)
   {
      return nodeMap.containsKey(entry.getFqn());
   }

   public void removeNodeEntry(NodeEntry entry)
   {
      EvictionListEntry le = nodeMap.remove(entry.getFqn());
      if (le != null)
      {
         list.remove(le);
         this.numElements -= le.node.getNumberOfElements();
      }
   }

   public void addNodeEntry(NodeEntry entry)
   {
      if (!this.containsNodeEntry(entry))
      {
         entry.queue = this;
         EvictionListEntry le = new EvictionListEntry(entry);
         list.addToBottom(le);
         nodeMap.put(entry.getFqn(), le);
         this.numElements += entry.getNumberOfElements();
      }
   }

   public int getNumberOfNodes()
   {
      return list.size();
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
      list.clear();
      this.numElements = 0;
   }

   public Iterator<NodeEntry> iterator()
   {
      return list.iterator();
   }

   @Override
   public String toString()
   {
      return list.toString();
   }
}

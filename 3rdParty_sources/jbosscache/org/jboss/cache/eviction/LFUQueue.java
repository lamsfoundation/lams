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

import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;

/**
 * LFUQueue EvictionQueue implementation for LFU Policy.
 * <p/>
 * The queue is sorted in least frequently used order.
 *
 * @author Daniel Huang (dhuang@jboss.org)
 * @version $Revision$
 */
public class LFUQueue implements SortedEvictionQueue
{
   private Map<Fqn, NodeEntry> nodeMap;
   private LinkedList<NodeEntry> evictionList;
   private Comparator<NodeEntry> comparator;

   private Set<NodeEntry> removalQueue;
   private int numElements = 0;

   protected LFUQueue()
   {
      nodeMap = new HashMap<Fqn, NodeEntry>();
      comparator = new LFUComparator();
      evictionList = new LinkedList<NodeEntry>();
      removalQueue = new HashSet<NodeEntry>();
   }

   /**
    * Return the first node to evict.
    * <p/>
    * This method will return the least frequently used entry in the queue.
    */
   public NodeEntry getFirstNodeEntry()
   {
      try
      {
         NodeEntry ne;
         while ((ne = evictionList.getFirst()) != null)
         {
            if (removalQueue.contains(ne))
            {
               evictionList.removeFirst();
               removalQueue.remove(ne);
            }
            else
            {
               break;
            }
         }
         return ne;
      }
      catch (NoSuchElementException e)
      {
         return null;
      }
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
      NodeEntry ne = nodeMap.remove(entry.getFqn());
      if (ne != null)
      {
         // don't remove directly from the LinkedList otherwise we will incur a O(n) = n
         // performance penalty for every removal! In the prune method for LFU, we will iterate the LinkedList through ONCE
         // doing a single O(n) = n operation and removal. This is much preferred over running O(n) = n every single time
         // remove is called. There is also special logic in the getFirstNodeEntry that will know to check
         // the removalQueue before returning.
         this.removalQueue.add(ne);
/*         if(!evictionList.remove(ne)) {
            throw new RuntimeException("");
         } */
         this.numElements -= ne.getNumberOfElements();
      }
   }

   public void addNodeEntry(NodeEntry entry)
   {
      if (!this.containsNodeEntry(entry))
      {
         Fqn fqn = entry.getFqn();
         entry.queue = this;
         nodeMap.put(fqn, entry);
         evictionList.add(entry);
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

   public void clear()
   {
      nodeMap.clear();
      evictionList.clear();
      removalQueue.clear();
      this.numElements = 0;
   }

   public void resortEvictionQueue()
   {
      Collections.sort(evictionList, comparator);
   }

   public void modifyElementCount(int difference)
   {
      this.numElements += difference;
   }

   protected void prune()
   {
      Iterator<NodeEntry> it = this.iterator();
      while (it.hasNext() && removalQueue.size() > 0)
      {
         if (removalQueue.remove(it.next()))
         {
            it.remove();
         }
      }
   }

   protected final List<NodeEntry> getEvictionList()
   {
      return this.evictionList;
   }

   protected final Set<NodeEntry> getRemovalQueue()
   {
      return this.removalQueue;
   }

   public Iterator<NodeEntry> iterator()
   {
      return evictionList.iterator();
   }

   /**
    * Comparator class for LFU.
    * <p/>
    * This class will sort the eviction queue in the correct eviction order.
    * The top of the list should evict before the bottom of the list.
    * <p/>
    * The sort is based on ascending order of nodeVisits.
    * <p/>
    * Note: this class has a natural ordering that is inconsistent with equals as defined by the java.lang.Comparator
    * contract.
    */
   protected static class LFUComparator implements Comparator<NodeEntry>
   {

      public int compare(NodeEntry ne1, NodeEntry ne2)
      {
         if (ne1.equals(ne2))
         {
            return 0;
         }

         int neNodeHits = ne1.getNumberOfNodeVisits();
         int ne2NodeHits = ne2.getNumberOfNodeVisits();

         if (neNodeHits > ne2NodeHits)
         {
            return 1;
         }
         else if (neNodeHits < ne2NodeHits)
         {
            return -1;
         }
         else if (neNodeHits == ne2NodeHits)
         {
            return 0;
         }

         throw new RuntimeException("Should never reach this condition");
      }
   }

}

